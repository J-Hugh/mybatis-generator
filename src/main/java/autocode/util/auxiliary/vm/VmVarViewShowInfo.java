package autocode.util.auxiliary.vm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import autocode.util.JqGridUtil;
import dataset.model.IPEntity;
import dataset.model.impl.Field;
import dataset.viewmodel.IDetailInfoForm;
import dataset.viewmodel.IForm;
import dataset.viewmodel.IVMField;
import dataset.viewmodel.impl.VMField;

public class VmVarViewShowInfo extends VmVarViewBaseInfo {
    
    private IDetailInfoForm form;
    private int fieldsLength;
    private String showType;

    public VmVarViewShowInfo(IPEntity entity) {
        super(entity);
        form = this.entity.getDetailInfoForm();
        fieldsLength = form.getFields().size();
        showType = form.getShowType();
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
    
    public Collection getNotPkFields() {
		List<VMField> tmpVmFields = new ArrayList<VMField>();
		List<VMField> vmFields = (List<VMField>)form.getFields();
		List<Field> fields = (List<Field>)getPKFields();
		for (VMField vmField : vmFields) {
			for (Field field : fields) {
				if(vmField.getFieldId().equals(field.getId())){
					tmpVmFields.add(vmField);
				}
			}
		}
		if(tmpVmFields.size() > 0){
			vmFields.removeAll(tmpVmFields);
		}
		return vmFields;
	}
    
    /**
     * 获取录入字段的个数
     * @return
     */
    public int getFieldsLength() {
        return fieldsLength;
    }
    /**
     * 获取展示方式： tab、group、common
     * @return
     */
    public String getShowType() {
		return this.showType;
	}
    /**
     * 设置展示方式： tab、group、common
     * @param showType
     */
    public void setShowType(String showType){
    	this.showType = showType;
    }
    /**
     * 设置关联实体的视图信息
     * @param detailPEntitys 关联的持久化实体集合（列表元素类型为：IPEntity）
     */
    public void setRelationEntityViewInfos(List detailPEntitys){
        if(detailPEntitys == null || detailPEntitys.isEmpty()){
            return;
        }
        relationEntityViewInfos.clear();
        Iterator iterator = detailPEntitys.iterator();
        while (iterator.hasNext()) {
            IPEntity entity = (IPEntity) iterator.next();
            VmVarViewShowInfo info = new VmVarViewShowInfo(entity);
            info.setUseI18N(isUseI18N);
            info.setShowType(this.getShowType());
            relationEntityViewInfos.add(info);
        }
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
     * 构建JqGrid定义
     * @return
     */
    public String buildJqGrid(String url){
    	if("tab".equals(getShowType())){
    		return buildJqGrid(form, url, null);
    	}
        return buildJqGrid(form, url);    
    }
    
    /**
     * 获取详细页面JqGrid的ColModel定义的相关模版参数
     * @return
     */
    public Map getColumnMap(){
    	 JqGridUtil gridUtil = new JqGridUtil((IPEntity)entity,form,getlowerFirstEntityName(),"",isUseI18N,"");
    	 Map colMap = gridUtil.initColModelMap();
    	 System.out.println(colMap);
    	 return colMap;
    }
}
