package dataset.viewmodel.impl;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.XMLUtil;
import dataset.viewmodel.IListField;

/**
 * 展示模型查询结果信息字段
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class ListField extends VMField implements IListField {
    
    private String width;

    public void fromXML(Object obj) {
        if(!(obj instanceof Element)){
            return;
        }
        Element element = (Element)obj;
        super.fromXML(element);
        this.setWidth(XMLUtil.getAttribute(element, IDatasetConstant.WIDTH));
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.LIST_FIELD);
        this.buildElement(element);
        XMLUtil.addAttribute(element, IDatasetConstant.WIDTH, this.getWidth());
        return element;
    }


    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

}
