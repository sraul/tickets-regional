package com.tickets.gestion.atencion;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;

import com.coreweb.extras.reporte.DatosColumnas;
import com.coreweb.util.Misc;
import com.coreweb.util.MyPair;
import com.tickets.Configuracion;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Tarea;
import com.tickets.gestion.administracion.OperadorDTO;
import com.tickets.util.reporte.ReporteTurnos;

public class OperadorReporte extends ReporteTurnos {

	// 7 minutos
	private static long tiempoNada = 1000 * 60 * 7;
	private static int INICIO_DIA = 8; // empieza de 0, entonces 8hs.

	private static String TITULO = "Historial del Operador";
	private static String URL_ARCHIVO = "historialoperador";
	private static String NOMBRE_ARCHIVO = "HistorialOperador";

	// Columnas del Reporte
	static DatosColumnas col1 = new DatosColumnas("Tipo tarea", TIPO_STRING, 50);
	static DatosColumnas col2 = new DatosColumnas("Inicio", TIPO_STRING, 60);
	static DatosColumnas col3 = new DatosColumnas("Fin", TIPO_STRING, 60);
	static DatosColumnas col4 = new DatosColumnas("Tipo FDS", TIPO_STRING, 50);
	static DatosColumnas col5 = new DatosColumnas("Turno", TIPO_STRING, 30);
	static DatosColumnas col6 = new DatosColumnas("Servicio", TIPO_STRING);
	static DatosColumnas col7 = new DatosColumnas("Tiempo", TIPO_STRING, 50);

	static List<DatosColumnas> cols = new ArrayList<DatosColumnas>();

	static {
		col5.setAlineacionColuman(COLUMNA_ALINEADA_CENTRADA);

		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
	};

	Misc m = new Misc();

	private String nombreOperador;
	private String estadoActualOperador;
	private String desde = "";
	private String hasta = "";
	private Date horaInicioDia = null;

	Misc misc = new Misc();

	public String getNombreOperador() {
		return nombreOperador;
	}

	public void setNombreOperador(String nombreOperador) {
		this.nombreOperador = nombreOperador;
	}

	public String getEstadoActualOperador() {
		return estadoActualOperador;
	}

	public void setEstadoActualOperador(String estadoActualOperador) {
		this.estadoActualOperador = estadoActualOperador;
	}

	@Override
	public void informacionReporte() {
		this.setTitulo(TITULO);
		this.setDirectorio(URL_ARCHIVO);
		this.setNombreArchivo(NOMBRE_ARCHIVO);
		this.setTitulosColumnas(cols);
		this.setBody(this.getCuerpo());
	}

	// private ComponentBuilder getCuerpo(String nombreOperador, String
	// estadoActualOperador) {
	@SuppressWarnings("rawtypes")
	private ComponentBuilder getCuerpo() {

		VerticalListBuilder out = null;
		out = cmp.verticalList();

		out.add(cmp.horizontalFlowList().add(this.texto("")));

		out.add(cmp
				.horizontalFlowList()
				.add(this.textoParValor("NOMBRE OPERADOR", this.nombreOperador))
				.add(this.textoParValor("ESTADO ACTUAL",
						this.estadoActualOperador))
				.add(this.textoParValor("PERIODO", this.desde + " al "
						+ this.hasta)));

		out.add(cmp.horizontalFlowList().add(
				this.textoParValor("HORA INICIO",
						m.dateToString(this.horaInicioDia, m.DD_MMMM_HH_MM))));

		out.add(cmp.horizontalFlowList().add(this.texto("")));

		return out;
	}

	public void setInformacion(OperadorDTO ope, Date inicio, Date fin)
			throws Exception {
		inicio = m.toFecha0000(inicio);
		fin = m.toFecha2400(fin);
		this.desde = m.dateToString(inicio, m.DD_MM_YYYY);
		this.hasta = m.dateToString(fin, m.DD_MM_YYYY);

		this.horaInicioDia = inicio;
		this.horaInicioDia.setHours(INICIO_DIA);

		RegisterDomain rr = RegisterDomain.getInstance();
		// List<Tarea> tareasViejas =
		// rr.getTareas(this.selectedOperador.getId());
		List<Tarea> tareas = rr.getTareasPorFechas(ope.getId(), inicio, fin);
		List<Object[]> data = new ArrayList<>();
		Object[] obj = new Object[4];

		Date dFin = null;
		Date dIni = null;

		if (tareas.size() > 0) {
			dFin = this.horaInicioDia;
		}

		for (Tarea t : tareas) {

			dIni = t.getInicio();
			long dNada = dIni.getTime() - dFin.getTime();
			if (dNada > tiempoNada) {
				// tiempo sin hacer nada
				obj = new Object[7];
				obj[6] = m.tiempoTareas(dFin, dIni);
				data.add(obj);
			}

			obj = new Object[7];
			obj[0] = t.getTipo().getDescripcion();
			obj[1] = m.dateToString(t.getInicio(), m.DD_MMMM_HH_MM);
			obj[2] = m.dateToString(t.getFin(), m.DD_MMMM_HH_MM);
			if (t.getTipo().getSigla().compareTo(Configuracion.SIGLA_TAREA_FDS) == 0) {
				obj[3] = t.getTipoFds().getDescripcion();
			} else {
				obj[4] = t.getTurno().getNumero();
				obj[5] = t.getTurno().getServicio().getDescripcion();
			}
			obj[6] = m.tiempoTareas(t.getInicio(), t.getFin());
			data.add(obj);

			dFin = t.getFin();

		}

		this.setNombreOperador(ope.getNombre());
		this.setEstadoActualOperador(ope.getEstado().getText());
		this.setDatosReporte(data);

	}

	public static void main(String[] args) throws Exception {

		Misc m = new Misc();
		OperadorDTO ope = new OperadorDTO();
		ope.setId(new Long(4));
		ope.setNombre("Juan");
		ope.setEstado(new MyPair(1, "Estado"));
		Date fechaInicio = m.getFechaHoy00();
		Date fechaFin = m.getFechaHoy2359();
		OperadorReporte r = new OperadorReporte();

		fechaInicio = new Date(System.currentTimeMillis()
				- (2 * 60 * 60 * 1000));
		r.setInformacion(ope, fechaInicio, fechaFin);

		r.ejecutar(true);
	}

	public static void xmain(String[] args) throws Exception {

		Misc m = new Misc();

		Date dFin = new Date();
		dFin.setHours(INICIO_DIA);

		System.out.println(m.dateToString(dFin, m.DD_MMMM_HH_MM));

	}

}
