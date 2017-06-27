package com.coreweb.login;


import java.util.*;

import com.coreweb.Config;
import com.coreweb.dto.DTO;
import com.coreweb.util.MapDefault;

public class LoginUsuarioDTO extends DTO {

	private static String NO_LABEL = "label no definido";
	// empieza siempre deslogeado
	private boolean logeado = false;
	private String login = "login";
	private String nombre = "nombre";
	
	private String[] perfiles = {"Error de Perfil"};
	private String[] perfilesDescripcion = {"Error descripcion de Perfil"};
	
	private MapDefault formHabilitado = new MapDefault(false);
	private MapDefault formLabelH = new MapDefault(NO_LABEL);
	private MapDefault formUrl = new MapDefault("/inicio/errorurl.zul");
	private MapDefault permOperacion = new MapDefault(false);
	private MapDefault grupos = new MapDefault(false);
	private MapDefault allLabels = new MapDefault("error label");
	
	

	public boolean esGrupo(String grupo){
		return (boolean)this.grupos.get(grupo);
	}
	
	public void addGrupo(String grupo){
		this.grupos.put(grupo, true);
	}

	
	//************* Formulario 
	
	public void addFormulario(String formAlias, String formLabel, String url, boolean habilitado){
		// Un usuario con varios perfiles 
		boolean formOld = (boolean)this.formHabilitado.get(formAlias);

		this.formHabilitado.put(formAlias, (formOld || habilitado));
		this.formUrl.put(formAlias, url);
		this.formLabelH.put(formAlias, formLabel);
	}
	
	public String formLabel(String formAlias){
		String label = (String)this.formLabelH.get(formAlias);
		if (NO_LABEL.compareTo(label)==0){
			label = (String)this.allLabels.get(formAlias);
		}
		return label;
	}

	public String formURL(String formAlias){
		String out = "";
		out = (String)this.formUrl.get(formAlias);
		return out;
		//return (String)this.formUrl.get(formAlias);
	}
	

	public void addAllFormulario(String formAlias, String formLabel){
		this.allLabels.put(formAlias, formLabel);
	}
	
	
	public String alerta(){
		String out = "";
		out = "alert(hola)";
		return out;
	}

	public boolean formDeshabilitado(String formAlias){
			
		if (formAlias.compareTo(Config.ALIAS_HABILITADO_SI_O_SI)==0){
			return false;
		}
		return !(boolean)this.formHabilitado.get(formAlias);
	}
	
	
	// ************* Operacion 
	public void addOperacion(String formAlias, String opeAlias, boolean habilitado){
		// si ya tienen un permiso deja el true
		boolean opOld = this.opeHabilitada(formAlias, opeAlias);
		this.permOperacion.put(formAlias+opeAlias, (opOld || habilitado));
	}

	public boolean opeHabilitada(String formAlias, String opeAlias){
		boolean b = (boolean)this.permOperacion.get(formAlias+opeAlias);		
		return b;
	}
	//************************
	
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String[] getPerfiles() {
		return perfiles;
	}
	public void setPerfiles(String[] perfiles) {
		this.perfiles = perfiles;
	}

	

	public String[] getPerfilesDescripcion() {
		return perfilesDescripcion;
	}

	public void setPerfilesDescripcion(String[] perfilesDescripcion) {
		this.perfilesDescripcion = perfilesDescripcion;
	}

	public boolean isLogeado() {
		return logeado;
	}
	public void setLogeado(boolean logeado) {
		this.logeado = logeado;
	}

	
	// definiciones
	private Hashtable<String, List<String>> usuarioPropiedades = new Hashtable<String, List<String>>();
	
	public void addPropiedad(String key, String valor){
		List<String> l = this.usuarioPropiedades.get(key);
		if (l == null){
			l = new ArrayList<String>();
			this.usuarioPropiedades.put(key, l);
		}
		l.add(valor);
	}
	

    /**
     * Retorna una propiedad. Se supone que sólo hay una propiedad para esta clave.
     * Si la propiedad no existe, lanza una excepción.
     * @param key
     * @return
     */
	public String getPropiedad(String key) throws Exception{
		String out = "";
		try {
			List<String> l = this.usuarioPropiedades.get(key);
			if (l.size() != 1){
				throw new Exception ("No existe la propiedad \""+key+"\" o hay más de un valor definido para ella. Cantidad ("+l.size()+")");
			}else{
				out = l.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return out;
	}
	
	/**
	 * Retorna la lista de propiedades para una clave dada.
	 * Si no encuentra la propiedad, retormna la lista vacía.
	 * @param key
	 * @return
	 */
	public List<String> getPropiedades(String key){
		List<String> l = this.usuarioPropiedades.get(key);
		if (l == null){
			l = new ArrayList<String>();
		}
		return l;
	}
	
	/**
	 * Busca la propiedad y hace el parcer a double.
	 * Lanza una exepción si no puede hacer el parser.
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public double getPropiedadDouble(String key) throws Exception {
		double out = 0.0;
		String pro = this.getPropiedad(key);
		out = Double.parseDouble(pro);			
		return out;
	}

	
}




