package com.coreweb.templateABM;

import java.util.*;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ext.Disable;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.Button;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.control.GenericViewModel;
import com.coreweb.domain.Domain;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.browser.Browser;
import com.coreweb.login.ControlInicio;
import com.coreweb.login.Login;
import com.coreweb.login.LoginUsuario;
import com.coreweb.login.LoginUsuarioDTO;
import com.coreweb.util.MyArray;

public class Toolbar extends GenericViewModel {

	private Page pagina;

	private String estadoABM = "";
	public static String MODO_NADA = " ";
	public static String MODO_EDITAR = "[editar]";
	public static String MODO_BUSCAR = "[buscar]";
	public static String MODO_AGREGAR = "[agregar]";
	public static String MODO_ANULAR = "[anular]";
	public static String MODO_BORRAR = "[borrar]";
	public static String MODO_SOLO_CONSULTA = "[consulta]";

	public Page getPagina() {
		return pagina;
	}

	public void setPagina(Page pagina) {
		this.pagina = pagina;
	}

	@Init(superclass = true)
	public void initToolbar(@ExecutionParam("pageVM") Object pageVM) {

		Page page = (Page) pageVM;
		this.setPagina(page);
		page.setTool(this);
	}

	@AfterCompose(superclass = true)
	public void afterComposeToolbar(
			@ContextParam(ContextType.VIEW) Component xview) {
		Selectors.wireEventListeners(xview, this);
		Selectors.wireComponents(xview, this, false);

		this.deshabilitarComponentes();

	}

	public String getAliasFormularioCorriente() {
		return this.getPagina().getAliasFormularioCorriente();
	}

	public boolean getAgregarDeshabilitado() throws Exception {
		boolean b = this.isDeshabilitado()
				|| !(this.operacionHabilitada(IDCore.O_AGREGAR
						+ this.getAliasFormularioCorriente()));
		return b;
	}

	public boolean getEditarDeshabilitado() throws Exception {
		boolean esDTOnuevo = false;
		boolean esDTOreadonly = false;

		if (this.pagina.getDTOCorriente() == null) {
			esDTOnuevo = true;
		} else {
			esDTOnuevo = this.pagina.getDTOCorriente().esNuevo();
			esDTOreadonly = this.pagina.getDTOCorriente().esReadonly();
		}

		if (esDTOreadonly == true) {
			this.setEstadoABM(MODO_SOLO_CONSULTA);
		}

		boolean b = (this.isDeshabilitado()
				|| esDTOreadonly
				|| !(this.operacionHabilitada(IDCore.O_EDITAR
						+ this.getAliasFormularioCorriente())) || (esDTOnuevo == true));

		return b;
	}

	public boolean getAnularDeshabilitado() throws Exception {
		boolean esDTOnuevo = false;
		boolean esDTOreadonly = false;

		if (this.pagina.getDTOCorriente() == null) {
			esDTOnuevo = true;
		} else {
			esDTOnuevo = this.pagina.getDTOCorriente().esNuevo();
			esDTOreadonly = this.pagina.getDTOCorriente().esReadonly();
		}

		if (esDTOreadonly == true) {
			this.setEstadoABM(MODO_SOLO_CONSULTA);
		}

		boolean b = (this.isDeshabilitado()
				|| esDTOreadonly
				|| !(this.operacionHabilitada(IDCore.O_ANULAR
						+ this.getAliasFormularioCorriente())) || (esDTOnuevo == true));

		return b;
	}

	public boolean getBuscarDeshabilitado() throws Exception {
		boolean b = this.isDeshabilitado()
				|| !(this.operacionHabilitada(IDCore.O_BUSCAR
						+ this.getAliasFormularioCorriente()));
		return b;
	}

	public boolean getEliminarDeshabilitado() throws Exception {
		boolean esDTOnuevo = false;
		if ((this.pagina.getDTOCorriente() == null)
				|| (this.pagina.getDTOCorriente().esNuevo() == true)) {
			esDTOnuevo = true;
		}

		boolean b = (this.isDeshabilitado()
				|| !(this.operacionHabilitada(IDCore.O_ELIMINAR
						+ this.getAliasFormularioCorriente())) || (esDTOnuevo == true));
		return b;
	}

	public boolean getAgendaDeshabilitado() throws Exception {
		return this.getPagina().getBody().getAgendaDeshabilitado();
	}

	public boolean getImprimirDeshabilitado() throws Exception {
		return this.getPagina().getBody().getImprimirDeshabilitado();
	}

	public boolean getInformacionDeshabilitado() throws Exception {
		return this.getPagina().getBody().getInformacionDeshabilitado();
	}

	public boolean getCambioUsuarioDeshabilitado() throws Exception {
		return !(this.isDeshabilitado());
	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes() {
		this.disableAllComponents();
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes() {
		this.restoreAllDisabledComponents();
	}

	@Command
	@NotifyChange("*")
	public void agregarItem() throws Exception {
		

		if (this.getPagina().getBody().siAgregarIten() == false){
			// no hacer el agregar
			return;
			
		}
		
		// String texLabel = this.pagina.getTextoFormularioCorriente()
		// + " (Agregar)";
		// this.setTextoFormularioCorriente(texLabel);
		this.setEstadoABM(MODO_AGREGAR);

		DTO dtoAux = this.getPagina().getBody().nuevoDTO();
		this.getPagina().getBody().setDTOCorrienteDirty(dtoAux);
		
		BindUtils.postGlobalCommand(null, null, "habilitarComponentes", null);

	}

	@Command
	public void editarItem() {
		// String texLabel = this.pagina.getTextoFormularioCorriente()
		// + " (Editar)";
		// this.setTextoFormularioCorriente(texLabel);
		this.setEstadoABM(MODO_EDITAR);
	}

	@Command
	public void anularItem() throws Exception {
		this.setEstadoABM(MODO_ANULAR);
		Button b = Messagebox.show("Anular el registro?", "Anular",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if (b.compareTo(Messagebox.Button.YES) == 0) {

			this.getPagina().getBody().showAnular();

		}
		this.setEstadoABM(MODO_NADA);

	}

	@Command
	public void eliminarItem() {
		// String texLabel = this.pagina.getTextoFormularioCorriente()
		// + " (Eliminar)";
		// this.setTextoFormularioCorriente(texLabel);

		this.setEstadoABM(MODO_BORRAR);

		Button b = Messagebox.show("Eliminar el registro?", "Eliminar",
				new Messagebox.Button[] { Messagebox.Button.YES,
						Messagebox.Button.NO }, Messagebox.QUESTION, null);
		if (b.compareTo(Messagebox.Button.YES) == 0) {

			this.pagina.borrarDTOCorriente();
		}

		// texLabel = this.pagina.getTextoFormularioCorriente();
		// this.setTextoFormularioCorriente(texLabel);

		this.setEstadoABM(MODO_NADA);

	}

	@NotifyChange("*")
	@Command
	public void buscarItem() throws Exception {
		// String texLabel = this.pagina.getTextoFormularioCorriente()
		// + " (Buscar)";
		// this.setTextoFormularioCorriente(texLabel);

		this.setEstadoABM(MODO_BUSCAR);

		Browser br = this.getPagina().getBody().getBrowser();

		if (br == null) {
			// lo hace de forma clasica, usando el buscar all dtos

			List listaOrdenada = this.pagina.getBody().getAllModel();
			Collections.sort(listaOrdenada);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("modelo", new ListModelList<DTO>(listaOrdenada));
			map.put("dtoCC", this.pagina.getBody().getDTOCorriente());
			map.put("body", this.pagina.getBody());
			map.put("toolbar", this);

			Window window = (Window) Executions.createComponents(
					"/core/template/Finder.zul", null, map);
			window.doModal();

		} else {
			// tiene un browser definido
			br.show();
			if (br.isClickAceptar() == true) {

				br.setDatosParaNavegacion(this);

				MyArray m = br.getSelectedItem();

				// obtener un DTO del MyArray

				System.out.println("\n\n\n\nthis.getPagina().getBody().getEntidadPrincipal():"+this.getPagina().getBody().getEntidadPrincipal()+"\n\n\n\n");
				
				Assembler as = this.getPagina().getBody().getAss();
				DTO dto = as.getDto(this.getPagina().getBody()
						.getEntidadPrincipal(), m);
				this.getPagina().getBody().setDTOCorrienteDirty(dto);

			}
		}

		this.setEstadoABM(MODO_NADA);
		// BindUtils.postNotifyChange(null, null, this, "*");
		// BindUtils.postGlobalCommand(null, null, "deshabilitarComponentes",
		// null);

	}

	// este es el original, el que anda ahora
	@Command
	public void buscarItem_Ori() throws Exception {
		// String texLabel = this.pagina.getTextoFormularioCorriente()
		// + " (Buscar)";
		// /this.setTextoFormularioCorriente(texLabel);

		this.setEstadoABM(MODO_BUSCAR);

		List listaOrdenada = this.pagina.getBody().getAllModel();
		Collections.sort(listaOrdenada);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modelo", new ListModelList<DTO>(listaOrdenada));
		map.put("dtoCC", this.pagina.getBody().getDTOCorriente());
		map.put("body", this.pagina.getBody());

		Window window = (Window) Executions.createComponents(
				"/core/template/Finder.zul", null, map);
		window.doModal();

		// texLabel = this.pagina.getTextoFormularioCorriente();
		// this.setTextoFormularioCorriente(texLabel);
		this.setEstadoABM(MODO_NADA);
	}

	@Wire
	Toolbarbutton buscar2;

	@Listen("onClick=#buscar2")
	public void buscarMetodo() throws Exception {
		System.out.println("=============== buscar 2 ==============");
		String texLabel = this.pagina.getTextoFormularioCorriente()
				+ " (Buscar)";
		this.setTextoFormularioCorriente(texLabel);

		List listaOrdenada = this.pagina.getBody().getAllModel();
		Collections.sort(listaOrdenada);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("modelo", new ListModelList<DTO>(listaOrdenada));
		map.put("dtoCC", this.pagina.getBody().getDTOCorriente());
		map.put("body", this.pagina.getBody());

		Window window = (Window) Executions.createComponents(
				"/core/template/Finder.zul", null, map);
		window.doModal();

		texLabel = this.pagina.getTextoFormularioCorriente();
		this.setTextoFormularioCorriente(texLabel);

		BindUtils.postGlobalCommand(null, null, "refreshComponentes", null);

		// System.out.println("================= F-global command refresh componente");
		// System.out.println("================= I-Readonly componentes");
		// this.getPagina().getBody().readonlyAllComponents();
		// System.out.println("================= F-Readonly componentes");
		// BindUtils.postGlobalCommand(null, null, "refreshComponentes", null);
		// BindUtils.postNotifyChange(null, null, this, "*");
		// this.getPagina().getBody().readonlyAllComponents();

	}

	@Command
	public void showAgenda() throws Exception { 
		if (this.getAgendaDeshabilitado() == false) {
			this.getPagina().getBody().showAgenda();
		} else {
			this.mensajePopupTemporalWarning("Error, showAgenda no implementado");
		}
	}

	@Command
	public void showImprimir() throws Exception {
		if (this.getImprimirDeshabilitado() == false) {
			this.getPagina().getBody().showImprimir();
		} else {
			this.mensajePopupTemporalWarning("Error, showImprimir no implementado");
		}
	}

	@Command
	public void showInformacion() throws Exception {
		if (this.getInformacionDeshabilitado() == false) {
			this.getPagina().getBody().showInformacion();
		} else {
			this.mensajePopupTemporalWarning("Error, showInformacion no implementado");
		}

	}

	public String getUsuarioTemporal() {
		String usuarioTemporal = "";

		if (this.isHayUsuarioTemporal() == true) {
			usuarioTemporal = this.getUs().getNombre();
		}

		return usuarioTemporal;

	}

	@Command
	public void cambioUsuario() throws Exception {
		this.cambiarUsuarioTemporal();
	}

	public String getIdObjeto() {
		String out = "-";
		try {
			out = this.pagina.getDTOCorriente().getId() + "";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "(" + out + ")";
	}

	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getEstadoABM() {
		return estadoABM;
	}

	public void setEstadoABM(String estadoABM) {
		this.estadoABM = estadoABM;
	}

	// buscar ========================

	private int nBrowser = 0;
	private int ccBrowser = 0;
	private List listBrowser = new ArrayList();

	private long getIdListBrowser(int p) {
		long l = 0;
		Object obj = this.listBrowser.get(p);
		if (obj instanceof IiD) {
			l = ((IiD) obj).getId();
		} else {
			l = (long) ((Object[]) obj)[0];
		}
		return l;
	}

	public int getnBrowser() {
		return nBrowser;
	}

	public void setnBrowser(int nBrowser) {
		this.nBrowser = nBrowser;
	}

	public int getCcBrowserPos() {
		if (this.nBrowser == 0) {
			return 0;
		}
		return (ccBrowser + 1);
	}

	public int getCcBrowser() {
		return ccBrowser;
	}

	public void setCcBrowser(int ccBrowser) {
		this.ccBrowser = ccBrowser;
	}

	public String getStrBrowser() {
		if (this.getnBrowser() == 0) {
			return "";
		}
		String out = "";
		out = "[" + this.getCcBrowserPos() + " / " + this.getnBrowser() + "]";
		return out;
	}

	public void setBotonesNavegacion(List modelx, int indice) {
		this.setListBrowser(modelx);
		this.setnBrowser(modelx.size());
		this.setCcBrowser(indice);
	}

	@Command
	public void afterPosicion() throws Exception {
		if (this.getCcBrowserPos() < this.getnBrowser()) {
			Body body = this.getPagina().getBody();

			this.ccBrowser++;

			long id = this.getIdListBrowser(this.ccBrowser);
			DTO dto = this.getPagina().getBody()
					.getDTOById(body.getEntidadPrincipal(), id);
			body.setDTOCorrienteDirty(dto);

		} else {
			this.mensajePopupTemporal("Fin", 1);
		}

	}

	@Command
	public void beforePosicion() throws Exception {
		if ((this.getCcBrowserPos() > 1) && (this.getnBrowser() > 0)) {
			Body body = this.getPagina().getBody();

			this.ccBrowser--;

			long id = this.getIdListBrowser(this.ccBrowser);
			DTO dto = this.getPagina().getBody()
					.getDTOById(body.getEntidadPrincipal(), id);
			body.setDTOCorrienteDirty(dto);
		} else {
			this.mensajePopupTemporal("Inicio", 1);
		}

	}

	public List<IiD> getListBrowser() {
		return listBrowser;
	}

	public void setListBrowser(List<IiD> listBrowser) {
		this.listBrowser = listBrowser;
	}

}