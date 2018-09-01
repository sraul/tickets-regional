package com.tickets.gestion.informacion;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import com.coreweb.componente.ViewPdf;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.extras.reporte.DatosColumnas;
import com.coreweb.util.Misc;
import com.tickets.domain.Cliente;
import com.tickets.domain.Consulta;
import com.tickets.domain.Operador;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Servicio;
import com.tickets.domain.Turno;
import com.tickets.util.Utiles;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class InformacionViewModel extends SimpleViewModel {

	static final String REP_1 = "CANTIDAD DE TICKETS EMITIDOS";
	static final String REP_2 = "CANTIDAD DE TICKETS CANCELADOS";
	static final String REP_3 = "PRODUCTIVIDAD DE MEDICOS";
	static final String REP_4 = "TICKETS EMITIDOS AGRUPADO POR ESPECIALIDAD";
	static final String REP_5 = "HISTORIAL DE CONSULTAS";
	
	private Window win;
	
	private String selectedReporte = REP_1;
	
	private Date filtroDesde;
	private Date filtroHasta;
	private Servicio filtroServicio;
	private String filtroMedico = "";
	private Cliente filtroPaciente;
	private String filterCedula = "";

	@Init(superclass = true)
	public void init() {		
	}

	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	public void verReporte() {
		this.verReporte_();
	}
	
	/**
	 * @return los reportes..
	 */
	public List<String> getReportes() {
		List<String> out = new ArrayList<String>();
		out.add(REP_1);
		out.add(REP_2);
		out.add(REP_3);
		out.add(REP_4);
		out.add(REP_5);
		return out;
	}
	
	/**
	 * vista del reporte
	 */
	private void verReporte_() {
		switch (this.selectedReporte) {
		case REP_1:	
			this.ticketsEmitidos();
			break;
		case REP_2:		
			this.ticketsCancelados();
			break;
		case REP_3:			
			this.productividadMedicos();
			break;
		case REP_4:			
			this.ticketsEmitidosAgrupado();
			break;
		case REP_5:			
			this.historialConsultas();
			break;
		}
	}

	/**
	 * reporte cantidad tickets emitidos..
	 */
	private void ticketsEmitidos() {
		try {
			Date desde = this.filtroDesde;
			Date hasta = this.filtroHasta;
			Servicio serv = this.filtroServicio;

			if (desde == null)
				desde = new Date();

			if (hasta == null)
				hasta = new Date();
			
			if(serv == null)
				serv = new Servicio();

			RegisterDomain rr = RegisterDomain.getInstance();
			List<Object[]> data = new ArrayList<Object[]>();
			List<Turno> turnos = rr.getTurnos(desde, hasta, serv.getId());

			for (Turno turno : turnos) {
				data.add(new Object[] { m.dateToString(turno.getCreacion(), "dd-MM-yyyy (HH:mm:ss)"), turno.getNumero(),
						turno.getServicio().getDescripcion(), turno.getServicio().getImageSrc() });
			}

			ReporteTicketsEmitidos rep = new ReporteTicketsEmitidos(desde, hasta, turnos.size(), serv.getDescripcion());
			rep.setDatosReporte(data);
			rep.setApaisada();
				
			ViewPdf vp = new ViewPdf();
			vp.setBotonImprimir(false);
			vp.setBotonCancelar(false);
			vp.showReporte(rep, this);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Servicio getFiltroServicio() {
		return filtroServicio;
	}

	public void setFiltroServicio(Servicio filtroServicio) {
		this.filtroServicio = filtroServicio;
	}

	/**
	 * reporte cantidad tickets cancelados..
	 */
	private void ticketsCancelados() {
		try {
			Date desde = this.filtroDesde;
			Date hasta = this.filtroHasta;
			Servicio serv = this.filtroServicio;

			if (desde == null)
				desde = new Date();

			if (hasta == null)
				hasta = new Date();
			
			if(serv == null)
				serv = new Servicio();

			RegisterDomain rr = RegisterDomain.getInstance();
			List<Object[]> data = new ArrayList<Object[]>();
			List<Turno> turnos = rr.getTurnosCancelados(desde, hasta);

			for (Turno turno : turnos) {
				data.add(new Object[] { m.dateToString(turno.getCreacion(), "dd-MM-yyyy (HH:mm:ss)"), turno.getNumero(),
						turno.getServicio().getDescripcion(), turno.getServicio().getImageSrc() });
			}

			ReporteTicketsEmitidos rep = new ReporteTicketsEmitidos(desde, hasta, turnos.size(), serv.getDescripcion());
			rep.setDatosReporte(data);
			rep.setApaisada();
				
			ViewPdf vp = new ViewPdf();
			vp.setBotonImprimir(false);
			vp.setBotonCancelar(false);
			vp.showReporte(rep, this);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reporte cantidad tickets emitidos agrupados por especialidad..
	 */
	private void ticketsEmitidosAgrupado() {
		try {
			Date desde = this.filtroDesde;
			Date hasta = this.filtroHasta;

			if (desde == null)
				desde = new Date();

			if (hasta == null)
				hasta = new Date();
			
			RegisterDomain rr = RegisterDomain.getInstance();
			List<Object[]> data = new ArrayList<Object[]>();
			List<Turno> turnos = rr.getTurnos(desde, hasta);
			Map<String, Integer> cantidades = new HashMap<String, Integer>();
			Map<String, String> filas = new HashMap<String, String>();

			for (Turno turno : turnos) {				
				Integer count = cantidades.get(turno.getServicio().getDescripcion().toUpperCase());
				if (count == null) {
					cantidades.put(turno.getServicio().getDescripcion().toUpperCase(), 1);
				} else {
					cantidades.put(turno.getServicio().getDescripcion().toUpperCase(), count + 1);
				}
				filas.put(turno.getServicio().getDescripcion().toUpperCase(), turno.getServicio().getImageSrc());
			}
			
			for (String turno : cantidades.keySet()) {
				data.add(new Object[] { turno, cantidades.get(turno), filas.get(turno) });
			}
			
			Collections.sort(data, new Comparator<Object[]>() {
				@Override
				public int compare(Object[] o1, Object[] o2) {
					String val1 = (String) o1[0];
					String val2 = (String) o2[0];
					int compare = val1.compareTo(val2);				
					return compare;
				}
			});
			
			String source = JasperViewModel.SOURCE_TICKETS_EMITIDOS;
			Map<String, Object> params = new HashMap<String, Object>();
			JRDataSource dataSource = new TicketsEmitidosDataSource(data, cantidades);
			params.put("Titulo", "Tickets emitidos por Especialidad");
			params.put("Usuario", getUs().getNombre());
			params.put("Desde", Utiles.getFechaString(desde, "dd-MM-yyyy"));
			params.put("Hasta", Utiles.getFechaString(hasta, "dd-MM-yyyy"));
			this.imprimirJasper(source, params, dataSource, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reporte auditoria operadores..
	 */
	private void productividadMedicos() {
		try {
			Date desde = this.filtroDesde;
			Date hasta = this.filtroHasta;
			String medico = this.filtroMedico;

			if (desde == null)
				desde = new Date();

			if (hasta == null)
				hasta = new Date();

			RegisterDomain rr = RegisterDomain.getInstance();
			List<Object[]> data = new ArrayList<Object[]>();
			List<Turno> turnos = rr.getTurnosRemitidos(desde, hasta, medico);

			for (Turno turno : turnos) {
				data.add(new Object[] { m.dateToString(turno.getCreacion(), "dd-MM-yyyy (HH:mm:ss)"), turno.getNumero(),
						turno.getServicio().getDescripcion(), turno.getCliente().getDescripcion().toUpperCase() });
			}

			ReporteProductividadMedicos rep = new ReporteProductividadMedicos(desde, hasta, turnos.size(), medico);
			rep.setDatosReporte(data);
			rep.setApaisada();
				
			ViewPdf vp = new ViewPdf();
			vp.setBotonImprimir(false);
			vp.setBotonCancelar(false);
			vp.showReporte(rep, this);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * reporte historial consultas..
	 */
	private void historialConsultas() {
		try {
			Date desde = this.filtroDesde;
			Date hasta = this.filtroHasta;
			Cliente paciente = this.filtroPaciente;

			if (desde == null)
				desde = new Date();

			if (hasta == null)
				hasta = new Date();

			RegisterDomain rr = RegisterDomain.getInstance();
			List<Object[]> data = new ArrayList<Object[]>();
			List<Consulta> consultas = rr.getConsultas(paciente.getId());

			for (Consulta consulta : consultas) {
				data.add(new Object[] { m.dateToString(consulta.getFecha(), "dd-MM-yy"), consulta.getMotivo(),
						consulta.getCodigoCie(), consulta.getDoctorTratante(), consulta.getEspecialidad() });
			}

			ReporteHistorialConsulta rep = new ReporteHistorialConsulta(desde, hasta, consultas.size(), paciente.getDescripcion().toUpperCase());
			rep.setDatosReporte(data);
			rep.setApaisada();
				
			ViewPdf vp = new ViewPdf();
			vp.setBotonImprimir(false);
			vp.setBotonCancelar(false);
			vp.showReporte(rep, this);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Despliega el reporte en un pdf para su impresion..
	 */
	private void imprimirJasper(String source, Map<String, Object> parametros,
			JRDataSource dataSource, Object[] format) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("source", source);
		params.put("parametros", parametros);
		params.put("dataSource", dataSource);
		params.put("format", format);

		this.win = (Window) Executions.createComponents(JasperViewModel.ZUL_REPORTES, this.mainComponent, params);
		this.win.doModal();
	}
	
	/**
	 * GET´S / SET´S
	 */
	public List<Servicio> getServicios() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getAllServicios();
	}
	
	/**
	 * @return los medicos..
	 */
	public List<String> getMedicos() throws Exception {
		List<String> out = new ArrayList<String>();
		RegisterDomain rr = RegisterDomain.getInstance();
		for (Operador medico : rr.getMedicos()) {
			out.add(medico.getUsuario());
		}		 
		return out;
	}
	
	@DependsOn("filterCedula")
	public List<Cliente> getClientes() throws Exception {
		if (this.filterCedula.trim().isEmpty()) {
			return new ArrayList<Cliente>();
		}
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getClientes(this.filterCedula);
	}
	
	public String getSelectedReporte() {
		return selectedReporte;
	}

	public void setSelectedReporte(String selectedReporte) {
		this.selectedReporte = selectedReporte;
	}

	public Date getFiltroDesde() {
		return filtroDesde;
	}

	public void setFiltroDesde(Date filtroDesde) {
		this.filtroDesde = filtroDesde;
	}

	public Date getFiltroHasta() {
		return filtroHasta;
	}

	public void setFiltroHasta(Date filtroHasta) {
		this.filtroHasta = filtroHasta;
	}

	public String getFiltroMedico() {
		return filtroMedico;
	}

	public void setFiltroMedico(String filtroMedico) {
		this.filtroMedico = filtroMedico;
	}

	public Cliente getFiltroPaciente() {
		return filtroPaciente;
	}

	public void setFiltroPaciente(Cliente filtroPaciente) {
		this.filtroPaciente = filtroPaciente;
	}

	public String getFilterCedula() {
		return filterCedula;
	}

	public void setFilterCedula(String filterCedula) {
		this.filterCedula = filterCedula;
	}
}

/**
 * Reporte de tickets emitidos..
 */
class ReporteTicketsEmitidos extends ReporteTurnos {

	static final NumberFormat FORMATTER = new DecimalFormat("###,###,##0");
	private Date desde;
	private Date hasta;
	private int total = 0;
	private String servicio;

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	static DatosColumnas col0 = new DatosColumnas("Fecha", TIPO_STRING, 30);
	static DatosColumnas col1 = new DatosColumnas("Número", TIPO_STRING, 30);
	static DatosColumnas col2 = new DatosColumnas("Especialidad", TIPO_STRING);
	static DatosColumnas col3 = new DatosColumnas("Fila", TIPO_STRING, 30);

	public ReporteTicketsEmitidos(Date desde, Date hasta, int total, String servicio) {
		this.desde = desde;
		this.hasta = hasta;
		this.total = total;
		this.servicio = servicio;
	}

	static {
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
	}

	@Override
	public void informacionReporte() {
		this.setTitulo("Listado de Tickets emitidos");
		this.setDirectorio("");
		this.setNombreArchivo("Tickets-");
		this.setTitulosColumnas(cols);
		this.setBody(this.getCuerpo());
	}

	/**
	 * cabecera del reporte..
	 */
	@SuppressWarnings("rawtypes")
	private ComponentBuilder getCuerpo() {

		VerticalListBuilder out = cmp.verticalList();
		out.add(cmp.horizontalFlowList().add(this.texto("")));
		out.add(cmp.horizontalFlowList()
				.add(this.textoParValor("Desde",
						m.dateToString(this.desde, Misc.DD_MM_YYYY)))
				.add(this.textoParValor("Hasta",
						m.dateToString(this.hasta, Misc.DD_MM_YYYY)))
				.add(this.textoParValor("Especialidad", this.servicio))
				.add(this.textoParValor("Cantidad Total", this.total)));
		out.add(cmp.horizontalFlowList().add(this.texto("")));

		return out;
	}
}

/**
 * Reporte de productividad de medicos..
 */
class ReporteProductividadMedicos extends ReporteTurnos {

	static final NumberFormat FORMATTER = new DecimalFormat("###,###,##0");
	private Date desde;
	private Date hasta;
	private int total = 0;
	private String medico;

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	static DatosColumnas col0 = new DatosColumnas("Fecha", TIPO_STRING, 30);
	static DatosColumnas col1 = new DatosColumnas("Número", TIPO_STRING, 30);
	static DatosColumnas col2 = new DatosColumnas("Especialidad", TIPO_STRING, 30);
	static DatosColumnas col3 = new DatosColumnas("Paciente", TIPO_STRING);

	public ReporteProductividadMedicos(Date desde, Date hasta, int total, String medico) {
		this.desde = desde;
		this.hasta = hasta;
		this.total = total;
		this.medico = medico;
	}

	static {
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
	}

	@Override
	public void informacionReporte() {
		this.setTitulo("Productividad de medicos");
		this.setDirectorio("");
		this.setNombreArchivo("Tickets-");
		this.setTitulosColumnas(cols);
		this.setBody(this.getCuerpo());
	}

	/**
	 * cabecera del reporte..
	 */
	@SuppressWarnings("rawtypes")
	private ComponentBuilder getCuerpo() {

		VerticalListBuilder out = cmp.verticalList();
		out.add(cmp.horizontalFlowList().add(this.texto("")));
		out.add(cmp.horizontalFlowList()
				.add(this.textoParValor("Desde",
						m.dateToString(this.desde, Misc.DD_MM_YYYY)))
				.add(this.textoParValor("Hasta",
						m.dateToString(this.hasta, Misc.DD_MM_YYYY)))
				.add(this.textoParValor("Medico", this.medico.toUpperCase()))
				.add(this.textoParValor("Cantidad Total", this.total)));
		out.add(cmp.horizontalFlowList().add(this.texto("")));

		return out;
	}
}

/**
 * Reporte de historial de consultas..
 */
class ReporteHistorialConsulta extends ReporteTurnos {

	static final NumberFormat FORMATTER = new DecimalFormat("###,###,##0");
	private Date desde;
	private Date hasta;
	private int total = 0;
	private String paciente;

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	static DatosColumnas col0 = new DatosColumnas("Fecha", TIPO_STRING, 30);
	static DatosColumnas col1 = new DatosColumnas("Motivo", TIPO_STRING);
	static DatosColumnas col2 = new DatosColumnas("Codigo CIE", TIPO_STRING);
	static DatosColumnas col3 = new DatosColumnas("Medico", TIPO_STRING);
	static DatosColumnas col4 = new DatosColumnas("Especialidad", TIPO_STRING);

	public ReporteHistorialConsulta(Date desde, Date hasta, int total, String paciente) {
		this.desde = desde;
		this.hasta = hasta;
		this.total = total;
		this.paciente = paciente;
	}

	static {
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
	}

	@Override
	public void informacionReporte() {
		this.setTitulo("Historial de Consultas");
		this.setDirectorio("");
		this.setNombreArchivo("Tickets-");
		this.setTitulosColumnas(cols);
		this.setBody(this.getCuerpo());
	}

	/**
	 * cabecera del reporte..
	 */
	@SuppressWarnings("rawtypes")
	private ComponentBuilder getCuerpo() {

		VerticalListBuilder out = cmp.verticalList();
		out.add(cmp.horizontalFlowList().add(this.texto("")));
		out.add(cmp.horizontalFlowList()
				.add(this.textoParValor("Desde",
						m.dateToString(this.desde, Misc.DD_MM_YYYY)))
				.add(this.textoParValor("Hasta",
						m.dateToString(this.hasta, Misc.DD_MM_YYYY)))
				.add(this.textoParValor("Paciente", this.paciente.toUpperCase()))
				.add(this.textoParValor("Cantidad Total", this.total)));
		out.add(cmp.horizontalFlowList().add(this.texto("")));

		return out;
	}
}

/**
 * DataSource de Tickets emitidos agrupados..
 */
class TicketsEmitidosDataSource implements JRDataSource {

	static final NumberFormat FORMATTER = new DecimalFormat("###,###,##0");

	List<Object[]> values;
	Map<String, Integer> cantidades = new HashMap<String, Integer>();

	Misc misc = new Misc();

	public TicketsEmitidosDataSource(List<Object[]> values, Map<String, Integer> cantidades) {
		this.values = values;
		this.cantidades = cantidades;
	}

	private int index = -1;

	/**
	 * Object[] det = [0]:especialidad
	 */
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		Object[] det = this.values.get(index);

		if ("TituloDetalle".equals(fieldName)) {
			value = det[0];
		} else if ("Cantidad".equals(fieldName)) {
			value = cantidades.get(det[0]) + "";
		} else if ("Numero".equals(fieldName)) {
			value = det[2];
		} else if ("Total".equals(fieldName)) {
			value = cantidades.get(det[0]) + "";
		}
		return value;
	}

	@Override
	public boolean next() throws JRException {
		if (index < this.values.size() - 1) {
			index++;
			return true;
		}
		return false;
	}
}
