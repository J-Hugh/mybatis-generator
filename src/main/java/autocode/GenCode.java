/*
 * $Id: GenCode.java,v 1.1 2012/09/11 06:38:11 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.model.AutoCodeInfo;
import autocode.model.OneJavaInfo;
import autocode.model.OneJavaUiInfo;
import autocode.util.FileUtil;
import autocode.util.StringUtil;
import autocode.util.VmFileUtil;
import autocode.util.XmlUtil;
import autocode.util.auxiliary.vm.VmVarBase;
import autocode.util.auxiliary.vm.VmVarI18nInfo;
import autocode.util.auxiliary.vm.VmVarInfo;
import autocode.util.auxiliary.vm.VmVarJavaBaseInfo;
import autocode.util.auxiliary.vm.VmVarViewBaseInfo;
import autocode.util.auxiliary.vm.VmVarViewFormInfo;
import autocode.util.auxiliary.vm.VmVarViewListInfo;
import autocode.util.auxiliary.vm.VmVarViewShowInfo;
import autocode.util.auxiliary.vm.relationtable.VmVarControllerInfo;
import autocode.util.auxiliary.vm.relationtable.VmVarRestInfo;
import autocode.util.auxiliary.xml.HibernateDtdEntityResolver;
import dataset.model.IEntity;
import dataset.model.IPEntity;
import dataset.model.IVEntity;

/**
 * 根据dataset或hbm文件生成PO
 * 
 * @author Rone BizFoudation Framework Team: ganjp
 * @version 1.0
 * @since 4.3
 */
public class GenCode {
	static Logger log = LoggerFactory.getLogger(GenCode.class);
    
    /**
     * <p>单步生成Service(总共生成两个文件：Service和ServieImpl,再注册到ApplicationContext.xml)</p>
     * <pre>
     *      OneJavaUiInfo oneJavaUiInfo = new OneJavaUiInfo();
     *      oneJavaUiInfo.setFileName("UserService");
     *      oneJavaUiInfo.setFilePath("D:/workspace/src/main/java/com/chinasofti/ro/bizframework/modules/autocode");
     *      oneJavaUiInfo.setVmDirPath("D:/workspace/vm/blank");
     *      oneJavaUiInfo.setPackageName("com.chinasofti.ro.bizframework.modules.autocode");
     *      oneJavaUiInfo.setRegistFileFullPath("D:/workspace/applicationContext.xml");
     *      genService(oneJavaUiInfo);
     * </pre>
     * 
     * @param oneJavaUiInfo
     */
    public static void genService(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //生成service接口
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName());
        oneJavaInfo.setFilePath(oneJavaUiInfo.getFilePath());
        oneJavaInfo.setVmFileFullPath(oneJavaUiInfo.getVmDirPath()+"/Service.vm");
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        
//        genJavaCode(vin, oneJavaInfo.getVmFileFullPath(), fileFullPath);
//        vmVarJavaBaseInfo.set
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
        //生成serviceImpl
//        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName()+"Impl");
//        oneJavaInfo.setFilePath(oneJavaUiInfo.getFilePath()+"/impl");
//        oneJavaInfo.setVmFileFullPath(oneJavaUiInfo.getVmDirPath()+"/ServiceImpl.vm");
//        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName() + ".impl");
//        vmVarJavaBaseInfo.setClassName(oneJavaUiInfo.getFileName() + "Impl");
//        vmVarJavaBaseInfo.addImportTypes(oneJavaUiInfo.getPackageName() + "." + oneJavaUiInfo.getFileName());
//        vmVarJavaBaseInfo.setClassImplementInterfaces(oneJavaUiInfo.getFileName());
//        vmVarJavaBaseInfo.setBeanName(StringUtil.lowerFirst(oneJavaUiInfo.getFileName()));
//        vmVarJavaBaseInfo.setBeanClass(vmVarJavaBaseInfo.getPackageName() + "." + oneJavaInfo.getFileName());
//        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
//        if (StringUtil.isNotBlank(oneJavaUiInfo.getRegistFileFullPath())) {
////            registSpringBean(oneJavaUiInfo.getRegistFileFullPath(), vmVarJavaBaseInfo);
//        }
        
    }
    
    /**
     * <p>单步生成Service(总共生成两个文件：Service和ServieImpl,再注册到ApplicationContext.xml)</p>
     * <pre>
     *      OneJavaUiInfo oneJavaUiInfo = new OneJavaUiInfo();
     *      oneJavaUiInfo.setFileName("UserService");
     *      oneJavaUiInfo.setFilePath("D:/workspace/src/main/java/com/chinasofti/ro/bizframework/modules/autocode");
     *      oneJavaUiInfo.setVmDirPath("D:/workspace/vm/blank");
     *      oneJavaUiInfo.setPackageName("com.chinasofti.ro.bizframework.modules.autocode");
     *      oneJavaUiInfo.setRegistFileFullPath("D:/workspace/applicationContext.xml");
     *      genService(oneJavaUiInfo);
     * </pre>
     * 
     * @param oneJavaUiInfo
     */
    public static void genRest(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //生成rest接口
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName());
        oneJavaInfo.setFilePath(oneJavaUiInfo.getFilePath());
        oneJavaInfo.setVmFileFullPath(oneJavaUiInfo.getVmDirPath()+"/Rest.vm");
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setIndexName(StringUtil.lowerFirst(oneJavaInfo.getFileName().replace("RestController", "")));
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }
    
    /**
     * <p>单步生成Controller文件</p>
     * <pre>
     *      OneJavaUiInfo oneJavaUiInfo = new OneJavaUiInfo();
     *      oneJavaUiInfo.setFileName("UserController");
     *      oneJavaUiInfo.setFilePath("D:/workspace/src/main/java/com/chinasofti/ro/bizframework/modules/autocode");
     *      oneJavaUiInfo.setVmDirPath("D:/workspace/vm/blank");
     *      genController(oneJavaUiInfo);
     * </pre>
     * 
     * @param oneJavaUiInfo
     */
    public static void genController(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //生成Controller
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName());
        oneJavaInfo.setFilePath(oneJavaUiInfo.getFilePath());
        oneJavaInfo.setVmFileFullPath(oneJavaUiInfo.getVmDirPath()+"/Controller.vm");
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setIndexName(StringUtil.lowerFirst(oneJavaInfo.getFileName().replace("Controller", "")));
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }
    
    
    
    /**
     * <p>生成XXXModel文件</p>
     * 
     * @param oneJavaUiInfo
     */
    public static void genMobileModel(OneJavaUiInfo oneJavaUiInfo){
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //create Model
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName()+"Model");
        oneJavaInfo.setFilePath(StringUtil.replaceTemplate(oneJavaUiInfo.getFilePath(),""));
        oneJavaInfo.setEntityName(oneJavaUiInfo.getEntityName());
        oneJavaInfo.setVmFileFullPath(StringUtil.replaceTemplate(oneJavaUiInfo.getVmDirPath(), "/BizModel.vm"));
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setEntityName(oneJavaUiInfo.getEntityName());
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }
    /**
     * <p>生成XXXService文件</p>
     * 
     * @param oneJavaUiInfo
     */
    public static void genMobileService(OneJavaUiInfo oneJavaUiInfo){
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //生成service接口
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName()+"Service");
        String path  =oneJavaUiInfo.getFilePath();
        oneJavaInfo.setFilePath(path.substring(0, path.lastIndexOf("model")) + "service");
        oneJavaInfo.setVmFileFullPath(oneJavaUiInfo.getVmDirPath()+"/Service.vm");
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName()+".service");
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setBasePackageName(oneJavaUiInfo.getPackageName());
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
        //生成serviceImpl
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName()+"ServiceImpl");
        oneJavaInfo.setFilePath(path.substring(0, path.lastIndexOf("model"))+"service/impl");
        oneJavaInfo.setVmFileFullPath(oneJavaUiInfo.getVmDirPath()+"/ServiceImpl.vm");
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName() + ".service.impl");
        vmVarJavaBaseInfo.setBasePackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaUiInfo.getFileName() + "ServiceImpl");
        vmVarJavaBaseInfo.addImportTypes(oneJavaUiInfo.getPackageName() + "." + oneJavaUiInfo.getFileName());
        vmVarJavaBaseInfo.setClassImplementInterfaces(oneJavaUiInfo.getFileName());
        vmVarJavaBaseInfo.setBeanName(StringUtil.lowerFirst(oneJavaUiInfo.getFileName()));
        vmVarJavaBaseInfo.setBeanClass(vmVarJavaBaseInfo.getPackageName() + "." + oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setEntityName(StringUtil.upperFirst(oneJavaUiInfo.getEntityName()));

        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }
    
    /**
     * <p>生成Parser文件</p>
     * 
     * @param oneJavaUiInfo
     */
    public static void genParser(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //生成Parser
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName()+"Parser");
        oneJavaInfo.setFilePath(StringUtil.replaceTemplate(oneJavaUiInfo.getFilePath(),""));
        oneJavaInfo.setEntityName(oneJavaUiInfo.getEntityName());
        oneJavaInfo.setVmFileFullPath(StringUtil.replaceTemplate(oneJavaUiInfo.getVmDirPath(), "/Parser.vm"));
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setEntityName(oneJavaUiInfo.getEntityName());
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }
    /**
     * <p>生成DataAccess文件</p>
     * 
     * @param oneJavaUiInfo
     */
    public static void genDataAccess(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //生成DataAccess
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName()+"DataAccess");
        oneJavaInfo.setFilePath(StringUtil.replaceTemplate(oneJavaUiInfo.getFilePath(),""));
        oneJavaInfo.setEntityName(oneJavaUiInfo.getEntityName());
        oneJavaInfo.setVmFileFullPath(StringUtil.replaceTemplate(oneJavaUiInfo.getVmDirPath(), "/DataAccess.vm"));
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setEntityName(oneJavaUiInfo.getEntityName());
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }
    /**
     * <p>生成LocalDataAccess文件</p>
     * 
     * @param oneJavaUiInfo
     */
    public static void genLocalDataAccess(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //生成LocalDataAccess
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName()+"LocalDataAccess");
        oneJavaInfo.setFilePath(StringUtil.replaceTemplate(oneJavaUiInfo.getFilePath(),""));
        oneJavaInfo.setEntityName(oneJavaUiInfo.getEntityName());
        oneJavaInfo.setVmFileFullPath(StringUtil.replaceTemplate(oneJavaUiInfo.getVmDirPath(), "/LocalDataAccess.vm"));
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setEntityName(oneJavaUiInfo.getEntityName());
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }
    /**
     * <p>生成ServerDataAccess文件</p>
     * 
     * @param oneJavaUiInfo
     */
    public static void genServerDataAccess(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        //生成ServerDataAccess
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName()+"ServerDataAccess");
        oneJavaInfo.setFilePath(StringUtil.replaceTemplate(oneJavaUiInfo.getFilePath(),""));
        oneJavaInfo.setEntityName(oneJavaUiInfo.getEntityName());
        oneJavaInfo.setVmFileFullPath(StringUtil.replaceTemplate(oneJavaUiInfo.getVmDirPath(), "/ServerDataAccess.vm"));
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        vmVarJavaBaseInfo.setEntityName(oneJavaUiInfo.getEntityName());
        genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }
   
    
    public static void genJavaCode(OneJavaInfo oneJavaInfo, VmVarJavaBaseInfo vmVarJavaBaseInfo) {
        if (StringUtil.isNotBlank(oneJavaInfo.getFileName()) && StringUtil.isNotBlank(oneJavaInfo.getVmFileFullPath())) {
            String vmString = VmFileUtil.getVmString(oneJavaInfo.getVmFileFullPath(), vmVarJavaBaseInfo.getVelocityContext());
            String fileFullPath = oneJavaInfo.getFilePath() + "/" + oneJavaInfo.getFileName() +".java"; 
            FileUtil.toFile(fileFullPath, vmString);
        } else {
            throw new RuntimeException("请设置生成文件的文件名和VM文件路径");
        }
    }
    
    /**
     * 批量生成java代码
     * 
     * @param vmVarInfoList
     * @param autoCodeInfoMap
     * @param fileType
     */
    public static void genJavaCode(List vmVarInfoList) {
        if (vmVarInfoList==null || vmVarInfoList.isEmpty()) {
            log.info("vm变量信息集为空");
            return;
        }
        for (Iterator iterator = vmVarInfoList.iterator(); iterator.hasNext();) {
            VmVarInfo vmVarInfo = (VmVarInfo) iterator.next();
            genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
        }
    }
    
    /**
     * 根据vm文件生成生成java代码
     * 
     * @param vmVarInfo
     * @param vmFilePath
     */
    public static void genJavaCode(VmVarInfo vmVarInfo, String vmFilePath, String outFilePath) {
        if (StringUtil.isNotBlank(vmFilePath)) {
            if (StringUtil.isBlank(vmVarInfo.getPackageName())) {
                throw new RuntimeException(vmVarInfo.getEntityName()+"请设置生成文件的包名");
            }
            if (StringUtil.isBlank(outFilePath)) {
                throw new RuntimeException(vmVarInfo.getEntityName()+"请设置生成文件的路径");
            }
            if (vmVarInfo.getClassFirstUpperName().equalsIgnoreCase(vmVarInfo.getPoFirstUpperName())) { 
                File hbmFile = new File(outFilePath.replaceAll(vmVarInfo.getClassFirstUpperName()+".java", vmVarInfo.getClassFirstUpperName()+".hbm.xml"));
                genAssociateField(vmVarInfo, hbmFile);
            }
            String vmString = VmFileUtil.getVmString(vmFilePath, vmVarInfo);//把vmVarInfo赋值到vm
            FileUtil.toFile(outFilePath, vmString);
            //如果生成java代码是PO时并且该po又是多主键，那么就得生成多主键对应主键类
            if (vmVarInfo.getClassFirstUpperName().equalsIgnoreCase(vmVarInfo.getPoFirstUpperName()) && vmVarInfo.isPoCompositePk()) {
                outFilePath = outFilePath.replaceAll(vmVarInfo.getClassFirstUpperName()+".java", vmVarInfo.getClassFirstUpperName()+"Pk.java");
                genPoPk(vmVarInfo, vmFilePath, outFilePath);
            }
            log.debug(vmVarInfo.getEntityName() + "对应的vm模板" + vmFilePath + "生成成功.");
        }
    }
    /**
     * 生成查询实体Controller代码
     * @param entity
     * @param autoCodeInfo
     */
    public static void genController(IVEntity entity, AutoCodeInfo autoCodeInfo){
    	if (StringUtil.isNotBlank(autoCodeInfo.getControllerVmFilePath()) && StringUtil.isNotBlank(autoCodeInfo.getControllerPath())) {
    		
        	VmVarControllerInfo vmVarInfo = new VmVarControllerInfo(entity,autoCodeInfo);
        	vmVarInfo.setBasePackageName(autoCodeInfo.getBasePackageName());
        	vmVarInfo.setModule(autoCodeInfo.getModuleName());
        	
        	String[] vmPath = FileUtil.getDirPathAndFileName(autoCodeInfo.getControllerVmFilePath());
    		String controller = VmFileUtil.getVmString(vmPath[0], vmPath[1], vmVarInfo.getVelocityContext(), vmVarInfo.getEncode());
    		FileUtil.toFile(autoCodeInfo.getControllerPath(), controller,vmVarInfo.getEncode());        
//        	registBizMvc(vmVarInfo, autoCodeInfo.getBizmvcConfigPath() ,autoCodeInfo.getBizMvcVmFilePath());
    	}
    }
    
    /**
     * 生成查询实体Controller代码
     * @param entity
     * @param autoCodeInfo
     */
    public static void genRestController(IVEntity entity, AutoCodeInfo autoCodeInfo){
    	if (StringUtil.isNotBlank(autoCodeInfo.getRestVmFilePath()) && StringUtil.isNotBlank(autoCodeInfo.getRestPath())) {
    		
    		VmVarRestInfo vmVarInfo = new VmVarRestInfo(entity,autoCodeInfo);
        	vmVarInfo.setBasePackageName(autoCodeInfo.getBasePackageName());
        	vmVarInfo.setModule(autoCodeInfo.getModuleName());
        	
        	String[] vmPath = FileUtil.getDirPathAndFileName(autoCodeInfo.getRestVmFilePath());
    		String controller = VmFileUtil.getVmString(vmPath[0], vmPath[1], vmVarInfo.getVelocityContext(), vmVarInfo.getEncode());
    		FileUtil.toFile(autoCodeInfo.getRestPath(), controller,vmVarInfo.getEncode());        
//        	registBizMvc(vmVarInfo, autoCodeInfo.getBizmvcConfigPath() ,autoCodeInfo.getBizMvcVmFilePath());
    	}
    }
    
    /**
     * 生成View页面
     * 
     * @param pEntity
     * @param autoCodeInfo
     */
    public static void genView(IPEntity pEntity, AutoCodeInfo autoCodeInfo) {
        //生成查询结果页面
        if(pEntity.getQueryForm() != null && pEntity.getResultForm() != null 
        		&& pEntity.getResultForm().getFields().size() >0){
            VmVarViewListInfo viewListInfo = new VmVarViewListInfo(pEntity);
            viewListInfo.setUseI18N(autoCodeInfo.isUseI18n());
            viewListInfo.setViewJsPath(autoCodeInfo.getViewListJsPath());
            if(StringUtil.isNotBlank(autoCodeInfo.getViewListVmFilePath())){
                //生成JSP页面
                genView(viewListInfo, autoCodeInfo.getViewListVmFilePath(), autoCodeInfo.getViewListFilePath());
            }
            if(StringUtil.isNotBlank(autoCodeInfo.getViewListJsVmFilePath())){
                //成才JS文件
                genView(viewListInfo,autoCodeInfo.getViewListJsVmFilePath(),autoCodeInfo.getViewListJsPath());
            }
        }
        //生成新增页面
        if(pEntity.getNewForm() != null && pEntity.getNewForm().getFields().size() > 0){
            VmVarViewFormInfo viewNewInfo = new VmVarViewFormInfo(pEntity);
            viewNewInfo.setUseI18N(autoCodeInfo.isUseI18n());
            viewNewInfo.setViewJsPath(autoCodeInfo.getViewNewJsPath());
            if(StringUtil.isNotBlank(autoCodeInfo.getViewNewVmFilePath())){
                //生成JSP页面
                genView(viewNewInfo, autoCodeInfo.getViewNewVmFilePath(), autoCodeInfo.getViewNewFilePath());
            }
            if(StringUtil.isNotBlank(autoCodeInfo.getViewNewJsVmFilePath())){
                //成才JS文件
                genView(viewNewInfo, autoCodeInfo.getViewNewJsVmFilePath(),autoCodeInfo.getViewNewJsPath());
            }
        }
        //生成编辑页面
        if(pEntity.getNewForm() != null && pEntity.getNewForm().getFields().size() > 0){
            VmVarViewFormInfo viewNewInfo = new VmVarViewFormInfo(pEntity);
            viewNewInfo.setUseI18N(autoCodeInfo.isUseI18n());
            viewNewInfo.setViewJsPath(autoCodeInfo.getViewUpdateJsPath());
            if(StringUtil.isNotBlank(autoCodeInfo.getViewUpdateVmFilePath())){
                //生成JSP页面
                genView(viewNewInfo, autoCodeInfo.getViewUpdateVmFilePath(), autoCodeInfo.getViewUpdateFilePath());
            }
            if(StringUtil.isNotBlank(autoCodeInfo.getViewUpdateJsVmFilePath())){
                //成才JS文件
                genView(viewNewInfo, autoCodeInfo.getViewUpdateJsVmFilePath(), autoCodeInfo.getViewUpdateJsPath());
            }
        }
        //生成查看页面
        if(pEntity.getDetailInfoForm() != null && pEntity.getDetailInfoForm().getFields().size() > 0){
            VmVarViewShowInfo viewShowInfo = new VmVarViewShowInfo(pEntity);
            viewShowInfo.setUseI18N(autoCodeInfo.isUseI18n());
            viewShowInfo.setViewJsPath(autoCodeInfo.getViewShowJsPath());
            if(StringUtil.isNotBlank( autoCodeInfo.getViewShowVmFilePath())){
                //生成JSP页面
                genView(viewShowInfo, autoCodeInfo.getViewShowVmFilePath(), autoCodeInfo.getViewShowFilePath());
            }
            if(StringUtil.isNotBlank(autoCodeInfo.getViewShowJsVmFilePath())){
                //成才JS文件
                genView(viewShowInfo, autoCodeInfo.getViewShowJsVmFilePath(), autoCodeInfo.getViewShowJsPath());
            }
        }
    }
    
    /**
     * 生成View页面
     * @param pEntity
     * @param autoCodeInfo
     * @param detailPEntitys 关联实体列表(元素类型：IPEntity)
     */
    public static void genView(IPEntity pEntity, AutoCodeInfo autoCodeInfo,List detailPEntitys){
        //生成查询结果页面
        if(pEntity.getQueryForm() != null && pEntity.getResultForm() != null){
            VmVarViewListInfo viewListInfo = new VmVarViewListInfo(pEntity);
            viewListInfo.setUseI18N(autoCodeInfo.isUseI18n());
            viewListInfo.setViewJsPath(autoCodeInfo.getViewListJsPath());
            viewListInfo.setRelationEntityViewInfos(detailPEntitys);
            if(StringUtil.isNotBlank(autoCodeInfo.getViewListVmFilePath())){
                //生成JSP页面
                genView(viewListInfo, autoCodeInfo.getViewListVmFilePath(), autoCodeInfo.getViewListFilePath());
            }
            if(StringUtil.isNotBlank(autoCodeInfo.getViewListJsVmFilePath())){
                //成才JS文件
                genView(viewListInfo,autoCodeInfo.getViewListJsVmFilePath(),autoCodeInfo.getViewListJsPath());
            }
        }
        //生成新增页面
        if(pEntity.getNewForm() != null){
            VmVarViewFormInfo viewNewInfo = new VmVarViewFormInfo(pEntity);
            viewNewInfo.setUseI18N(autoCodeInfo.isUseI18n());
            viewNewInfo.setViewJsPath(autoCodeInfo.getViewNewJsPath());
            viewNewInfo.setRelationEntityViewInfos(detailPEntitys);
            if(StringUtil.isNotBlank(autoCodeInfo.getViewNewVmFilePath())){
                //生成JSP页面
                genView(viewNewInfo, autoCodeInfo.getViewNewVmFilePath(), autoCodeInfo.getViewNewFilePath());
            }
            if(StringUtil.isNotBlank(autoCodeInfo.getViewNewJsVmFilePath())){
                //成才JS文件
                genView(viewNewInfo, autoCodeInfo.getViewNewJsVmFilePath(),autoCodeInfo.getViewNewJsPath());
            }
        }
        //生成编辑页面
        if(pEntity.getNewForm() != null){
            VmVarViewFormInfo viewNewInfo = new VmVarViewFormInfo(pEntity);
            viewNewInfo.setUseI18N(autoCodeInfo.isUseI18n());
            viewNewInfo.setViewJsPath(autoCodeInfo.getViewUpdateJsPath());
            viewNewInfo.setRelationEntityViewInfos(detailPEntitys);
            if(StringUtil.isNotBlank(autoCodeInfo.getViewUpdateVmFilePath())){
                //生成JSP页面
                genView(viewNewInfo, autoCodeInfo.getViewUpdateVmFilePath(), autoCodeInfo.getViewUpdateFilePath());
            }
            if(StringUtil.isNotBlank(autoCodeInfo.getViewUpdateJsVmFilePath())){
                //成才JS文件
                genView(viewNewInfo, autoCodeInfo.getViewUpdateJsVmFilePath(), autoCodeInfo.getViewUpdateJsPath());
            }
        }
        //生成查看页面
        if(pEntity.getDetailInfoForm() != null){
            VmVarViewShowInfo viewShowInfo = new VmVarViewShowInfo(pEntity);
            viewShowInfo.setUseI18N(autoCodeInfo.isUseI18n());
            viewShowInfo.setViewJsPath(autoCodeInfo.getViewShowJsPath());
            viewShowInfo.setRelationEntityViewInfos(detailPEntitys);
            if(StringUtil.isNotBlank( autoCodeInfo.getViewShowVmFilePath())){
                //生成JSP页面
                genView(viewShowInfo, autoCodeInfo.getViewShowVmFilePath(), autoCodeInfo.getViewShowFilePath());
            }
            if(StringUtil.isNotBlank(autoCodeInfo.getViewShowJsVmFilePath())){
                //成才JS文件
                genView(viewShowInfo, autoCodeInfo.getViewShowJsVmFilePath(), autoCodeInfo.getViewShowJsPath());
            }
        }
    }
    
    /**
     * 用于生成View页面或者对应的JS文件
     * @param viewInfo
     * @param vmFilePath
     * @param viewFilePath
     */
    public static void genView(VmVarViewBaseInfo viewInfo,String vmFilePath,String viewFilePath){
        String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
        String vmString = VmFileUtil.getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], viewInfo.getVelocityContext(), null);
        FileUtil.toFile(viewFilePath,vmString);
        log.debug(viewInfo.getlowerFirstEntityName() + "对应的vm模板" + vmFilePath + "生成成功.");
    }
    
    /**
     * 生成View页面
     * @param entity
     * @param autoCodeInfo
     */
    public static void genView(IEntity entity, AutoCodeInfo autoCodeInfo) {
        //生成查询结果页面
        if(entity.getQueryForm() != null && entity.getResultForm() != null){
            VmVarBase vmVarInfo = VmVarViewFactory.createVmVarView(entity, entity.getQueryForm());
            vmVarInfo.setUseI18N(autoCodeInfo.isUseI18n());
            vmVarInfo.setViewJsPath(autoCodeInfo.getViewListJsPath());
            vmVarInfo.setPageEncoding(autoCodeInfo.getEncode());
            if(FileUtil.exist(autoCodeInfo.getViewListVmFilePath())){
                //生成JSP页面
                genView(vmVarInfo, autoCodeInfo.getViewListVmFilePath(), autoCodeInfo.getViewListFilePath());
            }
            if(FileUtil.exist(autoCodeInfo.getViewListJsVmFilePath())){
                //成才JS文件
                genView(vmVarInfo,autoCodeInfo.getViewListJsVmFilePath(),autoCodeInfo.getViewListJsPath());
            }
        }
        //生成新增页面
        if(entity.getNewForm() != null){
        	VmVarBase vmVarInfo = VmVarViewFactory.createVmVarView(entity, entity.getNewForm());
        	vmVarInfo.setUseI18N(autoCodeInfo.isUseI18n());
        	vmVarInfo.setViewJsPath(autoCodeInfo.getViewNewJsPath());
        	vmVarInfo.setPageEncoding(autoCodeInfo.getEncode());
            if(FileUtil.exist(autoCodeInfo.getViewNewVmFilePath())){
                //生成JSP页面
                genView(vmVarInfo, autoCodeInfo.getViewNewVmFilePath(), autoCodeInfo.getViewNewFilePath());
            }
            if(FileUtil.exist(autoCodeInfo.getViewNewJsVmFilePath())){
                //成才JS文件
                genView(vmVarInfo, autoCodeInfo.getViewNewJsVmFilePath(),autoCodeInfo.getViewNewJsPath());
            }
        }
        //生成编辑页面
        if(entity.getNewForm() != null){
        	VmVarBase vmVarInfo = VmVarViewFactory.createVmVarView(entity, entity.getNewForm());
        	vmVarInfo.setUseI18N(autoCodeInfo.isUseI18n());
        	vmVarInfo.setViewJsPath(autoCodeInfo.getViewNewJsPath());
        	vmVarInfo.setPageEncoding(autoCodeInfo.getEncode());
            if(FileUtil.exist(autoCodeInfo.getViewUpdateVmFilePath())){
                //生成JSP页面
                genView(vmVarInfo, autoCodeInfo.getViewUpdateVmFilePath(), autoCodeInfo.getViewUpdateFilePath());
            }
            if(FileUtil.exist(autoCodeInfo.getViewUpdateJsVmFilePath())){
                //成才JS文件
                genView(vmVarInfo, autoCodeInfo.getViewUpdateJsVmFilePath(), autoCodeInfo.getViewUpdateJsPath());
            }
        }
        //生成查看页面
        if(entity.getDetailInfoForm() != null){
        	VmVarBase vmVarInfo = VmVarViewFactory.createVmVarView(entity, entity.getDetailInfoForm());
        	vmVarInfo.setUseI18N(autoCodeInfo.isUseI18n());
        	vmVarInfo.setViewJsPath(autoCodeInfo.getViewNewJsPath());
        	vmVarInfo.setPageEncoding(autoCodeInfo.getEncode());
            if(FileUtil.exist(autoCodeInfo.getViewShowVmFilePath())){
                //生成JSP页面
                genView(vmVarInfo, autoCodeInfo.getViewShowVmFilePath(), autoCodeInfo.getViewShowFilePath());
            }
            if(FileUtil.exist(autoCodeInfo.getViewShowJsVmFilePath())){
                //成才JS文件
                genView(vmVarInfo, autoCodeInfo.getViewShowJsVmFilePath(), autoCodeInfo.getViewShowJsPath());
            }
        }
    }

    /**
     * 用于生成View页面或者对应的JS文件
     * @param viewInfo
     * @param vmFilePath
     * @param viewFilePath
     */
    public static void genView(VmVarBase viewInfo,String vmFilePath,String viewFilePath){
        String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
        String vmString = VmFileUtil.getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], viewInfo.getVelocityContext(), viewInfo.getPageEncoding());
        FileUtil.toFile(viewFilePath,vmString,viewInfo.getPageEncoding());
        log.debug(viewInfo.getEntityName() + "对应的vm模板" + vmFilePath + "生成成功.");
    }
    
    /**
     * 用于生成View页面或者对应的JS文件
     * @param viewInfo
     * @param vmFilePath
     * @param viewFilePath
     */
    public static void genSQLJavaCode(autocode.util.auxiliary.vm.relationtable.VmVarJavaBaseInfo javaInfo,String vmFilePath,String viewFilePath){
        String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
        String vmString = VmFileUtil.getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], javaInfo.getVelocityContext(), null);
        FileUtil.toFile(viewFilePath,vmString);
        log.debug(javaInfo.getEntity().getName() + "对应的vm模板" + vmFilePath + "生成成功.");
    }
    
    
    /**
     * 生成关联的字段对象在po里面
     * 
     * @param vmVarInfo
     * @param hbmFile
     */
    private static void genAssociateField(VmVarInfo vmVarInfo, File hbmFile) {
        if (hbmFile.exists()) {
            Document document = XmlUtil.getDocument(hbmFile, new HibernateDtdEntityResolver());
            Element hbmNode = document.getRootElement();
            Element classNode = hbmNode.element("class");
            Iterator it = classNode.elementIterator("many-to-one");
            while (it.hasNext()) {
                Element many2OneElement = (Element) it.next();
                String fieldName = many2OneElement.attributeValue("name");
                String className = many2OneElement.attributeValue("class");
                vmVarInfo.addPoFieldName(StringUtil.lowerFirst(fieldName));
                vmVarInfo.addPoFieldFirstUpperName(StringUtil.upperFirst(fieldName));
                vmVarInfo.addPoFieldType(className);
                vmVarInfo.addClassImportType(className);
                vmVarInfo.addPoFieldRobaseSimpleType(className.substring(className.lastIndexOf(".")+1, className.length()));
                vmVarInfo.addPoFieldHibernateSimpleType(className.substring(className.lastIndexOf(".")+1, className.length()));
                vmVarInfo.addPoFieldIsPersist(false);
            }
            Element one2oneEl = classNode.element("one-to-one");
            if (one2oneEl != null) {
                String fieldName = one2oneEl.attributeValue("name");
                String className = one2oneEl.attributeValue("class");
                vmVarInfo.addPoFieldName(StringUtil.lowerFirst(fieldName));
                vmVarInfo.addPoFieldFirstUpperName(StringUtil.upperFirst(fieldName));
                vmVarInfo.addPoFieldType(className);
                vmVarInfo.addClassImportType(className);
                vmVarInfo.addPoFieldRobaseSimpleType(className.substring(className.lastIndexOf(".")+1, className.length()));
                vmVarInfo.addPoFieldHibernateSimpleType(className.substring(className.lastIndexOf(".")+1, className.length()));
                vmVarInfo.addPoFieldIsPersist(false);
            }
        }
    }
    
    private static void genPoPk(VmVarInfo vmVarInfo, String vmFilePath, String filePath) {
        vmFilePath = vmFilePath.substring(0, vmFilePath.length()-3)+"Pk.vm";
        String vmString = VmFileUtil.getVmString(vmFilePath, vmVarInfo.getVmVarPoPkInfo());//把vmVarPoPkInfo赋值到vm
        FileUtil.toFile(filePath, vmString);
    }
    
//    /**
//     * 把注册到bean注册到applicationContext文件里
//     * 
//     * @param vmVarInfoList
//     */
//    public static void registSpringBean(List vmVarInfoList) {
//        if (vmVarInfoList==null || vmVarInfoList.isEmpty()) {
//            log.info("vm变量信息集为空");
//            return;
//        }
//        for (Iterator iterator = vmVarInfoList.iterator(); iterator.hasNext();) {
//            VmVarInfo vmVarInfo = (VmVarInfo) iterator.next();
//            registSpringBean(vmVarInfo);
//        }
//    }
    
    /**
     * 把注册到bean注册到applicationContext文件里
     * 
     * @param vmVarInfo
     */
    public static void registSpringBean(VmVarInfo vmVarInfo) {
        if (StringUtil.isBlank(vmVarInfo.getRegistFile())) {
            log.error(vmVarInfo.getEntityName()+"请设置注册的配置文件名");
            return;
        }
        String beanName = vmVarInfo.getRegistBeanName();
        String className = vmVarInfo.getRegistClass();
        FileUtil.writeOrReplaceText(vmVarInfo.getRegistFile(), 
                StringUtil.format(springBeanExistStr, beanName),
                StringUtil.format(springBeanRegexExist, beanName, beanName),
                StringUtil.formatWithNumber(springBeanReplaceStrExist, beanName, className),
                springBeanRegexNoExist, 
                StringUtil.formatWithNumber(springBeanReplaceStrNoExit, beanName, className));
    }
//    /**
//     * 把注册到bean注册到applicationContext文件里
//     * 
//     * @param vmVarInfo
//     */
//    public static void registSpringBean(VmVarServiceInfo vmVarInfo) {
//        if (StringUtil.isBlank(vmVarInfo.getRegistFile())) {
//            log.error(vmVarInfo.getEntity().getName()+"请设置注册的配置文件名");
//            return;
//        }
//        String beanName = vmVarInfo.getRegistBeanName();
//        String className = vmVarInfo.getRegistClass();
//        FileUtil.writeOrReplaceText(vmVarInfo.getRegistFile(), 
//                StringUtil.format(springBeanExistStr, beanName),
//                StringUtil.format(springBeanRegexExist, beanName, beanName),
//                StringUtil.formatWithNumber(springBeanReplaceStrExist, beanName, className),
//                springBeanRegexNoExist, 
//                StringUtil.formatWithNumber(springBeanReplaceStrNoExit, beanName, className));
//    }
    
    /**
     * 把注册到bean注册到applicationContext文件里
     * 
     * @param registFilePath
     * @param vmVarJavaBaseInfo
     */
    public static void registSpringBean(String registFilePath, VmVarJavaBaseInfo vmVarJavaBaseInfo) {
        if (StringUtil.isBlank(registFilePath)) {
            log.error("请设置注册的配置文件名");
            return;
        }
        String beanName = vmVarJavaBaseInfo.getBeanName();
        String beanClass = vmVarJavaBaseInfo.getBeanClass();
        FileUtil.writeOrReplaceText(registFilePath, 
                StringUtil.format(springBeanExistStr, beanName),
                StringUtil.format(springBeanRegexExist, beanName, beanName),
                StringUtil.formatWithNumber(springBeanReplaceStrExist, beanName, beanClass),
                springBeanRegexNoExist, 
                StringUtil.formatWithNumber(springBeanReplaceStrNoExit, beanName, beanClass));
    }
    
    /**
     * 把注册到bean注册到applicationContext文件里
     * 
     * @param vmVarInfo
     * @param springBeanFilePath
     */
    public static void registSpringBean(VmVarInfo vmVarInfo, String springBeanFilePath) {
        if (StringUtil.isBlank(vmVarInfo.getRegistBeanName())) {
            throw new RuntimeException(vmVarInfo.getEntityName()+"请设置生成文件的包名");
        }
        String beanName = vmVarInfo.getRegistBeanName();
        String className = vmVarInfo.getPackageName() + "." + vmVarInfo.getClassFirstUpperName();
        FileUtil.writeOrReplaceText(springBeanFilePath, 
                StringUtil.format(springBeanExistStr, beanName),
                StringUtil.format(springBeanRegexExist, beanName, beanName),
                StringUtil.formatWithNumber(springBeanReplaceStrExist, beanName, className),
                springBeanRegexNoExist, 
                StringUtil.formatWithNumber(springBeanReplaceStrNoExit, beanName, className));
    }
    
    /**
     * 判断是否需要生成文件
     * 
     * @param autoCodeInfo
     * @param fileType
     * @return
     */
    public static boolean isGen(AutoCodeInfo autoCodeInfo, String fileType) { 
        if (GenVmVarInfos.FILE_TYPE_DOMAIN.equalsIgnoreCase(fileType)) {
            if (StringUtil.isBlank(autoCodeInfo.getPoVmFilePath())) {
                return false;
            }
        }else if (StringUtil.isBlank(autoCodeInfo.getDaoPath()) || StringUtil.isBlank(autoCodeInfo.getDaoName()) ||
                StringUtil.isBlank(autoCodeInfo.getDaoVmFilePath())) {
            return false;
        }else if (StringUtil.isBlank(autoCodeInfo.getPoPath()) || StringUtil.isBlank(autoCodeInfo.getPoName()) ||
                    StringUtil.isBlank(autoCodeInfo.getPoVmFilePath())) {
                return false;
        } else if (GenVmVarInfos.FILE_TYPE_SERVICE.equalsIgnoreCase(fileType)) {
            if (StringUtil.isBlank(autoCodeInfo.getServicePath()) || StringUtil.isBlank(autoCodeInfo.getServiceName()) ||
                    StringUtil.isBlank(autoCodeInfo.getServiceVmFilePath())) {
                return false;
            }
        } else if (GenVmVarInfos.FILE_TYPE_SERVICEIMPL.equalsIgnoreCase(fileType)) {
            if (StringUtil.isBlank(autoCodeInfo.getServiceImplPath()) || StringUtil.isBlank(autoCodeInfo.getServiceImplName()) ||
                    StringUtil.isBlank(autoCodeInfo.getServiceImplVmFilePath())) {
                return false;
            }
        } else if (GenVmVarInfos.FILE_TYPE_SERVICE_TEST.equalsIgnoreCase(fileType)) {
            if (StringUtil.isBlank(autoCodeInfo.getServiceTestPath()) || StringUtil.isBlank(autoCodeInfo.getServiceTestName()) ||
                    StringUtil.isBlank(autoCodeInfo.getServiceTestVmFilePath())) {
                return false;
            }
        } else if (GenVmVarInfos.FILE_TYPE_CONTROLLER.equalsIgnoreCase(fileType)) {
            if (StringUtil.isBlank(autoCodeInfo.getControllerPath()) || StringUtil.isBlank(autoCodeInfo.getControllerName()) ||
                    StringUtil.isBlank(autoCodeInfo.getControllerVmFilePath())) {
                return false;
            }
        }
        return true;
    }
    /**
     * 注册Controller的MVC配置信息
     * @param vmVarInfo
     * @param bizMvcFilePath mvc配置文件路径
     * @param vmFilePath mvc对应的vm模版文件路径
     */
    public static void registBizMvc(VmVarInfo vmVarInfo, String bizMvcFilePath,String vmFilePath) {
        if (StringUtil.isNotBlank(bizMvcFilePath) && StringUtil.isNotBlank(vmFilePath)) {
            String poName = vmVarInfo.getPoFirstLowerName();
            String mvcContent = VmFileUtil.getVmString(vmFilePath, vmVarInfo);
            FileUtil.writeOrReplaceText(bizMvcFilePath, 
                    StringUtil.format(bizMvcExistStr, poName),
                    StringUtil.format(bizMvcRegexExist, poName, poName),
                    StringUtil.format(bizMvcReplaceStrExist,mvcContent),
                    bizMvcRegexNoExist, 
                    StringUtil.format(bizMvcReplaceStrNoExit,mvcContent));
            log.debug(vmVarInfo.getEntityName() + "对应的vm模板" + vmFilePath + "生成成功.");
        }   
    }
    /**
     * 注册Controller的MVC配置信息
     * @param vmVarInfo mvc配置文件路径
     * @param bizMvcFilePath  mvc.xml文件路径
     * @param vmFilePath mvc对应的vm模版文件路径
     */
    public static void registBizMvc(VmVarControllerInfo vmVarInfo, String bizMvcFilePath,String vmFilePath) {
        if (StringUtil.isNotBlank(bizMvcFilePath) && StringUtil.isNotBlank(vmFilePath)) {
            String poName = vmVarInfo.getEntityName(true);
            String[] vmPath = FileUtil.getDirPathAndFileName(vmFilePath);
            String mvcContent = VmFileUtil.getVmString(vmPath[0],vmPath[1],vmVarInfo.getVelocityContext(),vmVarInfo.getEncode());
            FileUtil.writeOrReplaceText(bizMvcFilePath, 
                    StringUtil.format(bizMvcExistStr, poName),
                    StringUtil.format(bizMvcRegexExist, poName, poName),
                    StringUtil.format(bizMvcReplaceStrExist,mvcContent),
                    bizMvcRegexNoExist, 
                    StringUtil.format(bizMvcReplaceStrNoExit,mvcContent));
            log.debug(vmVarInfo.getEntityName(false) + "对应的vm模板" + vmFilePath + "生成成功.");
        }   
    }
    
//    /**
//     * 注册Controller的MVC配置信息
//     * @param vmVarInfoList
//     * @param bizMvcFilePath mvc配置文件路径
//     * @param vmFilePath mvc对应的vm模版文件路径
//     */
//    public static void registBizMvc(List vmVarInfoList, String bizMvcFilePath,String vmFilePath) {
//        if (vmVarInfoList==null || vmVarInfoList.isEmpty()) {
//            log.info("vm变量信息集为空");
//            return;
//        }
//        for (Iterator iterator = vmVarInfoList.iterator(); iterator.hasNext();) {
//            VmVarInfo vmVarInfo = (VmVarInfo) iterator.next();
//            registBizMvc(vmVarInfo, bizMvcFilePath,vmFilePath);
//        }   
//    }
    
//    /**
//     * 注册Controller的MVC配置信息
//     * @param vmVarInfoList
//     * @param autoCodeInfoMap
//     */
//    public static void registBizMvc(List vmVarInfoList, Map autoCodeInfoMap) {
//        if (vmVarInfoList==null || vmVarInfoList.isEmpty()) {
//            log.info("vm变量信息集为空");
//            return;
//        }
//        for (Iterator iterator = vmVarInfoList.iterator(); iterator.hasNext();) {
//            VmVarInfo vmVarInfo = (VmVarInfo) iterator.next();
//            AutoCodeInfo autoCodeInfo = (AutoCodeInfo)autoCodeInfoMap.get(vmVarInfo.getEntityName());
//            registBizMvc(vmVarInfo, autoCodeInfo.getBizmvcConfigPath(),autoCodeInfo.getBizMvcVmFilePath());
//        }
//    }
    
    /**
     * 注册实体的国际化配置信息
     * 
     * @param vmVarInfo
     * @param i18nFilePath
     * @param vmFilePath
     */
    public static void registI18n(AutoCodeInfo autoCodeInfo, IPEntity pEntity) {
        String vmFilePath = autoCodeInfo.getI18nVmFilePath();
        VmVarI18nInfo vmVarI18nInfo = new VmVarI18nInfo(pEntity);
        if (autoCodeInfo.isUseI18n() && StringUtil.isNotBlank(autoCodeInfo.getI18nVmFilePath())) {
            String[] i18nFileNames = autoCodeInfo.getI18nFileNames();
            for(int i=0;i<i18nFileNames.length;i++){
                String i18nFile = autoCodeInfo.getI18nConfigRelativePath() + "/" + i18nFileNames[i];  
                String poName = pEntity.getName();
                String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
                VelocityContext velocityContext;
                if(i18nFileNames[i].indexOf("zh") == -1) {
                    velocityContext = vmVarI18nInfo.getVelocityContext(VmVarI18nInfo.LANGUAGE_EN);
                } else {
                    velocityContext = vmVarI18nInfo.getVelocityContext(VmVarI18nInfo.LANGUAGE_ZH);
                }
                String mvcContent = VmFileUtil.getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], velocityContext, null);
                FileUtil.replaceOrAppenderAsciiText(i18nFile, 
                        StringUtil.format(i18nExistStr, poName),
                        StringUtil.format(i18nRegexExist, poName, poName),mvcContent);
                log.debug(pEntity.getName() + "对应的vm模板" + vmFilePath + "生成成功.");
            }
        }
    }
    
    /**
     * 注册实体的国际化配置信息
     * 
     * @param vmVarInfo
     * @param i18nFilePath
     * @param vmFilePath
     */
    public static void registI18n(AutoCodeInfo autoCodeInfo, IVEntity vEntity) {
        String vmFilePath = autoCodeInfo.getI18nVmFilePath();
        autocode.util.auxiliary.vm.relationtable.VmVarI18nInfo vmVarI18nInfo = new autocode.util.auxiliary.vm.relationtable.VmVarI18nInfo(vEntity);
        if (autoCodeInfo.isUseI18n() && StringUtil.isNotBlank(autoCodeInfo.getI18nVmFilePath())) {
            String[] i18nFileNames = autoCodeInfo.getI18nFileNames();
            for(int i=0;i<i18nFileNames.length;i++){
                String i18nFile = autoCodeInfo.getI18nConfigRelativePath() + "/" + i18nFileNames[i];  
                String poName = StringUtil.lowerFirst(vEntity.getName());
                String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
                VelocityContext velocityContext;
                if(i18nFileNames[i].indexOf("zh") == -1) {
                    velocityContext = vmVarI18nInfo.getVelocityContext(VmVarI18nInfo.LANGUAGE_EN);
                } else {
                    velocityContext = vmVarI18nInfo.getVelocityContext(VmVarI18nInfo.LANGUAGE_ZH);
                }
                String mvcContent = VmFileUtil.getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], velocityContext, null);
                FileUtil.replaceOrAppenderAsciiText(i18nFile, 
                        StringUtil.format(i18nExistStr, poName),
                        StringUtil.format(i18nRegexExist, poName, poName),mvcContent);
                log.debug(vEntity.getName() + "对应的vm模板" + vmFilePath + "生成成功.");
            }
        }
    }
    
    public static String springBeanExistStr = "<!--%s-start-->";
    public static String springBeanRegexExist = "<!--%s-start-->(?s:.)*<!--%s-end-->";          
    public static String springBeanReplaceStrExist = "<!--%s1-start-->\r\n    <bean name=\"%s1\" class=\"%s2\" autowire=\"byName\" />\r\n    <!--%s1-end-->";
    public static String springBeanRegexNoExist = "</beans>";
    public static String springBeanReplaceStrNoExit = "    <!--%s1-start-->\r\n    <bean name=\"%s1\" class=\"%s2\" autowire=\"byName\"/>\r\n    <!--%s1-end-->\r\n</beans>";

    public static String bizMvcExistStr = "<!--%s-CONTROLLER-START-->";
    public static String bizMvcRegexExist = "<!--%s-CONTROLLER-START-->(?s:.)*<!--%s-CONTROLLER-END-->";            
    public static String bizMvcReplaceStrExist = "%s";
    public static String bizMvcRegexNoExist = "</bizframework-mvc>";
    public static String bizMvcReplaceStrNoExit = "\t" + bizMvcReplaceStrExist + "\r\n</bizframework-mvc>";
    
    public static String i18nExistStr = "# -- %s-START";
    public static String i18nRegexExist = "# -- %s-START(?s:.)*# -- %s-END";            
    public static String i18nReplaceStrExist = "%s";
    
    
    public static void main(String[] args) {
        try {
            OneJavaUiInfo oneJavaUiInfo = new OneJavaUiInfo();
            oneJavaUiInfo.setVmDirPath("D:/MyEclipse2014/CodeGen/src/com/uni2uni/codecreater/template");
            oneJavaUiInfo.setEntityName("User");
            //oneJavaUiInfo.setTemplateContext();
            oneJavaUiInfo.setFileName("UserService");
            oneJavaUiInfo.setFilePath("C:/Users/Administrator/Desktop/AutoCodeTest");
            oneJavaUiInfo.setRegistFileFullPath("D:/program/grean/softwareDevelop/ide/eclipse/r1studio/workspace/BizFramework4JDK1.4/src/test/java/com/chinasofti/ro/bizframework/modules/autocode/applicationContext.xml");
            oneJavaUiInfo.setPackageName("com.util.uni2uni");
            genService(oneJavaUiInfo);
            oneJavaUiInfo.setFileName("UserController");
            genController(oneJavaUiInfo);
            genRest(oneJavaUiInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

} 