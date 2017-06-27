package com.coreweb.templateABM.casoBase;

import com.coreweb.dto.DTO;


public class BodyDatoDto extends DTO {

	
	String nombre = "";
	String apellido = "";
	String ruc = ""+System.currentTimeMillis();
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	
	public String getMtring(){
		String out = this.apellido + " - " + this.nombre + " - " + this.ruc + " [" + this.getId()+"]";
		System.out.println("out: "+out);
		return out;
	}
	
	public String toString(){
		String out = this.apellido + " - " + this.nombre + " - " + this.ruc + " [" + this.getId()+"]";
		System.out.println("out: "+out);
		return out;
	}
	
	
	
}
