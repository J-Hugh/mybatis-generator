package autocode.util.auxiliary.vm.relationtable;

import autocode.model.AutoCodeInfo;
import autocode.util.StringUtil;
import dataset.model.IEntity;
import dataset.model.IField;
import dataset.model.IVEntity;

public class VmVarDaoInfo extends VmVarJavaBaseInfo {

	private AutoCodeInfo autoCodeInfo;

	public VmVarDaoInfo(IVEntity entity, AutoCodeInfo autoCodeInfo) {
		super(entity);
		this.autoCodeInfo = autoCodeInfo;
		addImportTypes(autoCodeInfo.getPoPackageName() + "." + autoCodeInfo.getPoName());
		setPackageName(autoCodeInfo.getDaoPackageName());
		setClassName(StringUtil.lowerFirst(autoCodeInfo.getDaoName()));
		setPoFirstUpperName(StringUtil.upperFirst(autoCodeInfo.getPoName()));
		setPoPKFirstLowerName(StringUtil.lowerFirst(getPKFieldNameReal(entity)));
		setPoPKName(getPKFieldNameReal(entity));
	}
	
	
	public String getDaoPackageName() {
        return autoCodeInfo.getDaoPackageName();
    }

	public String getUpperDaoName() {
		return autoCodeInfo.getDaoName();
	}

	public String getDaoLowerFirstName() {
		return StringUtil.lowerFirst(autoCodeInfo.getDaoName());
	}
	
	public String getPKFieldNameReal(IEntity entity) {
		return ((IField) (entity.getPKFields().iterator().next())).getName();
	}

}
