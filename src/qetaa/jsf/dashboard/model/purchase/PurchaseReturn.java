package qetaa.jsf.dashboard.model.purchase;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PurchaseReturn implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private Purchase purchase;
	private Date returnDate;
	private char method;
	private char transactionType;
	private int returnedBy;
	private long cartId;
	private List<PurchaseReturnProduct> purchaseReturnProducts;
	private List<PurchasePayment> purchasePayments;
	
	@JsonIgnore
	public double getTotalPurchaseReturnCost() {
		double total = 0;
		if(this.purchaseReturnProducts == null || this.purchaseReturnProducts.isEmpty()) {
			return 0;
		}
		for(PurchaseReturnProduct prp : this.purchaseReturnProducts) {
			total = total + prp.getUnitCost() * prp.getQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getTotalPurchaseReturnCostWv() {
		double total = 0;
		if(this.purchaseReturnProducts == null || this.purchaseReturnProducts.isEmpty()) {
			return 0;
		}
		for(PurchaseReturnProduct prp : this.purchaseReturnProducts) {
			total = total + prp.getUnitCostWv() * prp.getQuantity();
		}
		return total;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Purchase getPurchase() {
		return purchase;
	}
	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public char getMethod() {
		return method;
	}
	public void setMethod(char method) {
		this.method = method;
	}
	public int getReturnedBy() {
		return returnedBy;
	}
	public void setReturnedBy(int returnedBy) {
		this.returnedBy = returnedBy;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public List<PurchaseReturnProduct> getPurchaseReturnProducts() {
		return purchaseReturnProducts;
	}
	public void setPurchaseReturnProducts(List<PurchaseReturnProduct> purchaseReturnProducts) {
		this.purchaseReturnProducts = purchaseReturnProducts;
	}

	public char getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(char transactionType) {
		this.transactionType = transactionType;
	}

	public List<PurchasePayment> getPurchasePayments() {
		return purchasePayments;
	}

	public void setPurchasePayments(List<PurchasePayment> purchasePayments) {
		this.purchasePayments = purchasePayments;
	}

	
}
