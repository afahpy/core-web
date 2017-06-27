package com.coreweb.templateABM;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.Button;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

import com.coreweb.Config;
import com.coreweb.control.GenericViewModel;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.dto.DTO;
import com.coreweb.login.ControlInicio;
import com.coreweb.login.LoginUsuarioDTO;

public class Footer extends GenericViewModel {

	private Page pagina;
	private boolean yesClick = true;

	public Page getPagina() {
		return pagina;
	}

	public void setPagina(Page pagina) {
		this.pagina = pagina;
	}

	@Init(superclass = true)
	public void initFooter(@ExecutionParam("pageVM") Object pageVM) {
		Page page = (Page) pageVM;
		this.setPagina(page);
		page.setFooter(this);
	}

	@AfterCompose(superclass = true)
	public void afterComposeFooter() {
		this.deshabilitarComponentes();
	}

	public String getAliasFormularioCorriente() {
		return this.getPagina().getAliasFormularioCorriente();
	}

	@Command
	public void save() {
		// no es necesario un NotifyChange porque desde el zul se invoca el
		// globalCommand refreshComponents
		this.saveDato();
		this.yesClick = true;
	}

	private boolean saveDato() {
		boolean out = false;

		DTO dto = this.pagina.getBody().getDTOCorriente();
		if ((this.pagina.getBody().siControlCambio() == true) && (dto.esNuevo() == false)) {

			// ver la fecha del dto actual y del objeto del dominio
			String entidad = this.pagina.getBody().getEntidadPrincipal();

			try {
				Register rr = Register.getInstance();
				Domain dom = rr.getObject(entidad, dto.getId());

				if (dom.getModificado().compareTo(dto.getModificado()) > 0) {
					this.mensajeError("Error al intentar actualizar, ya existe una version mas reciente modificada el "
							+ dom.getModificado().toString() + " por " + dom.getUsuarioMod());
					return false;
				}
			} catch (Exception e) {
				this.mensajeError(e.getMessage());
				e.printStackTrace();
			}

		}

		if (this.pagina.getBody().verificarAlGrabar() == false)

		{
			this.mensajeError(this.pagina.getBody().textoErrorVerificarGrabar());
			return false;
		}

		try {
			this.pagina.grabarDTOCorriente(); // graba
			this.pagina.getBody().grabarEventosAgenda();

			this.mensajePopupTemporal("Información grabada!!", 1000);
			out = true;
		} catch (Exception e) {
			e.printStackTrace();
			this.mensajeError("Error grabando la información\n" + e.getMessage());
			out = false;
		}

		return out;
	}

	@Command
	public void discard() throws Exception {
		// no es necesario un NotifyChange porque desde el zul se invoca el
		// globalCommand deshabilitarComponentes
		this.yesClick = false;
		boolean siDirty = this.getPagina().getBody().siHuboCambiosEnDTO();

		// no hay cambios, sale nomas
		if (siDirty == false) {
			this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
			this.yesClick = true;
			return;
		}

		// preguntar que hacer con los cambios
		int click = this.mensajeSiNoCancelar("Grabar los cambios antes de salir?");

		if (click == Config.BOTON_CANCEL) {
			// vuelve a la pantalla
			this.yesClick = false;
			return;
		}
		if (click == Config.BOTON_YES) {
			// por que puede que NO haya podido grabar, entonces vuelve
			if (this.saveDato() == true) {
				this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
				this.yesClick = true;
			} else {
				// como no pudo grabar, entonces vuelve a la página donde
				// estaba.
				this.yesClick = false;
				return;
			}
		}
		if (click == Config.BOTON_NO) {
			this.m.mensajePopupTemporalWarning("Últimos cambios NO grabados!!", 1000);
			this.pagina.refreshDTOCorriente();
			this.getPagina().getTool().setEstadoABM(Toolbar.MODO_NADA);
			this.yesClick = true;
		}

	}

	@GlobalCommand
	@NotifyChange("*")
	public void habilitarComponentes() {
		this.restoreAllDisabledComponents();
		Window win = (Window) this.mainComponent;
		win.setVisible(true);
	}

	@GlobalCommand
	@NotifyChange("*")
	public void deshabilitarComponentes() {

		this.restaurarUsuarioOriginal();

		this.disableAllComponents();
		Window win = (Window) this.mainComponent;
		win.setVisible(false);
		BindUtils.postGlobalCommand(null, null, "refreshComponentes", null);
	}

	public boolean isYesClick() {
		return yesClick;
	}

	public void setYesClick(boolean yesClick) {
		this.yesClick = yesClick;
	}

	@Override
	public boolean getCondicionComponenteSiempreHabilitado() {
		// TODO Auto-generated method stub
		return true;
	}

}
