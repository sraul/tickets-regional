package com.tickets.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.coreweb.domain.Register;
import com.coreweb.domain.Tipo;
import com.coreweb.domain.Usuario;
import com.coreweb.util.Misc;
import com.coreweb.util.MyPair;
import com.tickets.Configuracion;

@SuppressWarnings("unchecked")
public class RegisterDomain extends Register {

	private static RegisterDomain instance = new RegisterDomain();

	static int pos = 10;
	static RegisterDomain[] list = null;
	static Random r = new Random();

	private RegisterDomain() {
	}

	public synchronized static RegisterDomain NO_getInstance() {
		if (instance == null) {
			list = new RegisterDomain[15];
			for (int i = 0; i < 15; i++) {
				list[i] = new RegisterDomain();
			}
			instance = new RegisterDomain();
		}
		return list[r.nextInt(pos) + 1];
	}

	public synchronized static RegisterDomain getInstance() {
		if (instance == null) {
			instance = new RegisterDomain();
		}
		return instance;
	}

	
	
	public List<Cliente> getAllClientes() throws Exception {
		List<Cliente> out = super.getObjects(com.tickets.domain.Cliente.class
				.getName());
		return out;
	}

	public List<Operador> getAllOperarios() throws Exception {
		List<Operador> out = super.getObjects(com.tickets.domain.Operador.class
				.getName());
		return out;
	}

	public List<Servicio> getAllServicios() throws Exception {
		List<Servicio> out = super.getObjects(com.tickets.domain.Servicio.class
				.getName());
		return out;
	}

	public List<Turno> getAllTurnos() throws Exception {
		List<Turno> out = super.getObjects(com.tickets.domain.Turno.class
				.getName());
		return out;
	}

	public List<Puesto> getAllExtras() throws Exception {
		List<Puesto> out = super.getObjects(com.tickets.domain.Puesto.class
				.getName());
		return out;
	}

	public List<Tarea> getAllTareas() throws Exception {
		List<Tarea> out = super.getObjects(com.tickets.domain.Tarea.class
				.getName());
		return out;
	}

	/**
	 * Recibe la clave encriptada y la verifica contra la del usuario pasado
	 * como parametro
	 * 
	 * @param clave
	 * @param usuario
	 * @return true si es valdio el login y false de lo contrario
	 * @throws Exception
	 */
	public boolean validarUsuario(String clave, String usuario)
			throws Exception {
		String query = "select u.clave from Usuario u where u.login='"
				+ usuario + "'";
		List<Object> list = null;
		list = this.hql(query);
		if (list.size() > 0 && clave.compareTo((String) list.get(0)) == 0) {
			return true;
		} else {
			return false;
		}

	}

	public List<Tipo> getTipos(String tipoTipoDescripcion) throws Exception {
		List<Tipo> list = null;
		String query = "select t from Tipo t where t.tipoTipo.descripcion = '"
				+ tipoTipoDescripcion + "'";
		list = this.hql(query);
		return list;
	}

	public AccesoApp getAcceso(String login) throws Exception {
		List<AccesoApp> list = null;
		AccesoApp a = new AccesoApp();
		String queryAcceso = "" + " select ac " + " from AccesoApp ac"
				+ " where ac.usuario.login = ? ";
		list = this.hql(queryAcceso, login);
		if (list.size() == 1) {
			a = list.get(0);
		} else {
			throw new Exception(
					"Error: Este usuario no tiene ningun acceso asignado.");
		}
		return a;
	}

	public Turno getTurnoActual(Long idOperador) throws Exception {
		List<Turno> list = null;
		Turno t = new Turno();
		String queryTurno = "" + " select t " + " from Turno t"
				+ " where t.operador.id = ? and t.estado.sigla like '"
				+ Configuracion.SIGLA_ESTADO_TURNO_ATENDIENDO + "'";
		list = this.hql(queryTurno, idOperador);
		if (list.size() == 1) {
			t = list.get(0);
		} else {
			throw new Exception(
					"Error: Este usuario no tiene ningun turno asignado.");
		}
		return t;
	}

	public List<Turno> getTurnosParaOperador(Long idOperador) throws Exception {
		List<Turno> list = null;
		String queryTurno = ""
				+ " select t "
				+ " from Operador as op, Turno as t join op.servicios as servicio "
				+ " where op.id = ? and t.servicio.id = servicio.id and t.estado.sigla like '"
				+ Configuracion.SIGLA_ESTADO_TURNO_ESPERANDO + "'";
		list = this.hql(queryTurno, idOperador);

		return list;
	}

	public List<Tarea> getTareas(Long idOperador) throws Exception {
		List<Tarea> list = null;
		String queryTarea = "" + " select t " + " from Tarea as t "
				+ " where t.operador.id = ? ";
		list = this.hql(queryTarea, idOperador);

		return list;
	}

	Misc m = new Misc();

	/**
	 * Obtiene las tareas de un operador filtradas por fecha
	 * 
	 * @param idOperador
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public List<Tarea> getTareasPorFechas(Long idOperador, Date fechaDesde,
			Date fechaHasta) throws Exception {
		List<Tarea> list = null;
		String queryTarea = "" + " select t " + " from Tarea as t "
				+ " where t.operador.id = ? " + " and t.inicio BETWEEN "
				+ "TO_DATE('" + m.dateToString(fechaDesde, m.YYYY_MM_DD)
				+ "' , '" + m.YYYY_MM_DD + "') AND TO_DATE('"
				+ m.dateToString(fechaHasta, m.YYYY_MM_DD) + "' , '"
				+ m.YYYY_MM_DD + "') " + " order by t.inicio ";

		queryTarea = "" + " select t " + " from Tarea as t "
				+ " where t.operador.id = ? "
				+ " and t.inicio > ?  and t.inicio < ?) "
				+ " order by t.inicio ";

		System.out.println("\n\n" + queryTarea);
		Object[] para = { idOperador, fechaDesde, fechaHasta };

		list = this.hql(queryTarea, para);

		return list;
	}

	/**
	 * Retorna la Razon Social correspondiente al ruc segun la BD del SET
	 */
	public String getRazonSocialSET(String ruc) throws Exception {
		RucSet set = (RucSet) this.getObject(RucSet.class.getName(), "ruc",
				ruc.trim());
		if (set == null) {
			return "";
		}
		return set.getRazonSocial();
	}


	/**
	 * Retorna la Razon Social correspondiente al ruc segun la BD del SET
	 */
	public String getRazonSocialSET_RUC_CI(String ruc) throws Exception {
		String query = "select c.razonSocial from RucSet c where c.ruc" + " like '%" + ruc
				+ "%'";
		List<String> list = this.hql(query);
		if (list.size() == 1){
			return list.get(0);
		}
		return null;
	}

	
	
	/**
	 * Retorna el Cliente recibiendo el ruc
	 */
	public Cliente getClienteByRuc(String ruc) throws Exception {
		String query = "select c from Cliente c where c.ruc" + " like '" + ruc
				+ "'";
		List<Cliente> list = this.hql(query);
		if (list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Retorna el Cliente recibiendo la cedula
	 */
	public Cliente getClienteByCedula(String cedula) throws Exception {
		String query = "select c from Cliente c where c.cedula" + " like '"
				+ cedula + "'";
		List<Cliente> list = this.hql(query);
		if (list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Retorna el Cliente Nuevo por Defecto
	 */
	public Cliente getClienteNuevoDefault() throws Exception {
		String cedula = Configuracion.CEDULA_CLIENTE_NUEVO;
		String query = "select c from Cliente c where c.cedula" + " like '"
				+ cedula + "'";
		List<Cliente> list = this.hql(query);
		if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new Exception("Error al buscar el Cliente Nuevo por Defecto..");
		}	
	}

	public List<Turno> getTurnosByEstado(String idStr, MyPair estado)
			throws Exception {

		String query = "select t from Turno t " + " where t.numero like '"
				+ idStr + "%' and t.estado.id = " + estado.getId()
				+ " order by t.creacion ";
		List<Turno> list = this.hql(query);
		return list;
	}

	public List<Puesto> getPuestosDisponibles(Long idOperador)
			throws Exception {
		List<Puesto> list = null;
		String queryPuestos = " select p from Operador o join o.puestos p where o.id = "
				+ idOperador;
		list = this.hql(queryPuestos);

		return list;
	}
	
	/**
	 * Levanta el operador a partir del login
	 * @throws Exception 
	 */
	public Operador getOperadorByLogin(String login) throws Exception {
		String query = "select o from Operador o where o.usuario = '" + login + "'";
		
		List<Operador> list = this.hql(query);
		
		if (list.size() == 1) {
			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * Retorna el estado actual del turno
	 */
	public Tipo getEstadoTurno(long idTurno) throws Exception {
		String query = "select t.estado from Turno t where t.id = " + idTurno;
		List<Tipo> list = this.hql(query);
		
		if (list.size() == 1) {
			return list.get(0);
		}		
		return null; 
	}
	
	/**
	 * Retorna la descripcion del turno..
	 */
	public String getDescripcionTurno(long idTurno) throws Exception {
		String query = "select t.descripcion from Turno t where t.id = " + idTurno;
		List<String> list = this.hql(query);
		
		if (list.size() == 1) {
			return list.get(0);
		}		
		return null; 
	}
	
	
	/**
	 * Retorna la lista de operadores segun el servicio..
	 */
	public List<Operador> getOperadoresByServicio(long idServicio)
			throws Exception {

		String query = "select op from Operador op inner join op.servicios serv where serv.id = "
				+ idServicio;
		List<Operador> list = this.hql(query);

		if (list.size() > 0) {
			return list;
		}
		return new ArrayList<Operador>();
	}
	
	/**
	 * @return los topes..
	 */
	public List<Tope> getTopes(int nroDia) throws Exception {
		String query = "select t from Tope t where t.dia = " + nroDia + " order by t.servicio";
		return this.hql(query);
	}
	
	/**
	 * @return los tickets segun fecha..
	 */
	public List<Turno> getTurnos(Date desde, Date hasta, long idServicio) throws Exception {

		String query = "select t from Turno t where t.servicio.id = " + idServicio
				+ " and t.creacion between ? and ?" + " order by t.creacion";

		List<Object> listParams = new ArrayList<Object>();
		listParams.add(desde);
		listParams.add(hasta);

		Object[] params = new Object[listParams.size()];
		for (int i = 0; i < listParams.size(); i++) {
			params[i] = listParams.get(i);
		}
		return this.hql(query, params);
	}
	
	/**
	 * @return los tickets segun fecha..
	 */
	public List<Turno> getTurnos(Date desde, Date hasta) throws Exception {
		String query = "select t from Turno t where"
				+ " t.creacion between ? and ?" + " order by t.servicio.descripcion, t.creacion";

		List<Object> listParams = new ArrayList<Object>();
		listParams.add(desde);
		listParams.add(hasta);

		Object[] params = new Object[listParams.size()];
		for (int i = 0; i < listParams.size(); i++) {
			params[i] = listParams.get(i);
		}
		return this.hql(query, params);
	}
	
	/**
	 * @return los tickets cancelados segun fecha..
	 */
	public List<Turno> getTurnosCancelados(Date desde, Date hasta) throws Exception {

		String query = "select t from Turno t where"
				+ " t.estado.sigla = '" + Configuracion.SIGLA_ESTADO_TURNO_CANCELADO + "' and"
				+ " t.creacion between ? and ?" + " order by t.creacion";

		List<Object> listParams = new ArrayList<Object>();
		listParams.add(desde);
		listParams.add(hasta);

		Object[] params = new Object[listParams.size()];
		for (int i = 0; i < listParams.size(); i++) {
			params[i] = listParams.get(i);
		}
		return this.hql(query, params);
	}
	
	/**
	 * @return las localidades..
	 */
	public List<Localidad> getLocalidades(String codDpto, String dpto, String distrito) throws Exception {
		String query = "select l from Localidad l where upper(l.codigoDpto) like '%" + codDpto.toUpperCase() + "%' "
				+ " and upper(l.departamento) like '%" + dpto.toUpperCase() + "%'"
				+ " and upper(l.distrito) like '%" + distrito.toUpperCase() + "%'";
		return this.hql(query);
	}
	
	/**
	 * @return los paises..
	 */
	public List<Pais> getPaises(String descripcion, String nacionalidad) throws Exception {
		String query = "select p from Pais p where upper(p.descripcion) like '%" + descripcion.toUpperCase() + "%' "
				+ " and upper(p.nacionalidad) like '%" + nacionalidad.toUpperCase() + "%' order by p.descripcion";
		return this.hql(query);
	}
	
	/**
	 * @return usuarios segun login..
	 */
	public List<Usuario> getUsuarios(String login) throws Exception {
		String query = "select u from Usuario u where upper(u.login) = '" + login.toUpperCase() + "' ";
		return this.hql(query);
	}
	
	/**
	 * @return los topes..
	 */
	public List<Tope> getTopes() throws Exception {
		String query = "select t from Tope t order by t.servicio";
		return this.hql(query);
	}
	
	/**
	 * @return usuarios segun rol..
	 */
	public List<Usuario> getUsuarios(long idRol) throws Exception {
		String query = "select u from Usuario u join u.perfiles p where p.id = " + idRol;
		return this.hql(query);
	}
	
	public static void main(String[] args) {
		RegisterDomain rr = RegisterDomain.getInstance();
		try {
			List<Tope> list = rr.getTopes(1);
			for (int i = 2; i < 7; i++) {
				for (Tope tope : list) {
					Tope tp = new Tope();
					tp.setDia(i);
					tp.setServicio(tope.getServicio());
					tp.setTM(0);
					tp.setTT(0);
					rr.saveObject(tp, "sys");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
