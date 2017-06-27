package com.coreweb.extras.reporte;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

public interface ReporteInterface {
	
	
	public void setDirectorioBase(String directorioBase);
	
	public String getDirectorioBase();
		
	public void setUsuario(String usuario);

	public void setEmpresa(String empresa);

	public void ejecutar(boolean mostrar);
	
	public String getUrlReporte();

	public String getTitulo();
	
	public boolean isBorrarDespuesDeVer();
	
	public String getArchivoPathReal();

	public MyReport getReporte();



}
