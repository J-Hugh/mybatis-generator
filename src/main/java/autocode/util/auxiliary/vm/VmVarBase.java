package autocode.util.auxiliary.vm;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;

import autocode.util.StringUtil;
import dataset.model.IEntity;
import dataset.viewmodel.IForm;
import dataset.viewmodel.IVMField;

public abstract class VmVarBase {
	
    public static final String PAGE_BASE_RELATIVE_PATH = "WebApp/WEB-INF/views/";
    public static final String WEB_ROOT = "WebApp";
    
    
    protected IEntity entity;
    protected String title = "";  //页面标题
    
    protected String formId = "";  //默认表单Id
    protected boolean isUseI18N = false; //是否使用国际化
    protected String pageEncoding = "UTF-8";
    protected String viewJsPath;
    protected static final String CONTENT_KEY = "viewInfo";
    protected String contentKey = CONTENT_KEY;
    

    /**
     * 获取VelocityContext中映射当前对象的key值
     * @return
     */
    public String getContentKey(){
        if(StringUtil.isBlank(contentKey)){
            contentKey = CONTENT_KEY;
        }
        return contentKey;
    }
    /**
     * 设置VelocityContext中映射当前对象的key值
     * @param contentKey
     */
    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }
    /**
     * 获取当前对象的VelocityContext
     * @return
     */
    public VelocityContext getVelocityContext(){
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put(getContentKey(), this);
        return velocityContext;
    }
    
    /**
     * 获取form表单ID
     * @return
     */
    public String getFormId() {
        return formId;
    }
    /**
     * 获取实体名称
     * @return
     */
    public abstract String getEntityName();
	
    /**
     * 是否使用国际化配置
     * @return
     */
    public boolean isUseI18N() {
        return isUseI18N;
    }

    /**
     * 是否使用国际化配置
     * @param isUseI18N
     */
    public void setUseI18N(boolean isUseI18N) {
        this.isUseI18N = isUseI18N;
    }
    
    /**
     * 获取页面编码，默认为UTF-8
     * @return
     */
    public String getPageEncoding() {
		return pageEncoding;
	}

    /**
     * 设置页面编码，默认为UTF-8
     * @return
     */
    public void setPageEncoding(String pageEncoding) {
    	if(StringUtil.isBlank(pageEncoding)){
    		this.pageEncoding = "UTF-8";
    	}
		this.pageEncoding = pageEncoding;
	}
    
    /**
     * 返回当前的显示模型定义
     * @return IForm
     */
    public abstract IForm getViewModel();
    
    /**
     * 获取form表单 enctype
     * @return
     */
    public String getFormEncType(){
    	IForm form = getViewModel();
    	if(form == null){
    		return "";
    	}
    	Iterator iterator =form.getFields().iterator();
    	while (iterator.hasNext()) {
			IVMField field = (IVMField) iterator.next();
			if(field.getTag().getTagType().equals("file")){
				return "enctype=\"multipart/form-data\"";
			}
		}
    	return "";
    }
    
    /**
     * 获取Web页面的访问URI路径
     *  示例：
     *      fullPath = "F:/EclipseSpace/BizTest/WebApp/pages/pm/omProduct/omProductShow.jsp"
     *      subfix = ".jsp"
     *      return "pm/omProduct/omProductShow"
     * @param fullPath Web页面完全路径
     * @param subfix Web页面文件扩展名，如果为null或者空字符串则默认为".jsp"
     * @return 
     */
    public static String getViewPath(String fullPath,String subfix) {
        if(StringUtil.isBlank(fullPath)){
            return fullPath;
        }
        if(StringUtil.isBlank(subfix)){
            subfix = ".jsp";
        }
        int index = fullPath.indexOf(PAGE_BASE_RELATIVE_PATH);
        int length = PAGE_BASE_RELATIVE_PATH.length();
        int start = index + length;
        int end = fullPath.length() - subfix.length();
        return fullPath.substring(start,end);
    }
    
    /**
     * 设置页面对应的JS文件的Web路径
     *  示例：
     *      fullPath = "F:/EclipseSpace/BizTest/WebApp/pages/pm/omProduct/omProductShow.jsp"
     *      return "/WebApp/WEB-INF/pages/pm/omProduct/omProductShow.jsp"
     * @param fullPath Web页面完全路径
     * @return 
     */
    public void setViewJsPath(String fullPath){
        if(StringUtil.isBlank(fullPath)){
            viewJsPath = fullPath;
        }
        int index = fullPath.indexOf(WEB_ROOT);
        int start = index + WEB_ROOT.length();
        viewJsPath = fullPath.substring(start); 
    }
    
    /**
     * 获取页面对应的JS文件的Web路径
     * @return
     */
    public String getViewJsPath(){
        return viewJsPath;
    }
    
    
	/**
	 * 是否需要生成查询列表页面
	 * @return
	 */
	public boolean hasList(){
		return entity.getResultForm().getFields().size() > 0;
	}
	/**
	 * 是否需要生成新增或编辑页面
	 * @return
	 */
	public boolean hasAddOrEdit(){
		return entity.getNewForm().getFields().size() > 0;
	}
	/**
	 * 是否需要生成详细页面
	 * @return
	 */
	public boolean hasShow(){
		return entity.getDetailInfoForm().getFields().size() > 0;
	}
	
	/**
	 * 是否需要生成详细页面
	 * @return
	 */
	public boolean hasExport(){
		return entity.getQueryForm().isExportExcel();
	}

    
    /**
     * 获取查询按钮标题名称
     * @return
     */
    public String getSearchTitle(){
        if(isUseI18N()){
            return BUTTON_SEARCH;
        }
        return "查询";
    }


	/**
     * 获取提交按钮标题名称
     * @return
     */
    public String getSubmitTitle(){
        if(isUseI18N()){
            return BUTTON_SUBMIT;
        }
        return "提交";
    }
    /**
     * 获取重置按钮标题名称
     * @return
     */
    public String getResetTitle(){
        if(isUseI18N()){
            return BUTTON_RESET;
        }
        return "重置";
    }
    /**
     * 获取返回按钮标题名称
     * @return
     */
    public String getBackTitle(){
        if(isUseI18N()){
            return BUTTON_BACK;
        }
        return "返回";
    }
    /**
     * 获取查看按钮标题名称
     * @return
     */
    public String getViewTitle(){
        if(isUseI18N()){
            return BUTTON_VIEW;
        }
        return "查看";
    }
    /**
     * 获取添加按钮标题名称
     * @return
     */
    public String getAddTitle(){
        if(isUseI18N()){
            return BUTTON_ADD;
        }
        return "增加";
    }
    /**
     * 获取编辑按钮标题名称
     * @return
     */
    public String getEditTitle(){
        if(isUseI18N()){
            return BUTTON_EDIT;
        }
        return "编辑";
    }
    /**
     * 获取删除按钮标题名称
     * @return
     */
    public String getDeleteTitle(){
        if(isUseI18N()){
            return BUTTON_DELETE;
        }
        return "删除";
    }
    /**
     * 获取导出按钮标题名称
     * @return
     */
    public String getExportTitle(){
        if(isUseI18N()){
            return BUTTON_EXPORT;
        }
        return "导出";
    }
    /**
     * 获取查询条件标题名称
     * @return
     */
    public String getSearchConditionTitle(){
        if(isUseI18N()){
            return LABLE_SEARCH_CONDITION;
        }
        return "查询条件";
    }
    
    protected static final String BUTTON_SEARCH = "<fmt:message key='button.search'/>";
    protected static final String BUTTON_SUBMIT = "<fmt:message key='button.submit'/>";
    protected static final String BUTTON_RESET = "<fmt:message key='button.reset'/>";
    protected static final String BUTTON_BACK = "<fmt:message key='button.back'/>";
    protected static final String BUTTON_VIEW = "<fmt:message key='button.view'/>";
    protected static final String BUTTON_ADD = "<fmt:message key='button.add'/>";
    protected static final String BUTTON_EDIT = "<fmt:message key='button.edit'/>";
    protected static final String BUTTON_DELETE = "<fmt:message key='button.delete'/>";
    protected static final String BUTTON_EXPORT = "<fmt:message key='button.export'/>";
    protected static final String LABLE_SEARCH_CONDITION = "<fmt:message key='lable.search.condition'/>";
    
}
