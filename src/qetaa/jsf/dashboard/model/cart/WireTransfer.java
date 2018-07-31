package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.customer.Customer;

public class WireTransfer implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private long customerId;	
	private Date created;
	private char status;//W= active, P = paid
	private double amount;
	private int confirmedBy;
	private Date confirmed;
	private Cart cart;
	private Customer customer;
	
	
	@JsonIgnore
	public int getNewLoyaltyPoints() {
		return (int) amount/20;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getConfirmedBy() {
		return confirmedBy;
	}
	public void setConfirmedBy(int confirmedBy) {
		this.confirmedBy = confirmedBy;
	}
	public Date getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(Date confirmed) {
		this.confirmed = confirmed;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((confirmed == null) ? 0 : confirmed.hashCode());
		result = prime * result + confirmedBy;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
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
		WireTransfer other = (WireTransfer) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (cartId != other.cartId)
			return false;
		if (confirmed == null) {
			if (other.confirmed != null)
				return false;
		} else if (!confirmed.equals(other.confirmed))
			return false;
		if (confirmedBy != other.confirmedBy)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (customerId != other.customerId)
			return false;
		if (id != other.id)
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	
	
}
