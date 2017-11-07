/**
 * $Id: DatasetUtil.java,v 1.2 2013/11/16 04:57:53 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import dataset.RelationType;
import dataset.model.IAssociationField;
import dataset.model.IDataset;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IPEntity;
import dataset.model.IRelation;
import dataset.model.impl.AssociationField;
import dataset.model.impl.Dataset;
import dataset.model.impl.Relation;

/**
 * 数据集工具类
 * 
 * @author BizFoundation Team: LiuJun
 * 
 *         {注释}
 * 
 * @version 1.0
 * @since 4.2
 */
public class DatasetUtil {

	/********************************* 数据集相关操作 ******************************************/
	/**
	 * 从数据集文件创建Dataset对象实例
	 * 
	 * @param fileName
	 *            示例1：src/com/chinasofti/ro.bizframework/modules/dataset/util/
	 *            dataset.xml 示例2：d:/dataset.xml
	 * @return IDataset
	 * @throws Exception
	 */
	public static IDataset buildDatasetFromXML(String fileName)
			throws Exception {
		IDataset dataset = new Dataset(fileName);
		dataset.initDataset();
		String file = fileName.replaceAll("\\\\", "/");
		int index = file.lastIndexOf("/");
		dataset.setName(file.substring(index + 1));
		return dataset;
	}

	/**
	 * 从文件创建Dataset对象实例
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static IDataset buildDatasetFromXML(File file) throws Exception {
		IDataset dataset = new Dataset(file.getPath());
		dataset.initDataset(file);
		dataset.setName(file.getName());
		return dataset;
	}
	/**
	 * 将数据集写入文件
	 * 
	 * @param dataset
	 * @param fileName
	 * @throws Exception
	 */
	public static void writeDataSetToXML(IDataset dataset, String fileName)
			throws Exception {
		if (dataset == null || fileName == null) {
			return;
		}
		String encodeing = "UTF-8";
		// 5.0RC
		OutputStreamWriter outWriter = null;
		try {
			Document document = XMLUtil.createDocument();
			// 将数据集写入Document对象
			dataset.toXML(document);
			// 将Document对象写入文件
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, encodeing);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			outWriter = new OutputStreamWriter(new FileOutputStream(fileName),
					encodeing);
			StreamResult result = new StreamResult(outWriter);
			transformer.transform(source, result);
			// log.info("数据集 " + dataset.getName() + " 成功写入文件 " + fileName);
		} catch (Exception e) {
			// log.error("数据集写入文件时出错：?",e);
			throw e;
		} finally {
			// soszou 重构 2013-09-24
			if (outWriter != null)
				outWriter.close();
		}

	}

	/********************************* 数据实体相关操作 ******************************************/
	/**
	 * 根据实体Id从数据集获取实体对象
	 * 
	 * @param dataset
	 * @param entityId
	 * @return
	 */
	public static IEntity getEntity(IDataset dataset, String entityId) {
		if (dataset == null || entityId == null) {
			return null;
		}

		Iterator iterator = dataset.getEntities().iterator();
		while (iterator.hasNext()) {
			IEntity entity = (IEntity) iterator.next();
			if (entity.getId().equals(entityId)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * 根据指定的实体id集合获取实体集合
	 * 
	 * @param dataset
	 * @param ids
	 * @return
	 */
	public static List getEntitys(IDataset dataset, String[] ids) {
		List list = new ArrayList();
		if (dataset == null || ids == null) {
			return list;
		}

		for (int i = 0; i < ids.length; i++) {
			IEntity entity = DatasetUtil.getEntity(dataset, ids[i]);
			if (entity != null) {
				list.add(entity);
			}
		}
		return list;
	}

	/**
	 * 根据指定的实体id集合获取实体集合
	 * 
	 * @param dataset
	 * @param ids
	 * @return
	 */
	public static List getEntitys(IDataset dataset, List ids) {
		List list = new ArrayList();
		if (dataset == null || ids == null) {
			return list;
		}
		Iterator iterator = ids.iterator();
		while (iterator.hasNext()) {
			String id = (String) iterator.next();
			IEntity entity = DatasetUtil.getEntity(dataset, id);
			if (entity != null) {
				list.add(entity);
			}
		}
		return list;
	}

	/**
	 * 根据字段Id从实体获取字段对象
	 * 
	 * @param entity
	 * @param fieldId
	 * @return IField
	 */
	public static IField getField(IEntity entity, String fieldId) {
		if (entity == null || fieldId == null) {
			return null;
		}

		Iterator iterator = entity.getFields().iterator();
		while (iterator.hasNext()) {
			IField field = (IField) iterator.next();
			if (field.getId().equals(fieldId)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 从关联关系列表中查找目标端实体为tgtEntity的关联关系
	 * 
	 * @param tgtEntity
	 *            目标端实体
	 * @param relations
	 *            关联关系列表
	 * @return
	 */
	public static IRelation findRelationbyTgtEntity(IPEntity tgtEntity,
			Collection relations) {
		if (relations == null || tgtEntity == null) {
			return null;
		}

		Iterator iterator = relations.iterator();
		while (iterator.hasNext()) {
			IRelation relation = (IRelation) iterator.next();
			// soszou 重构 2013-09-24
			if (relation.getTgtEntity().getId().equals(tgtEntity.getId())) {
				return relation;
			}
		}
		return null;
	}

	/**
	 * 获取列表中所有实体之间存在的关联关系（说明：每个实体之间只有一种关联关系关系）
	 * 
	 * @param entities
	 *            实体列表
	 * @return
	 */
	public static List<IRelation> getRelations(List entities) {
		List<IRelation> relations = new ArrayList<IRelation>();
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			IPEntity srcEntity = (IPEntity) entities.get(i);
			for (int j = 1; j < size; j++) {
				IPEntity tgtEntity = (IPEntity) entities.get(j);

				IRelation iRelation = findRelationbyTgtEntity(tgtEntity,
						srcEntity.getSrcRelations());
				if (iRelation == null) {
					iRelation = findRelationbyTgtEntity(tgtEntity,
							srcEntity.getTgtRelations());
				}
				if (iRelation != null && !relations.contains(iRelation)) {
					relations.add(iRelation);
				}
			}
		}

		return relations;
	}

	/**
	 * 获取实体所有关联关系： 1）将双向1对N关联关系转换为单向关系 1.1）如果实体为双向1对N关联关系的N端，则转换为N对1关联关系
	 * 1.2）如果实体为双向1对N关联关系的1端，则转换为1对N关联关系 2）实体其它类型关联关系不转换
	 * 
	 * @param pEntity
	 * @return
	 */
	public static List getRelations(IPEntity pEntity) {
		List list = new ArrayList();
		if (pEntity == null) {
			return list;
		}
		Iterator iterator = pEntity.getSrcRelations().iterator();
		while (iterator.hasNext()) {
			IRelation relation = (IRelation) iterator.next();
			list.add(convertRelation(relation, true));// 当前实体为关联关系的源端实体
		}
		iterator = pEntity.getTgtRelations().iterator();
		while (iterator.hasNext()) {
			IRelation relation = (IRelation) iterator.next();
			list.add(convertRelation(relation, false));// 当前实体为关联关系的目标端实体
		}
		return list;
	}

	/**
	 * 转换关联关联 1）将双向1对N关联关系转换为单向关系 1.1）如果实体为双向1对N关联关系的N端，则转换为N对1关联关系
	 * 1.2）如果实体为双向1对N关联关系的1端，则转换为1对N关联关系 2）实体其它类型关联关系不转换
	 * 
	 * @param relation
	 * @param isSrcEntity
	 *            true：实体是关联关系的源端 false：实体为关联关系的目标端
	 * @return
	 */
	private static IRelation convertRelation(IRelation relation,
			boolean isSrcEntity) {
		if (relation == null) {
			return relation;
		}
		if (!relation.getRelationType().equals(RelationType.TWOWAY_ONE2MANY)) {
			return relation;
		}
		IRelation cRelation = new Relation();
		cRelation.setId(relation.getId());
		cRelation.setFkName(relation.getFkName());
		if (isSrcEntity) {// 将双向1对N关联关系转换为单向1对N关联关系
			cRelation.setRelationType(RelationType.ONEWAY_ONE2MANY);
			cRelation.setSrcCascade(relation.getSrcCascade());
			cRelation.setSrcEntity(relation.getSrcEntity());
			cRelation.setSrcLazyLoad(relation.isSrcLazyLoad());
			cRelation.setSrcRefTgtDisplayName(relation
					.getSrcRefTgtDisplayName());
			cRelation.setSrcRefTgtName(relation.getSrcRefTgtName());
			cRelation.setTgtCascade(relation.getTgtCascade());
			cRelation.setTgtEntity(relation.getTgtEntity());
			cRelation.setTgtLazyLoad(relation.isTgtLazyLoad());
			cRelation.setTgtRefSrcDisplayName(relation
					.getTgtRefSrcDisplayName());
			cRelation.setTgtRefSrcName(relation.getTgtRefSrcName());
			cRelation.getRelationFields().addAll(relation.getRelationFields());
		} else {// 将双向1对N关联关系转换为单向N对1关联关系（交换双向关联关系的源端、目标端）
			cRelation.setRelationType(RelationType.ONEWAY_MANY2ONE);
			cRelation.setSrcCascade(relation.getTgtCascade());
			cRelation.setSrcEntity(relation.getTgtEntity());
			cRelation.setSrcLazyLoad(relation.isTgtLazyLoad());
			cRelation.setSrcRefTgtDisplayName(relation
					.getTgtRefSrcDisplayName());
			cRelation.setSrcRefTgtName(relation.getTgtRefSrcName());
			cRelation.setTgtCascade(relation.getSrcCascade());
			cRelation.setTgtEntity(relation.getSrcEntity());
			cRelation.setTgtLazyLoad(relation.isSrcLazyLoad());
			cRelation.setTgtRefSrcDisplayName(relation
					.getSrcRefTgtDisplayName());
			cRelation.setTgtRefSrcName(relation.getSrcRefTgtName());
			Iterator iterator = relation.getRelationFields().iterator();
			while (iterator.hasNext()) {
				IAssociationField field = (IAssociationField) iterator.next();
				cRelation.getRelationFields().add(
						convertAssociationField(field));
			}
		}

		return cRelation;
	}

	/**
	 * 交换关联关系字段的源端与目标端
	 * 
	 * @param field
	 * @return
	 */
	private static IAssociationField convertAssociationField(
			IAssociationField field) {
		if (field == null) {
			return null;
		}
		IAssociationField aField = new AssociationField();
		aField.setSrcField(field.getTgtField());
		aField.setTgtField(field.getSrcField());
		aField.setDescription(field.getDescription());
		return aField;
	}
}
