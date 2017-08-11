package com.coreweb.extras.usuarioPerfil;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Popup;

import com.coreweb.Config;
import com.coreweb.componente.BuscarElemento;
import com.coreweb.componente.VerificaAceptarCancelar;
import com.coreweb.componente.WindowPopup;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.domain.Formulario;
import com.coreweb.domain.Operacion;
import com.coreweb.domain.Perfil;
import com.coreweb.domain.Tipo;
import com.coreweb.domain.Usuario;
import com.coreweb.dto.UtilCoreDTO;
import com.coreweb.util.MyArray;

public class ConfiguracionUsuarioPerfilVM extends SimpleViewModel {

	ControlUsuario controlUsuario = new ControlUsuario();
	static final String PATH_POPUP = "/core/usuario/popupUsuarioPerfil.zul";

	static final int NUEVO_USUARIO = 0;
	static final int NUEVO_PERFIL = 1;
	static final int NUEVO_MODULO = 2;
	static final int NUEVO_FORMULARIO = 3;
	static final int NUEVO_OPERACION = 4;
	static final int EDITAR_OPERACION = 5;
	static final int EDITAR_FORMULARIO = 6;
	static final int EDITAR_MODULO = 7;
	static final int EDITAR_PERFIL = 8;

	private MyArray editItem = new MyArray();

	static final String[] TITULOS = { "Nuevo Usuario", "Nuevo Perfíl", "Nuevo Módulo", "Nuevo Formulario",
			"Nueva Operación", "Modificar Operación", "Modificar Formulario", "Modificar Módulo", "Modificar Perfíl" };

	private boolean visibleUsuario = false;
	private boolean visiblePerfil = false;
	private boolean visibleModulo = false;
	private boolean visibleFormulario = false;
	private boolean visibleOperacion = false;
	private boolean visibleEditarOperacion = false;
	private boolean visibleEditarFormulario = false;
	private boolean visibleEditarModulo = false;
	private boolean visibleEditarPerfil = false;

	private boolean visibleTabsDesarrollador = false;

	// ATRIBUTOS PARA TAB USUARIO

	@Wire("#motivoInactivacion")
	Popup popupMotivoInactivacion;

	private MyArray newUsuario = new MyArray();
	private MyArray selectedUsuario = new MyArray();
	private MyArray selectedPerfilAdd = new MyArray();
	private MyArray selectedPerfilRemove = new MyArray();
	private MyArray usuarioParaCopiarPerfiles = new MyArray();

	private List<MyArray> usuarios = new ArrayList<MyArray>();
	private List<MyArray> perfilesDeUsuario = new ArrayList<MyArray>();

	private String filterLoginUsuario = "";
	private String filterNombreUsuario = "";
	private String motivoDeInactivacion = "";

	private boolean usuarioSeleccionado = false;
	private boolean perfilParaRemoverSeleccionado = false;

	final static String[] ATT_PERFILES = { "nombre", "descripcion", "grupo" };
	final static String[] COLUMNAS_PERFILES = { "Nombre", "Descripción", "Grupo" };

	final static String[] ATT_USUARIOS = { "nombre", "login" };
	final static String[] COLUMNAS_USUARIOS = { "Nombre", "Login" };

	// ATRIBUTOS PARA TAB PERFIL

	private MyArray newPerfil = new MyArray();
	private MyArray selectedPerfil = new MyArray();
	private MyArray selectedOperacionAdd = new MyArray();
	private MyArray selectedOperacionRemove = new MyArray();

	private List<MyArray> perfiles = new ArrayList<MyArray>();
	private List<MyArray> permisosDePerfil = new ArrayList<MyArray>();

	private String filterNombrePerfil = "";
	private String filterDescripcionPerfil = "";
	private String filterGrupoPerfil = "";

	private boolean perfilSeleccionado = false;
	private boolean operacionParaRemoverSeleccionada = false;

	final static String[] ATT_OPERACIONES = { "nombre", "descripcion", "habilitado" };
	final static String[] COLUMNAS_OPERACIONES = { "Nombre", "Descripción", "Habilitado" };

	// ATRIBUTOS PARA TAB MODULO

	private MyArray newModulo = new MyArray();
	private MyArray selectedModulo = new MyArray();
	private MyArray selectedFormularioAdd = new MyArray();
	private MyArray selectedFormularioRemove = new MyArray();

	private List<MyArray> modulos = new ArrayList<MyArray>();
	private List<MyArray> formulariosDeModulo = new ArrayList<MyArray>();

	private String filterNombreModulo = "";
	private String filterDescripcionModulo = "";

	private boolean moduloSeleccionado = false;
	private boolean formularioParaRemoverSeleccionado = false;

	final static String[] ATT_FORMULARIOS = { "label", "alias", "descripcion", "habilitado" };
	final static String[] COLUMNAS_FORMULARIOS = { "Label", "Alias", "Descripción", "Habilitado" };

	// ATRIBUTOS PARA TAB FORMULARIO

	private MyArray newFormulario = new MyArray();
	private MyArray selectedFormuario = new MyArray();
	private MyArray selectedOperacionFormAdd = new MyArray();
	private MyArray selectedOperacionFormRemove = new MyArray();

	private List<MyArray> formularios = new ArrayList<MyArray>();
	private List<MyArray> operacionesDeFormulario = new ArrayList<MyArray>();

	private String filterLabelFormulario = "";
	private String filterAliasFormulario = "";

	private boolean formularioSeleccionado = false;
	private boolean operacionFormParaRemoverSeleccionada = false;

	// ATRIBUTOS PARA TAB OPERACIONES

	private MyArray newOperacion = new MyArray();
	private MyArray selectedOperacion = new MyArray();

	private List<MyArray> operaciones = new ArrayList<MyArray>();

	private String filterAliasOperacion = "";
	private String filterNombreOperacion = "";
	private String filterDescripcionOperacion = "";

	private boolean operacionSeleccionada = false;

	@Init(superclass = true)
	public void init() throws Exception {
		this.verificarPermisosDeDesarrollador();
	}

	@AfterCompose(superclass = true)
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	/***************** COMANDOS *****************/

	@Command
	@NotifyChange("*")
	public void implementando() {
		this.mensajeInfo("Implementando...");
	}

	@Command
	public void crearNuevoItem(@BindingParam("index") int index) throws Exception {
		this.crearNuevoItem_(index);
	}

	@Command
	public void modificarItem(@BindingParam("index") int index) throws Exception {
		this.modificarItem_(index);
	}

	@Command
	@NotifyChange("perfilesDeUsuario")
	public void copiarPerfilesDe() throws Exception {
		this.copiarPerfilesDe_();
	}

	@Command
	@NotifyChange("perfilesDeUsuario")
	public void agregarNuevoPerfilAUsuario() throws Exception {
		this.agregarNuevoPerfilAUsuario_();
	}

	@Command
	@NotifyChange("permisosDePerfil")
	public void agregarNuevaOperacionAPerfil() throws Exception {
		this.agregarNuevaOperacionAPerfil_();
	}

	@Command
	@NotifyChange("formulariosDeModulo")
	public void agregarNuevoFormularioAModulo() throws Exception {
		this.agregarNuevoFormularioAModulo_();
	}

	@Command
	@NotifyChange("operacionesDeFormulario")
	public void agregarNuevaOperacionAFormulario() throws Exception {
		this.agregarNuevaOperacionAFormulario_();
	}

	@Command
	@NotifyChange("perfilesDeUsuario")
	public void removerPerfilDeUsuario() throws Exception {
		if (this.mensajeSiNo("Remover el Perfil del Usuario?"))
			this.removerPerfilDeUsuario_();
	}

	@Command
	@NotifyChange({ "permisosDePerfil", "operacionParaRemoverSeleccionada" })
	public void deshabilitarPermisoOperacionDePerfil() throws Exception {
		this.deshabilitarPermisoOperacionDePerfil_();
	}

	@Command
	@NotifyChange({ "permisosDePerfil", "operacionParaRemoverSeleccionada" })
	public void habilitarPermisoOperacionDePerfil() throws Exception {
		this.habilitarPermisoOperacionDePerfil_();
	}

	@Command
	@NotifyChange({ "formulariosDeModulo", "formularioParaRemoverSeleccionado" })
	public void deshabilitarFormularioDelModulo() throws Exception {
		this.deshabilitarFormularioDelModulo_();
	}

	@Command
	@NotifyChange({ "formulariosDeModulo", "formularioParaRemoverSeleccionado" })
	public void habilitarFormularioDelModulo() throws Exception {
		this.habilitarFormularioDelModulo_();
	}

	@Command
	@NotifyChange({ "operacionesDeFormulario", "operacionFormParaRemoverSeleccionada" })
	public void deshabilitarOperacionDelFormulario() throws Exception {
		this.deshabilitarOperacionDelFormulario_();
	}

	@Command
	@NotifyChange({ "operacionesDeFormulario", "operacionFormParaRemoverSeleccionada" })
	public void habilitarOperacionDelFormulario() throws Exception {
		this.habilitarOperacionDelFormulario_();
	}

	@Command
	@NotifyChange({ "usuarios", "selectedUsuario" })
	public void activarUsuario(@BindingParam("index") boolean index) throws Exception {
		this.activarUsuario_(index);
	}

	@Command
	@NotifyChange({ "usuarios", "selectedUsuario" })
	public void inactivarUsuario() throws Exception {
		this.popupMotivoInactivacion.close();
		controlUsuario.activarInactivarUsuario(this.selectedUsuario.getId(), false, this.getLoginNombre(),
				this.motivoDeInactivacion);
		Clients.showNotification("Usuario inactivado..");
		this.selectedUsuario.setPos3("NO");
	}

	@Command
	@NotifyChange("usuarios")
	public void resetPass() throws Exception {
		this.resetPass_();
	}

	/***************** FUNCIONES *****************/

	public void verificarPermisosDeDesarrollador() throws Exception {
		String propiedadDesarrollador = this.getUs().getPropiedad(Config.USUARIO_DESARROLLADOR);
		this.visibleTabsDesarrollador = propiedadDesarrollador.trim().equals(Config.SIGLA_TIPO_USUARIO_DESARROLLADOR);
	}

	/**
	 * Abre popup para crear nuevo item
	 */
	private void crearNuevoItem_(int index) throws Exception {
		this.visivilizarPopup(index);
		WindowPopup wp = new WindowPopup();
		wp.setTitulo(TITULOS[index]);
		wp.setModo(WindowPopup.NUEVO);
		wp.setDato(this);
		wp.setCheckAC(this.validador(index));
		wp.setWidth("400px");
		wp.setHigth("300px");
		wp.show(PATH_POPUP);
		if (wp.isClickAceptar()) {
			this.grabar(index);
		}
	}

	/**
	 * Abre popup para editar nuevo item
	 */
	private void modificarItem_(int index) throws Exception {
		this.getItemAModificar(index);
		this.visivilizarPopup(index);
		WindowPopup wp = new WindowPopup();
		wp.setTitulo(TITULOS[index]);
		wp.setModo(WindowPopup.NUEVO);
		wp.setDato(this);
		wp.setCheckAC(this.validador(index));
		wp.setWidth("400px");
		wp.setHigth("300px");
		wp.show(PATH_POPUP);
		if (wp.isClickAceptar()) {
			this.grabar(index);
		}
	}

	/**
	 * para seleccionar que formulario mostrar en el popup.zul
	 */
	private void visivilizarPopup(int index) {
		switch (index) {
		case NUEVO_USUARIO:
			this.visibleUsuario = true;
			this.visiblePerfil = false;
			this.visibleModulo = false;
			this.visibleFormulario = false;
			this.visibleOperacion = false;
			this.visibleEditarOperacion = false;
			this.visibleEditarFormulario = false;
			this.visibleEditarModulo = false;
			this.visibleEditarPerfil = false;
			break;

		case NUEVO_PERFIL:
			this.visibleUsuario = false;
			this.visiblePerfil = true;
			this.visibleModulo = false;
			this.visibleFormulario = false;
			this.visibleOperacion = false;
			this.visibleEditarOperacion = false;
			this.visibleEditarFormulario = false;
			this.visibleEditarModulo = false;
			this.visibleEditarPerfil = false;
			break;

		case NUEVO_MODULO:
			this.visibleUsuario = false;
			this.visiblePerfil = false;
			this.visibleModulo = true;
			this.visibleFormulario = false;
			this.visibleOperacion = false;
			this.visibleEditarOperacion = false;
			this.visibleEditarFormulario = false;
			this.visibleEditarModulo = false;
			this.visibleEditarPerfil = false;
			break;

		case NUEVO_FORMULARIO:
			this.visibleUsuario = false;
			this.visiblePerfil = false;
			this.visibleModulo = false;
			this.visibleFormulario = true;
			this.visibleOperacion = false;
			this.visibleEditarOperacion = false;
			this.visibleEditarFormulario = false;
			this.visibleEditarModulo = false;
			this.visibleEditarPerfil = false;
			break;

		case NUEVO_OPERACION:
			this.visibleUsuario = false;
			this.visiblePerfil = false;
			this.visibleModulo = false;
			this.visibleFormulario = false;
			this.visibleOperacion = true;
			this.visibleEditarOperacion = false;
			this.visibleEditarFormulario = false;
			this.visibleEditarModulo = false;
			this.visibleEditarPerfil = false;
			break;

		case EDITAR_OPERACION:
			this.visibleUsuario = false;
			this.visiblePerfil = false;
			this.visibleModulo = false;
			this.visibleFormulario = false;
			this.visibleOperacion = false;
			this.visibleEditarOperacion = true;
			this.visibleEditarFormulario = false;
			this.visibleEditarModulo = false;
			this.visibleEditarPerfil = false;
			break;

		case EDITAR_FORMULARIO:
			this.visibleUsuario = false;
			this.visiblePerfil = false;
			this.visibleModulo = false;
			this.visibleFormulario = false;
			this.visibleOperacion = false;
			this.visibleEditarOperacion = false;
			this.visibleEditarFormulario = true;
			this.visibleEditarModulo = false;
			this.visibleEditarPerfil = false;
			break;

		case EDITAR_MODULO:
			this.visibleUsuario = false;
			this.visiblePerfil = false;
			this.visibleModulo = false;
			this.visibleFormulario = false;
			this.visibleOperacion = false;
			this.visibleEditarOperacion = false;
			this.visibleEditarFormulario = false;
			this.visibleEditarModulo = true;
			this.visibleEditarPerfil = false;
			break;

		case EDITAR_PERFIL:
			this.visibleUsuario = false;
			this.visiblePerfil = false;
			this.visibleModulo = false;
			this.visibleFormulario = false;
			this.visibleOperacion = false;
			this.visibleEditarOperacion = false;
			this.visibleEditarFormulario = false;
			this.visibleEditarModulo = false;
			this.visibleEditarPerfil = true;
			break;
		}
	}

	/**
	 * Para seleccionar cual verfificador de campos retornar
	 */
	private VerificaAceptarCancelar validador(int index) {
		VerificaAceptarCancelar out = null;
		switch (index) {
		case NUEVO_USUARIO:
			out = new ValidadorUsuario(newUsuario);
			break;

		case NUEVO_PERFIL:
			out = new ValidadorPerfil(newPerfil);
			break;

		case NUEVO_MODULO:
			out = new ValidadorModulo(newModulo);
			break;

		case NUEVO_FORMULARIO:
			out = new ValidadorFormulario(newFormulario);
			break;

		case NUEVO_OPERACION:
			out = new ValidadorOperacion(newOperacion);
			break;

		case EDITAR_OPERACION:
			out = new ValidadorOperacion(editItem);
			break;

		case EDITAR_FORMULARIO:
			out = new ValidadorFormulario(editItem);
			break;

		case EDITAR_MODULO:
			out = new ValidadorModulo(editItem);
			break;

		case EDITAR_PERFIL:
			out = new ValidadorPerfil(editItem);
		}
		return out;
	}

	/**
	 * para grabar el nuevo item
	 */
	private void grabar(int index) throws Exception {
		switch (index) {

		case NUEVO_USUARIO:
			this.newUsuario.setPos4(this.m.encriptar(this.newUsuario.getPos4().toString()));
			this.setSelectedUsuario(controlUsuario.agregarNuevoUsuario(this.newUsuario, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "usuarios");
			BindUtils.postNotifyChange(null, null, this, "perfilesDeUsuario");
			BindUtils.postNotifyChange(null, null, this, "usuarioSeleccionado");
			Clients.showNotification("Usuario creado..");
			this.newUsuario = new MyArray();
			break;

		case NUEVO_PERFIL:
			this.setSelectedPerfil(controlUsuario.agregarNuevoPerfil(this.newPerfil, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "perfiles");
			BindUtils.postNotifyChange(null, null, this, "permisosDePerfil");
			BindUtils.postNotifyChange(null, null, this, "perfilSeleccionado");
			Clients.showNotification("Perfil creado..");
			this.newPerfil = new MyArray();
			break;

		case NUEVO_MODULO:
			this.setSelectedModulo(controlUsuario.agregarNuevoModulo(this.newModulo, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "modulos");
			BindUtils.postNotifyChange(null, null, this, "formulariosDeModulo");
			BindUtils.postNotifyChange(null, null, this, "");
			Clients.showNotification("Módulo Creado..");
			this.newModulo = new MyArray();
			break;

		case NUEVO_FORMULARIO:
			this.setSelectedFormuario(controlUsuario.agregarNuevoFormulario(newFormulario, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "formularios");
			BindUtils.postNotifyChange(null, null, this, "operacionesDeFormulario");
			BindUtils.postNotifyChange(null, null, this, "formularioSeleccionado");
			Clients.showNotification("Formulario Creado..");
			break;

		case NUEVO_OPERACION:
			this.setSelectedOperacion(controlUsuario.agregarNuevaOperacion(newOperacion, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "operaciones");
			BindUtils.postNotifyChange(null, null, this, "operacionSeleccionada");
			Clients.showNotification("Operación Creada..");
			break;

		case EDITAR_OPERACION:
			this.setSelectedOperacion(controlUsuario.guardarOperacionEditada(editItem, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "operaciones");
			Clients.showNotification("Operación Modificada..");
			break;

		case EDITAR_FORMULARIO:
			this.setSelectedFormuario(controlUsuario.guardarFormularioEditado(editItem, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "formularios");
			Clients.showNotification("Formulario Modificado..");
			break;

		case EDITAR_MODULO:
			this.setSelectedModulo(controlUsuario.guardarModuloEditado(editItem, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "modulos");
			Clients.showNotification("Modulo Modificado..");
			break;

		case EDITAR_PERFIL:
			this.setSelectedPerfil(controlUsuario.guardarPerfilEditado(editItem, getLoginNombre()));
			BindUtils.postNotifyChange(null, null, this, "perfiles");
			Clients.showNotification("Perfil Modificado..");
			break;
		}
	}

	/**
	 * Obtiene el item que se desea modificar
	 */
	public void getItemAModificar(int index) throws Exception {
		this.editItem = new MyArray();
		switch (index) {
		case EDITAR_OPERACION:
			this.editItem = controlUsuario.getOperacionParaEditar(this.selectedOperacion.getId());
			break;

		case EDITAR_FORMULARIO:
			this.editItem = controlUsuario.getFormularioParaEditar(this.selectedFormuario.getId());
			break;

		case EDITAR_MODULO:
			this.editItem = controlUsuario.getModuloParaEditar(this.selectedModulo.getId());
			break;

		case EDITAR_PERFIL:
			this.editItem = controlUsuario.getPerfilParaEditar(this.selectedPerfil.getId());
			break;

		}
	}

	/**
	 * Copia los perfiles de un usuario al usuario seleccionado
	 */
	private void copiarPerfilesDe_() throws Exception {
		BuscarElemento b = new BuscarElemento();
		b.setClase(Usuario.class);
		b.setAtributos(ATT_USUARIOS);
		b.setNombresColumnas(COLUMNAS_USUARIOS);
		b.setTitulo("Selecione Usuario para copiar sus perfiles..");
		b.setWidth("600px");
		b.addOrden("nombre");
		b.addWhere("id != " + this.selectedUsuario.getId());
		b.show("%");
		if (b.isClickAceptar()) {
			this.usuarioParaCopiarPerfiles = b.getSelectedItem();
			controlUsuario.copiarPerfilesDeUsuarioAUsuario(this.usuarioParaCopiarPerfiles.getId(),
					this.selectedUsuario.getId(), getLoginNombre());
			Clients.showNotification("Perfiles asignados..");
		}
	}

	/**
	 * Asigna un nuevo perfil al usuario seleccionado
	 */
	private void agregarNuevoPerfilAUsuario_() throws Exception {
		BuscarElemento b = new BuscarElemento();
		b.setClase(Perfil.class);
		b.setAtributos(ATT_PERFILES);
		b.setNombresColumnas(COLUMNAS_PERFILES);
		b.setTitulo("Buscar Perfil");
		b.setWidth("600px");
		b.addOrden("nombre");
		if (this.perfilesDeUsuario.size() > 0)
			b.addWhere(this.getWherePerfiles());
		b.show("%");
		if (b.isClickAceptar()) {
			this.selectedPerfilAdd = b.getSelectedItem();
			controlUsuario.addPerilAUsuario(this.selectedPerfilAdd.getId(), this.selectedUsuario.getId(),
					getLoginNombre());
			Clients.showNotification("Perfil asignado..");
		}
	}

	/**
	 * Genera where para obtener los perfiles que aun no han sido asignados al
	 * usuario selecionado
	 */
	private String getWherePerfiles() {
		String where = "";
		for (int i = 0; i < this.perfilesDeUsuario.size(); i++) {
			where += "id != " + this.perfilesDeUsuario.get(i).getId();
			if ((i + 1) < this.perfilesDeUsuario.size())
				where += " and ";
		}
		return where;
	}

	/**
	 * Asigna una nueva operacion al perfil seleccionado
	 * 
	 * @throws Exception
	 */
	private void agregarNuevaOperacionAPerfil_() throws Exception {
		BuscarElemento b = new BuscarElemento();
		b.setClase(Operacion.class);
		b.setAtributos(ATT_OPERACIONES);
		b.setNombresColumnas(COLUMNAS_OPERACIONES);
		b.setTitulo("Buscar Operación");
		b.setWidth("600px");
		b.addOrden("nombre");
		if (this.permisosDePerfil.size() > 0)
			b.addWhere(this.getWhereOperaciones());
		b.show("%");
		if (b.isClickAceptar()) {
			this.selectedOperacionAdd = b.getSelectedItem();
			controlUsuario.addOperacionAPerfil(this.selectedOperacionAdd.getId(), this.selectedPerfil.getId(),
					getLoginNombre());
			Clients.showNotification("Operación asignada..");
		}
	}

	/**
	 * genera where para obtener todas las operaciones que aun no han sido
	 * asignadas al perfil seleccionado
	 */
	private String getWhereOperaciones() {
		String where = "";
		for (int i = 0; i < this.permisosDePerfil.size(); i++) {
			where += "id != " + this.permisosDePerfil.get(i).getPos5().toString().trim();
			if ((i + 1) < this.permisosDePerfil.size())
				where += " and ";
		}
		return where;
	}

	/**
	 * Asigna un nuevo formulario al modulo seleccionado
	 */
	private void agregarNuevoFormularioAModulo_() throws Exception {
		BuscarElemento b = new BuscarElemento();
		b.setClase(Formulario.class);
		b.setAtributos(ATT_FORMULARIOS);
		b.setNombresColumnas(COLUMNAS_FORMULARIOS);
		b.setTitulo("Buscar Formulario");
		b.setWidth("600px");
		b.addOrden("label");
		if (this.formulariosDeModulo.size() > 0)
			b.addWhere(this.getWhereFormularios());
		b.show("%");
		if (b.isClickAceptar()) {
			this.selectedFormularioAdd = b.getSelectedItem();
			controlUsuario.addFormularioAModulo(this.selectedFormularioAdd.getId(), this.selectedModulo.getId(),
					getLoginNombre());
			Clients.showNotification("Formulario asignado..");
		}
	}

	/**
	 * genera where para obtener todas las operaciones que aun no han sido
	 * asignadas al perfil seleccionado
	 */
	private String getWhereFormularios() {
		String where = "";
		for (int i = 0; i < this.formulariosDeModulo.size(); i++) {
			where += "id != " + this.formulariosDeModulo.get(i).getId().toString().trim();
			if ((i + 1) < this.formulariosDeModulo.size())
				where += " and ";
		}
		return where;
	}

	/**
	 * Asigna una nueva operacion al formulario seleccionado
	 */
	private void agregarNuevaOperacionAFormulario_() throws Exception {
		BuscarElemento b = new BuscarElemento();
		b.setClase(Operacion.class);
		b.setAtributos(ATT_OPERACIONES);
		b.setNombresColumnas(COLUMNAS_OPERACIONES);
		b.setTitulo("Buscar Operación");
		b.setWidth("600px");
		b.addOrden("nombre");
		if (this.operacionesDeFormulario.size() > 0)
			b.addWhere(this.getWhereOperacionesForm());
		b.show("%");
		if (b.isClickAceptar()) {
			this.selectedOperacionFormAdd = b.getSelectedItem();
			controlUsuario.addOperacionAFormulario(this.selectedOperacionFormAdd.getId(),
					this.selectedFormuario.getId(), getLoginNombre());
			Clients.showNotification("Operación asignada..");
		}
	}

	/**
	 * genera where para obtener todas las operaciones que aun no han sido
	 * asignadas al perfil seleccionado
	 */
	private String getWhereOperacionesForm() {
		String where = "";
		for (int i = 0; i < this.operacionesDeFormulario.size(); i++) {
			where += "id != " + this.operacionesDeFormulario.get(i).getId().toString().trim();
			if ((i + 1) < this.operacionesDeFormulario.size())
				where += " and ";
		}
		return where;
	}

	/**
	 * Remueve un perfil de la lista de perfiles del usuario seleccionado
	 */
	private void removerPerfilDeUsuario_() throws Exception {
		controlUsuario.removePerfilUsuario(this.selectedPerfilRemove.getId(), this.selectedUsuario.getId(),
				getLoginNombre());
		Clients.showNotification("Perfil removido..");
	}

	/**
	 * Deshabilita el permiso del perfil asociada a la operacion seleccionada
	 */
	private void deshabilitarPermisoOperacionDePerfil_() throws Exception {
		if (this.selectedOperacionRemove.getPos4().toString().trim().equals("SI")) {
			if (this.mensajeSiNo("Deshabilitar el Permiso de la Operación en el Perfil?")) {
				controlUsuario.disabledPermisoOperacionPerfil(this.selectedOperacionRemove.getId(), getLoginNombre());
				Clients.showNotification("Permiso Deshabilitado..");
			}
		} else {
			Clients.showNotification("Ya se encuentra deshabilitado..");
		}
	}

	/**
	 * Habilita el permiso del perfil asociada a la operacion seleccionada
	 */
	private void habilitarPermisoOperacionDePerfil_() throws Exception {
		if (this.selectedOperacionRemove.getPos4().toString().trim().equals("NO")) {
			if (this.mensajeSiNo("Habilitar el Permiso de la Operación en el Perfil?")) {
				controlUsuario.enablePermisoOperacionPerfil(this.selectedOperacionRemove.getId(), getLoginNombre());
				Clients.showNotification("Permiso Habilitado..");
			}
		} else {
			Clients.showNotification("Ya se encuentra habilitado..");
		}
	}

	/**
	 * deshabilita un formulario de la lista de formularios del modulo
	 * seleccionado
	 */
	private void deshabilitarFormularioDelModulo_() throws Exception {
		if (this.selectedFormularioRemove.getPos4().toString().trim().equals("SI")) {
			if (this.mensajeSiNo("Deshabilitar el Formulario?")) {
				controlUsuario.disabledFormularioModulo(this.selectedFormularioRemove.getId(), getLoginNombre());
				Clients.showNotification("Formulario deshabilitado..");
			}
		} else {
			Clients.showNotification("Ya se encuetra deshabilitado..");
		}
	}

	/**
	 * habilita un formulario de la lista de formularios del modulo seleccionado
	 */
	private void habilitarFormularioDelModulo_() throws Exception {
		if (this.selectedFormularioRemove.getPos4().toString().trim().equals("NO")) {
			if (this.mensajeSiNo("Habilitar el Formulario?")) {
				controlUsuario.enabledFormularioModulo(this.selectedFormularioRemove.getId(), getLoginNombre());
				Clients.showNotification("Formulario habilitado..");
			}
		} else {
			Clients.showNotification("Ya se encuetra habilitado..");
		}
	}

	/**
	 * deshabilita una operacion de la lista de operaciones del formulario
	 * seleccionado
	 */
	private void deshabilitarOperacionDelFormulario_() throws Exception {
		if (this.selectedOperacionFormRemove.getPos3().toString().trim().equals("SI")) {
			if (this.mensajeSiNo("Deshabilitar la Operación?")) {
				controlUsuario.disabledOperacionFormulario(this.selectedOperacionFormRemove.getId(), getLoginNombre());
				Clients.showNotification("Operación deshabilitada..");
			}
		} else {
			Clients.showNotification("Ya se encuentra deshabilitada..");
		}
	}

	/**
	 * habilita una operacion de la lista de operaciones del formulario
	 * seleccionado
	 */
	private void habilitarOperacionDelFormulario_() throws Exception {
		if (this.selectedOperacionFormRemove.getPos3().toString().trim().equals("NO")) {
			if (this.mensajeSiNo("Habilitar la Operación?")) {
				controlUsuario.enabledOperacionFormulario(this.selectedOperacionFormRemove.getId(), getLoginNombre());
				Clients.showNotification("Operación habilitada..");
			}
		} else {
			Clients.showNotification("Ya se encuentra habilitada..");
		}
	}

	/**
	 * Inactiva o activa usuarios
	 * 
	 */
	public void activarUsuario_(boolean index) throws Exception {
		if (index) {
			if (this.mensajeSiNo("Está seguro de que desea activar el Usuario?")) {
				controlUsuario.activarInactivarUsuario(this.selectedUsuario.getId(), index, this.getLoginNombre(), "");
				Clients.showNotification("Usuario activado..");
				this.selectedUsuario.setPos3("SI");
			}
		} else {
			this.motivoDeInactivacion = "";
			if (this.mensajeSiNo("Está seguro de que desea inactivar el Usuario?")) {
				this.popupMotivoInactivacion.open(popupMotivoInactivacion, "at_pointer");
				this.popupMotivoInactivacion.setFocus(true);
			}
		}
	}

	/**
	 * Resetear password de usuario
	 * 
	 * @throws Exception
	 */

	public void resetPass_() throws Exception {
		if (this.mensajeSiNo("Resetear clave?")) {
			controlUsuario.resetPass(this.selectedUsuario.getId(), this.getLoginNombre());
			Clients.showNotification(
					"Clave reseteada...\n Nueva clave de " + this.selectedUsuario.getPos1() + " es 123");
		}
	}

	/***************** GET y SET *****************/

	/**
	 * Obtiene la lista de usuarios segun el filtro
	 */
	@DependsOn({ "filterLoginUsuario", "filterNombreUsuario" })
	public List<MyArray> getUsuarios() throws Exception {
		this.usuarios = controlUsuario.getUsuarios(filterLoginUsuario, filterNombreUsuario);
		return this.usuarios;
	}

	/**
	 * obtiene la lista de perfiles segun el filtro
	 */
	@DependsOn({ "filterNombrePerfil", "filterDescripcionPerfil", "filterGrupoPerfil" })
	public List<MyArray> getPerfiles() throws Exception {
		this.perfiles = controlUsuario.getPerfiles(filterNombrePerfil, filterDescripcionPerfil);
		return this.perfiles;
	}

	/**
	 * Obtiene la lista de modulos segun el filtro
	 */
	@DependsOn({ "filterNombreModulo", "filterDescripcionModulo" })
	public List<MyArray> getModulos() throws Exception {
		this.modulos = controlUsuario.getModulos(filterNombreModulo, filterDescripcionModulo);
		return this.modulos;
	}

	/**
	 * Obtiene la lista de formularios segun el filtro
	 */
	@DependsOn({ "filterLabelFormulario", "filterAliasFormulario" })
	public List<MyArray> getFormularios() throws Exception {
		this.formularios = controlUsuario.getFormularios(filterLabelFormulario, filterAliasFormulario);
		return formularios;
	}

	/**
	 * Obtiene la lista de operaciones segun el filtro
	 */
	@DependsOn({ "filterAliasOperacion", "filterNombreOperacion", "filterDescripcionOperacion" })
	public List<MyArray> getOperaciones() throws Exception {
		this.operaciones = controlUsuario.getOperaciones(filterAliasOperacion, filterNombreOperacion,
				filterDescripcionOperacion);
		return operaciones;
	}

	/**
	 * Obtiene todos los perfiles del usuario seleccionado
	 */
	@DependsOn("selectedUsuario")
	public List<MyArray> getPerfilesDeUsuario() throws Exception {
		selectedPerfilAdd = new MyArray(); // limpia el selectedPerfilAdd
		selectedPerfilRemove = new MyArray();
		if (selectedUsuario.esNuevo() == false) {
			perfilesDeUsuario = controlUsuario.getPerfilesUsuario(selectedUsuario.getId());
		} else {
			perfilesDeUsuario = new ArrayList<MyArray>();
		}
		return perfilesDeUsuario;
	}

	/**
	 * Obtiene todos los permisos del perfil seleccionado
	 */
	@DependsOn("selectedPerfil")
	public List<MyArray> getPermisosDePerfil() throws Exception {
		selectedOperacionAdd = new MyArray(); // limpia el selectedOperacionAdd
		selectedOperacionRemove = new MyArray();
		if (selectedPerfil.esNuevo() == false) {
			permisosDePerfil = controlUsuario.getPermisoOperacionesDePerfil(this.selectedPerfil.getId());
		} else {
			permisosDePerfil = new ArrayList<MyArray>();
		}
		return permisosDePerfil;
	}

	/**
	 * Obtiene todos los formularios del modulo seleccionado
	 */
	@DependsOn("selectedModulo")
	public List<MyArray> getFormulariosDeModulo() throws Exception {
		this.selectedFormularioAdd = new MyArray();
		this.selectedFormularioRemove = new MyArray();
		if (selectedModulo.esNuevo() == false) {
			this.formulariosDeModulo = controlUsuario.getFormulariosDeModulo(this.selectedModulo.getId());
		} else {
			this.formulariosDeModulo = new ArrayList<MyArray>();
		}
		return this.formulariosDeModulo;
	}

	/**
	 * Obtiene todas la operaciones del formularios seleccionado
	 */
	@DependsOn("selectedFormuario")
	public List<MyArray> getOperacionesDeFormulario() throws Exception {
		this.selectedOperacionFormAdd = new MyArray();
		this.selectedOperacionFormRemove = new MyArray();
		if (selectedFormuario.esNuevo() == false) {
			this.operacionesDeFormulario = controlUsuario.getOperacionesDeFormulario(this.selectedFormuario.getId());
		} else {
			this.operacionesDeFormulario = new ArrayList<MyArray>();
		}
		return operacionesDeFormulario;
	}

	@DependsOn("selectedUsuario")
	public boolean isUsuarioSeleccionado() {
		this.setSelectedPerfilRemove(new MyArray());
		if (selectedUsuario.esNuevo())
			usuarioSeleccionado = false;
		else
			usuarioSeleccionado = true;
		return usuarioSeleccionado;
	}

	@DependsOn("selectedPerfil")
	public boolean isPerfilSeleccionado() {
		this.setSelectedOperacionRemove(new MyArray());
		if (selectedPerfil.esNuevo())
			perfilSeleccionado = false;
		else
			perfilSeleccionado = true;
		return perfilSeleccionado;
	}

	@DependsOn("selectedModulo")
	public boolean isModuloSeleccionado() {
		this.setSelectedFormularioRemove(new MyArray());
		if (selectedModulo.esNuevo())
			moduloSeleccionado = false;
		else
			moduloSeleccionado = true;
		return moduloSeleccionado;
	}

	@DependsOn("selectedFormuario")
	public boolean isFormularioSeleccionado() {
		this.setSelectedOperacionFormRemove(new MyArray());
		if (selectedFormuario.esNuevo())
			formularioSeleccionado = false;
		else
			formularioSeleccionado = true;
		return formularioSeleccionado;
	}

	@DependsOn("selectedOperacion")
	public boolean isOperacionSeleccionada() {
		if (selectedOperacion.esNuevo())
			operacionSeleccionada = false;
		else
			operacionSeleccionada = true;
		return operacionSeleccionada;
	}

	@DependsOn({ "selectedPerfilRemove", "selectedUsuario" })
	public boolean isPerfilParaRemoverSeleccionado() {
		if (selectedPerfilRemove.esNuevo())
			perfilParaRemoverSeleccionado = false;
		else
			perfilParaRemoverSeleccionado = true;
		return perfilParaRemoverSeleccionado;
	}

	@DependsOn({ "selectedOperacionRemove", "selectedPerfil" })
	public boolean isOperacionParaRemoverSeleccionada() {
		if (selectedOperacionRemove.esNuevo())
			operacionParaRemoverSeleccionada = false;
		else
			operacionParaRemoverSeleccionada = true;
		return operacionParaRemoverSeleccionada;
	}

	@DependsOn({ "selectedFormularioRemove", "selectedModulo" })
	public boolean isFormularioParaRemoverSeleccionado() {
		if (selectedFormularioRemove.esNuevo())
			formularioParaRemoverSeleccionado = false;
		else
			formularioParaRemoverSeleccionado = true;
		return formularioParaRemoverSeleccionado;
	}

	@DependsOn({ "selectedOperacionFormRemove", "selectedFormuario" })
	public boolean isOperacionFormParaRemoverSeleccionada() {
		if (selectedOperacionFormRemove.esNuevo())
			operacionFormParaRemoverSeleccionada = false;
		else
			operacionFormParaRemoverSeleccionada = true;
		return operacionFormParaRemoverSeleccionada;
	}

	public void setPerfilesDeUsuario(List<MyArray> perfiles) {
		this.perfilesDeUsuario = perfiles;
	}

	public MyArray getSelectedPerfilAdd() {
		return selectedPerfilAdd;
	}

	public void setSelectedPerfilAdd(MyArray selectedPerfilAdd) {
		this.selectedPerfilAdd = selectedPerfilAdd;
	}

	public MyArray getSelectedPerfilRemove() {
		return selectedPerfilRemove;
	}

	public void setSelectedPerfilRemove(MyArray selectedPerfilRemove) {
		this.selectedPerfilRemove = selectedPerfilRemove;
	}

	public MyArray getSelectedUsuario() {
		return selectedUsuario;
	}

	public void setSelectedUsuario(MyArray selectedUsuario) {
		this.selectedUsuario = selectedUsuario;
	}

	public void setUsuarioSeleccionado(boolean usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	public void setPerfilParaRemoverSeleccionado(boolean perfilParaRemoverSeleccionado) {
		this.perfilParaRemoverSeleccionado = perfilParaRemoverSeleccionado;
	}

	public MyArray getSelectedPerfil() {
		return selectedPerfil;
	}

	public void setSelectedPerfil(MyArray selectedPerfil) {
		this.selectedPerfil = selectedPerfil;
	}

	public String getFilterLoginUsuario() {
		return filterLoginUsuario;
	}

	public void setFilterLoginUsuario(String filterLoginUsuario) {
		this.filterLoginUsuario = filterLoginUsuario;
	}

	public String getFilterNombreUsuario() {
		return filterNombreUsuario;
	}

	public void setFilterNombreUsuario(String filterNombreUsuario) {
		this.filterNombreUsuario = filterNombreUsuario;
	}

	public String getFilterNombrePerfil() {
		return filterNombrePerfil;
	}

	public void setFilterNombrePerfil(String filterNombrePerfil) {
		this.filterNombrePerfil = filterNombrePerfil;
	}

	public String getFilterDescripcionPerfil() {
		return filterDescripcionPerfil;
	}

	public void setFilterDescripcionPerfil(String filterDescripcionPerfil) {
		this.filterDescripcionPerfil = filterDescripcionPerfil;
	}

	public String getFilterGrupoPerfil() {
		return filterGrupoPerfil;
	}

	public void setFilterGrupoPerfil(String filterGrupoPerfil) {
		this.filterGrupoPerfil = filterGrupoPerfil;
	}

	public void setPermisosDePerfil(List<MyArray> permisosDePerfil) {
		this.permisosDePerfil = permisosDePerfil;
	}

	public MyArray getSelectedOperacionRemove() {
		return selectedOperacionRemove;
	}

	public void setSelectedOperacionRemove(MyArray selectedOperacionRemove) {
		this.selectedOperacionRemove = selectedOperacionRemove;
	}

	public MyArray getSelectedOperacionAdd() {
		return selectedOperacionAdd;
	}

	public void setSelectedOperacionAdd(MyArray selectedOperacionAdd) {
		this.selectedOperacionAdd = selectedOperacionAdd;
	}

	public void setUsuarios(List<MyArray> usuarios) {
		this.usuarios = usuarios;
	}

	public MyArray getNewUsuario() {
		return newUsuario;
	}

	public void setNewUsuario(MyArray newUsuario) {
		this.newUsuario = newUsuario;
	}

	public void setPerfilSeleccionado(boolean perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public void setOperacionParaRemoverSeleccionada(boolean operacionParaRemoverSeleccionada) {
		this.operacionParaRemoverSeleccionada = operacionParaRemoverSeleccionada;
	}

	public void setPerfiles(List<MyArray> perfiles) {
		this.perfiles = perfiles;
	}

	public boolean isVisibleUsuario() {
		return visibleUsuario;
	}

	public void setVisibleUsuario(boolean visibleUsuario) {
		this.visibleUsuario = visibleUsuario;
	}

	public boolean isVisiblePerfil() {
		return visiblePerfil;
	}

	public void setVisiblePerfil(boolean visiblePerfil) {
		this.visiblePerfil = visiblePerfil;
	}

	public boolean isVisibleOperacion() {
		return visibleOperacion;
	}

	public void setVisibleOperacion(boolean visibleOperacion) {
		this.visibleOperacion = visibleOperacion;
	}

	public MyArray getNewPerfil() {
		return newPerfil;
	}

	public void setNewPerfil(MyArray newPerfil) {
		this.newPerfil = newPerfil;
	}

	public MyArray getNewModulo() {
		return newModulo;
	}

	public void setNewModulo(MyArray newModulo) {
		this.newModulo = newModulo;
	}

	public MyArray getSelectedModulo() {
		return selectedModulo;
	}

	public void setSelectedModulo(MyArray selectedModulo) {
		this.selectedModulo = selectedModulo;
	}

	public MyArray getSelectedFormularioAdd() {
		return selectedFormularioAdd;
	}

	public void setSelectedFormularioAdd(MyArray selectedFormularioAdd) {
		this.selectedFormularioAdd = selectedFormularioAdd;
	}

	public MyArray getSelectedFormularioRemove() {
		return selectedFormularioRemove;
	}

	public void setSelectedFormularioRemove(MyArray selectedFormularioRemove) {
		this.selectedFormularioRemove = selectedFormularioRemove;
	}

	public void setModulos(List<MyArray> modulos) {
		this.modulos = modulos;
	}

	public void setFormulariosDeModulo(List<MyArray> formulariosDeModulo) {
		this.formulariosDeModulo = formulariosDeModulo;
	}

	public String getFilterNombreModulo() {
		return filterNombreModulo;
	}

	public void setFilterNombreModulo(String filterNombreModulo) {
		this.filterNombreModulo = filterNombreModulo;
	}

	public String getFilterDescripcionModulo() {
		return filterDescripcionModulo;
	}

	public void setFilterDescripcionModulo(String filterDescripcionModulo) {
		this.filterDescripcionModulo = filterDescripcionModulo;
	}

	public void setModuloSeleccionado(boolean moduloSeleccionado) {
		this.moduloSeleccionado = moduloSeleccionado;
	}

	public boolean isVisibleModulo() {
		return visibleModulo;
	}

	public void setVisibleModulo(boolean visibleModulo) {
		this.visibleModulo = visibleModulo;
	}

	public boolean isVisibleFormulario() {
		return visibleFormulario;
	}

	public void setVisibleFormulario(boolean visibleFormulario) {
		this.visibleFormulario = visibleFormulario;
	}

	public void setFormularioParaRemoverSeleccionado(boolean formularioParaRemoverSeleccionado) {
		this.formularioParaRemoverSeleccionado = formularioParaRemoverSeleccionado;
	}

	public MyArray getNewFormulario() {
		return newFormulario;
	}

	public void setNewFormulario(MyArray newFormulario) {
		this.newFormulario = newFormulario;
	}

	public MyArray getSelectedFormuario() {
		return selectedFormuario;
	}

	public void setSelectedFormuario(MyArray selectedFormuario) {
		this.selectedFormuario = selectedFormuario;
	}

	public MyArray getSelectedOperacionFormAdd() {
		return selectedOperacionFormAdd;
	}

	public void setSelectedOperacionFormAdd(MyArray selectedOperacionFormAdd) {
		this.selectedOperacionFormAdd = selectedOperacionFormAdd;
	}

	public MyArray getSelectedOperacionFormRemove() {
		return selectedOperacionFormRemove;
	}

	public void setSelectedOperacionFormRemove(MyArray selectedOperacionFormRemove) {
		this.selectedOperacionFormRemove = selectedOperacionFormRemove;
	}

	public void setFormularios(List<MyArray> formularios) {
		this.formularios = formularios;
	}

	public void setOperacionesDeFormulario(List<MyArray> operacionesDeFormulario) {
		this.operacionesDeFormulario = operacionesDeFormulario;
	}

	public String getFilterLabelFormulario() {
		return filterLabelFormulario;
	}

	public void setFilterLabelFormulario(String filterLabelFormulario) {
		this.filterLabelFormulario = filterLabelFormulario;
	}

	public String getFilterAliasFormulario() {
		return filterAliasFormulario;
	}

	public void setFilterAliasFormulario(String filterAliasFormulario) {
		this.filterAliasFormulario = filterAliasFormulario;
	}

	public void setFormularioSeleccionado(boolean formularioSeleccionado) {
		this.formularioSeleccionado = formularioSeleccionado;
	}

	public void setOperacionFormParaRemoverSeleccionada(boolean operacionFormParaRemoverSeleccionada) {
		this.operacionFormParaRemoverSeleccionada = operacionFormParaRemoverSeleccionada;
	}

	public MyArray getNewOperacion() {
		return newOperacion;
	}

	public void setNewOperacion(MyArray newOperacion) {
		this.newOperacion = newOperacion;
	}

	public MyArray getSelectedOperacion() {
		return selectedOperacion;
	}

	public void setSelectedOperacion(MyArray selectedOperacion) {
		this.selectedOperacion = selectedOperacion;
	}

	public void setOperaciones(List<MyArray> operaciones) {
		this.operaciones = operaciones;
	}

	public String getFilterAliasOperacion() {
		return filterAliasOperacion;
	}

	public void setFilterAliasOperacion(String filterAliasOperacion) {
		this.filterAliasOperacion = filterAliasOperacion;
	}

	public String getFilterNombreOperacion() {
		return filterNombreOperacion;
	}

	public void setFilterNombreOperacion(String filterNombreOperacion) {
		this.filterNombreOperacion = filterNombreOperacion;
	}

	public String getFilterDescripcionOperacion() {
		return filterDescripcionOperacion;
	}

	public void setFilterDescripcionOperacion(String filterDescripcionOperacion) {
		this.filterDescripcionOperacion = filterDescripcionOperacion;
	}

	public void setOperacionSeleccionada(boolean operacionSeleccionada) {
		this.operacionSeleccionada = operacionSeleccionada;
	}

	public boolean isVisibleEditarOperacion() {
		return visibleEditarOperacion;
	}

	public void setVisibleEditarOperacion(boolean visibleEditarOperacion) {
		this.visibleEditarOperacion = visibleEditarOperacion;
	}

	public boolean isVisibleEditarFormulario() {
		return visibleEditarFormulario;
	}

	public void setVisibleEditarFormulario(boolean visibleEditarFormulario) {
		this.visibleEditarFormulario = visibleEditarFormulario;
	}

	public boolean isVisibleEditarModulo() {
		return visibleEditarModulo;
	}

	public void setVisibleEditarModulo(boolean visibleEditarModulo) {
		this.visibleEditarModulo = visibleEditarModulo;
	}

	public MyArray getEditItem() {
		return editItem;
	}

	public void setEditItem(MyArray editItem) {
		this.editItem = editItem;
	}

	public boolean isVisibleTabsDesarrollador() {
		return visibleTabsDesarrollador;
	}

	public void setVisibleTabsDesarrollador(boolean visibleTabsDesarrollador) {
		this.visibleTabsDesarrollador = visibleTabsDesarrollador;
	}

	public MyArray getUsuarioParaCopiarPerfiles() {
		return usuarioParaCopiarPerfiles;
	}

	public void setUsuarioParaCopiarPerfiles(MyArray usuarioParaCopiarPerfiles) {
		this.usuarioParaCopiarPerfiles = usuarioParaCopiarPerfiles;
	}

	public boolean isVisibleEditarPerfil() {
		return visibleEditarPerfil;
	}

	public void setVisibleEditarPerfil(boolean visibleEditarPerfil) {
		this.visibleEditarPerfil = visibleEditarPerfil;
	}

	public String getMotivoDeInactivacion() {
		return motivoDeInactivacion;
	}

	public void setMotivoDeInactivacion(String motivoDeInactivacion) {
		this.motivoDeInactivacion = motivoDeInactivacion;
	}

}

/**
 * Validador Usuario
 *
 */
class ValidadorUsuario implements VerificaAceptarCancelar {

	ControlUsuario controlUsuario = new ControlUsuario();
	private String mensaje = "";
	private MyArray item;

	public ValidadorUsuario(MyArray item) {
		this.item = item;
	}

	@Override
	public boolean verificarAceptar() {
		boolean out = true;
		this.mensaje = "No se puede completar la operación debido a: \n";

		if (this.item.getPos1().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un nombre \n";
		}
		if (this.item.getPos2().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un login \n";
		}
		try {
			if (controlUsuario.getExisteLogin(this.item.getPos2().toString()) == true) {
				out = false;
				this.mensaje += "- Este login ya existe \n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.item.getPos3().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar una clave \n";
		}
		if (!(this.item.getPos3().toString().equals(this.item.getPos4().toString()))) {
			out = false;
			this.mensaje += "- Las claves no coinciden \n";
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
		return "Error al cancelar";
	}
}

/**
 * Validador de Perfil
 *
 */
class ValidadorPerfil implements VerificaAceptarCancelar {

	private String mensaje = "";
	private MyArray item;

	public ValidadorPerfil(MyArray item) {
		this.item = item;
	}

	@Override
	public boolean verificarAceptar() {
		boolean out = true;
		this.mensaje = "No se puede completar la operación debido a: \n";

		if (this.item.getPos1().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un nombre \n";
		}
		if (this.item.getPos2().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar una descripción \n";
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
		return "Error al cancelar";
	}
}

/**
 * Validador de Modulo
 *
 */
class ValidadorModulo implements VerificaAceptarCancelar {

	private String mensaje = "";
	private MyArray item;

	public ValidadorModulo(MyArray item) {
		this.item = item;
	}

	@Override
	public boolean verificarAceptar() {
		boolean out = true;
		this.mensaje = "No se puede completar la operación debido a: \n";

		if (this.item.getPos1().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un nombre \n";
		}
		if (this.item.getPos2().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar una descripción \n";
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
		return "Error al cancelar";
	}
}

/**
 * Validador de formulario
 *
 */
class ValidadorFormulario implements VerificaAceptarCancelar {

	private String mensaje = "";
	private MyArray item;

	public ValidadorFormulario(MyArray item) {
		this.item = item;
	}

	@Override
	public boolean verificarAceptar() {
		boolean out = true;
		this.mensaje = "No se puede completar la operación debido a: \n";

		if (this.item.getPos1().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un label \n";
		}
		if (this.item.getPos2().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar una descripción \n";
		}
		if (this.item.getPos3().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar una url \n";
		}
		if (this.item.getPos4().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un alias \n";
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
		return "Error al cancelar";
	}
}

/**
 * Validador de Operacion
 *
 */
class ValidadorOperacion implements VerificaAceptarCancelar {

	private String mensaje = "";
	private MyArray item;

	public ValidadorOperacion(MyArray item) {
		this.item = item;
	}

	@Override
	public boolean verificarAceptar() {
		boolean out = true;
		this.mensaje = "No se puede completar la operación debido a: \n";

		if (this.item.getPos1().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un alias \n";
		}
		if (this.item.getPos2().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un nombre \n";
		}
		if (this.item.getPos3().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar una descripción \n";
		}
		if (this.item.getPos4().toString().trim().length() == 0) {
			out = false;
			this.mensaje += "- Debe ingresar un idTexto \n";
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
		return "Error al cancelar";
	}
}