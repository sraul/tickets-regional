package com.tickets.control.cola;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

import com.coreweb.control.Control;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Tipo;
import com.coreweb.dto.Assembler;
import com.coreweb.util.AutoNumeroControl;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.Configuracion;
import com.tickets.UtilDTO;
import com.tickets.domain.Operador;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Servicio;
import com.tickets.domain.Tarea;
import com.tickets.domain.Turno;
import com.tickets.gestion.administracion.OperadorAssembler;
import com.tickets.gestion.administracion.OperadorDTO;
import com.tickets.gestion.panel.TareaAssembler;
import com.tickets.gestion.panel.TareaDTO;
import com.tickets.gestion.panel.TurnoAssembler;
import com.tickets.gestion.panel.TurnoDTO;
import com.tickets.inicio.Inicio;

@SuppressWarnings({"static-access", "rawtypes", "unchecked"})
public class ControlCola extends Control {

	private static int PRIORIDAD_UNO = 1;
	private static int PRIORIDAD_DOS = 2;

	public ControlCola(Assembler ass) {
		super(ass);
	}

	// El control que lleva los números de las colas
	private AutoNumeroControl autoNumero = new AutoNumeroControl();

	// ============= ID & Util

	
	private String getIdStr(MyPair sucursal, MyArray servicio) {
		
		String out = "";
		if (Configuracion.USA_SUCURSALES == true) {
			out = sucursal.getText() + "-" + servicio.getPos2() + "";
		} else {
			out = servicio.getPos2() + "";
		}

		return out;
	}

	private UtilDTO getUtil() {
		return (UtilDTO) this.getDtoUtil();
	}

	// ========================================================================
	// ========================================================================
	/**
	 * Crea un nuevo Turno y lo pone en la cola que le corresponde
	 * 
	 * @param sucursal
	 * @param cliente
	 * @param servicio
	 * @return
	 * @throws Exception
	 */
	public TurnoDTO nuevoTurno(MyPair sucursal, MyArray cliente,
			MyArray servicio, String horario) throws Exception {
		return this.nuevoTurno(sucursal, cliente, servicio, true, horario);
	}

	private TurnoDTO nuevoTurno(MyPair sucursal, MyArray cliente,
			MyArray servicio, boolean generaNumero, String horario) throws Exception {

		String idStr = this.getIdStr(sucursal, servicio);

		TurnoDTO turno = new TurnoDTO();

		turno.setSucursal(sucursal);
		turno.setServicio(servicio);
		turno.setCreacion(new Date());
		turno.setCliente(cliente);
		turno.setPrioridad(PRIORIDAD_UNO);
		turno.setDescripcion(horario.equals("TM") ? "TURNO MAÑANA" : "TURNO TARDE");

		turno.setEstado(this.getUtil().getEstadoEsperando());

		// buscar el número que le corresponde
		if (generaNumero == true) {
			String dStr = this.m.dateToString(new Date(), Misc.YYYY_MM_DD);
			String letras = (String) servicio.getPos2();
			String[] letras_ = letras.split("-");
			String letra = "";
			if (horario.equals("TM")) {
				letra = letras_[0];
			} else {
				letra = letras_[1];
			}
			long numero = this.autoNumero.getAutoNumero(dStr + "-" + letra);
			this.autoNumero.getAutoNumero(dStr + "-" + servicio.getPos1() + "-" + horario);
			
			if (numero == Configuracion.LIMITE_NUMERACION) {
				AutoNumeroControl.inicializarAutonumero(dStr+"-"+idStr);
				numero = this.autoNumero.getAutoNumero(dStr+"-"+idStr);
			}
			turno.setNumero(letra + numero);
		}

		turno = (TurnoDTO) this.saveDTO(turno, new TurnoAssembler());
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("ticket", turno.getNumero());
		args.put("idServicio", turno.getServicio().getId());
		
		EventQueues.lookup(Configuracion.EVENTO_NUEVO_CLIENTE,
				EventQueues.APPLICATION, true).publish(
				new Event(Configuracion.ON_NUEVO_CLIENTE, null, args));

		return turno;
	}

	// ========================================================================
	// ========================================================================

	/**
	 * Recuperar el próximo turno disponible para este servicio. Retorna null si
	 * no hay
	 * 
	 * @param sucursal
	 * @param servicio
	 * @return
	 * @throws Exception
	 */
	public TurnoDTO getTurnoDisponibleByServicio(MyPair sucursal,
			MyArray servicio, String remitido) throws Exception {
		TurnoDTO turno = this.verProximoTurnoDisponible(sucursal, servicio, remitido);
		return turno;
	}

	/**
	 * Dado un operador, retorna una lista de turnos, donde cada turno
	 * corresponde a un servicio que ese operador maneja. Sólo un turno por
	 * servicio
	 * 
	 * @param sucursal
	 * @param oper
	 * @return
	 * @throws Exception
	 */

	public List<TurnoDTO> getTurnosDisponiblesByOperador(OperadorDTO oper)
			throws Exception {
		// la sucursal no es necesaria, porque el operador tiene la sucursal

		List<TurnoDTO> listaTurnos = new ArrayList<>();
		for (MyArray serv : oper.getServicios()) {
			listaTurnos.addAll(this.getTurnosDisponiblesByServicio(oper.getSucursal(), serv.getId()));
		}		
		return listaTurnos;
	}
	
	public List<TurnoDTO> getTurnosDisponiblesByRemitido(OperadorDTO oper)
			throws Exception {
		// la sucursal no es necesaria, porque el operador tiene la sucursal

		List<TurnoDTO> listaTurnos = new ArrayList<>();
		listaTurnos.addAll(this.getTurnosDisponiblesByRemitido(oper.getSucursal(), oper.getUsuario()));
		return listaTurnos;
	}

	// ========================================================================
	// ========================================================================

	/**
	 * Pone el turno en estado "bloqueado", pero NO lo quita de la cola. Si el
	 * turno ya a sido bloqueado por otro usuario, retorna "null"
	 * 
	 * @param turno
	 * @throws Exception
	 */
	public TurnoDTO bloquearTurno(TurnoDTO turno) throws Exception {
		TurnoAssembler ass = new TurnoAssembler();

		// primero ver si este turno no se encuentra bloqueado por otro usuario.

		TurnoDTO turnoDB = (TurnoDTO) this.getDTOById(Turno.class.getName(),
				turno.getId(), ass);
		long estTrn = turnoDB.getEstado().getId();
		long estBlq = this.getUtil().getEstadoBloqueado().getId();
		if (estTrn == estBlq) {
			// el turno ya fue bloqueado por otro usuario
			return null;
		}

		// grabar el turno actual como bloqueado
		turno.setEstado(this.getUtil().getEstadoBloqueado());
		turno = (TurnoDTO) this.saveDTO(turno, ass);

		return turno;
	}

	/**
	 * Pone el turno en estado "esperando"
	 * 
	 * @param turno
	 * @throws Exception
	 */
	public TurnoDTO desbloquearTurno(TurnoDTO turno) throws Exception {
		TurnoAssembler ass = new TurnoAssembler();

		// primero ver si este turno no se encuentra bloqueado por otro usuario.

		//TurnoDTO turnoDB = (TurnoDTO) this.getDTOById(Turno.class.getName(),
		//		turno.getId(), ass);
		//long estTrn = turnoDB.getEstado().getId();
		//long estBlq = this.getUtil().getEstadoBloqueado().getId();

		turno.setEstado(this.getUtil().getEstadoEsperando());
		turno = (TurnoDTO) this.saveDTO(turno, ass);
		
		/*
		if (estTrn == estBlq) {
			// grabar el turno actual como esperando
			turno.setEstado(this.getUtil().getEstadoEsperando());
			turno = (TurnoDTO) this.saveDTO(turno, ass);
		} else {
			System.out.println("El turno: (" + turno.getId() + ") "
					+ turno.getNumero() + "  NO estaba bloqueado");
		}
		*/
		return turno;
	}

	// ========================================================================
	// ========================================================================

	private static String SRC_PREFIJOS = "/recursos/prefijos/";
	private static String SRC_NUMEROS = "/recursos/numeros/";
	
	/**
	 * Pone el turno en estado "llamando", pero NO lo quita de la cola.
	 * 
	 * @param turno
	 * @throws Exception
	 */
	public TurnoDTO llamarTurno(TurnoDTO turno, String puesto, String operador) throws Exception {

		/*turno.setEstado(this.getUtil().getEstadoLlamando());
		turno.setDescripcion(operador);
		turno = (TurnoDTO) this.saveDTO(turno, new TurnoAssembler());*/

		Map<String, String> args = new HashMap<String, String>();
		String[] turnoArray = this.m.separarLetrasDeNumeros(turno.getNumero());
		args.put("turnoLetra", turnoArray[0]);
		args.put("turnoNumero", turnoArray[1]);
		args.put("puesto", "Fila " + puesto);
		args.put("servicio", (String) turno.getServicio().getPos1());
		args.put("audio_prefijo", SRC_PREFIJOS + turnoArray[0].trim().toLowerCase() + ".mp3");
		args.put("audio_ticket_numero", SRC_NUMEROS + turnoArray[1] + ".mp3");
		args.put("audio_box_numero", SRC_NUMEROS + puesto + ".mp3");

		EventQueues.lookup(Configuracion.EVENTO_LLAMAR_TURNO,
				EventQueues.APPLICATION, true).publish(
				new Event(Configuracion.ON_LLAMAR_TURNO, null, args));

		return turno;
	}

	// ========================================================================
	// ========================================================================

	/**
	 * Atiende el turno, lo quita de la cola y crea una tarea y la graba.
	 * Retorna una TareaDTO. El turno estaba o bloqueado o llamando, pero sólo
	 * por el mismo operador.
	 * 
	 * @param sucursal
	 * @param turno
	 * @param oper
	 * @param puesto
	 * @return
	 * @throws Exception
	 */
	public TareaDTO atenderTurno(TurnoDTO turno, OperadorDTO oper,
			MyArray puesto) throws Exception {

		Date inicio = new Date();

		turno.setEstado(this.getUtil().getEstadoAtendiendo());
		turno.setInicioAtencion(inicio);

		turno = (TurnoDTO) this.saveDTO(turno, new TurnoAssembler());

		TareaDTO tarea = new TareaDTO();
		tarea.setActiva(true);
		tarea.setTipo(this.getUtil().getTareaAtencion());
		tarea.setOperador(oper.toMyArray());
		tarea.setPuesto(puesto);
		tarea.setTurno(turno);
		tarea.setSucursal(turno.getSucursal());
		tarea.setInicio(inicio);

		tarea = (TareaDTO) this.saveDTO(tarea, new TareaAssembler());

		oper.setTareaCorriente(tarea.toMyArray());
		oper.setEstado(getUtil().getEstadoOperadorAtendiendo());
		oper = (OperadorDTO) this.saveDTO(oper, new OperadorAssembler());

		return tarea;
	}

	// ========================================================================
	// ========================================================================

	/**
	 * Dada una tarea, la cierra y quita al operador la tarea corriente.
	 * 
	 * @param tarea
	 * @throws Exception
	 */
	public void cerrarTarea(TareaDTO tarea, OperadorDTO oper, boolean remitido)
			throws Exception {
		Date fin = new Date();

		tarea.setActiva(false);
		tarea.setFin(fin);

		if (remitido == false) {
			tarea.getTurno().setFinAtencion(fin);
			tarea.getTurno().setEstado(this.getUtil().getEstadoTerminado());
		}		

		tarea = (TareaDTO) this.saveDTO(tarea, new TareaAssembler());

		oper.setEstado(getUtil().getEstadoOperadorLibre());
		oper = (OperadorDTO) this.saveDTO(oper, new OperadorAssembler());

	}
	
	public void cerrarTarea(TareaDTO tarea, OperadorDTO oper) throws Exception {
		this.cerrarTarea(tarea, oper, false);
	}

	// ========================================================================
	// ========================================================================

	/**
	 * Cancela un turno. Lo quita de la cola y lo cancela. Nota: No se puede
	 * cancelar un turno que se está atendiendo, si pasa esto da una Excepcion.
	 * 
	 * @param turno
	 * @throws Exception
	 */
	public void cancelarTurno(TurnoDTO turno, String motivo) throws Exception {

		long estTrn = turno.getEstado().getId();
		long estAte = this.getUtil().getEstadoAtendiendo().getId();

		if (estTrn == estAte) {
			String txt = "";
			txt += "Este turno se está atendiendo, no se puede cancelar. \n id:"
					+ turno.getId() + "";
			throw new Exception(txt);
		}

		turno.setEstado(this.getUtil().getEstadoCancelado());

		turno = (TurnoDTO) this.saveDTO(turno, new TurnoAssembler());

	}
	
	/**
	 * llamar luego..
	 */
	public void llamarLuegoTurno(TurnoDTO turno) throws Exception {
		turno.setPrioridad(2);
		turno = (TurnoDTO) this.saveDTO(turno, new TurnoAssembler());

	}

	// ========================================================================
	// ========================================================================

	/**
	 * Para buscar tarea que está un operador atendiendo ahora.
	 * 
	 * @param oper
	 * @return
	 * @throws Exception
	 */
	public TareaDTO getTareaCorrientePor(OperadorDTO oper) throws Exception {

		MyArray ma = oper.getTareaCorriente();

		TareaAssembler ass = new TareaAssembler();
		TareaDTO tarea = ass.getTareaDto(ma);

		return tarea;
	}

	// ========================================================================
	// ========================================================================

	/**
	 * Al turno pasado como parámetro lo vuelve a poner en espera a ser
	 * atenndido. El turno que sigue le pone más prioridad, así se llama antes
	 * que este.
	 * 
	 * @param turno
	 * @return
	 * @throws Exception
	 */
	public void llamarTurnoLuego(TurnoDTO turno) throws Exception {

		System.out.println("\n\n=========== llamar luego ===============");

		System.out.println("turno actual:" + turno.getNumero());
		TurnoAssembler ass = new TurnoAssembler();

		// buscar el turno siguiente
		TurnoDTO nextTurno = this.getTurnoDisponibleByServicio(
				turno.getSucursal(), turno.getServicio(), "");

		if (nextTurno != null) {
			System.out.println("turno sigui:" + nextTurno.getNumero());

			nextTurno.setPrioridad(PRIORIDAD_DOS);
			nextTurno = (TurnoDTO) this.saveDTO(nextTurno, ass);
		}

		turno.setEstado(this.getUtil().getEstadoEsperando());
		turno.setPrioridad(PRIORIDAD_UNO);
		turno = (TurnoDTO) this.saveDTO(turno, ass);

		System.out.println("==================================");
	}

	// ========================================================================
	// ========================================================================

	/**
	 * Deriva un turno a otro servicio. Mantiene el número y la hora de
	 * creación, así el cliente sigue en el mismo orden de llegada. Da una
	 * exception si hay una tarea que lo esté tratando
	 * 
	 * @param turno
	 * @param servicioNuevo
	 * @throws Exception
	 */
	public void derivarTurno(TurnoDTO turno, MyArray servicioNuevo,
			OperadorDTO oper) throws Exception {

		// / verificar que no haya una tarea que lo esté tratando
		String query = "";
		query += "select ta from Tarea ta where ta.activa = ? and ta.turno.id = "
				+ turno.getId();
		RegisterDomain rr = RegisterDomain.getInstance();
		List l = rr.hql(query, true);
		if (l.size() > 0) {
			Tarea ta = (Tarea) l.get(0);
			String txt = "El turno '" + turno.getNumero()
					+ "' está activo en la tarea id:" + ta.getId();
			throw new Exception(txt);
		}
		// =============================

		TurnoDTO nuevoTurno = this.nuevoTurno(turno.getSucursal(),
				turno.getCliente(), servicioNuevo, false, "");

		nuevoTurno.setCreacion(turno.getCreacion());
		nuevoTurno.setNumero(turno.getNumero());

		long estTrn = turno.getEstado().getId();
		long estTer = this.getUtil().getEstadoTerminado().getId();

		if (estTrn != estTer) {
			turno.setEstado(this.getUtil().getEstadoTerminado());
			turno.setFinAtencion(new Date());
		}

		TurnoAssembler ass = new TurnoAssembler();
		ass.derivarTurno(turno, nuevoTurno, this.getLoginNombre());

		oper.setEstado(getUtil().getEstadoOperadorLibre());
		oper = (OperadorDTO) this.saveDTO(oper, new OperadorAssembler());
	}
	
	/**
	 * Remite un turno a otro operador..
	 */
	public void remitirTurno(TurnoDTO turno, String remitido,
			OperadorDTO oper) throws Exception {
		
		turno.setEstado(getUtil().getEstadoEsperando());
		turno.setPrioridad(PRIORIDAD_UNO);
		turno.setRemitido(remitido);
		turno.setDescripcion(remitido);
		this.saveDTO(turno, new TurnoAssembler());
		
		oper.setEstado(getUtil().getEstadoOperadorLibre());
		oper = (OperadorDTO) this.saveDTO(oper, new OperadorAssembler());		
	}

	/**
	 * Lista de turnos activos, agrupa por servicios y pone el ultimo llamado
	 * .... mejorar el SQL. Los turnos que se están atendiendo ahora.
	 * 
	 * @param sucursal
	 * @return
	 * @throws Exception
	 */
	public List<TurnoDTO> getUltimosTurnosActivos(IiD sucursal)
			throws Exception {
		List<TurnoDTO> lDtos = new ArrayList<>();
		TurnoAssembler ass = new TurnoAssembler();

		String query = "";
		query += " select ta.turno, ta.puesto.descripcion";
		query += " from Tarea ta";
		query += " where ta.activa = 'TRUE' and ta.sucursal.id = "
				+ sucursal.getId();
		// query += " group by ta.turno.servicio ";
		query += " order by ta.turno.servicio, ta.inicio desc ";

		RegisterDomain rr = RegisterDomain.getInstance();
		List<Object[]> l = rr.hql(query);
		long idServicioCC = 0;
		for (int i = 0; i < l.size(); i++) {
			TurnoDTO t = ass.getTurnoDto((IiD) l.get(i)[0]);
			long idServicio = t.getServicio().getId();
			if (idServicio != idServicioCC) {
				t.setPuesto((String) l.get(i)[1]);
				lDtos.add(t);
				// seteamos el servicio cc
				idServicioCC = idServicio;
			}
		}

		return lDtos;
	}

	public TareaDTO cambiarAEstadoFds(OperadorDTO oper, MyPair tipoTareaFds,
			String motivo) throws Exception {

		TareaDTO tarea = new TareaDTO();
		tarea.setActiva(true);
		tarea.setTipo(this.getUtil().getTareaFDS());
		tarea.setOperador(oper.toMyArray());
		// tarea.setSucursal(turno.getSucursal());
		tarea.setTipoFds(tipoTareaFds);
		tarea.setInicio(new Date());
		tarea.setObservacion(motivo);

		tarea = (TareaDTO) this.saveDTO(tarea, new TareaAssembler());

		oper.setTareaCorriente(tarea.toMyArray());
		oper.setEstado(getUtil().getEstadoOperadorFds());
		oper = (OperadorDTO) this.saveDTO(oper, new OperadorAssembler());

		return tarea;
	}

	public void cambiarAEstadoLibre(OperadorDTO oper/* , TareaDTO tareaCorriente */)
			throws Exception {

		TareaDTO tc = this.getTareaCorrientePor(oper);
		tc.setActiva(false);
		tc.setFin(new Date());

		tc = (TareaDTO) this.saveDTO(tc, new TareaAssembler());

		oper.setEstado(getUtil().getEstadoOperadorLibre());
		oper = (OperadorDTO) this.saveDTO(oper, new OperadorAssembler());

	}

	// ========================================================================
	// ========================================================================

	public static void xmain(String[] args) throws Exception {
		Inicio.init();

		MyPair sucursal = new MyPair();
		sucursal.setId((long) 1);
		sucursal.setText("S1");

		ControlCola cc = new ControlCola(null);
		List<TurnoDTO> lis = cc.getUltimosTurnosActivos(sucursal);
		for (int i = 0; i < lis.size(); i++) {
			TurnoDTO t = lis.get(i);
			System.out.println(t.getInicioAtencion() + " ] " + t.getServicio()
					+ " - " + t.getNumero() + "(" + t.getId() + ")");
		}
	}

	public static void pruebaMain(String[] args) throws Exception {

		RegisterDomain rr = RegisterDomain.getInstance();
		Operador opeDom = (Operador) rr.getObject(Operador.class.getName(), 8);
		OperadorAssembler ass = new OperadorAssembler();

		OperadorDTO ope = (OperadorDTO) ass.domainToDto(opeDom);

		ControlCola cc = new ControlCola(null);

		// TurnoDTO t1 = cc.nuevoTurno(sucursal, cliente1, servicio);
		// TurnoDTO t2 = cc.nuevoTurno(sucursal, cliente2, servicio);

		List<TurnoDTO> lis = cc.getTurnosDisponiblesByOperador(ope);
		for (int i = 0; i < lis.size(); i++) {
			TurnoDTO t = lis.get(i);
			System.out.println(t.getNumero() + "(" + t.getId() + ")");
		}

		/*
		 * long ini = System.currentTimeMillis(); long fin = 0;
		 * System.out.println("esperandoo"); while ((fin-ini)<(10*1000)){ fin =
		 * System.currentTimeMillis(); } System.out.println("chau");
		 */

	}

	// =======================================================
	// =======================================================

	/**
	 * Para ver el próximo turno disponible
	 * 
	 * @param idStr
	 * @return
	 * @throws Exception
	 */	
	public TurnoDTO verProximoTurnoDisponible(MyPair sucursal,
			MyArray servicio, String remitido)
			throws Exception {

		long esperandoId = this.getUtil().getEstadoEsperando().getId();
		long servicioId = servicio.getId();
		long sucursalId = sucursal.getId();

		String query = "";
		query += " select tu";
		query += " from Turno tu";
		query += " where tu.estado.id = " + esperandoId
				+ "  and tu.sucursal.id = " + sucursalId
				+ " and tu.servicio.id = " + servicioId
				+ " and (tu.descripcion = '" + Configuracion.TICKET_DISPONIBLE
				+ "' or tu.descripcion = '" + remitido + "')";
		query += " order by tu.prioridad desc, tu.creacion asc ";

		TurnoDTO turno = null;
		RegisterDomain rr = RegisterDomain.getInstance();

		TurnoAssembler ass = new TurnoAssembler();
		List<Turno> l = rr.hql(query);
		if (l.size() > 0) {

			Turno tu = null;

			for (Turno tr : l) {
				if (tr.getRemitido().compareTo(remitido) == 0) {
					tu = tr;
				}
			}

			if (tu == null) {
				boolean find = false;
				for (Turno tr : l) {
					if ((tr.getRemitido().length() == 0) && (find == false)) {
						tu = tr;
						find = true;
					}
				}
			}

			turno = (TurnoDTO) ass.domainToDto(tu);
		}

		return turno;
	}
	
	/**
	 * @return la lista de turnos disponibles del servicio facturacion..
	 */
	public List<TurnoDTO> getTurnosDisponiblesByServicio(MyPair sucursal, long idServicio)
			throws Exception {

		long esperandoId = this.getUtil().getEstadoEsperando().getId();
		long sucursalId = sucursal.getId();

		String query = "";
		query += " select tu";
		query += " from Turno tu";
		query += " where tu.estado.id = " + esperandoId
				+ " and tu.sucursal.id = " + sucursalId
				+ " and tu.servicio.id = " + idServicio
				+ " and tu.remitido = ''";
		query += " order by tu.prioridad desc, tu.creacion asc ";

		List<TurnoDTO> turnos = new ArrayList<TurnoDTO>();
		RegisterDomain rr = RegisterDomain.getInstance();

		TurnoAssembler ass = new TurnoAssembler();
		List<Turno> l = rr.hql(query);
		if (l.size() > 0) {

			for (Turno turno : l) {
				TurnoDTO turno_ = (TurnoDTO) ass.domainToDto(turno);
				turnos.add(turno_);
			}
		}
		return turnos;
	}
	
	/**
	 * @return la lista de turnos disponibles del servicio remitido..
	 */
	public List<TurnoDTO> getTurnosDisponiblesByRemitido(MyPair sucursal, String remitido)
			throws Exception {
		Date fecha = new Date();
		String desde = this.m.dateToString(fecha, Misc.YYYY_MM_DD) + " 00:00:00";
		String hasta = this.m.dateToString(fecha, Misc.YYYY_MM_DD) + " 23:59:00";

		long esperandoId = this.getUtil().getEstadoEsperando().getId();
		long sucursalId = sucursal.getId();

		String query = "";
		query += " select tu";
		query += " from Turno tu";
		query += " where tu.estado.id = " + esperandoId
				+ " and tu.sucursal.id = " + sucursalId
				+ " and tu.remitido = '" + remitido + "'"
				+ " and (tu.creacion > '" + desde + "' and tu.creacion < '" + hasta + "')";
		query += " order by tu.prioridad desc, tu.creacion asc ";

		List<TurnoDTO> turnos = new ArrayList<TurnoDTO>();
		RegisterDomain rr = RegisterDomain.getInstance();

		TurnoAssembler ass = new TurnoAssembler();
		List<Turno> l = rr.hql(query);
		if (l.size() > 0) {

			for (Turno turno : l) {
				TurnoDTO turno_ = (TurnoDTO) ass.domainToDto(turno);
				turnos.add(turno_);
			}
		}
		return turnos;
	}
	
	/**
	 * @return la lista de turnos disponibles del servicio facturacion..
	 */
	public List<TurnoDTO> getTurnosDisponiblesFacturacion(MyPair sucursal)
			throws Exception {

		long esperandoId = this.getUtil().getEstadoEsperando().getId();
		long sucursalId = sucursal.getId();

		String query = "";
		query += " select tu";
		query += " from Turno tu";
		query += " where tu.estado.id = " + esperandoId
				+ " and tu.sucursal.id = " + sucursalId
				+ " and tu.servicio.letra = '" + Configuracion.LETRA_SERVICIO_FACTURACION + "'";
		query += " order by tu.prioridad desc, tu.creacion asc ";

		List<TurnoDTO> turnos = new ArrayList<TurnoDTO>();
		RegisterDomain rr = RegisterDomain.getInstance();

		TurnoAssembler ass = new TurnoAssembler();
		List<Turno> l = rr.hql(query);
		if (l.size() > 0) {

			for (Turno turno : l) {
				TurnoDTO turno_ = (TurnoDTO) ass.domainToDto(turno);
				turnos.add(turno_);
			}
		}
		return turnos;
	}
	
	/**
	 * @return la lista de turnos disponibles del servicio caja..
	 */
	public List<TurnoDTO> getTurnosDisponiblesCaja(MyPair sucursal)
			throws Exception {

		long esperandoId = this.getUtil().getEstadoEsperando().getId();
		long sucursalId = sucursal.getId();

		String query = "";
		query += " select tu";
		query += " from Turno tu";
		query += " where tu.estado.id = " + esperandoId
				+ " and tu.sucursal.id = " + sucursalId
				+ " and tu.servicio.letra = '" + Configuracion.LETRA_SERVICIO_CAJA + "'";
		query += " order by tu.prioridad desc, tu.creacion asc ";

		List<TurnoDTO> turnos = new ArrayList<TurnoDTO>();
		RegisterDomain rr = RegisterDomain.getInstance();

		TurnoAssembler ass = new TurnoAssembler();
		List<Turno> l = rr.hql(query);
		if (l.size() > 0) {

			for (Turno turno : l) {
				TurnoDTO turno_ = (TurnoDTO) ass.domainToDto(turno);
				turnos.add(turno_);
			}
		}
		return turnos;
	}

	// ======================================
	/**
	 * Para ver el estado actual de todos los operadores de la sucursal
	 * 
	 * @param sucursal
	 * @return
	 * @throws Exception
	 */

	public List<EstadoOperador> estadoOperadores(MyPair sucursal)
			throws Exception {

		List<EstadoOperador> opers = new ArrayList<>();

		String query = "" + "select op "
				+ " from Operador op  where op.sucursal.id = "
				+ sucursal.getId() + " Order by op.nombre ";

		RegisterDomain rr = RegisterDomain.getInstance();
		List l = rr.hql(query);
		for (int i = 0; i < l.size(); i++) {
			Operador op = (Operador) l.get(i);

			EstadoOperador eOpe = new EstadoOperador();
			eOpe.setId(op.getId());
			eOpe.setNombreOperador(op.getNombre());

			// tarea
			Tarea ta = op.getTareaCorriente();
			if (ta != null) {

				Turno tu = ta.getTurno();
				if (tu != null) {
					// tiene turno
					String servicio = tu.getServicio().getDescripcion();
					String numero = tu.getNumero();
					eOpe.setDescripcion(servicio + " - " + numero);

				} else {
					// es FDS
					String fds = ta.getTipoFds().getDescripcion();
					eOpe.setDescripcion(fds);
				}

				eOpe.setInicioTarea(ta.getInicio());
				eOpe.setFinTarea(ta.getFin());
				Date fin = ta.getFin();
				if (fin == null) {
					fin = new Date();
				}
				eOpe.setTiempoTareaActiva(m.tiempoTareas(ta.getInicio(), fin));
			}
			opers.add(eOpe);

		}
		return opers;

	}

	// ========================================================
	/**
	 * El estado actual de las cosas. Retorna una lista por Servicios, id,
	 * nombre, lista<String> de tareas activas, y lista<String> de turnos
	 * esperandos.
	 * 
	 * @param sucursal
	 * @return
	 */

	public List<MyArray> getEstadoColas(MyPair sucursal) throws Exception {

		Hashtable<String, MyArray> todo = new Hashtable<>();
		Date dAhora = new Date();

		long esperando = this.getUtil().getEstadoEsperando().getId();

		RegisterDomain rr = RegisterDomain.getInstance();

		
		// ========= servicios activos ==========
		System.out.println("=== servicio ===");
		String qSe = " select se " + " from Servicio se "
				+ " where se.sucursal.id = " + sucursal.getId()
				+ " and se.activo = 'True'  order by se.descripcion ";
		List<Servicio> lSe = rr.hql(qSe);
		for (Iterator iterator = lSe.iterator(); iterator.hasNext();) {
			Servicio se = (Servicio) iterator.next();
			String seStr = se.getId()+"";
			
			MyArray dato = new MyArray();
			dato.setId(se.getId());
			dato.setPos1(se.getDescripcion());
			dato.setPos2(new ArrayList<String>()); // tareas
			dato.setPos3(new ArrayList<String>()); // turnos

			todo.put(seStr, dato);
			System.out.println(seStr);
		}

		// =============== tareas activas ========
		System.out.println("=== Tareas === ");
		String qTa = " select ta " + " from Tarea ta"
				+ " where ta.sucursal.id = " + sucursal.getId()
				+ " and ta.activa = 'True'";
		List<Tarea> lTa = rr.hql(qTa);

		for (Iterator iterator = lTa.iterator(); iterator.hasNext();) {
			Tarea ta = (Tarea) iterator.next();

			if (ta.getTurno() != null){
				
				String tiempo = this.m.tiempoTareas(ta.getInicio(), dAhora);

				String seStr = ta.getTurno().getServicio().getId() + "";
				String taStr = ta.getDescripcion() + " "
						+ ta.getPuesto().getDescripcion() + " "
						+ ta.getTurno().getCliente().getDescripcion()+" "
						+ tiempo;
				
				MyArray dato = todo.get(seStr);
				if (dato == null){
					System.out.println("TAREA: no encontro el servicio:"+seStr+"  "+taStr);
				}else{
					((ArrayList<String>) dato.getPos2()).add(taStr);
				}
				
			}
			

		}

		// ========== turnos esperando ===========
		System.out.println("==== Turnos");
		String qTu = " select tu " + " from Turno tu "
				+ " where tu.sucursal.id = " + sucursal.getId()
				+ " and tu.estado.id = " + esperando;
		List<Turno> lTu = rr.hql(qTu);

		for (Iterator iterator = lTu.iterator(); iterator.hasNext();) {
			Turno tu = (Turno) iterator.next();

			String tiempo = this.m.tiempoTareas(tu.getCreacion(), dAhora);

			String seStr = "";
			
			if (tu.getServicio() != null) {
				seStr = tu.getServicio().getId() + "";
			}		
			
			String tuStr = tu.getNumero() + " "
					+ tu.getCliente().getDescripcion() + " " + tiempo;
			MyArray dato = todo.get(seStr);

			if (dato == null){
				System.out.println("TURNO: no encontro el servicio:"+seStr+"  "+tuStr);
			}else{
				((ArrayList<String>) dato.getPos3()).add(tuStr);
			}

			
		}

		//===================================================
		//===================================================

		System.out.println("=== FIN ==s");
		List<MyArray> out = new ArrayList<>();
		Enumeration<MyArray> enu = todo.elements();
		while(enu.hasMoreElements()==true){
			MyArray m = enu.nextElement();
			out.add(m);
		}
		
		Collections.sort(out, new ComparatorListMyArray());
		
		
		return out;
	}
	
	/**
	 * Resetea los turnos..
	 */
	public void resetearTurnos(MyPair sucursal) throws Exception {
		
		long esperandoId = this.getUtil().getEstadoEsperando().getId();
		long sucursalId = sucursal.getId();

		String query = "";
		query += " select tu";
		query += " from Turno tu";
		query += " where tu.estado.id = " + esperandoId
				+ "  and tu.sucursal.id = " + sucursalId ;

		TurnoDTO turno = null;
		RegisterDomain rr = RegisterDomain.getInstance();

		TurnoAssembler ass = new TurnoAssembler();
		List l = rr.hql(query);
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			Turno tuD = (Turno) iterator.next();
			turno = (TurnoDTO) ass.domainToDto(tuD);
			turno.setEstado(this.getUtil().getEstadoCancelado());
			
			tuD = (Turno) ass.dtoToDomain(turno);
			rr.saveObject(tuD, this.getUs().getNombre());
			
		}

	}
	
	/**
	 * Resetea las tareas..
	 */
	public void resetearTareas(MyPair sucursal) throws Exception {
		
		String qTa = " select ta " +
				"  from Tarea ta"
				+ " where ta.sucursal.id = " + sucursal.getId()
				+ " and ta.activa = 'True'";
		
		RegisterDomain rr = RegisterDomain.getInstance();
		
		Tipo ti = rr.getTipoPorSigla(this.getUtil().getEstadoOperadorLibre().getSigla());
		
		List l = rr.hql(qTa);
		
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			Tarea ta = (Tarea) iterator.next();
			Operador op = ta.getOperador();
			
			// liberamos el operador
			op.setTareaCorriente(null);
			op.setEstado(ti);
			rr.saveObject(op, this.getUs().getNombre() );
			
			// terminar la tarea
			ta.setFin(new Date());
			ta.setActiva(false);
			rr.saveObject(ta, this.getUs().getNombre() );
			
			
		}
		
		
	}

}

@SuppressWarnings("rawtypes")
class ComparatorListMyArray implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		String m1 = (String) ((MyArray) o1).getPos1();
		String m2 = (String) ((MyArray) o2).getPos1();
		return m1.compareTo(m2);
	}
	
}
