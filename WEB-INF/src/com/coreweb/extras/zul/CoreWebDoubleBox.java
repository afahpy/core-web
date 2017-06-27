package com.coreweb.extras.zul;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;

/**
 * Para que quede seleccionado el componente al tener focus, y mejorar la
 * expeiencia del usuario
 * 
 * @author daniel
 *
 */
public class CoreWebDoubleBox extends Doublebox {

	public CoreWebDoubleBox() {
		this.addEventListener(Events.ON_FOCUS, new CoreWebOnFocus());
	}

}

