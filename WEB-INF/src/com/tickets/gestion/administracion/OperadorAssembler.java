package com.tickets.gestion.administracion;

import com.coreweb.domain.Domain;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.tickets.domain.Operador;
import com.tickets.domain.RegisterDomain;

public class OperadorAssembler extends Assembler {

	private static String[] attIguales = {"usuario", "pass", "nombre", 
		"descripcion", "imagen"};
	
	private static String[] attServicio = {"descripcion", "letra", "estado"};
	
	private static String[] attPuesto = {"descripcion", "estado"};
	
	private static String[] attTarea = {"descripcion"};
	
	@Override
	public Domain dtoToDomain(DTO dto) throws Exception {
		Operador domain = (Operador) getDomain(dto, Operador.class);
		
		this.copiarValoresAtributos(dto, domain, attIguales);
		this.myPairToDomain(dto, domain, "estado");
		this.myPairToDomain(dto, domain, "sucursal");
		//if(((Operador)domain).getTareaCorriente()!=null){
		//this.myArrayToDomain(dto, domain, "tareaCorriente", true);
		//}
		this.listaMyArrayToListaDomain(dto, domain, "servicios");
		this.listaMyArrayToListaDomain(dto, domain, "puestos");
		
		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		OperadorDTO dto = (OperadorDTO) getDTO(domain, OperadorDTO.class);	
		
		this.copiarValoresAtributos(domain, dto, attIguales);
		this.domainToMyPair(domain, dto, "estado");
		this.domainToMyPair(domain, dto, "sucursal");
		//if(dto.getTareaCorriente()!=null){
			this.domainToMyArray(domain, dto, "tareaCorriente",attTarea);
		//}
		
		this.listaDomainToListaMyArray(domain, dto, "servicios", attServicio);
		this.listaDomainToListaMyArray(domain, dto, "puestos", attPuesto);
		
		return dto;
	}
	
	/**
	 * obtiene el operador a partir del login..
	 */
	public OperadorDTO getOperadorByLogin(String login) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		Operador operador = rr.getOperadorByLogin(login);
		
		if (operador != null) {
			return (OperadorDTO) this.domainToDto(operador);
		}		
		return null;
	}
}
