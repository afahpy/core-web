package com.coreweb.util;


public class MyPair extends MyAuxObject {

	// private Long id = new Long(-1);
	private String text = " ";
	private String sigla = "";

	public MyPair() {
	}
	
	public MyPair(long id) {
		this.setId(id);
	}

	public MyPair(long id, String text) {
		this.setId(id);
		this.text = text;
	}

	public MyPair(long id, String text, String sigla) {
		this.setId(id);
		this.text = text;
		this.sigla = sigla;
	}
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String toString() {
		String out = this.text ; // + " (" + this.getId() + ")";
		if (this.esNuevo() == true) {
			out = " ";
		}
		return out;
	}

	public Object xclone() {
		Object clone = null;
		clone = super.xclone();
		return clone;
	}

}
