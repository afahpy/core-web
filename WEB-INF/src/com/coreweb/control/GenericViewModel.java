package com.coreweb.control;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Constrainted;

import com.coreweb.Config;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.componente.VerificaAceptarCancelar;
import com.coreweb.dto.Assembler;
import com.coreweb.util.Check;
import com.coreweb.util.MyConverter;

public abstract class GenericViewModel extends Control {


	private boolean deshabilitado = true;
	private boolean siempreHabilitado = true;
	public Check check = new Check(this);

	private DisableEnableComponent disableEnableComponent = new DisableEnableComponent(this);
	
	
	/*
	 * esto es para darle un tratamiento especial a algunos que se tienen que
	 * deshabilitar/habilitar de forma especial
	 */


	public GenericViewModel(Assembler ass) throws RemoteException {
		super(ass);
		// TODO Auto-generated constructor stub
	}

	public GenericViewModel() {
		super(null);
	}

	public Component mainComponent = null;

	// creo que este método debería estar en control
	// *ver* Para que se usaba?
	// public abstract String getAliasFormularioCorriente();



	@Init(superclass = true)
	public void initGenericViewModel(
			@ContextParam(ContextType.VIEW) Component view) {

		this.mainComponent = view;

	}

	@AfterCompose(superclass = true)
	public void afterComposeGenericViewModel() {
		this.addCamposObligotorios(this.mainComponent);
	}

	public void readonlyAllComponents(AbstractComponent cmp){
		this.disableEnableComponent.disableComponents(cmp);
	}

	public void restoreAllReadonlyComponents(AbstractComponent cmp) {
		this.disableEnableComponent.restoreComponents(cmp);
	}

	
	public void readonlyAllComponents() {
		this.deshabilitado = true;
		this.disableEnableComponent.disableComponents((AbstractComponent) this.mainComponent);
	}

	public void disableAllComponents() {
		this.deshabilitado = true;
		this.disableEnableComponent.disableComponents((AbstractComponent) this.mainComponent);
	}


	public void restoreAllReadonlyComponents() {
		this.deshabilitado = false;
		this.disableEnableComponent.restoreComponents((AbstractComponent) this.mainComponent);
	}

	public void restoreAllDisabledComponents() {
		this.deshabilitado = false;
		this.disableEnableComponent.restoreComponents((AbstractComponent) this.mainComponent);
	}

	public void setDeshabilitado(boolean deshabilitado){
		this.deshabilitado = deshabilitado;
	}
	
	public boolean isDeshabilitado() {
		return deshabilitado;
	}

	@GlobalCommand
	@NotifyChange("*")
	public void refreshComponentes() {
	}

	// Pone el (*) en los campos con constraint, si tiene constraint es
	// obligatorio
	public void addCamposObligotorios(Component ac) {
		this.addCamposObligotorios2((AbstractComponent) ac);
	}
	
	private AbstractComponent addCamposObligotorios_ORI_DR(AbstractComponent ac) {

		List<AbstractComponent> children2 = new ArrayList<AbstractComponent>();

		boolean nuevaLista = false;
		List children = ac.getChildren();
		

		if (children != null) {
			int len = children.size();
			for (int i = (len - 1); i >= 0; i--) {
				AbstractComponent co = (AbstractComponent) children.get(i);
				AbstractComponent co2 = addCamposObligotorios_ORI_DR(co);
				if (co != co2) {
					children.remove(co);
					children.add(i, co2);
				}
			}
		}

		if (ac instanceof Constrainted) {

			Constrainted c = (Constrainted) ac;
			Constraint cn = c.getConstraint();

			if (cn != null) {
				Component parent = ac.getParent();
				
				Label l = new Label();
				l.setValue("(*)");
				l.setStyle("color:red");

				Hlayout hl = new Hlayout();
				hl.getChildren().add(ac);
				hl.getChildren().add(l);
				
				ac = hl;

			}
		}

		return ac;

	}
	
	private void addCamposObligotorios2(AbstractComponent ac) {

		List<Component> children = ac.getChildren();

		if (children != null) {
			int len = children.size();
			for (int i = (len - 1); i >= 0; i--) {
				AbstractComponent co = (AbstractComponent) children.get(i);			
				addCamposObligotorios2(co);
			}
		}

		if (ac instanceof Constrainted) {

			Constrainted c = (Constrainted) ac;
			Constraint cn = c.getConstraint();

			if (cn != null) {

				Label l = new Label();
				l.setValue("(*)");
				l.setStyle("color:red");
				
				Hlayout hl = new Hlayout();			
				
				ac.getParent().insertBefore(hl, ac);
				
				hl.appendChild(ac);
				hl.appendChild(l);
			}
		}
	}


	private void addCamposObligotorio_SER(AbstractComponent ac) {

		List<Component> children = ac.getChildren();

		if (children != null) {
			int len = children.size();
			for (int i = (len - 1); i >= 0; i--) {
				AbstractComponent co = (AbstractComponent) children.get(i);
				addCamposObligotorio_SER(co);
			}
		}

		if (ac instanceof Constrainted) {

			Constrainted c = (Constrainted) ac;
			Constraint cn = c.getConstraint();

			if (cn != null) {

				Label l = new Label();
				l.setValue("(*)");
				l.setStyle("color:red");
				
				Hlayout hl = new Hlayout();				

				ac.getParent().appendChild(hl);
				hl.appendChild(ac);
				hl.appendChild(l);
			}
		}
	}

	// Crea un nuevo Window recibiendo como parametro el Path del zul..
	public Window createWindow(Window window, String zulPath) {

		window = (Window) Executions.createComponents(zulPath,
				this.mainComponent, null);
		this.addCamposObligotorios(window);

		return window;
	}

	public MyConverter getCnv() {
		return new MyConverter();
	}

	public boolean isSiempreHabilitado() {
		return siempreHabilitado
				&& this.getCondicionComponenteSiempreHabilitado();
	}

	public void setSiempreHabilitado(boolean siempreHabilitado) {
		this.siempreHabilitado = siempreHabilitado;
	}

	public abstract boolean getCondicionComponenteSiempreHabilitado();

	public Check getCheck() {
		return check;
	}

	public void setCheck(Check check) {
		this.check = check;
	}

	
	
	public void enmascarar(Component comp, String texto, int textoSize) {	
		// Pone un texto en formato de mascara de agua
		Vbox v = new Vbox();		
		v.setHeight("100%");
		v.setWidth("100%");
		v.setPack("center");
		v.setAlign("center");

		Label l = new Label();
		l.setValue(texto);
		l.setStyle("font-weight:bold;font-size:"+textoSize+"pt");		
					
		Div mask = new Div();
		mask.setZclass("z-modal-mask");
		mask.setStyle("z-index: 2000; display: block; background:#ffcc99; opacity:0.2; filter:alpha(opacity=30); ");
		
		l.setParent(v);
		v.setParent(mask);
		
		comp.getParent().appendChild(mask);	
	}

	// para poner una imagen los iconoss de la tilde y el contra mano
	public String getImgSiNo(boolean b){
		String out = Config.IMAGEN_CANCEL;
		if (b == true){
			out = Config.IMAGEN_OK;
		}
		return out;
	}
	

	/**
	 * Retorna un String que es el mensaje ..
	 * Retorna vacio en caso de que se dio click en Cancelar..
	 * */
	public String getMensaje(String titulo, String placeHolder) {
		
		Div dv = new Div();
		dv.setHeight("5px");
		dv.setStyle("padding:10px");
		
		Textbox tx = new Textbox();
		tx.setRows(5);
		tx.setHflex("true");
		tx.setPlaceholder(placeHolder);
		tx.setParent(dv);
		
		BodyPopupAceptarCancelar b = new BodyPopupAceptarCancelar();	
		b.addComponente("", dv);
		b.setHighWindows("200px");
		b.setCheckAC(new VerificadorMotivoAnulacion(tx));
		b.showPopupUnaColumna(titulo);		
		if (b.isClickAceptar()) {
			return tx.getValue().trim();
		}		
		return "";
	}	


	/**
	 * Retorna un String que es el motivo de anulacion..
	 * Retorna vacio en caso de que se dio click en Cancelar..
	 * */
	public String getMotivoAnulacion() {
		return this.getMensaje("Motivo de la Anulación", "Especifique el motivo de la anulación");
	}	


}

// verificador del popup del motivo anulacion..
class VerificadorMotivoAnulacion implements VerificaAceptarCancelar {
	
	private String mensaje = "";
	private Textbox txMotivo;
	
	public VerificadorMotivoAnulacion(Textbox txMotivo) {
		this.txMotivo = txMotivo;
	}

	@Override
	public boolean verificarAceptar() {
		boolean out = true;
		this.mensaje = "No se puede completar la operación debido a: ";
		
		if (txMotivo.getValue().trim().length() == 0) {
			out = false;
			this.mensaje += "\n - Debe ingresar el motivo..";
		}
		
		return out;
	}

	@Override
	public String textoVerificarAceptar() {
		return this.mensaje;
	}

	@Override
	public boolean verificarCancelar() {
		return true;
	}

	@Override
	public String textoVerificarCancelar() {
		return "Error al cancelar..";
	}
}

