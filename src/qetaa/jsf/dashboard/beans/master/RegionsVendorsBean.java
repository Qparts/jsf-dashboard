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
import qetaa.jsf.dashboard.model.location.Region;
import qetaa.jsf.dashboard.model.location.RegionsVendorsHolder;
import qetaa.jsf.dashboard.model.vendor.VendorHolder;
import qetaa.jsf.dashboard.model.vendor.VendorRegion;

@Named
@ViewScoped
public class RegionsVendorsBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<RegionsVendorsHolder> regionsVendors;
	private RegionsVendorsHolder selectedHolder;
	private int vendorId;
	
	@Inject
	private Requester reqs;
	
	@Inject 
	private VendorBean vendorBean;
	
	@Inject
	private LoginBean loginBean;
	
	@PostConstruct
	private void init() {
		initRegions();
		initRegionVendors();
		initUnselectedVendors();
	}
	
	public void addNewVendor() {
		VendorRegion vr = new VendorRegion();
		vr.setCreatedBy(loginBean.getUserHolder().getUser().getId());
		vr.setRegionId(this.selectedHolder.getRegion().getId());
		vr.setVendor(vendorBean.getVendorFromId(vendorId));
		Response r = reqs.postSecuredRequest(AppConstants.POST_VENDOR_REGION, vr);
		if(r.getStatus() == 201) {
			Helper.redirect("regions-vendors");
		}
		else if(r.getStatus() == 409) {
			Helper.addErrorMessage("vendor already added for this region");
		}
		else {
			Helper.addErrorMessage("an error occured");
		}
	}
	
	
	private void initUnselectedVendors() {
		for(RegionsVendorsHolder rvh : this.regionsVendors) {
			for(VendorHolder vh : vendorBean.getVendorHolders()) {
				if(!rvh.getVendors().contains(vh.getVendor())) {
					rvh.getUnselected().add(vh.getVendor());
				}
			}
		}
	}
	
	private void initRegionVendors() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_VENDORS_REGIONS);
		if(r.getStatus() == 200) {
			List<VendorRegion> vendorRegions = r.readEntity(new GenericType<List<VendorRegion>>() {});
			for(RegionsVendorsHolder rvh : this.regionsVendors) {
				for(VendorRegion vr : vendorRegions) {
					if(rvh.getRegion().getId() == vr.getRegionId()) {
						rvh.getVendors().add(vr.getVendor());
					}
				}
			}
		}
	}
	
	private void initRegions() {
		regionsVendors = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.getCountryRegions(1));
		if(r.getStatus() == 200) {
			List<Region> regions = r.readEntity(new GenericType<List<Region>>() {});
			for(Region region : regions) {
				RegionsVendorsHolder holder = new RegionsVendorsHolder();
				holder.setRegion(region);
				holder.setVendors(new ArrayList<>());
				holder.setUnselected(new ArrayList<>());
				this.regionsVendors.add(holder);
			}
		}
	}

	public List<RegionsVendorsHolder> getRegionsVendors() {
		return regionsVendors;
	}

	public void setRegionsVendors(List<RegionsVendorsHolder> regionsVendors) {
		this.regionsVendors = regionsVendors;
	}

	public Requester getReqs() {
		return reqs;
	}

	public void setReqs(Requester reqs) {
		this.reqs = reqs;
	}

	public RegionsVendorsHolder getSelectedHolder() {
		return selectedHolder;
	}

	public void setSelectedHolder(RegionsVendorsHolder selectedHolder) {
		this.selectedHolder = selectedHolder;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	
	
	
}
