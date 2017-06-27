package com.coreweb.domain;

import java.util.HashSet;
import java.util.Set;

public class Modulo extends Domain {
	
	private String nombre = "nommbre modulo";
	private String descripcion = "descripcion";
	
	
	private Set<Formulario> formularios = new HashSet<Formulario>();
	
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



	public Set<Formulario> getFormularios() {
		return formularios;
	}


	public void setFormularios(Set<Formulario> formularios) {
		this.formularios = formularios;
	}




	
	public int compareTo(Object arg0) {
		System.out.println(this.getClass().getName()+" - compareTo NO IMPLEMENTADO");
		return -1;
	}
	
	

}
