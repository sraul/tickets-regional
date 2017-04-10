package com.tickets.gestion.atencion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.control.SoloViewModel;
import com.coreweb.util.MyPair;
import com.tickets.Configuracion;
import com.tickets.ID;
import com.tickets.UtilDTO;

public class FDSControl extends SoloViewModel {

	private AtencionViewModel dato;

	public AtencionViewModel getDato() {
		return dato;
	}

	public void setDato(AtencionViewModel dato) {
		this.dato = dato;
	}

	List<MyPair> tipos = new ArrayList<MyPair>();

	public List<MyPair> getTipos() {
		return tipos;
	}

	public void setTipos(List<MyPair> tipos) {
		this.tipos = tipos;
	}

	@Init(superclass = true)
	public void initDerivarTurnoControl(
			@ExecutionArgParam(Configuracion.DATO_SOLO_VIEW_MODEL) AtencionViewModel dato) {
		this.dato = dato;
		this.cargarTipos();
	}

	@AfterCompose(superclass = true)
	public void afterComposeDerivarTurnoControl() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return ID.F_ATENCION;
	}

	public void cargarTipos() {
		for (MyPair mp : ((UtilDTO) this.getDtoUtil()).getTiposDeTareaFDS()) {
			this.tipos.add(mp);
		}
	}

}
