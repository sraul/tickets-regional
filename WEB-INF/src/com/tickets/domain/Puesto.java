package com.tickets.domain;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class Puesto extends Domain {

	private String descripcion;
	private Tipo estado;
	private Tipo sucursal;
	private String nombreGenerico;

	/**
	 * Simultaneo indica la cantidad de clientes que pueden atenderse en una
	 * misma ubicación física o puesto.
	 **/
	private int simultaneo;

	public Tipo getSucursal() {
		return sucursal;
	}

	public void setSucursal(Tipo sucursal) {
		this.sucursal = sucursal;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Tipo getEstado() {
		return estado;
	}

	public void setEstado(Tipo estado) {
		this.estado = estado;
	}

	public int getSimultaneo() {
		return simultaneo;
	}

	public void setSimultaneo(int simultaneo) {
		this.simultaneo = simultaneo;
	}

	public String getNombreGenerico() {
		return nombreGenerico;
	}

	public void setNombreGenerico(String nombreGenerico) {
		this.nombreGenerico = nombreGenerico;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
