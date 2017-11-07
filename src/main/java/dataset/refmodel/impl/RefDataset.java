package dataset.refmodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.model.IEntity;
import dataset.model.IPField;
import dataset.refmodel.IRefDataset;
import dataset.refmodel.IRefEntity;
import dataset.util.XMLUtil;

public class RefDataset extends Ref implements IRefDataset {
	/**
	 * 工程路径
	 */
	private String projectPath= null;

	protected Collection refEntitys = null;
	/**
	 * 实体的依赖模型
	 */
	protected IEntity parentEntity = null;
	/**
	 * 持久化属性的依赖模型
	 */
	protected IPField parentPField = null;

	public IEntity getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(IEntity parentEntity) {
		this.parentEntity = parentEntity;
	}

	public IPField getParentPField() {
		return parentPField;
	}

	public void setParentPField(IPField parentPField) {
		this.parentPField = parentPField;
	}

	@Override
	public Collection getRefEntitys() {
		if (refEntitys == null) {
			refEntitys = new ArrayList();
		}
		return refEntitys;
	}

	public void setRefEntitys(Collection refEntitys) {
		this.refEntitys = refEntitys;
	}

	public void addRefEntity(IRefEntity refEntity) {
		if (refEntitys == null) {
			refEntitys = new ArrayList();
		}
		refEntitys.add(refEntity);

	}
	public void removeRefEntity(IRefEntity refEntity){
			if (refEntitys == null) {
				return;
			}
			refEntitys.remove(refEntity);
	}
	
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	
	@Override
	public Element toXML(Document document) {
		Element element = XMLUtil.createElement(document,IDatasetConstant.REF_DATASET);
		this.buildElement(element);

		Iterator refEntityIterator = this.getRefEntitys().iterator();
		while (refEntityIterator.hasNext()) {
			IRefEntity refEntity = (IRefEntity) refEntityIterator.next();
			XMLUtil.addChildElement(element,refEntity.toXML(element.getOwnerDocument()));
		}
		return element;
	}

	@Override
	public void fromXML(Object object) {
		if (!(object instanceof Element)) {
			return;
		}
		Element element = (Element) object;
		this.loadRef(element);
		if (element != null) {
			Iterator iterator = XMLUtil.getChildElements(element,IDatasetConstant.REF_ENTITY).iterator();
			while (iterator.hasNext()) {
				Element entityElem = (Element) iterator.next();
				IRefEntity refEntity = new RefEntity();
				refEntity.fromXML(entityElem);
				addRefEntity(refEntity);
			}
		}
	}

}
