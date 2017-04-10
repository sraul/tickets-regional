package com.tickets.gestion.administracion;

import java.util.ArrayList;
import java.util.List;

import com.coreweb.extras.browser.Browser;
import com.coreweb.extras.browser.ColumnaBrowser;
import com.tickets.domain.Operador;

public class OperadorBrowser extends Browser {

	@Override
	public List<ColumnaBrowser> getColumnasBrowser() {
		ColumnaBrowser col1 = new ColumnaBrowser();
		ColumnaBrowser col2 = new ColumnaBrowser();
		ColumnaBrowser col3 = new ColumnaBrowser();
		//ColumnaBrowser col4 = new ColumnaBrowser();
		//ColumnaBrowser col5 = new ColumnaBrowser();

		col1.setCampo("usuario");
		col1.setTitulo("Usuario");

		col2.setCampo("nombre");
		col2.setTitulo("Nombre");

		col3.setCampo("descripcion");
		col3.setTitulo("Descripción");

		List<ColumnaBrowser> columnas = new ArrayList<ColumnaBrowser>();
		columnas.add(col1);
		columnas.add(col2);
		columnas.add(col3);

		return columnas;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntidadPrincipal() {
		return Operador.class;
	}

	@Override
	public void setingInicial() {
		this.setWidthWindows("850px");
		this.setHigthWindows("75%");
	}

	@Override
	public String getTituloBrowser() {
		return "Operadores";
	}

}
