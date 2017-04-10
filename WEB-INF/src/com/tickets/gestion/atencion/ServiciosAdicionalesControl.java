package com.tickets.gestion.atencion;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.control.SoloViewModel;
import com.tickets.Configuracion;
import com.tickets.ID;

public class ServiciosAdicionalesControl extends SoloViewModel {

	private AtencionViewModel dato;

	public AtencionViewModel getDato() {
		return dato;
	}

	public void setDato(AtencionViewModel dato) {
		this.dato = dato;
	}

	@Init(superclass = true)
	public void initDerivarTurnoControl(
			@ExecutionArgParam(Configuracion.DATO_SOLO_VIEW_MODEL) AtencionViewModel dato) {
		this.dato = dato;
		this.dato.setObservacion("");
	}

	@AfterCompose(superclass = true)
	public void afterComposeDerivarTurnoControl() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return ID.F_ATENCION;
	}
	
}
