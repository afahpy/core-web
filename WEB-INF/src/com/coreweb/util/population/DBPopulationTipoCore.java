package com.coreweb.util.population;

import com.coreweb.Config;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.domain.Tipo;
import com.coreweb.domain.TipoTipo;

public class DBPopulationTipoCore {

	private Register rr = Register.getInstance();

	private void grabarDB(Domain d) throws Exception {
		// poner acá un control para que no se carguen dos veces los mismos
		// tipos o tipostipos. Esto ayuda para que se pueda correr el carga
		// tipos sin problema a que se pierdan datos.
		rr.saveObject(d, "sys");
	}

	/**
	 * Funcion genérica para cargar los tipos y tiposTipos. En el arreglo, en la
	 * posición 0 está la descripción del TipoTipo, y luego siempre hay 2
	 * lugares por cada tipo. Si el segundo lugar es cero, entonces se usa el
	 * mismo dato de sigla para la descripción del tipo
	 * 
	 * @param tipoTipoDescripcion
	 * @param tipos
	 * @throws Exception
	 */
	public void cargarTipos(String[] tipos) throws Exception {

		// tipoTipo
		String tipoTipoDescripcion = tipos[0];

		TipoTipo tipoTipo = new TipoTipo();
		tipoTipo.setDescripcion(tipoTipoDescripcion);
		grabarDB(tipoTipo);

		// los tipos

		for (int i = 1; i < tipos.length; i = i + 2) {
			String sigla = tipos[i];
			String descripcion = tipos[i + 1];
			if (descripcion.trim().length() == 0) {
				descripcion = sigla;
			}

			Tipo tipo = new Tipo();
			tipo.setSigla(sigla);
			tipo.setDescripcion(descripcion);
			tipo.setTipoTipo(tipoTipo);
			grabarDB(tipo);
		}
	}

	public void cargarTiposCore() throws Exception {

		/**************************
		 * Tipo y Nivel Alerta
		 ************************/
		cargarTipos(Config.ARR_TIPO_ALERTAS);
		cargarTipos(Config.ARR_TIPO_NIVEL_ALERTAS);

	}

}
