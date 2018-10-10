package qetaa.jsf.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private char status;

	@JsonIgnore
	private boolean vatIncluded;
	
	
	@JsonIgnore
	public ProductPrice getCloned() {
		ProductPrice pp = new ProductPrice();
		pp.setCostPrice(costPrice);
		pp.setCostPriceWv(costPriceWv);
		pp.setCreatedBy(createdBy);
		pp.setId(id);
		pp.setMakeId(makeId);
		pp.setProductId(productId);
		pp.setStatus(status);
		pp.setVatIncluded(vatIncluded);
		pp.setVendorId(vendorId);
		return pp;
	}
	
	
	
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public boolean isVatIncluded() {
		return vatIncluded;
	}
	public void setVatIncluded(boolean vatIncluded) {
		this.vatIncluded = vatIncluded;
	}
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
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductPrice other = (ProductPrice) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
