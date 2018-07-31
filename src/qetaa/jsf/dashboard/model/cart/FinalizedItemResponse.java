package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.vendor.Vendor;

public class FinalizedItemResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private long quotationVendorItemId;
	private Vendor vendor;
	private String partNumber;
	private int submittedQuantity;
	private int selectedQuantity;
	private double unitCost;
	private double salesPercentage;
	
	
	@JsonIgnore
	public double getUnitSales() {
		return unitCost + (unitCost * salesPercentage);
	}
	
	@JsonIgnore
	public double getTotalSales() {
		return (unitCost + (unitCost * salesPercentage)) * selectedQuantity;
	}
	
	@JsonIgnore
	public double getTotalCost() {
		return unitCost * selectedQuantity;
	}
	
	
	public long getQuotationVendorItemId() {
		return quotationVendorItemId;
	}
	public void setQuotationVendorItemId(long quotationVendorItemId) {
		this.quotationVendorItemId = quotationVendorItemId;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public int getSubmittedQuantity() {
		return submittedQuantity;
	}
	public void setSubmittedQuantity(int submittedQuantity) {
		this.submittedQuantity = submittedQuantity;
	}
	public int getSelectedQuantity() {
		return selectedQuantity;
	}
	public void setSelectedQuantity(int selectedQuantity) {
		this.selectedQuantity = selectedQuantity;
	}
	public double getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}
	public double getSalesPercentage() {
		return salesPercentage;
	}
	public void setSalesPercentage(double salesPercentage) {
		this.salesPercentage = salesPercentage;
	}
	
	
}
