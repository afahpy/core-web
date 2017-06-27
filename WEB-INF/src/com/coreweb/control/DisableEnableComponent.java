package com.coreweb.control;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Rows;
import org.zkoss.zul.event.ZulEvents;

public class DisableEnableComponent {

	private static String DISABLED = "Disabled";
	private static String READONLY = "Readonly";
	private static String BUTTON_VISIBLE = "ButtonVisible";

	
	private static Object[][] listaClasePropiedad = {
			// { Toolbarbutton.class, DISABLED, true },
			{ Button.class, DISABLED, true },
			{ Bandbox.class, DISABLED, true }, { Radio.class, DISABLED, true },
			 { Checkbox.class, DISABLED, true },
			{ Combobox.class, BUTTON_VISIBLE, false },
			// { Combobox.class, READONLY, true },
			{ Datebox.class, BUTTON_VISIBLE, false },
			{ Datebox.class, READONLY, true } };

	private static Object[][] listaInstancePropiedad = new Object[listaClasePropiedad.length][3];

	
	// para guardar el estado de los componentes, esto es porque segun los
	// permisos puede que haya algunos que ya estan desabilitados, y cuando se
	// restaure queremos que tenga su estado original

	private Hashtable<Component, Object[][]> tmpCmpDeshabilitadosOri = new Hashtable<Component, Object[][]>();
	
	
	private GenericViewModel vm;
	
	public DisableEnableComponent(GenericViewModel vm){
		this.vm = vm;
	}
	
	static {
		for (int i = 0; i < listaInstancePropiedad.length; i++) {
			try {
				listaInstancePropiedad[i][0] = ((Class) listaClasePropiedad[i][0])
						.newInstance();
				listaInstancePropiedad[i][1] = listaClasePropiedad[i][1];
				listaInstancePropiedad[i][2] = listaClasePropiedad[i][2];
			} catch (Exception e) {
			}
		}
	}

	private void clearTmpComponentesDeshabilitadosx() {
		// this.tmpComponentesDeshabilitados = new Hashtable<Component,
		// String>();
		this.tmpCmpDeshabilitadosOri = new Hashtable<Component, Object[][]>();
	}

	public void disableComponents(AbstractComponent ac) {

		//System.out.println("------------------------------- "+ac.getId()+"  "+
		// ac);

		try {

			boolean siEraEspecifica = false;
			for (int i = 0; i < listaInstancePropiedad.length; i++) {

				if (listaInstancePropiedad[i][0].getClass().isInstance(ac) == true) {

					siEraEspecifica = true;
					String propertyAux = (String) listaInstancePropiedad[i][1];
					boolean valueDisable = (boolean) listaInstancePropiedad[i][2];

					// si se va a aplicar la propiedad luego, entonces no es
					// necesario hacerlo
					// ahora. Además, si se hace ahora, luego da como que ya
					// tenia la propiedad de
					// antes y entonce luego no lo vuelve a habilitar
					// if (propertyAux.compareTo(property) != 0) {
					this.disableComponente(ac, valueDisable, true, propertyAux);
					// }

				}
			}
			if (siEraEspecifica == false) {
				// obtener su estado y guardar los deshabilitados
				this.disableComponente(ac, true, false, null);
			}

		} catch (Exception e) {
		}

		if (ac instanceof Grid) {
			Grid grid = (Grid) ac;
			Rows rows = grid.getRows();
			if ((rows == null)) { // || (rows.getChildren().size() == 0)){
				grid.addEventListener(ZulEvents.ON_AFTER_RENDER,
						new RefreshAfterRender(this.vm,
								RefreshAfterRender.TIPO_GRID));
			}
		}

		if (ac instanceof Listbox) {
			Listbox listbox = (Listbox) ac;
			List lAux = listbox.getItems();
			if ((lAux == null) || (lAux.size() == 0)) {
				listbox.addEventListener(ZulEvents.ON_AFTER_RENDER,
						new RefreshAfterRender(this.vm,
								RefreshAfterRender.TIPO_LISTBOX));
			}
		}

		if (ac instanceof Radiogroup) {
			Radiogroup rg = (Radiogroup) ac;
			List lAux = rg.getItems();
			if ((lAux == null) || (lAux.size() == 0)) {
				rg.addEventListener(ZulEvents.ON_AFTER_RENDER,
						new RefreshAfterRender(this.vm,
								RefreshAfterRender.TIPO_RADIOGROUP));
			}
		}

		List children = ac.getChildren();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				AbstractComponent co = (AbstractComponent) children.get(i);
				disableComponents(co);
			}
		}

	}

	
	

	private void disableComponente(AbstractComponent ac, boolean valueDisabled,
			boolean siPropiedadEspecifica, String property) throws Exception {

		Object[][] pros = { { DISABLED, !valueDisabled },
				{ READONLY, !valueDisabled } };

		pros = new Object[][] { { READONLY, !valueDisabled } };

		if (siPropiedadEspecifica == true) {
			pros = new Object[][] { { property, !valueDisabled } };
		}

		for (int i = 0; i < pros.length; i++) {
			String pr = (String) pros[i][0];
			Method mget = ac.getClass().getMethod("is" + pr);
			boolean v = (Boolean) mget.invoke(ac);
			pros[i][1] = v;

			if (v == valueDisabled) {
				this.tmpCmpDeshabilitadosOri.put(ac, pros);
			}
			// deshabilita el componente
			//System.out.println("------------------------------- "+ac.getId()+"  "+ ac + " pr:"+pr+":"+valueDisabled);
			Method mset = ac.getClass().getMethod("set" + pr, Boolean.TYPE);
			mset.invoke(ac, valueDisabled);
		}

	}

	
	
	public void restoreComponents(AbstractComponent ac) {
		try {


			Object[][] pros = this.tmpCmpDeshabilitadosOri.get(ac);

			if (pros == null) {

				try {

					boolean siEraEspecifico = false;

					for (int i = 0; i < listaInstancePropiedad.length; i++) {

						if (listaInstancePropiedad[i][0].getClass().isInstance(
								ac)) {
							siEraEspecifico = true;
							String propertyAux = (String) listaInstancePropiedad[i][1];
							boolean valueDisable = (boolean) listaInstancePropiedad[i][2];
							this.restoreComponente(ac, !valueDisable, true,
									propertyAux);

						}
					}

					if (siEraEspecifico == false) {
						// habilita el componente
						this.restoreComponente(ac, false, false, null);
					}

				} catch (Exception e) {

				}
			} else {
				// el componente tiene algo deshabilitado, pero hay que
				// verificar que
				this.restoreComponenteProps(ac, pros);

			}

			/*
			 * if (ac instanceof Radiogroup){
			 * 
			 * Radiogroup rg = (Radiogroup)ac;
			 * 
			 * List<Component> lr = rg.getChildren(); //rg.getModel() for (int i
			 * = 0; i < lr.size(); i++) {
			 * System.out.println("------------- (restore) Radio "+property);
			 * AbstractComponent r = (AbstractComponent)lr.get(i);
			 * //disableComponents(r, property); } }
			 */

			List children = ac.getChildren();
			if (children != null) {
				for (int i = 0; i < children.size(); i++) {
					AbstractComponent co = (AbstractComponent) children.get(i);
					restoreComponents(co);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void restoreComponente(AbstractComponent ac, boolean valueEnable,
			boolean siPropiedadEspecifica, String property) throws Exception {
		// habilita el componente

		Object[][] pros = { { DISABLED, valueEnable },
				{ READONLY, valueEnable } };

		pros = new Object[][] { { READONLY, valueEnable } };

		if (siPropiedadEspecifica == true) {
			pros = new Object[][] { { property, valueEnable } };
		}

		restoreComponenteProps(ac, pros);
	}

	private void restoreComponenteProps(AbstractComponent ac, Object[][] pros)
			throws Exception {
		// habilita el componente

		for (int i = 0; i < pros.length; i++) {
			String po = (String) pros[i][0];
			boolean va = (boolean) pros[i][1];

			Method mset = ac.getClass().getMethod("set" + po, Boolean.TYPE);
			mset.invoke(ac, va);
		}

	}

	
	
}



class RefreshAfterRender implements EventListener {

	public static int TIPO_GRID = 1;
	public static int TIPO_LISTBOX = 2;
	public static int TIPO_RADIOGROUP = 3;

	private int tipo = 0;
	private GenericViewModel vm;
	private DisableEnableComponent disableEnableComponent = null;

	public RefreshAfterRender(GenericViewModel vm, int tipo) {
		this.vm = vm;
		this.tipo = tipo;
		this.disableEnableComponent = new DisableEnableComponent(this.vm); 
	}

	@Override
	public void onEvent(Event ev) throws Exception {
		Component cmp = ev.getTarget();

		if (this.tipo == TIPO_GRID) {
			aplicarAccion(cmp);

		} else if (this.tipo == TIPO_LISTBOX) {
			Listbox lb = (Listbox) cmp;
			List lbis = lb.getItems();
			for (Iterator iterator = lbis.iterator(); iterator.hasNext();) {
				Component item = (Component) iterator.next();
				aplicarAccion(item);
			}

		} else if (this.tipo == TIPO_RADIOGROUP) {
			Radiogroup lb = (Radiogroup) cmp;
			List lbis = lb.getItems();
			for (Iterator iterator = lbis.iterator(); iterator.hasNext();) {
				Component item = (Component) iterator.next();
				aplicarAccion(item);
			}
		}
	}

	private void aplicarAccion(Component cmp) {
		if (this.vm.isDeshabilitado() == true) {
			this.disableEnableComponent.disableComponents((AbstractComponent) cmp);
		} else {
			// System.out.println("-- evento after render 11 : " +
			// cmp.getClass().getName());
			// this.vm.restoreAllReadonlyComponents();
		}
	}

}
