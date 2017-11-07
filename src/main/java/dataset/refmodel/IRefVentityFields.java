package dataset.refmodel;

import java.util.Collection;

public interface IRefVentityFields extends IRef {
    /**
     * 获取依赖数据集
     * @return
     */
    public Collection getRefDatasets();
}
