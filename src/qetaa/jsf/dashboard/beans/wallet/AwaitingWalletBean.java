package qetaa.jsf.dashboard.beans.wallet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.BanksBean;
import qetaa.jsf.dashboard.beans.master.CitiesBean;
import qetaa.jsf.dashboard.beans.master.VendorBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.helpers.QuotationGenerator;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.payment.Bank;
import qetaa.jsf.dashboard.model.payment.Wallet;
import qetaa.jsf.dashboard.model.payment.WalletItem;
import qetaa.jsf.dashboard.model.payment.WalletItemVendor;
import qetaa.jsf.dashboard.model.payment.WalletQuotation;
import qetaa.jsf.dashboard.model.payment.WalletQuotationItem;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;
import qetaa.jsf.dashboard.model.vendor.Vendor;

@Named
@ViewScoped
public class AwaitingWalletBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Wallet wallet;
	private int bankId;
	private boolean refund;
	private boolean quotationAvailable;
	private Purchase purchase;
	private boolean purchaseAvailable;
	private PurchaseProduct purchaseProduct;
	private WalletItem selectedWalletItem;
	private List<Vendor> pricingVendors;
	private int newPricingVendorId;
	private CartReview review;

	@Inject
	private Requester reqs;
	@Inject
	private BanksBean bankBean;
	@Inject
	private LoginBean loginBean;

	@Inject
	private CitiesBean citiesBean;

	@Inject
	private VendorBean vendorBean;

	@PostConstruct
	private void init() {
		this.review = new CartReview();
		purchase = new Purchase();
		purchase.setPurchaseProducts(new ArrayList<>());
		selectedWalletItem = new WalletItem();
		refund = false;
		String s = Helper.getParam("wallet");
		try {
			initWallet(s);
			String header = reqs.getSecurityHeader();
			Thread[] threads = new Thread[2];
			threads[0] = initProducts(header);
			threads[1] = initCart(header);
			for (int i = 0; i < threads.length; i++) {
				try {
					threads[i].start();
					threads[i].join();
				} catch (InterruptedException e) {
				}
			}
			initCartVariables();
			initPricingVendors();
			initWalletItemVendors();
		} catch (Exception ex) {
			ex.printStackTrace();
			Helper.redirect("wallets-process");
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

	public void addNewPricingVendor() {
		Vendor vendor = vendorBean.getVendorFromId(this.newPricingVendorId);
		if (!pricingVendors.contains(vendor)) {
			this.pricingVendors.add(vendor);
			initWalletItemVendors();
			Helper.addInfoMessage("new pricing vendor added");
		} else {
			Helper.addErrorMessage("this vendor is already added");
		}
	}

	private void initWalletItemVendors() {
		for (WalletItem walletItem : this.wallet.getProductWalletItem()) {
			for (Vendor vendor : this.pricingVendors) {
				if (walletItem.getWalletItemVendor(vendor.getId()) == null) {
					WalletItemVendor wiv = new WalletItemVendor();
					wiv.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
					wiv.setVendorId(vendor.getId());
					wiv.setWalletItemId(walletItem.getId());
					wiv.setNewlyAdded(true);
					walletItem.getWalletItemVendors().add(wiv);
				}
			}
		}
	}

	public void saveWalletItemVendors() {
		Set<WalletItemVendor> wivs = new HashSet<>();
		for (WalletItem walletItem : this.wallet.getProductWalletItem()) {
			wivs.addAll(walletItem.getWalletItemVendors());
		}
		Response r = reqs.postSecuredRequest(AppConstants.POST_WALLET_ITEM_VENDOR, wivs);
		if (r.getStatus() == 201) {
			Helper.addInfoMessage("vendor prices updated");
		} else {
			Helper.addErrorMessage("an error occured");
		}
	}
	
	public void updateWalletITem() {
		double vat = this.selectedWalletItem.getNewPrice() * this.wallet.getCart().getVatPercentage();
		double promoDisc = this.wallet.getDiscountPercentage() * this.selectedWalletItem.getNewPrice();
		this.selectedWalletItem.setUnitSales(this.selectedWalletItem.getNewPrice());
		this.selectedWalletItem.setUnitSalesWv(this.selectedWalletItem.getUnitSales() + vat);
		this.selectedWalletItem.setUnitSalesNet(this.selectedWalletItem.getNewPrice() - promoDisc);
		this.selectedWalletItem.setUnitSalesNetWv(this.selectedWalletItem.getUnitSalesNet() + vat);
		Response r = reqs.putSecuredRequest(AppConstants.PUT_WALLET_ITEM, this.selectedWalletItem);
		if(r.getStatus() == 201) {
			Helper.redirect("wallet-awaiting?wallet=" + wallet.getId());
		}
		else {
			Helper.addErrorMessage("An errro occured");
		}
	}

	private void initPricingVendors() {
		this.pricingVendors = new ArrayList<>();
		City city = citiesBean.getCityFromId(this.wallet.getCart().getCityId());
		Response r = reqs.getSecuredRequest(AppConstants.getRegionVendors(city.getRegion().getId()));
		if (r.getStatus() == 200) {
			List<Vendor> regionVendors = r.readEntity(new GenericType<List<Vendor>>() {
			});
			List<Vendor> makeVendors = vendorBean.getMakeVendors(wallet.getCart().getMakeId());
			for (Vendor v : regionVendors) {
				if (makeVendors.contains(v)) {
					this.pricingVendors.add(v);
				}
			}
		}

		for (WalletItem wi : this.wallet.getWalletItems()) {
			for (WalletItemVendor wiv : wi.getWalletItemVendors()) {
				Vendor v = vendorBean.getVendorFromId(wiv.getVendorId());
				if (!pricingVendors.contains(v)) {
					pricingVendors.add(v);
				}
			}
		}
	}

	private Bank getSelectedBank() {
		for (Bank b : bankBean.getActiveBanks()) {
			if (b.getBankId() == this.bankId) {
				return b;
			}
		}
		return null;
	}

	public void createPurchase() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_PURCHASE, "");
		if (r.getStatus() == 200) {
			Long purchaseId = r.readEntity(Long.class);
			List<WalletItem> pwi = new ArrayList<>();
			pwi.addAll(this.getPurchaseWalletItems());
			purchase.setId(purchaseId);
			purchase.setCartId(this.wallet.getCartId());
			purchase.setCreatedBy(loginBean.getUserHolder().getUser().getId());
			purchase.setCustomerId(wallet.getCustomerId());
			purchase.setDueDate(new Date());
			purchase.setMakeId(wallet.getCart().getMakeId());
			purchase.setPaymentStatus('I');// incomplete
			purchase.setCreated(new Date());
			purchase.setPurchasePayments(new ArrayList<>());
			purchase.setTransactionType('T');
			for (WalletItem wi : pwi) {
				if (wi.getItemType() == 'P') {
					PurchaseProduct pp = new PurchaseProduct();
					pp.setProductId(wi.getProductId());
					pp.setQuantity(wi.getNewQuantity());
					pp.setUnitCost(null);
					pp.setUnitCostWv(null);
					pp.setWalletItemId(wi.getId());
					purchase.getPurchaseProducts().add(pp);
				}
			}

			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_PURCHASE, purchase);
			if (r2.getStatus() == 201) {
				Helper.redirect("wallet-awaiting?wallet=" + this.wallet.getId());
			}
		}

	}
	
	public void createQuotation() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_WALLET_QUOTATION, wallet.getId());
		if(r.getStatus() == 201) {
			Long wqId = r.readEntity(Long.class);
			WalletQuotation wq = new WalletQuotation();
			wq.setCreatedBy(loginBean.getLoggedUserId());
			wq.setId(wqId);
			wq.setWalletId(wallet.getId());
			initQuotableSelectedWalletItems(wq);
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_QUOTATION_WALLET, wq);
			if (r2.getStatus() == 201) {
				Helper.redirect("wallet-awaiting?wallet=" + this.wallet.getId());
			}
		}
	}

	public void refundItems() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_WALLET_REFUND, wallet.getCartId());
		if (r.getStatus() == 201) {
			Long walletId = r.readEntity(Long.class);
			Wallet rwallet = new Wallet();
			rwallet.setBank(getSelectedBank());
			rwallet.setId(walletId);
			rwallet.setBankConfirmedBy(loginBean.getUserHolder().getUser().getId());
			rwallet.setCartId(wallet.getCartId());
			rwallet.setCcCompany(null);
			rwallet.setCreated(new Date());
			rwallet.setCreditFees(null);
			rwallet.setCurrency("SAR");
			rwallet.setCustomerId(wallet.getCustomerId());
			rwallet.setCustomerName(wallet.getCustomerName());
			rwallet.setDiscountPercentage(wallet.getDiscountPercentage());
			rwallet.setGateway(null);
			rwallet.setPaymentType("wiretransfer");
			rwallet.setStatus('R');// refunded
			rwallet.setTransactionId(null);
			rwallet.setWalletType('R');// refund wallet
			initRefundSelectedWalletItems(rwallet);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("originalWalletId", this.wallet.getId());
			map.put("wallet", rwallet);
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_REFUND_WALLET, map);
			if (r2.getStatus() == 201) {
				Helper.redirect("wallet-awaiting?wallet=" + this.wallet.getId());
			}
		}
	}

	public List<WalletItem> getSelectedRefundItems() {
		List<WalletItem> walletItems = new ArrayList<>();
		for (WalletItem wi : wallet.getWalletItems()) {
			if (wi.isRefund()) {
				walletItems.add(wi);
			}
		}
		return walletItems;
	}
	
	public List<WalletItem> getSelectedQuotableItems() {
		List<WalletItem> walletItems = new ArrayList<>();
		for (WalletItem wi : wallet.getWalletItems()) {
			if (wi.isQuotation()) {
				walletItems.add(wi);
			}
		}
		return walletItems;
	}

	public List<WalletItem> getSelectedPurchaseItems() {
		List<WalletItem> walletItems = new ArrayList<>();
		for (WalletItem wi : wallet.getWalletItems()) {
			if (wi.isPurchase()) {
				walletItems.add(wi);
			}
		}
		return walletItems;
	}
	
	private void initQuotableSelectedWalletItems(WalletQuotation wq) {
		List<WalletQuotationItem> wqItems = new ArrayList<>();
		for(WalletItem walletItem : this.wallet.getWalletItems()) {
			if(walletItem.isQuotation()) {
				WalletQuotationItem wqi = new WalletQuotationItem();
				wqi.setWalletId(wq.getWalletId());
				wqi.setWalletItemId(walletItem.getId());
				wqi.setWalletQuotationId(wq.getId());
				wqItems.add(wqi);
			}
		}
		wq.setWalletQuotationItems(wqItems);
	}

	private void initRefundSelectedWalletItems(Wallet rwallet) {
		List<WalletItem> walletItems = new ArrayList<>();
		for (WalletItem walletItem : this.wallet.getWalletItems()) {
			if (walletItem.isRefund()) {
				WalletItem wi = new WalletItem();
				wi.setCartId(walletItem.getCartId());
				wi.setWalletId(rwallet.getId());
				wi.setItemDesc(walletItem.getItemDesc());
				wi.setQuantity(walletItem.getNewQuantity());
				wi.setItemNumber(walletItem.getItemNumber());
				wi.setItemType(walletItem.getItemType());// its a product item
				wi.setProductId(walletItem.getProductId());
				wi.setPurchasedItemId(null);// not yet purchased
				wi.setRefundedItemId(walletItem.getId());
				wi.setRefundNote("Wallet Refunded");// no refund note
				wi.setStatus('R');// refunded
				wi.setUnitQuotedCost(walletItem.getUnitQuotedCost());
				wi.setUnitQuotedCostWv(walletItem.getUnitQuotedCostWv());
				wi.setUnitSales(walletItem.getUnitSales());
				wi.setUnitSalesWv(walletItem.getUnitSalesWv());
				wi.setUnitSalesNet(walletItem.getUnitSalesNet());
				wi.setUnitSalesNetWv(walletItem.getUnitSalesNetWv());
				wi.setVendorId(walletItem.getVendorId());
				walletItems.add(wi);
			}
		}
		rwallet.setWalletItems(walletItems);
	}

	public List<WalletItem> getPurchaseWalletItems() {
		List<WalletItem> wis = new ArrayList<>();
		for (WalletItem wi : wallet.getWalletItems()) {
			if (wi.isPurchase()) {
				wis.add(wi);
			}
		}
		return wis;
	}

	public boolean isRefundable() {
		boolean refund = false;
		if (wallet.getWalletItems() != null) {
			for (WalletItem wi : this.wallet.getWalletItems()) {
				if (wi.isRefund()) {
					refund = true;
					break;
				}
			}
		}
		return refund;
	}
	
	public boolean isQuotable() {
		boolean quotable = false;
		if (wallet.getWalletItems() != null) {
			for (WalletItem wi : this.wallet.getWalletItems()) {
				if (wi.isQuotation()) {
					quotable = true;
					break;
				}
			}
		}
		return quotable;
	}

	public boolean isPurchaseable() {
		boolean purchasable = false;
		if (wallet.getWalletItems() != null) {
			for (WalletItem wi : this.wallet.getWalletItems()) {
				if (wi.isPurchase()) {
					purchasable = true;
					break;
				}
			}
		}
		return purchasable;
	}

	public double getTotalProductsSales() {
		double total = 0;
		if (wallet.getWalletItems() != null) {
			for (WalletItem wi : wallet.getWalletItems()) {
				if (wi.getItemType() == 'P') {
					total = total + wi.getUnitSales() * wi.getQuantity();
				}
			}
		}
		return total;
	}

	public double getTotalVat() {
		double total = 0;
		if (wallet.getWalletItems() != null) {
			for (WalletItem wi : wallet.getWalletItems()) {
				total = total + ((wi.getUnitSalesWv() - wi.getUnitSales()) * wi.getQuantity());
			}
		}
		return total;
	}

	public double getTotalDiscounts() {
		double total = 0;
		if (wallet.getWalletItems() != null) {
			for (WalletItem wi : wallet.getWalletItems()) {
				total = total + ((wi.getUnitSales() - wi.getUnitSalesNet()) * wi.getQuantity());
			}
		}
		return total;
	}

	public StreamedContent getPdfQuotation(long walletQuotationId) {
		WalletQuotation walletQuotation = null;
		for(WalletQuotation wq : this.wallet.getWalletQuotations()) {
			if(wq.getId() == walletQuotationId)
				walletQuotation = wq;
		}
		QuotationGenerator ig = new QuotationGenerator(wallet, walletQuotation);
		return new DefaultStreamedContent(ig.getInputStream(), "application/pdf", ig.getIdString() + ".pdf");
	}

	public double getDelivery() {
		double total = 0;
		if (wallet.getWalletItems() != null) {
			for (WalletItem wi : wallet.getWalletItems()) {
				if (wi.getItemType() == 'D') {
					total = total + wi.getUnitSales();
				}
			}
		}
		return total;
	}

	public double getTotalNet() {
		double total = 0;
		if (wallet.getWalletItems() != null) {
			for (WalletItem wi : wallet.getWalletItems()) {
				double sum = wi.getUnitSalesNetWv() * (wi.getItemType() == 'P' ? wi.getQuantity() : 1);
				total = total + sum;
			}
		}
		return total;
	}

	private void initWallet(String param) throws Exception {
		wallet = new Wallet();
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getAwaitingWallet(id));
		if (r.getStatus() == 200) {
			wallet = r.readEntity(Wallet.class);
			for (WalletItem walletItem : wallet.getWalletItems()) {
				walletItem.setNewQuantity(walletItem.getQuantity());
			}
		} else {
			throw new Exception();
		}
	}

	public void chooseWalletItem(WalletItem walletItem) {
		purchaseProduct = new PurchaseProduct();
		purchaseProduct.setProductId(walletItem.getProductId().longValue());
		purchaseProduct.setProduct(walletItem.getProduct());
		purchaseProduct.setQuantity(walletItem.getQuantity());
		purchaseProduct.setNewQuantity(walletItem.getQuantity());
		purchaseProduct.setUnitCostWv(walletItem.getUnitQuotedCostWv());
		purchaseProduct.setUnitCost(walletItem.getUnitQuotedCost());
		this.purchaseProduct.setWalletItem(walletItem);
		this.selectedWalletItem = walletItem;
	}

	private Thread initProducts(String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread[] threads = new Thread[wallet.getWalletItems().size() - 1];
					int index = 0;
					for (WalletItem wi : wallet.getWalletItems()) {
						if (wi.getItemType() == 'P') {
							threads[index] = ThreadRunner.initProduct(wi, wi.getCartId(), header);
							threads[index].start();
							threads[index].join();
							index++;
						}
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
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

	public void editRefund() {
		if (refund) {
			for (WalletItem wi : wallet.getWalletItems()) {
				wi.setNewQuantity(wi.getQuantity());
				wi.setRefund(false);
			}
			refund = false;
		} else {
			refund = true;
		}
	}
	
	public void editQuotation() {
		if (quotationAvailable) {
			for (WalletItem wi : wallet.getWalletItems()) {
				wi.setQuotation(false);
			}
			quotationAvailable = false;
		} else {
			quotationAvailable = true;
		}
	}

	public void editPurchase() {
		if (purchaseAvailable) {
			for (WalletItem wi : wallet.getWalletItems()) {
				wi.setPurchase(false);
				wi.setNewQuantity(wi.getQuantity());
			}
			purchaseAvailable = false;
		} else {
			purchaseAvailable = true;
		}
	}

	public void recalculatePrice() {
		purchaseProduct.setUnitCost(this.deductAddedPercentage(purchaseProduct.getUnitCostWv(), .05));
	}

	private double deductAddedPercentage(double orig, double percentage) {
		double x = orig / (1.0 + percentage);
		return x;
	}

	public void addToPurchaseOrder() {
		boolean found = false;
		for (PurchaseProduct pp : this.purchase.getPurchaseProducts()) {
			if (pp.getWalletItem().getId() == this.purchaseProduct.getWalletItem().getId()) {
				found = true;
			}
		}
		if (!found) {
			PurchaseProduct pp = new PurchaseProduct();
			pp.setQuantity(purchaseProduct.getNewQuantity());
			pp.setWalletItem(purchaseProduct.getWalletItem());
			pp.setProduct(purchaseProduct.getProduct());
			pp.setProductId(purchaseProduct.getProductId());
			pp.setUnitCost(purchaseProduct.getUnitCost());
			pp.setUnitCostWv(purchaseProduct.getUnitCostWv());
			purchase.getPurchaseProducts().add(pp);
			purchaseProduct = new PurchaseProduct();
		} else {
			Helper.addErrorMessage("Product already added");
		}
	}

	private void prepareCartReview() {
		review.setStage(6);
		review.setCartId(this.wallet.getCart().getId());
		review.setReviewerId(this.loginBean.getUserHolder().getUser().getId());
		review.setStatus(review.getStatusFromActionValue());
	}

	public void submitReview() {
		prepareCartReview();
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, this.review);
		if (r.getStatus() == 200) {
			Helper.redirect("wallet-awaiting?wallet=" + this.wallet.getId());
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public WalletItem getSelectedWalletItem() {
		return selectedWalletItem;
	}

	public void setSelectedWalletItem(WalletItem selectedWalletItem) {
		this.selectedWalletItem = selectedWalletItem;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public boolean isRefund() {
		return refund;
	}

	public boolean isPurchaseAvailable() {
		return purchaseAvailable;
	}

	public PurchaseProduct getPurchaseProduct() {
		return purchaseProduct;
	}

	public void setPurchaseProduct(PurchaseProduct purchaseProduct) {
		this.purchaseProduct = purchaseProduct;
	}

	public Purchase getPurchase() {
		return this.purchase;
	}

	public List<Vendor> getPricingVendors() {
		return pricingVendors;
	}

	public void setPricingVendors(List<Vendor> pricingVenodrs) {
		this.pricingVendors = pricingVenodrs;
	}

	public int getNewPricingVendorId() {
		return newPricingVendorId;
	}

	public void setNewPricingVendorId(int newPricingVendorId) {
		this.newPricingVendorId = newPricingVendorId;
	}

	public CartReview getReview() {
		return review;
	}

	public void setReview(CartReview review) {
		this.review = review;
	}

	public boolean isQuotationAvailable() {
		return quotationAvailable;
	}

	public void setQuotationAvailable(boolean quotationAvailable) {
		this.quotationAvailable = quotationAvailable;
	}
	
	

}
