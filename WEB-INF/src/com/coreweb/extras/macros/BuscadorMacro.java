package com.coreweb.extras.macros;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import com.coreweb.componente.BuscarElemento;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;


@ComponentAnnotation({"value:@ZKBIND(ACCESS=both,SAVE_EVENT=onEdited)", 
	"disabled:@ZKBIND(ACCESS=both,SAVE_EVENT=onEdited)", 
	"readonly:@ZKBIND(ACCESS=both,SAVE_EVENT=onEdited)"})
public abstract class BuscadorMacro extends HtmlMacroComponent {

	private Object dato = null;
	private String campo = this.getCampoDefault();
	private String where = "";
	private String width = "150px";
	private Misc misc = new Misc();
	private Object parent = null;
	private String[] valor = {};
	private boolean disabled = false;
	private boolean readonly = false;

	private String atributo = "atributo";

	private String[][] camposTabla = this.getPosicionesCampos();

/*	@Wire
	Div dv;*/

	@Wire
	Button searchBtn;

	@Wire
	Textbox searchText;

	

	
	
	public void afterCompose() {
		super.afterCompose();
		this.setPlaceholder();
	//	this.setSize();

		searchBtn.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				executeEvent();
			}
		});

		searchText.addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				executeEvent();
			}
		});
	}

	// Ejecuta las funciones del componente
	private void executeEvent() throws Exception {

		this.setDato(this.buscarDatoBrowser());
		if (this.getDato() != null) {

			this.setTextValue(this.getDato());

			this.setMetodo(this.parent, this.getAtributo(), this.getDato());
		}
		BindUtils.postGlobalCommand(null, null, "refreshComponentes", null);
	}

	private void setPlaceholder() {
		String[][] campos = this.camposTabla;
		for (int i = 0; i < campos.length; i++) {
			String[] campoValor = campos[i];

			if (this.campo.compareTo(campoValor[0]) == 0) {
				searchText.setPlaceholder(campoValor[1]);
			}

		}
	}

	// retorna la posicion del filtro segun el valor [campoFiltro]
	private int getPosFiltro() {

		int out = 0;

		String[][] campos = this.camposTabla;
		for (int i = 0; i < campos.length; i++) {
			String campoValor = campos[i][0];
			if (this.campo.compareTo(campoValor) == 0) {
				out = i;
			}
		}

		return out;
	}

/*	private void setSize() {

		dv.setWidth(this.getWidth());

		int widthRoot = Integer.parseInt(dv.getWidth().replace("px", ""));
		int widthText = widthRoot - 28;
		searchText.setWidth(widthText + "px");
	}*/

	/*
	 * private Object getAtributoControl() throws Exception { Object out = null;
	 * out = this.misc.ejecutarMetoto(this.parent, "get" + this.getAtributo());
	 * return out; }
	 */

	public Object getValue() {
		return parent;
	}

	public void setValue(Object value) {
		this.parent = value;
	}

	private void setTextValue(Object valor) {
		String value = "";
		try {
			String[][] campos = this.camposTabla;

			if (valor instanceof DTO) {

				for (int i = 0; i < campos.length; i++) {
					String campoAtributo = campos[i][0];
					if (this.campo.compareTo(campoAtributo) == 0) {

						value = (this.getMetodo(valor, campoAtributo))
								.toString();

						// value = this.misc.ejecutarMetoto(valor, "get" +
						// campoAtributo).toString();
					}
				}
			}

			if (valor instanceof MyArray) {

				for (int i = 0; i < campos.length; i++) {

					String campoAtributo = campos[i][0];

					if (campo.compareTo(campoAtributo) == 0) {

						value = (this.getMetodo(valor, "pos" + (i + 1)))
								.toString();

					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		searchText.setValue(value);
	}

	// ========================= buscador ===================

	private String[] getNombreTiposAtributos() {
		String[] out = new String[this.getPosicionesCampos().length];
		String[][] campos = this.camposTabla;
		for (int i = 0; i < campos.length; i++) {
			out[i] = campos[i][2];
		}
		return out;
	}

	private String[] getNombreAtributos() {
		String[] out = new String[this.getPosicionesCampos().length];
		String[][] campos = this.camposTabla;
		for (int i = 0; i < campos.length; i++) {
			out[i] = campos[i][0];
		}
		return out;
	}

	private DTO buscarDatoBrowser() throws Exception {
		DTO out = null;

		String valor = this.searchText.getValue().trim();
		int posicion = this.getPosFiltro();

		

		BuscarElemento b = new BuscarElemento();
		b.setClase(this.getClaseObject());
		b.setAtributos(this.getNombreAtributos());
		b.setTipos(this.getNombreTiposAtributos());
		b.setTitulo("Buscar Banco Cuenta");
		b.setNombresColumnas(this.getNombreAtributos());
		b.setAssembler(this.getAssembler());
		b.setWidth("800px");
		if (this.valor.length > 0) {
			// los valores tienen la misma logintud que los atributos :(
			// b.setValores(this.valor);
		}
		b.show(valor, posicion);
		if (b.isClickAceptar() == true) {
			out = b.getSelectedItemDTO();
		}

		return out;
	}

	// ================ metodos a implementar ================

	// creo que es para definir el campo inicial de la búsqueda
	public abstract String getCampoDefault();

	// las psiciones de los campos
	public abstract String[][] getPosicionesCampos();

	// el atributo de la clase de control donde se guarda el dato buscado
	public abstract String getAtributo();

	// El dominio de la clase
	public abstract Class getClaseObject();

	// titulo para buscar
	public abstract String getTitulo();
	
	public abstract Assembler getAssembler();

	// ================================== GET AND SET =======

	public Object getDato() {
		return dato;
	}

	public void setDato(Object dato) {
		this.dato = dato;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

/*	public Div getDv() {
		return dv;
	}

	public void setDv(Div dv) {
		this.dv = dv;
	}*/

	private Object getMetodo(Object obj, String att) throws Exception {
		Method m = new PropertyDescriptor(att, obj.getClass()).getReadMethod();
		Object v = m.invoke(obj);
		return v;
	}

	private void setMetodo(Object obj, String att, Object value)
			throws Exception {

		Object vv = value;
		if (vv != null) {
			vv = vv.getClass().getName() + "" + vv;
		}

		new PropertyDescriptor(att, obj.getClass()).getWriteMethod().invoke(
				obj, value);
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		
		this.disabled = disabled;
		this.searchText.setDisabled(disabled);
		this.searchBtn.setDisabled(disabled);
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		
		this.readonly = readonly;
		this.searchText.setReadonly(readonly);
		this.searchBtn.setDisabled(readonly);
	}

}
