package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.payment.Wallet;

@Named
@ViewScoped
public class WalletsReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private int year;
	private int month;
	private String paymentType;
	private List<Wallet> wallets;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct()
	private void init() {
		wallets = new ArrayList<>();
		initCurrentDate();
		//discountPromos = new ArrayList<>();
		//initAllDiscountPromoCodes();
	}
	
	private void initCurrentDate() {
		Calendar c = Calendar.getInstance();
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
	}
	
	public double getTotalAmounts() {
		double total = 0;
		for(Wallet w : wallets) {
			total = total  + (w.getWalletType() == 'P' ? w.getNetAmount() : -1*w.getNetAmount());
		}
		return total;
	}
	
	public double getTotalCreditFees() {
		double total = 0;
		for(Wallet w : wallets) {
			if(w.getCreditFees()!= null)
				total = total +  w.getCreditFees();
		}
		return total;
	}
	
	public Double getNetTotal() {
		double total = 0;
		for(Wallet w : wallets) {
			total = total  + (w.getWalletType() == 'P' ? w.getNetAmount() : -1*w.getNetAmount());
			if(w.getCreditFees() != null)
				total = total - w.getCreditFees();
		}
		return total;
	}
	
	
	public void generateReport() {
		Response r = reqs.getSecuredRequest(AppConstants.getWalletsReport(year, month, paymentType));
		if(r.getStatus() == 200) {
			this.wallets = r.readEntity(new GenericType<List<Wallet>>() {});
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public List<Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}
	
	

}
