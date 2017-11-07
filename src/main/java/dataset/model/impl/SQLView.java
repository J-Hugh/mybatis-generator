/**
 * $Id: SQLView.java,v 1.2 2013/11/16 04:57:49 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.model.IEntity;
import dataset.model.IView;
import dataset.util.StringUtil;
import dataset.util.XMLUtil;

/**
 * 查询实体视图（包括数据库视图和SQL查询语句）
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class SQLView implements IView {
    
    private String dialect;
    private String type;
    private String content;
    
    private IEntity parentEntity = null;
    
    
    public void fromXML(Object obj) {
        if(!(obj instanceof Element)){
            return;
        }
        Element element = (Element)obj;
        this.setDialect(XMLUtil.getAttribute(element, IDatasetConstant.DB_DIALECT));
        this.setType(XMLUtil.getAttribute(element, IDatasetConstant.TYPE));
        this.setContent(XMLUtil.getText(element));
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.SQL);
        
        XMLUtil.addAttribute(element, IDatasetConstant.DB_DIALECT, this.getDialect());
        XMLUtil.addAttribute(element, IDatasetConstant.TYPE, this.getType());
        XMLUtil.setText(element, this.getContent(), true);
        return element;
        
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public IEntity getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(IEntity parentEntity) {
        this.parentEntity = parentEntity;
    }

    

}
