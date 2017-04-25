package com.tickets.gestion.informacion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.coreweb.control.SimpleViewModel;

public class InformacionViewModel extends SimpleViewModel {

	static final String REP_1 = "CANTIDAD DE TICKETS EMITIDOS";
	static final String REP_2 = "CANTIDAD DE TICKETS CANCELADOS";
	static final String REP_3 = "AUDITORÍA DE OPERADORES";

	@Init(superclass = true)
	public void init() {
		
	}

	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	public List<String> getReportes() {
		List<String> out = new ArrayList<String>();
		out.add(REP_1);
		out.add(REP_2);
		out.add(REP_3);
		return out;
	}
}
