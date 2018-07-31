package qetaa.jsf.dashboard.model.cart;

import java.util.Date;

import qetaa.jsf.dashboard.model.user.User;
import qetaa.jsf.dashboard.model.vendor.VendorUser;

public class PartCollectionItem {

	private long partsApprovedId;
	private Integer vendorId;
	private long cartId;
	private int quantity;
	private double unitCost;
	private String itemDesc;
	private String itemNumber;

	private Date quoted;
	private Integer quotedBy;
	private VendorUser quotedByObject;
	
	private Date collected;
	private Integer collectedBy;
	private User collectedByObject;
	
	private Date prepared;
	private Integer preparedBy;
	private VendorUser preparedByObject;
	
	private Date received;
	private Integer receivedBy;
	private User receivedByObject;
	
	private char status;
	
	
	
	
	public VendorUser getPreparedByObject() {
		return preparedByObject;
	}
	public void setPreparedByObject(VendorUser preparedByObject) {
		this.preparedByObject = preparedByObject;
	}
	public Date getPrepared() {
		return prepared;
	}
	public void setPrepared(Date prepared) {
		this.prepared = prepared;
	}
	public Integer getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(Integer preparedBy) {
		this.preparedBy = preparedBy;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	
	public User getCollectedByObject() {
		return collectedByObject;
	}
	public void setCollectedByObject(User collectedByObject) {
		this.collectedByObject = collectedByObject;
	}
	public User getReceivedByObject() {
		return receivedByObject;
	}
	public void setReceivedByObject(User receivedByObject) {
		this.receivedByObject = receivedByObject;
	}
	public VendorUser getQuotedByObject() {
		return quotedByObject;
	}
	public void setQuotedByObject(VendorUser quotedByObject) {
		this.quotedByObject = quotedByObject;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public Date getCollected() {
		return collected;
	}
	public void setCollected(Date collected) {
		this.collected = collected;
	}
	public Integer getCollectedBy() {
		return collectedBy;
	}
	public void setCollectedBy(Integer collectedBy) {
		this.collectedBy = collectedBy;
	}
	public Date getReceived() {
		return received;
	}
	public void setReceived(Date received) {
		this.received = received;
	}
	public Integer getReceivedBy() {
		return receivedBy;
	}
	public void setReceivedBy(Integer receivedBy) {
		this.receivedBy = receivedBy;
	}
	public long getPartsApprovedId() {
		return partsApprovedId;
	}
	public void setPartsApprovedId(long partsApprovedId) {
		this.partsApprovedId = partsApprovedId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public Date getQuoted() {
		return quoted;
	}
	public void setQuoted(Date quoted) {
		this.quoted = quoted;
	}
	public Integer getQuotedBy() {
		return quotedBy;
	}
	public void setQuotedBy(Integer quotedBy) {
		this.quotedBy = quotedBy;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((collected == null) ? 0 : collected.hashCode());
		result = prime * result + ((collectedBy == null) ? 0 : collectedBy.hashCode());
		result = prime * result + ((itemDesc == null) ? 0 : itemDesc.hashCode());
		result = prime * result + ((itemNumber == null) ? 0 : itemNumber.hashCode());
		result = prime * result + (int) (partsApprovedId ^ (partsApprovedId >>> 32));
		result = prime * result + quantity;
		result = prime * result + ((quoted == null) ? 0 : quoted.hashCode());
		result = prime * result + quotedBy;
		result = prime * result + ((received == null) ? 0 : received.hashCode());
		result = prime * result + ((receivedBy == null) ? 0 : receivedBy.hashCode());
		long temp;
		temp = Double.doubleToLongBits(unitCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
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
		PartCollectionItem other = (PartCollectionItem) obj;
		if (cartId != other.cartId)
			return false;
		if (collected == null) {
			if (other.collected != null)
				return false;
		} else if (!collected.equals(other.collected))
			return false;
		if (collectedBy == null) {
			if (other.collectedBy != null)
				return false;
		} else if (!collectedBy.equals(other.collectedBy))
			return false;
		if (itemDesc == null) {
			if (other.itemDesc != null)
				return false;
		} else if (!itemDesc.equals(other.itemDesc))
			return false;
		if (itemNumber == null) {
			if (other.itemNumber != null)
				return false;
		} else if (!itemNumber.equals(other.itemNumber))
			return false;
		if (partsApprovedId != other.partsApprovedId)
			return false;
		if (quantity != other.quantity)
			return false;
		if (quoted == null) {
			if (other.quoted != null)
				return false;
		} else if (!quoted.equals(other.quoted))
			return false;
		if (quotedBy != other.quotedBy)
			return false;
		if (received == null) {
			if (other.received != null)
				return false;
		} else if (!received.equals(other.received))
			return false;
		if (receivedBy == null) {
			if (other.receivedBy != null)
				return false;
		} else if (!receivedBy.equals(other.receivedBy))
			return false;
		if (Double.doubleToLongBits(unitCost) != Double.doubleToLongBits(other.unitCost))
			return false;
		if (vendorId == null) {
			if (other.vendorId != null)
				return false;
		} else if (!vendorId.equals(other.vendorId))
			return false;
		return true;
	}

	
}
