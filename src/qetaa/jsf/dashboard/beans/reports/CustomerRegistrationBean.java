package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;
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
import qetaa.jsf.dashboard.model.customer.Customer;

@Named
@ViewScoped
public class CustomerRegistrationBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int totalRegisteredCustomers;
	private int currentMonthCustomers;
	private int lastMonthCustomers;
	private double loginToCustomerRatio;
	private List<Customer> customers;
	private int year;
	private int month;

	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		initTotalRegisteredCustomers();
		initCurrentMonthRegisteredCustomers();
		initLastMonthRegisteredCustomers();
		initLoginCustomerRatio();
	}
	
	private void initLoginCustomerRatio() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_LOGIN_TO_CUSTOMER_RATIO);
		if(r.getStatus() == 200) {
			this.loginToCustomerRatio = r.readEntity(Double.class);
		}
	}
	
	
	private void initTotalRegisteredCustomers() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_TOTAL_REGISTERED_CUSTOMERS);
		if(r.getStatus() == 200) {
			totalRegisteredCustomers = r.readEntity(Integer.class);
		}
	}
	
	private void initCurrentMonthRegisteredCustomers() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_CURRENT_MONTH_REGISTERED_CUSTOMERS);
		if(r.getStatus() == 200) {
			this.currentMonthCustomers = r.readEntity(Integer.class);
		}
	}
	
	private void initLastMonthRegisteredCustomers() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_LAST_MONTH_REGISTERED_CUSTOMERS);
		if(r.getStatus() == 200) {
			this.lastMonthCustomers = r.readEntity(Integer.class);
		}
	}
	
	
	public void generateReport() {
		Response r = reqs.getSecuredRequest(AppConstants.getNweCustomersReport(year, month));
		if(r.getStatus() == 200) {
			this.customers = r.readEntity(new GenericType<List<Customer>>() {
			});
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public int getTotalRegisteredCustomers() {
		return totalRegisteredCustomers;
	}

	public void setTotalRegisteredCustomers(int totalRegisteredCustomers) {
		this.totalRegisteredCustomers = totalRegisteredCustomers;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
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

	public int getCurrentMonthCustomers() {
		return currentMonthCustomers;
	}

	public void setCurrentMonthCustomers(int currentMonthCustomers) {
		this.currentMonthCustomers = currentMonthCustomers;
	}

	public double getLoginToCustomerRatio() {
		return loginToCustomerRatio;
	}

	public void setLoginToCustomerRatio(double loginToCustomerRatio) {
		this.loginToCustomerRatio = loginToCustomerRatio;
	}

	public int getLastMonthCustomers() {
		return lastMonthCustomers;
	}

	public void setLastMonthCustomers(int lastMonthCustomers) {
		this.lastMonthCustomers = lastMonthCustomers;
	}
	
	
	
	
	
	
	
}
