package com.tickets;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.coreweb.templateABM.Body;
import com.tickets.inicio.AccesoDTO;

/**
 * Esta clase intermedia nos permite tener métodos propios para cada aplicación, y mantener 
 * la estructura del Core.
 * NOTA: se puede implementar una clase similar para los otros controles, SimpleViewModel y SoloViewModel
 * @author daniel
 *
 */
public abstract class BodyApp extends Body {

	private UsuarioPropiedadApp usuarioPropiedad = null;
	private AccesoDTO acceso = null;
	
	@Init(superclass = true)
	public void initBodyApp(){
		this.usuarioPropiedad = new UsuarioPropiedadApp(this.getUs());
	}
	
	@AfterCompose(superclass = true)
	public void afterComposeBodyApp(){
		
	}
	
	// ============== Util core ===================================

	@Override
	/**
	 * Hace el cast para trabajar en la aplicacion.
	 */
	public UtilDTO getDtoUtil() {
		UtilDTO u = (UtilDTO)super.getDtoUtil();
		return u;
	}
		
	// ========================================================================
	
	public UsuarioPropiedadApp getUsuarioPropiedad(){
		return this.usuarioPropiedad;
	}

	public AccesoDTO getAcceso() {
		if (this.acceso == null){
			Session s = Sessions.getCurrent();
			this.acceso = (AccesoDTO) s.getAttribute(Configuracion.ACCESO);
		}
		return acceso;
	}	
	
}
