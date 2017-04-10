package com.tickets.gestion.atencion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.control.SoloViewModel;
import com.tickets.Configuracion;
import com.tickets.ID;
import com.tickets.UtilDTO;
import com.tickets.control.cola.ControlCola;
import com.tickets.gestion.panel.TurnoDTO;

public class SeleccionarTurnoControl extends SoloViewModel {

	UtilDTO dtoUtil = (UtilDTO) this.getDtoUtil();

	//private OperadorDTO dato;
	private AtencionViewModel dato;
	private List<TurnoDTO> turnos = new ArrayList<TurnoDTO>();
	private boolean hayTurnos = false;
	ControlCola cc = new ControlCola(null);
	
	Date date = null;

	public AtencionViewModel getDato() {
		return dato;
	}

	public void setDato(AtencionViewModel dato) {
		this.dato = dato;
	}

	public List<TurnoDTO> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<TurnoDTO> turnos) {
		this.turnos = turnos;
	}

	public boolean isHayTurnos() {
		return hayTurnos;
	}

	public void setHayTurnos(boolean hayTurnos) {
		this.hayTurnos = hayTurnos;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Init(superclass = true)
	public void initSeleccionarTurnoControl(
			@ExecutionArgParam(Configuracion.DATO_SOLO_VIEW_MODEL) AtencionViewModel dato) {
		this.dato = dato;
		this.date = new Date();
		this.cargarTurnos();
		if ((turnos != null) && (turnos.size()>0)){
			this.dato.setSelectedTurno(this.turnos.get(0));
		}
	}

	@AfterCompose(superclass = true)
	public void afterComposeSeleccionarTurnoControl() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return ID.F_ATENCION;
	}

	/**
	 * Obtiene los posibles turnos que el operador puede atender
	 */
	public void cargarTurnos() {
		//RegisterDomain rr = RegisterDomain.getInstance();
		try {
			this.turnos = cc.getTurnosDisponiblesByOperador(this.dato.getSelectedOperador());
			if (this.turnos != null && !this.turnos.isEmpty()) {
				this.hayTurnos = true;
				this.dato.setSelectedTurno(this.turnos.get(0));
				Date ahora = new Date();
				for (TurnoDTO turnoDto : this.turnos) {
					turnoDto.setEspera((ahora.getTime() - turnoDto.getCreacion().getTime()) );
					
				}
			} 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
