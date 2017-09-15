package com.coreweb.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jme.Monto;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.time.DateUtils;
import org.zkoss.io.Files;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import com.coreweb.Config;
import com.coreweb.domain.IiD;

public class Misc {

	public static final NumberFormat FORMATTER = new DecimalFormat("###,###,##0");

	private static long idUnique = 0;
	private static String dir;

	public static String TEXTO_ROJO = "font-weight: bold; color:Red; text-align: right";
	public static String TEXTO_NARANJA = "font-weight: bold; color:Orange; text-align: right";
	public static String TEXTO_VERDE = "font-weight: bold; color:DarkGreen; text-align: right";
	public static String TEXTO_NORMAL = "text-align: right";
	public static String LABEL_AZUL = "color:#003390; font-size:14px; text-align: center";
	public static String LABEL_ROJO = "color:red; font-size:14px; text-align: center";
	public static String BACKGROUND_NARANJA = "background:#f6d197";
	public static String COLOR_ROJO = "color:red";
	public static String SIMBOLO_ASTERISCO = "(*)";

	public static String YYYY_MM_DD = "yyyy-MM-dd";
	public static String YYYY_MM_DD_HORA_MIN = "yyyy-MM-dd (HH:mm)";
	public static String DD_MM__YYY_HORA_MIN = "dd-MM-yyyy (HH:mm)";
	public static String YYYY_MM_DD_HORA_MIN_SEG = "yyyy-MM-dd HH:mm:ss";
	public static String YYYY_MM_DD_HORA_MIN_SEG2 = "yyyy-MM-dd (H:m:s)";
	public static String YYYY_MM_DD_HORA_MIN_SEG3 = "yyyy-MM-dd (HH:mm:ss)";
	public static String YYYY_MM_DD_HORA_MIN_SEG_MIL = "yyyy-MM-dd_HH_mm_ss_S";
	public static String D_MMMM_YYYY = "d 'de' MMMM 'del' yyyy";
	public static String E_D_MMMM_YYYY = "E d 'de' MMMM 'del' yyyy";
	public static String D_MMMM_YYYY2 = "d MMMM yyyy (HH:mm:ss)";
	public static String DD_MM_YYYY = "dd-MM-yyyy";
	public static String DD_MMMM_HH_MM = "dd MMMM (HH:mm)";
	public static String HH_MM_SS = "h:m:s";

	public static String LABEL_BORDER = "border:1px solid; border-color:#54afcb; padding:2px";

	public static int TIPO_IMAGEN = 1;
	public static int TIPO_DOCUMENTO = 2;

	public static String NOTIFICACION_WARNING = "warning";
	public static String NOTIFICACION_NORMAL = "normal";

	public boolean checkLogin(String login) {

		char[] invalidos = { ';', '}', '{', ']', '[', '%', '&', '\'', '\\' };

		boolean out = true;
		if (login.trim().length() == 0) {
			out = false;
		}
		for (int i = 0; i < invalidos.length; i++) {
			char c = invalidos[i];
			if (login.indexOf(c) >= 0) {
				out = false;
			}
		}

		return out;
	}

	public static String doubleToString(double numero) {
		return String.valueOf(numero);
	}

	public String formatearDimension(double numero) {
		NumberFormat formatter = new DecimalFormat("##0.00#");
		return formatter.format(numero);
	}

	public String redondearPrecio(double precio, double redondeo) {
		/**
		 * precio =253812, redondeo = 3, resultado = 254000 precio =253312,
		 * redondeo = 3, resultado = 253000
		 */
		double var = Math.pow(10, redondeo);

		return String.valueOf(Math.round(precio / var) * var);
	}
	
	public double redondearConPrecioMinimo(double precio, double precioMinimo, double redondeoMax, double redondeoMin) {
		/**
		 * precio =253812, redondeo = 3, resultado = 254000 precio =253312,
		 * redondeo = 3, resultado = 253000
		 */
		double redondeo = redondeoMin;
		if(precio > precioMinimo){
			redondeo = redondeoMax;
		}
		double var = Math.pow(10, redondeo);

		return Math.round(precio / var) * var;
	}

	public static String getDir() {
		return dir;
	}

	public static void setDir(String dir) {
		Misc.dir = dir;
	}

	public static synchronized long getIdUnique() {
		long tmp = System.currentTimeMillis();
		while (tmp == idUnique) {
			tmp = System.currentTimeMillis();
		}
		idUnique = tmp;
		return idUnique;
	}

	public String colorVariacion(double actual, double nuevo) {
		double d = nuevo - actual;
		double porc = d / actual * 100;
		String color = TEXTO_NORMAL;

		if (porc >= 10) {
			color = TEXTO_ROJO;
		} else if (porc >= 0.01) {
			color = TEXTO_NARANJA;
		} else if (porc < -0.01) {
			color = TEXTO_VERDE;
		}

		return color;
	}

	public int obtenerUnidad(String param) {
		/**
		 * se obtiene un numero indicando si es unidad, decena, centena, etc.
		 * 10,3 --> 10 resultado 2 3 resultado 1
		 */
		if (param.indexOf(",") != -1)
			param = param.substring(0, param.indexOf(","));
		return param.length();
	}

	public String completarCeros(String param, int tamanio) {
		/**
		 * recibe un string y completa con ceros hasta que alcance el tamaño
		 * indicado en la variable tamaño
		 */
		if (param == null) {
			param = "";
		}
		for (int i = obtenerUnidad(param); i < tamanio; i++)
			param = "0" + param;
		return param;
	}

	// 1:color rojo, 2:color azul, 3:---
	public String colorVariacionByParam(int param) {

		String color = null;
		switch (param) {

		case 1:
			color = LABEL_ROJO;
			break;

		case 2:
			color = LABEL_AZUL;
			break;
		}

		return color;
	}

	public String rowColorDescuento(double valor) {
		String color = "";
		if (valor < 0) {
			color = BACKGROUND_NARANJA;
		}
		return color;
	}

	// Metodo para cambiar el color de las filas en el Grid del SubDiario
	// Detalle
	public String rowColorCuentaHaber(int tipo) {
		String color = "";
		if (tipo < 0.001) {
			color = BACKGROUND_NARANJA;
		}
		return color;
	}

	public java.util.Date stringMesDiaAnoToDate(String dd) {
		dd = dd + "0000000000000000000"; // son 19 caracteres, formato completo
		int dia = 0;
		int mes = 0;
		int anio = 0;

		int hora24 = 0;
		int minuto = 0;
		int segundo = 0;

		mes = Integer.parseInt(dd.substring(0, 2)) - 1;
		dia = Integer.parseInt(dd.substring(3, 5));
		anio = Integer.parseInt(dd.substring(7, 10));

		hora24 = Integer.parseInt(dd.substring(11, 13));
		minuto = Integer.parseInt(dd.substring(14, 16));
		segundo = Integer.parseInt(dd.substring(17, 19));

		Calendar calendar = Calendar.getInstance();
		calendar.set(anio, mes, dia, hora24, minuto, segundo);

		return calendar.getTime();

	}

	public java.util.Date stringToDate(String dd) {
		dd = dd + "0000000000000000000"; // son 19 caracteres, formato completo
		int anio = 0;
		int mes = 0;
		int dia = 0;
		int hora24 = 0;
		int minuto = 0;
		int segundo = 0;

		anio = Integer.parseInt(dd.substring(0, 4));
		mes = Integer.parseInt(dd.substring(5, 7)) - 1;
		dia = Integer.parseInt(dd.substring(8, 10));
		hora24 = Integer.parseInt(dd.substring(11, 13));
		minuto = Integer.parseInt(dd.substring(14, 16));
		segundo = Integer.parseInt(dd.substring(17, 19));

		Calendar calendar = Calendar.getInstance();
		calendar.set(anio, mes, dia, hora24, minuto, segundo);

		return calendar.getTime();

	}

	/**
	 * 
	 * @param fecha
	 *            Fecha que se desea convertir a Date
	 * @param formatoFecha
	 *            El formato del String que almacena la fecha Ej.: String fecha
	 *            = "03-27-2014 00:00:00" Formato de la fecha
	 *            "MM-dd-yyyy HH:mm:ss";
	 * 
	 *            Documentacion sobre SimpleDateFormat.html
	 *            http://docs.oracle.com
	 *            /javase/7/docs/api/java/text/SimpleDateFormat.html
	 * 
	 * @return
	 */
	public static Date ParseFecha(String fecha, String formatoFecha) {
		SimpleDateFormat formato = new SimpleDateFormat(formatoFecha);
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(fecha);
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return fechaDate;
	}

	public String dateHoyToString() {
		return dateToString(new Date(), YYYY_MM_DD_HORA_MIN_SEG2);
	}

	public String dateToString(java.util.Date dd, String format) {
		if (dd == null) {
			return "-";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String out2 = sdf.format(dd);

		return out2;

		/*
		 * // de acá en adelante no se usa mas
		 * 
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(dd);
		 * 
		 * 
		 * int anio = calendar.get(Calendar.YEAR); // año int mes =
		 * calendar.get(Calendar.MONTH) + 1; // mes, de 0 a 11 int dia =
		 * calendar.get(Calendar.DAY_OF_MONTH); // dia del mes
		 * 
		 * int hora24 = calendar.get(Calendar.HOUR_OF_DAY); // hora en formato
		 * 24hs int minutos = calendar.get(Calendar.MINUTE); int segundos =
		 * calendar.get(Calendar.SECOND);
		 * 
		 * String out = ""; if (format.compareTo(YYYY_MM_DD) == 0) { out = (anio
		 * + "-" + lastNchars("00" + mes, 2) + "-" + lastNchars( "00" + dia,
		 * 2)); }
		 * 
		 * if ((format.compareTo(YYYY_MM_DD_HORA_MIN_SEG) == 0) ||
		 * (format.isEmpty())) {
		 * 
		 * out = (anio + "-" + lastNchars("00" + mes, 2) + "-" + lastNchars("00"
		 * + dia, 2) + " " + lastNchars("00" + hora24, 2) + ":" +
		 * lastNchars("00" + minutos, 2) + ":" + lastNchars("00" + segundos,
		 * 2)); }
		 * 
		 * return out;
		 */

	}

	public Date toFechaMes01(Date fecha) {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(fecha);
		// fin del dia
		dateCal.set(Calendar.DAY_OF_MONTH, 1);
		Date dd = dateCal.getTime();
		return this.toFecha0000(dd);
	}

	public Date toFechaMes31(Date fecha) {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(fecha);
		// fin del dia
		
		dateCal.add(Calendar.MONTH, 1);  
		dateCal.set(Calendar.DAY_OF_MONTH, 1);  
		dateCal.add(Calendar.DATE, -1);

		Date dd = dateCal.getTime();
		return this.toFecha2400(dd);
	}
	
	
	public Date toFecha2400(Date fecha) {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(fecha);
		// fin del dia
		dateCal.set(Calendar.HOUR_OF_DAY, 23);
		dateCal.set(Calendar.MINUTE, 59);
		dateCal.set(Calendar.SECOND, 59);
		dateCal.set(Calendar.MILLISECOND, 999);
		return dateCal.getTime();
	}

	public Date toFecha0000(Date fecha) {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(fecha);
		// fin del dia
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		return dateCal.getTime();
	}

	public Date getFechaManana() {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(new Date());
		// make it tomorrow
		dateCal.add(Calendar.DAY_OF_YEAR, 1);
		// Now set it to the time you want
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		return dateCal.getTime();
	}

	public Date getFechaHoy00() {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(new Date());
		// Now set it to the time you want
		dateCal.set(Calendar.HOUR_OF_DAY, 0);
		dateCal.set(Calendar.MINUTE, 0);
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		return dateCal.getTime();
	}

	public Date getFechaHoy2359() {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(new Date());
		// Now set it to the time you want
		dateCal.set(Calendar.HOUR_OF_DAY, 23);
		dateCal.set(Calendar.MINUTE, 59);
		dateCal.set(Calendar.SECOND, 59);
		dateCal.set(Calendar.MILLISECOND, 999);
		return dateCal.getTime();
	}

	public Date getFechaHoyDetalle() {

		Calendar dateCal = Calendar.getInstance();
		// make it now
		dateCal.setTime(new Date());
		return dateCal.getTime();
	}

	/**
	 * Muestra la diferencia entre fechas con formato HH:mm:ss
	 * 
	 * @param desde
	 * @param hasta
	 * @return
	 */
	public String tiempoTareas(Date desde, Date hasta) {
		String out = "";
		if ((desde == null) || (hasta == null)) {
			return "";
		}
		long dif = hasta.getTime() - desde.getTime();
		dif = dif / 1000;
		long hor = dif / 3600;
		long min = (dif - (3600 * hor)) / 60;
		long seg = dif - ((hor * 3600) + (min * 60));
		out = hor + "h " + min + "m " + seg + "s";

		return out;
	}

	/**
	 * Muestra la diferencia entre fechas con formato HH:mm:ss
	 * 
	 * @param desde
	 * @param hasta
	 * @return
	 */
	public String tiempoTareas(long tiempo) {
		String out = "";
		long dif = tiempo;
		dif = dif / 1000;
		long hor = dif / 3600;
		long min = (dif - (3600 * hor)) / 60;
		long seg = dif - ((hor * 3600) + (min * 60));
		out = hor + "h " + min + "m " + seg + "s";

		return out;
	}

	/**
	 * Data dos fechas, dice la diferencia de dias entre ellas, no considera las
	 * horas y minutos. Ojo, la fecha 2 debe ser mas mayor.
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public long diasEntreFechas(Date d1, Date d2) {
		long aux = 0;
		long out = 0;
		long ld1 =  this.toFecha0000(d1).getTime();
		long ld2 =  this.toFecha2400(d2).getTime();

		long dia = (24 * 60 * 60 * 1000);
		long diff = ld2 - ld1;

		if (diff < dia && diff > 0) {
			return 0;
		}
		if (diff < 0) {
			aux = -1;
			if (diff * -1 < dia) {
				return -1;
			}
		}
 		out = (diff / dia) + aux;
		return out;
	}

	/**
	 * Agrega dias a una fecha dada..
	 */
	public Date agregarDias(Date fecha, int dias) {
		return DateUtils.addDays(fecha, dias);
	}

	/**
	 * Retorna verdadero si la fecha del parametro es mayor a la fecha al
	 * momento de llamar el metodo. La comparacion se hace en base solamente a
	 * las fechas.(Estan formateadas a 00:00:00)
	 * 
	 * @param fecha
	 * @return
	 */
	public boolean isFechaFutura(Date fecha) {
		boolean out = false;
		Date currentDate00 = this.getFechaHoy00();
		Date fechaDate00 = this.toFecha0000(fecha);
		if (fechaDate00.compareTo(currentDate00) > 0) {
			out = true;
		}
		return out;
	}

	/**
	 * Obtiene los n ultimos caracteres de un string
	 */
	public String lastNchars(String tex, int n) {
		String blank = new String(new char[n]).replace("\0", " ");
		tex = blank + tex;
		int l = tex.length();
		String out = tex.substring(l - n, l);
		return out;
	}

	public String getFormatoPedidoCompra() {
		Calendar fecha = Calendar.getInstance();
		String dia = String.valueOf(fecha.get(Calendar.DATE));
		if (dia.length() == 1) {
			dia = "0" + dia;

		}

		String mes = String.valueOf(fecha.get(Calendar.MONTH) + 1);
		if (mes.length() == 1) {
			mes = "0" + mes;

		}

		String yy = String.valueOf(fecha.get(Calendar.YEAR));

		return yy + ":" + mes + ":" + dia;
	}

	public boolean esAproximado(double d1, double d2, double diferencia) {
		double diff = d1 - d2;
		double d = Math.sqrt(diff * diff);
		return d <= diferencia;
	}

	public boolean esIgual(String d1, String d2) {
		if (d1 == d2) {
			return true;
		}
		if ((d1 == null) || (d2 == null)) {
			return false;
		}
		if (d1.trim().toLowerCase().compareTo(d2.trim().toLowerCase()) == 0) {
			return true;
		}
		return false;
	}

	public boolean esIgual(double d1, double d2) {
		double diff = d1 - d2;
		return ((diff * diff) < 0.00001);
	}

	public boolean esIgual(double d1, double d2, double margen) {
		double diff = d1 - d2;
		return ((diff * diff) < (margen * margen));
	}

	
	public boolean esIgual(double[] d1, double[] d2) {
		boolean out = true;
		try {
			for (int i = 0; i < d1.length; i++) {
				double v1 = d1[i];
				double v2 = d2[i];
				out = out && this.esIgual(v1, v2);
			}
		} catch (Exception e) {
			out = false;
		}
		return out;
	}

	public double redondeo(double d) {
		double d2 = Math.rint(d * 1000) / 1000;
		return d2;
	}

	public double redondeoDosDecimales(double d) {
		double d2 = Math.rint(d * 100) / 100;
		return d2;
	}

	public double redondeoCuatroDecimales(double d) {
		double d2 = Math.rint(d * 10000) / 10000;
		return d2;
	}

	/**
	 * Para poner numero en blanco si son cero
	 * 
	 * @param dato
	 * @param longitud
	 * @param izquierda
	 * @param blanco
	 * @return
	 */
	public String formato(Object dato, int longitud, boolean izquierda, boolean blanco) {
		if ((dato instanceof Double) && (blanco == true)) {
			double d = (double) dato;
			if ((d * d) < 0.0001) {
				dato = " ";
			}
		}

		return formato(dato, longitud, izquierda);
	}

	public String formato(Object dato, int longitud) {
		return formato(dato, longitud, true);
	}

	public String formatoNumero(Object dato) {
		if (dato instanceof Double) {
			NumberFormat formatter = new DecimalFormat("###,###,##0.00");
			String str = formatter.format(dato);
			dato = str;
		}

		if (dato instanceof Long) {
			NumberFormat formatter = new DecimalFormat("###,###,##0");
			String str = formatter.format(dato);
			dato = str;
		}
		return formato("" + dato, 15, false);
	}

	public String formatoNumeroBig(Object dato, boolean siDecimal) {
		String dec = "";
		if (siDecimal == true) {
			dec = ".00";
		}
		if (dato instanceof Double) {
			NumberFormat formatter = new DecimalFormat("###,###,###,###,##0" + dec);
			String str = formatter.format(dato);
			dato = str;
		}

		if (dato instanceof Long) {
			NumberFormat formatter = new DecimalFormat("###,###,###,###,##0" + dec);
			String str = formatter.format(dato);
			dato = str;
		}
		return formato("" + dato, 22, false);
	}

	public String formato(Object dato, int longitud, boolean izquierda) {
		if (dato instanceof Boolean) {
			if ((boolean) dato) {
				dato = "Si";
			} else {
				dato = "No";
			}
		}

		if (dato instanceof Double) {
			NumberFormat formatter = new DecimalFormat("###,###,##0.00");
			String str = formatter.format(dato);
			dato = str;
		}

		if (dato instanceof Long) {
			NumberFormat formatter = new DecimalFormat("###,###,##0");
			String str = formatter.format(dato);
			dato = str;
		}
		return formato("" + dato, longitud, izquierda);
	}

	public String formatoGs(double dato) {
		return formatoGs(dato, false);
	}

	public String formatoIfDecimal(double dato, boolean blanco) {

		if ((blanco == true) && ((dato * dato) < 0.001)) {
			return " ";
		}

		// saber si tiene 1 decimal
		int x = (int) (dato + 0);
		double y = x + 0.0;
		String fomatoIf = "###,###,##0";
		if (y != dato){
			fomatoIf = "###,###,##0.0";
		}
		
		NumberFormat formatter = new DecimalFormat(fomatoIf);
		String str = formatter.format(dato);
		return str;
	}
	
	
	public String formatoGs(double dato, boolean blanco) {

		if ((blanco == true) && ((dato * dato) < 0.001)) {
			return " ";
		}

		NumberFormat formatter = new DecimalFormat("###,###,##0");
		String str = formatter.format(dato);
		return str;
	}

	public String formatoGs(double dato, int longitud, boolean izquierda) {
		NumberFormat formatter = new DecimalFormat("###,###,##0");
		String str = formatter.format(dato);
		return formato(str, longitud, izquierda);
	}

	public String formatoDs(Object dato, int longitud, boolean izquierda) {
		NumberFormat formatter = new DecimalFormat("###,###,##0.00");
		String str = formatter.format(dato);
		return formato(str, longitud, izquierda);
	}

	public String formato(String dato, int longitud, boolean izquierda) {
		String bl = "                                                                                       ";

		String out = "";
		if (izquierda == true) {
			out = (dato + bl).substring(0, longitud);
		} else {
			out = bl + dato;
			int l = out.length();
			out = out.substring(l - longitud);
		}

		return out;
	}

	public double div(double d1, double d2) {
		double d = 0;
		if (d2 != 0) {
			d = d1 / d2;
		}
		return d;
	}

	public String pasaArchivoAString(String pathname) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

	/**
	 * Graba un string en un archivo. Si el archivo existe lo borra
	 * 
	 * @param pathFile
	 * @param data
	 * @throws Exception
	 */
	public void grabarStringToArchivo(String pathFile, String data) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(pathFile);
			out.write(data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}

	}

	public void agregarStringToArchivo(String fileName, String data) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
			out.write(data);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * repite un string n veces
	 * 
	 * @param str
	 * @param numero
	 * @return
	 */
	public String repeatSrt(String str, int numero) {
		String out = "";
		for (int i = 0; i < numero; i++) {
			out += str;
		}
		return out;
	}

	/**
	 * Recibe un array de direcciones de correo y retorna un array de tipo
	 * Object[0]: booleano si son correctos o no los mails y Object[1]: el
	 * String con los correos incorrectos
	 */
	public Object[] chequearMultipleCorreos(String[] correos) {

		String correosIncorrectos = "";

		boolean out = true;
		String validador = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

		if (correos[0].trim().length() > 0) {
			for (int i = 0; i < correos.length; i++) {
				boolean check = correos[i].matches(validador);
				if (check == false) {
					out = false;
					correosIncorrectos = correosIncorrectos + " \n - " + correos[i];
				}
			}
		}

		return new Object[] { out, correosIncorrectos };
	}

	/**
	 * Encrita considerando caseSensitive
	 * 
	 * @param cadena
	 * @param caseSensitive
	 * @return
	 */
	public String encriptar(String cadena, boolean caseSensitive) {

		if (caseSensitive == false) {
			cadena = cadena.toLowerCase();
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "** error encriptacion **";
		}
		md.reset();
		md.update(cadena.getBytes());
		byte[] b = md.digest();
		String out = toHexadecimal(b);
		return out;
	}

	/**
	 * Encripta una cadena, pasando primero todo a minúscula
	 * 
	 * @param cadena
	 * @return
	 */
	public String encriptar(String cadena) {
		return this.encriptar(cadena, false);
	}

	private String toHexadecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}

	public String[] split(String dato, String regex) {
		String[] out = dato.split(regex);
		for (int i = 0; i < out.length; i++) {
			out[i] = out[i].trim();
		}
		return out;
	}

	public Map<String, Object> getQueryParam(String query) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (query.trim().length() > 0) {
			String[] params = query.split("&");

			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				String name = param.split("=")[0];
				String value = param.split("=")[1];
				map.put(name, value);
			}
		}

		return map;
	}

	public String xxparserPalabrasAMayusculas(String pre, String str) {
		// pre = "F"
		// str = "palabraConString"
		// out = "F_PALABRA_CON_STRING"
		String out = "";

		String[] r = str.split("(?=[A-Z])");
		for (int i = 0; i < r.length; i++) {
			String p = (r[i]).trim();
			if (p.length() > 0) {
				out += "_" + p;
			}
		}
		out = pre + out.toUpperCase();
		return out;
	}

	public String parserPalabrasAMayusculas(String pre, String str) {
		// pre = "F"
		// str = "palabraConString"
		// out = "F_PALABRA_CON_STRING"
		String out = "";

		boolean era1 = false;
		String[] r = str.split("(?=[A-Z])");
		for (int i = 0; i < r.length; i++) {
			String p = (r[i]).trim();

			// elimina los blancos
			if (p.length() > 0) {
				if (era1 == true) {
					out += p;
				} else {
					out += "_" + p;
				}
				era1 = ((p.length() == 1) && (Character.isUpperCase(p.charAt(0))));
			}

		}
		out = pre + out.toUpperCase();
		return out;
	}

	public boolean testAliasInID(String tipo, String alias, Class aClass) {

		boolean out = false;
		try {

			String campo = this.parserPalabrasAMayusculas(tipo, alias);

			Field field = aClass.getField(campo);
			String valor = (String) field.get(null);
			out = (alias.compareTo(valor) == 0);
			if (out == false) {
				System.err.println("[Fa] Error atributo en clase ID: " + campo + " = " + alias);
			}
		} catch (Exception e) {
			System.err.println("[Ex] Error atributo en clase ID: " + e.getMessage() + " = " + alias);
		}

		return out;

	}

	public boolean testIdInAlias(Hashtable<String, String> aliasTipo, Class aClass) {

		System.out.println("-");
		boolean out = false;
		try {

			Field[] fields = aClass.getFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String nombreVarianble = field.getName();
				String alias = (String) field.get(null);

				// System.out.println("nombreVarianble:"+nombreVarianble +
				// " alias:" + alias);
				String tipo = aliasTipo.get(alias);
				if (tipo == null) {
					System.err.println("[Fa] Error, la variable " + alias + " no esta en el menu_config.ini");
					out = false;
				} else {
					String tAux = nombreVarianble.substring(0, 1);
					if (tipo.compareTo(tAux) != 0) {
						System.err.println("[Fa] Error, la variabnle " + nombreVarianble + " es de tipo diferente a "
								+ tipo + "-" + tAux);
						out = false;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[Ex] Error " + e.getMessage());
		}

		return out;

	}

	private static long seed = System.currentTimeMillis();
	private static Random rand = new Random(seed);

	synchronized public int numeroRandom(int maximo) {
		int v = 0;
		synchronized (rand) {
			v = rand.nextInt(maximo);
		}
		return v;
	}

	synchronized public String numeroRandomCero(int maximo) {
		String l = maximo + "";
		int v = numeroRandom(maximo);
		String out = "0000000000" + v;
		return out.substring(out.length() - l.length());
	}

	public boolean containsOnlyNumbersAndPercent(String str) {

		// It can't contain only numbers if it's null or empty...
		if (str == null || str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {

			// If we find a non-digit character we return false.
			if (!Character.isDigit(str.charAt(i)) && (Character.toString(str.charAt(i)).compareTo("%") != 0)) {
				return false;
			}
			if ((Character.toString(str.charAt(i)).compareTo("%") == 0) && str.length() == 1) {
				return false;
			}
		}

		return true;
	}

	public boolean containsOnlyNumbers(String str) {

		// It can't contain only numbers if it's null or empty...
		if (str == null || str.length() == 0)
			return false;

		for (int i = 0; i < str.length(); i++) {

			// If we find a non-digit character we return false.
			if (!Character.isDigit(str.charAt(i)) && (Character.toString(str.charAt(i)).compareTo("-") != 0)) {
				return false;
			}
		}

		return true;
	}

	// Genera un PDF con JasperPrint y luego abre en una nueva pestanha
	public void redirectAndPrint(Map parameters, Collection dataSrc, String reportSrc, String destFileSrc,
			String link) {
		try {

			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(dataSrc);
			JasperPrint print = JasperFillManager.fillReport(reportSrc, parameters, ds);
			JasperExportManager.exportReportToPdfFile(print, destFileSrc);
			Executions.getCurrent().sendRedirect(link, "_blank");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Genera un archivo PDF con JasperPrint
	public void generarPDFconJasperPrint(Map parameters, Collection dataSrc, String reportSrc, String destFileSrc) {
		try {

			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(dataSrc);
			JasperPrint print = JasperFillManager.fillReport(reportSrc, parameters, ds);
			JasperExportManager.exportReportToPdfFile(print, destFileSrc);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadFile(String pathCompleto, InputStream file) {
		try {
			OutputStream out = new java.io.FileOutputStream(pathCompleto);
			InputStream in = file;
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			in.close();
			out.flush();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void uploadFile(String path, String name, String ext, InputStream file) {

		this.uploadFile(path + name + ext, file);
	}

	/**
	 * Este método sirve para cuando queremos subir archivos al servidor lo que
	 * hace es recibir como parametro el evento tipo upload la ruta del
	 * directorio y el nombre del archivo y tambien el tipo de archivo para
	 * controlar si se quieren subir imagenes o docs..
	 */
	public boolean uploadFile(String folder, String fileName, UploadEvent event, int tipo) throws IOException {

		boolean ok = true;

		String format = event.getMedia().getFormat().toLowerCase();
		InputStream file = event.getMedia().getStreamData();
		String destino = folder + fileName + "." + format;
		System.out.println("destino:" + destino);

		if ((tipo == TIPO_IMAGEN) && (Config.EXTENSION_IMAGEN.indexOf(format)) >= 0) {

			this.copiarArchivo(file, destino);

		} else if ((tipo == TIPO_DOCUMENTO) && (Config.EXTENSION_DOCUMENTO.indexOf(format) >= 0)) {

			this.copiarArchivo(file, destino);

		} else {
			ok = false;
			this.mensajeError("Archivo con formato Incorrecto..");
		}

		return ok;
	}

	// Obtiene el valor segun el porcentaje
	public double obtenerValorDelPorcentaje(double valor, double porcentaje) {
		return (valor * porcentaje) / 100;
	}

	// Obtiene el porcentaje segun el valor
	public double obtenerPorcentajeDelValor(double valor1, double valor2) {
		return (valor1 / valor2) * 100;
	}

	// Para obtener un id unico =======================
	private static Object sycId = "para sincronizar";
	private static long idUnico = System.currentTimeMillis();

	public long getIdUnico() {

		synchronized (sycId) {
			long idAux = 0;
			while (idAux <= idUnique) {
				idAux = System.currentTimeMillis();
			}
			idUnico = idAux;
		}
		return idUnico;
	}

	// ======================================================

	public boolean mensajeEliminar(String texto) {

		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Eliminar",
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;
	}

	public boolean mensajeAgregar(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Agregar",
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;

	}

	public void mensajeInfo(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Informacion",
				new Messagebox.Button[] { Messagebox.Button.OK }, Messagebox.INFORMATION, null);
	}

	public void mensajeError(String texto) {
		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Error",
				new Messagebox.Button[] { Messagebox.Button.OK }, Messagebox.ERROR, null);
	}

	public boolean mensajeSiNo(String texto) {

		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Confirmar",
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, null);

		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			return true;
		}
		return false;
	}

	public int mensajeSiNoCancelar(String texto) {
		int out = Config.BOTON_CANCEL;

		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Confirmar",
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO, Messagebox.Button.CANCEL },
				Messagebox.QUESTION, null);

		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			out = Config.BOTON_YES;
		}
		if ((b != null) && (b.compareTo(Messagebox.Button.NO)) == 0) {
			out = Config.BOTON_NO;
		}
		return out;
	}

	public int mensajeSiCancelar(String texto) {
		int out = Config.BOTON_CANCEL;

		org.zkoss.zul.Messagebox.Button b = Messagebox.show(texto, "Confirmar",
				new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.CANCEL }, Messagebox.QUESTION, null);

		if ((b != null) && (b.compareTo(Messagebox.Button.YES)) == 0) {
			out = Config.BOTON_YES;
		}
		return out;
	}

	// Metodo que retorna el valor del Iva a partir de un valor iva incluido..
	// 10 = calcularIVA(110,10)
	public double calcularIVA(double ivaIncluido, int porcentajeIva) {
		return (ivaIncluido / (100 + porcentajeIva)) * porcentajeIva;
	}

	// Metodo que retorna el valor Gravado sin Iva a partir de un valor Gravado
	// iva incluido..
	// 100 = calcularIVA(110,10)
	public double calcularGravado(double ivaIncluido, int porcentajeIva) {
		return (ivaIncluido / (100 + porcentajeIva)) * 100;
	}

	public Object newInstance(String claseName) throws Exception {
		Class classObject = Class.forName(claseName);
		Constructor ctor = classObject.getDeclaredConstructor();
		ctor.setAccessible(true);
		Object obj = ctor.newInstance();
		return obj;
	}

	// ejecuta el método de una clase
	public void ejecutarMetoto(String clase, String metodo) throws Exception {

		Class cls = Class.forName(clase);
		Object obj = cls.newInstance();

		Class[] noparams = {};
		Method method = cls.getDeclaredMethod(metodo, noparams);
		method.invoke(obj, null);

	}

	// ejecuta el método de una clase
	public void ejecutarMetoto(String clase, String metodo, Object obj, Object value) throws Exception {

		Class cls = Class.forName(clase);

		Class[] noparams = { value.getClass() };
		Method method = cls.getDeclaredMethod(metodo, noparams);
		method.setAccessible(true);
		method.invoke(obj, value);

	}

	// ejecuta el método de una clase sin parametros y retorna el resultado
	public Object ejecutarMetoto(Object obj, String metodo) throws Exception {

		Class[] noparams = {};
		Method method = obj.getClass().getMethod(metodo, noparams);
		method.setAccessible(true);
		return method.invoke(obj, new Object[] {});

	}

	// hace un set
	public void setValue(Object obj, String att, Object value) throws Exception {
		Field fd = obj.getClass().getDeclaredField(att);
		fd.setAccessible(true);
		fd.set(obj, value);
	}

	// hace un get de un atributo, si no puede prueba con la super clase (sólo
	// una)
	public Object getValue(Object obj, String att) throws Exception {
		Method m = new PropertyDescriptor(att, obj.getClass()).getReadMethod();
		Object v = m.invoke(obj);
		return v;
	}

	/*
	 * NO ESTA REVISADO SI FUNCIONA // ejecuta el método de una clase public
	 * void ejecutarMetoto(String clase, String metodo, Object[] params) throws
	 * Exception {
	 * 
	 * Class cls = Class.forName(clase); Object obj = cls.newInstance();
	 * 
	 * 
	 * Class[] paramsT = new Class[params.length]; for(int i=0; i<params.length;
	 * i++){ paramsT[i] = params.getClass(); }
	 * 
	 * Method method = cls.getDeclaredMethod(metodo, paramsT);
	 * method.invoke(obj, params);
	 * 
	 * }
	 */

	public String ceros(String str, int ancho) {
		String out = "0000000000000000000000000000000000000000000000000000000000000000000000" + str.trim();
		out = out.substring(out.length() - ancho, out.length());
		return out;
	}

	/**
	 * Retorna -1 si el valor del primer parametro 'val' es menor al segundo
	 * parametro 'cmp' Retorna 0 si el valor del primer parametro 'val' es igual
	 * al segundo parametro 'cmp' Retorna 1 si el valor del primer parametro
	 * 'val' es mayor al segundo parametro 'cmp'
	 */
	public int compararNumeros(Number n1, Number n2) {
		long l1 = n1.longValue();
		long l2 = n2.longValue();
		if (l1 != l2)
			return (l1 < l2 ? -1 : 1);
		return Double.compare(n1.doubleValue(), n2.doubleValue());
	}

	/**
	 * Retorna false si el correo no es valido
	 */
	public boolean checkEmail(String email) {
		String validador = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(validador);
	}

	/**
	 * Verifica si es T, F, o vacio
	 */
	public boolean checkTrueFalse(String dato) {
		String validador = "[TtFf]?";
		return dato.matches(validador);
	}

	public void borrarArchivo(String archivo) {
		// Delete if tempFile exists
		File fileTemp = new File(archivo);
		if (fileTemp.exists()) {
			fileTemp.delete();
		} else {
			System.out.println("[Misc] No existe:" + archivo);
		}
	}

	// Convierte un numero a su equivalente en Letras rango (0 a 999999999999)..
	public String numberToLetter(Object numero) {
		String out = "";

		try {
			if (numero instanceof Integer) {
				out = Monto.aLetras(numero.toString());

			} else if (numero instanceof Long) {
				out = Monto.aLetras(numero.toString());

			} else if (numero instanceof Double) {

				DecimalFormat f = new DecimalFormat("##0.00");
				String valor = f.format(numero);
				String sepDec = f.getDecimalFormatSymbols().getDecimalSeparator() + "";
				int index = valor.lastIndexOf(sepDec);
				String entero = valor.substring(0, index);
				String decimal = valor.substring(index + 1);

				if (decimal.compareTo("00") != 0) {
					out = Monto.aLetras(entero) + decimal + "/100";
				} else {
					out = Monto.aLetras(entero);
				}

			} else if (numero instanceof String) {
				out = Monto.aLetras((String) numero);

			} else {
				throw new Exception("El tipo de dato no es un número");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String first = out.trim().substring(0, 1);
		return out.replaceFirst(first, first.toUpperCase());
	}

	private String tab = "     ";

	public String textoLog(String d) {
		String out = "";
		out += this.dateHoyToString() + "  " + d + "\n";
		return out;
	}

	public String textoLog(List<String> ls) {
		String out = "";
		out += this.dateHoyToString() + "\n";
		for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
			String d = (String) iterator.next();
			out += tab + d + "\n";
		}
		out += "----------------------------------------------\n";
		return out;
	}

	// Recibe una lista de arrays de string y retorna una sola concatenada..
	public String[] concatenarArraysDeString(List<String[]> arrays) {
		List<String> list = new ArrayList<String>();
		for (String[] string : arrays) {
			list.addAll(Arrays.asList(string));
		}
		return list.toArray(new String[list.size()]);
	}

	// Recibe de 1 a 5 arrays de string y retorna una sola concatenada..
	public String[] concatenarArraysDeString(String[] array1, String[] array2, String[] array3, String[] array4,
			String[] array5) {
		List<String[]> arrays = new ArrayList<String[]>();
		if (array1 != null) {
			arrays.add(array1);
		}
		if (array2 != null) {
			arrays.add(array2);
		}
		if (array3 != null) {
			arrays.add(array3);
		}
		if (array4 != null) {
			arrays.add(array4);
		}
		if (array5 != null) {
			arrays.add(array5);
		}
		return this.concatenarArraysDeString(arrays);
	}

	public byte[] serializar(Serializable o) {
		return SerializationUtils.serialize(o);
	}

	public Object deSerializar(byte[] bs) {
		return SerializationUtils.deserialize(bs);
	}

	public boolean siDirty(Serializable o2, byte[] bs1) {
		boolean out = true;
		byte[] bs2 = this.serializar(o2);
		if (bs1.length == bs2.length) {
			out = false;
			for (int i = 0; ((!out) && (i < bs2.length)); i++) {
				out = out || (bs1[i] != bs2[i]);
			}
		}
		return out;
	}

	
	/**
	 * Un objeto serializable se guada en el disco
	 * @param obj
	 * @param file
	 */
	public void grabarObjectToArchvo(Serializable obj, String file) {
		try {
			byte[] ff = this.serializar(obj);
			ObjectOutputStream fil = new ObjectOutputStream(new FileOutputStream(file));
			fil.write(ff);
			fil.close();
		} catch (Exception e) {
			System.out.println("NO pudo grabar en ["+file+"] "+e.getMessage());
		}
	}


	/**
	 * Leer desde el disco un objeto
	 * @param file
	 * @return
	 */
	public Serializable leerObjetoFromDisco(String file) {
		Serializable out = null;
		try {
			ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(file));
			byte[] ff = new byte[entrada.available()];
			entrada.readFully(ff);
			out = (Serializable) this.deSerializar(ff);
			entrada.close();
		} catch (Exception e) {
			System.out.println("NO pudo leer  ["+file+"] "+e.getMessage());
			out = null;
		}
		return out;
	}

	
	
	public void mensajePopupTemporal(String mensaje) {
		this.mensajePopupTemporal(mensaje, 3000);
	}

	public void mensajePopupTemporal(String mensaje, int time) {
		Clients.showNotification(mensaje, null, null, null, time);
	}

	public void mensajePopupTemporalWarning(String mensaje) {
		this.mensajePopupTemporalWarning(mensaje, 3000);
	}

	public void mensajePopupTemporalWarning(String mensaje, int time) {
		Clients.showNotification(mensaje, NOTIFICACION_WARNING, null, null, time);
	}

	public void mensajePopupTemporal(String mensaje, String tipo, Component posicion) {
		Clients.showNotification(mensaje, tipo, posicion, null, 2000);
	}

	public boolean esPersonaJuridica(String ruc) {
		// 80024884-8
		boolean out = false;
		try {
			ruc = ruc.trim();
			if (ruc.length() == 10) {
				String ochennta = ruc.substring(0, 2);
				int och = Integer.parseInt(ochennta);
				if (och == 80) {
					out = true;
				}
			}
		} catch (Exception e) {
			out = false;
		}
		return out;
	}

	/**
	 * Recibe un archivo y lo copia a un directorio destino..
	 */
	public void copiarArchivo(InputStream file, String destino) throws IOException {
		File dst = new File(destino);
		Files.copy(dst, file);
	}

	/**
	 * Retorna la fecha de vencimiento a partir del plazo que recibe como
	 * parametro
	 */
	public Date calcularFechaVencimiento(Date emision, int plazo) {
		Date out = this.agregarDias(emision, plazo);
		return out;
	}

	/**
	 * Retorna true si el nro (int) es par
	 * 
	 * @param int
	 *            nro
	 */
	public boolean esPar(int nro) {
		return (nro % 2) == 0;
	}

	/**
	 * Retorna true si el nro es múltiplo del factor..
	 * 
	 * @param int
	 *            factor
	 */
	public boolean esMultiploDe(int nro, int factor) {
		return (nro % factor) == 0;
	}

	/**
	 * Verifica si se puede parsear el String a Integer..
	 */
	public boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
		}
		return false;
	}

	/**
	 * Separa letras de numeros [0]: letra/s [1]: numero/s
	 */
	public String[] separarLetrasDeNumeros(String str) {
		String[] sp = str.split("");
		String letra = "";
		String numero = "";

		for (int i = 0; i < sp.length; i++) {
			if (this.isInteger(sp[i])) {
				numero += sp[i];
			} else {
				letra += sp[i];
			}
		}
		return new String[] { letra, numero };
	}

	/**
	 * Concatena dos String..
	 */
	public String concat(String str1, String str2, String separador) {
		return str1 + separador + str2;
	}

	/**
	 * Calcula la edad a partir de la fecha de nacimiento
	 * 
	 * @param fechaNacimiento
	 * @return edad
	 */
	public int calcularEdad(Date fechaNacimiento) {

		Calendar fNac = Calendar.getInstance();
		fNac.setTime(fechaNacimiento);
		Calendar fechaActual = Calendar.getInstance();
		int edad = fechaActual.get(Calendar.YEAR) - fNac.get(Calendar.YEAR);
		if (fechaActual.get(Calendar.MONTH) < fNac.get(Calendar.MONTH)) {
			edad--;
		} else if (fechaActual.get(Calendar.MONTH) == fNac.get(Calendar.MONTH)
				&& fechaActual.get(Calendar.DAY_OF_MONTH) < fNac.get(Calendar.DAY_OF_MONTH)) {
			edad--;
		}
		return edad;
	}

	/** busca items duplicados.. **/
	public boolean itemDuplicado(Object item, Collection lista) {
		boolean out = false;
		for (Object m : lista) {
			if (m instanceof IiD) {
				IiD im = (IiD) m;
				IiD iItem = (IiD) item;
				if (im.getId() == iItem.getId()) {
					out = true;
				}
			} else {
				if (m == item) {
					out = true;
				}
			}
		}
		return out;
	}

	/** busca items duplicados **/
	public boolean itemDuplicado(IiD item, Collection<IiD> lista) {
		boolean out = false;
		for (IiD m : lista) {
			if (m.getId() == item.getId()) {
				out = true;
			}
		}
		return out;
	}

	/**
	 * @return el mes en letras..
	 */
	public String getMesEnLetras(int mes) {
		switch (mes) {
		case 1:
			return "Enero";
		case 2:
			return "Febrero";
		case 3:
			return "Marzo";
		case 4:
			return "Abril";
		case 5:
			return "Mayo";
		case 6:
			return "Junio";
		case 7:
			return "Julio";
		case 8:
			return "Agosto";
		case 9:
			return "Setiembre";
		case 10:
			return "Octubre";
		case 11:
			return "Noviembre";
		case 12:
			return "Diciembre";
		}
		return "no valido";
	}

	public String concatenarSep(String punto, Object... values) {
		String out = "";
		boolean pri = true;
		for (Object c : values) {
			if (pri == true) {
				out = c + "";
			} else {
				out += punto + c;
			}
			pri = false;
		}
		return out;
	}

	public String concatenar(Object... values) {
		return concatenarSep(" - ", values);
	}

	// Los resultados van del 1 = Domingo, 2 = Lunes …
	public static int getDiaPorFecha(Date d) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Ordena una lista de algún tipo, por uno de sus campos
	 * 
	 * @param collection
	 * @param campo
	 */
	public void ordernar(List collection, String campo) {
		BeanComparator fieldComparator = new BeanComparator(campo);
		Collections.sort(collection, fieldComparator);
	}
	
	

	/**
	 * Ordena una lista de algún tipo, por uno de sus campos, Asc
	 * 
	 * @param collection
	 * @param campo
	 */
	public void ordernarAsc(List collection, String campo) {
		BeanComparator fieldComparator = new BeanComparator(campo);
		Collections.sort(collection, fieldComparator.reversed());
	}

	
	/**
	 * Controla que si el atributo es nulo, retorna un string en blanco
	 * @param o
	 * @param campo
	 * @return
	 */
	public String getString(Object obj, String att){
		String out = "";
		try {
			out = (this.getValue(obj, att)).toString();
		} catch (Exception e) {
		}
		return out;
	}

	
	
	public static void main(String[] args) {
		Misc m = new Misc();	

		double d1 = 130.1;
		double d2 = 100;
		double margen = 30; 
		
		System.out.println(" es: " +m.esIgual(d1, d2, margen));
		
		/*
		
		long dia = (24 * 60 * 60 * 1000);

		Date dd =  new Date(   (new Date()).getTime() - (dia * 10));
//		Date dd =  new Date();
		
		Date fechaDesde = m.toFechaMes01(dd);
		Date fechaHasta = m.toFechaMes31(dd);

		long nd = m.diasEntreFechas(fechaDesde, fechaHasta);
		
		System.out.println(m.dateToString(fechaDesde, m.YYYY_MM_DD_HORA_MIN_SEG) +"  - " + fechaDesde);
		System.out.println(m.dateToString(fechaHasta, m.YYYY_MM_DD_HORA_MIN_SEG) +"  - " + fechaHasta);
		System.out.println(nd);
		
//		System.out.println(m.redondearConPrecioMinimo(7740, 5000, 3, 2));
//		System.out.println(m.redondearConPrecioMinimo(3740, 5000, 3, 2));
		*/
	}
	
	
}


/*
 * class A {
 * 
 * public void setNameA(String nameA) { System.out.println("-- paso por set A");
 * }
 * 
 * public String getNameA() { return "andoA"; }
 * 
 * public String metodoA() { return "Estoy en A"; } }
 * 
 * class B extends A {
 * 
 * String name = "pepe";
 * 
 * public String getName() { return name; }
 * 
 * public void setName(String name) { System.out.println("pasa:" + name + " - "
 * + this.name + this); this.name = name; } }
 */
