package dataset.viewmodel;

/**
 * 展示模型录入信息字段接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IAddField extends IVMField {
    /**
     * 获取字段是否显示
     * @return
     */
    public boolean isDisplay();
    /**
     * 设置字段是否显示
     * @param isDisplay
     */
    public void setDisplay(boolean isDisplay);
}
