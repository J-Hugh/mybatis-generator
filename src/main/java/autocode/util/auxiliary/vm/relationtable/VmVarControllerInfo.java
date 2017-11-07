package autocode.util.auxiliary.vm.relationtable;

import java.util.ArrayList;
import java.util.List;

import autocode.model.AutoCodeInfo;
import autocode.util.StringUtil;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IVEntity;

public class VmVarControllerInfo extends VmVarJavaBaseInfo {

	// 增加额外查询字段的相关vm参数 by bruce 20121210
	public List queryExtFieldNames = new ArrayList();

	public List queryExtFieldTypes = new ArrayList();

	public List queryExtFieldFirstUpperNames = new ArrayList();

	public List editExtFieldNames = new ArrayList();

	public List editExtFieldTypes = new ArrayList();

	public List editExtFieldFirstUpperNames = new ArrayList();
	
	private AutoCodeInfo autoCodeInfo;
	
	protected String serviceFirstLowerName; //service 类名(第一个字母小写)
    
    protected String serviceFirstUpperName; //service 类名 (第一个字母大写)

	public VmVarControllerInfo(IVEntity entity , AutoCodeInfo autoCodeInfo) {
		super(entity);
		this.autoCodeInfo = autoCodeInfo;
		setPackageName(autoCodeInfo.getControllerPackageName());
		setClassName(autoCodeInfo.getControllerName());
		addImportTypes(autoCodeInfo.getServicePackageName() + "." + autoCodeInfo.getServiceName());
		addImportTypes(autoCodeInfo.getPoPackageName() + "." + autoCodeInfo.getPoName());
		setPoFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getPoName()));
		setPoFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getPoName()));
		setPoPKName(getPKFieldNameReal(entity));
		setPoPKFirstLowerName(StringUtil.lowerFirst(getPKFieldNameReal(entity)));
		setServiceFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getServiceName()));
		setServiceFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getServiceName()));
		setModule(autoCodeInfo.getModuleName());
		
//		IQueryForm queryform = this.entity.getQueryForm();
//		if (queryform != null) {
//			List<QueryField> queryFieldlist = (List<QueryField>) queryform
//					.getFields();
//			if (queryFieldlist.size() != 0) {
//				for (int i = 0; i < queryFieldlist.size(); i++) {
//					QueryField queryField = queryFieldlist.get(i);
//					if (queryField.getRefEntity() != null) {
//						if (queryField.isMultiValue()) {
//							queryExtFieldNames.add(queryField
//									.getFieldNameInMultiValueCase());
//							queryExtFieldTypes.add(StringUtil
//									.getSimpleTypeName(queryField
//											.getFieldTypeInMultiValueCase()));
//							queryExtFieldFirstUpperNames.add(StringUtil
//									.upperFirst(queryField
//											.getFieldNameInMultiValueCase()));
//						}
//					}
//				}
//			}
//		}
	}

	/**
	 * 获取实体名称
	 * 
	 * @param lowerFirst
	 *            实体名称首字母是否小写
	 * @return
	 */
	public String getEntityName(boolean lowerFirst) {
		if (lowerFirst) {
			return StringUtil.lowerFirst(this.entity.getName());
		}
		return StringUtil.upperFirst(this.entity.getName());
	}

	/**
	 * 获取实体名称
	 * 
	 * @param entity
	 *            实体对象
	 * @param lowerFirst
	 *            实体名称首字母是否小写
	 * @return
	 */
	public String getEntityName(IEntity entity, boolean lowerFirst) {
		if (entity == null) {
			return null;
		}
		if (lowerFirst) {
			return StringUtil.lowerFirst(entity.getName());
		}
		return StringUtil.upperFirst(entity.getName());
	}

	/**
	 * 获取实体字段的Java类型
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldJavaType(IField field) {
		String type = field.getFieldType();
		type = type.substring(type.lastIndexOf(".") + 1);
		return type;
	}

	/**
	 * 获取主键字段名称
	 * 
	 * @param lowerFirst
	 * @return
	 */
	public String getPKFieldName(boolean lowerFirst) {
		return getPKFieldName(this.getEntity(), lowerFirst);
	}

	/**
	 * 获取实体主键字段名称（不支持联合主键）
	 * 
	 * @param entity
	 *            实体对象
	 * @param lowerFirst
	 *            字段名称首字母是否小写
	 * @return
	 */
	public String getPKFieldName(IEntity entity, boolean lowerFirst) {
		if (entity == null) {
			return null;
		}
		IField field = (IField) (entity.getPKFields().iterator().next());
		if (lowerFirst) {
			return StringUtil.lowerFirst(field.getName());
		}
		return StringUtil.upperFirst(field.getName());
	}

	public String getPKFieldNameReal() {
		return ((IField) (this.getEntity().getPKFields().iterator().next()))
				.getFieldName();
	}

	public String getPKFieldNameReal(IEntity entity) {
		return ((IField) (entity.getPKFields().iterator().next())).getName();
	}

	public String getPKFieldJavaType() {
		if (entity == null) {
			return null;
		}
		IField field = (IField) (entity.getPKFields().iterator().next());
		return getFieldJavaType(field);
	}

	public String getPKFieldJavaType(IEntity entity) {
		if (entity == null) {
			return null;
		}
		IField field = (IField) (entity.getPKFields().iterator().next());
		return getFieldJavaType(field);
	}

	public List getQueryExtFieldNames() {
		return queryExtFieldNames;
	}

	public void setQueryExtFieldNames(List queryExtFieldNames) {
		this.queryExtFieldNames = queryExtFieldNames;
	}

	public List getQueryExtFieldTypes() {
		return queryExtFieldTypes;
	}

	public void setQueryExtFieldTypes(List queryExtFieldTypes) {
		this.queryExtFieldTypes = queryExtFieldTypes;
	}

	public List getQueryExtFieldFirstUpperNames() {
		return queryExtFieldFirstUpperNames;
	}

	public void setQueryExtFieldFirstUpperNames(
			List queryExtFieldFirstUpperNames) {
		this.queryExtFieldFirstUpperNames = queryExtFieldFirstUpperNames;
	}

	public String getServiceFirstLowerName() {
		return serviceFirstLowerName;
	}

	public void setServiceFirstLowerName(String serviceFirstLowerName) {
		this.serviceFirstLowerName = serviceFirstLowerName;
	}

	public String getServiceFirstUpperName() {
		return serviceFirstUpperName;
	}

	public void setServiceFirstUpperName(String serviceFirstUpperName) {
		this.serviceFirstUpperName = serviceFirstUpperName;
	}
	
}