package dataset.refmodel;

import java.util.Collection;

public interface IRefEntity extends IRef{
    /**
     * 获得依赖实体属性集合
     * @return
     */
    public Collection getRefFields();
}
