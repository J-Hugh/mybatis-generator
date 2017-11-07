package dataset.viewmodel.impl;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.StringUtil;
import dataset.util.XMLUtil;
import dataset.viewmodel.IAddField;

/**
 * 展示模型录入信息字段
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class AddField extends VMField implements IAddField {
    private boolean isDisplay;
    
    public void fromXML(Object obj){
        if(!(obj instanceof Element)){
            return;
        }
        Element element = (Element) obj;
        super.fromXML(element);
        this.setDisplay(StringUtil.toBoolean(XMLUtil.getAttribute(element, IDatasetConstant.IS_DISPLAY)));
        
        Element tagElem = XMLUtil.getChildElement(element, IDatasetConstant.TAG);
        if(tagElem != null){
            this.tag = new Tag();
            tag.fromXML(tagElem);
        }
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.ADD_FIELD);
        this.buildElement(element);
        XMLUtil.addAttribute(element, IDatasetConstant.IS_DISPLAY, String.valueOf(this.isDisplay()));
        return element;
    }


    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }
}
