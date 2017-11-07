package autocode.util.auxiliary.vm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.util.QueryFilter;
import autocode.util.StringUtil;
import dataset.IDatasetConstant;
import dataset.model.IPEntity;
import dataset.viewmodel.IAddField;
import dataset.viewmodel.IForm;
import dataset.viewmodel.INewForm;
import dataset.viewmodel.IQueryField;
import dataset.viewmodel.IQueryForm;
import dataset.viewmodel.IResultForm;
import dataset.viewmodel.ITag;
import dataset.viewmodel.ITagAttribute;
import dataset.viewmodel.IVMField;

/**
 * 代码生成查询页面生成的VelocityContext内容定义
 * 
 * @author BizFoundation Team: LiuJun
 * 
 *         {注释}
 * 
 * @version 1.0
 * @since 4.3
 */
public class VmVarViewListInfo extends VmVarViewBaseInfo {

	static Logger log = LoggerFactory.getLogger(VmVarViewListInfo.class);

	private IResultForm resultForm;// 查询结果信息
	private IQueryForm queryForm;// 查询条件信息
	private int queryFieldsLength;// 查询条件字段个数
	private int resultFieldsLength; // 查询结果列表字段个数
	private int pageSize; // 分页大小
	private String rowSelectType;// 查询结果列表行选择类型 checkbox radio none
	private String orderFields;// 排序字段
	private String orders; // 排序字段排序规则
	private String queryConditionBuild; // 生成查询条件构建的JavaScript脚本

	private List editFieldInOrder;

	private INewForm form;
	private int fieldsLength;

	public VmVarViewListInfo(IPEntity entity) {
		super(entity);
		this.entity = entity;

		resultForm = entity.getResultForm();
		queryForm = entity.getQueryForm();
		form = this.entity.getNewForm();
		if (queryForm != null) {
			queryFieldsLength = queryForm.getFields().size();
			pageSize = queryForm.getPageSize();
			orderFields = queryForm.getOrderFields();
			orders = queryForm.getOrders();
			queryConditionBuild = buildConditionStr(queryForm);
		}
		if (resultForm != null) {
			resultFieldsLength = resultForm.getFields().size();
			rowSelectType = resultForm.getRowSelectType();
		}
		fieldsLength = form.getFields().size();
		if (form != null) {
			// 写了两种对新增字段的排序算法，sortAddField2使用map进行先根遍历，貌似快一些
			// 两种代码都留着吧，挺好玩的
			sortAddField2();
			// sortAddField();
		}
	}

	/**
	 * 返回当前的显示模型定义
	 */
	public IForm getViewModel() {
		return queryForm;
	}

	/**
	 * 是否行多选
	 * 
	 * @return
	 */
	public boolean multiSelect() {
		String rowSelectType = resultForm.getRowSelectType();
		if (rowSelectType != null && rowSelectType.equalsIgnoreCase("checkbox")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 生成查询条件构建的JavaScript脚本 示例： if(element.name == 'productName'){ searchStr +=
	 * 'LIKES_' + element.name + ':' + '%' + element.value + ',' }
	 * 
	 * @param queryForm
	 * @return
	 */
	private String buildConditionStr(IQueryForm queryForm) {
		String conditions = "";
		Iterator iterator = queryForm.getFields().iterator();
		while (iterator.hasNext()) {
			IQueryField field = (IQueryField) iterator.next();
			Class fieldType = null;
			try {
				fieldType = Class.forName(field.getFieldType());
			} catch (ClassNotFoundException e) {
				fieldType = String.class;
				log.error("查询字段的Java类型设置不正确:" + field.getFieldType(), e);
			}
			String type = QueryFilter.getPropertyTypeKey(fieldType);
			String operator = field.getOperator();
			// 处理LIKE左匹配、右匹配、左右匹配的情况
			if (operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT)
					|| operator.equals(IDatasetConstant.OPERATOR_LIKE_RIGHT)
					|| operator
							.equals(IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT)) {
				operator = IDatasetConstant.OPERATOR_LIKE;
			}
			operator = QueryFilter.MatchType.getKey(operator);

			// String matchType = field.getMatchTYpe(); //左匹配 右匹配 左右匹配

			String condition = "if(element.name == '" + field.getFieldName()
					+ "'){\r\n\t\t\t\t";
			condition += "searchStr += '" + operator + type
					+ "_' + element.name + ':' + ";

			operator = field.getOperator();
			if (operator.equals(IDatasetConstant.OPERATOR_LIKE_RIGHT)) {
				condition += "'%' + element.value + ',';";
			} else if (operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT)) {
				condition += "element.value + '%' + ',';";
			} else if (operator
					.equals(IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT)) {
				condition += "'%' + element.value + '%' + ',';";
			} else {
				condition += "element.value + ',';";
			}
			condition += "\r\n\t\t\t}";
			conditions += condition + "\r\n\t\t\t";
		}
		return conditions;
	}

	public Map getLikeCondition() {
		Map likeCondition = new HashMap();
		Iterator iterator = queryForm.getFields().iterator();
		while (iterator.hasNext()) {
			IQueryField field = (IQueryField) iterator.next();
			String operator = field.getOperator();

			if (IDatasetConstant.OPERATOR_LIKE_LEFT.equals(operator)) {
				likeCondition.put(field.getFieldName(), "value = value + '%';");
			} else if (IDatasetConstant.OPERATOR_LIKE_RIGHT.equals(operator)) {
				likeCondition.put(field.getFieldName(), "value = '%' + value;");
			} else if (IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT
					.equals(operator)) {
				likeCondition.put(field.getFieldName(),
						"value = '%' + value + '%';");
			}
		}
		return likeCondition;
	}

	/**
	 * 获取查询结果字段列表
	 * 
	 * @return
	 */
	public Collection getResultFields() {
		return resultForm.getFields();
	}

	/**
	 * 获取查询条件字段列表
	 * 
	 * @return
	 */
	public Collection getQueryFields() {
		return queryForm.getFields();
	}

	public Collection getEditFieldInOrder() {
		return editFieldInOrder;
	}

	/**
	 * 获取查询结果字段显示名称
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getResultFieldDisplayName(String fieldName) {
		IVMField field = getVMField(resultForm.getFields(), fieldName);
		if (field == null) {
			return null;
		}
		if (isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName()
					+ ".queryResult." + field.getFieldName() + "\"/>";
		}
		return field.getDisplayName();
	}

	/**
	 * 获取查询结果字段显示名称
	 * 
	 * @param field
	 *            查询结果字段
	 * @return
	 */
	public String getResultFieldDisplayName(IVMField field) {
		if (field == null) {
			return null;
		}
		if (isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName()
					+ ".queryResult." + field.getFieldName() + "\"/>";
		}
		return field.getDisplayName();
	}

	/**
	 * 获取查询条件字段显示名称
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getQueryFieldDisplayName(String fieldName) {
		IVMField field = getVMField(queryForm.getFields(), fieldName);
		if (field == null) {
			return null;
		}
		if (isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName()
					+ ".queryForm." + field.getFieldName() + "\"/>";
		}
		return field.getDisplayName();
	}

	/**
	 * 获取查询条件字段显示名称
	 * 
	 * @param field
	 *            查询条件字段
	 * @return
	 */
	public String getQueryFieldDisplayName(IVMField field) {
		if (field == null) {
			return null;
		}
		if (isUseI18N) {
			return "<fmt:message key=\"" + getlowerFirstEntityName()
					+ ".queryForm." + field.getFieldName() + "\"/>";
		}
		return field.getDisplayName();
	}

	/**
	 * 获取查询条件字段个数
	 * 
	 * @return
	 */
	public int getQueryFieldsLength() {
		return queryFieldsLength;
	}

	/**
	 * 获取查询列表中字段个数
	 * 
	 * @return
	 */
	public int getResultFieldsLength() {
		return resultFieldsLength;
	}

	/**
	 * 获取查询结果列表行选择类型 checkbox radio none
	 * 
	 * @return
	 */
	public String getRowSelectType() {
		return rowSelectType;
	}

	/**
	 * 获取分页大小
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 生成查询条件构建的JavaScript脚本 示例： if(element.name == 'productName'){ searchStr +=
	 * 'LIKES_' + element.name + ':' + '%' + element.value + ',' }
	 * 
	 * @return
	 */
	public String getQueryConditionBuild() {
		if (StringUtil.isBlank(queryConditionBuild)) {
			queryConditionBuild = buildConditionStr(queryForm);
		}
		return queryConditionBuild;
	}

	/**
	 * 获取排序字段 示例：name,age
	 * 
	 * @return
	 */
	public String getOrderFields() {
		return orderFields;
	}

	/**
	 * 获取排序规则 示例：desc,asc
	 * 
	 * @return
	 */
	public String getOrders() {
		return orders;
	}

	/**
	 * 构建JqGrid定义
	 * 
	 * @return
	 */
	public String buildJqGrid(String url) {
		return buildJqGrid(entity.getResultForm(), url);
	}

	/**
	 * 获取录入字段集合
	 * 
	 * @return
	 */
	public Collection getFields() {
		return form.getFields();
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
			relationEntityViewInfos.add(info);
		}
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
	 * 构建JqGrid定义
	 * 
	 * @return
	 */
	public String buildFormJqGrid(String url) {
		return buildJqGrid(form, url);
	}

	/**
	 * 弹出窗口页面特殊处理 获取字段的页面录入组件HTML定义字符串
	 * 
	 * @param field
	 * @return
	 */
	// public String getEditFieldTagHtml(IVMField field) {
	// return TagUtil.buildHtml(getlowerFirstEntityName(),
	// field).replaceFirst(field.getFieldName(), "edit_" +
	// field.getFieldName());
	// }
	//
	// public String getEditFieldTagScript(IVMField field){
	//
	// return
	// TagUtil.buildScript(field,formId).replaceFirst(field.getFieldName(),
	// "edit_" + field.getFieldName());
	// }

	/**
	 * @Title: sortAddField2
	 * @Description: 同样针对form中的AddField字段进行排序，针对字段中有父子关系的情况 目前只有级联下拉框对应这种情况，需要排序
	 *               这种方法就遍历列表一次，之后递归生成结果，理论上比第一种插入并检测的方式快
	 * @return void 返回类型
	 * @throws
	 */
	public void sortAddField2() {
		LinkedList result = new LinkedList();
		// 为了防止有在parent参数中填错值的，也就是级联连不上的情况，讲级联的下拉框组件都添加到一个list中
		// 记录所有级联下拉框，到最后如果有没进行排序处理的，直接放在控件最后面，防止有漏生成的现象
		// 最后会打个warning
		LinkedList allCascadeSelect = new LinkedList();

		if (null != form) {
			Map relationship = new HashMap();
			List noCascadeSelects = new ArrayList();
			Collection fields = form.getFields();
			Iterator fIt = fields.iterator();
			while (fIt.hasNext()) {
				IAddField field = (IAddField) fIt.next();
				ITag tag = field.getTag();
				boolean skip = false;
				String id = null;
				String parentId = null;
				if (tag.getTagType().equals("select")) {
					Iterator aIt = tag.getAttributes().iterator();
					while (aIt.hasNext()) {
						ITagAttribute attribute = (ITagAttribute) aIt.next();
						if (attribute.getName().equals("name")) {
							id = attribute.getAttrValue();
						}
						// 非级联的下拉框不做顺序处理都排在前面
						if (attribute.getName().equals("parent")) {
							parentId = attribute.getAttrValue();
						}
						if (attribute.getName().equals("isCascade"))
							skip = true;
					}
					if (!skip)
						noCascadeSelects.add(field);
				}
				// 此处非级联的下拉框(可能作为级联下拉框的父框)及其他控件都放在排序后的结果最前端
				if (!skip)
					result.add(field);
				// 级联下拉框之间的关系记录到map中,key为parent id
				else {
					allCascadeSelect.add(field);
					List list = (List) relationship.get(parentId);
					if (null == list) {
						list = new ArrayList();
						list.add(field);
						relationship.put(parentId, list);
					} else {
						list.add(field);
					}
				}
			}
			fIt = noCascadeSelects.iterator();
			while (fIt.hasNext()) {
				IAddField field = (IAddField) fIt.next();
				List param = new ArrayList();
				param.add(field);
				getOrderResult(result, param, relationship, allCascadeSelect);
			}
			// 处理级联参数填写错误的级联下拉框
			fIt = allCascadeSelect.iterator();
			while (fIt.hasNext()) {
				IAddField field = (IAddField) fIt.next();
				result.add(field);
				log.warn("级联下拉框没有找到父下拉框\r\n下拉框name:" + field.getFieldName()
						+ "\r\n");
				log.warn("父下拉框name:" + getFieldParent(field) + "\r\n");
				log.warn("请检查下拉框的parent属性设置拼写");
			}
		}
		editFieldInOrder = result;
	}

	/**
	 * @Title: getOrderResult
	 * @Description: 通过递归的方式将非级联select的所有下级select通过中根次序填充到result中
	 * @param result
	 * @param ids
	 * @param relationship
	 * @param allCascadeSelect
	 *            参数及返回值
	 * @return void 返回类型
	 * @throws
	 */
	private void getOrderResult(List result, List ids, Map relationship,
			List allCascadeSelect) {
		List newIds = new ArrayList();
		if (null != ids) {
			Iterator it = ids.iterator();
			while (it.hasNext()) {
				IAddField field = (IAddField) it.next();
				List l = (List) relationship.get(field.getFieldName());
				if (null != l && 0 != l.size()) {
					Iterator i = l.iterator();
					while (i.hasNext()) {
						IAddField son = (IAddField) i.next();
						newIds.add(son);
						result.add(son);
						allCascadeSelect.remove(son);
					}
				}
			}
		}
		if (0 != newIds.size())
			getOrderResult(result, newIds, relationship, allCascadeSelect);
	}

	/**
	 * @Title: sortAddField
	 * @Description: 针对NewForm中的AddField字段进行排序，针对字段中有父子关系的情况
	 *               目前只有级联下拉框对应这种情况，需要排序
	 * @return void 返回类型
	 * @throws
	 */
	private void sortAddField() {
		LinkedList list = new LinkedList();
		Iterator it = form.getFields().iterator();
		Map relationship = new HashMap();
		LinkedList cascades = new LinkedList();
		Map mark = new HashMap();
		while (it.hasNext()) {
			IAddField field = (IAddField) it.next();
			if (null == getFieldParent(field)) {
				list.add(field);
				relationship.put(field.getFieldName(), field);
			} else {
				cascades.add(field);
				mark.put(field.getFieldName(), new Integer(0));
			}
		}

		while (true) {
			it = cascades.iterator();
			List temp = new ArrayList();
			while (it.hasNext()) {
				IAddField field = (IAddField) it.next();
				String parentName = getFieldParent(field);
				Object parent = relationship.get(parentName);
				if (null != parent) {
					int index = list.indexOf(parent);
					// 将子字段插入到父子段的后面
					list.add(index + 1, field);
					relationship.put(field.getFieldName(), field);
					temp.add(field);
					// cascades.remove(field);
					resetMark(mark);
				} else {
					mark.put(field.getFieldName(),
							((Integer) mark.get(field.getFieldName())) + 1);
				}
			}
			it = temp.iterator();
			while (it.hasNext()) {
				cascades.remove(it.next());
			}
			if (checkMark(cascades, mark))
				break;
		}
		it = cascades.iterator();
		while (it.hasNext()) {
			IAddField field = (IAddField) it.next();
			log.warn("级联下拉框没有找到父下拉框\r\n下拉框name:" + field.getFieldName()
					+ "\r\n");
			log.warn("父下拉框name:" + getFieldParent(field) + "\r\n");
			log.warn("请检查下拉框的parent属性设置拼写");
		}
		list.addAll(cascades);
		editFieldInOrder = list;
	}

	/**
	 * @Title: resetMark
	 * @Description: 重置所有mark
	 * @param mark
	 *            参数及返回值
	 * @return void 返回类型
	 * @throws
	 */
	private void resetMark(Map mark) {
		Set key = mark.keySet();
		Iterator it = key.iterator();
		while (it.hasNext()) {
			mark.put(it.next(), new Integer(0));
		}
	}

	/**
	 * @Title: checkMark
	 * @Description: 查看待检查序列中的对象在mark中是否都记录了两次以上 如果都记录了两次以上，证明这些级联下拉框无法关联到任何其他字段
	 *               也就是说级联参数设置错误，结束找parent的操作，然后把这些数据放在最后 并且提示
	 * @return 参数及返回值
	 * @return boolean 如果未满足结束
	 * @throws
	 */
	private boolean checkMark(Collection collection, Map mark) {
		int result = 0;
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			Integer i = (Integer) mark.get(((IAddField) it.next())
					.getFieldName());
			if (i.intValue() == 2)
				result++;
		}
		return result == collection.size();
	}

}
