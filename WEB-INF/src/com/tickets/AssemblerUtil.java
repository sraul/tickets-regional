package com.tickets;

import java.util.List;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Ping;
import com.coreweb.domain.Tipo;
import com.coreweb.dto.AssemblerCoreUtil;
import com.coreweb.dto.DTO;
import com.coreweb.dto.UtilCoreDTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.domain.Cliente;
import com.tickets.domain.Puesto;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Servicio;

public class AssemblerUtil extends AssemblerCoreUtil {

	private String[] attServicios = new String[] { "descripcion", "letra" , "estado", "imageSrc", "auxi", "orden"};
	private String[] attPuestos = new String[] { "descripcion" , "estado"};

	public static UtilDTO getDTOUtil() {
		AssemblerUtil as = new AssemblerUtil();
		UtilCoreDTO dto = getDTOUtilCore(as);
		return (UtilDTO) dto;
	}

	@Override
	public Domain dtoToDomain(DTO dtoC) throws Exception {
		// TODO Auto-generated method stub
		UtilDTO dto = (UtilDTO) dtoC;

		Ping ping = new Ping();
		ping.setEcho("Configuracion modificada: " + System.currentTimeMillis());

		// Guarda todos los servicios
		this.listaMyArrayToListaDomain(dto.getServicios(),
				com.tickets.domain.Servicio.class, attServicios);
		// Guarda todos los puestos
		this.listaMyArrayToListaDomain(dto.getPuestos(),
				com.tickets.domain.Puesto.class, attPuestos);
		
		listaMyPairToListaDomainTipo(dto.getTiposDeTareaFDS(),
				Configuracion.ID_TIPO_TAREA_FDS);

		return ping;
	}

	@Override
	public DTO domainToDto(Domain domain) throws Exception {
		UtilDTO dto = new UtilDTO();
		RegisterDomain rr = RegisterDomain.getInstance();

		List<Tipo> _estadosDeTurnos = rr
				.getTipos(Configuracion.ID_TIPO_ESTADO_TURNO);
		List<Tipo> _tiposDeTarea = rr.getTipos(Configuracion.ID_TIPO_TAREA);
		List<Tipo> _tiposDeTareaFDS = rr
				.getTipos(Configuracion.ID_TIPO_TAREA_FDS);
		List<Tipo> _estadosDeOperadores = rr
				.getTipos(Configuracion.ID_TIPO_ESTADO_OPERADOR);
		List<Tipo> _estadosDeServicios = rr
				.getTipos(Configuracion.ID_TIPO_ESTADO_SERVICIO);
		List<Tipo> _estadosDePuestos = rr
				.getTipos(Configuracion.ID_TIPO_ESTADO_PUESTO);

		Tipo _estadoBloqueado = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_TURNO_BLOQUEADO);

		Tipo _estadoLlamando = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_TURNO_LLAMANDO);
		Tipo _estadoAtendiendo = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_TURNO_ATENDIENDO);
		Tipo _estadoEsperando = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_TURNO_ESPERANDO);
		Tipo _estadoTerminado = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_TURNO_TERMINADO);
		Tipo _estadoCancelado = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_TURNO_CANCELADO);
		
		

		Tipo _tareaAtencion = rr
				.getTipoPorSigla(Configuracion.SIGLA_TAREA_ATENCION);
		Tipo _tareaFDS = rr.getTipoPorSigla(Configuracion.SIGLA_TAREA_FDS);

		Tipo _FDS_desayuno = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_DESAYUNO);
		Tipo _FDS_almuerzo = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_ALMUERZO);
		Tipo _FDS_merienda = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_MERIENDA);
		Tipo _FDS_telemarketing = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_TELEMARKETING);
		Tipo _fds_visita = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_VISITA);
		Tipo _fds_sanitario = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_SANITARIO);
		Tipo _fds_reunion = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_REUNION);
		Tipo _fds_servicio_tecnico = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_SERV_TEC);
		Tipo _fds_capacitacion = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_CAPACITACION);
		Tipo _fds_deposito = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_DEPOSITO);
		Tipo _fds_permiso = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_PERMISO);
		Tipo _fds_presupuesto = rr.getTipoPorSigla(Configuracion.SIGLA_FDS_PRESUPUESTO);

		Tipo _estado_operador_libre = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_OPERADOR_LIBRE);
		Tipo _estado_operador_atendiendo = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_OPERADOR_ATENDIENDO);
		Tipo _estado_operador_fds = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_OPERADOR_FDS);

		Tipo _estado_servicio_activo = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_SERVICIO_ACTIVO);
		Tipo _estado_servicio_inactivo = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_SERVICIO_INACTIVO);

		Tipo _estado_puesto_activo = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_PUESTO_ACTIVO);
		Tipo _estado_puesto_inactivo = rr
				.getTipoPorSigla(Configuracion.SIGLA_ESTADO_PUESTO_INACTIVO);

		List<MyPair> estadosDeTurnos = this
				.listaTiposToListaMyPair(_estadosDeTurnos);
		List<MyPair> tiposDeTarea = this.listaTiposToListaMyPair(_tiposDeTarea);
		List<MyPair> tiposDeTareaFDS = this
				.listaTiposToListaMyPair(_tiposDeTareaFDS);
		List<MyPair> estadosDeOperadores = this
				.listaTiposToListaMyPair(_estadosDeOperadores);
		List<MyPair> estadosDeServicios = this
				.listaTiposToListaMyPair(_estadosDeServicios);
		List<MyPair> estadosDePuestos = this
				.listaTiposToListaMyPair(_estadosDePuestos);

		MyPair estadoBloqueado = this.tipoToMyPair(_estadoBloqueado);
		MyPair estadoLlamando = this.tipoToMyPair(_estadoLlamando);
		MyPair estadoAtendiendo = this.tipoToMyPair(_estadoAtendiendo);
		MyPair estadoEsperando = this.tipoToMyPair(_estadoEsperando);
		MyPair estadoTerminado = this.tipoToMyPair(_estadoTerminado);
		MyPair estadoCancelado = this.tipoToMyPair(_estadoCancelado);

		MyPair tareaAtencion = this.tipoToMyPair(_tareaAtencion);
		MyPair tareaFDS = this.tipoToMyPair(_tareaFDS);

		MyPair FDS_desayuno = this.tipoToMyPair(_FDS_desayuno);
		MyPair FDS_almuerzo = this.tipoToMyPair(_FDS_almuerzo);
		MyPair FDS_merienda = this.tipoToMyPair(_FDS_merienda);
		MyPair FDS_telemarketing = this.tipoToMyPair(_FDS_telemarketing);
		MyPair fds_presupuesto = this.tipoToMyPair(_fds_presupuesto);
		MyPair fds_visita = this.tipoToMyPair(_fds_visita);
		MyPair fds_sanitario = this.tipoToMyPair(_fds_sanitario);
		MyPair fds_reunion = this.tipoToMyPair(_fds_reunion);
		MyPair fds_servicio_tecnico = this.tipoToMyPair(_fds_servicio_tecnico);
		MyPair fds_capacitacion = this.tipoToMyPair(_fds_capacitacion);
		MyPair fds_deposito = this.tipoToMyPair(_fds_deposito);
		MyPair fds_permiso = this.tipoToMyPair(_fds_permiso);		

		MyPair estadoOperadorLibre = this.tipoToMyPair(_estado_operador_libre);
		MyPair estadoOperadorAtendiendo = this
				.tipoToMyPair(_estado_operador_atendiendo);
		MyPair estadoOperadorFds = this.tipoToMyPair(_estado_operador_fds);

		MyPair estadoServicioActivo = this
				.tipoToMyPair(_estado_servicio_activo);
		MyPair estadoServicioInactivo = this
				.tipoToMyPair(_estado_servicio_inactivo);

		MyPair estadoPuestoActivo = this.tipoToMyPair(_estado_puesto_activo);
		MyPair estadoPuestoInactivo = this
				.tipoToMyPair(_estado_puesto_inactivo);

		dto.setEstadosDeTurnos(estadosDeTurnos);
		dto.setTiposDeTarea(tiposDeTarea);
		dto.setTiposDeTareaFDS(tiposDeTareaFDS);
		dto.setEstadosDeOperadores(estadosDeOperadores);
		dto.setEstadosDeServicios(estadosDeServicios);
		dto.setEstadosDePuestos(estadosDePuestos);

		dto.setEstadoBloqueado(estadoBloqueado);
		dto.setEstadoLlamando(estadoLlamando);
		dto.setEstadoAtendiendo(estadoAtendiendo);
		dto.setEstadoEsperando(estadoEsperando);
		dto.setEstadoTerminado(estadoTerminado);
		dto.setEstadoCancelado(estadoCancelado);

		dto.setTareaAtencion(tareaAtencion);
		dto.setTareaFDS(tareaFDS);

		dto.setFDS_desayuno(FDS_desayuno);
		dto.setFDS_almuerzo(FDS_almuerzo);
		dto.setFDS_merienda(FDS_merienda);
		dto.setFDS_telemarketing(FDS_telemarketing);
		dto.setFds_presupuesto(fds_presupuesto);
		dto.setFds_visita(fds_visita);
		dto.setFds_sanitario(fds_sanitario);
		dto.setFds_reunion(fds_reunion);
		dto.setFds_servicio_tecnico(fds_servicio_tecnico);
		dto.setFds_capacitacion(fds_capacitacion);
		dto.setFds_deposito(fds_deposito);
		dto.setFds_permiso(fds_permiso);

		dto.setEstadoOperadorLibre(estadoOperadorLibre);
		dto.setEstadoOperadorAtendiendo(estadoOperadorAtendiendo);
		dto.setEstadoOperadorFds(estadoOperadorFds);

		dto.setEstadoServicioActivo(estadoServicioActivo);
		dto.setEstadoServicioInactivo(estadoServicioInactivo);

		dto.setEstadoPuestoActivo(estadoPuestoActivo);
		dto.setEstadoPuestoInactivo(estadoPuestoInactivo);
		
		dto.setClienteNuevoPorDefecto(this.getClienteNuevoPorDefecto());

		// Levanta todos los servicios
		this.utilDomainToListaMyArray(dto, "servicios",
				Servicio.class.getName(), attServicios);

		// Levanta todos los puestos
		this.utilDomainToListaMyArray(dto, "puestos", Puesto.class.getName(),
				attPuestos);	

		return dto;
	}
	
	// Levanta el cliente nuevo por defecto
	private MyArray getClienteNuevoPorDefecto() throws Exception{
		
		RegisterDomain rr = RegisterDomain.getInstance();
		Cliente cliente = rr.getClienteNuevoDefault();
		
		MyArray out = new MyArray();
		out.setId(cliente.getId());
		out.setPos1(cliente.getDescripcion());
		out.setPos2(cliente.getCedula());
		out.setPos3(cliente.getRuc());
		out.setPos4(cliente.isNuevo());
		
		return out;
	}
}
