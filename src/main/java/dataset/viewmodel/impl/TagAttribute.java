package dataset.viewmodel.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.XMLUtil;
import dataset.viewmodel.ITagAttribute;

/**
 * 展示模型字段的展示控件属性
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class TagAttribute implements ITagAttribute {

    private String name;
    /**
     * type类型property | event | validate | style
     */
    private String type;
    private String attrValue;
    private String defaultValue;

    public void fromXML(Object obj) {
        if (!(obj instanceof Element)) {
            return;
        }
        Element element = (Element) obj;
        this.setName(XMLUtil.getAttribute(element, IDatasetConstant.NAME));
        this.setType(XMLUtil.getAttribute(element, IDatasetConstant.TYPE));
        this.setDefaultValue(XMLUtil.getAttribute(element, IDatasetConstant.DEFAULT_VALUE));
        this.setAttrValue(XMLUtil.getAttribute(element, IDatasetConstant.ATTRIBUTE_VALUE));
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.ATTRIBUTYE);
        XMLUtil.addAttribute(element, IDatasetConstant.NAME, this.getName());
        XMLUtil.addAttribute(element, IDatasetConstant.ATTRIBUTE_VALUE, this.getAttrValue());
        XMLUtil.addAttribute(element, IDatasetConstant.DEFAULT_VALUE, this.getDefaultValue());
        XMLUtil.addAttribute(element, IDatasetConstant.TYPE, this.getType());
        
        return element;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
