package dataset.viewmodel.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.XMLUtil;
import dataset.viewmodel.IShowField;

/**
 * 展示模型详细信息字段定义
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class ShowField extends VMField implements IShowField {
    
    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.SHOW_FIELD);
        this.buildElement(element);
        
        return element;
    }
}
