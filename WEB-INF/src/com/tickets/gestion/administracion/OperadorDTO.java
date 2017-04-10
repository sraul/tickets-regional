package com.tickets.gestion.administracion;

import java.util.ArrayList;
import java.util.List;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

@SuppressWarnings("serial")
public class OperadorDTO extends DTO {

	private String usuario = "";
	private String pass = "";
	private String nombre = "";
	private String descripcion = "";
	private MyArray tareaCorriente = new MyArray();
	private MyPair sucursal = new MyPair();
	private List<MyArray> servicios = new ArrayList<MyArray>();
	private List<MyArray> puestos = new ArrayList<MyArray>();
	private MyPair estado = new MyPair();
	private String imagen = "";

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

	public MyArray getTareaCorriente() {
		return tareaCorriente;
	}

	public void setTareaCorriente(MyArray tareaCorriente) {
		this.tareaCorriente = tareaCorriente;
	}

	public List<MyArray> getServicios() {
		return servicios;
	}

	public void setServicios(List<MyArray> servicios) {
		this.servicios = servicios;
	}

	public MyPair getEstado() {
		return estado;
	}

	public void setEstado(MyPair estado) {
		this.estado = estado;
	}

	public List<MyArray> getPuestos() {
		return puestos;
	}

	public void setPuestos(List<MyArray> puestos) {
		this.puestos = puestos;
	}

	public MyPair getSucursal() {
		return sucursal;
	}

	public void setSucursal(MyPair sucursal) {
		this.sucursal = sucursal;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}
