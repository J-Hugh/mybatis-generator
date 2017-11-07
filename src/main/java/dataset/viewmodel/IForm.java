package dataset.viewmodel;

import java.util.Collection;

import dataset.model.IEntity;
import dataset.model.INodeXmlPart;

/**
 * 展示模型接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IForm extends INodeXmlPart{
    /**
     * 获取Id
     * @return
     */
    public String getId();
    /**
     * 设置Id
     * @param id
     */
    public void setId(String id);
    /**
     * 获取标题
     * @return
     */
    public String getTitle();
    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title);
    /**
     * 获取字段列表
     * @return
     */
    public Collection getFields();
    /**
     * 设置字段列表
     * @param fields
     */
    public void setFields(Collection fields);
    /**
     * 获取展现模型所属的实体
     * @return
     */
	public IEntity getParentEntity();
	/**
	 * 设置展现模型所属的实体
	 * @param parentEntity
	 */
	public void setParentEntity(IEntity parentEntity);
}
