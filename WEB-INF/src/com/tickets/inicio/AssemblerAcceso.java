package com.tickets.inicio;

import com.coreweb.domain.Domain;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.tickets.domain.AccesoApp;
import com.tickets.domain.RegisterDomain;

public class AssemblerAcceso extends Assembler {

	@Override
	public Domain dtoToDomain(DTO dto) throws Exception {
		AccesoApp dom = (AccesoApp) getDomain(dto, AccesoApp.class);
		
		this.myArrayToDomain(dto, dom, "usuario");
		
		return dom;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		AccesoDTO dto = (AccesoDTO) getDTO(domain, AccesoDTO.class);
		
		this.domainToMyArray(domain, dto, "usuario", new String [] {"nombre", "login"});		
		
		return dto;
	}

	public DTO obtenerAccesoDTO(String login) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		AccesoApp acceso = (AccesoApp) rr.getAcceso(login);
		
		AssemblerAcceso as = new AssemblerAcceso();
		AccesoDTO aDto = (AccesoDTO) as.domainToDto(acceso);
		
		return aDto;
	}	
	
}
