package com.coreweb.domain;

public class UsuarioPropiedad extends Domain {

	Tipo clave;
	String valor = "";
	
	@Override
	public int compareTo(Object o) {
		System.out.println(this.getClass().getName()+" - compareTo NO IMPLEMENTADO");
		return -1;
	}

	public Tipo getClave() {
		return clave;
	}

	public void setClave(Tipo clave) {
		this.clave = clave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	
	
	
}
