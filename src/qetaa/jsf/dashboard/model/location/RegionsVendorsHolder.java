package qetaa.jsf.dashboard.model.location;

import java.util.List;

import qetaa.jsf.dashboard.model.vendor.Vendor;

public class RegionsVendorsHolder {

	private Region region;
	private List<Vendor> vendors;
	private List<Vendor> unselected;
	
	
	
	public List<Vendor> getUnselected() {
		return unselected;
	}
	public void setUnselected(List<Vendor> unselected) {
		this.unselected = unselected;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public List<Vendor> getVendors() {
		return vendors;
	}
	public void setVendors(List<Vendor> vendors) {
		this.vendors = vendors;
	}
	
	
	
}
