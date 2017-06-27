package com.coreweb.extras.zul;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Longbox;

public class CoreWebLongBox extends Longbox {

	public CoreWebLongBox() {
		this.addEventListener(Events.ON_FOCUS, new CoreWebOnFocus());
	}

}
