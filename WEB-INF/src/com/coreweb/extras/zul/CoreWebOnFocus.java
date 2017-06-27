package com.coreweb.extras.zul;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.impl.InputElement;

public class CoreWebOnFocus implements EventListener {

	@Override
	public void onEvent(Event arg0) throws Exception {
		((InputElement) arg0.getTarget()).select();
	}

}
