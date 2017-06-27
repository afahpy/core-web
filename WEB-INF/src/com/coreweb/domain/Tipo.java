package com.coreweb.domain;

public class Tipo extends Domain {

	private String descripcion;
	private String sigla;
	private TipoTipo tipoTipo;
	
	
	
	public TipoTipo getTipoTipo() {
		return tipoTipo;
	}



	public void setTipoTipo(TipoTipo tipoTipo) {
		this.tipoTipo = tipoTipo;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}







	public String getSigla() {
		return sigla;
	}



	public void setSigla(String sigla) {
		this.sigla = sigla;
	}



	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return -1;
	}

	public String toString(){
		String out = "";
		out = "("+this.id+") "+ this.descripcion;
		return out;
	}
	
}
