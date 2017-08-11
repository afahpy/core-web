package com.coreweb.extras.usuarioPerfil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coreweb.domain.Formulario;
import com.coreweb.domain.Modulo;
import com.coreweb.domain.Operacion;
import com.coreweb.domain.Perfil;
import com.coreweb.domain.Permiso;
import com.coreweb.domain.Register;
import com.coreweb.domain.Tipo;
import com.coreweb.domain.TipoTipo;
import com.coreweb.domain.Usuario;
import com.coreweb.domain.UsuarioPropiedad;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;

public class ControlUsuario {

	public void copiarPerfilesDeUsuarioAUsuario(long idUsuarioPerfilesCopy, long idUsuarioSelected, String login)
			throws Exception {
		Register rr = Register.getInstance();
		Usuario usuarioPerfilesCopy = (Usuario) rr.getObject(Usuario.class.getName(), idUsuarioPerfilesCopy);
		Usuario usuarioSelected = (Usuario) rr.getObject(Usuario.class.getName(), idUsuarioSelected);
		for (Perfil perfil : usuarioPerfilesCopy.getPerfiles()) {
			usuarioSelected.getPerfiles().add(perfil);
		}
		rr.saveObject(usuarioSelected, login);
	}

	/**
	 * Obtiene lista de usuarios según filtro
	 */
	public List<MyArray> getUsuarios(String login, String nombre) throws Exception {
		Register rr = Register.getInstance();
		MyArray usuario = new MyArray();
		List<Usuario> usuarios = new ArrayList<Usuario>();
		List<MyArray> usuariosMyArray = new ArrayList<MyArray>();
		usuarios = rr.getUsuarios(login, nombre);

		for (Usuario usu : usuarios) {
			usuario = new MyArray();
			usuario.setId(usu.getId());
			usuario.setPos1(usu.getLogin());
			usuario.setPos2(usu.getNombre());
			usuario.setPos3(usu.isActivo() ? "SI" : "NO");
			usuariosMyArray.add(usuario);
		}
		return usuariosMyArray;
	}

	/**
	 * Obtiene lista de perfiles según filtro
	 */
	public List<MyArray> getPerfiles(String nombre, String descripcion) throws Exception {
		Register rr = Register.getInstance();
		MyArray perfil = new MyArray();
		List<Perfil> perfiles = new ArrayList<Perfil>();
		List<MyArray> perfilesMyArray = new ArrayList<MyArray>();
		perfiles = rr.getPerfiles(nombre, descripcion);

		for (Perfil per : perfiles) {
			perfil = new MyArray();
			perfil.setId(per.getId());
			perfil.setPos1(per.getNombre());
			perfil.setPos2(per.getDescripcion());
			perfil.setPos3(per.getGrupo());
			perfilesMyArray.add(perfil);
		}
		return perfilesMyArray;
	}

	/**
	 * Obtiene lista de modulos según filtro
	 */
	public List<MyArray> getModulos(String nombre, String descripcion) throws Exception {
		Register rr = Register.getInstance();
		MyArray modulo = new MyArray();
		List<Modulo> modulos = new ArrayList<Modulo>();
		List<MyArray> modulosMyArray = new ArrayList<MyArray>();
		modulos = rr.getModulos(nombre, descripcion);

		for (Modulo mod : modulos) {
			modulo = new MyArray();
			modulo.setId(mod.getId());
			modulo.setPos1(mod.getNombre());
			modulo.setPos2(mod.getDescripcion());
			modulosMyArray.add(modulo);
		}
		return modulosMyArray;
	}

	/**
	 * Obtiene lista de formularios según filtro
	 */
	public List<MyArray> getFormularios(String label, String alias) throws Exception {
		Register rr = Register.getInstance();
		MyArray formulario = new MyArray();
		List<Formulario> formularios = new ArrayList<Formulario>();
		List<MyArray> formulariosMyArray = new ArrayList<MyArray>();
		formularios = rr.getFormularios(label, alias);

		for (Formulario form : formularios) {
			formulario = new MyArray();
			formulario.setId(form.getId());
			formulario.setPos1(form.getLabel());
			formulario.setPos2(form.getAlias());
			formulario.setPos3(form.isHabilitado() ? "SI" : "NO");
			formulariosMyArray.add(formulario);
		}
		return formulariosMyArray;
	}

	/**
	 * Obtiene lista de operaciones según filtro
	 */
	public List<MyArray> getOperaciones(String alias, String nombre, String descripcion) throws Exception {
		Register rr = Register.getInstance();
		MyArray operacion = new MyArray();
		List<Operacion> operaciones = new ArrayList<Operacion>();
		List<MyArray> operacionesMyArray = new ArrayList<MyArray>();
		operaciones = rr.getOperaciones(alias, nombre, descripcion);

		for (Operacion op : operaciones) {
			operacion = new MyArray();
			operacion.setId(op.getId());
			operacion.setPos1(op.getAlias());
			operacion.setPos2(op.getNombre());
			operacion.setPos3(op.getDescripcion());
			operacion.setPos4(op.isHabilitado() ? "SI" : "NO");
			operacionesMyArray.add(operacion);
		}
		return operacionesMyArray;
	}

	/**
	 * Obtiene todos los perfiles del usuario especificado
	 */
	public List<MyArray> getPerfilesUsuario(long idUsuario) throws Exception {
		Register rr = Register.getInstance();
		Usuario usuario = rr.getUsuario(idUsuario);
		MyArray perfil = new MyArray();
		List<MyArray> perfiles = new ArrayList<MyArray>();
		for (Perfil per : usuario.getPerfiles()) {
			perfil = new MyArray();
			perfil.setId(per.getId());
			perfil.setPos1(per.getNombre());
			perfil.setPos2(per.getDescripcion());
			perfil.setPos3(per.getGrupo());
			perfiles.add(perfil);
		}
		Collections.sort(perfiles, new MyArrayCompare());
		return perfiles;
	}

	/**
	 * Obtiene todos los permisos del perfil especificado
	 */
	public List<MyArray> getPermisoOperacionesDePerfil(long idPerfil) throws Exception {
		Register rr = Register.getInstance();
		Perfil perfil = rr.getPerfil(idPerfil);

		MyArray permisoOperacion = new MyArray();
		List<MyArray> permisoOperacionesDePerfil = new ArrayList<MyArray>();
		for (Permiso per : perfil.getPermisos()) {
			permisoOperacion = new MyArray();
			permisoOperacion.setId(per.getId());
			permisoOperacion.setPos1(per.getOperacion().getNombre());
			permisoOperacion.setPos2(per.getOperacion().getDescripcion());
			permisoOperacion.setPos3(per.getOperacion().isHabilitado() ? "SI" : "NO"); // operación
																						// habilitada?
			permisoOperacion.setPos4(per.isHabilitado() ? "SI" : "NO"); // tiene
																		// permiso?
			permisoOperacion.setPos5(per.getOperacion().getId());// id de la
																	// operacion
			permisoOperacionesDePerfil.add(permisoOperacion);
		}
		Collections.sort(permisoOperacionesDePerfil, new MyArrayCompare());
		return permisoOperacionesDePerfil;
	}

	/**
	 * Obtiene todos los formularios del modulo especificado
	 */
	public List<MyArray> getFormulariosDeModulo(long idModulo) throws Exception {
		Register rr = Register.getInstance();
		Modulo modulo = rr.getModulo(idModulo);

		MyArray formulario = new MyArray();
		List<MyArray> formulariosDeModulo = new ArrayList<MyArray>();
		for (Formulario form : modulo.getFormularios()) {
			formulario = new MyArray();
			formulario.setId(form.getId());
			formulario.setPos1(form.getLabel());
			formulario.setPos2(form.getAlias());
			formulario.setPos3(form.getDescripcion());
			formulario.setPos4(form.isHabilitado() ? "SI" : "NO"); // tiene
																	// permiso?
			formulariosDeModulo.add(formulario);
		}
		Collections.sort(formulariosDeModulo, new MyArrayCompare());
		return formulariosDeModulo;
	}

	/**
	 * Obtiene todos las operaciones del formulario especificado
	 */
	public List<MyArray> getOperacionesDeFormulario(long idFormulario) throws Exception {
		Register rr = Register.getInstance();
		Formulario formulario = rr.getFormulario(idFormulario);

		MyArray operacion = new MyArray();
		List<MyArray> operacionesDeModulo = new ArrayList<MyArray>();
		for (Operacion op : formulario.getOperaciones()) {
			operacion = new MyArray();
			operacion.setId(op.getId());
			operacion.setPos1(op.getAlias());
			operacion.setPos2(op.getNombre());
			operacion.setPos3(op.isHabilitado() ? "SI" : "NO");
			operacionesDeModulo.add(operacion);
		}
		Collections.sort(operacionesDeModulo, new MyArrayCompare());
		return operacionesDeModulo;
	}

	/**
	 * Persiste un Nuevo usuario
	 */
	public MyArray agregarNuevoUsuario(MyArray usuario, String login) throws Exception {
		Register rr = Register.getInstance();
		Usuario usuarioNuevo = new Usuario();
		usuarioNuevo.setNombre(usuario.getPos1().toString());
		usuarioNuevo.setLogin(usuario.getPos2().toString().trim());
		usuarioNuevo.setClave(usuario.getPos4().toString().trim());
		usuarioNuevo.setFechaDeIngreso(new Date());
		usuarioNuevo.setPerfiles(new HashSet<Perfil>());
		usuarioNuevo.setUsuarioPropiedades(new HashSet<UsuarioPropiedad>());
		rr.saveObject(usuarioNuevo, login);
		usuario.setId(usuarioNuevo.getId());
		return usuario;
	}

	/**
	 * Verifica si ya existe ese login
	 */
	public boolean getExisteLogin(String login) throws Exception {
		Register rr = Register.getInstance();
		return rr.getExisteLogin(login);
	}

	/**
	 * Persiste un nuevo Perfil
	 */
	public MyArray agregarNuevoPerfil(MyArray perfil, String login) throws Exception {
		Register rr = Register.getInstance();
		Perfil perfilNuevo = new Perfil();
		perfilNuevo.setNombre(perfil.getPos1().toString());
		perfilNuevo.setDescripcion(perfil.getPos2().toString());
		if (perfil.getPos3().toString().trim().isEmpty() == false)
			perfilNuevo.setGrupo(perfil.getPos3().toString());
		rr.saveObject(perfilNuevo, login);
		perfil.setId(perfilNuevo.getId());
		return perfil;
	}

	/**
	 * Persiste un nuevo Modulo
	 */
	public MyArray agregarNuevoModulo(MyArray modulo, String login) throws Exception {
		Register rr = Register.getInstance();
		Modulo moduloNuevo = new Modulo();
		moduloNuevo.setNombre(modulo.getPos1().toString());
		moduloNuevo.setDescripcion(modulo.getPos2().toString());
		rr.saveObject(moduloNuevo, login);
		modulo.setId(moduloNuevo.getId());
		return modulo;
	}

	/**
	 * Persiste un nuevo Formulario
	 */
	public MyArray agregarNuevoFormulario(MyArray formulario, String login) throws Exception {
		Register rr = Register.getInstance();
		Formulario formularioNuevo = new Formulario();
		formularioNuevo.setLabel(formulario.getPos1().toString());
		formularioNuevo.setDescripcion(formulario.getPos2().toString());
		formularioNuevo.setUrl(formulario.getPos3().toString());
		formularioNuevo.setAlias(formulario.getPos4().toString());
		formularioNuevo.setHabilitado(true);
		rr.saveObject(formularioNuevo, login);
		formulario = new MyArray();
		formulario.setId(formularioNuevo.getId());
		formulario.setPos1(formularioNuevo.getLabel());
		formulario.setPos2(formularioNuevo.getAlias());
		formulario.setPos3(formularioNuevo.isHabilitado() ? "SI" : "NO");
		return formulario;
	}

	/**
	 * Persiste una nuevo Operacion
	 */
	public MyArray agregarNuevaOperacion(MyArray operacion, String login) throws Exception {
		Register rr = Register.getInstance();
		Operacion operacionNueva = new Operacion();
		operacionNueva.setAlias(operacion.getPos1().toString());
		operacionNueva.setNombre(operacion.getPos2().toString());
		operacionNueva.setDescripcion(operacion.getPos3().toString());
		operacionNueva.setHabilitado(true);
		operacionNueva.setIdTexto(operacion.getPos4().toString());
		rr.saveObject(operacionNueva, login);
		operacion = new MyArray();
		operacion.setId(operacionNueva.getId());
		operacion.setPos1(operacionNueva.getAlias());
		operacion.setPos2(operacionNueva.getNombre());
		operacion.setPos3(operacionNueva.getDescripcion());
		operacion.setPos4(operacionNueva.isHabilitado() ? "SI" : "NO");
		return operacion;
	}

	/**
	 * Agrega el perfil seleccionado a la lista de perfiles del usuario
	 * seleccionado
	 */
	public void addPerilAUsuario(long idPerfil, long idUsuario, String login) throws Exception {
		Register rr = Register.getInstance();
		Usuario usuario = rr.getUsuario(idUsuario);
		Perfil perfil = rr.getPerfil(idPerfil);
		usuario.getPerfiles().add(perfil);
		rr.saveObject(usuario, login);
	}

	/**
	 * Agrega la operacion seleccionada a la lista de permisos del perfil
	 * seleccionado
	 */
	public void addOperacionAPerfil(long idOperacion, long idPerfil, String login) throws Exception {
		Register rr = Register.getInstance();
		Operacion operacion = rr.getOperacion(idOperacion);
		Perfil perfil = rr.getPerfil(idPerfil);
		Permiso permiso = new Permiso();
		permiso.setOperacion(operacion);
		permiso.setHabilitado(true);
		permiso.setPerfil(perfil);
		rr.saveObject(permiso, login);
		perfil.getPermisos().add(permiso);
		rr.saveObject(perfil, login);
	}

	/**
	 * Agrega el formulario seleccionado a la lista de formularios del modulo
	 * seleccionado
	 */
	public void addFormularioAModulo(long idFormulario, long idModulo, String login) throws Exception {
		Register rr = Register.getInstance();
		Formulario formulario = rr.getFormulario(idFormulario);
		Modulo modulo = rr.getModulo(idModulo);
		modulo.getFormularios().add(formulario);
		rr.saveObject(modulo, login);
	}

	/**
	 * Agrega la operacion seleccionada a la lista de operaciones del formulario
	 * seleccionado
	 */
	public void addOperacionAFormulario(long idOperacion, long idFormulario, String login) throws Exception {
		Register rr = Register.getInstance();
		Formulario formulario = rr.getFormulario(idFormulario);
		Operacion operacion = rr.getOperacion(idOperacion);
		operacion.setFormulario(formulario);
		formulario.getOperaciones().add(operacion);
		rr.saveObject(operacion, login);
		rr.saveObject(formulario, login);
	}

	/**
	 * Remueve un perfil de la lista de perfiles del usuario seleccionado
	 */
	public void removePerfilUsuario(long idPerfil, long idUsuario, String login) throws Exception {
		Register rr = Register.getInstance();
		Usuario usuario = rr.getUsuario(idUsuario);
		Perfil perfil = rr.getPerfil(idPerfil);
		Set<Perfil> perfiles = new HashSet<Perfil>();
		for (Perfil perf : usuario.getPerfiles()) {
			if (perf.getId() != perfil.getId())
				perfiles.add(perf);
		}
		usuario.setPerfiles(perfiles);
		rr.saveObject(usuario, login);
	}

	/**
	 * Deshabilita el permiso del perfil asociada a la operacion seleccionada
	 */
	public void disabledPermisoOperacionPerfil(long idPermiso, String login) throws Exception {
		Register rr = Register.getInstance();
		Permiso permiso = rr.getPermiso(idPermiso);
		permiso.setHabilitado(false);
		rr.saveObject(permiso, login);
	}

	/**
	 * Habilita el permiso del perfil asociada a la operacion seleccionada
	 */
	public void enablePermisoOperacionPerfil(long idPermiso, String login) throws Exception {
		Register rr = Register.getInstance();
		Permiso permiso = rr.getPermiso(idPermiso);
		permiso.setHabilitado(true);
		rr.saveObject(permiso, login);
	}

	/**
	 * deshabilitar un formulario de la lista de formulario del modulo
	 * seleccionado
	 */
	public void disabledFormularioModulo(long idFormulario, String login) throws Exception {
		Register rr = Register.getInstance();
		Formulario formulario = rr.getFormulario(idFormulario);
		formulario.setHabilitado(false);
		rr.saveObject(formulario, login);
	}

	/**
	 * habilitar un formulario de la lista de formulario del modulo seleccionado
	 */
	public void enabledFormularioModulo(long idFormulario, String login) throws Exception {
		Register rr = Register.getInstance();
		Formulario formulario = rr.getFormulario(idFormulario);
		formulario.setHabilitado(true);
		rr.saveObject(formulario, login);
	}

	/**
	 * deshabilita la operacion selecionada
	 */
	public void disabledOperacionFormulario(long idOperacion, String login) throws Exception {
		Register rr = Register.getInstance();
		Operacion operacion = rr.getOperacion(idOperacion);
		operacion.setHabilitado(false);
		rr.saveObject(operacion, login);
	}

	/**
	 * habilita la operacion selecionada
	 */
	public void enabledOperacionFormulario(long idOperacion, String login) throws Exception {
		Register rr = Register.getInstance();
		Operacion operacion = rr.getOperacion(idOperacion);
		operacion.setHabilitado(true);
		rr.saveObject(operacion, login);
	}

	/**
	 * obtiene la operacion para editarla
	 */

	public MyArray getOperacionParaEditar(long idOperacion) throws Exception {
		MyArray operacionOut = new MyArray();
		Register rr = Register.getInstance();
		Operacion operacion = rr.getOperacion(idOperacion);
		operacionOut.setId(operacion.getId());
		operacionOut.setPos1(operacion.getAlias());
		operacionOut.setPos2(operacion.getNombre());
		operacionOut.setPos3(operacion.getDescripcion());
		operacionOut.setPos4(operacion.getIdTexto());
		return operacionOut;
	}

	/**
	 * obtiene el formulario para editarlo
	 */

	public MyArray getFormularioParaEditar(long idFormulario) throws Exception {
		MyArray formularioOut = new MyArray();
		Register rr = Register.getInstance();
		Formulario formulario = rr.getFormulario(idFormulario);
		formularioOut.setId(formulario.getId());
		formularioOut.setPos1(formulario.getLabel());
		formularioOut.setPos2(formulario.getDescripcion());
		formularioOut.setPos3(formulario.getUrl());
		formularioOut.setPos4(formulario.getAlias());
		return formularioOut;
	}

	/**
	 * obtiene el modulo para editarlo
	 */

	public MyArray getModuloParaEditar(long idModulo) throws Exception {
		MyArray moduloOut = new MyArray();
		Register rr = Register.getInstance();
		Modulo modulo = rr.getModulo(idModulo);
		moduloOut.setId(modulo.getId());
		moduloOut.setPos1(modulo.getNombre());
		moduloOut.setPos2(modulo.getDescripcion());
		return moduloOut;
	}

	/**
	 * obtiene el perfil para editarlo
	 */

	public MyArray getPerfilParaEditar(long idPerfil) throws Exception {
		MyArray perfilOut = new MyArray();
		Register rr = Register.getInstance();
		Perfil perfil = rr.getPerfil(idPerfil);
		perfilOut.setId(perfil.getId());
		perfilOut.setPos1(perfil.getNombre());
		perfilOut.setPos2(perfil.getDescripcion());
		perfilOut.setPos3(perfil.getGrupo());
		return perfilOut;
	}

	/**
	 * Persiste Operacion editada
	 */
	public MyArray guardarOperacionEditada(MyArray operacion, String login) throws Exception {
		Register rr = Register.getInstance();
		Operacion operacionEdit = rr.getOperacion(operacion.getId());
		operacionEdit.setAlias(operacion.getPos1().toString());
		operacionEdit.setNombre(operacion.getPos2().toString());
		operacionEdit.setDescripcion(operacion.getPos3().toString());
		operacionEdit.setIdTexto(operacion.getPos4().toString());
		rr.saveObject(operacionEdit, login);

		operacion.setPos4(operacionEdit.isHabilitado() ? "SI" : "NO");
		return operacion;
	}

	/**
	 * Persiste Formulario editado
	 */
	public MyArray guardarFormularioEditado(MyArray formulario, String login) throws Exception {
		Register rr = Register.getInstance();
		Formulario formularioEdit = rr.getFormulario(formulario.getId());
		formularioEdit.setLabel(formulario.getPos1().toString());
		formularioEdit.setDescripcion(formulario.getPos2().toString());
		formularioEdit.setUrl(formulario.getPos3().toString());
		formularioEdit.setAlias(formulario.getPos4().toString());
		rr.saveObject(formularioEdit, login);

		formulario = new MyArray();
		formulario.setId(formularioEdit.getId());
		formulario.setPos1(formularioEdit.getLabel());
		formulario.setPos2(formularioEdit.getAlias());
		formulario.setPos3(formularioEdit.isHabilitado() ? "SI" : "NO");
		return formulario;
	}

	/**
	 * Persiste modulo editado
	 */
	public MyArray guardarModuloEditado(MyArray modulo, String login) throws Exception {
		Register rr = Register.getInstance();
		Modulo moduloEdit = rr.getModulo(modulo.getId());
		moduloEdit.setNombre(modulo.getPos1().toString());
		moduloEdit.setDescripcion(modulo.getPos2().toString());
		rr.saveObject(moduloEdit, login);

		modulo = new MyArray();
		modulo.setId(moduloEdit.getId());
		modulo.setPos1(moduloEdit.getNombre());
		modulo.setPos2(moduloEdit.getDescripcion());
		return modulo;
	}

	/**
	 * Persiste modulo editado
	 */
	public MyArray guardarPerfilEditado(MyArray perfil, String login) throws Exception {

		Register rr = Register.getInstance();
		Perfil perfilEdit = rr.getPerfil(perfil.getId());
		perfilEdit.setNombre(perfil.getPos1().toString());
		perfilEdit.setDescripcion(perfil.getPos2().toString());
		perfilEdit.setGrupo(perfil.getPos3().toString());
		rr.saveObject(perfilEdit, login);
		return perfil;
	}

	/**
	 * Activa o inactiva usuario
	 */
	public void activarInactivarUsuario(long id, boolean activo, String login, String motivoInactivacion)
			throws Exception {
		Register rr = Register.getInstance();
		Usuario usuario = rr.getUsuario(id);
		usuario.setActivo(activo);
		if (!activo) {
			usuario.setMotivoDeInactivacion(motivoInactivacion);
			usuario.setFechaDeInactivacion(new Date());
		}
		rr.saveObject(usuario, login);

	}

	public void resetPass(long idUsuario, String login) throws Exception {
		Register rr = Register.getInstance();
		Usuario usuario = rr.getUsuario(idUsuario);
		usuario.setClave((new Misc()).encriptar("123"));
		rr.saveObject(usuario, login);
	}

}

class MyArrayCompare implements Comparator<MyArray> {

	@Override
	public int compare(MyArray p1, MyArray p2) {
		return ((String) p1.getPos1()).compareTo((String) p2.getPos1());
	}
}
