/**La Estructura de entrada es un String[] de cabecera, String[] de detalle y
 * direccion del archivo CVS.
 * Tendremos un Objeto con: Cabecera en una tabla hash (contenidoCab),
 * Cabecera de Detalle en tabla hash (hd) y el Detalle en un ArrayList
 * de String (contenidoDetalle)
 */
package com.coreweb.extras.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import com.csvreader.CsvReader;

public class CSV {
	// Atributos

	static private char DELIMITADOR_DEFAULT = ',';
	private static boolean debug = false;

	private char delimitador = DELIMITADOR_DEFAULT;

	// Datos de Cabecera en la Clase CSV
	private Hashtable<String, String> contenidoCab = new Hashtable<String, String>();
	// posicionCabDetalle tendra las posiciones en ArrayList de los detalles
	private Hashtable<String, Integer> posicionCabDetalle = new Hashtable<String, Integer>();
	private ArrayList<Object[]> contenidoDetalle = new ArrayList<Object[]>();
	private Hashtable<String, String> hc = new Hashtable<>(); // para cargar la
																// cabecera de
	// entrada
	private Hashtable<String, String> hd = new Hashtable<>(); // para cargar
																// cabecera
																// detalle de
	// entrada
	private String direccion = new String();
	CsvReader fileImport;

	public static final String STRING = "String";
	public static final String NUMERICO = "Numerico";
	public static final String DATE = "Date";

	Iterator<Object[]> ite;
	Object[] lineaRecorrido;

	// Constructor
	public CSV(String[][] cab, String[][] cabDet, String nombreDir) throws Exception {
		// hace el parser
		leerArchivo(cab, cabDet, nombreDir, DELIMITADOR_DEFAULT);
	}

	// Constructor
	public CSV(String[][] cab, String[][] cabDet, String nombreDir, char delimitador) throws Exception {
		// hace el parser
		leerArchivo(cab, cabDet, nombreDir, delimitador);
	}

	private void leerArchivo(String[][] cab, String[][] cabDet, String nombreDir, char delimitador)
			throws FileNotFoundException, Exception {
		this.setDelimitador(delimitador);
		String key, value;
		String[] dato;
		direccion = nombreDir;

		for (int i = 0; i < cab.length; i++) {
			dato = cab[i];
			key = dato[0];
			value = dato[1];
			hc.put(key, value);
		}

		for (int j = 0; j < cabDet.length; j++) {
			dato = cabDet[j];
			key = dato[0];
			value = dato[1];
			hd.put(key, value);
		}

		fileImport = new CsvReader(direccion, this.delimitador, StandardCharsets.UTF_8);
		fileImport.setDelimiter(this.delimitador);
		try {
			cabecera();
			detalle();
		} catch (Exception ex) {
			throw ex;

		} finally {
			fileImport.close();
		}
	}

	/*
	 * Despues de leer el archivo CSV pregunta por los campos si son los tipos
	 * de datos esperados
	 */
	private void cabecera() throws Exception {
		String[] ll; // guarda la linea del archivo CVS

		try {
			boolean lineaBlanco = false;

			while ((lineaBlanco == false) && fileImport.readRecord()) {
				ll = fileImport.getValues();
				String campo = ll[0];
				String valor = ll[1];

				// para saber que SI estoy en una linea en blanco
				if (campo.trim().length() == 0) {
					lineaBlanco = true;
				} else {
					String tipo = (String) this.hc.get(campo);
					System.out.println("------- tipo:" + tipo + "    valor:" + valor);
					comprobarTipo(tipo, valor); // Comprueba el tipo de
												// dato esperado
					contenidoCab.put(campo, valor);

				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Despues de leer el archivo CSV preguntar por los campos si son los tipos
	 * de datos esperados comparando en hashC
	 */
	private void detalle() throws Exception {
		String[] ll;
		float[] verificar; // ir guardando los totales
		int[] numericos; // si 0 NO numerico, si 1 ES numerico
		String[] arrayTipos;

		fileImport.readRecord();
		ll = fileImport.getValues();
		String[] detalleCabecera = ll; // una copia de ll para no perder la
										// cabecera detalle
		arrayTipos = new String[ll.length];
		verificar = new float[ll.length];
		numericos = new int[ll.length];

		// busca los tipos de los campos del detalle
		for (int i = 0; i < ll.length; i++) {
			String keyCampo = ll[i];
			printLog("(" + i + ")" + keyCampo);
			String tipo = (String) hd.get(keyCampo);
			if (tipo == null) {
				String err = "--- error, no encuentra el tipo de (" + keyCampo + ") posicion i:" + i;
				printLogln("\n" + err);
				throw new Exception(err);
			}

			arrayTipos[i] = tipo;
			this.posicionCabDetalle.put(keyCampo, i); // i = indice de cabecera
														// detalle

			// saber en que columnas hay numericos
			if (tipo.compareTo(CSV.NUMERICO) == 0) {
				numericos[i] = 1;
			}
		}

		// leemos los detalles
		Object[] lineaTipo;
		boolean lineaBlanca = false;
		while ((lineaBlanca == false) && (fileImport.readRecord())) {
			ll = fileImport.getValues();

			printLinea(ll);

			if (ll[0].trim().length() == 0) {
				lineaBlanca = true;
			} else {
				// tratamos la linea
				lineaTipo = comprobarTipoLinea(arrayTipos, ll);
				this.contenidoDetalle.add(lineaTipo);

				// ir sumando para controlar
				for (int i = 0; i < ll.length; i++) {
					if (numericos[i] == 1) {
						if (ll[i].trim().compareTo("") == 0) {
							ll[i] = "0";
						}
						float valor = parserNumero(ll[i]);
						verificar[i] = verificar[i] + valor;
					}
				}

			}

		}

		// controlar los totales
		fileImport.readRecord();
		ll = fileImport.getValues();
		for (int k = 0; k < ll.length; k++) {
			String tipoVariable = arrayTipos[k];
			if (tipoVariable.compareTo(CSV.NUMERICO) == 0) {
				float numero = parserNumero(ll[k]);
				float numeroControl = verificar[k];
				float dife = numero - numeroControl;

				float dd = (dife * dife);
				if (dd > 0.00001) {
					String txtEx = "Error en la suma de la columna: " + detalleCabecera[k] + ". Deberia ser "
							+ verificar[k] + " y no " + numero + ". Diferencia:" + dd;
					System.out.println("** control deshabilitad: " + txtEx);
					// dr throw new Exception(txtEx);
				}
			}
		}
	}

	private void printLinea(String[] ll) {
		String out = "";
		for (int i = 0; i < ll.length; i++) {
			out = out + " - " + ll[i];
		}
		printLogln(out);
	}

	/* Parametros: clave del hash Cabecera, cadena del archivo */
	private Object comprobarTipo(String campo, String valor) throws Exception {

		if (valor.trim().compareTo("") == 0) {
			valor = "0";
		}
		Object out = null;

		boolean valido = false;
		switch (campo) {
		case CSV.STRING:
			// No hace nada porque ya es un String
			valido = true;
			out = valor;
			break;
		case CSV.DATE:
			try {
				SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()); // Aca
																											// se
																											// define
																											// el
																											// formato
																											// de
																											// fecha
																											// permitido
				formatoFecha.setLenient(false);
				formatoFecha.parse(valor);
				out = formatoFecha.get2DigitYearStart();
				valido = true;
			} catch (ParseException e) {
				valido = false;
			}
			break;
		case CSV.NUMERICO:
			try {
				out = parserNumero(valor);
				valido = true;
			} catch (Exception nfe) {
				valido = false;
			}
			break;
		/*
		 * case CSV.NUMERICO: try { out = Double.parseDouble(valor); valido =
		 * true; } catch (NumberFormatException nfe) { try { String valor2 =
		 * valor.replace(',', '.');
		 * System.out.println("valor:"+valor2+" - "+valor); out =
		 * Double.parseDouble(valor2); valido = true; } catch
		 * (NumberFormatException nfe2) { nfe2.printStackTrace(); valido =
		 * false; } } break;
		 */
		default:
			valido = false;
			break;
		}

		if (valido == false) {
			throw new Exception("Error en de tipo:" + campo + " - (" + valor + ")");
		}
		return out;
	}

	private Object[] comprobarTipoLinea(String[] tipos, String[] valor) throws Exception {
		Object[] valoresLinea = new Object[valor.length];

		for (int i = 0; i < valor.length; i++) {
			String t = tipos[i];
			String v = valor[i];
			Object valorTipo = comprobarTipo(t, v);
			valoresLinea[i] = valorTipo;
		}
		return valoresLinea;
	}

	/*
	 * El String cadena es argumento como clave para la tabla contenidoCab
	 * Devuelve el valor de la tabla
	 */
	public String getCabecera(String cadena) throws Exception {
		String salida = this.contenidoCab.get(cadena);
		if (salida == null) {
			throw new Exception("Error: no existe en la cabecera " + cadena);
		}
		return salida;
	}

	public Object getDetalle(String cadena) throws Exception {
		Integer posicion = this.posicionCabDetalle.get(cadena);
		if (posicion == null) {
			throw new Exception("Error: no existe en la cabecera de detalle: " + cadena);
		}
		return this.lineaRecorrido[posicion];
	}

	public String getDetalleString(String cadena) throws Exception {
		String out = (String) this.getDetalle(cadena);
		return out;
	}

	public double getDetalleDouble(String cadena) throws Exception {
		float outf = (float) this.getDetalle(cadena);
		double out = outf + 0;
		return out;
	}

	public int getDetalleInteger(String cadena) throws Exception {
		int outf = (int) this.getDetalle(cadena);
		int out = outf + 0;
		return out;
	}

	public void start() {
		this.ite = this.contenidoDetalle.iterator();
	}

	public boolean hashNext() {
		boolean out = this.ite.hasNext();
		if (out == true) {
			this.lineaRecorrido = (Object[]) this.ite.next();
		}
		return out;
	}

	/*
	 * public void imprimirTabla() { Enumeration<String> elem =
	 * this.contenidoCab.keys(); String clave; printLogln("--- cabecera ---");
	 * while (elem.hasMoreElements()) { clave = elem.nextElement(); System.out
	 * .print(clave + "->" + this.contenidoCab.get(clave) + "\n"); }
	 * printLogln("--- detalle ---"); for (int i = 0; i <
	 * this.contenidoDetalle.size(); i++) { String[] linea =
	 * this.contenidoDetalle.get(i); for (int j = 0; j < linea.length; j++) {
	 * printLog(linea[j] + " - "); } printLogln(""); } }
	 * 
	 * public void imprimirPosHash() { Enumeration<String> elem =
	 * this.posicionCabDetalle.keys(); String clave;
	 * printLogln("--- posicionDetalleCabecera ---"); while
	 * (elem.hasMoreElements()) { clave = elem.nextElement(); printLog(clave +
	 * "->" + this.posicionCabDetalle.get(clave) + "\n"); } }
	 */
	/****************************************************************************/
	/***********************
	 * EJEMPĹO DE USO DEL CSV
	 *****************************/
	/****************************************************************************/

	public static void ejemploUso() {
		String direccion = new String("/home/daniel/Factura.csv");

		String[][] cab = { { "Cliente", CSV.STRING }, { "Fecha", CSV.DATE }, { "Codigo", CSV.NUMERICO },
				{ "Tipo de Compra", CSV.STRING } };

		String[][] cabDet = { { "codigo", CSV.NUMERICO }, { "cantidad", CSV.NUMERICO }, { "articulo", CSV.STRING },
				{ "valor", CSV.NUMERICO }, { "total", CSV.NUMERICO } };
		try {

			// Leer el CSV
			CSV csv = new CSV(cab, cabDet, direccion);

			// Como acceder a la cabecera
			printLogln("Cliente:" + csv.getCabecera("Cliente") + " (" + csv.getCabecera("Codigo") + " )");

			// Como recorrer el detalle
			printLogln("--- inicio detalle ---");
			csv.start();
			while (csv.hashNext()) {
				String articulo = csv.getDetalleString("articulo");
				double valor = csv.getDetalleDouble("valor");

				printLogln(articulo + " - " + valor);
			}
			printLogln("--- fin detalle ---");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	float parserNumero(String dato) throws Exception {
		float out = 0;
		try {
			out = Float.parseFloat(dato);
		} catch (Exception e) {
			dato = dato.replace(',', '.');
			try {
				out = Float.parseFloat(dato);
			} catch (Exception e2) {
				throw new Exception("No pudo parserar [" + dato + "]");
			}
		}

		return out;
	}

	private static void printLog(String dato) {
		if (debug == true) {
			System.out.print(dato);
		}
	}

	private static void printLogln(String dato) {
		printLog(dato + "\n");
	}

	public static void main(String[] args) {
		ejemploUso();
	}

	public char getDelimitador() {
		return delimitador;
	}

	public void setDelimitador(char delimitador) {
		this.delimitador = delimitador;
	}

}