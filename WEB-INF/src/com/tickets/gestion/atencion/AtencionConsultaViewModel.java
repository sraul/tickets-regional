package com.tickets.gestion.atencion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.util.Clients;

import com.coreweb.control.SimpleViewModel;

public class AtencionConsultaViewModel extends SimpleViewModel {
	
	@Init(superclass = true)
	public void init() {
	}

	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	public void guardarConsulta() {
		Clients.showNotification("CONSULTA GUARDADA..!");
	}
	
	/**
	 * GETS / SETS
	 */
	
	public List<String> getTiposEstudios() {
		List<String> out = new ArrayList<String>();
		out.add("Análisis de Sangre");
		out.add("Análisis de Orina");
		out.add("Electrocardiograma");
		out.add("Radiografía");
		out.add("Tomografía");
		return out;
	}
}
