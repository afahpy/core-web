package com.coreweb.control;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Execution;

import com.coreweb.Config;
import com.coreweb.componente.WindowPopup;



/**
 * Se usa para los casos que tenemos un WindowPopup y queremos tener acceso a la BD y demás cosas 
 * que tienen que ver con el framework
 * Como diferencia con el SimpleViewmodel, en este caso NO se actualiza el Texto del alias del formulario
 */

public abstract class SoloViewModel extends GenericViewModel {

	
	@Init(superclass = true)
	public void initSoloViewModel(){	
	}
	

	@AfterCompose(superclass = true)
	public void afterComposeSoloViewModel(@ContextParam(ContextType.EXECUTION) Execution execution){
		String modo = (String) execution.getAttribute(Config.MODO_SOLO_VIEW_MODEL);
		WindowPopup wpu = (WindowPopup) execution.getAttribute(Config.WINDOW_POPUP);
		
		if (wpu != null){
			wpu.setVm(this);
		}else{
			wpu = new WindowPopup();
			wpu.setVm(this);
		}
		
		
		if (modo.compareTo(Config.MODO_DISABLE)==0){
			this.readonlyAllComponents();
			this.setDeshabilitado(true);
			
		}else if (modo.compareTo(Config.MODO_NO_DISABLE)==0){
			this.setDeshabilitado(false);
			// nada, ya viene editable
			
		}else if (modo.compareTo(Config.MODO_EDITABLE)==0){
			this.readonlyAllComponents();
			// ver los permisos de este usuario
			wpu.permitirEditar(true);
		}
	}
	
	
	
	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		return true;
	}

}
