package com.tickets.gestion.panel;

import java.util.Date;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.Configuracion;

@SuppressWarnings("serial")
public class TurnoDTO extends DTO {

	private String numero = "";
	private String descripcion = Configuracion.TICKET_DISPONIBLE;
	private Date creacion = new Date();
	private int prioridad = 1;
	private Date inicioAtencion;
	private Date finAtencion;
	// Ojo, no va en la BD, esto se usa simplificar a la hora de mostrar en que puesto se está atendiendo n turno.
	private String puesto = "";
	private String remitido = "";

	private MyPair sucursal = new MyPair();
	private MyPair estado = new MyPair();
	private MyArray cliente = new MyArray();
	private MyArray servicio = new MyArray();
	private MyArray derivado = new MyArray();

	private long espera = 0;
	
	public String getNumero_() {
		char letra = this.numero.charAt(0);
		String nro = this.numero.substring(1);
		return letra + String.format("%07d", Integer.parseInt(nro));
	}
	
	public String getEsperaStr() {
		return this.getMisc().tiempoTareas(this.espera);
	}

	public void setEsperaStr(String esperaStr) {
		System.out.println("NO se debe usar TurnoDTO.setEsperaStr");
	}

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

	public MyPair getEstado() {
		return estado;
	}

	public void setEstado(MyPair estado) {
		this.estado = estado;
	}

	public MyArray getCliente() {
		return cliente;
	}

	public void setCliente(MyArray cliente) {
		this.cliente = cliente;
	}

	public MyArray getServicio() {
		return servicio;
	}

	public void setServicio(MyArray servicio) {
		this.servicio = servicio;
	}

	public MyArray getDerivado() {
		return derivado;
	}

	public void setDerivado(MyArray derivado) {
		this.derivado = derivado;
	}

	public MyPair getSucursal() {
		return sucursal;
	}

	public void setSucursal(MyPair sucursal) {
		this.sucursal = sucursal;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public long getEspera() {
		return espera;
	}

	public void setEspera(long espera) {
		this.espera = espera;
	}

	public String getRemitido() {
		return remitido;
	}

	public void setRemitido(String remitido) {
		this.remitido = remitido;
	}

}
