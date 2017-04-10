package com.tickets.domain;

import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Tipo;

@SuppressWarnings("serial")
public class Operador extends Domain {

	private String usuario;
	private String pass;
	private String nombre;
	private String descripcion;
	private Tarea tareaCorriente;
	private Set<Servicio> servicios;
	private Set<Puesto> puestos;
	private Tipo estado;
	private Tipo sucursal;
	private String imagen;

	public Tipo getSucursal() {
		return sucursal;
	}

	public void setSucursal(Tipo sucursal) {
		this.sucursal = sucursal;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Tarea getTareaCorriente() {
		return tareaCorriente;
	}

	public void setTareaCorriente(Tarea tareaCorriente) {
		this.tareaCorriente = tareaCorriente;
	}

	public Set<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(Set<Servicio> servicios) {
		this.servicios = servicios;
	}

	public Set<Puesto> getPuestos() {
		return puestos;
	}

	public void setPuestos(Set<Puesto> puestos) {
		this.puestos = puestos;
	}

	public Tipo getEstado() {
		return estado;
	}

	public void setEstado(Tipo estado) {
		this.estado = estado;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
