package dataset.viewmodel.impl;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.StringUtil;
import dataset.util.XMLUtil;
import dataset.viewmodel.IQueryField;
import dataset.viewmodel.IQueryForm;

/**
 * 展示模型查询信息定义
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class QueryForm extends Form implements IQueryForm {

    private int pageSize = 15;
    private boolean isTotalRows;
    private boolean isDistinct;
    private String orderFields;
    private String orders;
    private boolean exportExcel = false;

    public void fromXML(Object obj) {
        Element element = null;
        if (!(obj instanceof Element)) {
            return;
        }
        element = (Element) obj;
        this.loadForm(element);
        this.setPageSize(Integer.parseInt(XMLUtil.getAttribute(element, IDatasetConstant.PAGE_SIZE)));
        this.setDistinct(StringUtil.toBoolean(XMLUtil.getAttribute(element, IDatasetConstant.DISTINCE)));
        this.setTotalRows(StringUtil.toBoolean(XMLUtil.getAttribute(element, IDatasetConstant.IS_TOTAL_ROWS)));
        this.setOrderFields(XMLUtil.getAttribute(element, IDatasetConstant.ORDER_FIELDS));
        this.setOrders(XMLUtil.getAttribute(element, IDatasetConstant.ORDERS));
        this.setExportExcel(StringUtil.toBoolean(XMLUtil.getAttribute(element, IDatasetConstant.EXPORT_EXCEL)));
        
        Iterator iterator = XMLUtil.getChildElements(element, IDatasetConstant.QUERY_FIELD).iterator();
        while (iterator.hasNext()) {
            Element fieldElem = (Element) iterator.next();
            IQueryField field = new QueryField();
            field.setParentForm(this);
            field.fromXML(fieldElem);
            this.getFields().add(field);
        }
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.QUERY_FORM);
        this.buildElement(element);
        
        XMLUtil.addAttribute(element, IDatasetConstant.PAGE_SIZE, String.valueOf(this.getPageSize()));
        XMLUtil.addAttribute(element, IDatasetConstant.IS_TOTAL_ROWS, String.valueOf(this.isTotalRows()));
        XMLUtil.addAttribute(element, IDatasetConstant.DISTINCE, String.valueOf(this.isDistinct()));
        XMLUtil.addAttribute(element, IDatasetConstant.ORDER_FIELDS, this.getOrderFields());
        XMLUtil.addAttribute(element, IDatasetConstant.ORDERS, this.getOrders());
        XMLUtil.addAttribute(element, IDatasetConstant.EXPORT_EXCEL, String.valueOf(this.isExportExcel()));
        
        return element;

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isTotalRows() {
        return isTotalRows;
    }

    public void setTotalRows(boolean isTotalRows) {
        this.isTotalRows = isTotalRows;
    }

    public boolean isDistinct() {
        return isDistinct;
    }

    public void setDistinct(boolean isDistinct) {
        this.isDistinct = isDistinct;
    }

    public String getOrderFields() {
        return orderFields;
    }

    public void setOrderFields(String orderFields) {
        this.orderFields = orderFields;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    /**
     * 获取是否导出Excel
     * @return
     */
	public boolean isExportExcel() {
		return exportExcel;
	}
	/**
	 * 设置是否导出Excel
	 * @param exportExcel
	 */
	public void setExportExcel(boolean exportExcel) {
		this.exportExcel = exportExcel;
	}
    
}
