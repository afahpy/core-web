package com.coreweb.extras.barraProgreso;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.coreweb.control.GenericViewModel;
import com.coreweb.util.Misc;


public class BarraProgresoVM {

	
	Misc m = new Misc();
	
	ParteBarraProgreso pbpControl = null;

	Window window = null;
	public void show(int totalPartes, Component parent) {

		pbpControl = new ParteBarraProgreso();
		pbpControl.setTotal(totalPartes);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ParteBarraProgreso", pbpControl);

		window = (Window) Executions.createComponents(
				"/core/misc/barraProgreso.zul", null, map);

		//window.doModal();
		//window.doOverlapped();
		//window.doPopup();
		this.m.mensajeSiNoCancelar("Creo");
		window.doHighlighted();
		
	}

	public void nextParte() {
		pbpControl.nextParte();
	}

	public void cerrarBarraProgreso() {
		window.detach();
	}
	
	// ============== VIEW MODEL =================
/*
	@Wire
	private Progressmeter progress;

	@Wire
	private Timer timer;
*/
	// cuantos pasos son
	private ParteBarraProgreso pbpVM;

	@Init(superclass = true)
	public void initBarraProgresoVM(
			@ExecutionArgParam("ParteBarraProgreso") ParteBarraProgreso pbp) {
		this.pbpVM = pbp;
	}

	@AfterCompose(superclass = true)
	public void afterBarraProgresoVM() {
//		System.out.println("=================> afterBarraProgresoVM "+timer);
//		progress.setValue(0);
//		timer.start();
	}

	@Command
	@NotifyChange("*")
	public void actualizarBarra() throws InterruptedException {
//		timer.stop();
		int percent = pbpVM.getPorcentaje();
		System.out.println("==== percent: "+percent);
//		progress.setValue(percent);
//		timer.start();
	}

	public int getPorcentaje(){
		int percent = pbpVM.getPorcentaje();
		return percent;
	}

}

class ParteBarraProgreso {

	private int total = 0;
	private int cc = 0;

	public void nextParte() {
		this.cc++;
		System.out.println("=== nextParte: "+this.cc);
	}

	public int getPorcentaje(){
		return this.getCc() * 100 / this.getTotal();
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCc() {
		return cc;
	}

	public void setCc(int cc) {
		this.cc = cc;
	}
}

class BarraThread extends Thread {
	
	Window window = null;
	
	public BarraThread(Window window ){
		this.window = window;
	}
	
	
	public void run(){
		window.doModal();
	}
	
}

