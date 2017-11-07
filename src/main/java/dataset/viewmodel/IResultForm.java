package dataset.viewmodel;

/**
 * 展示模型结果信息接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IResultForm extends IForm {
    
    /**
     * 获取行选择方式 checkbox|radio|none
     * @return
     */
    public String getRowSelectType();
    /**
     * 设置行选择方式 checkbox|radio|none
     * @param rowSelectType
     */
    public void setRowSelectType(String rowSelectType);
}
