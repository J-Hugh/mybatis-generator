/*
 * $Id: SaxReaderUtil.java,v 1.1 2012/09/11 06:38:58 chenhua Exp $
 *
 * Copyright (c) 2010 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne BizFoundation Project
 *
 */

package autocode.util.auxiliary.xml;

import java.util.List;

import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * XMLUtil
 * 
 * @author ganjp
 * @since 4.3
 */
public class SaxReaderUtil {
	static Logger log = LoggerFactory.getLogger(SaxReaderUtil.class);
    private SAXReader saxReader; 
    
    /**
     * 创建一个dom4j SAXReader对象，用entityResolver验证xml，把出错的信息放置在errorList
     * 
     * @param file 
     * @param errorsList
     * @param entityResolver
     * @return
     */
    public SAXReader createSAXReader(String file, List errorsList, EntityResolver entityResolver) {
        if (saxReader == null) { 
            saxReader = new SAXReader(); 
        }    
        saxReader.setEntityResolver(entityResolver);
        saxReader.setErrorHandler( new SaxReaderErrorHandler(file, errorsList) );
        saxReader.setMergeAdjacentText(true);
        if (entityResolver != null) {
            saxReader.setValidation(true);
        } else {
            saxReader.setValidation(false);
        }
        
        return saxReader;
    }
    
    /**
     * 创建一个dom4j SAXReader对象，用entityResolver验证xml，把出错的信息放置在errorList
     * 
     * @param errorsList
     * @param entityResolver
     * @return
     */
    public SAXReader createSAXReader(List errorsList, EntityResolver entityResolver) {
        if (saxReader == null) { 
            saxReader = new SAXReader(); 
        }    
        saxReader.setEntityResolver(entityResolver);
        saxReader.setErrorHandler(new SaxReaderErrorHandler(errorsList) );
        saxReader.setMergeAdjacentText(true);
        if (entityResolver != null) {
            saxReader.setValidation(true);
        } else {
            saxReader.setValidation(false);
        }
        return saxReader;
    }

    /**
     * 自定义错误处理类
     * @author ganjp
     *
     */
    public static class SaxReaderErrorHandler implements ErrorHandler {
        private String file;
        private List errorList;
        SaxReaderErrorHandler(String file, List errorList) {
            this.file=file;
            this.errorList = errorList;
        }
        SaxReaderErrorHandler(List errorList) {
            this.errorList = errorList;
        }
        public void error(SAXParseException error) {
            if (file == null) {
                log.error( "Error parsing XML file line:" + error.getLineNumber() + error.getMessage() );
            } else {
                log.error( "Error parsing XML: " + file + '(' + error.getLineNumber() + ") " + error.getMessage() );
            }
            errorList.add(error);
        }
        public void fatalError(SAXParseException error) {
            error(error);
        }
        public void warning(SAXParseException warn) {
            if (file == null) {
                log.warn( "Error parsing XML file line:" + warn.getLineNumber() +  warn.getMessage() );
            } else {
                log.warn( "Warning parsing XML: " + file + '(' + warn.getLineNumber() + ") " + warn.getMessage() );
            }
        }
    }
}
