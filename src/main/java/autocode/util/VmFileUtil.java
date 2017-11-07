/**
 * $Id: VmFileUtil.java,v 1.4 2013/04/19 09:51:24 zhanghp Exp $
 */
package autocode.util;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.util.auxiliary.vm.VmVarInfo;
import autocode.util.auxiliary.vm.VmVarPoPkInfo;

/**
 * <p>
 * vm文件操作工具类
 * </p>
 * 
 * @author ResourceOne BizFoundation Team: ganjp
 * @version 1.0
 * @since 4.3
 */
public class VmFileUtil {
	 static Logger log = LoggerFactory.getLogger(VmFileUtil.class);
    
    /**
     * 把vmVarInfo里面的值赋给vmFile文件并得到对应的字符串
     * 
     * @param vmfilePath  eg:"d:/tmp/po.vm" 或 "src/vm/po.vm"
     * @param vmVarInfo    
     * @return
     */
    public static String getVmString(String vmFilePath, VmVarInfo vmVarInfo) {
        String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
        return getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmVarInfo, null);
    }
    
    /**
     * 把velocityContext里面的值赋给vmFile文件并得到对应的字符串
     * 
     * @param vmFilePath
     * @param velocityContext
     * @return
     */
    public static String getVmString(String vmFilePath, VelocityContext velocityContext) {
        String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
        return getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], velocityContext, null);
    }
    
    /**
     * 把vmVarPoPkInfo里面的值赋给vmFile文件并得到对应的字符串
     * 
     * @param vmfilePath  eg:"d:/tmp/po.vm" 或 "src/vm/po.vm"
     * @param vmVarPoPkInfo    
     * @return
     */
    public static String getVmString(String vmFilePath, VmVarPoPkInfo vmVarPoPkInfo) {
        String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
        return getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmVarPoPkInfo, null);
    }
    
    /**
     * 把vmVarInfo里面的值赋给vmFile文件并得到对应的字符串
     * 
     * @param vmDirPath    eg:"d:/tmp" 或 "src/vm"
     * @param vmFileName   eg:"po.vm"
     * @param vmVarInfo    
     * @return
     */
    public static String getVmString(String vmDirPath, String vmFileName, VmVarInfo vmVarInfo) {
        return getVmString(vmDirPath, vmFileName, vmVarInfo, null);
    }
    
    /**
     * 把vmVarInfo里面的值赋给vmFile文件并得到对应的字符串
     * 
     * @param vmfilePath  eg:"d:/tmp/po.vm" 或 "src/vm/po.vm"
     * @param vmVarInfo
     * @param encode      eg:"utf-8"
     * @return
     */
    public static String getVmString(String vmFilePath, VmVarInfo vmVarInfo, String encode) {
        String[] dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(vmFilePath);
        return getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmVarInfo,encode);                      
    }
    
    /**
     * 把velocityContext里面的值赋给vmFile文件并得到对应的字符串
     * 
     * @param vmDirPath   eg:"d:/tmp" 或 "src/vm"
     * @param vmFileName  eg:"po.vm"
     * @param VelocityContext velocityContext
     * @param encode
     * @return
     */
    public static String getVmString(String vmDirPath, String vmFileName, VelocityContext velocityContext, String encode) {
        try {
            VelocityEngine velocity = new VelocityEngine();
            Properties properties = new Properties();
            properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, vmDirPath);
            velocity.init(properties);
            
            if (StringUtil.isBlank(encode)) encode = "utf-8";
            Template template = velocity.getTemplate(vmFileName, encode);
            StringWriter stringWriter = new StringWriter();
            template.merge(velocityContext, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }    
    }
     
    /**
     * 把vmVarInfo里面的值赋给vmFile文件并得到对应的字符串
     * 
     * @param vmDirPath   eg:"d:/tmp" 或 "src/vm"
     * @param vmFileName  eg:"po.vm"
     * @param vmVarInfo
     * @param encode
     * @return
     */
    public static String getVmString(String vmDirPath, String vmFileName, VmVarInfo vmVarInfo, String encode) {
        VelocityContext velocityContext = new VelocityContext();
        //类的相关信息
        velocityContext.put(VmVarInfo.MODULE_NAME, vmVarInfo.getModuleName());
        velocityContext.put(VmVarInfo.PACKAGE_NAME, vmVarInfo.getPackageName());
        velocityContext.put(VmVarInfo.CLASS_FIRST_UPPER_NAME, vmVarInfo.getClassFirstUpperName());
        velocityContext.put(VmVarInfo.CLASS_FIRST_LOWER_NAME, vmVarInfo.getClassFirstLowerName());
        velocityContext.put(VmVarInfo.CLASS_IMPORT_TYPES, vmVarInfo.getClassImportTypes());
        velocityContext.put(VmVarInfo.CLASS_EXTENTS_CLASS, vmVarInfo.getClassExtentsClass());
        velocityContext.put(VmVarInfo.CLASS_IMPLEMENTS_INTERFACES, vmVarInfo.getClassImplementInterfaces());
        
        //使用的PO信息
        velocityContext.put(VmVarInfo.PO_FIRST_UPPER_NAME, vmVarInfo.getPoFirstUpperName());
        velocityContext.put(VmVarInfo.PO_FIRST_LOWER_NAME, vmVarInfo.getPoFirstLowerName());
        velocityContext.put(VmVarInfo.PO_IS_COMPOSITE_PK, new Boolean(vmVarInfo.isPoCompositePk()));
        velocityContext.put(VmVarInfo.PO_PK_FIRST_UPPER_NAME, vmVarInfo.getPoPkFirstUpperName());
        velocityContext.put(VmVarInfo.PO_PK_FIRST_LOWER_NAME, vmVarInfo.getPoPkFirstLowerName());
        velocityContext.put(VmVarInfo.PO_PK_TYPE, vmVarInfo.getPoPkType());
        velocityContext.put(VmVarInfo.PO_PK_SIMPLE_TYPE, vmVarInfo.getPoPkSimpleType());
        velocityContext.put(VmVarInfo.ENTITY_NAME, vmVarInfo.getEntityName());
        velocityContext.put(VmVarInfo.PK_NAME_FOR_ONEPK, vmVarInfo.getPkNameForOnePk());
        
        velocityContext.put(VmVarInfo.PO_DETAILS_FOR_MASTER_FIRST_UPPER, vmVarInfo.getPoDetailsFirstUpperForMaster());
        velocityContext.put(VmVarInfo.PO_DETAILS_FOR_MASTER_FIRST_LOWER, vmVarInfo.getPoDetailsFirstLowerForMaster());
        
        velocityContext.put(VmVarInfo.DETAIL_DAO_FIRST_LOWER_NAME,vmVarInfo.getDetailDaoFirstLowerName());
        velocityContext.put(VmVarInfo.DETAIL_DAO_FIRST_UPPER_NAME,vmVarInfo.getDetailDaoFirstUpperName());
        velocityContext.put(VmVarInfo.DETAIL_PO_FIRST_LOWER_NAME,vmVarInfo.getDetailPOFirstLowerName());
        velocityContext.put(VmVarInfo.DETAIL_PO_FIRST_UPPER_NAME,vmVarInfo.getDetailPOFirstUpperName());
        velocityContext.put(VmVarInfo.DETAIL_PO_PK_FIRST_LOWER_NAME,vmVarInfo.getDetailPoPkFirstLowerName());
        velocityContext.put(VmVarInfo.DETAIL_PO_PK_FIRST_UPPER_NAME,vmVarInfo.getDetailPoPkFirstUpperName());
        velocityContext.put(VmVarInfo.RELATION_DETAIL_FIELD_FIRST_LOWER_NAME,vmVarInfo.getRelationDetailFieldFirstLowerName());
        velocityContext.put(VmVarInfo.RELATION_DETAIL_FIELD_FIRST_UPPER_NAME,vmVarInfo.getRelationDetailFieldFirstUpperName());
        velocityContext.put(VmVarInfo.RELATION_MASTER_FIELD_FIRST_LOWER_NAME,vmVarInfo.getRelationMasterFieldFirstLowerName());
        velocityContext.put(VmVarInfo.RELATION_MASTER_FIELD_FIRST_UPPER_NAME,vmVarInfo.getRelationMasterFieldFirstUpperName());
        
        velocityContext.put(VmVarInfo.PO_FK_FIRST_UPPER_NAME, vmVarInfo.getPoFkFirstUpperName());
        velocityContext.put(VmVarInfo.PO_FK_FIRST_LOWER_NAME, vmVarInfo.getPoFkFirstLowerName());
        
        //使用的DAO信息
        velocityContext.put(VmVarInfo.DAO_FIRST_LOWER_NAME, vmVarInfo.getDaoFirstLowerName());
        velocityContext.put(VmVarInfo.DAO_FIRST_UPPER_NAME, vmVarInfo.getDaoFirstUpperName());
        velocityContext.put(VmVarInfo.IS_MASTER_ENTITY, vmVarInfo.isMasterEntity());
        
        //使用的Service信息
        velocityContext.put(VmVarInfo.SERVICE_FIRST_LOWER_NAME, vmVarInfo.getServiceFirstLowerName());
        velocityContext.put(VmVarInfo.SERVICE_FIRST_UPPER_NAME, vmVarInfo.getServiceFirstUpperName());
        
        //设置额外查询用字段的模板参数 by bruce 20121207
        velocityContext.put(VmVarInfo.PO_QUERY_EXT_FIELD_NAMES, vmVarInfo.getPoQueryExtFieldNames());
        velocityContext.put(VmVarInfo.PO_QUERY_EXT_FIELD_FIRST_UPPER_NAMES, vmVarInfo.getPoQueryExtFieldFirstUpperNames());
        velocityContext.put(VmVarInfo.PO_QUERY_EXT_FIELD_TYPES, vmVarInfo.getPoQueryExtFieldTypes());
        
        velocityContext.put(VmVarInfo.VM_VAR_INFO,vmVarInfo);
        
        velocityContext.put(VmVarInfo.TABLE_NAME,vmVarInfo.getTableName());
        
        velocityContext.put(VmVarInfo.PO_FIELD_NAMES, vmVarInfo.getPoFieldNames());
        velocityContext.put(VmVarInfo.PO_FIELD_CAPITAL_NAMES, vmVarInfo.getPoFieldCapitalNames());
        velocityContext.put(VmVarInfo.PO_FIELD_FIRST_UPPER_NAMES, vmVarInfo.getPoFieldFirstUpperNames());
        velocityContext.put(VmVarInfo.PO_FIELD_TYPES, vmVarInfo.getPoFieldTypes());
        velocityContext.put(VmVarInfo.PO_FIELD_HIBERNATE_SIMPLE_TYPES, vmVarInfo.getPoFieldHibernateSimpleTypes());
        velocityContext.put(VmVarInfo.PO_FIELD_ROBASE_SIMPLE_TYPES, vmVarInfo.getPoFieldRobaseSimpleTypes());
        velocityContext.put(VmVarInfo.PO_FIELD_IS_PERSISTS, vmVarInfo.getPoFieldIsPersists());
        velocityContext.put(VmVarInfo.PO_FIELD_NOTBLANKS, vmVarInfo.getPoFieldNotBlanks());
        
        //使用的其它类信息
        velocityContext.put(VmVarInfo.IMPORT_CLASS_FIRST_UPPER_NAME, vmVarInfo.getImportClassFirstUpperName());
        velocityContext.put(VmVarInfo.IMPORT_CLASS_FIRST_LOWER_NAME, vmVarInfo.getImportClassFirstLowerName());
        //注册MVC配置中使用的视图URI
        velocityContext.put(VmVarInfo.VIEW_LIST_URI, vmVarInfo.getViewListURI());
        velocityContext.put(VmVarInfo.VIEW_NEW_URI, vmVarInfo.getViewNewURI());
        velocityContext.put(VmVarInfo.VIEW_UPDATE_URI, vmVarInfo.getViewUpdateURI());
        velocityContext.put(VmVarInfo.VIEW_SHOW_URI, vmVarInfo.getViewShowURI());
        //关联表信息
        velocityContext.put(VmVarInfo.RELATION_ENTITY_INFO_LIST, vmVarInfo.getRelEntityInfoList());
        
        // 2012/6/27 chenhua BizAnyWhere 生成业务模型对象需要的SqL语句
        velocityContext.put(VmVarInfo.MOBILE_MODULE_SQL, vmVarInfo.getMobileModuleSql());

        //.by liutsh.2011.11.09.如果有扩展信息，加载之。
        Map extendPropertys = vmVarInfo.getExtendPropertys();
        if ((extendPropertys != null) && (!extendPropertys.isEmpty())){
            for (Iterator iterator =  extendPropertys.keySet().iterator(); iterator.hasNext(); ) {
                String propertyName = (String)iterator.next();
                Object propertyValue = extendPropertys.get(propertyName);
                velocityContext.put(propertyName, propertyValue);
            }
        }
        
        velocityContext.put(VmVarInfo.GEN_LIST_VIEW, vmVarInfo.isGenListView());
        velocityContext.put(VmVarInfo.GEN_ADD_VIEW, vmVarInfo.isGenAddView());
        velocityContext.put(VmVarInfo.GEN_EDIT_VIEW, vmVarInfo.isGenEditView());
        velocityContext.put(VmVarInfo.GEN_SHOW_VIEW, vmVarInfo.isGenShowView());
        
        velocityContext.put(VmVarInfo.EXPORT_EXCEL, vmVarInfo.isExportExcel());
        
        return getVmString(vmDirPath, vmFileName, velocityContext, encode);
    }
    
    /**
     * 把vmVarPoPkInfo里面的值赋给vmFile文件并得到对应的字符串
     * 
     * @param vmDirPath   eg:"d:/tmp" 或 "src/vm"
     * @param vmFileName  eg:"po.vm"
     * @param vmVarInfo
     * @param encode
     * @return
     */
    public static String getVmString(String vmDirPath, String vmFileName, VmVarPoPkInfo vmVarPoPkInfo, String encode) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put(VmVarPoPkInfo.PACKAGE_NAME, vmVarPoPkInfo.getPackageName());
        velocityContext.put(VmVarPoPkInfo.PO_PK_FIRST_UPPER_NAME, vmVarPoPkInfo.getPoPkFirstUpperName());
        velocityContext.put(VmVarPoPkInfo.PO_PK_FIRST_LOWER_NAME, vmVarPoPkInfo.getPoPkFirstLowerName());
        velocityContext.put(VmVarPoPkInfo.PO_PK_FIELD_NAMES, vmVarPoPkInfo.getPoPkFiedNames());
        velocityContext.put(VmVarPoPkInfo.PO_PK_FIELD_FIRST_UPPER_NAMES, vmVarPoPkInfo.getPoPkFieldFirstUpperNames());
        velocityContext.put(VmVarPoPkInfo.PO_PK_FIELD_HIBERNATE_SIMPLE_TYPES, vmVarPoPkInfo.getPoPkFieldHibernateSimpleTypes());
        velocityContext.put(VmVarPoPkInfo.PO_PK_FIELD_ROBASE_SIMPLE_TYPES, vmVarPoPkInfo.getPoPkFieldRobaseSimpleTypes());
        velocityContext.put(VmVarPoPkInfo.VM_VAR_PO_PK_INFO, vmVarPoPkInfo);
        return getVmString(vmDirPath, vmFileName, velocityContext, encode);
    }
    
    public static void main(String[] args) {
        String vmFilePath = "src/main/java/com/chinasofti/ro/bizframework/modules/autocode/util/auxiliary/vm/resource/bean1.vm";
        VmVarInfo vmVarInfo = new VmVarInfo();
        System.out.println(getVmString(vmFilePath, vmVarInfo));
    }

}
