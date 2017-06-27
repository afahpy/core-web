package com.coreweb.extras.reporte;


import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.LongType;
import net.sf.dynamicreports.report.builder.grid.ColumnGridComponentBuilder;
import net.sf.dynamicreports.report.builder.grid.HorizontalColumnGridListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;


import java.util.ArrayList;
import java.util.List;

public class CabeceraReporte {

	private String titulo;
	private String tituloSecundario;
	private String usuario;
	private String fechaReporte;

	private List<DatosColumnas> columnas = new ArrayList<DatosColumnas>();

	
	public void setColumnas(List<DatosColumnas> columnas) {
		this.columnas = columnas;
	}
	
	/*
	public void xaddColumna(String titulo, String tipo) {
		xaddColumna(titulo,tipo, 0);
	}


	public void xaddColumna(String titulo, String tipo, int ancho) {
		DatosColumnas columna = new DatosColumnas(titulo, tipo);
		columna.setAncho(ancho);
		columnas.add(columna);
	}
	*/

	
	
	public List<DatosColumnas> getColumnas(){
		return this.columnas;
	}
	
	
	public String[] getColumnasDS() {
		String[] items = new String[columnas.size() + 1];
		for (int i = 0; i < columnas.size(); i++) {
			DatosColumnas columna = columnas.get(i);
			items[i] = columna.getTitulo().replace(" ", "").toLowerCase();

		}
		return items;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTituloSecundario() {
		return tituloSecundario;
	}

	public void setTituloSecundario(String tituloSecundario) {
		this.tituloSecundario = tituloSecundario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getFechaReporte() {
		return fechaReporte;
	}

	public void setFechaReporte(String fechaReporte) {
		this.fechaReporte = fechaReporte;
	}

}
