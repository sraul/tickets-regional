package com.tickets.gestion.atencion;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.control.SoloViewModel;
import com.tickets.Configuracion;
import com.tickets.ID;
import com.tickets.gestion.administracion.OperadorDTO;

public class AutenticarOperadorControl extends SoloViewModel {

	private OperadorDTO dato;

	public OperadorDTO getDato() {
		return dato;
	}

	public void setDato(OperadorDTO dato) {
		this.dato = dato;
	}

	@Init(superclass = true)
	public void initAutenticarOperadorControl(
			@ExecutionArgParam(Configuracion.DATO_SOLO_VIEW_MODEL) OperadorDTO dato) {
		this.dato = dato;
	}

	@AfterCompose(superclass = true)
	public void afterComposeAutenticarOperadorControl() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return ID.F_ATENCION;
	}

}
