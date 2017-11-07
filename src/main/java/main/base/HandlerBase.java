package main.base;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author wangzhichao
 *
 */
public interface HandlerBase {
	
	
	/**
	 * 生成所有代码
	 * @return
	 */
	public boolean run() throws Exception;
	
	
	/**
	 * 生成Service
	 * @return
	 */
	public boolean runService(String tbName, String packName, List<Map<String, Object>> dbDetail, String pk) throws Exception;
	
	
	/**
	 * 生成dao
	 * @return
	 */
	public boolean runDao(String tbName, String packName, List<Map<String, Object>> dbDetail, String pk) throws Exception;
	
	
	/**
	 * 生成rest
	 * @return
	 */
	public boolean runRest(String tbName, String packName, List<Map<String, Object>> dbDetail, String pk) throws Exception;
	
	
	/**
	 * 生成domain
	 * @return
	 */
	public boolean runDomain(String tbName, String packName, List<Map<String, Object>> dbDetail) throws Exception;
	
	
	/**
	 * 生成Mapper文件
	 * @return
	 * @throws Exception
	 */
	public boolean runDaoMapper(String tbName_old, String tbName, String packName, List<Map<String, Object>> dbDetail) throws Exception;
	
	
}
