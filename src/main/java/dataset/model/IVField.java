/**
 * $Id: IVField.java,v 1.2 2013/11/16 04:57:51 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

/**
 * @Title: IVField.java
 * @Description: <br>查询实体属性接口
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:19:25
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface IVField extends IField{
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
     * 获取字段别名
     * @return
     */
	public String getAlias();
	/**
	 * 设置字段别名
	 * @param alias
	 */
	public void setAlias(String alias);
	/**
	 * 获取字段引用的持久化实体ID（适用于查询实体字段通过持久化实体定义的场景）
	 * @return
	 */
	public String getRefEntityId();
	/**
	 * 设置字段引用的持久化实体ID（适用于查询实体字段通过持久化实体定义的场景）
	 * @param refEntityId
	 */
	public void setRefEntityId(String refEntityId);
	/**
	 * 获取字段引用的持久化实体字段的ID（适用于查询实体字段通过持久化实体定义的场景）
	 * @return
	 */
	public String getRefFieldId();
	/**
	 * 设置字段引用的持久化实体字段的ID（适用于查询实体字段通过持久化实体定义的场景）
	 * @param refFieldId
	 */
	public void setRefFieldId(String refFieldId);
	/**
	 * 获取引用的持久化实体
	 * @return
	 */
	public IEntity getRefEntity();
	/**
	 * 获取引用的持久化实体字段
	 * @return
	 */
	public IField getRefField();
	/**
	 * 获取数据集
	 * @return
	 */
	public String getRefDataset();
	/**
	 * 设置依赖数据集文件
	 * @param refDataset
	 */
	public void setRefDataset(String refDataset); 
}
