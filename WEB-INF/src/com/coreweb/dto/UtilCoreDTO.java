package com.coreweb.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coreweb.util.MyPair;

@SuppressWarnings("serial")
public class UtilCoreDTO extends DTO {

	/*
	 * Hay un utilDTO que hereda de este. Lo puse acá para poder asociarlo al
	 * control general sin cruzar el core con la aplicación
	 */

	// para gestion de usuarios (vere)
	List<MyPair> habilitado = new ArrayList<MyPair>();
	
	Set<String> logueados = new HashSet<String>();

	public List<MyPair> getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(List<MyPair> habilitado) {
		this.habilitado = habilitado;
	}

	// ==== Para el manejo de propiedades del usuario

	public String getPropiedad(String key) {
		String out = "";

		return out;
	}

	// ==== Para alertas

	MyPair tipoAlertaUno = new MyPair();
	MyPair tipoAlertaComunitaria = new MyPair();
	MyPair tipoAlertaMuchos = new MyPair();
	MyPair nivelAlertaInformativa = new MyPair();
	MyPair nivelAlertaError = new MyPair();

	public MyPair getTipoAlertaUno() {
		return tipoAlertaUno;
	}

	public void setTipoAlertaUno(MyPair tipoAlertaUno) {
		this.tipoAlertaUno = tipoAlertaUno;
	}

	public MyPair getTipoAlertaComunitaria() {
		return tipoAlertaComunitaria;
	}

	public void setTipoAlertaComunitaria(MyPair tipoAlertaComunitaria) {
		this.tipoAlertaComunitaria = tipoAlertaComunitaria;
	}

	public MyPair getTipoAlertaMuchos() {
		return tipoAlertaMuchos;
	}

	public void setTipoAlertaMuchos(MyPair tipoAlertaMuchos) {
		this.tipoAlertaMuchos = tipoAlertaMuchos;
	}

	public MyPair getNivelAlertaInformativa() {
		return nivelAlertaInformativa;
	}

	public void setNivelAlertaInformativa(MyPair nivelAlertaInformativa) {
		this.nivelAlertaInformativa = nivelAlertaInformativa;
	}

	public MyPair getNivelAlertaError() {
		return nivelAlertaError;
	}

	public void setNivelAlertaError(MyPair nivelAlertaError) {
		this.nivelAlertaError = nivelAlertaError;
	}

	public Set<String> getLogueados() {
		return logueados;
	}

	public void setLogueados(Set<String> logueados) {
		this.logueados = logueados;
	}

}
