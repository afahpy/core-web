package com.coreweb.templateABM.casoBase;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;

import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.templateABM.Body;



public class BodyPrueba extends Body{


	private BodyDatoDto dato = new BodyDatoDto();
	
	private boolean noapellido = true;
	
	@AfterCompose(superclass=true)
    public void afterCompose(){
    }

	
	@Init(superclass = true)
	public void init() {
	}

	public BodyDatoDto getDato(){
		return dato;
	}
	
	@Override
	public DTO getDTOCorriente() {
		// TODO Auto-generated method stub
		return dato;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.dato = (BodyDatoDto) dto;
		
	}

	@Override
	public DTO nuevoDTO() {
		return new BodyDatoDto();
		
	}


	public boolean isNoapellido() {
		return noapellido;
	}

	public boolean getNoapellido() {
		return noapellido;
	}

	public void setNoapellido(boolean noapellido) {
		this.noapellido = noapellido;
	}


	@Override
	public String getEntidadPrincipal() {
		return "----------- entidad no definida------";
	}


	@Override
	public List<DTO> getAllModel() {
		// TODO Auto-generated method stub
		List<DTO> ldto = new ArrayList<DTO>();
		for (int i = 0; i < 10; i++) {
			BodyDatoDto dto1 = new BodyDatoDto();
			dto1.setApellido("Ape "+i);
			dto1.setNombre("Nomb "+i);
			dto1.setRuc("RUC "+i);
			ldto.add(dto1);
		}
		return ldto;
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
		return "BodyPrueba.textoErrorVerificarGrabar";
	}






	

}
