package dataset.viewmodel;

/**
 * 展示模型详细信息接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IDetailInfoForm extends IForm {
	/**
	 * 获取页面内容展示方式：
	 * 	common：普通方式
	 * 	group：分组方式
	 * 	tab：tab标签页方式
	 * @return
	 */
	public String getShowType();
	/**
	 * 设置页面内容展示方式：
	 * 	common：普通方式
	 * 	group：分组方式
	 * 	tab：tab标签页方式
	 * @param showType
	 */
	public void setShowType(String showType);
}
