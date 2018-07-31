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

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.cart.FinalizedItem;
import qetaa.jsf.dashboard.model.cart.FinalizedItemsHolder;
import qetaa.jsf.dashboard.model.cart.ManualQuotationVendor;
import qetaa.jsf.dashboard.model.cart.Quotation;
import qetaa.jsf.dashboard.model.cart.QuotationItem;
import qetaa.jsf.dashboard.model.cart.QuotationVendorItem;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.user.User;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;
import qetaa.jsf.dashboard.model.vendor.Vendor;

@Named
@ViewScoped
public class QuotationViewBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Cart cart;
	private List<Quotation> quotations;
	private QuotationVendorItem selectedVendorItem;
	private List<FinalizedItem> allResponses;
	private List<FinalizedItem> finalizedItems;
	private QuotationItem selectedQuotationItem;
	private List<Vendor> makeVendors;
	private List<CartReview> reviews;
	private CartReview review;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("cart-id");
			if (s == null)
				throw new Exception();
			initCart(s);
			this.quotations = new ArrayList<>();
			initQuotations();
			initCartVariables();
			review = new CartReview();

		} catch (Exception ex) {
			ex.printStackTrace();
			Helper.redirect("quotations-monitoring");
		}
	}

	private void initCart(String param) throws Exception {
		Long id = Long.parseLong(param);
		// get the cart
		Response r = reqs.getSecuredRequest(AppConstants.getCart(id));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
			if(!(cart.getStatus() == 'W' || cart.getStatus() == 'N' || cart.getStatus() == 'Q' || cart.getStatus() == 'A' || cart.getStatus() == 'R')) {
				throw new Exception();
			}
		} else
			throw new Exception();
	}

	private void initQuotations() {
		Response r = reqs.getSecuredRequest(AppConstants.getCartQuotations(this.cart.getId()));
		if (r.getStatus() == 200) {
			this.quotations = r.readEntity(new GenericType<List<Quotation>>() {
			});
			for (Quotation q : quotations) {
				Response r2 = reqs.getSecuredRequest(AppConstants.getUser(q.getCreatedBy()));
				if (r2.getStatus() == 200) {
					User user = r2.readEntity(User.class);
					q.setCreatedByObject(user);
				}
			}
		}
	}

	public void initCartVariables() throws InterruptedException {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[6];
		Thread t1 = initModelYear(cart, header);
		Thread t2 = initCustomer(cart, header);
		Thread t3 = initCity(cart, header);
		Thread t4 = initFinalizedCart(cart, header);
		Thread t5 = initAllResponses(cart, header);
		Thread t6 = initReviews(cart, header);
		threads[0] = t1;
		threads[1] = t2;
		threads[2] = t3;
		threads[3] = t4;
		threads[4] = t5;
		threads[5] = t6;
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		for (int i = 0; i < threads.length; i++)
			threads[i].join();

	}
	
	private Thread initReviews(Cart cart, String header) {
		this.reviews = new ArrayList<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartReviews(cart.getId()), header);
					if (r.getStatus() == 200) {
						QuotationViewBean.this.reviews = r.readEntity(new GenericType<List<CartReview>>() {
						});
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initFinalizedCart(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if (cart.getStatus() == 'R') {
					try {
						Response r = PojoRequester.getSecuredRequest(AppConstants.getFinalizedItems(cart.getId()),
								header);
						if (r.getStatus() == 200) {
							finalizedItems = r.readEntity(new GenericType<List<FinalizedItem>>() {
							});
						}
					} catch (Exception ex) {

					}
				}
			}
		});
		return thread;
	}

	private Thread initModelYear(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getModelYear(cart.getVehicleYear()),
							header);
					if (r.getStatus() == 200) {
						cart.setModelYear(r.readEntity(ModelYear.class));
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initCustomer(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCustomer(cart.getCustomerId()),
							header);
					if (r.getStatus() == 200) {
						cart.setCustomer(r.readEntity(Customer.class));
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

	private Thread initAllResponses(Cart cart, String header) {
		allResponses = new ArrayList<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if (cart.getStatus() == 'R') {
					try {
						Response r = PojoRequester.getSecuredRequest(AppConstants.getFinalizedItemsFull(cart.getId()),
								header);
						if (r.getStatus() == 200) {
							allResponses = r.readEntity(new GenericType<List<FinalizedItem>>() {
							});
						}

					} catch (Exception ex) {

					}
				}
			}
		});
		return thread;
	}

	public void addManualQuotationVendor(QuotationItem qitem, int vendorId) {
		ManualQuotationVendor manual = new ManualQuotationVendor();
		manual.setVendorId(vendorId);
		manual.setCreatedBy(loginBean.getUserHolder().getUser().getId());
		manual.setQuotationItem(qitem);
		Response r = reqs.postSecuredRequest(AppConstants.POST_MANUAL_QUOTATION_VENDOR, manual);
		if (r.getStatus() == 201) {
			Helper.redirect("view-quotation?cart-id=" + this.cart.getId());
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	private void prepareCartReview() {
		review.setStage(2);
		review.setCartId(this.cart.getId());
		review.setReviewerId(this.loginBean.getUserHolder().getUser().getId());
		review.setStatus(review.getStatusFromActionValue());
	}
	
	public void submitReview() {
		prepareCartReview();
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, this.review);
		if (r.getStatus() == 200) {
			if (review.getActionValue() == 'X') {
				Helper.redirect("quotations");
			} else {
				Helper.redirect("view-quotation?cart-id=" + this.cart.getId());
			}
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public double getFinalizedTotalCost() {
		if (finalizedItems != null) {
			double total = 0;
			for (FinalizedItem fitem : this.finalizedItems) {
				total += fitem.getTotalCostPrice();
			}
			return total;
		} else
			return 0;
	}

	public double getFinalizedTotalSales() {
		if (finalizedItems != null) {
			double total = 0;
			for (FinalizedItem fitem : this.finalizedItems) {
				total += fitem.getTotalSalesPrice();
			}
			return total;
		} else
			return 0;
	}

	public boolean isFirstColumnShown(Quotation quotation) {
		boolean found = false;
		for (QuotationItem qitem : quotation.getQuotationItems()) {
			if (qitem.getVendorItems() != null && qitem.getVendorItems().size() > 0) {
				found = true;
				break;
			}
		}
		return found;
	}

	public boolean isSecondColumnShown(Quotation quotation) {
		boolean found = false;
		for (QuotationItem qitem : quotation.getQuotationItems()) {
			if (qitem.getVendorItems() != null && qitem.getVendorItems().size() > 1) {
				found = true;
				break;
			}
		}
		return found;
	}

	public boolean isThirdColumnShown(Quotation quotation) {
		boolean found = false;
		for (QuotationItem qitem : quotation.getQuotationItems()) {
			if (qitem.getVendorItems() != null && qitem.getVendorItems().size() > 2) {
				found = true;
				break;
			}
		}
		return found;
	}

	public boolean isFourthColumnShown(Quotation quotation) {
		boolean found = false;
		for (QuotationItem qitem : quotation.getQuotationItems()) {
			if (qitem.getVendorItems() != null && qitem.getVendorItems().size() > 3) {
				found = true;
				break;
			}
		}
		return found;
	}

	public void selectQuotationItem(QuotationItem qitem) {
		this.selectedQuotationItem = qitem;
		initMakeVendors();
	}

	private void initMakeVendors() {
		Response r = reqs.getSecuredRequest(AppConstants.getMakeVendors(this.cart.getMakeId()));
		if (r.getStatus() == 200) {
			this.makeVendors = r.readEntity(new GenericType<List<Vendor>>() {
			});
		} else {

		}

	}

	public void submitFinalized() {
		if (cart.getDeliveryFees() > 0) {
			FinalizedItemsHolder holder = new FinalizedItemsHolder();
			holder.setFinalizedItems(finalizedItems);
			holder.setCartId(this.cart.getId());
			holder.setDeliveryFees(this.cart.getDeliveryFees());
			holder.setCreatedBy(loginBean.getUserHolder().getUser().getId());
			Response r = reqs.postSecuredRequest(AppConstants.APPROVE_QUOTATION, holder);
			if (r.getStatus() == 200) {
				Helper.redirect("quotations");
			} else if (r.getStatus() == 409) {
				Helper.redirect("quotations");
			} else {
				Helper.addErrorMessage("An error occured");
			}
		} else {
			Helper.addErrorMessage("Enter delivery cost");
		}

	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public List<Quotation> getQuotations() {
		return quotations;
	}

	public void setQuotations(List<Quotation> quotations) {
		this.quotations = quotations;
	}

	public QuotationVendorItem getSelectedVendorItem() {
		return selectedVendorItem;
	}

	public void setSelectedVendorItem(QuotationVendorItem selectedVendorItem) {
		this.selectedVendorItem = selectedVendorItem;
	}

	public List<FinalizedItem> getAllResponses() {
		return allResponses;
	}

	public void setAllResponses(List<FinalizedItem> allResponses) {
		this.allResponses = allResponses;
	}

	public List<FinalizedItem> getFinalizedItems() {
		return finalizedItems;
	}

	public void setFinalizedItems(List<FinalizedItem> finalizedItems) {
		this.finalizedItems = finalizedItems;
	}

	public QuotationItem getSelectedQuotationItem() {
		return selectedQuotationItem;
	}

	public void setSelectedQuotationItem(QuotationItem selectedQuotationItem) {
		this.selectedQuotationItem = selectedQuotationItem;
	}

	public List<Vendor> getMakeVendors() {
		return makeVendors;
	}

	public void setMakeVendors(List<Vendor> makeVendors) {
		this.makeVendors = makeVendors;
	}
	
	public List<CartReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<CartReview> reviews) {
		this.reviews = reviews;
	}
	
	public CartReview getReview() {
		return review;
	}

	public void setReview(CartReview review) {
		this.review = review;
	}

}
