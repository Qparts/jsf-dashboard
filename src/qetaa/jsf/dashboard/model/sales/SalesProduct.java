package qetaa.jsf.dashboard.model.sales;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;

public class SalesProduct implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private Sales sales;
	private long productId;
	@JsonIgnore
	private Product product;
	private int quantity;
	private double unitSales;
	private double unitSalesWv;
	private Double unitCost;
	private Double unitCostWv;
	private PurchaseProduct purchaseProduct;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Sales getSales() {
		return sales;
	}
	public void setSales(Sales sales) {
		this.sales = sales;
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
	public double getUnitSales() {
		return unitSales;
	}
	public void setUnitSales(double unitSales) {
		this.unitSales = unitSales;
	}
	public double getUnitSalesWv() {
		return unitSalesWv;
	}
	public void setUnitSalesWv(double unitSalesWv) {
		this.unitSalesWv = unitSalesWv;
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
	public PurchaseProduct getPurchaseProduct() {
		return purchaseProduct;
	}
	public void setPurchaseProduct(PurchaseProduct purchaseProduct) {
		this.purchaseProduct = purchaseProduct;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
	
}
