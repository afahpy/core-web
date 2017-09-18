package com.coreweb.extras.reporte;


import com.coreweb.util.Misc;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;

public class DatosColumnas extends ReporteDefinicion{
	private String titulo;
	private String tipo;
	private int ancho = 0;
	private int alto = 0;
	private boolean totaliza = false;
	private int alineacionColuman = 0;
	private boolean agrupar = false;

	public DatosColumnas(String titulo, String tipo) {
		this.setColumna(titulo, tipo, 0);
	}

	public DatosColumnas(String titulo, String tipo, int ancho) {
		this.setColumna(titulo, tipo, ancho);
	}

	public DatosColumnas(String titulo, String tipo, boolean totaliza) {
		this.setColumna(titulo, tipo, 0);
		this.setTotaliza(totaliza);
	}

	public DatosColumnas(String titulo, String tipo, int ancho, boolean totaliza) {
		this.setColumna(titulo, tipo, ancho);
		this.setTotaliza(totaliza);
	}

	private void setColumna(String titulo, String tipo, int ancho) {
		this.titulo = titulo;
		this.setTipo(tipo);
		this.ancho = ancho;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		/*
		 * if (tipo.equals(TIPO_BIGDECIMAL) || tipo.equals(TIPO_DATE) ||
		 * tipo.equals(TIPO_DATEDAY) || tipo.equals(TIPO_DATEMONTH) ||
		 * tipo.equals(TIPO_DATEYEAR) || tipo.equals(TIPO_INTEGER)) this.tipo =
		 * tipo; else this.tipo = TIPO_STRING;
		 */
		this.tipo = tipo;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public boolean isTotaliza() {
		return totaliza;
	}

	public void setTotaliza(boolean totaliza) {
		this.totaliza = totaliza;
	}

	public int getAlineacionColuman() {
		return alineacionColuman;
	}

	public void setAlineacionColuman(int alineacionColuman) {
		this.alineacionColuman = alineacionColuman;
	}

	public boolean isAgrupar() {
		return agrupar;
	}

	public void setAgrupar(boolean agrupar) {
		this.agrupar = agrupar;
	}

	public TextColumnBuilder getColumnBuilder() {

		String titu = this.getTitulo().replaceAll(" ", "").toLowerCase();
		
		
		TextColumnBuilder tx = col.column(this.getTitulo(), titu,
				(DatosReporte.getTipo(this.getTipo())));

		if (this.getTipo().compareTo(DatosReporte.TIPO_DOUBLE_GS) == 0) {
			tx.setValueFormatter(new ValueFormatterGS());
		}

		if (this.getTipo().compareTo(DatosReporte.TIPO_DOUBLE_BIG_GS) == 0) {
			tx.setValueFormatter(new ValueFormatterBigGS());
		}
		
		
		if (this.getTipo().compareTo(DatosReporte.TIPO_DOUBLE_DS) == 0) {
			tx.setValueFormatter(new ValueFormatterDS());
		}

		if (this.alineacionColuman == DatosReporte.COLUMNA_ALINEADA_CENTRADA) {
			tx.setStyle(Templates.columnStyleCenter);
		}
		if (this.alineacionColuman == DatosReporte.COLUMNA_ALINEADA_IZQUIERDA) {
			tx.setStyle(Templates.columnStyleLeft);
		}
		if (this.alineacionColuman == DatosReporte.COLUMNA_ALINEADA_DERECHA) {
			tx.setStyle(Templates.columnStyleRigth);
		}

		if (this.getAncho() > 0)
			tx.setWidth(this.getAncho());

		return tx;
	}

}

class ValueFormatterGS extends AbstractValueFormatter<String, Number> {

	private Misc m = new Misc();
	private static final long serialVersionUID = 1L;

	@Override
	public String format(Number value, ReportParameters reportParameters) {

		String d = m.formatoGs((Double) value, 11, false);

		return d;
	}

}

class ValueFormatterBigGS extends AbstractValueFormatter<String, Number> {

	private Misc m = new Misc();
	private static final long serialVersionUID = 1L;

	@Override
	public String format(Number value, ReportParameters reportParameters) {

		String d = m.formatoGs((Double) value, 18, false);

		return d;
	}

}



class ValueFormatterDS extends AbstractValueFormatter<String, Number> {

	private Misc m = new Misc();
	private static final long serialVersionUID = 1L;

	@Override
	public String format(Number value, ReportParameters reportParameters) {

		String d = m.formatoDs((Double) value, 11, false);

		return d;
	}

}
