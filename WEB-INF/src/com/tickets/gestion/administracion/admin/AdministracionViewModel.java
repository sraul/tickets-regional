package com.tickets.gestion.administracion.admin;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;

import com.coreweb.control.SimpleViewModel;
import com.coreweb.domain.AutoNumero;
import com.tickets.domain.RegisterDomain;
import com.tickets.domain.Tope;

public class AdministracionViewModel extends SimpleViewModel {
	
	private int selectedDay = 1;

	@Init(superclass = true)
	public void init() {
	}
	
	@AfterCompose(superclass = true)
	public void afterCompose() {
	}
	
	@Command
	@NotifyChange("selectedDay")
	public void selectDay(@BindingParam("day") int day) {
		this.selectedDay = day;
	}
	
	@Command
	public void edit(@BindingParam("tope") Tope tope) {
		tope.setEdit(true);
		BindUtils.postNotifyChange(null, null, tope, "*");
	}
	
	@Command
	public void save(@BindingParam("tope") Tope tope) throws Exception {
		tope.setEdit(false);
		RegisterDomain rr = RegisterDomain.getInstance();
		rr.saveObject(tope, this.getLoginNombre());
		
		String keyTM = tope.getDia() + "-" + tope.getServicio() + "-TM";
		AutoNumero auTM = rr.getAutoNumero(keyTM);
		if (auTM == null) {
			auTM = new AutoNumero();
		}
		auTM.setKey(keyTM);
		auTM.setNumero(tope.getTM() - 1);
		rr.saveObject(auTM, this.getLoginNombre());
		
		String keyTT = tope.getDia() + "-" + tope.getServicio() + "-TT";
		AutoNumero auTT = rr.getAutoNumero(keyTT);
		if (auTT == null) {
			auTT = new AutoNumero();
		}
		auTT.setKey(keyTT);
		auTT.setNumero(tope.getTT() - 1);
		rr.saveObject(auTT, this.getLoginNombre());
		
		Clients.showNotification("Registro Guardado..");
		BindUtils.postNotifyChange(null, null, tope, "*");
	}
	
	/**
	 * @return los topes..
	 */
	public List<Tope> getTopes(int nroDia) throws Exception {
		RegisterDomain rr = RegisterDomain.getInstance();
		return rr.getTopes(nroDia);
	}

	public int getSelectedDay() {
		return selectedDay;
	}

	public void setSelectedDay(int selectedDay) {
		this.selectedDay = selectedDay;
	}
}
