package com.coreweb.modulo;

import java.util.ArrayList;
import java.util.List;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;


public class ModuloDTO extends DTO {

	List<MyArray> modulos = new ArrayList<MyArray>();
	List<Object> listaAliasFormularios = new ArrayList<Object>();
	List<Object> listaAliasOperaciones = new ArrayList<Object>();
	List<Object> listaIdTextoOperaciones = new ArrayList<Object>();
	List<Object> listaNombresModulos = new ArrayList<Object>();

	public List<MyArray> getModulos() {
		return modulos;
	}

	public void setModulos(List<MyArray> modulos) {
		this.modulos = modulos;
	}

	public List<Object> getListaAliasFormularios() {
		return listaAliasFormularios;
	}

	public void setListaAliasFormularios(List<Object> listaAliasFormularios) {
		this.listaAliasFormularios = listaAliasFormularios;
	}

	public List<Object> getListaAliasOperaciones() {
		return listaAliasOperaciones;
	}

	public void setListaAliasOperaciones(List<Object> listaAliasOperaciones) {
		this.listaAliasOperaciones = listaAliasOperaciones;
	}

	public List<Object> getListaIdTextoOperaciones() {
		return listaIdTextoOperaciones;
	}

	public void setListaIdTextoOperaciones(List<Object> listaIdTextoOperaciones) {
		this.listaIdTextoOperaciones = listaIdTextoOperaciones;
	}

	public List<Object> getListaNombresModulos() {
		return listaNombresModulos;
	}

	public void setListaNombresModulos(List<Object> listaNombresModulos) {
		this.listaNombresModulos = listaNombresModulos;
	}
	
	

	
	
}
