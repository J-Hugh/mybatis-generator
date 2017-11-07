package autocode.util.auxiliary.vm.relationtable;

import java.util.Collection;

import autocode.util.StringUtil;
import dataset.model.IVEntity;
import dataset.viewmodel.IDetailInfoForm;
import dataset.viewmodel.IForm;
import dataset.viewmodel.IVMField;

public class VmVarViewShowInfo extends VmVarViewBaseInfo {
    
    private IDetailInfoForm form;
    private int fieldsLength;

    public VmVarViewShowInfo(IVEntity entity) {
        super(entity,entity.getDetailInfoForm().getTitle());
        form = entity.getDetailInfoForm();
        fieldsLength = form.getFields().size();
    }
    
    
    /**
     * 返回当前的显示模型定义
     */
    public IForm getViewModel(){
    	return form;
    }

    /**
     * 获取录入字段集合
     * @return
     */
    public Collection getFields(){
        return form.getFields();
    }
    
    /**
     * 获取录入字段的个数
     * @return
     */
    public int getFieldsLength() {
        return fieldsLength;
    }
    
    /**
     * 获取字段显示名称
     *  1.如果使用国际化则返回字段的国际化定义  示例：<fmt:message key="user.detailInfo.name"/>
     *  2.如果没有使用国际化，则返回展示模型定义的字段名称
     * @param field
     * @return
     */
    public String getFieldDisplayName(IVMField field){
        if(field == null){
            return null;
        }
        if(isUseI18N){
            return "<fmt:message key=\""+ getlowerFirstEntityName() + ".detailInfo." + field.getFieldName()  + "\"/>";
        }
        return field.getDisplayName();
    }
    /**
     * 获取查看页面标题
     * @return
     */
    public String getTitle() {
        if(this.isUseI18N){
            return "<fmt:message key=\"" + getEntityName(true) + ".show.title\"/>";
        }
        if(StringUtil.isBlank(title)){
            return entity.getDisplayName() + "明细信息";
        }
        return title;
    }
}
