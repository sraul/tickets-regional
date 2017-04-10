package com.tickets.gestion.panel;

import java.util.Date;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

public class TareaDTO extends DTO {

	private String descripcion = "";
	private boolean activa = false;
	private Date inicio;
	private Date fin;
	private MyPair tipo;
	private MyPair tipoFds;
	private MyArray operador;
	private MyArray puesto;
	private MyPair sucursal = new MyPair();
	private TurnoDTO turno;
	private String serviciosAdicionales = "";
	private String observacion = "";

	@Override
	public String[] getCamposMyArray() {
		String[] campos = { "descripcion", "activa", "inicio", "fin",
				"operador", "puesto", "turno" };
		return campos;
	}

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

	public MyPair getTipo() {
		return tipo;
	}

	public void setTipo(MyPair tipo) {
		this.tipo = tipo;
	}

	public MyPair getTipoFds() {
		return tipoFds;
	}

	public void setTipoFds(MyPair tipoFds) {
		this.tipoFds = tipoFds;
	}

	public MyArray getOperador() {
		return operador;
	}

	public void setOperador(MyArray operador) {
		this.operador = operador;
	}

	public MyArray getPuesto() {
		return puesto;
	}

	public void setPuesto(MyArray puesto) {
		this.puesto = puesto;
	}

	public TurnoDTO getTurno() {
		return turno;
	}

	public void setTurno(TurnoDTO turno) {
		this.turno = turno;
	}

	public MyPair getSucursal() {
		return sucursal;
	}

	public void setSucursal(MyPair sucursal) {
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

}
