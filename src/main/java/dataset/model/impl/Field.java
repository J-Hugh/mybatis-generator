/**
 * $Id: Field.java,v 1.2 2013/11/16 04:57:49 chenhua Exp $
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
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.refmodel.IRefDataset;
import dataset.refmodel.impl.RefDataset;
import dataset.util.XMLUtil;

/**
 * 
 * @author BizFoundation Team: LiuJun
 * 
 *         {注释}
 * 
 * @version 1.0
 * @since 4.2
 */
public class Field extends DatasetBasic implements IField {
	protected String defaultValue = null;
	protected String fieldType = null;
	protected IEntity parentEntity = null;
	protected String fieldName;
	protected boolean isPK = false;

	public void fromXML(Object object) {
		if (!(object instanceof Element)) {
			return;
		}
		this.loadField((Element) object);
	}

	public Element toXML(Document document) {
		Element element = XMLUtil.createElement(document,
				IDatasetConstant.FIELD);
		this.buildElement(element);
		return element;
	}

	/**
	 * 加载实体字段基本信息
	 * 
	 * @param element
	 */
	protected void loadField(Element element) {
		this.setId(XMLUtil.getAttribute(element, IDatasetConstant.ID));
		this.setDesc(XMLUtil.getAttribute(element, IDatasetConstant.DESC));
		this.setDisplayName(XMLUtil.getAttribute(element,
				IDatasetConstant.DISPLAYNAME));
		this.setDefault(XMLUtil.getAttribute(element, IDatasetConstant.DEFAULT));
		this.setFieldName(XMLUtil.getAttribute(element,
				IDatasetConstant.FIELDNAME));
		this.setFieldType(XMLUtil.getAttribute(element,
				IDatasetConstant.FIELDTYPE));

		this.setName(this.getFieldName());
	}

	/**
	 * 构建实体字段基本XML信息
	 */
	protected void buildElement(Element element) {
		if (element == null) {
			return;
		}
		super.buildElement(element);

		XMLUtil.addAttribute(element, IDatasetConstant.FIELDNAME,
				this.getFieldName());
		XMLUtil.addAttribute(element, IDatasetConstant.FIELDTYPE,
				this.getFieldType());
		XMLUtil.addAttribute(element, IDatasetConstant.DEFAULT,
				this.getDefault());
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public IEntity getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(IEntity parentEntity) {
		this.parentEntity = parentEntity;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDefault() {
		return defaultValue;
	}

	public void setDefault(String value) {
		this.defaultValue = value;
	}

	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}

}
