package com.coreweb.extras.macros;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Textbox;

public class CustomTextbox extends Textbox{

	private static final long serialVersionUID = 1L;
	
	private static String TIPO_CELULAR = "celular";
	private static String TIPO_FECHA = "fecha";
	
	private String tipo = "";
	
	public CustomTextbox(){
		super();		
	}	
	
	@Override
	public void setValue(String value) throws WrongValueException {
	  super.setValue(value);
	}
	  
	@Override
	public String getValue() throws WrongValueException {
	  return super.getValue();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		
		if (tipo.compareTo(TIPO_CELULAR) == 0) {
			setWidgetListener("onBind", "jq(this).mask('(9999) 999-999');");
			setPlaceholder("teléfono móvil..");
			setStyle("text-align:right");
			
		} else if (tipo.compareTo(TIPO_FECHA) == 0) {
			setWidgetListener("onBind", "jq(this).mask('11/11/1111');");
			setPlaceholder("fecha..");
			setStyle("text-align:right");
		}
		
		this.tipo = tipo;
	}
}
