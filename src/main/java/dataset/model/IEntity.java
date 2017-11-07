/**
 * $Id: IEntity.java,v 1.2 2013/11/16 04:57:51 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

import java.util.Collection;

import autocode.model.AutoCodeUiInfo;
import dataset.viewmodel.IDetailInfoForm;
import dataset.viewmodel.INewForm;
import dataset.viewmodel.IQueryForm;
import dataset.viewmodel.IResultForm;

/**
 * @Title: IEntity.java
 * @Description: <br>数据实体模型接口，也可以指的是非持久化实体
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:16:43
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface IEntity extends IDatasetBasic{
    /**
     * 获得实体package名称
     * @return
     */
    public String getPackageName();
    /**
     * 设置实体package名称
     * @param pkgName
     */
    public void setPackageName(String pkgName);
    /**
     * 获得实体类别， 持久化实体，非持久化实体，查询实体
     * @return
     */
    public String getEntityType();
    /**
     * 获得作者
     * @return
     */
    public String getAuthor();
    /**
     * 设置作者
     * @param author
     */
    public void setAuthor(String author);
    /**
     * 获取是否设置过实体的显示模型
     * @return
     */
    public boolean isSetViewModel();
    /**
     * 设置是否设置过实体的显示模型
     * @param isSetViewModel
     */
    public void setSetViewModel(boolean isSetViewModel);
    /**
     * 获得属性集合
     * @return
     */
    public Collection getFields();
    
    /**
     * 获得主键
     * @return
     */
    public Collection getPKFields();

    /**
     * 获取实体所属数据集
     * @return
     */
    public IDataset getParentDataset();
    /**
     * 设置实体所属数据集
     * @param dataset
     */
    public void setParentDataset(IDataset dataset);
    /**
     * 实体图元的坐标以及长宽
     * @return
     */
    public int[] getLocation();
    
    /**
     * 添加实体属性
     */
    public void addField(IField field);
    /**
     * 删除实体属性
     */
    public void removeField(IField field);
    
    /**
     * 获取查询信息定义
     * @return
     */
    public IQueryForm getQueryForm();
    /**
     * 设置查询信息定义
     * @param form
     */
    public void setQueryForm(IQueryForm form);
    /**
     * 获取查询结果信息定义
     * @return
     */
    public IResultForm getResultForm();
    /**
     * 设置查询结果信息定义
     * @param form
     */
    public void setResultForm(IResultForm form);
    /**
     * 获取详细信息定义
     * @return
     */
    public IDetailInfoForm getDetailInfoForm();
    /**
     * 设置详细信息定义
     * @param form
     */
    public void setDetailInfoForm(IDetailInfoForm form);
    /**
     * 获取录入信息定义
     * @return
     */
    public INewForm getNewForm();
    /**
     * 设置录入信息定义
     * @param form
     */
    public void setNewForm(INewForm form);
    
    /**
     * 获得AutoCodeUiInfo对象
     * 
     * @return
     */
    public AutoCodeUiInfo getAutoCodeUiInfo();

    /**
     * 设置AutoCodeUiInfo对象
     * 
     * @param autoCodeUiInfo
     */
    public void setAutoCodeUiInfo(AutoCodeUiInfo autoCodeUiInfo);
   
}
