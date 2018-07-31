package qetaa.jsf.dashboard.beans.product;

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
import qetaa.jsf.dashboard.model.product.ProductName;
import qetaa.jsf.dashboard.model.product.ProductNameAlternative;

@Named
@ViewScoped
public class ProductNameBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ProductName productName;
	private int altIndex;
	private String searchProductName;
	private List<ProductName> productNames;
	private ProductName selectedProductName;

	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		productNames = new ArrayList<>();
		productName = new ProductName();
		selectedProductName = new ProductName();
		productName.setAltProductNames(new ArrayList<>());
		altIndex = 0;
		searchProductName = "";
	}
	
	public void searchProducts() {
		productNames = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.searchProductsName(this.searchProductName));
		if(r.getStatus() == 200) {
			productNames = r.readEntity(new GenericType<List<ProductName>>() {});
			if(productNames.isEmpty()) {
				Helper.addErrorMessage("No Products Names Found");
			}
		}else {
			Helper.addErrorMessage("No Products Found");
		}
	}
	
	public void addAlternativeName() {
		ProductNameAlternative pna = new ProductNameAlternative();
		pna.setId(altIndex);
		productName.getAltProductNames().add(pna);
		altIndex++;
	}
	
	public void removeAlternativeName(ProductNameAlternative alt) {
		productName.getAltProductNames().remove(alt);
	}
	
	public void createProductName() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_PRODUCT_NAME, productName);
		if(r.getStatus() == 201) {
			init();
			Helper.addInfoMessage("Product Name added");
		}
		else if(r.getStatus() == 409) {
			Helper.addErrorMessage("Product Name exists");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public ProductName getProductName() {
		return productName;
	}

	public void setProductName(ProductName productName) {
		this.productName = productName;
	}

	public String getSearchProductName() {
		return searchProductName;
	}

	public void setSearchProductName(String searchProductName) {
		this.searchProductName = searchProductName;
	}

	public List<ProductName> getProductNames() {
		return productNames;
	}

	public void setProductNames(List<ProductName> productNames) {
		this.productNames = productNames;
	}

	public ProductName getSelectedProductName() {
		return selectedProductName;
	}

	public void setSelectedProductName(ProductName selectedProductName) {
		this.selectedProductName = selectedProductName;
	}
	

	
}
