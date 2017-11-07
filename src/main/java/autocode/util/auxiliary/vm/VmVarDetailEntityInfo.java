package autocode.util.auxiliary.vm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import autocode.util.StringUtil;
import dataset.model.IAssociationField;
import dataset.model.IField;
import dataset.model.IPEntity;
import dataset.model.IRelation;
import dataset.util.DatasetUtil;

/**
 * 代码生成关联表信息定义类
 * 
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.3
 */
public class VmVarDetailEntityInfo {
    
    private IPEntity masterEntity;
    private IPEntity detailEntity;
    private IRelation relation;
    
    private String masterEntityName; //主表实体名称
    private String detailEntityName;//从表实体名称
    
    public VmVarDetailEntityInfo(IPEntity masterEntity,IPEntity detailEntity){
        this.masterEntity = masterEntity;
        this.detailEntity = detailEntity;
        relation = DatasetUtil.findRelationbyTgtEntity(this.detailEntity, this.masterEntity.getSrcRelations());
    }
    
    /**
     * 获取首字母大写的实体名称
     * @return
     */
    public String getPoFirstUpperName(){
        return StringUtil.upperFirst(detailEntity.getName());
    }
    /**
     * 获取首字母大写的实体名称
     * @return
     */
    public String getPoFirstLowerName(){
        return StringUtil.lowerFirst(detailEntity.getName());
    }
    /**
     * 获取联合主键类名称（首字母大写）
     * @return
     */
    public String getPoPkFirstUpperName(){
        if(detailEntity.getPKFields().size()>1){
            return getPoFirstUpperName() + "Pk";
        }
        return null;
    }
    /**
     * 获取联合主键类名称（首字母小写）
     * @return
     */
    public String getPoPkFirstLowerName(){
        if(detailEntity.getPKFields().size()>1){
            return getPoFirstLowerName() + "Pk";
        }
        return null;
    }
    
    /**
     * 是否是联合主键
     * @return
     */
    public boolean isCompositeKey(){
        return (detailEntity.getPKFields().size() >1);
    }
    
    /**
     * 或者讲主表（实体）主键属性值值设置到从表（实体）对应的属性值上的赋值逻辑
     * @return
     */
    public String getLogic(){
        Iterator iterator = relation.getRelationFields().iterator();
        String masterModel = getMasterEntityName();
        if(masterEntity.getPKFields().size() > 1){
            masterModel = masterModel + "." + getGetMethodName(masterModel + "Pk") + "()";
        }
        String detailModel = getDetailEntityName();
        
        StringBuffer sb = new StringBuffer();
        
        if(detailEntity.getPKFields().size() > 1){
            String detailModelGetPk = detailModel + "." + getGetMethodName(detailModel + "Pk") + "()";
            String detailModelSetPk = detailModel + "." + getSetMethodName(detailModel + "Pk");
            sb.append("if(" + detailModelGetPk + " == null ){" + "\n\t\t\t\t\t\t");
            sb.append(detailModelSetPk + "(new " + getPoPkFirstUpperName() + "());" + "\n\t\t\t\t\t");
            sb.append("}");
        }
        
        while (iterator.hasNext()) {
            IAssociationField field = (IAssociationField) iterator.next();
            IField srcField = field.getSrcField();
            IField tgtField = field.getTgtField();
            if(sb.length() > 0){
                sb.append("\n\t\t\t\t\t");
            }
            if(isKeyField(detailEntity,tgtField) && detailEntity.getPKFields().size() > 1){
                sb.append(detailModel + "." + getGetMethodName(detailModel + "Pk") + "()."
                        + getSetMethodName(tgtField.getFieldName()) 
                        + "(" + masterModel + "." + getGetMethodName(srcField.getFieldName()) + "());");                
            }else {
                sb.append(detailModel + "." + getSetMethodName(tgtField.getFieldName()) 
                        + "(" + masterModel + "." + getGetMethodName(srcField.getFieldName()) + "());");
            }
        }
        return sb.toString();
    }
    
    /**
     * 获取属性get方法名称
     * @param fieldName
     * @return
     */
    private String getGetMethodName(String fieldName){
        return "get" + StringUtil.upperFirst(fieldName);
    }
    /**
     * 获取属性set方法名称
     * @param fieldName
     * @return
     */
    private String getSetMethodName(String fieldName){
        return "set" + StringUtil.upperFirst(fieldName);
    }
    
    /**
     * 判断字段是否为实体的主键字段
     * @param entity
     * @param field
     * @return
     */
    private boolean isKeyField(IPEntity entity,IField field){
        if(entity == null || field == null){
            return false;
        }
        Iterator iterator = entity.getPKFields().iterator();
        while (iterator.hasNext()) {
            IField pkField = (IField) iterator.next();
            if(pkField.getId().equals(field.getId())){
                return true;
            }
        }
        return false;
    }
    
    /**
     * 构建关联实体的代码生成信息对象列表
     * @param detailPEntitys
     * @return
     */
    public static List buildVmVarDetailEntityInfo(List detailPEntitys,IPEntity masterEntity){
        List list = new ArrayList();
        if(detailPEntitys == null){
            return list;
        }
        Iterator iterator = detailPEntitys.iterator();
        while (iterator.hasNext()) {
            IPEntity entity = (IPEntity) iterator.next();
            list.add(new VmVarDetailEntityInfo(masterEntity,entity));
        }
        return list;
    }

    /**
     * 获取主表实体名称
     * @return
     */
    public String getMasterEntityName() {
        if(masterEntityName == null){
            masterEntityName = StringUtil.lowerFirst(masterEntity.getName());
        }
        return masterEntityName;
    }
    /**
     * 设置主表实体名称
     * @param masterEntityName
     */
    public void setMasterEntityName(String masterEntityName) {
        this.masterEntityName = masterEntityName;
    }
    /**
     * 获取从表实体名称
     * @return
     */
    public String getDetailEntityName() {
        if(detailEntityName == null){
            detailEntityName = StringUtil.lowerFirst(detailEntity.getName());
        }
        return detailEntityName;
    }
    /**
     * 设置从表实体名称
     * @param detailEntityName
     */
    public void setDetailEntityName(String detailEntityName) {
        this.detailEntityName = detailEntityName;
    }
    
}
