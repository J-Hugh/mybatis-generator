package dataset.refmodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.refmodel.IRefEntity;
import dataset.refmodel.IRefField;
import dataset.util.XMLUtil;

public class RefEntity extends Ref implements IRefEntity {
	protected Collection refFields = null;

	@Override
	public Collection getRefFields() {
		if (refFields == null) {
			refFields = new ArrayList();
		}
		return refFields;
	}

	public void setRefFields(Collection refFields) {
		this.refFields = refFields;
	}

	public void addRefField(IRefField refField) {
		if (refFields == null) {
			refFields = new ArrayList();
		}
		refFields.add(refField);

	}
	@Override
	public void fromXML(Object object) {
		if (!(object instanceof Element)) {
			return;
		}
		Element entityElement = (Element) object;
		this.loadRef(entityElement);
		if (entityElement != null) {
			Iterator iterator = XMLUtil.getChildElements(entityElement,
					IDatasetConstant.REF_FIELD).iterator();
			while (iterator.hasNext()) {
				Element fieldElem = (Element) iterator.next();
				IRefField refField = new RefField();
				refField.fromXML(fieldElem);
				addRefField(refField);
			}
		}
	}

	@Override
	public Element toXML(Document document) {
		Element element = XMLUtil.createElement(document,IDatasetConstant.REF_ENTITY);
		this.buildElement(element);
		Iterator refFieldIterator = this.getRefFields().iterator();
		while (refFieldIterator.hasNext()) {
			IRefField refField = (IRefField) refFieldIterator.next();
			XMLUtil.addChildElement(element,refField.toXML(element.getOwnerDocument()));
		}
		return element;
	}

}
