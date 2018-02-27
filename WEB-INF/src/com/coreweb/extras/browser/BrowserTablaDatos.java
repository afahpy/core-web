package com.coreweb.extras.browser;

import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.A;
import org.zkoss.zhtml.Br;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Tbody;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tfoot;
import org.zkoss.zhtml.Th;
import org.zkoss.zhtml.Thead;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;

public class BrowserTablaDatos extends Control {

	private boolean botonColumnas = true;

	public BrowserTablaDatos() {
		super(null);
	}

	public BrowserTablaDatos(Assembler ass) {
		super(ass);
		// TODO Auto-generated constructor stub
	}

	BrowserTablaDatosInterface datos = null;

	@Init(superclass = true)
	public void initBrowserTablaDatos(@ExecutionParam("datos") Object obj) {
		System.out.println("init de BrowserTablaDatos:" + obj);

		BrowserTablaDatosInterface datos = (BrowserTablaDatosInterface) obj;
		this.datos = datos;
	}

	@AfterCompose(superclass = true)
	public void afterComposeBrowserTablaDatos() {
		this.cargarTabla();
	}

	private void cargarTabla() {
		Div div = (Div) Path.getComponent("/btd");
		if (div != null) {
			Components.removeAllChildren(div);

			// div.getChildren().add(this.getTablaColumnas());
			div.getChildren().add(this.getTabla());
		} else {
			this.mensajePopupTemporalWarning("Elemento no econtrado (btd)");
		}
	}

	/**
	 * no se usa
	 * 
	 * @return
	 */
	private Component getTablaColumnas() {

		Div div = new Div();

		if (this.botonColumnas == true) {
			Div divCol = new Div();

			Label lbTex = new Label();
			lbTex.setValue("Columnas: ");

			divCol.getChildren().add(lbTex);

			int col = this.datos.getNombres().length;
			String[] nombres = this.datos.getNombres();

			for (int i = 0; i < col; i++) {
				A a = new A();
				a.setSclass("toggle-vis");
				a.setDynamicProperty("data-column", "" + i + "");

				Label lb = new Label();
				lb.setParent(a);
				lb.setValue("[" + nombres[i] + "] ");

				divCol.getChildren().add(a);

				// Label lb2 = new Label();
				// lb2.setValue(" - ");
				// divCol.getChildren().add(lb2);

			}

			div.getChildren().add(divCol);
			div.getChildren().add(new Br());

		}

		Component tabla = getTabla();
		div.getChildren().add(tabla);

		return div;
	}

	private Table getTabla() {

		int col = this.datos.getNombres().length;
		String[] nombres = this.datos.getNombres();

		// === tabla
		Table table = new Table();
		table.setId(Config.ID_TABLA_DATO);
		table.setDynamicProperty("class", "display");
		table.setDynamicProperty("cellspacing", "0");
		table.setDynamicProperty("width", "100%");

		// === cabecera

		{
			Thead head = new Thead();
			head.setParent(table);

			Tr tr = new Tr();
			tr.setParent(head);

			for (int i = 0; i < col; i++) {
				Th th = new Th();
				th.setParent(tr);
				Label lb = new Label();
				lb.setParent(th);
				lb.setValue(nombres[i]);
			}
		}
		// === footer

		{
			Tfoot foot = new Tfoot();
			foot.setParent(table);

			Tr tr = new Tr();
			tr.setParent(foot);

			for (int i = 0; i < col; i++) {
				Th th = new Th();
				th.setParent(tr);
				Label lb = new Label();
				lb.setParent(th);
				lb.setValue(nombres[i]);
			}

		}

		// === datos

		Tbody tbody = new Tbody();
		tbody.setParent(table);

		ArrayList<Object[]> datos = null;

		if (this.datos.ifUseList() == true) {
			datos = this.datos.getLista();
		} else {
			datos = this.runQuery();
		}

		// System.out.println("--filas/columnas: [" + datos.size() + "/" + col +
		// "]");

		int ndatos = datos.size();
		for (int i = 0; i < ndatos; i++) {
			Tr tri = new Tr();
			tri.setParent(tbody);

			Object[] dato = datos.get(i);

			for (int j = 0; j < col; j++) {
				Td td = new Td();
				td.setParent(tri);

				Label lb = new Label();
				lb.setParent(td);
				lb.setValue(dato[j] + "");
			}
		}

		return table;

	}

	private ArrayList<Object[]> runQuery() {
		ArrayList<Object[]> out = new ArrayList<>();
		try {

			Register rr = Register.getInstance();

			if (this.datos.getQueryParametros() != null) {
				out = (ArrayList<Object[]>) rr.hql(this.datos.getQuery(), this.datos.getQueryParametros());
			} else {
				out = (ArrayList<Object[]>) rr.hql(this.datos.getQuery());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public boolean isBotonColumnas() {
		return botonColumnas;
	}

	public void setBotonColumnas(boolean botonColumnas) {
		this.botonColumnas = botonColumnas;
	}

}
