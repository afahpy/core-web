package com.coreweb.extras.agenda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.coreweb.dto.DTO;

public class AgendaEventoDTO extends DTO {

	private Date fecha;
	private int tipo;
	private String key;
	private List<AgendaEventoDetalleDTO> agendaEventoDetalles = new ArrayList<AgendaEventoDetalleDTO>();

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

	public List<AgendaEventoDetalleDTO> getAgendaEventoDetalles() {
		return agendaEventoDetalles;
	}

	/*public void setAgendaEventoDetalles(
			ArrayList<AgendaEventoDetalleDTO> agendaEventoDetalles) {
		this.agendaEventoDetalles = agendaEventoDetalles;
	}*/

	public void setAgendaEventoDetalles(
			List<AgendaEventoDetalleDTO> agendaEventoDetalles) {
		this.agendaEventoDetalles = agendaEventoDetalles;
	}

}
