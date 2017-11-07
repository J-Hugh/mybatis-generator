package dataset.viewmodel;

import java.util.Collection;

import dataset.model.INodeXmlPart;

/**
 * 展示模型字段展示控件接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface ITag extends INodeXmlPart{
    
    /**
     * 获取控件类型（全局唯一）
     * @return
     */
    public String getTagType();
    /**
     * 设置控件类型（全局唯一）
     * @param controlType
     */
    public void setTagType(String controlType);
    /**
     * 获取控件属性列表（包含attitude、event，validate，style）
     * @return
     */
    public Collection getAttributes();
    /**
     * 设置控件属性列表（包含attitude、event，validate，style）
     * @param attributes
     */
    public void setAttributes(Collection attributes);
}
