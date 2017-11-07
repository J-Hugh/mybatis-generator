package main.base.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.base.HandlerBase;
import main.util.Consts;
import main.util.GenUtil;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.BuildMapper;
import autocode.GenCode;
import autocode.model.AutoCodeInfo;
import autocode.model.OneJavaInfo;
import autocode.util.StringUtil;
import autocode.util.auxiliary.vm.VmVarInfo;
import autocode.util.auxiliary.vm.VmVarJavaBaseInfo;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.impl.PEntity;
import dataset.model.impl.PField;
import dataset.viewmodel.impl.QueryField;
import dataset.viewmodel.impl.QueryForm;

/**
 * 
 * @author wangzhichao
 * 
 * 主方法运行类
 *
 */
public class GenCodeHandler implements HandlerBase {
	
	
	 static Logger log = LoggerFactory.getLogger(GenCodeHandler.class);
	
	public GenCodeHandler(){}

	/**
	 * 运行所有方法
	 */
	@Override
	public boolean run() throws Exception {
		
		String pkName = "";  //主键
		GenUtil gu = new GenUtil();
		List<Map<String, String>> tableMapList = gu.getTableDetail();
		
		for(Map<String,String> tableMap : tableMapList){
			for(Map.Entry<String, String> entry : tableMap.entrySet()){
				String tableName = entry.getKey(); //表名称
				String packName = entry.getValue(); //包名称
				List<Map<String,Object>> tableDetail = gu.getResultList(tableName);
				
				for(Map<String,Object> dbM : tableDetail){
					String field = dbM.get("field") == null ? "" : dbM.get("field").toString();
					if(!dbM.get("key").equals("") && dbM.get("key") != null){
						if(dbM.get("key").toString().equalsIgnoreCase("pri")){
							pkName = field;
						}
					}
				}
				String tableName_old=tableName;
				if(tableName.contains("_")){
					String[] s = tableName.split("_");
					tableName = "";
					for(int a = 0 ; a < s.length ; a ++){
						if(a == 0){
							tableName += s[a];
						} else {
							tableName += StringUtil.upperFirst(s[a]);
						}
						
					}
				}
				this.runDao(tableName, packName, tableDetail,pkName);
				this.runDaoMapper(tableName_old,tableName, packName, tableDetail);
				this.runDomain(tableName, packName, tableDetail);
				this.runRest(tableName, packName, tableDetail,pkName);
				this.runService(tableName, packName, tableDetail,pkName);
			}
		}
		
		return false;
	}

	@Override
	public boolean runService(String tbName,String packName,List<Map<String, Object>> tableDetailList,String pk) throws Exception {
		
		OneJavaInfo javaService = new OneJavaInfo();
		VmVarJavaBaseInfo baseService = new VmVarJavaBaseInfo();
		List<String> serviceList = new ArrayList<String>();
		serviceList.add(packName + "." + Consts.VM_MODULESNAME_DAO_FIRST_LOWER + "." + StringUtil.upperFirst(tbName) + Consts.VM_MODULESNAME_DAO_FIRST_UPPER);
		serviceList.add(packName + "." + Consts.VM_MODULESNAME_DOMAIN_FIRST_LOWER + "." + StringUtil.upperFirst(tbName));
		String poPkUpperName = "",poPkLowerName = "";
		
		VelocityContext vcService = new VelocityContext();
		vcService.put(VmVarInfo.CLASS_FIRST_LOWER_NAME, StringUtil.lowerFirst(tbName));
		vcService.put(VmVarInfo.CLASS_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName));
		vcService.put(VmVarInfo.PACKAGE_NAME, packName + "." + Consts.VM_MODULESNAME_SERVICE_FIRST_LOWER);
		vcService.put(VmVarInfo.DAO_FIRST_LOWER_NAME, StringUtil.lowerFirst(tbName) + Consts.VM_MODULESNAME_DAO_FIRST_UPPER);
		vcService.put(VmVarInfo.DAO_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName) + Consts.VM_MODULESNAME_DAO_FIRST_UPPER);
		vcService.put(VmVarInfo.PO_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName));
		vcService.put(VmVarInfo.PO_FIRST_LOWER_NAME, StringUtil.lowerFirst(tbName));
		
		//vcService.put(VmVarInfo.PO_PK_FIRST_LOWER_NAME, StringUtil.lowerFirst(pk));
		String pk_new=pk.toLowerCase();
		if(pk_new.contains("_")){
			String[] s = pk_new.split("_");
			pk_new = "";
			for(int a = 0 ; a < s.length ; a ++){
				if(a == 0){
					pk_new += s[a];
				} else {
					pk_new += StringUtil.upperFirst(s[a]);
				}
				
			}
		}
		vcService.put(VmVarInfo.PO_PK_FIRST_LOWER_NAME, pk_new);
		
		
		vcService.put(VmVarInfo.PO_PK_FIRST_UPPER_NAME, StringUtil.upperFirst(pk));
		vcService.put(VmVarInfo.PK_NAME_FOR_ONEPK, poPkLowerName);
		vcService.put(VmVarInfo.CLASS_IMPORT_TYPES, serviceList);
		
		baseService.setVelocityContext(vcService);
		
		javaService.setEntityName(StringUtil.upperFirst(tbName));
		javaService.setFileName(StringUtil.upperFirst(tbName) + Consts.VM_MODULESNAME_SERVICE_FIRST_UPPER);
		javaService.setFilePath(GenUtil.getBasePath() + File.separator +"src" + File.separator +   GenUtil.packToPath(packName) + File.separator + Consts.VM_MODULESNAME_SERVICE_FIRST_LOWER);
		javaService.setPackageName(packName + File.separator + Consts.VM_MODULESNAME_SERVICE_FIRST_LOWER);
		
		javaService.setVmFileFullPath(Consts.VMFULLPATH  + "Service.vm"); 

		
		GenCode.genJavaCode(javaService,baseService);
		
		System.out.println(tbName + ".service生成完成");
		return true;
	}

	
	@Override
	public boolean runDaoMapper(String tbName_old,String tbName,String packName,List<Map<String,Object>> dbDetailList) throws Exception {
		
		StringBuilder daoPath = new StringBuilder();
		daoPath.append(GenUtil.getBasePath());
		daoPath.append(File.separator);
		daoPath.append("src");
		daoPath.append(File.separator);
		daoPath.append(GenUtil.packToPath(packName));
		daoPath.append(File.separator);
		daoPath.append(Consts.VM_MODULESNAME_DAO_FIRST_LOWER);
		
		IPField genField = null;
		QueryField queryField = null;
		
		IPEntity entity = new PEntity();
		List<QueryField> queryFieldList = new ArrayList<QueryField>();
		
		for(Map<String,Object> dbM : dbDetailList){
			queryField = new QueryField();
			genField = new PField();
			
			String field = dbM.get("field") == null ? "" : dbM.get("field").toString();
			String fieldTableColumn = field.toUpperCase();
			
			field=field.toLowerCase();
			if(field.contains("_")){
				String[] s = field.split("_");
				field = "";
				for(int a = 0 ; a < s.length ; a ++){
					if(a == 0){
						field += s[a];
					} else {
						field += StringUtil.upperFirst(s[a]);
					}
					
				}
			}
			
			//String field = dbM.get("field") == null ? "" : dbM.get("field").toString();
			String type = dbM.get("type") == null ? "" : dbM.get("type").toString();
			boolean isPk = dbM.get("key").equals("") || dbM.get("key") == null ? false : true;
			String desc = dbM.get("comment") == null || dbM.get("comment").equals("") ? "" : dbM.get("comment").toString();
			
			//普通字段
			genField.setFieldName(StringUtil.lowerFirst(field));
			genField.setPK(isPk);
			genField.setColumnName(fieldTableColumn);
			genField.setDisplayName(field);
			genField.setFieldType(GenUtil.getJavaType(type));
			genField.setDesc(desc);
			
			//查询字段
			queryField.setFieldName(StringUtil.lowerFirst(field));
			queryField.setDisplayName(field);
			queryField.setFieldType(GenUtil.getJavaType(type));
			
			queryFieldList.add(queryField);
			entity.addField(genField);
		}
		
		
		QueryForm queryForm = new QueryForm();
		queryForm.setFields(queryFieldList);
		
		entity.setName(tbName);
		//entity.setTable(tbName);
		entity.setTable(tbName_old);
		
		entity.setQueryForm(queryForm);
		
		AutoCodeInfo ai = new AutoCodeInfo();
		ai.setHbmPath(daoPath + "/" + StringUtil.upperFirst(tbName) + "Mapper.xml");
		ai.setDaoPackageName(packName + "." + Consts.VM_MODULESNAME_DAO_FIRST_LOWER);
		BuildMapper.buildMapperFile(entity,ai,"");
		
		System.out.println(tbName + ".daomapper生成完成");
		return true;
	}


	@Override
	public boolean runDao(String tbName,String packName,List<Map<String,Object>> dbDetailList,String pk) throws Exception {
		
		
		List<String> daoLst = new ArrayList<String>();
		daoLst.add(packName + "." + Consts.VM_MODULESNAME_DOMAIN_FIRST_LOWER + "." + StringUtil.upperFirst(tbName));
		
		VmVarJavaBaseInfo baseService = new VmVarJavaBaseInfo();
		VelocityContext vcService = new VelocityContext();
		StringBuilder daoPath = new StringBuilder();
		daoPath.append(GenUtil.getBasePath());
		daoPath.append(File.separator);
		daoPath.append("src");
		daoPath.append(File.separator);
		daoPath.append(GenUtil.packToPath(packName));
		daoPath.append(File.separator);
		daoPath.append(Consts.VM_MODULESNAME_DAO_FIRST_LOWER);
		
		OneJavaInfo oDao = new OneJavaInfo();
		oDao.setEntityName(StringUtil.upperFirst(tbName));
		oDao.setFileName(StringUtil.upperFirst(tbName) + Consts.VM_MODULESNAME_DAO_FIRST_UPPER);
		oDao.setFilePath(daoPath.toString());
		oDao.setPackageName(packName + File.separator + Consts.VM_MODULESNAME_DAO_FIRST_LOWER);
		oDao.setVmFileFullPath(Consts.VMFULLPATH + "Dao.vm");
		vcService.put(VmVarInfo.PACKAGE_NAME, packName + "." + Consts.VM_MODULESNAME_DAO_FIRST_LOWER);
		vcService.put(VmVarInfo.CLASS_IMPORT_TYPES, daoLst);
		vcService.put(VmVarInfo.PO_PK_FIRST_LOWER_NAME, StringUtil.lowerFirst(pk));
		vcService.put(VmVarInfo.PO_PK_FIRST_UPPER_NAME, StringUtil.upperFirst(pk));
		vcService.put(VmVarInfo.CLASS_FIRST_LOWER_NAME, StringUtil.lowerFirst(tbName));
		vcService.put(VmVarInfo.CLASS_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName));
		vcService.put(VmVarInfo.PO_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName));
		vcService.put(VmVarInfo.PO_FIRST_LOWER_NAME, StringUtil.lowerFirst(tbName));
		baseService.setVelocityContext(vcService);
		GenCode.genJavaCode(oDao,baseService);
		System.out.println(tbName + ".dao生成完成");
		return false;
	}



	@Override
	public boolean runRest(String tbName,String packName,List<Map<String,Object>> dbDetailList,String pk) throws Exception {
		List<String> restLst = new ArrayList<String>();
		
		restLst.add(packName + "." + Consts.VM_MODULESNAME_DOMAIN_FIRST_LOWER + "." + StringUtil.upperFirst(tbName));
		restLst.add(packName + "." + Consts.VM_MODULESNAME_SERVICE_FIRST_LOWER + "." + StringUtil.upperFirst(tbName) + Consts.VM_MODULESNAME_SERVICE_FIRST_UPPER);
		
		VmVarJavaBaseInfo baseService = new VmVarJavaBaseInfo();
		String sbName = packName.substring(Consts.REST_PRO_PATH.length(),packName.length());
		String fName = GenUtil.packToPath(sbName);
		
		VelocityContext vcService = new VelocityContext();
		String restPath = GenUtil.getBasePath() + File.separator + "src" + File.separator +  GenUtil.packToPath(packName) + File.separator + Consts.VM_MODULESNAME_REST_FIRST_LOWER;
		OneJavaInfo oRest = new OneJavaInfo();
		oRest.setEntityName(StringUtil.upperFirst(tbName));
		oRest.setFileName(StringUtil.upperFirst(tbName)+ Consts.VM_MODULESNAME_REST_FIRST_UPPER);
		oRest.setFilePath(restPath);
		oRest.setPackageName(packName + File.separator + Consts.VM_MODULESNAME_REST_FIRST_LOWER);
		oRest.setVmFileFullPath(Consts.VMFULLPATH +"newRest.vm");
		vcService.put(VmVarInfo.PACKAGE_NAME, packName + "." + Consts.VM_MODULESNAME_REST_FIRST_LOWER);
		vcService.put(VmVarInfo.SERVICE_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName) + Consts.VM_MODULESNAME_SERVICE_FIRST_UPPER);
		vcService.put(VmVarInfo.SERVICE_FIRST_LOWER_NAME, StringUtil.lowerFirst(tbName) + Consts.VM_MODULESNAME_SERVICE_FIRST_UPPER);
		vcService.put(VmVarInfo.PACKAGE_SUBNAME, fName);
		String pk_new=pk.toLowerCase();
		if(pk_new.contains("_")){
			String[] s = pk_new.split("_");
			pk_new = "";
			for(int a = 0 ; a < s.length ; a ++){
				if(a == 0){
					pk_new += s[a];
				} else {
					pk_new += StringUtil.upperFirst(s[a]);
				}
				
			}
		}
		vcService.put(VmVarInfo.PO_PK_FIRST_LOWER_NAME, pk_new);
		vcService.put(VmVarInfo.PO_PK_NAME, pk);
		vcService.put(VmVarInfo.PO_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName));
		vcService.put(VmVarInfo.CLASS_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName));
		vcService.put(VmVarInfo.CLASS_IMPORT_TYPES, restLst);
		baseService.setVelocityContext(vcService);
		GenCode.genJavaCode(oRest,baseService);
		
		System.out.println(tbName + ".rest生成完成");
		return true;
	}



	@Override
	public boolean runDomain(String tbName,String packName,List<Map<String,Object>> dbDetailList) throws Exception {
		List<String> dLst = new ArrayList<String>();    
		List<String> dtLst = new ArrayList<String>();   
		List<String> commentLst = new ArrayList<String>(); 
		
		
		VmVarJavaBaseInfo baseService = new VmVarJavaBaseInfo();
		String poPkUpperName = "",poPkLowerName = "";
		VelocityContext vcService = new VelocityContext();
		IPField f = null;
		for(Map<String,Object> dbM : dbDetailList){
			f = new PField();
			String field = dbM.get("field") == null ? "" : dbM.get("field").toString();
			field=field.toLowerCase();
			if(field.contains("_")){
				String[] s = field.split("_");
				field = "";
				for(int a = 0 ; a < s.length ; a ++){
					if(a == 0){
						field += s[a];
					} else {
						field += StringUtil.upperFirst(s[a]);
					}
					
				}
			}
			String type = dbM.get("type") == null ? "" : dbM.get("type").toString();
			String desc = dbM.get("comment") == null || dbM.get("comment").equals("") ? "" : dbM.get("comment").toString();
		    dLst.add(StringUtil.lowerFirst(field));
//			dLst.add(field);   //此处为决定 首字母是否大小写的地方
		    dtLst.add(GenUtil.getJavaType(type));
		    commentLst.add(desc);
		}
		
		String domainPath = GenUtil.getBasePath() + File.separator + "src" + File.separator +  GenUtil.packToPath(packName) + File.separator + Consts.VM_MODULESNAME_DOMAIN_FIRST_LOWER;
		OneJavaInfo oDomain = new OneJavaInfo();
		oDomain.setEntityName(StringUtil.upperFirst(tbName));
		oDomain.setFileName(StringUtil.upperFirst(tbName));
		oDomain.setFilePath(domainPath);
		oDomain.setPackageName(packName + File.separator + Consts.VM_MODULESNAME_DOMAIN_FIRST_LOWER);
		oDomain.setVmFileFullPath(Consts.VMFULLPATH + "Model.vm");
		vcService.put(VmVarInfo.PACKAGE_NAME, packName + "." + Consts.VM_MODULESNAME_DOMAIN_FIRST_LOWER);
		vcService.put(VmVarInfo.PO_FIELD_NAMES, dLst);
		vcService.put(VmVarInfo.CLASS_FIRST_UPPER_NAME, StringUtil.upperFirst(tbName));
		vcService.put(VmVarInfo.PO_FIELD_ROBASE_SIMPLE_TYPES, dtLst);
		vcService.put(VmVarInfo.PO_PK_FIRST_LOWER_NAME, poPkLowerName);
		vcService.put(VmVarInfo.PO_PK_FIRST_UPPER_NAME, poPkUpperName);
		vcService.put(VmVarInfo.PO_FIELD_COMMENT, commentLst);
		vcService.put(VmVarInfo.VM_VAR_INFO, new VmVarInfo());
		baseService.setVelocityContext(vcService);
		
		GenCode.genJavaCode(oDomain,baseService);
		
		System.out.println(tbName + ".damain生成完成");
		return true;
	}
	
}
