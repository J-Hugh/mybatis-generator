package dataset.viewmodel.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.util.StringUtil;
import dataset.IDatasetConstant;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.util.DatasetUtil;
import dataset.util.XMLUtil;
import dataset.viewmodel.IForm;
import dataset.viewmodel.ITag;
import dataset.viewmodel.IVMField;

/**
 * 展示模型字段
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class VMField implements IVMField {
    protected String fieldId;
    protected String fieldName;
    protected String displayName;
    protected String fieldType;
    protected int index;
    protected ITag tag;
    protected String refDataset = null;
	protected String refEntityId = null;
    protected IEntity refEntity;
    protected String refFieldId = null;
    protected IField refField;
    
    protected IForm parentForm;
    
    
    public void fromXML(Object obj){
        if(!(obj instanceof Element)){
            return;
        }
        Element element = (Element) obj;
        
        this.setFieldId(XMLUtil.getAttribute(element, IDatasetConstant.FIELD_ID));
        this.setFieldName(XMLUtil.getAttribute(element, IDatasetConstant.FIELDNAME));
        this.setDisplayName(XMLUtil.getAttribute(element, IDatasetConstant.DISPLAYNAME));
        this.setFieldType(XMLUtil.getAttribute(element, IDatasetConstant.FIELDTYPE));
        this.setIndex(Integer.parseInt(XMLUtil.getAttribute(element, IDatasetConstant.INDEX)));
        
        this.setRefDataset(XMLUtil.getAttribute(element, IDatasetConstant.REF_DATASET_FILE));
        this.setRefEntityId(XMLUtil.getAttribute(element, IDatasetConstant.REF_ENTITY_ID));
        this.setRefFieldId(XMLUtil.getAttribute(element, IDatasetConstant.REF_FIELD_ID));
        
        Element tagElem = XMLUtil.getChildElement(element, IDatasetConstant.TAG);
        if(tagElem != null){
            this.tag = new Tag();
            tag.fromXML(tagElem);
        }
    }

    public Element toXML(Document document) {
        return null;
    }
    
    /**
     * 构建展示模型字段基本XML信息
     * @param element
     */
    protected void buildElement(Element element) {
        if(element == null){
            return;
        }
        
        XMLUtil.addAttribute(element, IDatasetConstant.FIELD_ID, this.getFieldId());
        XMLUtil.addAttribute(element, IDatasetConstant.FIELDNAME, this.getFieldName());
        XMLUtil.addAttribute(element, IDatasetConstant.DISPLAYNAME, this.getDisplayName());
        XMLUtil.addAttribute(element, IDatasetConstant.FIELDTYPE, this.getFieldType());
        XMLUtil.addAttribute(element, IDatasetConstant.INDEX, String.valueOf(this.getIndex()));
        
        XMLUtil.addAttribute(element, IDatasetConstant.REF_DATASET_FILE, this.getRefDataset());
        XMLUtil.addAttribute(element, IDatasetConstant.REF_ENTITY_ID, this.getRefEntityId());
        XMLUtil.addAttribute(element, IDatasetConstant.REF_FIELD_ID, this.getRefFieldId());
        
        if(tag != null){
            XMLUtil.addChildElement(element, tag.toXML(element.getOwnerDocument()));
        }
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public IForm getParentForm() {
        return parentForm;
    }

    public void setParentForm(IForm parentForm) {
        this.parentForm = parentForm;
    }

    public ITag getTag() {
        return tag;
    }

    public void setTag(ITag tag) {
        this.tag = tag;
    }

	public String getRefEntityId() {
		return refEntityId;
	}
	public void setRefEntityId(String refEntityId) {
		this.refEntityId = refEntityId;
	}
	public String getRefFieldId() {
		return refFieldId;
	}
	public void setRefFieldId(String refFieldId) {
		this.refFieldId = refFieldId;
	}
	
	/**
	 * 获取数据集文件名
	 */
	public String getRefDataset() {
		try{
			if(StringUtil.isNullOrBlank(refDataset)){
				refDataset = StringUtil.getDatasetFileNameFormPath(this.getParentForm().getParentEntity().getParentDataset().getReferenceFilePath());
			}
		}catch (Exception e) {
		}
		return refDataset;
	}
	/**
	 * 获取dataset文件路径
	 * @return
	 */
	public String getRefDatasetPath(){
		return StringUtil.getProjectPathFormPath(this.getParentForm().getParentEntity().getParentDataset().getReferenceFilePath()) +getRefDataset();
	}
	public void setRefDataset(String refDataset) {
		this.refDataset = refDataset;
	}
	
	public IEntity getRefEntity() {
		if(!StringUtil.isNullOrBlank(getRefDataset())&&!StringUtil.isNullOrBlank(getRefEntityId())&& refEntity == null){
			try {
				refEntity = DatasetUtil.getEntity(DatasetUtil.buildDatasetFromXML(getRefDatasetPath()), getRefEntityId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return refEntity;
	}
	
	public IField getRefField() {
		if(getRefEntity() != null && !StringUtil.isNullOrBlank(getRefFieldId()) && refField == null){
			refField = DatasetUtil.getField(getRefEntity(), getRefFieldId());
		}
		return refField;
	}

}
