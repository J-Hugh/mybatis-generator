/**
 * $Id: IPField.java,v 1.2 2013/11/16 04:57:51 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

import java.util.Collection;

/**
 * @Title: IPField.java
 * @Description: <br>持久化实体属性的模型接口
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:25:16
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface IPField extends IField{
    
    /**
     *获取字段数据库列名
     * @return
     */
    public String getColumnName();
    /**
     * 设置字段数据库列名
     * @param columnName
     */
    public void setColumnName(String columnName);
    /**
     * 获得字段数据库列类型
     * @return
     */
    public String getColumnType();
    /**
     * 设置字段数据库列类型
     * @param columnType
     */
    public void setColumnType(String columnType);
    /**
     * 获取是否持久化
     * @return
     */
    public boolean isPersistent();
    /**
     * 设置是否持久化
     * @param persistent
     */
    public void setPersistent(boolean persistent);
    /**
     * 获取是否为外键
     * @return
     */
    public boolean isFK();
    /**
     * 设置是否为外键
     * @param fk
     */
    public void setFK(boolean fk);
    /**
     * 设置字段长度
     * @return
     */
    public String getLength();
    /**
     * 设置字段长度
     * @param length
     */
    public void setLength(String length);
    /**
     * 获取字段精度
     * @return
     */
    public String getPrecision();
    /**
     * 设置字段精度
     * @param precision
     */
    public void setPrecision(String precision);
    /**
     * 获取是否允许为空
     * 	true: 表示 not null
     *  false：表示可以为 null
     * @return
     */
    public boolean isNullAble();
    /**
     * 设置是否允许为空
     * 	true: 表示 not null
     *  false：表示可以为 null
     * @param nullAble
     */
    public void setNullAble(boolean nullAble);
    /**
     * 获取关联数据集
     * @return
     */
    public Collection getRefDatasets(); 
}
