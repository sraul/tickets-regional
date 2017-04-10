package com.tickets.util.reporte;

import com.coreweb.extras.reporte.DatosReporte;
import com.tickets.Configuracion;

public abstract class ReporteTurnos extends DatosReporte {

	
	@Override
	public void setDatosReportes() {
		
		this.setEmpresa(Configuracion.EMPRESA);
		
		this.informacionReporte();
	}
	
	
	public abstract void informacionReporte();
	
}
