/**
 * $Id: IVEntity.java,v 1.3 2013/11/16 04:57:51 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

import java.util.Collection;
import java.util.Map;

import dataset.refmodel.IRefDataset;

/**
 * 查询实体接口定义
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IVEntity extends IEntity{
    
    /**
     * 获取查询实体定义类型
     *  SQL_OR_VIEW：通过数据库视图或者SQL语句定义
     *  Entity:通过持久化实体定义（目前不支持）
     * @return
     */
    public String getType();
    /**
     * 设置查询实体定义类型
     *  SQL_OR_VIEW：通过数据库视图或者SQL语句定义
     *  Entity:通过持久化实体定义（目前不支持）
     * @param type
     */
    public void setType(String type);
    
    /**
     * 获取视图（包含数据库视图和SQL语句）定义
     * @return
     */
    public Collection<IView> getViews();
    /**
     * 设置视图（包含数据库视图和SQL语句）定义
     * @param views
     */
    public void setViews(Collection<IView> views);
    
    /**
     * 获取查询实体引用的持久化实体集合
     * @return
     */
    public Collection getRefEntities();
    
	/**
	 * 获取查询实体引用的持久化实体ID（实体ID之间用 ',' 分隔）
	 * @return
	 */
	public String getRefEntityIds();
	
	/**
	 * 设置查询实体引用的持久化实体ID（实体ID之间用 ',' 分隔）
	 * @param refEntityIds
	 */
	public void setRefEntityIds(String refEntityIds);
	/**
	 * 获取实体定义的查询SQL语句，包含两部分
	 * 	1、定义查询实体时映射的查询SQL语句
	 *  2、定义在查询列表中的查询条件
	 * @return
	 *
	 * @author liujun
	 * @since 5.0.5
	 * @version 1.0
	 *
	 */
	public Map<String,String> getQuerySql();
	 /**
     * 实体关联的实体
     * @return
     */
    public Collection getRefDatasets();
    /**
     * 设置实体关联的实体
     * @return
     */
	public void setRefDatasets(Collection<IRefDataset> refDatasets);
}
