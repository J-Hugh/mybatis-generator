/**
 * $Id: Dataset.java,v 1.2 2013/11/16 04:57:49 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.RelationType;
import dataset.model.IAssociationField;
import dataset.model.IDataset;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IPEntity;
import dataset.model.IRelation;
import dataset.util.DatasetUtil;
import dataset.util.EntityFactory;
import dataset.util.XMLUtil;

/**
 * 数据集对象
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class Dataset extends DatasetBasic implements IDataset {
//  private static final Logger log = LoggerFactory.getLogger(Dataset.class);

    private String author = null;
    private Collection entities = null;
    private String referenceFilePath = null;
    
    public Dataset(String path){
        setReferenceFilePath(path);
    }
    public Dataset(){
    }

    public void initDataset() throws Exception{
        InputStream inputStream = new FileInputStream(getReferenceFilePath());
        initDataset(inputStream);
    }
    
    public void initDataset(File file)throws Exception {
        InputStream inputStream = new FileInputStream(file);
        initDataset(inputStream);
    }

    public void initDataset(InputStream inputStream)throws Exception {
        Element root = XMLUtil.loadAsElement(inputStream, IDatasetConstant.DATASET);
        
        this.fromXML(root);     
    }

    public void fromXML(Object object) {
        
        if(!(object instanceof Element)){
            return;
        }
        Element root = (Element)object;
        
        this.setId(XMLUtil.getAttribute(root, IDatasetConstant.ID));
        this.setName(XMLUtil.getAttribute(root, IDatasetConstant.NAME));
        this.setDisplayName(XMLUtil.getAttribute(root,IDatasetConstant.DISPLAYNAME));
        this.setDesc(XMLUtil.getAttribute(root, IDatasetConstant.DESC));

        this.setAuthor(XMLUtil.getAttribute(root, IDatasetConstant.AUTHOR));
        //构建实体
        Iterator entityElems = XMLUtil.getChildElements(root,IDatasetConstant.ENTITY).iterator();
        while (entityElems.hasNext()) {
            Element elem = (Element) entityElems.next();
            String entityType = XMLUtil.getAttribute(elem,IDatasetConstant.ENTITYTYPE);
            IEntity entity = EntityFactory.createEntity(entityType);
            if (entity != null) {
            	entity.setParentDataset(this);
                entity.fromXML(elem);
                this.addEntity(entity);
            }
        }
        buildRelations();
    }
    
    /**
     * 构建持久化实体关联关系
     */
    public void buildRelations(){
        Iterator entitys = this.getEntities().iterator();
        while (entitys.hasNext()) {
            IEntity entity = (Entity) entitys.next();
            if(entity instanceof IPEntity){
                IPEntity pEntity = (IPEntity)entity;
                
                Iterator relations = pEntity.getSrcRelations().iterator();
                while (relations.hasNext()) {
                    //设置关联关系目标实体
                    IRelation relation = (IRelation) relations.next();
                    String tgtEntityId = relation.getTgtEntityId();
                    IPEntity tgtEntity = (IPEntity)DatasetUtil.getEntity(this, tgtEntityId);
                    relation.setTgtEntity(tgtEntity);
                    if(relation.getRelationType().equals(RelationType.TWOWAY_ONE2MANY)){
                        tgtEntity.getTgtRelations().add(relation);
                    }
                    //设置关联字段映射的目标字段
                    Iterator relationFieldsIterator = relation.getRelationFields().iterator();
                    while (relationFieldsIterator.hasNext()) {
                        IAssociationField associationField = (IAssociationField) relationFieldsIterator.next();
                        String tgtFieldId = associationField.getTgtFieldId();
                        IField tgtField = DatasetUtil.getField(tgtEntity, tgtFieldId);
                        associationField.setTgtField(tgtField);
                    }
                }
            }
        }
    }

    
    public Element toXML(Document document) {
        Element dataset = XMLUtil.createElement(document,IDatasetConstant.DATASET);
        this.buildElement(dataset);
        XMLUtil.addAttribute(dataset, IDatasetConstant.AUTHOR, this.getAuthor());
        
        Iterator entityIterator = this.getEntities().iterator();
        while (entityIterator.hasNext()) {
            IEntity entity = (IEntity) entityIterator.next();
            XMLUtil.addChildElement(dataset, entity.toXML(document));
        }
        XMLUtil.addRootElement(document, dataset);
        return dataset;
    }


    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }

    public Collection getEntities() {
        if (entities == null) {
            entities = new ArrayList();
        }
        return entities;
    }
    
    public void setEntities(Collection entities){
        this.entities = entities;
    }

    public void addEntity(IEntity entity) {
        if (entities == null) {
            entities = new ArrayList();
        }
        entities.add(entity);
    }
    
    public void addEntities(Collection entities) {
        if (this.entities == null) {
            this.entities = new ArrayList();
        }
        this.entities.addAll(entities);
    }

    public void removeEntity(IEntity entity) {
        if (entities == null)
            return;
        if (entities.contains(entity)) {
            entities.remove(entity);
        }

    }
    
    public String getReferenceFilePath() {
        return referenceFilePath;
    }

    public void setReferenceFilePath(String referenceFilePath) {
        this.referenceFilePath = referenceFilePath;
    }
}
