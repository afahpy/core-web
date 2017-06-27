package com.coreweb.login;

import java.util.Iterator;
import java.util.List;

import com.coreweb.domain.*;
import com.coreweb.util.Misc;



public class LoginUsuario {

	private Misc m = new Misc();
	
	public LoginUsuarioDTO log(String login, String clave) throws Exception {
		clave = m.encriptar(clave);
		
		LoginUsuarioDTO lu = new LoginUsuarioDTO();
		Register rr = Register.getInstance();
		Usuario u = rr.getUsuario(login, clave);
		
		if (u != null) {
			lu.setLogeado(true);
			lu.setNombre(u.getNombre());
			lu.setLogin(u.getLogin());
			lu.setId(u.getId());

			int cp = 0;
			String[] nombrePerfiles = new String[u.getPerfiles().size()];
			String[] nombrePerfilesDesc = new String[u.getPerfiles().size()];

			
			// recorre las propiedades 
			Iterator<UsuarioPropiedad> iteProp = u.getUsuarioPropiedades().iterator();
			while(iteProp.hasNext()){
				UsuarioPropiedad up = iteProp.next();
				lu.addPropiedad(up.getClave().getDescripcion(), up.getValor());
			}
			
			
			// recorre los perfiles
			Iterator<Perfil> iteP = u.getPerfiles().iterator();
			while (iteP.hasNext()) {

				Perfil p = iteP.next();
				nombrePerfiles[cp] = p.getNombre(); 
				nombrePerfilesDesc[cp] = p.getDescripcion() + " ["+p.getGrupo()+"]"; 
				lu.addGrupo(p.getGrupo());
				
				// recorre los permisos, considera que el formulario y la operacion esten habilitadas
				Iterator<Permiso> itePerm = p.getPermisos().iterator();
				while (itePerm.hasNext()) {
					Permiso per = itePerm.next();

					String formAlias = per.getOperacion().getFormulario().getAlias();
					String formLabel = per.getOperacion().getFormulario().getLabel();
					String  formUrl = per.getOperacion().getFormulario().getUrl();
					boolean formHab = per.getOperacion().getFormulario().isHabilitado();
					boolean opeHab = per.getOperacion().isHabilitado();					
					String opeAlias = per.getOperacion().getAlias();
					boolean perHab = per.isHabilitado();
					
					// un formulario estará deshabilitado, está deshabilitado para todos
					
					lu.addFormulario(formAlias, formLabel, formUrl, formHab);
					lu.addOperacion(formAlias, opeAlias, (formHab && opeHab && perHab));
				}
				cp++;
			}		
			lu.setPerfiles(nombrePerfiles);
			lu.setPerfilesDescripcion(nombrePerfilesDesc);
			
			
			// recorre los formularios para registrar todos los alias
			List<Formulario> lf = rr.getAllFormulario();
			for (int i = 0; i < lf.size(); i++) {
				Formulario f = lf.get(i);
				lu.addAllFormulario(f.getAlias(), f.getLabel());
			}
			
		}
		
		return lu;
	}

	public void printLog(String login, String clave) throws Exception {
		Misc m = new Misc();
		clave = m.encriptar(clave);
		
		Register rr = Register.getInstance();
		Usuario u = rr.getUsuario(login, clave);
		System.out.println(u.toString());
		Iterator<Perfil> iteP = u.getPerfiles().iterator();
		while (iteP.hasNext()) {
			Perfil p = iteP.next();
			System.out.println(p.toString());
			Iterator<Permiso> itePerm = p.getPermisos().iterator();
			while (itePerm.hasNext()) {
				Permiso per = itePerm.next();
				System.out.println(per.toString());
			}

		}
	}

	public static void main(String[] args) {
		try {
			LoginUsuario lu = new LoginUsuario();
			lu.printLog("juan", "juansa");
			System.out.println("================================");
			lu.printLog("maria", "mariasa");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
