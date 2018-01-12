package com.coreweb.util.population;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.EntityNameResolver;
import org.hibernate.cfg.Configuration;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.domain.*;
import com.coreweb.util.Misc;

public class DBMenuUserParser {

	// private static String fileD = "./WEB-INF/menu_config.ini";
	private String file = ""; // fileD;
	private Class idClass = null;
	private String userIni = "";
	private String[] usuarioPropiedades = {};

	private static String dfv = "defaultValue";
	private static String split = ";";

	private Properties prop = null;

	private Misc misc = new Misc();
	private Hashtable<String, Object> obj = new Hashtable<String, Object>();
	private Hashtable<String, String> aliasTipo = new Hashtable<String, String>();

	private Register rr = Register.getInstance();

	public DBMenuUserParser(String fileIni, Class idClass, String userIni, String[] usuarioPropiedades) {
		this.file = fileIni;
		this.idClass = idClass;
		this.userIni = userIni;
		this.usuarioPropiedades = usuarioPropiedades;
	}

	private void cargaMenu(boolean grabar) throws Exception {
		String m = "m";
		Modulo modulo = new Modulo();

		// 0 es el core.

		for (int mi = 0; ((mi <= 1) || (modulo != null)); mi++) {
			// buscar los modulos
			String m_ = m + mi;
			modulo = getModulo(m_);
			if (modulo != null) {
				// buscar los formularios

				parseFormulario(modulo, m_);

				if (grabar == true) {
					rr.saveObject(modulo, Config.USER_SYSTEMA);
				}
			}
		} // modulo

	}

	private void parseFormulario(Modulo modulo, String m_) {
		String f = "f";
		Formulario formulario = new Formulario();
		for (int fi = 1; (formulario != null); fi++) {
			String m_f_ = m_ + f + fi;
			formulario = getFormulario(m_f_);
			if (formulario != null) {

				modulo.getFormularios().add(formulario);

				parseOperacion(formulario, m_f_);

			}
		}
	}

	private void parseOperacion(Formulario formulario, String m_f_) {
		String o = "o";
		Operacion operacion = new Operacion();
		for (int oi = 1; (operacion != null); oi++) {
			String m_f_o_ = m_f_ + o + oi;
			operacion = getOperacion(m_f_o_, formulario.getAlias());
			if (operacion != null) {

				formulario.getOperaciones().add(operacion);
				operacion.setFormulario(formulario);
				this.obj.put(m_f_o_, operacion);

				/*
				 * try { Operacion op =
				 * rr.getOperacionFormulario(formulario.getAlias(),
				 * operacion.getAlias()); op.setIdTexto(m_f_o_);
				 * rr.saveObject(op); } catch (Exception e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */

			}
		}
	}

	private Modulo getModulo(String mn) {
		Modulo modulo = null;
		String mv = prop.getProperty(mn, dfv);
		if (mv.compareTo(dfv) != 0) {
			// nombre ; descripcion
			String[] value = misc.split(mv, split);
			modulo = new Modulo();
			modulo.setNombre(value[0]);
			modulo.setDescripcion(value[1]);
		}
		return modulo;
	}

	private Formulario getFormulario(String fn) {
		Formulario formulario = null;
		String fv = prop.getProperty(fn, dfv);
		if (fv.compareTo(dfv) != 0) {
			// alias ; habilitado ; label; descripcion ; url
			String[] value = misc.split(fv, split);
			formulario = new Formulario();
			formulario.setAlias(value[0]);
			formulario.setHabilitado(Boolean.parseBoolean(value[1]));
			formulario.setLabel(value[2]);
			formulario.setDescripcion(value[3]);
			formulario.setUrl(value[4]);

			this.aliasTipo.put(formulario.getAlias(), "F");
		}
		return formulario;
	}

	private Operacion getOperacion(String on, String formularioAlias) {
		Operacion operacion = null;
		String ov = prop.getProperty(on, dfv);
		if (ov.compareTo(dfv) != 0) {
			// alias ; nombre ; descripcion ; habilitado
			String[] value = misc.split(ov, split);
			operacion = new Operacion();
			operacion.setIdTexto(on);
			operacion.setAlias(value[0]);
			operacion.setNombre(value[1]);
			operacion.setDescripcion(value[2]);
			operacion.setHabilitado(Boolean.parseBoolean(value[3]));

			if (esOperacionABM(operacion.getAlias(), formularioAlias) == false) {
				this.aliasTipo.put(operacion.getAlias(), "O");
			}

		}
		return operacion;
	}

	private boolean esOperacionABM(String opAlias, String foAlias) {

		boolean ab = (IDCore.O_ABRIR + foAlias).compareTo(opAlias) == 0;
		boolean ag = (IDCore.O_AGREGAR + foAlias).compareTo(opAlias) == 0;
		boolean ed = (IDCore.O_EDITAR + foAlias).compareTo(opAlias) == 0;
		boolean el = (IDCore.O_ELIMINAR + foAlias).compareTo(opAlias) == 0;
		boolean bu = (IDCore.O_BUSCAR + foAlias).compareTo(opAlias) == 0;

		boolean out = (ab || ag || ed || el || bu);

		return out;
	}

	private Perfil getPerfil(String pn) {
		Perfil perfil = null;
		String pv = prop.getProperty(pn, dfv);
		if (pv.compareTo(dfv) != 0) {
			// nombre ; descripcion ; grupo
			String[] value = misc.split(pv, split);
			perfil = new Perfil();
			perfil.setNombre(value[0]);
			perfil.setDescripcion(value[1]);
			String grupo = (String) this.obj.get(value[2]);
			perfil.setGrupo(grupo);
			this.obj.put(pn, perfil);

		}
		return perfil;
	}

	private Permiso getPermiso(String pen) {
		Permiso permiso = null;
		String pev = prop.getProperty(pen, dfv);
		if (pev.compareTo(dfv) != 0) {
			// p_ (perfil) ; m_f_o_ (operacion) ; habilitado
			String[] value = misc.split(pev, split);

			Perfil perfil = (Perfil) this.obj.get(value[0]);
			Operacion operacion = (Operacion) this.obj.get(value[1]);
			boolean habilitado = Boolean.parseBoolean(value[2]);

			permiso = new Permiso();
			permiso.setOperacion(operacion);
			permiso.setHabilitado(habilitado);
			permiso.setPerfil(perfil);
			perfil.getPermisos().add(permiso);

		}
		return permiso;
	}

	private Usuario getUsuario(String un) {
		Usuario usuario = null;
		String uv = prop.getProperty(un, dfv);
		if (uv.compareTo(dfv) != 0) {
			// login ; nommbre ; clave ; p_ ... (lista de perfiles)
			String[] value = misc.split(uv, split);

			usuario = new Usuario();
			usuario.setLogin(value[0]);
			usuario.setNombre(value[1]);

			// cuando se usa el menu que es generado, se tiene las claves
			// encriptadas, en ese caso no hay que volver a encriptar
			String clave = value[2];
			if (clave.trim().length() < 25) {
				clave = misc.encriptar(value[2]);
			}
			usuario.setClave(clave);

			// perfiles para este usuario
			for (int pi = 3; pi < value.length; pi++) {
				String pn = value[pi];
				Perfil perfil = (Perfil) this.obj.get(pn);
				usuario.getPerfiles().add(perfil);
			}
		}

		return usuario;
	}

	private void cargaUsuarioPerfil(boolean grabar) throws Exception {

		// carga los grupos
		String g = "g";
		String gv = "";
		for (int gi = 1; (gv != dfv); gi++) {
			String gn = g + gi;
			gv = this.prop.getProperty(gn, dfv);
			if (gv != dfv) {
				this.obj.put(gn, gv.trim());

			}
		}

		// carga los perfiles
		String p = "p";
		Perfil perfil = new Perfil();
		for (int pi = 1; (perfil != null); pi++) {
			String pn = p + pi;
			perfil = getPerfil(pn);
			if ((perfil != null) && (grabar == true)) {
				rr.saveObject(perfil, Config.USER_SYSTEMA);
			}
		}

		// carga los permisos por perfil
		/*
		 * String pe = "pe"; Permiso permiso = new Permiso(); for (int pei = 1;
		 * (permiso != null); pei++) { String pen = pe + pei; permiso =
		 * getPermiso(pen); if ((permiso != null) && (grabar == true)) {
		 * rr.saveObject(permiso); } }
		 */
		this.cargaPermisos(grabar);

		// carga los usuario
		String u = "u";
		Usuario usuario = new Usuario();
		for (int ui = 1; (usuario != null); ui++) {
			String un = u + ui;
			usuario = getUsuario(un);
			if ((usuario != null) && (grabar == true)) {
				rr.saveObject(usuario, Config.USER_SYSTEMA);
			}
		}

	}

	private void loadConfigMenu() throws Exception {

		// File f = new File(file);
		/*
		 * if (f.isFile() == false) { file = fileS; }
		 */
		prop = new Properties();
		// prop.load(new FileInputStream(file));
		prop.load(new InputStreamReader(new FileInputStream(file), "utf-8"));

	}

	private void deleteDatos() throws Exception {

		/*
		 * rr.deleteAllObjects(Permiso.class.getName());
		 * rr.deleteAllObjects(Operacion.class.getName());
		 * 
		 * rr.deleteAllObjects(Perfil.class.getName());
		 * rr.deleteAllObjects(Modulo.class.getName());
		 * 
		 * rr.deleteAllObjects(UsuarioPropiedad.class.getName());
		 * rr.deleteAllObjects(Usuario.class.getName());
		 */
		String delTablas = "DROP TABLE IF EXISTS " + "usuario, " + "usuario_usuarioperfil, " + "usuarioformulario, "
				+ "usuariomodulo, " + "usuariooperacion, " + "usuarioperfil, " + "usuariopermiso, "
				+ "usuariopropiedad " + "CASCADE";

		rr.sql2(delTablas);
		rr.resetTables();

		rr.deleteAllObjects(Tipo.class.getName(), "sigla", Config.TIPO_USUARIO_PROPIEDAD_SIGLA);
		rr.deleteAllObjects(TipoTipo.class.getName(), "descripcion", Config.TIPO_TIPO_USUARIO_PROPIEDAD);

	}

	public void testMenusPerfilesUsuarios() throws Exception {
		this.loadConfigMenu();
		this.cargaMenu(false);
		this.cargaUsuarioPerfil(false);
	}

	public void cargaMenusPerfilesUsuarios() throws Exception {
		this.deleteDatos();
		this.loadConfigMenu();
		this.cargaMenu(true);
		this.cargaUsuarioPerfil(true);
		System.out.println("\n======================= menu_config.ini -> ID \n");
		this.verificarAlias();
		System.out.println("\n======================= ID -> menu_config.ini \n");
		this.misc.testIdInAlias(this.aliasTipo, this.idClass);

		// CHISTES
		DBChistes chistes = new DBChistes();
		chistes.chistes();

		System.out.println("\n======================= fin =================");

		this.cargaUsuarioPropiedades();

	}

	private void verificarAlias() {
		Enumeration<String> enu = this.aliasTipo.keys();
		while (enu.hasMoreElements()) {
			String alias = enu.nextElement();
			String tipo = this.aliasTipo.get(alias);
			this.misc.testAliasInID(tipo, alias, this.idClass);
		}
		// cargar los valores del ABM
		this.aliasTipo.put(IDCore.O_ABRIR, "O");
		this.aliasTipo.put(IDCore.O_AGREGAR, "O");
		this.aliasTipo.put(IDCore.O_ELIMINAR, "O");
		this.aliasTipo.put(IDCore.O_EDITAR, "O");
		this.aliasTipo.put(IDCore.O_BUSCAR, "O");
	}

	private Permiso getPermisoFormatoNuevo(String k) {
		Permiso permiso = null;

		try {
			String tf = prop.getProperty(k);
			if ((tf.compareTo("TRUE") == 0) || (tf.compareTo("FAlSE") == 0)) {
				String[] pk = misc.split(k, "-");
				String p = pk[0];
				String o = pk[1];

				Perfil perfil = (Perfil) this.obj.get(p);
				Operacion operacion = (Operacion) this.obj.get(o);

				boolean habilitado = Boolean.parseBoolean(tf);
				permiso = new Permiso();
				permiso.setOperacion(operacion);
				permiso.setHabilitado(habilitado);
				permiso.setPerfil(perfil);
				perfil.getPermisos().add(permiso);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return permiso;
	}

	private void cargaPermisos(boolean grabar) throws Exception {
		// p1-m2f2o4 = TRUE
		Enumeration keys = this.prop.keys();
		while (keys.hasMoreElements()) {
			String k = (String) keys.nextElement();
			Permiso permiso = this.getPermisoFormatoNuevo(k.trim());
			if ((permiso != null) && (grabar == true)) {
				System.out.println("---------- k:(" + k.trim() + ")  permiso:" + permiso.getId() + "  oper:"
						+ permiso.getOperacion());
				rr.saveObject(permiso, Config.USER_SYSTEMA);
			}
		}
	}

	/**
	 * Carga las propiedadesd de los usuarios
	 * 
	 * @throws Exception
	 */
	public void cargaUsuarioPropiedades() throws Exception {

		if (this.usuarioPropiedades.length == 0) {
			return;
		}

		Properties upFile = new Properties();
		// prop.load(new FileInputStream(file));
		upFile.load(new InputStreamReader(new FileInputStream(this.userIni), "utf-8"));

		// cargar las propiedades como tipo
		List<Tipo> lup = new ArrayList<Tipo>();

		TipoTipo tipoUPro = new TipoTipo();
		tipoUPro.setDescripcion(Config.TIPO_TIPO_USUARIO_PROPIEDAD);
		rr.saveObject(tipoUPro, "SYS");

		for (int i = 0; i < this.usuarioPropiedades.length; i++) {
			String pN = this.usuarioPropiedades[i];
			Tipo propTipo = new Tipo();
			propTipo.setDescripcion(pN);
			propTipo.setTipoTipo(tipoUPro);
			propTipo.setSigla(Config.TIPO_USUARIO_PROPIEDAD_SIGLA);

			rr.saveObject(propTipo, "SYS");
			lup.add(propTipo);
		}

		List<Domain> lDoman = new ArrayList<Domain>();

		// buscar todos los usuarios
		List<Usuario> lu = rr.hql("from Usuario");

		// para cada usuario, recorrer la lista de propiedades y cargar
		for (Iterator iterator = lu.iterator(); iterator.hasNext();) {
			Usuario u = (Usuario) iterator.next();

			String login = u.getLogin();

			// recorro todas las propiedades y las asigno a los los usuarios
			for (Iterator iterator2 = lup.iterator(); iterator2.hasNext();) {
				Tipo propTipo = (Tipo) iterator2.next();

				String pN = propTipo.getDescripcion();

				String key = login + "_" + pN;

				String valor = upFile.getProperty(key);

				if (valor != null) {
					String[] valores = valor.split(",");
					for (int i = 0; i < valores.length; i++) {
						String valorItem = valores[i];

						UsuarioPropiedad up = new UsuarioPropiedad();
						up.setClave(propTipo);
						up.setValor(valorItem);
						u.getUsuarioPropiedades().add(up);
					}

				}
			}

			rr.saveObject(u, "SYS");

		}

	}

	// ==================================================

	private String getMenuConfigCabecera() {
		String out = "";
		out += "﻿####################################################################################################\n";
		out += "# Modulos: 	m_	= nombre ; descripcion\n";
		out += "# Formularios:	m_f_	= alias ; habilitado ; label ;  descripcion ; url\n";
		out += "# Operacion:	m_f_o_	= alias ; nombre ; descripcion ; habilitado \n";
		out += "####################################################################################################\n";
		out += "\n\n";
		return out;
	}

	private String getMenuConfigCabeceraUsuarios() {
		String out = "";
		out += "#################################################################################################### \n";
		out += "# Grupos:	g_ = texto  (Nota: no van en la DB, están en el Config.java) \n";
		out += "# Perfil:	p_ = nombre ; descripcion ; grupo \n";
		out += "# Usuario:	u_ = login ; nommbre ; clave ; p_ ... (lista de perfiles) \n";
		out += "# Permisos	p_(perfil)-m_f_o_ (operacion) = habilitado (TRUE|FALSE) \n";
		out += "\n";
		return out;
	}

	private String getOperacionStr(String preForm, Operacion oper, int id) {
		// # Operacion: m_f_o_ = alias ; nombre ; descripcion ; habilitado
		String out = "";

		String preOper = preForm + "o" + id;
		out += preOper + " = " + oper.getAlias() + " ; ";
		out += oper.getNombre() + " ; ";
		out += oper.getDescripcion() + " ; ";
		out += (oper.isHabilitado() + " ; ").toUpperCase();

		// set el idText en la Operacion
		oper.setIdTexto(preOper);
		try {
			rr.saveObject(oper, "sys");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return out;
	}

	private String getFormularioStr(String preMod, Formulario form, int id) {
		// # Formularios: m_f_ = alias ; habilitado ; label ; descripcion ; url
		String out = "";
		String preForm = preMod + "f" + id;

		out = preForm + " = " + form.getAlias() + " ; ";
		out += (form.isHabilitado() + " ; ").toUpperCase();
		out += form.getLabel() + " ; ";
		out += form.getDescripcion() + " ; ";
		out += form.getUrl();
		out += "\n";

		int i = 1;
		Set<Operacion> loper = form.getOperaciones();
		for (Iterator ite = loper.iterator(); ite.hasNext();) {
			Operacion oper = (Operacion) ite.next();
			out += this.getOperacionStr(preForm, oper, i++) + "\n";

		}

		return out;
	}

	private String getModuloStr(Modulo mod, int id) {
		// m_ = nombre ; descripcion
		String out = "";

		String preMod = "m" + id;
		out += preMod + " = " + mod.getNombre().trim() + " ; " + mod.getDescripcion().trim() + "\n";

		// recorre los formularios
		int i = 1;
		Set<Formulario> lfor = mod.getFormularios();
		for (Iterator ite = lfor.iterator(); ite.hasNext();) {
			Formulario form = (Formulario) ite.next();
			out += this.getFormularioStr(preMod, form, i++) + "\n";
		}

		return out;
	}

	private String getPermisoStr(Permiso perm, String prePer) {
		// # Permisos p_(perfil)-m_f_o_ (operacion) = habilitado (TRUE|FALSE)
		String out = "";
		out += prePer + "-";
		out += perm.getOperacion().getIdTexto() + " = ";
		out += (perm.isHabilitado() + "").toUpperCase();
		return out;
	}

	private String[] getPerfilStr(Perfil per, int id) {
		// {"idPerfil","strPerfil", "strPermisos"};
		// # Perfil: p_ = nombre ; descripcion ; grupo

		String prePer = "p" + id;
		String perStr = prePer + " = ";
		perStr += per.getNombre() + " ; ";
		perStr += per.getDescripcion() + " ; ";
		perStr += per.getGrupo();

		String permPerStr = "";

		Set<Permiso> sperm = per.getPermisos();
		for (Iterator ite = sperm.iterator(); ite.hasNext();) {
			Permiso perm = (Permiso) ite.next();
			permPerStr += this.getPermisoStr(perm, prePer) + "\n";
		}
		permPerStr += "\n";

		try {
			per.setAuxi(prePer);
			rr.saveObject(per, "sys");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		String[] out = { prePer, perStr, permPerStr };

		return out;
	}

	private String getUsariosStr(Usuario us, int id) {
		// # Usuario: u_ = login ; nommbre ; clave ; p_ ... (lista de perfiles)
		String out = "";

		String outPerfil = "";
		String outPermisos = "";

		String usPre = "u" + id;

		out += usPre + " = ";
		out += us.getLogin() + " ; ";
		out += us.getNombre() + " ; ";
		out += us.getClave() + " ";

		Set<Perfil> lper = us.getPerfiles();
		for (Iterator ite = lper.iterator(); ite.hasNext();) {
			Perfil per = (Perfil) ite.next();
			out += " ; " + per.getAuxi();
		}
		return out;
	}

	private String menuConfigInversa() throws Exception {
		String out = "";

		out = this.getMenuConfigCabecera();

		// load módulo
		List<Modulo> lmod = rr.getAllModulos();

		for (int i = 0; i < lmod.size(); i++) {

			// Modulos: m_ = nombre ; descripcion
			Modulo mod = lmod.get(i);
			out += this.getModuloStr(mod, (i + 1)) + "\n";
			out += "##------------------------\n";
		}

		out += "\n\n";

		String outPerfiles = "";
		String outPermisos = "";
		// load los Perfiles, así arma todo
		List<Perfil> lper = rr.getAllPerfiles();
		for (int i = 0; i < lper.size(); i++) {
			Perfil per = lper.get(i);
			String[] perStr = this.getPerfilStr(per, (i + 1));
			outPerfiles += perStr[1] + "\n";
			outPermisos += perStr[2] + "\n";
		}

		// load los usuarios
		out += this.getMenuConfigCabeceraUsuarios();

		List<Usuario> lus = rr.getAllUsuarios();
		for (int i = 0; i < lus.size(); i++) {
			Usuario us = lus.get(i);
			out += this.getUsariosStr(us, (i + 1)) + "\n";
		}

		out += "\n\n# Perfiles \n";
		out += outPerfiles;
		out += "\n\n# Permisos \n";
		out += outPermisos;

		return out;

	}

	public static void generarMenuInversa(String path) {
		try {
			Misc m = new Misc();
			DBMenuUserParser dd = new DBMenuUserParser(null, null, null, null);
			String ss = dd.menuConfigInversa();
			m.grabarStringToArchivo(path, ss);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("====== FIN");
	}

}
