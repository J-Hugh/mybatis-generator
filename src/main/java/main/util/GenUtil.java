package main.util;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import autocode.util.XmlUtil;


/**
 * 
 * @author wangzhichao
 *
 */
public class GenUtil {	
//	static final String DB_URL = "url";
//	static final String DB_DBNAME = "dbname";
//	static final String DB_DBTYPE = "dbtype";
//	static final String DB_USERNAME = "username";
//	static final String DB_DATASOURCE = "datasource";
//	static final String DB_PWD = "pwd";
//	static final String DB_DBCONFIGFILE = "code-gen.xml";
//	static final String DB_MODULE_GEN ="moduleGen";	
//	static final String CFG_ELEMENT_GENELEMENT = "genElement";
//	static final String CFG_ATTU_NAME = "table";
//	static final String CFG_ATTU_PCK = "package";
	
	
	public List<Map<String,Object>> getResultList(String tableName) throws Exception{
		
		Map<String,Object> map = null;
		List<Map<String,Object>> rstLst = new ArrayList<Map<String,Object>>();
		ClassLoader cl=GenUtil.class.getClassLoader();
		URL fileUrl=cl.getResource(Consts.CFG_DBCONFIGFILE);
		Document d = XmlUtil.getDocument(new File(fileUrl.getFile()));	
		//Document d = XmlUtil.getDocument(new File(Consts.CFG_USERDIR + "/src/" + Consts.CFG_DBCONFIGFILE));
		Element e = d.getRootElement();
		
		Element dbl = e.element(Consts.DB_DATASOURCE);
		
		String url = dbl.elementTextTrim(Consts.DB_URL);
		String dbName = dbl.elementTextTrim(Consts.DB_DBNAME);
		String dbType = dbl.elementTextTrim(Consts.DB_DBTYPE);
		String uName = dbl.elementTextTrim(Consts.DB_USERNAME);
		String pwd = dbl.elementTextTrim(Consts.DB_PWD);    
		
	    Class.forName(Consts.DB_MYSQLDRIVER);
	    Connection con = DriverManager.getConnection(url,uName,pwd);
	    
	    if(con.isClosed()){
	    	throw new Exception("获取数据库连接失败！");
		}
		
		Statement stt = con.createStatement();
		String sql = "SHOW FULL COLUMNS FROM " + dbName + "." + tableName;
				
		ResultSet rs = stt.executeQuery(sql);
		
//		int i = 0;
		while(rs.next()){
			map = new HashMap<String,Object>();
			map.put("field", rs.getString("Field"));
			map.put("type", rs.getString("Type"));
			map.put("key", rs.getObject("Key"));
			map.put("comment", rs.getObject("Comment"));
			rstLst.add(map);
		}
		
		
		rs.close();
		con.close();
		
		return rstLst;
	}
	
//	public static void main(String[] args) {
//		
//		List<Map<String, Object>> l = null;
//		try {
//			l = new GenUtil().getResultList("person");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//		for(Map<String, Object> m : l){
//			System.out.println(m.get("field"));
//			System.out.println(m.get("type"));
//			System.out.println(m.get("key"));
//			System.out.println("=====================");
//		}
//	}
//	
	
	public static String getBasePath(){
		
		return System.getProperty("user.dir");
		
	}
	
	
	public  List<Map<String,String>> getTableDetail() throws Exception{
		
		Map<String,String> entity = null;
		List<Map<String,String>> rstLst = new ArrayList<Map<String,String>>();
		
		ClassLoader cl=GenUtil.class.getClassLoader();
		URL url=cl.getResource(Consts.CFG_DBCONFIGFILE);
		Document d = XmlUtil.getDocument(new File(url.getFile()));	
		//Document d = XmlUtil.getDocument(new File(Consts.CFG_USERDIR + "/src/" + Consts.CFG_DBCONFIGFILE));	
		Element root = d.getRootElement();
		Element module = root.element(Consts.DB_MODULE_GEN );
		
		
		List<Element> lst = module.elements(Consts.CFG_ELEMENT_GENELEMENT);
		for(Element e : lst){
			entity = new HashMap<String,String>();
			String key = e.attributeValue(Consts.CFG_ATTU_NAME);
			String val = e.attributeValue(Consts.CFG_ATTU_PCK);
			entity.put(key, val);
			rstLst.add(entity);
		}
		
		return rstLst;
	}

	
	public static String packToPath(String pack){
		return pack.replace(".", "/");
	}
	
	
	public static String getJavaType(String type){
		
		if(type.equals("") || type == null){
			return "String";
		}
		if(type.indexOf("(") != -1){
			type = type.substring(0, type.indexOf("("));
		}
		
		if(type.equalsIgnoreCase("bigint")){
			return "Long";
		}
		
		if(type.equalsIgnoreCase("bit") || type.equalsIgnoreCase("blob") || type.equalsIgnoreCase("LONGBLOB") || type.equalsIgnoreCase("MEDIUMBLOB") || type.equalsIgnoreCase("TINYBLOB")){
			return "byte[]";
		}
		
		if(type.equalsIgnoreCase("bigint")){
			return "Long";
		}
		
		if(type.equalsIgnoreCase("char") || type.equalsIgnoreCase("enum") || type.equalsIgnoreCase("longtext")||type.equalsIgnoreCase("MEDIUMTEXT")
				|| type.equalsIgnoreCase("set") || type.equalsIgnoreCase("text") ||type.equalsIgnoreCase("TINYTEXT") || type.equalsIgnoreCase("VARCHAR")){
			return "String";
		}
		
		if(type.equalsIgnoreCase("date") || type.equalsIgnoreCase("year")){
			return "java.sql.Date";
		}
		
		if(type.equalsIgnoreCase("datetime")){
			return "java.sql.Timestamp";
		}
		
		if(type.equalsIgnoreCase("DECIMAL")){
			return "java.math.BigDecimal";
		}
		
		if(type.equalsIgnoreCase("DOUBLE") || type.equalsIgnoreCase("DOUBLE PRECISION")){
			return "Double";
		}
		
		if(type.equalsIgnoreCase("float")){
			return "Float";
		}
		
		if(type.equalsIgnoreCase("int") || type.equalsIgnoreCase("INTEGER") || type.equalsIgnoreCase("MEDIUMINT") || type.equals("SMALLINT")){
			return "Integer";
		}
		
		if(type.equalsIgnoreCase("time")){
			return "java.sql.Time";
		}
		
		if(type.equalsIgnoreCase("TIMESTAMP")){
			return "java.sql.Timestamp";
		}
		
		if(type.equalsIgnoreCase("TINYINT")){
			return "boolean";
		}
		
		return "String";
	}
	
	public static void main(String[] args) {
		System.out.println(getJavaType("bigint"));
	}
	
}
