package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.user.User;

public class Quotation implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private Date created, deadline;
	private char status;
	private int createdBy;
	private List<QuotationItem> quotationItems;
	
	
	@JsonIgnore
	private Cart cart;
	
	@JsonIgnore
	private boolean isManuallyAdded;

	@JsonIgnore
	private User createdByObject;

	@JsonIgnore
	public boolean allResponsesNotAvailable() {
		boolean found = false;
		for (QuotationItem qitem : quotationItems) {
			if (qitem.allVendorItemsNotAvailable()) {
				found = true;
				break;
			}
		}
		return found;

	}
	
	@JsonIgnore
	public Cart getCart() {
		return cart;
	}
	
	@JsonIgnore
	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@JsonIgnore
	public boolean isManuallyAdded() {
		return isManuallyAdded;
	}


	@JsonIgnore
	public void setManuallyAdded(boolean isManuallyAdded) {
		this.isManuallyAdded = isManuallyAdded;
	}



	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public List<QuotationItem> getQuotationItems() {
		return quotationItems;
	}

	public void setQuotationItems(List<QuotationItem> quotationItems) {
		this.quotationItems = quotationItems;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((deadline == null) ? 0 : deadline.hashCode());
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
		Quotation other = (Quotation) obj;
		if (cartId != other.cartId)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (deadline == null) {
			if (other.deadline != null)
				return false;
		} else if (!deadline.equals(other.deadline))
			return false;
		if (id != other.id)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	public User getCreatedByObject() {
		return createdByObject;
	}

	public void setCreatedByObject(User craetedByObject) {
		this.createdByObject = craetedByObject;
	}

}
