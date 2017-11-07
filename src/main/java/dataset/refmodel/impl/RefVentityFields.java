package dataset.refmodel.impl;

import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.model.IField;
import dataset.model.impl.Field;
import dataset.refmodel.IRefDataset;
import dataset.refmodel.IRefVentityFields;
import dataset.util.StringUtil;
import dataset.util.XMLUtil;

public class RefVentityFields extends Ref implements IRefVentityFields {
    protected IRefDataset refDataset = null ;
	@Override
	public void fromXML(Object object) {
		if (!(object instanceof Element)) {
			return;
		}
		Element refVentityFieldsElem = (Element) object;
		this.loadRef(refVentityFieldsElem);
		if (refVentityFieldsElem != null) {
		       Iterator iterator = XMLUtil.getChildElements(refVentityFieldsElem, IDatasetConstant.REF_DATASET).iterator();
	            while (iterator.hasNext()) {
	                Element DatasetElem = (Element) iterator.next();
	                this.refDataset = new RefDataset();
	                refDataset.fromXML(DatasetElem);
	            }
		}
	}

	@Override
	public Element toXML(Document document) {
		Element element = XMLUtil.createElement(document,
				IDatasetConstant.REF_VENTITYFIELDS);
		this.buildElement(element);
		return element;
	}

	@Override
	public Collection getRefDatasets() {
		// TODO Auto-generated method stub
		return null;
	}

}
