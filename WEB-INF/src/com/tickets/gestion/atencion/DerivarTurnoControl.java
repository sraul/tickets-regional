package com.tickets.gestion.atencion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.control.SoloViewModel;
import com.coreweb.util.MyArray;
import com.tickets.Configuracion;
import com.tickets.ID;
import com.tickets.UtilDTO;

public class DerivarTurnoControl extends SoloViewModel {

	private AtencionViewModel dato;

	public AtencionViewModel getDato() {
		return dato;
	}

	public void setDato(AtencionViewModel dato) {
		this.dato = dato;
	}

	List<MyArray> serviciosFinales = new ArrayList<MyArray>();

	public List<MyArray> getServiciosFinales() {
		return serviciosFinales;
	}

	public void setServiciosFinales(List<MyArray> serviciosFinales) {
		this.serviciosFinales = serviciosFinales;
	}

	@Init(superclass = true)
	public void initDerivarTurnoControl(
			@ExecutionArgParam(Configuracion.DATO_SOLO_VIEW_MODEL) AtencionViewModel dato) {
		this.dato = dato;
		this.cargarServiciosFinales();
	}

	@AfterCompose(superclass = true)
	public void afterComposeDerivarTurnoControl() {
	}

	@Override
	public String getAliasFormularioCorriente() {
		return ID.F_ATENCION;
	}

	public void cargarServiciosFinales() {
		for (MyArray ma : ((UtilDTO) this.getDtoUtil()).getServicios()) {
			this.serviciosFinales.add(ma);
		}
	}

}
