package dataset.viewmodel;

/**
 * 展示模型查询结果信息字段
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IListField extends IVMField {
    
    /**
     * 设置字段占据的列宽
     * @return
     */
    public String getWidth();
    /**
     * 获取字段占据的列宽
     * @param width
     */
    public void setWidth(String width);
}
