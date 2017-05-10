package com.tickets.gestion.informacion;

import java.util.Date;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.coreweb.util.Misc;

import net.sf.jasperreports.engine.JRDataSource;

public class JasperViewModel {
	
	public static final String ZUL_REPORTES = "/tickets/gestion/informacion/jasper_view.zul";
	
	public static final String SOURCE_TICKETS_EMITIDOS = "/reportes/jasper/TicketsEmitidos.jasper";
	
	static final String CONTEXT = Sessions.getCurrent().getWebApp().getRealPath("/");
	static final String LOGO = CONTEXT + "/logo.png";
	
	public static final Object[] FORMAT_PDF = new Object[]{ "PDF", "pdf" };
	public static final Object[] FORMAT_XLS = new Object[]{ "Excel", "xls" };
	public static final Object[] FORMAT_CSV = new Object[]{ "CSV", "csv" };
	
	private String source;
	private JRDataSource dataSource;
	private Map<String, Object> params;
	
	private Object[] selectedReporte;
	private Object[] selectedFormato;
	
	private ReportConfig reportConfig = null;	

	@Init()
	public void init(@ExecutionArgParam("source") String source,
			@ExecutionArgParam("parametros") Map<String, Object> params,
			@ExecutionArgParam("dataSource") JRDataSource dataSource,
			@ExecutionArgParam("format") Object[] format) {
		Misc misc = new Misc();
		this.source = source;
		this.params = params;
		this.dataSource = dataSource;
		this.selectedFormato = format == null ? FORMAT_PDF : format;
		this.params.put("Logo", LOGO);
		this.params.put("Empresa", "Hospital General de Luque");
		this.params.put("Generado", misc.dateToString(new Date(), "dd-MM-yyyy hh:mm:ss"));
	}
	
	@AfterCompose()
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}	
	
	@Wire
	private Jasperreport report;
	
	@Wire
	private Window win;	
	
	@Command("showReport")
	public void showReport() {
		
		Clients.showBusy(this.report, "Procesando Información..");
		
		this.reportConfig = new ReportConfig();
		this.reportConfig.setSource(this.source);
		this.reportConfig.setParameters(this.params);
		this.reportConfig.setDataSource(this.dataSource);
		
		Events.echoEvent("onLater", this.report, null);
	}	
	
	/**
	 * Cierra la ventana de progreso..
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	public void clearProgress() {
		Timer timer = new Timer();
		timer.setDelay(1000);
		timer.setRepeats(false);

		timer.addEventListener(Events.ON_TIMER, new EventListener() {
			@Override
			public void onEvent(Event evt) throws Exception {
				Clients.clearBusy(report);
			}
		});
		timer.setParent(this.win);
	}
	
	
	/************************* GET/SET *************************/

	public ReportConfig getReportConfig() {
		return reportConfig;
	}

	public void setReportConfig(ReportConfig reportConfig) {
		this.reportConfig = reportConfig;
	}

	public Object[] getSelectedReporte() {
		return selectedReporte;
	}

	public void setSelectedReporte(Object[] selectedReporte) {
		this.selectedReporte = selectedReporte;
	}

	public Object[] getSelectedFormato() {
		return selectedFormato;
	}

	public void setSelectedFormato(Object[] selectedFormato) {
		this.selectedFormato = selectedFormato;
	}
}
