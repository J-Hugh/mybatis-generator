/**
 * $Id: VmVarInfo.java,v 1.4 2013/04/19 09:51:25 zhanghp Exp $
 */
package autocode.util.auxiliary.vm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autocode.util.StringUtil;

/**
 * <p>
 * ��vm�ļ�ʹ�õı���ֵ������Ϣ
 * </p>
 * 
 * @author ResourceOne BizFoundation Team: ganjp
 * @version 1.0
 * @since 4.3
 */
public class VmVarInfo {

	// ---------��������Ϣ
	public static final String MODULE_NAME = "MODULE_NAME"; // ģ����
	public static final String PACKAGE_NAME = "PACKAGE_NAME"; // ����
	public static final String PO_FIELD_COMMENT = "PO_FIELD_COMMENT"; //ע��
	public static final String CLASS_FIRST_UPPER_NAME = "CLASS_FIRST_UPPER_NAME"; // ��һ����ĸ��д������
	public static final String CLASS_FIRST_LOWER_NAME = "CLASS_FIRST_LOWER_NAME"; // ��һ����ĸСд������
	public static final String CLASS_IMPORT_TYPES = "CLASS_IMPORT_TYPES"; // ��Ҫ������ֶμ�
	public static final String CLASS_EXTENTS_CLASS = "CLASS_EXTENTS_CLASS"; // ��Ҫ�̳е���
	public static final String PACKAGE_SUBNAME = "PACKAGE_SUBNAME";
	public static final String CLASS_IMPLEMENTS_INTERFACES = "CLASS_IMPLEMENTS_INTERFACES"; // ��Ҫʵ�ֵĽӿ�

	private String moduleName = ""; // ģ����
	private String packageName = ""; // ����
	private String classFirstUpperName = ""; // ��һ����ĸ��д������
	private String classFirstLowerName = ""; // ��һ����ĸСд������
	private List classImportTypes = Collections.EMPTY_LIST; // ��Ҫimport����
	private String classExtentsClass = ""; // ��Ҫ�̳е���
	private String classImplementInterfaces = "";// ��Ҫʵ�ֵĽӿ�

	// ---------ʹ�õ�PO��Ϣ
	public static final String PO_FIRST_UPPER_NAME = "PO_FIRST_UPPER_NAME"; // ��һ����ĸ��д�ĳ־û�������
	public static final String PO_FIRST_LOWER_NAME = "PO_FIRST_LOWER_NAME"; // ��һ����ĸСд�ĳ־û�������
	public static final String PO_PK_FIRST_UPPER_NAME = "PO_PK_FIRST_UPPER_NAME"; // ��һ����ĸ��д�ĳ־û������������
	public static final String PO_PK_FIRST_LOWER_NAME = "PO_PK_FIRST_LOWER_NAME"; // ��һ����ĸСд�ĳ־û������������
	public static final String PO_PK_TYPE = "PO_PK_TYPE"; // �־û��������������
	public static final String PO_PK_SIMPLE_TYPE = "PO_PK_SIMPLE_TYPE"; // �־û���������ļ�����
	public static final String PO_IS_COMPOSITE_PK = "PO_IS_COMPOSITE_PK"; // �Ƿ��Ǹ�������
	public static final String VM_VAR_INFO = "VmVarInfo";
	public static final String PO_PK_NAME = "PO_PK_NAME";
	public static final String PO_FK_FIRST_UPPER_NAME = "PO_FK_FIRST_UPPER_NAME"; // ��һ����ĸ��д�ĳ־û�����������
	public static final String PO_FK_FIRST_LOWER_NAME = "PO_FK_FIRST_LOWER_NAME"; // ��һ����ĸСд�ĳ־û�����������

	public static final String PK_NAME_FOR_ONEPK = "PK_NAME_FOR_ONEPK";
	public String pkNameForOnePk = ""; // ������

	// ------ʹ��DAO����Ϣ
	public static final String DAO_FIRST_UPPER_NAME = "DAO_FIRST_UPPER_NAME"; // ��һ����ĸ��д�ĳ־û�������
	public static final String DAO_FIRST_LOWER_NAME = "DAO_FIRST_LOWER_NAME"; // ��һ����ĸСд�ĳ־û�������

	// ------ʹ��Service����Ϣ
	public static final String SERVICE_FIRST_UPPER_NAME = "SERVICE_FIRST_UPPER_NAME"; // ��һ����ĸ��д�ĳ־û�������
	public static final String SERVICE_FIRST_LOWER_NAME = "SERVICE_FIRST_LOWER_NAME"; // ��һ����ĸСд�ĳ־û�������

	public static final String TABLE_NAME = "TABLE_NAME";
	private String tableName = "";// ����

	private String daoFirstUpperName = ""; // ��һ����ĸ��д�ĳ־û��������
	private String daoFirstLowerName = ""; // ��һ����ĸСд�ĳ־û��������

	// ------ʹ��DAO����Ϣ
	private String serviceFirstUpperName = ""; // ��һ����ĸ��д�ĳ־û��������
	private String serviceFirstLowerName = ""; // ��һ����ĸСд�ĳ־û��������

	// ���Ӷ����ѯ���ֶεļ��� by bruce 20121207
	public static final String PO_QUERY_EXT_FIELD_NAMES = "PO_QUERY_EXT_FIELD_NAMES"; // ʵ������ж����ѯ���ֶμ���
	public static final String PO_QUERY_EXT_FIELD_FIRST_UPPER_NAMES = "PO_QUERY_EXT_FIELD_FIRST_UPPER_NAMES"; // ʵ������ж����ѯ���ֶμ���
	public static final String PO_QUERY_EXT_FIELD_TYPES = "PO_QUERY_EXT_FIELD_TYPES"; // ʵ������ж����ѯ���ֶμ���

	public static final String PO_FIELD_NAMES = "PO_FIELD_NAMES"; // �־û������ֶ����
	public static final String PO_FIELD_CAPITAL_NAMES = "PO_FIELD_CAPITAL_NAMES"; // �־û������ֶ����д����
	public static final String PO_FIELD_FIRST_UPPER_NAMES = "PO_FIELD_FIRST_UPPER_NAMES";// �־û������һ����ĸ��д���ֶ����
	public static final String PO_FIELD_TYPES = "PO_FIELD_TYPES"; // �־û������ֶ����ͼ���
	public static final String PO_FIELD_HIBERNATE_SIMPLE_TYPES = "PO_FIELD_HIBERNATE_SIMPLE_TYPES"; // �־û�����������ֶ�hibernate���ͼ���
	public static final String PO_FIELD_ROBASE_SIMPLE_TYPES = "PO_FIELD_ROBASE_SIMPLE_TYPES"; // �־û�����������ֶ�robase���ͼ���
	public static final String PO_FIELD_IS_PERSISTS = "PO_FIELD_IS_PERSISTS";// ��Ӧ���ֶ��Ƿ���Ҫ�־û�
	public static final String PO_FIELD_NOTBLANKS = "PO_FIELD_NOTBLANKS";// ��Ӧ���ֶ��Ƿ����Ϊ��

	// ���ӱ��ϵ
	public static final String PO_DETAILS_FOR_MASTER_FIRST_UPPER = "PO_DETAILS_FOR_MASTER_FIRST_UPPER"; // ���ӱ��ϵ��,�����дӱ�ļ������Լ���
	public static final String PO_DETAILS_FOR_MASTER_FIRST_LOWER = "PO_DETAILS_FOR_MASTER_FIRST_LOWER"; // ���ӱ��ϵ��,�����дӱ�ļ������Լ���
	public static final String IS_MASTER_ENTITY = "IS_MASTER_ENTITY"; // �Ƿ�����ʵ��

	// �����дӱ�List����
	private List PoDetailsFirstUpperForMaster = Collections.EMPTY_LIST; // ���ӱ��ϵ��,�����дӱ�ļ������Լ���
	private List PoDetailsFirstLowerForMaster = Collections.EMPTY_LIST; // ���ӱ��ϵ��,�����дӱ�ļ������Լ���
	private boolean isMasterEntity = false;
	
	//���ӱ�service����
	public static final String DETAIL_DAO_FIRST_UPPER_NAME = "DETAIL_DAO_FIRST_UPPER_NAME"; // �ӱ��һ����ĸ��д��Dao��
	public static final String DETAIL_DAO_FIRST_LOWER_NAME = "DETAIL_DAO_FIRST_LOWER_NAME"; // �ӱ��һ����ĸСд��Dao��
	public static final String DETAIL_PO_PK_FIRST_UPPER_NAME = "DETAIL_PO_PK_FIRST_UPPER_NAME"; // �ӱ��һ����ĸ��д��Dao��
	public static final String DETAIL_PO_PK_FIRST_LOWER_NAME = "DETAIL_PO_PK_FIRST_LOWER_NAME"; // �ӱ��һ����ĸСд��Dao��
	public static final String RELATION_DETAIL_FIELD_FIRST_LOWER_NAME = "RELATION_DETAIL_FIELD_FIRST_LOWER_NAME"; // �����дӱ�����Ӧ���ֶ���һ����ĸСд
	public static final String RELATION_DETAIL_FIELD_FIRST_UPPER_NAME = "RELATION_DETAIL_FIELD_FIRST_UPPER_NAME"; // �����дӱ�����Ӧ���ֶ���һ����ĸ��д
	public static final String RELATION_MASTER_FIELD_FIRST_LOWER_NAME = "RELATION_MASTER_FIELD_FIRST_LOWER_NAME"; // �����дӱ�����Ӧ���ֶ���һ����ĸСд
	public static final String RELATION_MASTER_FIELD_FIRST_UPPER_NAME = "RELATION_MASTER_FIELD_FIRST_UPPER_NAME"; // �����дӱ�����Ӧ���ֶ���һ����ĸ��д

	private String detailDaoFirstUpperName = ""; // �ӱ��һ����ĸ��д��Dao���
	private String detailDaoFirstLowerName = ""; // �ӱ��һ����ĸСд��Dao���
	private String detailPoPkFirstUpperName = ""; // �ӱ��һ����ĸ��д��������
	private String detailPoPkFirstLowerName = ""; // �ӱ��һ����ĸСд��������
	private String relationDetailFieldFirstUpperName = ""; // �����дӱ�����Ӧ���ֶ���һ����ĸ��д
	private String relationDetailFieldFirstLowerName = ""; // �����дӱ�����Ӧ���ֶ���һ����ĸСд
	private String relationMasterFieldFirstUpperName = ""; // ��������������Ӧ���ֶ���һ����ĸ��д
	private String relationMasterFieldFirstLowerName = ""; // ��������������Ӧ���ֶ���һ����ĸСд
	
	
	public static final String DETAIL_PO_FIRST_UPPER_NAME = "DETAIL_PO_FIRST_UPPER_NAME"; // �ӱ��һ����ĸ��д�ĳ־û�������
	public static final String DETAIL_PO_FIRST_LOWER_NAME = "DETAIL_PO_FIRST_LOWER_NAME"; // �ӱ��һ����ĸСд�ĳ־û�������

	private String detailPOFirstUpperName = ""; // �ӱ��һ����ĸ��д�ĳ־û��������
	private String detailPOFirstLowerName = ""; // �ӱ��һ����ĸСд�ĳ־û��������

	private String poFirstUpperName = ""; // ��һ����ĸ��д�ĳ־û��������
	private String poFirstLowerName = ""; // ��һ����ĸСд�ĳ־û��������
	private String poPkFirstUpperName = ""; // ��һ����ĸ��д�ĳ־û������������
	private String poPkFirstLowerName = ""; // ��һ����ĸСд�ĳ־û������������
	private String poPkType = ""; // �־û���������ļ�����
	private String poPkSimpleType = ""; // �־û���������ļ�����
	private boolean poCompositePk = false; // �Ƿ��Ǹ�������
	private String poFkFirstUpperName = ""; // ��һ����ĸ��д�ĳ־û�����������
	private String poFkFirstLowerName = ""; // ��һ����ĸСд�ĳ־û�����������

	// �����getter��setter��Ϣ
	private String poPkGetterStr = "";
	private String poPkSetterStr = "";

	// �����ѯ���ֶε����ģ����� by bruce 20121207
	private List poQueryExtFieldNames = Collections.EMPTY_LIST; // ʵ���ж���Ķ���Ĳ�ѯ���ֶ����
	private List poQueryExtFieldFirstUpperNames = Collections.EMPTY_LIST; // ʵ���ж���Ķ���Ĳ�ѯ���ֶ����һ����Ļ��д����
	private List poQueryExtFieldTypes = Collections.EMPTY_LIST; // ʵ���ж���Ķ���Ĳ�ѯ���ֶ����ͼ���

	// ����¼�����ֶε����ģ�����
	private List poEditExtFieldNames = Collections.EMPTY_LIST; // ʵ���ж���Ķ����¼�����ֶ����
	private List poEditExtFieldOriginNames = Collections.EMPTY_LIST; // ʵ���ж���Ķ����¼����ԭʼ�ֶ����
	private List poEditExtFieldFirstUpperNames = Collections.EMPTY_LIST; // ʵ���ж���Ķ����¼�����ֶ����һ����ĸ��д����
	private List poEditExtFieldTypes = Collections.EMPTY_LIST; // ʵ���ж���Ķ����¼�����ֶ����ͼ���

	private List poFieldNames = Collections.EMPTY_LIST; // �־û������ֶ����
	private List poFieldCapitalNames = Collections.EMPTY_LIST; // �־û������ֶ����
	private List poFieldFirstUpperNames = Collections.EMPTY_LIST; // �־û������һ����ĸ��д���ֶ����
	private List poFieldTypes = Collections.EMPTY_LIST; // �־û������ֶ����ͼ���
	private List poFieldHibernateSimpleTypes = Collections.EMPTY_LIST; // �־û�����������ֶ�hibernate���ͼ���
	private List poFieldRobaseSimpleTypes = Collections.EMPTY_LIST; // �־û�����������ֶ�robase���ͼ���
	private List poFieldIsPersists = Collections.EMPTY_LIST; // ��Ӧ���ֶ��Ƿ���Ҫ�־û�
	private List poFieldNotBlanks = Collections.EMPTY_LIST; // ��Ӧ�ֶ��Ƿ�����Ϊ��
	private VmVarPoPkInfo vmVarPoPkInfo = null; // ����������������ֵ

	// .by liutsh.2011.11.09
	private Map extendPropertys = Collections.EMPTY_MAP; // ��չ����

	// ---------ʹ�õ���������Ϣ
	public static final String IMPORT_CLASS_FIRST_UPPER_NAME = "IMPORT_CLASS_FIRST_UPPER_NAME"; // �����һ����ĸ��д������
	public static final String IMPORT_CLASS_FIRST_LOWER_NAME = "IMPORT_CLASS_FIRST_LOWER_NAME"; // �����һ����ĸСд������
	private String importClassFirstUpperName = ""; // �������һ����ĸ��д�����
	private String importClassFirstLowerName = ""; // �������һ����ĸСд�����

	// MVC�����ļ���ʹ�õ���ͼURI
	private String viewListURI = "";
	private String viewNewURI = "";
	private String viewUpdateURI = "";
	private String viewShowURI = "";

	public static final String VIEW_LIST_URI = "VIEW_LIST_URI";
	public static final String VIEW_NEW_URI = "VIEW_NEW_URI";
	public static final String VIEW_UPDATE_URI = "VIEW_UPDATE_URI";
	public static final String VIEW_SHOW_URI = "VIEW_SHOW_URI";

	// ����ʵ����Ϣ�б�
	public static final String RELATION_ENTITY_INFO_LIST = "RELATION_ENTITY_INFO_LIST";
	private List relEntityInfoList = Collections.EMPTY_LIST;

	public static final String ENTITY_NAME = "ENTITY_NAME";

	// 2012/6/27 chenhua BizAnyWhere ���ҵ��ģ�Ͷ�����Ҫ��SqL���
	public static final String MOBILE_MODULE_SQL = "MOBILE_MODULE_SQL"; // ģ����

	private String MobileModuleSql = ""; // ģ����

	public String getMobileModuleSql() {
		return MobileModuleSql;
	}

	public void setMobileModuleSql(String mobileModuleSql) {
		MobileModuleSql = mobileModuleSql;
	}

	// �Ƿ���Ҫ���List��Add��Edit��Showҳ���Ӧ��Java����
	private boolean genListView = false;
	private boolean genAddView = false;
	private boolean genEditView = false;
	private boolean genShowView = false;
	// ��ģ�������ƶ��壩�Ƿ���Ҫ���List��Add��Edit��Showҳ���Ӧ��Java����
	public static final String GEN_LIST_VIEW = "GEN_LIST_VIEW";
	public static final String GEN_ADD_VIEW = "GEN_ADD_VIEW";
	public static final String GEN_EDIT_VIEW = "GEN_EDIT_VIEW";
	public static final String GEN_SHOW_VIEW = "GEN_SHOW_VIEW";

	// �Ƿ���Ҫ��ɵ���Excel��Java����
	private boolean exportExcel = false;
	public static final String EXPORT_EXCEL = "EXPORT_EXCEL";

	// ---------��ʱ����
	private String entityName = ""; // ʹ��Dateset��ʵ�����
	private String fileNameAndType = ""; // ��ɴ������ļ�����ƺ��ļ�����
	private String outFilePath = ""; // ����ļ���·��(�����ļ���)
	private String vmFilePath = ""; // vmģ���·��
	private String registBeanName = ""; // ��ע�뵽�����ļ������
	private String registClass = ""; // ��Ҫע�����
	private String registFile = ""; // ��Ҫע�ᵽ�ļ�

	public String getModuleName() {
		return moduleName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassFirstUpperName() {
		return classFirstUpperName;
	}

	public void setClassFirstUpperName(String classFirstUpperName) {
		this.classFirstUpperName = classFirstUpperName;
	}

	public String getClassFirstLowerName() {
		return classFirstLowerName;
	}

	public void setClassFirstLowerName(String classFirstLowerName) {
		this.classFirstLowerName = classFirstLowerName;
	}

	public String getClassExtentsClass() {
		return classExtentsClass;
	}

	public void setClassExtentsClass(String classExtentsClass) {
		this.classExtentsClass = classExtentsClass;
	}

	public String getPoFirstUpperName() {
		return poFirstUpperName;
	}

	public void setPoFirstUpperName(String poFirstUpperName) {
		this.poFirstUpperName = poFirstUpperName;
	}

	public String getPoFirstLowerName() {
		return poFirstLowerName;
	}

	public void setPoFirstLowerName(String poFirstLowerName) {
		this.poFirstLowerName = poFirstLowerName;
	}

	public String getPoPkFirstUpperName() {
		return poPkFirstUpperName;
	}

	public void setPoPkFirstUpperName(String poPkFirstUpperName) {
		this.poPkFirstUpperName = poPkFirstUpperName;
	}

	public String getPoPkFirstLowerName() {
		return poPkFirstLowerName;
	}

	public void setPoPkFirstLowerName(String poPkFirstLowerName) {
		this.poPkFirstLowerName = poPkFirstLowerName;
	}

	public String getPoPkType() {
		return poPkType;
	}

	public void setPoPkType(String poPkType) {
		this.poPkType = poPkType;
	}

	public String getPoPkSimpleType() {
		return poPkSimpleType;
	}

	public void setPoPkSimpleType(String poPkSimpleType) {
		this.poPkSimpleType = poPkSimpleType;
	}

	public String getPoPkGetterStr() {
		return poPkGetterStr;
	}

	public void setPoPkGetterStr(String poPkGetterStr) {
		this.poPkGetterStr = poPkGetterStr;
	}

	public String getPoPkSetterStr() {
		return poPkSetterStr;
	}

	public void setPoPkSetterStr(String poPkSetterStr) {
		this.poPkSetterStr = poPkSetterStr;
	}

	public boolean isPoCompositePk() {
		return poCompositePk;
	}

	public void setPoCompositePk(boolean poCompositePk) {
		this.poCompositePk = poCompositePk;
	}

	public VmVarPoPkInfo getVmVarPoPkInfo() {
		return vmVarPoPkInfo;
	}

	public void setVmVarPoPkInfo(VmVarPoPkInfo vmVarPoPkInfo) {
		this.vmVarPoPkInfo = vmVarPoPkInfo;
	}

	public String getImportClassFirstUpperName() {
		return importClassFirstUpperName;
	}

	public void setImportClassFirstUpperName(String importClassFirstUpperName) {
		this.importClassFirstUpperName = importClassFirstUpperName;
	}

	public String getImportClassFirstLowerName() {
		return importClassFirstLowerName;
	}

	public void setImportClassFirstLowerName(String importClassFirstLowerName) {
		this.importClassFirstLowerName = importClassFirstLowerName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFileNameAndType() {
		return fileNameAndType;
	}

	public void setFileNameAndType(String fileNameAndType) {
		this.fileNameAndType = fileNameAndType;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getClassImplementInterfaces() {
		return classImplementInterfaces;
	}

	public void setClassImplementInterfaces(String classImplementInterfaces) {
		this.classImplementInterfaces = classImplementInterfaces;
	}

	public List getClassImportTypes() {
		return classImportTypes;
	}

	public void setClassImportTypes(List classImportTypes) {
		this.classImportTypes = classImportTypes;
	}

	public void addClassImportType(String classImportType) {
		if (this.classImportTypes == null || this.classImportTypes.isEmpty()) {
			this.classImportTypes = new ArrayList();
		}
		this.classImportTypes.add(classImportType);
	}

	// ������Entity����ɶ���Ĳ�ѯ���ֶ� by bruce 20121206
	public List getPoQueryExtFieldNames() {
		if (this.poQueryExtFieldNames == null
				|| this.poQueryExtFieldNames.isEmpty()) {
			this.poQueryExtFieldNames = new ArrayList();
		}
		return poQueryExtFieldNames;
	}

	public void setPoQueryExtFieldNames(List poQueryExtFieldNames) {
		this.poQueryExtFieldNames = poQueryExtFieldNames;
	}

	public List getPoQueryExtFieldFirstUpperNames() {
		if (this.poQueryExtFieldFirstUpperNames == null
				|| this.poQueryExtFieldFirstUpperNames.isEmpty()) {
			this.poQueryExtFieldFirstUpperNames = new ArrayList();
		}
		return poQueryExtFieldFirstUpperNames;
	}

	public void setPoQueryExtFieldFirstUpperNames(
			List poQueryExtFieldFirstUpperNames) {
		this.poQueryExtFieldFirstUpperNames = poQueryExtFieldFirstUpperNames;
	}

	public List getPoQueryExtFieldTypes() {
		if (this.poQueryExtFieldTypes == null
				|| this.poQueryExtFieldTypes.isEmpty()) {
			this.poQueryExtFieldTypes = new ArrayList();
		}
		return poQueryExtFieldTypes;
	}

	public void setPoQueryExtFieldTypes(List poQueryExtFieldTypes) {
		this.poQueryExtFieldTypes = poQueryExtFieldTypes;
	}

	// ������Entity����ɶ����¼�����ֶ�
	public List getPoEditExtFieldNames() {
		if (this.poEditExtFieldNames == null
				|| this.poEditExtFieldNames.isEmpty()) {
			this.poEditExtFieldNames = new ArrayList();
		}
		return poEditExtFieldNames;
	}

	public void setPoEditExtFieldNames(List poEditExtFieldNames) {
		this.poEditExtFieldNames = poEditExtFieldNames;
	}

	public List getPoEditExtFieldOriginNames() {
		if (this.poEditExtFieldOriginNames == null
				|| this.poEditExtFieldOriginNames.isEmpty()) {
			this.poEditExtFieldOriginNames = new ArrayList();
		}
		return poEditExtFieldOriginNames;
	}

	public void setPoEditExtFieldOriginNames(List poEditExtFieldOriginNames) {
		this.poEditExtFieldOriginNames = poEditExtFieldOriginNames;
	}

	public List getPoEditExtFieldFirstUpperNames() {
		if (this.poEditExtFieldFirstUpperNames == null
				|| this.poEditExtFieldFirstUpperNames.isEmpty()) {
			this.poEditExtFieldFirstUpperNames = new ArrayList();
		}
		return poEditExtFieldFirstUpperNames;
	}

	public void setPoEditExtFieldFirstUpperNames(
			List poEditExtFieldFirstUpperNames) {
		this.poEditExtFieldFirstUpperNames = poEditExtFieldFirstUpperNames;
	}

	public List getPoEditExtFieldTypes() {
		if (this.poEditExtFieldTypes == null
				|| this.poEditExtFieldTypes.isEmpty()) {
			this.poEditExtFieldTypes = new ArrayList();
		}
		return poEditExtFieldTypes;
	}

	public void setPoEditExtFieldTypes(List poEditExtFieldTypes) {
		this.poQueryExtFieldTypes = poEditExtFieldTypes;
	}

	public List getPoFieldNames() {
		return poFieldNames;
	}

	public void setPoFieldNames(List poFieldNames) {
		this.poFieldNames = poFieldNames;
	}

	public void addPoFieldName(String poFieldName) {
		if (this.poFieldNames == null || this.poFieldNames.isEmpty()) {
			this.poFieldNames = new ArrayList();
			this.poFieldCapitalNames = new ArrayList();
		}
		this.poFieldNames.add(poFieldName);
		this.poFieldCapitalNames.add(poFieldName.toUpperCase());
	}

	public List getPoDetailsFirstUpperForMaster() {
		return PoDetailsFirstUpperForMaster;
	}

	public void setPoDetailsFirstUpperForMaster(
			List poDetailsFirstUpperForMaster) {
		PoDetailsFirstUpperForMaster = poDetailsFirstUpperForMaster;
	}

	public void addPoDetailsFirstUpperForMaster(String detailForMaster) {
		if (this.PoDetailsFirstUpperForMaster == null
				|| this.PoDetailsFirstUpperForMaster.isEmpty()) {
			this.PoDetailsFirstUpperForMaster = new ArrayList();
		}
		this.PoDetailsFirstUpperForMaster.add(StringUtil
				.upperFirstFormatJavaName(detailForMaster));
	}

	public List getPoDetailsFirstLowerForMaster() {
		return PoDetailsFirstLowerForMaster;
	}

	public void setPoDetailsFirstLowerForMaster(
			List poDetailsFirstLowerForMaster) {
		PoDetailsFirstLowerForMaster = poDetailsFirstLowerForMaster;
	}

	public void addPoDetailsFirstLowerForMaster(String detailForMaster) {
		if (this.PoDetailsFirstLowerForMaster == null
				|| this.PoDetailsFirstLowerForMaster.isEmpty()) {
			this.PoDetailsFirstLowerForMaster = new ArrayList();
		}
		this.PoDetailsFirstLowerForMaster.add(StringUtil
				.lowerFirst(detailForMaster));
	}

	public List getPoFieldCapitalNames() {
		return poFieldCapitalNames;
	}

	public void setPoFieldCapitalNames(List poFieldCapitalNames) {
		this.poFieldCapitalNames = poFieldCapitalNames;
	}

	public List getPoFieldFirstUpperNames() {
		return poFieldFirstUpperNames;
	}

	public void setPoFieldFirstUpperNames(List poFieldFirstUpperNames) {
		this.poFieldFirstUpperNames = poFieldFirstUpperNames;
	}

	public void addPoFieldFirstUpperName(String poFieldFirstUpperName) {
		if (this.poFieldFirstUpperNames == null
				|| this.poFieldFirstUpperNames.isEmpty()) {
			this.poFieldFirstUpperNames = new ArrayList();
		}
		this.poFieldFirstUpperNames.add(poFieldFirstUpperName);
	}

	public List getPoFieldTypes() {
		return poFieldTypes;
	}

	public void setPoFieldTypes(List poFieldTypes) {
		this.poFieldTypes = poFieldTypes;
	}

	public void addPoFieldType(String poFieldType) {
		if (this.poFieldTypes == null || this.poFieldTypes.isEmpty()) {
			this.poFieldTypes = new ArrayList();
		}
		this.poFieldTypes.add(poFieldType);
	}

	public List getPoFieldHibernateSimpleTypes() {
		return poFieldHibernateSimpleTypes;
	}

	public void setPoFieldHibernateSimpleTypes(List poFieldHibernateSimpleTypes) {
		this.poFieldHibernateSimpleTypes = poFieldHibernateSimpleTypes;
	}

	public void addPoFieldHibernateSimpleType(String poFieldHibernateSimpleType) {
		if (this.poFieldHibernateSimpleTypes == null
				|| this.poFieldHibernateSimpleTypes.isEmpty()) {
			this.poFieldHibernateSimpleTypes = new ArrayList();
		}
		this.poFieldHibernateSimpleTypes.add(poFieldHibernateSimpleType);
	}

	public List getPoFieldRobaseSimpleTypes() {
		return poFieldRobaseSimpleTypes;
	}

	public void setPoFieldRobaseSimpleTypes(List poFieldRobaseSimpleTypes) {
		this.poFieldRobaseSimpleTypes = poFieldRobaseSimpleTypes;
	}

	public void addPoFieldRobaseSimpleType(String poFieldRobaseSimpleType) {
		if (this.poFieldRobaseSimpleTypes == null
				|| this.poFieldRobaseSimpleTypes.isEmpty()) {
			this.poFieldRobaseSimpleTypes = new ArrayList();
		}
		this.poFieldRobaseSimpleTypes.add(poFieldRobaseSimpleType);
	}

	public List getPoFieldNotBlanks() {
		return poFieldNotBlanks;
	}

	public void setPoFieldNotBlanks(List poFieldNotBlanks) {
		this.poFieldNotBlanks = poFieldNotBlanks;
	}

	public void addPoFieldNotBlank(boolean poFieldNotBlank) {
		if (this.poFieldNotBlanks == null || this.poFieldNotBlanks.isEmpty()) {
			this.poFieldNotBlanks = new ArrayList();
		}
		this.poFieldNotBlanks.add(poFieldNotBlank);
	}

	public List getPoFieldIsPersists() {
		return poFieldIsPersists;
	}

	public void setPoFieldIsPersists(List poFieldIsPersists) {
		this.poFieldIsPersists = poFieldIsPersists;
	}

	public void addPoFieldIsPersist(boolean poFieldIsPersist) {
		if (this.poFieldIsPersists == null || this.poFieldIsPersists.isEmpty()) {
			this.poFieldIsPersists = new ArrayList();
		}
		this.poFieldIsPersists.add(new Boolean(poFieldIsPersist));
	}

	public String getOutFilePath() {
		return outFilePath;
	}

	public void setOutFilePath(String outFilePath) {
		this.outFilePath = outFilePath;
	}

	public String getVmFilePath() {
		return vmFilePath;
	}

	public void setVmFilePath(String vmFilePath) {
		this.vmFilePath = vmFilePath;
	}

	public String getRegistBeanName() {
		return registBeanName;
	}

	public void setRegistBeanName(String registBeanName) {
		this.registBeanName = registBeanName;
	}

	public String getRegistClass() {
		return registClass;
	}

	public void setRegistClass(String registClass) {
		this.registClass = registClass;
	}

	public String getRegistFile() {
		return registFile;
	}

	public void setRegistFile(String registFile) {
		this.registFile = registFile;
	}

	public String getViewListURI() {
		return viewListURI;
	}

	public void setViewListURI(String viewListURI) {
		this.viewListURI = viewListURI;
	}

	public String getViewNewURI() {
		return viewNewURI;
	}

	public void setViewNewURI(String viewNewURI) {
		this.viewNewURI = viewNewURI;
	}

	public String getViewUpdateURI() {
		return viewUpdateURI;
	}

	public void setViewUpdateURI(String viewUpdateURI) {
		this.viewUpdateURI = viewUpdateURI;
	}

	public String getViewShowURI() {
		return viewShowURI;
	}

	public void setViewShowURI(String viewShowURI) {
		this.viewShowURI = viewShowURI;
	}

	public List getRelEntityInfoList() {
		return relEntityInfoList;
	}

	public void setRelEntityInfoList(List relEntityInfoList) {
		this.relEntityInfoList = relEntityInfoList;
	}

	// .by liutsh.2011.11.09
	public Map getExtendPropertys() {
		return extendPropertys;
	}

	public void setExtendPropertys(Map extendPropertys) {
		this.extendPropertys = extendPropertys;
	}

	public void addExtendProperty(String propertyName, Object propertyValue) {
		if (this.extendPropertys == null || this.extendPropertys.isEmpty()) {
			this.extendPropertys = new HashMap();
		}
		this.extendPropertys.put(propertyName, propertyValue);
	}

	public boolean isGenListView() {
		return genListView;
	}

	public void setGenListView(boolean genListView) {
		this.genListView = genListView;
	}

	public boolean isGenAddView() {
		return genAddView;
	}

	public void setGenAddView(boolean genAddView) {
		this.genAddView = genAddView;
	}

	public boolean isGenEditView() {
		return genEditView;
	}

	public void setGenEditView(boolean genEditView) {
		this.genEditView = genEditView;
	}

	public boolean isGenShowView() {
		return genShowView;
	}

	public void setGenShowView(boolean genShowView) {
		this.genShowView = genShowView;
	}

	public boolean isExportExcel() {
		return exportExcel;
	}

	public void setExportExcel(boolean exportExcel) {
		this.exportExcel = exportExcel;
	}

	public String getJavaBeanFieldName(String fieldName) {
		return StringUtil.getJavaBeanFieldName(fieldName);
	}

	public String getDaoFirstUpperName() {
		return daoFirstUpperName;
	}

	public void setDaoFirstUpperName(String daoFirstUpperName) {
		this.daoFirstUpperName = daoFirstUpperName;
	}

	public String getDaoFirstLowerName() {
		return daoFirstLowerName;
	}

	public void setDaoFirstLowerName(String daoFirstLowerName) {
		this.daoFirstLowerName = daoFirstLowerName;
	}

	/**
	 * ��ȡ serviceFirstUpperName
	 * 
	 * @return ���� serviceFirstUpperName
	 */
	public String getServiceFirstUpperName() {
		return serviceFirstUpperName;
	}

	/**
	 * ���� serviceFirstUpperName
	 * 
	 * @param ��serviceFirstUpperName���и�ֵ
	 */
	public void setServiceFirstUpperName(String serviceFirstUpperName) {
		this.serviceFirstUpperName = serviceFirstUpperName;
	}

	/**
	 * ��ȡ serviceFirstLowerName
	 * 
	 * @return ���� serviceFirstLowerName
	 */
	public String getServiceFirstLowerName() {
		return serviceFirstLowerName;
	}

	/**
	 * ���� serviceFirstLowerName
	 * 
	 * @param ��serviceFirstLowerName���и�ֵ
	 */
	public void setServiceFirstLowerName(String serviceFirstLowerName) {
		this.serviceFirstLowerName = serviceFirstLowerName;
	}

	public String getPkNameForOnePk() {
		return pkNameForOnePk;
	}

	public void setPkNameForOnePk(String pkNameForOnePk) {
		this.pkNameForOnePk = pkNameForOnePk;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isMasterEntity() {
		return isMasterEntity;
	}

	public void setMasterEntity(boolean isMasterEntity) {
		this.isMasterEntity = isMasterEntity;
	}

	public String getPoFkFirstUpperName() {
		return poFkFirstUpperName;
	}

	public void setPoFkFirstUpperName(String poFkFirstUpperName) {
		this.poFkFirstUpperName = poFkFirstUpperName;
	}

	public String getPoFkFirstLowerName() {
		return poFkFirstLowerName;
	}

	public void setPoFkFirstLowerName(String poFkFirstLowerName) {
		this.poFkFirstLowerName = poFkFirstLowerName;
	}

	public String getDetailDaoFirstUpperName() {
		return detailDaoFirstUpperName;
	}

	public void setDetailDaoFirstUpperName(String detailDaoFirstUpperName) {
		this.detailDaoFirstUpperName = detailDaoFirstUpperName;
	}

	public String getDetailDaoFirstLowerName() {
		return detailDaoFirstLowerName;
	}

	public void setDetailDaoFirstLowerName(String detailDaoFirstLowerName) {
		this.detailDaoFirstLowerName = detailDaoFirstLowerName;
	}

	public String getDetailPOFirstUpperName() {
		return detailPOFirstUpperName;
	}

	public void setDetailPOFirstUpperName(String detailPOFirstUpperName) {
		this.detailPOFirstUpperName = detailPOFirstUpperName;
	}

	public String getDetailPOFirstLowerName() {
		return detailPOFirstLowerName;
	}

	public void setDetailPOFirstLowerName(String detailPOFirstLowerName) {
		this.detailPOFirstLowerName = detailPOFirstLowerName;
	}

	public String getDetailPoPkFirstUpperName() {
		return detailPoPkFirstUpperName;
	}

	public void setDetailPoPkFirstUpperName(String detailPoPkFirstUpperName) {
		this.detailPoPkFirstUpperName = detailPoPkFirstUpperName;
	}

	public String getDetailPoPkFirstLowerName() {
		return detailPoPkFirstLowerName;
	}

	public void setDetailPoPkFirstLowerName(String detailPoPkFirstLowerName) {
		this.detailPoPkFirstLowerName = detailPoPkFirstLowerName;
	}

	public String getRelationDetailFieldFirstUpperName() {
		return relationDetailFieldFirstUpperName;
	}

	public void setRelationDetailFieldFirstUpperName(
			String relationDetailFieldFirstUpperName) {
		this.relationDetailFieldFirstUpperName = relationDetailFieldFirstUpperName;
	}

	public String getRelationDetailFieldFirstLowerName() {
		return relationDetailFieldFirstLowerName;
	}

	public void setRelationDetailFieldFirstLowerName(
			String relationDetailFieldFirstLowerName) {
		this.relationDetailFieldFirstLowerName = relationDetailFieldFirstLowerName;
	}

	public String getRelationMasterFieldFirstUpperName() {
		return relationMasterFieldFirstUpperName;
	}

	public void setRelationMasterFieldFirstUpperName(
			String relationMasterFieldFirstUpperName) {
		this.relationMasterFieldFirstUpperName = relationMasterFieldFirstUpperName;
	}

	public String getRelationMasterFieldFirstLowerName() {
		return relationMasterFieldFirstLowerName;
	}

	public void setRelationMasterFieldFirstLowerName(
			String relationMasterFieldFirstLowerName) {
		this.relationMasterFieldFirstLowerName = relationMasterFieldFirstLowerName;
	}
}
