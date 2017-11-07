/**
 * $Id: IRelation.java,v 1.2 2013/11/16 04:57:51 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

import java.util.Collection;

import dataset.RelationType;

/**
 * @Title: IRelation.java
 * @Description: <br>实体关联关系的模型接口
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:17:12
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface IRelation extends INodeXmlPart{

    /**
     * 获取Id
     */
    public String getId();
    /**
     * 设置Id
     */
    public void setId(String id);
    /**
     * 获取目标实体属性名称
     * @return
     */
    public String getSrcRefTgtName();
    /**
     * 设置目标实体属性名称
     */
    public void setSrcRefTgtName(String srcRefTgtName);
    /**
     * 获取目标实体属性显示名称
     * @return
     */
    public String getSrcRefTgtDisplayName();
    /**
     * 设置目标实体属性显示名称
     */
    public void setSrcRefTgtDisplayName(String srcRefTgtDisplayName);   
    /**
     * 获取源实体属性名称（双向1对N关联关系时使用）
     */
    public String getTgtRefSrcName();
    /**
     * 设置源实体属性名称（双向1对N关联关系时使用）
     * @param tgtRefSrcName
     */
    public void setTgtRefSrcName(String tgtRefSrcName);
    /**
     * 获取源实体属性显示名称(双向1对N关联关系时使用)
     */
    public String getTgtRefSrcDisplayName();
    /**
     * 设置源实体属性显示名称(双向1对N关联关系时使用)
     * @param tgtRefSrcDisplayName
     * @return
     */
    public void setTgtRefSrcDisplayName(String tgtRefSrcDisplayName);
    /**
     * 获取外键名称
     * @return
     */
    public String getFkName();
    /**
     * 设置外键名称
     * @param fnName
     */
    public void setFkName(String fnName);
    /**
     * 获取关联源实体
     * @return
     */
    public IEntity getSrcEntity();
    /**
     * 设置关联源实体
     * @param entity
     */
    public void setSrcEntity(IEntity entity);
    /**
     * 获取关联目标实体
     * @return
     */
    public IEntity getTgtEntity();
    /**
     * 设置关联目标实体
     * @param entity
     */
    public void setTgtEntity(IEntity entity);
    /**
     * 获取关联关系类型
     * @return
     */
    public RelationType getRelationType();
    /**
     * 设置关联关系类型
     * @param type
     */
    public void setRelationType(RelationType type);
    /**
     * 获取关联字段
     * @return 实现IAssociationField接口的对象实例集合
     */
    public Collection getRelationFields();
    /**
     * 添加关联字段
     * @param field
     */
    public void addRelationField(IAssociationField field);
    /**
     * 移除关联字段
     * @param field
     */
    public void removeRelationField(IAssociationField field);
    /**
     * 获取源实体到目标实体级联关系
     * @return
     */
    public String getSrcCascade();
    /**
     * 设置源实体到目标实体关联关系
     */
    public void setSrcCascade(String cascade);
    /**
     * 获取目标实体到源实体关联关系
     * @return
     */
    public String getTgtCascade();
    /**
     * 获取目标实体到源实体关联关系
     */
    public void setTgtCascade(String cascade);
    /**
     * 获取是否延迟加载目标实体对象（src：表示当前访问关系为源实体到目标实体）
     * @return
     */
    public boolean isSrcLazyLoad();
    /**
     * 设置是否延迟加载目标实体对象（src：表示当前访问关系为源实体到目标实体）
     * @param lazyLoad
     */
    public void setSrcLazyLoad(boolean lazyLoad);
    /**
     * 获取是否延迟加载源实体对象（tgt：表示当前访问关系为目标实体到源实体）
     * @return
     */
    public boolean isTgtLazyLoad();
    /**
     * 设置是否延迟加载源实体对象（tgt：表示当前访问关系为目标实体到源实体）
     * @param lazyLoad
     */
    public void setTgtLazyLoad(boolean lazyLoad);
    /**
     * 获取目标实体Id
     * @return
     */
    public String getTgtEntityId();
    /**
     * 设置目标实体Id，注意:谨慎使用
     * @return
     */
    public void setTgtEntityId(String tgtEntityId);
}
