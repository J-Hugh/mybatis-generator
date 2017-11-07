/*
 * $Id: ConfigInfo.java,v 1.1 2012/09/11 06:38:05 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode.model;

import java.util.List;

/**
 * 
 * @author Rone BizFoudation Team: ganjp
 *
 * 生成数据库需要输入的属性集，包括连接的数据库信息，hbm文件等等
 *
 * @version 4.3
 * @since 4.3
 */
public class ConfigInfo extends DatabaseInfo {
    
    private String[] hbmFilePathArr; //需要生成数据库信息的hbm文件路径(包括文件名称)数组
    private List hbmFileList; //需要生成数据库信息的hbm文件集合
    private String outFile; //生成数据库的脚本文件完整路径包括名称
    private boolean printDDLToConsole = true; //True就是把DDL语句输出到控制台
    private boolean hbmToDatabase = true; //True就是根据持久类和映射文件在数据库中先执行删除再执行创建操作
    private boolean justDrop = false;  //false表示不是仅仅执行Drop语句还执行创建操作
    private boolean justCreate = false; //true表示仅仅是创建
    private boolean isOverride = true; //false为不覆盖，true为覆盖
    
    /**
     * 获取需要生成数据库信息的hbm文件路径数组
     * 
     * @return eg：["model/user.hbm.xml","model/org.hbm.xml"]
     */
    public String[] getHbmFilePathArr() {
        return hbmFilePathArr;
    }

    /**
     * 设置需要生成数据库信息的hbm文件路径数组
     * 
     * @param hbmFilePathArr eg：["model/user.hbm.xml","model/org.hbm.xml"]
     */
    public void setHbmFilePathArr(String[] hbmFilePathArr) {
        this.hbmFilePathArr = hbmFilePathArr;
    }

    /**
     * 获得生成脚本的文件完整路径（包括文件名称）
     * @return
     */
    public String getOutFile() {
        return outFile;
    }

    /**
     * 设置生成脚本的文件完整路径（包括文件名称），如："db/script/testScript.sql"
     * @param scriptFilePathAndName
     */
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    /**
     * 是否把DDL语句输出到控制台
     * 
     * @return
     */
    public boolean isPrintDDLToConsole() {
        return printDDLToConsole;
    }

    /**
     * 设置是否把DDL语句输出到控制台
     * 
     * @param printDDLToConsole
     */
    public void setPrintDDLToConsole(boolean printDDLToConsole) {
        this.printDDLToConsole = printDDLToConsole;
    }

    /**
     * 是否根据持久类和映射文件在数据库中先执行删除再执行创建操作
     * @return
     */
    public boolean isHbmToDatabase() {
        return hbmToDatabase;
    }

    /**
     * 设置是否根据映射文件在数据库中先执行删除再执行创建操作
     * 
     * @param hbmToDatabase
     */
    public void setHbmToDatabase(boolean hbmToDatabase) {
        this.hbmToDatabase = hbmToDatabase;
    }

    /**
     * 是否仅仅执行Drop语句而不执行创建操作
     * @return
     */
    public boolean isJustDrop() {
        return justDrop;
    }

    /**
     * 设置是否仅仅执行Drop语句而不执行创建操作
     * 
     * @param justDrop
     */
    public void setJustDrop(boolean justDrop) {
        this.justDrop = justDrop;
    }

    /**
     * 是否仅仅是创建
     * @return
     */
    public boolean isJustCreate() {
        return justCreate;
    }

    /**
     * 设置是否仅仅是创建
     * @param justCreate
     */
    public void setJustCreate(boolean justCreate) {
        this.justCreate = justCreate;
    }

    /**
     * 获取Hbm文件集合
     * @return
     */
    public List getHbmFileList() {
        return hbmFileList;
    }

    /**
     * 设置Hbm文件集
     * 
     * @param hbmFileList
     */
    public void setHbmFileList(List hbmFileList) {
        this.hbmFileList = hbmFileList;
    }

    public boolean isOverride() {
        return isOverride;
    }

    public void setOverride(boolean isOverride) {
        this.setJustCreate(!isOverride);
        this.isOverride = isOverride;
    }

} 