package qetaa.jsf.dashboard.model.purchase;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.sales.SalesReturnProduct;

public class PurchaseReturnProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long productId;
	private PurchaseReturn purchaseReturn;
	private PurchaseProduct purchaseProduct;
	private int quantity;
	private double unitCost;
	private double unitCostWv;
	@JsonIgnore
	private Product product;
	private SalesReturnProduct salesReturnProduct;
	
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
	public PurchaseReturn getPurchaseReturn() {
		return purchaseReturn;
	}
	public void setPurchaseReturn(PurchaseReturn purchaseReturn) {
		this.purchaseReturn = purchaseReturn;
	}
	public PurchaseProduct getPurchaseProduct() {
		return purchaseProduct;
	}
	public void setPurchaseProduct(PurchaseProduct purchaseProduct) {
		this.purchaseProduct = purchaseProduct;
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
	public double getUnitCostWv() {
		return unitCostWv;
	}
	public void setUnitCostWv(double unitCostWv) {
		this.unitCostWv = unitCostWv;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public SalesReturnProduct getSalesReturnProduct() {
		return salesReturnProduct;
	}
	public void setSalesReturnProduct(SalesReturnProduct salesReturnProduct) {
		this.salesReturnProduct = salesReturnProduct;
	}
	
	
	
	
	

}
