package dataset.viewmodel;

/**
 * 展示模型查询信息字段接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IQueryField extends IVMField {
    
    /**
     * 获取查询字段占据的行数
     * @return
     */
    public int getRowSpan();

    /**
     * 设置查询字段占据的行数
     * @param rowSpan
     */
    public void setRowSpan(int rowSpan);
    /**
     * 获取查询字段占据的列数
     * @return
     */
    public int getColumnSpan();
    /**
     * 设置查询字段占据的列数
     * @param columnSpan
     */
    public void setColumnSpan(int columnSpan);
    /**
     * 获取条件操作符（>、>=、 <、 <=、 !=、 value%[like左匹配]、%value[like右匹配]、%value%[like左右匹配]、like:[兼容处理、以后不再支持]）
     * @return
     */
    public String getOperator();
    /**
     * 设置条件操作符（>、>=、 <、 <=、 !=、 value%[like左匹配]、%value[like右匹配]、%value%[like左右匹配]、[like:兼容处理、以后不再支持]）
     * @param operator
     */
    public void setOperator(String operator);
    /**
     * 获取匹配类型，当条件操作符为like时有效
     * @return
     */
    public String getMatchTYpe();
    /**
     * 设置匹配类型，当条件操作符为like时有效
     * @param matchTYpe
     */
    public void setMatchTYpe(String matchTYpe);
    
    /**
     * @Title: isMultiValue
     * @Description: 返回查询字段是否为多值字段(对应sql中的select in)
     * @return true or false
     * @return boolean 返回类型
     * @throws
     */
    public boolean isMultiValue();

//	/**
//	 * @Title: setMultiValue
//	 * @Description: 设置查询字段是否为多值字段(对应sql中的select in)
//	 * @param isMultiValue 参数及返回值
//	 * @return void 返回类型
//	 * @throws
//	 */
//	public void setMultiValue(boolean isMultiValue);
	
	/**
	 * @Title: getFieldNameInMultiValueCase
	 * @Description: 当这个查询字段为多值字段(对应sql中的select in)时,对应的Entity的属性名称
	 * @return 参数及返回值
	 * @return String 返回类型
	 * @throws
	 */
	public String getFieldNameInMultiValueCase();
	
	/**
	 * @Title: getFieldTypeInMultiValueCase
	 * @Description: 这个查询字段为多值字段(对应sql中的select in)时,对应的Entity的属性类型
	 * @return 参数及返回值
	 * @return String 返回类型
	 * @throws
	 */
	public String getFieldTypeInMultiValueCase();
}
