package com.tickets.domain;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class Pais extends Domain {
	
	private String descripcion;
	private String nacionalidad;

	@Override
	public int compareTo(Object arg0) {
		return -1;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
}
