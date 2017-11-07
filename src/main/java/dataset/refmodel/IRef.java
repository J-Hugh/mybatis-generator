package dataset.refmodel;

import dataset.model.INodeXmlPart;
/**
 * 依赖模型接口
 * @author chenhua
 */
public interface IRef extends INodeXmlPart{
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
}
