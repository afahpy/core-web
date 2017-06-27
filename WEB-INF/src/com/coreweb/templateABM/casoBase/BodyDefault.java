package com.coreweb.templateABM.casoBase;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.coreweb.Config;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.templateABM.Body;



public class BodyDefault extends Body {

	

	@Init(superclass = true)
	public void initBodyDefault() {	
		//this.setAliasFormularioCorriente(Config.ALIAS_HABILITADO_SI_O_SI);
		this.getPagina().setAliasFormularioCorriente(Config.ALIAS_HABILITADO_SI_O_SI);
	}

	
	@AfterCompose(superclass = true)
	public void afterComposeBodyDefault(){
		
	}

	
	@Override
	public DTO getDTOCorriente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		// TODO Auto-generated method stub
	}

	@Override
	public DTO nuevoDTO() {
		return null;
	}

	@Override
	public String getEntidadPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DTO> getAllModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Assembler getAss() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verificarAlGrabar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String textoErrorVerificarGrabar() {
		// TODO Auto-generated method stub
		return "BodyDefault.textoErrorVerificarGrabar";
	}


}
