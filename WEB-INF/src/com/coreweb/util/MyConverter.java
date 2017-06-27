package com.coreweb.util;

import java.text.DecimalFormat;
import java.util.Date;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;

import com.coreweb.Config;

public class MyConverter implements Converter {

	Misc m = new Misc();

	// los tipos de formato definido
	private static int FORMAT_FACTURA_PY = 1;
	private static int FORMAT_CREDIT_CARD = 2;
	private static int FORMAT_MONEDA_LOCAL = 3;
	private static int FORMAT_MONEDA_EXTRANJERA = 4;
	private static int FORMAT_FECHA = 5;
	private static int FORMAT_FECHA_HORA = 6;

	// tipo del converter corriente
	private int tipoFormato = 0;

	private Misc misc = new Misc();

	// en el constructor definimos el tipo
	public MyConverter(int tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public MyConverter() {
		this.tipoFormato = 0;
	}

	// para llamarlo desde el ZUL

	public MyConverter getFacturaPy() {
		return new MyConverter(FORMAT_FACTURA_PY);
	}

	public MyConverter getCreditCard() {
		return new MyConverter(FORMAT_CREDIT_CARD);
	}

	public MyConverter getMonedaLocal() {
		return new MyConverter(FORMAT_MONEDA_LOCAL);
	}

	public MyConverter getMonedaExtranjera() {
		return new MyConverter(FORMAT_MONEDA_EXTRANJERA);
	}

	public MyConverter getFecha() {
		return new MyConverter(FORMAT_FECHA);
	}

	public MyConverter getFechaHora() {
		return new MyConverter(FORMAT_FECHA_HORA);
	}

	@Override
	public Object coerceToBean(Object arg0, Component arg1, BindContext arg2) {
		// TODO Auto-generated method stub
		return coerceTo(arg0, arg1);
	}

	@Override
	public Object coerceToUi(Object arg0, Component arg1, BindContext arg2) {
		// TODO Auto-generated method stub
		return coerceTo(arg0, arg1);
	}

	public Object coerceTo(Object val, Component comp) {
		Object out = null;

		if (this.tipoFormato == FORMAT_FACTURA_PY) {
			out = this.formatFactura((String) val, comp);

		} else if (this.tipoFormato == FORMAT_CREDIT_CARD) {
			out = this.formatCreditCard((String) val, comp);

		} else if (this.tipoFormato == FORMAT_MONEDA_EXTRANJERA) {
			out = this.formatMonedaExtranjera(val, comp);

		} else if (this.tipoFormato == FORMAT_MONEDA_LOCAL) {
			out = this.formatMonedaLocal(val, comp);

		} else if (this.tipoFormato == FORMAT_FECHA) {
			out = this.formatFecha(val, comp);

		} else if (this.tipoFormato == FORMAT_FECHA_HORA) {
			out = this.formatFechaHora(val, comp);
		}

		return val == null ? null : out;
	}

	/************************* FORMATOS ****************************/

	// Formato Factura
	public String formatFactura(String val, Component comp) {
		if ((val == null) || (val.trim().length() == 0)) {
			return val;
		}
		if (((val.split("-").length != 3) || !misc.containsOnlyNumbers((String) val))) {
			Clients.showNotification("Mal formato de la factura n-nn-nnn", "error", comp, "end_center", 3000, true);
			return val;
		}
		String[] s = ((String) val).split("-");
		String out = misc.ceros(s[0], 3) + "-" + misc.ceros(s[1], 3) + "-" + misc.ceros(s[2], 7);
		return out;
	}

	// Formato Tarjeta de Credito (Solo se permite ingresar los ultimos 4
	// digitos)
	public String formatCreditCard(String val, Component comp) {
		if ((val == null) || (val.trim().length() == 0)) {
			return val;
		}
		if (((val.trim().length() != 4) || !misc.containsOnlyNumbers((String) val))) {
			Clients.showNotification("No es el formato", "error", comp, "end_center", 3000, true);
			return "";
		}
		// String[] s = ((String) val).split("-");
		// String out = misc.ceros(s[0], 4)+"-"+misc.ceros(s[1],
		// 4)+"-"+misc.ceros(s[2], 4)+"-"+misc.ceros(s[3], 4);
		return val;
	}

	// Formato Moneda Local
	public String formatMonedaLocal(Object val, Component comp) {
		final Number number = (Number) val;
		return new DecimalFormat(Config.FORMAT_MONEDA_LOCAL).format(number);
	}

	// Formato Moneda Extranjera
	public String formatMonedaExtranjera(Object val, Component comp) {
		final Number number = (Number) val;
		return new DecimalFormat(Config.FORMAT_MONEDA_EXTRANJERA).format(number);
	}

	// Formato Fecha
	public String formatFecha(Object val, Component comp) {
		Date fecha = (Date) val;
		return this.m.dateToString(fecha, Misc.YYYY_MM_DD);
	}

	// Formato Fecha
	public String formatFechaHora(Object val, Component comp) {
		Date fecha = (Date) val;
		return this.m.dateToString(fecha, Misc.YYYY_MM_DD_HORA_MIN_SEG3);
	}

}
