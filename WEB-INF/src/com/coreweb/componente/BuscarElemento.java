package com.coreweb.componente;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import com.coreweb.Config;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.browser.Browser;
import com.coreweb.extras.browser.ColumnaBrowser;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;

public class BuscarElemento implements VerificaAceptarCancelar {

	Class clase;
	Assembler assembler;
	String[] atributos;
	String[] nombresColumnas;
	String[] componentes;
	String[] ancho;
	boolean anchoAsignado = false;
	String[] valores;
	String[] tipos = null;
	String join = "";
	String orden = "";
	String titulo = "Buscar ...";
	String width = "400px";
	String height = "400px";
	String msgVacia = "Ingrese un criterio de filtro ...";
	boolean continuaSiHayUnElemento = true;
	boolean multiSelected = false;
	boolean sinLimite = false;
	boolean renderAll = false;
	List<String> where = new ArrayList<String>();

	Misc m = new Misc();

	// para los casos que sea uno solo
	boolean unDatoAceptar = false;
	Object[] unDato;

	Listbox listbox = new Listbox();

	BodyPopupAceptarCancelar bpac = null;

	public void show(String dato) throws Exception {
		this.show(dato, 0);
	}

	/**
	 * @param dato
	 *            : el string con la información para filtrar
	 * @param posFiltro
	 *            : la posición del arreglo donde hay que filtrar. 0 (cero) es
	 *            la primer posición.
	 * @throws Exception
	 */
	public void show(String dato, int posFiltro) throws Exception {

		posFiltro += 1; // en el 0 está el ID

		// carga los tipos, por defecto String
		if (this.tipos == null) {
			this.tipos = new String[this.atributos.length];
			for (int i = 0; i < this.atributos.length; i++) {
				this.tipos[i] = ""; // toma por defecto String
			}
		}

		try {
			valores[posFiltro] = dato.trim();
			List<Object[]> datos = this.getModelo();
			if ((datos.size() == 1) && (this.continuaSiHayUnElemento == true)) {
				this.unDatoAceptar = true;
				this.unDato = datos.get(0);
				this.m.mensajePopupTemporal("Un registro encontrado", 1000);
				return;
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			if (msg.compareTo(Config.STR_ERROR_BUSCAR_ELEMENTO) == 0) {
				// nada
			} else {
				System.out.println(msg);
			}
		}

		if (this.multiSelected == true) {
			// DR parametro
			this.listbox.setMultiple(true);
			this.listbox.setCheckmark(true);
		}

		this.listbox.setEmptyMessage(this.msgVacia);
		this.listbox.setSclass("small-list");
		this.listbox.setStyle("margin:5px");
		this.listbox.setHeight("350px");

		// Modelo
		ListModelList model = new ListModelList(new ArrayList());
		model.setMultiple(this.multiSelected);
		listbox.setModel(model);

		// Los filtros
		List<Textbox> listFiltros = new ArrayList<Textbox>(); // para recuperar
																// los valores
		Auxhead ah = new Auxhead();
		listbox.getChildren().add(ah);

		if (this.multiSelected == true) {
			Auxheader ahc = new Auxheader();
			ahc.setWidth("32px");
			ah.getChildren().add(ahc);
		}

		for (int i = 0; i < valores.length; i++) {
			Auxheader ahc = new Auxheader();
			Textbox ahcT = new Textbox();
			ahcT.setHflex("1");
			ahc.getChildren().add(ahcT);
			ah.getChildren().add(ahc);
			listFiltros.add(ahcT);

			FiltroBuscarElementoEvento ev = new FiltroBuscarElementoEvento(this, listFiltros);
			ahcT.addEventListener(Events.ON_OK, ev);
			// quitamos el doble click, por que era molesto cuando uno queria
			// selecionar, además sólo es necesario el enter en la cabecera
			ahcT.addEventListener(Events.ON_DOUBLE_CLICK, ev);

			if (i == 0) {
				ahcT.setDisabled(true);
			}
			if (i == posFiltro) {
				ahcT.setValue(valores[posFiltro]); // el dato que viene como
													// parámetro
				ahcT.setFocus(true);
				ahcT.focus();
			}

		}

		// Los headers
		Listhead lh = new Listhead();
		listbox.getChildren().add(lh);

		if (this.multiSelected == true) {
			Listheader lhc = new Listheader();
			lhc.setWidth("32px");
			lh.getChildren().add(lhc);
		}

		for (int i = 0; i < nombresColumnas.length; i++) {
			String colName = nombresColumnas[i];
			Listheader lhc = new Listheader();
			lhc.setLabel(colName);
			if (this.anchoAsignado == true) {
				lhc.setWidth(this.ancho[i]);
			}

			lh.getChildren().add(lhc);

			if (i == 0) {
				// es el ID
				lhc.setWidth("0px");
				lhc.setVisible(false);
			}
		}

		this.refreshModeloListbox();

		listbox.setItemRenderer(new MyListitemRenderer(this.multiSelected, componentes));

		if (this.renderAll == true) {
			listbox.renderAll();
		}

		/*
		 * listbox.setItemRenderer(new ListitemRenderer() {
		 * 
		 * @Override public void render(Listitem listItem, Object data, int
		 * arg2) throws Exception { listItem.setValue(data);
		 * 
		 * Object[] row = (Object[]) data;
		 * 
		 * for (int i = 0; i < row.length; i++) { Object va = row[i]; String d =
		 * ""; if (va != null) { d = va.toString(); } new
		 * Listcell(d).setParent(listItem); }
		 * 
		 * } });
		 */
		this.showAgain();

	}

	public boolean isMultiSelected() {
		return multiSelected;
	}

	public void setMultiSelected(boolean multiSelected) {
		this.multiSelected = multiSelected;
	}

	public void showAgain() {
		bpac = new BodyPopupAceptarCancelar();
		ListboxEventListener ev = new ListboxEventListener(bpac, this);
		listbox.addEventListener(Events.ON_DOUBLE_CLICK, ev);
		listbox.addEventListener(Events.ON_OK, ev);

		// hace los calculos para que quede el scroll del listbox y no del
		// windows
		this.setAnchoAlto(bpac, listbox, this.width, this.height);

		// Para centrar el listbox
		Hbox hb = new Hbox();
		hb.setWidth(bpac.getWidthWindows());
		hb.setHeight("100%");
		hb.setPack("center");
		hb.setAlign("start");
		hb.getChildren().add(listbox);
		/*
		 * Hlayout hl = new Hlayout(); hl.setWidth(bpac.getWidthWindows());
		 * hl.setValign("middle"); hl.getChildren().add(listbox);
		 */
		bpac.setCheckAC(this);
		bpac.addComponente("Buscar", hb);
		bpac.showPopupUnaColumna(this.titulo);

	}

	private void setAnchoAlto(BodyPopupAceptarCancelar bpac, Listbox listbox, String w, String h) {
		int ancho = Integer.parseInt(w.substring(0, w.length() - 2));
		int alto = Integer.parseInt(h.substring(0, h.length() - 2));

		// listbox.setWidth(w);
		// listbox.setHeight(h);
		listbox.setHflex("true");
		listbox.setVflex(true);
		bpac.setWidthWindows((ancho + 30) + "px");
		bpac.setHighWindows((alto + 100) + "px");

	}

	protected void refreshModeloListbox() throws Exception {

		List<Object[]> datos = new ArrayList<Object[]>();
		String msg = this.msgVacia;
		try {
			datos = this.getModelo();
			msg = "Elementos encotrados: " + datos.size() + " - " + msg;

		} catch (Exception e) {
			// e.printStackTrace();
			msg = e.getMessage();
		}

		this.listbox.setEmptyMessage(msg);
		ListModelList model = new ListModelList(datos);
		model.setMultiple(this.multiSelected);
		this.listbox.setModel(model);
		this.listbox.setFocus(true);
		if (datos.size() > 0) {
			if (this.multiSelected == false) {
				this.listbox.setSelectedIndex(0);
			}
		}

		if (this.renderAll == true) {
			listbox.renderAll();
		}
	}

	private List<Object[]> getModelo() throws Exception {
		Register rr = Register.getInstance();
		List<Object[]> datos = new ArrayList<Object[]>();
		// datos = (List<Object[]>) rr.buscarElemento(clase,atributos, valores,
		// tipos, where);
		datos = (List<Object[]>) rr.buscarElemento(clase, atributos, valores, tipos, false, !this.sinLimite,
				Config.CUANTOS_BUSCAR_ELEMENTOS, true, where, this.join, this.orden);
		return datos;
	}

	public Class getClase() {
		return clase;
	}

	public void setClase(Class clase) {
		this.clase = clase;
	}

	public String[] getAtributos() {
		return atributos;
	}

	public void setAtributos(String[] att) {
		// agrego el ID para todos los casos
		this.atributos = new String[att.length + 1];
		this.valores = new String[att.length + 1];

		this.atributos[0] = "id";
		this.valores[0] = "";

		for (int i = 1; i < this.atributos.length; i++) {
			this.atributos[i] = att[i - 1];
			valores[i] = "";
		}

	}

	public String[] getNombresColumnas() {
		return nombresColumnas;
	}

	public void setNombresColumnas(String[] nomCol) {
		// agrego el ID para todos los casos
		this.nombresColumnas = new String[nomCol.length + 1];

		this.nombresColumnas[0] = "Id";

		for (int i = 1; i < this.nombresColumnas.length; i++) {
			this.nombresColumnas[i] = nomCol[i - 1];
		}

	}

	public void setAnchoColumnas(String[] ancho) {
		this.anchoAsignado = true;
		this.ancho = new String[ancho.length + 1];

		this.ancho[0] = "0px";

		for (int i = 1; i < this.ancho.length; i++) {
			this.ancho[i] = ancho[i - 1];
		}

	}

	public boolean isClickAceptar() {
		if (this.multiSelected == false) {
			return ((this.unDatoAceptar == true) || ((this.getSelectedItem() != null) && (this.bpac.clickAceptar)));
		}
		// es selected multible
		Set<Listitem> sli = this.listbox.getSelectedItems();
		if ((sli != null) && (sli.size() > 0)) {
			return true;
		}

		return false;

	}

	public MyArray getSelectedItem() {
		// para el caso que es un solo elemento
		if (this.unDatoAceptar == true) {
			MyArray ma = new MyArray(this.unDato);
			return ma;
		}

		Listitem li = this.listbox.getSelectedItem();
		if (li == null) {
			return null;
		}
		Object[] dato = (Object[]) li.getValue();
		MyArray ma = new MyArray(dato);
		return ma;
	}

	public List<MyArray> getMultiSelectedItems() {
		List<MyArray> out = new ArrayList<>();

		Set<Listitem> sli = this.listbox.getSelectedItems();

		for (Listitem li : sli) {
			if (li != null) {
				Object[] dato = (Object[]) li.getValue();
				MyArray ma = new MyArray(dato);
				System.out.println("El id del objeto seleccionado es: " + ma.getId());
				out.add(ma);
			}
		}

		return out;
	}

	public DTO getSelectedItemDTO() throws Exception {
		// para el caso que es un solo elemento
		if (this.unDatoAceptar == true) {
			long id = (long) this.unDato[0];
			return this.getDto(id);
		}

		Listitem li = this.listbox.getSelectedItem();
		if (li == null) {
			return null;
		}
		Object[] dato = (Object[]) li.getValue();
		long id = (long) dato[0];
		return this.getDto(id);
	}

	public List<DTO> getMultiSelectedItemDTO() throws Exception {
		// para el caso que es un solo elemento
		List<DTO> out = new ArrayList<>();

		Set<Listitem> sli = this.listbox.getSelectedItems();

		for (Listitem li : sli) {
			if (li != null) {
				Object[] dato = (Object[]) li.getValue();
				long id = (long) dato[0];
				DTO dto = this.getDto(id);
				out.add(dto);
			}
		}
		return out;

	}

	private DTO getDto(long id) throws Exception {
		Register rr = Register.getInstance();
		Domain dom = rr.getObject(this.clase.getName(), id);
		DTO dto = this.assembler.domainToDto(dom);
		return dto;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String[] getValores() {
		return valores;
	}

	public void setValores(String[] valores) {
		this.valores = valores;
	}

	public Listbox getListbox() {
		return listbox;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public Assembler getAssembler() {
		return assembler;
	}

	public void setAssembler(Assembler assembler) {
		this.assembler = assembler;
	}

	public void addWhere(String w) {
		this.where.add(w);
	}

	public void addJoin(String join) {
		this.join = join;
	}

	public void addOrden(String orden) {
		this.orden = orden;
	}

	public String[] getTipos() {
		return tipos;
	}

	public void setTipos(String[] tipos) {
		// agrego el ID para todos los casos
		this.tipos = new String[tipos.length + 1];

		this.tipos[0] = Config.TIPO_NUMERICO;

		for (int i = 1; i < this.tipos.length; i++) {
			this.tipos[i] = tipos[i - 1];
		}

	}

	String textoError = "";

	@Override
	public boolean verificarAceptar() {
		textoError = "";

		if (this.getSelectedItem() == null) {
			textoError = "Debe seleccionar un item!";
			return false;
		}

		return true;
	}

	@Override
	public String textoVerificarAceptar() {
		// TODO Auto-generated method stub
		return textoError;
	}

	@Override
	public boolean verificarCancelar() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String textoVerificarCancelar() {
		// TODO Auto-generated method stub
		return "";
	}

	public boolean isContinuaSiHayUnElemento() {
		return continuaSiHayUnElemento;
	}

	public void setContinuaSiHayUnElemento(boolean continuaSiHayUnElemento) {
		this.continuaSiHayUnElemento = continuaSiHayUnElemento;
	}

	public boolean isSinLimite() {
		return sinLimite;
	}

	public void setSinLimite(boolean sinLimite) {
		this.sinLimite = sinLimite;
	}

	public boolean isRenderAll() {
		return renderAll;
	}

	public void setRenderAll(boolean renderAll) {
		this.renderAll = renderAll;
	}

	public String[] getComponentes() {
		return componentes;
	}

	public void setComponentes(String[] comp) {

		// agrego el ID para todos los casos
		this.componentes = new String[comp.length + 1];

		this.componentes[0] = "";

		for (int i = 1; i < this.componentes.length; i++) {
			this.componentes[i] = comp[i - 1];
		}

	}

}

class FiltroBuscarElementoEvento implements EventListener {

	List<Textbox> listTx = null;
	BuscarElemento be = null;

	public FiltroBuscarElementoEvento(BuscarElemento be, List<Textbox> listTx) {
		this.be = be;
		this.listTx = listTx;
	}

	@Override
	public void onEvent(Event ev) throws Exception {
		// TODO Auto-generated method stub

		if (ev.getName().compareTo(Events.ON_DOUBLE_CLICK) == 0) {
			// de esta forma filtro el doble click de la cabecera, para que no
			// lo capture el del listbox
			return;
		}

		String[] valores = new String[listTx.size()];
		for (int i = 0; i < listTx.size(); i++) {
			Textbox tx = (Textbox) listTx.get(i);
			valores[i] = tx.getValue().trim();
		}
		this.be.setValores(valores);
		this.be.refreshModeloListbox();

	}

}

class ListboxEventListener implements EventListener {

	BodyPopupAceptarCancelar bpac;
	BuscarElemento be;

	public ListboxEventListener(BodyPopupAceptarCancelar bpac, BuscarElemento be) {
		this.bpac = bpac;
		this.be = be;
	}

	@Override
	public void onEvent(Event ev) throws Exception {

		if (this.be.getSelectedItem() == null) {
			// para que no se cierre en el doble click o enter, que si o si haya
			// seleccionado algo
			return;
		}

		this.bpac.getControlVM().aceptar();
	}

}

class MyListitemRenderer implements ListitemRenderer {

	BrowserRender br = new BrowserRender();
	String[] componentes = {};

	boolean isMulti = false;

	public MyListitemRenderer(boolean isMultiSelected) {
		this.isMulti = isMultiSelected;
	}

	public MyListitemRenderer(boolean isMultiSelected, String[] componentes) {
		this.isMulti = isMultiSelected;
		this.componentes = componentes;
	}

	@Override
	public void render(Listitem listItem, Object data, int arg2) throws Exception {
		listItem.setValue(data);

		Object[] row = (Object[]) data;

		if (this.componentes == null || this.componentes.length == 0) {
			this.componentes = new String[row.length];
		}

		if (this.isMulti == true) {
			new Listcell().setParent(listItem);

		}

		for (int i = 0; i < row.length; i++) {
			Object va = row[i];
			String compStr = componentes[i];

			if ((va == null) || (compStr == null) || (compStr.trim().length() == 0)) {
				compStr = Browser.LABEL;
			}

			HtmlBasedComponent comp = null;
			Method m = this.br.getClass().getMethod(compStr, Object.class, Object[].class);
			comp = (HtmlBasedComponent) m.invoke(br, va, null);

			// String d = "";
			// if (va != null) {
			// d = va.toString();
			// }
			Listcell lc = new Listcell();
			
			if (compStr.compareTo(Browser.LABEL_GS)==0){
				
			}
			
			comp.setParent(lc);

			lc.setParent(listItem);
		}

	}

}

class BrowserRender extends Browser {

	@Override
	public List<ColumnaBrowser> getColumnasBrowser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class getEntidadPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setingInicial() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTituloBrowser() {
		// TODO Auto-generated method stub
		return null;
	}

}