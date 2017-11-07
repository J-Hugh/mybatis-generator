/*
 * $Id: GenVmVarInfos.java,v 1.4 2013/04/19 09:51:24 zhanghp Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.model.AutoCodeInfo;
import autocode.util.SqliteUtil;
import autocode.util.StringUtil;
import autocode.util.auxiliary.vm.VmVarInfo;
import autocode.util.auxiliary.vm.VmVarPoPkInfo;
import autocode.util.auxiliary.vm.VmVarViewBaseInfo;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IVEntity;
import dataset.model.impl.PEntity;
import dataset.model.impl.Relation;
import dataset.model.impl.VField;
import dataset.viewmodel.IQueryField;

/**
 * 根据DataSet文件或hbm文件生成VmVarInfo对象集（供生成代码使用）
 * 
 * @author Rone BizFoudation Framework Team: ganjp
 * @version 1.0
 * @since 4.3
 */
public class GenVmVarInfos {
	static Logger log = LoggerFactory.getLogger(GenVmVarInfos.class);

	public static final String FILE_TYPE_DOMAIN = "po";

	public static final String FILE_TYPE_PO = "po";

	public static final String FILE_TYPE_DAO = "dao";

	public static final String FILE_TYPE_SERVICE = "service";
	
	public static final String FILE_TYPE_REST = "rest";

	public static final String FILE_TYPE_SERVICEIMPL = "serviceImpl";

	public static final String FILE_TYPE_SERVICE_TEST = "serviceTest";

	public static final String FILE_TYPE_CONTROLLER = "controller";

	public static final String FILE_TYPE_BIZ_MVC = "bizMvc";

	/**
	 * 从实体获取对应的信息到VmVarInfo对象(PO信息)，并返回VmVarInfo对象集
	 * 
	 * @param pEntitys
	 * @param packageName
	 * @param outDir
	 * 
	 * @return List(VmVarInfo);
	 */
	public static List getVmVarInfosFromEntitys(Collection pEntitys) {
		if (pEntitys == null || pEntitys.isEmpty()) {
			log.error("持久化实体集合不能为空");
			return null;
		}
		List vmVarInfoList = new ArrayList();
		for (Iterator iterator = pEntitys.iterator(); iterator.hasNext();) {
			IPEntity pEntity = (IPEntity) iterator.next();
			VmVarInfo vmVarInfo = getVmVarInfoFromEntity(pEntity);
			vmVarInfoList.add(vmVarInfo);
		}
		return vmVarInfoList;
	}

	/**
	 * 从实体获取对应的信息到VmVarInfo对象(PO信息)，并返回VmVarInfo对象
	 * 
	 * @param pEntity
	 * @return
	 */
	public static VmVarInfo getVmVarInfoFromEntity(IPEntity pEntity) {
		VmVarInfo vmVarInfo = new VmVarInfo();
		vmVarInfo.setEntityName(pEntity.getName());
		vmVarInfo.setModuleName(pEntity.getModuleName() == null ? "" : pEntity
				.getModuleName().toLowerCase());
		vmVarInfo.setPackageName(pEntity.getPackageName() == null ? ""
				: pEntity.getPackageName().toLowerCase());
		vmVarInfo.setPoFirstUpperName(StringUtil.upperFirst(pEntity.getName()));
		vmVarInfo.setPoFirstLowerName(StringUtil.lowerFirst(pEntity.getName()));
		vmVarInfo.setMobileModuleSql(SqliteUtil.getCreateTable(pEntity));
		vmVarInfo.setPoCompositePk(pEntity.getPKFields() != null
				&& pEntity.getPKFields().size() > 1);
		// 定义向导是否需要生成List、Add、Edit、Show页面对应相关的Java代码与配置文件
		vmVarInfo
				.setGenListView(pEntity.getResultForm().getFields().size() > 0);
		vmVarInfo.setGenAddView(pEntity.getNewForm().getFields().size() > 0);
		vmVarInfo.setGenEditView(pEntity.getNewForm().getFields().size() > 0);
		vmVarInfo
				.setGenShowView(pEntity.getDetailInfoForm().getFields().size() > 0);
		vmVarInfo.setTableName(pEntity.getTable());
		
		//主表
		List<Relation> relations = (List<Relation>)pEntity.getSrcRelations();
		for(Relation relation : relations){
			PEntity tgtEntity = (PEntity)relation.getTgtEntity();
			vmVarInfo.addPoDetailsFirstUpperForMaster(tgtEntity.getName());
			vmVarInfo.addPoDetailsFirstLowerForMaster(tgtEntity.getName());
		}

		// 设置实体中额外的查询字段信息 by bruce 20121210
		if (null != pEntity.getQueryForm()
				&& null != pEntity.getQueryForm().getFields()) {
			Iterator it = pEntity.getQueryForm().getFields().iterator();
			List names = vmVarInfo.getPoQueryExtFieldNames();
			List upperNames = vmVarInfo.getPoQueryExtFieldFirstUpperNames();
			List types = vmVarInfo.getPoQueryExtFieldTypes();
			while (it.hasNext()) {
				IQueryField field = (IQueryField) it.next();
				if (field.isMultiValue()) {
					names.add(field.getFieldNameInMultiValueCase());
					upperNames.add(StringUtil.upperFirst(field
							.getFieldNameInMultiValueCase()));
					types.add(convertRobseSimpleType(field
							.getFieldTypeInMultiValueCase())); // 多值绑定数组
				}

			}
		}
		// 定义是否需要生成导出Excel的Java代码
		vmVarInfo.setExportExcel(pEntity.getQueryForm().isExportExcel());

		setVmVarInfo(vmVarInfo, pEntity);
		return vmVarInfo;
	}

	public static VmVarInfo getVmVarInfoFromEntity(IVEntity vEntity) {
		VmVarInfo vmVarInfo = new VmVarInfo();
		List classImportTypeList = new ArrayList();
		boolean isCompositePk = false;

		vmVarInfo.setEntityName(vEntity.getName());
		vmVarInfo.setPackageName(vEntity.getPackageName() == null ? ""
				: vEntity.getPackageName().toLowerCase());
		vmVarInfo.setPoFirstUpperName(StringUtil.upperFirst(vEntity.getName()));
		vmVarInfo.setPoFirstLowerName(StringUtil.lowerFirst(vEntity.getName()));
		vmVarInfo.setPoCompositePk(isCompositePk);

		Collection fileds = vEntity.getFields();
		List columnNames = new ArrayList();
		List columnInTables = new ArrayList();

		for (Iterator iterator2 = fileds.iterator(); iterator2.hasNext();) {
			VField vField = (VField) iterator2.next();

			vmVarInfo.addPoFieldName(vField.getFieldName());
			vmVarInfo.addPoFieldFirstUpperName(StringUtil.upperFirst(vField
					.getFieldName()));
			vmVarInfo.addPoFieldType(vField.getFieldType());
			setImportTypeList(classImportTypeList, vField.getFieldType());
			vmVarInfo
					.addPoFieldHibernateSimpleType(convertHibernateSimpleType(vField
							.getFieldType()));
			vmVarInfo.addPoFieldRobaseSimpleType(convertRobseSimpleType(vField
					.getFieldType()));
			vmVarInfo.addPoFieldIsPersist(false);
			// chenhua由查询实体生成 2012-7-4
			int index = vField.getColumnName().indexOf(".");
			String columnName = null, tableName = null;
			if (index < 0) {
				IPEntity pentity = (IPEntity) vField.getRefEntity();
				if (pentity != null) {
					columnName = vField.getColumnName();
					tableName = pentity.getTable();
				} else {
					columnName = vField.getColumnName();
					tableName = "";
				}
			} else {
				columnName = vField.getColumnName().substring(index + 1);
				tableName = vField.getColumnName().substring(0, index);
			}
			columnNames.add(columnName);
			columnInTables.add(tableName);

		}

		// 增加两个扩充属性。
		vmVarInfo.addExtendProperty("PO_FIELD_COLUMN_NAMES", columnNames);
		vmVarInfo.addExtendProperty("PO_FIELD_TABLE_NAMES", columnInTables);

		if (!classImportTypeList.isEmpty())
			vmVarInfo.setClassImportTypes(classImportTypeList);
		// 定义向导是否需要生成List、Add、Edit、Show页面对应相关的Java代码与配置文件
		vmVarInfo
				.setGenListView(vEntity.getResultForm().getFields().size() > 0);
		vmVarInfo.setGenAddView(vEntity.getNewForm().getFields().size() > 0);
		vmVarInfo.setGenEditView(vEntity.getNewForm().getFields().size() > 0);
		vmVarInfo
				.setGenShowView(vEntity.getDetailInfoForm().getFields().size() > 0);
		// 定义是否需要生成导出Excel的Java代码
		vmVarInfo.setExportExcel(vEntity.getQueryForm().isExportExcel());

		return vmVarInfo;
	}

	/**
	 * 设置po字段的相关信息
	 * 
	 * @param vmVarInfo
	 * @param pEntity
	 */
	public static void setVmVarInfo(VmVarInfo vmVarInfo, IPEntity pEntity) {
		Collection fileds = pEntity.getFields();
		List classImportTypeList = new ArrayList();
		boolean isCompositePk = false;
		VmVarPoPkInfo vmVarPoPkInfo = null;
		if (pEntity.getPKFields() == null || pEntity.getPKFields().isEmpty()) {
			throw new RuntimeException(pEntity.getName() + "主键不能为空");
		} else if (pEntity.getPKFields().size() > 1) {
			isCompositePk = true;
			String pkName = GenHbm.getCompositPkName(pEntity.getName());
			vmVarInfo.setPoPkFirstUpperName(pkName);
			vmVarInfo.setPoPkSimpleType(pkName);
			vmVarInfo.setPoPkFirstLowerName(StringUtil.lowerFirst(pkName));
			vmVarInfo.addPoFieldName(StringUtil.lowerFirst(pkName));
			vmVarInfo.addPoFieldFirstUpperName(pkName);
			vmVarInfo.addPoFieldHibernateSimpleType(pkName);
			vmVarInfo.addPoFieldRobaseSimpleType(pkName);
			vmVarInfo.addPoFieldIsPersist(false);
			vmVarPoPkInfo = new VmVarPoPkInfo(
					vmVarInfo.getPoPkFirstUpperName(),
					vmVarInfo.getPoFirstLowerName());
			vmVarInfo.setVmVarPoPkInfo(vmVarPoPkInfo);
		}
		vmVarInfo.setPoCompositePk(isCompositePk);
		for (Iterator iterator2 = fileds.iterator(); iterator2.hasNext();) {
			IPField pField = (IPField) iterator2.next();
			if (pField.isPK()) {
				if (isCompositePk) {
					vmVarPoPkInfo.addPoPkFiedName(pField.getFieldName());
					vmVarPoPkInfo.addPoPkFieldFirstUpperName(StringUtil
							.upperFirst(pField.getFieldName()));
					vmVarPoPkInfo.addPoPkFieldType(pField.getFieldType());
					vmVarPoPkInfo
							.addPoPkFieldHibernateSimpleType(convertHibernateSimpleType(pField
									.getFieldType()));
					vmVarPoPkInfo
							.addPoPkFieldRobaseSimpleType(convertRobseSimpleType(pField
									.getFieldType()));
					vmVarInfo.addPoFieldNotBlank(false);
					continue;
				} else {
					// 非联合主键实体情况下(单主键)的主键信息
					vmVarInfo.setPkNameForOnePk(pField.getName());
					vmVarInfo.setPoPkFirstUpperName(StringUtil
							.upperFirst(pField.getFieldName()));
					vmVarInfo.setPoPkFirstLowerName(StringUtil
							.lowerFirst(pField.getFieldName()));
					vmVarInfo.setPoPkType(pField.getFieldType());
					vmVarInfo.setPoPkSimpleType(convertRobseSimpleType(pField
							.getFieldType()));
					vmVarInfo.addPoFieldNotBlank(false);
				}
			}
			vmVarInfo.addPoFieldName(pField.getFieldName());
			vmVarInfo.addPoFieldFirstUpperName(StringUtil.upperFirst(pField
					.getFieldName()));
			vmVarInfo.addPoFieldType(pField.getFieldType());
			setImportTypeList(classImportTypeList, pField.getFieldType());
			vmVarInfo
					.addPoFieldHibernateSimpleType(convertHibernateSimpleType(pField
							.getFieldType()));
			vmVarInfo.addPoFieldRobaseSimpleType(convertRobseSimpleType(pField
					.getFieldType()));
			vmVarInfo.addPoFieldNotBlank(pField.isNullAble());
			if (pField.isPersistent()) {
				vmVarInfo.addPoFieldIsPersist(true);
			} else {
				vmVarInfo.addPoFieldIsPersist(false);
			}
		}
		if (!classImportTypeList.isEmpty())
			vmVarInfo.setClassImportTypes(classImportTypeList);
	}

	/**
	 * 把fieldType类型设置到importFieldTypeList里面
	 * 
	 * @param importTypeList
	 * @param fieldType
	 */
	public static void setImportTypeList(List importTypeList, String fieldType) {
		if (StringUtil.isBlank(fieldType) || fieldType.indexOf(".") == -1
				|| fieldType.indexOf("java.lang") != -1) {
			return;
		}
		if (importTypeList == null) {
			importTypeList = new ArrayList();
		}
		if (importTypeList.contains(fieldType)) {
			return;
		}
		importTypeList.add(fieldType);
	}

	/**
	 * 把完整类(java.lang.String)转化简单类(String)
	 * 
	 * @param fieldType
	 * @return
	 */
	public static String convertRobseSimpleType(String fieldType) {
		if (StringUtils.isNotBlank(fieldType) && fieldType.indexOf(".") != -1) {
			int index = fieldType.lastIndexOf(".");
			fieldType = fieldType.substring(index + 1);
		}
		return fieldType;
	}

	/**
	 * 把完整的路径，转成只有简单型的类名
	 * 
	 * <pre>
	 * java.lang.Byte-->byte
	 * java.lang.Short-->short
	 * java.lang.Integer->int
	 * java.lang.Long->long
	 * java.lang.Double->double
	 * java.sql.Blob->InputStream
	 * java.sql.Clob->Reader
	 * binary-->InputStream
	 * </pre>
	 * 
	 * @param fieldType
	 * 
	 * @return
	 */
	public static String convertHibernateSimpleType(String fieldType) {
		if (StringUtils.isNotBlank(fieldType) && fieldType.indexOf(".") != -1) {
			int index = fieldType.lastIndexOf(".");
			fieldType = fieldType.substring(index + 1);
		}
		if ("Byte".equals(fieldType))
			fieldType = "byte";
		if ("Short".equals(fieldType))
			fieldType = "short";
		if ("Integer".equals(fieldType))
			fieldType = "int";
		if ("Long".equals(fieldType))
			fieldType = "int";
		if ("Double".equals(fieldType))
			fieldType = "double";
		return fieldType;
	}

	/**
	 * 转成robase简单类型
	 * 
	 * java.lang.Byte-->Byte java.lang.Short-->Short java.lang.Integer->Integer
	 * java.lang.Long->Long java.lang.Double->Double java.sql.Blob->InputStream
	 * java.sql.Clob->Reader binary-->InputStream
	 * 
	 * @param fieldType
	 * @return
	 */
	public static String convertRobaseSimpleType(String fieldType) {
		if (StringUtils.isNotBlank(fieldType)) {
			if (fieldType.indexOf(".") == -1) {
				if ("binary".equalsIgnoreCase(fieldType)) {
					fieldType = "InputStream";
				}
			} else {
				if ("java.sql.Blob".equalsIgnoreCase(fieldType)) {
					fieldType = "InputStream";
				} else if ("java.sql.Blob".equalsIgnoreCase(fieldType)) {
					fieldType = "Reader";
				} else {
					int index = fieldType.lastIndexOf(".");
					fieldType = fieldType.substring(index + 1);
				}
			}

		}
		return fieldType;
	}

	/**
	 * 设置根据vm文件生成代码输出的文件名
	 * 
	 * @param vmVarInfos
	 * @param outFileType
	 */
	public static void setCustomProperty(List vmVarInfos, String moduleName,
			String basePackageName, String outFileType) {
		if (StringUtil.isBlank(outFileType)) {
			throw new RuntimeException("输出的文件类型不能为空outFileType");
		}
		for (Iterator iterator = vmVarInfos.iterator(); iterator.hasNext();) {
			VmVarInfo vmVarInfo = (VmVarInfo) iterator.next();
			setCustomProperty(vmVarInfo, moduleName, basePackageName,
					outFileType);
		}
	}

	/**
	 * 设置根据vm文件生成代码输出的文件名
	 * 
	 * @param vmVarInfos
	 * @param outFileType
	 */
	public static void setCustomProperty(List vmVarInfos, Map autoCodeInfoMap,
			String outFileType) {
		if (StringUtil.isBlank(outFileType)) {
			throw new RuntimeException("输出的文件类型不能为空outFileType");
		}
		for (Iterator iterator = vmVarInfos.iterator(); iterator.hasNext();) {
			VmVarInfo vmVarInfo = (VmVarInfo) iterator.next();
			AutoCodeInfo autoCodeInfo = (AutoCodeInfo) autoCodeInfoMap
					.get(vmVarInfo.getEntityName());
			setCustomProperty(vmVarInfo, autoCodeInfo, outFileType);
		}
	}

	/**
	 * 设置根据vm文件生成代码输出的文件名
	 * 
	 * @param vmVarInfo
	 * @param outFileType
	 */
	public static void setCustomProperty(VmVarInfo vmVarInfo,
			String moduleName, String basePackageName, String outFileType) {
		if (StringUtil.isBlank(moduleName)
				|| StringUtil.isBlank(basePackageName)) {
			throw new RuntimeException("模块名和包名不能为空");
		}
		vmVarInfo.setModuleName(moduleName);
		if (FILE_TYPE_PO.equalsIgnoreCase(outFileType)) {
			vmVarInfo.setPackageName(basePackageName + "." + moduleName
					+ ".model");// 设置包名
			if (vmVarInfo.getVmVarPoPkInfo() != null)
				vmVarInfo.getVmVarPoPkInfo().setPackageName(
						vmVarInfo.getPackageName());
			vmVarInfo.setClassFirstUpperName(vmVarInfo.getPoFirstUpperName());
		} else if (FILE_TYPE_SERVICE.equalsIgnoreCase(outFileType)) {
			vmVarInfo.setPackageName(basePackageName + "." + moduleName
					+ ".service");// 设置包名
			setClassNameAndImportTypes(vmVarInfo, "Service");// 设置类名和引入的po类
		} else if (FILE_TYPE_SERVICEIMPL.equalsIgnoreCase(outFileType)) {
			vmVarInfo.setPackageName(basePackageName + "." + moduleName
					+ ".service.impl");// 设置包名
			setClassNameAndImportTypes(vmVarInfo, "ServiceImpl");// 设置类名和引入的po类
			vmVarInfo.setClassImplementInterfaces(vmVarInfo
					.getPoFirstUpperName() + "Service");// 引入需要实现的所有接口
			vmVarInfo.setRegistBeanName(vmVarInfo.getPoFirstLowerName()
					+ "Service");// 设置bean的名称
			// 引入service类
			String servicePackageName = vmVarInfo.getPackageName().replaceAll(
					"service.impl", "service");
			vmVarInfo.getClassImportTypes().add(servicePackageName + ".*");
		} else if (FILE_TYPE_SERVICE_TEST.equalsIgnoreCase(outFileType)) {
			vmVarInfo.setPackageName(basePackageName + "." + moduleName
					+ ".service");// 设置包名
			setClassNameAndImportTypes(vmVarInfo, "ServiceTest");// 设置类名和引入的po类
			vmVarInfo.setImportClassFirstUpperName(vmVarInfo
					.getPoFirstUpperName() + "Service");
			vmVarInfo.setImportClassFirstLowerName(vmVarInfo
					.getPoFirstLowerName() + "Service");
		} else if (FILE_TYPE_CONTROLLER.equalsIgnoreCase(outFileType)) {
			// 设置包名
			vmVarInfo.setPackageName(basePackageName + "." + moduleName
					+ ".controller");
			setClassNameAndImportTypes(vmVarInfo, "Controller");
			vmVarInfo.setRegistBeanName(vmVarInfo.getPoFirstLowerName()
					+ "Controller");
			vmVarInfo
					.addClassImportType(basePackageName + "." + moduleName
							+ ".service." + vmVarInfo.getPoFirstUpperName()
							+ "Service");
		}
		vmVarInfo.setFileNameAndType(vmVarInfo.getClassFirstUpperName()
				+ ".java");// 设置输出文件的名称
	}

	/**
	 * 设置根据vm文件生成代码输出的文件名
	 * 
	 * @param vmVarInfo
	 * @param outFileType
	 */
	public static void setCustomProperty(VmVarInfo vmVarInfo,
			AutoCodeInfo autoCodeInfo, String outFileType) {
		if (GenCode.isGen(autoCodeInfo, outFileType)) {
			List importPoTypes = new ArrayList();
			if (FILE_TYPE_PO.equalsIgnoreCase(outFileType)) {
				vmVarInfo.setPackageName(autoCodeInfo.getPoPackageName());// 设置包名
				vmVarInfo.setClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getPoName()));// 设置类名(第一个字母大写)
				vmVarInfo.setClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getPoName()));// 设置类名(第一个字母小写)
				if (vmVarInfo.isPoCompositePk())
					vmVarInfo.getVmVarPoPkInfo().setPackageName(
							vmVarInfo.getPackageName());
				vmVarInfo.setOutFilePath(autoCodeInfo.getPoPath());// 设置输出路径(包括文件名)
				vmVarInfo.setVmFilePath(autoCodeInfo.getPoVmFilePath());
				
				Set<String> importDate = getFieldImportClass(vmVarInfo.getPoFieldTypes());
				if(importDate != null){
					importPoTypes.addAll(importDate);
				}
				String importList = getImportListClass(vmVarInfo.getPoDetailsFirstUpperForMaster());
				if(importList != null){
					importPoTypes.add(importList);
				}
				
				vmVarInfo.setClassImportTypes(importPoTypes);
			} else if (FILE_TYPE_DAO.equalsIgnoreCase(outFileType)) {
				importPoTypes.add(autoCodeInfo.getPoPackageName() + ".*");
				vmVarInfo.setPackageName(autoCodeInfo.getDaoPackageName());// 设置包名
				vmVarInfo.setClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getDaoName()));
				vmVarInfo.setClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getDaoName()));
				// 引入所有的po类.
				vmVarInfo.setClassImportTypes(importPoTypes);
				vmVarInfo.setOutFilePath(autoCodeInfo.getDaoPath());
				vmVarInfo.setVmFilePath(autoCodeInfo.getDaoVmFilePath());
				vmVarInfo.setEntityName(autoCodeInfo.getPoName());
				vmVarInfo.setPoFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getPoName()));
			} else if (FILE_TYPE_REST.equalsIgnoreCase(outFileType)) {
				importPoTypes.add(autoCodeInfo.getPoPackageName() + ".*");
				vmVarInfo.setPackageName(autoCodeInfo.getRestPackageName());// 设置包名
				vmVarInfo.setClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getRestName()));
				vmVarInfo.setClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getRestName()));
				StringBuffer restImportBuf = new StringBuffer();
				restImportBuf.append(autoCodeInfo.getServicePackageName()).append(".").append(autoCodeInfo.getServiceName());
				importPoTypes.add(restImportBuf.toString());
				vmVarInfo.setClassImportTypes(importPoTypes);
				vmVarInfo.setOutFilePath(autoCodeInfo.getRestPath());
				vmVarInfo.setVmFilePath(autoCodeInfo.getRestVmFilePath());
				vmVarInfo.setPoFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getPoName()));
				vmVarInfo.setServiceFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getServiceName()));
				vmVarInfo.setServiceFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getServiceName()));
			} else if (FILE_TYPE_SERVICE.equalsIgnoreCase(outFileType)) {
				importPoTypes.add(autoCodeInfo.getPoPackageName() + ".*");
				vmVarInfo.setPackageName(autoCodeInfo.getServicePackageName());// 设置包名
				vmVarInfo.setClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getServiceName()));
				vmVarInfo.setClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getServiceName()));
				StringBuffer daoImportBuf = new StringBuffer();
//				daoImportBuf.append(autoCodeInfo.getDaoPackageName()).append(".").append(autoCodeInfo.getDaoName());
				daoImportBuf.append(autoCodeInfo.getDaoPackageName()).append(".").append("*");
				importPoTypes.add(daoImportBuf.toString());
				vmVarInfo.setClassImportTypes(importPoTypes);
				vmVarInfo.setOutFilePath(autoCodeInfo.getServicePath());
				vmVarInfo.setVmFilePath(autoCodeInfo.getServiceVmFilePath());
				vmVarInfo.setPoFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getPoName()));
				vmVarInfo.setDaoFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getDaoName()));
				vmVarInfo.setDaoFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getDaoName()));
			} else if (FILE_TYPE_SERVICEIMPL.equalsIgnoreCase(outFileType)) {
				importPoTypes.add(autoCodeInfo.getPoPackageName() + ".*");
				vmVarInfo.setPackageName(autoCodeInfo
						.getServiceImplPackageName());// 设置包名
				vmVarInfo.setClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getServiceImplName()));
				vmVarInfo.setClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getServiceImplName()));
				vmVarInfo.setClassImportTypes(importPoTypes);
				vmVarInfo.setClassImplementInterfaces(autoCodeInfo
						.getServiceName());// 引入需要实现的所有接口
				vmVarInfo.getClassImportTypes().add(
						autoCodeInfo.getServicePackageName() + ".*");// 引入service类
				vmVarInfo.setOutFilePath(autoCodeInfo.getServiceImplPath());
				vmVarInfo
						.setVmFilePath(autoCodeInfo.getServiceImplVmFilePath());
				vmVarInfo.setRegistClass(autoCodeInfo
						.getServiceImplPackageName()
						+ "."
						+ autoCodeInfo.getServiceImplName());
				vmVarInfo.setRegistBeanName(StringUtil.lowerFirst(autoCodeInfo
						.getServiceName()));
				vmVarInfo.setRegistFile(autoCodeInfo.getSrcSpringConfigPath());
			} else if (FILE_TYPE_SERVICE_TEST.equalsIgnoreCase(outFileType)) {
				importPoTypes.add(autoCodeInfo.getPoPackageName() + ".*");
				vmVarInfo.setPackageName(autoCodeInfo
						.getServiceTestPackageName());// 设置包名
				vmVarInfo.setClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getServiceTestName()));
				vmVarInfo.setClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getServiceTestName()));
				vmVarInfo.setClassImportTypes(importPoTypes);
				vmVarInfo.setImportClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getServiceName()));
				vmVarInfo.setImportClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getServiceName()));
				vmVarInfo.setOutFilePath(autoCodeInfo.getServiceTestPath());
				vmVarInfo
						.setVmFilePath(autoCodeInfo.getServiceTestVmFilePath());
				vmVarInfo.setRegistClass(autoCodeInfo
						.getServiceImplPackageName()
						+ "."
						+ autoCodeInfo.getServiceImplName());
				vmVarInfo.setRegistBeanName(StringUtil.lowerFirst(autoCodeInfo
						.getServiceName()));
				vmVarInfo.setRegistFile(autoCodeInfo.getTestSpringConfigPath());
			} else if (FILE_TYPE_CONTROLLER.equalsIgnoreCase(outFileType)) {
				importPoTypes.add(autoCodeInfo.getPoPackageName() + ".*");
				vmVarInfo.setPackageName(autoCodeInfo
						.getControllerPackageName());
				vmVarInfo.setClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getControllerName()));
				vmVarInfo.setClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getControllerName()));
				vmVarInfo.setClassImportTypes(importPoTypes);
				vmVarInfo.setImportClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getServiceName()));
				vmVarInfo.setImportClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getServiceName()));
				vmVarInfo.getClassImportTypes().add(
						autoCodeInfo.getServicePackageName() + ".*");
				vmVarInfo.setOutFilePath(autoCodeInfo.getControllerPath());
				vmVarInfo.setVmFilePath(autoCodeInfo.getControllerVmFilePath());
				vmVarInfo.setEntityName(autoCodeInfo.getPoName());
				vmVarInfo.setPoFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getPoName()));
				vmVarInfo.setModuleName(autoCodeInfo.getModuleName());
			} else if (FILE_TYPE_BIZ_MVC.equalsIgnoreCase(outFileType)) {
				importPoTypes.add(autoCodeInfo.getPoPackageName() + ".*");
				vmVarInfo.setPackageName(autoCodeInfo
						.getControllerPackageName());
				vmVarInfo.setClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getControllerName()));
				vmVarInfo.setClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getControllerName()));
				vmVarInfo.setClassImportTypes(importPoTypes);
				vmVarInfo.setImportClassFirstUpperName(StringUtil
						.upperFirst(autoCodeInfo.getServiceName()));
				vmVarInfo.setImportClassFirstLowerName(StringUtil
						.lowerFirst(autoCodeInfo.getServiceName()));
				vmVarInfo.getClassImportTypes().add(
						autoCodeInfo.getControllerPackageName() + ".*");
				vmVarInfo.setOutFilePath(autoCodeInfo.getBizmvcConfigPath());
				vmVarInfo.setVmFilePath(autoCodeInfo.getBizMvcVmFilePath());
				// 设置MVC配置文件中使用的视图URI
				vmVarInfo.setViewListURI(VmVarViewBaseInfo.getViewPath(
						autoCodeInfo.getViewListFilePath(), null));
				vmVarInfo.setViewNewURI(VmVarViewBaseInfo.getViewPath(
						autoCodeInfo.getViewNewFilePath(), null));
				vmVarInfo.setViewUpdateURI(VmVarViewBaseInfo.getViewPath(
						autoCodeInfo.getViewUpdateFilePath(), null));
				vmVarInfo.setViewShowURI(VmVarViewBaseInfo.getViewPath(
						autoCodeInfo.getViewShowFilePath(), null));
			}
		}
	}

	/**
	 * 是否需要引入 Java.util.Date 类
	 * 
	 * @param poFieldTypes
	 * @return
	 */
	private static Set<String> getFieldImportClass(List poFieldTypes) {
		Set<String> importList = new HashSet<String>();
		if (poFieldTypes == null || poFieldTypes.size() < 1) {
			return null;
		}
		for (int i = 0; i < poFieldTypes.size(); i++) {
			if(poFieldTypes.get(i).toString().indexOf("Date") != -1){
				importList.add("java.util.Date");
				importList.add("com.fasterxml.jackson.annotation.JsonFormat");
			} else if(poFieldTypes.get(i).toString().indexOf("Blob") != -1){
				importList.add("java.sql.Blob");
			} else if(poFieldTypes.get(i).toString().indexOf("Clob") != -1){
				importList.add("java.sql.Clob");
			} else if(poFieldTypes.get(i).toString().indexOf("BigDecimal") != -1){
				importList.add("java.math.BigDecimal");
			}
		}
		return importList;
	}
	
	/**
	 * 在主从表关系中,主表的PO中是否需要引入List类
	 * @param detailsEntityList
	 * @return
	 */
	private static String getImportListClass(List detailsEntityList){
		if (detailsEntityList == null || detailsEntityList.size() < 1) {
			return null;
		}
		return "java.util.List";
	}
	
	/**
	 * 设置代码生成中的自定义扩展属性
	 * 
	 * @param vmVarInfo
	 * @param autoCodeInfo
	 * @param outFileType
	 * @param relEntityInfoList
	 *            关联实体信息对象列表
	 */
	public static void setCustomProperty(VmVarInfo vmVarInfo,
			AutoCodeInfo autoCodeInfo, String outFileType,
			List relEntityInfoList) {
		setCustomProperty(vmVarInfo, autoCodeInfo, outFileType);
		vmVarInfo.setRelEntityInfoList(relEntityInfoList);
	}

	/**
	 * 设置类名和引入的po类
	 * 
	 * @param vmVarInfo
	 * @param suffixClassName
	 */
	public static void setClassNameAndImportTypes(VmVarInfo vmVarInfo,
			String suffixClassName) {
		// 设置类名
		String className = vmVarInfo.getPoFirstUpperName() + suffixClassName;
		vmVarInfo.setClassFirstUpperName(StringUtil.upperFirst(className));
		vmVarInfo.setClassFirstLowerName(StringUtil.lowerFirst(className));

		// 设置引入的po类
		List importTypes = new ArrayList();
		String poPackageName = vmVarInfo.getPackageName()
				.replaceAll("service.impl", "model")
				.replaceAll("service", "model");
		importTypes.add(poPackageName + ".*");
		vmVarInfo.setClassImportTypes(importTypes);
	}

	/**
	 * java类型->hibernate类型 byte, -> byte short,java.lang.Short -> short
	 * int,java.lang.Integer -> integer long,java.lang.Long -> long
	 * float,java.lang.Float -> float double,java.lang.Double -> double
	 * java.math.BigDecimal -> big_decimal char,java.lang.Character -> character
	 * boolean,java.lang.Boolean -> boolean java.lang.String -> string
	 * boolean,java.lang.Boolean -> yes_no boolean,java.lang.Boolean ->
	 * true_false java.util.Date,java.sql.Date -> date
	 * java.util.Date,java.sql.Time -> time java.util.Date,java.sql.Timestamp->
	 * timestamp java.util.Calendar -> calendar java.util.Calendar ->
	 * calendar_date byte[] -> binary java.lang.String -> text
	 * java.io.Serializable -> serializable java.sql.Clob -> clob java.sql.Blob
	 * -> blob java.lang.Class -> class java.util.Locale -> locale
	 * java.util.TimeZone -> timezone java.util.Currency -> currency
	 * 
	 * @param fieldType
	 * @return
	 */
	public static String convertHibernateType(String fieldType) {
		// TODO
		return null;
	}

	/**
	 * 转成Robase类型
	 * 
	 * java.sql.Blob->java.io.InputStream java.sql.Clob->java.io.Reader
	 * binary-->java.io.InputStream
	 * 
	 * @param fieldType
	 * @return
	 */
	public static String convertRobaseType(String fieldType) {
		// TODO
		return null;
	}

	public static void main(String[] args) throws Exception {

	}
}