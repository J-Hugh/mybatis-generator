/*
 * $Id: AutoCodeUiInfo.java,v 1.1 2012/09/11 06:38:07 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode.model;

import autocode.util.StringUtil;

/**
 * 生成代码和相关配置文件时需要的一些属性值
 * 
 * @author Rone BizFoudation Team: ganjp
 * @version 4.3
 * @since 4.3
 */
public class AutoCodeUiInfo {
    //基本信息项
    private String projectPath;
    private String moduleName; 
    private String basePackageName;
    private boolean useI18n = true;
    private String encode = "UTF-8";//字符编码
    
    //以下三项是默认的配置,也可设置值
    private String outJavaSrcBaseDirPath;
    private String outJavaTestBaseDirPath;
    private String outPageBaseDirPath;
    private String srcSpringConfigPath;
    private String testSpringConfigPath;
    private String bizMvcConfigPath;
    
    //vm模板路径
    private String poVmFilePath;
    private String daoVmFilePath;
    private String restVmFilePath;
    private String serviceVmFilePath;
    private String serviceImplVmFilePath;
    private String serviceTestVmFilePath;
    private String controllerVmFilePath;
    private String bizMvcVmFilePath;
    private String viewNewVmFilePath;
    private String viewNewJsVmFilePath;
    private String viewListVmFilePath;
    private String viewListJsVmFilePath;
    private String viewUpdateVmFilePath;
    private String viewUpdateJsVmFilePath;
    private String viewShowVmFilePath;
    private String viewShowJsVmFilePath;
    private String i18nVmFilePath;
    
    //生成后的类对应的包名
    private String poPackageName;
    private String daoPackageName;
    private String restPackageName;
    private String servicePackageName;
    private String serviceImplPackageName;
    private String serviceTestPackageName;
    private String controllerPackageName;
    
    //生成后的文件名称(包括后缀名)
    private String hbmFileFullName;
    private String poFileFullName;
    private String daoFileFullName;
    private String restFileFullName;
    private String serviceFileFullName;
    private String serviceImplFileFullName;
    private String serviceTestFileFullName;
    private String controllerFileFullName;
    private String srcServiceConfigFileFullName;
    private String testServiceConfigFileFullName;
    private String bizMvcFileFullName;
    private String viewListPageFileFullName;
    private String viewListJsFileFullName;
    private String viewNewPageFileFullName;
    private String viewNewJsFileFullName;
    private String viewUpdatePageFileFullName;
    private String viewUpdateJsFileFullName;
    private String viewShowPageFileFullName;
    private String viewShowJsFileFullName;
    private String[] i18nFileNames = {"_messages.properties","_messages_zh_CN.properties","_messages_en.properties"}; 


    //生成后的文件相对路径(不包括文件名)
    private String hbmFileRelativePath;
    private String poFileRelativePath;
    private String daoFileRelativePath;
    private String restFileRelativePath;
    private String serviceFileRelativePath;
    private String serviceImplFileRelativePath;
    private String serviceTestFileRelativePath;
    private String controllerFileRelativePath;
    private String srcServiceConfigRelativePath;
    private String testServiceConfigRelativePath;
    private String bizMvcFileRelativePath;
    //页面文件的相对路径，从WebApp目录开始，即相对BizFoundation工程的路径
    private String viewListPageRelativePath;
    private String viewListJsRelativePath;
    private String viewNewPageRelativePath;
    private String viewNewJsRelativePath;
    private String viewUpdatePageRelativePath;
    private String viewUpdateJsRelativePath;
    private String viewShowPageRelativePath;
    private String viewShowJsRelativePath;
    private String i18nFileRelativePath;
    
    public String getProjectPath() {
        return projectPath;
    }
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
    
    public String getModuleName() {
        return moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
    public String getBasePackageName() {
        return basePackageName;
    }
    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }
    
    public String getOutJavaSrcBaseDirPath() {
        if (StringUtil.isBlank(this.outJavaSrcBaseDirPath)) {
            this.outJavaSrcBaseDirPath = getDealProjectPath() + "modules/" + moduleName + "/java" ;
        }
        return outJavaSrcBaseDirPath;
    }
    public void setOutJavaSrcBaseDirPath(String outJavaSrcBaseDirPath) {
        this.outJavaSrcBaseDirPath = outJavaSrcBaseDirPath;
    }
    
    public String getOutJavaTestBaseDirPath() {
        if (StringUtil.isBlank(this.outJavaTestBaseDirPath)) {
            this.outJavaTestBaseDirPath = getDealProjectPath() + "test/" + moduleName + "/java" ;
        }
        return this.outJavaTestBaseDirPath;
    }
    public void setOutJavaTestBaseDirPath(String outJavaTestBaseDirPath) {
        this.outJavaTestBaseDirPath = outJavaTestBaseDirPath;
    }
    
    public String getOutPageBaseDirPath() {
        if (StringUtil.isBlank(this.outPageBaseDirPath)) {
            this.outPageBaseDirPath = getDealProjectPath() + "WebApp/pages";
        }
        return outPageBaseDirPath;
    }
    public void setOutPageBaseDirPath(String outPageBaseDirPath) {
        this.outPageBaseDirPath = outPageBaseDirPath;
    }
    
    public String getSrcSpringConfigPath() {
        if (StringUtil.isBlank(this.srcSpringConfigPath)) {
            this.srcSpringConfigPath = getDealProjectPath() + "modules/" + moduleName + "/resources/spring/applicationContext-" + moduleName + ".xml" ;
        }
        return srcSpringConfigPath;
    }
    public void setSrcSpringConfigPath(String srcSpringConfigPath) {
        this.srcSpringConfigPath = srcSpringConfigPath;
    }
    
    public String getTestSpringConfigPath() {
        if (StringUtil.isBlank(this.testSpringConfigPath)) {
            this.testSpringConfigPath = getDealProjectPath() + "test/" + moduleName + "/resources/spring/test-applicationContext-" + moduleName + ".xml" ;
        }
        return testSpringConfigPath;
    }
    public void setTestSpringConfigPath(String testSpringConfigPath) {
        this.testSpringConfigPath = testSpringConfigPath;
    }
    
    public String getBizMvcConfigPath() {
        if (StringUtil.isBlank(this.bizMvcConfigPath)) {
            this.bizMvcConfigPath = getDealProjectPath() + "modules/" + moduleName + "/resources/config/" + moduleName + ".mvc.xml" ;
        }
        return bizMvcConfigPath;
    }
    public void setBizMvcConfigPath(String bizMvcConfigPath) {
        this.bizMvcConfigPath = bizMvcConfigPath;
    }
    
    private String getDealProjectPath() {
        return StringUtil.isBlank(projectPath)?"":(projectPath.trim() + "/");
    }
    public String getPoVmFilePath() {
        return poVmFilePath;
    }
    public void setPoVmFilePath(String poVmFilePath) {
        this.poVmFilePath = poVmFilePath;
    }
	public String getDaoVmFilePath() {
		return daoVmFilePath;
	}
	public void setDaoVmFilePath(String daoVmFilePath) {
		this.daoVmFilePath = daoVmFilePath;
	}
	public String getRestVmFilePath() {
		return restVmFilePath;
	}
	public void setRestVmFilePath(String restVmFilePath) {
		this.restVmFilePath = restVmFilePath;
	}
	public String getServiceVmFilePath() {
        return serviceVmFilePath;
    }
    public void setServiceVmFilePath(String serviceVmFilePath) {
        this.serviceVmFilePath = serviceVmFilePath;
    }
    public String getServiceImplVmFilePath() {
        return serviceImplVmFilePath;
    }
    public void setServiceImplVmFilePath(String serviceImplVmFilePath) {
        this.serviceImplVmFilePath = serviceImplVmFilePath;
    }
    public String getServiceTestVmFilePath() {
        return serviceTestVmFilePath;
    }
    public void setServiceTestVmFilePath(String serviceTestVmFilePath) {
        this.serviceTestVmFilePath = serviceTestVmFilePath;
    }
    public String getControllerVmFilePath() {
        return controllerVmFilePath;
    }
    public void setControllerVmFilePath(String controllerVmFilePath) {
        this.controllerVmFilePath = controllerVmFilePath;
    }
    public String getHbmFileFullName() {
        return hbmFileFullName;
    }
    public void setHbmFileFullName(String hbmFileFullName) {
        this.hbmFileFullName = hbmFileFullName;
    }
    public String getPoFileFullName() {
        return poFileFullName;
    }
    public void setPoFileFullName(String poFileFullName) {
        this.poFileFullName = poFileFullName;
    }
	public String getDaoFileFullName() {
		return daoFileFullName;
	}
	public void setDaoFileFullName(String daoFileFullName) {
		this.daoFileFullName = daoFileFullName;
	}
	public String getRestFileFullName() {
		return restFileFullName;
	}
	public void setRestFileFullName(String restFileFullName) {
		this.restFileFullName = restFileFullName;
	}
	public String getServiceFileFullName() {
        return serviceFileFullName;
    }
    public void setServiceFileFullName(String serviceFileFullName) {
        this.serviceFileFullName = serviceFileFullName;
    }
    public String getServiceImplFileFullName() {
        return serviceImplFileFullName;
    }
    public void setServiceImplFileFullName(String serviceImplFileFullName) {
        this.serviceImplFileFullName = serviceImplFileFullName;
    }
    public String getServiceTestFileFullName() {
        return serviceTestFileFullName;
    }
    public void setServiceTestFileFullName(String serviceTestFileFullName) {
        this.serviceTestFileFullName = serviceTestFileFullName;
    }
    public String getControllerFileFullName() {
        return controllerFileFullName;
    }
    public void setControllerFileFullName(String controllerFileFullName) {
        this.controllerFileFullName = controllerFileFullName;
    }
    public String getSrcServiceConfigFileFullName() {
        return srcServiceConfigFileFullName;
    }
    public void setSrcServiceConfigFileFullName(String srcServiceConfigFileFullName) {
        this.srcServiceConfigFileFullName = srcServiceConfigFileFullName;
    }
    public String getTestServiceConfigFileFullName() {
        return testServiceConfigFileFullName;
    }
    public void setTestServiceConfigFileFullName(
            String testServiceConfigFileFullName) {
        this.testServiceConfigFileFullName = testServiceConfigFileFullName;
    }
    public String getBizMvcFileFullName() {
        return bizMvcFileFullName;
    }
    public void setBizMvcFileFullName(String bizMvcFileFullName) {
        this.bizMvcFileFullName = bizMvcFileFullName;
    }
    public String getHbmFileRelativePath() {
        return hbmFileRelativePath;
    }
    public void setHbmFileRelativePath(String hbmFileRelativePath) {
        this.hbmFileRelativePath = hbmFileRelativePath;
    }
    public String getPoFileRelativePath() {
        return poFileRelativePath;
    }
    public void setPoFileRelativePath(String poFileRelativePath) {
        this.poFileRelativePath = poFileRelativePath;
    }
	public String getDaoFileRelativePath() {
		return daoFileRelativePath;
	}
	public void setDaoFileRelativePath(String daoFileRelativePath) {
		this.daoFileRelativePath = daoFileRelativePath;
	}
	public String getRestFileRelativePath() {
		return restFileRelativePath;
	}
	public void setRestFileRelativePath(String restFileRelativePath) {
		this.restFileRelativePath = restFileRelativePath;
	}
	public String getServiceFileRelativePath() {
        return serviceFileRelativePath;
    }
    public void setServiceFileRelativePath(String serviceFileRelativePath) {
        this.serviceFileRelativePath = serviceFileRelativePath;
    }
    public String getServiceImplFileRelativePath() {
        return serviceImplFileRelativePath;
    }
    public void setServiceImplFileRelativePath(String serviceImplFileRelativePath) {
        this.serviceImplFileRelativePath = serviceImplFileRelativePath;
    }
    public String getServiceTestFileRelativePath() {
        return serviceTestFileRelativePath;
    }
    public void setServiceTestFileRelativePath(String serviceTestFileRelativePath) {
        this.serviceTestFileRelativePath = serviceTestFileRelativePath;
    }
    public String getControllerFileRelativePath() {
        return controllerFileRelativePath;
    }
    public void setControllerFileRelativePath(String controllerFileRelativePath) {
        this.controllerFileRelativePath = controllerFileRelativePath;
    }
    public String getSrcServiceConfigRelativePath() {
        return srcServiceConfigRelativePath;
    }
    public void setSrcServiceConfigRelativePath(String srcServiceConfigRelativePath) {
        this.srcServiceConfigRelativePath = srcServiceConfigRelativePath;
    }
    public String getTestServiceConfigRelativePath() {
        return testServiceConfigRelativePath;
    }
    public void setTestServiceConfigRelativePath(
            String testServiceConfigRelativePath) {
        this.testServiceConfigRelativePath = testServiceConfigRelativePath;
    }
    public String getBizMvcFileRelativePath() {
        return bizMvcFileRelativePath;
    }
    public void setBizMvcFileRelativePath(String bizMvcFileRelativePath) {
        this.bizMvcFileRelativePath = bizMvcFileRelativePath;
    }
    public String getPoPackageName() {
        return poPackageName;
    }
    public void setPoPackageName(String poPackageName) {
        this.poPackageName = poPackageName;
    }
	public String getDaoPackageName() {
		return daoPackageName;
	}
	public void setDaoPackageName(String daoPackageName) {
		this.daoPackageName = daoPackageName;
	}
	public String getRestPackageName() {
		return restPackageName;
	}
	public void setRestPackageName(String restPackageName) {
		this.restPackageName = restPackageName;
	}
	public String getServicePackageName() {
        return servicePackageName;
    }
    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }
    public String getServiceImplPackageName() {
        return serviceImplPackageName;
    }
    public void setServiceImplPackageName(String serviceImplPackageName) {
        this.serviceImplPackageName = serviceImplPackageName;
    }
    public String getServiceTestPackageName() {
        return serviceTestPackageName;
    }
    public void setServiceTestPackageName(String serviceTestPackageName) {
        this.serviceTestPackageName = serviceTestPackageName;
    }
    public String getControllerPackageName() {
        return controllerPackageName;
    }
    public void setControllerPackageName(String controllerPackageName) {
        this.controllerPackageName = controllerPackageName;
    }
    public String getBizMvcVmFilePath() {
        return bizMvcVmFilePath;
    }
    public void setBizMvcVmFilePath(String bizMvcVmFilePath) {
        this.bizMvcVmFilePath = bizMvcVmFilePath;
    }
    
    public String getViewNewVmFilePath() {
        return viewNewVmFilePath;
    }
    public void setViewNewVmFilePath(String viewNewVmFilePath) {
        this.viewNewVmFilePath = viewNewVmFilePath;
    }
    public String getViewListVmFilePath() {
        return viewListVmFilePath;
    }
    public void setViewListVmFilePath(String viewListVmFilePath) {
        this.viewListVmFilePath = viewListVmFilePath;
    }
    public String getViewUpdateVmFilePath() {
        return viewUpdateVmFilePath;
    }
    public void setViewUpdateVmFilePath(String viewUpdateVmFilePath) {
        this.viewUpdateVmFilePath = viewUpdateVmFilePath;
    }
    public String getViewShowVmFilePath() {
        return viewShowVmFilePath;
    }
    public void setViewShowVmFilePath(String viewShowVmFilePath) {
        this.viewShowVmFilePath = viewShowVmFilePath;
    }
    public String getViewListPageFileFullName() {
        return viewListPageFileFullName;
    }
    public void setViewListPageFileFullName(String viewListPageFileFullName) {
        this.viewListPageFileFullName = viewListPageFileFullName;
    }
    public String getViewNewPageFileFullName() {
        return viewNewPageFileFullName;
    }
    public void setViewNewPageFileFullName(String viewNewPageFileFullName) {
        this.viewNewPageFileFullName = viewNewPageFileFullName;
    }
    public String getViewUpdatePageFileFullName() {
        return viewUpdatePageFileFullName;
    }
    public void setViewUpdatePageFileFullName(String viewUpdatePageFileFullName) {
        this.viewUpdatePageFileFullName = viewUpdatePageFileFullName;
    }
    public String getViewShowPageFileFullName() {
        return viewShowPageFileFullName;
    }
    public void setViewShowPageFileFullName(String viewShowPageFileFullName) {
        this.viewShowPageFileFullName = viewShowPageFileFullName;
    }
    public String getViewListPageRelativePath() {
        return viewListPageRelativePath;
    }
    public void setViewListPageRelativePath(String viewListPageRelativePath) {
        this.viewListPageRelativePath = viewListPageRelativePath;
    }
    public String getViewNewPageRelativePath() {
        return viewNewPageRelativePath;
    }
    public void setViewNewPageRelativePath(String viewNewPageRelativePath) {
        this.viewNewPageRelativePath = viewNewPageRelativePath;
    }
    public String getViewUpdatePageRelativePath() {
        return viewUpdatePageRelativePath;
    }
    public void setViewUpdatePageRelativePath(String viewUpdatePageRelativePath) {
        this.viewUpdatePageRelativePath = viewUpdatePageRelativePath;
    }
    public String getViewShowPageRelativePath() {
        return viewShowPageRelativePath;
    }
    public void setViewShowPageRelativePath(String viewShowPageRelativePath) {
        this.viewShowPageRelativePath = viewShowPageRelativePath;
    }
    public String getI18nVmFilePath() {
        return i18nVmFilePath;
    }
    public void setI18nVmFilePath(String vmFilePath) {
        i18nVmFilePath = vmFilePath;
    }
    public String[] getI18nFileNames() {
        return i18nFileNames;
    }
    public void setI18nFileNames(String[] fileNames) {
        i18nFileNames = fileNames;
    }
    public String getI18nFileRelativePath() {
        return i18nFileRelativePath;
    }
    public void setI18nFileRelativePath(String fileRelativePath) {
        i18nFileRelativePath = fileRelativePath;
    }
    public boolean isUseI18n() {
        return useI18n;
    }
    public void setUseI18n(boolean useI18n) {
        this.useI18n = useI18n;
    }
    public String getViewListJsFileFullName() {
        return viewListJsFileFullName;
    }
    public void setViewListJsFileFullName(String viewListJsFileFullName) {
        this.viewListJsFileFullName = viewListJsFileFullName;
    }
    public String getViewNewJsFileFullName() {
        return viewNewJsFileFullName;
    }
    public void setViewNewJsFileFullName(String viewNewJsFileFullName) {
        this.viewNewJsFileFullName = viewNewJsFileFullName;
    }
    public String getViewUpdateJsFileFullName() {
        return viewUpdateJsFileFullName;
    }
    public void setViewUpdateJsFileFullName(String viewUpdateJsFileFullName) {
        this.viewUpdateJsFileFullName = viewUpdateJsFileFullName;
    }
    public String getViewShowJsFileFullName() {
        return viewShowJsFileFullName;
    }
    public void setViewShowJsFileFullName(String viewShowJsFileFullName) {
        this.viewShowJsFileFullName = viewShowJsFileFullName;
    }
    public String getViewListJsRelativePath() {
        return viewListJsRelativePath;
    }
    public void setViewListJsRelativePath(String viewListJsRelativePath) {
        this.viewListJsRelativePath = viewListJsRelativePath;
    }
    public String getViewNewJsRelativePath() {
        return viewNewJsRelativePath;
    }
    public void setViewNewJsRelativePath(String viewNewJsRelativePath) {
        this.viewNewJsRelativePath = viewNewJsRelativePath;
    }
    public String getViewUpdateJsRelativePath() {
        return viewUpdateJsRelativePath;
    }
    public void setViewUpdateJsRelativePath(String viewUpdateJsRelativePath) {
        this.viewUpdateJsRelativePath = viewUpdateJsRelativePath;
    }
    public String getViewShowJsRelativePath() {
        return viewShowJsRelativePath;
    }
    public void setViewShowJsRelativePath(String viewShowJsRelativePath) {
        this.viewShowJsRelativePath = viewShowJsRelativePath;
    }
    public String getViewNewJsVmFilePath() {
        return viewNewJsVmFilePath;
    }
    public void setViewNewJsVmFilePath(String viewNewJsVmFilePath) {
        this.viewNewJsVmFilePath = viewNewJsVmFilePath;
    }
    public String getViewListJsVmFilePath() {
        return viewListJsVmFilePath;
    }
    public void setViewListJsVmFilePath(String viewListJsVmFilePath) {
        this.viewListJsVmFilePath = viewListJsVmFilePath;
    }
    public String getViewUpdateJsVmFilePath() {
        return viewUpdateJsVmFilePath;
    }
    public void setViewUpdateJsVmFilePath(String viewUpdateJsVmFilePath) {
        this.viewUpdateJsVmFilePath = viewUpdateJsVmFilePath;
    }
    public String getViewShowJsVmFilePath() {
        return viewShowJsVmFilePath;
    }
    public void setViewShowJsVmFilePath(String viewShowJsVmFilePath) {
        this.viewShowJsVmFilePath = viewShowJsVmFilePath;
    }

	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		if(StringUtil.isBlank(encode)){
			encode = "UTF-8";
		}
		this.encode = encode;
	}    
} 