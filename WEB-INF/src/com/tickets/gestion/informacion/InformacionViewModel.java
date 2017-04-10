package com.tickets.gestion.informacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.coreweb.componente.BodyPopupAceptarCancelar;
import com.coreweb.componente.ViewPdf;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.UsuarioPropiedadApp;
import com.tickets.control.cola.ControlCola;
import com.tickets.control.cola.EstadoOperador;
import com.tickets.domain.Operador;
import com.tickets.gestion.administracion.OperadorAssembler;
import com.tickets.gestion.administracion.OperadorDTO;
import com.tickets.gestion.atencion.OperadorReporte;

public class InformacionViewModel extends SimpleViewModel {

	MyPair sucursal = null;

	@Init(superclass = true)
	public void init() {

		try {
			UsuarioPropiedadApp uss = new UsuarioPropiedadApp(this.getUs());
			this.sucursal = uss.getSucursal();

			this.setAss(new OperadorAssembler());
			
			this.refresh();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterCompose
	public void afterCompose() {
	}

	private List<EstadoOperador> operadores = new ArrayList<EstadoOperador>();
	private List<MyArray> estados = new ArrayList<MyArray>();
	
	private Date ultimoRefresh = new Date();
	private ControlCola ctr = new ControlCola(null);

	@Command @NotifyChange("*")
	public void refresh() throws Exception {
		this.operadores = ctr.estadoOperadores(this.sucursal);
		this.estados = this.ctr.getEstadoColas(this.sucursal);
		
		this.ultimoRefresh = new Date();
	}

	/** Reporte para el operador dadas fechas de incio y fin **/
	@Command
	public void reporteOperador(
			@BindingParam("operador") EstadoOperador operador) throws Exception {

		OperadorDTO _operador = (OperadorDTO) this.getDTOById(
				Operador.class.getName(), operador.getId());

		Grid grid = new Grid();

		Columns cols = new Columns();
		cols.setParent(grid);

		Column col = new Column();
		col.setWidth("100px");
		col.setParent(cols);

		Column col2 = new Column();
		col2.setParent(cols);

		
		Rows rows = new Rows();
		rows.setParent(grid);

		Row row1 = new Row();
		row1.setParent(rows);

		Label label1 = new Label();
		label1.setValue("Inicio:");
		label1.setParent(row1);

		Row row2 = new Row();
		row2.setParent(rows);

		Label label2 = new Label();
		label2.setValue("Fin:");
		label2.setParent(row2);

		Datebox dbx1 = new Datebox();
		dbx1.setValue(this.m.getFechaHoy00());
		dbx1.setWidth("150px");
		dbx1.setReadonly(true);
		dbx1.setParent(row1);

		Datebox dbx2 = new Datebox();
		dbx2.setValue(this.m.getFechaHoy2359());
		dbx2.setWidth("150px");
		dbx2.setReadonly(true);
		dbx2.setParent(row2);

		BodyPopupAceptarCancelar b = new BodyPopupAceptarCancelar();
		b.addComponente("", grid);
		b.setHighWindows("200px");
		b.setWidthWindows("320px");
		b.showPopupUnaColumna("");

		if (b.isClickAceptar()) {
			Date fechaInicio = dbx1.getValue();
			Date fechaFin = dbx2.getValue();
			generarReporte(_operador, fechaInicio, fechaFin);
		}
	}

	/**
	 * Genera el reporte del operador
	 */
	public void generarReporte(OperadorDTO operador, Date fechaInicio,
			Date fechaFin) throws Exception {

		OperadorReporte r = new OperadorReporte();

		r.setInformacion(operador, fechaInicio, fechaFin);

		ViewPdf vp = new ViewPdf();
		vp.showReporte(r, this);
	}
	
	/**
	 * Resetea los tickets..
	 */
	@Command @NotifyChange("*")
	public void resetearTurnos() throws Exception {
		if (this.mensajeSiNo("Esta seguro de Resetear todos los turnos..?") == true) {
			this.ctr.resetearTurnos(this.sucursal);
			this.mensajePopupTemporal("Tickets reiniciados correctamente..");
		}
		System.out.println("----- Reset de Tickets por: " + this.getLoginNombre());
	}

	/**
	 * Resetea las tareas..
	 */
	@Command @NotifyChange("*")
	public void resetearTareas() throws Exception {
		if (this.mensajeSiNo("Esta seguro de Resetear todas las tareas..?") == true) {
			this.ctr.resetearTareas(this.sucursal);
			this.mensajePopupTemporal("Tareas reiniciadas correctamente..");
		}
		System.out.println("----- Reset de Tareas por: " + this.getLoginNombre());
	}

	/******************************* GET/SET ********************************/

	public List<EstadoOperador> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<EstadoOperador> estados) {
		this.operadores = estados;
	}

	public Date getUltimoRefresh() {
		return ultimoRefresh;
	}

	public void setUltimoRefresh(Date ultimoRefresh) {
		this.ultimoRefresh = ultimoRefresh;
	}

	public List<MyArray> getEstados() {
		return estados;
	}

	public void setEstados(List<MyArray> estados) {
		this.estados = estados;
	}
}
