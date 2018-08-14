package qetaa.jsf.dashboard.beans.purchase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;
import qetaa.jsf.dashboard.model.purchase.PurchaseReturnProduct;

@Named
@ViewScoped
public class IncompletePurchaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Purchase purchase;
	private Product newProduct;
	private PurchaseProduct selectedPurchaseProduct;
	private double price;

	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			newProduct = new Product();
			selectedPurchaseProduct = new PurchaseProduct();
			String id = Helper.getParam("id");
			initPurchase(id);
			initCart();
			initVariables();
		} catch (Exception ex) {
			ex.printStackTrace();
			Helper.redirect("purchases-incomplete");
		}
	}

	public void findProduct() {
		Response r = reqs.getSecuredRequest(AppConstants.getProduct(this.newProduct.getId()));
		if (r.getStatus() == 200) {
			Product product = r.readEntity(Product.class);
			this.selectedPurchaseProduct.setProduct(product);
			this.selectedPurchaseProduct.setProductChanged(true);
			this.selectedPurchaseProduct.setProductId(product.getId());
			Helper.addInfoMessage("Product Replaced");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	private boolean updateChangedProducts() {
		List<PurchaseProduct> pps = new ArrayList<>();
		for (PurchaseProduct pp : this.purchase.getPurchaseProducts()) {
			if (pp.isProductChanged()) {
				pps.add(pp);
			}
		}
		Response r = reqs.putSecuredRequest(AppConstants.PUT_REPLACE_PURCHASE_PRODUCT, pps);
		if (r.getStatus() == 201) {
			return true;
		}
		return false;

	}

	public void updatePurchase() {
		if (ready()) {
			// update purchase products changed first
			if (updateChangedProducts()) {
				purchase.setCompletedBy(loginBean.getUserHolder().getUser().getId());
				Response r = reqs.putSecuredRequest(AppConstants.PUT_COMPLETE_PURCHASE, purchase);
				if (r.getStatus() == 201) {
					Helper.redirect("purchases-incomplete");
				} else {
					Helper.addErrorMessage("An error occured");
				}
			}
			else {
				Helper.addErrorMessage("An error occured");
			}
		} else {
			Helper.addErrorMessage("Enter all costs");
		}
	}

	private boolean ready() {
		boolean ready = true;
		for (PurchaseProduct pp : purchase.getPurchaseProducts()) {
			if (pp.getUnitCost() == null) {
				ready = false;
				break;
			}
		}
		return ready;
	}

	private void initCart() throws Exception {
		purchase.setCart(new Cart());
		Response r = reqs.getSecuredRequest(AppConstants.getCart(purchase.getCartId()));
		if (r.getStatus() == 200) {
			purchase.setCart(r.readEntity(Cart.class));
		} else {
			throw new Exception();
		}
	}

	private void initVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[4];
		threads[0] = ThreadRunner.initModelYear(purchase.getCart(), header);
		threads[1] = ThreadRunner.initCustomer(purchase.getCart(), header);
		threads[2] = ThreadRunner.initPromoCode(purchase.getCart(), header);
		threads[3] = initProducts(header);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
			}
		}
	}

	private void initPurchase(String id) throws Exception {
		Long pid = Long.parseLong(id);
		Response r = reqs.getSecuredRequest(AppConstants.getIncompletePurchase(pid));
		if (r.getStatus() == 200) {
			this.purchase = r.readEntity(Purchase.class);
		} else {
			throw new Exception();
		}
	}

	private Thread initProducts(String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread[] threads = new Thread[purchase.getPurchaseProducts().size()
							+ purchase.getPurchaseReturnProducts().size()];
					int index = 0;
					for (PurchaseProduct sp : purchase.getPurchaseProducts()) {
						threads[index] = ThreadRunner.initProduct(sp, header);
						threads[index].start();
						threads[index].join();
						index++;
					}

					for (PurchaseReturnProduct srp : purchase.getPurchaseReturnProducts()) {
						threads[index] = ThreadRunner.initProduct(srp, header);
						threads[index].start();
						threads[index].join();
						index++;
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	public void updatePrice() {
		if (selectedPurchaseProduct.isWithVat()) {
			selectedPurchaseProduct.setUnitCostWv(price);
			selectedPurchaseProduct.setUnitCost(price / 1.05);
		} else {
			selectedPurchaseProduct.setUnitCost(price);
			selectedPurchaseProduct.setUnitCostWv(price * 1.05);
		}
		price = 0;
	}

	public void chooseSelectedPurchaseProduct(PurchaseProduct pp) {
		this.selectedPurchaseProduct = pp;
		this.newProduct = pp.getProduct();

	}

	public PurchaseProduct getSelectedPurchaseProduct() {
		return selectedPurchaseProduct;
	}

	public void setSelectedPurchaseProduct(PurchaseProduct selectedPurchaseProduct) {
		this.selectedPurchaseProduct = selectedPurchaseProduct;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Product getNewProduct() {
		return newProduct;
	}

	public void setNewProduct(Product newProduct) {
		this.newProduct = newProduct;
	}

}
