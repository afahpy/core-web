package com.coreweb.domain;

import java.io.Serializable;
import java.util.Date;

import com.coreweb.dto.DBEstado;

@SuppressWarnings({ "rawtypes", "serial" })
public abstract class Domain implements Serializable, Comparable, IiD {

	protected Long id = new Long(-1);

	private char dbEstado = DBEstado.DB_EDITABLE;

	private Date modificado = new Date();

	private String usuarioMod = "popu";

	private String auxi = "";
	
	private String orden = "-1";
	
	private String ip_pc = "";
	
	
	public void xxdefinirOrden(){
		this.orden = String.valueOf(id);
	}
	
	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden){
		this.orden = orden;
	}
	
	public String getUsuarioMod() {
		return usuarioMod;
	}

	public void setUsuarioMod(String usuarioMod) {
		this.usuarioMod = usuarioMod;
	}

	public boolean esNuevo() {
		return (id < 1);
	}

	
	
	public String getAuxi() {
		return auxi;
	}

	public void setAuxi(String auxi) {
		this.auxi = auxi;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
 	}

	public Date getModificado() {
		return modificado;
	}

	public void setModificado(Date modificado) {
		this.modificado = modificado;
	}

	public char getDbEstado() {
		return dbEstado;
	}

	public void setDbEstado(char dbEstado) {
		this.dbEstado = dbEstado;
	}

	public String getIp_pc() {
		return ip_pc;
	}

	public void setIp_pc(String ip) {
		this.ip_pc = ip;
	}

}
