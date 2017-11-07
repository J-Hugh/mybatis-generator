/*
 * $Id: OneFileUiInfo.java,v 1.1 2012/09/11 06:38:07 chenhua Exp $
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
public class OneFileUiInfo {
    //生成文件的名称
    private String fileName;
    //生成文件的路径(不包括文件名和文件类型)
    private String filePath;
    //生成文件的路径(包括文件名和文件类型)
    private String vmDirPath;
    
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getVmDirPath() {
        return vmDirPath;
    }
    public void setVmDirPath(String vmDirPath) {
        this.vmDirPath = vmDirPath;
    }
} 