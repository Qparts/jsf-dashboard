package qetaa.jsf.dashboard.beans.operation;
 
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
import qetaa.jsf.dashboard.beans.master.CitiesBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.model.cart.ApprovedQuotationItem;
import qetaa.jsf.dashboard.model.cart.WireTransfer;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;
 
@Named
@ViewScoped
public class WireBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<WireTransfer> wires;
	private WireTransfer selectedWire;

	@Inject
	private Requester reqs;
	
	@Inject 
	private CitiesBean citiesBean;

	@PostConstruct
	private void init() {
		selectedWire = new WireTransfer();
		initWires();
	}
	
	private List<ModelYear> initAllModelYears() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_MODEL_YEARS);
		if(r.getStatus() == 200) {
			List<ModelYear> modelYears = r.readEntity(new GenericType<List<ModelYear>>() {});
			return modelYears;
		}
		else {
			return new ArrayList<>();
		}
	}

	private void initWires() {
		wires = new ArrayList<>();
		List<City> allCities = citiesBean.getCities();
		List<ModelYear> allModels = this.initAllModelYears();
		Response r = reqs.getSecuredRequest(AppConstants.GET_ACTIVE_WIRE_TRANSFERS);
		if (r.getStatus() == 200) {
			wires = r.readEntity(new GenericType<List<WireTransfer>>() {
			});
			for(WireTransfer w : wires) {
				Response r2 = reqs.getSecuredRequest(AppConstants.getCustomerPartsApprovedITems(w.getCartId()));
				if(r2.getStatus() == 200) {
					List<ApprovedQuotationItem> approved = r2.readEntity(new GenericType<List<ApprovedQuotationItem>>() {});
					w.getCart().setApprovedItems(approved);
				}
				if(null != w.getCart().getPromotionCode()) {
					Response r3 = reqs.getSecuredRequest(AppConstants.getPromoCode(w.getCart().getPromotionCode()));
					if(r3.getStatus() == 200) {
						w.getCart().setPromoCodeObject(r3.readEntity(PromotionCode.class));
					}
				}
				for(City city : allCities) {
					if(city.getId() == w.getCart().getCityId()) {
						w.getCart().setCity(city);
						break;
					}
				}
				for(ModelYear my : allModels) {
					if(my.getId() == w.getCart().getVehicleYear()) {
						w.getCart().setModelYear(my);
					}
				}
			}
		}

	}
	
	


	public List<WireTransfer> getWires() {
		return wires;
	}

	public WireTransfer getSelectedWire() {
		return selectedWire;
	}

	public void setSelectedWire(WireTransfer selectedWire) {
		this.selectedWire = selectedWire;
	}

	public void setWires(List<WireTransfer> wires) {
		this.wires = wires;
	}

}
