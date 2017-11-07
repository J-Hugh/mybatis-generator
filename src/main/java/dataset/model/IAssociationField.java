package dataset.model;

/**
 * 实体关联关系中字段映射对象接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IAssociationField extends INodeXmlPart {
    /**
     * 获取源实体字段
     * @return
     */
    public IField getSrcField();
    /**
     * 设置源实体字段
     * @param srcField
     */
    public void setSrcField(IField srcField);
    /**
     * 获取目标实体字段
     * @return
     */
    public IField getTgtField();
    /**
     * 设置目标实体字段
     * @param tgtField
     */
    public void setTgtField(IField tgtField);
    /**
     * 获取描述信息
     * @return
     */
    public String getDescription();
    /**
     * 设置描述信息
     * @param description
     */
    public void setDescription(String description);
    /**
     * 获取目标实体字段Id
     * @return
     */
    public String getTgtFieldId();
    /**
     * 设置目标实体字段Id，注意：谨慎使用
     * @return
     */
    public void setTgtFieldId(String tgtFieldId);
}
