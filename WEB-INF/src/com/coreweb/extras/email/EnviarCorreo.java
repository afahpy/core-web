package com.coreweb.extras.email;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.coreweb.SistemaPropiedad;
import com.coreweb.domain.MensajeEmail;
import com.coreweb.domain.Register;

public class EnviarCorreo {


	SistemaPropiedad sisPro = new SistemaPropiedad();

	public void sendMessage(String recipients[], String[] recipientsCCO,
			String subject, String message) throws Exception {

		sendMessage(recipients, new String[] { "" }, recipientsCCO, subject,
				message, this.sisPro.getEmailDefault(),
				this.sisPro.getEmailPassDefault(), null, null);
	}

	public void sendMessage(String[] recipients, String[] recipientsCC,
			String[] recipientsCCO, String subject, String message,
			String from, String pass, String fileName, String path)
			throws Exception {

		if (this.sisPro.siEnviarCorreo() == false) {
			return;
		}

		boolean debug = false;
		Properties props = new Properties();

		props.put("mail.smtp.host", this.sisPro.getSmtpHost());
		props.put("mail.smtp.port", this.sisPro.getSmtpPort());
		props.put("mail.smtp.starttls.enable",
				this.sisPro.getSmtpStatTlsEnable());
		props.put("mail.smtp.auth", "true");

		/*
		 * Session session = Session.getDefaultInstance(props, new
		 * javax.mail.Authenticator() { protected PasswordAuthentication
		 * getPasswordAuthentication() { return new PasswordAuthentication(from,
		 * emailFromPasswrd); } });
		 */

		Session session = Session.getDefaultInstance(props,
				new MiAuthenticator(from, pass));

		session.setDebug(debug);

		BodyPart mensaje = new MimeBodyPart();
		mensaje.setText(message);

		MimeMultipart multiParte = new MimeMultipart();
		multiParte.addBodyPart(mensaje);

		if (path != null) {
			BodyPart adjunto = new MimeBodyPart();
			adjunto.setDataHandler(new DataHandler(new FileDataSource(path)));
			adjunto.setFileName(fileName);
			multiParte.addBodyPart(adjunto);
		}

		Message msg = new MimeMessage(session);
		// aqui cambiar la cuenta, para que reciba como
		// parametro..provisoriamente usa esta cuenta
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// verifica si destinatario CC no es vacio
		if (recipientsCC[0].isEmpty() == false) {

			InternetAddress[] addressToCC = new InternetAddress[recipientsCC.length];
			for (int i = 0; i < recipientsCC.length; i++) {
				addressToCC[i] = new InternetAddress(recipientsCC[i]);
			}
			msg.setRecipients(Message.RecipientType.CC, addressToCC);

		}

		// verifica si destinatario CCO no es vacio
		if (recipientsCCO[0].isEmpty() == false) {

			InternetAddress[] addressToCCO = new InternetAddress[recipientsCCO.length];
			for (int i = 0; i < recipientsCCO.length; i++) {
				addressToCCO[i] = new InternetAddress(recipientsCCO[i]);
			}
			msg.setRecipients(Message.RecipientType.BCC, addressToCCO);

		}

		// Configura el asunto y el tipo de contenido
		msg.setSubject(subject);
		msg.setContent(multiParte);

		// para que se registren los emails enviados
		boolean enviado = false;
		MensajeEmail mensajeEmail = new MensajeEmail();
		mensajeEmail.setRemitente(from);
		mensajeEmail.setDestinatario(this.arrayToString(recipients));
		mensajeEmail.setDestinatarioCopia(this.arrayToString(recipientsCC));
		mensajeEmail.setDestinatarioCopiaOculta(this.arrayToString(recipientsCCO));
		mensajeEmail.setAsunto(subject);
		mensajeEmail.setCuerpo(message);
		mensajeEmail.setAdjunto(fileName);
		
		try {
			Transport.send(msg);
			enviado = true;
			
		} catch (Exception e) {

			throw e;

		} finally {
			
			mensajeEmail.setEnviado(enviado);
			Register rr = Register.getInstance();
			rr.saveObject(mensajeEmail, "Sis");
			
		}
		
		

	}
	
	private String arrayToString(String [] elementos){
		String elemento = "";
		for (int i = 0; i < elementos.length; i++) {
			elemento += elementos[i] + ",";
		}
		elemento = elemento.substring(0, elemento.length() - 1);
		return elemento;
		
	}

	public static void main(String[] args) throws Exception {
		
		String [] v = {"asdf","jklz","mnopq"};
		EnviarCorreo elem = new EnviarCorreo();
		System.out.println("String es: " + elem.arrayToString(v));
		
		if (1 == 1) {
			return;
		}

		System.out.println("inicio");
		try {
			System.out.println("try");

			int x = 10 / 0;
			
			
		} catch (Exception e) {
			System.out.println("catch");
			throw e;

		} finally {
			System.out.println("finally");
		}
		System.out.println("terminó");


		if (1 == 1) {
			return;
		}

		try {

			SistemaPropiedad
					.reloadSistemaPropiedad("/home/daniel/datos/varios/propuestas/scg33/proyecto-scg33/scg33/WEB-INF/sistema-propiedad.ini");

			System.out.println("==== paso =====");
			EnviarCorreo co = new EnviarCorreo();
			String[] recipients = { "daniel.omar.romero@gmail.com" };
			String[] recipientsCC = { "" };
			String[] recipientsCCO = { "" };
			String subject = "asunto va";
			String message = "mensaje";
			String from = "daniel@vidrioluz.com.py";
			String pass = "qwerty";
			String fileName = null;
			String path = null;

			co.sendMessage(recipients, recipientsCC, recipientsCCO, subject,
					message, from, pass, fileName, path);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class MiAuthenticator extends javax.mail.Authenticator {

	String from = "desde";
	String pass = "clave";

	public MiAuthenticator(String from, String pass) {
		this.from = from;
		this.pass = pass;

	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.from, this.pass);
	}
}
