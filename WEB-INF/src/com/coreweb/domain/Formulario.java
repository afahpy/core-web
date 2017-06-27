package com.coreweb.domain;

import java.util.HashSet;
import java.util.Set;

public class Formulario extends Domain {

	private String label = "labelFormulario";
	private String descripcion = "descripcion de que hace el formulario";
	private String url = "/misc/noDefinido.zul?label="+label;
	private String alias = "alias";
	private boolean habilitado = false; // Permite habilitar o deshabilitar un formulario
	
	private Set<Operacion> operaciones = new HashSet<Operacion>();
	
	
	public int compareTo(Object arg0) {
		System.out.println(this.getClass().getName()+" - compareTo NO IMPLEMENTADO");
		return -1;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public boolean isHabilitado() {
		return habilitado;
	}


	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}


	public Set<Operacion> getOperaciones() {
		return operaciones;
	}


	public void setOperaciones(Set<Operacion> operaciones) {
		this.operaciones = operaciones;
	}
	
	

}
