/**
 * $Id: XMLUtil.java,v 1.2 2013/11/16 04:57:53 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */
package dataset.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * <p>
 * xml格式的文件操作工具类
 * </p>
 * 
 * @author ResourceOne BizFoundation Team: ganjp
 * @version 1.0
 * @since 4.2
 */
public abstract class XMLUtil {

    /**
     * <p>
     * 对字符串进行html编码
     * <p>
     * 
     * @param src 源字符串
     * @return html编码的字符串
     */
    public static String encode(String src) {
        if (src == null) {
            return "";
        }
        char[] chars = src.toString().toCharArray();
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
            case '&':
                out.append("&amp;");
                break;
            case '<':
                out.append("&lt;");
                break;
            case '>':
                out.append("&gt;");
                break;
            case '\"':
                out.append("&quot;");
                break;
            default:
                out.append(chars[i]);
            }
        }
        return out.toString();
    }

    /**
     * <p>
     * 获得子节点对象
     * </p>
     * 
     * @param parent 父节点元素
     * @param childName 子节点名称
     * @return 子节点对象
     */
    public static Element getChildElement(Element parent, String childName) {
        NodeList children = parent.getChildNodes();
        int size = children.getLength();
        for (int i = 0; i < size; i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (childName.equals(element.getNodeName())) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * 获得所有子节点对象集
     * </p>
     * 
     * @param parent 父节点对象
     * @param childName 子节点名称
     * @return 子节点对象集
     */
    public static List getChildElements(Element parent, String childName) {
        NodeList children = parent.getChildNodes();
        List list = new ArrayList();
        int size = children.getLength();
        for (int i = 0; i < size; i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (childName.equals(element.getNodeName())) {
                    list.add(element);
                }
            }
        }
        return list;
    }

    /**
     * <p>
     * 获得节点对象显示文本值
     * </p>
     * 
     * @param element 节点对象
     * @return 节点显示文本
     */
    public static String getText(Element element) {
        StringBuffer sb = new StringBuffer();
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            switch (child.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.TEXT_NODE:
                sb.append(child.getNodeValue());
            }
        }
        return sb.toString().trim();
    }
    
    /**
     * 设置节点文本
     * @param element 节点对象
     * @param text 文本
     * @param isCDATA 是否使用CDATA方式设置存储文本
     */
    public static void setText(Element element,String text,boolean isCDATA){
    	if(element == null || text == null){
    		return;
    	}
    	if(isCDATA){
    		element.appendChild(element.getOwnerDocument().createCDATASection(text));
    	}else{
    		element.setTextContent(text);
    	}
    }

    /**
     * <p>
     * 获得去除前后空格的的属性值
     * </p>
     * 
     * @param element 节点对象
     * @param attributeName 属性名
     * @return 去除前后空格的属性值
     */
    public static String getAttribute(Element element, String attributeName) {
        return element.getAttribute(attributeName);
    }

    /**
     * <p>
     * 文档错误处理内部类
     * </p>
     * 
     * @author Rone Framework Team: ganjp
     * 
     * {注释}
     * 
     * @version 1.0
     * @since 4.2
     */
    public static class DocumentErrorHandler implements ErrorHandler {
        public DocumentErrorHandler() {
        }

        public void warning(SAXParseException exception) throws SAXException {
        }

        public void error(SAXParseException exception) throws SAXException {
            throw new SAXException(getMessage(exception));
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            throw new SAXException(getMessage(exception));
        }

        private String getMessage(SAXParseException exception) {
            return exception.getMessage() + " (" + "line:" + exception.getLineNumber()
                    + ((exception.getColumnNumber() > -1) ? ("col:" + exception.getColumnNumber()) : "")
                    + ") ";
        }
    }

    /**
     * <p>
     * 所有错误处理内部类
     * </p>
     * 
     * @author Rone Framework Team: ganjp
     * 
     * {注释}
     * 
     * @version 1.0
     * @since 4.2
     */
    public static class AllExceptionsErrorHandler implements ErrorHandler {
        private final List exceptions = new ArrayList();

        public AllExceptionsErrorHandler() {

        }

        public List getExceptions() {
            return exceptions;
        }

        public void warning(SAXParseException exception) throws SAXException {

        }

        public void error(SAXParseException exception) throws SAXException {
            addMessage(exception);
        }

        public void fatalError(SAXParseException exception) throws SAXException {
            addMessage(exception);
        }

        private void addMessage(SAXParseException exception) {
            exceptions.add(exception.getMessage() + " (" + "line:" + exception.getLineNumber()
                    + ((exception.getColumnNumber() > -1) ? ("col:" + exception.getColumnNumber()) : "")
                    + ") ");
        }
    }

    /**
     * <p>
     * 通过输入流和根元素名获得节点对象
     * </p>
     * 
     * @param is 输入流
     * @param rootName 根节点名称
     * @param valid 是否验证
     * @param entityResolver 解析实体
     * @return 节点对象
     * @throws Exception
     */
    public static Element loadAsElement(InputStream is, String rootName, boolean valid,
            EntityResolver entityResolver) throws Exception {
        Document doc = loadAsDocument(is, valid, entityResolver);
        NodeList nodes = (NodeList) doc.getElementsByTagName(rootName);
        if ((nodes == null) || (nodes.getLength() == 0)) {
            return null;
        }
        return (Element) nodes.item(0);
    }

    /**
     * <p>
     * 通过输入流和根元素名获得节点对象
     * </p>
     * 
     * @param is 输入流
     * @param rootName 根节点名称
     * @return 节点对象
     * @throws Exception
     */
    public static Element loadAsElement(InputStream is, String rootName) throws Exception {
        return loadAsElement(is, rootName, false, null);
    }

    /**
     * <p>
     * 通过输入流和根元素名获得节点对象集
     * </p>
     * 
     * @param is 输入流
     * @param rootName 根节点名称
     * @param valid 是否验证
     * @param entityResolver 解析实体
     * @return 节点对象集
     * @throws Exception
     */
    public static List loadAsElements(InputStream is, String rootName, boolean valid,
            EntityResolver entityResolver) throws Exception {
        try {
            List r = new ArrayList();
            Document doc = loadAsDocument(is, valid, entityResolver);
            NodeList nodes = (NodeList) doc.getElementsByTagName(rootName);
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element item = (Element) nodes.item(i);
                    r.add(item);
                }
            }
            return r;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * <p>
     * 通过输入流和根元素名获得节点对象数组
     * </p>
     * 
     * @param is 输入流
     * @param rootName 根节点名称
     * @param valid 是否验证
     * @param entityResolver 解析实体
     * @return 节点对象数组
     * @throws Exception
     */
    public static Element[] loadAsElements(InputStream is, String[] rootNames, boolean valid,
            EntityResolver entityResolver) throws Exception {
        try {
            if ((rootNames == null) || (rootNames.length == 0)) {
                return new Element[] {};
            }
            Element[] elements = new Element[rootNames.length];
            Document doc = loadAsDocument(is, valid, entityResolver);
            for (int i = 0; i < elements.length; i++) {
                NodeList nodes = doc.getElementsByTagName(rootNames[i]);
                if ((nodes != null) && (nodes.getLength() > 0)) {
                    elements[i] = (Element) nodes.item(0);
                } else {
                    elements[i] = null;
                }
            }

            return elements;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * <p>
     * 通过输入流获得整个文档对象
     * </p>
     * 
     * @param is 输入流
     * @param valid 是否验证
     * @param entityResolver 解析实体
     * @return 文档对象
     * @throws Exception
     */
    public static Document loadAsDocument(InputStream is, boolean valid, EntityResolver entityResolver)
            throws Exception {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(valid);
            
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
                if(entityResolver != null){
                    db.setEntityResolver(entityResolver);
                }
            } catch (ParserConfigurationException e) {
                throw new SAXException("Cannot creating document builder!!!", e);
            }

            db.setErrorHandler(new DocumentErrorHandler());
            Document doc = null;

            try {
                doc = db.parse(is);
            } catch (Exception e) {
                throw e;
            }
            return doc;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * <p>
     * 通过输入流获得整个文档对象
     * </p>
     * 
     * @param is 输入流
     * @return 文档对象
     * @throws Exception
     */
    public static Document loadAsDocument(InputStream is) throws Exception {
        return loadAsDocument(is, false, null);
    }
    
    /**
     * 添加属性
     * @param element
     * @param name
     * @param value
     */
    public static void addAttribute(Element element,String name,String value){
        if(value == null){
            return;
        }
        element.setAttribute(name, value);
    }
    
    /**
     * 添加子节点
     * @param parent
     * @param child
     */
    public static void addChildElement(Element parent,Element child){
        if(child == null){
            return;
        }
        parent.appendChild(child);
    }
    
    
    /**
     * 创建Document
     * @return
     * @throws Exception
     */
    public static Document createDocument() throws Exception{
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return document;
    }
    
    /**
     * 添加根节点
     * @param document
     * @param root
     */
    public static void addRootElement(Document document,Element root) {
        if(root == null){
            return;
        }
        document.appendChild(root);
    }

    /**
     * 创建Element
     * @param document
     * @param elemName
     * @return
     */
    public static Element createElement(Document document,String elemName){
        if(document == null || elemName == null || elemName.trim().equals("")){
            return null;
        }
        return document.createElement(elemName);
    }
}
