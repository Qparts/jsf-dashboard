package qetaa.jsf.dashboard.model.payment;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WalletItemVendor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private long id;
	private long walletItemId;
	private Integer vendorId;
	private Double cost;
	private Integer createdBy;
	private Date date;
	@JsonIgnore
	private boolean newlyAdded;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getWalletItemId() {
		return walletItemId;
	}
	public void setWalletItemId(long walletItemId) {
		this.walletItemId = walletItemId;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isNewlyAdded() {
		return newlyAdded;
	}
	public void setNewlyAdded(boolean newlyAdded) {
		this.newlyAdded = newlyAdded;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (newlyAdded ? 1231 : 1237);
		result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
		result = prime * result + (int) (walletItemId ^ (walletItemId >>> 32));
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
		WalletItemVendor other = (WalletItemVendor) obj;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (newlyAdded != other.newlyAdded)
			return false;
		if (vendorId == null) {
			if (other.vendorId != null)
				return false;
		} else if (!vendorId.equals(other.vendorId))
			return false;
		if (walletItemId != other.walletItemId)
			return false;
		return true;
	}
	
	
	
}
