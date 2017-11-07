/**
 * $Id: VmVarI18nInfo.java,v 1.1 2012/09/11 06:37:46 chenhua Exp $
 */
package autocode.util.auxiliary.vm.relationtable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import autocode.util.StringUtil;
import dataset.model.IField;
import dataset.model.IVEntity;
import dataset.viewmodel.IForm;
import dataset.viewmodel.impl.VMField;



/**
 * <p>
 * 供vm文件使用的变量值对象信息
 * </p>
 * 
 * @author ResourceOne BizFoundation Team: ganjp
 * @version 1.0
 * @since 5.0
 */
public class VmVarI18nInfo {
    public static String LANGUAGE_ZH = "zh";
    public static String LANGUAGE_EN = "en";
    
    private List zhFields;
    private List enFields;
    private VelocityContext velocityContext;
    
    public VmVarI18nInfo(IVEntity vEntity) {
        String modelName = StringUtil.lowerFirst(vEntity.getName());
        //获取显示模型的设置的title
        Map zhTitleMap = new HashMap();
        Map enTitleMap = new HashMap();
        setTitleMap(vEntity.getResultForm(), zhTitleMap, enTitleMap, modelName+".list.title", modelName+" list");
        setTitleMap(vEntity.getNewForm(), zhTitleMap, enTitleMap, modelName+".form.title", modelName+" input");
        setTitleMap(vEntity.getDetailInfoForm(), zhTitleMap, enTitleMap, modelName+".show.title", modelName+" show");
        addZhFields(zhTitleMap);
        addEnFields(enTitleMap);
        //获取显示模型设置的字段信息
        Collection fields = vEntity.getFields();
        Collection queryFormFields = vEntity.getQueryForm().getFields();
        Collection queryResultFields = vEntity.getResultForm().getFields();
        Collection newFormFields = vEntity.getNewForm().getFields();
        Collection detailInfoFields = vEntity.getDetailInfoForm().getFields();
        if (queryFormFields!=null && !queryFormFields.isEmpty()){   
            addFieldMap(fields, queryFormFields, modelName+".queryForm.");
        }
        addFieldMap(fields, queryResultFields, modelName+".queryResult.");
        addFieldMap(fields, newFormFields, modelName+".form.");
        addFieldMap(fields, detailInfoFields, modelName+".detailInfo.");
        //把相关信息设置到velocityContext变量
        velocityContext = new VelocityContext();
        velocityContext.put("PO_FIRST_LOWER_NAME", modelName);
    }
    
    public void setTitleMap(IForm form, Map zhTitleMap, Map enTitleMap, String key, String enTitle) {
        String zhTitle = StringUtil.isBlank(form.getTitle())?enTitle:form.getTitle();
        zhTitleMap.put(key, zhTitle);
        enTitleMap.put(key, enTitle);
    }
    
    public void addFieldMap(Collection srcFileds, Collection newFileds, String prefix) {
        Map zhFieldMap = new HashMap();
        Map enFieldMap = new HashMap();
        if (newFileds!=null && !newFileds.isEmpty()) {
            for (Iterator iterator = newFileds.iterator(); iterator.hasNext();) {
                VMField field = (VMField) iterator.next();
                String displayName = StringUtil.isBlank(field.getDisplayName())?field.getFieldName():field.getDisplayName();
                zhFieldMap.put(prefix+field.getFieldName(), displayName);
                enFieldMap.put(prefix+field.getFieldName(), field.getFieldName());
            }
        } else {
            for (Iterator iterator = srcFileds.iterator(); iterator.hasNext();) {
                IField field = (IField) iterator.next();
                String displayName = StringUtil.isBlank(field.getDisplayName())?field.getFieldName():field.getDisplayName();
                zhFieldMap.put(prefix+field.getFieldName(), displayName);
                enFieldMap.put(prefix+field.getFieldName(), field.getFieldName());
            }
        }
        addZhFields(zhFieldMap);
        addEnFields(enFieldMap);
    }
    
    public VelocityContext getVelocityContext(String language){
        if (LANGUAGE_ZH.equalsIgnoreCase(language)) {
            velocityContext.put("FIELDS", zhFields);
        } else if (LANGUAGE_EN.equalsIgnoreCase(language)) {
            velocityContext.put("FIELDS", enFields);
        }
        return velocityContext;
    }
    
    public List getZhFields() {
        return zhFields;
    }

    public void setZhFields(List zhFields) {
        this.zhFields = zhFields;
    }

    public void addZhFields(Map map) {
        if (this.zhFields==null) {
            this.zhFields = new ArrayList();
        }
        this.zhFields.add(map);
    }
    
    public List getEnFields() {
        return enFields;
    }

    public void setEnFields(List enFields) {
        this.enFields = enFields;
    }
    
    public void addEnFields(Map map) {
        if (this.enFields==null) {
            this.enFields = new ArrayList();
        }
        this.enFields.add(map);
    }
    
}
