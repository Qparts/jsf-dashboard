package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApprovedQuotationItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long cartId;
	private String itemDesc;
	private String itemNumber;
	private int quantity;
	private long quotationItemId;
	private double unitSales;
	
	@JsonIgnore
	public double getTotalSales() {
		return unitSales * quantity;
	}
	
	
	
	public String getItemNumber() {
		return itemNumber;
	}



	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}



	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public long getQuotationItemId() {
		return quotationItemId;
	}
	public void setQuotationItemId(long quotationItemId) {
		this.quotationItemId = quotationItemId;
	}
	public double getUnitSales() {
		return unitSales;
	}
	public void setUnitSales(double unitSales) {
		this.unitSales = unitSales;
	}
	
	
	
	
	
}
