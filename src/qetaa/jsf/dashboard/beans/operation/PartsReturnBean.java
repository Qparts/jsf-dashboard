package qetaa.jsf.dashboard.beans.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.cart.PartsOrder;
import qetaa.jsf.dashboard.model.cart.PartsOrderItemApproved;
import qetaa.jsf.dashboard.model.cart.PartsOrderItemReturn;
import qetaa.jsf.dashboard.model.location.City;

@Named
@SessionScoped
public class PartsReturnBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long cartId;
	private Cart cart;
	private PartsOrderItemApproved selectedApproved;
	private PartsOrderItemReturn returnItem;
	private List<PartsOrderItemReturn> returnItems;

	@Inject
	private Requester reqs;
	
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		selectedApproved = new PartsOrderItemApproved();
		returnItem = new PartsOrderItemReturn();
		returnItems = new ArrayList<>();
		cartId = 0L;
		cart = new Cart();
	}
	
	public double totalReturnedAmount() {
		double total = 0;
		for(PartsOrderItemReturn r : this.returnItems) {
			total = total + (r.getReturnAmount() * r.getReturnQuantity() - r.getCostPrice() * r.getReturnQuantity());
		}
		return total;
	}
	
	public double totalReturnedShipmentCost() {
		double total = 0;
		for(PartsOrderItemReturn r : this.returnItems) {
			total = total + r.getShipmentCost();
		}
		return total;
	}
	
	public void initReturnItem(PartsOrderItemApproved approved) {
		selectedApproved = approved;
		returnItem = new PartsOrderItemReturn();
		returnItem.setApprovedId(approved.getId());
		returnItem.setReturnAmount(approved.getSalesPrice());
		returnItem.setCartId(approved.getCartId());
		returnItem.setCostPrice(approved.getCostPrice());
		returnItem.setPartsItemId(approved.getPartsItemId());
		returnItem.setPartsOrderId(approved.getPartsOrderId());
		returnItem.setReturnBy(this.loginBean.getUserHolder().getUser().getId());
		returnItem.setReturnQuantity(approved.getApprovedQuantity());
		returnItem.setVendorId(approved.getVendorId());
		returnItem.setItemNumber(approved.getItemNumber());
	}
	
	public void returnItem() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_RETURN_ITEM, returnItem);
		if(r.getStatus() == 201) {
			init();
		}
		else if(r.getStatus() == 403) {
			Helper.addErrorMessage("Return Items exceeded order quantity");
		}
		else if(r.getStatus() == 409) {
			
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	
	public void findCart() {
		Response r = reqs.getSecuredRequest(AppConstants.getPartsOrderCart(cartId));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
			initVariables();
		} else if (r.getStatus() == 404) {
			Helper.addErrorMessage("Cart not found");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	private void initVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[5];
		Thread t1 = initReviews(cart, header);
		Thread t2 = initCity(cart, header);
		Thread t3 = initApprovedItems(cart, header);
		Thread t4 = initPartsOrder(cart, header);
		Thread t5 = initReturnItems(cart, header);
		threads[0] = t1;
		threads[1] = t2;
		threads[2] = t3;
		threads[3] = t4;
		threads[4] = t5;
		for (int i = 0; i < threads.length; i++)
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	private Thread initPartsOrder(Cart cart2, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getPartsOrder(cart.getId()), header);
					if (r.getStatus() == 200) {
						cart.setPartsOrder(r.readEntity(PartsOrder.class));
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initReturnItems(Cart cart2, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getPartsItemReturns(cart.getId()), header);
					if (r.getStatus() == 200) {
						returnItems = r.readEntity(new GenericType<List<PartsOrderItemReturn>>() {});
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initReviews(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartReviews(cart.getId()), header);
					if (r.getStatus() == 200) {
						List<CartReview> reviews = r.readEntity(new GenericType<List<CartReview>>() {
						});
						cart.setReviews(reviews);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initCity(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCity(cart.getCityId()), header);
					if (r.getStatus() == 200) {
						City city = r.readEntity(City.class);
						cart.setCity(city);
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initApprovedItems(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getFullPartsApprovedItems(cart.getId()),
							header);
					if (r.getStatus() == 200) {
						List<PartsOrderItemApproved> approved = r
								.readEntity(new GenericType<List<PartsOrderItemApproved>>() {
								});
						cart.setPartsItemsApproved(approved);
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public PartsOrderItemApproved getSelectedApproved() {
		return selectedApproved;
	}

	public void setSelectedApproved(PartsOrderItemApproved selectedApproved) {
		this.selectedApproved = selectedApproved;
	}

	public PartsOrderItemReturn getReturnItem() {
		return returnItem;
	}

	public void setReturnItem(PartsOrderItemReturn returnItem) {
		this.returnItem = returnItem;
	}

	public List<PartsOrderItemReturn> getReturnItems() {
		return returnItems;
	}

	public void setReturnItems(List<PartsOrderItemReturn> returnItems) {
		this.returnItems = returnItems;
	}
	
	

}
