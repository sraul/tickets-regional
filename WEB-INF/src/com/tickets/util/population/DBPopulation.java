package com.tickets.util.population;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coreweb.domain.Domain;
import com.coreweb.domain.Usuario;
import com.coreweb.util.Misc;
import com.tickets.Configuracion;
import com.tickets.domain.AccesoApp;
import com.tickets.domain.Operador;
import com.tickets.domain.Puesto;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Servicio;

public class DBPopulation {

	private static RegisterDomain rr = RegisterDomain.getInstance();
	private static final String FILA_ICON = "/core/images/fila.png";
	
	private static Misc misc = new Misc();

	private static void grabarDB(Domain d) throws Exception {
		rr.saveObject(d, Configuracion.USER_SYSTEMA);
	}

	public static void main(String[] args) throws Exception {
		
		boolean cargarDatosDePrueba = false;
		boolean cargarDBSET = false;
		boolean cargarDBClientes = false;

		rr.dropAllTables();
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		System.out.println("Cargar Datos de Prueba? (Clientes, Turnos y Tareas) [Y/N] :");
		String in = br.readLine();		
		if (in.compareTo("Y") == 0) {
			cargarDatosDePrueba = true;
		}
		
		System.out.println("Cargar DB-RUC-SET ? [Y/N]:");
		in = br.readLine();
		if (in.compareTo("Y") == 0) {
			cargarDBSET = true;
		}
		
		System.out.println("Cargar DB-CLIENTES ? [Y/N]:");
		in = br.readLine();
		if (in.compareTo("Y") == 0) {
			cargarDBClientes = true;
		}
		
		DBPopulationTipos tt = new DBPopulationTipos();
		tt.cargarTipos();
		
		UsuarioPerfilParser.loadMenuConfig();

		/**
		 * Accesos
		 */
		Usuario usr1 = (Usuario) rr.getObject(Usuario.class.getName(),
				new Long(1));
		
		Usuario usr2 = (Usuario) rr.getObject(Usuario.class.getName(),
				new Long(2));
		
		Usuario usr3 = (Usuario) rr.getObject(Usuario.class.getName(),
				new Long(3));
		
		Usuario usr4 = (Usuario) rr.getObject(Usuario.class.getName(),
				new Long(4));
		
		Usuario usr5 = (Usuario) rr.getObject(Usuario.class.getName(),
				new Long(5));
		
		Usuario usr6 = (Usuario) rr.getObject(Usuario.class.getName(),
				new Long(6));
		
		Usuario usr7 = (Usuario) rr.getObject(Usuario.class.getName(),
				new Long(7));
		
		List<Usuario> users = new ArrayList<Usuario>();
		users.add(usr1);
		users.add(usr2);
		users.add(usr3);
		users.add(usr4);
		users.add(usr5);
		users.add(usr6);
		users.add(usr7);
		
		for (Usuario usuario : users) {
			AccesoApp acc = new AccesoApp();
			acc.setDescripcion("Acceso usuario " + usuario.getNombre());
			acc.setUsuario(usuario);
			grabarDB(acc);
		}

		/**
		 * Puestos iniciales
		 */
		
		Puesto box1 = new Puesto();
		box1.setDescripcion("1");
		box1.setEstado(tt.estadoPuesto_Activo);
		
		Puesto box2 = new Puesto();
		box2.setDescripcion("2");
		box2.setEstado(tt.estadoPuesto_Activo);
		
		Puesto box3 = new Puesto();
		box3.setDescripcion("3");
		box3.setEstado(tt.estadoPuesto_Activo);

		Puesto box4 = new Puesto();
		box4.setDescripcion("4");
		box4.setEstado(tt.estadoPuesto_Activo);
		
		Puesto box5 = new Puesto();
		box5.setDescripcion("5");
		box5.setEstado(tt.estadoPuesto_Activo);		
		
		Puesto box6 = new Puesto();
		box6.setDescripcion("6");
		box6.setEstado(tt.estadoPuesto_Activo);

		Puesto box7 = new Puesto();
		box7.setDescripcion("7");
		box7.setEstado(tt.estadoPuesto_Activo);

		Puesto box8 = new Puesto();
		box8.setDescripcion("8");
		box8.setEstado(tt.estadoPuesto_Activo);
		
		Puesto box9 = new Puesto();
		box9.setDescripcion("9");
		box9.setEstado(tt.estadoPuesto_Activo);

		Puesto box10 = new Puesto();
		box10.setDescripcion("10");
		box10.setEstado(tt.estadoPuesto_Activo);	

		grabarDB(box1);
		grabarDB(box2);
		grabarDB(box3);
		grabarDB(box4);
		grabarDB(box5);
		grabarDB(box6);
		grabarDB(box7);
		grabarDB(box8);
		grabarDB(box9);
		grabarDB(box10);
		
		Set<Puesto> boxes = new HashSet<Puesto>();
		boxes.add(box1); boxes.add(box2);
		boxes.add(box3); boxes.add(box4);
		boxes.add(box5); boxes.add(box6);
		boxes.add(box7); boxes.add(box8);
		boxes.add(box9); boxes.add(box10);

		/**
		 * Servicios iniciales
		 */	
		
		Servicio fila1 = new Servicio();
		fila1.setSucursal(tt.sucursal1);
		fila1.setDescripcion("Fila 1");
		fila1.setLetra("A");
		fila1.setImageSrc(FILA_ICON);
		fila1.setEstado(tt.estadoServicio_Activo);
		Set<Puesto> puestosFila1 = new HashSet<Puesto>();
		puestosFila1.addAll(boxes);
		fila1.setPuestos(puestosFila1);
		
		Servicio fila2 = new Servicio();
		fila2.setSucursal(tt.sucursal1);
		fila2.setDescripcion("Fila 2");
		fila2.setLetra("B");
		fila2.setImageSrc(FILA_ICON);
		fila2.setEstado(tt.estadoServicio_Activo);
		Set<Puesto> puestosFila2 = new HashSet<Puesto>();
		puestosFila2.addAll(boxes);
		fila2.setPuestos(puestosFila2);		

		Servicio fila3 = new Servicio();
		fila3.setSucursal(tt.sucursal1);
		fila3.setDescripcion("Fila 3");
		fila3.setLetra("C");
		fila3.setImageSrc(FILA_ICON);
		fila3.setEstado(tt.estadoServicio_Activo);
		Set<Puesto> puestosFila3 = new HashSet<Puesto>();
		puestosFila3.addAll(boxes);
		fila3.setPuestos(puestosFila3);

		Servicio fila4 = new Servicio();
		fila4.setSucursal(tt.sucursal1);
		fila4.setDescripcion("Fila 4");
		fila4.setLetra("D");
		fila4.setImageSrc(FILA_ICON);
		fila4.setEstado(tt.estadoServicio_Activo);
		Set<Puesto> puestosFila4 = new HashSet<Puesto>();
		puestosFila4.addAll(boxes);
		fila4.setPuestos(puestosFila4);			
		
		Servicio fila5 = new Servicio();
		fila5.setSucursal(tt.sucursal1);
		fila5.setDescripcion("Fila 5");
		fila5.setLetra("E");
		fila5.setImageSrc(FILA_ICON);
		fila5.setEstado(tt.estadoServicio_Activo);
		Set<Puesto> puestosFila5 = new HashSet<Puesto>();
		puestosFila5.addAll(boxes);
		fila5.setPuestos(puestosFila5);
		
		Servicio fila6 = new Servicio();
		fila6.setSucursal(tt.sucursal1);
		fila6.setDescripcion("Fila 6");
		fila6.setLetra("F");
		fila6.setImageSrc(FILA_ICON);
		fila6.setEstado(tt.estadoServicio_Activo);
		Set<Puesto> puestosFila6 = new HashSet<Puesto>();
		puestosFila6.addAll(boxes);
		fila6.setPuestos(puestosFila6);	
		
		Servicio fila7 = new Servicio();
		fila7.setSucursal(tt.sucursal1);
		fila7.setDescripcion("Fila 7");
		fila7.setLetra("G");
		fila7.setImageSrc(FILA_ICON);
		fila7.setEstado(tt.estadoServicio_Activo);
		Set<Puesto> puestosFila7 = new HashSet<Puesto>();
		puestosFila7.addAll(boxes);
		fila7.setPuestos(puestosFila7);
		
		Servicio fila8 = new Servicio();
		fila8.setSucursal(tt.sucursal1);
		fila8.setDescripcion("Fila 8");
		fila8.setLetra("H");
		fila8.setImageSrc(FILA_ICON);
		fila8.setEstado(tt.estadoServicio_Activo);
		Set<Puesto> puestosFila8 = new HashSet<Puesto>();
		puestosFila8.addAll(boxes);
		fila8.setPuestos(puestosFila8);

		grabarDB(fila1);
		grabarDB(fila2);		
		grabarDB(fila3);
		grabarDB(fila4);			
		grabarDB(fila5);
		grabarDB(fila6);
		grabarDB(fila7);
		grabarDB(fila8);

		/**
		 * Operadores iniciales
		 */

		//Operadores..
		
		Operador operador1 = new Operador();
		operador1.setSucursal(tt.sucursal1);
		operador1.setUsuario("operador_1");
		operador1.setPass(misc.encriptar("123"));
		operador1.setNombre("Operador 1");
		operador1.setEstado(tt.estadoOperador_libre);
		Set<Servicio> serviciosOpe1 = new HashSet<Servicio>();
		serviciosOpe1.add(fila1);
		serviciosOpe1.add(fila2);
		operador1.setServicios(serviciosOpe1);
		Set<Puesto> puestosO1 = new HashSet<Puesto>();
		puestosO1.add(box1); 
		operador1.setPuestos(puestosO1);
		
		Operador operador2 = new Operador();
		operador2.setSucursal(tt.sucursal1);
		operador2.setUsuario("operador_2");
		operador2.setPass(misc.encriptar("123"));
		operador2.setNombre("Operador 2");
		operador2.setEstado(tt.estadoOperador_libre);
		Set<Servicio> serviciosOpe2 = new HashSet<Servicio>();
		serviciosOpe2.add(fila3);
		serviciosOpe2.add(fila4);
		operador2.setServicios(serviciosOpe2);
		Set<Puesto> puestosO2 = new HashSet<Puesto>();
		puestosO2.add(box2); 
		operador2.setPuestos(puestosO2);
		
		Operador operador3 = new Operador();
		operador3.setSucursal(tt.sucursal1);
		operador3.setUsuario("operador_3");
		operador3.setPass(misc.encriptar("123"));
		operador3.setNombre("Operador 3");
		operador3.setEstado(tt.estadoOperador_libre);
		Set<Servicio> serviciosOpe3 = new HashSet<Servicio>();
		serviciosOpe3.add(fila5);
		serviciosOpe3.add(fila6);
		operador3.setServicios(serviciosOpe3);
		Set<Puesto> puestosO3 = new HashSet<Puesto>();
		puestosO3.add(box3); 
		operador3.setPuestos(puestosO3);
		
		Operador operador4 = new Operador();
		operador4.setSucursal(tt.sucursal1);
		operador4.setUsuario("operador_4");
		operador4.setPass(misc.encriptar("123"));
		operador4.setNombre("Operador 4");
		operador4.setEstado(tt.estadoOperador_libre);
		Set<Servicio> serviciosOpe4 = new HashSet<Servicio>();
		serviciosOpe4.add(fila7);
		serviciosOpe4.add(fila8);
		operador4.setServicios(serviciosOpe4);
		Set<Puesto> puestosO4 = new HashSet<Puesto>();
		puestosO4.add(box4); 
		operador4.setPuestos(puestosO4);
		
		grabarDB(operador1);
		grabarDB(operador2);
		grabarDB(operador3);
		grabarDB(operador4);
		
		/**
		 * De aqui en adelante son datos de prueba..
		 */		
		if (cargarDatosDePrueba == true) {}	
		
		/**
		 * Carga de RUC del SET
		 */
		if (cargarDBSET == true) {
			DBPopulationSET set = new DBPopulationSET();
			set.cargaAllRuc();
		}
		
		/**
		 * Carga los Clientes
		 */
		if (cargarDBClientes == true) {
			DBPopulationClientes clientes = new DBPopulationClientes();
			clientes.poblarClientes();
		}
	}
}