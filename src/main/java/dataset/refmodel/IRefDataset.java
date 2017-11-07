package dataset.refmodel;

import java.util.Collection;

import dataset.model.IEntity;
import dataset.model.IPField;

public interface IRefDataset extends IRef {
    /**
     * 获得实体集合
     * @return
     */
    public Collection getRefEntitys();
    /**
     * 添加依赖实体
     * @param refEntity
     */
    public void addRefEntity(IRefEntity refEntity);
    /**
     * 删除依赖实体
     * @param refEntity
     */
    public void removeRefEntity(IRefEntity refEntity);
    /**
     * 获取父实体
     * @return IEntity
     */
    public IEntity getParentEntity();
    /**
     * 设置父实体
     * @param parentEntity
     */
    public void setParentEntity(IEntity parentEntity);
    /**
     * 获取父持久化实体属性
     * @return IPField
     */
    public IPField getParentPField();
    /**
     * 设置父持久化实体属性
     * @param parentPField
     */
    public void setParentPField(IPField parentPField);
    /**
     * 获取工程路径
     * @return
     */
    public String getProjectPath() ;
    /**
     * 设置工程路径
     * @param projectPath
     */
	public void setProjectPath(String projectPath);
}