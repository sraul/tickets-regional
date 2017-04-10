package com.tickets.domain;

import java.util.Date;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class Tarea extends Domain {

	private String descripcion;
	private boolean activa;
	private Date inicio;
	private Date fin;
	private Tipo tipo;
	private Tipo tipoFds;
	private Operador operador;
	private Puesto puesto;
	private Turno turno;
	private Tipo sucursal;
	private String serviciosAdicionales;
	private String observacion;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Tipo getTipoFds() {
		return tipoFds;
	}

	public void setTipoFds(Tipo tipoFds) {
		this.tipoFds = tipoFds;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Puesto getPuesto() {
		return puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}

	public Tipo getSucursal() {
		return sucursal;
	}

	public void setSucursal(Tipo sucursal) {
		this.sucursal = sucursal;
	}

	public String getServiciosAdicionales() {
		return serviciosAdicionales;
	}

	public void setServiciosAdicionales(String serviciosAdicionales) {
		this.serviciosAdicionales = serviciosAdicionales;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
