package com.coreweb.domain;

import java.io.Serializable;

public interface IiD extends Serializable {
	

	public Long getId();

	public void setId(Long id);

	public boolean esNuevo();
	
	public String getOrden();

}
