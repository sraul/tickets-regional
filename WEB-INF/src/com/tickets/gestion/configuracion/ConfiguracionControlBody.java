package com.tickets.gestion.configuracion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;

import com.coreweb.control.Control;
import com.coreweb.dto.Assembler;
import com.coreweb.dto.DTO;
import com.coreweb.util.MyArray;
import com.coreweb.util.MyPair;
import com.tickets.AssemblerUtil;
import com.tickets.BodyApp;
import com.tickets.UtilDTO;

public class ConfiguracionControlBody extends BodyApp {

	private String tab = "all";

	@Init(superclass = true)
	public void initConfiguracionControlBody(@ExecutionParam("tab") String tab) {
		this.dto = this.getDtoUtil();
		if (tab != null) {
			this.tab = tab;
			this.setTextoFormularioCorriente("Definiciones [" + this.tab + "]");
		}
	}

	@AfterCompose(superclass = true)
	public void afterComposeConfiguracionControlBody() {
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
		// TODO Auto-generated method stub
		return new AssemblerUtil();
	}

	@Override
	public DTO getDTOCorriente() {
		// TODO Auto-generated method stub
		return this.dto;
	}

	@Override
	public void setDTOCorriente(DTO dto) {
		this.dto = (UtilDTO) dto;

	}

	@Override
	public DTO nuevoDTO() throws Exception {
		// TODO Auto-generated method stub
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

	

	// ============================ TABs ================================

	public boolean tab(String tab) {
		if (this.tab.compareTo("all") == 0) {
			return true;
		}
		return this.tab.contains(tab);

	}	

	@Override
	public boolean verificarAlGrabar() {
		// TODO Auto-generated method stub
		boolean res = true;

		for (MyArray ma : this.nuevosMA) {
			if (((String) ma.getPos1()).compareTo("") == 0
					|| ((String) ma.getPos1()).compareTo("--editar--") == 0) {
				res = false;
			}
		}
		for (MyPair mp : this.nuevosMP) {
			if (((String) mp.getText()).compareTo("") == 0
					|| ((String) mp.getText()).compareTo("--editar--") == 0) {
				res = false;
			}
		}
		return res;
	}

	@Override
	public String textoErrorVerificarGrabar() {
		// TODO Auto-generated method stub
		return " Verifique que la descripción de los elementos es correcta,\n no puede contener la plabra --editar-- ni ser vacía. ";
	}
}
