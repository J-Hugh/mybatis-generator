/*
 * $Id: GenHbmForDataAndScript.java,v 1.1 2013/09/04 06:35:23 chenhua Exp $
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
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.util.FileUtil;
import autocode.util.HbmUtil;
import autocode.util.StringUtil;
import autocode.util.XmlUtil;
import dataset.IDatasetConstant;
import dataset.RelationType;
import dataset.model.IAssociationField;
import dataset.model.IDataset;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IRelation;
import dataset.model.IVEntity;
import dataset.model.IVField;
import dataset.util.DatasetUtil;

/**
 * 根据DataSet文件生成hbm文件
 * 生成数据库表或数据库脚本
 * @author Rone BizFoudation Framework Team: chenhua
 * @version 1.0
 * @since 4.3
 */
public class GenHbmForDataAndScript {
	static Logger log = LoggerFactory.getLogger(GenHbmForDataAndScript.class);
	// 生成模型hbm文件的根节点
	private static String elementRoot = "model-mapping";
	// 生成模型hbm文件的根节点,生成数据集脚本时使用的临时hbm.xml文件
	private static String elementRootForGenScript = "hibernate-mapping";

	/**
	 * 通过持久化实体集合生成hbm文件
	 * 
	 * @param pEntitys   持久化实体集
	 * @param packageName hbm所在的包
	 * @param outBaseDirPath  基本输出目录路径
	 * @return
	 */
	public static List genHbmFile(Collection pEntitys, String packageName,
			String dbHibernateDialect,String outBaseDirPath) {
		if (pEntitys == null || pEntitys.isEmpty()) {
			log.error("持久化实体集合不能为空");
			return null;
		}
		String filePath = FileUtil.getDirPath(packageName, outBaseDirPath);
		List hbmFileList = new ArrayList();
		// Document hbmDocument = HbmUtil.createHbmDocument();
		Document hbmDocument = HbmUtil.createHbmDocumentForGenScript();

		for (Iterator iterator = pEntitys.iterator(); iterator.hasNext();) {
			IPEntity iPEntity = (IPEntity) iterator.next();

			Element classElement = genClassElement(
					hbmDocument.addElement(elementRootForGenScript), iPEntity,
					packageName);
			// 生成数据库脚本时不生成SQL定义
			// setSqlElement("list","default",iPEntity.getQuerySql(),classElement);
			genIdAndPropertyElement(classElement, iPEntity, dbHibernateDialect,packageName);
			genRelationsElement(classElement, iPEntity, packageName);

			File hbmFile = FileUtil.createFile(filePath + "/"
					+ StringUtil.upperFirst(iPEntity.getName()) + ".hbm.xml");
			XmlUtil.saveToFile(hbmDocument, hbmFile);
			hbmFileList.add(hbmFile);

			log.info("gen " + iPEntity.getName() + " hbm file success");
			hbmDocument.clearContent();
		}
		return hbmFileList;
	}



	/**
	 * <p>
	 * 在element下面增加一个class的Element
	 * </p>
	 * 
	 * <pre>
	 *     <class name="com.csi.ro.bizapp.tp.model.TpRoastHouse" table="TP_ROAST_HOUSE">
	 * </pre>
	 * 
	 * @param element
	 * @param iPEntity
	 * @param packageName
	 */
	public static Element genClassElement(Element element, IPEntity iPEntity,
			String packageName) {
		Element classElement = element.addElement("class");
		String className = StringUtil.upperFirst(iPEntity.getName());
		// 如果packageName不为空并且className没有带包名自动把packageName加到className上
		if (StringUtil.isNotBlank(packageName) && className.indexOf(".") == -1) {
			className = packageName + "." + className;
		}
		classElement.addAttribute("name", className);
		classElement.addAttribute("table", iPEntity.getTable());

		// .by liutsh.2011.11.9.将显示名做为COMMENT.
		classElement.addElement("comment").addText(iPEntity.getDisplayName());
		return classElement;
	}

	/**
	 * <p>
	 * 在element下面增加主键和所有的字段的Element
	 * </p>
	 * 
	 * <pre>
	 *    <id name="roastHouseId" type="java.lang.String" column="ROAST_HOUSE_ID" >
	 *      <generator class="uuid"/>
	 *    </id>
	 *    <composite-id name="userPk" class="com.csi.ro.bizapp.tp.model.UserPk">
	 *      <key-property name="userId" type="java.lang.String" column="USER_ID" />
	 *      <key-property name="userName" type="java.lang.String" column="USER_NAME"/>
	 *    </composite-id>
	 *    <composite-id class="UserLoginPk" mapped="false" name="userLoginPk" unsaved-value="undefined">
	 *      <key-many-to-one name="userLoginId" class="User" foreign-key="USERLOGIN_FK">
	 *         <column name="user_login_id1">
	 *             <comment>user_id1</comment>
	 *         </column>
	 *         <column name="user_login_id2">
	 *             <comment>user_id2</comment>
	 *         </column>    
	 *      </key-many-to-one>
	 *    </composite-id>
	 *    <property name="roastHouseCd" type="java.lang.String" column="ROAST_HOUSE_CD"/>
	 *    <id name="userLoginId" type="java.lang.String"  column="USER_LOGIN_ID" length="32">
	 *      <generator class="foreign">
	 *          <param name="property">user</param>
	 *      </generator>
	 *    </id>
	 * </pre>
	 * 
	 * @param element
	 * @param pEntity
	 * @param dbHibernateDialect
	 * @param packageName
	 */
	public static void genIdAndPropertyElement(Element element,
			IPEntity pEntity,String dbHibernateDialect, String packageName) {
		Collection pks = pEntity.getPKFields();
		if (pks == null || pks.isEmpty()) {
			throw new RuntimeException(element.attributeValue("name")
					+ "主键不能为空");
		} else {
			List srcRelations = GenEntityInfo.getActSrcRelationList(pEntity);
			boolean isOneToOne = false;
			String tgtName = "";
			if (srcRelations != null && !srcRelations.isEmpty()) {
				for (Iterator iterator = srcRelations.iterator(); iterator
						.hasNext();) {
					IRelation iRelation = (IRelation) iterator.next();
					if (RelationType.ONEWAY_ONE2ONE.equals(iRelation
							.getRelationType())) {
						isOneToOne = true;
						tgtName = iRelation.getSrcRefTgtName();
					}
				}
			}
			if (pks.size() == 1) {
				Element idElement = element.addElement("id");
				IPField pField = (IPField) pks.iterator().next();
				idElement.addAttribute("name", pField.getName());
				idElement.addAttribute("type", pField.getFieldType());
				setColumnProperty(pField, idElement,dbHibernateDialect);
				Element generatorElement = idElement.addElement("generator");

				String one2oneTgtRelation = pEntity.getPEntityConditon()
						.getOne2oneTgtRelation();
				if (StringUtil.isNotBlank(one2oneTgtRelation)) {
					generatorElement.addAttribute("class", "foreign");
					Element paramElement = generatorElement.addElement("param");
					paramElement.addAttribute("name", "property");
					String[] strArr = one2oneTgtRelation.split(";");
					paramElement.addText(strArr[0]);
				} else {
					if (IDatasetConstant.GEN_STRATEGY_CUSTOM
							.equalsIgnoreCase(pEntity.getGenStrategy())) {
						generatorElement.addAttribute("class", "assigned");
					} else if (IDatasetConstant.GEN_STRATEGY_UUID64
							.equalsIgnoreCase(pEntity.getGenStrategy())
							&& StringUtil.isBlank(packageName)) {
						generatorElement.addAttribute("class", "uuid");
					} else {
						generatorElement.addAttribute("class",
								pEntity.getGenStrategy());
					}
				}
			} else {
				Element compositeElement = element.addElement("composite-id");
				String pkClassName = getCompositPkName(pEntity.getName());
				compositeElement.addAttribute("name",
						StringUtil.lowerFirst(pkClassName));
				if (StringUtil.isNotBlank(packageName))
					pkClassName = packageName + "." + pkClassName;
				compositeElement.addAttribute("class", pkClassName);
				for (Iterator iterator = pks.iterator(); iterator.hasNext();) {
					IPField pField = (IPField) iterator.next();
					Element keyPropertyElement = compositeElement
							.addElement("key-property");
					keyPropertyElement.addAttribute("name",
							pField.getFieldName());
					keyPropertyElement.addAttribute("type",
							pField.getFieldType());
					setColumnProperty(pField, keyPropertyElement,dbHibernateDialect);
				}
			}
		}
		Collection fields = pEntity.getFields();
		for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
			IPField pField = (IPField) iterator.next();
			if (pField.isPK() || !pField.isPersistent()) {
				continue;
			}
			Element propertyElement = element.addElement("property");
			propertyElement.addAttribute("name", pField.getFieldName());
			if (StringUtil.isBlank(packageName)) {
				if ("java.io.InputStream".equalsIgnoreCase(pField
						.getFieldType())) {
					propertyElement.addAttribute("type", "java.sql.Blob");
				} else if ("java.io.Reader".equalsIgnoreCase(pField
						.getFieldType())) {
					propertyElement.addAttribute("type", "java.sql.Clob");
				} else {
					propertyElement.addAttribute("type", pField.getFieldType());
				}
			} else {
				propertyElement.addAttribute("type", pField.getFieldType());
			}

			setColumnProperty(pField, propertyElement,dbHibernateDialect);
			// if (pField.isNullAble())
			// {//not-null属性应该定义在column节点上，参考:setColumnProperty方法
			// propertyElement.addAttribute("not-null", "true");
			// }
		}
	}

	/**
	 * 获得复合主键的主键类名
	 * 
	 * @param className
	 * @return
	 */
	public static String getCompositPkName(String className) {
		return StringUtil.upperFirst(className) + "Pk";
	}

	/**
	 * 对parentElement进行设置列属性值
	 * 
	 * @param pField
	 * @param parentElement
	 */
	public static void setColumnProperty(IPField pField, Element parentElement,String dbHibernateDialect) {
		if(StringUtil.isBlank(dbHibernateDialect)){
			return;
		}
		if("org.hibernate.dialect.MySQLDialect".equals(dbHibernateDialect)){
			setColumnPropertyForMysql(pField, parentElement);
		}else{
			setColumnProperty(pField, parentElement);
		} 
			
	}
	/**
	 * 对parentElement进行设置列属性值
	 * 
	 * @param pField
	 * @param parentElement
	 */
	public static void setColumnProperty(IPField pField, Element parentElement) {
		Element columnElement = parentElement.addElement("column");
		columnElement.addAttribute("name", pField.getColumnName());

		if (pField.isNullAble()) {
			columnElement.addAttribute("not-null", "true");
		}

		// .by liutsh.2011.11.9.将显示名做为COMMENT.
		columnElement.addElement("comment").addText(pField.getDisplayName());

		if ("java.lang.String".equalsIgnoreCase(pField.getFieldType())) {
			if ("char".equalsIgnoreCase(pField.getColumnType())) {
				// <column name="DEMO_ID" sql-type="char(32)" not-null="true"/>
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("sql-type",
							"char(" + pField.getLength() + ")");
				} else {
					columnElement.addAttribute("sql-type", "char(" + 255 + ")");
				}
				if (StringUtil.isNotBlank(pField.getDefault())
						&& !pField.isPK()) {
					columnElement.addAttribute("default",
							"'" + pField.getDefault() + "'");
				}
			} else {
				if (StringUtil.isNotBlank(pField.getDefault())
						&& !pField.isPK()) {
					columnElement.addAttribute("default",
							"'" + pField.getDefault() + "'");
					if (StringUtil.isNotBlank(pField.getLength())) {
						columnElement
								.addAttribute("length", pField.getLength());
					}
				} else {
					if (StringUtil.isNotBlank(pField.getLength())) {
						columnElement
								.addAttribute("length", pField.getLength());
					}
				}
			}
		} else if ("java.lang.Float|java.lang.Double|java.math.BigDecimal"
				.indexOf(pField.getFieldType()) != -1) {
			if (pField.isPK()) {
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("precision", pField.getLength());
				}
				if (StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("scale", pField.getPrecision());
				}
			} else if (StringUtil.isNotBlank(pField.getDefault())) {
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("precision", pField.getLength());
				}
				if (StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("scale", pField.getPrecision());
				}
				columnElement.addAttribute("default", pField.getDefault());
			} else { // <property name="balance" type="big_decimal"
						// column="BALANCE" precision="12" scale="2"/>
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("precision", pField.getLength());
				}
				if (StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("scale", pField.getPrecision());
				}
			}
		} else {
			if (StringUtil.isNotBlank(pField.getDefault()) && !pField.isPK()) {
				if ("java.sql.Date|java.sql.Time|java.sql.Timestamp|java.io.Reader"
						.indexOf(pField.getFieldType()) != -1) {
					columnElement.addAttribute("default",
							"'" + pField.getDefault() + "'");
				} else {
					columnElement.addAttribute("default", pField.getDefault());
				}
			}

			// .add by liutsh.2011.11.4
			// 修改Bug:BIZFOUNDATION-739,处理java.io.Reader对应数据库的Clob在数据建模工具生成DB2数据库脚本时长度无效的问题
			if ("java.io.reader".equalsIgnoreCase(pField.getFieldType())) {
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("length", pField.getLength());
				}
			}
		}
	}
	/**
	 * mysql数据库生成的Column节点
	 * @param pField
	 * @param parentElement
	 */
	public static void setColumnPropertyForMysql(IPField pField, Element parentElement){
		Element columnElement = parentElement.addElement("column");
		columnElement.addAttribute("name", pField.getColumnName());

		if (pField.isNullAble()) {
			columnElement.addAttribute("not-null", "true");
		}

		// .by liutsh.2011.11.9.将显示名做为COMMENT.
		columnElement.addElement("comment").addText(pField.getDisplayName());

		if ("java.lang.String".equalsIgnoreCase(pField.getFieldType())) {
			if ("char".equalsIgnoreCase(pField.getColumnType())) {
				// <column name="DEMO_ID" sql-type="char(32)" not-null="true"/>
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("sql-type",
							"char(" + pField.getLength() + ")");
				} else {
					columnElement.addAttribute("sql-type", "char(" + 255 + ")");
				}
				if (StringUtil.isNotBlank(pField.getDefault())
						&& !pField.isPK()) {
					columnElement.addAttribute("default",
							"'" + pField.getDefault() + "'");
				}
			} else {
				if (StringUtil.isNotBlank(pField.getDefault())&& !pField.isPK()) {
					columnElement.addAttribute("default","'" + pField.getDefault() + "'");
					if (StringUtil.isNotBlank(pField.getLength())) {
						columnElement.addAttribute("length", pField.getLength());
					}
				} else {
					if (StringUtil.isNotBlank(pField.getLength())) {
						columnElement.addAttribute("length", pField.getLength());
					}
				}
			}
		} else if ("java.lang.Float|java.lang.Double|java.math.BigDecimal"
				.indexOf(pField.getFieldType()) != -1) {
			if (pField.isPK()) {
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("precision", pField.getLength());
				}
				if (StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("scale", pField.getPrecision());
				}
				if (StringUtil.isNotBlank(pField.getColumnType())&&StringUtil.isNotBlank(pField.getLength())&&StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("sql-type", pField.getColumnType()+"(" + pField.getLength() + ")");
				}
			} else if (StringUtil.isNotBlank(pField.getDefault())) {
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("precision", pField.getLength());
				}
				if (StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("scale", pField.getPrecision());
				}
				if (StringUtil.isNotBlank(pField.getColumnType())&&StringUtil.isNotBlank(pField.getLength())&&StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("sql-type", pField.getColumnType()+"(" + pField.getLength() + ")");
				}
				if (StringUtil.isNotBlank(pField.getColumnType())&&StringUtil.isNotBlank(pField.getLength())&&StringUtil.isBlank(pField.getPrecision())) {
					columnElement.addAttribute("sql-type", pField.getColumnType()+"(" + pField.getLength() + ","+0+")");
				}
				columnElement.addAttribute("default", pField.getDefault());
			} else {
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("precision", pField.getLength());
				}
				if (StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("scale", pField.getPrecision());
				}
				if (StringUtil.isNotBlank(pField.getColumnType())&&StringUtil.isNotBlank(pField.getLength())&&StringUtil.isNotBlank(pField.getPrecision())) {
					columnElement.addAttribute("sql-type", pField.getColumnType()+"(" + pField.getLength() + ","+pField.getPrecision()+")");
				}
				if (StringUtil.isNotBlank(pField.getColumnType())&&StringUtil.isNotBlank(pField.getLength())&&StringUtil.isBlank(pField.getPrecision())) {
					columnElement.addAttribute("sql-type", pField.getColumnType()+"(" + pField.getLength() + ","+0+")");
				}
			}
		}else if("java.lang.Integer".indexOf(pField.getFieldType()) != -1){
			if (StringUtil.isNotBlank(pField.getColumnType())&&StringUtil.isNotBlank(pField.getLength())&&StringUtil.isNotBlank(pField.getPrecision())) {
				columnElement.addAttribute("sql-type", pField.getColumnType()+"(" + pField.getLength() + ")");
			}
			if (StringUtil.isNotBlank(pField.getColumnType())&&StringUtil.isNotBlank(pField.getLength())&&StringUtil.isBlank(pField.getPrecision())) {
				columnElement.addAttribute("sql-type", pField.getColumnType()+"(" + pField.getLength() + ")");
			}
		} else {
			if (StringUtil.isNotBlank(pField.getDefault()) && !pField.isPK()) {
				if ("java.sql.Date|java.sql.Time|java.sql.Timestamp|java.io.Reader".indexOf(pField.getFieldType()) != -1) {
					columnElement.addAttribute("default","'" + pField.getDefault() + "'");
				} else {
					columnElement.addAttribute("default", pField.getDefault());
				}
			}

			// .add by liutsh.2011.11.4
			// 修改Bug:BIZFOUNDATION-739,处理java.io.Reader对应数据库的Clob在数据建模工具生成DB2数据库脚本时长度无效的问题
			if ("java.io.reader".equalsIgnoreCase(pField.getFieldType())) {
				if (StringUtil.isNotBlank(pField.getLength())) {
					columnElement.addAttribute("length", pField.getLength());
				}
			}
		}
	}
	/**
	 * <p>
	 * 在element下面所有的关联关系Element
	 * </p>
	 * 
	 * <pre>
	 *    <one-to-one name="user" class="User" constrained="true" foreign-key="USERLOGIN_USER_FK"/>
	 *    <many-to-one name="parent" class="Parent" column="parent_id" foreign-key="CHILD_PARENT_FK"/>
	 *    <many-to-one name="parent" class="Parent" not-null="true" foreign-key="CHILD_PARENT_FK">
	 *      <column name="parent_pk1" />
	 *      <column name="parent_pk2" />
	 *    </many-to-one>
	 *    <set name="children" inverse="true">
	 *      <key column="parent_id"/>
	 *      <key>
	 *          <column name="parent_id1"/>
	 *          <column name="parent_id2"/>
	 *      </key>
	 *      <one-to-many class="Child"/>
	 *    </set>
	 * </pre>
	 * 
	 * @param element
	 * @param pEntity
	 * @param packageName
	 */
	public static void genRelationsElement(Element element, IPEntity pEntity,
			String packageName) {
		String one2oneTgtRelation = pEntity.getPEntityConditon()
				.getOne2oneTgtRelation();
		if (StringUtil.isNotBlank(one2oneTgtRelation)) {
			String[] strArr = one2oneTgtRelation.split(";");
			String relateEntityName = strArr[0];
			String foreignName = strArr[1];
			String className = StringUtil.upperFirst(relateEntityName);
			if (StringUtil.isNotBlank(packageName)) {
				className = packageName + "." + className;
			}
			if (strArr.length == 3) {
				Element one2oneElement = element.addElement("one-to-one");
				one2oneElement.addAttribute("name",
						StringUtil.lowerFirst(relateEntityName));
				one2oneElement.addAttribute("class", className);
				if (StringUtil.isNotBlank(foreignName)) {
					one2oneElement.addAttribute("foreign-key", foreignName);
				}
				one2oneElement.addAttribute("constrained", "true");
			} else {
				Element many2oneElement = element.addElement("many-to-one");
				many2oneElement.addAttribute("name",
						StringUtil.lowerFirst(relateEntityName));
				many2oneElement.addAttribute("class", className);
				for (int i = 2; i < strArr.length; i++) {
					Element columnElement = many2oneElement
							.addElement("column");
					columnElement.addAttribute("name",
							(strArr[i].split(":"))[0]);
					columnElement.addElement("comment").addText(
							(strArr[i].split(":"))[1]);
				}
				if (StringUtil.isNotBlank(foreignName)) {
					many2oneElement.addAttribute("foreign-key", strArr[1]);
				}
			}
		}
		List srcRelations = GenEntityInfo.getActSrcRelationList(pEntity);
		if (srcRelations != null && !srcRelations.isEmpty()) {
			for (Iterator iterator = srcRelations.iterator(); iterator
					.hasNext();) {
				IRelation srcRelation = (IRelation) iterator.next();
				if (RelationType.ONEWAY_ONE2ONE.equals(srcRelation
						.getRelationType())) {
					String className = StringUtil.upperFirst(srcRelation
							.getTgtEntity().getName());
					if (StringUtil.isNotBlank(packageName)) {
						className = packageName + "." + className;
					}
					if (pEntity.getPKFields() != null
							&& pEntity.getPKFields().size() > 1
							&& StringUtil.isNotBlank(packageName)) {
						Element many2oneElement = element
								.addElement("many-to-one");
						many2oneElement.addAttribute("name",
								srcRelation.getSrcRefTgtName());
						many2oneElement.addAttribute("class", className);
						if (StringUtil.isNotBlank(srcRelation.getFkName())) {
							many2oneElement.addAttribute("foreign-key",
									srcRelation.getFkName());
						}
						Collection relationFields = srcRelation
								.getRelationFields();
						for (Iterator iterator2 = relationFields.iterator(); iterator2
								.hasNext();) {
							IAssociationField iAssociationField = (IAssociationField) iterator2
									.next();
							Element columnElement = many2oneElement
									.addElement("column");
							columnElement.addAttribute("name",
									((IPField) iAssociationField.getSrcField())
											.getColumnName());
							columnElement.addElement("comment").addText(
									((IPField) iAssociationField.getTgtField())
											.getColumnName());
						}
					} else {
						Element one2oneElement = element
								.addElement("one-to-one");
						one2oneElement.addAttribute("name",
								srcRelation.getSrcRefTgtName());
						one2oneElement.addAttribute("class", className);
						if (StringUtil.isNotBlank(srcRelation.getFkName())) {
							one2oneElement.addAttribute("foreign-key",
									srcRelation.getFkName());
						}
						// if (srcRelation.getRelationFields()!=null &&
						// srcRelation.getRelationFields().size()==1) {
						// one2oneElement.addAttribute("constrained", "true");
						// }
					}
				} else if (RelationType.ONEWAY_ONE2MANY.equals(srcRelation
						.getRelationType())) {
					Element setElement = element.addElement("set");
					setElement.addAttribute("name",
							srcRelation.getSrcRefTgtName());
					Element keyElement = setElement.addElement("key");
					if (StringUtil.isNotBlank(srcRelation.getFkName())) {
						keyElement.addAttribute("foreign-key",
								srcRelation.getFkName());
					}
					Collection relationFields = srcRelation.getRelationFields();
					if (relationFields.size() == 1) {
						IAssociationField iAssociationField = (IAssociationField) relationFields
								.iterator().next();
						keyElement.addAttribute("column",
								((IPField) iAssociationField.getTgtField())
										.getColumnName());
						keyElement.addAttribute("not-null", "true");
					} else {
						for (Iterator iterator2 = relationFields.iterator(); iterator2
								.hasNext();) {
							IAssociationField iAssociationField = (IAssociationField) iterator2
									.next();
							Element columnElement = keyElement
									.addElement("column");
							columnElement.addAttribute("name",
									((IPField) iAssociationField.getTgtField())
											.getColumnName());
						}
					}
					Element oneManyElement = setElement
							.addElement("one-to-many");
					String className = StringUtil.upperFirst(srcRelation
							.getTgtEntity().getName());
					if (StringUtil.isNotBlank(packageName)) {
						className = packageName + "." + className;
					}
					oneManyElement.addAttribute("class", className);
				} else if (RelationType.ONEWAY_MANY2ONE.equals(srcRelation
						.getRelationType())) {
					Element many2oneElement = element.addElement("many-to-one");
					many2oneElement.addAttribute("name", StringUtil
							.lowerFirst(srcRelation.getSrcRefTgtName()));
					Collection relationFields = srcRelation.getRelationFields();
					String className = StringUtil.upperFirst(srcRelation
							.getTgtEntity().getName());
					if (StringUtil.isNotBlank(packageName)) {
						className = packageName + "." + className;
					}
					many2oneElement.addAttribute("class", className);
					if (relationFields.size() == 1) {
						IAssociationField iAssociationField = (IAssociationField) relationFields
								.iterator().next();
						many2oneElement.addAttribute("column",
								((IPField) iAssociationField.getSrcField())
										.getColumnName());
					} else {
						for (Iterator iterator2 = relationFields.iterator(); iterator2
								.hasNext();) {
							IAssociationField iAssociationField = (IAssociationField) iterator2
									.next();
							Element columnElement = many2oneElement
									.addElement("column");
							columnElement.addAttribute("name",
									((IPField) iAssociationField.getSrcField())
											.getColumnName());
							columnElement.addElement("comment").addText(
									((IPField) iAssociationField.getSrcField())
											.getColumnName());
						}
					}
					if (StringUtil.isNotBlank(srcRelation.getFkName())) {
						many2oneElement.addAttribute("foreign-key",
								srcRelation.getFkName());
					}
				} else if (RelationType.TWOWAY_ONE2MANY.equals(srcRelation
						.getRelationType())) {
					Element setElement = element.addElement("set");
					setElement.addAttribute("name",
							srcRelation.getSrcRefTgtName());
					setElement.addAttribute("inverse", "true");
					Element keyElement = setElement.addElement("key");
					Collection relationFields = srcRelation.getRelationFields();
					if (relationFields.size() == 1) {
						IAssociationField iAssociationField = (IAssociationField) relationFields
								.iterator().next();
						String columnName = ((IPField) iAssociationField
								.getTgtField()).getColumnName();
						keyElement.addAttribute("column", columnName);
						keyElement.addAttribute("not-null", "true");
					} else {
						for (Iterator iterator2 = relationFields.iterator(); iterator2
								.hasNext();) {
							IAssociationField iAssociationField = (IAssociationField) iterator2
									.next();
							Element columnElement = keyElement
									.addElement("column");
							columnElement.addAttribute("name",
									((IPField) iAssociationField.getTgtField())
											.getColumnName());
						}
					}
					Element oneManyElement = setElement
							.addElement("one-to-many");
					String className = StringUtil.upperFirst(srcRelation
							.getTgtEntity().getName());
					if (StringUtil.isNotBlank(packageName)) {
						className = packageName + "." + className;
					}
					oneManyElement.addAttribute("class", className);
				}
			}
		}
		List tgtRelations = GenEntityInfo.getActTgtRelationList(pEntity);
		if (tgtRelations != null && !tgtRelations.isEmpty()) {
			for (Iterator iterator = tgtRelations.iterator(); iterator
					.hasNext();) {
				IRelation tgtRelation = (IRelation) iterator.next();
				Element many2oneElement = element.addElement("many-to-one");
				many2oneElement.addAttribute("name",
						StringUtil.lowerFirst(tgtRelation.getTgtRefSrcName()));
				String className = StringUtil.upperFirst(tgtRelation
						.getSrcEntity().getName());
				if (StringUtil.isNotBlank(packageName)) {
					className = packageName + "." + className;
				}
				many2oneElement.addAttribute("class", className);
				Collection relationFields = tgtRelation.getRelationFields();
				if (relationFields.size() == 1) {
					IAssociationField iAssociationField = (IAssociationField) relationFields
							.iterator().next();
					many2oneElement.addAttribute("column",
							((IPField) iAssociationField.getTgtField())
									.getColumnName());
				} else {
					for (Iterator iterator2 = relationFields.iterator(); iterator2
							.hasNext();) {
						IAssociationField iAssociationField = (IAssociationField) iterator2
								.next();
						Element columnElement = many2oneElement
								.addElement("column");
						columnElement.addAttribute("name",
								((IPField) iAssociationField.getTgtField())
										.getColumnName());
						columnElement.addElement("comment").addText(
								((IPField) iAssociationField.getSrcField())
										.getColumnName());
					}
				}
				if (StringUtil.isNotBlank(tgtRelation.getFkName())) {
					many2oneElement.addAttribute("foreign-key",
							tgtRelation.getFkName());
				}
			}
		}
		pEntity.getPEntityConditon().clear();
	}

	/**
	 * 生成查询实体（SqlModel）的hbm文件
	 * 
	 * @param vEntity
	 * @param packageName
	 * @param filePath
	 * @return
	 */
	public static File genHbmFile4SqlModel(IVEntity vEntity,
			String packageName, String filePath) {

		Document hbmDocument = HbmUtil.createHbmDocument();
		Element hbmElement = hbmDocument.addElement(elementRoot);
		// *****添加class元素********
		Element classElement = hbmElement.addElement("class");
		String className = StringUtil.upperFirst(vEntity.getName());
		// 如果packageName不为空并且className没有带包名自动把packageName加到className上
		if (StringUtil.isNotBlank(packageName) && className.indexOf(".") == -1) {
			className = packageName + "." + className;
		}
		classElement.addAttribute("name", className);

		// *****添加class comment元素********
		classElement.addElement("comment").addText(vEntity.getDisplayName());
		// *****添加class sql元素********
		Iterator<Map.Entry<String,String>> iterator = vEntity.getQuerySql().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,String> view = iterator.next();
			if (StringUtil.isNotBlank(view.getValue())) {
				setSqlElement(null, view.getKey(), view.getValue(),classElement);
			}
		}
		
		// *****添加class properties元素********
		genIdAndPropertyElement(classElement, vEntity, packageName);

		File hbmFile = FileUtil.createFile(filePath);
		XmlUtil.saveToFile(hbmDocument, hbmFile);
		log.debug("gen " + vEntity.getName() + " hbm file success");

		return hbmFile;
	}

	/**
	 * 生成查询实体（SqlModel）的映射信息（包括唯一标识与普通属性字段ORM）
	 * 
	 * @param element
	 * @param vEntity
	 * @param packageName
	 */
	public static void genIdAndPropertyElement(Element element,
			IVEntity vEntity, String packageName) {
		Collection pks = vEntity.getPKFields();
		if (pks != null && !pks.isEmpty()) {
			if (pks.size() == 1) {
				Element idElement = element.addElement("id");
				IVField vField = (IVField) pks.iterator().next();
				idElement.addAttribute("name", vField.getName());
				idElement.addAttribute("type", vField.getFieldType());
				setColumnProperty(vField, idElement);
			} else {
				Element compositeElement = element.addElement("composite-id");
				String pkClassName = getCompositPkName(vEntity.getName());
				compositeElement.addAttribute("name",
						StringUtil.lowerFirst(pkClassName));
				if (StringUtil.isNotBlank(packageName)) {
					pkClassName = packageName + "." + pkClassName;
				}
				compositeElement.addAttribute("class", pkClassName);

				for (Iterator iterator = pks.iterator(); iterator.hasNext();) {
					IVField vField = (IVField) iterator.next();
					Element keyPropertyElement = compositeElement
							.addElement("key-property");
					keyPropertyElement.addAttribute("name",
							vField.getFieldName());
					keyPropertyElement.addAttribute("type",
							vField.getFieldType());
					setColumnProperty(vField, keyPropertyElement);
				}
			}
		}

		Collection fields = vEntity.getFields();
		for (Iterator iterator = fields.iterator(); iterator.hasNext();) {
			IVField vField = (IVField) iterator.next();
			if (vField.isPK()) {
				continue;
			}
			if (vField.getRefEntity() == null
					&& vField.getParentEntity() instanceof IVEntity) {
				continue;
			}
			Element propertyElement = element.addElement("property");
			propertyElement.addAttribute("name", vField.getFieldName());
			propertyElement.addAttribute("type", vField.getFieldType());
			setColumnProperty(vField, propertyElement);
		}
	}

	/**
	 * 设置查询实体hbm中国的sql
	 * 
	 * @param type
	 * @param sql
	 * @param parentElement
	 */
	public static void setSqlElement(String id, String type, String sql,
			Element parentElement) {
		if (sql == null) {
			return;
		}
		Element sqlElement = parentElement.addElement("sql");
		if (id != null) {
			sqlElement.addAttribute("id", id.toLowerCase());
		}
		if (type != null) {
			sqlElement.addAttribute("type", type.toLowerCase());
		}
		sqlElement.addCDATA(sql);
	}

	/**
	 * 设置查询实体中属性的column定义
	 * 
	 * @param vField
	 * @param parentElement
	 */
	public static void setColumnProperty(IVField vField, Element parentElement) {
		Element columnElement = parentElement.addElement("column");
		String colName = StringUtil.isNotBlank(vField.getAlias()) ? vField
				.getAlias() : vField.getColumnName();
		columnElement.addAttribute("name", colName);
		columnElement.addAttribute("table",
				((IPEntity) vField.getRefEntity()).getTable());
		if (StringUtil.isNotBlank(vField.getDisplayName())) {
			columnElement.addElement("comment")
					.addText(vField.getDisplayName());
		}
	}

	public static void main(String[] args) throws Exception {
		File datasetFile = new File(
				"src/test/java/com/chinasofti/ro/bizframework/modules/dataset/resource/dataset.xml");
		IDataset dataset = DatasetUtil.buildDatasetFromXML(datasetFile);
		Collection pEntitys = DatasetUtil.getEntitys(dataset,
				new String[] { "12345678901234567890123456789100" });
		// genHbmFile(dataset, pEntitys, "test",
		// "com.chinasofti.ro.bizframework.modules.autocode.resource");
		// genHbmFile(dataset, pEntitys, null, null);
	}
}