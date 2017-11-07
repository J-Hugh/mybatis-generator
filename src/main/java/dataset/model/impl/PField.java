/**
 * $Id: PField.java,v 1.2 2013/11/16 04:57:48 chenhua Exp $
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
import dataset.model.IPField;
import dataset.refmodel.IRefDataset;
import dataset.refmodel.impl.RefDataset;
import dataset.util.StringUtil;
import dataset.util.XMLUtil;

/**
 * @Title: PField.java
 * @Description: <br>
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-9 下午06:00:00
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public class PField extends Field implements IPField {
    private String precision;
    private String columnName = null;
    private String columnType = null;
    private String length;
    private boolean isFK = false;
    private boolean isNullAble = false;
    private boolean isPersistent = true;
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
    /**
     * 读取xml，即dataset
     */
    public void fromXML(Object object) {
        if (!(object instanceof Element)) {
            return;
        }
        Element fieldElement = (Element) object;
        this.loadField(fieldElement);

        this.setColumnName(XMLUtil.getAttribute(fieldElement, IDatasetConstant.COLUMNNAME));
        this.setColumnType(XMLUtil.getAttribute(fieldElement, IDatasetConstant.COLUMNTYPE));
        this.setPersistent(StringUtil.toBoolean(XMLUtil.getAttribute(fieldElement, IDatasetConstant.PERSISTENT)));
        this.setPK(StringUtil.toBoolean(XMLUtil.getAttribute(fieldElement, IDatasetConstant.PK)));
        this.setFK(StringUtil.toBoolean(XMLUtil.getAttribute(fieldElement, IDatasetConstant.FK)));
        this.setLength(XMLUtil.getAttribute(fieldElement, IDatasetConstant.LENGTH));
        this.setPrecision(XMLUtil.getAttribute(fieldElement, IDatasetConstant.PRECISION));
        this.setNullAble(StringUtil.toBoolean(XMLUtil.getAttribute(fieldElement, IDatasetConstant.NULLABLE)));
        
		// load查询实体数据集
		Element refVentityFieldsElem = XMLUtil.getChildElement(fieldElement,
				IDatasetConstant.REF_VENTITYFIELDS);
		if (refVentityFieldsElem != null) {
			Iterator iterator = XMLUtil.getChildElements(refVentityFieldsElem,
					IDatasetConstant.REF_DATASET).iterator();
			while (iterator.hasNext()) {
				Element DatasetElem = (Element) iterator.next();
				IRefDataset refDataset = new RefDataset();
				refDataset.fromXML(DatasetElem);
				addRefDataset(refDataset);
				refDataset.setParentPField(this);
			}
		}
    }

    /**
     * 写入xml，即dataset
     */
    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.FIELD);
        this.buildElement(element);
        
        XMLUtil.addAttribute(element, IDatasetConstant.PERSISTENT, String.valueOf(this.isPersistent()));
        XMLUtil.addAttribute(element, IDatasetConstant.COLUMNNAME, this.getColumnName());
        XMLUtil.addAttribute(element, IDatasetConstant.COLUMNTYPE, this.getColumnType());
        XMLUtil.addAttribute(element, IDatasetConstant.PK, String.valueOf(this.isPK()));
        XMLUtil.addAttribute(element, IDatasetConstant.FK, String.valueOf(this.isFK()));
        XMLUtil.addAttribute(element, IDatasetConstant.LENGTH, this.getLength());
        XMLUtil.addAttribute(element, IDatasetConstant.PRECISION, this.getPrecision());
        XMLUtil.addAttribute(element, IDatasetConstant.NULLABLE, String.valueOf(this.isNullAble()));
		
        Element refVentityFieldsElem = XMLUtil.createElement(element.getOwnerDocument(),
				IDatasetConstant.REF_VENTITYFIELDS);
		XMLUtil.addChildElement(element, refVentityFieldsElem);

		Iterator refDatasetIterator = this.getRefDatasets().iterator();
		while (refDatasetIterator.hasNext()) {
			IRefDataset refDataset = (IRefDataset) refDatasetIterator.next();
			XMLUtil.addChildElement(refVentityFieldsElem,refDataset.toXML(element.getOwnerDocument()));
		}
        return element;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public boolean isFK() {
        return isFK;
    }

    public void setFK(boolean isFK) {
        this.isFK = isFK;
    }

    public boolean isNullAble() {
        return isNullAble;
    }

    public void setNullAble(boolean isNullAble) {
        this.isNullAble = isNullAble;
    }

    public boolean isPersistent() {
        return isPersistent;
    }

    public void setPersistent(boolean isPersistent) {
        this.isPersistent = isPersistent;
    }
}
