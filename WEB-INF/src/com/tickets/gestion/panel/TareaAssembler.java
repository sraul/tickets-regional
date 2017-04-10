package com.tickets.gestion.panel;

import java.util.Date;
import java.util.List;

import com.coreweb.domain.Domain;
import com.coreweb.domain.IiD;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.domain.Operador;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Tarea;
import com.tickets.domain.Turno;
import com.tickets.gestion.administracion.OperadorAssembler;
import com.tickets.gestion.administracion.OperadorDTO;

public class TareaAssembler extends Assembler {

	private static String[] attTarea = { "descripcion", "activa", "inicio",
			"fin", "serviciosAdicionales", "observacion" };
	private static String[] attOperador = { "nombre", "descripcion" };
	private static String[] attPuesto = { "descripcion" };

	
	public TareaDTO getTareaDto(IiD dato) throws Exception{
		
		TareaDTO dto = (TareaDTO)this.getDTObyId(Tarea.class.getName(), dato.getId());
		return dto;
	}
	
	@Override
	public Domain dtoToDomain(DTO dtoOri) throws Exception {

		TareaDTO dto = (TareaDTO) dtoOri;
		Tarea domain = (Tarea) getDomain(dto, Tarea.class);
		this.copiarValoresAtributos(dto, domain, attTarea);
		this.myPairToDomain(dto, domain, "tipo");
		this.myPairToDomain(dto, domain, "tipoFds");
		this.myPairToDomain(dto, domain, "sucursal");
		this.myArrayToDomain(dto, domain, "operador");
		this.myArrayToDomain(dto, domain, "puesto");
		this.hijoDtoToHijoDomain(dto, domain, "turno", new TurnoAssembler(),
				true);
		
		// asignar/quitar al Operador la tareaCorriente
		// NO es necesaria esta parte.
		/*
		Operador op = domain.getOperador();
		if (domain.isActiva() == true){
			op.setTareaCorriente(domain);
		}else{
			op.setTareaCorriente(null);
		}
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(op, domain.getUsuarioMod());
		*/
		return domain;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {

		TareaDTO dto = (TareaDTO) getDTO(domain, TareaDTO.class);

		this.copiarValoresAtributos(domain, dto, attTarea);

		this.domainToMyPair(domain, dto, "tipo");
		this.domainToMyPair(domain, dto, "tipoFds");
		this.domainToMyPair(domain, dto, "sucursal");
		this.domainToMyArray(domain, dto, "operador", attOperador);
		this.domainToMyArray(domain, dto, "puesto", attPuesto);
		this.hijoDomainToHijoDTO(domain, dto, "turno", new TurnoAssembler());

		// TODO Auto-generated method stub
		return dto;
	}
	
	public static void main(String[] args) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		
		List<Turno> list = rr.getAllTurnos();
		Turno t = list.get(0);
		TurnoAssembler asTurno = new TurnoAssembler();
		TurnoDTO turno = (TurnoDTO) asTurno.domainToDto(t);
		
		List<Operador> list2 = rr.getAllOperarios();
		Operador o = list2.get(0);
		OperadorAssembler asOp = new OperadorAssembler();
		OperadorDTO ope = (OperadorDTO) asOp.domainToDto(o);
		
		TareaDTO dto = new TareaDTO();
		
		MyArray operador = new MyArray();
		operador.setId(1l);
		dto.setOperador(operador);
		//dto.setOperador(ope);
		
		MyPair sucursal = new MyPair();
		sucursal.setId(1l);
		dto.setSucursal(sucursal);
		
		MyPair tipo = new MyPair();
		tipo.setId(8l);
		dto.setTipo(tipo);
		
		dto.setActiva(true);
		dto.setDescripcion("blah blah");
		dto.setInicio(new Date());
		
		MyArray puesto = new MyArray();
		puesto.setId(1l);
		dto.setPuesto(puesto);
		
		MyPair tipoFds = new MyPair();
		tipoFds.setId(9l);
		dto.setTipoFds(tipoFds);
		
		dto.setTurno(turno);
		
		TareaAssembler as = new TareaAssembler();
		Tarea tarea = (Tarea)as.dtoToDomain(dto);
		System.out.println("============ creo el turno "+tarea.getId());
	}

}
