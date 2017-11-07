/**
 * $Id: VEntity.java,v 1.4 2013/11/16 04:57:49 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import autocode.util.StringUtil;
import dataset.IDatasetConstant;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IVEntity;
import dataset.model.IVField;
import dataset.model.IView;
import dataset.refmodel.IRefDataset;
import dataset.refmodel.impl.RefDataset;
import dataset.refmodel.impl.RefEntity;
import dataset.util.DatasetUtil;
import dataset.util.XMLUtil;
import dataset.viewmodel.IQueryField;
import dataset.viewmodel.impl.DetailInfoForm;
import dataset.viewmodel.impl.NewForm;
import dataset.viewmodel.impl.QueryForm;
import dataset.viewmodel.impl.ResultForm;

/**
 * @Title: VEntity.java
 * @Description: <br>
 * <br>
 * @Company: CSI
 * @Created on 2010-6-10 下午05:17:52
 * @author TangWei
 * @version $Revision: 1.4 $
 * @since 1.0
 */
public class VEntity extends Entity implements IVEntity {

	private String type;
	private Collection views = null;
	private String refEntityIds = "";
	private Collection refEntities = null;
	protected Collection refDatasets = null;

	public Collection getRefDatasets() {
		if (refDatasets == null&&StringUtil.isNotEmpty(refEntityIds)) {
			refDatasets = new ArrayList();
			RefDataset refDataset = new RefDataset();
			refDataset.setId(this.getParentDataset().getName());
			refDataset.setProjectPath(dataset.util.StringUtil.getProjectPathFormPath(this.getParentDataset().getReferenceFilePath()));
			String[] ids = this.getRefEntityIds().split(",");
			for (int i = 0; i < ids.length; i++) {
				RefEntity refEntity = new RefEntity();
				refEntity.setId(ids[i]);
				refDataset.addRefEntity(refEntity);
			}
			refDatasets.add(refDataset);
		}
		if (refDatasets == null&&StringUtil.isEmpty(refEntityIds)) {
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

	public VEntity() {
		this.entityType = IDatasetConstant.ENTITYTYPE_VENTITY;
	}

	/**
	 * 获得view集合
	 */
	public Collection getViews() {
		if (views == null) {
			views = new ArrayList();
		}
		return views;
	}

	public void setViews(Collection views) {
		this.views = views;
	}

	/**
	 * 读取xml，即dataset
	 */
	public void fromXML(Object object) {
		if (!(object instanceof Element)) {
			return;
		}

		Element element = (Element) object;
		this.loadEntity(element);

		this.setType(XMLUtil.getAttribute(element, IDatasetConstant.TYPE));
		this.setRefEntityIds(XMLUtil.getAttribute(element,
				IDatasetConstant.REF_ENTITY_IDS));

		Iterator sqlIterator = XMLUtil.getChildElements(element,
				IDatasetConstant.SQL).iterator();
		while (sqlIterator.hasNext()) {
			Element sqlElement = (Element) sqlIterator.next();
			IView view = new SQLView();
			view.fromXML(sqlElement);
			view.setParentEntity(this);
			this.getViews().add(view);
		}
		Element fieldsElem = XMLUtil.getChildElement(element,
				IDatasetConstant.FIELDS);
		Iterator fieldIterator = XMLUtil.getChildElements(fieldsElem,
				IDatasetConstant.FIELD).iterator();

		while (fieldIterator.hasNext()) {
			Element fieldElem = (Element) fieldIterator.next();
			IVField field = new VField();
			field.fromXML(fieldElem);
			this.addField(field);
			field.setParentEntity(this);
		}
		// load查询实体数据集
		Element refVentityFieldsElem = XMLUtil.getChildElement(element,
				IDatasetConstant.REF_VENTITYFIELDS);
		if (refVentityFieldsElem != null) {
			Iterator iterator = XMLUtil.getChildElements(refVentityFieldsElem,
					IDatasetConstant.REF_DATASET).iterator();
			while (iterator.hasNext()) {
				Element DatasetElem = (Element) iterator.next();
				IRefDataset refDataset = new RefDataset();
				refDataset.fromXML(DatasetElem);
				refDataset.setProjectPath(dataset.util.StringUtil.getProjectPathFormPath(this.getParentDataset().getReferenceFilePath()));
				this.addRefDataset(refDataset);
				refDataset.setParentEntity(this);
			}
		}
		// load实体展示模型
		Element viewModelElem = XMLUtil.getChildElement(element,
				IDatasetConstant.VIEW_MODEL);

		if (viewModelElem != null) {
			// 查询信息
			Element formElem = XMLUtil.getChildElement(viewModelElem,
					IDatasetConstant.QUERY_FORM);
			if (formElem != null) {
				this.queryForm = new QueryForm();
				this.queryForm.fromXML(formElem);
				this.queryForm.setParentEntity(this);
			}
			// 查询结果信息
			formElem = XMLUtil.getChildElement(viewModelElem,
					IDatasetConstant.RESULT_FORM);
			if (formElem != null) {
				this.resultForm = new ResultForm();
				this.resultForm.fromXML(formElem);
				this.resultForm.setParentEntity(this);
			}
			// 录入信息
			formElem = XMLUtil.getChildElement(viewModelElem,
					IDatasetConstant.ADD_FORM);
			if (formElem != null) {
				this.newForm = new NewForm();
				this.newForm.fromXML(formElem);
				this.newForm.setParentEntity(this);
			}
			// 详细信息
			formElem = XMLUtil.getChildElement(viewModelElem,
					IDatasetConstant.DETAIL_IFNO_FORM);
			if (formElem != null) {
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
		Element element = XMLUtil.createElement(document,
				IDatasetConstant.ENTITY);
		this.buildElement(element);

		XMLUtil.addAttribute(element, IDatasetConstant.TYPE, this.getType());
		XMLUtil.addAttribute(element, IDatasetConstant.REF_ENTITY_IDS,
				this.getRefEntityIds());

		Iterator viewIterator = this.getViews().iterator();
		while (viewIterator.hasNext()) {
			IView view = (IView) viewIterator.next();
			XMLUtil.addChildElement(element, view.toXML(document));
		}
		//添加依赖数据集和查询实体 modify chenhua
        Element refVentityFieldsElem = XMLUtil.createElement(element.getOwnerDocument(),
				IDatasetConstant.REF_VENTITYFIELDS);
		XMLUtil.addChildElement(element, refVentityFieldsElem);

		Iterator refDatasetIterator = this.getRefDatasets().iterator();
		while (refDatasetIterator.hasNext()) {
			IRefDataset refDataset = (IRefDataset) refDatasetIterator.next();
			XMLUtil.addChildElement(refVentityFieldsElem,refDataset.toXML(element.getOwnerDocument()));
		}
		//添加显示模型modify chenhua
		Element viewModelElem = XMLUtil.createElement(document,
				IDatasetConstant.VIEW_MODEL);
		XMLUtil.addChildElement(element, viewModelElem);

		if (queryForm != null) {
			XMLUtil.addChildElement(viewModelElem, queryForm.toXML(document));
		}
		if (resultForm != null) {
			XMLUtil.addChildElement(viewModelElem, resultForm.toXML(document));
		}
		if (detailInfoForm != null) {
			XMLUtil.addChildElement(viewModelElem,
					detailInfoForm.toXML(document));
		}
		if (newForm != null) {
			XMLUtil.addChildElement(viewModelElem, newForm.toXML(document));
		}

		return element;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Collection getRefEntities() {
		if (refEntities == null) {
			refEntities = DatasetUtil.getEntitys(this.getParentDataset(), this
					.getRefEntityIds().split(","));
		}
		return refEntities;
	}

	/**
	 * 获取查询实体引用的持久化实体ID（实体ID之间用 ',' 分隔）
	 * 
	 * @return
	 */
	public String getRefEntityIds() {
		if (refEntityIds == null) {
			refEntityIds = "";
		}
		return refEntityIds;
	}

	/**
	 * 设置查询实体引用的持久化实体ID（实体ID之间用 ',' 分隔）
	 * 
	 * @param refEntityIds
	 */
	public void setRefEntityIds(String refEntityIds) {
		this.refEntityIds = refEntityIds;
	}

	public Map<String, String> getQuerySql() {
		String lineSeparator = "\r\n";
		String tab = "\t\t";
		boolean hasPk = false;
		StringBuffer sqlQuery = new StringBuffer();

		Iterator iterator = this.getQueryForm().getFields().iterator();
		while (iterator.hasNext()) {
			IQueryField queryField = (IQueryField) iterator.next();
			String fieldName = queryField.getFieldNameInMultiValueCase();
			String operator = queryField.getOperator();
			// 手动添加的查询实体字段需用户自己在hbm文件中添加
			if (queryField.getRefEntity() != null) {
				String table = ((IPEntity) queryField.getRefEntity())
						.getTable();
				String colName = table + "."
						+ ((IPField) queryField.getRefField()).getColumnName();
				if (!hasPk) {
					hasPk = ((IPField) queryField.getRefField()).isPK();
				}

				if (IDatasetConstant.OPERATOR_LIKE_LEFT.equals(operator)) {
					sqlQuery.append(lineSeparator + tab + "#if(${" + fieldName
							+ "}) AND " + colName + " "
							+ IDatasetConstant.OPERATOR_LIKE
							+ " ${sqlUtil.leftLike(\":" + fieldName
							+ "\")} #end ");
				} else if (IDatasetConstant.OPERATOR_LIKE_RIGHT
						.equals(operator)) {
					sqlQuery.append(lineSeparator + tab + "#if(${" + fieldName
							+ "}) AND " + colName + " "
							+ IDatasetConstant.OPERATOR_LIKE
							+ " ${sqlUtil.rightLike(\":" + fieldName
							+ "\")} #end ");
				} else if (IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT
						.equals(operator)) {
					sqlQuery.append(lineSeparator + tab + "#if(${" + fieldName
							+ "}) AND " + colName + " "
							+ IDatasetConstant.OPERATOR_LIKE
							+ " ${sqlUtil.like(\":" + fieldName + "\")} #end ");
				} else if (IDatasetConstant.OPERATOR_IN
						.equalsIgnoreCase(operator)
						|| IDatasetConstant.OPERATOR_NOT_IN
								.equalsIgnoreCase(operator)) {
					sqlQuery.append(lineSeparator + tab + "#if(${" + fieldName
							+ "}) AND " + colName + " " + operator + "( :"
							+ fieldName + ") #end ");
				} else {
					sqlQuery.append(lineSeparator + tab + "#if(${" + fieldName
							+ "}) AND " + colName + " " + operator + " :"
							+ fieldName + " #end ");
				}
			}
		}
		if (!hasPk) {
			iterator = getPKFields().iterator();
			while (iterator.hasNext()) {
				IVField vField = (IVField) iterator.next();
				String fieldName = vField.getFieldName();
				String table = "";
				table = ((IPEntity) vField.getRefEntity()).getTable();
				String colName = table + "." + vField.getColumnName();
				sqlQuery.append(lineSeparator + tab + "#if(${" + fieldName
						+ "}) AND " + colName + " = :" + fieldName + " #end ");
			}
		}

		Map<String, String> sqls = new TreeMap<String, String>();
		iterator = this.getViews().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			IView view = (IView) iterator.next();
			if (StringUtil.isNotBlank(view.getContent())) {
				sqls.put(view.getDialect(),
						view.getContent() + sqlQuery.toString());
			}
		}
		return sqls;
	}

}
