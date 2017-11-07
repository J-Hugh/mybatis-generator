/*
 * $Id: AutoCodeInfo.java,v 1.1 2012/09/11 06:38:07 chenhua Exp $
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
public class AutoCodeInfo {
    
	
    private boolean useI18n;//是否使用国际化
    private String encode = "UTF-8";//字符编码
    private String moduleName; 
    private String basePackageName;
    
    //vm模板路径
    private String poVmFilePath;
    private String daoVmFilePath;
    private String restVmFilePath;
    private String serviceVmFilePath;
    private String serviceImplVmFilePath;
    private String serviceTestVmFilePath;
    private String controllerVmFilePath;
    private String bizMvcVmFilePath;
    private String viewListVmFilePath;
    private String viewListJsVmFilePath;
    private String viewNewVmFilePath;
    private String viewNewJsVmFilePath;
    private String viewUpdateVmFilePath;
    private String viewUpdateJsVmFilePath;
    private String viewShowVmFilePath;
    private String viewShowJsVmFilePath;
    private String i18nVmFilePath;
    
    //java包名
    private String poPackageName;
    private String daoPackageName;
    private String restPackageName;
    private String servicePackageName;
    private String serviceImplPackageName;
    private String serviceTestPackageName;
    private String controllerPackageName;
    
    //生成后的java文件名称
    private String poName;
    private String daoName;
    private String restName;
    private String serviceName;
    private String serviceImplName;
    private String serviceTestName;
    private String controllerName;
    
    //生成后的文件路径(包括文件名称和文件类型)
    private String poPath;
    private String daoPath;
    private String hbmPath;
    private String restPath;
    private String servicePath;
    private String serviceImplPath;
    private String serviceTestPath;
    private String controllerPath;
    
    //配置文件对应的路径(包括文件名称和文件类型)
    private String srcSpringConfigPath;
    private String testSpringConfigPath;
    private String bizmvcConfigPath;
    private String i18nConfigRelativePath;
    private String[] i18nFileNames; 
    
    //页面文件对应的路径
    private String viewListFilePath;
    private String viewListJsPath;
    private String viewNewFilePath;
    private String viewNewJsPath;
    private String viewUpdateFilePath;
    private String viewUpdateJsPath;
    private String viewShowFilePath;
    private String viewShowJsPath;
    
    
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
    public String getSrcSpringConfigPath() {
        return srcSpringConfigPath;
    }
    public void setSrcSpringConfigPath(String srcSpringConfigPath) {
        this.srcSpringConfigPath = srcSpringConfigPath;
    }
    public String getTestSpringConfigPath() {
        return testSpringConfigPath;
    }
    public void setTestSpringConfigPath(String testSpringConfigPath) {
        this.testSpringConfigPath = testSpringConfigPath;
    }
    public String getPoVmFilePath() {
        return poVmFilePath;
    }
    public void setPoVmFilePath(String poVmFilePath) {
        this.poVmFilePath = poVmFilePath;
    }
	public String getRestVmFilePath() {
		return restVmFilePath;
	}
	public void setRestVmFilePath(String restVmFilePath) {
		this.restVmFilePath = restVmFilePath;
	}
	public String getDaoVmFilePath() {
		return daoVmFilePath;
	}
	public void setDaoVmFilePath(String daoVmFilePath) {
		this.daoVmFilePath = daoVmFilePath;
	}
	public String getControllerVmFilePath() {
        return controllerVmFilePath;
    }
    public void setControllerVmFilePath(String controllerVmFilePath) {
        this.controllerVmFilePath = controllerVmFilePath;
    }
    public String getBizMvcVmFilePath() {
        
        return bizMvcVmFilePath;
    }
    public void setBizMvcVmFilePath(String bizMvcVmFilePath) {
        this.bizMvcVmFilePath = bizMvcVmFilePath;
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
    public String getPoName() {
        return poName;
    }
    public void setPoName(String poName) {
        this.poName = poName;
    }
	public String getDaoName() {
		return daoName;
	}
	public void setDaoName(String daoName) {
		this.daoName = daoName;
	}
	public String getRestName() {
		return restName;
	}
	public void setRestName(String restName) {
		this.restName = restName;
	}
	public String getServiceName() {
        return serviceName;
    }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    public String getServiceImplName() {
        return serviceImplName;
    }
    public void setServiceImplName(String serviceImplName) {
        this.serviceImplName = serviceImplName;
    }
    public String getServiceTestName() {
        return serviceTestName;
    }
    public void setServiceTestName(String serviceTestName) {
        this.serviceTestName = serviceTestName;
    }
    public String getControllerName() {
        return controllerName;
    }
    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }
    public String getPoPath() {
        return poPath;
    }
    public void setPoPath(String poPath) {
        this.poPath = poPath;
    }
	public String getDaoPath() {
		return daoPath;
	}
	public void setDaoPath(String daoPath) {
		this.daoPath = daoPath;
	}
	public String getServicePath() {
        return servicePath;
    }
    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }
    public String getServiceImplPath() {
        return serviceImplPath;
    }
    public void setServiceImplPath(String serviceImplPath) {
        this.serviceImplPath = serviceImplPath;
    }
    public String getServiceTestPath() {
        return serviceTestPath;
    }
    public void setServiceTestPath(String serviceTestPath) {
        this.serviceTestPath = serviceTestPath;
    }
    public String getControllerPath() {
        return controllerPath;
    }
    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }
    public String getHbmPath() {
        return hbmPath;
    }
    public void setHbmPath(String hbmPath) {
        this.hbmPath = hbmPath;
    }
	public String getRestPath() {
		return restPath;
	}
	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}
	public String getBizmvcConfigPath() {
        return bizmvcConfigPath;
    }
    public void setBizmvcConfigPath(String bizmvcConfigPath) {
        this.bizmvcConfigPath = bizmvcConfigPath;
    }
    public String getViewNewVmFilePath() {
        return viewNewVmFilePath;
    }
    public void setViewNewVmFilePath(String viewNewVmFilePath) {
        this.viewNewVmFilePath = viewNewVmFilePath;
    }
    public String getViewNewFilePath() {
        return viewNewFilePath;
    }
    public void setViewNewFilePath(String viewNewFilePath) {
        this.viewNewFilePath = viewNewFilePath;
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
    public String getViewListFilePath() {
        return viewListFilePath;
    }
    public void setViewListFilePath(String viewListFilePath) {
        this.viewListFilePath = viewListFilePath;
    }
    public String getViewUpdateFilePath() {
        return viewUpdateFilePath;
    }
    public void setViewUpdateFilePath(String viewUpdateFilePath) {
        this.viewUpdateFilePath = viewUpdateFilePath;
    }
    public String getViewShowFilePath() {
        return viewShowFilePath;
    }
    public void setViewShowFilePath(String viewShowFilePath) {
        this.viewShowFilePath = viewShowFilePath;
    }
    public String getI18nVmFilePath() {
        return i18nVmFilePath;
    }
    public void setI18nVmFilePath(String vmFilePath) {
        i18nVmFilePath = vmFilePath;
    }
    public String getI18nConfigRelativePath() {
        return i18nConfigRelativePath;
    }
    public void setI18nConfigRelativePath(String configRelativePath) {
        i18nConfigRelativePath = configRelativePath;
    }
    public String[] getI18nFileNames() {
        return i18nFileNames;
    }
    public void setI18nFileNames(String[] fileNames) {
        i18nFileNames = fileNames;
    }
    public boolean isUseI18n() {
        return useI18n;
    }
    public void setUseI18n(boolean useI18n) {
        this.useI18n = useI18n;
    }
    public String getViewListJsPath() {
        return viewListJsPath;
    }
    public void setViewListJsPath(String viewListJsPath) {
        this.viewListJsPath = viewListJsPath;
    }
    public String getViewNewJsPath() {
        return viewNewJsPath;
    }
    public void setViewNewJsPath(String viewNewJsPath) {
        this.viewNewJsPath = viewNewJsPath;
    }
    public String getViewUpdateJsPath() {
        return viewUpdateJsPath;
    }
    public void setViewUpdateJsPath(String viewUpdateJsPath) {
        this.viewUpdateJsPath = viewUpdateJsPath;
    }
    public String getViewShowJsPath() {
        return viewShowJsPath;
    }
    public void setViewShowJsPath(String viewShowJsPath) {
        this.viewShowJsPath = viewShowJsPath;
    }
    public String getViewListJsVmFilePath() {
        return viewListJsVmFilePath;
    }
    public void setViewListJsVmFilePath(String viewListJsVmFilePath) {
        this.viewListJsVmFilePath = viewListJsVmFilePath;
    }
    public String getViewNewJsVmFilePath() {
        return viewNewJsVmFilePath;
    }
    public void setViewNewJsVmFilePath(String viewNewJsVmFilePath) {
        this.viewNewJsVmFilePath = viewNewJsVmFilePath;
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
} 