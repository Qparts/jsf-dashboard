package qetaa.jsf.dashboard.beans.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.vehicle.Make;
import qetaa.jsf.dashboard.model.vendor.Vendor;
import qetaa.jsf.dashboard.model.vendor.VendorHolder;
import qetaa.jsf.dashboard.model.vendor.VendorMake;

@Named
@ViewScoped
public class VendorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private VendorHolder vendorHolder;
	private List<VendorHolder> vendorHolders;
	private VendorHolder selectedVendorHolder;
	private List<Make> unselectedMakes;
	private VendorMake vendorMake;
	
	@Inject
	private Requester reqs;
	
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() { 
		vendorMake = new VendorMake();
		unselectedMakes = new ArrayList<>();
		vendorHolder = new VendorHolder();
		vendorHolders = new ArrayList<>();
		selectedVendorHolder = new VendorHolder();
		initVendors();
	}

	private void initVendors() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_VENDORS);
		if (r.getStatus() == 200) {
			this.vendorHolders = r.readEntity(new GenericType<List<VendorHolder>>() {
			});
		} else {
			
		}
	}

	public void updateVendor() {
		selectedVendorHolder.getVendor().getName().trim();
		selectedVendorHolder.getVendor().getName().trim();
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_VENDOR, this.selectedVendorHolder);
		if (r.getStatus() == 200) {
			init();
			Helper.addInfoMessage("Vendor updated!");
		} else {
			Helper.addErrorMessage("An erro occured!");
		}
	}
	
	public Vendor getVendorFromId(int id) {
		for(VendorHolder holder : this.vendorHolders) {
			if(id == holder.getVendor().getId()) {
				return holder.getVendor();
			}
		}
		return null;
	}
	
	public List<Vendor> getMakeVendors(int makeId) {
		List<Vendor> vendors= new ArrayList<>();
		for(VendorHolder holder : this.vendorHolders) {
			for(VendorMake vm : holder.getVendorMakes()) {
				if(vm.getMakeId() == makeId) {
					vendors.add(holder.getVendor());
					break;
				}
			}
		}
		return vendors;
	}
	
	public void initUnselectedVehicleMakes(){
		unselectedMakes = new ArrayList<>();
		if(this.selectedVendorHolder.getVendor() != null && this.selectedVendorHolder.getVendor().getId() != 0){
			Response r = reqs.getSecuredRequest(AppConstants.getUnselectedMakes(this.selectedVendorHolder.getVendor().getId()));
			if(r.getStatus() == 200){
				this.unselectedMakes = r.readEntity(new GenericType<List<Make>>(){});
			}
		}
	}
	
	public void addVendorMake(){
		this.vendorMake.setVendor(selectedVendorHolder.getVendor());
		this.vendorMake.setCreatedBy(loginBean.getUserHolder().getUser().getId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_VENDOR_MAKE, vendorMake);
		if(r.getStatus() == 200){
			init();
		}
		else{
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public void removeVendorMake(VendorMake vm){
		Response r = reqs.deleteSecuredRequest(AppConstants.deleteVendorMake(vm.getVendor().getId(), vm.getMakeId()));
		if(r.getStatus() == 200){
			init();
		}
		else{
			Helper.addErrorMessage("An error occured");
		}
	}
	

	public void createVendor() {
		vendorHolder.getVendor().getName().trim();
		vendorHolder.getVendor().getNameAr().trim();
		vendorHolder.getVendor().setCreatedBy(loginBean.getUserHolder().getUser().getId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_VENDOR, vendorHolder);
		if (r.getStatus() == 200) {
			init();
			Helper.addInfoMessage("Vendor created");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	
	public double getPercentage(int vendorId, int makeId) {
		VendorHolder vh = null;
		for (VendorHolder vholder : getVendorHolders()) {
			if (vholder.getVendor().getId() == vendorId) {
				vh = vholder;
			}
		}

		if (vh != null) {
			for (VendorMake vm : vh.getVendorMakes()) {
				if (vm.getMakeId() == makeId) {
					return vm.getPercentage();
				}
			}
		}
		return .05;

	}

	public VendorHolder getVendorHolder() {
		return vendorHolder;
	}

	public void setVendorHolder(VendorHolder vendorHolder) {
		this.vendorHolder = vendorHolder;
	}

	public List<VendorHolder> getVendorHolders() {
		return vendorHolders;
	}

	public void setVendorHolders(List<VendorHolder> vendorHolders) {
		this.vendorHolders = vendorHolders;
	}
	
	public VendorHolder getSelectedVendorHolder() {
		return selectedVendorHolder;
	}

	public void setSelectedVendorHolder(VendorHolder selectedVendorHolder) {
		this.selectedVendorHolder = selectedVendorHolder;
	}

	public List<Make> getUnselectedMakes() {
		return unselectedMakes;
	}

	public void setUnselectedMakes(List<Make> unselectedMakes) {
		this.unselectedMakes = unselectedMakes;
	}

	public VendorMake getVendorMake() {
		return vendorMake;
	}

	public void setVendorMake(VendorMake vendorMake) {
		this.vendorMake = vendorMake;
	}

}
