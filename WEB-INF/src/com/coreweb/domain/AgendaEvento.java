package com.coreweb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AgendaEvento extends Domain{
	
	private Date fecha = new Date();
	private int tipo = 0;
	private String key = "";

	private Set<AgendaEventoDetalle> agendaEventoDetalles = new HashSet<AgendaEventoDetalle>();


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public int getTipo() {
		return tipo;
	}


	public void setTipo(int tipo) {
		this.tipo = tipo;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	


	public Set<AgendaEventoDetalle> getAgendaEventoDetalles() {
		return agendaEventoDetalles;
	}


	public void setAgendaEventoDetalles(
			Set<AgendaEventoDetalle> agendaEventoDetalles) {
		this.agendaEventoDetalles = agendaEventoDetalles;
	}


	public int compareTo(Object o) {
		AgendaEvento cmp = (AgendaEvento)o;
		if ((this.tipo == cmp.tipo) && (this.key.compareTo(cmp.key)==0)){
			return 0;
		}
		return -1;
	}

}
