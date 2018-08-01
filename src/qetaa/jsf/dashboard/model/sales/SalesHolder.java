package qetaa.jsf.dashboard.model.sales;

import java.util.List;

import qetaa.jsf.dashboard.model.purchase.Purchase;

public class SalesHolder {

	private Sales sales;
	private List<Purchase> purchases;
	private boolean purchasesComplete;

	public double getPartsPurchaseCost() {
		double total = 0;
		if (purchases != null) {
			for (Purchase purchase : purchases) {
				total += purchase.getTotalCostWv();
			}
		}
		return total;
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public boolean isPurchasesComplete() {
		return purchasesComplete;
	}

	public void setPurchasesComplete(boolean purchasesComplete) {
		this.purchasesComplete = purchasesComplete;
	}

	
}
