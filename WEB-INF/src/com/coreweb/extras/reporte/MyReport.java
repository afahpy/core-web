package com.coreweb.extras.reporte;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.AbstractJasperExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.ExporterBuilders;
import net.sf.dynamicreports.jasper.builder.export.JasperCsvExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperDocxExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.WhenNoDataType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

public class MyReport extends ReporteDefinicion {

	DatosReporte reporteCore = null;

	MiscReporte miscReporte = new MiscReporte();

	CabeceraReporte cabecera = new CabeceraReporte();
	List<Object[]> datos = new ArrayList<Object[]>();
	AbstractJasperExporterBuilder exporter;
	String tipoFormato = ReporteDefinicion.EXPORT_PDF;
	public JasperReportBuilder rep;
	ComponentBuilder body;
	ComponentBuilder footer = cmp.verticalList();
	// JasperPdfExporterBuilder pdfExporter;
	boolean landscape = false;
	PageType tipoPagina = PageType.A4;
	boolean putFooter = true;

	String titulo;
	String archivo;
	String usuario;
	String empresa;
	String logoEmpresa = "";
	int logoAncho = 0;
	int logoAlto = 0;
	boolean simple = false;
	boolean membretePropioReporte = false;
	ComponentBuilder membretePropio = null;
	boolean footerPropioReporte = false;
	ComponentBuilder footerPropio = null;

	public MyReport(boolean simple) {
		this.simple = simple;
	}

	public MyReport(DatosReporte reporteCore, boolean membretePropioReporte, ComponentBuilder membretePropio,
			CabeceraReporte cabecera, ComponentBuilder body, ComponentBuilder footer, List<Object[]> datos,
			String empresa, String logoEmpresa, int logoAncho, int logoAlto, String titulo, String usuario,
			String archivo, boolean footerPropioReporte, ComponentBuilder footerPropio) {
		this.reporteCore = reporteCore;
		this.membretePropioReporte = membretePropioReporte;
		this.membretePropio = membretePropio;
		this.cabecera = cabecera;
		this.body = body;
		this.footer = footer;
		this.datos = datos;
		this.empresa = empresa;
		this.titulo = titulo;
		this.usuario = usuario;
		this.archivo = archivo;
		this.logoEmpresa = logoEmpresa;
		this.logoAncho = logoAncho;
		this.logoAlto = logoAlto;
		this.footerPropioReporte = footerPropioReporte;
		this.footerPropio = footerPropio;
	}

	public boolean isLandscape() {
		return landscape;
	}

	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}

	public PageType getTipoPagina() {
		return tipoPagina;
	}

	public void setTipoPagina(PageType tipoPagina) {
		this.tipoPagina = tipoPagina;
	}

	public JasperReportBuilder getRep() {
		return rep;
	}

	public ComponentBuilder getMembrete() {
		Templates tmp = new Templates();
		ComponentBuilder membreteCab = null;
		if (this.membretePropioReporte == false) {
			membreteCab = tmp.createMembretePrincipal(empresa, logoEmpresa, logoAncho, logoAlto, titulo, usuario);
		} else {
			membreteCab = this.membretePropio;
		}
		return membreteCab;
	}

	private void build() {

		// StyleBuilder textStyle = stl.style(Templates.columnStyle).setBorder(
		// stl.pen1Point());

		if (cabecera == null) {
			cabecera = new CabeceraReporte();
		}

		try {

			// Templates template = new Templates();
			Templates tmp = new Templates();

			if (this.simple == false) {
				rep = report();
				rep.setTemplate(tmp.reportTemplate);
				rep.setColumnStyle(Templates.columnStyle);
				rep.setDetailSplitType(SplitType.IMMEDIATE);
				rep.setTitleSplitType(SplitType.IMMEDIATE);
				rep.setPageFormat(this.tipoPagina);
			}

			if (this.landscape == true) {
				rep.setPageFormat(this.tipoPagina, PageOrientation.LANDSCAPE);
			}

			if (this.simple == false) {

				ComponentBuilder membreteCab = null;
				if (this.membretePropioReporte == false) {
					membreteCab = tmp.createMembretePrincipal(empresa, logoEmpresa, logoAncho, logoAlto, titulo,
							usuario);
				} else {
					membreteCab = this.membretePropio;
				}
				rep.title(membreteCab);
			}

			if (this.simple == false) {

				// ####################################
				// si queremos que ponga en todas las páginas algo arriba
				// rep.pageHeader(this.texto("cabecera2"));
				// rep.setDetailHeaderSplitType(SplitType.STRETCH);
				// ####################################

				if (this.datos == null){
					// los datos que  vienen van en el detalle
					rep.addDetail(this.body);					
				}else{
					// tiene cabecera y tiene datos detalles
					rep.addTitle(this.body);
				}
				

				if (this.isPutFooter() == true) {

					if (this.footerPropioReporte == false) {
						rep.pageFooter(tmp.footerComponent);
					} else {
						rep.pageFooter(this.footerPropio);
					}
				}

				rep.setDataSource(this.miscReporte.createDataSource(cabecera.getColumnasDS(), datos));
				rep.addSummary(this.footer);
				rep.setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL);

				List<ColumnGroupBuilder> lGr = new ArrayList<ColumnGroupBuilder>();

				for (Iterator iterator = cabecera.getColumnas().iterator(); iterator.hasNext();) {

					DatosColumnas dc = (DatosColumnas) iterator.next();
					TextColumnBuilder tx = dc.getColumnBuilder();

					if (dc.isAgrupar() == true) {
						ColumnGroupBuilder grupo = grp.group(tx);
						// .setTitleWidth(100)
						// .setHeaderLayout(GroupHeaderLayout.TITLE_AND_VALUE).showColumnHeaderAndFooter();
						lGr.add(grupo);
					} else {
						rep.addColumn(tx);
						if (dc.isTotaliza() == true) {
							rep.subtotalsAtSummary(sbt.sum(tx));
						}
					}
				}
				// los grupos
				for (ColumnGroupBuilder gr : lGr) {
					rep.groupBy(gr);
				}
			}

			if (this.reporteCore != null){
				this.reporteCore.setPropiedadesExtras(rep);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void crearXLSX(String nombreArchivo) throws Exception {
		AbstractJasperExporterBuilder exporterAux = export.xlsxExporter(nombreArchivo);
		rep.toXlsx((JasperXlsxExporterBuilder) exporterAux);
	}

	public void crearDOCX(String nombreArchivo) throws Exception {
		AbstractJasperExporterBuilder exporterAux = export.docxExporter(nombreArchivo);
		rep.toDocx((JasperDocxExporterBuilder) exporterAux);
	}

	public void show(boolean ver) {

		try {

			build();
			if (tipoFormato.equals(ReporteDefinicion.EXPORT_CSV)) {
				exporter = export.csvExporter(archivo);
				rep.toCsv((JasperCsvExporterBuilder) exporter);
			} else if (tipoFormato.equals(ReporteDefinicion.EXPORT_PDF)) {
				exporter = export.pdfExporter(archivo);
				rep.toPdf((JasperPdfExporterBuilder) exporter);
			} else if (tipoFormato.equals(ReporteDefinicion.EXPORT_XLSX)) {
				exporter = export.xlsxExporter(archivo);
				rep.toXlsx((JasperXlsxExporterBuilder) exporter);

			} else if (tipoFormato.equals(ReporteDefinicion.EXPORT_DOCX)) {
				exporter = export.docxExporter(archivo);
				rep.toDocx((JasperDocxExporterBuilder) exporter);
			}

			System.out.println("--->archivo:" + archivo);
			/*
			 * build(); JasperPdfExporterBuilder pdfExporter =
			 * export.pdfExporter(archivo).setEncrypted(false);
			 * rep.toPdf(pdfExporter);
			 */

			if (ver == true) {
				rep.show();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFormato(String exportPdf) {
		tipoFormato = exportPdf;
	}

	public boolean isPutFooter() {
		return putFooter;
	}

	public void setPutFooter(boolean putFooter) {
		this.putFooter = putFooter;
	}

}
