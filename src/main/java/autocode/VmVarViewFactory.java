package autocode;

import autocode.util.auxiliary.vm.SingleTableViewFactory;
import autocode.util.auxiliary.vm.VmVarBase;
import autocode.util.auxiliary.vm.relationtable.RelationTableViewFactory;
import dataset.model.IEntity;
import dataset.model.IPEntity;
import dataset.model.IVEntity;
import dataset.viewmodel.IForm;

public class VmVarViewFactory {
	
	public static VmVarBase createVmVarView(IEntity entity,IForm form){
		if(entity instanceof IVEntity){
			return RelationTableViewFactory.createVmVarViewInfo((IVEntity)entity, form);
		}else if(entity instanceof IPEntity){
			return SingleTableViewFactory.createVmVarViewInfo((IPEntity)entity, form);
		}
		return null;
	}
}
