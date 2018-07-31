package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.customerservice.CustomerBean;
import qetaa.jsf.dashboard.beans.master.RegionsBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.ApprovedQuotationItem;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.location.Region;

@Named
@SessionScoped 
public class SearchCartBean implements Serializable {
	private static final long serialVersionUID = 1L;

	// search criteria
	private Long cartId;
	private String mobile;
	private Long customerId;
	private int cityId;
	private Region selectedRegion;
	private int selectedRegionId;
	private List<Cart> carts;
	private Cart selectedCart;
	private CartReview cartReview;

	@Inject
	private RegionsBean regionBean;

	@Inject
	private CustomerBean customerBean;

	@Inject
	private Requester reqs;
	
	@Inject 
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		selectedRegion = new Region();
		cartReview = new CartReview();
	}

	public void chooseRegion() {
		if (this.selectedRegionId == 0) {
			this.cityId = 0;
			this.selectedRegion = new Region();
		} else {
			for (Region region : regionBean.getRegions()) {
				if (this.selectedRegionId == region.getId()) {
					this.selectedRegion = region;
					break;
				}
			}
		}
	}

	public void search() {
		carts = new ArrayList<>();
		this.cartReview = new CartReview();
		if (null != customerId && customerId > 0) {
		} else {
			customerId = 0L;
		}
		// find customer id from mobile
		if (null != mobile && mobile.trim().length() > 0) {
			customerBean.setSearch(mobile);
			customerBean.searchCustomers();
			;
			if (customerBean.getCustomers().size() == 1) {
				customerId = customerBean.getCustomers().get(0).getId();
			}
		}

		if (this.cartId == null || this.cartId == 0L) {
			cartId = 0L;
		}

		if ((this.cartId == 0L) && (this.customerId == 0L)) {
			Helper.addErrorMessage("Enter at least one search item");
		} else {
			Response r = reqs.getSecuredRequest(AppConstants.searchCart(cartId, customerId));
			if (r.getStatus() == 200) {
				carts = r.readEntity(new GenericType<List<Cart>>() {
				});
				try {
					initCartVariables();
				} catch (Exception ex) {

				}
			}
		}

	}
	
	public void openClosedCart() {
		cartReview = new CartReview();
		cartReview.setActionValue('B');
		cartReview.setCartId(this.selectedCart.getId());
		cartReview.setCreated(new Date());
		cartReview.setReviewerId(this.loginBean.getUserHolder().getUser().getId());
		cartReview.setReviewText("Open Closed Cart");
		cartReview.setStatus('A');
		cartReview.setStage(5);
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, cartReview);
		if(r.getStatus() == 200) {
			if(selectedCart.getStatus() == 'X') {
				if(selectedCart.isNoVin() || this.selectedCart.isVinImage()) {
					selectedCart.setStatus('N');
					Response r2 = reqs.postSecuredRequest(AppConstants.PUT_EDIT_CART, this.selectedCart);
					if(r2.getStatus() == 201) {
						selectedCart.setStatus('N');
					}
				}
			}
			else {
				selectedCart.setStatus('S');
			}
			
			Helper.addInfoMessage("Cart " + selectedCart.getId() + " is now open");
			
		}
		else {
			Helper.addErrorMessage("an error occured");
		}
	}

	private void initCartVariables() throws InterruptedException {
		String header = reqs.getSecurityHeader();
		Thread[] mainThreads = new Thread[carts.size()];
		int index = 0;
		for (Cart cart : carts) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[4];
					threads[0] = ThreadRunner.initReviews(cart, header);
					threads[1] = ThreadRunner.initCity(cart, header);
					threads[2] = initApprovedItems(cart, header);
					threads[3] = ThreadRunner.initPromoCode(cart, header);
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

	

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public Region getSelectedRegion() {
		return selectedRegion;
	}

	public void setSelectedRegion(Region selectedRegion) {
		this.selectedRegion = selectedRegion;
	}

	public int getSelectedRegionId() {
		return selectedRegionId;
	}

	public void setSelectedRegionId(int selectedRegionId) {
		this.selectedRegionId = selectedRegionId;
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

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}
