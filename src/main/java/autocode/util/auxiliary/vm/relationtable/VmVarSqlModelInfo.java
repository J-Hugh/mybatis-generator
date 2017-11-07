package autocode.util.auxiliary.vm.relationtable;

import java.util.ArrayList;
import java.util.List;

import autocode.model.AutoCodeInfo;
import autocode.util.StringUtil;
import dataset.IDatasetConstant;
import dataset.model.IEntity;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IVEntity;
import dataset.model.IVField;
import dataset.model.IView;
import dataset.viewmodel.IQueryForm;
import dataset.viewmodel.impl.QueryField;

/**
 * 
 * @author BizFoundation Team: chenhua
 * 
 *         {注释}
 * 
 * @version 5.0
 * @since 4.3
 */
public class VmVarSqlModelInfo extends VmVarJavaBaseInfo {
    public List fields_commn = new ArrayList();

    public List field_types = new ArrayList();

    public List field_tables = new ArrayList();

    public List columns_names = new ArrayList();

    public List field_first_upper_names = new ArrayList();

    public List view_dic_list = new ArrayList();

    public List view_content_list = new ArrayList();
    //查询语句中 实体名称和字段名称的组合：user.name
    public List query_entity_column = new ArrayList();

    public List query_name_type = new ArrayList();

    public List query_name_type2 = new ArrayList();

    public List query_column_name = new ArrayList();
    
    //增加额外查询字段的相关vm参数 by bruce 20121210
    public List queryExtFieldNames = new ArrayList();
    
    public List queryExtFieldTypes = new ArrayList();
    
    public List queryExtFieldFirstUpperNames = new ArrayList();

    public VmVarSqlModelInfo(IVEntity entity, AutoCodeInfo autoCodeInfo) {
        super(entity);

        setPackageName(autoCodeInfo.getPoPackageName());
        setClassName(StringUtil.lowerFirst(autoCodeInfo.getPoName()));
        List<IVField> fields = (List<IVField>) entity.getFields();
        for (int i = 0; i < fields.size(); i++) {
            IVField ivField = fields.get(i);
            fields_commn.add(i, ivField.getFieldName());
            field_first_upper_names.add(i, StringUtil.upperFirst(ivField.getFieldName()));
            field_types.add(i, StringUtil.split(ivField.getFieldType(), ".")[StringUtil.split(ivField.getFieldType(), ".").length-1]);
//            if(!importTypes.contains(ivField.getFieldType())){
//                field_types.add(i,ivField.getFieldType());
//            }
            if(!importTypes.contains(ivField.getFieldType()) && !ivField.getFieldType().startsWith("java.lang.")){
            	addImportTypes(ivField.getFieldType());
            }
            if (ivField.getRefEntity() != null) {
                field_tables.add(i, ((IPEntity) ivField.getRefEntity()).getTable());
                columns_names.add(i, ((IPField) ivField.getRefField()).getColumnName());
            } else {
                field_tables.add(i, "null");
                columns_names.add(i, "null");
            }
        }
      
        List<IView> views = (List<IView>) this.entity.getViews();
        if (views != null && views.size() != 0) {
            for (int i = 0; i < views.size(); i++) {
                IView ivView = views.get(i);
                if (ivView.getContent() != null && ivView.getContent().length() != 0) {
                    view_dic_list.add(i, StringUtil.lowerFirst(ivView.getDialect().toLowerCase())+"Sql");
                    view_content_list.add(i, ivView.getContent());
                }
            }
        }
        IQueryForm queryform = this.entity.getQueryForm();
        if (queryform != null) {
            List<QueryField> queryFieldlist = (List<QueryField>) queryform.getFields();
            if (queryFieldlist.size() != 0) {
                for (int i = 0; i < queryFieldlist.size(); i++) {
                    QueryField queryField = queryFieldlist.get(i);
                    if (queryField.getRefEntity() != null) {
                    	if(queryField.isMultiValue()){
    	                    queryExtFieldNames.add(queryField.getFieldNameInMultiValueCase());
    	                    queryExtFieldTypes.add(StringUtil.getSimpleTypeName(queryField.getFieldTypeInMultiValueCase()));
    	                    queryExtFieldFirstUpperNames.add(StringUtil.upperFirst(queryField.getFieldNameInMultiValueCase()));
                        }
                        query_entity_column.add( ((IPEntity) queryField.getRefEntity()).getTable() + "." +((IPField) queryField.getRefField()).getColumnName());
                        query_name_type.add( getOperator(queryField.getOperator()));
                        query_name_type2.add(getOperatorDetail(queryField.getOperator(), queryField.getRefField().getFieldName()));
                        query_column_name.add(queryField.getRefField().getFieldName());
                    }
                }
            }
        }
        List<IVField> pkfields=(List<IVField>)entity.getPKFields();
        for (int i = 0,size=query_entity_column.size() - 1; i < pkfields.size(); i++,size++) {
            IVField ivField = pkfields.get(i);
            if (ivField.getRefEntity() != null) {
                if(!query_entity_column.contains(((IPEntity) ivField.getRefEntity()).getTable() + "." +((IPField) ivField.getRefField()).getColumnName())){
                    query_entity_column.add( ((IPEntity) ivField.getRefEntity()).getTable() + "." +((IPField) ivField.getRefField()).getColumnName());
                    query_name_type.add( IDatasetConstant.OPERATOR_EQ);
                    query_name_type2.add("");
                    query_column_name.add( ivField.getRefField().getFieldName());
                }
            }
        }
    }

    public String getOperator(String operator) {
        if (operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT)
                || operator.equals(IDatasetConstant.OPERATOR_LIKE_RIGHT)
                || operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT)) {
            return IDatasetConstant.OPERATOR_LIKE;
        } else {
            return operator;
        }
    }

    public String getOperatorDetail(String operator, String filename) {
        StringBuffer sb = new StringBuffer();
        if (operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT)) {
            sb.append(filename + " = ");
            sb.append(filename + " + \"%\"");
        } else if (operator.equals(IDatasetConstant.OPERATOR_LIKE_RIGHT)) {
            sb.append(filename + " = ");
            sb.append("\"%\" +" + filename);
        } else if (operator.equals(IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT)) {
            sb.append(filename + " = ");
            sb.append("\"%\" +" + filename + " + \"%\"");

        }
        return sb.toString();
    }

    public String getDbString() {
        StringBuffer sb = new StringBuffer();
        IVEntity ventity = (IVEntity) entity;
        List<IView> views = (List<IView>) ventity.getViews();
        for (IView iView : views) {
            sb.append("this." + iView.getDialect() + "=");
            sb.append("\"" + iView.getContent() + "\"" + ";\n");
        }
        return sb.toString();
    }

    public List getFields_commn() {
        return fields_commn;
    }

    public void setFields_commn(List fields_commn) {
        this.fields_commn = fields_commn;
    }

    public List getField_types() {
        return field_types;
    }

    public void setField_types(List field_types) {
        this.field_types = field_types;
    }

    public List getField_tables() {
        return field_tables;
    }

    public void setField_tables(List field_tables) {
        this.field_tables = field_tables;
    }

    public List getColumns_names() {
        return columns_names;
    }

    public void setColumns_names(List columns_names) {
        this.columns_names = columns_names;
    }

    public List getField_first_upper_names() {
        return field_first_upper_names;
    }

    public void setField_first_upper_names(List field_first_upper_names) {
        this.field_first_upper_names = field_first_upper_names;
    }

    public List getView_dic_list() {
        return view_dic_list;
    }

    public void setView_dic_list(List view_dic_list) {
        this.view_dic_list = view_dic_list;
    }

    public List getView_content_list() {
        return view_content_list;
    }

    public void setView_content_list(List view_content_list) {
        this.view_content_list = view_content_list;
    }

    public List getQuery_entity_column() {
        return query_entity_column;
    }

    public void setQuery_entity_column(List query_entity_column) {
        this.query_entity_column = query_entity_column;
    }

    public List getQuery_name_type() {
        return query_name_type;
    }

    public void setQuery_name_type(List query_name_type) {
        this.query_name_type = query_name_type;
    }

    public List getQuery_name_type2() {
        return query_name_type2;
    }

    public void setQuery_name_type2(List query_name_type2) {
        this.query_name_type2 = query_name_type2;
    }

    public List getQuery_column_name() {
        return query_column_name;
    }

    public void setQuery_column_name(List query_column_name) {
        this.query_column_name = query_column_name;
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

	public void setQueryExtFieldFirstUpperNames(List queryExtFieldFirstUpperNames) {
		this.queryExtFieldFirstUpperNames = queryExtFieldFirstUpperNames;
	}
}
