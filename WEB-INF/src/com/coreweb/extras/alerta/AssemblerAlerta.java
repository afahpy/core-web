package com.coreweb.extras.alerta;

import java.util.Iterator;
import java.util.List;

import com.coreweb.domain.AgendaEvento;
import com.coreweb.domain.Alerta;
import com.coreweb.domain.Domain;
import com.coreweb.domain.Register;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.agenda.AgendaEventoDTO;
import com.coreweb.extras.agenda.AgendaEventoDetalleDTO;
import com.coreweb.extras.agenda.AssemblerAgenda;

public class AssemblerAlerta extends Assembler {

	private static String[] attAlerta = { "numero", "fechaCreacion", "fechaCancelacion",
			"creador", "destino", "descripcion", "observacion", "cancelada", "propietario"};

	public Domain dtoToDomain(DTO dtoG) throws Exception {

		AlertaDTO dto = (AlertaDTO) dtoG;
		Alerta domain = (Alerta) getDomain(dto, Alerta.class);
		this.copiarValoresAtributos(dto, domain, attAlerta);
		this.myPairToDomain(dto, domain, "nivel");
		this.myPairToDomain(dto, domain, "tipo");

		return domain;
	}

	public AlertaDTO domainToDto(Domain domain) throws Exception {

		AlertaDTO dto = (AlertaDTO) getDTO(domain, AlertaDTO.class);

		this.copiarValoresAtributos(domain, dto, attAlerta);
		this.domainToMyPair(domain, dto, "nivel");
		this.domainToMyPair(domain, dto, "tipo");

		return dto;
	}

	public static void main(String[] args) {
		try {

			Register rr = Register.getInstance();
			/*Alerta alerta = rr.getAlerta(1);
			AssemblerAlerta ass = new AssemblerAlerta();
			AlertaDTO dto = ass.domainToDto(alerta);
			System.out.println("fechaCreacion:" + dto.getFechaCreacion());
			System.out.println("destino:" + dto.getDestino());
			System.out.println("tipo:" + dto.getTipo().getText());
			System.out.println("nivel:" + dto.getNivel().getText());*/
			List<Alerta> alertas = rr.getAllAlertas(0, 50, ",juan,");
			//System.out.println(alertas);
			for (Alerta alerta : alertas) {
				System.out.println("alerta" + alerta.getDestino());
			}
			int cant = rr.getCantidadAlertasNoCanceladas("juan");
			System.out.println("Cantidad de alertas no canceladas: "+cant);


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
