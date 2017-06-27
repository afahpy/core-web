package com.coreweb.extras.email;

public class CuerpoCorreo {
	
	String remitente = "";
	String clave = "";
	String destinatario = "";
	String destinatarioCopia = "";
	String destinatarioCopiaOculta = "";
	String asunto = "";
	String cuerpo = "";	
	String adjunto = "";
	
	public CuerpoCorreo(String remitente, String clave, String cuerpo) {
		this.remitente = remitente;
		this.cuerpo = cuerpo;
	}
	
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getDestinatarioCopia() {
		return destinatarioCopia;
	}
	public void setDestinatarioCopia(String destinatarioCopia) {
		this.destinatarioCopia = destinatarioCopia;
	}
	public String getDestinatarioCopiaOculta() {
		return destinatarioCopiaOculta;
	}
	public void setDestinatarioCopiaOculta(String destinatarioCopiaOculta) {
		this.destinatarioCopiaOculta = destinatarioCopiaOculta;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public String getCuerpo() {
		return cuerpo;
	}
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public String getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}


}
