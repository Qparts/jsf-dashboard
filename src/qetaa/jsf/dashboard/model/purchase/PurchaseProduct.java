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
	
	
	
	
	
	
}
