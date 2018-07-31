package qetaa.jsf.dashboard.beans.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.VehiclesBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartItem;
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
import qetaa.jsf.dashboard.model.vehicle.Make;
import qetaa.jsf.dashboard.model.vehicle.Model;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;
import qetaa.jsf.dashboard.model.vendor.Vendor;
 
@Named
@ViewScoped
public class QuotationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Cart cart;
	private List<Quotation> quotations;
	private int[] quantityArray;
	private QuotationVendorItem selectedVendorItem;
	private QuotationItem selectedQuotationItem;
	private List<FinalizedItem> finalizedItems;
	private List<FinalizedItem> allResponses;
	private List<Vendor> makeVendors;
	private CartReview review;
	private List<CartReview> reviews;
	private Make selectedMake;
	private int selectedMakeId;
	private Model selectedModel;
	private int selectedModelId;
	private ModelYear selectedModelYear;
	private int selectedModelYearId;

	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;
	@Inject
	private VehiclesBean vehiclesBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("cart-id");
			if (s == null)
				throw new Exception();
			initCart(s);
			this.quotations = new ArrayList<>();
			initQuotations();
			initNewQuotation();
			initCartVariables();
			review = new CartReview();
			selectedVendorItem = new QuotationVendorItem();
			this.selectedMake = new Make();
			this.selectedModel = new Model();
			this.selectedModelYear = new ModelYear();

		} catch (Exception ex) {
			ex.printStackTrace();
			Helper.redirect("quotations");
		}
	}

	public int[] getQuantityArrayOfVendor() {
		if (this.selectedVendorItem != null) {
			for (Quotation q : quotations) {
				for (QuotationItem qi : q.getQuotationItems()) {
					if (qi.getId() == this.selectedVendorItem.getQuotationItemId()) {
						int[] quantityArray = new int[qi.getQuantity()];
						for (int i = 0; i < quantityArray.length; i++) {
							quantityArray[i] = i + 1;
						}
						return quantityArray;
					}
				}
			}
		}
		return new int[0];
	}

	public void editCart() {
		this.cart.getVin().toUpperCase();
		this.cart.setVehicleYear(this.selectedModelYearId);
		Response r = reqs.putSecuredRequest(AppConstants.PUT_EDIT_CART, cart);
		if (r.getStatus() == 201) {
			Helper.redirect("process_quotation?cart-id=" + cart.getId());
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
				Helper.redirect("process_quotation?cart-id=" + this.cart.getId());
			}
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void archiveCart() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_ARCHIVE_CART, cart);
		if (r.getStatus() == 201) {
			Helper.redirect("quotations");
		} else {
			Helper.addErrorMessage("An eror occured");
		}
	}

	public void addManualQuotationVendor(QuotationItem qitem, int vendorId) {
		ManualQuotationVendor manual = new ManualQuotationVendor();
		manual.setVendorId(vendorId);
		manual.setCreatedBy(loginBean.getUserHolder().getUser().getId());
		manual.setQuotationItem(qitem);
		Response r = reqs.postSecuredRequest(AppConstants.POST_MANUAL_QUOTATION_VENDOR, manual);
		if (r.getStatus() == 201) {
			Helper.redirect("process_quotation?cart-id=" + this.cart.getId());
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void initCartVariables() throws InterruptedException {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[7];
		threads[0] = initModelYear(cart, header);
		threads[1] = initCustomer(cart, header);
		threads[2] = initCity(cart, header);
		threads[3] = initFinalizedCart(cart, header);
		threads[4] = initAllResponses(cart, header);
		threads[5] = initReviews(cart, header);
		threads[6] = initPromoCode(cart, header);
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
			threads[i].join();
		}
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
							QuotationBean.this.finalizedItems = r.readEntity(new GenericType<List<FinalizedItem>>() {
							});
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
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

	private Thread initReviews(Cart cart, String header) {
		this.reviews = new ArrayList<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartReviews(cart.getId()), header);
					if (r.getStatus() == 200) {
						QuotationBean.this.reviews = r.readEntity(new GenericType<List<CartReview>>() {
						});
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
							QuotationBean.this.allResponses = r.readEntity(new GenericType<List<FinalizedItem>>() {
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
						initVehicleEdit();
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
	
	public void deleteQuotationItem(QuotationItem qitem) {
		Response r = reqs.deleteSecuredRequest(AppConstants.deleteQuotationItem(qitem.getId()));
		if(r.getStatus() == 201) {
			Helper.redirect("process_quotation?cart-id=" + cart.getId());
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void selectQuotationItem(QuotationItem qitem) {
		this.selectedQuotationItem = qitem;
		initMakeVendors();
	}

	public void selectVendorItem(QuotationVendorItem qvi) {
		this.selectedVendorItem = qvi;
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
		if (cart.getDeliveryFees() >= 0) {
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

	private void initNewQuotation() {
		if (cart.getStatus() == 'N') {
			Quotation q = new Quotation();
			q.setCartId(cart.getId());
			q.setCreatedBy(loginBean.getUserHolder().getUser().getId());
			q.setCreatedByObject(loginBean.getUserHolder().getUser());
			q.setQuotationItems(new ArrayList<>());
			q.setStatus('N');
			for (CartItem cartItem : cart.getCartItems()) {
				QuotationItem qitem = new QuotationItem();
				qitem.setQuotationId(cartItem.getId());
				qitem.setCreatedBy(loginBean.getUserHolder().getUser().getId());
				qitem.setItemDesc(cartItem.getName());
				qitem.setQuantity(cartItem.getQuantity());
				q.getQuotationItems().add(qitem);
			}
			this.quotations.add(q);
		}
	}

	public boolean isAllowedToAddQuotation() {
		boolean found = true;
		for (Quotation q : quotations) {
			if (q.getStatus() == 'N') {
				found = false;
				break;
			}
		}
		return found;
	}

	public void updatePriceManually() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_MANUAL_QUOTATION_VENDOR_ITEM, this.selectedVendorItem);
		if (r.getStatus() == 201) {
			Helper.redirect("process_quotation?cart-id=" + cart.getId());
			Helper.addInfoMessage("Updated");
		} else {
			Helper.addErrorMessage("Something went wrong");
		}
	}
	

	public void deleteQuotation(Quotation quotation) {
		Response r = reqs.deleteSecuredRequest(AppConstants.deleteQuotation(quotation.getId()));
		if(r.getStatus() == 201) {
			Helper.redirect("process_quotation?cart-id=" + this.cart.getId());
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void createNewQuotationManually() {
		Quotation q = new Quotation();
		q.setManuallyAdded(true);
		q.setCartId(cart.getId());
		q.setCreatedBy(loginBean.getUserHolder().getUser().getId());
		q.setCreatedByObject(loginBean.getUserHolder().getUser());
		q.setQuotationItems(new ArrayList<>());
		q.setStatus('N');
		q.setCreated(new Date());
		this.addItem(q);
		this.quotations.add(q);
	}

	public void submitQuotationOrder(Quotation quotation) {
		boolean ok = true;
		for (QuotationItem qi : quotation.getQuotationItems()) {
			if (qi.getItemDesc().length() == 0) {
				ok = false;
				break;
			}
		}
		if (ok) {
			Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION, quotation);
			if (r.getStatus() == 200) {
				Helper.redirect("process_quotation?cart-id=" + this.cart.getId());
			} else {
				Helper.addErrorMessage("An error occured");
			}
		} else {
			Helper.addErrorMessage("Enter Item name");
		}
	}
	
	
	public void submitQuotationOrderToFinder(Quotation quotation) {
		boolean ok = true;
		for (QuotationItem qi : quotation.getQuotationItems()) {
			if (qi.getItemDesc().length() == 0) {
				ok = false;
				break;
			}
		}
		if (ok) {
			Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_FOR_FINDERS, quotation);
			if (r.getStatus() == 200) {
				Helper.redirect("process_quotation?cart-id=" + this.cart.getId());
			} else {
				Helper.addErrorMessage("An error occured");
			}
		} else {
			Helper.addErrorMessage("Enter Item name");
		}
	}
	

	public void updateQuotationOrder(Quotation quotation) {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_ADDITIONAL_QUOTATION, quotation);
		if (r.getStatus() == 200) {
			Helper.redirect("quotations");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public void chooseMake() {
		for(Make make : this.vehiclesBean.getMakes()) {
			if(make.getId() == this.selectedMakeId) {
				this.selectedMake = make;
				break;
			}
		}
	}
	
	public void chooseModelYear() {
		for(ModelYear myear : this.selectedModel.getModelYears()) {
			if(myear.getId() == this.selectedModelYearId) {
				this.selectedModelYear = myear;
				break;
			}
		}
	}
	
	
	public void chooseModel() {
		for(Model model : this.selectedMake.getModels()) {
			if(model.getId() == this.selectedModelId) {
				this.selectedModel = model;
				break;
			}
		}
	}
	
	public void initVehicleEdit() {
		if(this.cart.getModelYear() != null) {
			for(Make make : this.vehiclesBean.getMakes()) {
				if(make.getId() == cart.getModelYear().getMake().getId()) {
					selectedMake = make;
					break;
				}
			}
			this.selectedMakeId = cart.getModelYear().getMake().getId();
			
			for(Model model : this.selectedMake.getModels()) {
				if(cart.getModelYear().getModel().getId() == model.getId()) {
					selectedModel = model;
					break;
				}
			}
			this.selectedModelId = cart.getModelYear().getModel().getId();
			this.selectedModelYear = cart.getModelYear();
			this.selectedModelYearId = cart.getModelYear().getId();
		}
		else {
			
		}
	}

	public void addItem(Quotation quotation) {
		QuotationItem item = new QuotationItem();
		quotation.getQuotationItems().add(item);
	}

	public void removeItem(Quotation quotation, QuotationItem item) {
		quotation.getQuotationItems().remove(item);
	}

	private void initCart(String param) throws Exception {
		Long id = Long.parseLong(param);
		initquantityArray();
		// get the cart from active carts
		//Response r = reqs.getSecuredRequest(AppConstants.getCart(id, loginBean.getUserHolder().getUser().getId()));
		Response r = reqs.getSecuredRequest(AppConstants.getCart(id));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
			if(!(cart.getStatus() == 'W' || cart.getStatus() == 'N' || cart.getStatus() == 'Q' || cart.getStatus() == 'A' || cart.getStatus() == 'R')) {
				throw new Exception();
			}
		} else
			throw new Exception();
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

	private void initquantityArray() {
		quantityArray = new int[20];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
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

	public int[] getQuantityArray() {
		return quantityArray;
	}

	public void setQuantityArray(int[] quantityArray) {
		this.quantityArray = quantityArray;
	}

	public QuotationVendorItem getSelectedVendorItem() {
		return selectedVendorItem;
	}

	public void setSelectedVendorItem(QuotationVendorItem selectedVendorItem) {
		this.selectedVendorItem = selectedVendorItem;
	}

	public List<FinalizedItem> getFinalizedItems() {
		return finalizedItems;
	}

	public void setFinalizedItems(List<FinalizedItem> finalizedItems) {
		this.finalizedItems = finalizedItems;
	}

	public List<FinalizedItem> getAllResponses() {
		return allResponses;
	}

	public void setAllResponses(List<FinalizedItem> allResponses) {
		this.allResponses = allResponses;
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

	public CartReview getReview() {
		return review;
	}

	public void setReview(CartReview review) {
		this.review = review;
	}

	public List<CartReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<CartReview> reviews) {
		this.reviews = reviews;
	}

	public Make getSelectedMake() {
		return selectedMake;
	}

	public void setSelectedMake(Make selectedMake) {
		this.selectedMake = selectedMake;
	}

	public ModelYear getSelectedModelYear() {
		return selectedModelYear;
	}

	public void setSelectedModelYear(ModelYear selectedModelYear) {
		this.selectedModelYear = selectedModelYear;
	}

	public int getSelectedMakeId() {
		return selectedMakeId;
	}

	public void setSelectedMakeId(int selectedMakeId) {
		this.selectedMakeId = selectedMakeId;
	}

	public Model getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(Model selectedModel) {
		this.selectedModel = selectedModel;
	}

	public int getSelectedModelId() {
		return selectedModelId;
	}

	public void setSelectedModelId(int selectedModelId) {
		this.selectedModelId = selectedModelId;
	}

	public int getSelectedModelYearId() {
		return selectedModelYearId;
	}

	public void setSelectedModelYearId(int selectedModelYearId) {
		this.selectedModelYearId = selectedModelYearId;
	}
	

}
