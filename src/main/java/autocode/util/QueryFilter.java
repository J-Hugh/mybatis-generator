/*
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 * 
 */
package autocode.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;



/**
 * 查询过滤器:主要记录页面中简单的搜索过滤条件
 * 
 * @author BizFoundation Team: zhaoqy
 * 
 * @version1.0
 * @since 4.2
 * 
 * $Revision: 1.1 $
 */
public class QueryFilter implements Serializable {

    private static final long serialVersionUID = 6838975339936792774L;
    private boolean isMultiValue = false;
    
    /** 多个属性间OR关系的分隔符. */
    public static final String OR_SEPARATOR = "_OR_";

    /** 属性数据类型. */
    static final Map PropertyType = new HashMap();
    
    public static final String C = "C";
    public static final String I = "I";
    public static final String S = "S";
    public static final String L = "L";
    public static final String F = "F";
    public static final String N = "N";
    public static final String D = "D";
    public static final String B = "B";
    public static final String K = "K";
    public static final String T = "T";
    public static final String Y = "Y";
    public static final String H = "H";
    public static final String E = "E";
    public static final String O = "O";
    public static final String J = "J";
    
    static {
        PropertyType.put(C, Character.class);
        PropertyType.put(I, Integer.class);
        PropertyType.put(S, String.class);
        PropertyType.put(L, Long.class);
        PropertyType.put(F, Float.class);
        PropertyType.put(D, Date.class);
        PropertyType.put(B, Boolean.class);
        PropertyType.put(T, Timestamp.class);
        
        PropertyType.put(N, Double.class);
        PropertyType.put(K, BigDecimal.class);
        PropertyType.put(Y, Byte.class);
        PropertyType.put(H, Short.class);
        PropertyType.put(E, Blob.class);
        PropertyType.put(O, Clob.class);
        PropertyType.put(J, Time.class);
    }

    /** 属性比较类型. */
    public static class MatchType {
        public static final String EQ = "=";
        public static final String LIKE = "like";
        public static final String LT = "<";
        public static final String GT = ">";
        public static final String LE = "<=";
        public static final String GE = ">=";
        public static final String UNEQ = "!=";

        static final Map matchType = new HashMap();
        static {
            matchType.put("EQ", "=");
            matchType.put("LIKE", "like");
            matchType.put("LT", "<");
            matchType.put("GT", ">");
            matchType.put("LE", "<=");
            matchType.put("GE", ">=");
            matchType.put("UNEQ", "!=");
        }

        public static String getValue(String type) {
            return (String) matchType.get(type);
        }

        public static String getKey(String value) {
            String key = null;
            if (matchType.containsValue(value)) {
                Iterator it = matchType.keySet().iterator();
                while (it.hasNext()) {
                    String k = (String) it.next();
                    if (matchType.get(k).equals(value)) {
                        key = k;
                        break;
                    }
                }
            }
            return key;
        }
    }

    private String[] propertyNames = null;
    private Object propertyValue = null;
    private Class propertyType = null;
    private Object matchType = null;

    /**
     * 根据属性数据类型获得相应的java类型，比如 I 对应 Integer.class
     * @param key
     * @return
     */
    public static String getPropertyTypeValue(String key) {
        return (String) PropertyType.get(key);
    }
    
    /**
     * 根据java类型获得相应的属性数据类型，比如 Integer.class 对应 I
     * @param value
     * @return
     */
    public static String getPropertyTypeKey(Class value) {
        String key = null;
        if (PropertyType.containsValue(value)) {
            Iterator it = PropertyType.keySet().iterator();
            while (it.hasNext()) {
                String k = (String) it.next();
                if (PropertyType.get(k).equals(value)) {
                    key = k;
                    break;
                }
            }
        }
        return key;
    }
    
    /**
     * @param filterName 比较属性字符串,含待比较的比较类型、属性值类型及属性列表. eg. LIKES_NAME_OR_LOGIN_NAME
     * @param value 待比较的值.
     * 
     */
    public QueryFilter(final String filterName, String value) {
        String matchTypeStr = StringUtils.substringBefore(filterName, "_");
        String matchTypeCode = StringUtils.substring(matchTypeStr, 0, matchTypeStr.length() - 1);
        String propertyTypeCode = StringUtils.substring(matchTypeStr, matchTypeStr.length() - 1, matchTypeStr.length());
        this.propertyValue = value.split(QueryFilter.OR_SEPARATOR);
        try {
            matchType = MatchType.getValue(matchTypeCode);
            if (T.equalsIgnoreCase(propertyTypeCode) && matchTypeCode.indexOf(":") == -1 && value.indexOf(QueryFilter.OR_SEPARATOR)==-1) {
                value += " 00:00:00";
            }
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.");
        }

        try {
            propertyType = (Class) PropertyType.get(propertyTypeCode);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.");
        }
        
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        propertyNames = propertyNameStr.split(QueryFilter.OR_SEPARATOR);

        Assert.isTrue(propertyNames.length > 0, "filter名称" + filterName + "没有按规则编写,无法得到属性名称.");
        if (value.indexOf(QueryFilter.OR_SEPARATOR)==-1) {
            // 按entity property中的类型将字符串转化为实际类型.
            this.propertyValue = ConvertUtils.convert(value, propertyType);
        } else {
            this.propertyValue = value;
            setMultiValue(true);
        }
    }

    /**
     * 
     * @param filterName 要查询的属性
     * @param operation 比较操作符
     * @param value 值
     */
    public QueryFilter(final String filterName, final String operation, final String value) {
        this(MatchType.getKey(operation.toUpperCase()) + "S_" + filterName, value);
    }

    /**
     * 
     * @param filterName 要查询的属性
     * @param operation 比较操作符
     * @param value 值
     * @param type 值的类型
     */
    public QueryFilter(final String filterName, final String operation, final Object value, final String type) {
        this(MatchType.getKey(operation.toUpperCase()) + type + "_" + filterName, String.valueOf(value));
    }

    /**
     * 是否比较多个属性.
     */
    public boolean isMultiProperty() {
        return (propertyNames.length > 1);
    }

    /**
     * 获取比较属性名称列表.
     */
    public String[] getPropertyNames() {
        return propertyNames;
    }

    /**
     * 获取唯一的比较属性名称.
     */
    public String getPropertyName() {
        if (propertyNames.length > 1) {
            throw new IllegalArgumentException("There are not only one property");
        }
        return propertyNames[0];
    }

    /**
     * 获取比较值.
     */
    public Object getPropertyValue() {
        if(propertyValue!=null){
            if(String.class.isInstance(propertyValue)){
                return String.valueOf(propertyValue).trim();
            }
        }
        return propertyValue;
    }

    /**
     * 获取比较值的类型.
     */
    public Class getPropertyType() {
        return propertyType;
    }

    /**
     * 获取比较方式类型.
     */
    public Object getMatchType() {
        return this.matchType;
    }

    public boolean isMultiValue() {
        return isMultiValue;
    }

    public void setMultiValue(boolean isMultiValue) {
        this.isMultiValue = isMultiValue;
    }

    /**
     * 对查询的条件进行特殊处理
     * 
     * @param t
     * @param v
     * @return
     */
    public static Object getProtertyValue(Object t, Object v, Class proType) {

        // 如果是like 查询，则加上%
        if (t.equals(MatchType.LIKE)) {
//          return "%" + v + "%";
            return v;//由应用程序维护匹配规则
        }

        // 对日期字段进行特殊的处理
        // 对java.sql.Date和java.sql.Timestamp暂时都统一成java.sql.Timestamp处理
        String className = proType.getName();
        String dClassName = ((Class) PropertyType.get(D)).getName();
        String tClassName = ((Class) PropertyType.get(T)).getName();
        if (className.equals(dClassName) || className.equals(tClassName)) {
            if(String.class.isInstance(v)){
                return LenientTimestampEditor.parseLenient(null, (String)v);
            }
        }
        return v;
    }
}
