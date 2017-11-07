package main.util;

import java.io.File;
import java.net.URL;

import main.base.impl.GenCodeHandler;

import org.dom4j.Document;

import autocode.util.XmlUtil;

/**
 * 常量类
 * @author 王志超
 *
 */
public class Consts {
	
	/**
	 * 数据库配置
	 */
	public static final String DB_URL = "url";
	public static final String DB_DBNAME = "dbname";
	public static final String DB_DBTYPE = "dbtype";
	public static final String DB_USERNAME = "username";
	public static final String DB_DATASOURCE = "datasource";
	public static final String DB_PWD = "pwd";
	public static final String DB_MODULE_GEN ="moduleGen";
	public static final String DB_MYSQLDRIVER = "com.mysql.jdbc.Driver";  
	
	/**
	 * 配置文件相关配置
	 */
	public static final String CFG_ELEMENT_GENELEMENT = "genElement";
	public static final String CFG_ATTU_NAME = "table";
	public static final String CFG_ATTU_PCK = "package";
	public static final String CFG_DBCONFIGFILE = "code-gen.xml";
	public static final String CFG_USERDIR = System.getProperty("user.dir");	
	public static final String REST_PRO_PATH = "com.uni2uni.bsp.modules.";
	
	/**
	 * 类名后缀
	 */
	public static final String VM_MODULESNAME_SERVICE_FIRST_UPPER = "Service";
	public static final String VM_MODULESNAME_SERVICE_FIRST_LOWER = "service";
	public static final String VM_MODULESNAME_DAO_FIRST_UPPER = "Dao";
	public static final String VM_MODULESNAME_DAO_FIRST_LOWER = "dao";
	public static final String VM_MODULESNAME_REST_FIRST_UPPER = "Rest";
	public static final String VM_MODULESNAME_REST_FIRST_LOWER = "rest";
	public static final String VM_MODULESNAME_DOMAIN_FIRST_UPPER = "Domain";
	public static final String VM_MODULESNAME_DOMAIN_FIRST_LOWER = "domain";
	public static final String TMPLATE_PACKAGE_NAME = "template";
	
	public static final String FILE_SPERATOR = File.separator;
	//public static final String VMFULLPATH1 = Consts.CFG_USERDIR + File.separator  + "src" + File.separator + Consts.TMPLATE_PACKAGE_NAME + File.separator;
	public static final  ClassLoader cl=Consts.class.getClassLoader();
	public static final  URL fileUrl=cl.getResource(Consts.TMPLATE_PACKAGE_NAME);
	public static final String  VMFULLPATH = fileUrl.getPath() + File.separator;

}
