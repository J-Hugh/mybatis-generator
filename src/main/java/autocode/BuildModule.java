/*
 * 文 件 名:  BuildFodule.java
 * 版    权:  Hiaward Information Technology Co., Ltd. Copyright(2012),All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  yaolei
 * 创建时间: 2014-4-18
 * 修 改 人:  
 * 修改时间: 
 * 修改内容:  <修改内容>
 */
package autocode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.model.AutoCodeInfo;
import autocode.model.AutoCodeUiInfo;
import autocode.util.FileUtil;
import autocode.util.StringUtil;
import autocode.util.TagUtil;
import autocode.util.VmFileUtil;
import autocode.util.auxiliary.vm.VmVarDetailEntityInfo;
import autocode.util.auxiliary.vm.VmVarInfo;
import autocode.util.auxiliary.vm.relationtable.VmVarDaoInfo;
import autocode.util.auxiliary.vm.relationtable.VmVarServiceInfo;
import autocode.util.auxiliary.vm.relationtable.VmVarSqlModelInfo;
import dataset.model.IEntity;
import dataset.model.IPEntity;
import dataset.model.IRelation;
import dataset.model.IVEntity;
import dataset.model.impl.AssociationField;
import dataset.model.impl.Field;
import dataset.model.impl.PEntity;
import dataset.model.impl.PEntityConditon;
import dataset.model.impl.PField;
import dataset.model.impl.Relation;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author yaolei
 * @version [版本号, 2014-4-18]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BuildModule {
	public static final String DETAIL_TABLE_TYPE = "detail";
	
//	public static final String RELATION_TABLE_TYPE = "relation";

	static Logger log = LoggerFactory.getLogger(BuildModule.class);

	/**
	 * 单表自动生成所有的代码，如果对应生成文件的路径(包括文件名)是空,那么就默认为不生成
	 * 
	 * @param dealPEntity
	 */
	public static void buildSingleTable(IPEntity dealPEntity) {
		AutoCodeInfo autoCodeInfo = convertAutoCodeInfo(dealPEntity
				.getAutoCodeUiInfo());
		VmVarInfo vmVarInfo = GenVmVarInfos.getVmVarInfoFromEntity(dealPEntity);

		// 生成Mapper.xml
		BuildMapper.buildMapperFile(dealPEntity, autoCodeInfo, null);

		// 生成DAO.java
		GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
				GenVmVarInfos.FILE_TYPE_DAO);
		GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
				vmVarInfo.getOutFilePath());

		// 生成PO.java
		GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
				GenVmVarInfos.FILE_TYPE_PO);
		GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
				vmVarInfo.getOutFilePath());
		// 生成Service.java
		if (StringUtil.isNotBlank(autoCodeInfo.getServiceVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getServicePath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_SERVICE);
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
		}
		// 生成RestController.java
		if (StringUtil.isNotBlank(autoCodeInfo.getRestVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getRestPath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_REST);
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
		}
		// 生成Controller.java
		if (StringUtil.isNotBlank(autoCodeInfo.getControllerVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getControllerPath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_CONTROLLER);
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
		}
		// 生成Domain
		if (StringUtil.isNotBlank(autoCodeInfo.getServiceTestVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getServiceTestPath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_SERVICE_TEST);
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
			// GenCode.registSpringBean(vmVarInfo);
		}
		// 生成mvc.xml
		// GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
		// GenVmVarInfos.FILE_TYPE_BIZ_MVC);
		// GenCode.registBizMvc(vmVarInfo, autoCodeInfo.getBizmvcConfigPath(),
		// autoCodeInfo.getBizMvcVmFilePath());
		// 生成国际化配置
		GenCode.registI18n(autoCodeInfo, dealPEntity);
		// 生成View
		GenCode.genView(dealPEntity, autoCodeInfo);
		dealPEntity.setAutoCodeUiInfo(null);
	}

	/**
	 * 生成主从表代码
	 * 
	 * @param masterPEntity
	 * @param detailPEntitys
	 * @param vmMap
	 */
	public static void buildMasterDetailTable(IPEntity masterPEntity,
			List detailPEntitys, Map vmMap) {
		TagUtil.setVmMap(vmMap);
		if (masterPEntity == null || detailPEntitys == null
				|| detailPEntitys.isEmpty()) {
			log.info("主表信息为空或者从表信息为空");
			return;
		}

		dealMasterDetailRelation(masterPEntity, detailPEntitys);

		// 生成主表的Hbm以及Model
		AutoCodeInfo autoCodeInfo = convertAutoCodeInfo(masterPEntity
				.getAutoCodeUiInfo());
		VmVarInfo vmVarInfo = GenVmVarInfos
				.getVmVarInfoFromEntity(masterPEntity);
		// 生成hbm
		BuildMapper.buildMapperFile(masterPEntity, autoCodeInfo, null);
		// 生成Po.java
		GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
				GenVmVarInfos.FILE_TYPE_PO);
		GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
				vmVarInfo.getOutFilePath());

		// 生成国际化配置
		GenCode.registI18n(autoCodeInfo, masterPEntity);

		// 生成从表的Hbm以及Model
		Iterator iterator = detailPEntitys.iterator();
		while (iterator.hasNext()) {
			IPEntity detaiPEntity = (IPEntity) iterator.next();
			AutoCodeInfo detailAutoCodeInfo = convertAutoCodeInfo(detaiPEntity
					.getAutoCodeUiInfo());
			VmVarInfo detailVmVarInfo = GenVmVarInfos
					.getVmVarInfoFromEntity(detaiPEntity);
			BuildMapper.buildMapperFile(detaiPEntity, detailAutoCodeInfo, DETAIL_TABLE_TYPE);
			GenVmVarInfos.setCustomProperty(detailVmVarInfo,
					detailAutoCodeInfo, GenVmVarInfos.FILE_TYPE_PO);
			GenCode.genJavaCode(detailVmVarInfo,
					detailVmVarInfo.getVmFilePath(),
					detailVmVarInfo.getOutFilePath());
			GenCode.registI18n(detailAutoCodeInfo, detaiPEntity);

			// 生成Dao.java
			if (StringUtil.isNotBlank(detailAutoCodeInfo.getDaoVmFilePath())
					&& StringUtil.isNotBlank(detailAutoCodeInfo.getDaoPath())) {
				GenVmVarInfos.setCustomProperty(detailVmVarInfo,
						detailAutoCodeInfo, GenVmVarInfos.FILE_TYPE_DAO);
				detailVmVarInfo.setMasterEntity(false);

				// 遍历主键字段,是否为外键,为模板信息中设置外键值
				List<PField> pkFields = (List<PField>) detaiPEntity
						.getPKFields();
				for (PField field : pkFields) {
					if (field.isFK()) {
						String fkName = field.getName();
						detailVmVarInfo.setPoFkFirstLowerName(StringUtil
								.lowerFirst(fkName));
						detailVmVarInfo.setPoFkFirstUpperName(StringUtil
								.upperFirst(fkName));
					}
				}

				// 遍历非主键字段,是否为外键,为模板信息中设置外键值
				List<PField> fields = (List<PField>) detaiPEntity.getFields();
				for (PField field : fields) {
					if (field.isFK()) {
						String fkName = field.getName();
						detailVmVarInfo.setPoFkFirstLowerName(StringUtil
								.lowerFirst(fkName));
						detailVmVarInfo.setPoFkFirstUpperName(StringUtil
								.upperFirst(fkName));
					}
				}

				GenCode.genJavaCode(detailVmVarInfo,
						detailVmVarInfo.getVmFilePath(),
						detailVmVarInfo.getOutFilePath());

				// 外键字段值清空
				detailVmVarInfo.setPoFkFirstLowerName("");
				detailVmVarInfo.setPoFkFirstUpperName("");
			}
		}
		List detailEntityInfos = VmVarDetailEntityInfo
				.buildVmVarDetailEntityInfo(detailPEntitys, masterPEntity);
		// 生成Dao
		if (StringUtil.isNotBlank(autoCodeInfo.getDaoVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getDaoPath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_DAO, detailEntityInfos);
			vmVarInfo.setMasterEntity(true);
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
		}

		// 生成Service.java
		if (StringUtil.isNotBlank(autoCodeInfo.getServiceVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getServicePath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_SERVICE, detailEntityInfos);
			
			String masterFieldName = "";
			String detailFieldName = "";
			List<Relation> relations = (List<Relation>) masterPEntity
					.getSrcRelations();
			for (int i = 0; i < relations.size(); i++) {
				if(i == 0){
					Relation relation = relations.get(i);
					List<AssociationField> fields = (List<AssociationField>)relation.getRelationFields();
					
					if(fields.size() > 0 ){
						AssociationField aField = (AssociationField)fields.get(0);
						Field srcField = (Field)aField.getSrcField();
						Field tgtField = (Field)aField.getTgtField();
						masterFieldName = srcField.getName();
						detailFieldName = tgtField.getName();
					}
				}
			}

			// 取第一个从表实体dao
			vmVarInfo.setDetailDaoFirstLowerName(StringUtil
					.lowerFirst(((PEntity) detailPEntitys.get(0)).getName())
					+ "Dao");
			vmVarInfo.setDetailDaoFirstUpperName(StringUtil
					.upperFirst(((PEntity) detailPEntitys.get(0)).getName())
					+ "Dao");
			vmVarInfo.setDetailPOFirstLowerName(StringUtil
					.lowerFirst(((PEntity) detailPEntitys.get(0)).getName()));
			vmVarInfo.setDetailPOFirstUpperName(StringUtil
					.upperFirst(((PEntity) detailPEntitys.get(0)).getName()));
			List<PField> detailPFileds = (List<PField>) ((PEntity) detailPEntitys
					.get(0)).getPKFields();
			if (detailPFileds.size() > 0) {
				PField pField = detailPFileds.get(0);
				vmVarInfo.setDetailPoPkFirstLowerName(StringUtil
						.lowerFirst(pField.getName()));
				vmVarInfo.setDetailPoPkFirstUpperName(StringUtil
						.upperFirst(pField.getName()));
			}
			vmVarInfo.setRelationMasterFieldFirstLowerName(StringUtil.lowerFirst(masterFieldName));
			vmVarInfo.setRelationMasterFieldFirstUpperName(StringUtil.upperFirst(masterFieldName));
			vmVarInfo.setRelationDetailFieldFirstLowerName(StringUtil.lowerFirst(detailFieldName));
			vmVarInfo.setRelationDetailFieldFirstUpperName(StringUtil.upperFirst(detailFieldName));
			
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
		}
		// 生成Rest.java
		if (StringUtil.isNotBlank(autoCodeInfo.getRestVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getRestPath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_REST, detailEntityInfos);
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
		}
		// 生成ServiceImpl.java
		if (StringUtil.isNotBlank(autoCodeInfo.getServiceImplVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getServiceImplPath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_SERVICEIMPL, detailEntityInfos);
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
			GenCode.registSpringBean(vmVarInfo);
		}
		// 生成ServiceTest.java
		if (StringUtil.isNotBlank(autoCodeInfo.getServiceTestVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getServiceTestPath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_SERVICE_TEST);
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
			GenCode.registSpringBean(vmVarInfo);
		}
		// 生成Controller.java
		if (StringUtil.isNotBlank(autoCodeInfo.getControllerVmFilePath())
				&& StringUtil.isNotBlank(autoCodeInfo.getControllerPath())) {
			GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
					GenVmVarInfos.FILE_TYPE_CONTROLLER, detailEntityInfos);
			
			vmVarInfo.setDetailPOFirstLowerName(StringUtil
					.lowerFirst(((PEntity) detailPEntitys.get(0)).getName()));
			vmVarInfo.setDetailPOFirstUpperName(StringUtil
					.upperFirst(((PEntity) detailPEntitys.get(0)).getName()));
			
			GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(),
					vmVarInfo.getOutFilePath());
		}
		// 生成mvc.xml
		GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo,
				GenVmVarInfos.FILE_TYPE_BIZ_MVC, detailEntityInfos);
		// GenCode.registBizMvc(vmVarInfo, autoCodeInfo.getBizmvcConfigPath(),
		// autoCodeInfo
		// .getBizMvcVmFilePath());

		// 生成View
		GenCode.genView(masterPEntity, autoCodeInfo, detailPEntitys);
		masterPEntity.setAutoCodeUiInfo(null);
	}

	/**
	 * 构建主表与从表需要生成的关联关系：在主表中添加指定从表的1对N关联关系
	 * 
	 * @param masterPEntity
	 * @param detailPEntitys
	 */
	private static void dealMasterDetailRelation(IPEntity masterPEntity,
			List detailPEntitys) {
		if (masterPEntity == null || detailPEntitys == null
				|| detailPEntitys.size() == 0) {
			return;
		}

		List detailIds = new ArrayList();
		Iterator iterator = detailPEntitys.iterator();

		while (iterator.hasNext()) {
			IPEntity entity = (IPEntity) iterator.next();
			detailIds.add(entity.getId());
		}

		PEntityConditon entityConditon = new PEntityConditon();
		entityConditon.addSrcRelationId("");
		iterator = masterPEntity.getSrcRelations().iterator();
		while (iterator.hasNext()) {
			IRelation relation = (IRelation) iterator.next();
			if (detailIds.contains(relation.getTgtEntityId())) {
				entityConditon.addSrcRelationId(relation.getId());
			}

		}
		masterPEntity.setPEntityConditon(entityConditon);
	}
	
	 /**
     * 自动生成关联表crud代码，如果对应生成vm文件的路径(包括文件名)是空,那么就默认为不生成
     * 
     * @param dealPEntitys
     * @param typeAndVmPathMap
     *            (页面组件类型：对应vm模板路径)
     */
    public static void genRelationTable(List dealPEntitys, Map typeAndVmPathMap) {
        TagUtil.setVmMap(typeAndVmPathMap);
        if (dealPEntitys == null || dealPEntitys.isEmpty()) {
            log.info("查询实体集合为空");
            return;
        }
        for (Iterator iterator = dealPEntitys.iterator(); iterator.hasNext();) {
        	IEntity dealEntity = (IEntity) iterator.next();
            genRelationTable(dealEntity);
        }
    }
	
	/**
     * 关联表自动生成所有的代码，如果对应生成文件的路径(包括文件名)是空,那么就默认为不生成
     * 
     * @param dealPEntity
     */
    public static void genRelationTable(IEntity dealEntity) {
    	IVEntity dealVEntity = (IVEntity)dealEntity;
        AutoCodeInfo autoCodeInfo = convertAutoCodeInfo(dealEntity.getAutoCodeUiInfo());
        // 生成Domain
        VmVarSqlModelInfo vmsql = new VmVarSqlModelInfo(dealVEntity, autoCodeInfo);
        String[] dirPathAndFileNameArr = null;
        if (StringUtil.isNotBlank(autoCodeInfo.getPoVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getPoPath())) {
            dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo.getPoVmFilePath());
            String sqlmodel = VmFileUtil
                    .getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmsql
                            .getVelocityContext(), null);
            FileUtil.toFile(autoCodeInfo.getPoPath(), sqlmodel, null);

        }
        // 生成Service.java
        VmVarServiceInfo vmservice = new VmVarServiceInfo(dealVEntity, autoCodeInfo);

        if (StringUtil.isNotBlank(autoCodeInfo.getServiceVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServicePath())) {
            dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo
                    .getServiceVmFilePath());
            String service = VmFileUtil
                    .getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmservice
                            .getVelocityContext(), null);
            FileUtil.toFile(autoCodeInfo.getServicePath(), service, null);
        }
        //生成Mapper文件
        if(StringUtil.isNotBlank(autoCodeInfo.getHbmPath())){
        	BuildMapper.buildMapperFile(dealEntity, autoCodeInfo, null);
        }
        
        VmVarDaoInfo vmDao = new VmVarDaoInfo(dealVEntity, autoCodeInfo);
        // 生成Dao.java
	    if (StringUtil.isNotBlank(autoCodeInfo.getDaoVmFilePath()) && StringUtil.isNotBlank(autoCodeInfo.getDaoPath())) {
	          dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo.getDaoVmFilePath());
	          String dao = VmFileUtil.getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmDao
	                          .getVelocityContext(), null);
	          FileUtil.toFile(autoCodeInfo.getDaoPath(), dao, null);
	      }
        // 生成Test.java
//        if (StringUtil.isNotBlank(autoCodeInfo.getServiceTestVmFilePath())
//                && StringUtil.isNotBlank(autoCodeInfo.getServiceTestPath())) {
//            dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo
//                    .getServiceTestVmFilePath());
//            String servicetest = VmFileUtil
//                    .getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmservice
//                            .getVelocityContext(), null);
//            FileUtil.toFile(autoCodeInfo.getServiceTestPath(), servicetest, null);
//            vmservice.initRegistServiceTest();
////            GenCode.registSpringBean(vmservice);
//        }
        // 生成Controller.java
        if (StringUtil.isNotBlank(autoCodeInfo.getControllerVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getControllerPath())) {
            GenCode.genController(dealVEntity, autoCodeInfo);
        }
        // 生成RestController.java
        if (StringUtil.isNotBlank(autoCodeInfo.getRestVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getRestPath())) {
            GenCode.genRestController(dealVEntity, autoCodeInfo);
        }
        // 生成国际化
        GenCode.registI18n(autoCodeInfo, dealVEntity);
        GenCode.genView(dealEntity, autoCodeInfo);
    }

	/**
	 * <p>
	 * 把autoCodeUiInfo信息转化成autoCodeInfo
	 * </p>
	 * 
	 * @param autoCodeUiInfo
	 *            录入界面录入的信息
	 * @return AutoCodeInfo(生成代码时需要的信息)
	 */
	public static AutoCodeInfo convertAutoCodeInfo(AutoCodeUiInfo autoCodeUiInfo) {
		AutoCodeInfo autoCodeInfo = new AutoCodeInfo();
		// Doamin文件的信息
		autoCodeInfo.setPoVmFilePath(autoCodeUiInfo.getPoVmFilePath());
		autoCodeInfo.setPoName(StringUtil.getFileName(autoCodeUiInfo
				.getPoFileFullName()));
		autoCodeInfo.setPoPackageName(autoCodeUiInfo.getPoPackageName());
		autoCodeInfo.setPoPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getPoFileRelativePath() + "/"
				+ autoCodeUiInfo.getPoFileFullName());
		// Dao文件的信息
		autoCodeInfo.setDaoVmFilePath(autoCodeUiInfo.getDaoVmFilePath());
		autoCodeInfo.setDaoName(StringUtil.getFileName(autoCodeUiInfo
				.getDaoFileFullName()));
		autoCodeInfo.setDaoPackageName(autoCodeUiInfo.getDaoPackageName());
		autoCodeInfo.setDaoPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getDaoFileRelativePath() + "/"
				+ autoCodeUiInfo.getDaoFileFullName());
		// 处理生成model 模版为空时，不生成对应的hbm文件
		if (autoCodeUiInfo.getDaoVmFilePath() != null) {
			autoCodeInfo.setHbmPath(autoCodeInfo.getDaoPath().replaceAll(
					autoCodeUiInfo.getDaoFileFullName(),
					autoCodeUiInfo.getHbmFileFullName()));
		}
		// Rest文件的信息
		autoCodeInfo.setRestVmFilePath(autoCodeUiInfo.getRestVmFilePath());
		autoCodeInfo.setRestName(StringUtil.getFileName(autoCodeUiInfo
				.getRestFileFullName()));
		autoCodeInfo.setRestPackageName(autoCodeUiInfo.getRestPackageName());
		autoCodeInfo.setRestPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getRestFileRelativePath() + "/"
				+ autoCodeUiInfo.getRestFileFullName());
		// service文件的信息
		autoCodeInfo
				.setServiceVmFilePath(autoCodeUiInfo.getServiceVmFilePath());
		autoCodeInfo.setServiceName(StringUtil.getFileName(autoCodeUiInfo
				.getServiceFileFullName()));
		autoCodeInfo.setServicePackageName(autoCodeUiInfo
				.getServicePackageName());
		autoCodeInfo.setServicePath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getServiceFileRelativePath() + "/"
				+ autoCodeUiInfo.getServiceFileFullName());
		// serviceImpl文件的信息
		autoCodeInfo.setServiceImplVmFilePath(autoCodeUiInfo
				.getServiceImplVmFilePath());
		autoCodeInfo.setServiceImplName(StringUtil.getFileName(autoCodeUiInfo
				.getServiceImplFileFullName()));
		autoCodeInfo.setServiceImplPackageName(autoCodeUiInfo
				.getServiceImplPackageName());
		autoCodeInfo.setServiceImplPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getServiceImplFileRelativePath() + "/"
				+ autoCodeUiInfo.getServiceImplFileFullName());
		// servicetTest文件的信息
		autoCodeInfo.setServiceTestVmFilePath(autoCodeUiInfo
				.getServiceTestVmFilePath());
		autoCodeInfo.setServiceTestName(StringUtil.getFileName(autoCodeUiInfo
				.getServiceTestFileFullName()));
		autoCodeInfo.setServiceTestPackageName(autoCodeUiInfo
				.getServiceTestPackageName());
		autoCodeInfo.setServiceTestPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getServiceTestFileRelativePath() + "/"
				+ autoCodeUiInfo.getServiceTestFileFullName());
		autoCodeInfo.setSrcSpringConfigPath(autoCodeUiInfo
				.getSrcSpringConfigPath());
		autoCodeInfo.setTestSpringConfigPath(autoCodeUiInfo
				.getTestSpringConfigPath());
		// autoCodeInfo.setBizmvcConfigPath(autoCodeUiInfo.getBizmvcConfigPath());
		// controller
		autoCodeInfo.setControllerVmFilePath(autoCodeUiInfo
				.getControllerVmFilePath());
		autoCodeInfo.setControllerName(StringUtil.getFileName(autoCodeUiInfo
				.getControllerFileFullName()));
		autoCodeInfo.setControllerPackageName(autoCodeUiInfo
				.getControllerPackageName());
		autoCodeInfo.setControllerPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getControllerFileRelativePath() + "/"
				+ autoCodeUiInfo.getControllerFileFullName());
		autoCodeInfo.setBizMvcVmFilePath(autoCodeUiInfo.getBizMvcVmFilePath());
		autoCodeInfo.setBizmvcConfigPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getBizMvcFileRelativePath() + "/"
				+ autoCodeUiInfo.getBizMvcFileFullName());

		// viewList
		autoCodeInfo.setViewListFilePath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getViewListPageRelativePath() + "/"
				+ autoCodeUiInfo.getViewListPageFileFullName());
		autoCodeInfo.setViewListVmFilePath(autoCodeUiInfo
				.getViewListVmFilePath());
		autoCodeInfo.setViewListJsPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getViewListJsRelativePath() + "/"
				+ autoCodeUiInfo.getViewListJsFileFullName());
		autoCodeInfo.setViewListJsVmFilePath(autoCodeUiInfo
				.getViewListJsVmFilePath());
		// viewNew
		autoCodeInfo.setViewNewFilePath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getViewNewPageRelativePath() + "/"
				+ autoCodeUiInfo.getViewNewPageFileFullName());
		autoCodeInfo
				.setViewNewVmFilePath(autoCodeUiInfo.getViewNewVmFilePath());
		autoCodeInfo.setViewNewJsPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getViewNewJsRelativePath() + "/"
				+ autoCodeUiInfo.getViewNewJsFileFullName());
		autoCodeInfo.setViewNewJsVmFilePath(autoCodeUiInfo
				.getViewNewJsVmFilePath());
		// viewEdit
		autoCodeInfo.setViewUpdateFilePath(autoCodeUiInfo.getProjectPath()
				+ "/" + autoCodeUiInfo.getViewUpdatePageRelativePath() + "/"
				+ autoCodeUiInfo.getViewUpdatePageFileFullName());
		autoCodeInfo.setViewUpdateVmFilePath(autoCodeUiInfo
				.getViewUpdateVmFilePath());
		autoCodeInfo.setViewUpdateJsPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getViewUpdateJsRelativePath() + "/"
				+ autoCodeUiInfo.getViewUpdateJsFileFullName());
		autoCodeInfo.setViewUpdateJsVmFilePath(autoCodeUiInfo
				.getViewUpdateJsVmFilePath());
		// viewShow
		autoCodeInfo.setViewShowFilePath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getViewShowPageRelativePath() + "/"
				+ autoCodeUiInfo.getViewShowPageFileFullName());
		autoCodeInfo.setViewShowVmFilePath(autoCodeUiInfo
				.getViewShowVmFilePath());
		autoCodeInfo.setViewShowJsPath(autoCodeUiInfo.getProjectPath() + "/"
				+ autoCodeUiInfo.getViewShowJsRelativePath() + "/"
				+ autoCodeUiInfo.getViewShowJsFileFullName());
		autoCodeInfo.setViewShowJsVmFilePath(autoCodeUiInfo
				.getViewShowJsVmFilePath());
		// I18N
		autoCodeInfo.setUseI18n(autoCodeUiInfo.isUseI18n());
		autoCodeInfo.setI18nConfigRelativePath(autoCodeUiInfo.getProjectPath()
				+ "/" + autoCodeUiInfo.getI18nFileRelativePath());
		autoCodeInfo.setI18nFileNames(autoCodeUiInfo.getI18nFileNames());
		autoCodeInfo.setI18nVmFilePath(autoCodeUiInfo.getI18nVmFilePath());

		// 设置编码
		autoCodeInfo.setEncode(autoCodeUiInfo.getEncode());
		autoCodeInfo.setBasePackageName(autoCodeUiInfo.getBasePackageName());
		autoCodeInfo.setModuleName(autoCodeUiInfo.getModuleName());
		return autoCodeInfo;
	}
}
