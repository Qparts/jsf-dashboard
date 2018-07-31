package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.product.Product;

public class QuotationItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private long quotationId;
	private int quantity;
	private int createdBy;
	private String itemDesc, itemDescAr;
	private Date created;
	private char status;
	@JsonIgnore
	private boolean edit;
	@JsonIgnore
	private boolean notAvailable;

	private List<QuotationVendorItem> vendorItems;
	@JsonIgnore
	private Product product;
	@JsonIgnore
	public boolean allVendorItemsNotAvailable() {
		boolean found = true;
		if(vendorItems == null) {
			return false;
		}
		for (QuotationVendorItem vitem : vendorItems) {
			if (vitem.getStatus() == 'W' || vitem.getStatus() == 'C') {
				found = false;
				break;
			}
		}
		return found;
	}
	
	@JsonIgnore
	public Product getProduct() {
		return product;
	}
	
	@JsonIgnore
	public void setProduct(Product product) {
		this.product = product;
	}


	public List<QuotationVendorItem> getVendorItems() {
		return vendorItems;
	}

	public void setVendorItems(List<QuotationVendorItem> vendorItems) {
		this.vendorItems = vendorItems;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + createdBy;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((itemDesc == null) ? 0 : itemDesc.hashCode());
		result = prime * result + ((itemDescAr == null) ? 0 : itemDescAr.hashCode());
		result = prime * result + quantity;
		result = prime * result + (int) (quotationId ^ (quotationId >>> 32));
		result = prime * result + ((vendorItems == null) ? 0 : vendorItems.hashCode());
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
		QuotationItem other = (QuotationItem) obj;
		if (cartId != other.cartId)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdBy != other.createdBy)
			return false;
		if (id != other.id)
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
		if (quantity != other.quantity)
			return false;
		if (quotationId != other.quotationId)
			return false;
		if (vendorItems == null) {
			if (other.vendorItems != null)
				return false;
		} else if (!vendorItems.equals(other.vendorItems))
			return false;
		return true;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isNotAvailable() {
		return notAvailable;
	}

	public void setNotAvailable(boolean notAvailable) {
		this.notAvailable = notAvailable;
	}

}
