package autocode.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import dataset.model.IField;
import dataset.model.IPEntity;
import dataset.viewmodel.IDetailInfoForm;
import dataset.viewmodel.IForm;
import dataset.viewmodel.INewForm;
import dataset.viewmodel.IQueryForm;
import dataset.viewmodel.IResultForm;
import dataset.viewmodel.ITagAttribute;
import dataset.viewmodel.IVMField;

/**
 * JqGrid（可编辑Grid）代码生成工具类
 * 
 * @author BizFoundation Team: LiuJun
 * 
 *         {注释}
 * 
 * @version 1.0
 * @since 4.3
 */
public class JqGridUtil {

    protected String grid;

    protected String pager;

    protected String url; // 示例："<c:url value='/omOrder/listOmOrderItem'/>";

    protected String caption;

    protected String datatype = "json";

    protected String mtype = "post";

    protected String colNames; // 示例："['订单明细ID','订单ID','商品ID','价格','数量','总计']";

    protected int rowNum = 10;

    protected String rowList = "[10, 15, 20, 25, 50, 100]";

    protected boolean viewrecords = true;

    protected boolean multiselect = true;

    protected boolean cellEdit = true;

    protected String cellsubmit = "clientArray";

//  protected int height = 300;

    protected String postData = "{queryFilters:getQueryFilters()}";

    protected String prmNames = "{page:\"pageNo\",rows:\"pageSize\",sort:\"orderFields\",order:\"order\"}";

    protected String jsonReader = "{repeatitems:false}";

    protected boolean toppager = true;

    private List colModePropertyNames;// jqGrid列模型属性名称

    private void initColModeProperties() {
        colModePropertyNames = new ArrayList();
        colModePropertyNames.add("name");
        colModePropertyNames.add("index");
        colModePropertyNames.add("fixed");
        colModePropertyNames.add("key");
        colModePropertyNames.add("editable");
        colModePropertyNames.add("sortable");
        colModePropertyNames.add("width");
        colModePropertyNames.add("sorttype");
        colModePropertyNames.add("label");
        colModePropertyNames.add("hidden");
        colModePropertyNames.add("formatter");
        colModePropertyNames.add("formatoptions");
        colModePropertyNames.add("align");
        colModePropertyNames.add("classes");
        colModePropertyNames.add("datefmt");
        colModePropertyNames.add("defval");
        colModePropertyNames.add("editoptions");
        colModePropertyNames.add("editrules");
        colModePropertyNames.add("edittype");
    }

    private boolean isUseI18N;

    private String lowerEntityName;
    
    private IPEntity entity;
    private IForm form;

    private VelocityContext context;

    /**
     * 构造函数
     * 
     * @param entity
     *            编辑的实体
     * @param form
     *            页面信息定义
     * @param lowerEntityName
     *            实体名称（首字母小写）
     * @param url
     *            查询数据的URL
     * @param isUseI18N
     *            是否使用国际化配置
     * @param caption
     *            标题
     */
    public JqGridUtil(IPEntity entity, IForm form, String lowerEntityName, String url,
            boolean isUseI18N, String caption) {
    	this.form = form;
    	this.entity = entity;
        initColModeProperties();

        this.url = url;
        // url为null，则JqGrid不请求数据，不设置以下内容
        if (StringUtil.isBlank(url)) {
            datatype = "local";
            mtype = null;
            postData = null;
        } else {
            postData = "{queryFilters:get" + StringUtil.upperFirst(lowerEntityName)
                    + "QueryFilters()}";
        }
        this.isUseI18N = isUseI18N;
        this.lowerEntityName = lowerEntityName;
        this.grid = lowerEntityName + "Table";
        this.pager = lowerEntityName + "Page";
        this.caption = caption;
        if (form instanceof INewForm) {
            cellEdit = true;
            toppager = true;
            multiselect = true;
        } else {
            cellEdit = false;
            toppager = false;
            multiselect = false;
        }
        initColNames(entity, form);
        Map colModelMap = initColModelMap();
        Map gridMap = initGridMap(form);
        gridMap.put(COLUMN_MAP, colModelMap);
        context = new VelocityContext(gridMap);
    }

    /**
     * 获取JqGrid模版的VelocityContext
     * 
     * @return
     */
    public VelocityContext getVelocityContext() {
        return context;
    }

    /**
     * 构建JqGrid定义
     * 
     * @param vmFilePath
     * @return
     */
    public String buildJqGrid() {
        String vmFilePath = TagUtil.getScriptVmPath(JQGRID_TYPE);
        String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
        return VmFileUtil
                .getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], getVelocityContext(), null);
    }

    /**
     * 初始化JqGrid模版参数信息
     * 
     * @return
     */
    private Map initGridMap(IForm form) {
        Map gridMap = new HashMap();

        gridMap.put("id", grid);
        gridMap.put("url", url);
        gridMap.put("datatype", datatype);
        gridMap.put("mtype", mtype);
        gridMap.put("colNames", colNames);
        if(caption != null){
        	gridMap.put("caption", caption);
        }
        //gridMap.put("height", new Integer(height));
        gridMap.put("width", "auto");
        gridMap.put("navtype",getNavtype(form));
        gridMap.put("editway","cell");
        if (form instanceof IDetailInfoForm) {
        } else {
            gridMap.put("navopt", "{edit:false,view:false}");
        }
        gridMap.put("multiselect", String.valueOf(multiselect));
        gridMap.put("cellEdit", String.valueOf(cellEdit));
        gridMap.put("cellsubmit", cellsubmit);
        gridMap.put("rowNum", new Integer(rowNum));
        gridMap.put("rowList", rowList);
        gridMap.put("pager", pager);
        gridMap.put("viewrecords", String.valueOf(viewrecords));
        gridMap.put("jsonReader", jsonReader);
        gridMap.put("prmNames", prmNames);
        gridMap.put("postData", postData);
        if (!toppager) {
            gridMap.put("toppager", String.valueOf(toppager));
        }
        return gridMap;
    }

    /**
     * 初始化显示列名称
     * 
     * @param entity
     *            实体
     * @param form
     *            录入信息
     */
    private void initColNames(IPEntity entity, IForm form) {
        this.colNames = "";
        // 如果是联合主键，则添加 compositeKeyStr 字段
        if (entity.getPKFields().size() > 1) {
            colNames += "\"" + COMPOSITE_KEY_NAME + "\"";
        }
        // 添加隐藏的主键列
        Iterator iterator = getPkField(entity, form).iterator();
        while (iterator.hasNext()) {
            IField field = (IField) iterator.next();
            if (colNames.length() > 0) {
                colNames += ",";
            }
            colNames += "\"" + this.getFieldDisplayName(field, form) + "\"";
        }
        // 添加编辑列
        iterator = form.getFields().iterator();
        while (iterator.hasNext()) {
            IVMField field = (IVMField) iterator.next();
            if (colNames.length() > 0) {
                colNames += ",";
            }
            colNames += "\"" + this.getFieldDisplayName(field, form) + "\"";
        }
        colNames = "[" + colNames + "]";
    }

    /**
     * 获取字段显示名称 1.如果使用国际化则返回字段的国际化定义 示例：<fmt:message key="user.name"/>
     * 2.如果没有使用国际化，则返回展示模型定义的字段名称
     * 
     * @param field
     * @return
     */
    public String getFieldDisplayName(IVMField field, IForm form) {
        if (field == null) {
            return null;
        }
        if (isUseI18N) {
            return "<fmt:message key=\"" + this.lowerEntityName + "." + getFormType(form) + "."
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
    public String getFieldDisplayName(IField field, IForm form) {
        if (field == null) {
            return null;
        }
        if (isUseI18N) {
            return "<fmt:message key=\"" + this.lowerEntityName + "." + getFormType(form) + "."
                    + field.getFieldName() + "\"/>";
        }
        return field.getDisplayName();
    }

    private String getFormType(IForm form) {
        if (form instanceof IQueryForm) {
            return "queryForm";
        } else if (form instanceof IResultForm) {
            return "queryResult";
        } else if (form instanceof INewForm) {
            return "form";
        } else if (form instanceof IDetailInfoForm) {
            return "detailInfo";
        }
        return "";
    }
    private String getNavtype(IForm form) {
      if (form instanceof IDetailInfoForm) {
            return "none";
        }
        return "top";
    }
    // 初始化JqGrid列模型模版参数
    public Map initColModelMap() {
        Map colModelMap = new HashMap();

        Map colMap;
        Map editOptionMap;

        // 添加隐藏的主键列
        boolean isSetKeyCol = false;
        // 如果是联合主键，则添加 compositeKeyStr 字段
        if (entity.getPKFields().size() > 1) {
            colMap = new HashMap();
            colMap.put(NAME, COMPOSITE_KEY_NAME);
            colMap.put(HIDDEN, String.valueOf(true));
            colMap.put(JQGRID_KEY_PROPERTITY_NAME, "true");
            colModelMap.put(new Integer(colModelMap.size() + 1), colMap);
            isSetKeyCol = true;
        }
        List pkFields = getPkField(entity, form);
        Iterator iterator = pkFields.iterator();
        while (iterator.hasNext()) {
            colMap = new HashMap();
            IField field = (IField) iterator.next();
            colMap.put(NAME, field.getName());
            colMap.put(HIDDEN, String.valueOf(true));
            if (!isSetKeyCol) {
                colMap.put(JQGRID_KEY_PROPERTITY_NAME, "true");
                isSetKeyCol = true;
            }
            colModelMap.put(new Integer(colModelMap.size() + 1), colMap);
        }

        // 添加编辑列
        iterator = form.getFields().iterator();
        while (iterator.hasNext()) {
            colMap = new HashMap();
            editOptionMap = new HashMap();
            IVMField vmField = (IVMField) iterator.next();
            String editType = vmField.getTag().getTagType();
            if (editType.equals(COMBOBOX)) {
                editType = SELECT;
            }
            Iterator attrIterator = vmField.getTag().getAttributes().iterator();
            while (attrIterator.hasNext()) {
                ITagAttribute attr = (ITagAttribute) attrIterator.next();
                if (colModePropertyNames.contains(attr.getName())) {
                    colMap.put(attr.getName(), attr.getAttrValue());
                } else {
                    if("id".equals(attr.getName())){
                        editOptionMap.put(attr.getName(), "\""+attr.getAttrValue()+"\"");
                    }else{
                        editOptionMap.put(attr.getName(), attr.getAttrValue());
                    }
                }
            }
            colMap.put(LABEL, this.getFieldDisplayName(vmField, form));

            if (cellEdit) {
                colMap.put(EDIT_TYPE, editType);
                colMap.put(EDITABLE, String.valueOf(true));
                colMap.put(EDIT_OPTIONS, editOptionMap);
            }
            if (!isSetKeyCol && isPkField(entity, vmField.getFieldName())) {
                colMap.put(JQGRID_KEY_PROPERTITY_NAME, "true");
                isSetKeyCol = true;
            }
            colModelMap.put(new Integer(colModelMap.size() + 1), colMap);
        }
        return colModelMap;
    }

    /**
     * 获取不是编辑列的主键字段
     * 
     * @param entity
     *            实体
     * @param form
     *            录入信息
     * @return
     */
    private List getPkField(IPEntity entity, IForm form) {
        List pkFields = new ArrayList();
        if (entity == null || form == null) {
            return pkFields;
        }
        entity.getPKFields();
        Iterator iterator = form.getFields().iterator();
        List fieldNames = new ArrayList();
        while (iterator.hasNext()) {
            IVMField field = (IVMField) iterator.next();
            fieldNames.add(field.getFieldName());
        }

        iterator = entity.getPKFields().iterator();
        while (iterator.hasNext()) {
            IField field = (IField) iterator.next();
            if (!fieldNames.contains(field.getFieldName())) {
                pkFields.add(field);
            }
        }
        return pkFields;
    }

    /**
     * 判断是否为主键字段
     * 
     * @param entity
     * @param fieldName
     * @return
     */
    private boolean isPkField(IPEntity entity, String fieldName) {
        if (entity == null || StringUtil.isBlank(fieldName)) {
            return false;
        }
        Iterator iterator = entity.getPKFields().iterator();
        while (iterator.hasNext()) {
            IField field = (IField) iterator.next();
            if (field.getFieldName() == fieldName) {
                return true;
            }
        }
        return false;
    }

    private static final String EDIT_OPTIONS = "editoptions";// 单元格编辑组件的编辑选项

    private static final String COLUMN_MAP = "columnMap";

    private static final String NAME = "name";

    private static final String LABEL = "label";

    private static final String HIDDEN = "hidden";

    private static final String COMBOBOX = "combobox";

    private static final String SELECT = "select";

    private static final String JQGRID_TYPE = "jqgrid";

    private static final String EDITABLE = "editable";

    private static final String EDIT_TYPE = "edittype";

    private static final String COMPOSITE_KEY_NAME = "compositeKeyStr";

    private static final String JQGRID_KEY_PROPERTITY_NAME = "key";

}
