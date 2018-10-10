package qetaa.jsf.dashboard.beans.product;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.product.Manufacturer;

@Named
@ViewScoped
public class ProductManufacturerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Manufacturer manufacturer;
	private List<Manufacturer> manufacturers;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		manufacturers = new ArrayList<>();
		this.manufacturer = new Manufacturer();
		initCategories();
	}

	public void createManufacturer() {
		try {
			manufacturer.setName(manufacturer.getName().trim());
			manufacturer.setNameAr(manufacturer.getNameAr().trim());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("manufacturer", manufacturer);
			String main64 = Helper.inputStreamToBase64(manufacturer.getMain().getInputStream());
			map.put("main", main64);
			map.put("thumbnail", Helper.inputStreamToBase64(manufacturer.getThumbnail().getInputStream()));
			Response r = reqs.postSecuredRequest(AppConstants.POST_PRODUCT_MANUFACTURER, map);
			if (r.getStatus() == 201) {
				Helper.addInfoMessage("Category created");
				init();
			}
		} catch (IOException e) {
			Helper.addErrorMessage("An error occured");
		}
	}

	private void initCategories() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_PRODUCT_MANUFACTURERS);
		if (r.getStatus() == 200) {
			this.manufacturers = r.readEntity(new GenericType<List<Manufacturer>>() {
			});
		}
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(List<Manufacturer> manufacturers) {
		this.manufacturers = manufacturers;
	}

}
