package com.tickets.gestion.panel;

import com.coreweb.domain.Domain;
import com.coreweb.domain.IiD;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.tickets.domain.Cliente;
import com.tickets.domain.Derivado;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Turno;

public class TurnoAssembler extends Assembler {

	private static String[] attIguales = { "numero", "descripcion", "creacion",
			"prioridad", "inicioAtencion", "finAtencion", "remitido" };

	private static String[] attCliente = { "descripcion", "cedula", "ruc", "nuevo"};

	private static String[] attServicio = { "descripcion", "letra", "estado" };

	private static String[] attDerivado = { "turnoPrevio", "horaDerivado" };

	public TurnoDTO getTurnoDto(IiD dato) throws Exception {

		TurnoDTO dto = (TurnoDTO) this.getDTObyId(Turno.class.getName(),
				dato.getId());
		return dto;
	}

	public void derivarTurno(TurnoDTO turnoDesde, TurnoDTO turnoHasta,
			String login) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();

		Turno th = (Turno) this.dtoToDomain(turnoHasta);
		rr.saveObject(th, login);

		Derivado d = new Derivado();
		d.setTurnoProximo(th);
		rr.saveObject(d, login);

		Turno td = (Turno) this.dtoToDomain(turnoDesde);
		td.setDerivado(d);
		rr.saveObject(td, login);

	}

	@Override
	public Domain dtoToDomain(DTO dtoOri) throws Exception {
		TurnoDTO dto = (TurnoDTO) dtoOri;
		Turno domain = (Turno) getDomain(dto, Turno.class);

		this.copiarValoresAtributos(dto, domain, attIguales);
		this.myPairToDomain(dto, domain, "estado");
		this.myPairToDomain(dto, domain, "sucursal");
//		this.myArrayToDomain(dto, domain, "cliente");
	
		// saber si el cliente es nuevo o no, para crearlo
		boolean clienteNuevo = dto.getCliente().esNuevo();
		if (clienteNuevo == true){
			MyArray clMa = dto.getCliente();
			Cliente cl = new Cliente();
			cl.setDescripcion((String)clMa.getPos1());
			cl.setCedula((String)clMa.getPos2());
			cl.setRuc((String)clMa.getPos3());
			cl.setNuevo(true);
			RegisterDomain rr = RegisterDomain.getInstance();
			rr.saveObject(cl, this.getLogin());
			clMa.setId(cl.getId());
		}
		
		this.myArrayToDomain(dto, domain, "cliente", false);

		this.myArrayToDomain(dto, domain, "servicio");

		this.myArrayToDomain(dto, domain, "derivado");

		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		TurnoDTO dto = (TurnoDTO) getDTO(domain, TurnoDTO.class);

		this.copiarValoresAtributos(domain, dto, attIguales);
		this.domainToMyPair(domain, dto, "estado");
		this.domainToMyPair(domain, dto, "sucursal");
		this.domainToMyArray(domain, dto, "cliente", attCliente);

		this.domainToMyArray(domain, dto, "servicio", attServicio);

		this.domainToMyArray(domain, dto, "derivado", attDerivado);

		return dto;
	}

	public static void main(String[] args) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();

		Turno t = (Turno) rr.getObject(Turno.class.getName(), 15);
		
		TurnoAssembler ass = new TurnoAssembler();
		TurnoDTO d = (TurnoDTO) ass.domainToDto(t);
		
		MyArray ma = new MyArray("Nuevo", "Nuevo", "Nuevo", true);
		ma.setId((long)-1);
		d.setCliente(ma);
		
		Turno t2 = (Turno) ass.dtoToDomain(d);
		
		System.out.println(d.getCreacion());
		

	}

}
