package com.coreweb.domain;

public class DeleteObjetos extends Domain {

	String usuario = "";
	String clase = "";
	long idReferencia = 0;
	String texto = "";	
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getClase() {
		return clase;
	}


	public void setClase(String clase) {
		this.clase = clase;
	}


	public long getIdReferencia() {
		return idReferencia;
	}


	public void setIdReferencia(long idReferencia) {
		this.idReferencia = idReferencia;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}

}
