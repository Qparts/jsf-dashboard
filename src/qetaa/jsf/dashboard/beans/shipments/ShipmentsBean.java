package qetaa.jsf.dashboard.beans.shipments;

import java.io.Serializable;
import java.util.ArrayList;
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

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.customer.CustomerAddress;
import qetaa.jsf.dashboard.model.payment.WalletItem;
import qetaa.jsf.dashboard.model.shipment.Courier;
import qetaa.jsf.dashboard.model.shipment.Shipment;
import qetaa.jsf.dashboard.model.shipment.ShipmentItem;

@Named
@ViewScoped
public class ShipmentsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<WalletItem> walletItems;
	private Shipment shipment;
	private List<CustomerAddress> addresses;

	@Inject
	private Requester reqs;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private CourierBean courierBean;

	@PostConstruct
	private void init() {
		try {
			shipment = new Shipment();
			walletItems = new ArrayList<>();
			String customerId = Helper.getParam("customer");
			initWalletItems(customerId);
			initAddresses();

		} catch (Exception ex) {
			Helper.redirect("wallets-process");
		}
	}

	private void initWalletItems(String customerId) throws Exception {
		Long cid = Long.parseLong(customerId);
		Response r = reqs.getSecuredRequest(AppConstants.getSoldWalletItems(cid));
		if(r.getStatus() == 200) {
			walletItems = r.readEntity(new GenericType<List<WalletItem>>() {});
			shipment.setCustomerId(cid);
			if(walletItems.isEmpty()) {
				throw new Exception();
			}
		}
		else {
			throw new Exception();
		}
	}
	
	private void initAddresses() {
		Set<Long> set = new HashSet<Long>();
		for(WalletItem wi : this.walletItems) {
			set.add(wi.getCartId());
		}
		Response r = reqs.postSecuredRequest(AppConstants.POST_ADDRESSES_FROM_CART_IDS, set);
		if(r.getStatus() == 200) {
			this.addresses = r.readEntity(new GenericType<List<CustomerAddress>>() {});
		}
		else {
			addresses = new ArrayList<>();
		}
		//addresses/cart-ids
	}
	
	public List<WalletItem> getSelectedWalletItems() {
		List<WalletItem> wis = new ArrayList<>();
		for(WalletItem wi : this.walletItems) {
			if(wi.isShipmentSelection()) {
				wis.add(wi);
			}
		}
		return wis;
	}
	
	public void createShipment() {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("customerId", this.shipment.getCustomerId());
		map.put("addressId", this.shipment.getAddressId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_SHIPMENT, map);
		if(r.getStatus() == 200) {
			Long shId = r.readEntity(Long.class);
			shipment.setId(shId);
			shipment.setBound('O');
			shipment.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
			initShipmentItems();
			initCourierTrackable();
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_SHIPMENT, shipment);
			if(r2.getStatus() == 201) {
				Helper.redirect("shipments?customer=" + this.shipment.getCustomerId());
			}
			else {
				Helper.addErrorMessage("An error occured");
			}
		}
	}
	
	private void initCourierTrackable() {
		for(Courier c : this.courierBean.getCouriers()) {
			if(c.getId() == this.shipment.getCourierId()) {
				shipment.setTrackable(c.isTrackable());
			}
		}
	}
	
	private void initShipmentItems() {
		shipment.setShipmentItems(new ArrayList<>());
		for(WalletItem wi : this.getSelectedWalletItems()) {
			if(wi.isShipmentSelection()) {
				ShipmentItem si = new ShipmentItem();
				si.setQuantity(wi.getQuantity());
				si.setShipmentId(shipment.getId());
				si.setShippedBy(loginBean.getUserHolder().getUser().getId());
				si.setWalletItemId(wi.getId());
				shipment.getShipmentItems().add(si);
			}
		}
	}

	public List<WalletItem> getWalletItems() {
		return walletItems;
	}

	public void setWalletItems(List<WalletItem> walletItems) {
		this.walletItems = walletItems;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public List<CustomerAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<CustomerAddress> addresses) {
		this.addresses = addresses;
	}


	
	
	
	
	
	

}
