package com.coreweb.domain;

public class Operacion extends Domain {

	private String alias = "alias";
	private String nombre = "nombre";
	private String descripcion = "descripcion";
	private boolean habilitado = false;
	private String idTexto = "mfo";
	private Formulario formulario = null;

	
	public int compareTo(Object arg0) {
		System.out.println(this.getClass().getName()+" - compareTo NO IMPLEMENTADO");
		return -1;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public boolean isHabilitado() {
		return habilitado;
	}


	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}


	public Formulario getFormulario() {
		return formulario;
	}


	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}


	public String getIdTexto() {
		return idTexto;
	}


	public void setIdTexto(String idTexto) {
		this.idTexto = idTexto;
	}


}
