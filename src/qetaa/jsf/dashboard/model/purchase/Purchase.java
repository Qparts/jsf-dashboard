package qetaa.jsf.dashboard.model.purchase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.cart.Cart;

public class Purchase implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private int vendorId;
	private int makeId;
	private char transactionType;
	private char paymentStatus;
	private Date purchaseDate;
	private Date created;
	private Date completed;
	private int createdBy;
	private Integer completedBy;
	private Date dueDate;
	private long cartId;
	private long customerId;
	private List<PurchaseProduct> purchaseProducts;
	private List<PurchasePayment> purchasePayments;
	private List<PurchaseReturn> purchaseReturns;
	
	@JsonIgnore
	private Cart cart;
	
	
	@JsonIgnore
	public List<PurchaseReturnProduct> getPurchaseReturnProducts(){
		List<PurchaseReturnProduct> srps = new ArrayList<>();
		for(PurchaseReturn sr : purchaseReturns) {
			srps.addAll(sr.getPurchaseReturnProducts());
		}
		return srps;
	}
	
	@JsonIgnore
	public List<PurchasePayment> getReturnPayments(){
		List<PurchasePayment> pays = new ArrayList<>();
		if(this.purchaseReturns != null) {
			for(PurchaseReturn pr : this.purchaseReturns) {
				if(pr.getPurchasePayments() != null) {
					pays.addAll(pr.getPurchasePayments());
				}
			}
		}
		return pays;
	}
	
	@JsonIgnore
	public double getOutstanding() {
		return this.getTotalCostWv() - this.getPaid() + this.getReturned();
	}
	
	@JsonIgnore
	public double getReturned() {
		double total = 0;
		if(this.purchaseReturns != null) {
			for(PurchaseReturn pr : this.purchaseReturns) {
				if(pr.getPurchasePayments() != null) {
					for(PurchasePayment pp : pr.getPurchasePayments()) {
						total += pp.getAmount();
					}
				}
			}
		}
		return total;
	}

	@JsonIgnore
	public double getPaid() {
		double total = 0;
		if(this.purchasePayments != null) {
			for(PurchasePayment pp : this.purchasePayments) {
				total += pp.getAmount();
			}
		}
		return total;
	}
	
	
	public Date getCreated() {
		return created;
	}




	public void setCreated(Date created) {
		this.created = created;
	}




	@JsonIgnore
	public double getTotalCost() {
		double total = 0;
		if(purchaseProducts == null) {
			return 0;
		}
		for(PurchaseProduct pp : purchaseProducts) {
			if(pp.getUnitCost() != null)
				total = total + pp.getUnitCost() * pp.getQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getTotalReturnCost() {
		double total = 0;
		for(PurchaseReturnProduct pp : getPurchaseReturnProducts()) {
			total = total + pp.getUnitCost() * pp.getQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getTotalReturnCostWv() {
		double total = 0;
		for(PurchaseReturnProduct pp : getPurchaseReturnProducts()) {
			total = total + pp.getUnitCostWv() * pp.getQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getTotalCostWv() {
		double total = 0;
		if(purchaseProducts == null) {
			return 0;
		}
		for(PurchaseProduct pp : purchaseProducts) {
			if(pp.getUnitCostWv() != null)
				total = total + pp.getUnitCostWv() * pp.getQuantity();
		}
		return total;
	}
	
	
	@JsonIgnore
	public double getPartTotalCostWv(PurchaseProduct ppcheck, int quantity) {
		double total = 0;
		if(purchaseProducts == null) {
			return 0;
		}
		for(PurchaseProduct pp : purchaseProducts) {
			if(ppcheck.equals(pp)) {
				if(pp.getUnitCostWv() != null)
					total = total + pp.getUnitCostWv() * quantity;
			}
			
		}
		return total;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public char getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(char transactionType) {
		this.transactionType = transactionType;
	}
	public char getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(char paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public List<PurchaseProduct> getPurchaseProducts() {
		return purchaseProducts;
	}
	public void setPurchaseProducts(List<PurchaseProduct> purchaseProducts) {
		this.purchaseProducts = purchaseProducts;
	}
	public List<PurchasePayment> getPurchasePayments() {
		return purchasePayments;
	}
	public void setPurchasePayments(List<PurchasePayment> purchasePayments) {
		this.purchasePayments = purchasePayments;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	
	

	public int getMakeId() {
		return makeId;
	}

	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + createdBy;
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + paymentStatus;
		result = prime * result + ((purchaseDate == null) ? 0 : purchaseDate.hashCode());
		result = prime * result + ((purchasePayments == null) ? 0 : purchasePayments.hashCode());
		result = prime * result + ((purchaseProducts == null) ? 0 : purchaseProducts.hashCode());
		result = prime * result + transactionType;
		result = prime * result + vendorId;
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
		Purchase other = (Purchase) obj;
		if (cartId != other.cartId)
			return false;
		if (createdBy != other.createdBy)
			return false;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (id != other.id)
			return false;
		if (paymentStatus != other.paymentStatus)
			return false;
		if (purchaseDate == null) {
			if (other.purchaseDate != null)
				return false;
		} else if (!purchaseDate.equals(other.purchaseDate))
			return false;
		if (purchasePayments == null) {
			if (other.purchasePayments != null)
				return false;
		} else if (!purchasePayments.equals(other.purchasePayments))
			return false;
		if (purchaseProducts == null) {
			if (other.purchaseProducts != null)
				return false;
		} else if (!purchaseProducts.equals(other.purchaseProducts))
			return false;
		if (transactionType != other.transactionType)
			return false;
		if (vendorId != other.vendorId)
			return false;
		return true;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public List<PurchaseReturn> getPurchaseReturns() {
		return purchaseReturns;
	}

	public void setPurchaseReturns(List<PurchaseReturn> purchaseReturns) {
		this.purchaseReturns = purchaseReturns;
	}




	public Integer getCompletedBy() {
		return completedBy;
	}




	public void setCompletedBy(Integer completedBy) {
		this.completedBy = completedBy;
	}




	public Date getCompleted() {
		return completed;
	}




	public void setCompleted(Date completed) {
		this.completed = completed;
	}
	
	
	
	
	
	
	
	
}
