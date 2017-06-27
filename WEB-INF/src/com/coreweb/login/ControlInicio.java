package com.coreweb.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Window;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.domain.Register;
import com.coreweb.domain.Usuario;
import com.coreweb.dto.Assembler;
import com.coreweb.extras.alerta.ControlAlertas;
import com.coreweb.extras.carita.Carita;
import com.coreweb.util.Misc;

public class ControlInicio extends Control implements DesktopCleanup {

	private Carita carita = new Carita();
	private Component view;

	public ControlInicio(Assembler ass) {
		super(ass);
	}

	public ControlInicio() {
		super(null);
	}

	String menu = "";
	String menuSistema = "";

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getMenuSistema() {
		return menuSistema;
	}

	public void setMenuSistema(String menuSistema) {
		this.menuSistema = menuSistema;
	}

	@Init(superclass = true)
	public void init(@QueryParam("menu") String menu, @QueryParam("menuSistema") String menuSistema,
			@ContextParam(ContextType.VIEW) Component view) {
		Session s = this.getSessionZK();
		s.setAttribute(Config.USUARIO, null);
		this.setMenu(menu);
		this.setMenuSistema(menuSistema);
		this.view = view;

		// el control en la session para luego manipular las alertas
		this.setAtributoSession(Config.MI_ALERTAS, this);
		this.setAtributoSession(Config.CONTROL_INICIO, this);
	}

	@GlobalCommand
	@NotifyChange("*")
	public void refreshComponentes() {
	}

	String colorBarra = "background-color:" + Config.COLOR_BARRA + " ; border-color: " + Config.COLOR_BARRA + " ;";

	public String getColorBarra() {
		return this.colorBarra;
	}

	public void setColorBarra(String color) {
		String d = "background-color:XX ; border-color: XX ;";
		d = d.replaceAll("XX", color);
		this.colorBarra = d;
	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes() {
		this.setMenuVisible(false);
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes() {
		this.setMenuVisible(true);
	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarMenu() {
		this.setMenuVisible(true);
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarMenu() {
		this.setMenuVisible(false);
	}

	@Command
	public void mostrarCarita() {
		this.carita.mostrarCarita();
	}

	static final String PATH_POPUP_INFO_USU = "/core/modulo/infoUsuarioPopup.zul";
	private Usuario usuarioActual = new Usuario();
	private boolean mostrarCambioClave = false;
	private boolean habilitarCambioClave = false;
	private String claveAnterior = "";
	private String claveNueva = "";
	private String claveNuevaVerificar = "";

	@Command
	public void informacionUsuario() throws Exception {
		this.mostrarCambioClave = false;
		this.habilitarCambioClave = false;
		Window w = (Window) Executions.createComponents(PATH_POPUP_INFO_USU, this.view, null);
		w.doModal();
	}

	@Command
	@NotifyChange("*")
	public void cambiarClave() {
		this.mostrarCambioClave = true;
		this.habilitarCambioClave = false;
		this.claveAnterior = "";
		this.claveNueva = "";
		this.claveNuevaVerificar = "";
	}

	@Command
	@NotifyChange("mostrarCambioClave")
	public void confirmarClaveNueva() throws Exception {
		if ((this.claveNueva.trim().length() > 0) && (this.claveNueva.equals(this.claveNuevaVerificar))) {
			Register rr = Register.getInstance();
			this.usuarioActual.setClave(this.m.encriptar(this.claveNueva));
			rr.saveObject(usuarioActual, this.getLoginNombre());
			this.mostrarCambioClave = false;
			Clients.showNotification("Clave cambiada..");
		} else {
			this.m.mensajePopupTemporalWarning("Las claves no coinciden..");
		}
	}

	@Command
	@NotifyChange("habilitarCambioClave")
	public void verificarClaveAterior() {
		Register rr = Register.getInstance();
		try {
			this.usuarioActual = rr.getUsuario(this.getUs().getLogin(), this.m.encriptar(this.claveAnterior));
			System.out.println(this.usuarioActual.getId());
			this.habilitarCambioClave = true;
			this.m.mensajePopupTemporal("Clave correcta..");
		} catch (Exception e) {
			this.usuarioActual = new Usuario();
			this.m.mensajePopupTemporalWarning("Clave anterior incorrecta..");
		}
	}

	public void menuItem(Object o, String formAlias) {
		this.menuItem(o, formAlias, "");
	}

	
	public void menuItemPopUp(Object o, String formAlias, boolean siPopUp) {
	    this.menuItem(o, formAlias, "", siPopUp);
	}
	
	

	public void menuItem(Object o, String formAlias, String paramsFromMenu) {
		menuItem(o, formAlias, paramsFromMenu,false);
	}

	public void menuItem(Object o, String formAlias, String paramsFromMenu, boolean siPopUp) {
		if (this.getUs() == null) {
			return;
		}
		String label = this.getUs().formLabel(formAlias);
		String url = this.getUs().formURL(formAlias);
		boolean deshabilitado = this.getUs().formDeshabilitado(formAlias);

		/*
		 * quita del menu la opción que no tiene permiso
		 * 
		 * if (deshabilitado == true){ return; }
		 */

		if (o instanceof Nav) {
			Nav nv = (Nav) o;
			Navitem nvi = new Navitem();
			nvi.setDisabled(deshabilitado);
			nvi.setLabel(label);
			// nvi.addEventListener("onClick", new MenuitemOnclick(url,
			// formAlias, label, paramsFromMenu, this));
			nvi.addEventListener("onClick", new MenuitemOnclick(formAlias, paramsFromMenu, this));

			nv.getChildren().add(nvi);

		} else if (o instanceof Menupopup) {
			Menupopup mp = (Menupopup) o;

			Menuitem m = new Menuitem();
			m.setDisabled(deshabilitado);
			m.setLabel(label.toUpperCase());
			m.setIconSclass("z-icon-caret-right");
			m.setStyle("font-size:12px");
			m.addEventListener("onClick", new MenuitemOnclick(formAlias, paramsFromMenu, this, siPopUp));
			mp.getChildren().add(m);

		} else if (o instanceof Menubar) {
			Menubar mp = (Menubar) o;

			Menuitem m = new Menuitem();
			m.setDisabled(deshabilitado);
			m.setLabel(label.toUpperCase());
			m.setIconSclass("z-icon-caret-right");
			m.setStyle("font-size:12px");
			m.addEventListener("onClick", new MenuitemOnclick(formAlias, paramsFromMenu, this, siPopUp));
			mp.getChildren().add(m);
		}
	}

	public void menuItemUser(Object o) {

		Menubar mp = (Menubar) o;
		Menuitem m = new Menuitem();

		m.setLabel(this.getUs().getNombre());
		m.addEventListener("onClick", new MenuitemUser(this.getUs(), this));

		mp.getChildren().add(m);
	}

	// Menu visible ================
	public boolean menuVisible = true;

	public boolean isMenuVisible() {
		return menuVisible;
	}

	public void setMenuVisible(boolean menuVisible) {
		this.menuVisible = menuVisible;
	}

	// ====================== ALERTAS =======================

	private boolean hayAlertas = true;
	private int cantidadAlertas = 0;

	public boolean isHayAlertas() {
		return this.hayAlertas;
	}

	public void setHayAlerta() {
		this.hayAlertas = true;
	}

	@Command
	public void mostrarAlertas() {
		ControlAlertas alertaControl = new ControlAlertas();
		alertaControl.mostrarAlertas();
	}

	public String getMisAlertas() {
		String err = "";
		if (true || (this.isHayAlertas() == true)) {
			try {
				this.cantidadAlertas = this.getCantidadAlertasNoCanceladas();
				this.hayAlertas = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				err = "[error] ";
			}
		}
		String out = "Mis Alertas " + err + "(" + this.cantidadAlertas + ")"; // ["+this+"]
																				// "+this.getLoginNombre();
		return out;
	}

	public int getCantidadAlertasNoCanceladas() throws Exception {
		int cant = 0;
		String login = this.getLoginNombre();
		Register rr = Register.getInstance();
		cant = rr.getCantidadAlertasNoCanceladas(login);
		// System.out.println("cantidad de alertas: "+cant);
		return cant;
	}

	public void refreshAlertas() {
		// this.setHayAlerta();
		System.out.println("=================== Refrescando ============");
		BindUtils.postNotifyChange(null, null, this, "*");

		// EventQueue que = EventQueues.lookup("groupTest",
		// EventQueues.APPLICATION, true);
	}

	/**
	 * para la implementacion de la busqueda de productos..
	 */
	@Command
	public void buscarArticulos() throws Exception {
		try {
			Object ctr = this.getSessionZK().getAttribute(Config.CONTROLADOR_BUSCADOR_PRODUCTOS);
			this.m.ejecutarMetoto(ctr, "buscarArticulos");
		} catch (Exception e) {
			System.out.println("no se encontro el metodo [buscarArticulos]");
		}
	}

	/**
	 * para la implementacion de la vista de mis ventas..
	 */
	@Command
	public void misVentas() throws Exception {
		try {
			Object ctr = this.getSessionZK().getAttribute(Config.CONTROLADOR_MIS_VENTAS);
			this.m.ejecutarMetoto(ctr, "misVentas");
		} catch (Exception e) {
			System.out.println("no se encontro el metodo [misVentas]");
		}
	}

	/**
	 * para la implementacion del visor de cta cte..
	 */
	@Command
	public void visorCtaCte() throws Exception {
		try {
			Object ctr = this.getSessionZK().getAttribute(Config.CONTROLADOR_VISOR_CTACTE);
			this.m.ejecutarMetoto(ctr, "visorCtaCte");
		} catch (Exception e) {
			System.out.println("no se encontro el metodo [visorCtaCte]");
		}
	}

	@Override
	public void cleanup(Desktop arg0) throws Exception {
		Session s = Sessions.getCurrent();
		String login = (String) s.getAttribute(Config.LOGIN);
		String ip = (String) s.getAttribute(Config.IP_ADDRESS);
		if (this.getDtoUtil() != null) {
			this.getDtoUtil().getLogueados().remove(login + ";" + ip);
		}
		System.out.println("LOGOUT.." + login + " - " + new Date());
	}

	/**
	 * @return true si existe un controlador para la busqueda de art..
	 */
	public boolean isBuscarArticuloVisible() {
		return this.getSessionZK().getAttribute(Config.CONTROLADOR_BUSCADOR_PRODUCTOS) != null;
	}

	/**
	 * @return true si existe un controlador para el visor de cta cte..
	 */
	public boolean isVisorCtaCteVisible() {
		return this.getSessionZK().getAttribute(Config.CONTROLADOR_VISOR_CTACTE) != null;
	}

	/**
	 * @return true si existe un controlador para la vista 'Mis ventas'..
	 */
	public boolean isMisVentasVisible() {
		return this.getSessionZK().getAttribute(Config.CONTROLADOR_MIS_VENTAS) != null;
	}

	/**
	 * @return la lista de usuarios logueados..
	 */
	public List<String[]> getUsuariosLogueados() {
		List<String[]> out = new ArrayList<String[]>();
		try {
			
		for (String item : this.getDtoUtil().getLogueados()) {
			String login = item.substring(0, item.indexOf(';'));
			String ip = item.substring(item.indexOf(';') + 1, item.length());
			String[] usu = { login, ip };
			out.add(usu);
		}
		} catch (Exception e) {
			out = new ArrayList<String[]>();
		}
		return out;
	}

	public boolean isMostrarCambioClave() {
		return mostrarCambioClave;
	}

	public void setMostrarCambioClave(boolean mostrarCambioClave) {
		this.mostrarCambioClave = mostrarCambioClave;
	}

	public String getClaveAnterior() {
		return claveAnterior;
	}

	public void setClaveAnterior(String claveAnterior) {
		this.claveAnterior = claveAnterior;
	}

	public String getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(String claveNueva) {
		this.claveNueva = claveNueva;
	}

	public String getClaveNuevaVerificar() {
		return claveNuevaVerificar;
	}

	public void setClaveNuevaVerificar(String claveNuevaVerificar) {
		this.claveNuevaVerificar = claveNuevaVerificar;
	}

	public boolean isHabilitarCambioClave() {
		return habilitarCambioClave;
	}

	public void setHabilitarCambioClave(boolean habilitarCambioClave) {
		this.habilitarCambioClave = habilitarCambioClave;
	}
}

class MenuitemOnclick implements EventListener {

	boolean siPopUp = false;
	String aliasFormulario = "";
	String paramsFromMenu = "";
	Control ctr = null;

	/*
	 * String url = ""; String label = "";
	 * 
	 * public MenuitemOnclick(String aliasFormulario, String label, String
	 * paramsFromMenu, Control ctr) { super(); this.url =
	 * ctr.getUs().formURL(aliasFormulario); this.aliasFormulario =
	 * aliasFormulario; this.label = label; this.paramsFromMenu =
	 * paramsFromMenu; this.ctr = ctr; }
	 */
	public MenuitemOnclick(String aliasFormulario, String paramsFromMenu, Control ctr) {
		this.aliasFormulario = aliasFormulario;
		this.paramsFromMenu = paramsFromMenu;
		this.ctr = ctr;
	}

	public MenuitemOnclick(String aliasFormulario, String paramsFromMenu, Control ctr, boolean siPopUp) {
		this.aliasFormulario = aliasFormulario;
		this.paramsFromMenu = paramsFromMenu;
		this.ctr = ctr;
		this.siPopUp = siPopUp;
	}

	
	public void onEvent(Event event) throws Exception {
		if (this.siPopUp == false){
			this.ctr.abrirFormulario(aliasFormulario, paramsFromMenu);
		}else{
			this.ctr.abrirFormularioConPopUp(aliasFormulario, paramsFromMenu);
		}

	}

}

class MenuitemUser implements EventListener {

	LoginUsuarioDTO userDTO = null;
	Control ctr = null;

	public MenuitemUser(LoginUsuarioDTO userDTO, Control ctr) {
		this.userDTO = userDTO;
		this.ctr = ctr;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {

		String perStr = "Perfiles del usuario:\n";
		String[] arrPer = this.userDTO.getPerfilesDescripcion();
		for (int i = 0; i < arrPer.length; i++) {
			perStr += "  - " + arrPer[i] + "\n";
		}

		this.ctr.mensajeInfo(perStr);

	}

}

class AlertaEvento implements EventListener {

	ControlInicio ctr = null;

	public AlertaEvento(ControlInicio ctr) {
		this.ctr = ctr;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		ctr.refreshAlertas();

	}

}
