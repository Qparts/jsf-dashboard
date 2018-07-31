package qetaa.jsf.dashboard.beans.operation;
 
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

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.PartCollectionItem;
import qetaa.jsf.dashboard.model.cart.PartsOrderItemApproved;
import qetaa.jsf.dashboard.model.product.Product;

@Named
@ViewScoped
public class PartOrdersBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cart> carts;
	private Cart selectedCart;
	private double shipmentCost;
	private String shipmentReference;
	private String courrierName;

	@Inject
	private LoginBean loginBean;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		try {
		this.selectedCart = new Cart();
		initCarts();
		initCartVariables();
		}catch(InterruptedException ex) {
			
		}
	}

	private void initCarts() {
		carts = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_WAITING_PART_CARTS);
		if (r.getStatus() == 200) {
			this.carts = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {

		}
	}

	public void shipItems() throws InterruptedException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("cartId", String.valueOf(this.selectedCart.getId()));
		map.put("shipmentCost", String.valueOf(this.shipmentCost));
		map.put("shippedBy", String.valueOf(this.loginBean.getUserHolder().getUser().getId()));
		map.put("shipmentReference", this.shipmentReference);
		map.put("courrier", this.courrierName);
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_SHIP_ITEMS, map);
		if (r.getStatus() == 201) {
			//update address
			reqs.putSecuredRequest(AppConstants.PUT_UPDATE_ADDRESS, this.selectedCart.getAddress());
			this.init();
			Helper.addInfoMessage("Items Shipped!");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public void updateAddress() {
		
		
	}

	public void initCartVariables() throws InterruptedException {
		String header = reqs.getSecurityHeader();

		Thread[] mainThreads = new Thread[carts.size()];
		int index = 0;

		for (Cart cart : carts) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[6];
					threads[0] = ThreadRunner.initModelYear(cart, header);
					threads[1] = ThreadRunner.initCustomer(cart, header);
					threads[2] = ThreadRunner.initAddress(cart, header);
					threads[3] = initPartsApproved(cart, header);
					threads[4] = ThreadRunner.initReviews(cart, header);
					threads[5] = ThreadRunner.initPromoCode(cart, header);
					for (int i = 0; i < threads.length; i++)
						try {
							threads[i].start();
							threads[i].join();
						} catch (InterruptedException e) {
						}

				}
			});
			mainThreads[index] = t;
			index++;
		}
		for (int i = 0; i < mainThreads.length; i++) {
			mainThreads[i].start();
			mainThreads[i].join();
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

	private Thread initCollectionItems(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartCollectionItems(cart.getId()),
							header);
					if (r.getStatus() == 200) {
						List<PartCollectionItem> collections = r
								.readEntity(new GenericType<List<PartCollectionItem>>() {
								});
						cart.setCollections(collections);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public Cart getSelectedCart() {
		return selectedCart;
	}

	public void setSelectedCart(Cart selectedCart) {
		this.selectedCart = selectedCart;
	}

	public double getShipmentCost() {
		return shipmentCost;
	}

	public void setShipmentCost(double shipmentCost) {
		this.shipmentCost = shipmentCost;
	}

	public String getShipmentReference() {
		return shipmentReference;
	}

	public void setShipmentReference(String shipmentReference) {
		this.shipmentReference = shipmentReference;
	}

	public String getCourrierName() {
		return courrierName;
	}

	public void setCourrierName(String courrierName) {
		this.courrierName = courrierName;
	}

}
