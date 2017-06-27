package com.coreweb.extras.browser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.converter.sys.GridModelConverter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.componente.BuscarElemento;
import com.coreweb.componente.VerificaAceptarCancelar;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Register;
import com.coreweb.extras.reporte.DatosReporte;
import com.coreweb.templateABM.Toolbar;
import com.coreweb.util.Check;
import com.coreweb.util.MyArray;

public abstract class Browser2 extends SimpleViewModel {

	@Init(superclass = true)
	public void initBrowser() {

	}

	@AfterCompose(superclass = true)
	public void afterComposeBrowser() {

	}

	public static String LABEL = "getLabel";
	public static String LABEL_NUMERICO = "getLabelNumerico";
	public static String TEXT_BOX = "getTextbox";
	public static String LONG_BOX = "getLongbox";
	public static String CHECK_BOX = "getCheckbox";
	public static String RADIOGROUP = "getRadiogroup";
	public static String RADIO = "getRadio";
	// se llama label, pero es un Date lo que retorna
	public static String LABEL_DATE = "getLabelDate";

	private List<Column> listaColumnasGrid = new ArrayList<>();

	private boolean showBotonExportarCSV = false;

	public boolean isShowBotonExportarCSV() {
		return showBotonExportarCSV;
	}

	public void setShowBotonExportarCSV(boolean showBotonExportarCSV) {
		this.showBotonExportarCSV = showBotonExportarCSV;
	}

	/*
	 * public static String TIPO_STRING = "String"; public static String
	 * TIPO_NUMERICO = "Numerico"; public static String TIPO_BOOL = "Bool";
	 * public static String TIPO_DATE = "Date";
	 */

	public abstract String getCabeceraZulUrl();

	// la info de olas columnas del browser
	public abstract List<ColumnaBrowser> getColumnasBrowser();

	// query principal
	public abstract String getQuery();

	/**
	 * Query para exportar CSV, por dafault retorna lo mismo que el otro query
	 * 
	 * @return
	 */
	public String getQueryCSV() {
		return this.getQuery();
	}

	/**
	 * Las columnas que se van a usar, por Defaul las toma de las columnas del
	 * browser. Si se tiene un browserCSV especial, hay que ddefinarlas y ver
	 * que sean iguales al queryCSV
	 * 
	 * @return
	 */
	public String[] getCamposCSV() {
		List<ColumnaBrowser> lc = this.getColumnasBrowser();
		String[] out = new String[lc.size()];

		for (int i = 0; i < lc.size(); i++) {
			ColumnaBrowser cb = lc.get(i);
			String col = cb.getTitulo();
			out[i] = col;
		}

		return out;
	}

	String whereGeneral = "";
	Hashtable<String, Object> parametroWhereGeneral = new Hashtable<>();

	public void setWhere(String where) {
		this.whereGeneral = where;
	}

	public void setWhere(String where, Hashtable<String, Object> parametros) {
		this.whereGeneral = where;
		this.parametroWhereGeneral = parametros;
	}

	// para inicializar valores
	public abstract void setingInicial();

	public abstract String getTituloBrowser();

	private String widthWindows = Config.ANCHO_APP;
	private String higthWindows = "400px";

	private Grid grid = new Grid();

	private boolean checkVisible = false;

	// estos datos se inicializan con el metodo cargarColumnas()
	List<ColumnaBrowser> columnas; // la informacion de las columnas

	// usados por el Register para armar el HQL
	// private String[] nombres; // nombre de la columna
	private String[] atributos; // atributo de la clase
	private Boolean[] usarFiltro; // atributo de la clase
	private String[] valores;
	private String[] tipos; // los tipos de los campos
	private String[] wheres;
	private String join = "";
	private String attOrden = "";

	public static Object[] NO_SELECTED_ITEM = { -1 };

	private Object[] selectedItem = NO_SELECTED_ITEM;
	private Row selectedRow = new Row();
	private Row selectedRowPrevio = new Row();
	private String styleSepectedRowOriginal = "";
	public static String STYLE_SELECTED_ROW = "background-color: #DAE7F6;";

	Radiogroup rg = new Radiogroup();

	public void refreshBrowser() throws Exception {
		this.refreshModeloGrid();
	}

	private void cargarColumnas() {

		/*
		 * // una ColumnaBrowser para el ID ColumnaBrowser id = new
		 * ColumnaBrowser(); id.setTitulo("Id"); id.setCampo("id");
		 * id.setTipo(Config.TIPO_NUMERICO);
		 * 
		 * // Columnas del browser + la columnad del ID this.columnas =
		 * this.getColumnasBrowser(); this.columnas.add(0, id);
		 */
		this.columnas = this.getColumnasBrowser();

		int numeroColumnas = this.columnas.size(); // por el id
		// usados por el Register para armar HQL
		this.atributos = new String[numeroColumnas];
		this.usarFiltro = new Boolean[numeroColumnas];
		this.valores = new String[numeroColumnas];
		// this.nombres = new String[numeroColumnas];
		this.wheres = new String[numeroColumnas];
		this.tipos = new String[numeroColumnas];

		for (int i = 0; i < numeroColumnas; i++) {
			ColumnaBrowser col = this.columnas.get(i);

			// nombres[i] = col.getTitulo();
			atributos[i] = col.getCampo();
			usarFiltro[i] = col.isUsarFiltro();
			valores[i] = "";
			wheres[i] = col.getWhere();
			tipos[i] = col.getTipo();
		}
	}

	@Override
	public List<String> getColumnNames() {
		ArrayList<String> out = new ArrayList<>();

		for (int i = 0; i < this.listaColumnasGrid.size(); i++) {
			Column c = this.listaColumnasGrid.get(i);
			if (c.isVisible() == true) {
				out.add(c.getLabel());
			}
		}
		return out;
	}

	public void crearBrowser() throws Exception {

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

		// las columnas que son la cabecera del grid
		this.listaColumnasGrid = new ArrayList<>();

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

			this.listaColumnasGrid.add(c);

			// el textbox del filtro
			InputElement imputbox = new Textbox();
			if (col.getTipo().compareTo(Config.TIPO_NUMERICO) == 0) {
				imputbox = new Longbox();
			} else if (col.getTipo().compareTo(Config.TIPO_BOOL) == 0) {
				// restringe que se escriba T o F
				imputbox.setConstraint(this.getCheck().getTrueFalse());
				imputbox.setMaxlength(1);
			}
			// se setea despues, por que puede cambiar según el tipo
			imputbox.setWidth(col.getWidthComponente());

			listFiltros.add(imputbox);

			FiltroBrowserEvento2 ev = new FiltroBrowserEvento2(this, listFiltros);
			imputbox.addEventListener("onOK", ev);

			if (col.isUsarFiltro() == false) {
				imputbox.setVisible(false);
			}

			Vlayout vl = new Vlayout();
			vl.appendChild(imputbox);

			c.appendChild(vl);

			lcol.getChildren().add(c);

			// if (i == 0) {
			// // es el ID
			// c.setWidth("50px");
			// }
		}

		this.refreshModeloGrid();

		this.grid.setWidth("100%");
		this.grid.setRowRenderer(new GridRowRender2(this, rg));

		this.rg.getChildren().add(this.grid);
	}

	// modelo
	private List<Object[]> datos = new ArrayList<Object[]>();

	public List<Object[]> getDatos() {
		return datos;
	}

	public void setDatos(List<Object[]> datos) {
		this.datos = datos;
	}

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
		Register rr = Register.getInstance();
		try {
			datos = (List<Object[]>) rr.buscarElementoBrowser(this.getQuery(), atributos, valores, tipos,
					this.whereGeneral, this.parametroWhereGeneral);
		} catch (Exception e) {
			throw e;
		}

		return datos;
	}

	public List<Object[]> getModeloSinLimite() throws Exception {
		Register rr = Register.getInstance();
		try {
			datos = (List<Object[]>) rr.buscarElementoQuery(this.getQuery(), atributos, valores, tipos,
					this.whereGeneral, this.parametroWhereGeneral, false, 0, true);
		} catch (Exception e) {
			throw e;
		}

		return datos;
	}

	/**
	 * NO funiona aún, es para pensar. Hay que ver como usar el query del csv
	 * junto con el where de los campos del browser
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getModeloCSV() throws Exception {
		Register rr = Register.getInstance();
		try {
			datos = (List<Object[]>) rr.buscarElementoQuery(this.getQueryCSV(), atributos, valores, tipos,
					this.whereGeneral, this.parametroWhereGeneral, false, 0, true);
		} catch (Exception e) {
			throw e;
		}

		return datos;
	}

	public void setDatosParaNavegacion(Toolbar toolbar) {

		toolbar.setBotonesNavegacion(this.datos, this.datos.indexOf(selectedItem));
	}

	/*
	 * public boolean isClickAceptar() { return false; }
	 */

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

	/**
	 * Retorna todos los objetos filtrados, es decir, lo que se está viendo en
	 * el browser ahora mismo
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<MyArray> getItemsFiltrados() throws Exception {
		List<MyArray> lma = new ArrayList<>();
		for (int i = 0; i < this.datos.size(); i++) {
			Object[] arr = this.datos.get(i);
			lma.add(this.arregloToMyArray(arr));
		}
		return lma;
	}

	/**
	 * Retorna el objeto seleccioado, sino no hay, retorna null
	 * 
	 * @return
	 * @throws Exception
	 */
	public MyArray getSelectedItem() throws Exception {
		if (selectedItem == NO_SELECTED_ITEM) {
			return null;
		}
		MyArray ma = this.arregloToMyArray(this.selectedItem);
		return ma;
	}

	private MyArray arregloToMyArray(Object[] arr) throws Exception {
		MyArray ma = new MyArray();
		for (int i = 0; i < arr.length; i++) {
			this.m.ejecutarMetoto(MyArray.class.getName(), "setPos" + (i + 1), ma, arr[i]);
		}
		return ma;
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

	// *********************************************************
	// Lista de componentes

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
		date.setWidth("140px"); // ancho por default
		date.setFormat(m.DD_MM__YYY_HORA_MIN);
		date.setHflex("true");
		date.setStyle("background:transparent; border:none");
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

	// *********************************************************

	/*
	 * @Override public boolean verificarAceptar() { boolean out = false; out =
	 * this.selectedItem != NO_SELECTED_ITEM; return out; }
	 * 
	 * @Override public String textoVerificarAceptar() { // TODO Auto-generated
	 * method stub return
	 * "Debe seleccionar un item o hacer click en [Cancelar] para continuar"; }
	 * 
	 * @Override public boolean verificarCancelar() { // TODO Auto-generated
	 * method stub return true; }
	 * 
	 * @Override public String textoVerificarCancelar() { // TODO Auto-generated
	 * method stub return null; }
	 * 
	 */

	// =============================================================

	public Grid getGrid() {
		return grid;
	}

	public DatosReporte getReporteVacio() {
		System.out.println("** Browser2.getReporteVacio:  No implementado en " + this.getClass().getName());

		return null;
	}

	public String getTituloReporte() {
		System.out.println("** Browser2.getTituloReporte:  No implementado en " + this.getClass().getName());

		return null;
	}

}

class FiltroBrowserEvento2 implements EventListener {

	List<InputElement> listTx = null;
	Browser2 be = null;

	public FiltroBrowserEvento2(Browser2 be, List<InputElement> listTx) {
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

class GridRowRender2 implements RowRenderer {

	Browser2 br;
	Radiogroup rg;

	public GridRowRender2(Browser2 br, Radiogroup rg) {
		this.br = br;
		this.rg = rg;
	}

	@Override
	public void render(Row row, Object data, int arg2) throws Exception {
		// TODO Auto-generated method stub
		row.setValue(data);
		row.addEventListener(Events.ON_CLICK, new RowEventListener2(this.br));
		row.addEventListener(Events.ON_DOUBLE_CLICK, new RowEventListener2(this.br));

		// en las columnas esta la info de configuración
		List<ColumnaBrowser> columnas = this.br.getColumnasBrowser();

		Object[] datosCel = (Object[]) data;

		// radiobutton
		Radio ra = new Radio();
		ra.setRadiogroup(this.rg);
		ra.setChecked(false);
		ra.setParent(row);

		// Object va = datosCel[0]; // el ID
		// Label lbId = new Label(va + "");
		// lbId.setParent(row);
		Object va = null;

		for (int i = 0; i < datosCel.length; i++) {
			va = datosCel[i];
			ColumnaBrowser col = columnas.get(i);

			Cell cel = new Cell();
			cel.setStyle(col.getEstilo());
			cel.setParent(row);

			HtmlBasedComponent comp = null;

			try {

				// invoca a la operación
				Method m = this.br.getClass().getMethod(col.getComponente(), Object.class, Object[].class);

				comp = (HtmlBasedComponent) m.invoke(this.br, va, datosCel);
			} catch (Exception e) {
				System.out
						.println("Error metodo componente [" + col.getComponente() + "]:" + e.getCause().getMessage());
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

class RowEventListener2 implements EventListener {

	private Browser2 br;

	public RowEventListener2(Browser2 br) {
		this.br = br;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		if (arg0.getName().compareTo(Events.ON_DOUBLE_CLICK) == 0) {
			// this.br.getBpac().getControlVM().aceptar();
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
