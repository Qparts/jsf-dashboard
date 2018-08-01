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
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesReturn;

@Named
@ViewScoped
public class SalesReportBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int year;
	private int month;
	private char method;
	private List<Sales> sales;
	private List<SalesReturn> salesReturns;

	@Inject
	private Requester reqs;

	@PostConstruct()
	private void init() {
		initCurrentDate();
		sales= new ArrayList<>();
		salesReturns = new ArrayList<>();
	}


	private void initSales() {
		Response r = reqs.getSecuredRequest(AppConstants.getSalesReport(year, month, method));
		if (r.getStatus() == 200) {
			sales = r.readEntity(new GenericType<List<Sales>>() {
			});
		} else {
			Helper.addErrorMessage("An error Occured in generating sales");
		}
	}
	
	private void initReturns() {
		Response r = reqs.getSecuredRequest(AppConstants.getSalesReturnReport(year, month));
		if (r.getStatus() == 200) {
			salesReturns = r.readEntity(new GenericType<List<SalesReturn>>() {
			});
		} else {
			Helper.addErrorMessage("An error Occured in generating returns");
		}
	}
	
	public double getTotalSalesWv() {
		double total = 0;
		for(Sales s : sales) {
			total = total + s.getTotalSalesWv();
		}
		return total;
	}
	
	public double getTotalNetSalesReturnWv() {
		double total = 0;
		for(SalesReturn s : salesReturns) {
			total = total + s.getNetSalesReturn();
		}
		return total;
	}
	
	public double getTotalNetCostReturnWv() {
		double total = 0;
		for(SalesReturn s : salesReturns) {
			total = total + s.getTotalPartsCostWv();
		}
		return total;
	}
	
	public double getTotalReturnShipment() {
		double total = 0;
		for(SalesReturn s : salesReturns) {
			total = total + s.getShipmentFees();
		}
		return total;
	}
	
	public double getTotalCostWv() {
		double total = 0;
		for(Sales s : sales) {
			total = total + s.getTotalCostWv();
		}
		return total;
	}
	
	public double getTotalReturnProfit() {
		double total = 0;
		for(SalesReturn s : salesReturns) {
			total = total + s.getReturnProfit();
		}
		return total;
	}
	
	public double getTotalProfit() {
		double total = 0;
		for(Sales s : sales) {
			total = total + s.getProfit();
		}
		return total;
	}

	public void initSalesCarts() {
		Thread[] threads = new Thread[sales.size()];
		String header = reqs.getSecurityHeader();
		int index = 0;
		for (Sales s : sales) {
			try {
				threads[index] = ThreadRunner.initSalesCart(s, header);
				threads[index].start();
				threads[index].join();
			} catch (Exception ex) {

			}
		}
	}
	
	public void initSalesReturnCarts() {
		Thread[] threads = new Thread[sales.size()];
		String header = reqs.getSecurityHeader();
		int index = 0;
		for (SalesReturn s : salesReturns) {
			try {
				threads[index] = ThreadRunner.initSalesCart(s.getSales(), header);
				threads[index].start();
				threads[index].join();
			} catch (Exception ex) {

			}
		}
	}

	public void generateReport() {
		initSales();
		initSalesCarts();
		initReturns();
		initSalesReturnCarts();
	}

	private void initCurrentDate() {
		Calendar c = Calendar.getInstance();
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
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

	public char getMethod() {
		return method;
	}

	public void setMethod(char method) {
		this.method = method;
	}

	public List<Sales> getSales() {
		return sales;
	}

	public void setSales(List<Sales> sales) {
		this.sales = sales;
	}


	public List<SalesReturn> getSalesReturns() {
		return salesReturns;
	}


	public void setSalesReturns(List<SalesReturn> salesReturn) {
		this.salesReturns = salesReturn;
	}

	
	
}
