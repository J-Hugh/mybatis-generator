package dataset.viewmodel;

import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.INodeXmlPart;
import dataset.util.StringUtil;

/**
 * 展示模型字段接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IVMField extends INodeXmlPart{
    
    /**
     * 获取展示模型字段Id
     * @return
     */
    public String getFieldId();
    /**
     * 设置展现模型字段Id
     * @param fieldId
     */
    public void setFieldId(String fieldId);
    /**
     * 获取展现模型字段名称
     * @return
     */
    public String getFieldName();
    /**
     * 设置展现模型字段名称
     * @param fieldName
     */
    public void setFieldName(String fieldName);
    /**
     * 获取展现模型字段显示名称
     * @return
     */
    public String getDisplayName();
    /**
     * 设置展示模型字段显示名称
     * @param displayName
     */
    public void setDisplayName(String displayName);
    /**
     * 获取展现模型字段Java类型
     * @return
     */
    public String getFieldType();
    /**
     * 设置展现模型字段Java类型
     * @param fieldType
     */
    public void setFieldType(String fieldType);
    /**
     * 获取展示模型字段排列顺序
     * @return
     */
    public int getIndex();
    /**
     * 设置展示模型字段排列顺序
     * @param index
     */
    public void setIndex(int index);
    /**
     * 获取展示模型字段控件
     * @return
     */
    public ITag getTag();
    /**
     * 设置展示模型字段控件
     * @param tag
     */
    public void setTag(ITag tag);
    /**
     * 获取展示模型字段所属的展示Form
     * @return
     */
    public IForm getParentForm();
    /**
     * 设置展示模型字段所属的展示Form
     * @param parentForm
     */
    public void setParentForm(IForm parentForm);
	/**
	 * 获取字段引用的持久化实体ID（适用于查询实体字段通过持久化实体定义的场景）
	 * @return
	 */
	public String getRefEntityId();
	/**
	 * 设置字段引用的持久化实体ID（适用于查询实体字段通过持久化实体定义的场景）
	 * @param refEntityId
	 */
	public void setRefEntityId(String refEntityId);
	/**
	 * 获取字段引用的持久化实体字段的ID（适用于查询实体字段通过持久化实体定义的场景）
	 * @return
	 */
	public String getRefFieldId();
	/**
	 * 设置字段引用的持久化实体字段的ID（适用于查询实体字段通过持久化实体定义的场景）
	 * @param refFieldId
	 */
	public void setRefFieldId(String refFieldId);
	/**
	 * 获取引用的持久化实体
	 * @return
	 */
	public IEntity getRefEntity();
	/**
	 * 获取引用的持久化实体字段
	 * @return
	 */
	public IField getRefField();
	/**
	 * 获取数据集文件的路径
	 */
	public String getRefDataset();

	/**
	 * 设置数据集文件的路径
	 */
	public void setRefDataset(String refDataset);
}
