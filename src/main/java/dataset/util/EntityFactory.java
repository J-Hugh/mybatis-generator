package dataset.util;

import dataset.IDatasetConstant;
import dataset.model.IEntity;
import dataset.model.impl.Entity;
import dataset.model.impl.PEntity;
import dataset.model.impl.VEntity;

/**
 * 实体工厂类
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class EntityFactory {
    /**
     * 根据实体类型创建实体
     * @param entityType 实体类型
     * @return IEntity
     */
    public static IEntity createEntity(String entityType) {
        if(entityType == null){
            return null;
        }else if(IDatasetConstant.ENTITYTYPE_PENTITY.equals(entityType)){
            return new PEntity();
        }else if(IDatasetConstant.ENTITYTYPE_VENTITY.equals(entityType)){
            return new VEntity();
        }else if(IDatasetConstant.ENTITYTYPE_NENTITY.equals(entityType)){
            return new Entity();
        }
        
        return null;
    }
}
