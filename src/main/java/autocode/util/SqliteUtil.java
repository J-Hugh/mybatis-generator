package autocode.util;

import java.util.List;

import dataset.model.IPEntity;
import dataset.model.impl.PField;

/**
 * 
 * @Title: SqliteUtil.java
 * @Description: <br>sqlite 与java属性的映射类
 * @Company: ChinaSoft International
 * @Created on 2012-6-27 下午1:34:26
 * @author chenhua
 * @version $Revision: 1.1 $
 * @since 1.0
 */
public class SqliteUtil {
    public static String getCreateTable(IPEntity entity) {
        //modify by zhangmh,add 'IF EXISTS '
        StringBuffer sb = new StringBuffer("CREATE TABLE IF EXISTS " + entity.getName() + " (");
        List<PField> list = (List<PField>) entity.getFields();
        for (int i = 0; i < list.size(); i++) {
            PField pfield = list.get(i);
            sb.append(pfield.getFieldName());
            sb.append(" ");
            sb.append(getSqliteDataType(pfield.getFieldType()));
            if (i != list.size() - 1) {
                sb.append(",");
            } else {
                sb.append(")");
            }
        }
        return sb.toString();
    }
    
  //modify by zhangmh,add 
    public static String getCreateSyncTable(IPEntity entity) {
        StringBuffer sb = new StringBuffer("CREATE TABLE IF EXISTS " + entity.getName() + " ( id  VARCHAR(64) primary key,");
        List<PField> list = (List<PField>) entity.getFields();
        for (int i = 0; i < list.size(); i++) {
            PField pfield = list.get(i);
            sb.append(pfield.getFieldName());
            sb.append(" ");
            sb.append(getSqliteDataType(pfield.getFieldType()));
            if (i != list.size() - 1) {
                sb.append(",");
            } else {
                sb.append(",syncTime TIMESTAMP,");
                sb.append("syncStatus NVARCHAR(20),");
                sb.append("syncAction NVARCHAR(15),");
                sb.append("updateTime TIMESTAMP) ");
            }
        }
        return sb.toString();
    }

    public static String getSqliteDataType(String datatype) {
        if (datatype == null) {
            return "TEXT";
        } else if (datatype.equals("java.lang.Integer") || datatype.equals("java.lang.Short")) {
            return "INTEGER";
        } else if (datatype.equals("java.lang.Long")) {
            return "INTEGER";
        } else if (datatype.equals("java.lang.String")) {
            return "TEXT";
        } else if (datatype.equals("java.lang.Byte")) {
            return "Blob";
        } else if (datatype.equals("java.math.BigDecimal")) {
            return "REAL";
        } else if (datatype.equals("java.lang.Float")) {
            return "Float";
        } else if (datatype.equals("java.lang.Float")) {
            return "Double";
        } else if (datatype.equals("java.sql.Time")) {
            return "NUMERIC";
        } else if (datatype.equals("java.sql.Date")) {
            return "Date";
        } else if (datatype.equals("java.sql.Timestamp")) {
            return "Timestamp";
        } else if (datatype.equals("java.sql.Boolean")) {
            return "Boolean";
        }
        return "TEXT";
    }
}
