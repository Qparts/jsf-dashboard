package qetaa.jsf.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;

public class ProductPrice implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long id;
	private long productId;
	private int makeId;
	private int vendorId;
	private double costPrice;
	private Date created;
	private int createdBy;
	private double costPriceWv;
	
	
	
	
	
	
	public double getCostPriceWv() {
		return costPriceWv;
	}
	public void setCostPriceWv(double costPriceWv) {
		this.costPriceWv = costPriceWv;
	}
	public int getMakeId() {
		return makeId;
	}
	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	
}
