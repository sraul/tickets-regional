package com.tickets.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utiles {

	public static Date getFecha(String fecha) throws Exception {
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		return formatter.parse(fecha);
	}
	
	public static String getFechaString(Date fecha, String format) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(fecha);
	}
}
