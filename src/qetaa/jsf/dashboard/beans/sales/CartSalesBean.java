package qetaa.jsf.dashboard.beans.sales;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.PartsOrderItemApproved;
import qetaa.jsf.dashboard.model.payment.PartsPayment;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesPayment;
import qetaa.jsf.dashboard.model.sales.SalesProduct;

@Named
@ViewScoped
public class CartSalesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Sales sales;
	private List<Purchase> purchases;
	private SalesProduct salesProduct;
	private Cart cart;
	private PartsPayment partsPayment;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("cart");
			purchases = new ArrayList<>();
			sales = new Sales();
			sales.setSalesProducts(new ArrayList<>());
			salesProduct = new SalesProduct();
			initCart(s);
			initCartVariables();
			if (cart.isPartsAvailableforPurchase()) {
				throw new Exception();
			}
			prepareSalesProducts();
		} catch (Exception ex) {
			Helper.redirect("part-orders");
		}
	}

	public void createSales() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_SALES, "");
		if (r.getStatus() == 200) {
			Long id = (r.readEntity(Long.class));
			sales.setId(id);
			sales.setMakeId(cart.getMakeId());
			sales.setCartId(cart.getId());
			sales.setCreatedBy(loginBean.getUserHolder().getUser().getId());
			sales.setCustomerId(cart.getCustomerId());
			sales.setDeliveryFees(cart.getDeliveryFees());
			sales.setPaymentStatus('P');
			sales.setPromotionDiscount(this.getPromotionDiscount());
			sales.setPromotionId(cart.getPromotionCode());
			sales.setTransactionType('C');
			sales.setVatPercentage(cart.getVatPercentage());
			initSalesPayment();
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_SALES, sales);
			if (r2.getStatus() == 201) {
				shipItems();
			} else {
				Helper.addErrorMessage("could not update sales");
			}
		} else {
			Helper.addErrorMessage("Could not create sales");
		}
	}

	public void shipItems() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("cartId", String.valueOf(this.cart.getId()));
		map.put("shipmentCost", String.valueOf(this.sales.getShipmentFees()));
		map.put("shippedBy", String.valueOf(this.loginBean.getUserHolder().getUser().getId()));
		map.put("shipmentReference", this.sales.getShipmentReference());
		map.put("courrier", this.sales.getCourrierName());
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_SHIP_ITEMS, map);
		if (r.getStatus() == 201) {
			// update address
			reqs.putSecuredRequest(AppConstants.PUT_UPDATE_ADDRESS, this.cart.getAddress());
			Helper.redirect("part-orders");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	private void initSalesPayment() {
		SalesPayment sp = new SalesPayment();
		sp.setAmount(this.getTotalSalesProducts() + this.getTotalVat() - this.getPromotionDiscount()
				+ this.cart.getDeliveryFees());
		if(partsPayment.getBank() != null) {
			sp.setBankId(partsPayment.getBank().getBankId());
		}
 
		if(partsPayment.getCreditFees() != null) {
			sp.setCreditFees((Double)partsPayment.getCreditFees()/100);
		}else {
			sp.setCreditFees(null);
		}
		sp.setMethod(partsPayment.getPaymentTypeChar());
		sp.setPaymentDate(partsPayment.getCreated());
		sp.setPaymentRef(partsPayment.getTransactionId());
		sp.setProvider(partsPayment.getProvider());
		sales.setSalesPayments(new ArrayList<>());
		sales.getSalesPayments().add(sp);
	}

	public double getTotalSalesProducts() {
		double t = 0;
		for (SalesProduct sp : sales.getSalesProducts()) {
			t = t + sp.getUnitSales() * sp.getQuantity();
		}
		return t;
	}

	@JsonIgnore
	public double getTotalSalesProductsWv() {
		double t = 0;
		for (SalesProduct sp : sales.getSalesProducts()) {
			t = t + sp.getUnitSalesWv() * sp.getQuantity();
		}
		return t;
	}

	@JsonIgnore
	public double getTotalVat() {
		return cart.getVatPercentage() * (getTotalSalesProducts() + cart.getDeliveryFees());
	}

	@JsonIgnore
	public double getPromotionDiscount() {
		if (cart.getPromoCodeObject() == null) {
			return 0;
		}
		if (!cart.getPromoCodeObject().isDiscountPromo()) {
			return 0;
		}
		return cart.getPromoCodeObject().getDiscountPercentage() * (getTotalSalesProducts() + cart.getDeliveryFees());
	}

	private void prepareSalesProducts() {
		for (Purchase purchase : purchases) {
			for (PurchaseProduct pp : purchase.getPurchaseProducts()) {
				SalesProduct sp = new SalesProduct();
				sp.setProductId(pp.getProductId());
				sp.setPurchaseProduct(pp);
				sp.setQuantity(pp.getQuantity());
				sp.setUnitCost(pp.getUnitCost());
				sp.setUnitCostWv(pp.getUnitCostWv());
				PartsOrderItemApproved approved = getApprovedFromProductId(pp.getProductId());
				sp.setUnitSales(approved.getSalesPrice());
				sp.setUnitSalesWv(approved.getSalesPrice() * cart.getVatPercentage() + approved.getSalesPrice());
				sp.setProduct(approved.getProduct());
				sales.getSalesProducts().add(sp);
			}
		}
	}

	private PartsOrderItemApproved getApprovedFromProductId(long productId) {
		for (PartsOrderItemApproved approved : cart.getPartsItemsApproved()) {
			if (approved.getProductId() == productId) {
				return approved;
			}
		}
		return null;
	}

	private void initCartVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[8];
		threads[0] = ThreadRunner.initModelYear(cart, header);
		threads[1] = ThreadRunner.initCustomer(cart, header);
		threads[2] = ThreadRunner.initAddress(cart, header);
		threads[3] = initPartsApproved(cart, header);
		threads[4] = ThreadRunner.initReviews(cart, header);
		threads[5] = ThreadRunner.initPromoCode(cart, header);
		threads[6] = initCartPurchases(cart, header);
		threads[7] = initPartsPayment(cart, header);
		for (int i = 0; i < threads.length; i++)
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
			}
	}

	private Thread initCartPurchases(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartPurchases(cart.getId()), header);
					if (r.getStatus() == 200) {
						purchases = r.readEntity(new GenericType<List<Purchase>>() {
						});
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	public void chooseApprovedItem(PartsOrderItemApproved approved) {
		salesProduct = new SalesProduct();
		salesProduct.setProductId(approved.getProductId().longValue());
		salesProduct.setQuantity(approved.getApprovedQuantity());
		salesProduct.setUnitCostWv(approved.getCostPrice());
	}

	private Thread initPartsPayment(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getPartPayment(cart.getId()), header);
					if (r.getStatus() == 200) {
						partsPayment = r.readEntity(PartsPayment.class);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});
		return thread;
	}

	private Thread initPartsApproved(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getFullPartsApprovedItems(cart.getId()),
							header);
					if (r.getStatus() == 200) {
						List<PartsOrderItemApproved> collections = r
								.readEntity(new GenericType<List<PartsOrderItemApproved>>() {
								});
						cart.setPartsItemsApproved(collections);
						for (PartsOrderItemApproved approved : cart.getPartsItemsApproved()) {
							if (approved.getProductId() != null) {
								Response r2 = PojoRequester
										.getSecuredRequest(AppConstants.getProduct(approved.getProductId()), header);
								if (r2.getStatus() == 200) {
									approved.setProduct(r2.readEntity(Product.class));
								}
							}
						}
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private void initCart(String param) throws Exception {
		cart = new Cart();
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getWaitingPartsOrder(id));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
		} else {
			throw new Exception();
		}
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public PartsPayment getPartsPayment() {
		return partsPayment;
	}

	public void setPartsPayment(PartsPayment partsPayment) {
		this.partsPayment = partsPayment;
	}

}
