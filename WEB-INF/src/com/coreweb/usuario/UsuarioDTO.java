package com.coreweb.usuario;

import java.util.ArrayList;
import java.util.List;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;


public class UsuarioDTO extends DTO {

	List<MyArray> usuarios = new ArrayList<MyArray>();
	List<MyArray> perfiles = new ArrayList<MyArray>();
	List<MyArray> modulos = new ArrayList<MyArray>();
	

	public List<MyArray> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<MyArray> usuarios) {
		this.usuarios = usuarios;
	}

	public List<MyArray> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<MyArray> perfiles) {
		this.perfiles = perfiles;
	}

	public List<MyArray> getModulos() {
		return modulos;
	}

	public void setModulos(List<MyArray> modulos) {
		this.modulos = modulos;
	}
	
	
	

}
