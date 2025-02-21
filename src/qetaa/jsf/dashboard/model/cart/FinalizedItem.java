package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FinalizedItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private long cartId;
	private long quotationId;
	private long quotationItemId;
	private int requestedQuantity;
	private char status;
	private String itemDesc;
	private List<FinalizedItemResponse> responses;
	
	@JsonIgnore
	public double getTotalSalesPrice() {
		double total = 0;
		for(FinalizedItemResponse response : responses) {
			total += response.getTotalSales();
		}
		return total;
	}
	
	@JsonIgnore
	public double getTotalCostPrice() {
		double total = 0;
		for(FinalizedItemResponse response : responses) {
			total += response.getTotalCost();
		}
		return total;
	}
	
	@JsonIgnore
	public int getTotalSelectedQuantity() {
		int total = 0;
		for(FinalizedItemResponse response : responses) {
			total += response.getSelectedQuantity();
		}
		return total;
	}
	
	
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public long getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(long quotationId) {
		this.quotationId = quotationId;
	}
	public long getQuotationItemId() {
		return quotationItemId;
	}
	public void setQuotationItemId(long quotationItemId) {
		this.quotationItemId = quotationItemId;
	}
	public int getRequestedQuantity() {
		return requestedQuantity;
	}
	public void setRequestedQuantity(int requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public List<FinalizedItemResponse> getResponses() {
		return responses;
	}
	public void setResponses(List<FinalizedItemResponse> responses) {
		this.responses = responses;
	}
	
	
	
}
