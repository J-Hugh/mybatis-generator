package dataset.viewmodel;

/**
 * 展示模型查询信息接口
 * @author BizFoundation Team: LiuJun
 *
 * {注释}
 *
 * @version 1.0
 * @since 4.2
 */
public interface IQueryForm extends IForm{

    /**
     * 获取分页大小
     * @return
     */
    public int getPageSize();
    /**
     * 设置分页大小
     * @param pageSize
     */
    public void setPageSize(int pageSize);
    /**
     * 获取是否需要统计总行数
     * @return
     */
    public boolean isTotalRows();
    /**
     * 设置是否需要统计总行数
     * @param isTotalRows
     */
    public void setTotalRows(boolean isTotalRows);
    /**
     * 获取结果集是否去重复数据
     * @return
     */
    public boolean isDistinct();
    /**
     * 设置结果集是否去重复数据
     * @param isDistinct
     */
    public void setDistinct(boolean isDistinct);
    /**
     * 获取排序字段(多个字段用 , 分隔)
     *  示例：id,name,desc
     * @return
     */
    public String getOrderFields();
    /**
     * 设置排序字段(多个字段用 , 分隔)
     *  示例：id,name,desc
     * @param orderFields
     */
    public void setOrderFields(String orderFields);
    /**
     * 获取排序(多个字段用 , 分隔)，与排序字段对应
     *  示例：asc,desc,asc
     * @return
     */
    public String getOrders();
    /**
     * 设置排序(多个字段用 , 分隔)，与排序字段对应
     *  示例：asc,desc,asc
     * @param orders
     */
    public void setOrders(String orders);
    
    /**
     * 获取是否导出Excel
     * @return
     */
	public boolean isExportExcel();
	/**
	 * 设置是否导出Excel
	 * @param exportExcel
	 */
	public void setExportExcel(boolean exportExcel);
}
