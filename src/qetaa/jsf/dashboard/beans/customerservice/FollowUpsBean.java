package qetaa.jsf.dashboard.beans.customerservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.CitiesBean;
import qetaa.jsf.dashboard.beans.master.UsersBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.user.User;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;
  
@Named
@ViewScoped
public class FollowUpsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Cart> carts;
	private Cart selectedCart;

	@Inject
	private Requester reqs;
	@Inject
	private CitiesBean citiesBean;
	
	@Inject
	private UsersBean usersBean;

	@PostConstruct
	public void init() {
		carts = new ArrayList<>();
		initCarts();
		try {
			initCartVariables();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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

	public void initCartVariables() throws InterruptedException {
		Thread[] mainThreads = new Thread[carts.size()];
		int index = 0;
		List<City> allcities = citiesBean.getCities();
		List<User> allUsers = usersBean.getUsers();
		List<Customer> allCustomers = initAllCustomers();
		List<ModelYear> allModelYears = initAllModelYears();
		for (Cart cart : carts) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[4];
					threads[0] = initCity(allcities, cart);
					threads[1] = initLastReviewer(allUsers, cart);
					threads[2] = initCustomer(allCustomers, cart);
					threads[3] = initModelYear(allModelYears, cart);
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
	
	private List<ModelYear> initAllModelYears() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_MODEL_YEARS);
		if(r.getStatus() == 200) {
			List<ModelYear> l = r.readEntity(new GenericType<List<ModelYear>>() {});
			return l;
		}
		else {
			return new ArrayList<>();
		}
	}


	private Thread initLastReviewer(List<User> users, Cart cart) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if(!cart.getReviews().isEmpty()) {
						int index = cart.getReviews().size() -1;
						for (User c : users) {
							if (c.getId() == cart.getReviews().get(index).getReviewerId()) {
								cart.getReviews().get(index).setReviewer(c);
								break;
							}
						}
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
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
	
	private Thread initModelYear(List<ModelYear> myears, Cart cart) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (ModelYear c : myears) {
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
		Response r = reqs.getSecuredRequest(AppConstants.GET_FOLLOW_UP_CARTS);
		if (r.getStatus() == 200) {
			this.carts = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {

		}
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public Cart getSelectedCart() {
		return selectedCart;
	}

	public void setSelectedCart(Cart selectedCart) {
		this.selectedCart = selectedCart;
	}

}
