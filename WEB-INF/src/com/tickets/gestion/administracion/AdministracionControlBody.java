package com.tickets.gestion.administracion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import com.coreweb.control.Control;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.templateABM.Body;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.AssemblerUtil;
import com.tickets.UtilDTO;

@SuppressWarnings("static-access")
public class AdministracionControlBody extends Body {

	@Init(superclass = true)
	public void initAdministracionControlBody() {
		this.dto = (UtilDTO) this.getDtoUtil();
	}

	@AfterCompose(superclass = true)
	public void afterComposeAdministracionControlBody() {
	}

	UtilDTO dto = null;
	
	public void afterSave() {
		Control.setInicialDtoUtil(new AssemblerUtil().getDTOUtil());
	}

	public UtilDTO getDto() {
		return dto;
	}

	public void setDto(UtilDTO dto) {
		this.dto = dto;
	}

	private List<MyArray> nuevosMA = new ArrayList<MyArray>();
	private List<MyPair> nuevosMP = new ArrayList<MyPair>();

	@Override
	public Assembler getAss() {
		return new AssemblerUtil();
	}

	@Override
	public DTO getDTOCorriente() {
		return this.dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.dto = (UtilDTO) dto;

	}

	@Override
	public DTO nuevoDTO() throws Exception {
		return (UtilDTO) AssemblerUtil.getDTOUtil();
	}

	@Override
	public String getEntidadPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DTO> getAllModel() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	// =========== servicios ================================

	private MyArray selectedServicio = null;

	public MyArray getSelectedServicio() {
		return selectedServicio;
	}

	public void setSelectedServicio(MyArray selectedServicio) {
		this.selectedServicio = selectedServicio;
	}

	@Command()
	@NotifyChange("*")
	public void eliminarServicio() {
		if (this.selectedServicio != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el servicio?")) {
				// verificar que no este asociado a ningun objeto

				this.getDto().getServicios().remove(this.selectedServicio);
			}
			this.nuevosMP.remove(this.selectedServicio);
			this.setSelectedServicio(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarServicio() {

		if (mensajeAgregar("Agregar un nuevo servicio?")) {
			MyArray nServ = new MyArray();
			nServ.setPos1("--editar--");

			this.getDto().getServicios().add(nServ);
			this.setSelectedServicio(nServ);
			this.nuevosMA.add(this.selectedServicio);
		}
	}

	// =========== puestos ================================

	private MyArray selectedPuesto = null;

	public MyArray getSelectedPuesto() {
		return selectedPuesto;
	}

	public void setSelectedPuesto(MyArray selectedPuesto) {
		this.selectedPuesto = selectedPuesto;
	}

	@Command()
	@NotifyChange("*")
	public void eliminarPuesto() {
		if (this.selectedPuesto != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar el puesto?")) {
				// verificar que no este asociado a ningun objeto

				this.getDto().getPuestos().remove(this.selectedPuesto);
			}
			this.nuevosMP.remove(this.selectedPuesto);
			this.setSelectedPuesto(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarPuesto() {

		if (mensajeAgregar("Agregar un nuevo puesto?")) {
			MyArray nPue = new MyArray();
			nPue.setPos1("--editar--");

			this.getDto().getPuestos().add(nPue);
			this.setSelectedPuesto(nPue);
			this.nuevosMA.add(this.selectedPuesto);
		}
	}

	// =========== tareaFDS ================================

	private MyPair selectedTarea = null;

	public MyPair getSelectedTarea() {
		return selectedTarea;
	}

	public void setSelectedTarea(MyPair selectedTarea) {
		this.selectedTarea = selectedTarea;
	}

	@Command()
	@NotifyChange("*")
	public void eliminarTarea() {
		if (this.selectedTarea != null) {

			if (mensajeEliminar("Está seguro que quiere eliminar la tarea?")) {
				// verificar que no este asociado a ningun objeto

				this.getDto().getTiposDeTareaFDS().remove(this.selectedTarea);
			}
			this.nuevosMP.remove(this.selectedTarea);
			this.setSelectedTarea(null);
		}
	}

	@Command()
	@NotifyChange("*")
	public void agregarTarea() {

		if (mensajeAgregar("Agregar una nueva tarea?")) {
			MyPair nTar = new MyPair();
			nTar.setText("--editar--");

			this.getDto().getTiposDeTareaFDS().add(nTar);
			this.setSelectedTarea(nTar);
			this.nuevosMP.add(this.selectedTarea);
		}
	}

	@Override
	public boolean verificarAlGrabar() {
		// TODO Auto-generated method stub
		boolean res = true;

		for (MyArray ma : this.nuevosMA) {
			if (((String) ma.getPos1()).compareTo("") == 0
					|| ((String) ma.getPos1()).compareTo("--editar--") == 0) {
				res = false;
				System.out.println("---OJO MA--- " + ma.getId() + " - "
						+ ma.getPos1());
			}
		}
		for (MyPair mp : this.nuevosMP) {
			if (((String) mp.getText()).compareTo("") == 0
					|| ((String) mp.getText()).compareTo("--editar--") == 0) {
				res = false;
				System.out.println("---OJO MP--- " + mp.getId() + " - "
						+ mp.getText() + " - " + mp.getSigla());
			}
		}

		return res;
	}

	@Override
	public String textoErrorVerificarGrabar() {
		return " Verifique que la descripción de los elementos es correcta,\n no puede contener la plabra --editar-- ni ser vacía. ";
	}

}