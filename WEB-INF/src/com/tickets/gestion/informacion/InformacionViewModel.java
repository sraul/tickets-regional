package com.tickets.gestion.informacion;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.util.Clients;

import com.coreweb.componente.ViewPdf;
import com.coreweb.control.SimpleViewModel;
import com.coreweb.extras.reporte.DatosColumnas;
import com.coreweb.util.Misc;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Turno;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;

public class InformacionViewModel extends SimpleViewModel {

	static final String REP_1 = "CANTIDAD DE TICKETS EMITIDOS";
	static final String REP_2 = "CANTIDAD DE TICKETS CANCELADOS";
	static final String REP_3 = "AUDITORÍA DE OPERADORES";
	
	private String selectedReporte = REP_1;
	
	private Date filtroDesde;
	private Date filtroHasta;

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
		}
	}

	/**
	 * reporte cantidad tickets emitidos..
	 */
	private void ticketsEmitidos() {
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

			for (Turno turno : turnos) {
				data.add(new Object[] { m.dateToString(turno.getCreacion(), "dd-MM-yyyy (HH:mm:ss)"), turno.getNumero(),
						turno.getServicio().getDescripcion(), turno.getServicio().getImageSrc() });
			}

			ReporteTicketsEmitidos rep = new ReporteTicketsEmitidos(desde, hasta);
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
	 * reporte cantidad tickets cancelados..
	 */
	private void ticketsCancelados() {
		try {
			Date desde = this.filtroDesde;
			Date hasta = this.filtroHasta;

			if (desde == null)
				desde = new Date();

			if (hasta == null)
				hasta = new Date();

			RegisterDomain rr = RegisterDomain.getInstance();
			List<Object[]> data = new ArrayList<Object[]>();
			List<Turno> turnos = rr.getTurnosCancelados(desde, hasta);

			for (Turno turno : turnos) {
				data.add(new Object[] { m.dateToString(turno.getCreacion(), "dd-MM-yyyy (HH:mm:ss)"), turno.getNumero(),
						turno.getServicio().getDescripcion(), turno.getServicio().getImageSrc() });
			}

			ReporteTicketsEmitidos rep = new ReporteTicketsEmitidos(desde, hasta);
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
	 * reporte auditoria operadores..
	 */
	private void auditoriaOperadores() {
		Clients.showNotification("Reporte pendiente..");
	}
	
	/**
	 * GET´S / SET´S
	 */
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

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();
	static DatosColumnas col0 = new DatosColumnas("Fecha", TIPO_STRING, 30);
	static DatosColumnas col1 = new DatosColumnas("Número", TIPO_STRING, 30);
	static DatosColumnas col2 = new DatosColumnas("Especialidad", TIPO_STRING);
	static DatosColumnas col3 = new DatosColumnas("Fila", TIPO_STRING, 30);

	public ReporteTicketsEmitidos(Date desde, Date hasta) {
		this.desde = desde;
		this.hasta = hasta;
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
						m.dateToString(this.hasta, Misc.DD_MM_YYYY))));
		out.add(cmp.horizontalFlowList().add(this.texto("")));

		return out;
	}
}
