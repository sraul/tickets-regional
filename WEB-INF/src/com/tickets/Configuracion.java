package com.tickets;

import org.zkoss.zk.ui.Sessions;

import com.coreweb.Config;

public class Configuracion extends Config {

	// 
	public static boolean USA_SUCURSALES = false;
	
	// Aplicación
	public static String EMPRESA = "Sistema de Turnos";
	public static String ACCESO = "AccesoDTO";
	public static String PATH_SESSION = ".";
	public static String OPERADOR = "OPERADOR";
	
	public static String URL_TIMBRE = "";
	
	static{
		try {
			
			URL_TIMBRE = Sessions.getCurrent().getWebApp().getRealPath("/recursos/timbre.wav");
			
		} catch (Exception e) {
			System.err.println("Sin session..");
		}
	}

	// ID's de Tipos
	public static String ID_TIPO_SUCURSAL = "Sucursal";
	public static String ID_TIPO_ESTADO_TURNO = "Estado del Turno";
	public static String ID_TIPO_TAREA = "Tipo de Tarea";
	public static String ID_TIPO_TAREA_FDS = "Tipo de Tarea FDS";
	public static String ID_TIPO_ESTADO_OPERADOR = "Estado del Operador";
	public static String ID_TIPO_TIPO_ALERTA = "Tipo Alerta";
	public static String ID_TIPO_NIVEL_ALERTA = "Nivel Alerta";
	public static String ID_TIPO_ESTADO_SERVICIO = "Estado del Servicio";
	public static String ID_TIPO_ESTADO_PUESTO = "Estado del Puesto";

	// Siglas de Tipos
	public static String SIGLA_SUCURSAL = "SUCURSAL";
	public static String SIGLA_ESTADO_TURNO_BLOQUEADO = "EST-TRN-BLQ";
	public static String SIGLA_ESTADO_TURNO_LLAMANDO = "EST-TRN-LLA";
	public static String SIGLA_ESTADO_TURNO_ATENDIENDO = "EST-TRN-ATE";
	public static String SIGLA_ESTADO_TURNO_ESPERANDO = "EST-TRN-ESP";
	public static String SIGLA_ESTADO_TURNO_TERMINADO = "EST-TRN-TER";
	public static String SIGLA_ESTADO_TURNO_CANCELADO = "EST-TRN-CAN";

	public static String SIGLA_TAREA_ATENCION = "TAR-ATN";
	public static String SIGLA_TAREA_FDS = "TAR-FDS";

	public static String SIGLA_FDS_DESAYUNO = "FDS-DES";
	public static String SIGLA_FDS_ALMUERZO = "FDS-ALM";
	public static String SIGLA_FDS_MERIENDA = "FDS-MER";
	public static String SIGLA_FDS_TELEMARKETING = "FDS-TMK";
	public static String SIGLA_FDS_PRESUPUESTO = "FDS-PRE";
	public static String SIGLA_FDS_VISITA = "FDS-VIS";
	public static String SIGLA_FDS_SANITARIO = "FDS-SAN";
	public static String SIGLA_FDS_REUNION = "FDS-REU";
	public static String SIGLA_FDS_SERV_TEC = "FDS-SER-TEC";
	public static String SIGLA_FDS_CAPACITACION = "FDS-CAP";
	public static String SIGLA_FDS_DEPOSITO = "FDS-DEP";
	public static String SIGLA_FDS_PERMISO = "FDS-PER";

	public static String SIGLA_ESTADO_OPERADOR_LIBRE = "EST-OPE-LIB";
	public static String SIGLA_ESTADO_OPERADOR_FDS = "EST-OPE-FDS";
	public static String SIGLA_ESTADO_OPERADOR_ATENDIENDO = "EST-OPE-ATE";

	public static String SIGLA_ESTADO_SERVICIO_ACTIVO = "EST-SER-ACT";
	public static String SIGLA_ESTADO_SERVICIO_INACTIVO = "EST-SER-INA";

	public static String SIGLA_ESTADO_PUESTO_ACTIVO = "EST-PUE-ACT";
	public static String SIGLA_ESTADO_PUESTO_INACTIVO = "EST-PUE-INA";

	public static String AUTENTICAR_OPERADOR_ZUL = "/tickets/gestion/atencion/autenticar_operador.zul";
	public static String SELECCIONAR_TURNO_ZUL = "/tickets/gestion/atencion/seleccionar_turno.zul";
	public static String MENSAJE_SERVICIO_ZUL = "/tickets/gestion/panel/mensaje_servicio.zul";
	public static String SRC_VIDEO = "/tickets/gestion/panel/video.zul";
	public static String DERIVAR_TURNO_ZUL = "/tickets/gestion/atencion/derivar_turno.zul";
	public static String TAREA_FDS_ZUL = "/tickets/gestion/atencion/tarea_fds.zul";
	public static String OPERACIONES_TURNO_ZUL = "/tickets/gestion/atencion/operaciones_turno.zul";
	public static String SERVICIOS_ADICIONALES_ZUL = "/tickets/gestion/atencion/servicios_adicionales.zul";
	
	//Eventos
	public static String EVENTO_LLAMAR_TURNO = "llamarTurno";
	public static String ON_LLAMAR_TURNO = "onLlamarTurno";
	public static String EVENTO_NUEVO_CLIENTE = "nuevoCliente";
	public static String ON_NUEVO_CLIENTE = "onNuevoCliente";
	
	//Otros
	public static String CEDULA_CLIENTE_NUEVO = "SIN-CEDULA-CL-NVO";
	public static String TICKET_DISPONIBLE = "Disponible";
	
	//Letras de los servicios
	public static String LETRA_SERVICIO_FACTURACION = "FF";
	public static String LETRA_SERVICIO_CAJA = "CC";
	public static String LETRA_SERVICIO_ATENCION_CLIENTE = "AA";
	public static String LETRA_SERVICIO_MAYORISTA = "BB";
	public static String LETRA_SERVICIO_MINORISTA = "DD";
	public static String LETRA_SERVICIO_CENTRO_APOYO = "EE";
	
	//Tope de numeracion en los tickets
	public static long LIMITE_NUMERACION = 400 + 1;
	

}
