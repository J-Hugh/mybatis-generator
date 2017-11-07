package dataset.viewmodel.impl;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.XMLUtil;
import dataset.viewmodel.IListField;
import dataset.viewmodel.IResultForm;

/**
 * 展示模型结果信息定义
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class ResultForm extends Form implements IResultForm {
    private String rowSelectType = "checkbox";

    public void fromXML(Object obj) {
        if(!(obj instanceof Element)){
            return;
        }
        Element element = (Element)obj;
        this.loadForm(element);
        this.setRowSelectType(XMLUtil.getAttribute(element, IDatasetConstant.ROW_SELECT_TYPE));
        
        Iterator iterator = XMLUtil.getChildElements(element, IDatasetConstant.LIST_FIELD).iterator();
        while (iterator.hasNext()) {
            Element elem = (Element) iterator.next();
            IListField field = new ListField();
            field.fromXML(elem);
            field.setParentForm(this);
            this.getFields().add(field);
        }
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.RESULT_FORM);
        this.buildElement(element);
        
        XMLUtil.addAttribute(element, IDatasetConstant.ROW_SELECT_TYPE, this.getRowSelectType());
        
        return element;     
    }

    public String getRowSelectType() {
        return rowSelectType;
    }

    public void setRowSelectType(String rowSelectType) {
        this.rowSelectType = rowSelectType;
    }
    
}
