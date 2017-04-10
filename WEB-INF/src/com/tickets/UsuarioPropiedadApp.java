package com.tickets;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.coreweb.UsuarioPropiedad;
import com.coreweb.domain.Tipo;
import com.coreweb.login.LoginUsuarioDTO;
import com.coreweb.util.MyPair;
import com.tickets.domain.RegisterDomain;

/**
 * Contiene las propiedades especificas de los usuarios
 * 
 * @author daniel
 * 
 */
public class UsuarioPropiedadApp extends UsuarioPropiedad {

	public UsuarioPropiedadApp(LoginUsuarioDTO usuario) {
		super(usuario);
		// TODO Auto-generated constructor stub
	}

	
	public synchronized MyPair getSucursal() throws Exception {

		String keyLog = this.getUsuario().getLogin()+"_"+Configuracion.SIGLA_SUCURSAL;	
		MyPair m = null;
		Session session = Sessions.getCurrent();
		Object obj = session.getAttribute(keyLog);
		if (obj == null) {
			String s = this.getUsuario().getPropiedad(
					Configuracion.SIGLA_SUCURSAL);
			RegisterDomain rr = RegisterDomain.getInstance();
			Tipo sucTipo = rr.getTipoPorDescripcion(s);
			m = new MyPair(sucTipo.getId(), sucTipo.getDescripcion());
			session.setAttribute(keyLog, m);
		}else{
			m = (MyPair) obj;
		}
		return m;
	}

}
