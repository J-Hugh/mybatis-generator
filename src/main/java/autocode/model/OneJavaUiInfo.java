/*
 * $Id: OneJavaUiInfo.java,v 1.1 2012/09/11 06:38:06 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode.model;

/**
 * 单步生成代码时使用的ui信息
 * 
 * @author Rone BizFoudation Team: ganjp
 * @version 4.3
 * @since 4.3
 */
public class OneJavaUiInfo extends OneFileUiInfo {
    //java类的注册文件完整路径
    private String registFileFullPath;
    //java文件对应的包名
    private String packageName;
    //对应的实体名称
    private String entityName;
    
    // 用于单步生成Controller时，自动生成mvc.xml信息
    private String templateContext;
    
    public String getTemplateContext() {
		return templateContext;
	}

	public void setTemplateContext(String templateContext) {
		this.templateContext = templateContext;
	}

	public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getRegistFileFullPath() {
        return registFileFullPath;
    }

    public void setRegistFileFullPath(String registFileFullPath) {
        this.registFileFullPath = registFileFullPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
} 