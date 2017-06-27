package com.coreweb.templateABM;

/************************************************
 * Importante!!
 * Las clases que heredan de Body deben tener dos método especiales
 * init y afterCompose, si o si
 * aunque estén vaciox
 * Ejemplo de como hacerlo :
 
  @Init(superclass=true)
  public void init_NOMBRE_DE_LA_CLASE() {
  }
  
  @AfterCompose(superclass = true)
  public void afterCompose_NOMBRE_DE_LA_CLASE() {
  }
  
 * 
 *************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;

import com.coreweb.control.GenericViewModel;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.agenda.ControlAgendaEvento;
import com.coreweb.extras.browser.Browser;

public abstract class Body extends GenericViewModel {

	private Page pagina;

	// componentes en la barra izquierda
	// Object[] = 0: Componente, 1: commando, 2:Map<String, Object> args
	private List<Object[]> barraAuxiliar = new ArrayList<Object[]>();

	public Page getPagina() {
		return pagina;
	}

	public void setPagina(Page pagina) {
		this.pagina = pagina;
		this.pagina.setAss(this.getAss());
	}

	@Init(superclass = true)
	public void initBody(@ExecutionParam("pageVM") Object pageVM,
			@ContextParam(ContextType.EXECUTION) Execution execution) {

		Page page = (Page) pageVM;
		page.setBody(this);
		this.setPagina(page);
		this.setAss(this.getAss());
		String idObjeto = (String) execution.getAttribute("idObjeto");
		if (idObjeto != null) {
			try {
				System.out.println("[ToDO] probar lo de recuperar un objeto por el id:" + idObjeto);
				DTO dtoAux = this.getDTOById(this.getEntidadPrincipal(), idObjeto);
				this.setDTOCorrienteDirty(dtoAux);
			} catch (Exception ex) {
				System.out.println("Error recuperando DTO: " + ex.getMessage());
				ex.printStackTrace();
			}
		}

	}

	@AfterCompose(superclass = true)
	public void afterComposeGenericBody() {

		// this.addCamposObligotorios(this.mainComponent);
		this.deshabilitarComponentes();

	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes() {

		this.restoreAllReadonlyComponents();
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes() {
		// System.out.println("=== deshabilitarComponentes:" +
		// this.getClass().getName());
		this.readonlyAllComponents();
	}

	public String getAliasFormularioCorriente() {
		return this.getPagina().getAliasFormularioCorriente();
	}

	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub

		return ((this.getDTOCorriente() != null) && (this.getDTOCorriente().esNuevo() == false));
	}



	
	
	// este método debe redefinir la clase que herera
	public Browser getBrowser() {
		return null;
	}

	// este método debe redefinir la clase que herera
	public void afterSave() {

	}

	/**
	 * Este método es invocado cuando se hace el click en el agregarItem. Si es
	 * FALSO, se cancela el agregar item. Se puede usar para poner un control
	 * antes de que se agregue el item. Un ejemplo de uso puede ser que se
	 * quiere que sólo una caja esté abierta, entonces, antes de crear una caja
	 * nnueva, se verifica que todas las cajas estén cerradas.
	 * 
	 * @return
	 */
	public boolean siAgregarIten() {
		return true;
	}

	/*
	 * public void grabarSalir(String mensaje){ // cambio el método doTask, por
	 * eso comenté esta parte //this.getPagina().getFooter().doTask(mensaje); }
	 */

	public void addComponenteToBarraAuxiliar(Component c, String command, Map<String, Object> args) {

		Object[] arr = { c, command, args };
		this.barraAuxiliar.add(arr);
	}

	// ===================================================================

	// Metodo alternativo de grabado del Body
	private boolean grabadoAlternativo = false;

	public boolean isGrabadoAlternativo() {
		return grabadoAlternativo;
	}

	public void setGrabadoAlternativo(boolean grabadoAlternativo) {
		this.grabadoAlternativo = grabadoAlternativo;
	}

	public void metodoGrabadoAlternativo() throws Exception {
		throw new Exception("Falta implementar el 'metodoGrabadoAlternativo'");
	}

	// ===================================================================

	public void desEnmascarar() {
		this.getPagina().getBodyMask().setVisible(false);
	}

	public void enmascarar() {
		this.getPagina().getBodyMask().setVisible(true);
	}

	public void setTextoEnmascarar(String textoEnmascarar) {
		this.getPagina().setTextoEnmascarar(textoEnmascarar);
	}

	public String getTextoEnmascarar() {
		return this.getPagina().getTextoEnmascarar();
	}

	public void setEstadoABMConsulta() {
		this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
		BindUtils.postGlobalCommand(null, null, "deshabilitarComponentes", null);
	}

	// ===================================================================
	// Para saber si hubo cambios en el DTO

	byte[] dtoSerializado = {};

	public void setDTOCorrienteDirty(DTO dto) {
		this.setDTOCorriente(dto);
		this.dtoSerializado = this.m.serializar(dto);
	}

	public void setDTOCorrienteDirty() {
		this.setDTOCorrienteDirty(this.getDTOCorriente());
	}

	public boolean siHuboCambiosEnDTO() {
		return this.m.siDirty(this.getDTOCorriente(), this.dtoSerializado);
	}

	// ===================================================================
	public void clickF1() throws Exception {
	}

	public void clickF2() throws Exception {
	}

	public void clickF3() throws Exception {
	}

	public void clickF4() throws Exception {
	}

	public void clickF5() throws Exception {
	}

	public void clickF6() throws Exception {
	}

	public void clickF7() throws Exception {
	}

	public void clickF8() throws Exception {
	}

	public void clickF9() throws Exception {
	}

	public void clickF10() throws Exception {
	}

	public void clickF11() throws Exception {
	}

	public void clickF12() throws Exception {
	}

	public void clickCtrA() throws Exception {
	}

	public void clickCtrB() throws Exception {
	}

	public void clickCtrF() throws Exception {
	}

	public void clickCtrI() throws Exception {
	}

	public void clickCtrL() throws Exception {
	}

	public void clickCtrS() throws Exception {
	}

	public void clickCtrU() throws Exception {
	}

	public void clickCtrX() throws Exception {
	}

	public void clickCtrY() throws Exception {
	}

	// ===================================================================

	// se verifica antes de hacer una grabación
	public abstract boolean verificarAlGrabar();

	public abstract String textoErrorVerificarGrabar();

	public abstract Assembler getAss();

	public abstract DTO getDTOCorriente();

	public abstract void setDTOCorriente(DTO dto);

	public abstract DTO nuevoDTO() throws Exception;

	public abstract String getEntidadPrincipal();

	public abstract List<DTO> getAllModel() throws Exception;

	
	public boolean siControlCambio(){
		return false;
	}
	
	
	// ======================= AGENDA ====================

	private ControlAgendaEvento ctrAgenda;
	private String ctrAgendaKey = "";
	private List<Object[]> eventosAgenda = new ArrayList<Object[]>();

	public final ControlAgendaEvento getCtrAgenda() {
		if (this.ctrAgenda == null) {
			this.ctrAgenda = new ControlAgendaEvento();
		}
		return this.ctrAgenda;
	}

	public int getCtrAgendaTipo() {
		return 0;
	}

	public String getCtrAgendaKey() {
		String out = this.getEntidadPrincipal() + "-" + this.getDTOCorriente().getId();
		return out;
	}

	public String getCtrAgendaTitulo() {
		String out = "Agenda (" + this.getDTOCorriente().getId() + ")";
		return out;
	}

	/**
	 * Agrega los eventos de la agenda a una lista de eventos que se guarda
	 * luego de Grabar el DTO.
	 * 
	 * @param tipoAgenda
	 * @param claveAgenda
	 * @param tipoDetalle
	 * @param texto
	 * @param link
	 */
	public void addEventoAgenda(int tipoAgenda, String claveAgenda, int tipoDetalle, String texto, String link) {
		Object[] evento = new Object[5];
		evento[0] = tipoAgenda;
		evento[1] = claveAgenda;
		evento[2] = tipoDetalle;
		evento[3] = texto;
		evento[4] = link;
		this.eventosAgenda.add(evento);
	}

	public void addEventoAgenda(String texto) {
		this.addEventoAgenda(this.getCtrAgendaTipo(), this.getCtrAgendaKey(), 0, texto, null);
	}

	public void addEventoAgendaLink(String texto, String link) {
		this.addEventoAgenda(this.getCtrAgendaTipo(), this.getCtrAgendaKey(), 0, texto, link);
	}

	public void grabarEventosAgenda() throws Exception {
		for (Object[] evento : this.eventosAgenda) {
			this.getCtrAgenda().addDetalle((int) evento[0], this.getCtrAgendaKey(), (int) evento[2], (String) evento[3],
					(String) evento[4]);
		}
		this.eventosAgenda = new ArrayList<>();
	}

	// para el manejo del toolbar ========================

	public boolean getAgendaDeshabilitado() throws Exception {
		return true;
	}

	public boolean getImprimirDeshabilitado() throws Exception {
		return true;
	}

	public boolean getInformacionDeshabilitado() throws Exception {
		return true;
	}

	public void showAgenda() throws Exception {
		if (this.getDTOCorriente().getId() < 1) {
			this.mensajeError("No hay información para mostrar. Verifique que haya grabado el registro");
			return;
		}

		int tipoAgenda = this.getCtrAgendaTipo();
		String keyAgenda = this.getCtrAgendaKey();
		String tituloAgenda = this.getCtrAgendaTitulo();

		this.getCtrAgenda().mostrarAgenda(tipoAgenda, keyAgenda, tituloAgenda);
	}

	public void showImprimir() throws Exception {
		this.mensajePopupTemporalWarning("Imprimir NO implementado");
	}

	public void showInformacion() throws Exception {
		this.mensajePopupTemporalWarning("Información NO implementado");
	}

	public void showAnular() throws Exception {
		this.mensajePopupTemporalWarning("Anular NO implementado");
	}

}
