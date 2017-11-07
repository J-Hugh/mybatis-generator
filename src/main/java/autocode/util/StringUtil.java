/*
 * $Id: StringUtil.java,v 1.3 2013/11/16 04:57:54 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package autocode.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import dataset.model.IEntity;

/**
 * <p>
 * 字符串工具类
 * </p>
 * 
 * @author ganjp
 * @version 1.0
 * @since 4.3
 */
public class StringUtil {

    public StringUtil() {
        super();
    }

    /**
     * <p>
     * 格式化字符串
     * </p>
     * 
     * <pre>
     * 	String str = "Hello %s,welcome to %s";
     * 	format(str,new Object[]{"Tom","China"});
     * =>Hello Tom,welcome to China
     * </pre>
     * 
     * @param str
     * @param args
     * @return
     */
    public static String format(String str, String[] args) {
        if (args == null)
            return str;

        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                if (str.indexOf("%s") != -1) {
                    str = str.replaceFirst("\\%s", args[i]);
                }
            }
        }
        return str;
    }

    /**
     * 格式化字符串
     * 
     * <pre>
     * 	String str = "Hello %s";
     * 	format(str,"world");
     * 	=>Hello world
     * </pre>
     * 
     * @param str
     * @param arg
     * @return
     */
    public static String format(String str, String arg) {
        return format(str, new String[] { arg });
    }

    /**
     * 格式化字符串
     * 
     * @param str
     * @param arg1
     * @param arg2
     * @return
     */
    public static String format(String str, String arg1, String arg2) {
        return format(str, new String[] { arg1, arg2 });
    }

    /**
     * 格式化字符串
     * 
     * @param str
     * @param arg1
     * @param arg2
     * @param arg3
     * @return
     */
    public static String format(String str, String arg1, String arg2, String arg3) {
        return format(str, new String[] { arg1, arg2, arg3 });
    }

    /**
     * <p>
     * 格式化字符串
     * </p>
     * 
     * <pre>
     * 	String str = "Hello %s1,welcome to %s2,%s1";
     * 	formatWithNumber(str,"Tom","China");
     * =>Hello Tom,welcome to China,Tom
     * </pre>
     * 
     * @param str
     * @param args
     * @return
     */
    public static String formatWithNumber(String str, String s1, String s2) {
        if (isNotBlank(s1)) {
            str = str.replaceAll("\\%s1", s1);
        }
        if (isNotBlank(s1)) {
            str = str.replaceAll("\\%s2", s2);
        }
        return str;
    }

    /**
     * <p>
     * 判断字符串是否为null或者""
     * </p>
     * <p/>
     * 
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty(&quot;&quot;)        = true
     * StringUtils.isEmpty(&quot; &quot;)       = false
     * StringUtils.isEmpty(&quot;bob&quot;)     = false
     * StringUtils.isEmpty(&quot;  bob  &quot;) = false
     * </pre>
     * <p/>
     * <p>
     * 注意：该方法不会去掉字符串的空格
     * </p>
     * 
     * @param str
     *            要判断的字符串
     * @return 如果字符串为null或者""，返回<code>true</code>
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>
     * 判断字符串不为空或者null
     * </p>
     * <p/>
     * 
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty(&quot;&quot;)        = false
     * StringUtils.isNotEmpty(&quot; &quot;)       = true
     * StringUtils.isNotEmpty(&quot;bob&quot;)     = true
     * StringUtils.isNotEmpty(&quot;  bob  &quot;) = true
     * </pre>
     * <p/>
     * <p>
     * 注意：该方法不会去掉字符串的空格
     * </p>
     * 
     * @param str
     *            要判断的字符串
     * @return 如果字符串不为null或者""，返回<code>true</code>
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }

    /**
     * <p>
     * 判断字符串是否为null或者""，首先trim然后再进行判断
     * </p>
     * 
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank(&quot;&quot;)        = true
     * StringUtils.isBlank(&quot; &quot;)       = true
     * StringUtils.isBlank(&quot;bob&quot;)     = false
     * StringUtils.isBlank(&quot;  bob  &quot;) = false
     * </pre>
     * 
     * @param str
     *            要判断的字符串
     * @return 如果字符串为null或者""，返回<code>true</code>
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定的str是否为null，如果为null返回defalutStr
     * 
     * @param str
     * @param defalutStr
     * @return
     */
    public static String blankStr(String str, String defalutStr) {
        if (defalutStr == null)
            defalutStr = "";

        if (str == null)
            return defalutStr;

        return str.trim();
    }

    /**
     * 判断给定的str是否为null，如果为null返回""
     * 
     * @param str
     * @return
     */
    public static String blankStr(String str) {
        return blankStr(str, null);
    }

    /**
     * <p>
     * 判断字符串是不为null或者""，首先trim然后再进行判断
     * </p>
     * <p/>
     * 
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank(&quot;&quot;)        = false
     * StringUtils.isNotBlank(&quot; &quot;)       = false
     * StringUtils.isNotBlank(&quot;bob&quot;)     = true
     * StringUtils.isNotBlank(&quot;  bob  &quot;) = true
     * </pre>
     * 
     * @param str
     *            要判断的字符串
     * @return 如果字符串不为null或者""，返回<code>true</code>
     * @since 2.0
     */
    public static boolean isNotBlank(String str) {
        return !StringUtil.isBlank(str);
    }

    /**
     * <p>
     * 转义数据库通配字符：'%','_'
     * </p>
     * <p/>
     * 
     * <pre>
     * 	convertCastChar(&quot;abc%bcd_&quot;) =&gt; abc\%bcd\_
     * </pre>
     * 
     * @param src
     *            要转义的字符串
     * @return 转义后的字符串
     */
    public static String convertCastChar(String src) {
        if (src == null || src.equals("")) {
            return src;
        }
        int length = src.length();
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < length; i++) {
            switch (src.charAt(i)) {
                case '%':
                case '_':
                case '\\':
                    tmp.append("\\");
                    break;
            }
            tmp.append(src.charAt(i));
        }
        return tmp.toString();
    }

    /**
     * <p>
     * 转换常见的Html符号
     * </p>
     * <p/>
     * 
     * <pre>
     * 	convertForHtml(&quot;&lt;input type='text'&gt;&quot;) =&gt; &lt;input?type='text'&gt;
     * </pre>
     * 
     * @param src
     *            要转义的字符串
     * @return 转义后的字符串
     */
    public static String convertForHtml(String src) {
        if (src == null || src.equals("")) {
            return src;
        }
        int length = src.length();
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < length; i++) {
            switch (src.charAt(i)) {
                case '<':
                    tmp.append("&lt;");
                    break;
                case '>':
                    tmp.append("&gt;");
                    break;
                case '"':
                    tmp.append("&quot;");
                    break;
                case ' ': {
                    int spaceCount = 0;
                    for (; src.charAt(i) == ' '; i++, spaceCount++)
                        ;
                    for (int j = 0; j < spaceCount / 2; j++) {
                        tmp.append("　");
                    }
                    if (spaceCount % 2 != 0) {
                        tmp.append("&#160;");
                    }
                    --i;
                    break;
                }
                case '\n':
                    tmp.append("<br/>");
                    break;
                case '&':
                    tmp.append("&amp;");
                    break;
                case '\r':
                    break;
                default:
                    tmp.append(src.charAt(i));
                    break;
            }
        }
        return tmp.toString();
    }

    /**
     * <pre>
     * convertForXml(&quot;&lt;name type='a' /&gt;&quot;) =&gt; &lt;name?type='a'?/&gt;
     * </pre>
     * 
     * @param src
     *            要转义的字符串
     * @return 转义后的字符串
     */
    public static String convertForXml(String src) {
        if (src == null || src.equals("")) {
            return src;
        }
        int length = src.length();
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < length; i++) {
            switch (src.charAt(i)) {
                case '<':
                    tmp.append("&lt;");
                    break;
                case '>':
                    tmp.append("&gt;");
                    break;
                case '"':
                    tmp.append("&quot;");
                    break;
                case ' ': {
                    int spaceCount = 0;
                    for (; src.charAt(i) == ' '; i++, spaceCount++)
                        ;
                    for (int j = 0; j < spaceCount / 2; j++) {
                        tmp.append("　");
                    }
                    if (spaceCount % 2 != 0) {
                        tmp.append("&#160;");
                    }
                    --i;
                    break;
                }
                case '&':
                    tmp.append("&amp;");
                    break;
                case '\r':
                    break;
                default:
                    tmp.append(src.charAt(i));
                    break;
            }
        }
        return tmp.toString();
    }

    /**
     * <p>
     * 转义单引号
     * </p>
     * <p/>
     * 
     * <pre>
     * convertSingleQuot(&quot;insert into table ('abc')&quot;) =&gt; insert into table (''abc'')
     * </pre>
     * 
     * @param src
     *            要转义的字符串
     * @return 转义后的字符串
     */
    public static String convertSingleQuot(String src) {
        if (src == null || src.equals("")) {
            return src;
        }
        int length = src.length();
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (src.charAt(i) == '\'') {
                tmp.append("\'\'");
            } else {
                tmp.append(src.charAt(i));
            }
        }
        return tmp.toString();
    }

    /**
     * <p>
     * 字符串替换 Creation date: (2000-12-23 15:32:25)
     * </p>
     * <p/>
     * 
     * <pre>
     * 	String str = &quot;abc_d&quot;;
     * str = replace(str,&quot;_&quot;,&quot;#&quot;); =&gt; abc#d
     * </pre>
     * 
     * @param src
     *            源字符串
     * @param mod
     *            替换目标
     * @param str
     *            替换字符串
     * @return 替换后的字符串
     */
    public static String replace(String src, String mod, String str) {
        if (src == null || src.length() == 0) {
            return src;
        }
        if (mod == null || mod.length() == 0) {
            return src;
        }
        if (src == null) {
            src = "";
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
     * <p>
     * 字符串替换
     * </p>
     * <p/>
     * 
     * <pre>
     *  int[] count = new int[1];
     * 	String str = &quot;abc_defee&quot;;
     * StringUtil.replace(str,&quot;e&quot;,&quot;#&quot;,count); =&gt; abc_d#f##
     *  System.out.println(count[0]) =&gt; 3
     * </pre>
     * 
     * @param src
     *            源字符串
     * @param mod
     *            替换目标
     * @param str
     *            替换字符串
     * @param count
     *            替换个数
     * @return 替换后的字符串
     */
    public static final String replace(String line, String oldString, String newString, int count[]) {
        if (line == null)
            return null;
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            int counter = 0;
            counter++;
            char line2[] = line.toCharArray();
            char newString2[] = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j;
            for (j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
            }

            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        } else {
            return line;
        }
    }

    /**
     * <p>
     * 字符串替换，忽略大小写
     * </p>
     * <p/>
     * 
     * <pre>
     * 	String str = &quot;abc_def&quot;;
     * StringUtil.replaceIgnoreCase(str,&quot;E&quot;,&quot;#&quot;); =&gt; abc_d#f
     * </pre>
     * 
     * @param line
     *            源字符串
     * @param oldString
     *            替换目标
     * @param newString
     *            替换字符串
     * @return 替换后的字符串
     */
    public static final String replaceIgnoreCase(String line, String oldString, String newString) {
        if (line == null)
            return null;
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            char line2[] = line.toCharArray();
            char newString2[] = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j;
            for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
            }

            buf.append(line2, j, line2.length - j);
            return buf.toString();
        } else {
            return line;
        }
    }

    /**
     * <p>
     * 字符串替换，忽略大小写,并记录替换的个数
     * </p>
     * <p/>
     * 
     * <pre>
     *  int[] count = new int[1];
     * 	String str = &quot;abc_defee&quot;;
     * StringUtil.replaceIgnoreCase(str,&quot;E&quot;,&quot;#&quot;,count); =&gt; abc_d#f##
     *  System.out.println(count[0]) =&gt; 3
     * </pre>
     * 
     * @param line
     *            源字符串
     * @param oldString
     *            替换目标
     * @param newString
     *            替换字符串
     * @param count
     *            替换个数
     * @return 替换后的字符串
     */
    public static final String replaceIgnoreCase(String line, String oldString, String newString,
            int count[]) {
        if (line == null)
            return null;
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            int counter = 0;
            char line2[] = line.toCharArray();
            char newString2[] = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j;
            for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
            }

            buf.append(line2, j, line2.length - j);
            count[0] = counter + 1;
            return buf.toString();
        } else {
            return line;
        }
    }

    /**
     * <p>
     * 字符串替换，只替换第一个匹配
     * </p>
     * <p/>
     * 
     * <pre>
     * 	String str = &quot;abc_defee&quot;;
     * StringUtil.replaceOnce(str,&quot;e&quot;,&quot;#&quot;); =&gt; abc_d#fee
     * </pre>
     * 
     * @param template
     * @param placeholder
     * @param replacement
     * @return
     */
    public static String replaceOnce(String template, String placeholder, String replacement) {
        int loc = template.indexOf(placeholder);
        if (loc < 0) {
            return template;
        } else {
            return new StringBuffer(template.substring(0, loc)).append(replacement)
                    .append(template.substring(loc + placeholder.length())).toString();
        }
    }
    /**
     * 如果是在jdk1.4下使用，建议直接使用java.lang.String.replaceAll
     * 
     * @param src
     *            ：要replaceAll的字符串
     * @param regex
     * @param replacement
     * @return add by zhaoqy 2009-11-23
     * @deprecated
     */
    public static String replaceAll(String src, String regex, String replacement) {
        // RE r = new RE(regex);
        // if (regex.indexOf("\\s*") != -1) {
        // return src;
        // }
        // String temp = src;
        // while (r.match(temp)) {
        // String expr = r.getParen(0);
        // src = StringUtil.replaceOnce(src, expr, replacement);
        // int end = r.getParenEnd(0);
        // temp = temp.substring(end, temp.length());
        // }
        // return src;
        if (src != null)
            return src.replaceAll(regex, replacement);
        return src;
    }

    /**
     * 如果是在jdk1.4下使用，建议直接使用java.lang.String.replaceAll
     * 
     * @param src
     *            ：要replaceAll的字符串
     * @param regex
     * @param replacement
     * @param model
     * @return add by zhaoqy 2009-11-23
     * @deprecated
     */
    public static String replaceAll(String src, String regex, String replacement, int model) {
        // RE r = new RE(regex, model);
        // if (regex.indexOf("\\s*") != -1) {
        // return src;
        // }
        // String temp = src;
        // while (r.match(temp)) {
        // String expr = r.getParen(0);
        // src = StringUtil.replaceOnce(src, expr, replacement);
        // int end = r.getParenEnd(0);
        // temp = temp.substring(end, temp.length());
        // }
        // return src;
        if (src != null)
            return src.replaceAll(regex, replacement);
        return src;
    }

    /**
     * <p>
     * 对html中的>和<进行替换
     * </p>
     * <p/>
     * 
     * <pre>
     * 	String str = &quot;&lt;input type='text' value='d' /&gt;&quot;;
     * 		StringUtil.escapeHTMLTags(str); =&gt; &lt;input type='text' value='d' /&gt;
     * </pre>
     * 
     * @param in
     * @return
     */
    public static final String escapeHTMLTags(String in) {
        if (in == null)
            return null;
        int i = 0;
        int last = 0;
        char input[] = in.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
        for (; i < len; i++) {
            char ch = input[i];
            if (ch <= '>')
                if (ch == '<') {
                    if (i > last)
                        out.append(input, last, i - last);
                    last = i + 1;
                    out.append(LT_ENCODE);
                } else if (ch == '>') {
                    if (i > last)
                        out.append(input, last, i - last);
                    last = i + 1;
                    out.append(GT_ENCODE);
                }
        }

        if (last == 0)
            return in;
        if (i > last)
            out.append(input, last, i - last);
        return out.toString();
    }

    /**
     * <p>
     * 将null转换为""
     * </p>
     * 
     * @param str
     * @return
     */
    public static final String escapeNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * <p>
     * 替换>为&lt;
     * </p>
     * 
     * @param string
     * @return
     */
    public static final String escapeForXML(String string) {
        if (string == null)
            return null;
        int i = 0;
        int last = 0;
        char input[] = string.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) ((double) len * 1.3D));
        for (; i < len; i++) {
            char ch = input[i];
            if (ch <= '>')
                if (ch == '<') {
                    if (i > last)
                        out.append(input, last, i - last);
                    last = i + 1;
                    out.append(LT_ENCODE);
                } else if (ch == '&') {
                    if (i > last)
                        out.append(input, last, i - last);
                    last = i + 1;
                    out.append(AMP_ENCODE);
                } else if (ch == '"') {
                    if (i > last)
                        out.append(input, last, i - last);
                    last = i + 1;
                    out.append(QUOTE_ENCODE);
                }
        }

        if (last == 0)
            return string;
        if (i > last)
            out.append(input, last, i - last);
        return out.toString();
    }

    /**
     * <p>
     * 将&lt;还原为<
     * </p>
     * <p>
     * 将&&gt;还原为>
     * </p>
     * <p>
     * 将&quot;还原为"
     * </p>
     * <p>
     * 将&&amp;还原为&
     * </p>
     * 
     * @param string
     * @return
     */
    public static final String unescapeFromXML(String string) {
        string = replace(string, "&lt;", "<");
        string = replace(string, "&gt;", ">");
        string = replace(string, "&quot;", "\"");
        return replace(string, "&amp;", "&");
    }

    /**
     * <p>
     * 对字符串进行分割
     * </p>
     * <p/>
     * 
     * <pre>
     * StringUtil.split(null, &quot;&quot;)         = null
     * StringUtil.split(&quot;&quot;, &quot;&quot;)           = []
     * StringUtil.split(&quot;a.b.c&quot;, &quot;.&quot;)    = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     * StringUtil.split(&quot;a..b.c&quot;, &quot;.&quot;)   = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     * StringUtil.split(&quot;a:b:c&quot;, &quot;.&quot;)    = [&quot;a:b:c&quot;]
     * StringUtil.split(&quot;a b c&quot;, &quot; &quot;)    = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     * </pre>
     * 
     * @param src
     *            要split的字符串
     * @param delim
     *            分隔符号
     * @return
     */
    public static String[] split(String src, String delim) {
        if (src == null || delim == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(src, delim);
        Vector vct = new Vector();
        while (st.hasMoreTokens()) {
            vct.add(st.nextToken());
        }
        Object[] tks = vct.toArray();
        String rt[] = new String[vct.size()];
        System.arraycopy(tks, 0, rt, 0, vct.size());
        return rt;
    }

    /**
     * </p> 根据原来模版路径替换新的模版
     * 
     * @param fullTemplatePath
     *            原模版全路径
     * @param TemplateName
     *            新模板名称
     */
    public static final String replaceTemplate(String fullTemplatePath, String templateName) {
        String[] pathAry = split(fullTemplatePath, "/");
        pathAry[pathAry.length - 1] = templateName;
        StringBuffer sbPath = new StringBuffer();
        for (int i = 0; i < pathAry.length; i++) {
            if (i == pathAry.length - 1) {
                sbPath.append(pathAry[i]);
            }else{
                sbPath.append(pathAry[i] + "/");
            }
        }
        return sbPath.toString();
    }

    /**
     * <p>
     * 提供基于MD5的信息摘要算法，并返回固定长度的哈希值
     * </p>
     * 
     * @param data
     * @return
     */
    public static final synchronized String hash(String data) {
        if (digest == null)
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsae) {
                System.err
                        .println("Failed to load the MD5 MessageDigest. Jive will be unable to function normally.");
                nsae.printStackTrace();
            }
        digest.update(data.getBytes());
        return encodeHex(digest.digest());
    }

    /**
     * <p>
     * 将字符串进行Hex编码
     * </p>
     * <p/>
     * 
     * <pre>
     * encodeHex(&quot;a&quot;.getBytes());
     * </pre>
     * 
     * @param bytes
     *            字符串对应的byte[]
     * @return
     */
    public static final String encodeHex(byte bytes[]) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 16)
                buf.append("0");
            buf.append(Long.toString(bytes[i] & 0xff, 16));
        }

        return buf.toString();
    }

    /**
     * <p>
     * 将字符串进行Hex解码
     * </p>
     * 
     * @param hex
     * @return
     */
    public static final byte[] decodeHex(String hex) {
        char chars[] = hex.toCharArray();
        byte bytes[] = new byte[chars.length / 2];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += 2) {
            byte newByte = 0;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = newByte;
            byteCount++;
        }

        return bytes;
    }

    /**
     * <p>
     * 将字符串进行Base64编码
     * </p>
     * 
     * @param data
     * @return 返回编码后的字符串
     */
    public static String encodeBase64(String data) {
        return encodeBase64(data.getBytes());
    }

    /**
     * <p>
     * 将字符串进行Base64编码
     * </p>
     * <p/>
     * 
     * <pre>
     * encodeBase64(&quot;a&quot;.getBytes());
     * </pre>
     * 
     * @param data
     * @return 以字符串形式返回编码后的结果
     */
    public static String encodeBase64(byte data[]) {
        int len = data.length;
        StringBuffer ret = new StringBuffer((len / 3 + 1) * 4);
        for (int i = 0; i < len; i++) {
            int c = data[i] >> 2 & 0x3f;
            ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
            c = data[i] << 4 & 0x3f;
            if (++i < len)
                c |= data[i] >> 4 & 0xf;
            ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(c));
            if (i < len) {
                c = data[i] << 2 & 0x3f;
                if (++i < len)
                    c |= data[i] >> 6 & 3;
                ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                        .charAt(c));
            } else {
                i++;
                ret.append('=');
            }
            if (i < len) {
                c = data[i] & 0x3f;
                ret.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                        .charAt(c));
            } else {
                ret.append('=');
            }
        }

        return ret.toString();
    }

    /**
     * <p>
     * 将字符串进行Base64解码
     * </p>
     * 
     * @param data
     * @return 返回解码后的字符串
     */
    public static String decodeBase64(String data) {
        return decodeBase64(data.getBytes());
    }

    /**
     * <p>
     * 将字符串进行Base64解码
     * </p>
     * <p/>
     * 
     * <pre>
     * decodeBase64(&quot;a&quot;.getBytes());
     * </pre>
     * 
     * @param data
     * @return 以字符串形式返回解码后的结果
     */
    public static String decodeBase64(byte data[]) {
        int len = data.length;
        StringBuffer ret = new StringBuffer((len * 3) / 4);
        for (int i = 0; i < len; i++) {
            int c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                    .indexOf(data[i]);
            i++;
            int c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                    .indexOf(data[i]);
            c = c << 2 | c1 >> 4 & 3;
            ret.append((char) c);
            if (++i < len) {
                c = data[i];
                if (61 == c)
                    break;
                c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                        .indexOf((char) c);
                c1 = c1 << 4 & 0xf0 | c >> 2 & 0xf;
                ret.append((char) c1);
            }
            if (++i >= len)
                continue;
            c1 = data[i];
            if (61 == c1)
                break;
            c1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
                    .indexOf((char) c1);
            c = c << 6 & 0xc0 | c1;
            ret.append((char) c);
        }

        return ret.toString();
    }

    /**
     * <p>
     * 使用BreakIterator.wordInstance()进行分词转换
     * </p>
     * <p>
     * 支持的分词标识：+,/,\\,#,*,),(,&
     * </p>
     * <p/>
     * 
     * <pre>
     * 	String str = &quot;a+b&quot;;
     *  toLowerCaseWordArray(str) =&gt; [a,b]
     * <p/>
     *  String str = &quot;a*b&quot;;
     *  toLowerCaseWordArray(str) =&gt; [a,b]
     * </pre>
     * 
     * @param text
     * @return
     */
    public static final String[] toLowerCaseWordArray(String text) {
        if (text == null || text.length() == 0)
            return new String[0];
        ArrayList wordList = new ArrayList();
        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);
        int start = 0;
        for (int end = boundary.next(); end != -1; end = boundary.next()) {
            String tmp = text.substring(start, end).trim();
            tmp = replace(tmp, "+", "");
            tmp = replace(tmp, "/", "");
            tmp = replace(tmp, "\\", "");
            tmp = replace(tmp, "#", "");
            tmp = replace(tmp, "*", "");
            tmp = replace(tmp, ")", "");
            tmp = replace(tmp, "(", "");
            tmp = replace(tmp, "&", "");
            if (tmp.length() > 0)
                wordList.add(tmp);
            start = end;
        }

        return (String[]) wordList.toArray(new String[wordList.size()]);
    }

    /**
     * <p>
     * 返回给定长度的随机字符串
     * </p>
     * 
     * @param length
     *            随机串的长度
     * @return
     */
    public static final String randomString(int length) {
        if (length < 1)
            return null;
        char randBuffer[] = new char[length];
        for (int i = 0; i < randBuffer.length; i++)
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];

        return new String(randBuffer);
    }

    /**
     * <p>
     * 返回一定长度的字符串
     * </p>
     * <p/>
     * 
     * <pre>
     *  StringUtil.chopAtWord(&quot;This is a nice String&quot;, 10)
     *  返回的结果是：This is a
     * </pre>
     * 
     * @param string
     * @param length
     * @return a substring of <code>string</code> whose length is less than or
     *         equal to <code>length</code>, and that is chopped at whitespace.
     */
    public static final String chopAtWord(String string, int length) {
        if (string == null)
            return string;
        char charArray[] = string.toCharArray();
        int sLength = string.length();
        if (length < sLength)
            sLength = length;
        for (int i = 0; i < sLength - 1; i++) {
            if (charArray[i] == '\r' && charArray[i + 1] == '\n')
                return string.substring(0, i + 1);
            if (charArray[i] == '\n')
                return string.substring(0, i);
        }

        if (charArray[sLength - 1] == '\n')
            return string.substring(0, sLength - 1);
        if (string.length() < length)
            return string;
        for (int i = length - 1; i > 0; i--)
            if (charArray[i] == ' ')
                return string.substring(0, i).trim();

        return string.substring(0, length);
    }

    /**
     * <p>
     * 得到指定长度的字符串，不够以0补齐
     * </p>
     * <p/>
     * 
     * <pre>
     * StringUtil.zeroPadString(&quot;This&quot;, 10) = 000000This
     * </pre>
     * 
     * @param string
     * @param length
     * @return
     */
    public static final String zeroPadString(String string, int length) {
        if (string == null || string.length() > length) {
            return string;
        } else {
            StringBuffer buf = new StringBuffer(length);
            buf.append(zeroArray, 0, length - string.length()).append(string);
            return buf.toString();
        }
    }

    /**
     * <p>
     * 格式化以下划线'_'或减号'-'隔开的字符串
     * </p>
     * <p>
     * 如： "user-name", 可以格式化为"userName"
     * </p>
     * 
     * @param name
     *            java.lang.String
     * @param firstCharUpperCase
     *            boolean 如果为true，那么返回的首字母大写，反之，小写
     * @return java.lang.String
     */
    public static final String formatJavaName(String name, boolean firstCharUpperCase) {
        if (name == null || name.length() <= 1)
            return name;
        StringTokenizer tokenizer = new StringTokenizer(name, "-_");
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

    /**
     * <p>
     * 将首字母转换为大写
     * </p>
     * <p/>
     * 
     * <pre>
     * StringUtil.upperFirst(&quot;name&quot;) =&gt; Name
     * </pre>
     * 
     * @param name
     * @return
     */
    public static final String upperFirst(String name) {
        StringBuffer sb = new StringBuffer();
        sb.append(Character.toUpperCase(name.charAt(0))).append(name.substring(1));
        return sb.toString();
    }

    /**
     * <p>
     * 将首字母转换为大写,格式化以下划线'_'或减号'-'隔开的字符串
     * </p>
     * <p>
     * 如："user-name", 可以格式化为"UserName"
     * </p>
     * 
     * @param name
     * @return
     */
    public static final String upperFirstFormatJavaName(String name) {
        return formatJavaName(name, true);
    }

    /**
     * <p>
     * 将首字母转换为小写
     * </p>
     * <p/>
     * 
     * <pre>
     * StringUtil.lowerFirst(&quot;Name&quot;) =&gt; name
     * </pre>
     * 
     * @param name
     * @return
     */
    public static final String lowerFirst(String name) {
        StringBuffer sb = new StringBuffer();
        sb.append(Character.toLowerCase(name.charAt(0))).append(name.substring(1));
        return sb.toString();
    }

    /**
     * <p>
     * 将首字母转换为小写,格式化以下划线'_'或减号'-'隔开的字符串
     * </p>
     * <p>
     * 如："user-name", 可以格式化为"userName"
     * </p>
     * 
     * @param name
     * @return
     */
    public static final String lowerFirstFormatJavaName(String name) {
        return formatJavaName(name, true);
    }

    /**
     * <p>
     * 得到标准javaBean get方法的名称
     * </p>
     * <p/>
     * 
     * <pre>
     * 	getGetMethodName(&quot;name&quot;) =&gt; getName
     * </pre>
     * 
     * @param name
     * @return
     */
    public static final String getGetMethodName(String name) {
        return "get" + formatJavaName(name, true);
    }

    /**
     * <p>
     * 得到标准javaBean set方法的名称
     * </p>
     * <p/>
     * 
     * <pre>
     * 	getSetMethodName(&quot;name&quot;) =&gt; setName
     * </pre>
     * 
     * @param name
     * @return
     */
    public static final String getSetMethodName(String name) {
        return "set" + formatJavaName(name, true);
    }

    /**
     * <p>
     * 格式化以下划线'_'或减号'-'隔开的字符串，首字母小写
     * </p>
     * <p>
     * 如： "user-name", 可以格式化为"userName"
     * </p>
     * 
     * @param name
     * @return java.lang.String
     */
    public static final String formatJavaName(String name) {
        return formatJavaName(name, false);
    }

    /**
     * <p>
     * 将字符数组转换成字符串，以给定的符号分隔
     * </p>
     * <p/>
     * 
     * <pre>
     * String[] strings = {&quot;a&quot;,&quot;b&quot;,&quot;c&quot;,&quot;d&quot;};
     * StringUtil.join(&quot;_&quot;, strings) =&gt; a_b_c_d
     * </pre>
     * 
     * @param seperator
     *            连接符
     * @param strings
     * @return
     */
    public static String join(String seperator, String[] strings) {
        int length = strings.length;
        if (length == 0)
            return EMPTY_STRING;
        StringBuffer buf = new StringBuffer(length * strings[0].length()).append(strings[0]);
        for (int i = 1; i < length; i++) {
            buf.append(seperator).append(strings[i]);
        }
        return buf.toString();
    }

    /**
     * <p>
     * 将Iterator转换成字符串，以给定的符号分隔
     * </p>
     * <p/>
     * 
     * <pre>
     * 	List s = new ArrayList();
     * s.add(&quot;a&quot;);
     * s.add(&quot;b&quot;);
     * s.add(&quot;c&quot;);
     *  StringUtil.join(&quot;_&quot;, s.iterator()) =&gt; a_b_c
     * </pre>
     * 
     * @param seperator
     * @param objects
     * @return
     */
    public static String join(String seperator, java.util.Iterator objects) {
        StringBuffer buf = new StringBuffer();
        buf.append(objects.next());
        while (objects.hasNext()) {
            buf.append(seperator).append(objects.next());
        }
        return buf.toString();
    }

    /**
     * <p>
     * 将字符数组转换成字符串，以","分隔
     * </p>
     * <p/>
     * 
     * <pre>
     * 	String[] strings = {&quot;a&quot;,&quot;b&quot;,&quot;c&quot;,&quot;d&quot;};
     *  StringUtil.Array2String(strings);=&gt;a,b,c,d
     * </pre>
     * 
     * @param values
     * @return
     */
    public static String Array2String(String[] values) {
        return StringUtil.join(",", values);
    }

    private static final byte hexCharToByte(char ch) {
        switch (ch) {
            case 48: // '0'
                return 0;

            case 49: // '1'
                return 1;

            case 50: // '2'
                return 2;

            case 51: // '3'
                return 3;

            case 52: // '4'
                return 4;

            case 53: // '5'
                return 5;

            case 54: // '6'
                return 6;

            case 55: // '7'
                return 7;

            case 56: // '8'
                return 8;

            case 57: // '9'
                return 9;

            case 97: // 'a'
                return 10;

            case 98: // 'b'
                return 11;

            case 99: // 'c'
                return 12;

            case 100: // 'd'
                return 13;

            case 101: // 'e'
                return 14;

            case 102: // 'f'
                return 15;

            case 58: // ':'
            case 59: // ';'
            case 60: // '<'
            case 61: // '='
            case 62: // '>'
            case 63: // '?'
            case 64: // '@'
            case 65: // 'A'
            case 66: // 'B'
            case 67: // 'C'
            case 68: // 'D'
            case 69: // 'E'
            case 70: // 'F'
            case 71: // 'G'
            case 72: // 'H'
            case 73: // 'I'
            case 74: // 'J'
            case 75: // 'K'
            case 76: // 'L'
            case 77: // 'M'
            case 78: // 'N'
            case 79: // 'O'
            case 80: // 'P'
            case 81: // 'Q'
            case 82: // 'R'
            case 83: // 'S'
            case 84: // 'T'
            case 85: // 'U'
            case 86: // 'V'
            case 87: // 'W'
            case 88: // 'X'
            case 89: // 'Y'
            case 90: // 'Z'
            case 91: // '['
            case 92: // '\\'
            case 93: // ']'
            case 94: // '^'
            case 95: // '_'
            case 96: // '`'
            default:
                return 0;
        }
    }

    /**
     * 把包名转化为路径
     * 
     * @param packageName
     * @return
     */
    public static String package2Path(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }

    /**
     * 把路径转化为包名
     * 
     * @param packageName
     * @return
     */
    public static String path2Package(String packageName) {
        return packageName.replaceAll("/", "\\.");
    }

    /**
     * 获得文件名称
     * 
     * @param fileFullName
     * @return
     */
    public static String getFileName(String fileFullName) {
        if (StringUtil.isBlank(fileFullName)) {
            return fileFullName;
        }
        return fileFullName.substring(0, fileFullName.indexOf("."));
    }

    /**
     * 将给定的值转换为小写
     * 
     * @param value
     * @return
     */
    private String toLowerCase(String value) {
        if (StringUtil.isBlank(value))
            return value;
        return value.trim().toLowerCase();
    }

    /**
     * Native to ascii string. It's same as execut native2ascii.exe.
     * 
     * @param str
     *            native string
     * @return ascii string
     */
    public static String native2Ascii(String str) {
        char[] chars = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            sb.append(char2Ascii(chars[i]));
        }
        return sb.toString();
    }

    /**
     * Native character to ascii string.
     * 
     * @param c
     *            native character
     * @return ascii string
     */
    private static String char2Ascii(char c) {
        if (c > 255) {
            StringBuffer sb = new StringBuffer();
            sb.append("\\u");
            int code = (c >> 8);
            String tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            code = (c & 0xFF);
            tmp = Integer.toHexString(code);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            return sb.toString();
        } else {
            return Character.toString(c);
        }
    }

    /**
     * Ascii to native string. It's same as execut native2ascii.exe -reverse.
     * 
     * @param str
     *            ascii string
     * @return native string
     */
    public static String ascii2Native(String str) {
        StringBuffer sb = new StringBuffer();
        int begin = 0;
        int index = str.indexOf("\\u");
        while (index != -1) {
            sb.append(str.substring(begin, index));
            sb.append(ascii2Char(str.substring(index, index + 6)));
            begin = index + 6;
            index = str.indexOf("\\u", begin);
        }
        sb.append(str.substring(begin));
        return sb.toString();
    }

    /**
     * Ascii to native character.
     * 
     * @param str
     *            ascii string
     * @return native character
     */
    private static char ascii2Char(String str) {
        if (str.length() != 6) {
            throw new IllegalArgumentException(
                    "Ascii string of a native character must be 6 character.");
        }
        if (!"\\u".equals(str.substring(0, 2))) {
            throw new IllegalArgumentException(
                    "Ascii string of a native character must start with \"\\u\".");
        }
        String tmp = str.substring(2, 4);
        int code = Integer.parseInt(tmp, 16) << 8;
        tmp = str.substring(4, 6);
        code += Integer.parseInt(tmp, 16);
        return (char) code;
    }

    private static final char QUOTE_ENCODE[] = "&quot;".toCharArray();

    private static final char AMP_ENCODE[] = "&amp;".toCharArray();

    private static final char LT_ENCODE[] = "&lt;".toCharArray();

    private static final char GT_ENCODE[] = "&gt;".toCharArray();

    private static MessageDigest digest = null;

    private static final int fillchar = 61;

    private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    private static Random randGen = new Random();

    private static char numbersAndLetters[] = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();

    private static final char zeroArray[] = "0000000000000000".toCharArray();

    public static final String EMPTY_STRING = "";

    public static final char DOT = '.';

    public static final char UNDERSCORE = '_';

    public static final String COMMA_SPACE = ", ";

    public static final String COMMA = ",";

    public static final String OPEN_PAREN = "(";

    public static final String CLOSE_PAREN = ")";

    public static final String EMPTY = "";

    public static void main(String[] args) {
//        try {
//            // toFile("tmp/com/Demo.java", "public class Demo {}");
//            String path = "D:/workspace/BizFramework4JDK1.4/src/test/java/com/chinasofti/ro/bizframework/core/cache";
//            System.out.println(StringUtil.path2Package(path.substring(path.indexOf("java") + 5)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    	
    	System.out.println(getJavaBeanFieldName("WangZhiChao"));
    }
    
    /**
     * 把完整类(java.lang.String)转化简单类(String)
     * 
     * @param fieldType
     * @return
     */
    public static String getSimpleTypeName(String fieldType) {
        if (StringUtils.isNotBlank(fieldType) && fieldType.indexOf(".") != -1) {
            int index = fieldType.lastIndexOf(".");
            fieldType = fieldType.substring(index + 1);
        }
        return fieldType;
    }
    
    /**
     * @Title: getJavaBeanFieldGetterStr
     * @Description: 返回符合JavaBean命名规范的Getter方法名
     * @param fieldName
     * @return 参数及返回值
     * @return String 返回类型
     * @throws
     */
    public static String getJavaBeanFieldGetterStr(String fieldName){
    	return "get" + getJavaBeanFieldName(fieldName);
    }
    /**
     * @Title: getJavaBeanFieldSetterStr
     * @Description: 返回符合JavaBean命名规范的Setter方法名
     * @param fieldName
     * @return 参数及返回值
     * @return String 返回类型
     * @throws
     */
    public static String getJavaBeanFieldSetterStr(String fieldName){
    	return "set" + getJavaBeanFieldName(fieldName);
    }
    
    /**
     * @Title: getJavaBeanFieldName
     * @Description: 返回符合JavaBean命名规范的Getter和Setter用的PropertyName
     * @param fieldName
     * @return 参数及返回值
     * @return String 返回类型
     * @throws
     */
    public static String getJavaBeanFieldName(String fieldName){
    	char[] names = new char[fieldName.length()];
    	fieldName.getChars(0, fieldName.length(), names, 0);
    	if(Character.isUpperCase(names[0])){
    		if(Character.isUpperCase(names[1])){
    			return String.valueOf(names);
    		}else{
    			return fieldName;
    			//throw new FieldNameIllegalException("字段名称不符合JavaBean命名规范,字段名称不能第一个字母大写第二个字母小写.字段名称:"+fieldName);
    		}
    	}else{
    		if(Character.isUpperCase(names[1])){
    			return String.valueOf(names);
    		}else{
    			names[0] = Character.toUpperCase(names[0]);
    			return String.valueOf(names);
    		}
    	}
    }
    /**
   	 * 判断
   	 * @param refEntities
   	 * @param refEntity
   	 * @return
   	 */
   	public static boolean judgeEntitys(List<IEntity> refEntities,IEntity refEntity) {
   		boolean mark = false;
   		if(refEntities==null||refEntities.size()==0){
   			return false; 
   		}
   		for (IEntity iEntity : refEntities) {
   			if(iEntity.getId().equals(refEntity.getId())){
   				mark = true;
   			}
   		}
   		return mark;
   	}
}
