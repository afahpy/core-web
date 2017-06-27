package com.coreweb.extras.imprimir;

import com.coreweb.util.AutoNumeroControl;

public class ServerImpresoraSingleton {

//	ReporteImpresora rep;
	String nombreImpresora = "";

	
	protected ServerImpresoraSingleton() {
	}

	/**
	 * El siguiente número. Es provisorio, no se graba.
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getSiguienteNumeroProvisorio() throws Exception {
		return AutoNumeroControl.getAutoNumero(this.nombreImpresora, true);
	}

	/**
	 * Para imprimir un pdf. Sirve para imprimir un pdf generado en el servidor,
	 * por ejemplo, usando una tablet.
	 * 
	 * @param pathPdf
	 * @throws Exception
	 */
	public synchronized void imprimirPdf(String pathPdf) throws Exception {
		Impresora imp = checkImpresora();
		imp.imprimirPdf(pathPdf, nombreImpresora);
	}

	public synchronized long imprimirReporteNumero(ReporteImpresora rep) throws Exception {

		Impresora imp = checkImpresora();

		// chequear que se haya seteado el reporte
		if (rep == null) {
			throw new Exception("No se seteo el objeto ReporteImpresora para la impresora [" + nombreImpresora + "]");
		}

		// buscar el número a imprimir
		long nro = AutoNumeroControl.getAutoNumero(this.nombreImpresora);

		// setear el número en el reporte
		rep.setNumero(nro);

		// crear el PDF
		rep.ejecutar(false);

		// imprimir
		imp.imprimirPdf(rep.getArchivoPathReal(), nombreImpresora);

		// retornar número
		return nro;
	}

	private Impresora checkImpresora() throws Exception {
		// chequer que exista la impresora
		Impresora imp = new Impresora();
		if (imp.existe(nombreImpresora) == false) {
			throw new Exception("No existe la impresora [" + nombreImpresora + "]");
		}
		return imp;
	}

/*
public ReporteImpresora getRep() {
		return rep;
	}

	public void setRep(ReporteImpresora rep) {
		this.rep = rep;
	}
*/
	public String getNombreImpresora() {
		return nombreImpresora;
	}

	protected void setNombreImpresora(String nombreImpresora) {
		this.nombreImpresora = nombreImpresora;
	}

}