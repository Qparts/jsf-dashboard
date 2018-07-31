package qetaa.jsf.dashboard.model.customer;

import java.io.Serializable;
import java.util.Date;

public class LoyaltyPoints implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private Customer customer;
	private int points;
	private String source;
	private Long cartId;
	private LoyaltyGift gift;
	private Double purchaseAmount;
	private Customer transferredCustomer;
	private Date created;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public LoyaltyGift getGift() {
		return gift;
	}
	public void setGift(LoyaltyGift gift) {
		this.gift = gift;
	}
	public Double getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(Double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public Customer getTransferredCustomer() {
		return transferredCustomer;
	}
	public void setTransferredCustomer(Customer transferredCustomer) {
		this.transferredCustomer = transferredCustomer;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cartId == null) ? 0 : cartId.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((gift == null) ? 0 : gift.hashCode());
		result = prime * result + id;
		result = prime * result + points;
		result = prime * result + ((purchaseAmount == null) ? 0 : purchaseAmount.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((transferredCustomer == null) ? 0 : transferredCustomer.hashCode());
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
		LoyaltyPoints other = (LoyaltyPoints) obj;
		if (cartId == null) {
			if (other.cartId != null)
				return false;
		} else if (!cartId.equals(other.cartId))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (gift == null) {
			if (other.gift != null)
				return false;
		} else if (!gift.equals(other.gift))
			return false;
		if (id != other.id)
			return false;
		if (points != other.points)
			return false;
		if (purchaseAmount == null) {
			if (other.purchaseAmount != null)
				return false;
		} else if (!purchaseAmount.equals(other.purchaseAmount))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (transferredCustomer == null) {
			if (other.transferredCustomer != null)
				return false;
		} else if (!transferredCustomer.equals(other.transferredCustomer))
			return false;
		return true;
	}
	
}
