package qetaa.jsf.dashboard.model.sales;

import java.util.List;

import qetaa.jsf.dashboard.model.purchase.Purchase;

public class SalesReturnHolder {
	private SalesReturn salesReturn;
	private List<Purchase> purchases; 
	private boolean purchasesComplete;
	
	
	
	public boolean isPurchasesComplete() {
		return purchasesComplete;
	}
	public void setPurchasesComplete(boolean purchasesComplete) {
		this.purchasesComplete = purchasesComplete;
	}
	public SalesReturn getSalesReturn() {
		return salesReturn;
	}
	public void setSalesReturn(SalesReturn salesReturn) {
		this.salesReturn = salesReturn;
	}
	public List<Purchase> getPurchases() {
		return purchases;
	}
	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}
	

	
}
