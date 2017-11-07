package autocode.util.auxiliary.vm;

import autocode.util.auxiliary.vm.VmVarBase;
import dataset.model.IPEntity;
import dataset.viewmodel.IDetailInfoForm;
import dataset.viewmodel.IForm;
import dataset.viewmodel.INewForm;
import dataset.viewmodel.IQueryForm;
import dataset.viewmodel.IResultForm;

public class SingleTableViewFactory{

	public static VmVarBase createVmVarViewInfo(IPEntity entity, IForm form) {
		if(entity == null || form == null){
			return null;
		}
		if(form instanceof IQueryForm || form instanceof IResultForm){
			return new VmVarViewListInfo(entity);
		}else if(form instanceof INewForm){
			return new VmVarViewFormInfo(entity);
		}else if(form instanceof IDetailInfoForm){
			return new VmVarViewShowInfo(entity);
		}
		return null;
	}
	
	
}
