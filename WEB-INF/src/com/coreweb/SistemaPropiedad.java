package com.coreweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Lee un archivo ini en donde se pondrá información propia del sistema Puede
 * ser extendido por cada aplicación si lo cree necesario, pero no es necesario
 * invocar nada raro, sólo hay que poner los métodos especiales de nombres de
 * valores y listo.
 * 
 * @author daniel
 *
 */
public class SistemaPropiedad {

	static private Hashtable<String, String> sysPro = null;

	static private long timeFechaArchivoIni = 0;

	// por defecto busca este archivo
	static private String FILE = Config.DIRECTORIO_BASE_REAL
			+ "/WEB-INF/sistema-propiedad.ini";

	// carga las propiedades
	static {
		reloadSistemaPropiedad();
	}

	public static synchronized void reloadSistemaPropiedad() {
		reloadSistemaPropiedad(FILE);
	}

	public static synchronized void reloadSistemaPropiedad(String file) {

		// solo lee si hubo un cambio en el archivo
		File fichero = new File(file);
		long ms = fichero.lastModified();
		if (ms == timeFechaArchivoIni) {
			return;
		}
		timeFechaArchivoIni = ms;

		// si hubo un cambio, entonces hace el reload

		sysPro = new Hashtable<>();

		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(file);

			// load a properties file
			prop.load(input);
			Enumeration<Object> enu = prop.keys();
			for (; enu.hasMoreElements();) {
				String k = (String) enu.nextElement();
				String v = prop.getProperty(k);
				sysPro.put(k, v);
			}

		} catch (Exception ex) {
			System.err
					.println("==========Sistema Propiedad =====================");
			ex.printStackTrace();
			System.err
					.println("=================================================");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public boolean isControlLoginPage(){
		boolean out = true;
		String ctrLogin = this.getPropiedad("CONTROL_LOGIN");
		if (ctrLogin != null){
			out = Boolean.parseBoolean(ctrLogin);
		}
		return out;
	}
	
	
	/**
	 * Lee una propiedad del sistema-propiedad.ini, retorna null si no la
	 * encuentra
	 * 
	 * @param propiedad
	 * @return
	 */
	public String getPropiedad(String propiedad) {
		reloadSistemaPropiedad();
		String out = sysPro.get(propiedad);
		if (out != null) {
			out = out.trim();
		}
		return out;
	}
	
	/**
	 * @return el nombre de la empresa..
	 */
	public String getNombreEmpresa() {
		return this.getPropiedad("NOMBRE_EMPRESA");
	}

	public boolean getPiePagina() {
		boolean siPie = true;
		String strSiPie = this.getPropiedad("PIE_PAGINA");
		if (strSiPie != null){
			siPie = Boolean.parseBoolean(strSiPie);
		}
		return siPie;
	}

	// =====================================================
	// Configuración del correo

	public String getSmtpHost() {
		return this.getPropiedad("SMTP_HOST_NAME");
	}

	public String getSmtpPort() {
		return this.getPropiedad("SMTP_PORT");
	}

	public String getSmtpStatTlsEnable() {
		return this.getPropiedad("SMTP_START_TLS_ENABLE");
	}

	public String getEmailDefault() {
		return this.getPropiedad("EMAIL_FROM");
	}

	public String getEmailPassDefault() {
		return this.getPropiedad("EMAIL_FROM_PASSWORD");
	}

	public boolean siEnviarCorreo() {
		boolean b = Boolean.parseBoolean(this
				.getPropiedad("EMAIL_ENVIAR_CORREO"));
		return b;
	}

	// ==============================================

}
