package qetaa.jsf.dashboard.beans.sales;

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
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesPayment;

@Named
@ViewScoped
public class ReceivablesBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<Sales> sales;
	private Sales selectedSales;
	private SalesPayment salesPayment;
	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;
	
	@PostConstruct
	private void init() {
		salesPayment = new SalesPayment();
		this.sales = new ArrayList<>();
		this.selectedSales = new Sales();
		initSales();
		initVariables();
	}
	
	
	private void initSales() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_SALES_RECEIVABLES);
		if(r.getStatus() == 200) {
			sales = r.readEntity(new GenericType<List<Sales>>() {});
		}
	}
	
	
	
	
	private List<Customer> initAllCustomers() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_ALL_CUSTOMERS_FROM_IDS, Helper.getCustomerIdsFromSales(sales));
		if(r.getStatus() == 200) {
			List<Customer> l = r.readEntity(new GenericType<List<Customer>>() {});
			return l;
		}
		else {
			return new ArrayList<>();
		}
	}
	
	private void initVariables() {
		List<Customer> allCustomers = initAllCustomers();
		Thread[] mainThreads = new Thread[sales.size()];
		int index = 0;
		for (Sales sale : sales) {
			sale.setCart(new Cart());
			sale.getCart().setCustomerId(sale.getCustomerId());
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[1];
					threads[0] = initCustomer(allCustomers, sale.getCart());
					for (int i = 0; i < threads.length; i++)
						try {
							threads[i].start();
							threads[i].join();
						} catch (InterruptedException e) {
							e.printStackTrace();	
						}
				}
			});
			mainThreads[index] = t;
			index++;
		}
		for (int i = 0; i < mainThreads.length; i++) {
			try {
				mainThreads[i].start();
				mainThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Thread initCustomer(List<Customer> customers, Cart cart) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (Customer c : customers) {
						if (c.getId() == cart.getCustomerId()) {
							cart.setCustomer(c);
							break;
						}
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	public double totalOustanding() {
		double total = 0;
		for(Sales sale: sales) {
			total += sale.getNetTotalSales() - sale.getPaid() + sale.getReturned();
		}
		return total;
	}
	
	public void createSalesPayment() {
		if(this.salesPayment.getAmount() <= this.selectedSales.getOutstanding()) {
			salesPayment.setMethod('R');
			//salesPayment.setPaidBy(loginBean.getUserHolder().getUser().getId());
			salesPayment.setCreditFees(null);
			salesPayment.setProvider(null);
			salesPayment.setSales(this.selectedSales);
			Response r = reqs.postSecuredRequest(AppConstants.POST_SALES_PAYMENT, salesPayment);
			if(r.getStatus() == 201) {
				Helper.redirect("receivables");
			}
			else {
				Helper.addErrorMessage("An error occured");
			}
		}
		else {
			Helper.addErrorMessage("Outstanding amount exceeded");
		}
	}
	
	public void chooseSales(Sales sales) {
		this.setSelectedSales(sales);
		this.salesPayment = new SalesPayment();
	}

	public List<Sales> getSales() {
		return sales;
	}

	public void setSales(List<Sales> sales) {
		this.sales = sales;
	}

	public Sales getSelectedSales() {
		return selectedSales;
	}

	public void setSelectedSales(Sales selectedSales) {
		this.selectedSales = selectedSales;
	}

	public SalesPayment getSalesPayment() {
		return salesPayment;
	}

	public void setSalesPayment(SalesPayment salesPayment) {
		this.salesPayment = salesPayment;
	}


	
	
}
