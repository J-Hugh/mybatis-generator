package autocode.util.auxiliary.vm.relationtable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import autocode.util.StringUtil;
import autocode.util.TagUtil;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IVEntity;
import dataset.viewmodel.IAddField;
import dataset.viewmodel.IForm;
import dataset.viewmodel.INewForm;
import dataset.viewmodel.IVMField;

public class VmVarViewFormInfo extends VmVarViewBaseInfo {

	private INewForm form;
	private int fieldsLength;

	private List refEntities;

	// private String showType;

	public VmVarViewFormInfo(IVEntity entity) {
		super(entity, entity.getNewForm().getTitle());
		form = entity.getNewForm();
		fieldsLength = form.getFields().size();
		refEntities = new ArrayList();
	}

	/**
	 * 返回当前的显示模型定义
	 */
	public IForm getViewModel() {
		return form;
	}

	/**
	 * 获取录入字段集合
	 * 
	 * @return
	 */
	public Collection getFields() {
		return form.getFields();
	}
	
	public List getRefEntities() {
		return refEntities;
	}

	public Map<Object, List> getFieldsAsGroup() {
		Map<Object, List> group = new HashMap();

		Iterator iterator = form.getFields().iterator();
		while (iterator.hasNext()) {
			IVMField field = (IVMField) iterator.next();
			IEntity refEntity = field.getRefEntity();
			if (!refEntities.contains(refEntity)) {
				refEntities.add(refEntity);
			}
			if (group.containsKey(field.getRefEntity())) {
				group.get(refEntity).add(field);
			} else {
				List fields = new ArrayList();
				fields.add(field);
				group.put(refEntity, fields);
			}
		}
		return group;
	}

	/**
	 * 获取查询实体引用的所有持久化实体的主键字段集合
	 * 
	 * @return
	 */
	public Collection getHidePKFields() {
		Collection hidePKFields = new ArrayList();
		Iterator iterator = form.getFields().iterator();
		Collection entities = new ArrayList();
		while (iterator.hasNext()) {
			IVMField vField = (IVMField) iterator.next();
			IField field = vField.getRefField();
			IEntity entity = field.getParentEntity();
			if (!entities.contains(entity)) {
				entities.add(entity);
				if(field.isPK()){
					hidePKFields.add(field);
				}
			}
		}
		return hidePKFields;
	}

	/**
	 * 构建字段的input定义，类型为：hidden 例如<input type="hidden" name="user.id"
	 * value="<c:out value='${user.id}'/>"/>
	 * 
	 * @param field
	 * @return
	 */
	public String buildHideFieldHtml(IField field) {
		IEntity entity = field.getParentEntity();
		String entityName = StringUtil.lowerFirst(entity.getName());
		String id = entityName + "_" + field.getName();
		String fieldName = entityName + "." + field.getName();
		return "<input type=\"hidden\" id=\"" + id + "\" name=\"" + fieldName
				+ "\" type=\"text\" value=\"<c:out value=\'${" + fieldName
				+ "}\'/>\"/>";
	}

	/**
	 * 获取展示方式： tab、group、common
	 * 
	 * @return
	 */
	public String getShowType() {
		return form.getShowType();
	}

	/**
	 * 获取录入字段的个数
	 * 
	 * @return
	 */
	public int getFieldsLength() {
		return fieldsLength;
	}

	public String getEntityTitle(IEntity entity) {
		if (entity == null) {
			return "";
		}
		if (this.isUseI18N) {
			return "<fmt:message key=\""
					+ StringUtil.lowerFirst(entity.getName())
					+ ".form.title\"/>";
		}
		return entity.getDisplayName();
	}

	/**
	 * 获取字段显示名称 1.如果使用国际化则返回字段的国际化定义 示例：<fmt:message key="user.newForm.name"/>
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
			return "<fmt:message key=\"" + getlowerFirstEntityName() + ".form."
					+ field.getFieldName() + "\"/>";
		}
		return field.getDisplayName();
	}

	/**
	 * 获取字段的页面录入组件HTML定义字符串
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldTagHtml(IVMField field) {
		Map resetAttrs = new HashMap();
		String entityName = StringUtil.lowerFirst(field.getRefEntity()
				.getName());
		resetAttrs.put("id", entityName + "_" + field.getFieldName());
		resetAttrs.put("name", entityName + "." + field.getFieldName());
		return TagUtil.buildHtml(entityName, field, resetAttrs, true);
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
		Map resetAttrs = new HashMap();
		String entityName = StringUtil.lowerFirst(field.getRefEntity()
				.getName());
		resetAttrs.put("id", entityName + "_" + field.getFieldName());
		resetAttrs.put("name", entityName + "." + field.getFieldName());
		return TagUtil.buildHtml(entityName, field, resetAttrs, false);
	}

	/**
	 * 获取字段的页面录入组件Script定义字符串
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldTagScript(IVMField field) {
		Map resetAttrs = new HashMap();
		String entityName = StringUtil.lowerFirst(field.getRefEntity()
				.getName());
		resetAttrs.put("id", entityName + "_" + field.getFieldName());
		resetAttrs.put("name", entityName + "." + field.getFieldName());
		// 针对级联下拉框，修正级联下拉框的parent属性
		if (field instanceof IAddField && isCascadeSelect(field)) {
			String id = null;
			String parentId = getFieldParent(field);
			id = entityName + "_" + parentId;
			resetAttrs.put("parent", id);
			return TagUtil.buildScript(entityName, field, formId, resetAttrs);
		}
		return TagUtil.buildScript(entityName, field, formId, resetAttrs);
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
		Map resetAttrs = new HashMap();
		String entityName = StringUtil.lowerFirst(field.getRefEntity()
				.getName());
		resetAttrs.put("id", entityName + "_" + field.getFieldName());
		resetAttrs.put("name", entityName + "." + field.getFieldName());
		// 针对级联下拉框，修正级联下拉框的parent属性
		if (field instanceof IAddField && isCascadeSelect(field)) {
			String id = null;
			String parentId = getFieldParent(field);
			id = entityName + "_" + parentId;
			resetAttrs.put("parent", id);
			return TagUtil.buildScript(entityName, field, formId, resetAttrs,
					false);
		}
		return TagUtil
				.buildScript(entityName, field, formId, resetAttrs, false);
	}

	/**
	 * 获取页面标题
	 */
	public String getTitle() {
		if (this.isUseI18N) {
			return "<fmt:message key=\"" + getEntityName(true)
					+ ".form.title\"/>";
		}
		if (StringUtil.isBlank(title)) {
			return "录入" + entity.getDisplayName();
		}
		return title;
	}

	public String getPEntityName(IField field) {
		return StringUtil.lowerFirst(field.getParentEntity().getName());
	}
}
