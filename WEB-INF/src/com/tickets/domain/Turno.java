package com.tickets.domain;

import java.util.Date;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class Turno extends Domain {

	private String numero;
	private String descripcion;
	private Date creacion = new Date();
	private int prioridad = 1;
	private Date inicioAtencion;
	private Date finAtencion;
	private Tipo estado;
	private Tipo sucursal;
	private Cliente cliente;
	private Servicio servicio;
	private Derivado derivado;
	private String remitido;
	
	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getCreacion() {
		return creacion;
	}

	public void setCreacion(Date creacion) {
		this.creacion = creacion;
	}

	public Date getInicioAtencion() {
		return inicioAtencion;
	}

	public void setInicioAtencion(Date inicioAtencion) {
		this.inicioAtencion = inicioAtencion;
	}

	public Date getFinAtencion() {
		return finAtencion;
	}

	public void setFinAtencion(Date finAtencion) {
		this.finAtencion = finAtencion;
	}

	public Tipo getEstado() {
		return estado;
	}

	public void setEstado(Tipo estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Derivado getDerivado() {
		return derivado;
	}

	public void setDerivado(Derivado derivado) {
		this.derivado = derivado;
	}

	public Tipo getSucursal() {
		return sucursal;
	}

	public void setSucursal(Tipo sucursal) {
		this.sucursal = sucursal;
	}	

	public String getRemitido() {
		return remitido;
	}

	public void setRemitido(String remitido) {
		this.remitido = remitido;
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
