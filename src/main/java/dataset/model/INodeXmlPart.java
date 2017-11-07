/**
 * $Id: INodeXmlPart.java,v 1.2 2013/11/16 04:57:51 chenhua Exp $
 *
 * Copyright (c) 2006 ChinaSoft International, Ltd. All rights reserved
 * ResourceOne Student Project
 *
 */
package dataset.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @Title: INodeXmlPart.java
 * @Description: <br>读写xml（dataset）接口
 *               <br>
 * @Company: CSI
 * @Created on 2010-6-8 下午06:21:54
 * @author TangWei
 * @version $Revision: 1.2 $
 * @since 1.0
 */
public interface INodeXmlPart {
    /**
     * 读取xml，即dataset
     */
    public void fromXML(Object obj);
    /**
     * 写入xml，即dataset
     */
    public Element toXML(Document document);
}
