/**
 * $Id: StringUtil.java,v 1.2 2013/11/16 04:57:53 chenhua Exp $
 *
 * Copyright (c) 2007 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Studio V4 Project
 *
 */
package dataset.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataset.model.IEntity;


public class StringUtil {
    public static final String EMPTY_STRING_ARRAY[] = new String[0];
    
    public static boolean isNullOrBlank(Object obj) {
        if (obj == null)
            return true;
        if (obj.toString().trim().equals("")) //$NON-NLS-1$
            return true;
        return false;
    }
    
    public static String[] toArrayString(Object []objs){
        List list=new ArrayList();
        for(int i=0;i<objs.length;i++){
            list.add(objs[i].toString());
        }
        return (String[])list.toArray(new String[list.size()]);
    }
    
    private final static String _FromEncode_ = "GBK"; //$NON-NLS-1$
    private final static String _ToEncode_ = "GBK"; //$NON-NLS-1$
    private static int compare(String str1, String str2){
        int result = 0;
        String m_s1 = null;
        String m_s2 = null;
        try
        {
            m_s1 = new String(str1.getBytes(_FromEncode_), _ToEncode_);
            m_s2 = new String(str2.getBytes(_FromEncode_), _ToEncode_);
        }
        catch(Exception e)
        {
            return str1.compareTo(str2);
        }
        result = chineseCompareTo(m_s1, m_s2);
        return result;
    }

    private static int getCharCode(String s){
    	//soszou 重构 2013-09-24  && 修改为||
        if(s == null || s.equals(""))
            return -1;
        byte b[] = s.getBytes();
        int value = 0;
        for(int i = 0; i < b.length && i <= 2; i++)
            value = value * 100 + b[i];

        return value;
    }

    private static int chineseCompareTo(String s1, String s2){
        int len1 = s1.length();
        int len2 = s2.length();
        int n = Math.min(len1, len2);
        for(int i = 0; i < n; i++)
        {
            int s1_code = getCharCode(s1.charAt(i) + ""); //$NON-NLS-1$
            int s2_code = getCharCode(s2.charAt(i) + ""); //$NON-NLS-1$
            if(s1_code * s2_code < 0)
                return Math.min(s1_code, s2_code);
            if(s1_code != s2_code)
                return s1_code - s2_code;
        }

        return len1 - len2;
    }

    /**
     * 获得首字符
     * @param res
     * @return
     */
    public static String getBeginCharacter(String res){
        String a = res;
        String result = ""; //$NON-NLS-1$
        for(int i = 0; i < a.length(); i++)
        {
            String current = a.substring(i, i + 1);
            if(compare(current, "\u554A") < 0) //$NON-NLS-1$
                result = result + current;
            else
            if(compare(current, "\u554A") >= 0 && compare(current, "\u5EA7") <= 0) //$NON-NLS-1$ //$NON-NLS-2$
                if(compare(current, "\u531D") >= 0) //$NON-NLS-1$
                    result = result + "z"; //$NON-NLS-1$
                else
                if(compare(current, "\u538B") >= 0) //$NON-NLS-1$
                    result = result + "y"; //$NON-NLS-1$
                else
                if(compare(current, "\u6614") >= 0) //$NON-NLS-1$
                    result = result + "x"; //$NON-NLS-1$
                else
                if(compare(current, "\u6316") >= 0) //$NON-NLS-1$
                    result = result + "w"; //$NON-NLS-1$
                else
                if(compare(current, "\u584C") >= 0) //$NON-NLS-1$
                    result = result + "t"; //$NON-NLS-1$
                else
                if(compare(current, "\u6492") >= 0) //$NON-NLS-1$
                    result = result + "s"; //$NON-NLS-1$
                else
                if(compare(current, "\u7136") >= 0) //$NON-NLS-1$
                    result = result + "r"; //$NON-NLS-1$
                else
                if(compare(current, "\u671F") >= 0) //$NON-NLS-1$
                    result = result + "q"; //$NON-NLS-1$
                else
                if(compare(current, "\u556A") >= 0) //$NON-NLS-1$
                    result = result + "p"; //$NON-NLS-1$
                else
                if(compare(current, "\u54E6") >= 0) //$NON-NLS-1$
                    result = result + "o"; //$NON-NLS-1$
                else
                if(compare(current, "\u62FF") >= 0) //$NON-NLS-1$
                    result = result + "n"; //$NON-NLS-1$
                else
                if(compare(current, "\u5988") >= 0) //$NON-NLS-1$
                    result = result + "m"; //$NON-NLS-1$
                else
                if(compare(current, "\u5783") >= 0) //$NON-NLS-1$
                    result = result + "l"; //$NON-NLS-1$
                else
                if(compare(current, "\u5580") >= 0) //$NON-NLS-1$
                    result = result + "k"; //$NON-NLS-1$
                else
                if(compare(current, "\u51FB") > 0) //$NON-NLS-1$
                    result = result + "j"; //$NON-NLS-1$
                else
                if(compare(current, "\u54C8") >= 0) //$NON-NLS-1$
                    result = result + "h"; //$NON-NLS-1$
                else
                if(compare(current, "\u5676") >= 0) //$NON-NLS-1$
                    result = result + "g"; //$NON-NLS-1$
                else
                if(compare(current, "\u53D1") >= 0) //$NON-NLS-1$
                    result = result + "f"; //$NON-NLS-1$
                else
                if(compare(current, "\u86FE") >= 0) //$NON-NLS-1$
                    result = result + "e"; //$NON-NLS-1$
                else
                if(compare(current, "\u642D") >= 0) //$NON-NLS-1$
                    result = result + "d"; //$NON-NLS-1$
                else
                if(compare(current, "\u64E6") >= 0) //$NON-NLS-1$
                    result = result + "c"; //$NON-NLS-1$
                else
                if(compare(current, "\u82AD") >= 0) //$NON-NLS-1$
                    result = result + "b"; //$NON-NLS-1$
                else
                if(compare(current, "\u554A") >= 0) //$NON-NLS-1$
                    result = result + "a"; //$NON-NLS-1$
        }

        return result;
    }
    
    /*
     * 使用分隔符将字符串转化成字符串数组
     */
    public static String[] splitShortString(String str, char separator) {
        int len;
        if (str == null || (len = str.length()) == 0)
            return EMPTY_STRING_ARRAY;
        int lastTokenIndex = 0;
        for (int pos = str.indexOf(separator); pos >= 0; pos = str.indexOf(
                separator, pos + 1))
            lastTokenIndex++;

        String list[] = new String[lastTokenIndex + 1];
        int oldPos = 0;
        int pos = str.indexOf(separator);
        int i = 0;
        for (; pos >= 0; pos = str.indexOf(separator, oldPos = pos + 1))
            list[i++] = substring(str, oldPos, pos);

        list[lastTokenIndex] = substring(str, oldPos, len);
        return list;
    }

    /**
     * @param str
     * @param begin
     * @param end
     * @return
     */
    public static String substring(String str, int begin, int end) {
        if (begin == end)
            return ""; //$NON-NLS-1$
        else
            return str.substring(begin, end);
    }

    /**
     * 字符串数组trim
     * @param strings
     * @return
     */
    public static String[] trim(String strings[]) {
        if (strings == null)
            return null;
        int i = 0;
        for (int len = strings.length; i < len; i++)
            strings[i] = strings[i].trim();

        return strings;
    }
    
    /**
     * 格式化以下划线'_'或减号'-'隔开的字符串，如：
     * "user-name", 可以格式化为"userName"
     * @return java.lang.String
     * @param name java.lang.String
     * @param firstCharUpperCase boolean 如果为true，那么返回的首字母大写，反之，小写
     */
    public static final String formatJavaName(String name, boolean firstCharUpperCase) {
        if (name == null || name.length() <= 1)
            return name;
        StringTokenizer tokenizer = new StringTokenizer(name, "-_"); //$NON-NLS-1$
        StringBuffer tmp = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            tmp.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1));
        }
        if (!firstCharUpperCase) {
            String ch = String.valueOf(Character.toLowerCase(tmp.charAt(0)));
            tmp.replace(0, 1, ch);
        }
        return tmp.toString();
    }
    
    public static final String getGetMethodName(String name) {
        return "get" + formatJavaName(name, true); //$NON-NLS-1$
    }
    
    public static final String getSetMethodName(String name) {
        return "set" + formatJavaName(name, true); //$NON-NLS-1$
    }
    
    public static String replace(String src, String mod, String str) {
        if (src == null || src.length() == 0) {
            return src;
        }
        if (mod == null || mod.length() == 0) {
            return src;
        }
        if (src == null) {
            src = ""; //$NON-NLS-1$
        }
        StringBuffer buffer = new StringBuffer();
        int idx1 = 0;
        int idx2 = 0;
        while ((idx2 = src.indexOf(mod, idx1)) != -1) {
            buffer.append(src.substring(idx1, idx2)).append(str);
            idx1 = idx2 + mod.length();
        }
        buffer.append(src.substring(idx1));
        return buffer.toString();
    }
    
    /** 
     * Replaces all occurances of oldString in mainString with newString
     * @param mainString The original string
     * @param oldString The string to replace
     * @param newString The string to insert in place of the old
     * @return mainString with all occurances of oldString replaced by newString
     */
    public static String replaceString(String mainString, String oldString, String newString) {
        if (mainString == null) {
            return null;
        }
        if (oldString == null || oldString.length() == 0) {
            return mainString;
        }
        if (newString == null) {
            newString = ""; //$NON-NLS-1$
        }

        int i = mainString.lastIndexOf(oldString);

        if (i < 0) return mainString;

        StringBuffer mainSb = new StringBuffer(mainString);

        while (i >= 0) {
            mainSb.replace(i, i + oldString.length(), newString);
            i = mainString.lastIndexOf(oldString, i - 1);
        }
        return mainSb.toString();
    }

    /**
     * Creates a single string from a List of strings seperated by a delimiter.
     * @param list a list of strings to join
     * @param delim the delimiter character(s) to use. (null value will join with no delimiter)
     * @return a String of all values in the list seperated by the delimiter
     */
    public static String join(List list, String delim) {
        if (list == null || list.size() < 1)
            return null;
        StringBuffer buf = new StringBuffer();
        Iterator i = list.iterator();

        while (i.hasNext()) {
            buf.append((String) i.next());
            if (i.hasNext())
                buf.append(delim);
        }
        return buf.toString();
    }

    /**
     * Splits a String on a delimiter into a List of Strings.
     * @param str the String to split
     * @param delim the delimiter character(s) to join on (null will split on whitespace)
     * @return a list of Strings
     */
    public static List split(String str, String delim) {
        List splitList = null;
        StringTokenizer st = null;

        if (str == null)
            return splitList;

        if (delim != null)
            st = new StringTokenizer(str, delim);
        else
            st = new StringTokenizer(str);

        if (st != null && st.hasMoreTokens()) {
            splitList = new ArrayList();

            while (st.hasMoreTokens())
                splitList.add(st.nextToken());
        }
        return splitList;
    }

    /**
     * Encloses each of a List of Strings in quotes.
     * @param list List of String(s) to quote.
     */
    public static List quoteStrList(List list) {
        List tmpList = list;

        list = new ArrayList();
        Iterator i = tmpList.iterator();

        while (i.hasNext()) {
            String str = (String) i.next();

            str = "'" + str + "''"; //$NON-NLS-1$ //$NON-NLS-2$
            list.add(str);
        }
        return list;
    }

    /**
     * Creates a Map from an encoded name/value pair string
     * @param str The string to decode and format
     * @param trim Trim whitespace off fields
     * @return a Map of name/value pairs
     */
//    public static Map strToMap(String str, boolean trim) {
//        if (str == null) return null;
//        Map decodedMap = new HashMap();
//        List elements = split(str, "|"); //$NON-NLS-1$
//        Iterator i = elements.iterator();
//
//        while (i.hasNext()) {
//            String s = (String) i.next();
//            List e = split(s, "="); //$NON-NLS-1$
//
//            if (e.size() != 2) {
//                continue;
//            }
//            String name = (String) e.get(0);
//            String value = (String) e.get(1);
//            if (trim) {
//                if (name != null) {
//                    name = name.trim();
//                }
//                if (value != null) {
//                    value = value.trim();
//                }
//            }
//
//            try {
//                decodedMap.put(URLDecoder.decode(name, "UTF-8"), URLDecoder.decode(value, "UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
//            } catch (UnsupportedEncodingException e1) {                
//                ICSSCore.getCore().error(e1);
//            }
//        }
//        return decodedMap;
//    }

    /**
     * Creates a Map from an encoded name/value pair string
     * @param str The string to decode and format
     * @return a Map of name/value pairs
     */
//    public static Map strToMap(String str) {
//        return strToMap(str, false);
//    }

    /**
     * Creates an encoded String from a Map of name/value pairs (MUST BE STRINGS!)
     * @param map The Map of name/value pairs
     * @return String The encoded String
     */
//    public static String mapToStr(Map map) {
//        if (map == null) return null;
//        StringBuffer buf = new StringBuffer();
//        Set keySet = map.keySet();
//        Iterator i = keySet.iterator();
//        boolean first = true;
//
//        while (i.hasNext()) {
//            Object key = i.next();
//            Object value = map.get(key);
//
//            if (!(key instanceof String) || !(value instanceof String))
//                continue;
//            String encodedName = null;
//            try {
//                encodedName = URLEncoder.encode((String) key, "UTF-8"); //$NON-NLS-1$
//            } catch (UnsupportedEncodingException e) {
//                ICSSCore.getCore().error(e);              
//            }
//            String encodedValue = null;
//            try {
//                encodedValue = URLEncoder.encode((String) value, "UTF-8"); //$NON-NLS-1$
//            } catch (UnsupportedEncodingException e) {
//                ICSSCore.getCore().error(e);                
//            }
//            
//            if (first)
//                first = false;
//            else
//                buf.append("|"); //$NON-NLS-1$
//
//            buf.append(encodedName);
//            buf.append("="); //$NON-NLS-1$
//            buf.append(encodedValue);
//        }
//        return buf.toString();
//    }

    /**
     * Create a Map from a List of keys and a List of values
     * @param keys List of keys
     * @param values List of values
     * @return Map of combined lists
     * @throws IllegalArgumentException When either List is null or the sizes do not equal
     */
    public static Map createMap(List keys, List values) {
        if (keys == null || values == null || keys.size() != values.size()) {
            throw new IllegalArgumentException("Keys and Values cannot be null and must be the same size"); //$NON-NLS-1$
        }
        Map newMap = new HashMap();
        for (int i = 0; i < keys.size(); i++) {
            newMap.put(keys.get(i), values.get(i));
        }
        return newMap;
    }

    /**
     * 流程名称命名规范
     * 
     * @param str
     * @param ends
     * @return
     */
    public static boolean validateString(String str, String ends) {
        boolean bl = true;
        if (str != null && !str.trim().equals("")) { //$NON-NLS-1$
            if (str.indexOf("/") >= 0) //$NON-NLS-1$
                bl = false;
            else if (str.indexOf("\\") >= 0) //$NON-NLS-1$
                bl = false;
            else if (!str.toLowerCase().endsWith(ends))
                bl = false;
        } else {
            bl = false;
        }
        return bl;
    }
    
    /** Make sure the string starts with a forward slash but does not end with one; converts back-slashes to forward-slashes; if in String is null or empty, returns zero length string. */
    public static String cleanUpPathPrefix(String prefix) {
        if (prefix == null || prefix.length() == 0) return ""; //$NON-NLS-1$

        StringBuffer cppBuff = new StringBuffer(prefix.replace('\\', '/'));

        if (cppBuff.charAt(0) != '/') {
            cppBuff.insert(0, '/');
        }
        if (cppBuff.charAt(cppBuff.length() - 1) == '/') {
            cppBuff.deleteCharAt(cppBuff.length() - 1);
        }
        return cppBuff.toString();
    }
    
    /** Removes all spaces from a string */
    public static String removeSpaces(String str) {
        StringBuffer newString = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ')
                newString.append(str.charAt(i));
        }
        return newString.toString();        
    }

    public static String toHexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            buf.append(hexChar[(bytes[i] & 0xf0) >>> 4]);
            buf.append(hexChar[bytes[i] & 0x0f]);
        }
        return buf.toString();

    }

    public static String cleanHexString(String str) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != (int) 32 && str.charAt(i) != ':') {
                buf.append(str.charAt(i));
            }
        }
        return buf.toString();
    }

    public static byte[] fromHexString(String str) {
        str = cleanHexString(str);
        int stringLength = str.length();
        if ((stringLength & 0x1) != 0) {
            throw new IllegalArgumentException("出现异常"); //$NON-NLS-1$
        }
        byte[] b = new byte[stringLength / 2];

        for (int i = 0, j = 0; i < stringLength; i+= 2, j++) {
            int high= convertChar(str.charAt(i));
            int low = convertChar(str.charAt(i+1));
            b[j] = (byte) ((high << 4) | low);
        }
        return b;
    }

    private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static int convertChar(char c) {
        if ( '0' <= c && c <= '9' ) {
            return c - '0' ;
        } else if ( 'a' <= c && c <= 'f' ) {
            return c - 'a' + 0xa ;
        } else if ( 'A' <= c && c <= 'F' ) {
            return c - 'A' + 0xa ;
        } else {
            throw new IllegalArgumentException("Invalid蔴ex蔯haracter:?" + c); //$NON-NLS-1$
        }
    }
    
    /**
     * 匹配XML语法
     * @param value
     * @return
     */
    public static boolean matchXMLSyntax(String value){
        Pattern a=Pattern.compile("(^[a-zA-Z_\u4e00-\u9fa5][a-zA-Z0-9_\u4e00-\u9fa5]+$)|[a-zA-Z_\u4e00-\u9fa5]"); //$NON-NLS-1$
        Matcher b=a.matcher(value); 
        return b.matches();
    }
    
    /**
     * 去后缀
     * @param value
     * @param extendName
     * @return
     */
    public static String getReduceExtendName(String value,String extendName){
        String name = null;
        if(value.endsWith(extendName)){
            int length = value.length();
            int extendLengh = extendName.length();
            name = value.substring(0,length-extendLengh);
        }
        return name;
    }
    /**
     * 加后缀
     * @param value
     * @param extendName
     * @return
     */
    public static String getAddExtendName(String value,String extendName){
        String name = value.concat(extendName);
        return name;
    }
    
    /**
     * 判断是否含有中文
     * @param str
     * @return
     */
    public static boolean hasChinese(String str){
        if(isNullOrBlank(str))
            return false;
        String regEx = "[\u4e00-\u9fa5]"; //$NON-NLS-1$
        Pattern p=Pattern.compile(regEx);
        Matcher m=p.matcher(str);
        return m.find();
    }
    
    /**
     * 把str内容中的"&"、"<"、">"转换为可以正常存储的转义字符
     * @param str
     * @return string 转换后内容
     */
    public static String transformString(String str){
        str = replaceString(str,"&", "&amp;");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"<", "&lt;");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,">", "&gt;");//$NON-NLS-1$ //$NON-NLS-2$
        return str;
    }
    /**
     * 还原str内容中被转义的字符
     * @param str
     * @return string 还原后内容
     */
    public static String reTransformString(String str){
        str = replaceString(str,"&gt;", ">");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"&lt;", "<");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"&amp;", "&");//$NON-NLS-1$ //$NON-NLS-2$
        return str;
    }
    
    /**
     * 把str内容中的"&"、"<"、">"、"\t"、"\r"、"\n"转换为可以正常存储的转义字符
     * @param str
     * @return string 转换后内容
     */
    public static String transformString2(String str){
        if(str==null||str.equals("null")) //$NON-NLS-1$
            return ""; //$NON-NLS-1$
        str = replaceString(str,"&", "&amp;");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"<", "&lt;");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,">", "&gt;");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"\t", "&#x0009;");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"\r", "&#x000D;");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"\n", "&#x000A;");//$NON-NLS-1$ //$NON-NLS-2$
        return str;
    }
    /**
     * 还原str内容中被转义的字符
     * 包括"&"、"<"、">"、"\t"、"\r"、"\n"
     * @param str
     * @return string 还原后内容
     */
    public static String reTransformString2(String str){
        if(str==null||str.equals("null")) //$NON-NLS-1$
            return ""; //$NON-NLS-1$
        str = replaceString(str,"&gt;", ">");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"&lt;", "<");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"&amp;", "&");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"&#x0009;", "\t");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"&#x000D;", "\r");//$NON-NLS-1$ //$NON-NLS-2$
        str = replaceString(str,"&#x000A;", "\n");//$NON-NLS-1$ //$NON-NLS-2$
        return str;
    }
    
    /**
     * 是否为浮点数(不考虑正负)
     * @param str
     * @return
     * 注:【0-9 为 0x0030-0x0039】【.为0x002e】 
     */
    public static boolean isValidFloat(String str){
        char c;
        int pointNum = 0;
        int position;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c == 0x002e) { // 是否为小数点
                if (pointNum == 0) { // 是否只有一个小数点
                    pointNum++;
                } else {
                    return false; // 超过一个小数点时出错
                }
            }else if(!(c <= 0x0039 && c >= 0x0030)){
                return false;
            }
        }
        position = str.indexOf("."); //$NON-NLS-1$
        if (position == -1)
            return true;
        if (position == 0 || position == str.length() - 1)
            return false; // 小数点位置在开头和末尾时出错
        // if(position != 3) return false; //小数点两位出错
        return true;
    }
    
    /**
     * 判断输入字符串是否是纯数字
     * @param String
     * @return
     */
    public static boolean isDigit(String str){
        if (str == null) {
            return false;
        }
        char tChar;
        for (int i = 0; i < str.length(); i++) {
            tChar = str.charAt(i);
            if (!Character.isDigit(tChar) ){
                return false;
            }
        }
        return true;
    }
    /**
     * 判断输入字符是否是纯字母
     * @author chenhua
     * 2010-8-24
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
        if (str == null) {
            return false;
        }
        char tChar;
        for (int i = 0; i < str.length(); i++) {
            tChar = str.charAt(i);
            if (!Character.isLetter(tChar)) {
                return false;
            }
        }
        String regEx = "[\u4e00-\u9fa5]";//$NON-NLS-1$
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return false;
        }
        return true;
    }
    /**
     * 判断输入字符串是否是纯数字(考虑首字符为0)
     * @param String
     * @return
     */
    public static boolean isDigitFirstNot0(String str){
        if (str == null) {
            return false;
        }
        char[] ch = str.toCharArray();
        if (ch.length > 1 && ch[0] == '0') {
            return false;
        }
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] < 0x0030 || ch[i] > 0x0039) {
                return false;
            }
        }
        return true;
    }
    
    public static  String reduceExtendName(String entityName){
        if(entityName!=null){
            String str = entityName;
            int i =entityName.lastIndexOf("."); //$NON-NLS-1$
            if(i!=-1){
                str = entityName.substring(0, i);
            }
            return str;
        }
        return ""; //$NON-NLS-1$
    }
    
    /**
     * 根据字符串首字符排序,忽略大小写,同时改变sortList 和 linkList中的顺序
     * <br>使用范围：sortList内为String类型，sortList与linkList为一一对应关系
     * <br>2009-10-10 add by liyang
     * @param sortList
     * @param linkList
     */
//    public static void sortByLetter(List<String> sortList,List linkList){
//      if (sortList == null || linkList == null
//              || sortList.size() != linkList.size()) {
//          return;
//      }
//      for (int i = 0; i < sortList.size(); i++) {
//          int popIndex = i;
//          char ch1 = ((String) sortList.get(i)).charAt(0);
//          if (ch1 >= 'a' && ch1 <= 'z') {
//              ch1 -= 0x20;
//          }
//          for (int j = i + 1; j < sortList.size(); j++) {
//              char ch2 = ((String) sortList.get(j)).charAt(0);
//              if (ch2 >= 'a' && ch2 <= 'z') {
//                  ch2 -= 0x20;
//              }
//              if (ch1 > ch2) {
//                  ch1 = ch2;
//                  popIndex = j;
//              }
//          }
//          if(i<popIndex){
//              String temp = sortList.get(i);
//              sortList.set(i, sortList.get(popIndex));
//              sortList.set(popIndex, temp);
//              
//              Object ob = linkList.get(i);
//              linkList.set(i, linkList.get(popIndex));
//              linkList.set(popIndex, ob);
//          }
//      }
//    }
    
    public static void main(String args[]){
//      String after=StringUtil.getBeginCharacter("公文系统李洋哈哈的发生的发生的法十分的积分开发时刻的分手快");
//      System.out.println(after.toUpperCase());
        
  //      String sss = "FILE-INFO"; //$NON-NLS-1$
  //      System.out.println(formatJavaName(sss,true));
    }
    
    /**
     * 判断是否含有中文字符，如【】，。；：“（）……等
     * @param str
     * @return
     * add by zangll 2009-11-27
     */
    public static boolean hasChineseChar(String str){
        char[] chars = str.toCharArray();
        boolean isChineseChar = false;
        for (int i = 0; i < chars.length; i++) {
         char ch = chars[i];
         byte[] bytes = ("" + ch).getBytes(); //$NON-NLS-1$
          if (bytes.length == 2 && !StringUtil.hasChinese(ch+"")) { //$NON-NLS-1$
              isChineseChar = true;
              break;            
          }
        }
        return isChineseChar;
    }
    

    /**
     * <p>
     * 将首字母转换为大写
     * </p>
     * 
     * <pre>
     * StringUtil.upperFirst(&quot;name&quot;) =&gt; Name
     * </pre>
     * 
     * @param name
     * @return
     */
    public static final String upperFirst(String name) {
        return formatJavaName(name, true);
    }
    
    /**
     * 将字符串转换为boolean值
     *  
     * @param s
     * @return  如果字符串为"true"，这返回ture，否则返回false
     */
    public static boolean toBoolean(String s) {
        return s != null && s.equalsIgnoreCase("true");
    }
    /**
     * 根据ReferenceFilePath获取数据集名称
     * @param DatasetPath
     * @return
     */
    public static String getDatasetNameFormPath(String DatasetPath){
    	String[] strs=DatasetPath.split("\\\\");
    	String datasetName =strs[strs.length-1];
    	return datasetName.replace(".dsxml", "");
    }
    /**
     * 根据ReferenceFilePath获取数据集文件名
     * @param DatasetPath
     * @return
     */
    public static String getDatasetFileNameFormPath(String DatasetPath){
    	String[] strs=DatasetPath.split("\\\\");
    	return strs[strs.length-1];
    }
    /**
     * 根据ReferenceFilePath获取工程路径带datamodel
     * @param DatasetPath
     * @return
     */
    public static String getProjectPathFormPath(String DatasetPath){
    	if(isNullOrBlank(DatasetPath)){
    		return "";
    	}else{
    		int last = DatasetPath.lastIndexOf("\\");
        	return DatasetPath.substring(0, last+1);
    	}
    }
    
}
