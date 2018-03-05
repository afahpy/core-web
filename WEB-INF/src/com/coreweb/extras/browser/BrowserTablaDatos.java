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
import org.zkoss.zhtml.Input;
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

	private static int ANCHO_INUT = 20;
	private int cantidadMaximaDeFilas = (10 * 1000);
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

	private Component getTabla() {

		// buscar los datos
		ArrayList<Object[]> datos = null;

		if (this.datos.ifUseList() == true) {
			datos = this.datos.getLista();
		} else {
			datos = this.runQuery();
		}

		int ndatos = datos.size();

		// ver que sea un número manejable de datos
		if (ndatos > this.cantidadMaximaDeFilas) {

			String nDatoStr = this.m.formatoNumero(ndatos);
			String nMaxStr = this.m.formatoNumero(this.cantidadMaximaDeFilas);
			Label lb = new Label();
			lb.setValue("Se intenta mostrar " + nDatoStr + " registros, y el máximo es " + nMaxStr + " !");
			return lb;
		}

		int col = this.datos.getNombres().length;
		String[] nombres = this.datos.getNombres();
		// ancho de la columna
		int[] anchos = new int[nombres.length];

		// === tabla
		Table table = new Table();
		table.setId(Config.ID_TABLA_DATO);
		table.setDynamicProperty("class", "display nowrap");
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
				th.setDynamicProperty("align", "left");
				Label lb = new Label();
				lb.setParent(th);
				lb.setValue(nombres[i]);
				this.setAnchoInput(anchos, i, nombres[i]);

			}
		}
		// === footer


		// === datos

		Tbody tbody = new Tbody();
		tbody.setParent(table);

		for (int i = 0; i < ndatos; i++) {
			Tr tri = new Tr();
			tri.setParent(tbody);

			Object[] dato = datos.get(i);

			for (int j = 0; j < col; j++) {
				String valor = dato[j] + "";
				
				Td td = new Td();
				td.setParent(tri);

				Label lb = new Label();
				lb.setParent(td);
				lb.setValue(valor);

				this.setAnchoInput(anchos, j, valor);
				
			}
		}

		
		{
			Tfoot foot = new Tfoot();
			//Thead foot = new Thead();
			foot.setParent(table);

			Tr tr = new Tr();
			tr.setParent(foot);

			for (int i = 0; i < col; i++) {
				Th th = new Th();
				th.setParent(tr);
				
				th.setDynamicProperty("filtro", "filtro");
				th.setDynamicProperty("align", "left");
				
				//Label lb = new Label();
				//lb.setParent(th);
				//lb.setValue(nombres[i]);
				
				Input imp = new Input();
				imp.setDynamicProperty("type", "text");
				imp.setDynamicProperty("placeholder", nombres[i]);
				imp.setDynamicProperty("size", anchos[i]+"");
				
				imp.setParent(th);
//				<input type="text" placeholder="'+title+'"  />

				
			}

		}
		
		return table;

	}

	private void setAnchoInput(int[] ancho, int pos, String dato){
		int pCC = ancho[pos];
		int pNew = dato.trim().length();
		pNew = (pNew > ANCHO_INUT)? ANCHO_INUT : pNew+2;
		
		if (pNew > pCC){
			ancho[pos] = pNew; 
		}
		
		
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

	public int getCantidadMaximaDeFilas() {
		return cantidadMaximaDeFilas;
	}

	public void setCantidadMaximaDeFilas(int cantidadMaximaDeFilas) {
		this.cantidadMaximaDeFilas = cantidadMaximaDeFilas;
	}

}
