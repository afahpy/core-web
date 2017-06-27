package com.coreweb.extras.macros;

import org.zkoss.zul.Doublebox;

public class CustomDoublebox extends Doublebox{

	private static final long serialVersionUID = 1L;
	
	private static String TIPO_GUARANI = "guarani";
	private static String TIPO_MONEDA_EXTRANJERA = "extranjera";	
	
	private static String FORMAT_LOCAL = "###,###,###,##0";
	private static String FORMAT_EXTRANJERA = "###,###,##0.00";
	
	private String tipo = "";
	private String simbolo = "";	
	
	public CustomDoublebox(){
		super();
	}	
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		
		if (tipo.compareTo(TIPO_GUARANI) == 0) {
			setWidgetListener("onBind", "jq(this).mask('999,999,999,990', {reverse: true});");	
			
		} else if (tipo.compareTo(TIPO_MONEDA_EXTRANJERA) == 0) {
			setWidgetListener("onBind", "jq(this).mask('999,999,999.00', {reverse: true});");
		} 
		
		this.tipo = tipo;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {		
		
		if (tipo.compareTo(TIPO_GUARANI) == 0) {
			setFormat(FORMAT_LOCAL + " " + simbolo);
			
		} else if (tipo.compareTo(TIPO_MONEDA_EXTRANJERA) == 0) {
			setFormat(FORMAT_EXTRANJERA + " " + simbolo);
			
		} else {
			setFormat("Error símbolo");
		}
		this.simbolo = simbolo;
	}	

	@Override
	public Double getValue() {
		return super.getValue();
	}

	@Override
	public void setValue(Double value) {
		super.setValue(value);
	}
}
