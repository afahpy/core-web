package com.coreweb.extras.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.coreweb.Config;
import com.coreweb.util.Misc;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilders;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.datatype.*;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilders;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;

public abstract class DatosReporte extends ReporteDefinicion implements ReporteInterface{
	/*
	 * public static final StringType TIPO_STRING = type.stringType(); public
	 * static final IntegerType TIPO_INTEGER = type.integerType(); public static
	 * final LongType TIPO_LONG = type.longType(); public static final
	 * DoubleType TIPO_DOUBLE = type.doubleType(); public static final
	 * DoubleType TIPO_DOUBLE_GS = type.doubleType(); public static final
	 * BigDecimalType TIPO_BIGDECIMAL = type.bigDecimalType(); public static
	 * final DateType TIPO_DATE = type.dateType(); public static final
	 * DateYearType TIPO_DATEYEAR = type.dateYearType(); public static final
	 * DateMonthType TIPO_DATEMONTH = type.dateMonthType(); public static final
	 * DateDayType TIPO_DATEDAY =type.dateDayType(); public static final
	 * BooleanType TIPO_BOOLEAN =type.booleanType();
	 */

	private boolean membretePropioReporte = false;
	private boolean footerPropioReporte = false;
	private MyReport reporte = null;

	public void setMembretePropioReporte(boolean membretePropioReporte) {
		this.membretePropioReporte = membretePropioReporte;
	}

	public void setFooterPropioReporte(boolean footerPropioReporte) {
		this.footerPropioReporte = footerPropioReporte;
	}

	private static Hashtable<String, DRIDataType> tipos = new Hashtable<>();

	static {
		DataTypeBuilders type = new DataTypeBuilders();

		tipos.put(TIPO_STRING, type.stringType());
		tipos.put(TIPO_STRING, type.stringType());
		tipos.put(TIPO_INTEGER, type.integerType());
		tipos.put(TIPO_LONG, type.longType());
		tipos.put(TIPO_DOUBLE, type.doubleType());
		tipos.put(TIPO_DOUBLE_GS, type.doubleType());
		tipos.put(TIPO_DOUBLE_DS, type.doubleType());
		tipos.put(TIPO_BIGDECIMAL, type.bigDecimalType());
		tipos.put(TIPO_DATE, type.dateType());
		tipos.put(TIPO_DATEYEAR, type.dateYearType());
		tipos.put(TIPO_DATEMONTH, type.dateMonthType());
		tipos.put(TIPO_DATEDAY, type.dateDayType());
		tipos.put(TIPO_BOOLEAN, type.booleanType());
	}

	public static DRIDataType getTipo(String key) {
		return tipos.get(key);
	}

	// por default pdf
	private String tipoFormatoExportar = EXPORT_PDF;

	public static final int COLUMNA_ALINEADA_CENTRADA = 1;
	public static final int COLUMNA_ALINEADA_IZQUIERDA = 2;
	public static final int COLUMNA_ALINEADA_DERECHA = 3;

	private CabeceraReporte cr;
	private ComponentBuilder body = cmp.horizontalList();
	// al final del reporte
	private ComponentBuilder footer = cmp.horizontalList();
	private List<Object[]> data;
	private String logoEmpresa = "";
	private int logoAncho = 0;
	private int logoAlto = 0;

	private String empresa;
	private String titulo;
	private String usuario;
	private String directorioBase = "./";
	private String idArchivo = "";
	private String directorio = "";
	private String nombreArchivo = "";
	private boolean apaisada = false;
	private boolean putFoot = true;

	private boolean borrarDespuesDeVer = true;

	private Misc m = new Misc();

	public DatosReporte() {
	}

	public void putFoot(boolean b) {
		this.putFoot = b;
	}

	public boolean isPutFoot() {
		return putFoot;
	}

	public String getTipoFormatoExportar() {
		return tipoFormatoExportar;
	}

	public void setTipoFormatoExportar(String tipoFormatoExportar) {
		this.tipoFormatoExportar = tipoFormatoExportar;
	}
	
	public void setTipoFormatoExportarPDF(){
		this.setTipoFormatoExportar(ReporteDefinicion.EXPORT_PDF);
	}

	public void setTipoFormatoExportarCSV(){
		this.setTipoFormatoExportar(ReporteDefinicion.EXPORT_CSV);
	}

	public void setTipoFormatoExportarXLSX(){
		this.setTipoFormatoExportar(ReporteDefinicion.EXPORT_XLSX);
	}
	
	public void setTipoFormatoExportarDOCX(){
		this.setTipoFormatoExportar(ReporteDefinicion.EXPORT_DOCX);
	}
	
	

	@Override
	public MyReport getReporte(){
		if (this.reporte == null){
			this.ejecutar(false);
		}
		return this.reporte;
	}
		
	public void setTitulosColumnas(List<DatosColumnas> columnas) {
		cr = new CabeceraReporte();
		cr.setColumnas(columnas);
	}

	public void setApaisada() {
		this.apaisada = true;
	}

	public void setVertical() {
		this.apaisada = false;
	}

	@Override
	public String getTitulo() {
		return titulo;
	}

	public String getDirectorio() {
		return directorio;
	}

	public void setDirectorio(String directorio) {
		this.directorio = directorio;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	@Override
	public String getDirectorioBase() {
		return directorioBase;
	}

	@Override
	public void setDirectorioBase(String directorioBase) {
		this.directorioBase = directorioBase;
	}

	public void setDatosReporte(List<Object[]> datos) {
		data = datos;
	}

	public void setDatosReporte(String titulo) {
		this.titulo = titulo;
	}

	@Override
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Override
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public void ejecutar() {
		ejecutar(false);
	}

	public String getArchivoSalida() {
		String out = "";
		if (this.idArchivo.trim().length() == 0) {
			long id = m.getIdUnico();
			this.idArchivo = m.dateToString(new Date(id), m.YYYY_MM_DD_HORA_MIN_SEG_MIL);
		}
		out = this.getDirectorio() + "/" + this.getNombreArchivo() + "" + this.idArchivo + this.tipoFormatoExportar;
		return out;
	}

	@Override
	public String getArchivoPathReal() {
		String out = Config.DIRECTORIO_REAL_REPORTES + "/" + this.getArchivoSalida();
		return out;
	}

	@Override
	public String getUrlReporte() {
		String out = Config.DIRECTORIO_WEB_REPORTES + "/" + this.getArchivoSalida();
		return out;
	}

	/**
	 * Este métoodo hay que redefinir si queremos que el reporte tenga su propia
	 * cabecera. Setear tambiem membretePropioReporte = true;
	 * 
	 * @return
	 */
	public ComponentBuilder getMembretePropioReporte() {
		VerticalListBuilder main = cmp.verticalList();
		//main.add(cmp.text("Menmbrete no definido"));
		main.add(cmp.text(""));
		main.add(cmp.line());
		main.add(cmp.verticalGap(10));
		return main;
	}

	/**
	 * Este métoodo hay que redefinir si queremos que el reporte tenga su propio
	 * footer. Setear tambiem footerPropioReporte = true;
	 * 
	 * @return
	 */
	public ComponentBuilder getFooterPropioReporte() {
		VerticalListBuilder main = cmp.verticalList();
		main.add(cmp.text("Footer no definido"));
		main.add(cmp.line());
		main.add(cmp.verticalGap(10));
		return main;
	}

	@Override
	public void ejecutar(boolean mostrar) {

		this.setA4(); // por defecto
		this.setDatosReportes();

		String pathCompleto = this.getArchivoPathReal();

		this.reporte = new MyReport(this.membretePropioReporte, this.getMembretePropioReporte(), cr, body, footer,
				data, empresa, logoEmpresa, logoAncho, logoAlto, titulo, usuario, pathCompleto,
				this.footerPropioReporte, this.getFooterPropioReporte());

		this.reporte.setTipoPagina(this.getTipoPagina());
		this.reporte.setPutFooter(this.isPutFoot());
		this.reporte.setFormato(this.tipoFormatoExportar);
		this.reporte.setLandscape(this.apaisada);
		this.reporte.show(mostrar);
		if ((mostrar == true) && (this.isBorrarDespuesDeVer() == true)) {
			this.m.borrarArchivo(pathCompleto);
		}
	}

	@Override
	public boolean isBorrarDespuesDeVer() {
		return borrarDespuesDeVer;
	}

	public void setBorrarDespuesDeVer(boolean borrarDespuesDeVer) {
		this.borrarDespuesDeVer = borrarDespuesDeVer;
	}

	public ComponentBuilder getBody() {
		return body;
	}

	public void setBody(ComponentBuilder body) {
		this.body = body;
	}

	public void setFooter(ComponentBuilder footer) {
		this.footer = footer;
	}

	public ComponentBuilder getFooter() {
		return footer;
	}

	abstract public void setDatosReportes();

	public void setLogoEmpresa(String logoEmpresa, int logoAncho, int logoAlto) {
		this.logoEmpresa = logoEmpresa;
		this.logoAncho = logoAncho;
		this.logoAlto = logoAlto;
	}
	
	
	public void exportarExcel(){
		
	}

	public CabeceraReporte getCabRep() {
		return cr;
	}

	
	

}





