package com.tickets.gestion.administracion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.extras.browser.Browser;
import com.coreweb.templateABM.Body;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.UtilDTO;
import com.tickets.domain.Operador;

@SuppressWarnings({"unchecked", "rawtypes"})
public class OperadorControlBody extends Body {

	OperadorDTO dto = new OperadorDTO();
	private UtilDTO miUtilDto = (UtilDTO) this.getDtoUtil();
	private List<MyArray> servicios;
	private List<MyArray> puestos;
	private MyArray selectedServicio;
	private MyArray selectedPuesto;
	private MyArray selectedServicioOpe;
	private MyArray selectedPuestoOpe;
	private MyPair selectedEstadoServicio;
	private MyPair selectedEstadoPuesto;

	public OperadorDTO getDto() {
		return dto;
	}

	public void setDto(OperadorDTO dto) {
		this.dto = dto;
	}

	public UtilDTO getMiUtilDto() {
		return miUtilDto;
	}

	public void setMiUtilDto(UtilDTO miUtilDto) {
		this.miUtilDto = miUtilDto;
	}

	public List<MyArray> getServicios() {
		return servicios;
	}

	public void setServicios(List<MyArray> servicios) {
		this.servicios = servicios;
	}

	public List<MyArray> getPuestos() {
		return puestos;
	}

	public void setPuestos(List<MyArray> puestos) {
		this.puestos = puestos;
	}

	public MyArray getSelectedServicio() {
		return selectedServicio;
	}

	public void setSelectedServicio(MyArray selectedServicio) {
		this.selectedServicio = selectedServicio;
	}

	public MyArray getSelectedPuesto() {
		return selectedPuesto;
	}

	public void setSelectedPuesto(MyArray selectedPuesto) {
		this.selectedPuesto = selectedPuesto;
	}

	public MyArray getSelectedServicioOpe() {
		return selectedServicioOpe;
	}

	public void setSelectedServicioOpe(MyArray selectedServicioOpe) {
		this.selectedServicioOpe = selectedServicioOpe;
	}

	public MyArray getSelectedPuestoOpe() {
		return selectedPuestoOpe;
	}

	public void setSelectedPuestoOpe(MyArray selectedPuestoOpe) {
		this.selectedPuestoOpe = selectedPuestoOpe;
	}
	
	public MyPair getSelectedEstadoServicio() {
		return selectedEstadoServicio;
	}

	public void setSelectedEstadoServicio(MyPair selectedEstadoServicio) {
		this.selectedEstadoServicio = selectedEstadoServicio;
	}

	public MyPair getSelectedEstadoPuesto() {
		return selectedEstadoPuesto;
	}

	public void setSelectedEstadoPuesto(MyPair selectedEstadoPuesto) {
		this.selectedEstadoPuesto = selectedEstadoPuesto;
	}

	@Init(superclass = true)
	public void initOperadorControlBody() {
		this.servicios = this.miUtilDto.getServicios();
		this.puestos = this.miUtilDto.getPuestos();
	}

	@AfterCompose(superclass = true)
	public void afterComposeOperadorControlBody() {

	}

	@Override
	public boolean verificarAlGrabar() {
		System.out.println("========== datos ============");
		System.out.println("servicios: "+this.dto.getServicios().size());
		System.out.println("puestos: " +this.dto.getPuestos().size());
		return true;
	}

	@Override
	public String textoErrorVerificarGrabar() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public Assembler getAss() {
		// TODO Auto-generated method stub
		return new OperadorAssembler();
	}

	@Override
	public DTO getDTOCorriente() {
		return dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.dto = (OperadorDTO) dto;
	}

	@Override
	public DTO nuevoDTO() throws Exception {
		OperadorDTO dtoNuevo = new OperadorDTO();
		dtoNuevo.setEstado(miUtilDto.getEstadoOperadorLibre());
		dtoNuevo.setServicios(new ArrayList<MyArray>());
		dtoNuevo.setPuestos(new ArrayList<MyArray>());
		return dtoNuevo;
	}

	@Override
	public String getEntidadPrincipal() {
		return Operador.class.getName();
	}

	@Override
	public List<DTO> getAllModel() throws Exception {
		return this.getAllDTOs(this.getEntidadPrincipal());
	}

	public Browser getBrowser() {
		return new OperadorBrowser();
	}
	
	
	@Command()
	@NotifyChange("*")
	public void agregarServicioAlOperador() {
		if (!((List) this.dto.getServicios())
				.contains(selectedServicioOpe))
			((List) this.dto.getServicios()).add(this.selectedServicioOpe);

		this.setSelectedServicioOpe(null);
	}
	
	@Command()
	@NotifyChange("*")
	public void agregarPuestoAlOperador() {
		if (!((List) this.dto.getPuestos())
				.contains(selectedPuestoOpe))
			((List) this.dto.getPuestos()).add(this.selectedPuestoOpe);

		this.setSelectedPuestoOpe(null);
	}
	
	@Command()
	@NotifyChange("*")
	public void eliminarServicioOperador() {
		if (this.selectedServicio != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el servicio del operador?")) {
				// verificar que no este asociado a ningun objeto

				((List) this.dto.getServicios())
						.remove(this.selectedServicio); //selectedServicioOperador
			}
			this.setSelectedServicio(null);
		}
	}
	
	@Command()
	@NotifyChange("*")
	public void eliminarPuestoOperador() {
		if (this.selectedPuesto != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el puesto del operador?")) {
				// verificar que no este asociado a ningun objeto

				((List) this.dto.getPuestos())
						.remove(this.selectedPuesto); // selectedPuestoOperador
			}
			this.setSelectedPuesto(null);
		}
	}
	
	/**
	 * Encripta el password..
	 */
	@Command
	public void encriptar(){
		String pass = this.dto.getPass();
		this.dto.setPass(this.m.encriptar(pass));
		BindUtils.postNotifyChange(null, null, this.dto, "pass");
	}

}
