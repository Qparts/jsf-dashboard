package qetaa.jsf.dashboard.beans.purchase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.VendorBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.purchase.PurchasePayment;
import qetaa.jsf.dashboard.model.vendor.Vendor;

@Named
@ViewScoped
public class PayableVendorBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Vendor vendor;
	private Double bulkAmount;
	private Date bulkDate;
	private Integer bulkBank;
	private String bulkReference;
	
	private List<Purchase> purchases;
	@Inject
	private Requester reqs;
	@Inject
	private VendorBean vendorBean;
	@Inject
	private LoginBean loginBean;
	
	@PostConstruct
	private void init() {
		try {
			purchases = new ArrayList<>();
			String vid = Helper.getParam("vendor-id");
			initPurchases(vid);
			this.vendor = vendorBean.getVendorFromId(Integer.parseInt(vid));
		}catch(Exception ex) {
			Helper.redirect("payables");
		}
	}
	
	
	
	private void initPurchases(String vidString) throws Exception {
		Integer vid = Integer.parseInt(vidString);
		Response r = reqs.getSecuredRequest(AppConstants.getVendorPayables(vid));
		if(r.getStatus() == 200) {
			purchases = r.readEntity(new GenericType<List<Purchase>>() {});
		}
	}
	
	public void createBulkPayment() {
		if(this.bulkAmount <= this.totalOustanding()) {
			double temp = bulkAmount;
			List<PurchasePayment> pps = new ArrayList<>();
			int index = 0;
			while(temp > 0) {
				Purchase purchase = this.purchases.get(index);
				double toPay = 0;
				if(temp >= purchase.getOutstanding()) {
					toPay = purchase.getOutstanding();
					temp += - toPay;
				}else {
					toPay = temp;
					temp -= toPay;
				}
				PurchasePayment pp = new PurchasePayment();
				pp.setAmount(toPay);
				pp.setBankId(bulkBank);
				pp.setMethod('R');
				pp.setPaidBy(loginBean.getUserHolder().getUser().getId());
				pp.setPaymentDate(bulkDate);
				pp.setPaymentRef(bulkReference);
				pp.setPurchase(purchase);
				pps.add(pp);
				index++;
			}
			
			Response r = reqs.postSecuredRequest(AppConstants.POST_PURCHASE_PAYMENTS, pps);
			if(r.getStatus() == 201) {
				Helper.redirect("payables-vendor?vendor-id=" + this.vendor.getId());
			}
			else {
				Helper.addErrorMessage("An error occured");
			}
			
		}
		else {
			Helper.addErrorMessage("Outstanding amount exceeded");
		}
	}
	
	public double totalOustanding() {
		double total = 0;
		for(Purchase purchase : purchases) {
			total += purchase.getTotalCostWv() - purchase.getPaid() + purchase.getReturned();
		}
		return total;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Double getBulkAmount() {
		return bulkAmount;
	}

	public void setBulkAmount(Double bulkAmount) {
		this.bulkAmount = bulkAmount;
	}

	public Date getBulkDate() {
		return bulkDate;
	}

	public void setBulkDate(Date bulkDate) {
		this.bulkDate = bulkDate;
	}

	public Integer getBulkBank() {
		return bulkBank;
	}

	public void setBulkBank(Integer bulkBank) {
		this.bulkBank = bulkBank;
	}

	public String getBulkReference() {
		return bulkReference;
	}

	public void setBulkReference(String bulkReference) {
		this.bulkReference = bulkReference;
	}
}
