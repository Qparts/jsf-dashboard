package qetaa.jsf.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String productNumber;
	private String productNumberUndecorated;
	private ProductName productName;
	private String desc;
	private Date created;
	private int makeId;
	private List<ProductPrice> priceList;
	private List<ProductStock> stockList;
	private Integer manufacturerId;
	private Integer categoryId;
	
	@JsonIgnore
	private ProductPrice selectedPrice;
	@JsonIgnore
	private long selectedPriceId;
	@JsonIgnore
	private ProductPrice newPrice = new ProductPrice();
	
	@JsonIgnore
	public String getProductNameCheck() {
		String s = "";
		if(this.getProductName() != null) {
			s = this.productName.getName() + " " + this.productName.getNameAr();
		}
		return s;
	}
	
	@JsonIgnore
	public double getBestPrice() {
		if(priceList.isEmpty()) {
			return 0;
		}
		double price = priceList.get(0).getCostPrice();
		
		for(ProductPrice pp : priceList) {
			if(pp.getCostPrice() < price) {
				price = pp.getCostPrice();
			}
		}
		return price;
	}
	
	@JsonIgnore
	public ProductPrice getBestProductPrice() {
		if(priceList.isEmpty()) {
			return null;
		}
		ProductPrice pprice = priceList.get(0);
		
		for(ProductPrice pp : priceList) {
			if(pp.getCostPrice() < pprice.getCostPrice()) {
				pprice = pp;
			}
		}
		return pprice;
	}
	
	
	@JsonIgnore
	public void resetSelectedPriceListFromSelectedId() {
		for(ProductPrice pl : this.priceList) {
			if(pl.getId() == this.selectedPriceId) {
				this.selectedPrice = pl;
			}
		}
	}
	
	@JsonIgnore
	public int getStockQuantity() {
		if(stockList == null) {
			return 0;
		}
		if(stockList.isEmpty()) {
			return 0;
		}
		int q = 0;
		for(ProductStock stock : stockList) {
			q = q + stock.getQuantity();
		}
		return q;
	}
	
	@JsonIgnore
	public Product getClonedProduct() {
		Product p = new Product();
		p.setCreated(created);
		p.setManufacturerId(manufacturerId);
		p.setCategoryId(categoryId);
		p.setDesc(desc);
		p.setId(id);
		p.setMakeId(makeId);
		p.setProductName(productName);
		p.setProductNumber(productNumber);
		p.setProductNumberUndecorated(productNumberUndecorated);
		p.setSelectedPriceId(selectedPriceId);
		p.setStockList(stockList);
		return p;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public ProductName getProductName() {
		return productName;
	}
	public void setProductName(ProductName productName) {
		this.productName = productName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getMakeId() {
		return makeId;
	}
	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getProductNumberUndecorated() {
		return productNumberUndecorated;
	}
	public void setProductNumberUndecorated(String productNumberUndecorated) {
		this.productNumberUndecorated = productNumberUndecorated;
	}
	
	public List<ProductStock> getStockList() {
		return stockList;
	}
	public void setStockList(List<ProductStock> stockList) {
		this.stockList = stockList;
	}

	public List<ProductPrice> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<ProductPrice> priceList) {
		this.priceList = priceList;
	}
	
	public boolean isPriceAvailable() {
		return !(this.priceList == null || this.priceList.isEmpty());
	}

	public ProductPrice getSelectedPrice() {
		if(selectedPrice == null) {
			selectedPrice = this.getBestProductPrice();
			selectedPriceId = selectedPrice.getId();
		}
		return selectedPrice;
	}

	public void setSelectedPrice(ProductPrice slectedPrice) {
		this.selectedPrice = slectedPrice;
	}

	public long getSelectedPriceId() {
		return selectedPriceId;
	}

	public void setSelectedPriceId(long selectedPriceId) {
		this.selectedPriceId = selectedPriceId;
	}

	public ProductPrice getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(ProductPrice newPrice) {
		this.newPrice = newPrice;
	}


	
	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
}
