package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;

public class QuotationVendorItem implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private long quotationId;
	private long quotationItemId;
	private Integer vendorId;
	private int quantity;
	private String itemNumber;
	private String itemDesc;
	private String itemDescAr;
	private Double itemCostPrice;
	private Double salesPercentage;
	private Date created;
	private Date responded;
	private Integer createdBy;
	private Integer respondedBy;
	private Integer respondedByFinder;
	private char status;
	private Long productId;
	private Character sentTo;
	private Integer finderId;
	
	

	
	public Integer getRespondedByFinder() {
		return respondedByFinder;
	}
	public void setRespondedByFinder(Integer respondedByFinder) {
		this.respondedByFinder = respondedByFinder;
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
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public Double getItemCostPrice() {
		return itemCostPrice;
	}
	public void setItemCostPrice(Double itemCostPrice) {
		this.itemCostPrice = itemCostPrice;
	}
	public Double getSalesPercentage() {
		return salesPercentage;
	}
	public void setSalesPercentage(Double salesPercentage) {
		this.salesPercentage = salesPercentage;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getResponded() {
		return responded;
	}
	public void setResponded(Date responded) {
		this.responded = responded;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getRespondedBy() {
		return respondedBy;
	}
	public void setRespondedBy(Integer respondedBy) {
		this.respondedBy = respondedBy;
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
	public String getItemDescAr() {
		return itemDescAr;
	}
	public void setItemDescAr(String itemDescAr) {
		this.itemDescAr = itemDescAr;
	}
	
	
	
	public Character getSentTo() {
		return sentTo;
	}
	public void setSentTo(Character sentTo) {
		this.sentTo = sentTo;
	}
	public Integer getFinderId() {
		return finderId;
	}
	public void setFinderId(Integer finderId) {
		this.finderId = finderId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((finderId == null) ? 0 : finderId.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((itemCostPrice == null) ? 0 : itemCostPrice.hashCode());
		result = prime * result + ((itemDesc == null) ? 0 : itemDesc.hashCode());
		result = prime * result + ((itemDescAr == null) ? 0 : itemDescAr.hashCode());
		result = prime * result + ((itemNumber == null) ? 0 : itemNumber.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + quantity;
		result = prime * result + (int) (quotationId ^ (quotationId >>> 32));
		result = prime * result + (int) (quotationItemId ^ (quotationItemId >>> 32));
		result = prime * result + ((responded == null) ? 0 : responded.hashCode());
		result = prime * result + ((respondedBy == null) ? 0 : respondedBy.hashCode());
		result = prime * result + ((respondedByFinder == null) ? 0 : respondedByFinder.hashCode());
		result = prime * result + ((salesPercentage == null) ? 0 : salesPercentage.hashCode());
		result = prime * result + ((sentTo == null) ? 0 : sentTo.hashCode());
		result = prime * result + status;
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
		QuotationVendorItem other = (QuotationVendorItem) obj;
		if (cartId != other.cartId)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (finderId == null) {
			if (other.finderId != null)
				return false;
		} else if (!finderId.equals(other.finderId))
			return false;
		if (id != other.id)
			return false;
		if (itemCostPrice == null) {
			if (other.itemCostPrice != null)
				return false;
		} else if (!itemCostPrice.equals(other.itemCostPrice))
			return false;
		if (itemDesc == null) {
			if (other.itemDesc != null)
				return false;
		} else if (!itemDesc.equals(other.itemDesc))
			return false;
		if (itemDescAr == null) {
			if (other.itemDescAr != null)
				return false;
		} else if (!itemDescAr.equals(other.itemDescAr))
			return false;
		if (itemNumber == null) {
			if (other.itemNumber != null)
				return false;
		} else if (!itemNumber.equals(other.itemNumber))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (quantity != other.quantity)
			return false;
		if (quotationId != other.quotationId)
			return false;
		if (quotationItemId != other.quotationItemId)
			return false;
		if (responded == null) {
			if (other.responded != null)
				return false;
		} else if (!responded.equals(other.responded))
			return false;
		if (respondedBy == null) {
			if (other.respondedBy != null)
				return false;
		} else if (!respondedBy.equals(other.respondedBy))
			return false;
		if (respondedByFinder == null) {
			if (other.respondedByFinder != null)
				return false;
		} else if (!respondedByFinder.equals(other.respondedByFinder))
			return false;
		if (salesPercentage == null) {
			if (other.salesPercentage != null)
				return false;
		} else if (!salesPercentage.equals(other.salesPercentage))
			return false;
		if (sentTo == null) {
			if (other.sentTo != null)
				return false;
		} else if (!sentTo.equals(other.sentTo))
			return false;
		if (status != other.status)
			return false;
		if (vendorId == null) {
			if (other.vendorId != null)
				return false;
		} else if (!vendorId.equals(other.vendorId))
			return false;
		return true;
	}
	
	
	
}
