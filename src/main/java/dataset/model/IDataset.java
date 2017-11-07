/**
 * $Id: IDataset.java,v 1.2 2013/11/16 04:57:51 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

/**
 * @Title: IDataset.java
 * @Description: <br>dataset,是实体集合得容器
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:16:22
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface IDataset extends IDatasetBasic{
    /**
     * 获得作者
     * @return
     */
    public String getAuthor();
    /**
     * 设置作者
     * @param author
     */
    public void setAuthor(String author);
    /**
     * 获得数据实体集合
     * @return
     */
    public Collection getEntities();
    /**
     * 设置数据集实体集合
     * @param entities
     */
    public void setEntities(Collection entities);
    
    /**
     * 获取关联dataset文件路径
     * @return
     */
    public String getReferenceFilePath();
    /**
     * 设置关联dataset文件的路径
     * @param fileName
     */
    public void setReferenceFilePath(String fileName);
    
    /**
     * 添加实体
     */
    public void addEntity(IEntity entity);
    /**
     * 删除实体
     */
    public void removeEntity(IEntity entity);
    /**
     * 初始化数据集对象（构建数据集对象内容）
     * @throws Exception
     */
    public void initDataset() throws Exception;
    /**
     * 初始化数据集对象（构建数据集对象内容）
     * @param file
     * @throws Exception
     */
    public void initDataset(File file) throws Exception;
    /**
     * 初始化数据集对象（构建数据集对象内容）
     * @param inputStream
     * @throws Exception
     */
    public void initDataset(InputStream inputStream) throws Exception;
    
    /**
     * 为了确定关联关系中的目标实体，需要对关联关系进行第二次的构建
     * 注意:调用此方法前，务必确保数据集中所有的IPEntity、IPField是完整的，IRelation仅仅缺少目标实体
     */
    public void buildRelations();
}
