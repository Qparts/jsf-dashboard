package qetaa.jsf.dashboard.beans.purchase;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;
import qetaa.jsf.dashboard.model.purchase.PurchaseReturnProduct;

@Named
@ViewScoped
public class PurchaseDetailsBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Purchase purchase;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("id");
			purchase = new Purchase();
			initPurchase(s);
			initCart();
			initCartVariables();
		} catch (Exception ex) {
			ex.printStackTrace();
			//Helper.redirect("sales-search");
		}
	}

	private void initPurchase(String param) throws Exception {
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getPurchaseFromId(id));
		if (r.getStatus() == 200) {
			purchase = r.readEntity(Purchase.class);
		} else {
			throw new Exception();
		}
	}

	private void initCart() throws Exception {
		Response r = reqs.getSecuredRequest(AppConstants.getCart(purchase.getCartId()));
		if (r.getStatus() == 200) {
			purchase.setCart(r.readEntity(Cart.class));
		} else {
			throw new Exception();
		}
	}

	private void initCartVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[6];
		threads[0] = ThreadRunner.initModelYear(purchase.getCart(), header);
		threads[1] = ThreadRunner.initCustomer(purchase.getCart(), header);
		threads[2] = ThreadRunner.initAddress(purchase.getCart(), header);
		threads[3] = ThreadRunner.initReviews(purchase.getCart(), header);
		threads[4] = ThreadRunner.initPromoCode(purchase.getCart(), header);
		threads[5] = initProducts(header);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
			}
		}
	}

	private Thread initProducts(String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread[] threads = new Thread[purchase.getPurchaseProducts().size() + purchase.getPurchaseReturnProducts().size()];
					int index = 0;
					for (PurchaseProduct sp : purchase.getPurchaseProducts()) {
						threads[index] = ThreadRunner.initProduct(sp, header);
						threads[index].start();
						threads[index].join();
						index++;
					}
					
					for(PurchaseReturnProduct srp : purchase.getPurchaseReturnProducts()) {
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

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
	
	
	
}
