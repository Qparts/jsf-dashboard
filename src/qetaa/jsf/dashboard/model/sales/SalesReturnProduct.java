package qetaa.jsf.dashboard.model.sales;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.product.ProductStock;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;

public class SalesReturnProduct implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private long productId;
	private SalesReturn salesReturn;
	private PurchaseProduct purchaseProduct;
	private int quantity;
	private double unitSales;
	private double unitSalesWv;
	private double unitCost;
	private double unitCostWv;
	private double returnDeductionFees;
	@JsonIgnore
	private Product product;
	@JsonIgnore
	private int newQuantity;
	
	
	@JsonIgnore
	public int getVendorSpecificStockQuantity() {
		int total = 0;
		if(product == null || product.getStockList() == null || product.getStockList().isEmpty()) {
			return 0;
		}
		for(ProductStock sp : product.getStockList()) {
			if(sp.getPurchaseId() == this.getPurchaseProduct().getPurchase().getId())
				total = total + sp.getQuantity();
		}
		return total;
	}
	
	
	@JsonIgnore
	public int getQuantityArray() []{
		int[] quantityArray = new int[quantity];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
		}
		return quantityArray;
	}
	
	public int getNewQuantity() {
		return newQuantity;
	}
	public void setNewQuantity(int newQuantity) {
		this.newQuantity = newQuantity;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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
	public SalesReturn getSalesReturn() {
		return salesReturn;
	}
	public void setSalesReturn(SalesReturn salesReturn) {
		this.salesReturn = salesReturn;
	}
	public PurchaseProduct getPurchaseProduct() {
		return purchaseProduct;
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
	public double getReturnDeductionFees() {
		return returnDeductionFees;
	}
	public void setReturnDeductionFees(double returnDeductionFees) {
		this.returnDeductionFees = returnDeductionFees;
	}
	public void setPurchaseProduct(PurchaseProduct purchaseProduct) {
		this.purchaseProduct = purchaseProduct;
	}
	
	
	
	
}
