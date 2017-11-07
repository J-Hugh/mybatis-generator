/**
 * $Id: IPEntity.java,v 1.2 2013/11/16 04:57:51 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

import java.util.Collection;

import dataset.model.impl.PEntityConditon;

/**
 * @Title: IPEntity.java
 * @Description: <br>持久化实体的模型接口
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:18:25
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface IPEntity extends IEntity{
    /**
     * 获得schema
     * @return
     */
    public String getSchema();
    /**
     * 设置schema
     * @param schema
     */
    public void setSchema(String schema);
    /**
     * 获得表名
     * @return
     */
    public String getTable();
    /**
     * 设置表名
     * @param table
     */
    public void setTable(String table);
    /**
     * 获得主键生成策略
     * @return
     */
    public String getGenStrategy();
    /**
     * 设置主键生成策略
     * @param genStrategy
     * @return
     */
    public void setGenStrategy(String genStrategy);
    
    /**
     * 获得关联关系集合（当前实体为源端）
     * @return
     */
    public Collection getSrcRelations();
    /**
     * 设置关联关系集合（当前实体为源端）
     * @param relations
     */
    public void setSrcRelations(Collection relations);
    /**
     * 添加关联关系
     */
    public void addRelation(IRelation relation);
    /**
     * 删除关联关系
     */
    public void removeRelation(IRelation relation);
 
    /**
     * 获取存储双向1对N关联关系引用对象的集合（当前实体为双向关联关系的目标实体）
     * @return
     */
    public Collection getTgtRelations();
    /**
     * 设置存储双向1对N关联关系引用对象的集合（当前实体为双向关联关系的目标实体）
     * @param hbmRelations
     */
    public void setTgtRelations(Collection tgtRelations);
    
    /**
     * 获得PEntityConditon对象
     * 
     * @return PEntityConditon
     */
    public PEntityConditon getPEntityConditon();

    /**
     * 设置PEntityConditon对象
     * @param entityConditon
     */
    public void setPEntityConditon(PEntityConditon entityConditon);
    
    /**
     * 获得模块名
     * 
     * @return String
     */
    public String getModuleName();

    /**
     * 设置模块名
     * 
     * @param moduleName
     */
    public void setModuleName(String moduleName);

    /**
     * 获得包名
     * 
     * @return String
     */
    public String getPackageName();

    /**
     * 设置包名
     * 
     * @param packageName
     */
    public void setPackageName(String packageName);
    
    /**
     * 根据查询条件获取查询的SQL语句
     * @return
     */
    public String getQuerySql();
    /**
     * 实体关联的实体
     * @return
     */
    public Collection getRefDatasets();
}
