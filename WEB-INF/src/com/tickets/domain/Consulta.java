package com.tickets.domain;

import java.util.Date;

import com.coreweb.domain.Domain;

@SuppressWarnings("serial")
public class Consulta extends Domain {

	private String axilarRectal;
	private String pulso;
	private String frecuenciaRespiratoria;
	private String talla;
	private String imc;
	private String estadoNutricional;
	private String presionArterial;
	private String frecuenciaCardiaca;
	private String pesoGramos;
	private String perimetroCelafico;
	private String circunferenciaAbdominal;
	private String alimentacion;
	
	private Date fecha;
	private String motivo;
	private String codigoCie;
	private String especialidad;
	private String doctorTratante;
	private String estudios;
	private String medicamentosRecetados;
	private String observaciones;
	
	private long idpaciente;
	
	@Override
	public int compareTo(Object o) {
		return -1;
	}

	public String getAxilarRectal() {
		return axilarRectal;
	}

	public void setAxilarRectal(String axilarRectal) {
		this.axilarRectal = axilarRectal;
	}

	public String getPulso() {
		return pulso;
	}

	public void setPulso(String pulso) {
		this.pulso = pulso;
	}

	public String getFrecuenciaRespiratoria() {
		return frecuenciaRespiratoria;
	}

	public void setFrecuenciaRespiratoria(String frecuenciaRespiratoria) {
		this.frecuenciaRespiratoria = frecuenciaRespiratoria;
	}

	public String getTalla() {
		return talla;
	}

	public void setTalla(String talla) {
		this.talla = talla;
	}

	public String getImc() {
		return imc;
	}

	public void setImc(String imc) {
		this.imc = imc;
	}

	public String getEstadoNutricional() {
		return estadoNutricional;
	}

	public void setEstadoNutricional(String estadoNutricional) {
		this.estadoNutricional = estadoNutricional;
	}

	public String getPresionArterial() {
		return presionArterial;
	}

	public void setPresionArterial(String presionArterial) {
		this.presionArterial = presionArterial;
	}

	public String getFrecuenciaCardiaca() {
		return frecuenciaCardiaca;
	}

	public void setFrecuenciaCardiaca(String frecuenciaCardiaca) {
		this.frecuenciaCardiaca = frecuenciaCardiaca;
	}

	public String getPesoGramos() {
		return pesoGramos;
	}

	public void setPesoGramos(String pesoGramos) {
		this.pesoGramos = pesoGramos;
	}

	public String getPerimetroCelafico() {
		return perimetroCelafico;
	}

	public void setPerimetroCelafico(String perimetroCelafico) {
		this.perimetroCelafico = perimetroCelafico;
	}

	public String getCircunferenciaAbdominal() {
		return circunferenciaAbdominal;
	}

	public void setCircunferenciaAbdominal(String circunferenciaAbdominal) {
		this.circunferenciaAbdominal = circunferenciaAbdominal;
	}

	public String getAlimentacion() {
		return alimentacion;
	}

	public void setAlimentacion(String alimentacion) {
		this.alimentacion = alimentacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getCodigoCie() {
		return codigoCie;
	}

	public void setCodigoCie(String codigoCie) {
		this.codigoCie = codigoCie;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getDoctorTratante() {
		return doctorTratante;
	}

	public void setDoctorTratante(String doctorTratante) {
		this.doctorTratante = doctorTratante;
	}

	public String getEstudios() {
		return estudios;
	}

	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}

	public String getMedicamentosRecetados() {
		return medicamentosRecetados;
	}

	public void setMedicamentosRecetados(String medicamentosRecetados) {
		this.medicamentosRecetados = medicamentosRecetados;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public long getIdpaciente() {
		return idpaciente;
	}

	public void setIdpaciente(long idpaciente) {
		this.idpaciente = idpaciente;
	}
}
