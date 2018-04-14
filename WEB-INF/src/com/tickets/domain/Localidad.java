package com.tickets.domain;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class Localidad extends Domain {
	
	private String codigoDpto;
	private String departamento;
	private String distrito;

	@Override
	public int compareTo(Object arg0) {
		return -1;
	}

	public String getCodigoDpto() {
		return codigoDpto;
	}

	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
}
