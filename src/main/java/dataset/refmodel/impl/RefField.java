package dataset.refmodel.impl;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.refmodel.IRefField;
import dataset.util.XMLUtil;

public class RefField extends Ref implements IRefField {

	@Override
	public void fromXML(Object object) {
		if (!(object instanceof Element)) {
			return;
		}
		Element element = (Element) object;
		this.loadRef(element);
	}

	@Override
	public Element toXML(Document document) {
		Element element = XMLUtil.createElement(document,
				IDatasetConstant.REF_FIELD);
		this.buildElement(element);
		return element;
	}

}
