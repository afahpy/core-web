package com.coreweb.extras.browser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import org.zkoss.zk.ui.IdSpace;
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
	String v5 = APP + "/core/browser2/DataTables/DataTables-1.10.16/css/jquery.dataTables.min.css";

	String v2 = APP + "/core/browser2/DataTables/datatables.min.js";
	String v3 = APP + "/core/browser2/DataTables/jQuery-3.2.1/jquery-3.2.1.min.js";
	String v4 = APP + "/core/browser2/DataTables/DataTables-1.10.16/js/jquery.dataTables.min.js";
	String v6 = APP + "/core/browser2/DataTables/code_jquery_com/jquery-1.12.4.js";

	String v7 = APP + "/core/browser2/DataTables/Buttons-1.4.2/js/dataTables.buttons.min.js";
	String v8 = APP + "/core/browser2/DataTables/Buttons-1.4.2/js/buttons.flash.min.js";
	String v9 = APP + "/core/browser2/DataTables/Buttons-1.4.2/js/buttons.html5.min.js";
	String v10 = APP + "/core/browser2/DataTables/Buttons-1.4.2/js/buttons.print.min.js";

	
	String v11 = APP + "/core/browser2/DataTables/otros/jszip.min.js";
	String v12 = APP + "/core/browser2/DataTables/otros/pdfmake.min.js";
	String v13 = APP + "/core/browser2/DataTables/otros/vfs_fonts.js";
	String v14 = APP + "/core/browser2/DataTables/Buttons-1.4.2/css/buttons.dataTables.min.css";
	String v15 = APP + "/core/browser2/DataTables/Buttons-1.4.2/js/buttons.colVis.min.js";

	
	
/*	
/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/Buttons-1.4.2/js/dataTables.buttons.min.js
https://cdn.datatables.net/buttons/1.5.1/js/dataTables.buttons.min.js

/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/Buttons-1.4.2/js/buttons.flash.min.js
https://cdn.datatables.net/buttons/1.5.1/js/buttons.flash.min.js

	/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/otros/jszip.min.js
https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js

	/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/otros/pdfmake.min.js
https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/pdfmake.min.js

	/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/otros/vfs_fonts.js	
https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/vfs_fonts.js


/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/Buttons-1.4.2/js/buttons.html5.min.js
https://cdn.datatables.net/buttons/1.5.1/js/buttons.html5.min.js

/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/Buttons-1.4.2/js/buttons.print.min.js
https://cdn.datatables.net/buttons/1.5.1/js/buttons.print.min.js	


no - https://code.jquery.com/jquery-1.12.4.js
ya - https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js

	
ya https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css

/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/Buttons-1.4.2/css/buttons.dataTables.min.css
https://cdn.datatables.net/buttons/1.5.1/css/buttons.dataTables.min.css	
	
	
/home/daniel/datos/afah/proyectos/coreweb/core-web/core/browser2/DataTables/Buttons-1.4.2/js/buttons.colVis.min.js
https://cdn.datatables.net/buttons/1.5.1/js/buttons.colVis.min.js	
	
*/
	
	
//	https://code.jquery.com/jquery-1.12.4.js
	
	
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
		
//		System.out.println("\n\n\n\n\nv1: "+v1+"\n\n\n\n\n");
		
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
		try {
			Include inc = (Include) this.getComponenteById(this.datos.getIdInclude());		
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

	public String getV5() {
		return v5;
	}

	public void setV5(String v5) {
		this.v5 = v5;
	}

	public String getV6() {
		return v6;
	}

	public void setV6(String v6) {
		this.v6 = v6;
	}

	public String getV7() {
		return v7;
	}

	public void setV7(String v7) {
		this.v7 = v7;
	}

	public String getV8() {
		return v8;
	}

	public void setV8(String v8) {
		this.v8 = v8;
	}

	public String getV9() {
		return v9;
	}

	public void setV9(String v9) {
		this.v9 = v9;
	}

	public String getV10() {
		return v10;
	}

	public void setV10(String v10) {
		this.v10 = v10;
	}

	public String getV11() {
		return v11;
	}

	public void setV11(String v11) {
		this.v11 = v11;
	}

	public String getV12() {
		return v12;
	}

	public void setV12(String v12) {
		this.v12 = v12;
	}

	public String getV13() {
		return v13;
	}

	public void setV13(String v13) {
		this.v13 = v13;
	}

	public String getV14() {
		return v14;
	}

	public void setV14(String v14) {
		this.v14 = v14;
	}

	public String getV15() {
		return v15;
	}

	public void setV15(String v15) {
		this.v15 = v15;
	}

}
