package autocode.util.auxiliary.vm.relationtable;

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

import autocode.util.StringUtil;
import autocode.util.TagUtil;
import dataset.model.IField;
import dataset.model.IVEntity;
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
	private INewForm newForm;
	private int queryFieldsLength;// 查询条件字段个数
	private int resultFieldsLength; // 查询结果列表字段个数
	private int pageSize; // 分页大小
	private String rowSelectType;// 查询结果列表行选择类型 checkbox radio none
	private String orderFields;// 排序字段
	private String orders; // 排序字段排序规则

	private List editFieldInOrder;

	public VmVarViewListInfo(IVEntity entity) {
		super(entity, entity.getResultForm().getTitle());
		newForm = entity.getNewForm();
		resultForm = entity.getResultForm();
		queryForm = entity.getQueryForm();
		if (queryForm != null) {
			queryFieldsLength = queryForm.getFields().size();
			pageSize = queryForm.getPageSize();
			orderFields = queryForm.getOrderFields();
			orders = queryForm.getOrders();
		}
		if (resultForm != null) {
			resultFieldsLength = resultForm.getFields().size();
			rowSelectType = resultForm.getRowSelectType();
		}
		if (newForm != null) {
			// 写了两种对新增字段的排序算法，sortAddField2使用map进行先根遍历，貌似快一些
			// 两种代码都留着吧，挺好玩的
			// sortAddField();
			sortAddField2();
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
	 * 获取查询结果字段列表
	 * 
	 * @return
	 */
	public Collection getResultFields() {
		return resultForm.getFields();
	}

	/**
	 * 
	 * @param excludePkField
	 * @return
	 */
	public Collection getResultFields(boolean excludePkField) {
		Iterator iterator = resultForm.getFields().iterator();
		Collection resultFields = new ArrayList();
		String pkFiledName = getPkFieldName();
		while (iterator.hasNext()) {
			IVMField field = (IVMField) iterator.next();
			if (!excludePkField) {
				resultFields.add(field);
			} else {
				String vmFieldName = field.getFieldName();
				if (!vmFieldName.equals(pkFiledName)) {
					resultFields.add(field);
				}
			}
		}
		return resultFields;
	}

	/**
	 * （查询列表中）是否隐藏主键字段
	 * 
	 * @return
	 */
	public boolean hidePkField() {
		IField pkField = (IField) entity.getPKFields().iterator().next();
		Iterator iterator = resultForm.getFields().iterator();
		while (iterator.hasNext()) {
			IVMField field = (IVMField) iterator.next();
			if (field.getFieldName().equals(pkField.getFieldName())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取主键字段（不支持联合主键）
	 * 
	 * @return
	 */
	public Object getPkField() {
		IField pkField = (IField) entity.getPKFields().iterator().next();
		Iterator iterator = resultForm.getFields().iterator();
		while (iterator.hasNext()) {
			IVMField field = (IVMField) iterator.next();
			if (field.getFieldName().equals(pkField.getFieldName())) {
				return field;
			}
		}
		return pkField;
	}

	/**
	 * 获取主键字段名称（不支持联合主键）
	 * 
	 * @return
	 */
	public String getPkFieldName() {
		IField pkField = (IField) entity.getPKFields().iterator().next();
		return pkField.getFieldName();
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
	 * 获取查询结果字段显示名称
	 * 
	 * @param field
	 *            查询结果字段
	 * @return
	 */
	public String getResultFieldDisplayName(IField field) {
		if (field == null) {
			return null;
		}
		String fieldName = field.getFieldName();
		Iterator iterator = this.resultForm.getFields().iterator();
		while (iterator.hasNext()) {
			IVMField vField = (IVMField) iterator.next();
			String vFieldName = vField.getFieldName();
			if (vFieldName.equals(fieldName)) {
				return getResultFieldDisplayName(vField);
			}
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
	 * 获取页面标题
	 */
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

	public Collection getAddFields() {
		return newForm.getFields();
	}

	/**
	 * @Title: sortAddField2
	 * @Description: 同样针对NewForm中的AddField字段进行排序，针对字段中有父子关系的情况
	 *               目前只有级联下拉框对应这种情况，需要排序 这种方法就遍历列表一次，之后递归生成结果，理论上比第一种插入并检测的方式快
	 * @return void 返回类型
	 * @throws
	 */
	public void sortAddField2() {
		LinkedList result = new LinkedList();
		// 为了防止有在parent参数中填错值的，也就是级联连不上的情况，讲级联的下拉框组件都添加到一个list中
		// 记录所有级联下拉框，到最后如果有没进行排序处理的，直接放在控件最后面，防止有漏生成的现象
		// 最后会打个warning
		LinkedList allCascadeSelect = new LinkedList();

		if (null != newForm) {
			Map relationship = new HashMap();
			List noCascadeSelects = new ArrayList();
			Collection fields = newForm.getFields();
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
		Iterator it = newForm.getFields().iterator();
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

	/*
	 * (非 Javadoc)Title: getFieldTagScriptDescription:
	 * 
	 * @param field
	 * 
	 * @return
	 * 
	 * @see
	 * autocode.util.auxiliary.vm.relationtable
	 * .VmVarViewBaseInfo
	 * #getFieldTagScript(dataset
	 * .viewmodel.IVMField)
	 */
	public String getFieldTagScript(IVMField field) {
		if (entity.getPKFields().size() > 1) {
			IField iField = getField(entity.getFields(), field.getFieldName());
			if (iField.isPK()) {
				return TagUtil.buildScript(getEntityName(true) + "."
						+ getlowerFirstEntityPkName(), field, formId, null);
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
		// 针对级联下拉框，修正级联下拉框的parent属性
		if (field instanceof IAddField && isCascadeSelect(field)) {
			Map resetAttr = new HashMap();
			String id = null;
			String parentId = getFieldParent(field);
			if (null != parentId) {
				Collection fields = newForm.getFields();
				Iterator it = fields.iterator();
				IAddField parentField = null;
				while (it.hasNext()) {
					IAddField f = (IAddField) it.next();
					if (f.getFieldName().equals(parentId))
						parentField = f;
				}
				if (null != parentField) {
					id = parentField.getRefEntity().getName() + "_" + parentId;
					resetAttr.put("parent", id);
				}
				return TagUtil.buildScript(getEntityName(true), field, formId,
						resetAttr);
			}
		}
		return TagUtil.buildScript(getEntityName(true), field, formId, null);
	}

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
		// 针对级联下拉框，修正级联下拉框的parent属性
		if (field instanceof IAddField && isCascadeSelect(field)) {
			Map resetAttr = new HashMap();
			String id = null;
			String parentId = getFieldParent(field);
			if (null != parentId) {
				Collection fields = newForm.getFields();
				Iterator it = fields.iterator();
				IAddField parentField = null;
				while (it.hasNext()) {
					IAddField f = (IAddField) it.next();
					if (f.getFieldName().equals(parentId))
						parentField = f;
				}
				if (null != parentField) {
					id = parentField.getRefEntity().getName() + "_" + parentId;
					resetAttr.put("parent", id);
				}
				return TagUtil.buildScript(getEntityName(true), field, formId,
						resetAttr, false);
			}
		}
		return TagUtil.buildScript(getEntityName(true), field, formId, null,
				false);
	}
}
