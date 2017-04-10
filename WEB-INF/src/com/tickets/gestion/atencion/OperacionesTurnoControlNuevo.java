package com.tickets.gestion.atencion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.coreweb.componente.VerificaAceptarCancelar;
import com.coreweb.componente.WindowPopup;
import com.coreweb.control.SoloViewModel;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.Configuracion;
import com.tickets.ID;
import com.tickets.UtilDTO;
import com.tickets.control.cola.ControlCola;
import com.tickets.domain.Puesto;
import com.tickets.domain.RegisterDomain;
import com.tickets.gestion.administracion.OperadorDTO;
import com.tickets.gestion.panel.TurnoDTO;

public class OperacionesTurnoControlNuevo extends SoloViewModel implements
		VerificaAceptarCancelar {

	private OperacionesTurnoControlNuevo dato;
	private WindowPopup wp;

	UtilDTO dtoUtil = (UtilDTO) this.getDtoUtil();
	private TurnoDTO turno = new TurnoDTO();
	private OperadorDTO operador = new OperadorDTO();
	private List<MyArray> puestos = new ArrayList<MyArray>();
	private MyArray selectedPuesto = new MyArray();
	private boolean hayPuestoSeleccionado = false;
	private String observacion = "";
	private boolean hizoAlgunaOpereacion = false;

	ControlCola cc = new ControlCola(null);

	public OperacionesTurnoControlNuevo getDato() {
		return dato;
	}

	public void setDato(OperacionesTurnoControlNuevo dato) {
		this.dato = dato;
	}

	public TurnoDTO getTurno() {
		return turno;
	}

	public void setTurno(TurnoDTO turno) {
		this.turno = turno;
	}

	public List<MyArray> getPuestos() {
		return puestos;
	}

	public void setPuestos(List<MyArray> puestos) {
		this.puestos = puestos;
	}

	public MyArray getSelectedPuesto() {
		return selectedPuesto;
	}

	public void setSelectedPuesto(MyArray selectedPuesto) {
		this.selectedPuesto = selectedPuesto;
	}

	public boolean isHayPuestoSeleccionado() {
		return hayPuestoSeleccionado;
	}

	public void setHayPuestoSeleccionado(boolean hayPuestoSeleccionado) {
		this.hayPuestoSeleccionado = hayPuestoSeleccionado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Init(superclass = true)
	public void init(
			@ExecutionArgParam(Configuracion.DATO_SOLO_VIEW_MODEL) OperacionesTurnoControlNuevo dato)
			throws Exception {
		this.dato = dato;
	}

	@AfterCompose(superclass = true)
	public void afterCompose() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return ID.F_ATENCION;
	}

	
	
	// Metodo que despliega la ventana..
	public void show(String modo, OperacionesTurnoControlNuevo dato, TurnoDTO turno, OperadorDTO operador) {
		try {
			if (turno != null) {
				this.turno = turno;
			}
			if (operador != null) {
				this.operador = operador;
				cargarPuestos();
			}
			wp = new WindowPopup();
			wp.setDato(dato);
			wp.setCheckAC(dato);
			wp.setModo(modo);
			wp.setTitulo("Operaciones");
			wp.setWidth("700px");
			wp.setHigth("550px");
			wp.show(Configuracion.OPERACIONES_TURNO_ZUL);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isClickAceptar() {
		return this.wp.isClickAceptar();
	}

	@Command @NotifyChange("*")
	public void cargarPuestos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		List<Puesto> puestos = rr.getPuestosDisponibles(
				this.operador.getId());
		this.puestos = new ArrayList<MyArray>();
		for (Puesto puesto : puestos) {
			MyArray p = new MyArray();
			p.setId(puesto.getId());
			p.setPos1(puesto.getDescripcion());
			this.puestos.add(p);
		}
	}

	/**
	 * Llama al turno seleccionado
	 * 
	 * @throws Exception
	 */
	long ultimoClickLlamar = 0;
	long tiempoEspera = 3 * 1000;

	@Command
	@NotifyChange("*")
	public void llamarTurno() throws Exception {
		this.hizoAlgunaOpereacion = true;
		long ahora = System.currentTimeMillis();
		if ((ahora - ultimoClickLlamar) < tiempoEspera) {
			return;
		}
		ultimoClickLlamar = ahora;
		//cc.llamarTurno(this.dato.getTurno(), (String) this.dato.selectedPuesto.getPos1());
		//String mensaje = "Hacer click en Aceptar para completar la operación ";
		String mensaje = "Llamando...";
		this.mensajePopupTemporal(mensaje, 5000);
	}

	/**
	 * Atiende el turno seleccionado y cambia el estado del operador
	 */
	@Command
	@NotifyChange("*")
	public void atenderTurno() throws Exception {
		this.hizoAlgunaOpereacion = true;

		this.dato.getTurno().setEstado(
				this.dtoUtil.getEstadoAtendiendo());
		/*
		 * cc.atenderTurno(this.turno, this.dato.getSelectedOperador(),
		 * this.selectedPuesto);
		 */
		// this.saltoDePagina("/turnos/gestion/atencion.zul");
		String mensaje = "Hacer click en Aceptar para completar la operación ";
		this.mensajePopupTemporal(mensaje, 5000);

	}

	/**
	 * Cancela el turno seleccionado
	 * 
	 * @throws Exception
	 */
	@Command
	@NotifyChange("*")
	public void cancelarTurno() throws Exception {
		this.hizoAlgunaOpereacion = true;

		this.dato.setObservacion(this.getMotivoAnulacion());
		this.dato.getTurno().setEstado(
				this.dtoUtil.getEstadoCancelado());
		// cc.cancelarTurno(this.turno, obsv);
		String mensaje = "Hacer click en Aceptar para completar la operación ";
		this.mensajePopupTemporal(mensaje, 5000);
	}

	/**
	 * Llamar luego al turno
	 * 
	 * @throws Exception
	 */
	@Command
	@NotifyChange("*")
	public void llamarLuego() throws Exception {
		this.hizoAlgunaOpereacion = true;

		this.dato.getTurno().setEstado(
				this.dtoUtil.getEstadoEsperando());
		/* cc.llamarTurnoLuego(this.turno); */
		String mensaje = "Hacer click en Aceptar para completar la operación.";
		this.mensajePopupTemporal(mensaje, 5000);
	}

	/**
	 * Habilitar botones
	 * 
	 * @throws Exception
	 */
	@Command
	@NotifyChange("*")
	public void actualizarHayPuestoSeleccionado() throws Exception {
		this.dato.setHayPuestoSeleccionado(true);
	}

	/**************************** VALIDACIONES *************************/

	private String msgError = "";

	@Override
	public boolean verificarAceptar() {
		long estadoTurno = this.turno.getEstado().getId();
		long estadoAtendiendo = this.dtoUtil.getEstadoAtendiendo().getId();
		long estadoEsperando = this.dtoUtil.getEstadoEsperando().getId();
		long estadoLlamando = this.dtoUtil.getEstadoLlamando().getId();
		long estadoCancelado = this.dtoUtil.getEstadoCancelado().getId();
		
		// Si selecciono algo, entonces ok
		if (estadoTurno == estadoAtendiendo
				|| estadoTurno == estadoCancelado
				|| estadoTurno == estadoEsperando) {
			
		//	this.dato.setSelectedPuesto(this.getSelectedPuesto());
			
			return true;
		}		

		// si hice click en llamar, entonces tiene que decidir que hacer
		if (estadoTurno == estadoLlamando) {
			this.msgError = "Debe seleccionar alguna de las siguientes opciones: "
					+ "Atender, Cancelar Turno o Llamar luego. ";
			return false;
		}

		// si no hizo nada, entonces false, para que haga algo
		this.msgError = "Seleccione una opción";
		return false;
	}


	
	public boolean verificarAceptar_OLD() {
		System.out.println("+++++++++++++++++++++++Entro a verificarAceptar");
		System.out.println(" selectedTurno "+ this.turno.getNumero());
		long estadoTurno = this.turno.getEstado().getId();
		long estadoAtendiendo = this.dtoUtil.getEstadoAtendiendo().getId();
		long estadoEsperando = this.dtoUtil.getEstadoEsperando().getId();
		long estadoLlamando = this.dtoUtil.getEstadoLlamando().getId();
		long estadoCancelado = this.dtoUtil.getEstadoCancelado().getId();
		
		if (estadoTurno == estadoLlamando) {
			System.out.println("+++++++++++++++++++++++Selecciono llamar");
			
			if (estadoTurno == estadoAtendiendo
					|| estadoTurno == estadoCancelado
					|| estadoTurno == estadoEsperando) {
				System.out
						.println("+++++++++++++++++++++++Selecciono otro ademas de llamar");
				// this.msgError = "La operación se ha realizado con éxito.";
				return true;
			} else {
				System.out
						.println("+++++++++++++++++++++++No selecciono otro ademas de llamar");
				this.msgError = "Debe seleccionar alguna de las siguientes opciones: "
						+ "Atender, Cancelar Turno o Llamar luego. ";
				return false;
			}
		} else {
			System.out.println("+++++++++++++++++++++++No selecciono nada");
			// this.msgError = "No se ha realizado ninguna operación.";
			return true;
		}
	}
	
	@Override
	public String textoVerificarAceptar() {
		return this.msgError;
	}

	@Override
	public boolean verificarCancelar() {

		if (this.hizoAlgunaOpereacion == true){
			String textoCancelar = "Todas los cambios aplicados se perderán\n Desea salir?";
			if ( this.mensajeSiNo(textoCancelar) == true){
				// poner en esperando de nuevo
				this.turno.setEstado(this.dtoUtil.getEstadoEsperando());
				return true;				
			}else{
				// sigue en el formulario
				return false;
			}
		}
		
		
		return true;
		
/*		
		
		long estadoTurno = this.turno.getEstado().getId();
		
		long estadoAtendiendo = this.dtoUtil.getEstadoAtendiendo().getId();
		long estadoEsperando = this.dtoUtil.getEstadoEsperando().getId();
		long estadoLlamando = this.dtoUtil.getEstadoLlamando().getId();
		long estadoCancelado = this.dtoUtil.getEstadoCancelado().getId();
		
		if (estadoTurno == estadoAtendiendo || estadoTurno == estadoCancelado
				|| estadoTurno == estadoEsperando || estadoTurno == estadoLlamando){
			
			String textoCancelar = "Todas los cambios aplicados se perderán\n Desea salir?";
			if ( this.mensajeSiNo(textoCancelar) == true){
				// poner en esperando de nuevo
				this.turno.setEstado(this.dtoUtil.getEstadoEsperando());
				return true;				
			}else{
				// sigue en el formulario
				return false;
			}
			
		}
			
		return true;
*/
	}


	public boolean verificarCancelar_OLD() {
		System.out.println("+++++++++++++++++++++++Entro a verificarCancelar");
		System.out.println(" selectedTurno "+ this.turno.getNumero());
		long estadoTurno = this.turno.getEstado().getId();
		long estadoAtendiendo = this.dtoUtil.getEstadoAtendiendo().getId();
		long estadoEsperando = this.dtoUtil.getEstadoEsperando().getId();
		//long estadoLlamando = this.dtoUtil.getEstadoLlamando().getId();
		long estadoCancelado = this.dtoUtil.getEstadoCancelado().getId();
		
		if (estadoTurno == estadoAtendiendo || estadoTurno == estadoCancelado
				|| estadoTurno == estadoEsperando){
			this.mensajeInfo("Todas los cambios aplicados se deshacerán");
			this.turno.setEstado(new MyPair());
		}
			
		return true;
	}
	
	
	
	@Override
	public String textoVerificarCancelar() {
		return "Error Al Cancelar";
	}

}
