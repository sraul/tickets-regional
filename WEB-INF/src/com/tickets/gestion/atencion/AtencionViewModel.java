package com.tickets.gestion.atencion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.componente.ViewPdf;
import com.coreweb.componente.WindowPopup;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.Configuracion;
import com.tickets.UtilDTO;
import com.tickets.control.cola.ControlCola;
import com.tickets.domain.Operador;
import com.tickets.domain.Puesto;
import com.tickets.domain.RegisterDomain;
import com.tickets.gestion.administracion.OperadorAssembler;
import com.tickets.gestion.administracion.OperadorDTO;
import com.tickets.gestion.panel.TareaDTO;
import com.tickets.gestion.panel.TurnoDTO;

public class AtencionViewModel extends SimpleViewModel {

	UtilDTO dtoUtil = (UtilDTO) this.getDtoUtil();

	List<OperadorDTO> operadores = new ArrayList<OperadorDTO>();

	private int nCol = 4;
	private Object[] opera = new Object[nCol];

	private List<OperadorDTO> operadores_c1 = new ArrayList<OperadorDTO>();
	private List<OperadorDTO> operadores_c2 = new ArrayList<OperadorDTO>();
	private OperadorDTO selectedOperador;
	private boolean operadorAutenticado = false;
	private boolean estadoLibre = false;
	private boolean estadoAtendiendo = false;
	private boolean estadoFds = false;
	private TareaDTO tareaActual = new TareaDTO();
	List<MyArray> servicios = new ArrayList<MyArray>();

	Misc misc = new Misc();
	ControlCola cc = new ControlCola(null);

	private TurnoDTO selectedTurno = new TurnoDTO();
	private MyPair selectedTareaFds;
	private String observacion = "";
	private MyArray selectedServicioDerivado;

	@Init(superclass = true)
	public void init() {
		try {
						
			this.estadoAtendiendo = false;
			this.estadoLibre = false;
			this.estadoFds = false;
			
			this.enlazarOperador();
			this.cargarTurnos();
			this.cargarServiciosFinales();
			this.enlazarBox();
			this.verificarEstadoServicio();
			this.subscribeNuevoCliente();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	/**
	 * Enlaza el login con el operador
	 */
	public void enlazarOperador() {
		OperadorDTO operador = (OperadorDTO) this.getSessionZK().getAttribute(
				Configuracion.OPERADOR);
		if (operador != null) {
			this.oper = operador;
			this.setUser(operador.getUsuario());
			this.operadorAutenticado = true;
			this.selectedOperador = this.oper;
			//this.setearEstado();
		}		
	}

	public List<OperadorDTO> cargarOperadores() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		List<Operador> operarios = rr.getAllOperarios();
		OperadorAssembler as = new OperadorAssembler();
		List<OperadorDTO> operadores = new ArrayList<OperadorDTO>();
		for (Operador operario : operarios) {
			operadores.add((OperadorDTO) as.domainToDto(operario));
		}
		return operadores;
	}
	
	/**
	 * Control de acceso del operador..
	 */
	
	private OperadorDTO oper;
	private String user = "";
	private String pass = "";
	private Window popup;
	
	@Command @NotifyChange("*")
	public void abrirPopUpContra(@BindingParam("d") OperadorDTO d) throws Exception {
		
		this.oper = d;
		this.setUser(d.getUsuario());
				
		popup = (Window) Executions.createComponents(Configuracion.AUTENTICAR_OPERADOR_ZUL, 
				this.mainComponent, null);
		popup.doModal();
	}
	
	@Command @NotifyChange("*")
	public void validarAcceso(@BindingParam("cmp") Textbox cmp){
		String pass = misc.encriptar(this.pass);
		if (pass.compareTo(this.oper.getPass()) == 0) {
			this.operadorAutenticado = true;
			this.selectedOperador = this.oper;
			this.setearEstado();
			this.popup.detach();
			this.user = "";
			this.pass = "";
		} else {
			String mensaje = "Contraseña incorrecta";
			cmp.setStyle("border-color:red");
			this.m.mensajePopupTemporal(mensaje, "error", cmp);
		}
	}
	
	@Command @NotifyChange("*")
	public void cancelarAcceso(){
		this.oper = new OperadorDTO();
		this.pass = "";
		this.user = "";
		this.popup.detach();
	}

	private String obsvCancelacion = "";
	private MyArray selectedPuesto;

	public String getObsvCancelacion() {
		return obsvCancelacion;
	}

	public void setObsvCancelacion(String obsvCancelacion) {
		this.obsvCancelacion = obsvCancelacion;
	}

	public MyArray getSelectedPuesto() {
		return selectedPuesto;
	}

	public void setSelectedPuesto(MyArray selectedPuesto) {
		this.selectedPuesto = selectedPuesto;
	}

	@Command
	@NotifyChange("*")
	public void abrirPopUpSeleccionarTurno() {
		try {
			/** Crea el PopUp para la seleccion del turno **/

			WindowPopup w = new WindowPopup();
			w.setModo(WindowPopup.NUEVO);
			w.setTitulo("Seleccionar Turno");
			w.setWidth("700px");
			w.setHigth("400px");
			w.setDato(this);
			w.show(Configuracion.SELECCIONAR_TURNO_ZUL);

			if (w.isClickAceptar()) {

				if (cc.bloquearTurno(this.selectedTurno) != null) { // Trata de
																	// bloquear
																	// el turno
					/** Crea el WindowPopUp para las operaciones del turno **/
					OperacionesTurnoControlNuevo wp = new OperacionesTurnoControlNuevo();
					wp.show(WindowPopup.NUEVO, wp, this.selectedTurno,
							this.selectedOperador);

					
					if (wp.isClickAceptar() == false) {
						/** Caso2: Hizo click en el boton cancelar **/
						cc.desbloquearTurno(this.selectedTurno);
						this.mensajePopupTemporal("No se realizó ninguna operación.");

					} else {

						/** Caso1: Hizo click en el boton Aceptar **/
						long estadoAtendiendo = this.dtoUtil
								.getEstadoAtendiendo().getId();
						long estadoEsperando = this.dtoUtil
								.getEstadoEsperando().getId();
						long estadoLlamando = this.dtoUtil.getEstadoLlamando()
								.getId();
						long estadoCancelado = this.dtoUtil
								.getEstadoCancelado().getId();

						
						long estadoTurno = this.selectedTurno.getEstado()
								.getId();

						String mensaje = "";

						if (estadoTurno == estadoLlamando) {
							cc.llamarTurnoLuego(this.selectedTurno);
							mensaje = "Se ha llamado al turno "
									+ this.selectedTurno.getNumero();

						} else if (estadoTurno == estadoAtendiendo) {
							cc.atenderTurno(this.selectedTurno,
									this.selectedOperador,
									wp.getSelectedPuesto());
							mensaje = "Se ha iniciado la atención del turno "
									+ this.selectedTurno.getNumero();

						} else if (estadoTurno == estadoCancelado) {
							// obtiene la observacion
							this.setObsvCancelacion(wp.getObservacion());
							cc.cancelarTurno(this.selectedTurno,
									this.obsvCancelacion);
							mensaje = "Se ha cancelado el turno "
									+ this.selectedTurno.getNumero();
						} else if (estadoTurno == estadoEsperando) {
							cc.llamarTurnoLuego(this.selectedTurno);
							mensaje = "Llamar luego al turno "
									+ this.selectedTurno.getNumero();
						} else {
							/** No hubieron cambios, desbloquear el turno **/
							mensaje = "No se realizó ninguna operación.";
							cc.desbloquearTurno(this.selectedTurno);
						}
						this.mensajePopupTemporal(mensaje, 5000);

					}
					this.setearEstado();
					this.operadorAutenticado = false;
				} else { // Si no puede bloquear retorna un mensaje
					this.mensajePopupTemporalWarning("El turno ya ha sido bloqueado por otro operador.");
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*****************************************************************/	
	

	@Command
	@NotifyChange("*")
	public void abrirPopUpFds() {
		try {
			WindowPopup w = new WindowPopup();
			w.setModo(WindowPopup.NUEVO);
			w.setTitulo("Tarea FDS");
			w.setWidth("300px");
			w.setHigth("400px");
			w.setDato(this);
			w.show(Configuracion.TAREA_FDS_ZUL);
			if (w.isClickAceptar()) {
				System.out.println("====== dato " + this.selectedTareaFds);
				String motivo = this.getMotivoAnulacion();
				this.cambiarEstadoToFds(this.selectedTareaFds, motivo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	@Command
	@NotifyChange("*")
	public void cambiarEstadoToFds(MyPair tipoTareaFds, String motivo) {
		
		this.selectedOperador.setEstado(dtoUtil.getEstadoOperadorFds());
		try {
			cc.cambiarAEstadoFds(this.selectedOperador, tipoTareaFds, motivo);
			this.estadoLibre = false;
			this.estadoFds = true;
			String mensaje = "El estado del operador "
					+ this.selectedOperador.getNombre() + " "
					+ " ha cambiado a Fuera de Servicio (FDS)";
			this.mensajePopupTemporal(mensaje, 5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void cambiarEstadoToLibre() {
		this.selectedOperador.setEstado(dtoUtil.getEstadoOperadorLibre());
		try {
			cc.cambiarAEstadoLibre(this.selectedOperador);
			this.estadoFds = false;
			this.estadoLibre = true;
			String mensaje = "El estado del operador "
					+ this.selectedOperador.getNombre() + " "
					+ " ha cambiado a Libre";
			this.mensajePopupTemporal(mensaje, 5000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void volverAlInicio() {
		this.operadorAutenticado = false;
	}

	public void obtenerTareaActual() {
		try {
			this.tareaActual = cc.getTareaCorrientePor(this.selectedOperador);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	List<MyArray> serviciosSeleccionados = new ArrayList<MyArray>();

	public List<MyArray> getServiciosSeleccionados() {
		return serviciosSeleccionados;
	}

	public void setServiciosSeleccionados(List<MyArray> serviciosSeleccionados) {
		this.serviciosSeleccionados = serviciosSeleccionados;
	}

	@Command 
	@NotifyChange({ "selectedOperador", "servicios", 
		"tareaActual", "estadoAtendiendo", "estadoLibre", "turnos", "selectedTurno" })
	public void terminarTurno() throws Exception {
		
		this.selectedOperador.setEstado(dtoUtil.getEstadoOperadorLibre());
		this.servicios = ((UtilDTO) this.getDtoUtil()).getServicios();

		cc.cerrarTarea(this.tareaActual, this.selectedOperador);
		this.estadoAtendiendo = false;
		this.estadoLibre = true;	
		
		this.cargarTurnos();		
		this.selectedTurno = new TurnoDTO();
		
		String mensaje = "Ticket finalizado: "
				+ this.tareaActual.getTurno().getNumero() + ".";
		this.mensajePopupTemporal(mensaje, 5000);
		this.tareaActual = new TareaDTO();
	}

	/** Reporte para el operador dadas fechas de inicio y fin **/

	@Command
	public void reporteOperador() throws Exception {
		
		this.notified = false;
		
		Grid gd = new Grid();

		Columns cls = new Columns();
		cls.setParent(gd);

		Column cl = new Column();
		cl.setWidth("100px");
		cl.setParent(cls);

		Column cl2 = new Column();
		cl2.setParent(cls);

		Rows rws = new Rows();
		rws.setParent(gd);

		Row rw = new Row();
		rw.setParent(rws);

		Label lb = new Label();
		lb.setValue("Inicio:");
		lb.setParent(rw);

		Row rw2 = new Row();
		rw2.setParent(rws);

		Label lb2 = new Label();
		lb2.setValue("Fin:");
		lb2.setParent(rw2);

		Datebox ix = new Datebox();
		ix.setValue(this.m.getFechaHoy00());
		ix.setWidth("150px");
		ix.setReadonly(true);
		ix.setParent(rw);

		Datebox ix2 = new Datebox();
		ix2.setValue(this.m.getFechaHoy2359());
		ix2.setWidth("150px");
		ix2.setReadonly(true);
		ix2.setParent(rw2);

		BodyPopupAceptarCancelar b = new BodyPopupAceptarCancelar();
		b.addComponente("", gd);
		b.setHighWindows("200px");
		b.setWidthWindows("320px");
		b.showPopupUnaColumna("");

		if (b.isClickAceptar()) {
			System.out.println("============ Entro a generarReporte");
			Date fechaInicio = ix.getValue();
			Date fechaFin = ix2.getValue();
			this.generarReporte(fechaInicio, fechaFin);
		}

	}

	/**
	 * Genera el reporte del operador
	 * 
	 * @throws Exception
	 */
	public void generarReporte(Date fechaInicio, Date fechaFin)
			throws Exception {

		OperadorReporte r = new OperadorReporte();

		r.setInformacion(this.selectedOperador, fechaInicio, fechaFin);

		ViewPdf vp = new ViewPdf();
		vp.showReporte(r, this);
	}
	
	
	/************************* DERIVAR TURNO *************************/
	
	private List<MyArray> serviciosFinales = new ArrayList<MyArray>();
	private Window derivar;
	private static String DERIVAR_ZUL = "/tickets/gestion/atencion/derivar_turno.zul";	
	
	@Command @NotifyChange("*")
	public void derivarTurno() throws Exception {
		
		this.notified = false;
		
		derivar = (Window) Executions.createComponents(DERIVAR_ZUL,
				this.mainComponent, null);
		derivar.doModal();
	}
	
	@Command @NotifyChange("*")
	public void cancelarDerivar() {
		this.derivar.detach();
		this.selectedServicioDerivado = new MyArray();
	}
	
	/**
	 * Deriva un Ticket a otro servicio..
	 * @throws Exception
	 */
	@Command @NotifyChange("*")
	public void aceptarDerivar() throws Exception {

		cc.cerrarTarea(this.tareaActual, this.selectedOperador);
		cc.derivarTurno(this.tareaActual.getTurno(),
				this.selectedServicioDerivado, this.selectedOperador);

		this.selectedOperador.setEstado(dtoUtil.getEstadoOperadorLibre());
		this.estadoAtendiendo = false;
		this.estadoLibre = true;

		String mensaje = "Se ha derivado el turno: "
				+ this.tareaActual.getTurno().getNumero() + ".";
		this.mensajePopupTemporal(mensaje, 5000);

		this.cargarTurnos();
		this.tareaActual = new TareaDTO();
		
		derivar.detach();
		this.selectedServicioDerivado = new MyArray();
	}
	
	/**
	 * carga los servicios para derivar
	 */
	public void cargarServiciosFinales() {
		for (MyArray ma : ((UtilDTO) this.getDtoUtil()).getServicios()) {
			this.serviciosFinales.add(ma);
		}
	}
	
	/*****************************************************************/
	
	
	/************************* REMITIR TURNO *************************/
	
	private List<MyPair> operadores_ = new ArrayList<MyPair>();
	private MyPair selectedOperador_ = new MyPair();
	private Window remitir;
	private static String REMITIR_ZUL = "/tickets/gestion/atencion/remitir_turno.zul";
	
	@Command @NotifyChange("*")
	public void remitirTurno() throws Exception {
		
		this.notified = false;
		
		this.cargarOperadoresPorServicio();

		remitir = (Window) Executions.createComponents(REMITIR_ZUL,
				this.mainComponent, null);
		remitir.doModal();	
	}
	
	@Command @NotifyChange("*")
	public void cancelarRemitir() {		
		this.operadores_ = new ArrayList<MyPair>();
		this.selectedOperador_ = new MyPair();
		remitir.detach();
	}
	
	@Command @NotifyChange("*")
	public void aceptarRemitir() throws Exception {
		
		cc.cerrarTarea(this.tareaActual, this.selectedOperador, true);
		cc.remitirTurno(this.tareaActual.getTurno(), this.selectedOperador_.getText(), oper);

		this.selectedOperador.setEstado(dtoUtil.getEstadoOperadorLibre());
		this.estadoAtendiendo = false;
		this.estadoLibre = true;

		String mensaje = "Se ha remitido el turno: "
				+ this.tareaActual.getTurno().getNumero() + ".";
		this.mensajePopupTemporal(mensaje, 5000);

		this.cargarTurnos();
		this.tareaActual = new TareaDTO();
		
		this.operadores_ = new ArrayList<MyPair>();
		this.selectedOperador_ = new MyPair();
		remitir.detach();
	}
	
	/**
	 * carga los operadores segun el servicio..
	 */
	private void cargarOperadoresPorServicio() throws Exception {
		
		RegisterDomain rr = RegisterDomain.getInstance();		
		long idServicio = this.oper.getServicios().get(0).getId();
		long idOperador = this.oper.getId().longValue();
		
		List<Operador> ops = rr.getOperadoresByServicio(idServicio);
		
		for (Operador op : ops) {
			MyPair mp = new MyPair(op.getId(), op.getNombre());
			if (op.getId().longValue() != idOperador) {
				this.operadores_.add(mp);
			}
		}
	}
	
	/*****************************************************************/
	
	
	/********************** SELECCION DE TURNOS **********************/
	
	private List<TurnoDTO> turnos = new ArrayList<TurnoDTO>();
	private boolean hayTurnos = false;
	
	/**
	 * Obtiene los posibles turnos que el operador puede atender
	 */
	public void cargarTurnos() {
		
		if (this.estadoAtendiendo == true) {
			return;
		}
		
		try {			
			
			this.turnos = cc.getTurnosDisponiblesByOperador(this.getSelectedOperador());
			if (this.turnos != null && !this.turnos.isEmpty()) {
				this.hayTurnos = true;
				Date ahora = new Date();
				for (TurnoDTO turnoDto : this.turnos) {
					turnoDto.setEspera((ahora.getTime() - turnoDto.getCreacion().getTime()) );					
				}
			} else {
				this.turnos = new ArrayList<TurnoDTO>();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/********************* ASIGNACION DE PUESTOS *********************/
	
	private MyArray selectedBox = new MyArray();
	
	/**
	 * Enlaza el puesto (Box) del operador..
	 */
	private void enlazarBox() throws Exception {
		this.selectedBox = this.getBox();
	}
	
	/**
	 * Retorna el puesto (Box) en el que puede atender el operador..
	 */
	private MyArray getBox() throws Exception {
		List<MyArray> out = new ArrayList<MyArray>();

		RegisterDomain rr = RegisterDomain.getInstance();
		List<Puesto> puestos = rr.getPuestosDisponibles(
				this.selectedOperador.getId());

		for (Puesto puesto : puestos) {
			MyArray p = new MyArray();
			p.setId(puesto.getId());
			p.setPos1(puesto.getDescripcion());
			out.add(p);
		}
		return out.get(0);
	}
	
	
	
	/*****************************************************************/
	
	
	/*************************** LLAMAR TURNO ************************/
	
	//private MyPair estadoOriginal = new MyPair();
	private Window llamando = null;	
	private boolean inAction = false;
	private boolean notified = false;
	private static String LLAMANDO_ZUL = "/tickets/gestion/atencion/llamando.zul";

	/**
	 * Llama un ticket
	 */
	@Command
	public void llamarTurno() throws Exception {

		this.notified = false;
		//this.estadoOriginal = this.selectedTurno.getEstado();
		
		//if (this.isTurnoDisponible() == false) {
			//return;
		//}
		
		this.inAction = true;

		Map<String, String> args = new HashMap<String, String>();
		args.put("ticket", this.selectedTurno.getNumero());

		cc.llamarTurno(this.selectedTurno, (String) this.selectedBox.getPos1(), this.oper.getNombre());

		llamando = (Window) Executions.createComponents(LLAMANDO_ZUL,
				this.mainComponent, args);
		llamando.doModal();
	}
	
	/**
	 * Deja de llamar un ticket..
	 */
	@Command @NotifyChange({ "inAction", "llamando" })
	public void endCall() throws Exception {
		this.inAction = false;
		this.llamando.detach();
		//this.selectedTurno.setEstado(this.estadoOriginal);
		//this.selectedTurno = (TurnoDTO) this.saveDTO(this.selectedTurno, new TurnoAssembler());
		this.llamando = null;
	}	
	
	/**
	 * Atiende el turno seleccionado y cambia el estado del operador
	 */
	@Command @NotifyChange("*")
	public void atenderTurno() throws Exception {
		
		this.notified = false;		
		this.selectedTurno.setEstado(this.dtoUtil.getEstadoAtendiendo());
		
		cc.atenderTurno(this.selectedTurno, this.selectedOperador, this.selectedBox);
		this.turnos = new ArrayList<TurnoDTO>();
		this.setearEstado();
		
		String mensaje = "Inicio de Atención Ticket: " + this.selectedTurno.getNumero();
		this.mensajePopupTemporal(mensaje, 5000);
	}
	
	/**
	 * Cancela el turno seleccionado..
	 */
	@Command @NotifyChange("*")
	public void cancelarTurno() throws Exception {
		
		this.notified = false;		
		this.inAction = true;

		if (this.mensajeSiNo("Esta seguro de cancelar el ticket: "
				+ this.selectedTurno.getNumero())) {
			
			String obs = "cancelado por: "
					+ this.selectedOperador.getDescripcion();
			
			this.selectedTurno.setEstado(this.dtoUtil.getEstadoCancelado());
			cc.cancelarTurno(this.selectedTurno, obs);
			this.selectedTurno = new TurnoDTO();
			this.cargarTurnos();
		}
		
		this.inAction = false;
	}
	
	/**
	 * llamar luego..
	 */
	@Command @NotifyChange("*")
	public void llamarLuego() throws Exception {
		
		this.notified = false;		
		this.inAction = true;

		if (this.mensajeSiNo("Esta seguro de llamar luego el ticket: "
				+ this.selectedTurno.getNumero())) {			
			cc.llamarLuegoTurno(this.selectedTurno);
			this.selectedTurno = new TurnoDTO();
			this.cargarTurnos();
		}
		
		this.inAction = false;
	}
	
	/*****************************************************************/	
	
	
	/********************** ESTADO DEL SERVICIO **********************/	
	
	private List<MyPair> opcionesFDS = this.getUtil().getTiposDeTareaFDS();
	private MyPair selectedFDS = new MyPair();
	private Window opcionesFueraServicio;
	private static String FUERA_SERVICIO_ZUL = "/tickets/gestion/atencion/opcionesFDS.zul";
	
	/**
	 * Pasa a fuera de servicio el estado del operador..
	 */
	@Command @NotifyChange("*")
	public void fueraDeServicio() throws Exception {
		this.notified = false;
		opcionesFueraServicio = (Window) Executions.createComponents(FUERA_SERVICIO_ZUL, mainComponent, null);
		opcionesFueraServicio.doModal();		
	}
	
	@Command @NotifyChange("*")
	public void aceptarOpcionFDS() throws Exception {
		
		this.selectedOperador.setEstado(dtoUtil.getEstadoOperadorFds());

		cc.cambiarAEstadoFds(this.selectedOperador, this.selectedFDS, "auto-generated");
		
		this.estadoLibre = false;
		this.estadoFds = true;
		
		String mensaje = "PASÓ A FUERA DE SERVICIO..";
		this.mensajePopupTemporal(mensaje, 3500);		
		
		this.enmascararFDS(this.mainComponent, "Fuera de Servicio", 70);
		
		opcionesFueraServicio.detach();
		this.selectedFDS = new MyPair();
	}
	
	@Command @NotifyChange("*")
	public void cancelarOpcionFDS() {
		opcionesFueraServicio.detach();
		this.selectedFDS = new MyPair();
	}
	
	/**
	 * Vuelve a retomar el servicio..
	 */
	@Command @NotifyChange("*")
	public void retomarServicio() throws Exception {
		this.notified = false;
		this.selectedOperador.setEstado(dtoUtil.getEstadoOperadorLibre());

		cc.cambiarAEstadoLibre(this.selectedOperador);
		this.estadoFds = false;
		this.estadoLibre = true;
		
		String mensaje = "HA RETOMADO SU SERVICIO..";
		this.mensajePopupTemporal(mensaje, 3500);
		
		this.mask.getParent().removeChild(mask);
		this.cargarTurnos();
	}
	
	
	/**
	 * Verifica el estado del servicio..
	 */
	public void verificarEstadoServicio() {
		if (this.estadoFds == true) {
			this.enmascararFDS(this.mainComponent, "Fuera de Servicio", 70);			
		}
	}
	
	/*****************************************************************/	
	
	
	/**************************** UTILES *****************************/
	
	private Div mask;
	
	/**
	 * refresca la lista de turnos..
	 */
	@Command 
	public void refresh() {
		this.cargarTurnos();
		BindUtils.postNotifyChange(null, null, this, "*");
	}
	
	/**
	 * Setea los estados..
	 */
	public void setearEstado() {		
		if (this.selectedOperador.getEstado().getText().compareTo("Libre") == 0) {
			this.estadoLibre = true;
			this.estadoAtendiendo = false;
			this.estadoFds = false;
		} else if (this.selectedOperador.getEstado().getText()
				.compareTo("Atendiendo") == 0) {
			this.estadoAtendiendo = true;
			this.estadoLibre = false;
			this.estadoFds = false;
			this.obtenerTareaActual();
		} else {
			this.estadoFds = true;
			this.estadoLibre = false;
			this.estadoAtendiendo = false;
		}
	}
	
	
	/**
	 * Enmascara el formulario..
	 */
	private void enmascararFDS(Component comp, String texto, int textoSize) {	
		// Pone un texto en formato de mascara de agua
		Vbox v = new Vbox();		
		v.setHeight("100%");
		v.setWidth("100%");
		v.setPack("center");
		v.setAlign("center");

		Label l = new Label();
		l.setValue(texto);
		l.setStyle("font-weight:bold;font-size:"+textoSize+"pt");		
					
		mask = new Div();
		mask.setId("mask");
		mask.setZclass("z-modal-mask");
		mask.setStyle("z-index: 2000; display: block; background:#ffcc99; opacity:0.2; filter:alpha(opacity=30); ");
		mask.setContext("salir");
		
		l.setParent(v);
		v.setParent(mask);
		
		comp.getParent().appendChild(mask);	
	}
	
	/*****************************************************************/
	
	
	/******************** NOTIFICACION NUEVO CLIENTE *****************/
	
	/**
	 * Evento que lanza un mensaje cuando ingresan nuevos clientes..
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private EventListener nuevoCliente = new EventListener() {

		@Override
		public void onEvent(Event event) throws Exception {
			Map<String, Object> args = (Map<String, Object>) event.getData();
			String ticket = (String) args.get("ticket");
			long idServicio = (long) args.get("idServicio");
			this.notifyNuevoCliente(ticket, idServicio);			
		}
		
		private void notifyNuevoCliente(String ticket, long idServicio) {
			
			long idEstadoActual = oper.getEstado().getId().longValue();
			long idEstadoLibre = getUtil().getEstadoOperadorLibre().getId().longValue();
			
			boolean servicio = false;
			for (MyArray serv : oper.getServicios()) {
				long id = serv.getId().longValue();
				if (id == idServicio) {
					servicio = true;
				}
			}
			
			if ((idEstadoActual == idEstadoLibre)
					&& (servicio == true)
						&& (inAction == false)) {
				refresh();
				
				if (notified == false) {
					notified = true;
					//Clients.evalJavaScript("notifyNuevoCliente()");					
					
				} else {
					//Clients.showNotification("Nuevo Cliente en Espera..!");
				}
				
			}				
		}
	};
	
	/**
	 * Suscribe el evento nuevoCliente en la cola de eventos..
	 */
	@SuppressWarnings("unchecked")
	public void subscribeNuevoCliente() {
		EventQueues.lookup(Configuracion.EVENTO_NUEVO_CLIENTE,
				EventQueues.APPLICATION, true).subscribe(nuevoCliente);
	}
	
	/*****************************************************************/
	

	/**************************** GET/SET ****************************/
	
	@DependsOn("turnos")
	public List<TurnoDTO> getTurnosPrincipal() {
		List<TurnoDTO> out = new ArrayList<TurnoDTO>();
		for (TurnoDTO turno : this.turnos) {
			if (turno.getPrioridad() == 1) {
				out.add(turno);
			}
		}
		return out;
	}
	
	@DependsOn("turnos")
	public List<TurnoDTO> getTurnosLlamarLuego() {
		List<TurnoDTO> out = new ArrayList<TurnoDTO>();
		for (TurnoDTO turno : this.turnos) {
			if (turno.getPrioridad() == 2) {
				out.add(turno);
			}
		}
		return out;
	}
	
	private UtilDTO getUtil() {
		return (UtilDTO) this.getDtoUtil();
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public OperadorDTO getOper() {
		return oper;
	}

	public void setOper(OperadorDTO oper) {
		this.oper = oper;
	}
	public List<OperadorDTO> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<OperadorDTO> operadores) {
		this.operadores = operadores;
	}

	public List<OperadorDTO> getOperadores_c1() {
		return operadores_c1;
	}

	public void setOperadores_c1(List<OperadorDTO> operadores_c1) {
		this.operadores_c1 = operadores_c1;
	}

	public List<OperadorDTO> getOperadores_c2() {
		return operadores_c2;
	}

	public void setOperadores_c2(List<OperadorDTO> operadores_c2) {
		this.operadores_c2 = operadores_c2;
	}

	public OperadorDTO getSelectedOperador() {
		return selectedOperador;
	}

	public void setSelectedOperador(OperadorDTO selectedOperador) {
		this.selectedOperador = selectedOperador;
	}

	public boolean isOperadorAutenticado() {
		return operadorAutenticado;
	}

	public void setOperadorAutenticado(boolean operadorAutenticado) {
		this.operadorAutenticado = operadorAutenticado;
	}

	public boolean isEstadoLibre() {
		return estadoLibre;
	}

	public void setEstadoLibre(boolean estadoLibre) {
		this.estadoLibre = estadoLibre;
	}

	public boolean isEstadoAtendiendo() {
		return estadoAtendiendo;
	}

	public void setEstadoAtendiendo(boolean estadoAtendiendo) {
		this.estadoAtendiendo = estadoAtendiendo;
	}

	public boolean isEstadoFds() {
		return estadoFds;
	}

	public void setEstadoFds(boolean estadoFds) {
		this.estadoFds = estadoFds;
	}

	public List<MyArray> getServicios() {
		return servicios;
	}

	public void setServicios(List<MyArray> servicios) {
		this.servicios = servicios;
	}

	public TareaDTO getTareaActual() {
		return tareaActual;
	}

	public void setTareaActual(TareaDTO tareaActual) {
		this.tareaActual = tareaActual;
	}

	public Object[] getOpera() {
		return opera;
	}

	public void setOpera(Object[] opera) {
		this.opera = opera;
	}

	public TurnoDTO getSelectedTurno() {
		return selectedTurno;
	}

	public void setSelectedTurno(TurnoDTO selectedTurno) {
		this.selectedTurno = selectedTurno;
	}

	public MyPair getSelectedTareaFds() {
		return selectedTareaFds;
	}

	public void setSelectedTareaFds(MyPair selectedTareaFds) {
		this.selectedTareaFds = selectedTareaFds;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public MyArray getSelectedServicioDerivado() {
		return selectedServicioDerivado;
	}

	public void setSelectedServicioDerivado(MyArray selectedServicioDerivado) {
		this.selectedServicioDerivado = selectedServicioDerivado;
	}

	public boolean isHayTurnos() {
		return hayTurnos;
	}

	public void setHayTurnos(boolean hayTurnos) {
		this.hayTurnos = hayTurnos;
	}

	public List<TurnoDTO> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<TurnoDTO> turnos) {
		this.turnos = turnos;
	}

	public MyArray getSelectedBox() {
		return selectedBox;
	}

	public void setSelectedBox(MyArray selectedBox) {
		this.selectedBox = selectedBox;
	}

	public List<MyArray> getServiciosFinales() {
		return serviciosFinales;
	}

	public void setServiciosFinales(List<MyArray> serviciosFinales) {
		this.serviciosFinales = serviciosFinales;
	}

	public List<MyPair> getOperadores_() {
		return operadores_;
	}

	public void setOperadores_(List<MyPair> operadores_) {
		this.operadores_ = operadores_;
	}

	public MyPair getSelectedOperador_() {
		return selectedOperador_;
	}

	public void setSelectedOperador_(MyPair selectedOperador_) {
		this.selectedOperador_ = selectedOperador_;
	}

	public List<MyPair> getOpcionesFDS() {
		return opcionesFDS;
	}

	public void setOpcionesFDS(List<MyPair> opcionesFDS) {
		this.opcionesFDS = opcionesFDS;
	}

	public MyPair getSelectedFDS() {
		return selectedFDS;
	}

	public void setSelectedFDS(MyPair selectedFDS) {
		this.selectedFDS = selectedFDS;
	}

}
