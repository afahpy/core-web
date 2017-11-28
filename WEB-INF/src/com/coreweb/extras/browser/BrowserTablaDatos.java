package com.coreweb.extras.browser;

import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Tbody;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Th;
import org.zkoss.zhtml.Thead;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;

import com.coreweb.control.Control;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;

public class BrowserTablaDatos extends Control {

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
		// System.out.println("after de BrowserTablaDatos");

		Div div = (Div) Path.getComponent("/btd");
		if (div != null) {
			div.getChildren().add(getTabla());
		} else {
			this.mensajePopupTemporalWarning("Elemento no econtrado");
		}
		// System.out.println("fin after de BrowserTablaDatos");
	}

	public Table getTabla() {

		int col = this.datos.getNombres().length;
		String[] nombres = this.datos.getNombres();

		// === tabla
		Table table = new Table();
		table.setId("coreWebBrowserTabla");
		table.setDynamicProperty("class", "display");
		table.setDynamicProperty("cellspacing", "0");
		table.setDynamicProperty("width", "100%");

		// === cabecera

		Thead head = new Thead();
		head.setParent(table);

		Tr tr = new Tr();
		tr.setParent(head);

		for (int i = 0; i < col; i++) {
			Th th = new Th();
			th.setParent(tr);
			Label lb = new Label();
			lb.setParent(th);
			;
			lb.setValue(nombres[i]);
		}

		// === datos

		Tbody tbody = new Tbody();
		tbody.setParent(table);
		ArrayList<Object[]> datos = this.runQuery();

		// System.out.println("--filas/columnas: [" + datos.size() + "/" + col +
		// "]");

		for (int i = 0; i < datos.size(); i++) {
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

}
