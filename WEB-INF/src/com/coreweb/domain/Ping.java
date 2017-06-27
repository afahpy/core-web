package com.coreweb.domain;

public class Ping extends Domain {

	private String echo = "";
	
	public Ping(){
	}

	public String getEcho() {
		return echo;
	}

	public void setEcho(String echo) {
		this.echo = echo;
	}
	
	public int compareTo(Object o1) {
		Ping cmp = (Ping)o1;
		return this.echo.compareTo(cmp.echo);
    }

}
