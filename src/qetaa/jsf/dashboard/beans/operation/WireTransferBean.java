package qetaa.jsf.dashboard.beans.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.BanksBean;
import qetaa.jsf.dashboard.beans.master.CitiesBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.cart.PartsOrderItemApproved;
import qetaa.jsf.dashboard.model.cart.WireTransfer;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.payment.Bank;
import qetaa.jsf.dashboard.model.payment.PartsPayment;
import qetaa.jsf.dashboard.model.payment.Wallet;
import qetaa.jsf.dashboard.model.payment.WalletItem;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

@Named(value = "wiretransferBean")
@ViewScoped
public class WireTransferBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private WireTransfer wire;
	private CartReview cartReview;
	private PartsPayment partsPayment;
	private int bankId;

	@Inject
	private Requester reqs;

	@Inject
	private BanksBean banksBean;

	@Inject
	private CitiesBean citiesBean;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("cart");
			partsPayment = new PartsPayment();
			cartReview = new CartReview();
			initWire(s);
			initParts();
			initCity();
			initPromoCode();
		} catch (Exception ex) {
			Helper.redirect("wire_transfers");
		}
	}
	
	private void initParts() {
		Response r2 = reqs.getSecuredRequest(AppConstants.getFullPartsApprovedItems(wire.getCartId()));
		if(r2.getStatus() == 200) {
			List<PartsOrderItemApproved> approved = r2.readEntity(new GenericType<List<PartsOrderItemApproved>>() {});
			wire.getCart().setPartsItemsApproved(approved);
		}
	}
	
	private void initPromoCode() {
		if (wire.getCart().getPromotionCode() != null) {
			Response r = reqs.getSecuredRequest(AppConstants.getPromoCode(wire.getCart().getPromotionCode()));
			if (r.getStatus() == 200) {
				wire.getCart().setPromoCodeObject(r.readEntity(PromotionCode.class));
			}
		}
	}
	

	
	private void initCity() {
		for (City city : citiesBean.getCities()) {
			if (city.getId() == wire.getCart().getCityId()) {
				wire.getCart().setCity(city);
				break;
			}
		}
	}
	
	private void initWire(String s) throws Exception {
		wire = new WireTransfer();
		Long cartId = Long.parseLong(s);
		Response r = reqs.getSecuredRequest(AppConstants.getWireTransfer(cartId));
		if (r.getStatus() == 200) {
			wire = r.readEntity(WireTransfer.class);
		} else {
			throw new Exception();
		}
	}
	
	public void deleteWireTransfer() {
		this.wire.setConfirmedBy(loginBean.getUserHolder().getUser().getId());
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UNDO_WIRE_TRANSFER, this.wire);
		if(r.getStatus() == 201) {
			Helper.addInfoMessage("Wire Transfer cancelled");
			init();
		}
		else {
			Helper.addErrorMessage("Something went wrong");
		}
	}
	
	private double getCalculatedDiscountPercentage() {
		if(wire.getCart().getPromoCodeObject() != null) {
			if(wire.getCart().getPromoCodeObject().isDiscountPromo()) {
				return wire.getCart().getPromoCodeObject().getDiscountPercentage();
			}
		}
		return 0;
	}
	
	public void fundWallet() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_WALLET, wire.getCartId());
		if(r.getStatus() == 201) {
			Long walletId = r.readEntity(Long.class);
			Wallet wallet = new Wallet();
			wallet.setBank(getSelectedBank());
			wallet.setId(walletId);
			wallet.setBankConfirmedBy(loginBean.getUserHolder().getUser().getId());
			wallet.setCartId(wire.getCartId());
			wallet.setCcCompany(null);
			wallet.setCreated(new Date());
			wallet.setCreditFees(null);
			wallet.setCurrency("SAR");
			wallet.setCustomerId(wire.getCustomerId());
			wallet.setCustomerName(wire.getCustomer().getFullName());
			wallet.setDiscountPercentage(getCalculatedDiscountPercentage());
			wallet.setGateway(null);
			wallet.setPaymentType("wiretransfer");
			wallet.setStatus('A');
			wallet.setTransactionId(null);
			wallet.setWalletType('P');
			initWalletItems(wallet);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("wireId", wire.getId());
			map.put("wallet", wallet);
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_FUND_WALLET, map);
			if(r2.getStatus() == 201) {
				Helper.redirect("wire_transfers");
			}
			else {
				Helper.addErrorMessage("Something went wrong");
			}
		}
		
		
	}
	
	private void initWalletItems(Wallet wallet) {
		List<WalletItem> walletItems = new ArrayList<>();
		for(PartsOrderItemApproved approved : wire.getCart().getPartsItemsApproved()) {
			WalletItem wi = new WalletItem();
			wi.setCartId(wire.getCartId());
			wi.setItemDesc(approved.getItemDesc());
			wi.setQuantity(approved.getApprovedQuantity());
			wi.setItemNumber(approved.getItemNumber());
			wi.setItemType('P');//its a product item
			wi.setProductId(approved.getProductId());
			wi.setPurchasedItemId(null);//not yet purchased
			wi.setRefundedItemId(null);//net yet refunded
			wi.setRefundNote(null);//no refund note
			wi.setStatus('A');
			
			wi.setUnitQuotedCost(approved.getCostPrice() / 1.05);
			wi.setUnitQuotedCostWv(approved.getCostPrice());
			wi.setUnitSales(approved.getSalesPrice());
			double vat = approved.getSalesPrice() * wire.getCart().getVatPercentage();
			double discount = approved.getSalesPrice() * wallet.getDiscountPercentage();
			wi.setUnitSalesWv(approved.getSalesPrice() + vat);
			wi.setUnitSalesNet(wi.getUnitSales() - discount);
			wi.setUnitSalesNetWv(wi.getUnitSalesNet() + vat);
			wi.setVendorId(approved.getVendorId());
			walletItems.add(wi);
		}
		//add delivery as a wallet item
		WalletItem di = new WalletItem();
		di.setCartId(wire.getCartId());
		di.setItemDesc("Delivery - رسوم التوصيل");
		di.setItemNumber("");
		di.setItemType('D');//its a delivery item
		di.setProductId(null);
		di.setPurchasedItemId(null);//not yet purchased
		di.setRefundedItemId(null);//net yet refunded
		di.setRefundNote(null);//no refund note
		di.setStatus('A');
		di.setUnitQuotedCost(0);
		di.setUnitQuotedCostWv(0);
		di.setUnitSales(wire.getCart().getDeliveryFees());
		double vat = wire.getCart().getDeliveryFees() * wire.getCart().getVatPercentage();
		double discount = wire.getCart().getDeliveryFees() * wallet.getDiscountPercentage();
		
		di.setUnitSalesWv(wire.getCart().getDeliveryFees() + vat);
		
		di.setUnitSalesNet(di.getUnitSales() - discount);
		di.setUnitSalesNetWv(di.getUnitSalesNet() + vat);			
		di.setVendorId(null);
		walletItems.add(di);
		
		wallet.setWalletItems(walletItems);
	}
	
	//to be retired
	public void confirmPartsPayment() {
		partsPayment = new PartsPayment(wire);
		prepareBank();
		Response r = reqs.postSecuredRequest(AppConstants.POST_BANK_PARTS_PAYMENT, partsPayment);
		if (r.getStatus() == 200) {
			wire.setConfirmedBy(loginBean.getUserHolder().getUser().getId());
			// update cart and deactivate payment request
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_CONFIRM_WIRE_TRANSFER, this.wire);
			if (r2.getStatus() == 200) {
				Helper.redirect("wire_transfers");
			}
		}
	}
	
	private void prepareCartReview() {
		cartReview.setStage(4);
		cartReview.setCartId(this.wire.getCartId());
		cartReview.setReviewerId(this.loginBean.getUserHolder().getUser().getId());
		cartReview.setStatus(cartReview.getStatusFromActionValue());
	}
	
	public void submitReview() {
		prepareCartReview();
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, this.cartReview);
		if (r.getStatus() == 200) {
				Helper.redirect("wire-transfer?cart=" + wire.getCartId());
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	private Bank getSelectedBank() {
		for (Bank b : banksBean.getActiveBanks()) {
			if (b.getBankId() == this.bankId) {
				return b;
			}
		}
		return null;
	}
	
	private void prepareBank() {
		for (Bank b : banksBean.getActiveBanks()) {
			if (b.getBankId() == this.bankId) {
				partsPayment.setBank(b);
				partsPayment.setBankConfirmedBy(this.loginBean.getUserHolder().getUser().getId());
				break;
			}
		}
	}

	public WireTransfer getWire() {
		return wire;
	}

	public void setWire(WireTransfer wire) {
		this.wire = wire;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public CartReview getCartReview() {
		return cartReview;
	}

	public void setCartReview(CartReview cartReview) {
		this.cartReview = cartReview;
	}
	
	


}
