package qetaa.jsf.dashboard.beans.purchase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.purchase.PurchasePayment;

@Named
@ViewScoped
public class PayablesBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<Purchase> purchases;
	private Purchase selectedPurchase;
	private PurchasePayment purchasePayment;
	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;
	
	@PostConstruct
	private void init() {
		purchasePayment = new PurchasePayment();
		this.purchases = new ArrayList<>();
		this.selectedPurchase = new Purchase();
		initPurchases();
	}
	
	private void initPurchases() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_PURCHASE_PAYABLES);
		if(r.getStatus() == 200) {
			purchases = r.readEntity(new GenericType<List<Purchase>>() {});
		}
	}
	
	public double totalOustanding() {
		double total = 0;
		for(Purchase purchase : purchases) {
			total += purchase.getTotalCostWv() - purchase.getPaid() + purchase.getReturned();
		}
		return total;
	}
	
	public void createPurchasePayment() {
		if(this.purchasePayment.getAmount() <= this.selectedPurchase.getOutstanding()) {
			purchasePayment.setMethod('R');
			purchasePayment.setPaidBy(loginBean.getUserHolder().getUser().getId());
			purchasePayment.setPurchase(this.selectedPurchase);
			Response r = reqs.postSecuredRequest(AppConstants.POST_PURCHASE_PAYMENT, purchasePayment);
			if(r.getStatus() == 201) {
				Helper.redirect("payables");
			}
			else {
				Helper.addErrorMessage("An error occured");
			}
		}
		else {
			Helper.addErrorMessage("Outstanding amount exceeded");
		}
	}
	
	public void choosePurchase(Purchase purchase) {
		this.setSelectedPurchase(purchase);
		this.purchasePayment = new PurchasePayment();
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public Purchase getSelectedPurchase() {
		return selectedPurchase;
	}

	public void setSelectedPurchase(Purchase selectedPurchase) {
		this.selectedPurchase = selectedPurchase;
	}

	public PurchasePayment getPurchasePayment() {
		return purchasePayment;
	}

	public void setPurchasePayment(PurchasePayment purchasePayment) {
		this.purchasePayment = purchasePayment;
	}
	
	
	
	
	
	
}
