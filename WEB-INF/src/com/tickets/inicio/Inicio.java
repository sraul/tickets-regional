package com.tickets.inicio;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.coreweb.Config;
import com.coreweb.control.Control;
import com.coreweb.login.LoginUsuarioDTO;
import com.tickets.AssemblerUtil;
import com.tickets.Configuracion;
import com.tickets.gestion.administracion.OperadorAssembler;
import com.tickets.gestion.administracion.OperadorDTO;

public class Inicio {

	public synchronized static void init() {
		// Para setear cualquier cosa antes de empezar

		// ver si tiene un UtilDTO seteado
		if (Control.existDtoUtil() == false) {
			Control.setEmpresa(Configuracion.EMPRESA);
			Control.setInicialDtoUtil(new AssemblerUtil().getDTOUtil());
		}
	}

	public void afterLogin() throws Exception {
		// asi se recupera la session
		Session s = Sessions.getCurrent();

		//System.out.println("======== after login ======");

		LoginUsuarioDTO uDto = (LoginUsuarioDTO) s.getAttribute(Config.USUARIO);
		String login = uDto.getLogin();

		AssemblerAcceso as = new AssemblerAcceso();
		AccesoDTO aDto = (AccesoDTO) as.obtenerAccesoDTO(login);
		s.setAttribute(Configuracion.ACCESO, aDto);
		
		OperadorAssembler opeAss = new OperadorAssembler();
		OperadorDTO ope = opeAss.getOperadorByLogin(login);
		if (ope != null) {
			s.setAttribute(Configuracion.OPERADOR, ope);
		}		
	}
}
