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
import dataset.model.IAssociationField;
import dataset.model.IField;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IRelation;
import dataset.model.impl.PEntity;
import dataset.model.impl.PField;
import dataset.model.impl.Relation;
import dataset.viewmodel.IForm;
import dataset.viewmodel.IQueryField;
import dataset.viewmodel.ITag;
import dataset.viewmodel.ITagAttribute;
import dataset.viewmodel.IVMField;
import dataset.viewmodel.impl.ListField;

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

	// protected IPEntity entity;
	protected String listTitle = ""; // 列表页面标题
	protected String newTitle = ""; // 新建页面标题
	protected String editTitle = ""; // 编辑页面标题
	protected String showTitle = ""; // 显示页面标题
	private static final String ATTR_VALUE = "value";
	private static final String SEPARATOR = "separator";

	protected String pkName;// 主键名称
	protected List relationEntityViewInfos = new ArrayList();

	public VmVarViewBaseInfo(IPEntity entity) {
		this.entity = entity;
		if (entity.getPKFields().size() == 1) {
			this.pkName = ((IPField) this.entity.getPKFields().iterator()
					.next()).getName();
		} else {
			this.pkName = "compositeKeyStr";
		}
		this.formId = getlowerFirstEntityName() + "Form";
		if (StringUtil.isNotBlank(entity.getResultForm().getTitle())) {
			this.listTitle = entity.getResultForm().getTitle();
		}
		if (StringUtil.isNotBlank(entity.getNewForm().getTitle())) {
			this.newTitle = entity.getNewForm().getTitle();
		}
		if (StringUtil.isNotBlank(entity.getNewForm().getTitle())) {
			this.editTitle = entity.getNewForm().getTitle();
		}
		if (StringUtil.isNotBlank(entity.getDetailInfoForm().getTitle())) {
			this.showTitle = entity.getDetailInfoForm().getTitle();
		}
	}
	
	/**
	 * 获取关联实体显示模型定义信息
	 * 
	 * @return
	 */
	public List getRelationEntityViewInfos() {
		return relationEntityViewInfos;
	}
	
	/**
	 * 获取detailField
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List getDetailFields(){
		List<Relation> relations  = (List<Relation>)((PEntity)entity).getSrcRelations();
		List fields = new ArrayList();
		for(int i = 0; i <relations.size();i++){
			if(i == 0){
				Relation relation = relations.get(i);
				PEntity tgtEntity = (PEntity)relation.getTgtEntity();
				fields.addAll(tgtEntity.getPKFields());
				fields.addAll(tgtEntity.getFields());
				return fields;
			}
		}
		return fields;
	}
	
	/**
	 * 获取MasterField
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public List getMasterFields(){
		List fields = new ArrayList();
		fields.addAll(entity.getPKFields());
		fields.addAll(entity.getFields());
		return fields;
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
	 * 判断是否显示主键列
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public boolean isShowPkFields(){
		List<ListField> resultForm = (List<ListField>)entity.getResultForm().getFields();
		List<PField> pFields = (List<PField>)entity.getPKFields();
		for (ListField listField : resultForm) {
			for (PField pField : pFields) {
				if(listField.getFieldId().equals(pField.getId())){
					return false;
				}
			}
		}
		return true;
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

			if (iField instanceof IPField && ((IPField) iField).isPK()) {
				return TagUtil.buildHtml(getlowerFirstEntityName() + "."
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
			return TagUtil.buildHtml(getlowerFirstEntityName(), field,
					resetAttr);
		}

		return TagUtil.buildHtml(getlowerFirstEntityName(), field);
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

			if (iField instanceof IPField && ((IPField) iField).isPK()) {
				return TagUtil.buildHtml(getlowerFirstEntityName() + "."
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
			return TagUtil.buildHtml(getlowerFirstEntityName(), field,
					resetAttr, false);
		}

		return TagUtil.buildHtml(getlowerFirstEntityName(), field, null, false);
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
			if (iField instanceof IPField && ((IPField) iField).isPK()) {
				return TagUtil.buildScript(getlowerFirstEntityName() + "."
						+ getlowerFirstEntityPkName(), field, formId);
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
					resetAttr);
		}
		return TagUtil.buildScript(getlowerFirstEntityName(), field, formId);
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
			if (iField instanceof IPField && ((IPField) iField).isPK()) {
				return TagUtil.buildScript(getlowerFirstEntityName() + "."
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
		return TagUtil.buildScript(getlowerFirstEntityName(), field, formId,
				null, false);
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
	
	public String getLowerFirstDetailEntityName(){
		List<Relation> relations = (List<Relation>)((PEntity)entity).getSrcRelations();
		for (Relation relation : relations) {
			return StringUtil.lowerFirst(relation.getTgtEntity().getName());
		}
		return null;
	}

	/**
	 * 获取实体名称
	 * 
	 * @return
	 */
	public String getEntityName() {
		return StringUtil.upperFirst(entity.getName());
	}

	protected String getlowerFirstEntityPkName() {
		return StringUtil.lowerFirst(entity.getName() + "Pk");
	}

	/**
	 * 获取字段的<c:out/> 字符串 示例：<c:out value='${user.name}'/>
	 * 
	 * @param field
	 * @return
	 */
	public String getCoutValue(IVMField field) {
		IField iField = getField(entity.getFields(), field.getFieldName());
		String pk = ".";
		if(iField.isPK() && entity.getPKFields().size() > 1){
			pk = "." + getlowerFirstEntityPkName() + ".";
		}
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
			return "<biz:out value='${" + getEntityName(true) + pk + fieldName
					+ "}'" + attrSeparator + "/>";
		}
		return "<biz:out value='${" + getEntityName(true) + pk
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
			if (iField instanceof IPField && ((IPField) iField).isPK()) {
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

	public String getPkName() {
		return pkName;
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
			IPField field = (IPField) iterator.next();
			if (this.getVMField(pkFieldFilters, field.getName()) == null) {
				pkFields.add(field);
			}
		}
		return pkFields;
	}

	/**
	 * 构建JqGrid定义
	 * 
	 * @param form
	 *            页面信息
	 * @param url
	 *            jqGrid查询数据的URL地址
	 * @return
	 */
	protected String buildJqGrid(IForm form, String url) {
		String caption = "";
		if (this instanceof VmVarViewListInfo) {
			caption = this.getListTitle();
		} else if (this instanceof VmVarViewFormInfo) {
			caption = this.getFormTitle();
		} else if (this instanceof VmVarViewShowInfo) {
			caption = this.getShowTitle();
		}
		JqGridUtil gridUtil = new JqGridUtil((IPEntity) entity, form,
				getlowerFirstEntityName(), url, isUseI18N, caption);
		return gridUtil.buildJqGrid();
	}

	/**
	 * 构建JqGrid定义
	 * 
	 * @param form
	 *            页面信息
	 * @param url
	 *            jqGrid查询数据的URL地址
	 * @param caption
	 *            标题
	 * @return
	 */
	protected String buildJqGrid(IForm form, String url, String caption) {
		JqGridUtil gridUtil = new JqGridUtil((IPEntity) entity, form,
				getlowerFirstEntityName(), url, isUseI18N, caption);
		return gridUtil.buildJqGrid();
	}

	/**
	 * 根据关联关系源端字段名称获取目标段字段名称（当前实体信息对应源端实体）
	 * 
	 * @param srcFieldName
	 * @param info
	 *            目标端实体信息
	 * @return
	 */
	public String getRelationTgtFieldName(String srcFieldName,
			VmVarViewBaseInfo info) {
		IPEntity tgtentity = (IPEntity) info.entity;
		Iterator iterator = ((IPEntity) this.entity).getSrcRelations()
				.iterator();
		while (iterator.hasNext()) {
			IRelation relation = (IRelation) iterator.next();
			if (relation.getTgtEntityId().equals(tgtentity.getId())) {
				Iterator fIterator = relation.getRelationFields().iterator();
				while (fIterator.hasNext()) {
					IAssociationField field = (IAssociationField) fIterator
							.next();
					if (field.getSrcField().getName().equals(srcFieldName)) {
						return field.getTgtField().getFieldName();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获取查询列表页面标题
	 * 
	 * @return
	 */
	public String getListTitle() {
		if (this.isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName()
					+ ".list.title\"/>";
		}
		if (StringUtil.isBlank(listTitle)) {
			return entity.getDisplayName() + "列表";
		}
		return listTitle;
	}

	/**
	 * 获取新增页面标题
	 * 
	 * @return
	 */
	public String getNewTitle() {
		if (this.isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName()
					+ ".form.title\"/>";
		}
		if (StringUtil.isBlank(newTitle)) {
			return "录入" + entity.getDisplayName();
		}
		return newTitle;
	}

	/**
	 * 获取录入页面标题
	 * 
	 * @return
	 */
	public String getFormTitle() {
		if (this.isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName()
					+ ".form.title\"/>";
		}
		if (StringUtil.isBlank(editTitle)) {
			return "录入" + entity.getDisplayName();
		}
		return editTitle;
	}

	/**
	 * 获取查看页面标题
	 * 
	 * @return
	 */
	public String getShowTitle() {
		if (this.isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName()
					+ ".show.title\"/>";
		}
		if (StringUtil.isBlank(showTitle)) {
			return entity.getDisplayName() + "明细信息";
		}
		return showTitle;
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
					(IPEntity) this.entity);
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
					(IPEntity) this.entity);
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
