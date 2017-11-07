package dataset;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体关联关系类型（枚举定义）
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class RelationType {
    private final String type;//关联关系类型
    private final String description;//关联关系描述
    private static Map instances = new HashMap();//关联关系类型集合，存放所有关联关系实例

    private RelationType(String type, String description) {
        this.type = type;
        this.description = description;
        instances.put(type, this);
    }
    

    /**
     * 1对1关联关系
     */
    public static final RelationType ONEWAY_ONE2ONE = new RelationType(
            "oneway_one2one", "oneway_one2one");

    /**
     * N对1关联关系
     */
    public static final RelationType ONEWAY_MANY2ONE = new RelationType(
            "oneway_many2one", "oneway_many2one");

    /**
     * 1对N关联关系
     */
    public static final RelationType ONEWAY_ONE2MANY = new RelationType(
            "oneway_one2many", "oneway_one2many");

    /**
     * 双向1对N关联关系
     */
    public static final RelationType TWOWAY_ONE2MANY = new RelationType(
            "twoway_one2many", "twoway_one2many");

    /**
     * 获取关联关系类型
     * @param type 关联关系类型名称
     * @return
     */
    public static RelationType getInstance(String type) {
        if (type == null || !instances.containsKey(type))
            return null;
        return (RelationType) instances.get(type);
    }

    /**
     * 获取实体关联关系类型
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 关联关系类型信息描述
     * @return
     */
    public String getDescription() {
        return description;
    }

    public String toString() {
        return description;
    }
}
