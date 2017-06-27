package com.coreweb.extras.browser;

import java.util.List;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

import com.coreweb.componente.ViewPdf;
import com.coreweb.control.GenericViewModel;
import com.coreweb.extras.reporte.DatosReporte;

public class Browser2Reporte {

	DatosReporte reporte = null;
	List<String> titulosCabecera = null;
	List<Object[]> datos = null;
	String tituloGeneral = "";
	GenericViewModel vm = null;

	public void show() throws Exception {
		reporte.setDatosReportes();
		reporte.setApaisada();
		// titulo del reporte
		reporte.setTitulo(this.tituloGeneral);
		reporte.setBody(this.getGrilla());
		ViewPdf viewPdf = new ViewPdf();
		viewPdf.showReporte(reporte, vm);
	}

	public ComponentBuilder getGrilla() throws Exception {
		ComponentBuilder out = null;

		String[][] cols = new String[titulosCabecera.size()][2];
		for (int i = 0; i < cols.length; i++) {
			cols[i][0] = titulosCabecera.get(i);
			cols[i][1] = "";
		}

		List<Object[]> datos = this.datos;

		out = this.reporte.getTabla(cols, datos, "");
		return out;
	}

	public void setReporte(DatosReporte reporte) {
		this.reporte = reporte;
	}

	public void setTitulosCabecera(List<String> titulosCabecera) {
		this.titulosCabecera = titulosCabecera;
	}

	public void setDatos(List<Object[]> datos) {
		this.datos = datos;
	}

	public void setTituloGeneral(String tituloGeneral) {
		this.tituloGeneral = tituloGeneral;
	}

	public void setVm(GenericViewModel vm) {
		this.vm = vm;
	}

}
