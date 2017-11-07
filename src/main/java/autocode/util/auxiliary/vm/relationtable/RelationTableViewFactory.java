package autocode.util.auxiliary.vm.relationtable;

import autocode.VmVarViewFactory;
import autocode.util.auxiliary.vm.VmVarBase;
import dataset.model.IVEntity;
import dataset.viewmodel.IDetailInfoForm;
import dataset.viewmodel.IForm;
import dataset.viewmodel.INewForm;
import dataset.viewmodel.IQueryForm;
import dataset.viewmodel.IResultForm;

public class RelationTableViewFactory{

	public static VmVarBase createVmVarViewInfo(IVEntity vEntity, IForm form) {
		if(vEntity == null || form == null){
			return null;
		}
		if(form instanceof IQueryForm || form instanceof IResultForm){
			return new VmVarViewListInfo(vEntity);
		}else if(form instanceof INewForm){
			return new VmVarViewFormInfo(vEntity);
		}else if(form instanceof IDetailInfoForm){
			return new VmVarViewShowInfo(vEntity);
		}
		return null;
	}
	
	
}
