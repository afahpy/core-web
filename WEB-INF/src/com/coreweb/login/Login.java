package com.coreweb.login;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Popup;

import com.coreweb.Archivo;
import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;

public class Login extends Control {

	String user = "";
	String pass = "";
	String msg = "";
	
	private List<Object[]> users;
	
	@Wire
	private Combobox name;
	
	@Wire
	private Popup usuarios;

	public Login(Assembler ass) {
		super(ass);
	}

	public Login() {
		super(null);
	}

	@Init
	public void initLogin() {
		Session s = this.getSessionZK();
		s.setAttribute(Config.LOGEADO, new Boolean(false));
		BindUtils.postGlobalCommand(null, null, "deshabilitarMenu", null);
		this.setTextoFormularioCorriente(" ");
		this.loadUsers();
	}

	@AfterCompose
	public void afterComposeLogin(@ContextParam(ContextType.VIEW) Component view) {
		 Selectors.wireComponents(view, this, false);
	}
	
	@Command @NotifyChange("*")
	public void loginOk() throws Exception {
		this.loginOk(false);
	}
	
	@Command @NotifyChange("*")
	public void loginOkBootstrap() throws Exception {
		this.loginOk(true);
	}

	
	public void loginOk(boolean bootstrap) throws Exception {

		LoginUsuario lu = new LoginUsuario();
		LoginUsuarioDTO uDto = lu.log(this.user, this.pass);

		this.setAtributoSession(Config.LOGEADO, uDto.isLogeado());
		this.setAtributoSession(Config.USUARIO, uDto);

		this.setUs(uDto);
		this.poneCarita(uDto.isLogeado());

		Component compTool = Path.getComponent("/templateInicio");
		Control vm = (Control) compTool.getAttribute("vm");
		vm.setUs(uDto);
		

		if (uDto.isLogeado() == true) {

			// registrar el login
			this.registrarLogin();
			
			
			try {
				// recupero el control de esta sesion y lo pongo a escuchar
				// eventos para este login

				ControlInicio miCi = (ControlInicio) this
						.getAtributoSession(Config.MI_ALERTAS);

				EventQueues.lookup(this.getLoginNombre(),
						EventQueues.APPLICATION, true).subscribe(
						new AlertaEvento(miCi));

				// ================

				this.m.ejecutarMetoto(Config.INIT_CLASE,
						Config.INIT_AFTER_LOGIN);
			} catch (Exception e) {
				e.printStackTrace();
				uDto.setLogeado(false);
				System.out.println("Error: Metodo afterLogin\n "
						+ "   InitClase:" + Config.INIT_CLASE + "\n    metodo:"
						+ Config.INIT_AFTER_LOGIN);
				this.msg = "Configuración incorrecta";
				Clients.evalJavaScript("loginFaild()");
				return;
			}

			Include inc = (Include) compTool.getFellow("menu");
			inc.invalidate(); // esto hace un refresh del menu
			
			if ( this.isSiPiePagina() == true){
				Include incS = (Include) compTool.getFellow("menuSistema");
				incS.invalidate(); // esto hace un refresh del menu
			}
			

			Object menuBar = inc.getFellow("menubar");
			habilitarDeshabilitarMenuBar(menuBar);
			
			
			BindUtils.postGlobalCommand(null, null, "habilitarMenu", null);

			this.setTextoFormularioCorriente("Bienvenido");
			this.saltoDePagina(bootstrap? Archivo.okLoginBootstrap : Archivo.okLogin);

		} else {
			this.msg = "Usuario o clave incorrecta";
			Clients.evalJavaScript("loginFaild()");
		}
	}

	//===============================================================
	private void habilitarDeshabilitarMenuBar(Object mobj){
		
		AbstractComponent m = (AbstractComponent) mobj;
		List lcmps = m.getChildren();
		for (Iterator iterator = lcmps.iterator(); iterator.hasNext();) {
			Object dato = (Object) iterator.next();
			
			this.siMenuHabilitado(dato);
			/*
			if (dato instanceof Menu) {
				Menu menu = (Menu) dato;
				menu.setVisible(this.siMenuHabilitado(menu));
			}
			*/
		}
	}

	private boolean siMenuHabilitado(Object m){
				
		// dos casos base
		if (m instanceof Menuitem){
			Menuitem mi = (Menuitem) m;
			boolean visible = (mi.isVisible()==true) && (mi.isDisabled() == false);
			mi.setVisible(visible);
			return visible;
		}
		if (m instanceof Navitem){
			Navitem mi = (Navitem) m;
			boolean visible = (mi.isVisible()==true) && (mi.isDisabled() == false);
			mi.setVisible(visible);
			return visible;
		}
		
		
		// ciclar los hijos
		AbstractComponent ac = (AbstractComponent) m;
		List listHijos = ac.getChildren();
		// si hay algún hijo visible, entonces visible
		boolean visible = false;
		for (int i = 0; i < listHijos.size(); i++) {
			Object hijo = listHijos.get(i);
			visible = visible || siMenuHabilitado(hijo);
		}

		if ((visible == false)&&(m instanceof Nav)){
			Nav mp = (Nav) ac;
			mp.setVisible(visible);
			//mp.close();
		}
		if (m instanceof Menu){
			Menu mp = (Menu) ac;
			ac.setVisible(visible);
		}
		return visible;
		
		/*
		if ((m instanceof Menu)||(m instanceof Menupopup)||(m instanceof Nav)||(m instanceof Navbar) ){
			AbstractComponent ac = (AbstractComponent) m;
			List listHijos = ac.getChildren();
			// si hay algún hijo visible, entonces visible
			boolean visible = false;
			for (int i = 0; i < listHijos.size(); i++) {
				Object hijo = listHijos.get(i);
				visible = visible || siMenuHabilitado(hijo);
			}
			
			if ((visible == false)&&(m instanceof Nav)){
				Nav mp = (Nav) ac;
				mp.setVisible(visible);
				//mp.close();
			}
			if (m instanceof Menu){
				Menu mp = (Menu) ac;
				ac.setVisible(visible);
			}
			
			return visible;
		}
		return false;
		*/
	}
	
	
	
	
	
	//===============================================================
	
	@Command
	public void openUserList() throws Exception {
		if(this.getUsers().size() > 1) {
			this.usuarios.open(this.name, "end_before");
		} else {
			this.usuarios.close();
		}		
	}
	
	private void loadUsers() {
		Register rr = Register.getInstance();
		try {
			this.users = rr.getAllLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@DependsOn("user")
	public List<String> getUsers() {
		List<String> out = new ArrayList<String>();
		
		if (this.user.trim().isEmpty())
			return new ArrayList<String>();
		
		for (Object[] user : this.users) {
			String login = (String) user[0];
			if (login.startsWith(this.user))
				out.add(login);
		}
		return out;
	}
	
	@DependsOn("user")
	public String getNombre() {
		for (Object[] user : this.users) {
			String login = (String) user[0];
			String name = (String) user[1];
			if(this.user.equals(login))
				return name;
		}
		return null;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
