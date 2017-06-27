package com.coreweb.extras.imprimir;

import java.util.Hashtable;
import java.util.List;

import com.coreweb.util.AutoNumeroControl;

public class ServerImpresora {

	static private Hashtable<String, ServerImpresoraSingleton> hashImpresoras = new Hashtable<>();

	/**
	 * Instancia de impresora para sincronizar las impresiones.
	 * @param nombreImpresora
	 * @return
	 */
	static public ServerImpresoraSingleton getInstancia(String nombreImpresora) {

		ServerImpresoraSingleton out = hashImpresoras.get(nombreImpresora);
		if (out == null) {
			ServerImpresoraSingleton newPr = new ServerImpresoraSingleton();
			newPr.setNombreImpresora(nombreImpresora);
			hashImpresoras.put(nombreImpresora, newPr);
			out = newPr;
		}

		return out;
	}
	
	
	/**
	 * Lista de impresoras
	 * @return
	 */
	static public List<String> getListaImpresoras(){
		Impresora imp= new Impresora();
		return imp.listaImpresoras();
	}
	

}
