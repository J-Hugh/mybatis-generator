/**
 * $Id: PEntity.java,v 1.6 2013/11/16 04:57:49 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IRelation;
import dataset.refmodel.IRefDataset;
import dataset.refmodel.impl.RefDataset;
import dataset.util.DatasetUtil;
import dataset.util.XMLUtil;
import dataset.viewmodel.IQueryField;
import dataset.viewmodel.impl.DetailInfoForm;
import dataset.viewmodel.impl.NewForm;
import dataset.viewmodel.impl.QueryForm;
import dataset.viewmodel.impl.ResultForm;

/**
 * @Title: PEntity.java
 * @Description: <br>
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-9 下午05:50:12
 * @author TangWei
 * @version $Revision: 1.6 $
 * @since 1.0
 */
public class PEntity extends Entity implements IPEntity {
    
    
    private String genStrategy = null;
    private String schema = null;
    private String table = null;
    private String moduleName = null; //模块名
    private String packageName = null;//包名
    private Collection srcRelations = null;// 实体关联关系（当前实体为源端）
    private Collection tgtRelations = null;//存储双向1对N关联关系对象的引用（当前实体为双向关联关系的目标实体）
    protected Collection refDatasets = null;

	public Collection getRefDatasets() {
		if (refDatasets == null) {
			refDatasets = new ArrayList();
		}
		return refDatasets;
	}

	public void setRefDatasets(Collection refDatasets) {
		this.refDatasets = refDatasets;
	}

	public void addRefDataset(IRefDataset refDataset) {
		if (refDatasets == null) {
			refDatasets = new ArrayList();
		}
		refDatasets.add(refDataset);

	}
    public PEntity(){
        this.entityType = IDatasetConstant.ENTITYTYPE_PENTITY;
    }
    
    
    /**
     * 读取xml，即dataset
     */
    public void fromXML(Object object) {
        Element element = null;
        if (!(object instanceof Element)) {
            return;
        }
        element = (Element) object;
        //load持久化实体基本信息
        this.loadEntity(element);
        this.setGenStrategy(XMLUtil.getAttribute(element, IDatasetConstant.GENSTRATEGY));
        this.setSchema(XMLUtil.getAttribute(element, IDatasetConstant.SCHEMA));
        this.setTable(XMLUtil.getAttribute(element, IDatasetConstant.TABLENAME));
        this.setModuleName(XMLUtil.getAttribute(element, IDatasetConstant.MODULE_NAME));
        this.setPackageName(XMLUtil.getAttribute(element, IDatasetConstant.PACKAGE_NAME));
        
        //load持久化实体字段信息
        Element fieldsElem = XMLUtil.getChildElement(element, IDatasetConstant.FIELDS);
        if(fieldsElem != null){
            Iterator iterator = XMLUtil.getChildElements(fieldsElem, IDatasetConstant.FIELD).iterator();
            while (iterator.hasNext()) {
                Element fieldElem = (Element) iterator.next();
                IPField field = new PField();
                field.setParentEntity(this);
                field.fromXML(fieldElem);
                this.addField(field);
            }
        }
    	// load查询实体数据集
		Element refVentityFieldsElem = XMLUtil.getChildElement(element,IDatasetConstant.REF_VENTITYFIELDS);
		if (refVentityFieldsElem != null) {
			Iterator iterator = XMLUtil.getChildElements(refVentityFieldsElem,
					IDatasetConstant.REF_DATASET).iterator();
			while (iterator.hasNext()) {
				Element DatasetElem = (Element) iterator.next();
				IRefDataset refDataset = new RefDataset();
				refDataset.fromXML(DatasetElem);
				refDatasets.add(refDataset);
			}
		}
        //load持久化实体关联关系信息
        Element relationsElem = XMLUtil.getChildElement(element, IDatasetConstant.RELATIONS);
        if(relationsElem != null){
            Iterator iterator = XMLUtil.getChildElements(relationsElem, IDatasetConstant.RELATION).iterator();
            while (iterator.hasNext()) {
                Element relationElement = (Element) iterator.next();
                IRelation relation = new Relation();
                relation.setSrcEntity(this);
                relation.fromXML(relationElement);
                this.addRelation(relation);
            }
        }
        
        //load持久化实体展示模型
        Element viewModelElem = XMLUtil.getChildElement(element, IDatasetConstant.VIEW_MODEL);
        if(viewModelElem != null){
            //查询信息
            Element formElem = XMLUtil.getChildElement(viewModelElem, IDatasetConstant.QUERY_FORM);
            if(formElem != null){
                this.queryForm = new QueryForm();
                this.queryForm.fromXML(formElem);
                this.queryForm.setParentEntity(this);
            }
            //查询结果信息
            formElem = XMLUtil.getChildElement(viewModelElem, IDatasetConstant.RESULT_FORM);
            if(formElem != null){
                this.resultForm = new ResultForm();
                this.resultForm.fromXML(formElem);
                this.resultForm.setParentEntity(this);
            }
            //录入信息
            formElem = XMLUtil.getChildElement(viewModelElem, IDatasetConstant.ADD_FORM);
            if(formElem != null){
                this.newForm = new NewForm();
                this.newForm.fromXML(formElem);
                this.newForm.setParentEntity(this);
            }
            //详细信息
            formElem = XMLUtil.getChildElement(viewModelElem, IDatasetConstant.DETAIL_IFNO_FORM);
            if(formElem != null){
                this.detailInfoForm = new DetailInfoForm();
                this.detailInfoForm.fromXML(formElem);
                this.detailInfoForm.setParentEntity(this);
            }
            
        }
    }

    /**
     * 写入xml，即dataset
     */
    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.ENTITY);
        this.buildElement(element);
        
        XMLUtil.addAttribute(element, IDatasetConstant.GENSTRATEGY, this.getGenStrategy());
        XMLUtil.addAttribute(element, IDatasetConstant.SCHEMA, this.getSchema());
        XMLUtil.addAttribute(element, IDatasetConstant.TABLENAME, this.getTable());
        //添加实体属性        
        Element relationsElem = XMLUtil.createElement(document,IDatasetConstant.RELATIONS);
        XMLUtil.addChildElement(element, relationsElem);
        Iterator relationIterator = this.getSrcRelations().iterator();
        while (relationIterator.hasNext()) {
            IRelation relation = (IRelation) relationIterator.next();
            XMLUtil.addChildElement(relationsElem, relation.toXML(document));
        }
        //添加依赖数据集和查询实体
        Element refVentityFieldsElem = XMLUtil.createElement(element.getOwnerDocument(),
				IDatasetConstant.REF_VENTITYFIELDS);
		XMLUtil.addChildElement(element, refVentityFieldsElem);

		Iterator refDatasetIterator = this.getRefDatasets().iterator();
		while (refDatasetIterator.hasNext()) {
			IRefDataset refDataset = (IRefDataset) refDatasetIterator.next();
			XMLUtil.addChildElement(refVentityFieldsElem,refDataset.toXML(element.getOwnerDocument()));
		}
		//添加显示模型
        Element viewModelElem = XMLUtil.createElement(document,IDatasetConstant.VIEW_MODEL);
        XMLUtil.addChildElement(element, viewModelElem);
        
        if(queryForm != null){
            XMLUtil.addChildElement(viewModelElem, queryForm.toXML(document));
        }
        if(resultForm != null){
            XMLUtil.addChildElement(viewModelElem, resultForm.toXML(document));
        }
        if(detailInfoForm != null){
            XMLUtil.addChildElement(viewModelElem, detailInfoForm.toXML(document));
        }
        if(newForm != null){
            XMLUtil.addChildElement(viewModelElem, newForm.toXML(document));
        }
      
        return element;
    }

    public String getGenStrategy() {
        return genStrategy;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }
    
    public Collection getSrcRelations() {
        if(srcRelations == null){
            srcRelations = new ArrayList();
        }
        return srcRelations;
    }
    
    public void addRelation(IRelation relation) {
        if(srcRelations==null){
            srcRelations = new ArrayList();
        }
        srcRelations.add(relation);
    }
    
    public void removeRelation(IRelation relation) {
        if(srcRelations!=null && srcRelations.contains(relation)){
            srcRelations.remove(relation);
        }
    }
    
    public void setSrcRelations(Collection srcRelations) {
        this.srcRelations = srcRelations;
    }

    public void setGenStrategy(String genStrategy) {
        this.genStrategy = genStrategy;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setTable(String table) {
        this.table = table;
    }





    public Collection getTgtRelations() {
        if(tgtRelations == null){
            tgtRelations = new ArrayList();
        }
        return tgtRelations;
    }

    public void setTgtRelations(Collection tgtRelations) {
        this.tgtRelations = tgtRelations;
    }

    public PEntityConditon getPEntityConditon() {
        if (this.pEntityConditon==null) {
            this.pEntityConditon = new PEntityConditon();
        }
        return this.pEntityConditon;
    }

    public void setPEntityConditon(PEntityConditon entityConditon) {
        pEntityConditon = entityConditon;
    } 

    private PEntityConditon pEntityConditon;

    public String getModuleName() {
        return moduleName;
    }


    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }


    public String getPackageName() {
        return packageName;
    }


    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    /**
     * 根据查询条件获取查询的SQL语句
     * @return
     */
    public String getQuerySql() {
    	String lineSeparator = "\r\n";
    	String tab = "\t\t";
    	StringBuffer sql = new StringBuffer("SELECT ");
    	List list = (List)this.getFields();
    	for(int i=0,j=0;i<list.size();i++){
			IPField field = (IPField)list.get(i);
			if(!field.isPersistent()){
				continue;
			}
			if(j>0){
				sql.append(",");
			}
			sql.append(field.getColumnName());
			j++;
		}
    	sql.append(" FROM " + this.getTable() + " WHERE 1=1");
    	Iterator iterator = this.getQueryForm().getFields().iterator();
    	while (iterator.hasNext()) {
			IQueryField queryField = (IQueryField) iterator.next();
			String fieldName = queryField.getFieldNameInMultiValueCase();
			String operator = queryField.getOperator();
			String colName = ((IPField)DatasetUtil.getField(this, queryField.getFieldId())).getColumnName();

			if(IDatasetConstant.OPERATOR_LIKE_LEFT.equals(operator)){
				sql.append(lineSeparator + tab + "#if(${" + fieldName + "}) AND " + colName + " " + IDatasetConstant.OPERATOR_LIKE + " ${sqlUtil.leftLike(\":" + fieldName + "\")} #end ");
			}else if (IDatasetConstant.OPERATOR_LIKE_RIGHT.equals(operator)) {
				sql.append(lineSeparator + tab + "#if(${" + fieldName + "}) AND " + colName + " " + IDatasetConstant.OPERATOR_LIKE + " ${sqlUtil.rightLike(\":" + fieldName + "\")} #end ");
			}else if (IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT.equals(operator)) {
				sql.append(lineSeparator + tab + "#if(${" + fieldName + "}) AND " + colName + " " + IDatasetConstant.OPERATOR_LIKE + " ${sqlUtil.like(\":" + fieldName + "\")} #end ");
			}else if(IDatasetConstant.OPERATOR_IN.equalsIgnoreCase(operator)||IDatasetConstant.OPERATOR_NOT_IN.equalsIgnoreCase(operator)) {
				sql.append(lineSeparator + tab + "#if(${" + fieldName + "}) AND " + colName + " " + operator + "( :" + fieldName + ") #end ");
			}
			else{
				sql.append(lineSeparator + tab + "#if(${" + fieldName + "}) AND " + colName + " " + operator + " :" + fieldName + " #end ");
			}

		}
    	
    	return sql.toString();
	}
    
}
