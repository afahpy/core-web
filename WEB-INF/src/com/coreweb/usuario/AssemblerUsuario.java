package com.coreweb.usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Formulario;
import com.coreweb.domain.Modulo;
import com.coreweb.domain.Operacion;
import com.coreweb.domain.Perfil;
import com.coreweb.domain.Permiso;
import com.coreweb.domain.Ping;
import com.coreweb.domain.Register;
import com.coreweb.domain.Usuario;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.AssemblerCoreUtil;
import com.coreweb.dto.DTO;
import com.coreweb.modulo.AssemblerModulo;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

public class AssemblerUsuario extends Assembler {

	/*
	 * listUsrArr: lista que contiene todos los usuarios usrArr: MyArray que
	 * guarda los datos del usuario id: id pos1: nombre pos2: login pos3: clave
	 * pos4: listPerfArr (lista de los perfiles del usuario) pos5: copia de la
	 * clave
	 * 
	 * listPerfArr: lista que contiene los perfiles perfArr: MyArray que guarda
	 * los datos del perfil id: id pos1: nombre pos2: descripcion pos3: usuarios
	 * que utilizan el perfil pos4: listPermArr listPermArr: lista que contiene
	 * los permisos de un perfil permArr: MyArray que guarda los datos del
	 * permiso id: id pos1: habilitado pos2: operacion pos3: perfil
	 */

	public static UsuarioDTO getDTOUsuario() {
		UsuarioDTO dto = null;
		try {
			AssemblerUsuario as = new AssemblerUsuario();
			dto = (UsuarioDTO) as.domainToDto(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dto.setId(new Long(1));
		return dto;
	}

	@Override
	public Domain dtoToDomain(DTO dtoU) throws Exception {
		// TODO Auto-generated method stub
		UsuarioDTO dto = (UsuarioDTO) dtoU;

		List<Domain> allUsuarios = new ArrayList<Domain>();
		List<Domain> allPerfilesUsuario = new ArrayList<Domain>();
		List<Domain> allPerfiles = new ArrayList<Domain>();
		List<Domain> allPermisos = new ArrayList<Domain>();

		List<MyArray> usuarios = dto.getUsuarios();
		Register rr = Register.getInstance();

		for (Iterator iterator = usuarios.iterator(); iterator.hasNext();) {
			MyArray usrArr = (MyArray) iterator.next();
			Usuario usrDom = new Usuario();

			usrDom.setId(usrArr.getId());
			usrDom.setNombre((String) usrArr.getPos1());
			usrDom.setLogin((String) usrArr.getPos2());
			usrDom.setClave((String) usrArr.getPos3());

			List<MyArray> perfiles = (List<MyArray>) usrArr.getPos4();
			Set<Perfil> setPerf = new HashSet<Perfil>();

			for (Iterator iterator2 = perfiles.iterator(); iterator2.hasNext();) {
				MyArray perfArr = (MyArray) iterator2.next();
				Perfil perfDom = new Perfil();

				perfDom.setId(perfArr.getId());
				perfDom.setNombre((String) perfArr.getPos1());
				perfDom.setDescripcion((String) perfArr.getPos2());
				setPerf.add(perfDom);
				allPerfilesUsuario.add(perfDom);
			}
			usrDom.setPerfiles(setPerf);
			allUsuarios.add(usrDom);
		}
		rr.saveObjects(allUsuarios, this.getLogin());

		// controlar usuarios
		List<Usuario> usuariosDom = rr.getAllUsuarios();
		boolean existeU = false;
		// boolean existePU = false;
		for (Usuario usuarioD : usuariosDom) {
			for (Domain usuarioNew : allUsuarios) {
				if (usuarioD.getId() == usuarioNew.getId()) {
					existeU = true;
				}
			}
			if (!existeU) {
				rr.deleteObject(usuarioD);
			}
			existeU = false;
		}

		// recorre los perfiles
		List<MyArray> perfiles = dto.getPerfiles();

		for (Iterator iterator = perfiles.iterator(); iterator.hasNext();) {
			Perfil perDom = new Perfil();
			MyArray perfArr = (MyArray) iterator.next();

			perDom.setId(perfArr.getId());
			perDom.setNombre((String) perfArr.getPos1());
			perDom.setDescripcion((String) perfArr.getPos2());

			List<MyArray> permisos = (List<MyArray>) perfArr.getPos4();
			Set<Permiso> setPerm = new HashSet<Permiso>();

			for (Iterator iterator2 = permisos.iterator(); iterator2.hasNext();) {
				MyArray permArr = (MyArray) iterator2.next();
				Permiso permDom = new Permiso();

				permDom.setId(permArr.getId());
				if (((MyPair) permArr.getPos1()).getId() == 1) {
					permDom.setHabilitado(true);
				} else {
					permDom.setHabilitado(false);
				}
				Domain oper = rr.getObject(
						com.coreweb.domain.Operacion.class.getName(),
						((MyArray) permArr.getPos2()).getId());
				permDom.setOperacion((Operacion) oper);
				permDom.setPerfil(perDom);
				setPerm.add(permDom);
				//rr.saveObject(permDom); // verificar si se debe o no guardar
				// ya aca
				allPermisos.add(permDom);
			}
			perDom.setPermisos(setPerm);
			rr.saveObject(perDom, this.getLogin());
			allPerfiles.add(perDom);
		}

		/*
		// controlar permisos (cuando esto esta sale el error de cascada, cuando
		// no esta sale el de transient..)
		List<Permiso> permisosDom = rr.getAllPermisos();
		boolean existePE = false;
		for (Permiso permisoD : permisosDom) {
			for (Domain permisoNew : allPermisos) {
				if (permisoD.getId() == permisoNew.getId()) {
					existePE = true;
				}
			}
			if (!existePE) {
				System.out.println("Eliminar------- "+permisoD.getId());
				rr.deleteObject(permisoD);
			}
			existePE = false;
		}
		*/

		// controlar perfiles
		List<Perfil> perfilesDom = rr.getAllPerfiles();
		boolean existeP = false;
		for (Perfil perfilD : perfilesDom) {
			for (Domain perfilNew : allPerfiles) {
				if (perfilD.getId() == perfilNew.getId()) {
					existeP = true;
				}
			}
			if (!existeP) {
				List<Usuario> listUsuarios = rr.getAllUsuarios();
				for (Iterator iterator = listUsuarios.iterator(); iterator
						.hasNext();) {
					Usuario usr = (Usuario) iterator.next();
					Set<Perfil> usrPerf = usr.getPerfiles();
					if (usrPerf.size() != 0) {
						for (Iterator iterator2 = usrPerf.iterator(); iterator2
								.hasNext();) {
							Perfil perfil = (Perfil) iterator2.next();
							if (perfil.getId() == perfilD.getId()) {
								usrPerf.remove(perfil);
							}
						}
					}
					rr.saveObject(usr, this.getLogin());
				}
				// verificar si esto es necesario
				Set<Permiso> permisos = perfilD.getPermisos();
				perfilD.getPermisos().removeAll(permisos);
				rr.deleteObject(perfilD);
			}
			existeP = false;
		}

		Ping ping = new Ping();
		ping.setEcho("Configuracion usuario modificada: "
				+ System.currentTimeMillis());
		return ping;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		// TODO Auto-generated method stub
		UsuarioDTO dto = new UsuarioDTO();

		// ========================================================================
		// hash entre usuarios y perfiles
		HashMap<Long, List<String>> usrPorPerfil = new HashMap<Long, List<String>>();
		// ========================================================================

		List<MyArray> listUsrArr = new ArrayList<MyArray>();
		Register rr = Register.getInstance();

		// recorre los usuarios
		List<Usuario> listUsr = rr.getAllUsuarios();

		for (Iterator iterator = listUsr.iterator(); iterator.hasNext();) {
			Usuario usuario = (Usuario) iterator.next();
			MyArray usrArr = new MyArray();

			usrArr.setId(usuario.getId());
			usrArr.setPos1(usuario.getNombre());
			usrArr.setPos2(usuario.getLogin());
			usrArr.setPos3(usuario.getClave());
			usrArr.setPos5(usuario.getClave());

			Set<Perfil> setPerf = usuario.getPerfiles();

			List<MyArray> listPerfArr = new ArrayList<MyArray>();
			for (Iterator iterator2 = setPerf.iterator(); iterator2.hasNext();) {
				Perfil perfil = (Perfil) iterator2.next();
				MyArray perfArr = new MyArray();

				// agrega la lista en la que se almacenarán los usuarios del
				// perfil
				List lista = usrPorPerfil.get(perfil.getId());
				if (lista == null) {
					lista = new ArrayList<String>();
					usrPorPerfil.put(perfil.getId(), lista);
				}
				lista.add(usuario.getNombre());

				perfArr.setId(perfil.getId());
				perfArr.setPos1(perfil.getNombre());
				perfArr.setPos2(perfil.getDescripcion());
				// depende de si al traer los usuarios se necesitan tb los
				// permisos, por ahora trae igual
				/*
				 * Set<Permiso> setPerm = perfil.getPermisos(); List<MyArray>
				 * listPermArr = new ArrayList<MyArray>(); for (Iterator
				 * iterator3 = setPerm.iterator(); iterator3 .hasNext();) {
				 * Permiso permiso = (Permiso) iterator3.next(); MyArray permArr
				 * = new MyArray();
				 * 
				 * permArr.setId(permiso.getId());
				 * permArr.setPos1(permiso.isHabilitado());
				 * permArr.setPos2(permiso.getOperacion());
				 * permArr.setPos3(permiso.getPerfil());
				 * listPermArr.add(permArr); } perfArr.setPos3(listPermArr);
				 */
				listPerfArr.add(perfArr);
			}
			usrArr.setPos4(listPerfArr);
			listUsrArr.add(usrArr);
		}
		dto.setUsuarios(listUsrArr);

		// recorre los perfiles

		List<MyArray> listPerfArr = new ArrayList<MyArray>();
		List<Perfil> listPerfi = rr.getAllPerfiles();

		for (Iterator iterator = listPerfi.iterator(); iterator.hasNext();) {
			Perfil perfil = (Perfil) iterator.next();
			MyArray perfArr = new MyArray();

			perfArr.setId(perfil.getId());
			perfArr.setPos1(perfil.getNombre());
			perfArr.setPos2(perfil.getDescripcion());
			perfArr.setPos3(usrPorPerfil.get(perfil.getId()));

			Set<Permiso> setPerm = perfil.getPermisos();
			List<MyArray> listPermArr = new ArrayList<MyArray>();
			for (Iterator iterator3 = setPerm.iterator(); iterator3.hasNext();) {
				Permiso permiso = (Permiso) iterator3.next();
				MyArray permArr = new MyArray();

				permArr.setId(permiso.getId());
				MyPair habilitado = new MyPair();
				if (permiso.isHabilitado()) {
					habilitado.setId(new Long(1));
					habilitado.setText("SI");
				} else {
					habilitado.setId(new Long(2));
					habilitado.setText("NO");
				}
				permArr.setPos1(habilitado);

				MyArray operMA = new MyArray();
				operMA = createMyArray(permiso.getOperacion(), new String[] {
						"alias", "nombre", "descripcion", "habilitado",
						"idTexto" });
				permArr.setPos2(operMA);

				permArr.setPos3(permiso.getPerfil().getId());

				listPermArr.add(permArr);
			}
			perfArr.setPos4(listPermArr);
			listPerfArr.add(perfArr);
		}
		dto.setPerfiles(listPerfArr);
		dto.setModulos(AssemblerModulo.getModulos());

		return dto;
	}

}
