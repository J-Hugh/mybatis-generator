package autocode.util.auxiliary.vm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

/**
 * 
 * @author BizFoundation Team: LiuJun
 * 
 *         {注释}
 * 
 * @version 1.0
 * @since 4.3
 */
public class VmVarJavaBaseInfo {
    protected static final String VELOCITY_CONTEXT_KEY = "javaInfo";

    private String packageName = ""; // 包名

    private List importTypes = Collections.EMPTY_LIST; // 需要import的类

    private String className = ""; // 类名
    
    private String indexName = ""; // 类名

    private String entityName = ""; // 实体名称

    private String basePackageName = "";//用户选择的包路径

    private String classExtentsClass = ""; // 需要继承的类
    
    private VelocityContext vc = null;

    private String classImplementInterfaces = ""; // 需要实现的接口

    private String beanName; // 注册到spring的ApplicationContext.xml文件的bean的名称

    private String beanClass; // 注册到spring的ApplicationContext.xml文件的bean的类名

    private Map registBeanNames = Collections.EMPTY_MAP;// 需要set注入的Bean

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List getImportTypes() {
        return importTypes;
    }

    public void setImportTypes(List importTypes) {
        this.importTypes = importTypes;
    }

    public void addImportTypes(String importType) {
        if (this.importTypes.isEmpty()) {
            this.importTypes = new ArrayList();
        }
        this.importTypes.add(importType);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
	 * 获取 indexName
	 * @return 返回 indexName
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * 设置 indexName
	 * @param 对indexName进行赋值
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getClassExtentsClass() {
        return classExtentsClass;
    }

    public void setClassExtentsClass(String classExtentsClass) {
        this.classExtentsClass = classExtentsClass;
    }

    public String getClassImplementInterfaces() {
        return classImplementInterfaces;
    }

    public void setClassImplementInterfaces(String classImplementInterfaces) {
        this.classImplementInterfaces = classImplementInterfaces;
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * 获取当前对象的VelocityContext
     * 
     * @return VelocityContext
     */
    public VelocityContext getVelocityContext() {
//        VelocityContext velocityContext = new VelocityContext();
//        velocityContext.put(VELOCITY_CONTEXT_KEY, this);
//        return velocityContext;
    	return vc;
    }
    
    public void setVelocityContext(VelocityContext context){
    	this.vc = context;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public Map getRegistBeanNames() {
        return registBeanNames;
    }

    public void setRegistBeanNames(Map registBeanNames) {
        this.registBeanNames = registBeanNames;
    }
}
