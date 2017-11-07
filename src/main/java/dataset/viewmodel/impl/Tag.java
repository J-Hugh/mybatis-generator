package dataset.viewmodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.XMLUtil;
import dataset.viewmodel.ITag;
import dataset.viewmodel.ITagAttribute;

/**
 * 展示模型字段的展示控件
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class Tag implements ITag {
    
    private String tagType;
    private Collection attributes;

    public void fromXML(Object obj) {
        if(!(obj instanceof Element)){
            return;
        }
        Element element = (Element)obj;
        this.setTagType(XMLUtil.getAttribute(element, IDatasetConstant.TYPE));
        
        Iterator iterator = XMLUtil.getChildElements(element, IDatasetConstant.ATTRIBUTYE).iterator();
        while (iterator.hasNext()) {
            Element elem = (Element) iterator.next();
            ITagAttribute attribute = new TagAttribute();
            attribute.fromXML(elem);
            this.getAttributes().add(attribute);
        }
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.TAG);
        XMLUtil.addAttribute(element, IDatasetConstant.TYPE, this.getTagType());
        
        Iterator iterator = this.getAttributes().iterator();
        while (iterator.hasNext()) {
            ITagAttribute attribute = (ITagAttribute) iterator.next();
            XMLUtil.addChildElement(element, attribute.toXML(document));
        }
        
        return element;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String controlType) {
        this.tagType = controlType;
    }

    public Collection getAttributes() {
        if(attributes == null){
            attributes = new ArrayList();
        }
        return attributes;
    }

    public void setAttributes(Collection attributes) {
        this.attributes = attributes;
    }

}
