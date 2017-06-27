package com.coreweb.extras.browser;

import java.lang.reflect.Constructor;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;

import com.coreweb.Config;
import com.coreweb.control.GenericViewModel;
import com.coreweb.domain.Register;
import com.coreweb.extras.reporte.DatosReporte;


public class Browser2ViewModel extends GenericViewModel {

	private Browser2 br2 = null;

	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		return false;
	}

	@Init(superclass = true)
	public void initBrowser2ViewModel(@ContextParam(ContextType.EXECUTION) Execution execution,
			@QueryParam("browser") String browserClass, @QueryParam("aliasForm") String aliasForm) throws Exception {

		if (browserClass == null) {
			browserClass = (String) execution.getAttribute("browser");
			aliasForm = (String) execution.getAttribute("aliasForm");
		}

		System.out.println("\n\n\n aliasForm:" + aliasForm + "\n\n\n");

		String labelF = this.getUs().formLabel(aliasForm);
		this.setAliasFormularioCorriente(aliasForm);
		this.setTextoFormularioCorriente(labelF);

		this.br2 = (Browser2) this.m.newInstance(browserClass);
		this.br2.crearBrowser();

	}

	@AfterCompose(superclass = true)
	public void afterComposeBrowser2ViewModel() {
		Div gridBrowser = (Div) this.mainComponent.getFellow("gridBrowser");
		gridBrowser.getChildren().add(this.br2.getGrid());

		String cabeceraZul = this.br2.getCabeceraZulUrl();
		if ((cabeceraZul != null) && (cabeceraZul.trim().length() > 0)) {
			Include cabeceraBrowser = (Include) this.mainComponent.getFellow("cabeceraBrowser");
			cabeceraBrowser.setDynamicProperty(Config.BROWSER2_VM, this.br2);
			cabeceraBrowser.setSrc(this.br2.getCabeceraZulUrl());
		}

	}

	@Command
	public void imprimir() throws Exception {

		Browser2Reporte br = new Browser2Reporte();
		br.setReporte(this.br2.getReporteVacio());
		br.setTituloGeneral(this.br2.getTituloReporte());
		br.setTitulosCabecera(this.br2.getColumnNames());
		br.setDatos(this.br2.getDatos());
		br.setVm(this);
		br.show();

		System.out.println("-----------------------------------------------");
		System.out.println("Explorador");

		List<String> cols = this.br2.getColumnNames();
		String titulos = "";
		for (int i = 0; i < cols.size(); i++) {
			titulos += cols.get(i) + " - ";
		}
		System.out.println(titulos);

		List<Object[]> datos = this.br2.getDatos();
		for (int i = 0; i < datos.size(); i++) {
			Object[] dd = datos.get(i);

			String linea = "";
			for (int j = 0; j < dd.length; j++) {
				linea += dd[j] + " - ";
			}
			System.out.println(linea);
		}
		System.out.println("-----------------------------------------------");

	}

	@Command
	public void exportarCSV() throws Exception {

		String sep = Config.CSV_SEPARADOR;

		this.mensajePopupTemporalWarning("exportarCSV");
		String query = this.br2.getQueryCSV();

		String[] titulos = this.br2.getCamposCSV();

		StringBuffer datosArchivo = new StringBuffer();
		String linea = "";
		// carga el titulo
		linea += "cantidad";
		for (int i = 0; i < titulos.length; i++) {
			linea += sep + titulos[i];
		}
		datosArchivo.append(linea + "\n");

		// carga los valores
		// Register rr = Register.getInstance();
		// List<Object[]> li = rr.hql(query);
		List<Object[]> li = this.br2.getModeloSinLimite();
		for (int i = 0; i < li.size(); i++) {
			linea = (i + 1) + "";
			Object[] fila = li.get(i);
			for (int j = 0; j < fila.length; j++) {
				String dd = "" + fila[j];
				linea += sep + dd;
			}
			datosArchivo.append(linea + "\n");
		}

		String fileName = Config.DIRECTORIO_REAL_REPORTES + "/br-" + System.nanoTime() + ".csv";
		this.m.grabarStringToArchivo(fileName, datosArchivo.toString());
		System.out.println("--- > fileName:" + fileName);

		Filedownload.save(datosArchivo.toString(), "text/plain", "datos.csv");
	}

	public Browser2 getBr2() {
		return br2;
	}

	public void setBr2(Browser2 br2) {
		this.br2 = br2;
	}

}
