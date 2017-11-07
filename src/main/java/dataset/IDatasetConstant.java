/**
 * $Id: IDatasetConstant.java,v 1.4 2013/11/16 04:57:54 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset;


/**
 * @Title: IDataConstant.java
 * @Description: <br>模型接口部分的常量
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:29:33
 * @author TangWei
 * @version $Revision: 1.4 $
 * @since 1.0
 */
public interface IDatasetConstant {
    
/***********************************实体类型***********************************/
    //持久化实体
    public static final String ENTITYTYPE_PENTITY = "persistent"; 
    //非持久化实体
    public static final String ENTITYTYPE_NENTITY = "noPersistent"; 
    //查询实体
    public static final String ENTITYTYPE_VENTITY = "query"; 
    
/***********************************主键生成策略***********************************/
    //用户自定义
    public static final String GEN_STRATEGY_CUSTOM = "custom";
    //数据库自动生成
    public static final String GEN_STRATEGY_NATIVE = "native";
    //uuid32
    public static final String GEN_STRATEGY_UUID = "uuid";
    //uuid64
    public static final String GEN_STRATEGY_UUID64 = "uuid64";
    
/***********************************关联关系类型***********************************/
    //单向1对1
    public static final String ONEWAY_ONE2ONE = "oneway_one2one"; 
    //单向多对1
    public static final String ONEWAY_MANY2ONE = "oneway_many2one"; 
    //单向1对多
    public static final String ONEWAY_ONE2MANY = "oneway_one2many"; 
    //双向多对1
    public static final String TWOWAY_ONE2MANY = "twoway_one2many"; 

/***********************************级联操作类型***********************************/
    //不进行级联操作
    public static final String CASCADE_NONE ="cascade_none"; 
    //只有对象保存操作或者是持久化对象的更新操作时才会级联操作关联对象（子对象）
    public static final String CASCADE_SAVE_UPDATE ="cascade_save_update"; 
    //将级联对象也持久化到数据库
    public static final String CASCADE_PERSIST ="cascade_persist"; 
    //对持久化对象的删除操作时会进行级联操作关联对象（子对象）
    public static final String CASCADE_DELETE = "cascade_delete"; 
    //对持久化对象的所有操作都会级联操作关联对象（子对象）
    public static final String CASCADE_ALL ="cascade_all"; 

    
/***********************************dataset文件的xml节点定义***********************************/

/***********************************公有部分***********************************/
    public static final String ID = "id"; 
    public static final String NAME = "name"; 
    public static final String DISPLAYNAME = "displayName"; 
    public static final String DESC = "description"; 
    public static final String AUTHOR = "author"; 
    public static final String TYPE = "type"; 
    public static final String DATASET = "dataset"; 
    
/***********************************实    体***********************************/    
    //实体根
    public static final String ENTITY ="entity"; 
    //实体类型
    public static final String ENTITYTYPE = "entityType";
    //模块
    public static final String MODULE_NAME = "moduleName";
    //包名
    public static final String PACKAGE_NAME = "packageName";
    //schema
    public static final String SCHEMA = "schema"; 
    //表名
    public static final String TABLENAME = "tableName"; 
    //主键生成方式
    public static final String GENSTRATEGY = "genStrategy"; 
    //关联得source
    public static final String TARGETCONNECTIONS = "targetConnections"; 
    //坐标以及长宽
    public static final String LOCATION = "location"; 
    public static final String X = "x"; 
    public static final String Y = "y"; 
    public static final String WIDE = "wide"; 
    public static final String HEIGHT = "height"; 
    
/***********************************实体属性***********************************/  
    //实体字段集合节点
    public static final String FIELDS = "fields";
    //实体字段节点
    public static final String FIELD = "field";
    //实体属性头
    public static final String COLUMN = "column"; 
    //属性名称
    public static final String FIELDNAME = "fieldName"; 
    //属性类型  java类型或hibernate类型
    public static final String FIELDTYPE = "fieldType"; 
    //延迟加载
    public static final String LAZYLOAD = "lazyLoad"; 
    //是否持久化
    public static final String PERSISTENT = "persistent"; 
    //表字段名
    public static final String COLUMNNAME = "columnName"; 
    //表字段类型
    public static final String COLUMNTYPE = "columnType"; 
    //长度
    public static final String LENGTH = "length"; 
    //精度
    public static final String PRECISION = "precision"; 
    //是为可为空
    public static final String NULLABLE = "nullAble"; 
    //是否是主键
    public static final String PK = "PK"; 
    //是否是外键
    public static final String FK = "FK"; 
    //默认值
    public static final String DEFAULT = "default"; 
    //别名
    public static final String ALIAS = "alias";
    //引用的数据文件
    public static final String REF_DATASET_FILE = "refDataset";
    //引用实体的ID
    public static final String REF_ENTITY_ID = "refEntityId";
    //引用字段的ID
    public static final String REF_FIELD_ID = "refFieldId";
    //页面内容展示方式：普通(common)、分组(group)、标签页(tab)
    public static final String CONTENT_SHOW_TYPE = "showType";
    
/***********************************实体关联关系***********************************/ 
    //relation集合节点
    public static final String RELATIONS = "relations"; 
    //relation根节点
    public static final String RELATION = "relation"; 
    //关联关系类型属性名称
    public static final String RELATE_TYPE= "relateType";
    //外键名称
    public static final String FK_NAME = "fkName";
    //目标实体属性名称
    public static final String SRC_REF_TGT_NAME = "srcRefTgtName";
    //目标实体属性显示名称
    public static final String SRC_REF_TGT_DISPLAY_NAME = "srcRefTgtDisplayName";
    //源实体属性名称（双向1对N关联关系时使用）
    public static final String TGT_REF_SRC_NAME = "tgtRefSrcName";
    //源实体属性显示名称(双向1对N关联关系时使用)
    public static final String TGT_REF_SRC_DISPLAY_NAME = "tgtRefSrcDisplayName";

    //源实体ID
    public static final String SOURCEENTITY = "sourceEntity"; 
    //目标实体ID
    public static final String TARGETENTITY = "targetEntity"; 
    //源实体字段ID
    public static final String SOURCE_FIELD_ID = "sourceId"; 
    //目标实体ID
    public static final String TARGET_FIELD_ID = "targetId"; 
    //源实体关联字段名称
    public static final String SOURCE_FIELD_NAME = "sourceName";
    //目标实体关联字段名称
    public static final String TARGET_FIELD_NAME = "targetName";
    //fkAssociationField的根节点
    public static final String FKASSOCIATIONFIELD = "fkAssociationField"; 
    
    
/***********************************视图***********************************/
    //查询实体定义方式：基于查询SQL或者数据库视图定义
    public static final String QUERY_TYPE_SQL = "SQL_OR_VIEW";
    //查询实体定义方式：基于持久化实体定义
    public static final String QUERY_TYPE_ENTITY_STRING = "ENTITY";
    //view查询实体得根节点
    //public static final String VIEW_TYPE ="VIEW"; 
    //数据库方言
    public static final String DB_DIALECT = "dialect"; 
    //使用有sql 否是view
    public static final String SQL_TYPE = "SQL"; 
    //view名称
    public static final String VIEWVALUE = "viewValue"; 
    //db语句 定义sql或者view
    public static final String SQL = "sql";
    //db定义是否保存到实体对象中
    public static final String SAVE = "save";
    //具体的db定义内容（sql语句或者VIEW名称）
    public static final String CONTENT = "content";
    public static final String REF_ENTITY_IDS = "refEntityIds";
    
/***********************************展现模型***********************************/  
    
    /***************************公共定义****************************/  
    //展示模型节点
    public static final String VIEW_MODEL = "viewModel";
    //是否设置过实体的显示模型
    public static final String IS_SET_VIEW_MODEL = "isSetViewModel";
    //展示模型字段id
    public static final String FIELD_ID = "fieldId";
    //标题
    public static final String TITLE = "title";
    //显示位置（顺序）
    public static final String INDEX = "index";
    //跨列数
    public static final String ROW_SPAN = "rowSpan";
    //跨行数
    public static final String COLUMN_SPAN = "columnSpan";
    //列宽
    public static final String WIDTH = "width";
    //是否显示
    public static final String IS_DISPLAY = "isDisplay";
    
    /***************************查询信息****************************/  
    public static final String QUERY_FORM = "queryForm";
    //结果集是否去重复
    public static final String DISTINCE = "isDistinct";
    //排序的字段（多个字段用 , 分隔）
    public static final String ORDER_FIELDS = "orderFields";
    //排序（多个用 , 分隔）
    public static final String ORDERS = "orders";
    //分页大小
    public static final String PAGE_SIZE = "pageSize";
    //是否统计总行数
    public static final String IS_TOTAL_ROWS = "isTotalRows";
    //查询条件字段
    public static final String QUERY_FIELD = "queryField";
    //导出Excel
    public static final String EXPORT_EXCEL = "exportExcel";
    //条件操作符
    public static final String OPERATOR = "operator";
    //LIKE 左匹配
    public static final String OPERATOR_LIKE_LEFT = "value%";
    //LIKE 右匹配
    public static final String OPERATOR_LIKE_RIGHT = "%value";
    //LIKE 左右匹配
    public static final String OPERATOR_LIKE_LEFT_RIGHT = "%value%";
    public static final String OPERATOR_LIKE = "like";
    public static final String OPERATOR_EQ = "=";
    public static final String OPERATOR_GT = ">";
    public static final String OPERATOR_GE = ">=";
    public static final String OPERATOR_LT = "<";
    public static final String OPERATOR_LE = "<=";
    public static final String OPERATOR_UNEQ = "!=";
    public static final String OPERATOR_IN = "in";
    public static final String OPERATOR_NOT_IN = "not in";
    
    /**
     * @Fields MULTI_VALUE_TAGS : 多值的前端控件,在设置查询字段时,多值的查询字段返回数组
     */
    public static final String[] MULTI_VALUE_TAGS = {"checkbox","checkboxlist"};
    /**
     * @Fields QUERY_ONLY_FIELD_SUFFIX : 生成查询用字段时为了和源字段区分加入的字段后缀
     */
    public static final String QUERY_ONLY_FIELD_SUFFIX = "ForQuery";
    
    /**
     * @Fields QUERY_ONLY_FIELD_SUFFIX : 生成录入用字段时为了和源字段区分加入的字段后缀
     */
    public static final String EDIT_ONLY_FIELD_SUFFIX = "ForEdit";

    //匹配类型（条件操作符为like的时候有效）
    public static final String MATCH_TYPE = "matchType";
    public static final String MATCH_TYPE_LEFT = "左匹配";
    public static final String MATCH_TYPE_RIGHT = "右匹配";
    public static final String MATCH_TYPE_LEFT_RIGHT = "左右匹配";
    

    
    /***************************查询结果界面****************************/ 
    public static final String RESULT_FORM = "queryResult";
    //行选择方式
    public static final String ROW_SELECT_TYPE  = "rowSelectType";
    //查询结果列表字段
    public static final String LIST_FIELD = "listField";
    
    /***************************详细信息页面****************************/ 
    public static final String DETAIL_IFNO_FORM = "detailInfo";
    //详细信息字段
    public static final String SHOW_FIELD = "showField";
    
    
    /***************************录入信息页面****************************/ 
    public static final String ADD_FORM = "form";
    //录入信息字段
    public static final String ADD_FIELD = "addField";
    
    
    /***************************页面组件Tag****************************/  
    public static final String TAG = "tag";
    public static final String ATTRIBUTYE = "attr";
    public static final String ATTRIBUTE_VALUE = "attrValue";
    public static final String DEFAULT_VALUE = "defaultValue";
    /*****************************关联实体属性 **************************/
    //关联查询实体属性集合
    public static final String  REF_VENTITYFIELDS="refVentityFields";
    //关联数据集
    public static final String  REF_DATASET="refdataset";
    //关联实体
    public static final String  REF_ENTITY="refentity";
    //关联字段
    public static final String  REF_FIELD="reffield";
    
    

}
