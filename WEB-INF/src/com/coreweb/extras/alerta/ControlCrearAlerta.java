package com.coreweb.extras.alerta;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.coreweb.Config;
import com.coreweb.IDCore;
import com.coreweb.componente.BuscarElemento;
import com.coreweb.componente.WindowPopup;
import com.coreweb.control.SoloViewModel;
import com.coreweb.domain.Usuario;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

public class ControlCrearAlerta extends SoloViewModel {

	@Init(superclass = true)
	public void initControlCrearAlerta() {

		this.tipos.add(this.getDtoUtil().getTipoAlertaUno());
		this.tipos.add(this.getDtoUtil().getTipoAlertaMuchos());
		this.cargarGrupos();
		this.cargarListas();

		this.alerta = new AlertaDTO();
		this.alerta.setCreador(this.getLoginNombre());
		this.alerta.setNivel(this.getDtoUtil().getNivelAlertaInformativa());
		this.alerta.setNumero("Sin asignar");
		this.alerta.setCancelada(false);
		this.alerta.setTipo(this.getDtoUtil().getTipoAlertaUno());
	}

	@AfterCompose(superclass = true)
	public void afterComposeControlCrearAlerta() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return IDCore.F_MIS_ALERTAS;
	}

	public AlertaDTO alerta;

	public AlertaDTO getAlerta() {
		return alerta;
	}

	public void setAlerta(AlertaDTO alerta) {
		this.alerta = alerta;
	}

	@Command
	@NotifyChange("*")
	public void buscarUsuario() {
		try {
			
			String filtro = this.alerta.getDestino();
			
			BuscarElemento be = new BuscarElemento();
			be.setTitulo("Buscar Usuario");
			be.setClase(Usuario.class);
			be.setAtributos(new String[] { "nombre", "login" });
			be.setNombresColumnas(new String[] { "Usuario nombre",
					"Usuario login" });
			be.addOrden("nombre");
			be.setWidth("400px");
			be.setHeight("500px");
			be.show((filtro.trim().length()==0 ? "%" : filtro), 0);
			
			if (be.isClickAceptar() == true) {
				String destino = (String) be.getSelectedItem().getPos2(); // login
				this.alerta.setDestino(destino);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String> destinos = new ArrayList<String>();
	public List<MyPair> tipos = new ArrayList<MyPair>();
	public MyPair selectedTipo = new MyPair();
	public List<String> grupos = new ArrayList<String>();
	public List<String> listas = new ArrayList<String>();
	public String selectedGrupo = "";
	public String selectedLista = "";
	public boolean habUsuario = true;
	public boolean habGrupo = true;

	public List<String> getDestinos() {
		return destinos;
	}

	public void setDestinos(List<String> destinos) {
		this.destinos = destinos;
	}

	public List<MyPair> getTipos() {
		return tipos;
	}

	public void setTipos(List<MyPair> tipos) {
		this.tipos = tipos;
	}

	public MyPair getSelectedTipo() {
		return selectedTipo;
	}

	public void setSelectedTipo(MyPair selectedTipo) {
		this.selectedTipo = selectedTipo;
	}

	public List<String> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<String> grupos) {
		this.grupos = grupos;
	}

	public List<String> getListas() {
		return listas;
	}

	public void setListas(List<String> listas) {
		this.listas = listas;
	}

	public String getSelectedGrupo() {
		return selectedGrupo;
	}

	public void setSelectedGrupo(String selectedGrupo) {
		this.selectedGrupo = selectedGrupo;
	}

	public String getSelectedLista() {
		return selectedLista;
	}

	public void setSelectedLista(String selectedLista) {
		this.selectedLista = selectedLista;
	}

	public boolean isHabUsuario() {
		return habUsuario;
	}

	public void setHabUsuario(boolean habUsuario) {
		this.habUsuario = habUsuario;
	}

	public boolean isHabGrupo() {
		return habGrupo;
	}

	public void setHabGrupo(boolean habGrupo) {
		this.habGrupo = habGrupo;
	}

	public void cargarGrupos() {
		// hacer el query y cargar
	}

	public void cargarListas() {
		// hacer el query y cargar
	}

	@Command
	@NotifyChange("*")
	public void agregarGrupo() {
		this.destinos.add(this.selectedGrupo);
	}

	@Command
	@NotifyChange("*")
	public void agregarLista() {
		this.destinos.add(this.selectedLista);
	}

	@Command
	@NotifyChange("*")
	public void actualizarTipo() {
		this.alerta.setTipo(this.selectedTipo);
		if (((String) this.selectedTipo.getSigla())
				.compareTo(Config.SIGLA_TIPO_ALERTA_UNO) == 0) {
			this.habUsuario = true;
			this.habGrupo = false;
		} else {
			this.habUsuario = false;
			this.habGrupo = true;
		}

	}

	private WindowPopup w;

	public void show() {
		try {
			w = new WindowPopup();
			w.setModo(WindowPopup.NUEVO);
			w.setTitulo("Crear Alerta");
			w.setWidth("670px");
			w.setHigth("460px");
			w.show(Config.CREAR_ALERTA_ZUL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isClickAceptar() {

		return w.isClickAceptar();
	}

	public AlertaDTO getNuevaAlerta() {
		return ((ControlCrearAlerta) this.w.getVm()).getAlerta();
	}

	public List<String> getListaDestinos() {
		return ((ControlCrearAlerta) this.w.getVm()).getDestinos();
	}

}
