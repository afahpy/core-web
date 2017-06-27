package com.coreweb.modulo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Formulario;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Modulo;
import com.coreweb.domain.Operacion;
import com.coreweb.domain.Perfil;
import com.coreweb.domain.Ping;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.AssemblerCoreUtil;
import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

public class AssemblerModulo extends Assembler{
	
	/* listModArr: lista que contiene todos los modulos
	 * modArr: MyArray que guarda los datos del modulo 
	 * 		id: id
	 * 		pos1: nombre
	 * 		pos2: descripcion
	 * 		pos3: listFormArr
	 * 		pos4: un valor booleano que indica si esta permitido o no eliminar el modulo
	 * listFormArr: lista que contiene los formularios de un modulo
	 * formArr: MyArray que guarda los datos del formulario
	 * 		id: id
	 * 		pos1: label
	 * 		pos2: descripcion
	 * 		pos3: url
	 * 		pos4: alias
	 * 		pos5: estado (habilitado o no)
	 * 		pos6: listOperArr
	 * 		pos7: un valor booleano que indica si esta permitido o no eliminar el formulario
	 * listOperArr: lista que contiene las operaciones de un formulario
	 * operArr: MyArray que guarda los datos de la operacion
	 * 		id: id
	 * 		pos1: alias
	 * 		pos2: nombre
	 * 		pos3: descripcion
	 * 		pos4: estado (habilitada o no)
	 * 		pos5: idTexto
	 * 		pos6: id del formulario al que pertenece la operacion
	 * 		pos7: la lista de los perfiles que utilizan la operacion
	 * 		pos8: un valor booleano que indica si esta permitido o no eliminar la operacion
	 */
	
	static List<Object> listaAliasFormularios = null;
	static List<Object> listaAliasOperaciones = null;
	static List<Object> listaIdTextoOperaciones = null;
	static List<Object> listaNombresModulos = null;

	public static ModuloDTO getDTOModulo() {
		ModuloDTO dto = null;
		try {
			AssemblerModulo as = new AssemblerModulo();
			dto = (ModuloDTO) as.domainToDto(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dto.setId(new Long(1));
		return dto;
	}

	private static String queryPerfil = "" + " select p.perfil.nombre "
			+ " from Permiso p" + " where p.operacion.id = ? ";

	@Override
	public Domain dtoToDomain(DTO dtoM) throws Exception {
		// TODO Auto-generated method stub
		ModuloDTO dto = (ModuloDTO) dtoM;

		List<MyArray> modulos = dto.getModulos();

		List<Domain> allModulos = new ArrayList<Domain>();
		List<Domain> allFormularios = new ArrayList<Domain>();
		List<Domain> allOperaciones = new ArrayList<Domain>();

		Register rr = Register.getInstance();

		// recorre cada modulo
		for (Iterator iterator = modulos.iterator(); iterator.hasNext();) {
			MyArray modArr = (MyArray) iterator.next();
			Modulo modDom = new Modulo();

			modDom.setId(modArr.getId());
			modDom.setNombre((String) modArr.getPos1());
			modDom.setDescripcion((String) modArr.getPos2());

			// recorre los formularios de cada modulo

			List<MyArray> formularios = (List<MyArray>) modArr.getPos3();
			Set<Formulario> setForm = new HashSet<Formulario>();
			for (Iterator iterator2 = formularios.iterator(); iterator2
					.hasNext();) {
				MyArray formArr = (MyArray) iterator2.next();
				Formulario formDom = new Formulario();

				formDom.setId(formArr.getId());
				formDom.setLabel((String) formArr.getPos1());
				formDom.setDescripcion((String) formArr.getPos2());
				formDom.setUrl((String) formArr.getPos3());
				formDom.setAlias((String) formArr.getPos4());
				if (((MyPair) formArr.getPos5()).getId() == 1) {
					formDom.setHabilitado(true);
				} else {
					formDom.setHabilitado(false);
				}

				List<MyArray> operaciones = (List<MyArray>) formArr.getPos6();
				Set<Operacion> setOper = new HashSet<Operacion>();
				for (Iterator iterator3 = operaciones.iterator(); iterator3
						.hasNext();) {
					MyArray operArr = (MyArray) iterator3.next();
					Operacion operDom = new Operacion();

					operDom.setId(operArr.getId());
					operDom.setAlias((String) operArr.getPos1());
					operDom.setNombre((String) operArr.getPos2());
					operDom.setDescripcion((String) operArr.getPos3());
					if (((MyPair) operArr.getPos4()).getId() == 1) {
						operDom.setHabilitado(true);
					} else {
						operDom.setHabilitado(false);
					}
					operDom.setIdTexto((String) operArr.getPos5());
					operDom.setFormulario(formDom);
					// rr.saveObject(operDom);
					setOper.add(operDom);
					allOperaciones.add(operDom);
				}
				formDom.setOperaciones(setOper);
				// rr.saveObject(formDom);
				setForm.add(formDom);
				allFormularios.add(formDom);
			}
			modDom.setFormularios(setForm);
			// rr.saveObject(modDom);
			allModulos.add(modDom);
		}

		rr.saveObjects(allOperaciones, this.getLogin());
		rr.saveObjects(allFormularios, this.getLogin());
		rr.saveObjects(allModulos, this.getLogin());

		if (false) {
			Ping ping = new Ping();
			ping.setEcho("Configuracion modulo modificada: "
					+ System.currentTimeMillis());
			return ping;
		}

		// controlar modulos
		List<Modulo> modulosDom = rr.getAllModulos();
		boolean existeM = false;
		for (Modulo moduloD : modulosDom) {
			for (Domain moduloNew : allModulos) {
				if (moduloD.getId() == moduloNew.getId()) {
					existeM = true;
				}
			}
			if (!existeM) {
				rr.deleteObject(moduloD);
			}
			existeM = false;
		}

		// controlar formularios
		List<Formulario> formulariosDom = rr.getAllFormulario();
		boolean existeF = false;
		for (Formulario formularioD : formulariosDom) {
			for (Domain formularioNew : allFormularios) {
				if (formularioD.getId() == formularioNew.getId()) {
					existeF = true;
				}
			}
			if (!existeF) {
				rr.deleteObject(formularioD);
			}
			existeF = false;
		}

		// controlar operaciones
		List<Operacion> operacionesDom = rr.getAllOperaciones();
		boolean existeO = false;
		for (Operacion operacionD : operacionesDom) {
			for (Domain operacionNew : allOperaciones) {
				if (operacionD.getId() == operacionNew.getId()) {
					existeO = true;
				}
			}
			if (!existeO) {
				rr.deleteObject(operacionD);
			}
			existeO = false;
		}

		Ping ping = new Ping();
		ping.setEcho("Configuracion modulo modificada: "
				+ System.currentTimeMillis());
		return ping;

	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		// TODO Auto-generated method stub
		ModuloDTO dto = new ModuloDTO();

		List<MyArray> listModArr = getModulos();

		dto.setModulos(listModArr);
		dto.setListaAliasFormularios(listaAliasFormularios);
		dto.setListaAliasOperaciones(listaAliasOperaciones);
		dto.setListaIdTextoOperaciones(listaIdTextoOperaciones);
		dto.setListaNombresModulos(listaNombresModulos);

		return dto;
	}

	public static List<MyArray> getModulos() throws Exception {
		List<MyArray> listModArr = new ArrayList<MyArray>();

		Register rr = Register.getInstance();
		// recorre los modulos
		List<Modulo> listMod = rr.getAllModulos();
		
		listaAliasFormularios = new ArrayList<Object>();
		listaAliasOperaciones = new ArrayList<Object>();
		listaIdTextoOperaciones = new ArrayList<Object>();
		listaNombresModulos = new ArrayList<Object>();


		for (Iterator iterator = listMod.iterator(); iterator.hasNext();) {
			Modulo mod = (Modulo) iterator.next();
			MyArray modArr = new MyArray();
			boolean permitidoMod = true;
			modArr.setId(mod.getId());
			modArr.setPos1(mod.getNombre());
			modArr.setPos2(mod.getDescripcion());
			
			listaNombresModulos.add(mod.getNombre());

			// recorre los formularios de cada modulo
			Set<Formulario> setForm = mod.getFormularios();
			List<MyArray> listFormArr = new ArrayList<MyArray>();

			for (Iterator iterator2 = setForm.iterator(); iterator2.hasNext();) {
				boolean permitidoForm = true;
				Formulario form = (Formulario) iterator2.next();
				MyArray formArr = new MyArray();

				formArr.setId(form.getId());
				formArr.setPos1(form.getLabel());
				formArr.setPos2(form.getDescripcion());
				formArr.setPos3(form.getUrl());
				formArr.setPos4(form.getAlias());	
				
				listaAliasFormularios.add(form.getAlias());	
				
				MyPair formHabilitado = new MyPair();
				if (form.isHabilitado()) {
					formHabilitado.setId(new Long(1));
					formHabilitado.setText("SI");
				} else {
					formHabilitado.setId(new Long(2));
					formHabilitado.setText("NO");
				}
				formArr.setPos5(formHabilitado);

				Set<Operacion> setOper = form.getOperaciones();
				List<MyArray> listOperArr = new ArrayList<MyArray>();
				// recorre las operaciones
				for (Iterator iterator3 = setOper.iterator(); iterator3
						.hasNext();) {
					Operacion oper = (Operacion) iterator3.next();
					MyArray operArr = new MyArray();
					operArr.setId(oper.getId());
					operArr.setPos1(oper.getAlias());
						
					listaAliasOperaciones.add(oper.getAlias());	
					
					operArr.setPos2(oper.getNombre());
					operArr.setPos3(oper.getDescripcion());
					MyPair operHabilitada = new MyPair();
					if (oper.isHabilitado()) {
						operHabilitada.setId(new Long(1));
						operHabilitada.setText("SI");
					} else {
						operHabilitada.setId(new Long(2));
						operHabilitada.setText("NO");
					}
					operArr.setPos4(operHabilitada);
					operArr.setPos5(oper.getIdTexto());
					
					listaIdTextoOperaciones.add(oper.getIdTexto());
								
					operArr.setPos6(oper.getFormulario().getId());
					List listPerfiles = rr.hql(queryPerfil, oper.getId());
					boolean permitido = true;
					if  ((listPerfiles != null) && (listPerfiles.size() > 0)) {
						permitido = false;
						permitidoForm = false;
						permitidoMod = false;
					}
					operArr.setPos7(listPerfiles);
					operArr.setPos8(permitido);
					listOperArr.add(operArr);
				}
				formArr.setPos6(listOperArr);
				formArr.setPos7(permitidoForm);
				listFormArr.add(formArr);
			}
			modArr.setPos3(listFormArr);
			modArr.setPos4(permitidoMod);
			listModArr.add(modArr);
		}
		return listModArr;
	}

	public static void xmain(String[] args) {
		try {
			Register rr = Register.getInstance();
			Modulo m = (Modulo) rr.getObject(Modulo.class.getName(), 1);

			AssemblerModulo ass = new AssemblerModulo();
			ModuloDTO mdto = (ModuloDTO) ass.domainToDto(m);

			System.out.println(mdto.getModulos().size());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
