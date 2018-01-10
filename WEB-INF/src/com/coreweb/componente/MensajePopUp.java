package com.coreweb.componente;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.Config;

public class MensajePopUp {

	String message = "texto";

	@Init(superclass = true)
	public void initMensajePopUp(@ExecutionArgParam(Config.DATO_SOLO_VIEW_MODEL) String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
