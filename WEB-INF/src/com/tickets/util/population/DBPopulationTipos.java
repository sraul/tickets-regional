package com.tickets.util.population;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;
import com.coreweb.domain.TipoTipo;
import com.tickets.Configuracion;
import com.tickets.domain.Cliente;
import com.tickets.domain.RegisterDomain;

public class DBPopulationTipos {

	private RegisterDomain rr = RegisterDomain.getInstance();
	
	private void grabarDB(Domain d) throws Exception {
		rr.saveObject(d, Configuracion.USER_SYSTEMA);
	}
	
	// Estados del Turno
	TipoTipo estadoTurno = new TipoTipo();
	Tipo estadoTurno_atendiendo = new Tipo();
	Tipo estadoTurno_bloqueeado = new Tipo();
	Tipo estadoTurno_llamando = new Tipo();
	Tipo estadoTurno_esperando = new Tipo();
	Tipo estadoTurno_terminado = new Tipo();
	Tipo estadoTurno_cancelado = new Tipo();
	
	// Tipos de Tarea
	TipoTipo tareaTipo = new TipoTipo();
	Tipo tarea_atencion = new Tipo();
	Tipo tarea_FDS = new Tipo();
	
	// Tipos de Tarea que son FDS..
	TipoTipo tipoFDS = new TipoTipo();
	Tipo fds_desayuno = new Tipo();
	Tipo fds_almuerzo = new Tipo();
	Tipo fds_merienda = new Tipo();
	Tipo fds_telemarketing = new Tipo();
	Tipo fds_presupuesto = new Tipo();
	Tipo fds_visita = new Tipo();
	Tipo fds_sanitario = new Tipo();
	Tipo fds_reunion = new Tipo();
	Tipo fds_servicio_tecnico = new Tipo();
	Tipo fds_capacitacion = new Tipo();
	Tipo fds_deposito = new Tipo();
	Tipo fds_permiso = new Tipo();
	
	// Estados del operador
	TipoTipo estadoOperador = new TipoTipo();
	Tipo estadoOperador_libre = new Tipo();
	Tipo estadoOperador_fds = new Tipo();
	Tipo estadoOperador_atendiendo = new Tipo();
	
	// Estados del servicio
	TipoTipo estadoServicio = new TipoTipo();
	Tipo estadoServicio_Activo = new Tipo();
	Tipo estadoServicio_Inactivo = new Tipo();
	
	// Estados del puesto
	TipoTipo estadoPuesto = new TipoTipo();
	Tipo estadoPuesto_Activo = new Tipo();
	Tipo estadoPuesto_Inactivo = new Tipo();

	// Alertas..
	TipoTipo tipoAlerta = new TipoTipo();
	Tipo alertaMuchos = new Tipo();
	Tipo alertaUno = new Tipo();
	Tipo alertaComunitaria = new Tipo();

	TipoTipo nivelAlerta = new TipoTipo();
	Tipo alertaInformativa = new Tipo();
	Tipo alertaError = new Tipo();

	TipoTipo sucursales = new TipoTipo();
	Tipo sucursal1 = new Tipo();
	
	
	public void cargarTipos() throws Exception {
		
		//Sucursales
		sucursales.setDescripcion(Configuracion.ID_TIPO_SUCURSAL);
		grabarDB(sucursales);
		
		sucursal1.setDescripcion("S1");
		sucursal1.setSigla(Configuracion.SIGLA_SUCURSAL);
		sucursal1.setTipoTipo(sucursales);
		grabarDB(sucursal1);
		
		
		// Estado Turno
		estadoTurno.setDescripcion(Configuracion.ID_TIPO_ESTADO_TURNO);
		grabarDB(estadoTurno);
		
		estadoTurno_bloqueeado.setDescripcion("Bloqueado");
		estadoTurno_bloqueeado.setSigla(Configuracion.SIGLA_ESTADO_TURNO_BLOQUEADO);
		estadoTurno_bloqueeado.setTipoTipo(estadoTurno);
		grabarDB(estadoTurno_bloqueeado);

		estadoTurno_llamando.setDescripcion("Llamando");
		estadoTurno_llamando.setSigla(Configuracion.SIGLA_ESTADO_TURNO_LLAMANDO);
		estadoTurno_llamando.setTipoTipo(estadoTurno);
		grabarDB(estadoTurno_llamando);

		
		estadoTurno_atendiendo.setDescripcion("Atendiendo");
		estadoTurno_atendiendo.setSigla(Configuracion.SIGLA_ESTADO_TURNO_ATENDIENDO);
		estadoTurno_atendiendo.setTipoTipo(estadoTurno);
		grabarDB(estadoTurno_atendiendo);
		
		estadoTurno_esperando.setDescripcion("Esperando");
		estadoTurno_esperando.setSigla(Configuracion.SIGLA_ESTADO_TURNO_ESPERANDO);
		estadoTurno_esperando.setTipoTipo(estadoTurno);
		grabarDB(estadoTurno_esperando);
		
		estadoTurno_terminado.setDescripcion("Terminado");
		estadoTurno_terminado.setSigla(Configuracion.SIGLA_ESTADO_TURNO_TERMINADO);
		estadoTurno_terminado.setTipoTipo(estadoTurno);
		grabarDB(estadoTurno_terminado);
		
		estadoTurno_cancelado.setDescripcion("Cancelado");
		estadoTurno_cancelado.setSigla(Configuracion.SIGLA_ESTADO_TURNO_CANCELADO);
		estadoTurno_cancelado.setTipoTipo(estadoTurno);
		grabarDB(estadoTurno_cancelado);
		
		
		//Tipo Tarea
		tareaTipo.setDescripcion(Configuracion.ID_TIPO_TAREA);
		grabarDB(tareaTipo);
		
		tarea_atencion.setDescripcion("Atención");
		tarea_atencion.setSigla(Configuracion.SIGLA_TAREA_ATENCION);
		tarea_atencion.setTipoTipo(tareaTipo);
		grabarDB(tarea_atencion);
		
		tarea_FDS.setDescripcion("Fuera de Servicio");
		tarea_FDS.setSigla(Configuracion.SIGLA_TAREA_FDS);
		tarea_FDS.setTipoTipo(tareaTipo);
		grabarDB(tarea_FDS);
		
		
		// Tipos de Tarea que son FDS..
		tipoFDS.setDescripcion(Configuracion.ID_TIPO_TAREA_FDS);
		grabarDB(tipoFDS);
		
		fds_desayuno.setDescripcion("Desayuno");
		fds_desayuno.setSigla(Configuracion.SIGLA_FDS_DESAYUNO);
		fds_desayuno.setTipoTipo(tipoFDS);
		grabarDB(fds_desayuno);
		
		fds_almuerzo.setDescripcion("Almuerzo");
		fds_almuerzo.setSigla(Configuracion.SIGLA_FDS_ALMUERZO);
		fds_almuerzo.setTipoTipo(tipoFDS);
		grabarDB(fds_almuerzo);
		
		fds_merienda.setDescripcion("Merienda");
		fds_merienda.setSigla(Configuracion.SIGLA_FDS_MERIENDA);
		fds_merienda.setTipoTipo(tipoFDS);
		grabarDB(fds_merienda);
		
		fds_telemarketing.setDescripcion("Telemarketing");
		fds_telemarketing.setSigla(Configuracion.SIGLA_FDS_TELEMARKETING);
		fds_telemarketing.setTipoTipo(tipoFDS);
		grabarDB(fds_telemarketing);
		
		fds_presupuesto.setDescripcion("Presupuesto");
		fds_presupuesto.setSigla(Configuracion.SIGLA_FDS_PRESUPUESTO);
		fds_presupuesto.setTipoTipo(tipoFDS);
		grabarDB(fds_presupuesto);
		
		fds_visita.setDescripcion("Visita a Clientes");
		fds_visita.setSigla(Configuracion.SIGLA_FDS_VISITA);
		fds_visita.setTipoTipo(tipoFDS);
		grabarDB(fds_visita);
		
		fds_sanitario.setDescripcion("Ir al Sanitario");
		fds_sanitario.setSigla(Configuracion.SIGLA_FDS_SANITARIO);
		fds_sanitario.setTipoTipo(tipoFDS);
		grabarDB(fds_sanitario);
		
		fds_reunion.setDescripcion("Reunión");
		fds_reunion.setSigla(Configuracion.SIGLA_FDS_REUNION);
		fds_reunion.setTipoTipo(tipoFDS);
		grabarDB(fds_reunion);
		
		fds_servicio_tecnico.setDescripcion("Ir a Servicio Técnico");
		fds_servicio_tecnico.setSigla(Configuracion.SIGLA_FDS_SERV_TEC);
		fds_servicio_tecnico.setTipoTipo(tipoFDS);
		grabarDB(fds_servicio_tecnico);
		
		fds_capacitacion.setDescripcion("Capacitación");
		fds_capacitacion.setSigla(Configuracion.SIGLA_FDS_CAPACITACION);
		fds_capacitacion.setTipoTipo(tipoFDS);
		grabarDB(fds_capacitacion);
		
		fds_deposito.setDescripcion("Ir al Depósito");
		fds_deposito.setSigla(Configuracion.SIGLA_FDS_DEPOSITO);
		fds_deposito.setTipoTipo(tipoFDS);
		grabarDB(fds_deposito);
		
		fds_permiso.setDescripcion("Permiso");
		fds_permiso.setSigla(Configuracion.SIGLA_FDS_PERMISO);
		fds_permiso.setTipoTipo(tipoFDS);
		grabarDB(fds_permiso);
		
		// Estado Operador
		estadoOperador.setDescripcion(Configuracion.ID_TIPO_ESTADO_OPERADOR);
		grabarDB(estadoOperador);
		
		estadoOperador_libre.setDescripcion("Libre");
		estadoOperador_libre.setSigla(Configuracion.SIGLA_ESTADO_OPERADOR_LIBRE);
		estadoOperador_libre.setTipoTipo(estadoOperador);
		grabarDB(estadoOperador_libre);
		
		estadoOperador_fds.setDescripcion("Fuera de servicio");
		estadoOperador_fds.setSigla(Configuracion.SIGLA_ESTADO_OPERADOR_FDS);
		estadoOperador_fds.setTipoTipo(estadoOperador);
		grabarDB(estadoOperador_fds);
		
		estadoOperador_atendiendo.setDescripcion("Atendiendo");
		estadoOperador_atendiendo.setSigla(Configuracion.SIGLA_ESTADO_OPERADOR_ATENDIENDO);
		estadoOperador_atendiendo.setTipoTipo(estadoOperador);
		grabarDB(estadoOperador_atendiendo);
				
		//Estado Servicio
		estadoServicio.setDescripcion(Configuracion.ID_TIPO_ESTADO_SERVICIO);
		grabarDB(estadoServicio);
		
		estadoServicio_Activo.setDescripcion("Activo");
		estadoServicio_Activo.setSigla(Configuracion.SIGLA_ESTADO_SERVICIO_ACTIVO);
		estadoServicio_Activo.setTipoTipo(estadoServicio);
		grabarDB(estadoServicio_Activo);
		
		estadoServicio_Inactivo.setDescripcion("Inactivo");
		estadoServicio_Inactivo.setSigla(Configuracion.SIGLA_ESTADO_SERVICIO_INACTIVO);
		estadoServicio_Inactivo.setTipoTipo(estadoServicio);
		grabarDB(estadoServicio_Inactivo);
		
		//Estado Puesto
		estadoPuesto.setDescripcion(Configuracion.ID_TIPO_ESTADO_PUESTO);
		grabarDB(estadoPuesto);
		
		estadoPuesto_Activo.setDescripcion("Activo");
		estadoPuesto_Activo.setSigla(Configuracion.SIGLA_ESTADO_PUESTO_ACTIVO);
		estadoPuesto_Activo.setTipoTipo(estadoPuesto);
		grabarDB(estadoPuesto_Activo);
		
		estadoPuesto_Inactivo.setDescripcion("Inactivo");
		estadoPuesto_Inactivo.setSigla(Configuracion.SIGLA_ESTADO_PUESTO_INACTIVO);
		estadoPuesto_Inactivo.setTipoTipo(estadoPuesto);
		grabarDB(estadoPuesto_Inactivo);
				
		// Tipo y Nivel de Alerta
		tipoAlerta.setDescripcion(Configuracion.ID_TIPO_TIPO_ALERTA);
		grabarDB(tipoAlerta);

		alertaMuchos.setDescripcion("Muchos destinos, muchos canceladores");
		alertaMuchos.setSigla("DESTINO-MUCHOS");
		alertaMuchos.setTipoTipo(tipoAlerta);
		grabarDB(alertaMuchos);

		alertaUno.setDescripcion("Un destino, un cancelador");
		alertaUno.setSigla("DESTINO-UNO");
		alertaUno.setTipoTipo(tipoAlerta);
		grabarDB(alertaUno);

		alertaComunitaria.setDescripcion("Muchos destinos, algun cancelador");
		alertaComunitaria.setSigla("DESTINO-COMUN");
		alertaComunitaria.setTipoTipo(tipoAlerta);
		grabarDB(alertaComunitaria);

		nivelAlerta.setDescripcion(Configuracion.ID_TIPO_NIVEL_ALERTA);
		grabarDB(nivelAlerta);

		alertaInformativa.setDescripcion("Alerta informativa");
		alertaInformativa.setSigla("ALER-INFO");
		alertaInformativa.setTipoTipo(nivelAlerta);
		grabarDB(alertaInformativa);

		alertaError.setDescripcion("Alerta error");
		alertaError.setSigla("ALER-ERROR");
		alertaError.setTipoTipo(nivelAlerta);
		grabarDB(alertaError);
		
		//Carga el cliente nuevo por defecto
		this.cargarClienteNuevoDefault();
	}
	
	private void cargarClienteNuevoDefault() throws Exception{
		Cliente cl = new Cliente();
		cl.setCedula(Configuracion.CEDULA_CLIENTE_NUEVO);
		cl.setDescripcion("CLIENTE NUEVO");
		cl.setRuc("");
		cl.setNuevo(true);
		grabarDB(cl);
	}
}
