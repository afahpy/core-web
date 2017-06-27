package com.coreweb.extras.imprimir;

public interface ReporteImpresora {

	/**
	 * Se usa para setear el número del reporte (ej, numero de la factura). Es decir, hay que implementar este método
	 * en la clase del reporte, y allí setear o darle el formato que corresponde
	 * @param numero
	 */
	public void setNumero(long numero);

	/**
	 * Traido de la interface com.coreweb.extras.reporte.ReporteInterface
	 * 
	 * @param mostrar
	 */
	public void ejecutar(boolean mostrar);

	/**
	 * Traido de la interface com.coreweb.extras.reporte.ReporteInterface
	 */
	public String getArchivoPathReal();

}
