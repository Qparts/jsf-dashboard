package qetaa.jsf.dashboard.beans.reports;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.ApprovedQuotationItem;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.PartsOrder;
import qetaa.jsf.dashboard.model.cart.PartsOrderItemApproved;
import qetaa.jsf.dashboard.model.cart.PartsOrderItemReturn;
import qetaa.jsf.dashboard.model.cart.Quotation;
import qetaa.jsf.dashboard.model.cart.QuotationItem;
import qetaa.jsf.dashboard.model.cart.QuotationItemResponse;
import qetaa.jsf.dashboard.model.cart.QuotationVendorItem;
import qetaa.jsf.dashboard.model.customer.HitActivityGroup;
import qetaa.jsf.dashboard.model.payment.PartsPayment;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.product.ProductPrice;
@Named
@SessionScoped 
public class CartDetailsBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long cartId;
	private Cart cart;
	private PartsOrderItemApproved selectedApproved;
	private PartsOrderItemReturn returnItem;
	private List<PartsOrderItemReturn> returnItems;
	private List<Quotation> quotations;
	private QuotationVendorItem selectedVendorItem;
	private HitActivityGroup selectedHitGroup;
	private List<HitActivityGroup> hitGroups;
	private PartsPayment partsPayment;
	private TimelineModel timeline;
	
	@Inject
	private Requester reqs;
		
	@PostConstruct
	private void init() {
		timeline = new TimelineModel();
		selectedApproved = new PartsOrderItemApproved();
		returnItem = new PartsOrderItemReturn();
		returnItems = new ArrayList<>();
		quotations = new ArrayList<>();
		partsPayment = new PartsPayment();
		cartId = 0L;
		cart = new Cart();
	}
	

	public double totalReturnedAmount() {
		double total = 0;
		for(PartsOrderItemReturn r : this.returnItems) {
			total = total + (r.getReturnAmount() * r.getReturnQuantity() - r.getCostPrice() * r.getReturnQuantity());
		}
		return total;
	}
	
	public double totalReturnedShipmentCost() {
		double total = 0;
		for(PartsOrderItemReturn r : this.returnItems) {
			total = total + (r.getShipmentCost() + r.getShipmentCost() * cart.getVatPercentage());
		}
		return total;
	}
	
	public void findCart() {
		timeline = new TimelineModel();
		Response r = reqs.getSecuredRequest(AppConstants.getCartInternal(cartId));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
			initVariables();
		//	initTimeline();
		} else if (r.getStatus() == 404) {
			Helper.addErrorMessage("Cart not found");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	private void initTimeline() {
		timeline = new TimelineModel();
		if(cart != null) {
		timeline.add(new TimelineEvent("Cart Created", cart.getCreated(), false, "", "cart-created"));
		if(cart.getSubmitted() != null)
			timeline.add(new TimelineEvent("Quotation Submitted", cart.getSubmitted(), false, "", "quotation-submitted"));
		if(cart.getPartsOrder() != null)
			timeline.add(new TimelineEvent("Parts Ordered", cart.getPartsOrder().getCreated(), false, "", "parts-ordered"));
		if(cart.getPartsOrder() != null && cart.getPartsOrder().getShipped() != null){
			timeline.add(new TimelineEvent("Parts Shipped", cart.getPartsOrder().getShipped(), false, "", "parts-shipped"));
		}
		if(cart.getStatus() == 'O') {
			timeline.add(new TimelineEvent("Cart Closed", cart.getReviews().get(cart.getReviews().size() -1).getCreated(), false, "", "cart-closed"));
		}
		
		if(cart.getStatus() == 'X') {
			timeline.add(new TimelineEvent("Cart Archived", cart.getReviews().get(cart.getReviews().size() -1).getCreated(), false, "", "cart-archived"));
		}
		
		}
		
		
	}
	
	private void initVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[9];
		threads[0] = ThreadRunner.initReviews(cart, header);
		threads[1] = initPartsApprovedItems(cart, header);
		threads[2] = initPartsOrder(cart, header);
		threads[3] = initReturnItems(cart, header);
		threads[4] = initQuotations(cart, header);
		threads[5] = initQuotationApprovedItems(cart, header);
		threads[6] = initAcitivityGroups(cart.getCustomerId(), header);
		threads[7] = initPartsPayment(cart, header);
		threads[8] = ThreadRunner.initPromoCode(cart, header);
		for (int i = 0; i < threads.length; i++)
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	public double getDiscountTotal() {
		double discount = 0;
		if(cart.getPromoCodeObject() != null && cart.getPromoCodeObject().isDiscountPromo()) {
			double perc = cart.getPromoCodeObject().getDiscountPercentage();
			discount = cart.getPartsItemsApprovedTotalSales() * perc + cart.getDeliveryFees() * perc;
		}
		return discount;
	}
	
	private Thread initAcitivityGroups(long customerId, String header) {
		hitGroups = new ArrayList<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCustomerActivityHits(customerId),header);
					if (r.getStatus() == 200) {
						hitGroups = r.readEntity(new GenericType<List<HitActivityGroup>>() {});
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
	
	private Thread initQuotations(Cart cart2, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartQuotations(cart.getId()), header);
					if (r.getStatus() == 200) {
						quotations = r.readEntity(new GenericType<List<Quotation>>() {});
						initQuotationItemProducts(header);
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private void initQuotationItemProducts(String header) throws Exception{
		for(Quotation q : quotations) {
			for (QuotationItem item : q.getQuotationItems()) {
				if(item.getQuotationItemResponses() != null) {
					for (QuotationItemResponse res : item.getQuotationItemResponses()) {
						if (res.getProductId() != null) {
							Response r = PojoRequester.getSecuredRequest(AppConstants.getProduct(res.getProductId()), header);
							if (r.getStatus() == 200) {
								Product p = r.readEntity(Product.class);
								res.setProduct(p);
							}
						}
						if (res.getProductPriceId() != null) {
							Response r = PojoRequester.getSecuredRequest(AppConstants.getProductPrice(res.getProductPriceId()), header);
							if (r.getStatus() == 200) {
								ProductPrice pp = r.readEntity(ProductPrice.class);
								res.setProductPrice(pp);
							}
						}
					}
				}
			}
		}		
	}
	
	private Thread initPartsOrder(Cart cart2, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getPartsOrder(cart.getId()), header);
					if (r.getStatus() == 200) {
						cart.setPartsOrder(r.readEntity(PartsOrder.class));
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initPartsPayment(Cart cart2, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getPartPayment(cart.getId()), header);
					if (r.getStatus() == 200) {
						partsPayment = r.readEntity(PartsPayment.class);
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initReturnItems(Cart cart2, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getPartsItemReturns(cart.getId()), header);
					if (r.getStatus() == 200) {
						returnItems = r.readEntity(new GenericType<List<PartsOrderItemReturn>>() {});
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}



	private Thread initPartsApprovedItems(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getFullPartsApprovedItems(cart.getId()),
							header);
					if (r.getStatus() == 200) {
						List<PartsOrderItemApproved> approved = r
								.readEntity(new GenericType<List<PartsOrderItemApproved>>() {
								});
						cart.setPartsItemsApproved(approved);
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initQuotationApprovedItems(Cart cart, String header) {
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
	
	public void selectVendorItem(QuotationVendorItem qvi) {
		this.selectedVendorItem = qvi;
	}

	
	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public PartsOrderItemApproved getSelectedApproved() {
		return selectedApproved;
	}

	public void setSelectedApproved(PartsOrderItemApproved selectedApproved) {
		this.selectedApproved = selectedApproved;
	}

	public PartsOrderItemReturn getReturnItem() {
		return returnItem;
	}

	public void setReturnItem(PartsOrderItemReturn returnItem) {
		this.returnItem = returnItem;
	}

	public List<PartsOrderItemReturn> getReturnItems() {
		return returnItems;
	}

	public void setReturnItems(List<PartsOrderItemReturn> returnItems) {
		this.returnItems = returnItems;
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


	public PartsPayment getPartsPayment() {
		return partsPayment;
	}


	public void setPartsPayment(PartsPayment partsPayment) {
		this.partsPayment = partsPayment;
	}


	public TimelineModel getTimeline() {
		return timeline;
	}


	public void setTimeline(TimelineModel timeline) {
		this.timeline = timeline;
	}
	
	
	
	
	
}
