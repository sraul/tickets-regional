package com.tickets.gestion.atencion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.coreweb.componente.VerificaAceptarCancelar;
import com.coreweb.control.SoloViewModel;
import com.coreweb.util.MyArray;
import com.tickets.Configuracion;
import com.tickets.ID;
import com.tickets.UtilDTO;
import com.tickets.control.cola.ControlCola;
import com.tickets.domain.Puesto;
import com.tickets.domain.RegisterDomain;
import com.tickets.gestion.panel.TurnoDTO;

public class OperacionesTurnoControl extends SoloViewModel implements
		VerificaAceptarCancelar {

	UtilDTO dtoUtil = (UtilDTO) this.getDtoUtil();

	private AtencionViewModel dato;
	private TurnoDTO turno;
	private List<MyArray> puestos = new ArrayList<MyArray>();
	private boolean hayPuestoSeleccionado = false;

	ControlCola cc = new ControlCola(null);

	public AtencionViewModel getDato() {
		return dato;
	}

	public void setDato(AtencionViewModel dato) {
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

	public boolean isHayPuestoSeleccionado() {
		return hayPuestoSeleccionado;
	}

	public void setHayPuestoSeleccionado(boolean hayPuestoSeleccionado) {
		this.hayPuestoSeleccionado = hayPuestoSeleccionado;
	}

	@Init(superclass = true)
	public void initOperacionesTurnoControl(
			@ExecutionArgParam(Configuracion.DATO_SOLO_VIEW_MODEL) AtencionViewModel dato)
			throws Exception {
		this.dato = dato;
		this.turno = this.dato.getSelectedTurno();
		this.cargarPuestos();
	}

	@AfterCompose(superclass = true)
	public void afterComposeOperacionesTurnoControl() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return ID.F_ATENCION;
	}

	@Command
	@NotifyChange("*")
	public void cargarPuestos() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		List<Puesto> puestos = rr.getPuestosDisponibles(this.dato
				.getSelectedOperador().getId());
		this.puestos = new ArrayList<MyArray>();
		for (Puesto puesto : puestos) {
			MyArray p = new MyArray();
			p.setId(puesto.getId());
			p.setPos1(puesto.getDescripcion());
			this.puestos.add(p);
		}
		/*
		 * for (MyArray ma : ((UtilDTO) this.getDtoUtil()).getPuestos()) {
		 * this.puestos.add(ma); }
		 */
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
		long ahora = System.currentTimeMillis();
		if ((ahora - ultimoClickLlamar) < tiempoEspera) {
			return;
		}
		ultimoClickLlamar = ahora;
		//cc.llamarTurno(this.turno, (String) this.dato.getSelectedPuesto()
		//		.getPos1());
		String mensaje = "Hacer click en Aceptar para completar la operación ";
		this.mensajePopupTemporal(mensaje, 5000);
	}

	/**
	 * Atiende el turno seleccionado y cambia el estado del operador
	 */
	@Command
	@NotifyChange("*")
	public void atenderTurno() throws Exception {
		this.dato.getSelectedTurno().setEstado(
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
		this.dato.setObsvCancelacion(this.getMotivoAnulacion());
		this.dato.getSelectedTurno().setEstado(
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
		this.dato.getSelectedTurno().setEstado(
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
		this.hayPuestoSeleccionado = true;
	}

	/**************************** VALIDACIONES *************************/

	private String msgError = "";

	@Override
	public boolean verificarAceptar() {
		System.out.println("+++++++++++++++++++++++Entro a verificarAceptar");
		long estadoTurno = this.dato.getSelectedTurno().getEstado().getId();
		long estadoAtendiendo = this.dtoUtil
				.getEstadoAtendiendo().getId();
		long estadoEsperando = this.dtoUtil
				.getEstadoEsperando().getId();
		long estadoLlamando = this.dtoUtil.getEstadoLlamando()
				.getId();
		long estadoCancelado = this.dtoUtil
				.getEstadoCancelado().getId();
		if (estadoTurno == estadoLlamando) {
			System.out.println("+++++++++++++++++++++++Selecciono llamar");
			if (estadoTurno == estadoAtendiendo || estadoTurno == estadoCancelado
					|| estadoTurno == estadoEsperando) {
				System.out.println("+++++++++++++++++++++++Selecciono otro ademas de llamar");
				//this.msgError = "La operación se ha realizado con éxito.";
				return true;
			} else {
				System.out.println("+++++++++++++++++++++++No selecciono otro ademas de llamar");
				this.msgError = "Debe seleccionar alguna de las siguientes opciones: "
						+ "Atender, Cancelar Turno o Llamar luego. ";
				return false;
			}
		} else {
			System.out.println("+++++++++++++++++++++++No selecciono nada");
			//this.msgError = "No se ha realizado ninguna operación.";
			return true;
		}
	}

	@Override
	public String textoVerificarAceptar() {
		return this.msgError;
	}

	@Override
	public boolean verificarCancelar() {
		System.out.println("+++++++++++++++++++++++Entro a verificarCancelar");
		long estadoTurno = this.dato.getSelectedTurno().getEstado().getId();
		long estadoAtendiendo = this.dtoUtil
				.getEstadoAtendiendo().getId();
		long estadoEsperando = this.dtoUtil
				.getEstadoEsperando().getId();
		//long estadoLlamando = this.dtoUtil.getEstadoLlamando()
		//		.getId();
		long estadoCancelado = this.dtoUtil
				.getEstadoCancelado().getId();
		if (estadoTurno == estadoAtendiendo || estadoTurno == estadoCancelado
				|| estadoTurno == estadoEsperando){
			this.mensajePopupTemporalWarning("Todas los cambios aplicados se deshacerán");
		}
			
		return true;
	}

	@Override
	public String textoVerificarCancelar() {
		return "Error Al Cancelar";
	}

}
