/*
 * $Id: VmVarPoPkInfo.java,v 1.2 2013/01/16 09:28:53 zhanghp Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode.util.auxiliary.vm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import autocode.util.StringUtil;

/**
 * <p>供vm文件(生成po的主键类)使用的变量值对象信息</p>
 * 
 * @author Rone BizFoudation Team: ganjp
 * @version 4.3
 * @since 4.3
 */
public class VmVarPoPkInfo {
    public static final String PACKAGE_NAME = "PACKAGE_NAME"; //包名
    public static final String PO_PK_FIRST_UPPER_NAME = "PO_PK_FIRST_UPPER_NAME";     //第一个字母大写的po主键名称
    public static final String PO_PK_FIRST_LOWER_NAME = "PO_PK_FIRST_LOWER_NAME";     //第一个字母小写的po主键名称
    public static final String PO_PK_FIELD_NAMES = "PO_PK_FIELD_NAMES";                 //po主键的所有字段集
    public static final String PO_PK_FIELD_FIRST_UPPER_NAMES = "PO_PK_FIELD_FIRST_UPPER_NAMES";      //第一个字母大写po主键的所有字段集 
    public static final String PO_PK_FIELD_TYPES = "PO_PK_FIELD_TYPES";                //po主键的所有字段类型集
    public static final String PO_PK_FIELD_HIBERNATE_SIMPLE_TYPES = "PO_PK_FIELD_HIBERNATE_SIMPLE_TYPES"; //po主键的所有字段hibernate类型集合
    public static final String PO_PK_FIELD_ROBASE_SIMPLE_TYPES = "PO_PK_FIELD_ROBASE_SIMPLE_TYPES";    //po主键的所有字段robase类型集合
    public static final String VM_VAR_PO_PK_INFO = "VmVarPoPkInfo";
    
    private String packageName = "";            //包名
    private String poPkFirstUpperName = "";     //第一个字母大写的po主键名称
    private String poPkFirstLowerName = "";     //第一个字母小写的po主键名称
    private List poPkFiedNames = Collections.EMPTY_LIST;                 //po主键的所有字段集
    private List poPkFieldFirstUpperNames = Collections.EMPTY_LIST;      //第一个字母大写po主键的所有字段集 
    private List poPkFieldTypes = Collections.EMPTY_LIST;                //po主键的所有字段类型集
    private List poPkFieldHibernateSimpleTypes = Collections.EMPTY_LIST; //po主键的所有字段hibernate类型集合
    private List poPkFieldRobaseSimpleTypes = Collections.EMPTY_LIST;    //po主键的所有字段robase类型集合
    
    private List poPkFieldGetters = Collections.EMPTY_LIST;
    private List poPkFieldSetters = Collections.EMPTY_LIST;
    
    public VmVarPoPkInfo (String poPkFirstUpperName,String poPkFirstLowerName) {
        this.poPkFirstUpperName = poPkFirstUpperName;
        this.poPkFirstLowerName = poPkFirstLowerName;
    }
    
    public String getPoPkFirstUpperName() {
        return poPkFirstUpperName;
    }
    public void setPoPkFirstUpperName(String poPkFirstUpperName) {
        this.poPkFirstUpperName = poPkFirstUpperName;
    }
    public String getPoPkFirstLowerName() {
        return poPkFirstLowerName;
    }
    public void setPoPkFirstLowerName(String poPkFirstLowerName) {
        this.poPkFirstLowerName = poPkFirstLowerName;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public List getPoPkFiedNames() {
        return poPkFiedNames;
    }
    public void setPoPkFiedNames(List poPkFiedNames) {
        this.poPkFiedNames = poPkFiedNames;
    }
    public void addPoPkFiedName(String poPkFiedName) {
        if (this.poPkFiedNames==null || this.poPkFiedNames.isEmpty()) {
            this.poPkFiedNames = new ArrayList();
        }
        this.poPkFiedNames.add(poPkFiedName);
    }
    public List getPoPkFieldFirstUpperNames() {
        return poPkFieldFirstUpperNames;
    }
    public void setPoPkFieldFirstUpperNames(List poPkFieldFirstUpperNames) {
        this.poPkFieldFirstUpperNames = poPkFieldFirstUpperNames;
    }
    public void addPoPkFieldFirstUpperName(String poPkFieldFirstUpperName) {
        if (this.poPkFieldFirstUpperNames==null || this.poPkFieldFirstUpperNames.isEmpty()) {
            this.poPkFieldFirstUpperNames = new ArrayList();
        }
        this.poPkFieldFirstUpperNames.add(poPkFieldFirstUpperName);
    }
    public List getPoPkFieldTypes() {
        return poPkFieldTypes;
    }
    public void setPoPkFieldTypes(List poPkFieldTypes) {
        this.poPkFieldTypes = poPkFieldTypes;
    }
    public void addPoPkFieldType(String poPkFieldType) {
        if (this.poPkFieldTypes==null || this.poPkFieldTypes.isEmpty()) {
            this.poPkFieldTypes = new ArrayList();
        }
        this.poPkFieldTypes.add(poPkFieldType);
    }
    public List getPoPkFieldHibernateSimpleTypes() {
        return poPkFieldHibernateSimpleTypes;
    }
    public void setPoPkFieldHibernateSimpleTypes(List poPkFieldHibernateSimpleTypes) {
        this.poPkFieldHibernateSimpleTypes = poPkFieldHibernateSimpleTypes;
    }
    public void addPoPkFieldHibernateSimpleType(String poPkFieldHibernateSimpleType) {
        if (this.poPkFieldHibernateSimpleTypes==null || this.poPkFieldHibernateSimpleTypes.isEmpty()) {
            this.poPkFieldHibernateSimpleTypes = new ArrayList();
        }
        this.poPkFieldHibernateSimpleTypes.add(poPkFieldHibernateSimpleType);
    }
    public List getPoPkFieldRobaseSimpleTypes() {
        return poPkFieldRobaseSimpleTypes;
    }
    public void setPoPkFieldRobaseSimpleTypes(List poPkFieldRobaseSimpleTypes) {
        this.poPkFieldRobaseSimpleTypes = poPkFieldRobaseSimpleTypes;
    }
    public void addPoPkFieldRobaseSimpleType(String poPkFieldRobaseSimpleType) {
        if (this.poPkFieldRobaseSimpleTypes==null || this.poPkFieldRobaseSimpleTypes.isEmpty()) {
            this.poPkFieldRobaseSimpleTypes = new ArrayList();
        }
        this.poPkFieldRobaseSimpleTypes.add(poPkFieldRobaseSimpleType);
    }
    public String getJavaBeanFieldName(String fieldName){
    	return StringUtil.getJavaBeanFieldName(fieldName);
    }
} 