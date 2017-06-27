package com.coreweb.extras.zul;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Intbox;

public class CoreWebIntBox extends Intbox{

	public CoreWebIntBox() {
		this.addEventListener(Events.ON_FOCUS, new CoreWebOnFocus());
	}
	
}
