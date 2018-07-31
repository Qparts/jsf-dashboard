package qetaa.jsf.dashboard.test.catalog;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.test.catalog.model.CarModel;
import qetaa.jsf.dashboard.test.catalog.model.CatalogGroup;
import qetaa.jsf.dashboard.test.catalog.model.Part;
import qetaa.jsf.dashboard.test.catalog.model.PartList;

@Named
@SessionScoped
public class CatalogBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String BASE = "https://api.parts-catalogs.com/v1/";
	private String vin = "KMHCT41B0GU987600";
	private String catalogId = "hyundai";
	private int makeId = 4;
	private CarModel carModel;
	private CatalogGroup selectedGroup;
	private CatalogGroup selectedSubGroup;
	
	@Inject
	private CatRequester catReqs;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		carModel = new CarModel();
	}
	
	public void loadSubGroup(CatalogGroup pg) {
		if(!pg.isHasParts()) {
			selectedGroup = pg;
			String link = BASE + "catalogs/" + catalogId + "/groups2?carId=" + carModel.getCarId() + "&groupId=" + pg.getId();
			Response r = catReqs.getSecuredRequest(link);
			if(r.getStatus() == 200) {
				List<CatalogGroup> pgs = r.readEntity(new GenericType<List<CatalogGroup>>() {});
				selectedGroup.setSubGroups(pgs);
			}
		}
	}
	
	public void loadParts(CatalogGroup pg) {
		if(pg.isHasParts()) {
			selectedSubGroup = pg;
			String link = BASE + "catalogs/" + catalogId + "/parts2/?carId=" + carModel.getCarId() + "&groupId=" + pg.getId();
			Response r = catReqs.getSecuredRequest(link);
			if(r.getStatus() == 200) {
				PartList plist = r.readEntity(PartList.class);
				selectedSubGroup.setPartList(plist);
				initProducts();
			}
		}
	}
	
	
	public void initProducts() {
		for(Part part : selectedSubGroup.getPartList().getPartGroups().get(0).getParts()) {
			Response r = reqs.getSecuredRequest(AppConstants.findProduct(part.getNumber(), this.makeId));
			System.out.println(r.getStatus());
			if(r.getStatus() == 200) {
				Product p = r.readEntity(Product.class);
				part.setProduct(p);
			}
		}
	}
	
	public void findGroups() {
		String link = BASE + "catalogs/" + catalogId + "/groups2?carId=" + carModel.getCarId();
		Response r = catReqs.getSecuredRequest(link);
		if(r.getStatus() == 200) {
			List<CatalogGroup> pgs = r.readEntity(new GenericType<List<CatalogGroup>>() {});
			carModel.setCatalogGroups(pgs);
		}
	}
	
	public void findVehicle() {
		String link = BASE + "catalogs/" + catalogId + "/cars-by-vin/?vin=" + vin;
		Response r = catReqs.getSecuredRequest(link);
		if(r.getStatus() == 200) {
			List<CarModel> carModels = r.readEntity(new GenericType<List<CarModel>>() {});
			if(!carModels.isEmpty()) {
				carModel = carModels.get(0);
				findGroups();
			}
		}
		else if(r.getStatus() == 400) {
			Helper.addErrorMessage("Vin Not Found");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public CarModel getCarModel() {
		return carModel;
	}

	public void setCarModel(CarModel carModel) {
		this.carModel = carModel;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public CatalogGroup getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(CatalogGroup selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public CatalogGroup getSelectedSubGroup() {
		return selectedSubGroup;
	}

	public void setSelectedSubGroup(CatalogGroup selectedSubGroup) {
		this.selectedSubGroup = selectedSubGroup;
	}
	
	
	
	
	
	

}
