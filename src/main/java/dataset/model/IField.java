/**
 * $Id: IField.java,v 1.2 2013/11/16 04:57:52 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;


/**
 * @Title: IField.java
 * @Description: <br>实体属性（字段）的模型接口
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:17:25
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface IField extends IDatasetBasic{

    /**
     * 获取字段名称
     * @return
     */
    public String getFieldName();
    /**
     * 设置字段名称
     * @param fieldName
     */
    public void setFieldName(String fieldName);
    /**
     * 获得字段的Java类型
     * @return
     */
    public String getFieldType();
    /**
     * 设置字段的Java类型
     */
    public void setFieldType(String fieldType);
    /**
     * 获得默认值
     * @return
     */
    public String getDefault();
    /**
     * 设置默认值
     * @param value
     */
    public void setDefault(String value);
    /**
     * 获取是否为主键
     * @return
     */
    public boolean isPK();
    /**
     * 设置是否为主键
     * @param pk
     */
    public void setPK(boolean pk);
    /**
     * 获得字段所属的实体
     * @return
     */
    public IEntity getParentEntity();
    /**
     * 设置字段所属的实体
     * @param entity
     */
    public void setParentEntity(IEntity entity);

}
