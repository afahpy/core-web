package com.coreweb.componente;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Vlayout;

import com.coreweb.Config;
import com.coreweb.control.DisableEnableComponent;
import com.coreweb.control.GenericViewModel;
import com.coreweb.control.SoloViewModel;


public class WindowPopup {

	//----- Window PopUp ------------------------
	// empieza editable
	public static String NUEVO = "-NUEVO-";
	// no editable
	public static String SOLO_LECTURA = "-SOLOLECTURA-";
	// si tiene permiso, habilita el boton de edicion
	public static String EDITABLE = "-EDITABLE-";

	// Habre sin el toolbar general (ojo, puede tener algo propio del formulario)
	public static String SIN_TOOLBAR = "-SINTOOLBAR-";
	//---------------------------------------------

	private boolean soloBotonCerrar = false;
	
	private VerificaAceptarCancelar checkAC = null;
	private Object dato = null;
	private String titulo = "Titulo";
	private String modo = "";
	private String width = "100px";
	private String higth = "300px";
	
	private boolean clickAceptar = false;
	
	List<Component> listaToolBar = new ArrayList<Component>();
	Label lmodo = new Label("-sin definir-");
	Button bEdit = new Button("Editar");

	
	SoloViewModel vm;
	
	public SoloViewModel getVm() {
		return vm;
	}


	public void setVm(SoloViewModel vm) {
		this.vm = vm;
	}


	public void show(String zul) throws Exception{

		String modoTxt = "--no definido--";
		boolean bEditDisable = false;
		boolean siBotonEdit = true;
		boolean siBotonAceptar = true;
		
		Vlayout vl = new Vlayout();

		Include inc = new Include();
		inc.setSrc(zul);
		
		// Para tener una referencia del VM que tiene
		inc.setDynamicProperty(Config.WINDOW_POPUP, this);
		
		// Solo lectura
		if (this.modo.indexOf(SOLO_LECTURA)>=0){
			inc.setDynamicProperty(Config.MODO_SOLO_VIEW_MODEL, Config.MODO_DISABLE);
			modoTxt = "Solo lectura";
			siBotonEdit = false;
			siBotonAceptar = false;
			bEditDisable = true;
		}
		// Es nuevo
		if (this.modo.indexOf(NUEVO)>=0){
			inc.setDynamicProperty(Config.MODO_SOLO_VIEW_MODEL, Config.MODO_NO_DISABLE);
			modoTxt = "Editable";
			siBotonEdit = false;
			siBotonAceptar = true;
			bEditDisable = true;
		}
		// Es editable
		if (this.modo.indexOf(EDITABLE)>=0){
			inc.setDynamicProperty(Config.MODO_SOLO_VIEW_MODEL, Config.MODO_EDITABLE);
			modoTxt = "Solo lectura";
			siBotonEdit = true;
			siBotonAceptar = false;
			bEditDisable = true;
		}
		
		if (this.dato != null){
			inc.setDynamicProperty(Config.DATO_SOLO_VIEW_MODEL, this.dato);
		}
		
		
		// falta pasarle como parametro el dato
		vl.getChildren().add(inc);
		
		BodyPopupAceptarCancelar b = new BodyPopupAceptarCancelar();
		b.setTenerBotonAceptar(siBotonAceptar);

		// Tool bar
		if ((this.modo.indexOf(SIN_TOOLBAR)<0)&&(siBotonEdit==true)){
			lmodo.setValue("("+modoTxt+") ");
			b.addToolBarComponente(lmodo);
			
			bEdit.setDisabled(bEditDisable);
			b.addToolBarComponente(bEdit);
			bEdit.addEventListener(Events.ON_CLICK, new BotonEditarListener(this,lmodo, b));
		}
		
		b.addComponente("vlayout", vl);
		b.setWidthWindows(this.width);
		b.setHighWindows(this.higth);
		b.setCheckAC(this.getCheckAC());
		if (this.soloBotonCerrar == true){
			b.setSoloBotonCerrar();
		}
		
		
		b.showPopupUnaColumna(this.titulo);
		this.clickAceptar = b.isClickAceptar();
	}	
	
	

	
	
	public void permitirEditar(boolean value){
		if (value == true){
			this.bEdit.setDisabled(false);
		}else{
			this.bEdit.setDisabled(true);
		}
	}
	
	public void setSoloBotonCerrar(){
		this.soloBotonCerrar = true;
	}
	
	public void addToolBarComponente(Component c) {
		this.listaToolBar.add(c);
	}
	
	public boolean isClickAceptar() {
		return clickAceptar;
	}



	public Object getDato() {
		return dato;
	}
	public void setDato(Object dato) {
		this.dato = dato;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getModo() {
		return modo;
	}
	public void setModo(String modo) {
		this.modo = modo;
	}


	public String getHigth() {
		return higth;
	}


	public void setHigth(String higth) {
		this.higth = higth;
	}


	public String getWidth() {
		return width;
	}


	public void setWidth(String width) {
		this.width = width;
	}


	public VerificaAceptarCancelar getCheckAC() {
		return checkAC;
	}


	public void setCheckAC(VerificaAceptarCancelar checkAC) {
		this.checkAC = checkAC;
	}


}

class BotonEditarListener implements EventListener {

	WindowPopup wpu;
	Label lmodo;
	BodyPopupAceptarCancelar b;
	// DisableEnableComponent disableEnableComponent = new DisableEnableComponent(new SoloViewModel());
	
	
	public BotonEditarListener(WindowPopup wpu, Label lmodo, BodyPopupAceptarCancelar b){
		this.wpu = wpu;
		this.lmodo = lmodo;
		this.b = b;
	}
	
	@Override
	public void onEvent(Event arg0) throws Exception {
		SoloViewModel vm = this.wpu.getVm();
		if (vm != null){
			vm.restoreAllDisabledComponents();
			this.lmodo.setValue("(Editable) ");
			b.setTenerBotonAceptar(true);
			BindUtils.postNotifyChange(null, null, this, "*");
		}else{
			// this.disableEnableComponent.restoreComponents(null);
			throw new Exception("No está definido el SoloViewModel del WindowPopUp ["+this.wpu.getTitulo()+"]");
		}
	}
	
	
}


