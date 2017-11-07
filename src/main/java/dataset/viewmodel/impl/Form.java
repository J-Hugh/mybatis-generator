package dataset.viewmodel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.model.IEntity;
import dataset.util.XMLUtil;
import dataset.viewmodel.IForm;
import dataset.viewmodel.IVMField;

/**
 * 展现模型信息定义
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public abstract class Form implements IForm {

    protected String id;
    protected String title;
    protected Collection fields;
    protected IEntity parentEntity = null;

    protected void loadForm(Element element) {
        this.setId(XMLUtil.getAttribute(element, IDatasetConstant.ID));
        this.setTitle(XMLUtil.getAttribute(element, IDatasetConstant.TITLE));
    }
    
    /**
     * 构建展示信息基本XML信息
     * @param element
     */
    protected void buildElement(Element element) {
        if(element == null){
            return;
        }
        
        XMLUtil.addAttribute(element, IDatasetConstant.ID, this.getId());
        XMLUtil.addAttribute(element, IDatasetConstant.TITLE, this.getTitle());
        
        Iterator iterator = this.getFields().iterator();
        while (iterator.hasNext()) {
            IVMField field = (IVMField) iterator.next();
            XMLUtil.addChildElement(element, field.toXML(element.getOwnerDocument()));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection getFields() {
        if(fields == null){
            fields = new ArrayList();
        }
        return fields;
    }

    public void setFields(Collection fields) {
        this.fields = fields;
    }

	public IEntity getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(IEntity parentEntity) {
		this.parentEntity = parentEntity;
	}

}
