package qetaa.jsf.dashboard.beans.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.helpers.WebsocketLinks;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartAssignment;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

@Named
@ViewScoped
public class QuotationsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cart> carts;
	private Cart selectedCart;
	private long mergingCartId;
	private CartAssignment cartAssignment;

	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			this.cartAssignment = new CartAssignment();
			this.selectedCart = new Cart();
			initCarts();
			initCartVariables();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public void assign() {
		this.cartAssignment.setAssignedBy(this.loginBean.getLoggedUserId());
		this.cartAssignment.setCartId(this.selectedCart.getId());
		this.cartAssignment.setStage(this.selectedCart.getStatus());
		Response r = reqs.postSecuredRequest(AppConstants.POST_ASSIGN_CART_TO_USER, cartAssignment);
		if(r.getStatus() == 200) {
			this.selectedCart.setActiveAssignment(r.readEntity(CartAssignment.class));
		}
		else if (r.getStatus() == 409) {
			Helper.addErrorMessage("This cart is already assigned! please refresh");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public void unassign() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", this.getSelectedCart().getActiveAssignment().getAssignedTo());
		map.put("cartId", this.selectedCart.getId());
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UNASSIGN_CART, map);
		if(r.getStatus() == 201) {
			Helper.addInfoMessage("Unassigned");
			this.selectedCart.setActiveAssignment(null);
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public void changeOccured() {
		try {
			Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			String data = map.get("param");
			if (data != null) {
				String[] messages = data.split(",");
				String function = messages[0];
				String value = messages[1];
				switch (function) {
				case "new cart":
					loadNewCart(Long.parseLong(value));
					break;
				case "assignment changed":
					loadAssignmentChangedCart(Long.parseLong(value));
					break;
				case "update cart":
					loadUpdatedCart(Long.parseLong(value));
					break;
				case "archive cart":
					loadArchivedCart(Long.parseLong(value));
					break;
				case "submit cart":
					loadSubmitedCart(Long.parseLong(value));
					break;
				case "not available cart":
					loadNotAvailableCart(Long.parseLong(value));
					break;
				case "edit cart":
					loadEditCart(Long.parseLong(value));
					break;
				default:
					System.out.println("default");

				}
			}
		} catch (Exception ex) {

		}

	}
	
	private void loadSubmitedCart(long cartId) {
		for (Cart cart : carts) {
			if (cart.getId() == cartId) {
				carts.remove(cart);
				Helper.addInfoMessage("Cart Submitted " + cart.getId());
				break;
			}
		}
	}

	private void loadArchivedCart(long cartId) {
		for (Cart cart : carts) {
			if (cart.getId() == cartId) {
				carts.remove(cart);
				Helper.addErrorMessage("Cart archived " + cart.getId());
				break;
			}
		}
	}
	
	private void loadNotAvailableCart(long cartId) {
		try {
			Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
			if (r.getStatus() == 200) {
				Cart reloaded = r.readEntity(Cart.class);
				for(int i=0; i < carts.size(); i++) {
					if (carts.get(i).getId() == reloaded.getId()) {
						carts.set(i, reloaded);
						Helper.addErrorMessage("Items not available" + carts.get(i).getId());
						break;
					}
				}
			}
		} catch (Exception ex) {

		}
	}

	private void loadUpdatedCart(long cartId) {
		try {
			Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
			if (r.getStatus() == 200) {
				Cart reloaded = r.readEntity(Cart.class);
				for(int i=0; i < carts.size(); i++) {
					if (carts.get(i).getId() == reloaded.getId()) {
						carts.set(i, reloaded);
						Helper.addWarMessage("Cart updated " + carts.get(i).getId());
						break;
					}
				}
			}
		} catch (Exception ex) {

		}
	}
	
	private void loadEditCart(long cartId) {
		try {
			Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
			if (r.getStatus() == 200) {
				Cart reloaded = r.readEntity(Cart.class);
				for(int i=0; i < carts.size(); i++) {
					if (carts.get(i).getId() == reloaded.getId()) {
						carts.set(i, reloaded);
						Helper.addWarMessage("Cart edit requested " + carts.get(i).getId());
						break;
					}
				}
			}
		} catch (Exception ex) {

		}
	}
	

	private void loadAssignmentChangedCart(long cartId) {
		try {
			Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
			if (r.getStatus() == 200) {
				Cart reloaded = r.readEntity(Cart.class);
				for(int i=0; i < carts.size(); i++) {
					if (carts.get(i).getId() == reloaded.getId()) {
						carts.set(i, reloaded);
						Helper.addWarMessage("Cart assignment updated " + (carts.get(i).getId()));
						break;
					}
				}
			}
		} catch (Exception ex) {

		}
	}
	

	private void loadNewCart(long cartId) {
		try {
			boolean found = false;
			for (Cart cart : carts) {
				if (cart.getId() == cartId) {
					found = true;
					break;
				}
			}
			if (!found) {
				Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
				if (r.getStatus() == 200) {
					Cart cart = r.readEntity(Cart.class);
					this.carts.add(cart);
					Helper.addInfoMessage("New cart " + cart.getId());
				}
			}

		} catch (Exception ex) {
		}
	}

	public void mergeCart() {
		Response r = reqs.putSecuredRequest(AppConstants.putMergeCarts(selectedCart.getId(), mergingCartId,
				loginBean.getUserHolder().getUser().getId()), null);
		if (r.getStatus() == 201) {
			Helper.redirect("quotations");
		} else {
			Helper.addErrorMessage("An error occured");
		}

	}

	public List<Cart> getSimilarCarts() {
		List<Cart> foundCarts = new ArrayList<>();
		if (carts != null) {
			for (Cart c : carts) {
				if (c.getCustomerId() == selectedCart.getCustomerId() && c.getVin().equals(selectedCart.getVin())
						&& c.getId() != selectedCart.getId()) {
					foundCarts.add(c);
				}
			}
		}
		return foundCarts;
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
	
	public void chooseCart(Cart cart) {
		this.selectedCart = cart;
		this.cartAssignment = new CartAssignment();
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

	public long getMergingCartId() {
		return mergingCartId;
	}

	public void setMergingCartId(long mergingCartId) {
		this.mergingCartId = mergingCartId;
	}

	public CartAssignment getCartAssignment() {
		return cartAssignment;
	}

	public void setCartAssignment(CartAssignment cartAssignment) {
		this.cartAssignment = cartAssignment;
	}
	
	public String getQuotationsWSLink() {
		return WebsocketLinks.getQuotationsLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
	}

	
}
