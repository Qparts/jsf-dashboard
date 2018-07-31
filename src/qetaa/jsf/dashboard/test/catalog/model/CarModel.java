package qetaa.jsf.dashboard.test.catalog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CarModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private String title;
	private String catalogId;
	private String carId;
	private String criteria;
	private List<ModelParameter> parameters;	
	@JsonIgnore
	private List<CatalogGroup> catalogGroups;
	
	
	public CarModel() {
		catalogGroups = new ArrayList<>();
	}

	
	
	
	

	
	
	public List<CatalogGroup> getCatalogGroups() {
		return catalogGroups;
	}








	public void setCatalogGroups(List<CatalogGroup> catalogGroups) {
		this.catalogGroups = catalogGroups;
	}








	public List<ModelParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<ModelParameter> parameters) {
		this.parameters = parameters;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	
}
