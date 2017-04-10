package com.tickets.control.cola;

import java.util.Date;

public class EstadoOperador {
	long id = 0;
	String nombreOperador = "";
	String descripcion = "";
	Date inicioTarea = null;
	Date finTarea = null;
	String tiempoTareaActiva = "";

	public String toString(){
		String out = "";
		out += this.nombreOperador+" - "+this.descripcion+" - "+this.inicioTarea+" - "+this.finTarea+" - "+this.tiempoTareaActiva;
		return out;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombreOperador() {
		return nombreOperador;
	}

	public void setNombreOperador(String nombreOperador) {
		this.nombreOperador = nombreOperador;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getInicioTarea() {
		return inicioTarea;
	}

	public void setInicioTarea(Date inicioTarea) {
		this.inicioTarea = inicioTarea;
	}

	public Date getFinTarea() {
		return finTarea;
	}

	public void setFinTarea(Date finTarea) {
		this.finTarea = finTarea;
	}

	public String getTiempoTareaActiva() {
		return tiempoTareaActiva;
	}

	public void setTiempoTareaActiva(String tiempoTareaActiva) {
		this.tiempoTareaActiva = tiempoTareaActiva;
	}


}
