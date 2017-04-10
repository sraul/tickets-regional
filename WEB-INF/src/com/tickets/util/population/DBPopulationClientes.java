package com.tickets.util.population;

import com.coreweb.extras.csv.CSV;
import com.tickets.domain.Cliente;
import com.tickets.domain.RegisterDomain;

public class DBPopulationClientes {

	static String src = "./WEB-INF/docs/Clientes/clientes.csv";
	
	static String CAMPO_DESCRIPCION = "Descripcion";
	static String CAMPO_CODIGO = "Codigo";
	static String CAMPO_CEDULA = "Cedula";
	static String CAMPO_RUC = "Ruc";

	/**
	 * Poblar datos de Clientes
	 */
	String[][] cab = { { "Sucursal", CSV.STRING } };
	String[][] det = { { CAMPO_CODIGO, CSV.STRING },
			{ CAMPO_DESCRIPCION, CSV.STRING }, { CAMPO_CEDULA, CSV.STRING },
			{ CAMPO_RUC, CSV.STRING } };

	public void poblarClientes() throws Exception {
		
		System.out.println("-- Poblando Clientes...");

		RegisterDomain rr = RegisterDomain.getInstance();
		
		CSV csv = new CSV(cab, det, src);
		csv.start();
		
		while (csv.hashNext()) {
			
			String descripcion = csv.getDetalleString(CAMPO_DESCRIPCION);
			String cedula = csv.getDetalleString(CAMPO_CEDULA);
			String ruc = csv.getDetalleString(CAMPO_RUC);
			
			Cliente cl = new Cliente();
			cl.setDescripcion(descripcion);
			cl.setCedula(cedula);
			cl.setRuc(ruc);
			rr.saveObject(cl, "sys");
			
			System.out.println("Cliente agregado: " + descripcion);
		}
		
		System.out.println("-- Fin Clientes...");

	}
	
	public static void main(String[] args) throws Exception {
		DBPopulationClientes clientes = new DBPopulationClientes();
		clientes.poblarClientes();
	}

}
