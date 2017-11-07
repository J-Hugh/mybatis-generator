/*
 * $Id: OneJavaInfo.java,v 1.1 2012/09/11 06:38:06 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode.model;

/**
 * 单步生成代码时使用的ui信息
 * 
 * @author BizFoudation Team: ganjp
 * @version 5.0
 * @since 5.0
 */
public class OneJavaInfo extends OneFileInfo {
    //applicationContext的完整路径
    private String registFileFullPath;
    //java文件对应的包名
    private String packageName;
    //对应的实体名称
    private String entityName;
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