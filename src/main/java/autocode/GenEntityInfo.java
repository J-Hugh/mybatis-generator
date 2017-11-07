/*
 * $Id: GenEntityInfo.java,v 1.2 2013/11/16 04:57:50 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.util.StringUtil;
import dataset.IDatasetConstant;
import dataset.RelationType;
import dataset.model.IAssociationField;
import dataset.model.IDataset;
import dataset.model.IEntity;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IRelation;
import dataset.util.DatasetUtil;

/**
 * 根据dataset或hbm文件生成PO
 * 
 * @author Rone BizFoudation Framework Team: ganjp
 * @version 1.0
 * @since 4.3
 */
public class GenEntityInfo {
	static Logger log = LoggerFactory.getLogger(GenEntityInfo.class);
    
    /**
     * <p>通过dataset文件获得实体类型是entityType的所有实体集合</p>
     * 
     * @param datasetFile dataset文件 如：new File("src/dataset.xml")或new File("d:/dataset.xml"); 
     * @param entityType 实体类型 如：IDatasetConstant.ENTITYTYPE_PENTITY
     * 
     * @return List(IEntity)
     */
    public static List getEntityList(File datasetFile, String entityType) {
        try {
            IDataset dataset = DatasetUtil.buildDatasetFromXML(datasetFile);
            return getEntityList(dataset.getEntities(), entityType);
        } catch (Exception e) {
            log.error("读取dataset文件失败：" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * <p>通过entities集合获得实体类型是entityType的所有实体集合</p>
     * 
     * @param entities IEntity集合
     * @param entityType 实体类型 如：IDatasetConstant.ENTITYTYPE_PENTITY
     * 
     * @return List(IEntity)
     */
    public static List getEntityList(Collection entities, String entityType) {
        if (StringUtil.isBlank(entityType)) {
            throw new RuntimeException("实体类型entityType不能为空");
        }
        if (entities==null || entities.isEmpty()) {
            return null;
        }
        List entityList = new ArrayList();
        for (Iterator iterator = entities.iterator(); iterator.hasNext();) {
            IEntity entity = (IEntity) iterator.next();
            if (entityType.equalsIgnoreCase(entity.getEntityType())) {
                entityList.add(entity);
            }
        }
        return entityList;          
    }
    /**
     * 获得没有关联的实体
     * 
     * @param pEntityList
     * 
     * @return pEntityList(IPEntity)
     */
    public static List setAndGetNoRelatePEntityList(List pEntityList) {
        for (Iterator iterator = pEntityList.iterator(); iterator.hasNext();) {
            IPEntity pEntity = (IPEntity) iterator.next();
            pEntity.getPEntityConditon().clear();
        }
        return pEntityList;
    }
    
    /**
     * <p>通过entities集合获得关联一级的所有实体集合，如：A实体有关联到D，但D不在entities内，那就得把D也包括进来</p>
     * 
     * @param dataset
     * @param pEntitys 持久化实体集合
     * 
     * @return List(IPEntity)
     */
    public static List getIncludeOneRelatePEntityList(IDataset dataset, List selPEntityList) {
        List allPEntityList = getEntityList(dataset.getEntities(), IDatasetConstant.ENTITYTYPE_PENTITY);
        return getIncludeOneRelatePEntityList(allPEntityList, selPEntityList);
    }
    
    /**
     * <p>通过entities集合获得关联一级的所有实体集合，如：A实体有关联到D，但D不在entities内，那就得把D也包括进来</p>
     * 
     * @param dataset
     * @param pEntitys 持久化实体集合
     * 
     * @return List(IPEntity)
     */
    public static List getIncludeOneRelatePEntityList(List allPEntityList, List selPEntityList) {
        List selPEntityIdList = getPEntityIdList(selPEntityList);
        List addPEntityList = new ArrayList();
        for (Iterator iterator = selPEntityList.iterator(); iterator.hasNext();) {
            IPEntity selPEntity = (IPEntity) iterator.next();
            setOneRelatePEntityList(allPEntityList, selPEntityIdList, selPEntity, addPEntityList, selPEntityList);
                
            Collection tgtRelations = selPEntity.getTgtRelations();
            for (Iterator iterator2 = tgtRelations.iterator(); iterator2.hasNext();) {
                IRelation tgtRelation = (IRelation) iterator2.next();
                if (selPEntityIdList.contains(tgtRelation.getSrcEntity().getId())) {
                    selPEntity.getPEntityConditon().addTgtRelationId(tgtRelation.getId());
                }
            }
        }
        if (!addPEntityList.isEmpty()) {
            selPEntityList.addAll(addPEntityList);
        }
        return selPEntityList;
    }
    
    /**
     * <p>通过allPEntityList获得持久化实体pEntity的所有一级关联实体（并且不在selPEntityIdList内），并把所有的一级关联实体放置到addPEntityList
     * 如：A实体有关联到D，但D不在entities内，那就得把D也包括进来</p>
     * 
     * @param allPEntityList 所有持久化实体
     * @param selPEntityIdList 所有被选中的实体ID
     * @param pEntity 持久化实体
     * 
     */
    public static void setOneRelatePEntityList(List allPEntityList, List selPEntityIdList, IPEntity pEntity, List addPEntityList, List selPEntityList) {
        List addPEntityIdList = new ArrayList();
        if (null == addPEntityList) {
            addPEntityList = new ArrayList();
        } else {
            for (Iterator iterator = addPEntityList.iterator(); iterator.hasNext();) {
                IPEntity addPEntity =(IPEntity) iterator.next();
                addPEntityIdList.add(addPEntity.getId());
            }
        }
        List srcRelationIdList = new ArrayList();
        Collection srcRelations = pEntity.getSrcRelations();
        for (Iterator iterator2 = srcRelations.iterator(); iterator2.hasNext();) {
            IRelation srcRelation = (IRelation) iterator2.next();
            srcRelationIdList.add(srcRelation.getId());
            String tgtEntityId = srcRelation.getTgtEntityId();
            if (!selPEntityIdList.contains(tgtEntityId)) {
                IPEntity newPEntity = (IPEntity)getEntity(allPEntityList, tgtEntityId);
                newPEntity.getPEntityConditon().clear();
                //如果是双向1对N，需要对TgtRelation设置值（如果是已经存在addPEntityList，用加入的，否则用清空设置）
                if (RelationType.TWOWAY_ONE2MANY.equals(srcRelation.getRelationType())) {
                    Collection tgtRelations = newPEntity.getTgtRelations();
                    for (Iterator iterator3 = tgtRelations.iterator(); iterator3.hasNext();) {
                        IRelation tgtRelation = (IRelation) iterator3.next();
                        if (pEntity.getId().equalsIgnoreCase(tgtRelation.getSrcEntity().getId())) {
                            if (addPEntityIdList.contains(newPEntity.getId())) {
                                IPEntity addPEntity = (IPEntity)getEntity(addPEntityList, newPEntity.getId());
                                addPEntity.getPEntityConditon().addTgtRelationId(tgtRelation.getId());
                            } else {
                                newPEntity.getPEntityConditon().addTgtRelationId(tgtRelation.getId());
                                addPEntityList.add(newPEntity);
                            }  
                        }
                    }
                }  else {
                    if (!addPEntityIdList.contains(newPEntity.getId())) {
                        if (RelationType.ONEWAY_ONE2ONE.equals(srcRelation.getRelationType())) {
                            Collection relationFields = srcRelation.getRelationFields();
                            String one2oneTgtRelation = pEntity.getName() + ";" ;
                            if (StringUtil.isBlank(srcRelation.getFkName())) {
                                one2oneTgtRelation += " " + ";";
                            } else {
                                one2oneTgtRelation += srcRelation.getFkName() + ";";
                            }
                            if (relationFields.size()==1) {
                                IAssociationField iAssociationField = (IAssociationField)relationFields.iterator().next();
                                one2oneTgtRelation += ((IPField)iAssociationField.getTgtField()).getColumnName();
                            } else {
                                for (Iterator iterator3 = relationFields.iterator(); iterator3.hasNext();) {
                                    IAssociationField iAssociationField = (IAssociationField) iterator3.next();
                                    one2oneTgtRelation += ((IPField)iAssociationField.getTgtField()).getColumnName() + ":" +
                                    ((IPField)iAssociationField.getSrcField()).getColumnName() + ";";
                                }
                            }
                            newPEntity.getPEntityConditon().setOne2oneTgtRelation(one2oneTgtRelation);
                        }
                        addPEntityList.add(newPEntity);
                    }
                }
            } else {
                if (RelationType.ONEWAY_ONE2ONE.equals(srcRelation.getRelationType())) {
                    for (Iterator iterator = selPEntityList.iterator(); iterator.hasNext();) {
                        IPEntity selPEntity = (IPEntity) iterator.next();
                        if (tgtEntityId.equalsIgnoreCase(selPEntity.getId())) {
                            Collection relationFields = srcRelation.getRelationFields();
                            String one2oneTgtRelation = pEntity.getName() + ";";
                            if (StringUtil.isBlank(srcRelation.getFkName())) {
                                one2oneTgtRelation += " " + ";";
                            } else {
                                one2oneTgtRelation += srcRelation.getFkName() + ";";
                            }
                            if (relationFields.size()==1) {
                                IAssociationField iAssociationField = (IAssociationField)relationFields.iterator().next();
                                one2oneTgtRelation += ((IPField)iAssociationField.getTgtField()).getColumnName();
                            } else {
                                for (Iterator iterator3 = relationFields.iterator(); iterator3.hasNext();) {
                                    IAssociationField iAssociationField = (IAssociationField) iterator3.next();
                                    one2oneTgtRelation += ((IPField)iAssociationField.getTgtField()).getColumnName() + ":" +
                                     ((IPField)iAssociationField.getSrcField()).getColumnName()+ ";";
                                }
                            }
                            selPEntity.getPEntityConditon().setOne2oneTgtRelation(one2oneTgtRelation);
                        }
                    }
                }
            }
        }
        List tgtRelationIdList = new ArrayList();
        Collection tgtRelations = pEntity.getTgtRelations();
        for (Iterator iterator2 = tgtRelations.iterator(); iterator2.hasNext();) {
            IRelation tgtRelation = (IRelation) iterator2.next();
            tgtRelationIdList.add(tgtRelation.getId());
            String srcEntityId = tgtRelation.getSrcEntity().getId();
            if (!selPEntityIdList.contains(srcEntityId)) {
                IPEntity newPEntity = (IPEntity)getEntity(allPEntityList, srcEntityId);
                newPEntity.getPEntityConditon().clear();
                Collection newSrcRelations = newPEntity.getSrcRelations();
                for (Iterator iterator3 = newSrcRelations.iterator(); iterator3.hasNext();) {
                    IRelation newSrcRelation = (IRelation) iterator3.next();
                    if (pEntity.getId().equalsIgnoreCase(newSrcRelation.getTgtEntityId())) {
                        if (addPEntityIdList.contains(newPEntity.getId())) {
                            IPEntity addPEntity = (IPEntity)getEntity(addPEntityList, newPEntity.getId());
                            addPEntity.getPEntityConditon().addSrcRelationId(newSrcRelation.getId());
                        } else {
                            newPEntity.getPEntityConditon().addSrcRelationId(newSrcRelation.getId());
                            addPEntityList.add(newPEntity);
                        }  
                    }
                }
            }
        }
        if (!srcRelationIdList.isEmpty()) {
            pEntity.getPEntityConditon().setSrcRelationIds(srcRelationIdList);
        }
        if (!tgtRelationIdList.isEmpty()) {
            pEntity.getPEntityConditon().setTgtRelationIds(tgtRelationIdList);
        }
    }
    /**
     * 获得持久化实体ID集合
     * 
     * @param pEntityList 持久化实体集合
     * 
     * @return List(String)
     */
    public static List getPEntityIdList(List pEntityList) {
        if (pEntityList==null || pEntityList.isEmpty()) {
            return null;
        }
        List pEntityIdList = new ArrayList();
        try {
            for (Iterator iterator = pEntityList.iterator(); iterator.hasNext();) {
                IPEntity pEntity = (IPEntity) iterator.next();
                pEntityIdList.add(pEntity.getId());
            }
        } catch (Exception e) {
            log.error("pEntitys只能是持久化实体，出现转化失败!");
            throw new RuntimeException("pEntitys只能是持久化实体，出现转化失败!");
        }
        return pEntityIdList;
    }
    
    /**
     * 把Collection转化成List
     * @param entitys  IEntity集合
     * 
     * @return List(IEntity)
     */
    public static List convertEntityList(Collection entitys) {
        if (entitys == null || entitys.isEmpty()) {
            return null;
        }
        List entityList = new ArrayList();
        for (Iterator iterator = entitys.iterator(); iterator.hasNext();) {
            IEntity entity = (IEntity) iterator.next();
            entityList.add(entity);
        }
        entitys = null;
        return entityList;
    }
    
    /**
     * 根据实体Id从实体集获取实体对象
     * 
     * @param dataset
     * @param entityId
     * 
     * @return IEntity
     */
    public static IEntity getEntity(List entityList,String entityId) {
        if(entityList == null || entityList.isEmpty() || StringUtil.isBlank(entityId)){
            return null;
        }
        for (Iterator iterator = entityList.iterator(); iterator.hasNext();) {
            IEntity entity = (IEntity) iterator.next();
            if(entity.getId().equals(entityId)){
                return entity;
            }
        }
        return null;
    }
    
    /**
     * 获得实际的源端关联关系集合
     * 
     * @param pEntity
     * 
     * @return List(IRelation)
     */
    public static List getActSrcRelationList(IPEntity pEntity) {
        Collection srcRelations = pEntity.getSrcRelations();
        List srcRelationIds = pEntity.getPEntityConditon().getSrcRelationIds();
        if (srcRelationIds==null || srcRelationIds.isEmpty()) {
            return null;
        }
        List actSrcRelationList = new ArrayList();
        for (Iterator iterator = srcRelations.iterator(); iterator.hasNext();) {
            IRelation relation = (IRelation) iterator.next();
            if (srcRelationIds.contains(relation.getId())) {
                actSrcRelationList.add(relation);
            }
        }
        return actSrcRelationList;
    }
    
    /**
     * 获得实际的目标端关联关系集合
     * 
     * @param pEntity
     * 
     * @return List(IRelation)
     */
    public static List getActTgtRelationList(IPEntity pEntity) {
        Collection srcRelations = pEntity.getTgtRelations();
        List tgtRelationIds = pEntity.getPEntityConditon().getTgtRelationIds();
        if (tgtRelationIds==null || tgtRelationIds.isEmpty()) {
            return null;
        }
        List actTgtRelationList = new ArrayList();
        for (Iterator iterator = srcRelations.iterator(); iterator.hasNext();) {
            IRelation relation = (IRelation) iterator.next();
            if (tgtRelationIds.contains(relation.getId())) {
                actTgtRelationList.add(relation);
            }
        }
        return actTgtRelationList;
    }
    
    
    /**
     * 设置模块名和包名
     * 
     * @param pEntitys 
     * @param moduleName   模块名,如果实体里面的module属性为空时，此值就不能为空,eg:"sale"
     * @param packageName  包名，如果实体里面的packageName属性为空时，此值就不能为空，eg："com.icss.appname.modulename.moduecatalog.model"
     * @return
     */
    public static void setModuleAndPackageName(Collection pEntitys, String moduleName, String packageName) {
        for (Iterator iterator = pEntitys.iterator(); iterator.hasNext();) {
            IPEntity pEntity = (IPEntity) iterator.next();
            if (StringUtil.isBlank(moduleName) && StringUtil.isBlank(pEntity.getModuleName())) {
                throw new RuntimeException(pEntity.getName() + "对应的模块名不能为空");
            }
            if (StringUtil.isBlank(packageName) && StringUtil.isBlank(pEntity.getPackageName())) {
                throw new RuntimeException(pEntity.getName() + "对应的包名不能为空");
            }
            if (StringUtil.isNotBlank(moduleName)) {
                pEntity.setModuleName(moduleName);
            }
            if (StringUtil.isNotBlank(packageName)) {
                pEntity.setPackageName(packageName);
            }
        }
    }
    
    /**
     * 设置包名
     * 
     * @param pEntitys 
     * @param packageName  包名，如果实体里面的packageName属性为空时，此值就不能为空，eg："com.icss.appname.modulename.moduecatalog.model"
     * @return
     */
    public static void setPackageName(Collection pEntitys, String packageName) {
        for (Iterator iterator = pEntitys.iterator(); iterator.hasNext();) {
            IPEntity pEntity = (IPEntity) iterator.next();
            if (StringUtil.isBlank(packageName) && StringUtil.isBlank(pEntity.getPackageName())) {
                throw new RuntimeException(pEntity.getName() + "对应的包名不能为空");
            }
            if (StringUtil.isNotBlank(packageName)) {
                pEntity.setPackageName(packageName);
            }
        }
    }
} 