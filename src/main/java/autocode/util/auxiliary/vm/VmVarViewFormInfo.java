package autocode.util.auxiliary.vm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import autocode.util.JqGridUtil;
import autocode.util.StringUtil;
import autocode.util.TagUtil;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IPEntity;
import dataset.model.impl.Field;
import dataset.viewmodel.IAddField;
import dataset.viewmodel.IForm;
import dataset.viewmodel.INewForm;
import dataset.viewmodel.IVMField;
import dataset.viewmodel.impl.VMField;

public class VmVarViewFormInfo extends VmVarViewBaseInfo {

	private INewForm form;
	private int fieldsLength;
	private String showType;

	public VmVarViewFormInfo(IPEntity entity) {
		super(entity);
		form = this.entity.getNewForm();
		fieldsLength = form.getFields().size();
		showType = form.getShowType();
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
	
	public Collection getNotPkFields() {
		List<VMField> tmpVmFields = new ArrayList<VMField>();
		List<VMField> vmFields = (List<VMField>)form.getFields();
		List<Field> fields = (List<Field>)getPKFields();
		for (VMField vmField : vmFields) {
			for (Field field : fields) {
				if(vmField.getFieldId().equals(field.getId())){
					tmpVmFields.add(vmField);
				}
			}
		}
		if(tmpVmFields.size() > 0){
			vmFields.removeAll(tmpVmFields);
		}
		return vmFields;
	}

	/**
	 * 获取录入字段的个数
	 * 
	 * @return
	 */
	public int getFieldsLength() {
		return fieldsLength;
	}

	/**
	 * 获取查询实体引用的所有持久化实体的主键字段集合
	 * 
	 * @return
	 */
	public Collection getHidePKFields() {
		return entity.getPKFields();
	}

	/**
	 * 获取展示方式： tab、group、common
	 * 
	 * @return
	 */
	public String getShowType() {
		return this.showType;
	}

	/**
	 * 设置展示方式： tab、group、common
	 * 
	 * @param showType
	 */
	public void setShowType(String showType) {
		this.showType = showType;
	}

	/**
	 * 设置关联实体的视图信息
	 * 
	 * @param detailPEntitys
	 *            关联的持久化实体集合（列表元素类型为：IPEntity）
	 */
	public void setRelationEntityViewInfos(List detailPEntitys) {
		if (detailPEntitys == null || detailPEntitys.isEmpty()) {
			return;
		}
		relationEntityViewInfos.clear();
		Iterator iterator = detailPEntitys.iterator();
		while (iterator.hasNext()) {
			IPEntity entity = (IPEntity) iterator.next();
			VmVarViewFormInfo info = new VmVarViewFormInfo(entity);
			info.setUseI18N(isUseI18N);
			info.setShowType(this.getShowType());
			relationEntityViewInfos.add(info);
		}
	}

	/**
	 * 构建字段的input定义，类型为：hidden 例如<input type="hidden" name="id"
	 * value="<c:out value='${user.id}'/>"/>
	 * 
	 * @param field
	 * @return
	 */
	public String buildHideFieldHtml(IField field) {
		IEntity entity = field.getParentEntity();
		String entityName = StringUtil.lowerFirst(entity.getName());
		String id = "edit_" + field.getName();
		String fieldName = field.getName();
		String pk = ".";
		if(field.isPK() && entity.getPKFields().size() > 1){//处理联合组件生成的情况
			pk = "." + entityName + "Pk.";
		}
		return "<input type=\"hidden\" id=\"" + id + "\" name=\"" + fieldName
				+ "\" type=\"text\" value=\"<c:out value=\'${" + entityName
				+ pk + fieldName + "}\'/>\"/>";
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
	 * @Title: getEditFieldTagHtml
	 * @Description: 获取字段的页面录入组件HTML定义字符串 不默认生成通过EL表达式回填数据的Value属性
	 * @param field
	 * @return 参数及返回值
	 * @return String 返回类型
	 * @throws
	 */
	public String getEditFieldTagHtml(IVMField field) {

		Map resetAttrs = new HashMap();
		String entityName = StringUtil.lowerFirst(entity.getName());

		resetAttrs.put("id", "edit_" + field.getFieldName());
		return TagUtil.buildHtml(entityName, field, resetAttrs, false);
	}

	/**
	 * 获取字段的页面录入组件Script定义字符串
	 * 
	 * @param field
	 * @return
	 */
	public String getEditFieldTagScript(IVMField field) {
		Map resetAttrs = new HashMap();
		String entityName = StringUtil.lowerFirst(entity.getName());
		resetAttrs.put("id", "edit_" + field.getFieldName());
		// 针对级联下拉框，修正级联下拉框的parent属性
		if (field instanceof IAddField && isCascadeSelect(field)) {
			String id = null;
			String parentId = getFieldParent(field);
			id = "edit_" + parentId;
			resetAttrs.put("parent", id);
			return TagUtil.buildScript(entityName, field, formId, resetAttrs,
					false);
		}
		return TagUtil
				.buildScript(entityName, field, formId, resetAttrs, false);
	}

	/**
	 * 构建JqGrid定义
	 * 
	 * @return
	 */
	public String buildJqGrid(String url) {
		if ("tab".equals(getShowType())) {
			return buildJqGrid(form, url, null);
		}
		return buildJqGrid(form, url);
	}

	/**
	 * 获取录入页面JqGrid的ColModel定义的相关模版参数
	 * 
	 * @return
	 */
	public Map getColumnMap() {
		JqGridUtil gridUtil = new JqGridUtil((IPEntity) entity, form,
				getlowerFirstEntityName(), "", isUseI18N, "");
		Map colMap = gridUtil.initColModelMap();
		System.out.println(colMap);
		return colMap;
	}

}
