package com.tickets.gestion.panel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.coreweb.control.SimpleViewModel;
import com.coreweb.util.AutoNumeroControl;
import com.coreweb.util.Misc;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.Configuracion;
import com.tickets.UsuarioPropiedadApp;
import com.tickets.UtilDTO;
import com.tickets.control.cola.ControlCola;
import com.tickets.domain.Cliente;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Tope;

public class TurnoViewModel extends SimpleViewModel {
	
	MyPair sucursal = null;
	private boolean mostrandoVideo = false;
	private Window win;
	
	@Init(superclass = true)
	public void init(@ExecutionParam("tipo") String tipo){		
		
		try {			
			UsuarioPropiedadApp uss = new UsuarioPropiedadApp(this.getUs());
			this.sucursal = uss.getSucursal();
			
			this.cliente = this.getDtoUtil().getClienteNuevoPorDefecto();
			this.distribuirServicios();
			
			if (tipo.compareTo(VISOR_TURNO) == 0) {
				this.avisarNuevoTurno();
				this.refreshTurnos();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose(){		
	}
	
	private TurnoDTO dto = new TurnoDTO();
	private MyArray cliente = new MyArray();
	private MyArray selectedServicio = new MyArray();
	private List<MyArray> servicios_c1 = new ArrayList<MyArray>();
	private List<MyArray> servicios_c2 = new ArrayList<MyArray>();
	private ControlCola ctrCola = new ControlCola(new TurnoAssembler());
	@SuppressWarnings("rawtypes")
	private Converter convert = new MyConverter();	
	
	/************************** CONSTANTES *******************************/
	
	private static String IDENTIFICACION = "INGRESE SU CÉDULA Ó RUC.";
	private static String SALUDO = "BIENVENIDO..";
	private static String ELEGIR_SERVICIO = "POR FAVOR, ELIJA UN SERVICIO";
	
	private static String SRC_LISTA_TURNOS = "/tickets/gestion/panel/lista_turnos.zul";
	private static String SRC_AVISO_TURNOS = "/tickets/gestion/panel/aviso_nuevo_turno.zul";
	
	private static String VISOR_TURNO = "visorTurno";
	
	/*********************************************************************/	
	
	
	/********************* HABILITAR PANEL DE SERVICIOS ******************/
	
	@Command @NotifyChange("*")
	public void panelServicios(@BindingParam("cmp1") Component cmp1,
			@BindingParam("cmp2") Component cmp2) throws Exception{
		cmp1.setVisible(false);
		cmp2.setVisible(true);	
		identificarse(BUSCAR_EN_BD_YHAGUY);
	}
	
	/*********************************************************************/	
	
	
	/******************** SERVICIOS EN DOS COLUMNAS *********************/	
	
	//Distribuye la lista de Servicios en 2 listas
	private void distribuirServicios() throws Exception {
		List<MyArray> serv = new ArrayList<MyArray>();
		
		Map<String, Tope> topes = new HashMap<String, Tope>();
		RegisterDomain rr = RegisterDomain.getInstance();
		List<Tope> topes_ = rr.getTopes(this.getNumberDay());
		for (Tope tope : topes_) {
			if (!tope.isDisabled()) {
				topes.put(tope.getServicio(), tope);
			}
		}
		
		for (MyArray servicio : this.getDtoUtil().getServicios()) {
			String desc = (String) servicio.getPos1();
			if (topes.get(desc) != null) {
				serv.add(servicio);
			}
		}		
		
		switch (serv.size()) {
		case 0:
			// nada que hacer
			break;

		case 1:
			this.servicios_c1 = serv;
			break;
		
		default:
			
			/*for (int i = 0; i < serv.size(); i++) {
				
				if (m.esPar(i) == true) {
					this.servicios_c1.add(serv.get(i));					
				} else {
					this.servicios_c2.add(serv.get(i));					
				}			}*/
			this.servicios_c1.addAll(serv.subList(0, serv.size() / 2));
			this.servicios_c2.addAll(serv.subList(serv.size() / 2, serv.size()));
			break;
		}
	}
	
	/*********************************************************************/		
	
	
	/*************************** IDENTIFICACION **************************/	
	
	private static final int BUSCAR_EN_BD_YHAGUY = 1;
	private static final int BUSCAR_EN_BD_SET = 2;
	private static final int NO_ENCONTRADO = 3;
	
	private String clave = "";
	
	/**
	 * Busca en la BD el cliente segun lo que se ingresa
	 * en el panel de identificacion..
	 * Mediante el converter verifica si se ingresó un valor
	 * con [punto, coma, espacio] y lo extrae para hacer la busqueda.. 
	 */
	public void identificarse(int busqueda) throws Exception{
		MyArray cliente = null;
		
		switch (busqueda) {
		
		case BUSCAR_EN_BD_YHAGUY:
			
			cliente = this.getClienteRegistrado(clave);
			if (cliente == null) {
				identificarse(BUSCAR_EN_BD_SET);
			}
			break;

		case BUSCAR_EN_BD_SET:
			
			cliente = this.getClienteSET(clave);
			if (cliente == null) {
				identificarse(NO_ENCONTRADO);
			}
			break;
			
		case NO_ENCONTRADO:
			
			cliente = this.getDtoUtil().getClienteNuevoPorDefecto();
			
			break;
		}	
		
		if (cliente != null) {
			this.cliente = cliente;
		}
	}
	
	/**
	 * Busca en la BD del SET el cliente con el ruc correspondiente..
	 * Si el cliente ingreso su cedula igual lo busca sin el digito verificador
	 * usando el operador like '% %'..
	 * Retorna null si encuentra a mas de uno ó a ninguno..
	 */
	private MyArray getClienteSET(String cedulaOrRuc) throws Exception{
		MyArray out = null;		
		RegisterDomain rr = RegisterDomain.getInstance();
		String razonSocial = rr.getRazonSocialSET_RUC_CI(cedulaOrRuc);
		
		if (razonSocial != null) {
			out = new MyArray();
			out.setPos1(razonSocial);
			out.setPos4(true); //para indicar que es un cliente nuevo..
		}
		
		return out;
	}
	
	/**
	 * Busca un cliente registrado en la BD por el campo ruc o por cedula..
	 * Retorna null si no encuentra por ninguno de los dos campos..
	 */
	private MyArray getClienteRegistrado(String cedulaOrRuc) throws Exception{
		MyArray out = null;
		RegisterDomain rr = RegisterDomain.getInstance();
		
		Cliente cl = rr.getClienteByCedula(cedulaOrRuc);
		
		if (cl == null) {
			cl = rr.getClienteByRuc(cedulaOrRuc);
			
		} else if (cl != null) {
			out = new MyArray();
			out.setId(cl.getId());
			out.setPos1(cl.getDescripcion());
			out.setPos2(cl.getCedula());
			out.setPos3(cl.getRuc());
			out.setPos4(cl.isNuevo());
		}
		return out;
	}
	
	@Command @NotifyChange("clave")
	public void identificacion(@BindingParam("val") String val) {
		String value = this.clave + val;
		this.setClave(value);
	}
	
	@Command @NotifyChange("clave")
	public void cancel() {
		this.setClave("");
	}
	
	/*********************************************************************/	
	
	
	/************************ SELECCIONAR SERVICIO ***********************/
	
	private static String TEXT_BUTTON = "IMPRIMIR SU TICKET";
	private static String CAMPO_CLIENTE = "GRACIAS POR SU VISITA";
	private static String CAMPO_SERVICIO = "USTED ELIGIÓ EL SIGUIENTE SERVICIO";
	private static String CAMPO_TURNO = "SU TURNO ES";
	
	private long disponibleTM = 0;
	private long disponibleTT = 0;	
	private long usadosTM = 0;
	private long usadosTT = 0;
	
	@Command @NotifyChange("*")
	public void seleccionarServicio(@BindingParam("servicio") MyArray servicio, @BindingParam("comp1") Component comp1,
			@BindingParam("comp2") Component comp2) throws Exception {		
		this.selectedServicio = servicio;		
		
		int dia = this.getNumberDay();
		String fecha = this.m.dateToString(new Date(), Misc.YYYY_MM_DD);
		
		String keyTM = dia + "-" + servicio.getPos1() + "-" + "TM";
		String keyTT = dia + "-" + servicio.getPos1() + "-" + "TT";
		String keyTM_ = fecha + "-" + servicio.getPos1() + "-" + "TM";
		String keyTT_ = fecha + "-" + servicio.getPos1() + "-" + "TT";
		
		this.disponibleTM = AutoNumeroControl.getAutoNumero(keyTM, true);
		this.disponibleTT = AutoNumeroControl.getAutoNumero(keyTT, true);
		this.usadosTM = AutoNumeroControl.getAutoNumero(keyTM_, true) - 1;
		this.usadosTT = AutoNumeroControl.getAutoNumero(keyTT_, true) - 1;
		
		this.clave = "";
		comp1.setVisible(true);
		comp2.setVisible(false);
	}
	
	@Command @NotifyChange("*")
	public void seleccionarServicio_(@BindingParam("comp1") Component comp1, 
			@BindingParam("comp2") Component comp2, @BindingParam("horario") String horario) throws Exception {
		comp1.setVisible(false);
		comp2.setVisible(true);
		
		String horario_ = horario.equals("TM") ? "T.M." : "T.T.";
		
		this.dto = this.ctrCola.nuevoTurno(this.sucursal, this.cliente, this.selectedServicio, horario);
		Map<String, String> args = new HashMap<String, String>();
		args.put("text_button", TEXT_BUTTON);
		args.put("campo_cliente", CAMPO_CLIENTE);
		args.put("campo_servicio", CAMPO_SERVICIO);
		args.put("campo_turno", CAMPO_TURNO);
		args.put("value_cliente", this.selectedServicio.getPos1().toString().toUpperCase() + " - " + horario_);
		args.put("value_servicio", this.selectedServicio.getPos4().toString().toUpperCase());
		args.put("value_turno", this.dto.getNumero());
		args.put("value_fecha", this.m.dateToString(new Date(), Misc.YYYY_MM_DD_HORA_MIN_SEG));
		
		String src = Configuracion.MENSAJE_SERVICIO_ZUL;
		Window w = (Window) Executions.createComponents(src, 
				this.mainComponent, args);
		w.doModal();
	}
	
	/*********************************************************************/
	
	
	/************************** LISTA DE TURNOS **************************/
	
	static int[] rango1 = new int[]{0, 4};
	static int[] rango2 = new int[]{4, 8};
	static int[] rango3 = new int[]{8, 12};
	static int[] rango4 = new int[]{12, 16};
	static int[] rango5 = new int[]{16, 20};
	
	private int count = 0;	
	private Hashtable<Integer, List<String[]>> listas = new Hashtable<Integer, List<String[]>>();
	
	private List<String[]> listaCorriente = new ArrayList<String[]>();
	private List<String[]> list1 = new ArrayList<String[]>();
	private List<String[]> list2 = new ArrayList<String[]>();
	private List<String[]> list3 = new ArrayList<String[]>();
	private List<String[]> list4 = new ArrayList<String[]>();
	private List<String[]> list5 = new ArrayList<String[]>();	
	
	
	@Command @NotifyChange("listaCorriente")
	public void refrescarLista() throws Exception {
		count ++;
		
		if (listas.get(count) == null) {
			count = 0;
			this.refreshTurnos();
			this.refrescarLista();
			
		} else {
			this.setListaCorriente(listas.get(count));
		}
	}
	
	@Command
	public void mostrarVideo() {
		if (this.listaCorriente.size() == 0 && this.mostrandoVideo == false) {
			this.mostrandoVideo = true;
			String src = Configuracion.SRC_VIDEO;
			win = (Window) Executions.createComponents(src, null, null);
			win.doModal();
		} else if (this.listaCorriente.size() > 0) {
			this.mostrandoVideo = false;
			if (win != null) {
				win.detach();
			}
		}
	}
	
	/**
	 * Devuelve los turnos invocando a la Cola de Turnos..
	 * @throws Exception
	 */
	private void refreshTurnos() throws Exception {
		
		List<TurnoDTO> turnos = this.ctrCola.getUltimosTurnosActivos(this.sucursal);		
		List<String[]> aux = new ArrayList<String[]>();
		
		for (TurnoDTO turno : turnos) {	
			String[] turno_ = new String[]{(String) turno.getServicio().getPos1(), 
					(String) turno.getServicio().getPos4(), turno.getNumero()};
			aux.add(turno_);
		}		
		this.dividirListas(aux);
	}
	
	/**
	 * Divide la lista de turnos en n listas de tamaño 6
	 * para el visor de turnos..
	 * @param turnos
	 */
	private void dividirListas(List<String[]> turnos){
		this.inicializarListas();
		int size = turnos.size();
		
		if ((size / rango5[1]) >= 1) {
			list1 = turnos.subList(rango1[0], rango1[1]);
			list2 = turnos.subList(rango2[0], rango2[1]);
			list3 = turnos.subList(rango3[0], rango3[1]);
			list4 = turnos.subList(rango4[0], rango4[1]);
			list5 = turnos.subList(rango5[0], rango5[1]);
			
			listas.put(1, list1); listas.put(2, list2);
			listas.put(3, list3); listas.put(4, list4);
			listas.put(5, list5);
			
		} else if ((size / rango4[1]) >= 1){
			list1 = turnos.subList(rango1[0], rango1[1]);
			list2 = turnos.subList(rango2[0], rango2[1]);
			list3 = turnos.subList(rango3[0], rango3[1]);
			list4 = turnos.subList(rango4[0], rango4[1]);
			
			listas.put(1, list1); listas.put(2, list2);
			listas.put(3, list3); listas.put(4, list4);
			
			if ((size - rango4[1]) > 0) {
				list5 = turnos.subList(rango5[0], size);
				listas.put(5, list5);
			}
			
		} else if ((size /rango3[1]) >= 1) {
			list1 = turnos.subList(rango1[0], rango1[1]);
			list2 = turnos.subList(rango2[0], rango2[1]);
			list3 = turnos.subList(rango3[0], rango3[1]);
			
			listas.put(1, list1); listas.put(2, list2);
			listas.put(3, list3);
			
			if ((size - rango3[1]) > 0) {
				list4 = turnos.subList(rango4[0], size);
				listas.put(4, list4);
			}
			
		} else if ((size /rango2[1]) >= 1) {
			list1 = turnos.subList(rango1[0], rango1[1]);
			list2 = turnos.subList(rango2[0], rango2[1]);
			
			listas.put(1, list1); listas.put(2, list2);
			
			if ((size - rango2[1]) > 0) {
				list3 = turnos.subList(rango3[0], size);
				listas.put(3, list3);
			}
			
		} else if ((size /rango1[1]) >= 1) {
			list1 = turnos.subList(rango1[0], rango1[1]);
			listas.put(1, list1);
			
			if ((size - rango1[1]) > 0) {
				list2 = turnos.subList(rango2[0], size);
				listas.put(2, list2);
			}
		
		} else {
			list1 = turnos.subList(rango1[0], size);
			listas.put(1, list1);
		}
		this.listaCorriente = list1;
	}
	
	/**
	 * inicializa las listas..
	 */
	private void inicializarListas(){
		
		this.listas = new Hashtable<>();
		this.list1 = new ArrayList<>();
		this.list2 = new ArrayList<>();
		this.list3 = new ArrayList<>();
		this.list4 = new ArrayList<>();
		this.list5 = new ArrayList<>();
		
	}
	
	/*********************************************************************/
	
	
	/************************** AVISO NUEVO TURNO ************************/
	
	private boolean llamando = false;	
	private List<Event> llamadas = new ArrayList<Event>();
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private EventListener nuevoTurno = new EventListener() {		
		
		@Override
		public void onEvent(Event event) throws Exception {			
			llamadas.add(event);	
			
			if (llamando == false) {
				showTurno();
			}
		}	
		
		private void showTurno() throws Exception{
			Event event = llamadas.get(0);
			
			llamando = true;
			
			Timer timer = new Timer();				
			timer.setDelay(6500);
			timer.setRepeats(false);			
			
			Map<String, String> args = (Map<String, String>) event.getData();

			String src = SRC_AVISO_TURNOS;
			final Window w = (Window) Executions.createComponents(src, 
				 mainComponent, args);
									
			timer.addEventListener(Events.ON_TIMER, new EventListener() {
				@Override
				public void onEvent(Event evt) throws Exception {
					w.detach();
					llamadas.remove(0);
					
					if (llamadas.size() > 0) {
						showTurno();
						
					} else {
						llamando = false;
					}
				}
			});
			timer.setParent(w);		
			w.doModal();			
		}
	};
	
	/**
	 * Cuando se llama a un Turno el Visor de Turnos lo despliega..
	 */
	@SuppressWarnings("unchecked")
	public void avisarNuevoTurno() {
		EventQueues.lookup(Configuracion.EVENTO_LLAMAR_TURNO, 
				EventQueues.APPLICATION, true).subscribe(nuevoTurno);
	}
	
	@SuppressWarnings("unchecked")
	@Command
	public void cerrar(@BindingParam("win") Window win) {
		EventQueues.lookup(Configuracion.EVENTO_LLAMAR_TURNO, 
				EventQueues.APPLICATION, true).unsubscribe(nuevoTurno);
		win.detach();
	}
	
	/*********************************************************************/
	
	
	/*************************** GETTER/SETTER ***************************/
	
	/**
	 * @return el nro de dia corriente..
	 */
	private int getNumberDay() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek - 1;
	}
	
	public boolean isDisabledTM() {
		return this.usadosTM == this.disponibleTM;
	}
	
	public boolean isDisabledTT() {
		return this.usadosTT == this.disponibleTT;
	}
	
	public String getLabelUsadosTM() {
		return "USADOS: " + this.usadosTM;
	}
	
	public String getLabelLimiteTM() {
		return "LÍMITE: " + this.disponibleTM;
	}
	
	public String getLabelUsadosTT() {
		return "USADOS: " + this.usadosTT;
	}
	
	public String getLabelLimiteTT() {
		return "LÍMITE: " + this.disponibleTT;
	}
	
	public TurnoDTO getDto() {
		return dto;
	}

	public void setDto(TurnoDTO dto) {
		this.dto = dto;
	}
	
	public UtilDTO getDtoUtil() {
		UtilDTO u = (UtilDTO)super.getDtoUtil();
		return u;
	}

	public List<MyArray> getServicios_c1() {
		return servicios_c1;
	}

	public void setServicios_c1(List<MyArray> servicios) {
		this.servicios_c1 = servicios;
	}

	public List<MyArray> getServicios_c2() {
		return servicios_c2;
	}

	public void setServicios_c2(List<MyArray> servicios_c2) {
		this.servicios_c2 = servicios_c2;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String nroIdentificacion) {
		this.clave = nroIdentificacion;
	}
	
	public String getSaludo(){
		return SALUDO;
	}
	
	public String getIdentificacion(){
		return IDENTIFICACION;
	}
	
	public String getElegirServicio(){
		return ELEGIR_SERVICIO;
	}

	public MyArray getCliente() {
		return cliente;
	}

	public void setCliente(MyArray cliente) {
		this.cliente = cliente;
	}

	public MyArray getSelectedServicio() {
		return selectedServicio;
	}

	public void setSelectedServicio(MyArray selectedServicio) {
		this.selectedServicio = selectedServicio;
	}

	public String getSrcListaTurnos() {
		return SRC_LISTA_TURNOS;
	}
	
	public boolean isPar(int index){
		return this.m.esPar(index);
	}

	public List<String[]> getListaCorriente() {
		return listaCorriente;
	}

	public void setListaCorriente(List<String[]> listaCorriente) {
		this.listaCorriente = listaCorriente;
		this.mostrarVideo();
	}

	@SuppressWarnings("rawtypes")
	public Converter getConvert() {
		return convert;
	}

	public long getDisponibleTM() {
		return disponibleTM;
	}

	public void setDisponibleTM(long disponibleTM) {
		this.disponibleTM = disponibleTM;
	}

	public long getDisponibleTT() {
		return disponibleTT;
	}

	public void setDisponibleTT(long disponibleTT) {
		this.disponibleTT = disponibleTT;
	}

	public long getUsadosTM() {
		return usadosTM;
	}

	public void setUsadosTM(long usadosTM) {
		this.usadosTM = usadosTM;
	}

	public long getUsadosTT() {
		return usadosTT;
	}

	public void setUsadosTT(long usadosTT) {
		this.usadosTT = usadosTT;
	}	
}


@SuppressWarnings("rawtypes")
class MyConverter implements Converter{

	/**
	 * Convertidor implementado por el Textbox del panel de Ingreso..
	 * Verifica si existe [punto, coma, espacio] en el valor de cedula 
	 * y lo extrae..
	 */
	
	@Override
	public Object coerceToBean(Object val, Component arg1, BindContext arg2) {
		String str = (String) val;		
		return str.trim().replace(",", "").replace(".", "").replace(" ", "");
	}

	@Override
	public Object coerceToUi(Object arg0, Component arg1, BindContext arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
