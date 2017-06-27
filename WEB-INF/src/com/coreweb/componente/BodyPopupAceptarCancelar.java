package com.coreweb.componente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import org.zkoss.zul.Caption;

import com.coreweb.Archivo;
import com.coreweb.util.Misc;
import com.coreweb.util.Pair;



public class BodyPopupAceptarCancelar {

	Window windowPopupAceptarCancelar;

	String widthWindows = "400px";
	String heightWindows = "";
	String widthColumnLabel = "100px";
	
	List<Pair> listaComponentes = new ArrayList<Pair>();
	List<Component> listaToolBar = new ArrayList<Component>();

	boolean tenerBotonAceptar = true;
	VerificaAceptarCancelar checkAC = null;
	
	boolean soloBotonCerrar = false;
	
	Misc m = new Misc();
	
	// para saber si se hizo click
	BodyPopupAceptarCancelar controlInicial;
	boolean clickAceptar = false;

	// este es el VM del control
	BodyPopupAceptarCancelar controlVM;
	

	@Init(superclass = true)
	public void initPopupAceptarCancelar(
			@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("control") Object control, 
			@ExecutionArgParam("soloBotonCerrar") boolean soloBotonCerrar, 
			@ExecutionArgParam("checkAC") VerificaAceptarCancelar checkAC) {
		                        
		this.windowPopupAceptarCancelar = (Window) view;
		this.controlInicial = (BodyPopupAceptarCancelar) control;
		this.controlInicial.controlVM = this;
		this.checkAC = checkAC;
		this.soloBotonCerrar = soloBotonCerrar;
		this.setTenerBotonAceptar(this.controlInicial.isTenerBotonAceptar());
		this.setHighWindows(this.controlInicial.getHighWindows());
	}

	public BodyPopupAceptarCancelar getControlVM() {
		return controlVM;
	}

	public void setControlVM(BodyPopupAceptarCancelar controlVM) {
		this.controlVM = controlVM;
	}

	@Command()
	public void aceptar() {
		// si no cumple la verificacion, entonces no sale
		if ((this.checkAC != null)&&(this.checkAC.verificarAceptar()==false)){
			this.m.mensajeError(this.checkAC.textoVerificarAceptar());
			//this.m.mensajePopupTemporalWarning(this.checkAC.textoVerificarAceptar(), 1);
			return;
		}
				
		this.controlInicial.clickAceptar = true;
		this.windowPopupAceptarCancelar.detach();
	}
	
	
	@Command()
	public void cerrar() {
		this.controlInicial.clickAceptar = false;
		this.windowPopupAceptarCancelar.detach();
	}

	

	@Command()
	public void cancelar() {
		// si no cumple la verificacion, entonces no sale
		if ((this.checkAC != null)&&(this.checkAC.verificarCancelar()==false)){
			//this.m.mensajeError(this.checkAC.textoVerificarCancelar());
			this.m.mensajePopupTemporalWarning(this.checkAC.textoVerificarCancelar(), 1);
			return;
		}
		this.controlInicial.clickAceptar = false;
		this.windowPopupAceptarCancelar.detach();
	}

	
	public void addComponente(String label, Component c) {
		Pair p = new Pair(label, c);
		this.listaComponentes.add(p);
	}
	
	public void addToolBarComponente(Component c) {
		this.listaToolBar.add(c);
	}
	
	
	public void addLabel(String texto){
		this.addComponente(texto,null);
	}


	public void showPopup(String titulo){
		Grid grid = new Grid();
		grid.setWidth(this.widthWindows);
		
		// definir la columnas
		Columns cols = new Columns();
		Column col1 = new Column();
		col1.setWidth(this.widthColumnLabel);
		Column col2 = new Column();
		cols.getChildren().add(col1);
		cols.getChildren().add(col2);
		grid.getChildren().add(cols);
		
		Rows rows = new Rows();
		grid.getChildren().add(rows);
	
		
		for (int i = 0; i < this.listaComponentes.size(); i++) {
			Pair p = this.listaComponentes.get(i);
			Label label = new Label();
			label.setValue((String)p.getLeft());
			Component c = (Component)p.getRight();

			Row row = new Row();
			row.getChildren().add(label);
			row.getChildren().add(c);
			
			rows.getChildren().add(row);
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("soloBotonCerrar", this.isSoloBotonCerrar());
		map.put("control", this);
		map.put("titulo", titulo);
		map.put("checkAC", this.checkAC);
		map.put("widthWindows", this.widthWindows);
		Window window = (Window) Executions.createComponents(
				Archivo.PopupAceptarCancelar, null, map);
		
		addToolBarComponents(window);
		
		Vlayout v = (Vlayout)window.getFellow("cuerpo");
		
		v.getChildren().add(grid);
		v.setWidth(this.widthWindows);
		window.setPosition("center");

		//window.setWidth(null);
		//System.out.println("\n\n --------------------------this.heightWindows:"+this.heightWindows+"\n\n");
		if (this.heightWindows.trim().length() > 0){
			window.setHeight(this.heightWindows);
		}

		window.doModal();		
	}
	

	public void showPopupUnaColumna(String titulo){
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("soloBotonCerrar", this.isSoloBotonCerrar());
		map.put("control", this);
		map.put("titulo", titulo);
		map.put("checkAC", this.checkAC);
		map.put("widthWindows", this.widthWindows);

		
		Window window = (Window) Executions.createComponents(
				Archivo.PopupAceptarCancelar, null, map);
		addToolBarComponents(window);
		Vlayout v = (Vlayout)window.getFellow("cuerpo");

		
		Grid grid = new Grid();
		grid.setWidth(this.widthWindows);

		
		
		// definir la columnas
		Columns cols = new Columns();
		Column col1 = new Column();
		col1.setWidth(this.widthWindows);

		cols.getChildren().add(col1);
		grid.getChildren().add(cols);
		
		Rows rows = new Rows();
		grid.getChildren().add(rows);
	
		
		for (int i = 0; i < this.listaComponentes.size(); i++) {
			Pair p = this.listaComponentes.get(i);
			Component c = (Component)p.getRight();
			v.getChildren().add(c);
			Row row = new Row();
			//row.getChildren().add(c);
			
			rows.getChildren().add(row);
		}

		

		//v.getChildren().add(grid);
		v.setWidth(this.widthWindows);
		window.setPosition("center");

		window.setWidth(null);
		if (this.heightWindows.trim().length() > 0){
			window.setHeight(this.heightWindows);
		}

		window.doModal();		
	}

	
	private void addToolBarComponents(Window w){
		Caption cap = (Caption) w.getFellow("captionPopup");
		for (Iterator iterator = this.listaToolBar.iterator(); iterator.hasNext();) {
			Component c = (Component) iterator.next();
			cap.getChildren().add(c);
		}
	}
	
	
	public boolean isClickAceptar(){
		return this.clickAceptar;
	}

	public String getWidthWindows() {
		return widthWindows;
	}

	public void setWidthWindows(String widthWindows) {
		this.widthWindows = widthWindows;
	}

	public String getWidthColumnLabel() {
		return widthColumnLabel;
	}

	public void setWidthColumnLabel(String widthColumnLabel) {
		this.widthColumnLabel = widthColumnLabel;
	}

	public VerificaAceptarCancelar getCheckAC() {
		return checkAC;
	}

	public void setCheckAC(VerificaAceptarCancelar checkAC) {
		this.checkAC = checkAC;
	}

	public String getHighWindows() {
		return heightWindows;
	}

	public void setHighWindows(String highWindows) {
		this.heightWindows = highWindows;
	}

	public boolean isTenerBotonAceptar() {
		return tenerBotonAceptar;
	}

	public void setTenerBotonAceptar(boolean tenerBotonAceptar) {
		this.tenerBotonAceptar = tenerBotonAceptar;
	}

	public boolean isSoloBotonCerrar() {
		return soloBotonCerrar;
	}

	public void setSoloBotonCerrar() {
		this.soloBotonCerrar = true;
	}
	
	
	
	
}

