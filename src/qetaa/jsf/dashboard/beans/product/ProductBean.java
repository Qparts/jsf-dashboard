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
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.product.ProductName;

@Named
@ViewScoped
public class ProductBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Product product;
	private Product selectedProduct;
	private String searchName;
	private String searchNumber;
	private int searchMakeId;
	private long updateProductName;
	private List<Product> products;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		searchName = "";
		searchNumber = "";
		searchMakeId = 0;
		product = new Product();
		products = new ArrayList<>();
		selectedProduct = new Product();
	}

	public void searchProduct() {
			products = new ArrayList<>();
			if (searchName.trim().equals("")) {
				searchName = " ";
			}
			if (searchNumber.trim().equals("")) {
				searchNumber = " ";
			}
			Response r = reqs.getSecuredRequest(AppConstants.searchProducts(searchName, searchNumber, searchMakeId));
			if(r.getStatus() == 200) {
				products = r.readEntity(new GenericType<List<Product>>() {});
			}
		
	}
	
	public void updateProduct() {
		this.selectedProduct.setProductName(new ProductName());
		this.selectedProduct.getProductName().setId(this.updateProductName);
		Response r = reqs.putSecuredRequest(AppConstants.putUpdateProductAdvanced(), this.selectedProduct);
		if(r.getStatus() == 201) {
			init();
			Helper.addInfoMessage("Product name updated");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void createProduct() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_PRODUCT, product);
		if (r.getStatus() == 201) {
			init();
			Helper.addInfoMessage("Product Created");
		} else if (r.getStatus() == 409) {
			Helper.addErrorMessage("Product already exists");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void selectProductName(ProductName pname) {
		this.product.setProductName(pname);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getSearchNumber() {
		return searchNumber;
	}

	public void setSearchNumber(String searchNumber) {
		this.searchNumber = searchNumber;
	}

	public int getSearchMakeId() {
		return searchMakeId;
	}

	public void setSearchMakeId(int searchMakeId) {
		this.searchMakeId = searchMakeId;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public long getUpdateProductName() {
		return updateProductName;
	}

	public void setUpdateProductName(long updateProductName) {
		this.updateProductName = updateProductName;
	}
	
	
	

}
