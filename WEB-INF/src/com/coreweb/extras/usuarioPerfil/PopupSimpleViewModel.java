package com.coreweb.extras.usuarioPerfil;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.Config;
import com.coreweb.control.SoloViewModel;

public class PopupSimpleViewModel extends SoloViewModel {

	ConfiguracionUsuarioPerfilVM dato;

	@Init(superclass = true)
	public void init(@ExecutionArgParam(Config.DATO_SOLO_VIEW_MODEL) ConfiguracionUsuarioPerfilVM dato) throws Exception {
		this.dato = dato;
	}

	@AfterCompose(superclass = true)
	public void afterCompose() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return this.dato.getAliasFormularioCorriente();
	}

	/************ GET Y SET *************/

	public ConfiguracionUsuarioPerfilVM getDato() {
		return dato;
	}

	public void setDato(ConfiguracionUsuarioPerfilVM dato) {
		this.dato = dato;
	}

}
