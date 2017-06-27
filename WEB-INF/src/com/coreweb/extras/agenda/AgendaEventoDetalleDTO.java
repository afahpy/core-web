package com.coreweb.extras.agenda;

import java.util.Date;

import com.coreweb.dto.DTO;
import com.coreweb.util.Misc;



public class AgendaEventoDetalleDTO extends DTO {

	private Date fecha;
	private String texto;
	private int tipo;
	private String link;
	private String usuario;

	private Misc m = new Misc();

	
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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

	public String getFechaHora() {
		String out = "";
		out = m.dateToString(this.fecha,Misc.YYYY_MM_DD_HORA_MIN_SEG3);
		return out;
	}


	
}
