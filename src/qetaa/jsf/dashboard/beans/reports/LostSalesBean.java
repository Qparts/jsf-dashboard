package qetaa.jsf.dashboard.beans.reports;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.CitiesBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;

@Named
@SessionScoped
public class LostSalesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Cart> carts;
	private int year;
	private int month;
	private Cart selectedCart;

	@Inject
	private Requester reqs;
	
	@Inject
	private CitiesBean citiesBean;

	@PostConstruct
	private void init() {
		Calendar c = Calendar.getInstance();
		this.month = c.get(Calendar.MONTH);
		this.year = c.get(Calendar.YEAR);
		carts = new ArrayList<>();
	}

	public void generateReport() {
		initCarts();
		try {
			initCartVariables();
		} catch (Exception ex) {

		}
	}

	public double salesTotal() {
		double total = 0;
		for (Cart cart : carts) {
			if (cart.getStatus() == 'S') {
				total = total + (cart.totalSales() + cart.getDeliveryFees()
						+ (cart.totalSales() + cart.getDeliveryFees()) * cart.getVatPercentage());
			}
		}
		return total;
	}
	
	private List<ModelYear> initAllModelYears(){
		Response r2 = reqs.getSecuredRequest(AppConstants.GET_ALL_MODEL_YEARS);
		if(r2.getStatus() == 200) {
			List<ModelYear> mys = r2.readEntity(new GenericType<List<ModelYear>>() {});
			return mys;
		}
		else return new ArrayList<>();
	}
	private List<Customer> initAllCustomers() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_ALL_CUSTOMERS_FROM_IDS, Helper.getCustomerIds(carts));
		if(r.getStatus() == 200) {
			List<Customer> l = r.readEntity(new GenericType<List<Customer>>() {});
			return l;
		}
		else {
			return new ArrayList<>();
		}
	}



	private void initCartVariables() throws InterruptedException {
		Thread[] mainThreads = new Thread[carts.size()];
		List<City> allcities = citiesBean.getCities();
		List<Customer> allCustomers = initAllCustomers();
		List<ModelYear> allModelYears = initAllModelYears();
		int index = 0;
		for (Cart cart : carts) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[3];
					Thread t1 = initCity(allcities, cart);
					Thread t2 = initCustomer(allCustomers, cart);
					Thread t3 = initModelYear(allModelYears, cart);
					threads[0] = t1;
					threads[1] = t2;
					threads[2] = t3;
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
			mainThreads[i].start();
			mainThreads[i].join();
		}
	}
	
	
	private Thread initCity(List<City> cities, Cart cart) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (City c : cities) {
						if (c.getId() == cart.getCityId()) {
							cart.setCity(c);
							break;
						}
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
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
	
	private Thread initModelYear(List<ModelYear> mys, Cart cart) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (ModelYear c : mys) {
						if (c.getId() == cart.getVehicleYear()) {
							cart.setModelYear(c);
							break;
						}
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private void initCarts() {
		carts = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.getLostSalesReport(year, month));
		if (r.getStatus() == 200) {
			this.carts = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
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

	public Cart getSelectedCart() {
		return selectedCart;
	}

	public void setSelectedCart(Cart selectedCart) {
		this.selectedCart = selectedCart;
	}
	
	

}
