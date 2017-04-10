package com.tickets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.coreweb.dto.UtilCoreDTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;

@SuppressWarnings("serial")
public class UtilDTO extends UtilCoreDTO {

	private List<MyPair> estadosDeTurnos = new ArrayList<MyPair>();
	private MyPair estadoBloqueado = new MyPair();
	private MyPair estadoLlamando = new MyPair();
	private MyPair estadoAtendiendo = new MyPair();
	private MyPair estadoEsperando = new MyPair();
	private MyPair estadoTerminado = new MyPair();
	private MyPair estadoCancelado = new MyPair();

	private List<MyPair> tiposDeTarea = new ArrayList<MyPair>();
	private MyPair tareaAtencion = new MyPair();
	private MyPair tareaFDS = new MyPair();

	private List<MyPair> tiposDeTareaFDS = new ArrayList<MyPair>();
	private MyPair FDS_desayuno = new MyPair();
	private MyPair FDS_almuerzo = new MyPair();
	private MyPair FDS_merienda = new MyPair();
	private MyPair FDS_telemarketing = new MyPair();
	private MyPair fds_presupuesto = new MyPair();
	private MyPair fds_visita = new MyPair();
	private MyPair fds_sanitario = new MyPair();
	private MyPair fds_reunion = new MyPair();
	private MyPair fds_servicio_tecnico = new MyPair();
	private MyPair fds_capacitacion = new MyPair();
	private MyPair fds_deposito = new MyPair();
	private MyPair fds_permiso = new MyPair();

	private List<MyPair> estadosDeOperadores = new ArrayList<MyPair>();
	private MyPair estadoOperadorLibre = new MyPair();
	private MyPair estadoOperadorAtendiendo = new MyPair();
	private MyPair estadoOperadorFds = new MyPair();

	private List<MyPair> estadosDeServicios = new ArrayList<MyPair>();
	private MyPair estadoServicioActivo = new MyPair();
	private MyPair estadoServicioInactivo = new MyPair();

	private List<MyPair> estadosDePuestos = new ArrayList<MyPair>();
	private MyPair estadoPuestoActivo = new MyPair();
	private MyPair estadoPuestoInactivo = new MyPair();

	private List<MyArray> servicios = new ArrayList<MyArray>();
	private List<MyArray> puestos = new ArrayList<MyArray>();
	
	private MyArray clienteNuevoPorDefecto = new MyArray();

	public MyPair getEstadoBloqueado() {
		return estadoBloqueado;
	}

	public void setEstadoBloqueado(MyPair estadoBloqueado) {
		this.estadoBloqueado = estadoBloqueado;
	}

	public MyPair getEstadoLlamando() {
		return estadoLlamando;
	}

	public void setEstadoLlamando(MyPair estadoLlamando) {
		this.estadoLlamando = estadoLlamando;
	}

	public List<MyPair> getEstadosDeTurnos() {
		return estadosDeTurnos;
	}

	public void setEstadosDeTurnos(List<MyPair> estadosDeTurnos) {
		this.estadosDeTurnos = estadosDeTurnos;
	}

	public List<MyPair> getTiposDeTarea() {
		return tiposDeTarea;
	}

	public void setTiposDeTarea(List<MyPair> tiposDeTarea) {
		this.tiposDeTarea = tiposDeTarea;
	}

	public List<MyPair> getTiposDeTareaFDS() {
		return tiposDeTareaFDS;
	}

	public void setTiposDeTareaFDS(List<MyPair> tiposDeTareaFDS) {
		this.tiposDeTareaFDS = tiposDeTareaFDS;
	}

	public MyPair getEstadoAtendiendo() {
		return estadoAtendiendo;
	}

	public void setEstadoAtendiendo(MyPair estadoAtendiendo) {
		this.estadoAtendiendo = estadoAtendiendo;
	}

	public MyPair getEstadoEsperando() {
		return estadoEsperando;
	}

	public void setEstadoEsperando(MyPair estadoEsperando) {
		this.estadoEsperando = estadoEsperando;
	}

	public MyPair getEstadoTerminado() {
		return estadoTerminado;
	}

	public void setEstadoTerminado(MyPair estadoTerminado) {
		this.estadoTerminado = estadoTerminado;
	}

	public MyPair getEstadoCancelado() {
		return estadoCancelado;
	}

	public void setEstadoCancelado(MyPair estadoCancelado) {
		this.estadoCancelado = estadoCancelado;
	}

	public MyPair getTareaAtencion() {
		return tareaAtencion;
	}

	public void setTareaAtencion(MyPair tareaAtencion) {
		this.tareaAtencion = tareaAtencion;
	}

	public MyPair getTareaFDS() {
		return tareaFDS;
	}

	public void setTareaFDS(MyPair tareaFDS) {
		this.tareaFDS = tareaFDS;
	}

	public MyPair getFDS_desayuno() {
		return FDS_desayuno;
	}

	public void setFDS_desayuno(MyPair fDS_desayuno) {
		FDS_desayuno = fDS_desayuno;
	}

	public MyPair getFDS_almuerzo() {
		return FDS_almuerzo;
	}

	public void setFDS_almuerzo(MyPair fDS_almuerzo) {
		FDS_almuerzo = fDS_almuerzo;
	}

	public MyPair getFDS_merienda() {
		return FDS_merienda;
	}

	public void setFDS_merienda(MyPair fDS_merienda) {
		FDS_merienda = fDS_merienda;
	}

	public List<MyArray> getServicios() {
		Collections.sort(this.servicios, new Comparator<MyArray>() {
			@Override
			public int compare(MyArray o1, MyArray o2) {				
				return o1.getPos1().toString().compareTo(o2.getPos1().toString());
			}
		});
		return servicios;
	}

	public void setServicios(List<MyArray> servicios) {
		this.servicios = servicios;
	}

	public List<MyPair> getEstadosDeOperadores() {
		return estadosDeOperadores;
	}

	public void setEstadosDeOperadores(List<MyPair> estadosDeOperadores) {
		this.estadosDeOperadores = estadosDeOperadores;
	}

	public MyPair getEstadoOperadorLibre() {
		return estadoOperadorLibre;
	}

	public void setEstadoOperadorLibre(MyPair estadoOperadorLibre) {
		this.estadoOperadorLibre = estadoOperadorLibre;
	}

	public MyPair getEstadoOperadorAtendiendo() {
		return estadoOperadorAtendiendo;
	}

	public void setEstadoOperadorAtendiendo(MyPair estadoOperadorAtendiendo) {
		this.estadoOperadorAtendiendo = estadoOperadorAtendiendo;
	}

	public MyPair getEstadoOperadorFds() {
		return estadoOperadorFds;
	}

	public void setEstadoOperadorFds(MyPair estadoOperadorFds) {
		this.estadoOperadorFds = estadoOperadorFds;
	}

	public List<MyPair> getEstadosDeServicios() {
		return estadosDeServicios;
	}

	public void setEstadosDeServicios(List<MyPair> estadosDeServicios) {
		this.estadosDeServicios = estadosDeServicios;
	}

	public MyPair getEstadoServicioActivo() {
		return estadoServicioActivo;
	}

	public void setEstadoServicioActivo(MyPair estadoServicioActivo) {
		this.estadoServicioActivo = estadoServicioActivo;
	}

	public MyPair getEstadoServicioInactivo() {
		return estadoServicioInactivo;
	}

	public void setEstadoServicioInactivo(MyPair estadoServicioInactivo) {
		this.estadoServicioInactivo = estadoServicioInactivo;
	}

	public List<MyPair> getEstadosDePuestos() {
		return estadosDePuestos;
	}

	public void setEstadosDePuestos(List<MyPair> estadosDePuestos) {
		this.estadosDePuestos = estadosDePuestos;
	}

	public MyPair getEstadoPuestoActivo() {
		return estadoPuestoActivo;
	}

	public void setEstadoPuestoActivo(MyPair estadoPuestoActivo) {
		this.estadoPuestoActivo = estadoPuestoActivo;
	}

	public MyPair getEstadoPuestoInactivo() {
		return estadoPuestoInactivo;
	}

	public void setEstadoPuestoInactivo(MyPair estadoPuestoInactivo) {
		this.estadoPuestoInactivo = estadoPuestoInactivo;
	}

	public List<MyArray> getPuestos() {
		return puestos;
	}

	public void setPuestos(List<MyArray> puestos) {
		this.puestos = puestos;
	}

	public MyArray getClienteNuevoPorDefecto() {
		return clienteNuevoPorDefecto;
	}

	public void setClienteNuevoPorDefecto(MyArray clienteNuevoPorDefecto) {
		this.clienteNuevoPorDefecto = clienteNuevoPorDefecto;
	}

	public MyPair getFDS_telemarketing() {
		return FDS_telemarketing;
	}

	public void setFDS_telemarketing(MyPair fDS_telemarketing) {
		FDS_telemarketing = fDS_telemarketing;
	}

	public MyPair getFds_presupuesto() {
		return fds_presupuesto;
	}

	public void setFds_presupuesto(MyPair fds_presupuesto) {
		this.fds_presupuesto = fds_presupuesto;
	}

	public MyPair getFds_visita() {
		return fds_visita;
	}

	public void setFds_visita(MyPair fds_visita) {
		this.fds_visita = fds_visita;
	}

	public MyPair getFds_sanitario() {
		return fds_sanitario;
	}

	public void setFds_sanitario(MyPair fds_sanitario) {
		this.fds_sanitario = fds_sanitario;
	}

	public MyPair getFds_reunion() {
		return fds_reunion;
	}

	public void setFds_reunion(MyPair fds_reunion) {
		this.fds_reunion = fds_reunion;
	}

	public MyPair getFds_servicio_tecnico() {
		return fds_servicio_tecnico;
	}

	public void setFds_servicio_tecnico(MyPair fds_servicio_tecnico) {
		this.fds_servicio_tecnico = fds_servicio_tecnico;
	}

	public MyPair getFds_capacitacion() {
		return fds_capacitacion;
	}

	public void setFds_capacitacion(MyPair fds_capacitacion) {
		this.fds_capacitacion = fds_capacitacion;
	}

	public MyPair getFds_deposito() {
		return fds_deposito;
	}

	public void setFds_deposito(MyPair fds_deposito) {
		this.fds_deposito = fds_deposito;
	}

	public MyPair getFds_permiso() {
		return fds_permiso;
	}

	public void setFds_permiso(MyPair fds_permiso) {
		this.fds_permiso = fds_permiso;
	}

}
