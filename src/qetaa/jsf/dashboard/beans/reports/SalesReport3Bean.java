package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.sales.SalesHolder;
import qetaa.jsf.dashboard.model.sales.SalesReturnHolder;

@Named
@ViewScoped
public class SalesReport3Bean implements Serializable{


	private static final long serialVersionUID = 1L;
	private int year;
	private int month;
	private char transactionType;
	private char paymentStatus;
	private int makeId;
	
	private List<SalesHolder> completeSalesHolders;
	private List<SalesReturnHolder> completeSalesReturnsHolders;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		initCurrentDate();
		completeSalesHolders = new ArrayList<>();
		completeSalesReturnsHolders = new ArrayList<>();
	}
	
	private void initCurrentDate() {
		Calendar c = Calendar.getInstance();
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
	}
	
	public void generateReport() {
		initSales();
		initSalesCarts();
		initSalesReturns();
		initSalesReturnCarts();
	}
	
	
	
	public void initSalesCarts() {
		Thread[] threads = new Thread[this.completeSalesHolders.size()];
		String header = reqs.getSecurityHeader();
		int index = 0;
		for (SalesHolder sh : completeSalesHolders) {
			try {
				threads[index] = ThreadRunner.initSalesCart(sh.getSales(), header);
				threads[index].start();
				threads[index].join();
			} catch (Exception ex) {

			}
		}
	}
	
	public void initSalesReturnCarts() {
		Thread[] threads = new Thread[this.completeSalesReturnsHolders.size()];
		String header = reqs.getSecurityHeader();
		int index = 0;
		for (SalesReturnHolder sh : completeSalesReturnsHolders) {
			try {
				threads[index] = ThreadRunner.initSalesCart(sh.getSalesReturn().getSales(), header);
				threads[index].start();
				threads[index].join();
			} catch (Exception ex) {

			}
		}
	}
	
	private void initSales() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("year", year);
		map.put("month", month);
		map.put("transaction", this.transactionType);
		map.put("paymentStatus", this.paymentStatus);
		map.put("makeId", this.makeId);
		Response r = reqs.postSecuredRequest(AppConstants.POST_SALES_REPORT_3, map);
		if (r.getStatus() == 200) {
			completeSalesHolders = r.readEntity(new GenericType<List<SalesHolder>>() {
			});
		} else {			
			Helper.addErrorMessage("An error Occured in generating sales  " + r.getStatus());
		}
	}
	
	private void initSalesReturns() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("year", year);
		map.put("month", month);
		map.put("transaction", this.transactionType);
		map.put("paymentStatus", this.paymentStatus);
		map.put("makeId", this.makeId);
		Response r = reqs.postSecuredRequest(AppConstants.POST_SALES_RETURN_REPORT_3, map);
		if (r.getStatus() == 200) {
			this.completeSalesReturnsHolders = r.readEntity(new GenericType<List<SalesReturnHolder>>() {
			});
		} else {
			Helper.addErrorMessage("An error Occured in generating sales returns");
		}
	}
	
	
	/////calculations////////
	
	public double getCompleteTotalSalesWv() {
		double total = 0;
		for(SalesHolder sh : this.completeSalesHolders) {
			if(sh.isPurchasesComplete()) {
				total = total + sh.getSales().getTotalSalesWv();
			}
		}
		return total;
	}
	
	public double getIncompleteTotalSalesWv() {
		double total = 0;
		for(SalesHolder sh : this.completeSalesHolders) {
			if(!sh.isPurchasesComplete()) {
				total = total + sh.getSales().getTotalSalesWv();
			}
		}
		return total;
	}
	
	public double getCompletedTotalCostWv() {
		double total = 0;
		for(SalesHolder sh : this.completeSalesHolders) {
			if(sh.isPurchasesComplete()){
				total = total + sh.getPartsPurchaseCost();
			}
		}
		return total;
	}
	
	public double getIncompletedTotalCostWv() {
		double total = 0;
		for(SalesHolder sh : this.completeSalesHolders) {
			if(!sh.isPurchasesComplete()){
				total = total + sh.getPartsPurchaseCost();
			}
		}
		return total;
	}
	
	
	public double getCompleteTotalSalesReturnWv() {
		double total = 0;
		for(SalesReturnHolder sh : this.completeSalesReturnsHolders) {
			if(sh.isPurchasesComplete()) {
				total = total + sh.getSalesReturn().getNetSalesReturn();
			}
		}
		return total;
	}
	
	public double getIncompleteTotalSalesReturnWv() {
		double total = 0;
		for(SalesReturnHolder sh : this.completeSalesReturnsHolders) {
			if(!sh.isPurchasesComplete()) {
				total = total + sh.getSalesReturn().getNetSalesReturn();
			}
		}
		return total;
	}
	
	public double getCompletedReturnTotalCostWv() {
		double total = 0;
		for(SalesReturnHolder sh : this.completeSalesReturnsHolders) {
			if(sh.isPurchasesComplete()){
				total = total + sh.getPartsPurchaseCost();
			} 
		}
		return total;
	}
	
	public double getIncompletedReturnTotalCostWv() {
		double total = 0;
		for(SalesReturnHolder sh : this.completeSalesReturnsHolders) {
			if(!sh.isPurchasesComplete()){
				total = total + sh.getPartsPurchaseCost();
			}
		}
		return total;
	}
	
	///////getters and setters/////////

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

	
	public char getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(char transactionType) {
		this.transactionType = transactionType;
	}

	public char getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(char paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public int getMakeId() {
		return makeId;
	}

	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}

	public List<SalesHolder> getCompleteSalesHolders() {
		return completeSalesHolders;
	}

	public void setCompleteSalesHolders(List<SalesHolder> completeSalesHolders) {
		this.completeSalesHolders = completeSalesHolders;
	}

	public List<SalesReturnHolder> getCompleteSalesReturnsHolders() {
		return completeSalesReturnsHolders;
	}

	public void setCompleteSalesReturnsHolders(List<SalesReturnHolder> completeSalesReturnsHolders) {
		this.completeSalesReturnsHolders = completeSalesReturnsHolders;
	}

	
	
	
	
	
	

}
