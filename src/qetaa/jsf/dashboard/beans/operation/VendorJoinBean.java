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
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.vendor.VendorJoinRequest;

@Named
@ViewScoped
public class VendorJoinBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<VendorJoinRequest> vendorJoins;
	private VendorJoinRequest selectedVendorJoin;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		vendorJoins = new ArrayList<>();
		selectedVendorJoin = new VendorJoinRequest();
		initVendorJoins();
	}
	
	private void initVendorJoins() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_VENDOR_JOIN_REQUESTS);
		if(r.getStatus() == 200) {
			vendorJoins = r.readEntity(new GenericType<List<VendorJoinRequest>>() {});
		}
	}
	
	public void archiveVendorJoin() {
		Response r = reqs.deleteSecuredRequest(AppConstants.deleteVendorJoinRequest(this.selectedVendorJoin.getId()));
		if(r.getStatus() == 201) {
			init();
		}
		else {
			Helper.addErrorMessage("Could not archive");
		}
	}
	
	

	public List<VendorJoinRequest> getVendorJoins() {
		return vendorJoins;
	}

	public void setVendorJoins(List<VendorJoinRequest> vendorJoins) {
		this.vendorJoins = vendorJoins;
	}

	public VendorJoinRequest getSelectedVendorJoin() {
		return selectedVendorJoin;
	}

	public void setSelectedVendorJoin(VendorJoinRequest selectedVendorJoin) {
		this.selectedVendorJoin = selectedVendorJoin;
	}

	public Requester getReqs() {
		return reqs;
	}

	public void setReqs(Requester reqs) {
		this.reqs = reqs;
	}
	
	
	
	

}
