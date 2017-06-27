package com.coreweb.extras.agenda;

import java.util.Iterator;

import com.coreweb.domain.AgendaEvento;
import com.coreweb.domain.AgendaEventoDetalle;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;



public class AssemblerAgenda extends Assembler {

	private static String[] attAgenda = {"fecha", "tipo", "key"};

	public Domain dtoToDomain(DTO dtoG) throws Exception {
		
		AgendaEventoDTO dto = (AgendaEventoDTO) dtoG;
		AgendaEvento domain = (AgendaEvento) getDomain(dto, AgendaEvento.class);
		this.copiarValoresAtributos(dto, domain, attAgenda);

		this.listaDTOToListaDomain(dto, domain, "agendaEventoDetalles", true, true,
				new AssemblerAgendaDetalle());
		
		return domain;
	}

	
	public AgendaEventoDTO domainToDto(Domain domain) throws Exception {

		AgendaEventoDTO dto = (AgendaEventoDTO) getDTO(domain, AgendaEventoDTO.class);

		this.copiarValoresAtributos(domain, dto, attAgenda);
		this.listaDomainToListaDTO(domain, dto, "agendaEventoDetalles",
				new AssemblerAgendaDetalle());
		
		
		
		return dto;
	}

	
	
	public static void main(String[] args) {
		try {
			
			Register rr = Register.getInstance();
			AgendaEvento ag = rr.getAgenda(1, "2013:02:01-2");
			AssemblerAgenda ass = new AssemblerAgenda();
			AgendaEventoDTO dto = ass.domainToDto(ag);
			System.out.println("key:"+dto.getKey());
			System.out.println("tipo:"+dto.getTipo());
			for (Iterator iterator = dto.getAgendaEventoDetalles().iterator(); iterator.hasNext();) {
				AgendaEventoDetalleDTO deta = (AgendaEventoDetalleDTO) iterator.next();
				
				System.out.println(deta.getFechaHora() +" - "+ deta.getTexto());
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}


class AssemblerAgendaDetalle extends Assembler {

	
	private static String[] attAgendaDetalle = {"fecha", "texto", "tipo", "link", "usuario" };
	
	@Override
	public Domain dtoToDomain(DTO dtoG) throws Exception {
		AgendaEventoDetalleDTO dto = (AgendaEventoDetalleDTO) dtoG;
		AgendaEventoDetalle domain = (AgendaEventoDetalle) getDomain(dto, AgendaEventoDetalle.class);
		this.copiarValoresAtributos(dto, domain, attAgendaDetalle);

		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {

		AgendaEventoDetalleDTO dto = (AgendaEventoDetalleDTO) getDTO(domain, AgendaEventoDetalleDTO.class);
		this.copiarValoresAtributos(domain, dto, attAgendaDetalle);

		return dto;
	}
	
}