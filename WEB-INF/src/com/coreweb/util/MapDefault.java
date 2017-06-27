package com.coreweb.util;


import java.io.Serializable;
import java.util.*;

public class MapDefault implements Serializable{
	// toma un valor por default en el constructor 

	private Object porDefecto = null;
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public MapDefault(Object porDefecto){
		this.porDefecto = porDefecto;
	}
	
	public void put(String key, Object value){
		this.map.put(key, value);
	}
	
	public Object get(String key){
		Object b = this.map.get(key);
		if (b == null){
			b = this.porDefecto;
		}
		return b;
	}
}
