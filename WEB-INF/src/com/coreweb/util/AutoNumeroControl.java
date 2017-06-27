package com.coreweb.util;

import java.util.Date;

import com.coreweb.Config;
import com.coreweb.domain.AutoNumero;
import com.coreweb.domain.Register;

public class AutoNumeroControl {

	/***
	 * Para generar un número incremental para cada clave
	 */

	static Misc m = new Misc();
	static long SIN_LIMITE = -1;

	/**
	 * Crea uno nuevo, si ya existe da un error. Ejemplo: Sirve para no crear dos veces la misma chequera
	 * 
	 * @param key
	 * @param ini
	 * @param descripcion
	 * @throws Exception
	 */
	public synchronized static void nuevoAutoNumero(String key, long ini,
			String descripcion) throws Exception {
		setAutoNumero(key, ini, SIN_LIMITE, descripcion, true);
	}

	/**
	 * Crea uno nuevo, si ya existe da un error. Ejemplo: Sirve para no crear dos veces la misma chequera
	 * 
	 * @param key
	 * @param ini
	 * @param numeroHasta
	 * @param descripcion
	 * @throws Exception
	 */
	public synchronized static void nuevoAutoNumero(String key, long ini,
			long numeroHasta, String descripcion) throws Exception {
		setAutoNumero(key, ini, numeroHasta, descripcion, true);
	}

	
	
	public synchronized static void inicializarAutonumero(String key) throws Exception {
		Register r = Register.getInstance();
		AutoNumero an = r.getAutoNumero(key);
		if (an == null){
			an = new AutoNumero();
			an.setKey(key);
		}
		an.setNumero(an.getNumeroDesde()-1);
		r.saveObject(an, Config.USER_SYSTEMA);

	}

	
	/**
	 * Inicializa el contador, si no existe lo crea. Ejemplo: Para crear los turnos todos los días
	 * 
	 * @param key
	 * @param ini
	 * @param descripcion
	 * @throws Exception
	 */
	public synchronized static void inicializarAutonumero(String key, long ini,
			String descripcion) throws Exception {
		setAutoNumero(key, ini, SIN_LIMITE, descripcion, false);
	}

	/**
	 * Inicializa el contador, si no existe lo crea.
	 * 
	 * @param key
	 * @param ini
	 * @param descripcion
	 * @throws Exception
	 */
	public synchronized static void inicializarAutonumero(String key, long ini,
			long numeroHasta, String descripcion) throws Exception {
		setAutoNumero(key, ini, numeroHasta, descripcion, false);
	}

	
	
	private synchronized static void setAutoNumero(String key, long ini,
			long numeroHasta, String descripcion, boolean controlarExiste)
			throws Exception {

		Register r = Register.getInstance();
		AutoNumero an = r.getAutoNumero(key);

		// control de que ya existe, entonces da un error
		if ((an != null) && (controlarExiste == true)) {
			throw new Exception("Ya existe el " + key);
		}

		if (an == null) {
			an = new AutoNumero();
			an.setKey(key);
		}
		an.setNumero(ini - 1);
		an.setNumeroDesde(ini);
		an.setNumeroHasta(numeroHasta);
		an.setDescripcion(descripcion);
		r.saveObject(an, Config.USER_SYSTEMA);

	}

	
	public synchronized static long getAutoNumero(String key) throws Exception {
		return getAutoNumero(key, false);
	}

	public synchronized static long getAutoNumero(String key, boolean provisorio)
			throws Exception {
		long out = 0;
		long hasta = 0;

		Register r = Register.getInstance();
		AutoNumero an = r.getAutoNumero(key);
		if (an == null) {
			an = new AutoNumero();
			an.setKey(key);
			an.setNumero(out);
			an.setNumeroDesde(out+1);
			an.setNumeroHasta(SIN_LIMITE);
			an.setDescripcion("auto generado");
		}

		out = an.getNumero() + 1;
		hasta = an.getNumeroHasta();
		if ((hasta != SIN_LIMITE) && (out > hasta)) {
			throw new Exception("Límite de numeración excedida [" + hasta + "]");
		}

		if (provisorio == false) {
			an.setNumero(out);
			an.setFecha(new Date());
			r.saveObject(an, Config.USER_SYSTEMA);
		}

		return out;
	}

	public synchronized static String getAutoNumero(String key, int ceros)
			throws Exception {
		return getAutoNumero(key, ceros, false);
	}

	public synchronized static String getAutoNumero(String key, int ceros,
			boolean provisorio) throws Exception {
		long i = getAutoNumero(key, provisorio);
		String out = m.ceros(i + "", ceros);
		return out;
	}

	public synchronized static String getAutoNumeroKey(String key, int ceros)
			throws Exception {
		return getAutoNumeroKey(key, ceros, false);
	}

	public synchronized static String getAutoNumeroKey(String key, int ceros,
			boolean provisorio) throws Exception {
		String out = getAutoNumero(key, ceros, provisorio);
		return (key + "-" + out);
	}

}
