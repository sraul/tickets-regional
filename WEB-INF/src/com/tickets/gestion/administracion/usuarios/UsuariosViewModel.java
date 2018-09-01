package com.tickets.gestion.administracion.usuarios;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Popup;

import com.coreweb.control.SimpleViewModel;
import com.coreweb.domain.Operacion;
import com.coreweb.domain.Perfil;
import com.coreweb.domain.Permiso;
import com.coreweb.domain.Usuario;
import com.coreweb.util.Misc;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Tope;

public class UsuariosViewModel extends SimpleViewModel {
	
	private Usuario nvo_usuario = new Usuario();
	private Perfil nvo_rol = new Perfil();
	private Perfil selected_rol;
	
	private Operacion selectedOperacion;
	private Usuario selectedUsuario;
	private Perfil selected_perfil;
	
	private String confirmarPassword = "";

	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	@NotifyChange("*")
	public void guardar(@BindingParam("popup") Popup popup) throws Exception {
		if (!this.validarDatosUsuario()) {
			Clients.showNotification("DATOS NO VALIDOS, FAVOR VERIFIQUE..", Clients.NOTIFICATION_TYPE_ERROR, null, null, 0);
			return;
		}
		this.cifrarPassword();
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.nvo_usuario, this.getLoginNombre());
		Clients.showNotification("REGISTRO GUARDADO..!");
		this.nvo_usuario = new Usuario();
		popup.close();
	}
	
	@Command
	@NotifyChange("*")
	public void guardarRol(@BindingParam("popup") Popup popup) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.nvo_rol, this.getLoginNombre());
		Clients.showNotification("REGISTRO GUARDADO..!");
		this.nvo_rol = new Perfil();
		popup.close();
	}
	
	@Command
	@NotifyChange("*")
	public void guardarRol_(@BindingParam("popup") Popup popup) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(this.selected_rol, this.getLoginNombre());
		Clients.showNotification("REGISTRO GUARDADO..!");
		popup.close();
	}
	
	@Command
	@NotifyChange("nvo_rol")
	public void asignarOperacion() {
		Permiso permiso = new Permiso();
		permiso.setHabilitado(true);
		permiso.setOperacion(this.selectedOperacion);
		permiso.setPerfil(this.nvo_rol);
		this.nvo_rol.getPermisos().add(permiso);
		
	}
	
	@Command
	@NotifyChange("selected_rol")
	public void asignarOperacion_() {
		Permiso permiso = new Permiso();
		permiso.setHabilitado(true);
		permiso.setOperacion(this.selectedOperacion);
		permiso.setPerfil(this.selected_rol);
		this.selected_rol.getPermisos().add(permiso);
		
	}
	
	@Command
	@NotifyChange("*")
	public void deleteRol() throws Exception {
		if (!this.validarRol()) {
			Clients.showNotification("NO SE PUEDE ELIMINAR EL ROL, EXISTEN USUARIOS ASIGNADOS..", Clients.NOTIFICATION_TYPE_ERROR, null, null, 0);
			return;
		}
		if(!this.mensajeSiNo("DESEA ELIMINAR EL ROL")) return;
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.deleteObject(this.selected_rol);
		this.selected_rol = null;
		Clients.showNotification("REGISTRO ELIMINADO..!");
	}
	
	@Command
	@NotifyChange("*")
	public void selectPerfil(@BindingParam("comp") Popup comp) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		this.selectedUsuario.getPerfiles().add(this.selected_perfil);
		rr.saveObject(this.selectedUsuario, this.getLoginNombre());
		
		this.selected_perfil = null;
		comp.close();
		Clients.showNotification("ROL ASIGNADO..");
	}
	
	@Command
	@NotifyChange("*")
	public void deletePerfil(@BindingParam("item") Perfil item) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		this.selectedUsuario.getPerfiles().remove(item);
		rr.saveObject(this.selectedUsuario, this.getLoginNombre());
		Clients.showNotification("ROL DES-ASIGNADO..");
	}
	
	/**
	 * @return true si no existe usuarios con ese rol..
	 */
	private boolean validarRol() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getUsuarios(this.selected_rol.getId()).size() == 0;
	}
	
	/**
	 * cifra el password en MD5
	 */
	private void cifrarPassword() {
		Misc m = new Misc();
		String password = m.encriptar(this.nvo_usuario.getClave());
		this.nvo_usuario.setClave(password);
	}
	
	/**
	 * @return true si son datos validos..
	 */
	private boolean validarDatosUsuario() throws Exception {
		boolean out = true;
		RegisterDomain rr = RegisterDomain.getInstance();
		
		if (this.nvo_usuario.getNombre() == null || this.nvo_usuario.getNombre().trim().isEmpty()) {
			out = false;
		}
		
		if (this.nvo_usuario.getLogin() == null || this.nvo_usuario.getLogin().trim().isEmpty()) {
			out = false;
		}
		
		if (this.nvo_usuario.getLogin() != null && rr.getUsuarios(this.nvo_usuario.getLogin()).size() > 0) {
			out = false;
		}
		
		if (this.nvo_usuario.getClave() != null && !this.nvo_usuario.getClave().equals(this.confirmarPassword)) {
			out = false;
		}		
		return out;
	}
	
	/**
	 * GETS / SETS
	 */
	
	/**
	 * @return los usuarios..
	 */
	public List<Usuario> getUsuarios() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getAllUsuarios();
	}
	
	/**
	 * @return los roles..
	 */
	public List<Perfil> getRoles() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getAllPerfiles();
	}
	
	/**
	 * @return las operaciones..
	 */
	public List<Operacion> getOperaciones() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getAllOperaciones();
	}
	
	/**
	 * @return las especialidades..
	 */
	public List<String> getEspecialidades() throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		List<Tope> topes = rr.getTopes();
		List<String> out = new ArrayList<String>();
		for (Tope tope : topes) {
			if (!out.contains(tope.getServicio())) {
				out.add(tope.getServicio());
			}
		}
		return out;
	}

	public Usuario getNvo_usuario() {
		return nvo_usuario;
	}

	public void setNvo_usuario(Usuario nvo_usuario) {
		this.nvo_usuario = nvo_usuario;
	}

	public String getConfirmarPassword() {
		return confirmarPassword;
	}

	public void setConfirmarPassword(String confirmarPassword) {
		this.confirmarPassword = confirmarPassword;
	}

	public Perfil getNvo_rol() {
		return nvo_rol;
	}

	public void setNvo_rol(Perfil nvo_rol) {
		this.nvo_rol = nvo_rol;
	}

	public Operacion getSelectedOperacion() {
		return selectedOperacion;
	}

	public void setSelectedOperacion(Operacion selectedOperacion) {
		this.selectedOperacion = selectedOperacion;
	}

	public Perfil getSelected_rol() {
		return selected_rol;
	}

	public void setSelected_rol(Perfil selected_rol) {
		this.selected_rol = selected_rol;
	}

	public Usuario getSelectedUsuario() {
		return selectedUsuario;
	}

	public void setSelectedUsuario(Usuario selectedUsuario) {
		this.selectedUsuario = selectedUsuario;
	}

	public Perfil getSelected_perfil() {
		return selected_perfil;
	}

	public void setSelected_perfil(Perfil selected_perfil) {
		this.selected_perfil = selected_perfil;
	}
}
