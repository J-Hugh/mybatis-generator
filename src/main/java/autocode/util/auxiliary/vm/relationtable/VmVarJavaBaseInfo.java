package autocode.util.auxiliary.vm.relationtable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import autocode.util.StringUtil;
import dataset.RelationType;
import dataset.model.IAssociationField;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IPEntity;
import dataset.model.IRelation;
import dataset.model.IVEntity;
import dataset.viewmodel.IVMField;
import dataset.viewmodel.impl.AddField;

/**
 * 
 * @author BizFoundation Team: chenhua
 * 
 *         {注释}
 * 
 * @version 5.0
 * @since 4.3
 */
public abstract class VmVarJavaBaseInfo {
    protected static final String VELOCITY_CONTEXT_KEY = "javaInfo";

    protected String packageName = ""; // 包名
    
    protected String basePackageName = ""; // 包名
    
    protected String module;//模块名称
    
    protected String encode = "UTF-8";//字符编码

    protected IVEntity entity = null;

    protected List importTypes = Collections.EMPTY_LIST; // 需要import的类

    protected String className = ""; // 类名

    protected String classExtentsClass = ""; // 需要继承的类

    protected String classImplementInterfaces = ""; // 需要实现的接口

    protected String poFirstUpperName; // po 类名(第一个字母大写)
    
    protected String poFirstLowerName; //  po 类名(第一个字母小写)
    
    protected String poPKFirstLowerName; // po 主键第一个字母小写)
    
    protected String poPKName; // po 主键
    
    private Map registBeanNames = Collections.EMPTY_MAP;// 需要set注入的Bean

    public VmVarJavaBaseInfo(IVEntity entity) {
        this.entity = entity;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassFirstUpperName() {
        return StringUtil.upperFirst(className);
    }

    public List getImportTypes() {
        return importTypes;
    }

    public void setImportTypes(List importTypes) {
        this.importTypes = importTypes;
    }

	/**
	 * 获取模块名称
	 * @return
	 */
	public String getModule() {
		return module;
	}
	/**
	 * 设置模块名称
	 * @param module
	 */
	public void setModule(String module) {
		this.module = module;
	}
	
	/**
	 * 获取基础package名称
	 * @return
	 */
    public String getBasePackageName() {
		return basePackageName;
	}
    /**
     * 设置基础package名称
     * @param basePackageName
     */
	public void setBasePackageName(String basePackageName) {
		this.basePackageName = basePackageName;
	}
	
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		if(StringUtil.isBlank(encode)){
			encode = "UTF-8";
		}
		this.encode = encode;
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

    public String getClassExtentsClass() {
        return classExtentsClass;
    }

    public void setClassExtentsClass(String classExtentsClass) {
        this.classExtentsClass = classExtentsClass;
    }

    public IVEntity getEntity() {
        return entity;
    }

    public void setEntity(IVEntity entity) {
        this.entity = entity;
    }
    /**
     * 获取录入页面中选择的实体
     * @return
     */
    public List<IPEntity> getNewFromRefEntity(){
        List<IPEntity> pentitys = new ArrayList();
        List<AddField> addfields = (List<AddField>) this.entity.getNewForm().getFields();
        for (int i = 0; i < addfields.size(); i++) {
            AddField addField = addfields.get(i);
            boolean mark = false;
            if(pentitys.contains(addField.getRefEntity())){
                mark = true;
                
            }
            if (!mark) {
                pentitys.add((IPEntity) addField.getRefEntity());
            }
        }
        return pentitys;
    }
    public String getClassImplementInterfaces() {
        return classImplementInterfaces;
    }
    
    public void setClassImplementInterfaces(String classImplementInterfaces) {
        this.classImplementInterfaces = classImplementInterfaces;
    }

    /**
     * 获取当前对象的VelocityContext
     * 
     * @return VelocityContext
     */
    public VelocityContext getVelocityContext() {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put(VELOCITY_CONTEXT_KEY, this);
        return velocityContext;
    }

    public Map getRegistBeanNames() {
        return registBeanNames;
    }
    
	public String getPoFirstUpperName() {
		return poFirstUpperName;
	}

	public void setPoFirstUpperName(String poFirstUpperName) {
		this.poFirstUpperName = poFirstUpperName;
	}

	public String getPoFirstLowerName() {
		return poFirstLowerName;
	}

	public void setPoFirstLowerName(String poFirstLowerName) {
		this.poFirstLowerName = poFirstLowerName;
	}

	public String getPoPKFirstLowerName() {
		return poPKFirstLowerName;
	}

	public void setPoPKFirstLowerName(String poPKFirstLowerName) {
		this.poPKFirstLowerName = poPKFirstLowerName;
	}

	public String getPoPKName() {
		return poPKName;
	}

	public void setPoPKName(String poPKName) {
		this.poPKName = poPKName;
	}

	public void setRegistBeanNames(Map registBeanNames) {
        this.registBeanNames = registBeanNames;
    }
    
	/**
	 * 获取录入（新增、编辑）界面引用的数据实体列表
	 * @return
	 */
	public List<IEntity> getEditRefEntities() {
		List<IEntity> refEntities = new ArrayList<IEntity>();
		Iterator iterator = this.entity.getNewForm().getFields().iterator();
		while (iterator.hasNext()) {
			IVMField vField = (IVMField) iterator.next();
			IEntity refEntity = vField.getRefEntity();
			if(!StringUtil.judgeEntitys(refEntities, refEntity)){
				refEntities.add(refEntity);
			}
		}
		return refEntities;
	}

	/**
	 * 获取被编辑（save、update）实体列表，列表中实体已经按照保存的先后顺序进行排列
	 * @return
	 */
	public List<IEntity> getEditEntityArgs(){
		List<IRelation> relations = getEditRelationArgs();
		List<IEntity> entities = new ArrayList<IEntity>();
		for(int i=0;i<relations.size();i++){
			IRelation relation = relations.get(i);
			if(i == 0){
				entities.add(relation.getSrcEntity());
			}
			entities.add(relation.getTgtEntity());
		}
		if(entities.size() == 0){
			return getEditRefEntities();
		}
		return entities;
	}
	
	/**
	 * 根据关联关系定义将源端实体的主键值设置到目标端实体
	 * 示例： user.setOrgId(org.getId());
	 * @param relation
	 * @return
	 */
	public String setRelationValue(IRelation relation){
		if(relation == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		IEntity srcEntity = relation.getSrcEntity();
		IEntity tgtEntity = relation.getTgtEntity();
		Iterator iterator = relation.getRelationFields().iterator();
		while (iterator.hasNext()) {
			IAssociationField associationField = (IAssociationField) iterator.next();
			IField srcField = associationField.getSrcField();
			IField tgtField = associationField.getTgtField();
			String srcGetMethod = StringUtil.lowerFirst(srcEntity.getName()) + "." + getFieldGetMethod(srcField) + "()";
			sb.append(StringUtil.lowerFirst(tgtEntity.getName()) + "." + getFieldSetMethod(tgtField) + "(" + srcGetMethod + ");\n");
		}
		return sb.toString();
	}
	
	/**
	 * 获取1对1场景下各个持久化实体的关联关系集合,列表中关联关系已经按照实体保存的先后顺序进行排列
	 * @return
	 */
	public List<IRelation> getEditRelationArgs(){
		List refEntities = getEditRefEntities();
		if(refEntities.size() <= 1){
			return new ArrayList<IRelation>();
		}
		
		List tmpEntities = new ArrayList();
		tmpEntities.addAll(refEntities);
		
		Map<IEntity, List<IRelation>> relations = new HashMap<IEntity, List<IRelation>>();
		for(int i=0;i<refEntities.size();i++){
			IPEntity entity = (IPEntity)refEntities.get(i);
			List tmp = (List)entity.getSrcRelations();
			for(int j=0;j<tmp.size();j++){
				IRelation relation = (IRelation)tmp.get(j);
				if(relation.getRelationType().equals(RelationType.ONEWAY_ONE2ONE) 
						&& refEntities.contains(relation.getTgtEntity())){
					if(relations.containsKey(entity)){
						relations.get(entity).add(relation);
					}else {
						List<IRelation> rList = new ArrayList<IRelation>();
						rList.add(relation);
						relations.put(entity, rList);
					}
					tmpEntities.remove(relation.getTgtEntity());
				}
			}
		}
		List<IRelation> args = new ArrayList<IRelation>();
		getEditRelationArgs((IEntity)tmpEntities.get(0), relations, args);
		return args;
	}
	
	/**
	 * 获取1对1场景下各个持久化实体的关联关系集合（说明：辅助方法，用于递归遍历实体关联关系）
	 * @param entity
	 * @param map
	 * @param args
	 */
	private void getEditRelationArgs(IEntity entity,Map<IEntity, List<IRelation>> map,List<IRelation> args){
		List<IRelation> list = map.get(entity);
		map.remove(entity);
		if(list == null)
			return;
		for(int i=0;i<list.size();i++){
			IRelation relation = list.get(i);
			args.add(relation);
			if(map.containsKey(relation.getTgtEntity())){
				getEditRelationArgs(relation.getTgtEntity(), map, args);
			}
		}
	}
	/**
	 * 获取字段get方法的名称
	 * @param field
	 * @return
	 */
	public String getFieldGetMethod(IField field){
//		return "get" + StringUtil.upperFirst(field.getName());
		return StringUtil.getJavaBeanFieldGetterStr(field.getName());
	}
	
	public String getFieldGetMethod(String fieldName){
//		return "get" + StringUtil.upperFirst(fieldName);
		return StringUtil.getJavaBeanFieldGetterStr(fieldName);
	}
	
	/**
	 * 获取字段set方法的名称
	 * @param field
	 * @return
	 */
	public String getFieldSetMethod(IField field){
//		return "set" + StringUtil.upperFirst(field.getName());
		return StringUtil.getJavaBeanFieldSetterStr(field.getFieldName());
	}
	
	public String getFieldSetMethod(String fieldName){
//		return "set" + StringUtil.upperFirst(fieldName);
		return StringUtil.getJavaBeanFieldSetterStr(fieldName);
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
	
}
