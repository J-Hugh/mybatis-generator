package autocode.util.auxiliary.vm.relationtable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import autocode.util.StringUtil;
import autocode.util.TagUtil;
import autocode.util.auxiliary.vm.VmVarBase;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IVEntity;
import dataset.viewmodel.IAddField;
import dataset.viewmodel.IQueryField;
import dataset.viewmodel.ITag;
import dataset.viewmodel.ITagAttribute;
import dataset.viewmodel.IVMField;

/**
 * 
 * @author BizFoundation Team: LiuJun
 * 
 *         {注释}
 * 
 * @version 1.0
 * @since 4.3
 */
public abstract class VmVarViewBaseInfo extends VmVarBase {
	protected List relationEntityViewInfos = new ArrayList();
	private static final String ATTR_VALUE = "value";
	private static final String SEPARATOR = "separator";

	public VmVarViewBaseInfo(IEntity entity, String title) {
		this.entity = entity;
		this.title = title;
		this.formId = getlowerFirstEntityName() + "Form";
	}

	/**
	 * 获取字段的页面录入组件HTML定义字符串
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldTagHtml(IVMField field) {
		if (entity.getPKFields().size() > 1) {
			IField iField = getField(entity.getFields(), field.getFieldName());
			if (iField.isPK()) {
				return TagUtil.buildHtml(getEntityName(true) + "."
						+ getlowerFirstEntityPkName(), field);
			}
		}
		// 转html前,将多值的查询字段名称加入后缀 by bruce 20121210
		if (field instanceof IQueryField) {
			Map resetAttr = new HashMap();
			resetAttr.put("id",
					((IQueryField) field).getFieldNameInMultiValueCase());
			resetAttr.put("name",
					((IQueryField) field).getFieldNameInMultiValueCase());
			return TagUtil.buildHtml(getEntityName(true), field, resetAttr);
		}

		return TagUtil.buildHtml(getEntityName(true), field);
	}

	/**
	 * @Title: getFieldTagHtmlNoValueAttr
	 * @Description: 获取字段的页面录入组件HTML定义字符串 不默认生成通过EL表达式回填数据的Value属性
	 * @param field
	 * @return 参数及返回值
	 * @return String 返回类型
	 * @throws
	 */
	public String getFieldTagHtmlNoValueAttr(IVMField field) {
		if (entity.getPKFields().size() > 1) {
			IField iField = getField(entity.getFields(), field.getFieldName());
			if (iField.isPK()) {
				return TagUtil.buildHtml(getEntityName(true) + "."
						+ getlowerFirstEntityPkName(), field, null, false);
			}
		}
		// 转html前,将多值的查询字段名称加入后缀 by bruce 20121210
		if (field instanceof IQueryField) {
			Map resetAttr = new HashMap();
			resetAttr.put("id",
					((IQueryField) field).getFieldNameInMultiValueCase());
			resetAttr.put("name",
					((IQueryField) field).getFieldNameInMultiValueCase());
			return TagUtil.buildHtml(getEntityName(true), field, resetAttr,
					false);
		}

		return TagUtil.buildHtml(getEntityName(true), field, null, false);
	}

	/**
	 * 获取字段的页面录入组件Script定义字符串
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldTagScript(IVMField field) {
		if (entity.getPKFields().size() > 1) {
			IField iField = getField(entity.getFields(), field.getFieldName());
			if (iField.isPK()) {
				return TagUtil.buildScript(getEntityName(true) + "."
						+ getlowerFirstEntityPkName(), field, formId, null,
						true);
			}
		}
		// 转js前,将多值的查询字段名称加入后缀 by bruce 20121210
		if (field instanceof IQueryField) {
			Map resetAttr = new HashMap();
			resetAttr.put("id",
					((IQueryField) field).getFieldNameInMultiValueCase());
			return TagUtil.buildScript(getEntityName(true), field, formId,
					resetAttr, true);
		}
		return TagUtil.buildScript(getEntityName(true), field, formId, null,
				true);
	}

	/**
	 * @Title: getFieldTagScriptNoValueAttr
	 * @Description: 获取字段的页面录入组件Script定义字符串， 不默认生成通过EL表达式回填数据的Value属性
	 * @param field
	 * @return 参数及返回值
	 * @return String 返回类型
	 * @throws
	 */
	public String getFieldTagScriptNoValueAttr(IVMField field) {
		if (entity.getPKFields().size() > 1) {
			IField iField = getField(entity.getFields(), field.getFieldName());
			if (iField.isPK()) {
				return TagUtil.buildScript(getEntityName(true) + "."
						+ getlowerFirstEntityPkName(), field, formId, null,
						false);
			}
		}
		// 转js前,将多值的查询字段名称加入后缀 by bruce 20121210
		if (field instanceof IQueryField) {
			Map resetAttr = new HashMap();
			resetAttr.put("id",
					((IQueryField) field).getFieldNameInMultiValueCase());
			resetAttr.put("name",
					((IQueryField) field).getFieldNameInMultiValueCase());
			return TagUtil.buildScript(getEntityName(true), field, formId,
					resetAttr, false);
		}
		
		
		return TagUtil.buildScript(getEntityName(true), field, formId, null,
				false);
	}

	/**
	 * 获取字段的页面录入组件类型
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldTagType(IVMField field) {
		return field.getTag().getTagType();
	}

	/**
	 * 获取首字母小写的实体名称
	 * 
	 * @return
	 */
	public String getlowerFirstEntityName() {
		return StringUtil.lowerFirst(entity.getName());
	}

	/**
	 * 获取实体名称
	 * 
	 * @param lowerFirst
	 *            实体名称首字母是否小写 true:小写 false：大写
	 * @return
	 */
	public String getEntityName(boolean lowerFirst) {
		if (lowerFirst) {
			return StringUtil.lowerFirst(entity.getName());
		}
		return StringUtil.upperFirst(entity.getName());
	}

	/**
	 * 获取实体名称
	 * 
	 * @return
	 */
	public String getEntityName() {
		return StringUtil.upperFirst(entity.getName());
	}

	public String getPEntityName(IVMField field, boolean lowerFirst) {
		if (lowerFirst) {
			return StringUtil.lowerFirst(field.getRefEntity().getName());
		}
		return StringUtil.upperFirst(field.getRefEntity().getName());
	}

	public String getPEntityName(IVMField field) {
		return getPEntityName(field, false);
	}

	protected String getlowerFirstEntityPkName() {
		return StringUtil.lowerFirst(entity.getName() + "Pk");
	}

	public String getTitle() {
		if (this.isUseI18N) {
			return "<fmt:message key=\"" + getEntityName(true)
					+ ".list.title\"/>";
		}
		if (StringUtil.isBlank(title)) {
			return entity.getDisplayName() + "列表";
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取字段的<c:out/> 字符串 示例：<c:out value='${user.name}'/>
	 * 
	 * @param field
	 * @return
	 */
	public String getCoutValue(IVMField field) {
		ITag tag = field.getTag();
		boolean isValueDef = false;
		String separator = "";
		Iterator iterator = tag.getAttributes().iterator();
		while (iterator.hasNext()) {
			ITagAttribute attr = (ITagAttribute) iterator.next();
			if (!isValueDef && attr.getName().toLowerCase().equals(ATTR_VALUE)) {
				isValueDef = true;
			}
			if (attr.getName().toLowerCase().equals(SEPARATOR)) {
				separator = attr.getAttrValue();
			}
		}
		if (!isValueDef) {
			String attrSeparator = "";
			String fieldName = field.getFieldName();
			if (tag.getTagType().equalsIgnoreCase("checkbox")) {
				if (!StringUtil.isBlank(separator)) {
					attrSeparator = ",".equals(separator) ? "" : " separator='"
							+ separator + "' ";
				}
			}
			return "<biz:out value='${" + getEntityName(true) + "." + fieldName
					+ "}'" + attrSeparator + "/>";
		}
		return "<biz:out value='${" + getEntityName(true) + "."
				+ field.getFieldName() + "}'/>";
	}

	/**
	 * 获取字段的<c:out/> 字符串 示例：<c:out value='${user.name}'/>
	 * 
	 * @param field
	 * @return
	 */
	public String getCoutValue(IField field) {
		if (entity.getPKFields().size() > 1) {
			IField iField = getField(entity.getFields(), field.getFieldName());
			if (iField.isPK()) {
				return "<biz:out value='${" + getlowerFirstEntityName() + "."
						+ getlowerFirstEntityPkName() + "."
						+ field.getFieldName() + "}'/>";
			}
		}
		return "<biz:out value='${" + getlowerFirstEntityName() + "."
				+ field.getFieldName() + "}'/>";
	}

	/**
	 * 根据属性（字段）名称获取属性（字段）
	 * 
	 * @param fieldName
	 *            属性（字段）名称
	 * @return
	 */
	protected IVMField getVMField(Collection fields, String fieldName) {
		if (fields == null || StringUtil.isBlank(fieldName)) {
			return null;
		}
		Iterator iterator = fields.iterator();
		while (iterator.hasNext()) {
			IVMField field = (IVMField) iterator.next();
			if (field.getFieldName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 根据属性（字段）名称获取属性（字段）
	 * 
	 * @param fieldName
	 *            属性（字段）名称
	 * @return
	 */
	protected IField getField(Collection fields, String fieldName) {
		if (fields == null || StringUtil.isBlank(fieldName)) {
			return null;
		}
		Iterator iterator = fields.iterator();
		while (iterator.hasNext()) {
			IField field = (IField) iterator.next();
			if (field.getFieldName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 获取字段显示名称 1.如果使用国际化则返回字段的国际化定义 示例：<fmt:message key="user.name"/>
	 * 2.如果没有使用国际化，则返回展示模型定义的字段名称
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldDisplayName(IVMField field) {
		if (field == null) {
			return null;
		}
		if (isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName() + "."
					+ field.getFieldName() + "\"/>";
		}
		return field.getDisplayName();
	}

	/**
	 * 获取字段显示名称 1.如果使用国际化则返回字段的国际化定义 示例：<fmt:message key="user.name"/>
	 * 2.如果没有使用国际化，则返回展示模型定义的字段名称
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldDisplayName(IField field) {
		if (field == null) {
			return null;
		}
		if (isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName() + "."
					+ field.getFieldName() + "\"/>";
		}
		return field.getDisplayName();
	}

	/**
	 * 获取字段名称
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldName(IVMField field) {
		if (field == null) {
			return null;
		}
		return field.getFieldName();
	}

	/**
	 * 获取字段名称
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldName(IField field) {
		if (field == null) {
			return null;
		}
		return field.getName();
	}

	/**
	 * 获取持久化实体主键字段
	 * 
	 * @return
	 */
	public Collection getPKFields() {
		return entity.getPKFields();
	}

	/**
	 * 获取经过pkFieldFilters过滤后的持久化实体的主键字段 即：获取持久化实体中那些不在pkFieldFilters集合中的主键字段
	 * 
	 * @param pkFieldFilters
	 *            主键字段过滤集合
	 * @return
	 */
	public Collection getPKFields(Collection pkFieldFilters) {
		Collection pkFields = new ArrayList();
		if (pkFieldFilters == null || pkFieldFilters.size() == 0) {
			return entity.getPKFields();
		}
		Iterator iterator = entity.getPKFields().iterator();
		while (iterator.hasNext()) {
			IField field = (IField) iterator.next();
			if (this.getVMField(pkFieldFilters, field.getName()) == null) {
				pkFields.add(field);
			}
		}
		return pkFields;
	}

	/**
	 * 获取查询列表页面模版Context
	 * 
	 * @return
	 */
	public VmVarViewBaseInfo getListInfo() {
		if (this instanceof VmVarViewListInfo) {
			return this;
		} else {
			VmVarViewListInfo listInfo = new VmVarViewListInfo(
					(IVEntity) this.entity);
			listInfo.setUseI18N(isUseI18N);
			listInfo.setPageEncoding(pageEncoding);
			listInfo.setContentKey("listInfo");
			return listInfo;
		}
	}

	/**
	 * 获取编辑页面模版Context
	 * 
	 * @return
	 */
	public VmVarViewBaseInfo getFormInfo() {
		if (this instanceof VmVarViewFormInfo) {
			return this;
		} else {
			VmVarViewFormInfo formInfo = new VmVarViewFormInfo(
					(IVEntity) this.entity);
			formInfo.setUseI18N(isUseI18N);
			formInfo.setPageEncoding(pageEncoding);
			formInfo.setContentKey("formInfo");
			return formInfo;
		}
	}

	/**
	 * 获取详细页面模版Context
	 * 
	 * @return
	 */
	public VmVarViewBaseInfo getShowInfo() {
		if (this instanceof VmVarViewShowInfo) {
			return this;
		} else {
			VmVarViewShowInfo showInfo = new VmVarViewShowInfo(
					(IVEntity) this.entity);
			showInfo.setUseI18N(isUseI18N);
			showInfo.setPageEncoding(pageEncoding);
			showInfo.setContentKey("showInfo");
			return showInfo;
		}
	}

	/**
	 * @Title: getFieldParent
	 * @Description: 获取字段的父字段(针对级联下拉框组件，后期如果有扩展其他级联组件，可扩展此方法)
	 * @param field
	 * @return String 父子段的name(注意，不是ID)
	 * @throws
	 */
	protected String getFieldParent(IVMField field) {
		ITag tag = field.getTag();
		boolean skip = false;
		String result = null;
		if (tag.getTagType().equals("select")) {
			Iterator aIt = tag.getAttributes().iterator();
			while (aIt.hasNext()) {
				ITagAttribute attribute = (ITagAttribute) aIt.next();

				if (attribute.getName().equals("parent")) {
					result = attribute.getAttrValue();
				}
			}
		}
		return result;
	}

	/**
	 * @Title: isCascadeSelect
	 * @Description: 判断控件是否是级联下拉框
	 * @param field
	 * @return 参数及返回值
	 * @return boolean 返回类型
	 * @throws
	 */
	protected boolean isCascadeSelect(IVMField field) {
		ITag tag = field.getTag();
		boolean flag = false;
		if (tag.getTagType().equals("select")) {
			Iterator aIt = tag.getAttributes().iterator();
			while (aIt.hasNext()) {
				ITagAttribute attribute = (ITagAttribute) aIt.next();
				if (attribute.getName().equals("isCascade"))
					flag = true;
			}
		}
		return flag;
	}
}
