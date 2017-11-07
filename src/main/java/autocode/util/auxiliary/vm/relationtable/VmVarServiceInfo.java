package autocode.util.auxiliary.vm.relationtable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import autocode.model.AutoCodeInfo;
import autocode.util.StringUtil;
import dataset.IDatasetConstant;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IRelation;
import dataset.model.IVEntity;
import dataset.model.IVField;
import dataset.viewmodel.IQueryField;

/**
 * 
 * @author BizFoundation Team: chenhua
 * 
 *         {注释}
 * 
 * @version 5.0
 * @since 4.3
 */
public class VmVarServiceInfo extends VmVarJavaBaseInfo {
    private String ventityname;

    private String upper_ventityname;
    
    private List pentityNameList = new ArrayList();

    private List oneway_one2oneList = new ArrayList();

    private List upper_pentityNameList = new ArrayList();

    private String registClass = null;

    private String registBeanName = null;

    private String registFile = null;
    
    protected String daoFirstLowerName; //dao 类名(第一个字母小写)
    
    protected String daoFirstUpperName; //dao 类名 (第一个字母大写)

    private AutoCodeInfo autoCodeInfo;

    public VmVarServiceInfo(IVEntity entity, AutoCodeInfo autoCodeInfo) {
        super(entity);
        this.autoCodeInfo = autoCodeInfo;
        setPackageName(autoCodeInfo.getServicePackageName());
        setClassName(StringUtil.lowerFirst(autoCodeInfo.getServiceName()));
        addImportTypes(autoCodeInfo.getDaoPackageName() + "." + autoCodeInfo.getDaoName());
        addImportTypes(autoCodeInfo.getPoPackageName() + "." + autoCodeInfo.getPoName());
        setDaoFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getDaoName()));
        setDaoFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getDaoName()));
        setPoFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getPoName()));
        setPoFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getPoName()));
        setPoPKFirstLowerName(StringUtil.lowerFirst(getPKFieldNameReal(entity)));
        setPoPKName(getPKFieldNameReal(entity));
        
//        setVEntityName(entity.getName());
//        setUpper_VEntityName(StringUtil.upperFirst(entity.getName()));
//        initSaveEntity();
    }
    
	public String getPKFieldNameReal(IEntity entity) {
		return ((IField) (entity.getPKFields().iterator().next())).getName();
	}

    /**
     * 获取查询条件字段
     * 	包括显示模型定义的查询条件字段 与 查询实体 唯一标识字段
     * @return
     */
    public List getQueryFields() {
    	List queryFields = new ArrayList();
    	List tmpQueryFields = (List)entity.getQueryForm().getFields();
    	for(int i=0;i<tmpQueryFields.size();i++){
    		IQueryField queryField =(IQueryField)tmpQueryFields.get(i);
    		if(queryField.getRefEntity() != null){
    			queryFields.add(queryField);
    		}
    	}
    	
    	Iterator iterator = entity.getPKFields().iterator();
    	
    	while (iterator.hasNext()) {
			IVField field = (IVField) iterator.next();
			boolean flag = true;
			for(int i=0;i<tmpQueryFields.size();i++){
				IQueryField queryField = (IQueryField)tmpQueryFields.get(i);
				if(field.equals(queryField.getRefField())){
					flag = false;
					break;
				}
			}
			if(flag){
				boolean mark = true;
				for(int i =0;i< queryFields.size(); i++){
					if(queryFields.get(i) instanceof IQueryField){
						IQueryField queryField=(IQueryField)queryFields.get(i);
						if(queryField.getRefFieldId().equals(field.getRefFieldId())){
							mark = false;
							break;
						}
					}
				}
				if (mark) {
					queryFields.add(field);
				}			
			}
		}
    	
		return queryFields;
	}

    /**
     * 获取字段的查询条件表达式
     * @param vField
     * @return
     */
    public String getQueryStr(IVField vField){
    	IPEntity pEntity = (IPEntity)vField.getRefEntity();
    	String colName = StringUtil.isNotBlank(vField.getAlias())? vField.getAlias() : vField.getColumnName();
    	String queryStr = "\"" + pEntity.getTable() +"." + colName + "  = :" + vField.getFieldName() + "\""; 
    	return queryStr;
    }
    /**
     * 获取字段的查询条件表达式
     * @param queryField
     * @return
     */
    public String getQueryStr(IQueryField queryField){
    	IPField vField = (IPField)queryField.getRefField();
    	IPEntity pEntity = (IPEntity)vField.getParentEntity();
    	String colName = vField.getColumnName();
    	String operator = getOperator(queryField.getOperator());
    	String queryStr = "";
    	if(IDatasetConstant.OPERATOR_IN.equalsIgnoreCase(operator)||IDatasetConstant.OPERATOR_NOT_IN.equalsIgnoreCase(operator)){
    		queryStr = "\"" + pEntity.getTable() +"." + colName + " " +  operator + "( :" + queryField.getFieldNameInMultiValueCase() + ")\"";
    	}else{
    		queryStr = "\"" + pEntity.getTable() +"." + colName + " " +  operator + " :" + queryField.getFieldNameInMultiValueCase() + "\""; 
    	}
    	return queryStr;
    }
    /**
     * 获取查询条件字段的sql条件操作符
     * @param operator
     * @return
     */
    public String getOperator(String operator) {
        if (operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT)
                || operator.equals(IDatasetConstant.OPERATOR_LIKE_RIGHT)
                || operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT)) {
            return IDatasetConstant.OPERATOR_LIKE;
        } else {
            return operator;
        }
    }
    /**
     * 获取sql条件的条件值表达式
     * @param operator
     * @param value
     * @return
     */
    public String getOperatorDetail(String operator, String value) {
        StringBuffer sb = new StringBuffer();
        if (operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT)) {
            sb.append(value + " + \"%\"");
        } else if (operator.equals(IDatasetConstant.OPERATOR_LIKE_RIGHT)) {
            sb.append("\"%\" + " + value);
        } else if (operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT)) {
            sb.append("\"%\" + " + value + " + \"%\"");

        }
        return sb.toString();
    }
    /**
     * 获取查询实体字段的get方法
     * @param field
     * @return
     */
    public String getGetFieldValue(IVField field) {
    	return StringUtil.getJavaBeanFieldGetterStr(field.getFieldName()) + "()";
	}
    /**
     * 获取查询实体字段的get方法
     * @param field
     * @return
     */
    public String getGetFieldValue(IQueryField field){
    	return StringUtil.getJavaBeanFieldGetterStr(field.getFieldNameInMultiValueCase()) + "()";
    }
    /**
     * 获取查询实体字段的set方法（根据条件操作符将当前的条件值处理后重新设置到当前实体）
     * 	如 LIKE左右匹配，则返回 setXXX("%" + entity.getXXX() + "%");
     * 如果无需处理，则返回空字符串
     * @param field
     * @return
     */
    public String getSetFieldValue(IVField field){
    	String value = getOperatorDetail("=", getGetFieldValue(field));
    	return "";
    }
    /**
     * 获取查询实体字段的set方法（根据条件操作符将当前的条件值处理后重新设置到当前实体）
     * 	如 LIKE左右匹配，则返回 setXXX("%" + entity.getXXX() + "%");
     * 如果无需处理，则返回空字符串
     * @param field
     * @return
     */
    public String getSetFieldValue(IQueryField field){
    	String value = getOperatorDetail(field.getOperator(),getEntityName(true) + "." + getGetFieldValue(field));
    	String operator = getOperator(field.getOperator());
    	if(IDatasetConstant.OPERATOR_LIKE.equals(operator)){
    		return StringUtil.getJavaBeanFieldSetterStr(field.getFieldNameInMultiValueCase()) + "(" + value + ")";
		}
    	return "";
    }

    /**
     * 初始化查询实体保存 说明： 只处理N对1与1对1的关系
     */
    public void initSaveEntity() {
        oneway_one2oneList = getEditRelationArgs();
        for(int i = 0;i<getEditEntityArgs().size();i++){
            IEntity pentity  = getEditEntityArgs().get(i);
            pentityNameList.add(i, StringUtil.lowerFirst(pentity.getName()));
            upper_pentityNameList.add(i, StringUtil.upperFirst(pentity.getName()));
        }
        for(int i = 0;i< getEditRelationArgs().size();i++){
            IRelation relation = getEditRelationArgs().get(i);
            oneway_one2oneList.add(i, setRelationValue(relation));
        }
        
    }

    public void initRegistServiceImpl() {
        setRegistClass(autoCodeInfo.getServiceImplPackageName() + "."
                + autoCodeInfo.getServiceImplName());
        setRegistBeanName(StringUtil.lowerFirst(autoCodeInfo.getServiceName()));
        setRegistFile(autoCodeInfo.getSrcSpringConfigPath());
    }

    public void initRegistServiceTest() {
        setRegistClass(autoCodeInfo.getServiceImplPackageName() + "."
                + autoCodeInfo.getServiceImplName());
        setRegistBeanName(StringUtil.lowerFirst(autoCodeInfo.getServiceName()));
        setRegistFile(autoCodeInfo.getTestSpringConfigPath());

    }

    public String getVEntityName() {
        return ventityname;
    }

    public void setVEntityName(String vEntityName) {
        ventityname = vEntityName;
    }

    public String getUpper_VEntityName() {
        return upper_ventityname;
    }

    public void setUpper_VEntityName(String upper_VEntityName) {
        upper_ventityname = upper_VEntityName;
    }
    
    public String getEntityName(boolean lowerFirst){
    	if(lowerFirst){
    		return StringUtil.lowerFirst(entity.getName());
    	}
    	return StringUtil.upperFirst(entity.getName());
    }

    public List getPentityNameList() {
        return pentityNameList;
    }

    public void setPentityNameList(List pentityNameList) {
        this.pentityNameList = pentityNameList;
    }

    public List getUpper_pentityNameList() {
        return upper_pentityNameList;
    }

    public void setUpper_pentityNameList(List upper_pentityNameList) {
        this.upper_pentityNameList = upper_pentityNameList;
    }

    public List getOneway_one2oneList() {
        return oneway_one2oneList;
    }

    public String getServicePackageName() {
        return autoCodeInfo.getServicePackageName();
    }

    public String getServiceImplPackageName() {
        return autoCodeInfo.getServiceImplPackageName();
    }

    public String getServiceTestPackageName() {
        return autoCodeInfo.getServiceTestPackageName();
    }

    public String getUpperServicename() {
        return autoCodeInfo.getServiceName();
    }

    public String getServicename() {
        return StringUtil.lowerFirst(autoCodeInfo.getServiceName());
    }

    public String getUpperServiceimpl() {
        return autoCodeInfo.getServiceImplName();
    }

    public String getUpperServiceTestname() {
        return autoCodeInfo.getServiceTestName();
    }

    public String getRegistClass() {
        return registClass;
    }

    public void setRegistClass(String registClass) {
        this.registClass = registClass;
    }

    public String getRegistBeanName() {
        return registBeanName;
    }

    public void setRegistBeanName(String registBeanName) {
        this.registBeanName = registBeanName;
    }

    public String getRegistFile() {
        return registFile;
    }

    public void setRegistFile(String registFile) {
        this.registFile = registFile;
    }
    
    public String getDaoFirstLowerName() {
		return daoFirstLowerName;
	}

	public void setDaoFirstLowerName(String daoFirstLowerName) {
		this.daoFirstLowerName = daoFirstLowerName;
	}

	public String getDaoFirstUpperName() {
		return daoFirstUpperName;
	}

	public void setDaoFirstUpperName(String daoFirstUpperName) {
		this.daoFirstUpperName = daoFirstUpperName;
	}
}
