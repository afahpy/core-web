package com.coreweb.extras.browser;

import java.util.ArrayList;
import java.util.Hashtable;

public interface BrowserTablaDatosInterface {

	/**
	 * El id que se usa para el include
	 * @return
	 */
	public String getIdInclude();
	
	/**
	 * Define su usa una lista o usa el query
	 * @return
	 */
	public boolean ifUseList();

	/**
	 * Nombres de las columnas
	 * @return
	 */
	public String[] getNombres();

	/**
	 * Query a ejecuutar (si no usa lista)
	 * @return
	 */
	public String getQuery();

	/**
	 * lista de datos, si Usa Lista
	 * @return
	 */
	public ArrayList<Object[]> getLista();

	/**
	 * si el Query necesita parámetros
	 * @return
	 */
	public Hashtable<String, Object> getQueryParametros();
	
}
