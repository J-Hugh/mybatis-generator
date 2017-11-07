/**
 * $Id: Relation.java,v 1.2 2013/11/16 04:57:49 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.RelationType;
import dataset.model.IAssociationField;
import dataset.model.IEntity;
import dataset.model.IRelation;
import dataset.util.DatasetUtil;
import dataset.util.XMLUtil;

/**
 * 
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class Relation implements IRelation {

    private String id;
    private String srcRefTgtName;//目标实体属性名称
    private String srcRefTgtDisplayName;//目标实体属性显示名称
    private String tgtRefSrcName;//源实体属性名称（双向1对N关联关系时使用）
    private String tgtRefSrcDisplayName;//源实体属性显示名称(双向1对N关联关系时使用)
    private String fkName;//外键名称
    private IEntity srcEntity = null;
    private IEntity tgtEntity = null;
    private RelationType relationType = null;
    private Collection relationFields;
    
    private String srcCascade = "";
    private String tgtCascade = "";
    private boolean srcLazyLoad = false;
    private boolean tgtLazyLoad = false;
    
    private String tgtEntityId;
    
    public Relation(){
        
    }
    
    public void fromXML(Object obj) {
        Element element = null;
        if (!(obj instanceof Element)) {
            return;
        }
        element = (Element) obj;
        
        this.setId(XMLUtil.getAttribute(element, IDatasetConstant.ID));
        this.setSrcRefTgtName(XMLUtil.getAttribute(element, IDatasetConstant.SRC_REF_TGT_NAME));
        this.setSrcRefTgtDisplayName(XMLUtil.getAttribute(element, IDatasetConstant.SRC_REF_TGT_DISPLAY_NAME));
        this.setTgtRefSrcName(XMLUtil.getAttribute(element, IDatasetConstant.TGT_REF_SRC_NAME));
        this.setTgtRefSrcDisplayName(XMLUtil.getAttribute(element, IDatasetConstant.TGT_REF_SRC_DISPLAY_NAME));
        this.setFkName(XMLUtil.getAttribute(element, IDatasetConstant.FK_NAME));
        
        this.tgtEntityId = XMLUtil.getAttribute(element, IDatasetConstant.TARGETENTITY);
        String type = XMLUtil.getAttribute(element, IDatasetConstant.RELATE_TYPE);
        this.setRelationType(RelationType.getInstance(type));
        
        Iterator iterator = XMLUtil.getChildElements(element, IDatasetConstant.FKASSOCIATIONFIELD).iterator();
        while (iterator.hasNext()) {
            Element fieldElem = (Element) iterator.next();
            String srcFieldId = XMLUtil.getAttribute(fieldElem, IDatasetConstant.SOURCE_FIELD_ID);
            String tgtFieldId = XMLUtil.getAttribute(fieldElem, IDatasetConstant.TARGET_FIELD_ID);
            IAssociationField field = new AssociationField();
            field.setSrcField(DatasetUtil.getField(srcEntity, srcFieldId));
            field.setTgtField(DatasetUtil.getField(tgtEntity, tgtFieldId));
            field.fromXML(fieldElem);
            this.getRelationFields().add(field);
        }
        
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.RELATION);

        XMLUtil.addAttribute(element, IDatasetConstant.ID, this.getId());
        XMLUtil.addAttribute(element, IDatasetConstant.SRC_REF_TGT_NAME, this.getSrcRefTgtName());
        XMLUtil.addAttribute(element, IDatasetConstant.SRC_REF_TGT_DISPLAY_NAME, this.getSrcRefTgtDisplayName());
        XMLUtil.addAttribute(element, IDatasetConstant.TGT_REF_SRC_NAME, this.getTgtRefSrcName());
        XMLUtil.addAttribute(element, IDatasetConstant.TGT_REF_SRC_DISPLAY_NAME,this.getTgtRefSrcDisplayName());
        XMLUtil.addAttribute(element, IDatasetConstant.FK_NAME, this.getFkName());
        XMLUtil.addAttribute(element, IDatasetConstant.SOURCEENTITY, this.getSrcEntity().getId());
        XMLUtil.addAttribute(element, IDatasetConstant.TARGETENTITY, this.getTgtEntity().getId());
        XMLUtil.addAttribute(element, IDatasetConstant.RELATE_TYPE, this.getRelationType().getType());
        
        Iterator iterator = relationFields.iterator();
        while (iterator.hasNext()) {
            IAssociationField field = (IAssociationField) iterator.next();
            XMLUtil.addChildElement(element, field.toXML(document));
        }
        
        return element;
    }


    public IEntity getSrcEntity() {
        return srcEntity;
    }

    public void setSrcEntity(IEntity srcEntity) {
        this.srcEntity = srcEntity;
    }

    public IEntity getTgtEntity() {
        return tgtEntity;
    }

    public void setTgtEntity(IEntity tgtEntity) {
        this.tgtEntity = tgtEntity;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }

    public Collection getRelationFields() {
        if(relationFields == null){
            relationFields = new ArrayList();
        }
        return relationFields;
    }

    public void addRelationField(IAssociationField field){
        if(relationFields == null){
            relationFields = new ArrayList();
        }
        relationFields.add(field);
    }
    public void removeRelationField(IAssociationField field){
        if(relationFields == null || field == null){
            return;
        }
        if(relationFields.contains(field)){
            relationFields.remove(field);
        }
        
    }

    public String getSrcCascade() {
        return srcCascade;
    }

    public void setSrcCascade(String srcCascade) {
        this.srcCascade = srcCascade;
    }

    public String getTgtCascade() {
        return tgtCascade;
    }

    public void setTgtCascade(String tgtCascade) {
        this.tgtCascade = tgtCascade;
    }

    public boolean isSrcLazyLoad() {
        return srcLazyLoad;
    }

    public void setSrcLazyLoad(boolean srcLazyLoad) {
        this.srcLazyLoad = srcLazyLoad;
    }

    public boolean isTgtLazyLoad() {
        return tgtLazyLoad;
    }

    public void setTgtLazyLoad(boolean tgtLazyLoad) {
        this.tgtLazyLoad = tgtLazyLoad;
    }


    public String getTgtEntityId() {
        if(tgtEntity != null)
            return tgtEntity.getId();
        return tgtEntityId;
    }


    public String getFkName() {
        return fkName;
    }

    public void setFkName(String fkName) {
        this.fkName = fkName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrcRefTgtName() {
        return srcRefTgtName;
    }

    public void setSrcRefTgtName(String srcRefTgtName) {
        this.srcRefTgtName = srcRefTgtName;
    }

    public String getSrcRefTgtDisplayName() {
        return srcRefTgtDisplayName;
    }

    public void setSrcRefTgtDisplayName(String srcRefTgtDisplayName) {
        this.srcRefTgtDisplayName = srcRefTgtDisplayName;
    }

    public String getTgtRefSrcName() {
        return tgtRefSrcName;
    }

    public void setTgtRefSrcName(String tgtRefSrcName) {
        this.tgtRefSrcName = tgtRefSrcName;
    }

    public String getTgtRefSrcDisplayName() {
        return tgtRefSrcDisplayName;
    }

    public void setTgtRefSrcDisplayName(String tgtRefSrcDisplayName) {
        this.tgtRefSrcDisplayName = tgtRefSrcDisplayName;
    }

    public void setTgtEntityId(String tgtEntityId) {
        this.tgtEntityId = tgtEntityId;
    }

}
