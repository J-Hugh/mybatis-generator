package dataset.viewmodel.impl;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataset.IDatasetConstant;
import dataset.util.StringUtil;
import dataset.util.XMLUtil;
import dataset.viewmodel.IQueryField;

/**
 * 展示模型查询信息字段
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public class QueryField extends VMField implements IQueryField {
    private int rowSpan;
    private int columnSpan;
    private String operator;
    private String matchTYpe;
    
    public void fromXML(Object obj){
        if(!(obj instanceof Element)){
            return;
        }
        super.fromXML(obj);
        Element element = (Element) obj;
        
        this.setRowSpan(Integer.parseInt(XMLUtil.getAttribute(element, IDatasetConstant.ROW_SPAN)));
        this.setColumnSpan(Integer.parseInt(XMLUtil.getAttribute(element, IDatasetConstant.COLUMN_SPAN)));
        this.setMatchTYpe(XMLUtil.getAttribute(element, IDatasetConstant.MATCH_TYPE));
        this.setOperator(XMLUtil.getAttribute(element, IDatasetConstant.OPERATOR));
        
    }

    public Element toXML(Document document) {
        Element element = XMLUtil.createElement(document,IDatasetConstant.QUERY_FIELD);
        this.buildElement(element);
        
        XMLUtil.addAttribute(element, IDatasetConstant.ROW_SPAN, String.valueOf(this.getRowSpan()));
        XMLUtil.addAttribute(element, IDatasetConstant.COLUMN_SPAN, String.valueOf(this.getColumnSpan()));
        XMLUtil.addAttribute(element, IDatasetConstant.OPERATOR, this.getOperator());
        XMLUtil.addAttribute(element, IDatasetConstant.MATCH_TYPE, this.getMatchTYpe());
        
        return element;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public void setColumnSpan(int columnSpan) {
        this.columnSpan = columnSpan;
    }

    public String getOperator() {
        //兼容处理：处理 通过 operator（为like） 与 matchType 两个属性定义操作条件类型的方式
        if(operator.equals(IDatasetConstant.OPERATOR_LIKE)){
            if(!StringUtil.isNullOrBlank(matchTYpe)){
                if(matchTYpe.equals(IDatasetConstant.MATCH_TYPE_LEFT)){
                    return IDatasetConstant.OPERATOR_LIKE_LEFT;
                }else if (matchTYpe.equals(IDatasetConstant.MATCH_TYPE_RIGHT)) {
                    return IDatasetConstant.OPERATOR_LIKE_RIGHT;
                }else if (matchTYpe.equals(IDatasetConstant.MATCH_TYPE_LEFT_RIGHT)) {
                    return IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT;
                }
            }
            return IDatasetConstant.OPERATOR_LIKE_LEFT_RIGHT;
        }
        return operator;
    }
    
    
    public String getFieldNameInMultiValueCase() {
    	return this.isMultiValue() ? this.getFieldName() + IDatasetConstant.QUERY_ONLY_FIELD_SUFFIX : this.getFieldName();
    }
    
    public String getFieldTypeInMultiValueCase() {
    	return this.isMultiValue() ? this.getFieldType() + "[]" : this.getFieldType();
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMatchTYpe() {
        return matchTYpe;
    }

    public void setMatchTYpe(String matchTYpe) {
        this.matchTYpe = matchTYpe;
    }

	public boolean isMultiValue() {
		//判断此查询字段是否是多值查询字段 by bruce 20121206
		//soszou 重构 2013-09-24 
		if(this.tag==null)
			return false;
        if(StringUtils.isNotBlank(tag.getTagType())){
        	//获取查询字段对应的前端控件
        	String tagType = this.tag.getTagType();
            
            for(int i=0;i<IDatasetConstant.MULTI_VALUE_TAGS.length;i++){
            	if(tagType.equalsIgnoreCase(IDatasetConstant.MULTI_VALUE_TAGS[i])){
            		return true;
            	}
            }
        }
		return false;
	}

	

}
