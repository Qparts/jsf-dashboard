package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartsOrderItem implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long quotationId;
	private long partsOrderId;
	private long quotationItemId;
	private int orderedQuantity;
	private char status;
	private double salesPrice;
	private Long productId;
	@JsonIgnore
	private int newQuantity;
	@JsonIgnore
	private String itemDesc;
	
	
	@JsonIgnore
	public int[] getQuantityArray() {
		int[] quantityArray = new int[orderedQuantity+1];
		for (int i = 0; i <= orderedQuantity; i++) {
			quantityArray[i] = i;
		}
		return quantityArray;
	}
	
	@JsonIgnore
	public double getNewQuantityTotalPrice() {
		return salesPrice * newQuantity;
	}
	
	@JsonIgnore
	public double getTotalPrice() {
		return salesPrice * this.orderedQuantity;
	}
	
	
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(long quotationId) {
		this.quotationId = quotationId;
	}
	public long getPartsOrderId() {
		return partsOrderId;
	}
	public void setPartsOrderId(long partsOrderId) {
		this.partsOrderId = partsOrderId;
	}
	public long getQuotationItemId() {
		return quotationItemId;
	}
	public void setQuotationItemId(long quotationItemId) {
		this.quotationItemId = quotationItemId;
	}
	public int getOrderedQuantity() {
		return orderedQuantity;
	}
	public void setOrderedQuantity(int orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + orderedQuantity;
		result = prime * result + (int) (partsOrderId ^ (partsOrderId >>> 32));
		result = prime * result + (int) (quotationId ^ (quotationId >>> 32));
		result = prime * result + (int) (quotationItemId ^ (quotationItemId >>> 32));
		long temp;
		temp = Double.doubleToLongBits(salesPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + status;
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
		PartsOrderItem other = (PartsOrderItem) obj;
		if (id != other.id)
			return false;
		if (orderedQuantity != other.orderedQuantity)
			return false;
		if (partsOrderId != other.partsOrderId)
			return false;
		if (quotationId != other.quotationId)
			return false;
		if (quotationItemId != other.quotationItemId)
			return false;
		if (Double.doubleToLongBits(salesPrice) != Double.doubleToLongBits(other.salesPrice))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	public int getNewQuantity() {
		return newQuantity;
	}

	public void setNewQuantity(int newQuantity) {
		this.newQuantity = newQuantity;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	
	
	
}
