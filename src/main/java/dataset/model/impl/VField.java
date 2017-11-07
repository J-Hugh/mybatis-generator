/**
 * $Id: VField.java,v 1.2 2013/11/16 04:57:49 chenhua Exp $
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
import dataset.model.IField;
import dataset.model.IVField;
import dataset.util.DatasetUtil;
import dataset.util.StringUtil;
import dataset.util.XMLUtil;

/**
 * @Title: VField.java
 * @Description: <br>
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-9 下午05:58:48
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public class VField extends Field implements IVField {

    private String columnName = null;
    private String columnType = null;
    
    private String alias = null;
    
    private String refDataset = null;
	private String refEntityId = null;
    private IEntity refEntity;
    private String refFieldId = null;
    private IField refField;

    
    public void fromXML(Object object){
        Element fieldElement = null;
        if(object instanceof Element){
            fieldElement = (Element) object;
        }
      //soszou 重构 2013-09-24
        if(fieldElement==null)
        	return ;
        this.loadField(fieldElement);
        this.setColumnName(XMLUtil.getAttribute(fieldElement, IDatasetConstant.COLUMNNAME));
        this.setColumnType(XMLUtil.getAttribute(fieldElement, IDatasetConstant.COLUMNTYPE));
        
        this.setAlias(XMLUtil.getAttribute(fieldElement, IDatasetConstant.ALIAS));
        this.setPK(StringUtil.toBoolean(XMLUtil.getAttribute(fieldElement, IDatasetConstant.PK)));
        this.setRefDataset(XMLUtil.getAttribute(fieldElement, IDatasetConstant.REF_DATASET_FILE));
        this.setRefEntityId(XMLUtil.getAttribute(fieldElement, IDatasetConstant.REF_ENTITY_ID));
        this.setRefFieldId(XMLUtil.getAttribute(fieldElement, IDatasetConstant.REF_FIELD_ID));
        
    }
    public Element toXML(Document document){
        Element element = XMLUtil.createElement(document,IDatasetConstant.FIELD);
        this.buildElement(element);
        
        XMLUtil.addAttribute(element, IDatasetConstant.COLUMNNAME, this.getColumnName());
        XMLUtil.addAttribute(element, IDatasetConstant.COLUMNTYPE, this.getColumnType());
        
        XMLUtil.addAttribute(element, IDatasetConstant.ALIAS, this.getAlias());
        XMLUtil.addAttribute(element, IDatasetConstant.PK, String.valueOf(this.isPK()));

        XMLUtil.addAttribute(element, IDatasetConstant.REF_DATASET_FILE, this.getRefDataset());
        XMLUtil.addAttribute(element, IDatasetConstant.REF_ENTITY_ID, this.getRefEntityId());
        XMLUtil.addAttribute(element, IDatasetConstant.REF_FIELD_ID, this.getRefFieldId());
        
        return element;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	/**
	 * 获取数据集文件相对路径
	 */
	public String getRefDataset() {
		if(StringUtil.isNullOrBlank(refDataset)){
			refDataset = StringUtil.getDatasetFileNameFormPath(this.getParentEntity().getParentDataset().getReferenceFilePath());
		}
		return refDataset;
	}
	/**
	 * 获取dataset的全部路径
	 * @return
	 */
	public String getRefDatasetPath(){
		return StringUtil.getProjectPathFormPath(this.getParentEntity().getParentDataset().getReferenceFilePath())+getRefDataset();

	}
	public void setRefDataset(String refDataset) {
		this.refDataset = refDataset;
	}

	public String getRefEntityId() {
		return refEntityId;
	}
	public void setRefEntityId(String refEntityId) {
		this.refEntityId = refEntityId;
	}
	public String getRefFieldId() {
		return refFieldId;
	}
	public void setRefFieldId(String refFieldId) {
		this.refFieldId = refFieldId;
	}
	public IEntity getRefEntity() {
		if(!StringUtil.isNullOrBlank(getRefDataset())&&!StringUtil.isNullOrBlank(getRefEntityId())&& refEntity == null){
			try {
				refEntity = DatasetUtil.getEntity(DatasetUtil.buildDatasetFromXML(getRefDatasetPath()), getRefEntityId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return refEntity;
	}
	public IField getRefField() {
		if(getRefEntity() != null && !StringUtil.isNullOrBlank(getRefFieldId()) && refField == null){
			refField = DatasetUtil.getField(getRefEntity(), getRefFieldId());
		}
		return refField;
	}
    
}
