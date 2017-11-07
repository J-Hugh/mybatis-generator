/**
 * $Id: Entity.java,v 1.3 2013/11/16 04:57:49 chenhua Exp $
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

import autocode.model.AutoCodeUiInfo;
import dataset.IDatasetConstant;
import dataset.model.IDataset;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.util.StringUtil;
import dataset.util.XMLUtil;
import dataset.viewmodel.IDetailInfoForm;
import dataset.viewmodel.INewForm;
import dataset.viewmodel.IQueryForm;
import dataset.viewmodel.IResultForm;
import dataset.viewmodel.impl.DetailInfoForm;
import dataset.viewmodel.impl.NewForm;
import dataset.viewmodel.impl.QueryForm;
import dataset.viewmodel.impl.ResultForm;

/**
 * 数据集实体对象，同时也是非持久化实体对象
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class Entity extends DatasetBasic implements IEntity{
    protected String author = null;
    protected String entityType = null;
    protected Collection fields = null;
    protected Collection pKFields;
    protected String packageName = null;
    protected IDataset parentDataset= null;
    protected boolean isSetViewModel = false;//是否设置过实体的显示模型
    //实体位置 x,y,wide,heigt,
    protected int[] location = new int[]{0,0,-1,-1};
    protected AutoCodeUiInfo autoCodeUiInfo;
    
    protected IQueryForm queryForm;
    protected IResultForm resultForm;
    protected IDetailInfoForm detailInfoForm;
    protected INewForm newForm;

    
    public Entity() {
        this.entityType = IDatasetConstant.ENTITYTYPE_NENTITY;
    }
    
    public String getAuthor() {
        return author;
    }

    public String getEntityType() {
        return entityType;
    }

    public Collection getFields() {
        if(fields == null){
            fields = new ArrayList();
        }
        return fields;
    }

    public String getPackageName() {
        return packageName;
    }



    public void addField(IField field) {
        if(fields==null){
            fields = new ArrayList();
		}
		if (field.isPK() && !getPKFields().contains(field)) {
			getPKFields().add(field);
		}
        fields.add(field);
        
    }

    public void removeField(IField field) {
        if(fields!=null&&fields.contains(field)){
            fields.remove(field);
        }
        
    }
    
    public Collection getPKFields() {
        if (pKFields == null) {
            pKFields = new ArrayList();
        }
        return pKFields;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public void setFields(Collection fields) {
        this.fields = fields;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public IDataset getParentDataset() {
        return parentDataset;
    }

    public void setParentDataset(IDataset parentDataset) {
        this.parentDataset = parentDataset;
    }
    
    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }
    
    public void setLocation(int x,int y,int wide, int height) {
        this.location[0] =x;
        this.location[1] =y;
        this.location[2] =wide;
        this.location[3] =height;
    }
    
    public void fromXML(Object object) {
        if(!(object instanceof Element)){
            return;
        }
        Element element = (Element)object;
        this.loadEntity(element);
        
        Element fieldsElem = XMLUtil.getChildElement(element, IDatasetConstant.FIELDS);
        if(fieldsElem != null){
            Iterator iterator = XMLUtil.getChildElements(fieldsElem, IDatasetConstant.FIELD).iterator();
            while (iterator.hasNext()) {
                Element fieldElem = (Element) iterator.next();
                IField field = new Field();
                field.setParentEntity(this);
                field.fromXML(fieldElem);
                this.addField(field);
            }
        }
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.ENTITY);
        this.buildElement(element);
        
        return element;
    }


    /**
     * 加载实体基本信息
     * @param element
     */
    protected void loadEntity(Element element){

        this.setId(XMLUtil.getAttribute(element, IDatasetConstant.ID));
        this.setName(XMLUtil.getAttribute(element, IDatasetConstant.NAME));
        this.setAuthor(XMLUtil.getAttribute(element, IDatasetConstant.AUTHOR));
        this.setDesc(XMLUtil.getAttribute(element, IDatasetConstant.DESC));
        this.setDisplayName(XMLUtil.getAttribute(element, IDatasetConstant.DISPLAYNAME));
        this.setPackageName(XMLUtil.getAttribute(element, IDatasetConstant.PACKAGE_NAME));
        this.setSetViewModel(StringUtil.toBoolean(XMLUtil.getAttribute(element, IDatasetConstant.IS_SET_VIEW_MODEL)));

        Element locationElement = XMLUtil.getChildElement(element,IDatasetConstant.LOCATION);
        if(locationElement==null){
            this.setLocation(0, 0, -1, -1);
        }else{
            String xPoint = XMLUtil.getAttribute(locationElement,IDatasetConstant.X);
            String yPoint = XMLUtil.getAttribute(locationElement,IDatasetConstant.Y);
            String widePoint = XMLUtil.getAttribute(locationElement,IDatasetConstant.WIDE);
            String heightPoint = XMLUtil.getAttribute(locationElement,IDatasetConstant.HEIGHT);
            int x = StringUtil.isDigit(xPoint)?Integer.valueOf(xPoint).intValue():0;
            int y = StringUtil.isDigit(yPoint)?Integer.valueOf(yPoint).intValue():0;
            int wide = StringUtil.isDigit(widePoint)?Integer.valueOf(widePoint).intValue():-1;
            int height = StringUtil.isDigit(heightPoint)?Integer.valueOf(heightPoint).intValue():-1;
            this.setLocation(x, y, wide, height);
        }
    }
    
    /**
     * 构建实体基本XML信息
     */
    protected void buildElement(Element element) {
        if(element == null){
            return;
        }
        
        super.buildElement(element);
        
        XMLUtil.addAttribute(element, IDatasetConstant.AUTHOR, this.getAuthor());
        XMLUtil.addAttribute(element, IDatasetConstant.ENTITYTYPE, this.getEntityType());
        XMLUtil.addAttribute(element, IDatasetConstant.PACKAGE_NAME, this.getPackageName());
        XMLUtil.addAttribute(element, IDatasetConstant.IS_SET_VIEW_MODEL, String.valueOf(this.isSetViewModel()));

        Element locationElem = XMLUtil.createElement(element.getOwnerDocument(),IDatasetConstant.LOCATION);
        XMLUtil.addAttribute(locationElem, IDatasetConstant.X, String.valueOf(location[0]));
        XMLUtil.addAttribute(locationElem, IDatasetConstant.Y, String.valueOf(location[1]));
        XMLUtil.addAttribute(locationElem, IDatasetConstant.WIDE, String.valueOf(location[2]));
        XMLUtil.addAttribute(locationElem, IDatasetConstant.HEIGHT, String.valueOf(location[3]));
        
        XMLUtil.addChildElement(element, locationElem);
        
        Element fieldsElem = XMLUtil.createElement(element.getOwnerDocument(),IDatasetConstant.FIELDS);
        XMLUtil.addChildElement(element, fieldsElem);

        Iterator fieldIterator = this.getFields().iterator();
        while (fieldIterator.hasNext()) {
            IField field = (IField) fieldIterator.next();
            XMLUtil.addChildElement(fieldsElem, field.toXML(element.getOwnerDocument()));
        }
    }

    public boolean isSetViewModel() {
        return isSetViewModel;
    }

    public void setSetViewModel(boolean isSetViewModel) {
        this.isSetViewModel = isSetViewModel;
    }

    public IQueryForm getQueryForm() {
        if(queryForm == null){
            queryForm = new QueryForm();
        }
        return queryForm;
    }


    public void setQueryForm(IQueryForm queryForm) {
        this.queryForm = queryForm;
    }


    public IResultForm getResultForm() {
        if(resultForm == null){
            resultForm = new ResultForm();
        }
        return resultForm;
    }


    public void setResultForm(IResultForm resultForm) {
        this.resultForm = resultForm;
    }


    public IDetailInfoForm getDetailInfoForm() {
        if(detailInfoForm == null){
            detailInfoForm = new DetailInfoForm();
        }
        return detailInfoForm;
    }


    public void setDetailInfoForm(IDetailInfoForm detailInfoForm) {
        this.detailInfoForm = detailInfoForm;
    }


    public INewForm getNewForm() {
        if(newForm == null){
            newForm = new NewForm();
        }
        return newForm;
    }


    public void setNewForm(INewForm newForm) {
        this.newForm = newForm;
    }

    public AutoCodeUiInfo getAutoCodeUiInfo() {
        return autoCodeUiInfo;
    }


    public void setAutoCodeUiInfo(AutoCodeUiInfo autoCodeUiInfo) {
        this.autoCodeUiInfo = autoCodeUiInfo;
    }
}
