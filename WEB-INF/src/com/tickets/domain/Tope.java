package com.tickets.domain;

import org.zkoss.bind.annotation.DependsOn;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class Tope extends Domain {

	private int dia;
	private String servicio;
	private int TM;
	private int TT;
	
	private boolean edit = false;
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}
	
	@DependsOn({ "TM", "TT" })
	public boolean isDisabled() {
		return this.TM == 0 && this.TT == 0;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public int getTM() {
		return TM;
	}

	public void setTM(int tM) {
		if (tM < 0) {
			tM = 0;
		}
		TM = tM;
	}

	public int getTT() {
		return TT;
	}

	public void setTT(int tT) {
		if (tT < 0) {
			tT = 0;
		}
		TT = tT;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

}
