package com.coreweb.extras.alerta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.componente.WindowPopup;
import com.coreweb.control.GenericViewModel;
import com.coreweb.control.SoloViewModel;
import com.coreweb.domain.AgendaEvento;
import com.coreweb.domain.Alerta;
import com.coreweb.domain.Register;
import com.coreweb.extras.agenda.AgendaEventoDTO;
import com.coreweb.extras.agenda.AgendaEventoDetalleDTO;
import com.coreweb.extras.agenda.AssemblerAgenda;
import com.coreweb.login.ControlInicio;
import com.coreweb.login.Login;
import com.coreweb.util.AutoNumeroControl;
import com.coreweb.util.Misc;
import com.coreweb.util.MyPair;
import com.jgoodies.binding.BindingUtils;

public class ControlAlertas extends SoloViewModel {

	private WindowPopup w;

	private List<AlertaDTO> alertas = new ArrayList<AlertaDTO>();

	private String mensaje = "";

	private AlertaDTO selectedAlerta;

	public List<AlertaDTO> getAlertas() {
		return alertas;
	}

	public void setAlertas(List<AlertaDTO> alertas) {
		this.alertas = alertas;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public AlertaDTO getSelectedAlerta() {
		return selectedAlerta;
	}

	public void setSelectedAlerta(AlertaDTO selectedAlerta) {
		this.selectedAlerta = selectedAlerta;
	}

	@Init(superclass = true)
	public void initControlAlertas() {
		this.alertas = this.cargarAlertas();
		// Clients.showNotification(this.alertas.size()+" - "+this.cargarAlertas().size());
		// BindUtils.postNotifyChange(null, null, this, "alertas");
	}

	@AfterCompose(superclass = true)
	public void afterComposeControlAlertas() {

	}

	@Override
	public String getAliasFormularioCorriente() {
		return IDCore.F_MIS_ALERTAS;
	}

	public void mostrarAlertas() {
		try {
			w = new WindowPopup();
			w.setModo(WindowPopup.NUEVO);
			w.setTitulo("Mis Alertas");
			w.setWidth("920px");
			w.setHigth("520px");
			w.setSoloBotonCerrar();
			w.show(Config.ALERTAS_ZUL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int desde = 0;
	public int cantidad = 20;

	public List<AlertaDTO> cargarAlertas() {
		List<AlertaDTO> alertas = new ArrayList<AlertaDTO>();
		try {
			Register rr = Register.getInstance();
			AssemblerAlerta as = new AssemblerAlerta();
			// System.out.println("entro a cargar: "+this.desde+" - "+this.hasta);
			List<Alerta> alertasDom = rr.getAllAlertas(desde, cantidad,
					this.getLoginNombre());
			for (Alerta alerta : alertasDom) {
				AlertaDTO alertaDto = new AlertaDTO();
				alertaDto = as.domainToDto(alerta);
				alertas.add(alertaDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alertas;
	}

	@Command
	@NotifyChange("alertas")
	public void next() {
		this.desde += 20;
		this.alertas = this.cargarAlertas();
	}

	@Command
	@NotifyChange("alertas")
	public void prev() {
		if (this.desde != 0) {
			this.desde -= 20;
			this.alertas = this.cargarAlertas();
		}
	}

	private String getLoginSession() {
		String out = "";
		return out;
	}

	public void crearAlertaUnoInformativa(String creador, String descripcion,
			String destino) throws Exception {
		MyPair tipo = this.getDtoUtil().getTipoAlertaUno();
		MyPair nivel = this.getDtoUtil().getNivelAlertaInformativa();
		this.crearAlerta(creador, nivel, tipo, descripcion, destino, destino);
	}

	public void crearAlertaMuchosInformativa(String creador,
			String descripcion, List<String> destino) throws Exception {
		MyPair tipo = this.getDtoUtil().getTipoAlertaMuchos();
		MyPair nivel = this.getDtoUtil().getNivelAlertaInformativa();
		// iterar sobre los detinos e ir creando una alerta para cada uno
		for (String dest : destino) {
			this.crearAlerta(creador, nivel, tipo, descripcion, dest, dest);
		}
	}

	public void crearAlertaUnoError(String creador, String descripcion,
			String destino) throws Exception {
		MyPair tipo = this.getDtoUtil().getTipoAlertaUno();
		MyPair nivel = this.getDtoUtil().getNivelAlertaError();
		this.crearAlerta(creador, nivel, tipo, descripcion, destino, destino);
	}

	public void crearAlertaMuchosError(String creador, String descripcion,
			List<String> destino) throws Exception {
		MyPair tipo = this.getDtoUtil().getTipoAlertaMuchos();
		MyPair nivel = this.getDtoUtil().getNivelAlertaError();
		// iterar sobre los detinos e ir creando una alerta para cada uno
		for (String dest : destino) {
			this.crearAlerta(creador, nivel, tipo, descripcion, dest, dest);
		}
	}

	public void crearAlertaComunitariaError(String creador, String descripcion,
			String[] destino, String[] propietario) throws Exception {
		MyPair tipo = this.getDtoUtil().getTipoAlertaComunitaria();
		MyPair nivel = this.getDtoUtil().getNivelAlertaError();
		String destinoStr = "";
		String propietarioStr = "";
		for (String dest : destino) {
			// crear string destino (entre comas)
			destinoStr += "," + dest + ",";
		}
		for (String prop : propietario) {
			// crear string propietario (entre comas)
			propietarioStr += "," + prop + ",";
		}
		this.crearAlerta(creador, nivel, tipo, descripcion, destinoStr,
				propietarioStr);
	}

	public void crearAlertaComunitariaInformativa(String creador,
			String descripcion, String[] destino, String[] propietario)
			throws Exception {
		MyPair nivel = this.getDtoUtil().getNivelAlertaError();
		MyPair tipo = this.getDtoUtil().getTipoAlertaComunitaria();
		String destinoStr = "";
		String propietarioStr = "";
		for (String dest : destino) {
			// crear string destino (entre comas)
			destinoStr += "," + dest + ",";
		}
		for (String prop : propietario) {
			// crear string propietario (entre comas)
			propietarioStr += "," + prop + ",";
		}
		this.crearAlerta(creador, nivel, tipo, descripcion, destinoStr,
				propietarioStr);
	}

	private void crearAlerta(String creador, MyPair nivel, MyPair tipo,
			String descripcion, String destino, String propietario)
			throws Exception {

		AlertaDTO alerta = new AlertaDTO();
		alerta.setNumero(AutoNumeroControl.getAutoNumeroKey(Config.NRO_ALERTA,
				7));
		alerta.setCreador(creador);
		alerta.setDestino(destino);
		alerta.setDescripcion(descripcion);
		alerta.setNivel(nivel);
		alerta.setTipo(tipo);
		alerta.setCancelada(false);
		alerta.setPropietario(propietario);

		this.grabarAlerta(alerta);
		// ojo, si destino es una lista, hay que desglosarlo
		this.refrescarAlertas(destino);
	}

	/*@Command
	@NotifyChange("*")
	public void crearAlertaRapida() throws Exception {
		ControlCrearAlerta ca = new ControlCrearAlerta();
		ca.show();
		if(ca.isClickAceptar()){
			AlertaDTO nuevaAlerta = ca.getNuevaAlerta();
			if (((String) nuevaAlerta.getTipo().getSigla())
					.compareTo(Config.SIGLA_TIPO_ALERTA_MUCHOS) == 0) {
				for (String d : ca.getListaDestinos()) {
					AlertaDTO alerta = nuevaAlerta;
					alerta.setNumero(AutoNumeroControl.getAutoNumeroKey(
							Config.NRO_ALERTA, 7));
					alerta.setDestino(d);
					this.grabarAlerta(alerta);
					this.refrescarAlertas(d);
				}
			} else {
				nuevaAlerta.setNumero(AutoNumeroControl.getAutoNumeroKey(
						Config.NRO_ALERTA, 7));
				this.grabarAlerta(nuevaAlerta);
				this.refrescarAlertas(nuevaAlerta.getDestino());
			}
			this.cargarAlertas();
		}
	}*/

	private void grabarAlerta(AlertaDTO alerta) throws Exception {
		String login = this.getLoginNombre();
		Register rr = Register.getInstance();
		AssemblerAlerta as = new AssemblerAlerta();
		rr.saveObject(as.dtoToDomain(alerta), login);
	}

	@Command
	@NotifyChange("*")
	public void cancelarAlerta() throws Exception {
		String login = this.getLoginNombre();
		String destino = this.selectedAlerta.getDestino();
		if (!this.selectedAlerta.isCancelada() && destino.contains(login)) {
			String obsv = this.getMotivoAnulacion();
			if (obsv.length() != 0) {
				this.selectedAlerta.setCancelada(true);
				this.selectedAlerta.setObservacion(obsv);
				this.grabarAlerta(this.selectedAlerta);
				this.refrescarAlertas(destino);
			}
		}
	}

	private void refrescarAlertas(String destino) {

		String[] ls = destino.split(",");
		for (int i = 0; i < ls.length; i++) {
			String login = ls[i];

			EventQueues.lookup(login, EventQueues.APPLICATION, true).publish(
					new Event(Config.ALERTAS, null, null));
		}
	}

}
