package qetaa.jsf.dashboard.beans.purchase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import qetaa.jsf.dashboard.model.purchase.PurchasePayment;
import qetaa.jsf.dashboard.model.purchase.PurchaseReturn;
import qetaa.jsf.dashboard.model.purchase.PurchaseReturnProduct;
import qetaa.jsf.dashboard.model.sales.SalesReturn;
import qetaa.jsf.dashboard.model.sales.SalesReturnProduct;

@Named
@ViewScoped
public class PurchaseReturnBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private SalesReturn salesReturn;
	private PurchaseReturn purchaseReturn;
	private long selectedPurchaseId;
	private Integer bankId;
	private int vendorId;
	private String paymentRef;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("srid");
			salesReturn = new SalesReturn();
			purchaseReturn = new PurchaseReturn();
			purchaseReturn.setPurchaseReturnProducts(new ArrayList<>());
			initSalesReturn(s);
			initCart(salesReturn.getCartId());
			initCartVariables();
			initProducts();
		} catch (Exception ex) {
			ex.printStackTrace();
			Helper.redirect("purchase-return-search");
		}
	}

	public void createPurchaseReturn() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_PURCHASE_RETURN, "");
		if (r.getStatus() == 200) {
			Long id = (r.readEntity(Long.class));
			purchaseReturn.setId(id);
			purchaseReturn.setMethod('R');
			purchaseReturn.setReturnedBy(this.loginBean.getUserHolder().getUser().getId());
			purchaseReturn.setTransactionType('T');
			for (PurchaseReturnProduct prp : purchaseReturn.getPurchaseReturnProducts()) {
				prp.setPurchaseReturn(null);
			}
			initPurchasePayment();

			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_PURCHASE_RETURN, this.purchaseReturn);
			if (r2.getStatus() == 201) {
				Helper.redirect("purchase-return-search");
			} else {
				Helper.addErrorMessage("could not update purchase return");
			}
		} else {
			Helper.addErrorMessage("Could not create purchase return");
		}

	}

	private void initPurchasePayment() {
		PurchasePayment pp = new PurchasePayment();
		pp.setAmount(purchaseReturn.getTotalPurchaseReturnCostWv() * -1);// negative because this amount should go back
																			// to us
		pp.setBankId(bankId);
		pp.setMethod('R');
		pp.setPaidBy(this.loginBean.getUserHolder().getUser().getId());
		pp.setPaymentDate(new Date());
		pp.setPaymentRef(this.paymentRef);
		pp.setPurchase(null);
		pp.setPurchaseReturn(null);
		purchaseReturn.setPurchasePayments(new ArrayList<>());
		purchaseReturn.getPurchasePayments().add(pp);

	}

	public List<Purchase> getPurchases() {
		List<Purchase> purchases = new ArrayList<>();
		for (SalesReturnProduct srp : salesReturn.getSalesReturnProducts()) {
			Purchase purchase = srp.getPurchaseProduct().getPurchase();
			if (srp.getVendorSpecificStockQuantity() > 0) {
				if (!purchases.contains(purchase)) {
					purchases.add(purchase);
				}
			}
		}
		return purchases;
	}

	public void initPurchaseReturn() {
		purchaseReturn = new PurchaseReturn();
		purchaseReturn.setPurchaseReturnProducts(new ArrayList<>());
		purchaseReturn.setCartId(salesReturn.getCartId());
		purchaseReturn.setPurchase(getAvailablePurchaseFromId());
		List<SalesReturnProduct> srps = getSalesReturnProductsByPurchase();
		for (SalesReturnProduct srp : srps) {
			PurchaseReturnProduct prp = new PurchaseReturnProduct();
			prp.setProductId(srp.getProductId());
			prp.setPurchaseProduct(srp.getPurchaseProduct());
			prp.setProduct(srp.getProduct());
			prp.setQuantity(srp.getQuantity());
			prp.setUnitCost(srp.getUnitCost());
			prp.setUnitCostWv(srp.getUnitCostWv());
			prp.setSalesReturnProduct(srp);
			purchaseReturn.getPurchaseReturnProducts().add(prp);
		}
	}

	private List<SalesReturnProduct> getSalesReturnProductsByPurchase() {
		List<SalesReturnProduct> srps = new ArrayList<>();
		for (SalesReturnProduct srp : salesReturn.getSalesReturnProducts()) {
			if (srp.getPurchaseProduct().getPurchase().getId() == this.selectedPurchaseId) {
				srps.add(srp);
			}
		}
		return srps;
	}

	private Purchase getAvailablePurchaseFromId() {
		for (SalesReturnProduct srp : salesReturn.getSalesReturnProducts()) {
			Purchase purchase = srp.getPurchaseProduct().getPurchase();
			if (purchase.getId() == this.selectedPurchaseId) {
				return purchase;
			}
		}
		return null;
	}

	public List<Integer> getAvailableVendorIds() {
		List<Integer> vendorIds = new ArrayList<>();
		for (SalesReturnProduct srp : salesReturn.getSalesReturnProducts()) {
			if (!vendorIds.contains(srp.getPurchaseProduct().getPurchase().getVendorId())) {
				vendorIds.add(srp.getPurchaseProduct().getPurchase().getVendorId());
			}
		}
		return vendorIds;
	}

	private void initCartVariables() throws Exception {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[2];
		threads[0] = ThreadRunner.initPromoCode(salesReturn.getSales().getCart(), header);
		threads[1] = ThreadRunner.initAddress(salesReturn.getSales().getCart(), header);
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
			threads[i].join();
		}
	}

	private void initCart(long cartId) {
		Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
		if (r.getStatus() == 200) {
			salesReturn.getSales().setCart(r.readEntity(Cart.class));
		}
	}

	private void initSalesReturn(String s) throws Exception {
		Long id = Long.parseLong(s);
		Response r = reqs.getSecuredRequest(AppConstants.getSalesReturnFromId(id));
		if (r.getStatus() == 200) {
			salesReturn = r.readEntity(SalesReturn.class);
		} else {
			throw new Exception();
		}
	}

	private void initProducts() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[salesReturn.getSalesReturnProducts().size()];
		int index = 0;
		try {
			for (SalesReturnProduct srp : salesReturn.getSalesReturnProducts()) {
				srp.setProduct(new Product());
				threads[index] = ThreadRunner.initProduct(srp, header);
				threads[index].start();
				threads[index].join();
				index++;

			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public SalesReturn getSalesReturn() {
		return salesReturn;
	}

	public void setSalesReturn(SalesReturn salesReturn) {
		this.salesReturn = salesReturn;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public PurchaseReturn getPurchaseReturn() {
		return purchaseReturn;
	}

	public void setPurchaseReturn(PurchaseReturn purchaseReturn) {
		this.purchaseReturn = purchaseReturn;
	}

	public long getSelectedPurchaseId() {
		return selectedPurchaseId;
	}

	public void setSelectedPurchaseId(long selectedPurchaseId) {
		this.selectedPurchaseId = selectedPurchaseId;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

}
