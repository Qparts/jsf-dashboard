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

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.location.Region;

@Named(value="regionBean")
@ViewScoped
public class RegionsBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Region region;
	private Region selectedRegion;
	private List<Region> regions;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init(){
		this.region = new Region();
		region.setLatitude(24.80410747347977);
		region.setLongitude(48.122892999999976);
		region.setMapZoom(4);
		this.regions = new ArrayList<>();
		this.selectedRegion= new Region();
		initRegions();
	}
	
	public void resetMap(){
		region.setLatitude(region.getCountry().getLatitude());
		region.setLongitude(region.getCountry().getLongitude());
		region.setMapZoom(region.getCountry().getMapZoom());
	}
	
	public void onStateChange(StateChangeEvent event) {
        LatLng latlng = event.getCenter();
        int zoomLevel = event.getZoomLevel();
        this.region.setMapZoom(zoomLevel);
        this.region.setLatitude(latlng.getLat());
        this.region.setLongitude(latlng.getLng());
    }
	
	public void selectRegion(Region region){
		this.selectedRegion = region;
	}

	private void initRegions() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_REGIONS);
		if(r.getStatus() == 200){
			this.regions = r.readEntity(new GenericType<List<Region>>() {});
		}
	}
	
	public void createRegion(){
		Response r= reqs.postSecuredRequest(AppConstants.POST_CREATE_REGION, region);
		if(r.getStatus() == 200){
			init();
			Helper.addInfoMessage("Region created");
		}
		else{
			Helper.addErrorMessage("An error has occured");
		} 
	}

	public Region getRegion() {
		return region;
	}

	public List<Region> getRegions() {
		return regions;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setRegions(List<Region> cities) {
		this.regions = cities;
	}

	public Region  getSelectedRegion() {
		return selectedRegion;
	}

	public void setSelectedRegion(Region selectedRegion) {
		this.selectedRegion  = selectedRegion ;
	}	
	
	
	
}
