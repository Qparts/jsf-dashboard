package qetaa.jsf.dashboard.beans.shipments;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.shipment.Courier;
 
@Named
@ViewScoped
public class CourierBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Courier courier; 
	private List<Courier> couriers;
	private List<Courier> activeCouriers;
	@Inject
	private Requester reqs;
	
	
	@PostConstruct
	private void init() {
		courier = new Courier();
		initCouriers();
		initActiveCouriers();
	}
	
	private void initCouriers() {
		couriers = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_COURIERS);
		if(r.getStatus() == 200) {
			this.couriers = r.readEntity(new GenericType<List<Courier>>() {});
		}
	}
	
	public Courier getCourierFromId(int courrierId) {
		for(Courier b : couriers) {
			if(b.getId() == courrierId)
				return b;
		}
		return null;
	}
	
	private void initActiveCouriers() {
		this.activeCouriers = new ArrayList<>();
		for(Courier b : couriers) {
			if(b.getInternalStatus() == 'A') {
				this.activeCouriers.add(b);
			}
		}
	}
	
	
	public void createCourier() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_COURIER, courier);
		if(r.getStatus() == 201) {
			init();
			Helper.addInfoMessage("Courier created");
		}
		else {
			Helper.addErrorMessage("Something went wrong");
		}
	}

	public Courier getCourier() {
		return courier;
	}

	public List<Courier> getCouriers() {
		return couriers;
	}

	public List<Courier> getActiveCouriers() {
		return activeCouriers;
	}

}
