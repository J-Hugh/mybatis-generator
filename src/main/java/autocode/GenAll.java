package autocode;


import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autocode.model.AutoCodeInfo;
import autocode.model.AutoCodeUiInfo;
import autocode.model.ConfigInfo;
import autocode.model.OneJavaInfo;
import autocode.model.OneJavaUiInfo;
import autocode.util.FileUtil;
import autocode.util.SqliteUtil;
import autocode.util.StringUtil;
import autocode.util.TagUtil;
import autocode.util.VmFileUtil;
import autocode.util.auxiliary.vm.VmVarBase;
import autocode.util.auxiliary.vm.VmVarDetailEntityInfo;
import autocode.util.auxiliary.vm.VmVarInfo;
import autocode.util.auxiliary.vm.VmVarJavaBaseInfo;
import autocode.util.auxiliary.vm.VmVarViewBaseInfo;
import autocode.util.auxiliary.vm.VmVarViewFormInfo;
import autocode.util.auxiliary.vm.relationtable.VmVarServiceInfo;
import autocode.util.auxiliary.vm.relationtable.VmVarSqlModelInfo;
import dataset.model.IDataset;
import dataset.model.IEntity;
import dataset.model.IPEntity;
import dataset.model.IRelation;
import dataset.model.IVEntity;
import dataset.model.impl.PEntity;
import dataset.model.impl.PEntityConditon;
import dataset.model.impl.VEntity;



public class GenAll {
	static Logger log = LoggerFactory.getLogger(GenAll.class);

    /**
     * <p>
     * 通过持久化实体集合生成hbm文件
     * </p>
     * 
     * @param dataset
     * @param selPEntitys
     * @param dbHibernateDialect
     * @param outFile
     * @param isGenRelation
     */
    public static List genScript(IDataset dataset, Collection selPEntitys,
            String dbHibernateDialect, String outFile, boolean isGenRelation) {
        List dealedPEntityList = getDealPEntityList(dataset, selPEntitys, isGenRelation);
        List hbmFileList = GenHbmForDataAndScript.genHbmFile(dealedPEntityList, null, dbHibernateDialect,"tmp");
        List errorList = null;// GenDatabase.genScript(hbmFileList, dbHibernateDialect, outFile);
        try {
            FileUtil.deleteDirectory(new File("tmp"));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return errorList;
    }

    /**
     * <p>
     * 通过configInfo可以生成数据库脚本和表
     * </p>
     * 
     * <pre>
     * IDataset dataset = DatasetUtil.buildDatasetFromXML(new File(
     *         &quot;src/test/resources/dataset/sampleapp.dataset&quot;));
     * Collection pEntitys = DatasetUtil
     *         .getEntitys(dataset, new String[] { &quot;4089eba2303fc5a701304000b5fc003c&quot; });
     * ConfigInfo configInfo = new ConfigInfo();
     * configInfo.setDbHibernateDialect(DatabaseInfo.DB_HIBERNATE_DIALECT_DB2);// 数据库方言
     * configInfo.setOutFile(&quot;test.sql&quot;);
     * configInfo.setOverride(true);
     * Class.forName(&quot;com.ibm.db2.jcc.DB2Driver&quot;);
     * Connection connection = DriverManager
     *         .getConnection(&quot;jdbc:db2://192.9.107.96:50000/bizapp&quot;, &quot;db2admin&quot;, &quot;db2admin&quot;);
     * GenAll.genScriptAndTable(connection, dataset, pEntitys, configInfo, true);
     * </pre>
     * 
     * @param connection
     *            数据库链接对象
     * @param dataset
     *            选中的数据集
     * @param selPEntitys
     *            选中的实体集
     * @param configInfo
     *            设置数据库方言,输出sql的文件完整路径,是否使用覆盖
     * @param isGenRelation
     *            是否生成关联表
     */
    public static List genScriptAndTable(Connection connection, IDataset dataset,
            Collection selPEntitys, ConfigInfo configInfo, boolean isGenRelation) {
        List dealedPEntityList = getDealPEntityList(dataset, selPEntitys, isGenRelation);
        List hbmFileList = GenHbmForDataAndScript.genHbmFile(dealedPEntityList, null,configInfo.getDbHibernateDialect(), "tmp");
        configInfo.setHbmFileList(hbmFileList);
        List errorList = null;// GenDatabase.genScriptAndTable(configInfo, connection);
        try {
            FileUtil.deleteDirectory(new File("tmp"));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return errorList;
    }

    /**
     * 自动生成单表crud代码，如果对应生成vm文件的路径(包括文件名)是空,那么就默认为不生成
     * 
     * @param dealPEntitys
     * @param typeAndVmPathMap
     *            (页面组件类型：对应vm模板路径)
     */
    public static void genSingleTable(List dealPEntitys, Map typeAndVmPathMap) {
        TagUtil.setVmMap(typeAndVmPathMap);
        if (dealPEntitys == null || dealPEntitys.isEmpty()) {
            log.info("持久化实体集合为空");
            return;
        }
        for (Iterator iterator = dealPEntitys.iterator(); iterator.hasNext();) {
            IPEntity dealPEntity = (PEntity) iterator.next();
            genSingleTable(dealPEntity);
        }
    }
    
    /**
     * 返回根据持久化实体生成查询列表需要的对象（jsp编辑器GridScene使用）
     * @param dealPEntity
     * @return
     * @since 5.0RC2
     */
    public static VmVarBase genVmVarViewListInfo4GridScene(IEntity dealEntity){
    	return VmVarViewFactory.createVmVarView(dealEntity, dealEntity.getResultForm());
    }
    
//    /**
//     * 返回根据查询实体生成查询列表需要的对象（jsp编辑器GridScene使用）
//     * @param dealVEntity
//     * @return
//     * @since 5.0RC2
//     */
//    public static VmVarBase genVmVarViewListInfo4GridScene(IVEntity dealVEntity){
//    	return RelationTableViewFactory.createVmVarViewInfo(dealVEntity, dealVEntity.getResultForm());
//    }
    
    /**
     * （jsp编辑器FormScene使用）
     * @param dealPEntity
     * @return
     * @since 5.0RC2
     */
    public static VmVarViewFormInfo genVmVarViewFormInfo4FormScene(IPEntity dealPEntity){
    	 VmVarViewFormInfo viewNewInfo = new VmVarViewFormInfo(dealPEntity);
    	 return viewNewInfo;
    }

    /**
     * 单表自动生成所有的代码，如果对应生成文件的路径(包括文件名)是空,那么就默认为不生成
     * 
     * @param dealPEntity
     */
    public static void genSingleTable(IPEntity dealPEntity) {
        AutoCodeInfo autoCodeInfo = convertAutoCodeInfo(dealPEntity.getAutoCodeUiInfo());
        VmVarInfo vmVarInfo = GenVmVarInfos.getVmVarInfoFromEntity(dealPEntity);
        
        // 生成hbm
        GenHbm.genHbmFile(dealPEntity, autoCodeInfo);
        // 生成Po.java
        GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_PO);
        GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
        // 生成Service.java
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServicePath())) {
            GenVmVarInfos
            		.setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_SERVICE);
            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
        }
        // 生成ServiceImpl.java
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceImplVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServiceImplPath())) {
            GenVmVarInfos
                    .setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_SERVICEIMPL);
            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
//            GenCode.registSpringBean(vmVarInfo);
        }
        // 生成ServiceTest.java
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceTestVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServiceTestPath())) {
            GenVmVarInfos
                    .setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_SERVICE_TEST);
            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
//            GenCode.registSpringBean(vmVarInfo);
        }
        // 生成Controller.java
        if (StringUtil.isNotBlank(autoCodeInfo.getControllerVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getControllerPath())) {
            GenVmVarInfos
                    .setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_CONTROLLER);
            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
        }
        // 生成mvc.xml
        GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_BIZ_MVC);
//        GenCode.registBizMvc(vmVarInfo, autoCodeInfo.getBizmvcConfigPath(), autoCodeInfo
//                .getBizMvcVmFilePath());
        // 生成国际化配置
        GenCode.registI18n(autoCodeInfo, dealPEntity);
        // 生成View
        GenCode.genView(dealPEntity, autoCodeInfo);
        dealPEntity.setAutoCodeUiInfo(null);
    }

    /**
     * <p>
     * 单步生成Rest
     * </p>
     * </pre>
     * 
     * @param oneJavaUiInfo
     */
    public static void genRest(OneJavaUiInfo oneJavaUiInfo) {
        GenCode.genRest(oneJavaUiInfo);
    }
    
    /**
     * <p>
     * 单步生成Service(总共生成两个文件：Service和ServieImpl,再注册到ApplicationContext.xml)
     * </p>
     * 
     * <pre>
     * OneJavaUiInfo oneJavaUiInfo = new OneJavaUiInfo();
     * oneJavaUiInfo.setFileName(&quot;UserService&quot;);
     * oneJavaUiInfo
     *         .setFilePath(&quot;D:/workspace/src/main/java/com/chinasofti/ro/bizframework/modules/autocode&quot;);
     * oneJavaUiInfo.setVmDirPath(&quot;D:/workspace/vm/blank&quot;);
     * oneJavaUiInfo.setPackageName(&quot;com.chinasofti.ro.bizframework.modules.autocode&quot;);
     * oneJavaUiInfo.setRegistFileFullPath(&quot;D:/workspace/applicationContext.xml&quot;);
     * genService(oneJavaUiInfo);
     * </pre>
     * 
     * @param oneJavaUiInfo
     */
    public static void genService(OneJavaUiInfo oneJavaUiInfo) {
        GenCode.genService(oneJavaUiInfo);
    }

    /**
     * <p>
     * 单步生成Controller文件
     * </p>
     * 
     * <pre>
     * OneJavaUiInfo oneJavaUiInfo = new OneJavaUiInfo();
     * oneJavaUiInfo.setFileName(&quot;UserController&quot;);
     * oneJavaUiInfo
     *         .setFilePath(&quot;D:/workspace/src/main/java/com/chinasofti/ro/bizframework/modules/autocode&quot;);
     * oneJavaUiInfo.setVmDirPath(&quot;D:/workspace/vm/blank&quot;);
     * genService(oneJavaUiInfo);
     * </pre>
     * 
     * @param oneJavaUiInfo
     */
    public static void genController(OneJavaUiInfo oneJavaUiInfo) {
        GenCode.genController(oneJavaUiInfo);
        //bizMvcFilePath
//        FileUtil.writeOrReplaceText(oneJavaUiInfo.getRegistFileFullPath(), 
//                StringUtil.format(GenCode.bizMvcExistStr, oneJavaUiInfo.getEntityName()),
//                StringUtil.format(GenCode.bizMvcRegexExist, oneJavaUiInfo.getEntityName(), oneJavaUiInfo.getEntityName()),
//                StringUtil.format(GenCode.bizMvcReplaceStrExist,oneJavaUiInfo.getTemplateContext()),
//                GenCode.bizMvcRegexNoExist, 
//                StringUtil.format(GenCode.bizMvcReplaceStrNoExit,oneJavaUiInfo.getTemplateContext()));
    }

    // .add by liutsh,2011.11.1
    // 增加单独生成查询实体和持久化实体的相关接口。

    /**
     * 生成空的持久化实体模型
     * 
     * @param oneJavaUiInfo
     */
    public static void genPModel(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName());
        oneJavaInfo.setFilePath(oneJavaUiInfo.getFilePath());
        oneJavaInfo.setVmFileFullPath(oneJavaUiInfo.getVmDirPath() + "/BlankModel.vm"); // VM文件的名称暂时写死，以后考虑由客户端传入。

        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        GenCode.genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }

    /**
     * 根据模型信息生成持久化实体模型
     * 
     * @param dealPEntitys
     */
    public static void genPModel(List dealPEntitys) {
        for (Iterator iterator = dealPEntitys.iterator(); iterator.hasNext();) {
            IPEntity dealPEntity = (PEntity) iterator.next();

            AutoCodeInfo autoCodeInfo = GenAll.convertAutoCodeInfo(dealPEntity.getAutoCodeUiInfo());
            VmVarInfo vmVarInfo = GenVmVarInfos.getVmVarInfoFromEntity(dealPEntity);
            GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_PO);

            // 生成模型文件的时时生成hbm文件。
//            GenHbm.genHbmFile(dealPEntity, autoCodeInfo);
//            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
        }
    }

    /**
     * 根据模型信息生成移动持久化实体模型
     * 
     * @param dealPEntitys
     */
    public static void genMModel(List dealPEntitys, Map allsourcemap) {
        for (Iterator iterator = dealPEntitys.iterator(); iterator.hasNext();) {
            IPEntity dealPEntity = (PEntity) iterator.next();

            AutoCodeInfo autoCodeInfo = GenAll.convertAutoCodeInfoForMobile(dealPEntity
                    .getAutoCodeUiInfo());
            VmVarInfo vmVarInfo = GenVmVarInfos.getVmVarInfoFromEntity(dealPEntity);
            // 构建移动业务对象
            vmVarInfo.setPackageName(autoCodeInfo.getPoPackageName() + ".model");// 设置包名
            vmVarInfo.setClassFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getPoName()));// 设置类名(第一个字母大写)
            vmVarInfo.setClassFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getPoName()));// 设置类名(第一个字母小写)
            if (vmVarInfo.isPoCompositePk())
                vmVarInfo.getVmVarPoPkInfo().setPackageName(vmVarInfo.getPackageName());

            String path = autoCodeInfo.getPoPath();

            path.substring(0, path.lastIndexOf("/"));

            vmVarInfo.setOutFilePath(autoCodeInfo.getPoPath());// 设置输出路径(包括文件名)
            vmVarInfo.setVmFilePath(autoCodeInfo.getPoVmFilePath());
            Map sourcemap = (Map) allsourcemap.get(dealPEntity.getName());
            //判断生成Model
            if (sourcemap.get("生成Model") != null) {
                GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath() + "/BizModel.vm", vmVarInfo
                        .getOutFilePath());
                //added by zhangmh,2012-7-2
                String newpath = autoCodeInfo.getPoPath().substring(0,autoCodeInfo.getPoPath().lastIndexOf("."))+"Sync.java";
                vmVarInfo.setOutFilePath(newpath);
                vmVarInfo.setMobileModuleSql(SqliteUtil.getCreateSyncTable(dealPEntity));//重置createSQL
                vmVarInfo.setClassFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getPoName())+"Sync");// 重置类名(第一个字母大写)
                vmVarInfo.setClassFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getPoName())+"Sync");// 重置类名(第一个字母小写)
                GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath() + "/BizSyncModel.vm", vmVarInfo
                        .getOutFilePath());
            }

            OneJavaUiInfo oneJavaUiInfo = new OneJavaUiInfo();
            oneJavaUiInfo.setFileName(StringUtil.upperFirst(dealPEntity.getName()));
            oneJavaUiInfo.setFilePath(autoCodeInfo.getPoPath());
            oneJavaUiInfo.setEntityName(dealPEntity.getName());
            oneJavaUiInfo.setPackageName(autoCodeInfo.getPoPackageName());
            oneJavaUiInfo.setVmDirPath(autoCodeInfo.getPoVmFilePath());
            //判断生成 Service
            if (sourcemap.get("生成Service") != null) {
                GenCode.genMobileService(oneJavaUiInfo);
            }

            /*
             * //生成实体对应Parser类 GenCode.genParser(oneJavaUiInfo);
             * //生成实体对应DataAccess类 GenCode.genDataAccess(oneJavaUiInfo);
             * //生成实体对应LocalDataAccess类
             * GenCode.genLocalDataAccess(oneJavaUiInfo);
             * //生成实体对应ServerDataAccess类
             * GenCode.genServerDataAccess(oneJavaUiInfo);
             */

        }
    }

    /**
     * 生成空的查询模型
     * 
     * @param oneJavaUiInfo
     */
    public static void genVModel(OneJavaUiInfo oneJavaUiInfo) {
        OneJavaInfo oneJavaInfo = new OneJavaInfo();
        oneJavaInfo.setFileName(oneJavaUiInfo.getFileName());
        oneJavaInfo.setFilePath(oneJavaUiInfo.getFilePath());
        oneJavaInfo.setVmFileFullPath(oneJavaUiInfo.getVmDirPath() + "/BlankQueryModel.vm"); // VM文件的名称暂时写死，以后考虑由客户端传入。

        VmVarJavaBaseInfo vmVarJavaBaseInfo = new VmVarJavaBaseInfo();
        vmVarJavaBaseInfo.setPackageName(oneJavaUiInfo.getPackageName());
        vmVarJavaBaseInfo.setClassName(oneJavaInfo.getFileName());
        GenCode.genJavaCode(oneJavaInfo, vmVarJavaBaseInfo);
    }

    /**
     * 根据查询实体生成查询模型
     * 
     * @param outFilePath
     *            :输出路径(包含文件名)
     * @param vmFilePath
     *            ：VM文件路径(包含文件名)
     */
    public static void genVModel(IVEntity queryEntity, String outFilePath, String vmFilePath) {
        VmVarInfo vmVarInfo = GenVmVarInfos.getVmVarInfoFromEntity(queryEntity);

        vmVarInfo.setClassFirstUpperName(StringUtil.upperFirst(queryEntity.getName()));// 设置类名(第一个字母大写)
        vmVarInfo.setClassFirstLowerName(StringUtil.lowerFirst(queryEntity.getName()));// 设置类名(第一个字母小写)
        vmVarInfo.setOutFilePath(outFilePath);
        vmVarInfo.setVmFilePath(vmFilePath);

        GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
    }
    
    /**
     * 生成查询实体（包括hbm文件）
     * @param dealVEntity
     * @since 5.0 RC2
     */
    public static void genDao(IPEntity dealPEntity,AutoCodeInfo autoCodeInfo){
        VmVarInfo vmVarInfo = GenVmVarInfos.getVmVarInfoFromEntity(dealPEntity);
        //生成DAO
        vmVarInfo.setPackageName(autoCodeInfo.getDaoPackageName());// 设置包名
		vmVarInfo.setClassFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getDaoName()));
		vmVarInfo.setClassFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getDaoName()));
		// 引入所有的po类.
		List importPoTypes = new ArrayList();
		importPoTypes.add(autoCodeInfo.getDaoPackageName().replace("dao.mybatis", "domain") + ".*");
		vmVarInfo.setClassImportTypes(importPoTypes);
		vmVarInfo.setOutFilePath(autoCodeInfo.getDaoPath());
		vmVarInfo.setVmFilePath(autoCodeInfo.getDaoVmFilePath());
		vmVarInfo.setEntityName(autoCodeInfo.getDaoName().replace("Dao", ""));
		vmVarInfo.setPoFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getDaoName().replace("Dao", "")));
        vmVarInfo.setPackageName(autoCodeInfo.getDaoPackageName());
        vmVarInfo.setEntityName(dealPEntity.getName());
        vmVarInfo.setClassFirstLowerName(autoCodeInfo.getDaoName());
        vmVarInfo.setClassFirstUpperName(autoCodeInfo.getDaoName());
        vmVarInfo.setPoCompositePk(false);
        GenCode.genJavaCode(vmVarInfo, autoCodeInfo.getDaoVmFilePath(), autoCodeInfo.getDaoPath());
        
//        VmVarSqlModelInfo vmsql = new VmVarSqlModelInfo(dealPEntity, autoCodeInfo);
//        String[] dirPathAndFileNameArr = null;
//        if (StringUtil.isNotBlank(autoCodeInfo.getPoVmFilePath()) && StringUtil.isNotBlank(autoCodeInfo.getPoPath())) {
//            dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo.getPoVmFilePath());
//            String sqlmodel = VmFileUtil.getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmsql.getVelocityContext(), null);
//            FileUtil.toFile(autoCodeInfo.getPoPath(), sqlmodel, null);
//
//        }
        //生成Mapper文件
        if(StringUtil.isNotBlank(autoCodeInfo.getHbmPath())){
        	BuildMapper.buildMapperFile(dealPEntity, autoCodeInfo, null);
//        	GenHbm.genHbmFile4SqlModel(dealVEntity, autoCodeInfo.getPoPackageName(),autoCodeInfo.getHbmPath());
        }
    }

    /**
     * 生成主从表代码
     * 
     * @param masterPEntity
     * @param detailPEntitys
     * @param vmMap
     */
    public static void genMasterDetailTable(IPEntity masterPEntity, List detailPEntitys, Map vmMap) {
        TagUtil.setVmMap(vmMap);
        if (masterPEntity == null || detailPEntitys == null || detailPEntitys.isEmpty()) {
            log.info("主表信息为空或者从表信息为空");
            return;
        }

        dealMasterDetailRelation(masterPEntity, detailPEntitys);

        // 生成主表的Hbm以及Model
        AutoCodeInfo autoCodeInfo = convertAutoCodeInfo(masterPEntity.getAutoCodeUiInfo());
        VmVarInfo vmVarInfo = GenVmVarInfos.getVmVarInfoFromEntity(masterPEntity);
        // 生成hbm
        GenHbm.genHbmFile(masterPEntity, autoCodeInfo);
        // 生成Po.java
        GenVmVarInfos.setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_PO);
        GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
        // 生成国际化配置
        GenCode.registI18n(autoCodeInfo, masterPEntity);

        // 生成从表的Hbm以及Model
        Iterator iterator = detailPEntitys.iterator();
        while (iterator.hasNext()) {
            IPEntity detaiPEntity = (IPEntity) iterator.next();
            AutoCodeInfo detailAutoCodeInfo = convertAutoCodeInfo(detaiPEntity.getAutoCodeUiInfo());
            VmVarInfo detailVmVarInfo = GenVmVarInfos.getVmVarInfoFromEntity(detaiPEntity);
            GenHbm.genHbmFile(detaiPEntity, detailAutoCodeInfo);
            GenVmVarInfos
                    .setCustomProperty(detailVmVarInfo, detailAutoCodeInfo, GenVmVarInfos.FILE_TYPE_PO);
            GenCode.genJavaCode(detailVmVarInfo, detailVmVarInfo.getVmFilePath(), detailVmVarInfo
                    .getOutFilePath());
            GenCode.registI18n(detailAutoCodeInfo, detaiPEntity);
        }
        List detailEntityInfos = VmVarDetailEntityInfo
                .buildVmVarDetailEntityInfo(detailPEntitys, masterPEntity);
        // 生成Service.java
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServicePath())) {
            GenVmVarInfos
                    .setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_SERVICE, detailEntityInfos);
            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
        }
        // 生成ServiceImpl.java
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceImplVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServiceImplPath())) {
            GenVmVarInfos
                    .setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_SERVICEIMPL, detailEntityInfos);
            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
            GenCode.registSpringBean(vmVarInfo);
        }
        // 生成ServiceTest.java
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceTestVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServiceTestPath())) {
            GenVmVarInfos
                    .setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_SERVICE_TEST);
            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
            GenCode.registSpringBean(vmVarInfo);
        }
        // 生成Controller.java
        if (StringUtil.isNotBlank(autoCodeInfo.getControllerVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getControllerPath())) {
            GenVmVarInfos
                    .setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_CONTROLLER, detailEntityInfos);
            GenCode.genJavaCode(vmVarInfo, vmVarInfo.getVmFilePath(), vmVarInfo.getOutFilePath());
        }
        // 生成mvc.xml
        GenVmVarInfos
                .setCustomProperty(vmVarInfo, autoCodeInfo, GenVmVarInfos.FILE_TYPE_BIZ_MVC, detailEntityInfos);
//        GenCode.registBizMvc(vmVarInfo, autoCodeInfo.getBizmvcConfigPath(), autoCodeInfo
//                .getBizMvcVmFilePath());

        // 生成View
        GenCode.genView(masterPEntity, autoCodeInfo, detailPEntitys);
        masterPEntity.setAutoCodeUiInfo(null);
    }

    /**
     * 构建主表与从表需要生成的关联关系：在主表中添加指定从表的1对N关联关系
     * 
     * @param masterPEntity
     * @param detailPEntitys
     */
    private static void dealMasterDetailRelation(IPEntity masterPEntity, List detailPEntitys) {
        if (masterPEntity == null || detailPEntitys == null || detailPEntitys.size() == 0) {
            return;
        }

        List detailIds = new ArrayList();
        Iterator iterator = detailPEntitys.iterator();

        while (iterator.hasNext()) {
            IPEntity entity = (IPEntity) iterator.next();
            detailIds.add(entity.getId());
        }

        PEntityConditon entityConditon = new PEntityConditon();
        entityConditon.addSrcRelationId("");
        iterator = masterPEntity.getSrcRelations().iterator();
        while (iterator.hasNext()) {
            IRelation relation = (IRelation) iterator.next();
            if (detailIds.contains(relation.getTgtEntityId())) {
                entityConditon.addSrcRelationId(relation.getId());
            }

        }
        masterPEntity.setPEntityConditon(entityConditon);
    }

    /**
     * <p>
     * 获得处理过的持久化实体（是否生成关联表），如果是生成关联表，那么把关联表也放入到List并返回，
     * 否则把所有的管理去除掉。（通过PEntityConditon属性处理关联关系）
     * </p>
     * 
     * @param dataset
     *            选中的数据集
     * @param selPEntitys
     *            选中的实体集
     * @param isGenRelation
     *            是否生成关联表
     * 
     * @return List(IPEntity)
     */
    public static List getDealPEntityList(IDataset dataset, Collection selPEntitys,
            boolean isGenRelation) {
        List dealedPEntityList = Collections.EMPTY_LIST;
        if (isGenRelation) {
            dealedPEntityList = GenEntityInfo.getIncludeOneRelatePEntityList(dataset, GenEntityInfo
                    .convertEntityList(selPEntitys));
        } else {
            dealedPEntityList = GenEntityInfo.setAndGetNoRelatePEntityList(GenEntityInfo
                    .convertEntityList(selPEntitys));
        }
        return dealedPEntityList;
    }

    /**
     * <p>
     * 为Ui生成默认的值(文件名称，java对应的包名，文件对应的相应路径)
     * </p>
     * 
     * @param dealPEntitys
     *            处理过关联关系的实体
     */
    public static void dealPEntityAutoCodeUiInfo(List dealPEntitys) {
        for (Iterator iterator = dealPEntitys.iterator(); iterator.hasNext();) {
            IEntity pEntity = (IEntity) iterator.next();
            dealAutoCodeUiInfo(pEntity);
        }
    }

    /**
     * 自动生成关联表crud代码，如果对应生成vm文件的路径(包括文件名)是空,那么就默认为不生成
     * 
     * @param dealVEntitys
     * @param typeAndVmPathMap
     *            (页面组件类型：对应vm模板路径)
     */
    public static void genRelationTable(List dealVEntitys, Map typeAndVmPathMap) {
        TagUtil.setVmMap(typeAndVmPathMap);
        if (dealVEntitys == null || dealVEntitys.isEmpty()) {
            log.info("查询实体集合为空");
            return;
        }
        for (Iterator iterator = dealVEntitys.iterator(); iterator.hasNext();) {
            IVEntity dealVEntity = (VEntity) iterator.next();
            genRelationTable(dealVEntity);
        }
    }

    /**
     * 关联表自动生成所有的代码，如果对应生成文件的路径(包括文件名)是空,那么就默认为不生成
     * 
     * @param dealVEntity
     */
    public static void genRelationTable(IVEntity dealVEntity) {
        AutoCodeInfo autoCodeInfo = convertAutoCodeInfo(dealVEntity.getAutoCodeUiInfo());
        // 生成SqlModel
        VmVarSqlModelInfo vmsql = new VmVarSqlModelInfo(dealVEntity, autoCodeInfo);
        String[] dirPathAndFileNameArr = null;
        if (StringUtil.isNotBlank(autoCodeInfo.getPoVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getPoPath())) {
            dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo.getPoVmFilePath());
            String sqlmodel = VmFileUtil
                    .getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmsql
                            .getVelocityContext(), null);
            FileUtil.toFile(autoCodeInfo.getPoPath(), sqlmodel, null);

        }
        //生成SqlModel的hbm文件
        if(StringUtil.isNotBlank(autoCodeInfo.getHbmPath())){
        	GenHbm.genHbmFile4SqlModel(dealVEntity, autoCodeInfo.getPoPackageName(), autoCodeInfo.getHbmPath());
        }
        // 生成Service.java
        VmVarServiceInfo vmservice = new VmVarServiceInfo(dealVEntity, autoCodeInfo);
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServicePath())) {
            dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo
                    .getServiceVmFilePath());
            String service = VmFileUtil
                    .getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmservice
                            .getVelocityContext(), null);
            FileUtil.toFile(autoCodeInfo.getServicePath(), service, null);
        }
        // 生成ServiceImpl.java
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceImplVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServiceImplPath())) {
            // 生成ServiceImpl
            dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo
                    .getServiceImplVmFilePath());
            String serviceimpl = VmFileUtil
                    .getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmservice
                            .getVelocityContext(), null);
            FileUtil.toFile(autoCodeInfo.getServiceImplPath(), serviceimpl, null);
            vmservice.initRegistServiceImpl();
//            GenCode.registSpringBean(vmservice);
        }
        // 生成ServiceTest.java
        if (StringUtil.isNotBlank(autoCodeInfo.getServiceTestVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getServiceTestPath())) {
            dirPathAndFileNameArr = FileUtil.getDirPathAndFileName(autoCodeInfo
                    .getServiceTestVmFilePath());
            String servicetest = VmFileUtil
                    .getVmString(dirPathAndFileNameArr[0], dirPathAndFileNameArr[1], vmservice
                            .getVelocityContext(), null);
            FileUtil.toFile(autoCodeInfo.getServiceTestPath(), servicetest, null);
            vmservice.initRegistServiceTest();
//            GenCode.registSpringBean(vmservice);
        }
        // 生成Controller.java
        if (StringUtil.isNotBlank(autoCodeInfo.getControllerVmFilePath())
                && StringUtil.isNotBlank(autoCodeInfo.getControllerPath())) {
            GenCode.genController(dealVEntity, autoCodeInfo);
        }
        // 生成国际化
        GenCode.registI18n(autoCodeInfo, dealVEntity);
        GenCode.genView(dealVEntity, autoCodeInfo);
    }

    /**
     * <p>
     * 为Ui生成默认的值(文件名称，java对应的包名，文件对应的相应路径)
     * </p>
     * 
     * @param pEntity
     */
    public static void dealAutoCodeUiInfo(IEntity pEntity) {
        AutoCodeUiInfo autoCodeUiInfo = pEntity.getAutoCodeUiInfo();
        if (autoCodeUiInfo != null) {
            String modulePackageName = autoCodeUiInfo.getBasePackageName() + "."
                    + autoCodeUiInfo.getModuleName();
            String srcBaseRelativePath = "modules/" + autoCodeUiInfo.getModuleName() + "/java/";
            String testBaseRelativePath = "test/" + autoCodeUiInfo.getModuleName() + "/java/";
            String pageBaseRelativePath = VmVarViewBaseInfo.PAGE_BASE_RELATIVE_PATH;
            // domain
            autoCodeUiInfo.setPoFileFullName(StringUtil.upperFirst(pEntity.getName()) + ".java");
            autoCodeUiInfo.setPoPackageName(modulePackageName + ".domain");
            autoCodeUiInfo.setPoFileRelativePath(srcBaseRelativePath
                    + StringUtil.package2Path(autoCodeUiInfo.getPoPackageName()));
            // dao
            autoCodeUiInfo.setDaoFileFullName(StringUtil.upperFirst(pEntity.getName()) + "Dao.java");
            autoCodeUiInfo.setDaoPackageName(modulePackageName + ".dao.mybatis");
            autoCodeUiInfo.setDaoFileRelativePath(srcBaseRelativePath
                    + StringUtil.package2Path(autoCodeUiInfo.getDaoPackageName()));
            // mapper
            autoCodeUiInfo
                    .setHbmFileFullName(StringUtil.upperFirst(pEntity.getName()) + "Mapper.xml");
            autoCodeUiInfo.setHbmFileRelativePath(autoCodeUiInfo.getDaoFileRelativePath());
            // rest
            autoCodeUiInfo.setRestFileFullName(StringUtil.upperFirst(pEntity.getName())
                    + "RestController.java");
            autoCodeUiInfo.setRestPackageName(modulePackageName + ".rest");
            autoCodeUiInfo.setRestFileRelativePath(srcBaseRelativePath
                    + StringUtil.package2Path(autoCodeUiInfo.getRestPackageName()));
            // service
            autoCodeUiInfo.setServiceFileFullName(StringUtil.upperFirst(pEntity.getName())
                    + "Service.java");
            autoCodeUiInfo.setServicePackageName(modulePackageName + ".service");
            autoCodeUiInfo.setServiceFileRelativePath(srcBaseRelativePath
                    + StringUtil.package2Path(autoCodeUiInfo.getServicePackageName()));
            autoCodeUiInfo.setSrcServiceConfigFileFullName("applicationContext-"
                    + autoCodeUiInfo.getModuleName() + ".xml");
            autoCodeUiInfo.setSrcServiceConfigRelativePath("modules/"
                    + autoCodeUiInfo.getModuleName() + "/resources/spring");
            // serviceImpl
            autoCodeUiInfo.setServiceImplFileFullName(StringUtil.upperFirst(pEntity.getName())
                    + "ServiceImpl.java");
            autoCodeUiInfo.setServiceImplPackageName(modulePackageName + ".service.impl");
            autoCodeUiInfo.setServiceImplFileRelativePath(srcBaseRelativePath
                    + StringUtil.package2Path(autoCodeUiInfo.getServiceImplPackageName()));
            // serviceTest
            autoCodeUiInfo.setServiceTestFileFullName(StringUtil.upperFirst(pEntity.getName())
                    + "ServiceTest.java");
            autoCodeUiInfo.setServiceTestPackageName(autoCodeUiInfo.getServicePackageName());
            autoCodeUiInfo.setServiceTestFileRelativePath(testBaseRelativePath
                    + StringUtil.package2Path(autoCodeUiInfo.getServiceTestPackageName()));
            autoCodeUiInfo.setTestServiceConfigFileFullName("test-applicationContext-"
                    + autoCodeUiInfo.getModuleName() + ".xml");
            autoCodeUiInfo.setTestServiceConfigRelativePath("test/"
                    + autoCodeUiInfo.getModuleName() + "/resources/spring");
            // controller
            autoCodeUiInfo.setControllerFileFullName(StringUtil.upperFirst(pEntity.getName())
                    + "Controller.java");
            autoCodeUiInfo.setControllerPackageName(modulePackageName + ".controller");
            autoCodeUiInfo.setControllerFileRelativePath(srcBaseRelativePath
                    + StringUtil.package2Path(autoCodeUiInfo.getControllerPackageName()));
            autoCodeUiInfo.setBizMvcFileFullName(autoCodeUiInfo.getModuleName() + ".mvc.xml");
            autoCodeUiInfo.setBizMvcFileRelativePath("modules/" + autoCodeUiInfo.getModuleName()
                    + "/resources/config");

            String viewPath = pageBaseRelativePath + autoCodeUiInfo.getModuleName() + "/"
                    + StringUtil.lowerFirst(pEntity.getName());

            // viewList
            autoCodeUiInfo.setViewListPageFileFullName(StringUtil.lowerFirst(pEntity.getName())
                    + "List.jsp");
            autoCodeUiInfo.setViewListPageRelativePath(viewPath);
            autoCodeUiInfo.setViewListJsFileFullName(StringUtil.lowerFirst(pEntity.getName())
                    + "List.js");
            autoCodeUiInfo.setViewListJsRelativePath(viewPath);
            // viewNew
            autoCodeUiInfo.setViewNewPageFileFullName(StringUtil.lowerFirst(pEntity.getName())
                    + "Add.jsp");
            autoCodeUiInfo.setViewNewPageRelativePath(viewPath);
            autoCodeUiInfo.setViewNewJsFileFullName(StringUtil.lowerFirst(pEntity.getName())
                    + "Add.js");
            autoCodeUiInfo.setViewNewJsRelativePath(viewPath);
            // viewUpdate
            autoCodeUiInfo.setViewUpdatePageFileFullName(StringUtil.lowerFirst(pEntity.getName())
                    + "Update.jsp");
            autoCodeUiInfo.setViewUpdatePageRelativePath(viewPath);
            autoCodeUiInfo.setViewUpdateJsFileFullName(StringUtil.lowerFirst(pEntity.getName())
                    + "Update.js");
            autoCodeUiInfo.setViewUpdateJsRelativePath(viewPath);
            // viewShow
            autoCodeUiInfo.setViewShowPageFileFullName(StringUtil.lowerFirst(pEntity.getName())
                    + "Detail.jsp");
            autoCodeUiInfo.setViewShowPageRelativePath(viewPath);
            autoCodeUiInfo.setViewShowJsFileFullName(StringUtil.lowerFirst(pEntity.getName())
                    + "Detail.js");
            autoCodeUiInfo.setViewShowJsRelativePath(viewPath);
            // I18N
            autoCodeUiInfo.setI18nFileRelativePath("modules/" + autoCodeUiInfo.getModuleName()
                    + "/resources/i18n");
            String[] i18nFileNames = new String[autoCodeUiInfo.getI18nFileNames().length];
            for (int i = 0; i < autoCodeUiInfo.getI18nFileNames().length; i++) {
                i18nFileNames[i] = autoCodeUiInfo.getModuleName()
                        + autoCodeUiInfo.getI18nFileNames()[i];
            }
            autoCodeUiInfo.setI18nFileNames(i18nFileNames);
        }
    }

    /**
     * <p>
     * 把autoCodeUiInfo信息转化成autoCodeInfo
     * </p>
     * <p>
     * 针对移动端的代码生成
     * </p>
     * 
     * @param autoCodeUiInfo
     *            录入界面录入的信息
     * @return AutoCodeInfo(生成代码时需要的信息)
     */
    public static AutoCodeInfo convertAutoCodeInfoForMobile(AutoCodeUiInfo autoCodeUiInfo) {
        AutoCodeInfo autoCodeInfo = new AutoCodeInfo();
        // hbm文件的信息
        autoCodeInfo.setPoVmFilePath(autoCodeUiInfo.getPoVmFilePath());
        autoCodeInfo.setPoName(StringUtil.getFileName(autoCodeUiInfo.getPoFileFullName()));
        autoCodeInfo.setPoPackageName(autoCodeUiInfo.getPoPackageName());
        autoCodeInfo
                .setPoPath(autoCodeUiInfo.getProjectPath() + "/"
                        + autoCodeUiInfo.getPoFileRelativePath() + "/"
                        + autoCodeUiInfo.getPoFileFullName());
        return autoCodeInfo;
    }

    /**
     * <p>
     * 把autoCodeUiInfo信息转化成autoCodeInfo
     * </p>
     * 
     * @param autoCodeUiInfo
     *            录入界面录入的信息
     * @return AutoCodeInfo(生成代码时需要的信息)
     */
    public static AutoCodeInfo convertAutoCodeInfo(AutoCodeUiInfo autoCodeUiInfo) {
        AutoCodeInfo autoCodeInfo = new AutoCodeInfo();
        // hbm文件的信息
        autoCodeInfo.setPoVmFilePath(autoCodeUiInfo.getPoVmFilePath());
        autoCodeInfo.setPoName(StringUtil.getFileName(autoCodeUiInfo.getPoFileFullName()));
        autoCodeInfo.setPoPackageName(autoCodeUiInfo.getPoPackageName());
        autoCodeInfo
                .setPoPath(autoCodeUiInfo.getProjectPath() + "/"
                        + autoCodeUiInfo.getPoFileRelativePath() + "/"
                        + autoCodeUiInfo.getPoFileFullName());
        // 处理生成model 模版为空时，不生成对应的hbm文件
        if (autoCodeUiInfo.getPoVmFilePath() != null) {
            autoCodeInfo.setHbmPath(autoCodeInfo.getPoPath().replaceAll(autoCodeUiInfo
                    .getPoFileFullName(), autoCodeUiInfo.getHbmFileFullName()));
        }
        // service文件的信息
        autoCodeInfo.setServiceVmFilePath(autoCodeUiInfo.getServiceVmFilePath());
        autoCodeInfo
                .setServiceName(StringUtil.getFileName(autoCodeUiInfo.getServiceFileFullName()));
        autoCodeInfo.setServicePackageName(autoCodeUiInfo.getServicePackageName());
        autoCodeInfo.setServicePath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getServiceFileRelativePath() + "/"
                + autoCodeUiInfo.getServiceFileFullName());
        // serviceImpl文件的信息
        autoCodeInfo.setServiceImplVmFilePath(autoCodeUiInfo.getServiceImplVmFilePath());
        autoCodeInfo.setServiceImplName(StringUtil.getFileName(autoCodeUiInfo
                .getServiceImplFileFullName()));
        autoCodeInfo.setServiceImplPackageName(autoCodeUiInfo.getServiceImplPackageName());
        autoCodeInfo.setServiceImplPath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getServiceImplFileRelativePath() + "/"
                + autoCodeUiInfo.getServiceImplFileFullName());
        // servicetTest文件的信息
        autoCodeInfo.setServiceTestVmFilePath(autoCodeUiInfo.getServiceTestVmFilePath());
        autoCodeInfo.setServiceTestName(StringUtil.getFileName(autoCodeUiInfo
                .getServiceTestFileFullName()));
        autoCodeInfo.setServiceTestPackageName(autoCodeUiInfo.getServiceTestPackageName());
        autoCodeInfo.setServiceTestPath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getServiceTestFileRelativePath() + "/"
                + autoCodeUiInfo.getServiceTestFileFullName());
        autoCodeInfo.setSrcSpringConfigPath(autoCodeUiInfo.getSrcSpringConfigPath());
        autoCodeInfo.setTestSpringConfigPath(autoCodeUiInfo.getTestSpringConfigPath());
        // autoCodeInfo.setBizmvcConfigPath(autoCodeUiInfo.getBizmvcConfigPath());
        // controller
        autoCodeInfo.setControllerVmFilePath(autoCodeUiInfo.getControllerVmFilePath());
        autoCodeInfo.setControllerName(StringUtil.getFileName(autoCodeUiInfo
                .getControllerFileFullName()));
        autoCodeInfo.setControllerPackageName(autoCodeUiInfo.getControllerPackageName());
        autoCodeInfo.setControllerPath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getControllerFileRelativePath() + "/"
                + autoCodeUiInfo.getControllerFileFullName());
        autoCodeInfo.setBizMvcVmFilePath(autoCodeUiInfo.getBizMvcVmFilePath());
        autoCodeInfo.setBizmvcConfigPath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getBizMvcFileRelativePath() + "/"
                + autoCodeUiInfo.getBizMvcFileFullName());

        // viewList
        autoCodeInfo.setViewListFilePath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getViewListPageRelativePath() + "/"
                + autoCodeUiInfo.getViewListPageFileFullName());
        autoCodeInfo.setViewListVmFilePath(autoCodeUiInfo.getViewListVmFilePath());
        autoCodeInfo.setViewListJsPath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getViewListJsRelativePath() + "/"
                + autoCodeUiInfo.getViewListJsFileFullName());
        autoCodeInfo.setViewListJsVmFilePath(autoCodeUiInfo.getViewListJsVmFilePath());
        // viewNew
        autoCodeInfo.setViewNewFilePath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getViewNewPageRelativePath() + "/"
                + autoCodeUiInfo.getViewNewPageFileFullName());
        autoCodeInfo.setViewNewVmFilePath(autoCodeUiInfo.getViewNewVmFilePath());
        autoCodeInfo.setViewNewJsPath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getViewNewJsRelativePath() + "/"
                + autoCodeUiInfo.getViewNewJsFileFullName());
        autoCodeInfo.setViewNewJsVmFilePath(autoCodeUiInfo.getViewNewJsVmFilePath());
        // viewEdit
        autoCodeInfo.setViewUpdateFilePath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getViewUpdatePageRelativePath() + "/"
                + autoCodeUiInfo.getViewUpdatePageFileFullName());
        autoCodeInfo.setViewUpdateVmFilePath(autoCodeUiInfo.getViewUpdateVmFilePath());
        autoCodeInfo.setViewUpdateJsPath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getViewUpdateJsRelativePath() + "/"
                + autoCodeUiInfo.getViewUpdateJsFileFullName());
        autoCodeInfo.setViewUpdateJsVmFilePath(autoCodeUiInfo.getViewUpdateJsVmFilePath());
        // viewShow
        autoCodeInfo.setViewShowFilePath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getViewShowPageRelativePath() + "/"
                + autoCodeUiInfo.getViewShowPageFileFullName());
        autoCodeInfo.setViewShowVmFilePath(autoCodeUiInfo.getViewShowVmFilePath());
        autoCodeInfo.setViewShowJsPath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getViewShowJsRelativePath() + "/"
                + autoCodeUiInfo.getViewShowJsFileFullName());
        autoCodeInfo.setViewShowJsVmFilePath(autoCodeUiInfo.getViewShowJsVmFilePath());
        // I18N
        autoCodeInfo.setUseI18n(autoCodeUiInfo.isUseI18n());
        autoCodeInfo.setI18nConfigRelativePath(autoCodeUiInfo.getProjectPath() + "/"
                + autoCodeUiInfo.getI18nFileRelativePath());
        autoCodeInfo.setI18nFileNames(autoCodeUiInfo.getI18nFileNames());
        autoCodeInfo.setI18nVmFilePath(autoCodeUiInfo.getI18nVmFilePath());
        // 设置编码
        autoCodeInfo.setEncode(autoCodeUiInfo.getEncode());
        autoCodeInfo.setBasePackageName(autoCodeUiInfo.getBasePackageName());
        autoCodeInfo.setModuleName(autoCodeUiInfo.getModuleName());
        return autoCodeInfo;
    }
}
