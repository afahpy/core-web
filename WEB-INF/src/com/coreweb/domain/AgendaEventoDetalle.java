package com.coreweb.domain;

import java.util.Date;

public class AgendaEventoDetalle extends Domain{


	private Date fecha = new Date();
	private String texto = "texto no definido";
	private int tipo = 0;
	private String link = "#";
	private String usuario = "error no definido";


	public int compareTo(Object o) {
		AgendaEventoDetalle cmp = (AgendaEventoDetalle)o;
		return (this.id.compareTo(cmp.id));
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public int getTipo() {
		return tipo;
	}


	public void setTipo(int tipo) {
		this.tipo = tipo;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	
	
	
}
