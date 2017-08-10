package com.coreweb.dto;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.domain.AgendaEvento;
import com.coreweb.domain.Domain;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Register;
import com.coreweb.domain.Tipo;
import com.coreweb.domain.TipoTipo;
import com.coreweb.login.LoginUsuarioDTO;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyAuxObject;
import com.coreweb.util.MyPair;

public abstract class Assembler {

	public static int MY_PAIR = 1;
	public static int MY_ARRAY = 2;
	public static int ASSEMBLER = 3;
	public static int MY_PAIR_TIPO = 4;
	public static int MY_ARRAY_TIPO = 5;

	private Misc m = new Misc();

	private String login = this.getClass().getName();

	private List<Domain> subModel = new ArrayList<Domain>();

	public abstract Domain dtoToDomain(DTO dto) throws Exception;

	public abstract DTO domainToDto(Domain domain) throws Exception;

	public Assembler() {
		try {
			Session s = Sessions.getCurrent();
			String login = ((LoginUsuarioDTO) s.getAttribute(Config.USUARIO)).getLogin();
			this.setLogin(login);
		} catch (Exception ex) {
			this.setLogin("err:" + this.getClass().getName());
		}
	}

//	private static UtilCoreDTO dtoUtil;

	public static void setDtoUtil(UtilCoreDTO dto) {
		Control.setInicialDtoUtil(dto);
//		dtoUtil = dto;
	}

	public static UtilCoreDTO getDtoUtil() {
		return Control.getDtoUtilStatic();
//		return dtoUtil;
	}

	public List<DTO> domainToDtoLista(List<Domain> list) throws Exception {

		List<DTO> out = new ArrayList<DTO>();
		Iterator<Domain> ite = list.iterator();
		while (ite.hasNext()) {
			Domain dom = ite.next();
			DTO dto = this.domainToDto(dom);
			out.add(dto);
		}

		return out;
	}

	public DTO getDTObyId(String entityName, Long idObjeto) throws Exception {
		Register register = Register.getInstance();
		Domain dom = register.getObject(entityName, idObjeto);
		DTO dto = this.domainToDto(dom);
		return dto;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	// *************************************************************************************
	// *************************************************************************************

	public List<MyPair> listaSiNo() {
		List<MyPair> lImpo = new ArrayList<MyPair>();

		MyPair si = new MyPair();
		si.setId(new Long(1));
		si.setText("SI");
		MyPair no = new MyPair();
		no.setId(new Long(2));
		no.setText("NO");
		lImpo.add(si);
		lImpo.add(no);

		return lImpo;

	}

	// *************************************************************************************
	// *************************************************************************************

	public List<Domain> getSubModel() {
		return subModel;
	}

	public void setSubModel(List<Domain> subModel) {
		this.subModel = subModel;
	}

	// *************************************************************************************
	// *************************************************************************************

	/**
	 * obtiene de la DB un objeto según el ID del DTO
	 * 
	 * @param dto
	 * @param classDomain
	 * @return
	 * @throws Exception
	 */
	public Domain getDomain(DTO dto, Class classDomain) throws Exception {
		String ip = "";
		try {
			ip = Executions.getCurrent().getRemoteAddr();
		} catch (Exception e) {
			ip = e.getMessage();
		}

		Domain dom = null;
		if (dto.esNuevo() == true) {
			// crea un nuevo objeto del dominio
			dom = (Domain) newInstance(classDomain);
		} else {
			// busca en la BD el objeto
			Register rr = Register.getInstance();
			dom = rr.getObject(classDomain.getName(), dto.getId());
		}
		dom.setDbEstado(dto.getDbEstado());
		dom.setModificado(new Date());
		dom.setUsuarioMod(dto.getUsuarioMod());
		dom.setAuxi(dto.getAuxi());
		dom.setOrden(dto.getOrden());
		dom.setIp_pc(ip);
		return dom;
	}

	/**
	 * crea un DTO con la id del Domain
	 * 
	 * @param dom
	 * @param classDTO
	 * @return
	 * @throws Exception
	 */
	public DTO getDTO(Domain dom, Class classDTO) throws Exception {
		long id = -1;
		if (dom != null) {
			id = dom.getId();
		}
		DTO dto = (DTO) newInstance(classDTO);
		dto.setId(id);
		dto.setDbEstado(dom.getDbEstado());
		dto.setModificado(dom.getModificado());
		dto.setUsuarioMod(dom.getUsuarioMod());
		dto.setAuxi(dom.getAuxi());
		dto.setOrden(dom.getOrden());
		dto.setIp_pc(dom.getIp_pc() == null ? "---" : dom.getIp_pc());
		return dto;
	}

	// *************************************************************************************
	// *************************************************************************************

	/**
	 * Colpia los valores de los atributos iguales de un objeto a otro
	 * 
	 * @param desde
	 * @param hasta
	 * @param atributos
	 * @throws Exception
	 */
	public void copiarValoresAtributos(Object desde, Object hasta, String[] atributos) throws Exception {

		// System.out.println("-----------------------------------");
		// System.out.println("\n\n desde:"+desde.getClass().getName() +
		// "\n hasta:" + hasta.getClass().getName());
		for (int i = 0; i < atributos.length; i++) {
			String att = atributos[i];

			// System.out.println(""+att);
			// hacer el get
			Object value = getValue(desde, att);

			// hacer el set
			setValue(hasta, att, value);
		}

		// System.out.println("-----------------------------------");
	}

	// *************************************************************************************
	// *************************************************************************************
	public void myPairToDomain(DTO dto, Domain dom, String atributo) throws Exception {
		this.myPairToDomain(dto, dom, atributo, true);

	}

	public void myPairToDomain(DTO dto, Domain dom, String atributo, boolean ignorarNuevo) throws Exception {

		MyPair mp = (MyPair) getValue(dto, atributo);
		if ((mp == null) || ((mp.esNuevo() == true) && (ignorarNuevo == true))) {
			// no tiene nada seteado, entonces retorna
			return;
		}

		myIiDToDomain(mp, dom, atributo);
	}

	private void myIiDToDomain(IiD mp, Domain dom, String atributo) throws Exception, NoSuchFieldException {
		Object value = getValue(dom, atributo);

		// System.out.println("Dom:" + dom + "" + dom.getClass().getName()
		// +" atributo:" + atributo + " value" + value);

		// ver si el objeto del dominio ya tiene el mismo valor seteado
		if (value != null) {
			Domain asso = (Domain) value;
			if (asso.getId().longValue() == mp.getId().longValue()) {
				// es el mismo, no hace nada
				return;
			}
		}

		// recuperar el objeto de la base de la datos y hacer el set
		Register rr = Register.getInstance();
		String entidad = getEntidadAtributo(dom, atributo);
		Domain d = rr.getObject(entidad, mp.getId());
		setValue(dom, atributo, d);

		if (d == null) {
			System.out.println("Error, valor NULO: " + dom.getClass().getName() + " atr:" + atributo);
		} else {
			// se quito para que no grabe siempre
			// rr.saveObject(dom, this.getLogin());
			mp.setId(d.getId());
		}

	}

	public void domainToMyPair(Domain dom, DTO dto, String atributo) throws Exception {
		domainToMyPair(dom, dto, atributo, "descripcion");
	}

	public void domainToMyPair(Domain dom, DTO dto, String atributo, String campo) throws Exception {

		domainToMyPair(dom, dto, atributo, new String[] { campo });
	}

	public void domainToMyPair(Domain dom, DTO dto, String atributo, String[] campos) throws Exception {
		// obtengo el valor del dominio
		Domain dato = (Domain) getValue(dom, atributo);
		if (dato == null) {
			// no tiene nada seteado, entonces termina
			return;
		}

		String sigla = "";
		if (dato instanceof Tipo) {
			Tipo t = (Tipo) dato;
			sigla = t.getSigla();
		}

		String texto = "";
		int cnt = campos.length;
		for (int i = 0; i < cnt; i++) {
			texto += getValue(dato, campos[i]);
			texto += (i < (cnt - 1)) ? " - " : "";
		}

		// setea los valores
		MyPair mp = new MyPair();
		mp.setText(texto);
		mp.setSigla(sigla);
		this.domainToMyAuxObject(dato, mp);
		setValue(dto, atributo, mp);
	}

	public MyPair pasaDomainToMyPair(Domain dom) throws Exception {
		return pasaDomainToMyPair(dom, "descripcion");
	}

	public MyPair pasaDomainToMyPair(Domain dom, String campo) throws Exception {
		// obtengo el valor del dominio
		Object dato = getValue(dom, campo);

		// setea los valores
		MyPair mp = new MyPair();
		mp.setText(dato.toString());
		this.domainToMyAuxObject(dom, mp);

		return mp;
	}

	public MyPair pasaDomainToMyPair(Domain dom, String[] campos) throws Exception {

		String texto = getValue(dom, campos[0]) + "";
		for (int i = 1; i < campos.length; i++) {
			String campo = campos[i];
			// obtengo el valor del dominio
			texto = texto + " - " + getValue(dom, campo) + "";
		}

		// setea los valores
		MyPair mp = new MyPair();
		mp.setText(texto);
		this.domainToMyAuxObject(dom, mp);
		return mp;
	}

	public MyArray pasaDomainToMyArray(Domain dom, String[] campos) throws Exception {

		MyArray mp = new MyArray();
		this.domainToMyAuxObject(dom, mp);
		for (int i = 0; i < campos.length; i++) {
			String campo = campos[i];
			// obtengo el valor del dominio
			Object dato = pasaDomainToDto(getValue(dom, campo));
			setValue(mp, "pos" + (i + 1), dato);
		}

		return mp;
	}

	// NOTA: este método se puede juntar con el de arriba
	public void domainToMyArray(Domain dom, DTO dto, String atributo, String[] campos) throws Exception {
		// obtengo el valor del dominio
		Domain dato = (Domain) getValue(dom, atributo);
		if (dato == null) {
			// no tiene nada seteado, entonces termina
			return;
		}
		MyArray ma = new MyArray();
		this.domainToMyAuxObject(dato, ma);
		for (int i = 0; i < campos.length; i++) {
			String campo = campos[i];
			Object valor = getValue(dato, campo);

			if (valor instanceof Domain) {
				valor = this.createMyPair((Domain) valor, "descripcion");
			}

			setValue(ma, "pos" + (i + 1), valor);
		}
		setValue(dto, atributo, ma);
	}

	public MyArray createMyArray(Domain dom, String[] campos) throws Exception {
		MyArray mp = new MyArray();
		this.domainToMyAuxObject(dom, mp);
		for (int i = 0; i < campos.length; i++) {
			String campo = campos[i];
			Object valor = getValue(dom, campo);
			setValue(mp, "pos" + (i + 1), valor);
		}
		return mp;
	}

	public MyPair createMyPair(Domain dom, String campo) throws Exception {
		MyPair mp = new MyPair();
		this.domainToMyAuxObject(dom, mp);

		Object valor = getValue(dom, campo);
		mp.setText(valor + "");

		return mp;
	}

	public void myArrayToDomain(DTO dto, Domain dom, String atributo) throws Exception {
		myArrayToDomain(dto, dom, atributo, true);
	}

	private void myArrayToDomain(DTO dto, Domain dom, String atributo, boolean ignorarNuevo) throws Exception {

		if (ignorarNuevo == false) {
			String msg = "\n\n\n error, ignorarNuevo no está implementado revisar\n atributo:" + atributo + "\n clase:"
					+ this.getClass().getName() + " \n\n\n";

			System.out.println(msg);
			throw new Exception(msg);
		}

		MyArray mp = (MyArray) getValue(dto, atributo);
		if ((mp == null) || ((mp.esNuevo() == true) && (ignorarNuevo == true))) {
			// no tiene nada seteado, entonces retorna
			return;
		}

		// dr: se quitó por que no grababa la relación
		// myIiDToDomain(mp, dom, atributo);

		Object value = getValue(dom, atributo);

		// ver si el objeto del dominio ya tiene el mismo valor seteado
		if (value != null) {
			Domain asso = (Domain) value;
			if (asso.getId().longValue() == mp.getId().longValue()) {
				// es el mismo, no hace nada
				return;
			}
		}

		// recuperar el objeto de la base de la datos y hacer el set
		Register rr = Register.getInstance();
		String entidad = getEntidadAtributo(dom, atributo);
		Domain d = rr.getObject(entidad, mp.getId());
		setValue(dom, atributo, d);

		// se quito para que no graba todas las veces
		// rr.saveObject(dom, this.getLogin());
		mp.setId(d.getId());

	}

	// *************************************************************************************
	// *************************************************************************************

	public void listaMyPairToListaDomain(DTO dto, Domain dom, String atributo) throws Exception {
		listaObjectToListaDomain(dto, dom, atributo, new String[] { "descripcion" }, false, false, MY_PAIR, null);

	}

	public void listaDomainToListaMyPair(Domain dom, DTO dto, String atributo) throws Exception {
		listaDomainToListaMyPair(dom, dto, atributo, "descripcion");
	}

	public void listaDomainToListaMyPair(Domain dom, DTO dto, String atributo, String campo) throws Exception {

		listaDomainToListaObject(dom, dto, atributo, new String[] { campo }, MY_PAIR, null);
	}

	// *************************************************************************************
	// *************************************************************************************

	// este es el caso en donde la relación era con los ID, es decir, NO se
	// modificaba los
	// los campos de los objetos
	public void listaMyArrayToListaDomain(DTO dto, Domain dom, String atributo) throws Exception {
		String[] arr = {};
		listaMyArrayToListaDomain(dto, dom, atributo, arr, false, false);
	}

	public void listaMyArrayToListaDomain(DTO dto, Domain dom, String atributo, String[] campos, boolean add,
			boolean delete) throws Exception {

		listaObjectToListaDomain(dto, dom, atributo, campos, add, delete, MY_ARRAY, null);

	}

	public void listaDomainToListaMyArray(Domain dom, DTO dto, String atributo, String[] campos) throws Exception {

		listaDomainToListaObject(dom, dto, atributo, campos, MY_ARRAY, null);
	}

	// *************************************************************************************
	// *************************************************************************************

	public void listaDTOToListaDomain(DTO dto, Domain dom, String atributo, boolean add, boolean delete, Assembler ass)
			throws Exception {

		listaObjectToListaDomain(dto, dom, atributo, new String[] {}, add, delete, ASSEMBLER, ass);

	}

	public void listaDomainToListaDTO(Domain dom, DTO dto, String atributo, Assembler ass) throws Exception {

		listaDomainToListaObject(dom, dto, atributo, new String[] {}, ASSEMBLER, ass);
	}

	// *************************************************************************************
	// *************************************************************************************

	public void hijoDtoToHijoDomain(DTO dto, Domain dom, String atributo, Assembler ass, boolean siActualiza)
			throws Exception {

		// se llama al get empresa??
		DTO mp = (DTO) getValue(dto, atributo);
		if (mp == null) {
			// no tiene nada seteado, entonces retorna
			return;
		}
		Register rr = Register.getInstance();
		Domain dd = null;
		if ((mp.esNuevo() == true) || (siActualiza == true)) {
			dd = ass.dtoToDomain(mp);
			rr.saveObject(dd, this.getLogin());
			mp.setId(dd.getId());
		} else {
			String entidad = getEntidadAtributo(dom, atributo);
			dd = rr.getObject(entidad, mp.getId());
		}

		setValue(dom, atributo, dd);
		// quitado, no deber'ia hacer falta
		// rr.saveObject(dom, this.getLogin());
		dto.setId(dom.getId());
	}

	public void hijoDomainToHijoDTO(Domain dom, DTO dto, String atributo, Assembler ass) throws Exception {

		Domain dd = (Domain) getValue(dom, atributo);
		if (dd == null) {
			// no tiene nada seteado, entonces retorna
			return;
		}

		DTO mp = ass.domainToDto(dd);
		setValue(dto, atributo, mp);

	}

	// *************************************************************************************
	// *************************************************************************************

	// permite agregar y borrar en la base de datos
	public void listaObjectToListaDomain(DTO dto, Object dom, String atributo, String[] campos, boolean add,
			boolean delete, int tipo, Assembler ass) throws Exception {

		Class tipoColeccion = getTipoColeccion(dom, atributo);

		// primero obtener las listas
		Collection<Domain> listaDom = (Collection<Domain>) getValue(dom, atributo);
		Collection<IiD> listIiD = (Collection<IiD>) getValue(dto, atributo);

		listaDTOtoListaDomain(listIiD, listaDom, campos, add, delete, tipo, ass, tipoColeccion, null);

	}

	protected void listaDTOtoListaDomain(Collection<IiD> listIiD, Collection<Domain> listaDom, String[] campos,
			boolean add, boolean delete, int tipo, Assembler ass, Class tipoColeccion, TipoTipo tipoTipo)
			throws Exception {

		Register rr = Register.getInstance();

		// hacer una copia de la lista de id que ya estan en el dominio
		// (oldLista)
		List<Domain> oldListaId = new ArrayList<Domain>();
		Iterator<Domain> iteOld = listaDom.iterator();
		while (iteOld.hasNext()) {
			Domain dAux = iteOld.next();
			oldListaId.add(dAux);
		}

		// recorrer la nueva lista de los id del dto y ver si están o no en el
		// dominio
		Iterator<IiD> iteIiD = listIiD.iterator();
		while (iteIiD.hasNext()) {
			Domain dAux = null;
			IiD mp = iteIiD.next();
			boolean esta = false;
			// recorrer la lista del dominio para ver si ya está
			for (int i = 0; i < oldListaId.size(); i++) {
				Domain dIte = oldListaId.get(i);
				if (dIte.getId().longValue() == mp.getId().longValue()) {
					esta = true;
					dAux = dIte; // guarda la referencia
				}
			}

			if (esta == false) {
				// recuperarlo de la BD
				dAux = rr.getObject(tipoColeccion.getName(), mp.getId());
			}

			if ((dAux == null) && (add == false)) {
				throw new Exception("No existe en " + tipoColeccion.getName() + " este id:" + mp.getId() + ".");
			}

			// si no esta en la BD pero hay que agregarlo, entonces primero lo
			// creamos
			if ((dAux == null) && (add == true)) {

				try {
					dAux = (Domain) newInstance(tipoColeccion);
				} catch (InstantiationException ex) {
					DTO dAuxH = (DTO) mp;
					dAux = (Domain) newInstance(dAuxH.getDomainFromDTO());
				}
			}

			if (add == true) {
				// editar los valores del objeto, tratar segun el tipo
				if (tipo == MY_PAIR) {
					// MyPair
					Object vAux = getValue(mp, "text");
					String campo = campos[0];
					setValue(dAux, campo, vAux);

				} else if (tipo == MY_PAIR_TIPO) {
					// MyPair
					Object vAux = getValue(mp, "text");
					String campo = campos[0];
					setValue(dAux, campo, vAux);
					setValue(dAux, "tipoTipo", tipoTipo);

				} else if (tipo == MY_ARRAY_TIPO) {
					// MyPair
					Object vAux1 = getValue(mp, "pos1");
					Object vAux2 = getValue(mp, "pos2");
					String descr = campos[0];
					String sigla = campos[1];
					setValue(dAux, descr, vAux1);
					setValue(dAux, sigla, vAux2);
					setValue(dAux, "tipoTipo", tipoTipo);

				} else if (tipo == MY_ARRAY) {
					// MyArray
					for (int i = 0; i < campos.length; i++) {
						Object vAux = getValue(mp, "pos" + (i + 1));
						// el myarray empieza de 1
						String campo = campos[i];

						if (vAux instanceof IiD) {
							// MyPair o MyArray
							myIiDToDomain((IiD) vAux, dAux, campo);

						} else if (vAux instanceof Collection) {
							// Tratar la coleccion (Lista seguramente)

							Class tipoColeccionAux = getTipoColeccion(dAux, campo);

							// primero obtener las listas
							Collection<Domain> listaDomAux = (Collection<Domain>) getValue(dAux, campo);
							Collection<IiD> listIiDAux = (Collection<IiD>) vAux;

							listaDTOtoListaDomain(listIiDAux, listaDomAux, new String[] { "descripcion" }, add, delete,
									MY_PAIR, null, tipoColeccionAux, null);

						} else {
							// Tipo primitivo (String, long, int... )
							setValue(dAux, campo, vAux);
						}
					}

				} else if (tipo == ASSEMBLER) {
					// Otro DTO, usar parametro Assembler
					DTO dtoAux = (DTO) mp;
					listaDom.remove(dAux);
					dAux = ass.dtoToDomain(dtoAux);
					listaDom.add(dAux);

				} else {
					throw new Exception("ERROR, no se definió el tipo correctamente, tipo: (" + tipo + ") en "
							+ tipoColeccion.getName() + " con id:" + mp.getId() + "");
				}

				rr.saveObject(dAux, this.getLogin());
				mp.setId(dAux.getId());
			}

			// si no estaba lo agregamos a la lista
			if (esta == false) {
				// para que no lo agregue dos veces.
				if (listaDom.contains(dAux) == false) {
					listaDom.add(dAux);
				}

			}
		}

		// recorrer la lista original (oldLista) y quitar los que NO estan el
		// dto
		for (int i = 0; i < oldListaId.size(); i++) {
			Domain dAux = oldListaId.get(i);

			boolean esta = false;
			Iterator<IiD> iteAux = listIiD.iterator();
			while (iteAux.hasNext()) {
				IiD mp = iteAux.next();
				if (mp.getId().longValue() == dAux.getId().longValue()) {
					esta = true;
				}
			}
			if (esta == false) {
				listaDom.remove(dAux);
				if (delete == true) {
					// borrarlo de la DB
					rr.deleteObject(dAux);
				}
			}

		}
	}

	public void listaDomainToListaObject(Domain dom, DTO dto, String atributo, String[] campos, int tipo, Assembler ass)
			throws Exception {

		// primero obtener las listas
		Collection<Domain> listaDom = (Collection<Domain>) getValue(dom, atributo);
		Collection<IiD> listIiD = (Collection<IiD>) getValue(dto, atributo);
		// la lista del DTO debería estar vacia
		if (listIiD.size() > 0) {
			listIiD.removeAll(listIiD);
		}

		Iterator<Domain> iteD = listaDom.iterator();
		while (iteD.hasNext()) {
			Domain d = iteD.next();
			IiD auxIiD = null;

			if (tipo == MY_PAIR) {
				MyPair aux = new MyPair();
				String texto = getValue(d, campos[0]).toString();
				aux.setText(texto);
				this.domainToMyAuxObject(dom, aux);
				auxIiD = aux;

			} else if (tipo == MY_ARRAY) {
				MyArray aux = new MyArray();
				this.domainToMyAuxObject(d, aux);
				for (int i = 0; i < campos.length; i++) {
					String campo = campos[i];
					Object value = getValue(d, campo);
					// los campos del MyArray empiezan del 1
					setValue(aux, "pos" + (i + 1), this.pasaDomainToDto(value));

				}
				auxIiD = aux;

			} else if (tipo == ASSEMBLER) {
				DTO aux = ass.domainToDto(d);
				auxIiD = aux;

			} else {
				throw new Exception("ERROR, no se definió el tipo correctamente, valor: (" + tipo + ")");
			}

			listIiD.add(auxIiD);
		}
	}

	// *************************************************************************************
	// *************************************************************************************

	public void domainToListaMyPair(DTO dto, String atributo, String entidad) throws Exception {
		domainToLista(dto, atributo, entidad, new String[] { "descripcion" }, MY_PAIR);
	}

	public void domainToListaMyArray(DTO dto, String atributo, String entidad, String[] campos) throws Exception {
		domainToLista(dto, atributo, entidad, campos, MY_ARRAY);
	}

	private void domainToLista(DTO dto, String atributo, String entidad, String[] campos, int tipo) throws Exception {

		List<IiD> datos = new ArrayList<IiD>();
		Register rr = Register.getInstance();
		List<Domain> lDom = (List<Domain>) rr.getObjects(entidad);
		for (int i = 0; i < lDom.size(); i++) {
			Domain dom = lDom.get(i);
			IiD dato = null;
			if (tipo == MY_PAIR) {
				MyPair mp = new MyPair();
				mp.setId(dom.getId());
				mp.setText(getValue(dom, campos[0]) + "");
				this.domainToMyAuxObject(dom, mp);
				
				dato = mp;
			} else if (tipo == MY_ARRAY) {
				MyArray ma = new MyArray();
				this.domainToMyAuxObject(dom, ma);
				for (int j = 0; j < campos.length; j++) {
					String campo = campos[j];
					// setValue(ma, "pos"+(j+1), getValue(dom, campo));
					Object valor = getValue(dom, campo);

					setValue(ma, "pos" + (j + 1), this.pasaDomainToDto(valor));

				}
				dato = ma;
			}

			datos.add(dato);
		}
		// setea el DTO
		setValue(dto, atributo, datos);

	}

	private Object pasaDomainToDto(Object valor) throws Exception {
		Object out = null;

		if (valor instanceof Tipo) {
			Tipo d = (Tipo) valor;
			MyPair mp = this.tipoToMyPair(d);
			out = mp;
		} else if (valor instanceof Domain) {
			Domain d = (Domain) valor;
			MyPair mp = this.pasaDomainToMyPair(d);
			out = mp;

		} else if (valor instanceof Collection) {
			List<MyPair> lMp = new ArrayList<MyPair>();

			Collection<Domain> setDom = (Collection<Domain>) valor;
			Iterator<Domain> ite = setDom.iterator();
			while (ite.hasNext()) {
				Domain d = ite.next();
				MyPair mp = this.pasaDomainToMyPair(d);
				lMp.add(mp);
			}
			out = lMp;

		} else {
			out = valor;
		}
		return out;

	}

	// *********************************************************************
	// Tipo Tipo

	public List<MyPair> getTipo(String tipoTipo) throws Exception {
		List<MyPair> lis = new ArrayList<MyPair>();
		String hql = "from Tipo t where t.tipoTipo.descripcion = '" + tipoTipo + "'";
		Register rr = Register.getInstance();
		List l = rr.hql(hql);
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			Tipo dom = (Tipo) iterator.next();
			MyPair mp = this.pasaDomainToMyPair(dom);
			lis.add(mp);
		}
		return lis;
	}

	public MyPair getTipo(String tipoTipo, long id) throws Exception {
		MyPair out = null;
		String hql = "from Tipo t where t.tipoTipo.descripcion = '" + tipoTipo + "'  and t.id = " + id;
		Register rr = Register.getInstance();
		List l = rr.hql(hql);
		Tipo dom = (Tipo) l.get(0);
		out = this.pasaDomainToMyPair(dom);
		out.setSigla(dom.getSigla());
		return out;
	}

	public MyPair getTipo(long id) throws Exception {
		MyPair out = null;
		String hql = "from Tipo t where  t.id = " + id;
		Register rr = Register.getInstance();
		List l = rr.hql(hql);
		Tipo dom = (Tipo) l.get(0);
		out = this.pasaDomainToMyPair(dom);
		out.setSigla(dom.getSigla());
		return out;
	}

	public MyPair getTipo(String tipoTipo, String desc) throws Exception {
		MyPair out = null;
		String hql = "from Tipo t where t.tipoTipo.descripcion = '" + tipoTipo + "'  and t.descripcion = '" + desc
				+ "'";

		Register rr = Register.getInstance();
		List l = rr.hql(hql);
		Tipo dom = (Tipo) l.get(0);
		out = this.pasaDomainToMyPair(dom);
		out.setSigla(dom.getSigla());
		return out;
	}

	public MyPair getTipoTipo(String tipoTipo) throws Exception {
		MyPair out = null;
		String hql = "from TipoTipo t where t.descripcion = '" + tipoTipo + "'";
		Register rr = Register.getInstance();
		List l = rr.hql(hql);
		TipoTipo dom = (TipoTipo) l.get(0);
		out = this.pasaDomainToMyPair(dom);
		return out;

	}

	public Tipo myPairToTipo(MyPair p) throws Exception {
		Register rr = Register.getInstance();
		Tipo t = (Tipo) rr.getObject(Tipo.class.getName(), p.getId());
		return t;
	}

	// **********************************************************************

	// Para que un error en algun maping de atributos no haga que deje de
	// funcionar otras partes...
	public void utilDomainToListaMyPair(DTO dto, String atributo, String entidad) {
		try {
			this.domainToListaMyPair(dto, atributo, entidad);
		} catch (Exception e) {
			System.out.println("[Error] utilDomainToListaMyPair: atributo " + atributo + " " + entidad);
			e.printStackTrace();
		}
	}

	public void utilDomainToListaMyArray(DTO dto, String atributo, String entidad, String[] campos) {
		try {
			domainToListaMyArray(dto, atributo, entidad, campos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("[Error] utilDomainToListaMyArray: atributo " + atributo + " " + entidad);
			e.printStackTrace();
		}
	}

	public void listaMyPairToListaDomain(List<MyPair> list, Class class1) throws Exception {
		Register rr = Register.getInstance();
		List<Domain> listDom = rr.getObjects(class1.getName());
		List<IiD> listIiD = new ArrayList<IiD>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			MyPair mp = (MyPair) iterator.next();
			listIiD.add(mp);
		}

		listaDTOtoListaDomain(listIiD, listDom, new String[] { "descripcion" }, true, true, MY_PAIR, null, class1,
				null);

	}

	public void listaMyPairToListaDomainTipo(List<MyPair> list, String tipoTipo) throws Exception {
		Object l = list;
		List<IiD> l2 = (List<IiD>) l;

		listaIiDToListaDomainTipo(l2, MY_PAIR_TIPO, tipoTipo, new String[] { "descripcion" });
	}

	public void listaMyArrayToListaDomainTipo(List<MyArray> list, String tipoTipo) throws Exception {
		Object l = list;
		List<IiD> l2 = (List<IiD>) l;

		listaIiDToListaDomainTipo(l2, MY_ARRAY_TIPO, tipoTipo, new String[] { "descripcion", "sigla" });
	}

	private void listaIiDToListaDomainTipo(List<IiD> list, int tipo, String tipoTipo, String[] campos)
			throws Exception {
		Register rr = Register.getInstance();

		String hqltt = "Select tt from TipoTipo tt where tt.descripcion = ?";
		TipoTipo tt = (TipoTipo) rr.hqlToObject(hqltt, tipoTipo);

		String hql = "Select t from Tipo t where t.tipoTipo.descripcion = '" + tipoTipo + "'";
		List<Domain> listDom = rr.hql(hql);

		List<IiD> listIiD = new ArrayList<IiD>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			IiD mp = (IiD) iterator.next();
			listIiD.add(mp);
		}

		listaDTOtoListaDomain(listIiD, listDom, campos, true, true, tipo, null, Tipo.class, tt);
	}

	public void listaMyArrayToListaDomain(List<MyArray> list, Class class1, String[] campos) throws Exception {
		Register rr = Register.getInstance();
		List<Domain> listDom = rr.getObjects(class1.getName());
		List<IiD> listIiD = new ArrayList<IiD>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			MyArray mp = (MyArray) iterator.next();
			listIiD.add(mp);
		}

		listaDTOtoListaDomain(listIiD, listDom, campos, true, true, MY_ARRAY, null, class1, null);

	}

	public DTO getDto(String entityName, IiD id) throws Exception {
		Register r = Register.getInstance();
		Domain d = r.getObject(entityName, id.getId());
		DTO dto = this.domainToDto(d);
		return dto;
	}

	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************

	// obtiene la entidad de un atributo
	private String getEntidadAtributo(Object dom, String atributo) throws NoSuchFieldException {
		Field fd = this.getField(dom.getClass(), atributo);
		String entidad = fd.getType().getName();
		return entidad;
	}

	// hace un get de un atributo, si no puede prueba con la super clase (sólo
	// una)
	private Object getValue(Object obj, String att) throws Exception {

		Method m = new PropertyDescriptor(att, obj.getClass()).getReadMethod();

		Object v = m.invoke(obj);
		/*
		 * if ((v == null)&&(m.getReturnType().isPrimitive() == true)){ v =
		 * m.getReturnType().newInstance(); }
		 * 
		 * if ((v == null)&&(IiD.class.isAssignableFrom(m.getReturnType()) ==
		 * false )){ v = m.getReturnType().newInstance(); }
		 */
		return v;

		// Field fd = getField(obj.getClass(), att);
		// return fd.get(obj);
	}

	private Field getField(Class clase, String att) {
		// System.out.println(clase.getName());
		Field out = null;
		try {
			out = clase.getDeclaredField(att);
			out.setAccessible(true);
		} catch (NoSuchFieldException e) {
			out = getField(clase.getSuperclass(), att);
		}
		return out;
	}

	// hace un set de un atributo, si no puede prueba con la super clase (sólo
	// una)
	private void setValue(Object obj, String att, Object value) throws Exception {

		Object vv = value;
		if (vv != null) {
			vv = vv.getClass().getName() + "" + vv;
		}

		// System.out.println("\n\n\n------------------------\n clase:
		// "+obj.getClass().getName()+" att:"+att+" value:"+vv+"\n\n\n\n");

		try {
			new PropertyDescriptor(att, obj.getClass()).getWriteMethod().invoke(obj, value);
		} catch (Exception e) {
			// si error no hace nada
		}

		// Field fd = getField(obj.getClass(), att);
		// fd.set(obj, value);
	}

	// obtiene el tipo generico de una coleccion
	private Class getTipoColeccion(Object obj, String att) throws Exception {
		Field fd = this.getField(obj.getClass(), att);
		fd.setAccessible(true);
		ParameterizedType tipoGenerico = (ParameterizedType) fd.getGenericType();
		Class<?> classLista = (Class<?>) tipoGenerico.getActualTypeArguments()[0];

		return classLista;
	}

	public Object newInstance(Class classObject) throws Exception {
		Constructor ctor = classObject.getConstructor();
		Object obj = ctor.newInstance();
		return obj;
	}

	
	//================================================================
	
	public Domain setMyAuxObjectToDomain(Class entityName, MyAuxObject aux){
		Domain d = null;
		Register r = Register.getInstance();
		try {
			d = r.getObject(entityName.getName(), aux.getId());
		} catch (Exception e) {
			// si no existe, o crea
			try {
				d = (Domain) this.newInstance(entityName);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		this.myAuxObjectToDomain(aux, d);
		return d;
	}
	
	public Tipo setMyPairToTipo(MyPair mp){
		return (Tipo) this.setMyAuxObjectToDomain(Tipo.class, mp);
	}
	
	
	public void domainToMyAuxObject (Domain d, MyAuxObject aux){
		aux.setId(d.getId());
		aux.setAuxi(d.getAuxi());
		aux.setOrden(d.getOrden());
	}

	public void myAuxObjectToDomain(MyAuxObject aux, Domain d){
		d.setAuxi(aux.getAuxi());
		d.setOrden(aux.getOrden());
		d.setId(aux.getId());
	}
	
	
	/*
	 * ********************** TIPO
	 * *******************************************************
	 * ******************
	 * *****************************************************************
	 */

	public MyPair tipoToMyPair(Tipo tipo) {
		if (tipo == null) {
			return null;
		}
		MyPair aux = new MyPair();
		aux.setText(tipo.getDescripcion());
		aux.setSigla(tipo.getSigla());
		this.domainToMyAuxObject(tipo, aux);
		return aux;
	}

	public MyArray tipoToMyArray(Tipo tipo) {
		if (tipo == null) {
			return null;
		}
		MyArray aux = new MyArray();
		this.domainToMyAuxObject(tipo, aux);
		aux.setPos1(tipo.getDescripcion());
		aux.setPos2(tipo.getSigla());
		return aux;
	}

	public List<MyPair> listaTiposToListaMyPair(List<Tipo> tipos) {
		List<MyPair> out = new ArrayList<MyPair>();
		for (Tipo t : tipos) {
			MyPair m = this.tipoToMyPair(t);
			out.add(m);
		}
		return out;
	}

	public List<MyArray> listaTiposToListaMyArray(List<Tipo> tipos) {
		List<MyArray> out = new ArrayList<MyArray>();
		for (Tipo t : tipos) {
			MyArray m = this.tipoToMyArray(t);
			out.add(m);
		}
		return out;
	}

	/**********************************************/


	public Misc getM() {
		return m;
	}

	public void setM(Misc m) {
		this.m = m;
	}

	/*
	 * public static void main(String[] args) { try { C c = new C();
	 * 
	 * System.out.println(Assembler.getValue(c, "datoA")); Assembler.setValue(c,
	 * "datoA", "dd"); System.out.println(Assembler.getValue(c, "datoA"));
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */

}

/*
 * class A { private String datoA = "aa";
 * 
 * public String getDatoA() { return datoA; }
 * 
 * public void setDatoA(String datoA) { this.datoA = datoA; }
 * 
 * }
 * 
 * class B extends A{ private String datoB = "bb";
 * 
 * public String getDatoB() { return datoB; }
 * 
 * public void setDatoB(String datoB) { this.datoB = datoB; }
 * 
 * }
 * 
 * class C extends B{ private String datoC = "cc";
 * 
 * public String getDatoC() { return datoC; }
 * 
 * public void setDatoC(String datoC) { this.datoC = datoC; }
 * 
 * }
 */