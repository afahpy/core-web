package com.coreweb.extras.browser;

import java.util.Hashtable;

public interface BrowserTablaDatosInterface {

	
	public String[] getNombres();
	
	public String getQuery();
	
	public Hashtable<String, Object> getQueryParametros();
	
}
