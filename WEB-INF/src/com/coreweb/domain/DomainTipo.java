package com.coreweb.domain;

public class DomainTipo extends Domain {

	private String descripcion = "";
	
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int compareTo(Object o) {
		DomainTipo dt = (DomainTipo)o;
		return this.descripcion.compareTo(dt.getDescripcion());
	}

}
