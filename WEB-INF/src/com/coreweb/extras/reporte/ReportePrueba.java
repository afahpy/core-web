package com.coreweb.extras.reporte;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;


import com.coreweb.extras.reporte.DatosColumnas;


public class ReportePrueba extends DatosReporte {

	
	String nombre = "";
	String apellido1 = "";
	String apellido2 = "";
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}


	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}



	// define las columnas del reporte
	static DatosColumnas col1 = new DatosColumnas("Fecha", TIPO_DATE);
	static DatosColumnas col2 = new DatosColumnas("Item",  TIPO_STRING);
	static DatosColumnas col3 = new DatosColumnas("Col3", TIPO_INTEGER);
	static DatosColumnas col4 = new DatosColumnas("Col4", TIPO_LONG, true);
	static DatosColumnas col5 = new DatosColumnas("Col5", TIPO_INTEGER, 50);
	static DatosColumnas col6 = new DatosColumnas("Col6", TIPO_DOUBLE, 50, true);
//ok	static DatosColumnas col6 = new DatosColumnas("Col6", TIPO_INTEGER, 50, true);

	
	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	
	static{
		col3.setAlineacionColuman(COLUMNA_ALINEADA_CENTRADA);
		col2.setAlineacionColuman(COLUMNA_ALINEADA_IZQUIERDA);
		col5.setAncho(80);
		col3.setTotaliza(true);
		
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
	}
	
	
	
	public void informacionReporte() {
		
		// titulo del reporte
		this.setTitulo("Reporte de Prueba");
		
		// archivo de salida
		this.setDirectorio("./");
		this.setDirectorioBase("./");
		this.setNombreArchivo("venta.pdf");

		// columnas del reporte
		this.setTitulosColumnas(cols);
		this.setBody(this.getCabecera());
	}
	
	
	private ComponentBuilder getCabecera(){
		VerticalListBuilder out = null;
		
		out = cmp.verticalList();
		
		HorizontalListBuilder f1 = cmp.horizontalList();
		f1.add(this.textoParValor("Nombre", this.nombre));
		
		//f1.add(this.texto("Nombre:"));
		//f1.add(this.texto(this.nombre));
			
		HorizontalListBuilder f2 = cmp.horizontalList();
		f2.add(this.textoParValor("Apellido 1", this.apellido1));
		f2.add(this.textoParValor("Apellido 2", this.apellido2));
		
		//f2.add(this.texto("Apellido:"));
		//f2.add(this.texto(this.apellido1));
		//f2.add(this.texto(this.apellido2));
		
		out.add(f1);
		out.add(f2);
		
		return out;
	}
	
	
	
	public static void main(String[] args) {
		
	
		List<Object[]> data = new ArrayList<>();

		Object[] obj = new Object[6];

		for (int i = 1; i < 40; i++) {
			obj = new Object[6];
			obj[0] = new Date(System.currentTimeMillis());
			obj[1] = new String("Dato "+i);
			obj[2] = 2 + i;
			obj[3] = 3L + i;
			obj[4] = (4 + i);
			obj[5] = (double)(5 + i + 1.34);
			data.add(obj);	
		}
		
		ReportePrueba dr = new ReportePrueba();
		dr.informacionReporte();
		dr.setDatosReporte(data);
		dr.setNombre("Daniel");
		dr.setApellido1("Romero");
		dr.setApellido2("no hay");
		
		dr.ejecutar(true);

	}


	@Override
	public void setDatosReportes() {
		// TODO Auto-generated method stub
		
	}



}
