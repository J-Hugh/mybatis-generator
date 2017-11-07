/**
 * $Id: IDatasetBasic.java,v 1.2 2013/11/16 04:57:52 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

/**
 * @Title: IDatasetBasic.java
 * @Description: <br>dataset，实体，实体属性，关联关系的基本模型，公有部分
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:18:06
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface IDatasetBasic extends INodeXmlPart{
    /**
     * 获得Id
     * @return
     */
    public String getId();
    /**
     * 设置Id
     * @return
     */
    public void setId(String id);
    /**
     * 获得名称
     * @return
     */
    public String getName();
    /**
     * 设置Name
     * @return
     */
    public void setName(String name);
    /**
     * 获得描述
     * @return
     */
    public String getDesc();
    /**
     * 设置Desc
     * @return
     */
    public void setDesc(String desc);
    /**
     * 获得显示名
     * @return
     */
    public String getDisplayName();
    /**
     * 设置DisplayName
     * @return
     */
    public void setDisplayName(String displayName);
}
