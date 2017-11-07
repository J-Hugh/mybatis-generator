/*
 * 文 件 名:  GenMapper.java
 * 版    权:  Hiaward Information Technology Co., Ltd. Copyright(2012),All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  yaolei
 * 创建时间: 2014-4-18
 * 修 改 人:  
 * 修改时间: 
 * 修改内容:  <修改内容>
 */
package autocode;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.model.AutoCodeInfo;
import autocode.util.FileUtil;
import autocode.util.MapperUtil;
import autocode.util.StringUtil;
import autocode.util.XmlUtil;
import dataset.model.IEntity;
import dataset.model.IPEntity;
import dataset.model.IPField;
import dataset.model.IVEntity;
import dataset.model.impl.Field;
import dataset.model.impl.PEntity;
import dataset.model.impl.PField;
import dataset.model.impl.VField;
import dataset.viewmodel.impl.QueryField;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  yaolei
 * @version [版本号, 2014-4-18]
 * @see     [相关类/方法]
 * @since   [产品/模块版本]
 */
public class BuildMapper {
	static Logger log = LoggerFactory.getLogger(GenHbm.class);
	// 生成模型Mapper文件的根节点
	private static String ELEMENTROOT = "mapper";
	
	private static String INCLUDE = "<include refid=\"columns\"/>";
	
	private static String CLASSNAME = "";
	
	/**
	 * 通过持久化实体集合生成Mapper文件
	 * 
	 * @param iEntity
	 * @param autoCodeInfo
	 */
	public static void buildMapperFile(IEntity iEntity, AutoCodeInfo autoCodeInfo, String tableType) {
		if (StringUtil.isNotBlank(autoCodeInfo.getHbmPath())) {
			Document mapperDocument = MapperUtil.createMapperDocument();
			Element rootElement = buildRootElement(mapperDocument, iEntity.getName(), autoCodeInfo.getDaoPackageName());
			if(iEntity instanceof IPEntity){//持久化实体
				buildSelectElement(rootElement, iEntity);
				buildSelectAllElement(rootElement, iEntity);
				buildSelectConditionElement(rootElement, iEntity);
				buildSelectPagElement(rootElement, iEntity);
				buildInsertElement(rootElement, (IPEntity)iEntity);
				buildDeleteElement(rootElement, (IPEntity)iEntity);
				buildUpdateElement(rootElement, (IPEntity)iEntity);
				//从表需要生成的
				if(BuildModule.DETAIL_TABLE_TYPE.equals(tableType)){
					buildSelectForeignKeyElement(rootElement, (IPEntity)iEntity);
					buildDeleteForeignKeyElement(rootElement, (IPEntity)iEntity);
				}
			} else if(iEntity instanceof IVEntity){//查询实体
				buildSelectElement(rootElement, iEntity);
				buildSelectAllElement(rootElement, iEntity);
				buildSelectConditionElement(rootElement, iEntity);
				buildSelectPagElement(rootElement, iEntity);
			}
			buildResultMap(rootElement, iEntity);
			buildSqlColumns(rootElement, iEntity);
			File mapperFile = FileUtil.createFile(autoCodeInfo.getHbmPath());
			XmlUtil.saveToFile(mapperDocument, mapperFile);
			log.debug("build " + CLASSNAME + " mapper file success");
		}
	}
	
	public static void main(String[] args){
		IPField f = new PField();
		f.setFieldName("id");
		f.setPK(true);
		f.setColumnName("id");
		f.setDisplayName("id");
		f.setFieldType("Integer");
		f.setDesc("主键");
		
		IPField f1 = new PField();
		f1.setFieldName("name");
		f1.setColumnName("name");
		f1.setDisplayName("name");
		f1.setPK(false);
		f1.setFieldType("String");
		f1.setDesc("姓名");
		
		IPField f2 = new PField();
		f2.setFieldName("address");
		f2.setColumnName("address");
		f2.setDisplayName("address");
		f2.setPK(false);
		f2.setFieldType("String");
		f2.setDesc("姓名");
		
		IPEntity ent = new PEntity();
		ent.addField(f);
		ent.addField(f1);
		ent.addField(f2);
		ent.setName("person");
		ent.setTable("person");
		
		AutoCodeInfo ai = new AutoCodeInfo();
		ai.setHbmPath("C:/Users/Administrator/Desktop/AutoCodeTest/test.xml");
		ai.setDaoPackageName("com.uni2uni.test");
		buildMapperFile(ent,ai,"");
	}
	
	/**
	 * <p>
	 * 在Document下面增加根
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iPEntity
	 * @param packageName
	 */
	public static Element buildRootElement(Document mapperDocument, String entityName, String packageName) {
		Element rootElement = mapperDocument.addElement(ELEMENTROOT);
		CLASSNAME = StringUtil.upperFirst(entityName);
		String classLongName = "";
		// 如果packageName不为空并且className没有带包名自动把packageName加到className上
		if (StringUtil.isNotBlank(packageName) && CLASSNAME.indexOf(".") == -1) {
			classLongName = packageName + "." + CLASSNAME + "Dao";
		}
		rootElement.addAttribute("namespace", classLongName);
		
		return rootElement;
	}
	
	/**
	 * <p>
	 * 在Root下面增加Select:查询一条记录
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iEntity
	 */
	@SuppressWarnings("unchecked")
	public static void buildSelectElement(Element rootElement, IEntity iEntity) {
		StringBuilder sqlStr = new StringBuilder();
		if(iEntity instanceof IPEntity){//持久化实体
			sqlStr.append("SELECT ").append(INCLUDE).append(" FROM ").append(((IPEntity)iEntity).getTable()).append(" WHERE ");
			List<PField> pkFields = (List<PField>)iEntity.getPKFields();
			for (int i = 0; i < pkFields.size(); i++) {
				String columnName = pkFields.get(i).getColumnName();
				String fieldName = pkFields.get(i).getFieldName();
				if(i == pkFields.size()-1){
					sqlStr.append(columnName).append("=#{").append(fieldName).append("}");
				}else{
					sqlStr.append(columnName).append("=#{").append(fieldName).append("} AND ");
				}
			}
		} else if(iEntity instanceof IVEntity) {//查询实体
			sqlStr.append(((IVEntity)iEntity).getQuerySql().get("Default").split("#")[0]);
			List<VField> pkFields = (List<VField>)iEntity.getPKFields();
			for (int i = 0; i < pkFields.size(); i++) {
				PEntity pEntity = (PEntity)pkFields.get(i).getRefEntity();
				String columnName = pkFields.get(i).getColumnName();
				String fieldName = pkFields.get(i).getFieldName();
				sqlStr.append(" AND ").append(pEntity.getTable()).append(".").append(columnName).append("=#{").append(fieldName).append("}");
			}
		}
		sqlStr.append(buildOrderByAttribute(iEntity));
		
		rootElement.addComment("查询一条记录");
		Element selectElement = rootElement.addElement("select");
		selectElement.addAttribute("id", "findById");
		selectElement.addAttribute("resultMap", CLASSNAME + "Map");
		selectElement.addText(sqlStr + "");
	}
	
	/**
	 * <p>
	 * 在Root下面增加Select:查询所有记录
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iEntity
	 */
	public static void buildSelectAllElement(Element rootElement, IEntity iEntity) {
		StringBuilder sqlStr = new StringBuilder();
		if(iEntity instanceof IPEntity){//持久化实体
			sqlStr.append("SELECT ").append(INCLUDE).append(" FROM ").append(((IPEntity)iEntity).getTable());
		} else if(iEntity instanceof IVEntity){//查询实体
			sqlStr.append(((IVEntity)iEntity).getQuerySql().get("Default").split("#")[0]);
		}
		
		rootElement.addComment("查询所有记录");
		Element selectAllElement = rootElement.addElement("select");
		selectAllElement.addAttribute("id", "findAll");
		selectAllElement.addAttribute("resultMap", CLASSNAME + "Map");
		selectAllElement.addText(sqlStr + "");
	}
	
	/**
	 * <p>
	 * 在Root下面增加Select:按条件查询
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iEntity
	 */
	@SuppressWarnings("unchecked")
	public static void buildSelectConditionElement(Element rootElement, IEntity iEntity) {
		StringBuilder sqlStr = new StringBuilder();
		if(iEntity instanceof IPEntity){//持久化实体
			sqlStr.append("SELECT ").append(INCLUDE).append(" FROM ").append(((IPEntity)iEntity).getTable());
		} else if(iEntity instanceof IVEntity){//查询实体
			sqlStr.append(((IVEntity)iEntity).getQuerySql().get("Default").split("#")[0]);
		}
		
		rootElement.addComment("按条件查询");
		Element selectPagElement = rootElement.addElement("select");
		selectPagElement.addAttribute("id", "search");
		selectPagElement.addAttribute("parameterType", "map");
		selectPagElement.addAttribute("resultMap", CLASSNAME + "Map");
		selectPagElement.addText(sqlStr + "");
		
		if(iEntity instanceof IPEntity){//持久化实体
			Element whereElement = selectPagElement.addElement("where");
			
			List<QueryField> queryFields = (List<QueryField>)iEntity.getQueryForm().getFields();
			for (int i = 0; i < queryFields.size(); i++) {
				StringBuilder testStr = new StringBuilder();
				StringBuilder ifStr = new StringBuilder();
				String fieldName = queryFields.get(i).getFieldName();
				String columnName = getColumnNameFromQueryForm(fieldName, (List<Field>)iEntity.getFields());
				if(i != 0){
					ifStr.append("AND ");
				}
				ifStr.append(columnName).append(" = #{").append(fieldName).append("}");
				testStr.append(fieldName).append(" != null and ").append(fieldName).append(" != ''");
				
				Element ifElement = whereElement.addElement("if");
				ifElement.addAttribute("test", testStr + "");
				ifElement.addText(ifStr + "");
			}
		} else if(iEntity instanceof IVEntity){//查询实体
			List<QueryField> queryFields = (List<QueryField>)iEntity.getQueryForm().getFields();
			for (int i = 0; i < queryFields.size(); i++) {
				PEntity pEntity = (PEntity)queryFields.get(i).getRefEntity();
				StringBuilder testStr = new StringBuilder();
				StringBuilder ifStr = new StringBuilder();
				String fieldName = queryFields.get(i).getFieldName();
				String columnName = getColumnNameFromQueryForm(fieldName, (List<Field>)iEntity.getFields());
				ifStr.append("AND ").append(pEntity.getTable()).append(".").append(columnName).append(" = #{").append(fieldName).append("}");
				testStr.append(fieldName).append(" != null and ").append(fieldName).append(" != ''");
				
				Element ifElement = selectPagElement.addElement("if");
				ifElement.addAttribute("test", testStr + "");
				ifElement.addText(ifStr + "");
			}
		}
	}
	
	/**
	 * <p>
	 * 在Root下面增加Select:分页查询
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iPEntity
	 */
	@SuppressWarnings("unchecked")
	public static void buildSelectPagElement(Element rootElement, IEntity iEntity) {
		StringBuilder sqlStr = new StringBuilder();
		if(iEntity instanceof IPEntity){//持久化实体
			sqlStr.append("SELECT ").append(INCLUDE).append(" FROM ").append(((IPEntity)iEntity).getTable());
		} else if(iEntity instanceof IVEntity){//查询实体
			sqlStr.append(((IVEntity)iEntity).getQuerySql().get("Default").split("#")[0]);
		}
		
		rootElement.addComment("分页查询");
		Element selectPagElement = rootElement.addElement("select");
		selectPagElement.addAttribute("id", "searchByPage");
		selectPagElement.addAttribute("parameterType", "map");
		selectPagElement.addAttribute("resultMap", CLASSNAME + "Map");
		selectPagElement.addText(sqlStr + "");
		
		if(iEntity instanceof IPEntity){//持久化实体
			Element whereElement = selectPagElement.addElement("where");
			
			List<QueryField> queryFields = (List<QueryField>)iEntity.getQueryForm().getFields();
			for (int i = 0; i < queryFields.size(); i++) {
				StringBuilder testStr = new StringBuilder();
				StringBuilder ifStr = new StringBuilder();
				String fieldName = queryFields.get(i).getFieldName();
				String columnName = getColumnNameFromQueryForm(fieldName, (List<Field>)iEntity.getFields());
				if(i != 0){
					ifStr.append("AND ");
				}
				ifStr.append(columnName).append(" = #{").append(fieldName).append("}");
				testStr.append(fieldName).append(" != null and ").append(fieldName).append(" != ''");
				
				Element ifElement = whereElement.addElement("if");
				ifElement.addAttribute("test", testStr + "");
				ifElement.addText(ifStr + "");
			}
		} else if(iEntity instanceof IVEntity){//查询实体
			List<QueryField> queryFields = (List<QueryField>)iEntity.getQueryForm().getFields();
			for (int i = 0; i < queryFields.size(); i++) {
				PEntity pEntity = (PEntity)queryFields.get(i).getRefEntity();
				StringBuilder testStr = new StringBuilder();
				StringBuilder ifStr = new StringBuilder();
				String fieldName = queryFields.get(i).getFieldName();
				String columnName = getColumnNameFromQueryForm(fieldName, (List<Field>)iEntity.getFields());
				ifStr.append("AND ").append(pEntity.getTable()).append(".").append(columnName).append(" = #{").append(fieldName).append("}");
				testStr.append(fieldName).append(" != null and ").append(fieldName).append(" != ''");
				
				Element ifElement = selectPagElement.addElement("if");
				ifElement.addAttribute("test", testStr + "");
				ifElement.addText(ifStr + "");
			}
		}
	}
	
	/**
	 * <p>
	 * 在Root下面增加Insert:插入记录
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iPEntity
	 */
	@SuppressWarnings("unchecked")
	public static void buildInsertElement(Element rootElement, IPEntity iPEntity) {
		StringBuilder sqlStr = new StringBuilder();
		StringBuilder subStr = new StringBuilder();
		List<PField> pFields = (List<PField>)iPEntity.getFields();
		for (int i = 0; i < pFields.size(); i++) {
			String fieldName = pFields.get(i).getFieldName();
			if(i == pFields.size()-1){
				subStr.append("#{").append(fieldName).append("})");
			}else{
				subStr.append("#{").append(fieldName).append("},");
			}
		}
		sqlStr.append("INSERT INTO ").append(iPEntity.getTable()).append(" (").append(INCLUDE).append(") VALUES (").append(subStr);
		
		rootElement.addComment("插入记录");
		Element insertElement = rootElement.addElement("insert");
		insertElement.addAttribute("id", "save");
		insertElement.addAttribute("parameterType", CLASSNAME);
		insertElement.addText(sqlStr + "");
	}
	
	/**
	 * <p>
	 * 在Root下面增加Delete:删除记录
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iPEntity
	 */
	@SuppressWarnings("unchecked")
	public static void buildDeleteElement(Element rootElement, IPEntity iPEntity) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("DELETE FROM ").append(iPEntity.getTable()).append(" WHERE ");
		List<PField> pkFields = (List<PField>)iPEntity.getPKFields();
		for (int i = 0; i < pkFields.size(); i++) {
			String columnName = pkFields.get(i).getColumnName();
			String fieldName = pkFields.get(i).getFieldName();
			if(i == pkFields.size()-1){
				sqlStr.append(columnName).append("=#{").append(fieldName).append("}");
			}else{
				sqlStr.append(columnName).append("=#{").append(fieldName).append("} AND ");
			}
		}
		
		rootElement.addComment("删除记录");
		Element deleteElement = rootElement.addElement("delete");
		deleteElement.addAttribute("id", "delete");
		deleteElement.addText(sqlStr + "");
	}
	
	/**
	 * <p>
	 * 在Root下面增加Update:修改记录
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iPEntity
	 */
	@SuppressWarnings("unchecked")
	public static void buildUpdateElement(Element rootElement, IPEntity iPEntity) {
		StringBuilder startSqlStr = new StringBuilder();
		StringBuilder endSqlStr = new StringBuilder();
		startSqlStr.append("UPDATE ").append(iPEntity.getTable());
		
		rootElement.addComment("修改记录");
		Element updateElement = rootElement.addElement("update");
		updateElement.addAttribute("id", "update");
		updateElement.addAttribute("parameterType", CLASSNAME);
		updateElement.addText(startSqlStr + "");
		
		Element setElement = updateElement.addElement("set");
		
		List<PField> pFields = (List<PField>)iPEntity.getFields();
		for (int i = 0; i < pFields.size(); i++) {
			String columnName = pFields.get(i).getColumnName();
			String fieldName = pFields.get(i).getFieldName();
			if(!pFields.get(i).isPK()){
				Element ifElement = setElement.addElement("if");
				ifElement.addAttribute("test", fieldName + " != null");
				ifElement.addText(columnName + " = #{" + fieldName + "}, ");
			}
		}
		endSqlStr.append("WHERE ");
		List<PField> pkFields = (List<PField>)iPEntity.getPKFields();
		for (int i = 0; i < pkFields.size(); i++) {
			String columnName = pkFields.get(i).getColumnName();
			String fieldName = pkFields.get(i).getFieldName();
			if(i == pkFields.size()-1){
				endSqlStr.append(columnName).append("=#{").append(fieldName).append("}");
			}else{
				endSqlStr.append(columnName).append("=#{").append(fieldName).append("} AND ");
			}
		}
		updateElement.addText(endSqlStr + "");
	}
	
	/**
	 * <p>
	 * 在Root下面增加Select:按外键查询
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iPEntity
	 */
	@SuppressWarnings("unchecked")
	public static void buildSelectForeignKeyElement(Element rootElement, IPEntity iPEntity) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ");
		List<PField> pFields = (List<PField>)iPEntity.getFields();
		sqlStr.append(INCLUDE).append(" FROM ").append(iPEntity.getTable()).append(" WHERE ");
		int keyCount = 0;
		for (int i = 0; i < pFields.size(); i++) {
			if(pFields.get(i).isFK()){
				String columnName = pFields.get(i).getColumnName();
				String fieldName = pFields.get(i).getFieldName();
				if(keyCount == 0){
					sqlStr.append(columnName).append("=#{").append(fieldName).append("}");
				}else{
					sqlStr.append(" AND ").append(columnName).append("=#{").append(fieldName).append("}");
				}
				keyCount++;
			}
		}
		
		rootElement.addComment("按外键查询");
		Element selectElement = rootElement.addElement("select");
		selectElement.addAttribute("id", "searchByForeignKeyId");
		selectElement.addAttribute("resultMap", CLASSNAME + "Map");
		selectElement.addText(sqlStr + "");
	}
	
	/**
	 * <p>
	 * 在Root下面增加Delete:按外键删除记录
	 * </p>
	 * 
	 * @param mapperDocument
	 * @param iPEntity
	 */
	@SuppressWarnings("unchecked")
	public static void buildDeleteForeignKeyElement(Element rootElement, IPEntity iPEntity) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("DELETE FROM ").append(iPEntity.getTable()).append(" WHERE ");
		List<PField> fields = (List<PField>)iPEntity.getFields();
		int keyCount = 0;
		for (int i = 0; i < fields.size(); i++) {
			if(fields.get(i).isFK()){
				String columnName = fields.get(i).getColumnName();
				String fieldName = fields.get(i).getFieldName();
				if(keyCount == 0){
					sqlStr.append(columnName).append("=#{").append(fieldName).append("}");
				}else{
					sqlStr.append(" AND ").append(columnName).append("=#{").append(fieldName).append("}");
				}
				keyCount++;
			}
		}
		
		rootElement.addComment("按外键删除记录");
		Element deleteElement = rootElement.addElement("delete");
		deleteElement.addAttribute("id", "deleteByForeignKeyId");
		deleteElement.addText(sqlStr + "");
	}
	
	/**
	 * 生成ResultMap标签
	 * 
	 * @param rootElement
	 * @param iEntity
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public static void buildResultMap(Element rootElement, IEntity iEntity){
		rootElement.addComment("结果映射");
		Element resultMapElement = rootElement.addElement("resultMap");
		resultMapElement.addAttribute("type", CLASSNAME);
		resultMapElement.addAttribute("id", CLASSNAME + "Map");
		
		if(iEntity instanceof IPEntity){//持久化实体
			List<PField> pFields = (List<PField>)iEntity.getFields();
			for (int i = 0; i < pFields.size(); i++) {
				String columnName = pFields.get(i).getColumnName();
				String fieldName = pFields.get(i).getFieldName();
//				String columnType = pFields.get(i).getColumnType();
				
				Element ifElement = resultMapElement.addElement("result");
				ifElement.addAttribute("column", columnName);
				ifElement.addAttribute("property", fieldName);
//				ifElement.addAttribute("jdbcType", columnType);
			}
		} else if(iEntity instanceof IVEntity){//查询实体
			
		}
	}
	
	/**
	 * 生成SQL标签
	 * 
	 * @param rootElement
	 * @param iEntity
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings("unchecked")
	public static void buildSqlColumns(Element rootElement, IEntity iEntity){
		StringBuilder columnStr = new StringBuilder();
		if(iEntity instanceof IPEntity){//持久化实体
			List<PField> pFields = (List<PField>)iEntity.getFields();
			for (int i = 0; i < pFields.size(); i++) {
				String columnName = pFields.get(i).getColumnName();
				if(i == pFields.size()-1){
					columnStr.append(columnName);
				}else{
					columnStr.append(columnName).append(",");
				}
			}
		} else if(iEntity instanceof IVEntity){//查询实体
			
		}
		
		rootElement.addComment("查询字段");
		Element sqlElement = rootElement.addElement("sql");
		sqlElement.addAttribute("id", "columns");
		sqlElement.addText(columnStr + "");
	}
	
	/**
	 * 生成排序字段
	 * 
	 * @param iPEntity
	 * @see [类、类#方法、类#成员]
	 */
	public static String buildOrderByAttribute(IEntity iEntity){
//		iPEntity.getFields();
		return "";
	}
	
	//获取列名
	public static String getColumnNameFromQueryForm(String fieldName, List<Field> fields){
		for (Field field : fields) {
			if(field instanceof PField){
				if(fieldName.equals(field.getFieldName())){
					return ((PField)field).getColumnName();
				}
			} else if(field instanceof VField) {
				if(fieldName.equals(field.getFieldName())){
					return ((VField)field).getColumnName();
				}
			}
		}
		return "";
	}
}
