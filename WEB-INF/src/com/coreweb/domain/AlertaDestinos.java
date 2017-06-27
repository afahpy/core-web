package com.coreweb.domain;

import com.coreweb.domain.Domain;

public class AlertaDestinos extends Domain {

	String destinos = "";
	String descripcion = "";

	public String getDestinos() {
		return destinos;
	}

	public void setDestinos(String destinos) {
		this.destinos = destinos;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

}

