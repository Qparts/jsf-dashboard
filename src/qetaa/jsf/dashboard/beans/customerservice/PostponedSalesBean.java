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
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.model.cart.ApprovedQuotationItem;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.location.City;

@Named
@ViewScoped
public class PostponedSalesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Cart> carts;
	private Cart selectedCart;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		carts = new ArrayList<>();
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


	private void initCartVariables() throws InterruptedException {
		String header = reqs.getSecurityHeader();
		Thread[] mainThreads = new Thread[carts.size()];
		int index = 0;
		for (Cart cart : carts) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[3];
					Thread t1 = initReviews(cart, header);
					Thread t2 = initCity(cart, header);
					Thread t3 = initApprovedItems(cart, header);
					threads[0] = t1;
					threads[1] = t2;
					threads[2] = t3;
					t1.start();
					t2.start();
					t3.start();
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

	private Thread initReviews(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartReviews(cart.getId()), header);
					if (r.getStatus() == 200) {
						List<CartReview> reviews = r.readEntity(new GenericType<List<CartReview>>() {
						});
						cart.setReviews(reviews);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initApprovedItems(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCustomerApprovedItems(cart.getId()),
							header);
					if (r.getStatus() == 200) {
						List<ApprovedQuotationItem> approved = r
								.readEntity(new GenericType<List<ApprovedQuotationItem>>() {
								});
						if (approved == null) {
							cart.setApprovedItems(new ArrayList<>());
						} else {
							cart.setApprovedItems(approved);
						}
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initCity(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCity(cart.getCityId()), header);
					if (r.getStatus() == 200) {
						cart.setCity(r.readEntity(City.class));
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private void initCarts() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_POSTPONED_CARTS);
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


	public Cart getSelectedCart() {
		return selectedCart;
	}

	public void setSelectedCart(Cart selectedCart) {
		this.selectedCart = selectedCart;
	}
	
	

}
