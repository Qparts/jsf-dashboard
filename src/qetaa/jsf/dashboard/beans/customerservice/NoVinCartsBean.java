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
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.location.City;

@Named(value = "novincartsBean")
@ViewScoped
public class NoVinCartsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cart> carts;
	private Cart selectedCart;
	private CartReview cartReview;
	
	@Inject
	private CitiesBean citiesBean;
	
	@Inject
	private Requester reqs;

	@PostConstruct
	private void init(){
		this.selectedCart = new Cart();
		cartReview = new CartReview();
		try {
			initCarts();
			initCartVariables();
		}catch(InterruptedException ex) {
			
		}
	}

	private void initCarts() {
		carts = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_NO_VIN_CARTS);
		if (r.getStatus() == 200) {
			this.carts = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {

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
		List<Customer> allCustomers = initAllCustomers();
		for (Cart cart : carts) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[2];
					Thread t1 = initCity(allcities, cart);
					Thread t2 = initCustomer(allCustomers, cart);
					threads[0] = t1;
					threads[1] = t2;
					t1.start();
					t2.start();
					for (int i = 0; i < threads.length; i++)
						try {
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

	public CartReview getCartReview() {
		return cartReview;
	}

	public void setCartReview(CartReview cartReview) {
		this.cartReview = cartReview;
	}
	
	

}
