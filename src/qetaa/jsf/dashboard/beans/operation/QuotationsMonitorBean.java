package qetaa.jsf.dashboard.beans.operation;

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
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

@Named
@ViewScoped
public class QuotationsMonitorBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cart> carts;
	private Cart selectedCart;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		try {
			this.selectedCart = new Cart();
			initCarts();
			initCartVariables();
		} catch (Exception ex) {

		}
	}

	private void initCarts() {
		carts = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_QUOTATIONS_WAITING);
		if (r.getStatus() == 200) {
			this.carts = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {

		}
	}

	public void initCartVariables() throws InterruptedException {
		String header = reqs.getSecurityHeader();

		Thread[] mainThreads = new Thread[carts.size()];
		int index = 0;

		for (Cart cart : carts) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[2];
					threads[0] = initCity(cart, header);
					threads[1] = initPromoCode(cart, header);
					for (int i = 0; i < threads.length; i++)
						try {
							threads[i].start();
							threads[i].join();
						} catch (InterruptedException e) {

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

	private Thread initPromoCode(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (cart.getPromotionCode() != null) {
						Response r = PojoRequester.getSecuredRequest(AppConstants.getPromoCode(cart.getPromotionCode()),
								header);
						if (r.getStatus() == 200) {
							cart.setPromoCodeObject(r.readEntity(PromotionCode.class));
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

}
