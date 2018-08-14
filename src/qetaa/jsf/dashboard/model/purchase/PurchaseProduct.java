package qetaa.jsf.dashboard.model.purchase;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.payment.WalletItem;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.sales.SalesProduct;

public class PurchaseProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long productId;	
	private int quantity;
	private Purchase purchase;
	private Double unitCost;
	private Double unitCostWv;
	private Long walletItemId;
	private SalesProduct salesProduct;
	@JsonIgnore
	private boolean withVat;
	@JsonIgnore
	private boolean productChanged;
	
	@JsonIgnore
	private Product product;
	@JsonIgnore
	private int newQuantity;
	@JsonIgnore
	private WalletItem walletItem;
	
	
	@JsonIgnore
	public int getQuantityArray(int stockQuantity) []{
		int[] quantityArray = new int[quantity - stockQuantity];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
		}
		return quantityArray;
	}
	
	public void chooseLaterPrice() {
		this.setUnitCost(null);
		this.setUnitCostWv(null);
	}
	
	

	public SalesProduct getSalesProduct() {
		return salesProduct;
	}

	public void setSalesProduct(SalesProduct salesProduct) {
		this.salesProduct = salesProduct;
	}

	public Long getWalletItemId() {
		return walletItemId;
	}

	public void setWalletItemId(Long walletId) {
		this.walletItemId = walletId;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Purchase getPurchase() {
		return purchase;
	}
	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
	public Double getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}
	public Double getUnitCostWv() {
		return unitCostWv;
	}
	public void setUnitCostWv(Double unitCostWv) {
		this.unitCostWv = unitCostWv;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public int getNewQuantity() {
		return newQuantity;
	}

	public void setNewQuantity(int newQuantity) {
		this.newQuantity = newQuantity;
	}

	public WalletItem getWalletItem() {
		return walletItem;
	}

	public void setWalletItem(WalletItem walletItem) {
		this.walletItem = walletItem;
	}

	public boolean isWithVat() {
		return withVat;
	}

	public void setWithVat(boolean withVat) {
		this.withVat = withVat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + newQuantity;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + (int) (productId ^ (productId >>> 32));
		result = prime * result + ((purchase == null) ? 0 : purchase.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((salesProduct == null) ? 0 : salesProduct.hashCode());
		result = prime * result + ((unitCost == null) ? 0 : unitCost.hashCode());
		result = prime * result + ((unitCostWv == null) ? 0 : unitCostWv.hashCode());
		result = prime * result + ((walletItem == null) ? 0 : walletItem.hashCode());
		result = prime * result + ((walletItemId == null) ? 0 : walletItemId.hashCode());
		result = prime * result + (withVat ? 1231 : 1237);
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
		PurchaseProduct other = (PurchaseProduct) obj;
		if (id != other.id)
			return false;
		if (newQuantity != other.newQuantity)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (productId != other.productId)
			return false;
		if (purchase == null) {
			if (other.purchase != null)
				return false;
		} else if (!purchase.equals(other.purchase))
			return false;
		if (quantity != other.quantity)
			return false;
		if (salesProduct == null) {
			if (other.salesProduct != null)
				return false;
		} else if (!salesProduct.equals(other.salesProduct))
			return false;
		if (unitCost == null) {
			if (other.unitCost != null)
				return false;
		} else if (!unitCost.equals(other.unitCost))
			return false;
		if (unitCostWv == null) {
			if (other.unitCostWv != null)
				return false;
		} else if (!unitCostWv.equals(other.unitCostWv))
			return false;
		if (walletItem == null) {
			if (other.walletItem != null)
				return false;
		} else if (!walletItem.equals(other.walletItem))
			return false;
		if (walletItemId == null) {
			if (other.walletItemId != null)
				return false;
		} else if (!walletItemId.equals(other.walletItemId))
			return false;
		if (withVat != other.withVat)
			return false;
		return true;
	}

	public boolean isProductChanged() {
		return productChanged;
	}

	public void setProductChanged(boolean productChanged) {
		this.productChanged = productChanged;
	}
	
	
	
	
	
	
}
