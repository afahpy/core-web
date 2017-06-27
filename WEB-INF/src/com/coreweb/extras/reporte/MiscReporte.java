package com.coreweb.extras.reporte;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;

/**
 * La idea es poner acá esos métodos que son independientes que permiten crear
 * componentes. Los voy a ir poniendo de a poco.
 * 
 * @author daniel
 *
 */
public class MiscReporte {

	
	
	
	public JRDataSource createDataSource(CabeceraReporte cabecera, List<Object[]> data) throws Exception{
		return this.createDataSource(cabecera.getColumnasDS(), data);
	}

	
	public JRDataSource createDataSource(String[] titles, List<Object[]> data)
			throws Exception {

		if (data == null) {
			data = new ArrayList<Object[]>();
			Object[] aux = new Object[titles.length];
			data.add(aux);
		}

		DRDataSource dataSource = null;

		Constructor<DRDataSource> contructor = DRDataSource.class
				.getConstructor(String[].class);
		dataSource = contructor.newInstance((Object) titles);

		for (int i = 0; i < data.size(); i++) {
			// System.out.println("-------------------- "+i);
			Object[] objects = (Object[]) data.get(i);
			// si hay más datos que las columnas, hay que quitar los datos extras
			if (objects.length > titles.length){
				Object[] aux = new Object[titles.length];
				for (int j = 0; j < aux.length; j++) {
					aux[j] = objects[j];
				}
				objects = aux;
			}
			dataSource.add(objects);
		}

		return dataSource;

	}

}
