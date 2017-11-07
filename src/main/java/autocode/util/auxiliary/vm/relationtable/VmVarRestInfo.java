package autocode.util.auxiliary.vm.relationtable;

import autocode.model.AutoCodeInfo;
import autocode.util.StringUtil;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IVEntity;

public class VmVarRestInfo extends VmVarJavaBaseInfo {

	private AutoCodeInfo autoCodeInfo;
	
	protected String serviceFirstLowerName; //service 类名(第一个字母小写)
    
    protected String serviceFirstUpperName; //service 类名 (第一个字母大写)

	public VmVarRestInfo(IVEntity entity, AutoCodeInfo autoCodeInfo) {
		super(entity);
		this.autoCodeInfo = autoCodeInfo;
		setPackageName(autoCodeInfo.getRestPackageName());
		setClassName(autoCodeInfo.getRestName());
		addImportTypes(autoCodeInfo.getServicePackageName() + "." + autoCodeInfo.getServiceName());
		addImportTypes(autoCodeInfo.getPoPackageName() + "." + autoCodeInfo.getPoName());
		setPoFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getPoName()));
		setPoFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getPoName()));
//		setPoPKName(getPKFieldNameReal(entity));
		setPoPKFirstLowerName(StringUtil.lowerFirst(getPKFieldNameReal(entity)));
		setServiceFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getServiceName()));
		setServiceFirstLowerName(StringUtil.lowerFirst(autoCodeInfo.getServiceName()));
		
	}
	
	public String getServiceFirstLowerName() {
		return serviceFirstLowerName;
	}

	public void setServiceFirstLowerName(String serviceFirstLowerName) {
		this.serviceFirstLowerName = serviceFirstLowerName;
	}

	public String getServiceFirstUpperName() {
		return serviceFirstUpperName;
	}

	public void setServiceFirstUpperName(String serviceFirstUpperName) {
		this.serviceFirstUpperName = serviceFirstUpperName;
	}

	public String getPKFieldNameReal(IEntity entity) {
		return ((IField) (entity.getPKFields().iterator().next())).getName();
	}
}
