package dataset.viewmodel.impl;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.XMLUtil;
import dataset.viewmodel.IDetailInfoForm;
import dataset.viewmodel.IShowField;

/**
 * 展示模型详细信息定义
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class DetailInfoForm extends Form implements IDetailInfoForm {
	
	private String showType;

    public void fromXML(Object obj) {
        if(!(obj instanceof Element)){
            return;
        }
        Element element = (Element)obj;
        this.loadForm(element);
        this.setShowType(XMLUtil.getAttribute(element, IDatasetConstant.CONTENT_SHOW_TYPE));
        
        Iterator iterator = XMLUtil.getChildElements(element, IDatasetConstant.SHOW_FIELD).iterator();
        while (iterator.hasNext()) {
            Element elem = (Element) iterator.next();
            IShowField field = new ShowField();
            field.setParentForm(this);
            field.fromXML(elem);
            this.getFields().add(field);
        }
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.DETAIL_IFNO_FORM);
        this.buildElement(element);
        XMLUtil.addAttribute(element, IDatasetConstant.CONTENT_SHOW_TYPE, this.getShowType());        
        return element;     
    }
	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}
}
