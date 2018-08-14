package qetaa.jsf.dashboard.beans.wallet;

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
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.payment.Wallet;
import qetaa.jsf.dashboard.model.payment.WalletItem;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesPayment;
import qetaa.jsf.dashboard.model.sales.SalesProduct;

@Named
@ViewScoped
public class SalesWalletBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Wallet wallet;
	private List<Purchase> purchases;
	private Sales sales;
	private CartReview review;

	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		review = new CartReview();
		sales = new Sales();
		sales.setSalesProducts(new ArrayList<>());
		purchases = new ArrayList<>();
		wallet = new Wallet();
		try {
			String s = Helper.getParam("wallet");
			initWallet(s);
			this.initPurchases();
			String header = reqs.getSecurityHeader();
			Thread[] ts = new Thread[2];
			ts[0] = initPurchasesProducts(header);
			ts[1] = initCart(header);
			for (int i = 0; i < ts.length; i++) {
				ts[i].start();
				ts[i].join();
			}
			initSales();
			initCartVariables();
		} catch (Exception ex) {
			ex.printStackTrace();
			// Helper.redirect("wallets-process");
		}
	}
	
	private void initCartVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[3];
		threads[0] = ThreadRunner.initAddress(wallet.getCart(), header);
		threads[1] = ThreadRunner.initReviews(wallet.getCart(), header);
		threads[2] = ThreadRunner.initPromoCode(wallet.getCart(), header);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void prepareCartReview() {
		review.setStage(7);
		review.setCartId(this.wallet.getCart().getId());
		review.setReviewerId(this.loginBean.getUserHolder().getUser().getId());
		review.setStatus(review.getStatusFromActionValue());
	}

	public void submitReview() {
		prepareCartReview();
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, this.review);
		if (r.getStatus() == 200) {
			Helper.redirect("wallet-sales?wallet=" + this.wallet.getId());
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public void createSales() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_SALES, "");
		if (r.getStatus() == 200) {
			Long id = (r.readEntity(Long.class));
			sales.setId(id);
			sales.setCartId(wallet.getCartId());
			sales.setCreatedBy(loginBean.getUserHolder().getUser().getId());
			sales.setCustomerId(wallet.getCustomerId());
			sales.setDeliveryFees(getDeliveryFees());
			sales.setDueDate(new Date());
			sales.setMakeId(wallet.getCart().getMakeId());
			
			if(wallet.isCreditSales()) {
				sales.setPaymentStatus('O');//outstanding
				sales.setTransactionType('T');
			}
			else {
				sales.setPaymentStatus('P');
				sales.setTransactionType('C');
				this.initSalesPayment();

			}
			sales.setPromotionDiscount(getSalesTotalDiscountAmount());
			sales.setPromotionId(wallet.getCart().getPromotionCode());
			sales.setSalesDate(new Date());
			sales.setVatPercentage(wallet.getCart().getVatPercentage());
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_SALES, sales);
			if (r2.getStatus() == 201) {
				Helper.redirect("wallets-process");
			} else {
				Helper.addErrorMessage("could not update sales");
			}
		}
	}
	
	
	private void initSalesPayment() {
		SalesPayment sp = new SalesPayment();
		sp.setAmount(this.getSalesTotalProductsWithVat() - this.getSalesTotalDiscountAmount()
				+ this.getDeliveryFeesVat());
		if(wallet.getBank() != null) {
			sp.setBankId(wallet.getBank().getBankId());
		}
		
		if(wallet.getCreditFees() != null) {
			sp.setCreditFees((Double) wallet.getCreditFees());
		}else {
			sp.setCreditFees(null);
		}
		sp.setMethod(wallet.getPaymentTypeChar());
		sp.setPaymentDate(wallet.getCreated());
		sp.setPaymentRef(wallet.getTransactionId());
		sp.setProvider(wallet.getGateway());
		sales.setSalesPayments(new ArrayList<>());
		sales.getSalesPayments().add(sp);
	}

	private void initPurchases() {
		Response r = reqs.getSecuredRequest(AppConstants.getCartPurchases(wallet.getCartId()));
		if (r.getStatus() == 200) {
			purchases = r.readEntity(new GenericType<List<Purchase>>() {
			});
		}

	}

	public double getSalesTotalProducts() {
		double total = 0;
		if (sales.getSalesProducts() != null) {
			for (SalesProduct sp : sales.getSalesProducts()) {
				total = total + sp.getUnitSales() * sp.getQuantity();
			}
		}
		return total;
	}
	
	public double getSalesTotalProductsVat() {
		double total = 0;
		if (sales.getSalesProducts() != null) {
			for (SalesProduct sp : sales.getSalesProducts()) {
				total = total + (sp.getUnitSalesWv() - sp.getUnitSales()) * sp.getQuantity();
			}
		}
		return total;
	}
	
	public double getDeliveryFees() {
		double d = 0;
		for(WalletItem wi : wallet.getWalletItems()) {
			if(wi.getItemType() == 'D') {
				if(wi.getStatus() == 'V') {
					d = wi.getUnitSales();
					break;
				}
			}
		}
		return d;
	}
	
	public double getDeliveryFeesVat() {
		double d = 0;
		for(WalletItem wi : wallet.getWalletItems()) {
			if(wi.getItemType() == 'D') {
				if(wi.getStatus() == 'V') {
					d = wi.getUnitSalesWv() - wi.getUnitSales();
					break;
				}
			}
		}
		return d;
	}
	
	public double getDeliveryFeesWithVat() {
		double d = 0;
		for(WalletItem wi : wallet.getWalletItems()) {
			if(wi.getItemType() == 'D') {
				if(wi.getStatus() == 'V') {
					d = wi.getUnitSalesWv();
					break;
				}
			}
		}
		return d;
	}
	
	public double getSalesTotalProductsWithVat() {
		double total = 0;
		if(sales.getSalesProducts()!= null) {
			for(SalesProduct sp : sales.getSalesProducts()) {
				total = total + sp.getUnitSalesWv() * sp.getQuantity();
			}
		}
		return total;
	}
	
	public double getSalesTotalDiscountAmount() {
		double total = 0;
		if(wallet.getDiscountPercentage() != null) {
			if(sales.getSalesProducts() != null) {
				for(SalesProduct sp : sales.getSalesProducts()) {
					total = total + sp.getUnitSales() * sp.getQuantity();
				}
			}
			// add delivery discount
			total = total + this.getDeliveryFees();
			
			total = total * wallet.getDiscountPercentage();
		}
		
		
		//add discount on the delivery
		
		
		else {
			total = total * 0;
		}
		return total;
	}

	private List<PurchaseProduct> getAllPurchaseProducts() {
		List<PurchaseProduct> pps = new ArrayList<>();
		for (Purchase p : purchases) {
			pps.addAll(p.getPurchaseProducts());
		}
		return pps;
	}

	public void initSales() {
		sales.setSalesProducts(new ArrayList<>());
		List<PurchaseProduct> pps = getAllPurchaseProducts();
		for (PurchaseProduct pp : pps) {
			SalesProduct sp = new SalesProduct();
			sp.setProduct(pp.getProduct());
			sp.setProductId(pp.getProductId());
			sp.setPurchaseProduct(pp);
			sp.setQuantity(pp.getQuantity());
			sp.setUnitCost(pp.getUnitCost());
			sp.setUnitCostWv(pp.getUnitCostWv());
			WalletItem wi = this.getWalletItemFromPurchaseProduct(pp);
			sp.setUnitSales(wi.getUnitSales());
			sp.setUnitSalesWv(wi.getUnitSalesWv());
			sales.getSalesProducts().add(sp);
		}
	}

	private WalletItem getWalletItemFromPurchaseProduct(PurchaseProduct pp) {
		for (WalletItem wi : wallet.getWalletItems()) {

			if (wi.getId() == pp.getWalletItemId()) {
				return wi;
			}
		}
		return null;
	}

	private Thread initPurchasesProducts(String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (PurchaseProduct pp : SalesWalletBean.this.getAllPurchaseProducts()) {
						if (pp.getProductId() > 0) {
							Response r2 = PojoRequester.getSecuredRequest(AppConstants.getProduct(pp.getProductId()),
									header);
							if (r2.getStatus() == 200) {
								pp.setProduct(r2.readEntity(Product.class));
							}
						}

					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}

	private void initWallet(String param) throws Exception {
		wallet = new Wallet();
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getPurchasedWallet(id));
		if (r.getStatus() == 200) {
			wallet = r.readEntity(Wallet.class);
		} else {
			throw new Exception();
		}
	}

	private Thread initCart(String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCart(wallet.getCartId()), header);
					if (r.getStatus() == 200) {
						wallet.setCart(r.readEntity(Cart.class));
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public CartReview getReview() {
		return review;
	}

	public void setReview(CartReview review) {
		this.review = review;
	}
	
	

}
