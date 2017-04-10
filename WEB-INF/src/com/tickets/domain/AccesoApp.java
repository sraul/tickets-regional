package com.tickets.domain;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Usuario;

@SuppressWarnings("serial")
public class AccesoApp extends Domain {

	private String descripcion;
	private Usuario usuario;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
