/**
 * $Id: IView.java,v 1.2 2013/11/16 04:57:52 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

/**
 * 查询实体视图（包括数据库视图和SQL查询语句）接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IView extends INodeXmlPart {


    /**
     *获取数据库类型
     * @return
     */
    public String getDialect();
    /**
     * 设置数据库类型
     * @param dbType
     */
    public void setDialect(String dailect);
    /**
     * 获取视图定义类型
     *  SQL：表示SQL查询语句
     *  View：表示数据库视图名称
     * @return
     */
    public String getType();
    /**
     * 设置视图定义类型
     *  SQL：表示SQL查询语句
     *  View：表示数据库视图
     * @param type
     */
    public void setType(String type);
    /**
     * 获取视图内容(SQL查询语句或者数据库View名称)
     * @return
     */
    public String getContent();
    /**
     * 设置视图内容(SQL查询语句或者数据库View名称)
     * @param content
     */
    public void setContent(String content);
    /**
     * 获取所属查询实体
     * @return
     */
    public IEntity getParentEntity();
    /**
     * 设置所属查询实体
     * @param entity
     */
    public void setParentEntity(IEntity entity);
}
