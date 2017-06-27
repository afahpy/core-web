package com.coreweb.domain;

public class MensajeEmail extends Domain {

	String remitente = "";
	String destinatario = "";
	String destinatarioCopia = "";
	String destinatarioCopiaOculta = "";
	String asunto = "";
	String cuerpo = "";
	String adjunto = "";
	Boolean enviado;

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

	public Boolean getEnviado() {
		return enviado;
	}

	public void setEnviado(Boolean enviado) {
		this.enviado = enviado;
	}

	@Override
	public int compareTo(Object o) {
		MensajeEmail cmp = (MensajeEmail) o;
		boolean isOk = true;

		isOk = isOk && (this.id.compareTo(cmp.id) == 0);

		if (isOk == true) {
			return 0;
		} else {
			return -1;
		}
	}
}
