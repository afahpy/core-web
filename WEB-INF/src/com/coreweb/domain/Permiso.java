package com.coreweb.domain;

import java.util.HashSet;
import java.util.Set;

public class Permiso extends Domain {

	private boolean habilitado = false;
	private Operacion operacion = null;
	private Perfil perfil = null;

	
	public boolean isHabilitado() {
		return habilitado;
	}


	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}


	public Operacion getOperacion() {
		return operacion;
	}


	public void setOperacion(Operacion operacion) {
		this.operacion = operacion;
	}


	public Perfil getPerfil() {
		return perfil;
	}


	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}


	public int compareTo(Object arg0) {
		System.out.println(this.getClass().getName()+" - compareTo NO IMPLEMENTADO");
		return -1;
	}
	
	public String toString(){
		String out = "";
		out += ""+this.operacion.getAlias()+": "+this.habilitado + " ["+this.operacion.getFormulario().getLabel()+"]";
		return out;
	}



}
