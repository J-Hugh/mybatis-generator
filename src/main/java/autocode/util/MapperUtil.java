/*
 * 文 件 名:  MapperUtil.java
 * 版    权:  Hiaward Information Technology Co., Ltd. Copyright(2012),All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  yaolei
 * 创建时间: 2014-4-18
 * 修 改 人:  
 * 修改时间: 
 * 修改内容:  <修改内容>
 */
package autocode.util;

import org.dom4j.Document;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  yaolei
 * @version [版本号, 2014-4-18]
 * @see     [相关类/方法]
 * @since   [产品/模块版本]
 */
public class MapperUtil {
	/**
     * 创建Mapper文件的Document
     * @return
     */
    public static Document createMapperDocument() {
        Document document = XmlUtil.createDocument();
        document.addDocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        return document;
    }
}
