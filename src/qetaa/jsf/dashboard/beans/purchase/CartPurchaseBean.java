package qetaa.jsf.dashboard.beans.purchase;

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
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.PartsOrderItemApproved;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.purchase.PurchasePayment;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;

@Named 
@ViewScoped
public class CartPurchaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Purchase purchase;
	private PurchaseProduct purchaseProduct;
	private PartsOrderItemApproved selectedApprovedItem;
	private PurchasePayment purchasePayment;
	private Cart cart;
	private List<Purchase> purchases;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("cart");
			initCart(s);
			initCartVariables();
			purchase = new Purchase();
			purchase.setPurchaseProducts(new ArrayList<>());
			purchaseProduct = new PurchaseProduct();
			purchasePayment = new PurchasePayment();
			//purchases = new ArrayList<>();
		} catch (Exception ex) {
			Helper.redirect("part-orders");
		}
	}

	public double getTotalCost() {
		double cost = 0;
		for (PurchaseProduct pp : this.purchase.getPurchaseProducts()) {
			cost = cost + pp.getUnitCost() * pp.getQuantity();
		}
		return cost;
	}

	public double getTotalCostWithVat() {
		double cost = 0;
		for (PurchaseProduct pp : this.purchase.getPurchaseProducts()) {
			cost = cost + pp.getUnitCostWv() * pp.getQuantity();
		}
		return cost;
	}

	public double getTotalVat() {
		double cost = 0;
		for (PurchaseProduct pp : this.purchase.getPurchaseProducts()) {
			cost = cost + ((pp.getUnitCostWv() - pp.getUnitCost()) * pp.getQuantity());
		}
		return cost;
	}

	public void createPurchase() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_PURCHASE, "");
		if (r.getStatus() == 200) {
			Long purchaseId = r.readEntity(Long.class);
			this.purchase.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
			this.purchase.setId(purchaseId);
			this.purchase.setMakeId(cart.getMakeId());
			this.purchase.setCartId(this.cart.getId());
			this.purchase.setPaymentStatus('O');
			this.purchase.setCustomerId(this.cart.getCustomerId());
			this.purchase.setPurchasePayments(new ArrayList<>());
			this.purchase.getPurchasePayments().add(this.purchasePayment);
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_PURCHASE, purchase);
			if (r2.getStatus() == 201) {
				Helper.redirect("purchase?cart=" + this.cart.getId());
			} else {
				Helper.addErrorMessage("An error occured");
			}
		}
	}
	
	public boolean isPartsAvailableforPurchase() {
		boolean found = false;
		for(PartsOrderItemApproved approved : cart.getPartsItemsApproved()) {
			if(approved.getStockQuantity() < approved.getApprovedQuantity()) {
				found = true;
				break;
			}
		}
		return found;
	}

	public void addToPurchaseOrder() {
		boolean found = false;
		for (PurchaseProduct pp : this.purchase.getPurchaseProducts()) {
			if (pp.getProductId() == this.purchaseProduct.getProductId()) {
				found = true;
			}
		}
		if (!found) {
			PurchaseProduct pp = new PurchaseProduct();
			pp.setQuantity(purchaseProduct.getNewQuantity());
			pp.setProduct(purchaseProduct.getProduct());
			pp.setProductId(purchaseProduct.getProductId());
			pp.setUnitCost(purchaseProduct.getUnitCost());
			pp.setUnitCostWv(purchaseProduct.getUnitCostWv());
			this.purchase.getPurchaseProducts().add(pp);
			purchaseProduct = new PurchaseProduct();
		} else {
			Helper.addErrorMessage("Product already added");
		}
	}

	public void chooseApprovedItem(PartsOrderItemApproved approved) {
		purchaseProduct = new PurchaseProduct();
		purchaseProduct.setProductId(approved.getProductId().longValue());
		purchaseProduct.setProduct(approved.getProduct());
		purchaseProduct.setQuantity(approved.getApprovedQuantity());
		purchaseProduct.setNewQuantity(approved.getApprovedQuantity());
		purchaseProduct.setUnitCostWv(approved.getCostPrice());
		purchaseProduct.setUnitCost(this.deductAddedPercentage(approved.getCostPrice(), cart.getVatPercentage()));
		this.selectedApprovedItem = approved;
	}

	private double deductAddedPercentage(double orig, double percentage) {
		double x = orig / (1.0 + percentage);
		return x;
	}

	public void recalculatePrice() {
		purchaseProduct
				.setUnitCost(this.deductAddedPercentage(purchaseProduct.getUnitCostWv(), cart.getVatPercentage()));
	}

	private void initCartVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[7];
		threads[0] = ThreadRunner.initModelYear(cart, header);
		threads[1] = ThreadRunner.initCustomer(cart, header);
		threads[2] = ThreadRunner.initAddress(cart, header);
		threads[3] = initPartsApproved(cart, header);
		threads[4] = ThreadRunner.initReviews(cart, header);
		threads[5] = ThreadRunner.initPromoCode(cart, header);
		threads[6] = initCartPurchases(cart, header);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
			}
		}
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
						for (PartsOrderItemApproved approved : collections) {
							if (approved.getProductId() != null) {
								Response r2 = PojoRequester.getSecuredRequest(
										AppConstants.getProductForCart(approved.getProductId(), cart.getId()), header);
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

	public Purchase getPurchaseFromId(long purchaseId) {
		for (Purchase p : purchases) {
			if (p.getId() == purchaseId) {
				return p;
			}
		}
		return null;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public PurchaseProduct getPurchaseProduct() {
		return purchaseProduct;
	}

	public void setPurchaseProduct(PurchaseProduct purchaseProduct) {
		this.purchaseProduct = purchaseProduct;
	}

	public PartsOrderItemApproved getSelectedApprovedItem() {
		return selectedApprovedItem;
	}

	public void setSelectedApprovedItem(PartsOrderItemApproved selectedApprovedItem) {
		this.selectedApprovedItem = selectedApprovedItem;
	}

	public PurchasePayment getPurchasePayment() {
		return purchasePayment;
	}

	public void setPurchasePayment(PurchasePayment purchasePayment) {
		this.purchasePayment = purchasePayment;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

}
