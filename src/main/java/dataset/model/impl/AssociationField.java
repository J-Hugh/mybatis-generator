package dataset.model.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.model.IAssociationField;
import dataset.model.IField;
import dataset.util.XMLUtil;

/**
 * 实体关联关系中字段映射对象
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class AssociationField implements IAssociationField {

    private IField srcField;
    private IField tgtField;
    private String description;
    private String tgtFieldId;
    
    
    public void fromXML(Object obj) {
        Element element = null;
        if (!(obj instanceof Element)) {
            return;
        }
        element = (Element) obj;
        tgtFieldId = XMLUtil.getAttribute(element, IDatasetConstant.TARGET_FIELD_ID);
        this.setDescription(XMLUtil.getAttribute(element, IDatasetConstant.DESC));
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.FKASSOCIATIONFIELD);
        
        XMLUtil.addAttribute(element, IDatasetConstant.SOURCE_FIELD_ID, this.getSrcField().getId());
        XMLUtil.addAttribute(element, IDatasetConstant.SOURCE_FIELD_NAME, this.getSrcField().getName());
        if(tgtField != null){
            XMLUtil.addAttribute(element, IDatasetConstant.TARGET_FIELD_ID, this.getTgtField().getId());
            XMLUtil.addAttribute(element, IDatasetConstant.TARGET_FIELD_NAME, this.getTgtField().getName());
        }
        XMLUtil.addAttribute(element, IDatasetConstant.DESC, this.getDescription());
        
        return element;
    }

    public IField getSrcField() {
        return srcField;
    }

    public void setSrcField(IField srcField) {
        this.srcField = srcField;
    }

    public IField getTgtField() {
        return tgtField;
    }

    public void setTgtField(IField tgtField) {
        this.tgtField = tgtField;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTgtFieldId() {
        if (tgtField != null) {
            return tgtField.getId();
        }
        return tgtFieldId;
    }

    public void setTgtFieldId(String tgtFieldId) {
        this.tgtFieldId = tgtFieldId;
    }


}
