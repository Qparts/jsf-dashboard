package qetaa.jsf.dashboard.beans.customerservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.NotificationBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.ApprovedQuotationItem;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.cart.PartsOrder;
import qetaa.jsf.dashboard.model.cart.PartsOrderItem;
import qetaa.jsf.dashboard.model.cart.Quotation;
import qetaa.jsf.dashboard.model.cart.QuotationItem;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.customer.CustomerAddress;
import qetaa.jsf.dashboard.model.customer.HitActivityGroup;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

@Named
@ViewScoped 
public class FollowUpBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Cart cart;
	private CartReview cartReview;
	private List<CartReview> cartReviews;
	private List<HitActivityGroup> hitGroups;
	private List<City> cities;
	private Quotation quotation;
	private int[] quantityArray;
	private PartsOrder partsOrder;
	private CustomerAddress address;
	private HitActivityGroup selectedHitGroup;
	private boolean promoVerified;
	private String promCodeString;
	private char paymentMethod;
	private int additionalDelivery;

	@Inject
	private NotificationBean notification;

	@Inject
	private LoginBean loginBean;

	@Inject
	private Requester reqs;
	
	
	public int getNewLoyaltyPoints(double value) {
		return (int) (value/20);
	}
	

	@PostConstruct
	private void init() {
		try {
			additionalDelivery = 0;
			String s = Helper.getParam("cart");
			initCart(s);
			cartReview = new CartReview();
			cartReviews = new ArrayList<>();
			this.hitGroups = new ArrayList<>();
			this.selectedHitGroup = new HitActivityGroup();
			prepareQuotation();
			this.initquantityArray();
			initCartVariables();
			initPartsHolder();
			initAddress();
		} catch (Exception ex) {
			ex.printStackTrace();
			Helper.redirect("followups");
		}
	}
	
	public void verifyPromCode() {
		Response r = reqs.getSecuredRequest(AppConstants.getPromotionCodeFromCode(this.promCodeString));
		if (r.getStatus() == 200) {
			this.promoVerified = true;
			PromotionCode pc = r.readEntity(PromotionCode.class);
			this.cart.setPromotionCode(pc.getId());
			this.cart.setPromoCodeObject(pc);
		} else if (r.getStatus() == 498) {
			Helper.addErrorMessage("Promo Code Expired");
		} else if (r.getStatus() == 410) {
			Helper.addErrorMessage("Promo Code Used");
		} else if (r.getStatus() == 404) {
			Helper.addErrorMessage("Promo Code Not Found");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public int getDeliveryFees() {
		return this.cart.getDeliveryFees() + this.additionalDelivery;
	}
	
	public void updateDeliveryFees() {
		if(this.paymentMethod == 'D') {
			this.additionalDelivery = 25;
		}
		else {
			this.additionalDelivery = 0;
		}
	}
	
	private void initPartsHolder() {
		partsOrder = new PartsOrder();
		partsOrder.setPartsItems(new ArrayList<>());
		for (ApprovedQuotationItem approved : cart.getApprovedItems()) {
			PartsOrderItem poi = new PartsOrderItem();
			poi.setOrderedQuantity(approved.getQuantity());
			poi.setQuotationItemId(approved.getQuotationItemId());
			poi.setSalesPrice(approved.getUnitSales());
			poi.setItemDesc(approved.getItemDesc());
			poi.setNewQuantity(approved.getQuantity());
			partsOrder.getPartsItems().add(poi);
		}
	}
	
	public double grandTotal() {
		double vat = cart.getVatPercentage();
		double dfees =cart.getDeliveryFees();
		double partsTotal = partsOrder.getNewTotalPartsPrice();
		double vatAmount = (partsTotal + dfees) * vat;
		double total = vatAmount + dfees + partsTotal;
		return total;
		
	}
	
	
	private void preparePartsOrder(Long pid) {
		//prepare address
		partsOrder.setCartId(this.cart.getId());
		partsOrder.setSalesAmount(grandTotal());
		partsOrder.adjustFinalQuantity();
		partsOrder.setPaymentId(pid);
		partsOrder.setAddress(this.address);
	}
	
	public void makeSalesRequest() {
		if(this.paymentMethod == 'W') {
			makeWireTransfer();
		}
		if(this.paymentMethod == 'T') {
			makeCreditPayment();
		}
		if(this.paymentMethod == 'D') {
			makeCashOnDeliveryPayment();
		}
	}
	
	private void makeCashOnDeliveryPayment() {
		preparePartsOrder(0L);
		Map<String,Object> map = new HashMap<String,Object>();
		cart.setDeliveryFees(this.getDeliveryFees());
		map.put("partsOrder", partsOrder);
		map.put("cart", this.cart);
		map.put("discountPercentage", getCalculatedDiscountPercentage());
		map.put("customerName", cart.getCustomer().getFullName());
		Response r = reqs.postSecuredRequest(AppConstants.POST_CASH_ON_DELIVERY, map);
		if (r.getStatus() == 201) {
			Helper.redirect("wallets-process");
		} else {
			Helper.addErrorMessage("Something went wrong please try again later");
		}
	}
	
	private void makeCreditPayment() {
		preparePartsOrder(0L);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("partsOrder", partsOrder);
		map.put("cart", this.cart);
		map.put("discountPercentage", getCalculatedDiscountPercentage());
		map.put("customerName", cart.getCustomer().getFullName());
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREDIT_SALES, map);
		if (r.getStatus() == 201) {
			Helper.redirect("wallets-process");
		} else {
			Helper.addErrorMessage("Something went wrong please try again later");
		}
	}
	

	private double getCalculatedDiscountPercentage() {
		if (this.getCart().getPromoCodeObject() != null) {
			if (this.getCart().getPromoCodeObject().isDiscountPromo()) {
				return this.getCart().getPromoCodeObject().getDiscountPercentage();
			}
		}
		return 0;
	}
	
	private void makeWireTransfer() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_EDIT_CART, cart);
		if(r.getStatus() == 201) {
			preparePartsOrder(0L);
			Response r2 = reqs.postSecuredRequest(AppConstants.POST_WIRE_TRASNFER, partsOrder);
			if (r2.getStatus() == 201) {
				Helper.redirect("wire_transfers");
			} else {
				Helper.addErrorMessage("Something went wrong please try again later");
			}
		}
	}

	private void initCart(String param) throws Exception {
		cart = new Cart();
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getCartFollowUp(id));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
		} else {
			throw new Exception();
		}
	}

	public void initCartVariables() throws InterruptedException {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[8];
		threads[0] = initModelYear(cart, header);
		threads[1] = initCustomer(cart, header);
		threads[2] = initApprovedItems(cart, header);
		threads[3] = initReviews(cart, header);
		threads[4] = initCities(header);
		threads[5] = initCity(cart, header);
		threads[6] = initAcitivityGroups(cart.getCustomerId(), header);
		threads[7] = ThreadRunner.initPromoCode(cart, header);
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
			threads[i].join();
		}
	}
	
	private Thread initAcitivityGroups(long customerId, String header) {
		hitGroups = new ArrayList<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCustomerActivityHits(customerId),header);
					if (r.getStatus() == 200) {
						FollowUpBean.this.hitGroups = r.readEntity(new GenericType<List<HitActivityGroup>>() {});
					}
					else {
						hitGroups = new ArrayList<>();
					}

				} catch (Exception ex) {

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
	
	

	private Thread initCities(String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCountryCities(1), header);
					if (r.getStatus() == 200) {
						cities = r.readEntity(new GenericType<List<City>>() {
						});
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}


	private Thread initReviews(Cart cart, String header) {
		cartReviews = new ArrayList<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartReviews(cart.getId()), header);
					if (r.getStatus() == 200) {
						cartReviews = r.readEntity(new GenericType<List<CartReview>>() {
						});
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
						cart.setApprovedItems(approved);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private void initAddress() {
		address = new CustomerAddress();
		address.setCustomerId(cart.getCustomerId());
		address.setCreatedBy(0);
		address.setCityId(this.cart.getCityId());
		address.setLine1("");
		address.setLine2("");
	}

	private void prepareCartReview() {
		cartReview.setStage(3);
		cartReview.setCartId(this.cart.getId());
		cartReview.setReviewerId(this.loginBean.getUserHolder().getUser().getId());
		cartReview.setStatus(cartReview.getStatusFromActionValue());
		cartReview.setCartPrice(cart.totalSales() + cart.getDeliveryFees()
				+ ((cart.getDeliveryFees() + cart.totalSales()) * cart.getVatPercentage()));
	}

	public void submitReview() {
		prepareCartReview();
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, this.cartReview);
		if (r.getStatus() == 200) {
			if (cartReview.getActionValue() == 'G') {
				submitNewQuotation();
			} else {
				if(cartReview.getStatus() == 'C') {
					Helper.redirect("followups");
				}
				else {
					Helper.redirect("followup?cart=" + cartReview.getCartId());
				}
			}
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	private void submitNewQuotation() {
		if (!quotation.getQuotationItems().isEmpty()) {
			Response r = reqs.postSecuredRequest(AppConstants.POST_ADDITIONAL_QUOTATION, quotation);
			if (r.getStatus() == 200) {
				Helper.redirect("followup?cart=" + cartReview.getCartId());
			} else {
				Helper.addErrorMessage("Could not create quotation");
			}
		} else {
			Helper.addErrorMessage("No quotation items added");
		}
	}

	private void initquantityArray() {
		quantityArray = new int[20];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
		}
	}

	private void prepareQuotation() {
		this.quotation = new Quotation();
		quotation.setCartId(cart.getId());
		quotation.setCreatedBy(loginBean.getUserHolder().getUser().getId());
		quotation.setQuotationItems(new ArrayList<>());
		quotation.setStatus('N');
	}

	public void addItem() {
		QuotationItem item = new QuotationItem();
		quotation.getQuotationItems().add(item);
	}

	public void removeItem(QuotationItem item) {
		quotation.getQuotationItems().remove(item);
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

	public List<CartReview> getCartReviews() {
		return cartReviews;
	}

	public void setCartReviews(List<CartReview> cartReviews) {
		this.cartReviews = cartReviews;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public int[] getQuantityArray() {
		return quantityArray;
	}

	public void setQuantityArray(int[] quantityArray) {
		this.quantityArray = quantityArray;
	}

	public PartsOrder getPartsOrder() {
		return partsOrder;
	}

	public void setPartsOrder(PartsOrder partsOrder) {
		this.partsOrder = partsOrder;
	}

	public CustomerAddress getAddress() {
		return address;
	}

	public void setAddress(CustomerAddress address) {
		this.address = address;
	}


	public List<HitActivityGroup> getHitGroups() {
		return hitGroups;
	}


	public void setHitGroups(List<HitActivityGroup> hitGroups) {
		this.hitGroups = hitGroups;
	}


	public HitActivityGroup getSelectedHitGroup() {
		return selectedHitGroup;
	}


	public void setSelectedHitGroup(HitActivityGroup selectedHitGroup) {
		this.selectedHitGroup = selectedHitGroup;
	}


	public boolean isPromoVerified() {
		return promoVerified;
	}


	public void setPromoVerified(boolean promoVerified) {
		this.promoVerified = promoVerified;
	}


	public String getPromCodeString() {
		return promCodeString;
	}


	public void setPromCodeString(String promCodeString) {
		this.promCodeString = promCodeString;
	}


	public char getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(char paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
