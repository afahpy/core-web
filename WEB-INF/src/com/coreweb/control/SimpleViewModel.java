package com.coreweb.control;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Execution;

/**
 * Para ser usada en ventanas que no pertenecen a un template, por ejemplo, WindowsPopup
 *
 */

public abstract class SimpleViewModel extends GenericViewModel {

	
	@Init(superclass = true)
	public void initSimpleViewModel(@ContextParam(ContextType.EXECUTION) Execution execution){		
		String aliasForm = (String) execution.getAttribute("aliasForm");
		String labelF = this.getUs().formLabel(aliasForm);
		this.setTextoFormularioCorriente(labelF);
		this.setAliasFormularioCorriente(aliasForm);
		this.setDeshabilitado(false);
	}
	

	@AfterCompose(superclass = true)
	public void afterComposeSimpleViewModel(){
		this.addCamposObligotorios(this.mainComponent);	
	}
	
	
	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		
		return true;
	}

}
