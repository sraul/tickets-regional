package com.tickets.inicio;

import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;

public class AccesoDTO extends DTO {
	
	private MyArray usuario;

	public MyArray getUsuario() {
		return usuario;
	}

	public void setUsuario(MyArray usuario) {
		this.usuario = usuario;
	}

}
