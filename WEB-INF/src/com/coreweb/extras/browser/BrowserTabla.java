package com.coreweb.extras.browser;

import java.util.ArrayList;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.dto.Assembler;
import com.coreweb.util.MyArray;

public class BrowserTabla extends Control {

	String prefix = (String) this.getAtributoSession(Config.PREFIX);

	String v1 = "/" + prefix + "/core/browser2/DataTables/DataTables-1.10.16/css/jquery.dataTables.css";
	String v2 = "/" + prefix + "/core/browser2/DataTables/datatables.min.js";
	String v3 = "/" + prefix + "/core/browser2/DataTables/jQuery-3.2.1/jquery-3.2.1.min.js";
	String v4 = "/" + prefix + "/core/browser2/DataTables/DataTables-1.10.16/js/jquery.dataTables.min.js";

	public BrowserTabla() {
		super(null);
	}

	public BrowserTabla(Assembler ass) {
		super(ass);
		// TODO Auto-generated constructor stub
	}

	BrowserTablaDatosInterface datos = null;

	@Init(superclass = true)
	public void initBrowserTabla(@ExecutionParam("datos") Object obj) {
		// System.out.println("init de BrowserTabla (obj):" + obj);
		BrowserTablaDatosInterface datos = (BrowserTablaDatosInterface) obj;
		this.datos = datos;
	}

	@AfterCompose(superclass = true)
	public void afterComposeBrowserTabla() {
		// System.out.println("after de BrowserTabla");
	}

	public BrowserTablaDatosInterface getDatos() {
		return datos;
	}

	public void setDatos(BrowserTablaDatosInterface datos) {
		this.datos = datos;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
