package com.coreweb.extras.reporte;

import java.awt.Color;

import com.coreweb.Config;
import com.coreweb.util.Misc;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.AbstractJasperExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperDocxExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JREmptyDataSource;

public abstract class ReporteSimple extends ReporteDefinicion implements ReporteInterface {

	MiscReporte miscReporte = new MiscReporte();

	AbstractJasperExporterBuilder exporter;
	String tipoFormato = ReporteDefinicion.EXPORT_PDF;
	JasperReportBuilder rep;
	public ComponentBuilder body;
	private String archivoSalida = "./archivoSalida";
	public int copias = 1;
	public int altura = 100;
	private String directorioBase = "./";
	private boolean borrarDespuesDeVer = true;
	private String titulo = "";

	private int anchoPagina = 600;
	private int alturaPagina = 955;
	private PageOrientation orientacionPagina = PageOrientation.PORTRAIT;

	private MyReport reporte = null;

	private Misc m = new Misc();

	@Override
	public String getDirectorioBase() {
		return directorioBase;
	}

	@Override
	public void setDirectorioBase(String directorioBase) {
		this.directorioBase = directorioBase;
	}

	/**
	 * Nada para reportes simple
	 */
	@Override
	public void setUsuario(String usuario) {

	}

	/**
	 * Nada para reportes simple
	 */
	@Override
	public void setEmpresa(String empresa) {

	}

	public ReporteSimple() {
	}

	private void build() {
		Templates tmp = new Templates();
		rep = new JasperReportBuilder();

		rep.setTemplate(tmp.reportTemplate);
		rep.setPageFormat(this.anchoPagina, this.alturaPagina, this.orientacionPagina);
		rep.setColumnStyle(Templates.columnStyle);
		rep.setDetailSplitType(SplitType.PREVENT);

		rep.setDataSource(new JREmptyDataSource());

		VerticalListBuilder vv = cmp.verticalList(); // .setFixedHeight(this.altura)
		System.out.println("this.altura:" + this.altura);
		for (int i = 0; i < this.copias; i++) {
			VerticalListBuilder vBo = cmp.verticalList().setFixedHeight(this.altura);
			vBo.add(this.body);
			vv.add(vBo);
			// if (i < (this.copias -1)){
			// vv.add(cmp.line());
			// }

		}
		this.body = vv;

		// vv.setStyle(stl.style().setBackgroundColor(Color.gray));

		rep.addDetail(this.body).setDetailSplitType(SplitType.STRETCH);

	}

	@Override
	public String getUrlReporte() {
		String out = Config.DIRECTORIO_WEB_REPORTES + "/" + this.getArchivoSalida();
		return out;
	}

	@Override
	public String getTitulo() {
		return titulo;
	}

	public int getAnchoPagina() {
		return anchoPagina;
	}

	public void setAnchoPagina(int anchoPagina) {
		this.anchoPagina = anchoPagina;
	}

	public int getAlturaPagina() {
		return alturaPagina;
	}

	public void setAlturaPagina(int alturaPagina) {
		this.alturaPagina = alturaPagina;
	}

	public PageOrientation getOrientacionPagina() {
		return orientacionPagina;
	}

	public void setOrientacionPagina(PageOrientation orientacionPagina) {
		this.orientacionPagina = orientacionPagina;
	}

	public void show(boolean ver) throws DRException {
		build();
		if (tipoFormato.equals(ReporteDefinicion.EXPORT_CSV)) {
			exporter = export.csvExporter(this.getArchivoPathReal() + ".csv");
			rep.toCsv((JasperCsvExporterBuilder) exporter);

		} else if (tipoFormato.equals(ReporteDefinicion.EXPORT_PDF)) {
			exporter = export.pdfExporter(this.getArchivoPathReal());
			rep.toPdf((JasperPdfExporterBuilder) exporter);

		} else if (tipoFormato.equals(ReporteDefinicion.EXPORT_XLSX)) {
			exporter = export.xlsxExporter(this.getArchivoPathReal());
			rep.toXlsx((JasperXlsxExporterBuilder) exporter);

		} else if (tipoFormato.equals(ReporteDefinicion.EXPORT_DOCX)) {
			exporter = export.docxExporter(this.getArchivoPathReal());
			rep.toDocx((JasperDocxExporterBuilder) exporter);
		}
		if (ver) {
			rep.show();

		}

	}

	abstract public void setDatosReportes();

	@Override
	public String getArchivoPathReal() {
		String out = Config.DIRECTORIO_REAL_REPORTES + "/" + this.getArchivoSalida();
		return out;
	}

	public String getArchivoSalida() {
		return archivoSalida;
	}

	public void setNombreArchivoSalida(String nombre) {
		this.archivoSalida = "./" + nombre;
	}

	public void xsetArchivoSalida(String archivoSalida) {
		this.archivoSalida = archivoSalida;
	}

	@Override
	public boolean isBorrarDespuesDeVer() {
		return borrarDespuesDeVer;
	}

	public void setBorrarDespuesDeVer(boolean borrarDespuesDeVer) {
		this.borrarDespuesDeVer = borrarDespuesDeVer;
	}

	@Override
	public MyReport getReporte() {
		if (this.reporte == null) {
			this.ejecutar(false);
		}
		return this.reporte;
	}

	@Override
	public void ejecutar(boolean mostrar) {

		this.setOficio(); // por defecto

		String pathCompleto = this.getArchivoPathReal();

		this.reporte = new MyReport(true);
		this.reporte.archivo = pathCompleto;

		this.setDatosReportes();
		this.build();

		this.reporte.setTipoPagina(this.getTipoPagina());
		this.reporte.body = this.body;
		this.reporte.setFormato(ReporteDefinicion.EXPORT_PDF);
		// reporte.setLandscape(this.apaisada);

		this.reporte.rep = this.rep;
		
		this.reporte.show(mostrar);

		if ((mostrar == true) && (this.isBorrarDespuesDeVer() == true)) {
			this.m.borrarArchivo(pathCompleto);
		}

	}

	/*
	 * public ComponentBuilder
	 * 
	 * cmp.horizontalList() .setFixedDimension(557, 20)
	 * .setBackgroundComponent(...) .add( //1. a thin gap element cmp.gap(557,2)
	 * ) .newRow() .add ( //2. a gap of width 70 cmp.gap(70,13), //3. the text
	 * field cmp.text("Hello World").setStyle(...) )
	 */

}
