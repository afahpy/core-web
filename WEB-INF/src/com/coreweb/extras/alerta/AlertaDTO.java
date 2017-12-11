package com.coreweb.extras.alerta;

import java.util.Date;

import org.zkoss.bind.annotation.DependsOn;

import com.coreweb.Config;
import com.coreweb.dto.DTO;
import com.coreweb.util.Misc;
import com.coreweb.util.MyPair;

public class AlertaDTO extends DTO {

	String numero = "";
	Date fechaCreacion = new Date();
	Date fechaCancelacion = new Date();
	String creador = "";
	String destino = "";
	String descripcion = "";
	String observacion = "";
	boolean cancelada;
	MyPair nivel = new MyPair();
	MyPair tipo = new MyPair();
	String propietario = "";

	@DependsOn("nivel")
	public String getNivelAlerta() {
		String out = "";
		if (this.nivel.getText().compareTo(Config.ALERTA_NIVEL_INFORMATIVO) == 0) {
			out = Config.ICONO_EXCLAMACION_YELLOW_16X16;
		}
		if (this.nivel.getText().compareTo(Config.ALERTA_NIVEL_ERROR) == 0) {
			out = Config.ICONO_EXCLAMACION_16X16;
		}
		return out;
	}

	@DependsOn("nivel")
	public String getMensajeNivel() {
		return this.nivel.getText();
	}

	@DependsOn("cancelada")
	public String getEstadoAlerta() {
		String out = "";
		if (this.cancelada == false) {
			out = Config.ICONO_ANULAR_16X16;
		}
		if (this.cancelada == true) {
			out = Config.ICONO_ACEPTAR_16X16;
		}
		return out;
	}

	@DependsOn("cancelada")
	public String getMensajeEstado() {
		return "";
	}

	public String getFechaCreacionStr() {
		return this.getMisc().dateToString(this.fechaCreacion,
				Misc.YYYY_MM_DD_HORA_MIN_SEG2);
	}

	public String getFechaCancelacionStr() {
		if (this.cancelada == false) {
			return "";
		}		
		return this.getMisc().dateToString(this.fechaCancelacion,
				Misc.YYYY_MM_DD_HORA_MIN_SEG2);
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	public String getCreador() {
		return creador;
	}

	public void setCreador(String creador) {
		this.creador = creador;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	public MyPair getNivel() {
		return nivel;
	}

	public void setNivel(MyPair nivel) {
		this.nivel = nivel;
	}

	public MyPair getTipo() {
		return tipo;
	}

	public void setTipo(MyPair tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}

}
