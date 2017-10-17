package com.coreweb.extras.browser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.impl.InputElement;

import com.coreweb.Config;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.componente.VerificaAceptarCancelar;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.domain.Register;
import com.coreweb.domain.Tipo;
import com.coreweb.templateABM.Toolbar;
import com.coreweb.util.MyArray;

public abstract class Browser extends SimpleViewModel implements
		VerificaAceptarCancelar {

	@Init(superclass = true)
	public void initBrowser() {

	}

	@AfterCompose(superclass = true)
	public void afterComposeBrowser() {
	}

	public static String LABEL = "getLabel";
	public static String LABEL_NUMERICO = "getLabelNumerico";
	public static String LABEL_GS = "getLabelGs";
	public static String TEXT_BOX = "getTextbox";
	public static String LONG_BOX = "getLongbox";
	public static String CHECK_BOX = "getCheckbox";
	public static String RADIOGROUP = "getRadiogroup";
	public static String RADIO = "getRadio";
	
	// se llama label, pero es un Date lo que retorna
	public static String LABEL_DATE = "getLabelDate";
	public static String LABEL_DATE_SIMPLE = "getLabelDateSimple";

	public static String LABEL_TIPO = "getLabelTipo";

	
	/*
	 * public static String TIPO_STRING = "String"; public static String
	 * TIPO_NUMERICO = "Numerico"; public static String TIPO_BOOL = "Bool";
	 * public static String TIPO_DATE = "Date";
	 */

	// la info de olas columnas del browser
	public abstract List<ColumnaBrowser> getColumnasBrowser();

	// la clase principal de busqueda
	public abstract Class getEntidadPrincipal();

	// para inicializar valores
	public abstract void setingInicial();

	public abstract String getTituloBrowser();

	private String widthWindows = Config.ANCHO_APP;
	private String higthWindows = "400px";

	private Grid grid = new Grid();
	private Class clase;

	private boolean checkVisible = false;

	// estos datos se inicializan con el metodo cargarColumnas()
	List<ColumnaBrowser> columnas; // la informacion de las columnas

	// usados por el Register para armar el HQL
	// private String[] nombres; // nombre de la columna
	private String[] atributos; // atributo de la clase
	private String[] valores;
	private String[] tipos; // los tipos de los campos
	private String[] wheres;
	private String join = "";
	private String attOrden = "";
	private int limit = 0;

	public static Object[] NO_SELECTED_ITEM = { -1 };

	private Object[] selectedItem = NO_SELECTED_ITEM;
	private Row selectedRow = new Row();
	private Row selectedRowPrevio = new Row();
	private String styleSepectedRowOriginal = "";
	public static String STYLE_SELECTED_ROW = "background-color: #DAE7F6;";

	Radiogroup rg = new Radiogroup();
	BodyPopupAceptarCancelar bpac = new BodyPopupAceptarCancelar();

	private void cargarColumnas() {
		this.clase = this.getEntidadPrincipal();

		// una ColumnaBrowser para el ID
		ColumnaBrowser id = new ColumnaBrowser();
		id.setTitulo("Id");
		id.setCampo("id");
		id.setTipo(Config.TIPO_NUMERICO);

		// Columnas del browser + la columnad del ID
		this.columnas = this.getColumnasBrowser();
		this.columnas.add(0, id);

		int numeroColumnas = this.columnas.size(); // por el id
		// usados por el Register para armar HQL
		this.atributos = new String[numeroColumnas];
		this.valores = new String[numeroColumnas];
		// this.nombres = new String[numeroColumnas];
		this.wheres = new String[numeroColumnas];
		this.tipos = new String[numeroColumnas];

		for (int i = 0; i < numeroColumnas; i++) {
			ColumnaBrowser col = this.columnas.get(i);

			// nombres[i] = col.getTitulo();
			atributos[i] = col.getCampo();
			valores[i] = col.getValue();
			wheres[i] = col.getWhere();
			tipos[i] = col.getTipo();
		}
	}

	public void show() throws Exception {

		// seteos iniciales
		this.setingInicial();

		// inicializa los valores de las columnas
		this.cargarColumnas();

		this.grid.setEmptyMessage("No hay elementos");

		// modelo vacio
		ListModelList model = new ListModelList(new ArrayList());
		this.grid.setModel(model);

		// Los filtros para recuperar los valores
		List<InputElement> listFiltros = new ArrayList<InputElement>();

		// la cebecera de la las columnas
		Columns lcol = new Columns();
		lcol.setMenupopup("auto");
		this.grid.getChildren().add(lcol);

		// radiobuton
		Column cRG = new Column();
		cRG.setLabel("ck");
		cRG.setWidth("30px");
		cRG.setVisible(this.checkVisible);
		lcol.getChildren().add(cRG);

		for (int i = 0; i < this.columnas.size(); i++) {

			ColumnaBrowser col = this.columnas.get(i);			
			
			Column c = new Column();
			c.setSort("auto(" + i + ")");
			c.setLabel(col.getTitulo());
			c.setVisible(col.isVisible());
			c.setWidth(col.getWidthColumna());
			c.setValue(col.getValue());

			
			// el textbox del filtro
			InputElement imputbox = new Textbox();
			if (col.getTipo().compareTo(Config.TIPO_NUMERICO) == 0) {
				imputbox = new Longbox();
				
			} else if (col.getTipo().compareTo(Config.TIPO_BOOL) == 0) {
				// restringe que se escriba T o F
				imputbox.setConstraint(this.getCheck().getTrueFalse());
				imputbox.setMaxlength(1);
			}
			
			if (imputbox instanceof Textbox){
				((Textbox)imputbox).setValue(col.getValue().trim());
			}
			
			// se setea despues, por que puede cambiar según el tipo
			//imputbox.setWidth(col.getWidthComponente());
			imputbox.setHflex("true");

			listFiltros.add(imputbox);

			FiltroBrowserEvento ev = new FiltroBrowserEvento(this, listFiltros);
			imputbox.addEventListener("onOK", ev);

			Vlayout vl = new Vlayout();
			vl.appendChild(imputbox);

			c.appendChild(vl);

			lcol.getChildren().add(c);

			if (i == 0) {
				// es el ID
				c.setWidth("50px");
			}
		}

		this.refreshModeloGrid();

		this.grid.setWidth("100%");
		this.grid.setHeight("485px");
		this.grid.setRowRenderer(new GridRowRender(this, rg));

		this.rg.getChildren().add(this.grid);
		bpac.setCheckAC(this);
		bpac.addComponente("Buscar", this.rg);
		bpac.setWidthWindows(this.getWidthWindows());
		bpac.setHighWindows(this.getHigthWindows());
		bpac.showPopupUnaColumna("Browser de " + this.getTituloBrowser());
	}

	// modelo
	List<Object[]> datos = new ArrayList<Object[]>();

	protected void refreshModeloGrid() throws Exception {

		String msg = "vacia..";
		try {
			datos = this.getModelo();
		} catch (Exception e) {
			msg = e.getMessage();
		}

		this.grid.setEmptyMessage(msg);
		this.grid.setModel(new ListModelList(datos));
	}

	/*
	 * recupera el conjunto de datos a mostrar [ToDo] mejorar para tener tipos
	 * diferentes en los valores
	 */

	private List<Object[]> getModelo() throws Exception {
		return getModelo(null);
	}
	
	private List<Object[]> getModelo(String[] tempWhere) throws Exception {
		Register rr = Register.getInstance();
		
		if (tempWhere != null){
			
		}
		
		try {
			datos = (List<Object[]>) rr.buscarElemento(clase, atributos,
					valores, wheres, tipos, true, this.join, this.attOrden, this.limit);
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		}

		return datos;
	}

	public void setDatosParaNavegacion(Toolbar toolbar) {

		toolbar.setBotonesNavegacion(this.datos,
				this.datos.indexOf(selectedItem));
	}

	public boolean isClickAceptar() {
		return this.bpac.isClickAceptar();
	}

	public String[] getValores() {
		return valores;
	}

	public void setValores(String[] valores) {
		this.valores = valores;
	}

	public String getWidthWindows() {
		return widthWindows;
	}

	public void setWidthWindows(String widthWindows) {
		this.widthWindows = widthWindows;
	}

	public String getHigthWindows() {
		return higthWindows;
	}

	public void setHigthWindows(String higthWindows) {
		this.higthWindows = higthWindows;
	}

	public MyArray getSelectedItem() throws Exception {
		if (this.isClickAceptar() == true) {
			return new MyArray(selectedItem);
		}
		throw new Exception("No se hizo click en Aceptar !!");
	}

	public void setSelectedItem(Object[] selectedItem) {
		this.selectedItem = selectedItem;
	}

	public Row getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(Row selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Row getSelectedRowPrevio() {
		return selectedRowPrevio;
	}

	public void setSelectedRowPrevio(Row selectedRowPrevio) {
		this.selectedRowPrevio = selectedRowPrevio;
	}

	public String getStyleSepectedRowOriginal() {
		return styleSepectedRowOriginal;
	}

	public void setStyleSepectedRowOriginal(String styleSepectedRowOriginal) {
		this.styleSepectedRowOriginal = styleSepectedRowOriginal;
	}

	public boolean isCheckVisible() {
		return checkVisible;
	}

	public void setCheckVisible(boolean checkVisible) {
		this.checkVisible = checkVisible;
	}

	public void addJoin(String join) {
		this.join = join;
	}

	public void addOrden(String orden) {
		this.attOrden = orden;
	}
	
	public void addLimite(int limit) {
		this.limit = limit;
	}

	// *********************************************************
	// Lista de componentes

	public BodyPopupAceptarCancelar getBpac() {
		return bpac;
	}

	public void setBpac(BodyPopupAceptarCancelar bpac) {
		this.bpac = bpac;
	}

	public HtmlBasedComponent getLabel(Object obj, Object[] datos) {
		Label l = new Label();
		String valor = "";
		if (obj != null) {
			l.setValue(obj.toString());
		}
		return l;
	}
	
	
	public HtmlBasedComponent getLabelNumerico(Object obj, Object[] datos) {
		Textbox t = new Textbox();
		t.setValue((this.m.formatoNumero(obj)).trim());
		t.setStyle("text-align: right");
		t.setReadonly(true);
		t.setInplace(true);
		return t;
	}

	public HtmlBasedComponent getLabelGs(Object obj, Object[] datos) {
		Label t = new Label();
		t.setValue((this.m.formatoGs((double)obj)));
		t.setStyle("text-align: right");
		
		Vbox vb = new Vbox();
		vb.setWidth("100%");
		vb.setAlign("end");
		
		t.setParent(vb);
		

//		Cell cel = new Cell();
//		cel.setAlign("right");
//		t.setParent(cel);
		
		return vb;
	}
	
	
	public HtmlBasedComponent getTextbox(Object obj, Object[] datos) {
		Textbox t = new Textbox();
		t.setReadonly(true);
		t.setValue(obj.toString());
		return t;
	}

	public HtmlBasedComponent getLongbox(Object obj, Object[] datos) {
		Longbox t = new Longbox();
		t.setFormat("###,###,###");
		t.setStyle("text-align: right");
		t.setReadonly(true);
		t.setValue((long) obj);
		return t;
	}

	public HtmlBasedComponent getCheckbox(Object obj, Object[] datos) {
		Checkbox ck = new Checkbox();
		ck.setChecked((boolean) obj);
		ck.setDisabled(true);
		return ck;
	}

	public HtmlBasedComponent getRadiogroup(Object obj, Object[] datos) {
		Radiogroup rg = new Radiogroup();
		Radio r = (Radio) getRadio(obj, datos);
		r.setRadiogroup(rg);
		rg.appendChild(r);
		return rg;
	}

	public HtmlBasedComponent getRadio(Object obj, Object[] datos) {
		Radio r = new Radio();
		r.setChecked((boolean) obj);
		r.setDisabled(true);
		return r;
	}

	public HtmlBasedComponent getLabelDateOld(Object obj, Object[] datos) {
		Label l = new Label();
		l.setValue(this.m.dateToString((Date) obj, m.YYYY_MM_DD_HORA_MIN_SEG2));
		return l;
	}

	public HtmlBasedComponent getLabelDate(Object obj, Object[] datos) {
		Datebox date = new Datebox();
		date.setValue((Date) obj);
		date.setButtonVisible(false);
		date.setReadonly(true);
		date.setFormat(m.DD_MM__YYY_HORA_MIN);
		date.setHflex("true");
		date.setSclass("transparent-input");
		return date;
	}

	public HtmlBasedComponent getLabelDateSimple(Object obj, Object[] datos) {
		Datebox date = new Datebox();
		date.setValue((Date) obj);
		date.setButtonVisible(false);
		date.setReadonly(true);
		date.setFormat(m.DD_MM_YYYY);
		date.setHflex("true");
		date.setSclass("transparent-input");
		return date;
	}

	public HtmlBasedComponent getImagenOKCancel(Object obj, Object[] datos) {
		Image img = new Image();
		if ((boolean) obj == true) {
			img.setSrc(Config.IMAGEN_OK);
		} else {
			img.setSrc(Config.IMAGEN_CANCEL);
		}
		return img;
	}

	public HtmlBasedComponent getImagenCheck(Object obj, Object[] datos) {
		Image img = new Image();
		if ((boolean) obj == true) {
			img.setSrc(Config.IMAGEN_CHECK);
		} else {
			img.setVisible(false);
		}
		return img;
	}

	
	public HtmlBasedComponent getLabelTipo(Object obj, Object[] datos) {
		String texto = ((Tipo)obj).getDescripcion();
		System.out.println("\n\n\n\n\n\n\n\n");
		System.out.println(texto+" - "+obj);
		System.out.println("\n\n\n\n\n\n\n\n");
		return getLabel(texto, datos);
	}
	
	
	
	
	// *********************************************************

	@Override
	public boolean verificarAceptar() {
		boolean out = false;
		out = this.selectedItem != NO_SELECTED_ITEM;
		return out;
	}

	@Override
	public String textoVerificarAceptar() {
		// TODO Auto-generated method stub
		return "Debe seleccionar un item o hacer click en [Cancelar] para continuar";
	}

	@Override
	public boolean verificarCancelar() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String textoVerificarCancelar() {
		// TODO Auto-generated method stub
		return null;
	}
	// =============================================================

}

class FiltroBrowserEvento implements EventListener {

	List<InputElement> listTx = null;
	Browser be = null;

	public FiltroBrowserEvento(Browser be, List<InputElement> listTx) {
		this.be = be;
		this.listTx = listTx;
	}

	@Override
	public void onEvent(Event ev) throws Exception {
		// TODO Auto-generated method stub

		String[] valores = new String[listTx.size()];
		for (int i = 0; i < listTx.size(); i++) {
			InputElement tx = (InputElement) listTx.get(i);
			Object valor = tx.getRawValue();
			if (valor == null) {
				valor = "";
			}
			valores[i] = (valor + "").trim();
		}
		this.be.setValores(valores);
		this.be.refreshModeloGrid();

	}

	// @Override
	public void xonEvent(Event ev) throws Exception {
		// TODO Auto-generated method stub

		String[] valores = new String[listTx.size()];
		for (int i = 0; i < listTx.size(); i++) {
			Textbox tx = (Textbox) listTx.get(i);
			valores[i] = tx.getValue().trim();
		}
		this.be.setValores(valores);
		this.be.refreshModeloGrid();

	}

}

class GridRowRender implements RowRenderer {

	Browser br;
	Radiogroup rg;

	public GridRowRender(Browser br, Radiogroup rg) {
		this.br = br;
		this.rg = rg;
	}

	@Override
	public void render(Row row, Object data, int arg2) throws Exception {
		// TODO Auto-generated method stub
		row.setValue(data);
		row.addEventListener(Events.ON_CLICK, new RowEventListener(this.br));
		row.addEventListener(Events.ON_DOUBLE_CLICK, new RowEventListener(
				this.br));

		// en las columnas esta la info de configuración
		List<ColumnaBrowser> columnas = this.br.getColumnasBrowser();

		Object[] datosCel = (Object[]) data;

		// radiobutton
		Radio ra = new Radio();
		ra.setRadiogroup(this.rg);
		ra.setChecked(false);
		ra.setParent(row);

		Object va = datosCel[0]; // el ID
		Label lbId = new Label(va + "");
		lbId.setParent(row);

		for (int i = 1; i < datosCel.length; i++) {
			va = datosCel[i];
			ColumnaBrowser col = columnas.get(i - 1);

			Cell cel = new Cell();
			cel.setStyle(col.getEstilo());
			cel.setParent(row);

			HtmlBasedComponent comp = null;

			try {

				// invoca a la operación
				Method m = this.br.getClass().getMethod(col.getComponente(),
						Object.class, Object[].class);

				comp = (HtmlBasedComponent) m.invoke(this.br, va, datosCel);
			} catch (Exception e) {
				System.out.println("Error metodo componente ["
						+ col.getComponente() + "]:"
						+ e.getCause().getMessage());
				comp = this.br.getLabel(va, datosCel);
			}
			String auxSt = comp.getStyle();
			comp.setStyle(auxSt + ";" + col.getEstilo());
			comp.setWidth(col.getWidthComponente());
			cel.setWidth(col.getWidthComponente());
			comp.setParent(cel);

		}

	}

}

class RowEventListener implements EventListener {

	private Browser br;

	public RowEventListener(Browser br) {
		this.br = br;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		if (arg0.getName().compareTo(Events.ON_DOUBLE_CLICK) == 0) {
			this.br.getBpac().getControlVM().aceptar();
			return;
		}

		// actualizar el estylo de la row que estaba antes.
		Row rPrevia = this.br.getSelectedRow();
		rPrevia.setStyle(this.br.getStyleSepectedRowOriginal());

		// nueva row selected
		Row rClick = (Row) arg0.getTarget();
		this.br.setSelectedRow(rClick);
		this.br.setStyleSepectedRowOriginal(rClick.getStyle());
		// le ponemos el style de seleccionado
		rClick.setStyle(Browser.STYLE_SELECTED_ROW);

		this.br.setSelectedItem((Object[]) rClick.getValue());
		Radio ra = (Radio) rClick.getChildren().get(0);
		ra.setChecked(true);
		ra.setFocus(true);

	}

}
