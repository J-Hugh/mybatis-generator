package dataset.refmodel.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.refmodel.IRef;
import dataset.util.XMLUtil;

public abstract class Ref implements IRef {
	private String id = null;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void fromXML(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public Element toXML(Document document) {
		return null;
	}

	/**
	 * 构建包含基本信息的Element对象
	 * 
	 * @param element
	 */
	protected void buildElement(Element element) {
		if (element == null) {
			return;
		}
		XMLUtil.addAttribute(element, IDatasetConstant.ID, this.getId());
	}

	/**
	 * 加载实体基本信息
	 * 
	 * @param element
	 */
	protected void loadRef(Element element) {
		this.setId(XMLUtil.getAttribute(element, IDatasetConstant.ID));
	}

}
