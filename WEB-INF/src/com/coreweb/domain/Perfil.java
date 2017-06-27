package com.coreweb.domain;

import java.util.HashSet;
import java.util.Set;

public class Perfil extends Domain {
	
	private String nombre = "nombre";
	private String descripcion = "descripcion";
	private Set<Permiso> permisos = new HashSet<Permiso>();
	private String grupo = "NO_DEFINIDO";
	
	public int compareTo(Object arg0) {
		System.out.println(this.getClass().getName()+" - compareTo NO IMPLEMENTADO");
		return -1;
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

	public Set<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(Set<Permiso> permisos) {
		this.permisos = permisos;
	}

	
	public String toString(){
		String out = "";
		out += this.nombre+ " (" + this.descripcion+")";
		return out;
	}



	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public boolean esGrupo(String grupo){
		return (this.getGrupo().compareTo(grupo)==0);
	}
	
	
}
