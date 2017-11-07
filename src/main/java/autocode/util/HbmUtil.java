/*
 * $Id: HbmUtil.java,v 1.3 2012/12/19 09:09:23 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */

package autocode.util;

import org.dom4j.Document;

/**
 * HbmUtil
 * 
 * @author ganjp
 * @since 4.3
 */
public class HbmUtil {

    /**
     * 创建Hbm文件的Document
     * @return
     */
    public static Document createHbmDocument() {
        Document document = XmlUtil.createDocument();
        document.addDocType("model-mapping", "-//ResourceOne/Model Mapping DTD 5.0//EN", "http://www.chinasofti.com/model-mapping-5.0.dtd");
        return document;
    }
    /**
     * 创建Hbm文件的Document
     * @return
     */
    public static Document createHbmDocumentForGenScript() {
        Document document = XmlUtil.createDocument();
        document.addDocType("hibernate-mapping", "-//Hibernate/Hibernate Mapping DTD 3.0//EN", "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
        return document;
    }

}
