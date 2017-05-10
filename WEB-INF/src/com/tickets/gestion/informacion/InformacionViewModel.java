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
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.coreweb.componente.ViewPdf;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.extras.reporte.DatosColumnas;
import com.coreweb.util.Misc;
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
	static final String REP_3 = "AUDITORÍA DE OPERADORES";
	static final String REP_4 = "TICKETS EMITIDOS AGRUPADO POR ESPECIALIDAD";
	
	private Window win;
	
	private String selectedReporte = REP_1;
	
	private Date filtroDesde;
	private Date filtroHasta;
	private Servicio filtroServicio;

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
			this.auditoriaOperadores();
			break;
		case REP_4:			
			this.ticketsEmitidosAgrupado();
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
	private void auditoriaOperadores() {
		Clients.showNotification("Reporte pendiente..");
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
