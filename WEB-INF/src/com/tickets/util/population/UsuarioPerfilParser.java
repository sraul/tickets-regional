package com.tickets.util.population;

import com.coreweb.Config;
import com.coreweb.util.population.DBMenuUserParser;
import com.tickets.Configuracion;
import com.tickets.ID;

/**
 * Caraga los datos del menu y la configuracion del usuario
 * 
 * @author daniel
 * 
 */
public class UsuarioPerfilParser {

	private static String[] propiedades = {
		Configuracion.SIGLA_SUCURSAL
	};

	/**
	 * Carga los datos del menu y de los usuarios. Se usa para cuando NO se
	 * ejecuta desde la WEB.
	 */
	public static void loadMenuConfig() throws Exception {
		String iniFile = "./WEB-INF/menu_config.ini";
		String iniUser = "./WEB-INF/usuarios-propiedad.ini";
		UsuarioPerfilParser.loadMenuConfig(iniFile, iniUser);
	}

	/**
	 * Carga los datos del menu y de los usuarios. Se usa para cuando se ejecuta
	 * desde la WEB.
	 */
	public static void loadMenuConfigBoton() throws Exception {
		String iniFile = Config.DIRECTORIO_BASE_REAL
				+ "WEB-INF/menu_config.ini";
		String iniUser = Config.DIRECTORIO_BASE_REAL
				+ "WEB-INF/usuarios-propiedad.ini";

		UsuarioPerfilParser.loadMenuConfig(iniFile, iniUser);
	}

	/**
	 * Carga los datos del menu y de los usuarios. Recibe la ubicación de donde
	 * está el archivo ini a leer
	 */
	@SuppressWarnings("rawtypes")
	private static void loadMenuConfig(String iniFile, String userFile)
			throws Exception {

		Class idClass = ID.class;
		DBMenuUserParser d = new DBMenuUserParser(iniFile, idClass, userFile,
				propiedades);
		d.cargaMenusPerfilesUsuarios();
	}

	
	
	
	public static void main(String[] args) throws Exception {
		UsuarioPerfilParser.loadMenuConfig();
	}

}
