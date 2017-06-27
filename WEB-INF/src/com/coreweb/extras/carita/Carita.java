package com.coreweb.extras.carita;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.coreweb.domain.Register;
import com.coreweb.domain.Tecorei;



public class Carita  extends SelectorComposer<Component> {
	
	
	@Wire
	Window winCarita;

	@Listen("onClick = #cerrarCarita")
	public void cerrarCarita(Event e) {
		winCarita.detach();
	}

	
	
	public void mostrarCarita() {

		try {

			Register rr = Register.getInstance();
			Tecorei te = rr.getTecorei();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("chiste", te.getTexto());

			
			Window window = (Window) Executions.createComponents(
					"/core/misc/carita.zul", null, map);
			window.doModal();

		} catch (Exception e) {
			// mostrar mensaje de error
			e.printStackTrace();
		}
	}

}
