/*
 * $Id: XmlUtil.java,v 1.1 2012/09/11 06:36:26 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */

package autocode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import autocode.GenVmVarInfos;
import autocode.util.auxiliary.xml.HibernateDtdEntityResolver;
import autocode.util.auxiliary.xml.SaxReaderUtil;
import autocode.util.auxiliary.xml.exception.AttributeNotFoundException;
import autocode.util.auxiliary.xml.exception.BlankValueException;
import autocode.util.auxiliary.xml.exception.ChildNotFoundException;

/**
 * XMLUtil
 * 
 * @author ganjp
 * @since 4.3
 */
public class XmlUtil {
	static Logger log = LoggerFactory.getLogger(GenVmVarInfos.class);
    //****************************************************获得Document**************************************
    /**
     * 创建一个Document对象
     */
    public static Document createDocument() {
        return DocumentFactory.getInstance().createDocument("UTF-8");
    }
    /**
     * 创建一个Document对象
     * @param encode
     * @return
     */
    public static Document createDocument(String encode) {
        if (StringUtil.isNotBlank(encode)) {
            return DocumentFactory.getInstance().createDocument(encode);
        } else {
            return DocumentFactory.getInstance().createDocument("UTF-8");
        }
        
    }
    /**
     * 通过text获得Document对象
     * 
     * @param text eg："<user><userName>ganjp<userName></user>"
     * @return Document
     * @throws DocumentException
     */
    public static Document getDocument(String text) throws DocumentException {
        return DocumentHelper.parseText(text);
    }

    /**
     * 通过xml文件获得Document对象
     * 
     * @param file
     * @return Document
     * @throws DocumentException
     */
    public static Document getDocument(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(file);
    }

    /**
     * 通过InputStream获得Document对象
     * 
     * @param InputStream
     * @return Document
     * @throws DocumentException
     */
    public static Document getDocument(InputStream inputStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(inputStream);
    }

    /**
     * 通过在文件File并用EntityResolver验证，获得Document
     * 
     * @param file xml文件
     * @param entityResolver
     * @return
     */
    public static Document getDocument(File file, EntityResolver entityResolver) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            log.debug("read inputStream from " + file.getAbsolutePath() + " sucess");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("file:" + file.getAbsolutePath() + " not found");
        }
        return getDocument(inputStream, entityResolver);  
    }
    
    /**
     * 通过xml文件的inputStream并用EntityResolver验证，获得Document
     * 
     * @param inputStream
     * @param entityResolver
     * @return
     */
    public static Document getDocument(InputStream inputStream, EntityResolver entityResolver) {
        if (inputStream==null) {
            return null;
        }
        Document document = null;
        List errorList = new ArrayList();
        try {
            SaxReaderUtil saxReaderUtil = new SaxReaderUtil();
            document = saxReaderUtil.createSAXReader(errorList, entityResolver).read(new InputSource(inputStream));
        } catch (DocumentException e) {
            throw new RuntimeException("invalid mapping", e);
        }
        if (!errorList.isEmpty()) {
            throw new RuntimeException("invalid mapping", (Throwable) errorList.get(0));
        }
        return document;
    }
    
    //****************************************************获得Element集合**************************************
    /**
     * 把字符串text的每个"< />"转化成元素
     * 
     * @param text "<head /><body />"
     * @return
     * @throws DocumentException
     */
    public static List getElementList(String text) throws DocumentException {
        Document doc = DocumentHelper.parseText("<root>" + text + "</root>");
        Element root = doc.getRootElement();
        List elementList = root.elements();
        for (int i = 0; i < elementList.size(); i++) {
            ((Element) elementList.get(i)).setParent(null);
        }
        return elementList;
    }

    /**
     * 通过文件获得Element集合
     * 
     * @param file
     * @return
     * @throws IOException
     * @throws DocumentException
     * @throws IOException
     * @throws DocumentException
     */
    public static List getElementList(File file) throws IOException, DocumentException {
        InputStream in = new FileInputStream(file);
        byte[] b = new byte[(int) file.length()];
        in.read(b);
        String str = new String(b);
        if (str.startsWith("<?")) {
            Document doc = DocumentHelper.parseText(str);
            Element root = doc.getRootElement();
            List elementList = new ArrayList();
            elementList.add(root);
            return elementList;
        }
        return getElementList(str);
    }
    
    /**
     * 通过Element获得子Element集合
     * 
     * @param ele
     * @param name
     * @param mustHave
     * @return
     * @throws ChildNotFoundException
     */
    public static List getElementList(Element ele, String name, boolean mustHave) throws ChildNotFoundException {
        List children = null;
        if (name != null) {
            children = ele.elements(name);
        } else {
            children = ele.elements();
        }
        if (children.size() == 0 && mustHave) {
            String errMsg = "";
            if (name == null)
                errMsg = "Element: " + ele.getName() + " Child Not Found";
            else
                errMsg = "Element: " + ele.getName() + " Child " + name + " Not Found";
            throw new ChildNotFoundException(errMsg);
        }
        return children;
    }
    
    //*************************************************获得Element***************************************
    /**
     * 通过字符串创建一个Element对象
     * @param elementName
     * @return Element
     */
    public static Element createElement(String elementName) {
        return DocumentFactory.getInstance().createElement( elementName );
    }
    /**
     * 获得子元素
     * @param ele
     * @param name
     * @param mustHave
     * @return
     * @throws ChildNotFoundException
     */
    public static Element getElement(Element ele, String name, boolean mustHave) throws ChildNotFoundException {
        Element child = ele.element(name);
        if (child == null && mustHave) {
            throw new ChildNotFoundException("Element: " + ele.getName() + " Child: " + name + " Not Found");
        }
        return child;
    }

    //*************************************************获得值***************************************
    /**
     * 获得属性值
     * 
     * @param element
     * @param name
     * @param mustHave
     * @param notBlank
     * @return
     * @throws AttributeNotFoundException
     */
    public static String getAttributeValue(Element element, String name, boolean mustHave, boolean notBlank) 
            throws AttributeNotFoundException {
        String value = element.attributeValue(name);
        if (value == null && mustHave) {
            throw new AttributeNotFoundException("Element: " + element.getName() + " Attribute: " + name + " not found.");
        } else if (value == null) {
            value = "";
        }
        if (value.trim().equals("") && notBlank) {
            throw new AttributeNotFoundException("Element: " + element.getName() + " Attribute: " + name + " has blank value.");
        }
        return value;
    }
    /**
     * 
     * @param ele
     * @param name
     * @param mustHave
     * @param notBlank
     * @return
     * @throws ChildNotFoundException
     * @throws BlankValueException
     */
    public static String getElementText(Element ele, String name, boolean mustHave, boolean notBlank)
            throws ChildNotFoundException, BlankValueException {
        return getText(getElement(ele, name, mustHave), notBlank);
    }

    /**
     * 
     * @param ele
     * @param notBlank
     * @return
     * @throws BlankValueException
     */
    public static String getText(Element ele, boolean notBlank) throws BlankValueException {
        if (ele == null && notBlank) {
            throw new BlankValueException("Element is null");
        } else if (ele == null) {
            return "";
        } else {
            String ret = ele.getText();
            if (ret.trim().equals("") && notBlank) {
                throw new BlankValueException("Element:" + ele.getName() + " has Blank Text.");
            }
            return ret;
        }
    }
    
    //****************************************************转换类型**************************************
    /**
     * 把document转换成string
     * 
     * @param document
     * @return
     * @throws IOException
     */
    public static String convertToStr(Document document) throws IOException {
        return convertToStr(document, "UTF-8");
    }
    /**
     * 把document转换成string
     * 
     * @param document
     * @param encode
     * @return
     * @throws IOException
     */
    public static String convertToStr(Document document, String encode) throws IOException {
        StringWriter stringWriter = new StringWriter();
        OutputFormat format = new OutputFormat();
        if (StringUtil.isNotBlank(encode)) {
            format.setEncoding("UTF-8");
        } else {
            format.setEncoding(encode);
        }
        XMLWriter writer = new XMLWriter(stringWriter, format);
        writer.write(document);
        writer.close();
        return stringWriter.toString();
    }

    /**
     * 把element转换成string
     * 
     * @param element
     * @return
     * @throws IOException
     */
    public static String convertToStr(Element element) throws IOException {
        StringWriter stringWriter = new StringWriter();
        XMLWriter writer = new XMLWriter(stringWriter);
        writer.write(element);
        writer.close();
        return stringWriter.toString();
    }

    //****************************************************写入操作**************************************
    /**
     * 把document保存到file文件
     * 
     * @param document
     * @param file
     * @return
     */
    public static boolean saveToFile(Document document, File file) {
        return saveToFile(document, file, "UTF-8");
    }
    
    /**
     * 把document保存到file文件
     * 
     * @param document
     * @param file
     * @param encode
     * @return
     */
    public static boolean saveToFile(Document document, File file, String encode) {
        FileOutputStream fos = null; 
        try {
            fos = new FileOutputStream(file);
            OutputFormat format = new OutputFormat();
            if (StringUtil.isNotBlank(encode)) {
                format.setEncoding("UTF-8");
            } else {
                format.setEncoding(encode);
            }
            format.setNewlines(true);
//            format.setTrimText(true); 
            format.setIndent(true);  //是否缩进
            XMLWriter writer =new XMLWriter(fos,format);
            writer.setEscapeText(false);
            writer.write(document);
            log.info("save file success");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            File file = new File("src/test/java/com/chinasofti/ro/bizframework/modules/autocode/resource/base/Demo.hbm.xml");
            //测试getElementList
//          List doc = getElementList("<head /><body />");
//          for (int i = 0; i < doc.size(); i++) {
//              Element el = (Element) doc.get(i);
//              //System.out.println(el.asXML());
//          }
            
            //""
//          List elementList = getElementList(new File("src/test/java/com/chinasofti/ro/bizframework/modules/autocode/resource/base/Demo.hbm.xml"));
//          for (Iterator iterator = elementList.iterator(); iterator.hasNext();) {
//              Element element = (Element) iterator.next();
//              System.out.println(element.asXML());
//          }
//          EntityResolver entityResolver = new HibernateDtdEntityResolver(); 
//          Document document = XmlUtil.getDocument(file, entityResolver);
//          Element hbmNode = document.getRootElement();
//          System.out.println(hbmNode.asXML());
            
            EntityResolver entityResolver = new HibernateDtdEntityResolver(); 
            Document document = XmlUtil.createDocument();
            document.addDocType("hibernate-mapping", "-//Hibernate/Hibernate Mapping DTD 3.0//EN", "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
            document.setEntityResolver(entityResolver);
            Element hibernateMappingElement = document.addElement("hibernate-mapping");
            Element classElement = hibernateMappingElement.addElement("class");
            XmlUtil.saveToFile(document, new File("hbm.xml"));
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
