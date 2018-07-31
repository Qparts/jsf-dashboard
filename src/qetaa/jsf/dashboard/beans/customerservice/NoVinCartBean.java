package qetaa.jsf.dashboard.beans.customerservice;

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

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.CitiesBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;

@Named(value = "novincartBean")
@ViewScoped
public class NoVinCartBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Cart cart;
	private CartReview cartReview;
	private List<Cart> previousCarts;
	private List<ModelYear> modelYears;

	@Inject
	private Requester reqs;
	
	@Inject
	private LoginBean loginBean;

	@Inject
	private CitiesBean citiesBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("cart");
			initCart(s);
			initCity();
			initPreviousCarts();
			cartReview = new CartReview();
			modelYears = new ArrayList<>();
		} catch (Exception ex) {
			Helper.redirect("no-vin-carts");
		}
	}
	
	private void initPreviousCarts() {
		Response r = reqs.getSecuredRequest(AppConstants.getPreviousCarts(cart.getCustomerId(), cart.getId()));
		if(r.getStatus() == 200) {
			this.previousCarts = r.readEntity(new GenericType<List<Cart>>() {});
			if(!previousCarts.isEmpty()) {
				Response r2 = reqs.getSecuredRequest(AppConstants.GET_ALL_MODEL_YEARS);
				if(r2.getStatus() == 200) {
					modelYears = r2.readEntity(new GenericType<List<ModelYear>>() {});
					for(Cart c : previousCarts) {
						for(ModelYear my : modelYears) {
							if(my.getId() == c.getVehicleYear()) {
								c.setModelYear(my);
								break;
							}
						}
					}
				}
				
			}
			
		}else {
			Helper.addErrorMessage("an error occured");
		}
	}

	private void initCity() {
		for (City city : citiesBean.getCities()) {
			if (city.getId() == cart.getCityId()) {
				cart.setCity(city);
				break;
			}
		}
	}

	private void initCart(String param) throws Exception {
		cart = new Cart();
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getNoVinCart(id));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
		} else {
			throw new Exception();
		}
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public CartReview getCartReview() {
		return cartReview;
	}

	public void setCartReview(CartReview cartReview) {
		this.cartReview = cartReview;
	}

	public void saveChanges() {
		if (cart.getVin().length() == 17) {
			cart.setVin(cart.getVin().toUpperCase());
			cart.setNoVin(false);
			Response r = reqs.putSecuredRequest(AppConstants.PUT_EDIT_CART, this.cart);
			if (r.getStatus() == 201) {
				Helper.redirect("no-vin-carts");
			} else {
				Helper.addErrorMessage("an error occured");
			}
		} else {
			Helper.addErrorMessage("VIN must be 17 digits");
		}
	}
	
	
	private void prepareCartReview() {
		cartReview.setStage(1);
		cartReview.setCartId(cart.getId());
		cartReview.setReviewerId(loginBean.getUserHolder().getUser().getId());
		cartReview.setStatus(cartReview.getStatusFromActionValue());
	}

	public void submitReview() {
		prepareCartReview();
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, this.cartReview);
		if (r.getStatus() == 200) {
			if (cartReview.getStatus() == 'C') {
				Helper.redirect("no-vin-carts");
			} else {
				Helper.redirect("no-vin-cart?cart=" + cart.getId());
			}

		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public String getVinImageDirectory() {
		Calendar c = Calendar.getInstance();
		c.setTime(cart.getCreated());
		return c.get(Calendar.YEAR) + "/"+ (c.get(Calendar.MONTH) + 1) + "/"+ c.get(Calendar.DAY_OF_MONTH);
	}

	public List<Cart> getPreviousCarts() {
		return previousCarts;
	}

	public void setPreviousCarts(List<Cart> previousCarts) {
		this.previousCarts = previousCarts;
	}
	
	

}
