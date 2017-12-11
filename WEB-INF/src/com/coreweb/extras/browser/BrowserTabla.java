package com.coreweb.extras.browser;

import java.util.ArrayList;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.dto.Assembler;
import com.coreweb.util.MyArray;

public class BrowserTabla extends Control {

	String APP = Config.APP_NAME;
	Component mainComp = null;

	String v1 = APP + "/core/browser2/DataTables/DataTables-1.10.16/css/jquery.dataTables.css";
	String v2 = APP + "/core/browser2/DataTables/datatables.min.js";
	String v3 = APP + "/core/browser2/DataTables/jQuery-3.2.1/jquery-3.2.1.min.js";
	String v4 = APP + "/core/browser2/DataTables/DataTables-1.10.16/js/jquery.dataTables.min.js";

	public BrowserTabla() {
		super(null);
	}

	public BrowserTabla(Assembler ass) {
		super(ass);
		// TODO Auto-generated constructor stub
	}

	BrowserTablaDatosInterface datos = null;

	
	@Init(superclass = true)
	public void initBrowserTabla(@ExecutionParam("datos") Object obj, 
			@ContextParam(ContextType.VIEW) Component view) {
		mainComp = view;
		
		System.out.println("\n\n\n\n\nv1: "+v1+"\n\n\n\n\n");
		
		// System.out.println("init de BrowserTabla (obj):" + obj);
		BrowserTablaDatosInterface datos = (BrowserTablaDatosInterface) obj;
		this.datos = datos;
	}

	@AfterCompose(superclass = true)
	public void afterComposeBrowserTabla() {
		// System.out.println("after de BrowserTabla");
	}

	
	@GlobalCommand
	@NotifyChange("*")
	public void refrescarDatosTabla() {
		
		
		this.mensajePopupTemporalWarning("Sin implementar aun");
		if (1==1){
			return;
		}

		try {

			Include inc = (Include) mainComp.getFellow("idBrowserTable", true);
			
			String src = inc.getSrc();
			inc.setSrc(null);
			inc.setMode("defer");
			inc.setSrc(src);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			this.mensajePopupTemporalWarning("Error al refrescar los datos\n" + e.getMessage());
		}
	}
	
	
	
	public BrowserTablaDatosInterface getDatos() {
		return datos;
	}

	public void setDatos(BrowserTablaDatosInterface datos) {
		this.datos = datos;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getV2() {
		return v2;
	}

	public void setV2(String v2) {
		this.v2 = v2;
	}

	public String getV3() {
		return v3;
	}

	public void setV3(String v3) {
		this.v3 = v3;
	}

	public String getV4() {
		return v4;
	}

	public void setV4(String v4) {
		this.v4 = v4;
	}

}
