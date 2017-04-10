package com.tickets.domain;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class Derivado extends Domain {

	Turno turnoProximo;

	public Turno getTurnoProximo() {
		return turnoProximo;
	}

	public void setTurnoProximo(Turno turnoProximo) {
		this.turnoProximo = turnoProximo;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
