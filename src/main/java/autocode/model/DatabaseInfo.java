/*
 * $Id: DatabaseInfo.java,v 1.1 2012/09/11 06:38:06 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode.model;

/**
 * 
 * @author Rone BizFoudation Team: ganjp
 *
 * 生成数据库需要输入的属性集，包括连接的数据库信息，hbm文件等等
 *
 * @version 4.3
 * @since 4.3
 */
public class DatabaseInfo {
    //各种数据库的hibernate对应的dialect
    public static final String DB_HIBERNATE_DIALECT_MYSQL="org.hibernate.dialect.MySQLDialect";
    public static final String DB_HIBERNATE_DIALECT_ORACLE_8I="org.hibernate.dialect.Oracle8iDialect";
    public static final String DB_HIBERNATE_DIALECT_ORACLE_9I="org.hibernate.dialect.Oracle9iDialect";
    public static final String DB_HIBERNATE_DIALECT_ORACLE_10G="org.hibernate.dialect.Oracle10gDialect";
    public static final String DB_HIBERNATE_DIALECT_ORACLE="org.hibernate.dialect.OracleDialect";
    public static final String DB_HIBERNATE_DIALECT_DB2="org.hibernate.dialect.DB2Dialect";
    public static final String DB_HIBERNATE_DIALECT_SQLSERVER="org.hibernate.dialect.SQLServerDialect";
    public static final String DB_HIBERNATE_DIALECT_H2="org.hibernate.dialect.H2Dialect";
    public static final String DB_HIBERNATE_DIALECT_HSQL="org.hibernate.dialect.HSQLDialect";
    public static final String DB_HIBERNATE_DIALECT_DERBY="org.hibernate.dialect.DerbyDialect";
    public static final String DB_HIBERNATE_DIALECT_DAMENG="org.hibernate.dialect.DmDialect";
    public static final String DB_HIBERNATE_DIALECT_KINGBASE="org.hibernate.dialect.KBDialect";
    
    private String dbHibernateDialect;//数据库对应的hibernate方言
    private String dbDriverClass; //连接数据库的jdbc驱动类
    private String dbUrl; //连接数据库的url
    private String dbUserName; //连接数据库用户名
    private String dbPassword; //连接数据库密码
    private String dbSchema; //数据库schema  
    
    /**
     * 获得数据库对应的Dialect
     * @return
     */
    public String getDbHibernateDialect() {
        return dbHibernateDialect;
    }

    /**
     * 设置数据库对应的Dialect
     * @return
     */
    public void setDbHibernateDialect(String dbHibernateDialect) {
        this.dbHibernateDialect = dbHibernateDialect;
    }

    /**
     * 获得数据库jdbc驱动类
     * @return
     */
    public String getDbDriverClass() {
        return dbDriverClass;
    }
    
    /**
     * 设置连接数据库jdbc驱动类,类的示例如下：
     * (1)mysql："com.mysql.jdbc.Driver"
     * (2)oracle："oracle.jdbc.driver.OracleDriver"
     * (3)db2："COM.ibm.db2.jdbc.app.DB2Driver"      
     * (4)sqlserver："com.microsoft.jdbc.sqlserver.SQLServerDriver"
     * (5)h2："org.h2.Driver"
     * (6)derby："org.apache.derby.jdbc.ClientDriver"
     * 
     * @param dbDriverClass
     */
    public void setDbDriverClass(String dbDriverClass) {
        this.dbDriverClass = dbDriverClass;
    }
    
    /**
     * 获得连接数据库的url
     * @return
     */
    public String getDbUrl() {
        return dbUrl;
    }
    
    /**
     * 设置连接数据库的url,url的示例如下：
     * (1)mysql："jdbc:mysql://localhost:3306/dbname?useUnicode=true&characterEncoding=UTF-8"}
     * (2)oracle："jdbc:oracle:thin:@localhost:1521:dbname"
     * (3)db2："jdbc:db2://localhost:50000/dbname"       
     * (4)sqlserver："jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=dbname"
     * (5)h2："jdbc:h2:tcp://localhost:9092/dbname"
     * (6)derby："jdbc:derby://localhost:1527/dbname"
     * 
     * @param dbUrl 
     *               
     */
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
    
    /**
     * 获得连接数据库的用户名
     * @return
     */
    public String getDbUserName() {
        return dbUserName;
    }
    
    /**
     * 设置数据库用户名 
     * @param dbUserName
     */
    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }
    
    /**
     * 获得连接数据库的密码
     * @return
     */
    public String getDbPassword() {
        return dbPassword;
    }
    
    /**
     * 设置数据库用户密码
     * @param dbPassword
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbSchema() {
        return dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }
    
} 