package dataset.viewmodel;

import dataset.model.INodeXmlPart;

/**
 * 展示模型字段的展示控件属性接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface ITagAttribute extends INodeXmlPart{
    
    /**
     * 获取属性名称
     * @return
     */
    public String getName();
    /**
     * 设置属性名称
     * @param name
     */
    public void setName(String name);
    /**
     * 获取属性类型，包括（property | event | validate | style）
     * @return
     */
    public String getType();
    /**
     * 设置属性类型,包括(property | event | validate | style)
     * @param type
     */
    public void setType(String type);
    /**
     * 获取属性值
     * @return
     */
    public String getAttrValue();
    /**
     * 设置属性值
     * @param attrValue
     */
    public void setAttrValue(String attrValue);
    /**
     * 获取属性默认值
     * @return
     */
    public String getDefaultValue();
    /**
     * 设置属性默认值
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue);
}
