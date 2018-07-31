package qetaa.jsf.dashboard.beans.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
import qetaa.jsf.dashboard.model.location.Country;


@Named(value="countryBean")
@ViewScoped
public class CountriesBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Country country;
	private Country selectedCountry;
	private List<Country> countries;

	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init(){
		this.country = new Country();
		country.setLatitude(24.80410747347977);
		country.setLongitude(48.122892999999976);
		country.setMapZoom(4);
		this.countries = new ArrayList<>();
		this.selectedCountry = new Country();
		initCountries();
	}
	
	public void onStateChange(StateChangeEvent event) {
        LatLng latlng = event.getCenter();
        int zoomLevel = event.getZoomLevel();
        this.country.setMapZoom(zoomLevel);
        this.country.setLatitude(latlng.getLat());
        this.country.setLongitude(latlng.getLng());
    }
	
	public void selectCountry(Country country){
		this.selectedCountry = country;
	}
    
    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	private void initCountries() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_COUNTRIES);
		if(r.getStatus() == 200){
			this.countries = r.readEntity(new GenericType<List<Country>>() {});
		}
	}
	
	public Country getCountry(int countryId) {
		for(Country c : countries) {
			if(c.getId() == countryId) {
				return c;
			}
		}
		return countries.get(0);
	}
	
	public void createCountry(){
		Response r= reqs.postSecuredRequest(AppConstants.POST_CREATE_COUNTRY, country);
		if(r.getStatus() == 200){
			init();
			Helper.addInfoMessage("Country created");
		}
		else{
			Helper.addErrorMessage("An error has occured");
		}
	}

	public Country getCountry() {
		return country;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}
	
	
	
}
