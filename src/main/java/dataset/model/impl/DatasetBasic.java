/**
 * $Id: DatasetBasic.java,v 1.2 2013/11/16 04:57:48 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model.impl;

import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.model.IDatasetBasic;
import dataset.util.XMLUtil;

/**
 * 数据集基本XML信息定义
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public abstract class DatasetBasic implements IDatasetBasic{
    private String id = null;
    private String name = null;
    private String desc = null;
    private String displayName = null;
    
    public String getDesc() {
        return desc;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * 构建包含基本信息的Element对象
     * @param element
     */
    protected void buildElement(Element element) {
        if(element == null){
            return;
        }
        XMLUtil.addAttribute(element, IDatasetConstant.ID, this.getId());
        XMLUtil.addAttribute(element, IDatasetConstant.NAME, this.getName());
        XMLUtil.addAttribute(element, IDatasetConstant.DISPLAYNAME, this.getDisplayName());
        XMLUtil.addAttribute(element, IDatasetConstant.DESC, this.getDesc());
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
