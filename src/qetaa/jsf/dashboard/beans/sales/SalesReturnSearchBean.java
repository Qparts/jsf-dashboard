package qetaa.jsf.dashboard.beans.sales;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.sales.Sales;

@Named
@SessionScoped 
public class SalesReturnSearchBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long cartId;
	private List<Sales> salesOrders;
	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		salesOrders = new ArrayList<>();
	}
	
	public void searchSales() {
		Response r = reqs.getSecuredRequest(AppConstants.getSalesFromCartId(cartId));
		System.out.println(r.getStatus());
		if(r.getStatus() == 200) {
			this.salesOrders = r.readEntity(new GenericType<List<Sales>>() {});
			initCarts();
		}
		else {
			Helper.addErrorMessage("Sales Not Found");
		}
	}
	
	private void initCarts() {
		for(Sales s : salesOrders) {
			Response r = reqs.getSecuredRequest(AppConstants.getCart(s.getCartId()));
			if(r.getStatus() == 200) {
				s.setCart(r.readEntity(Cart.class));
			}
		}
	}
	
	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public List<Sales> getSalesOrders() {
		return salesOrders;
	}

	public void setSalesOrders(List<Sales> salesOrders) {
		this.salesOrders = salesOrders;
	}
	
	
	
	
	
	
	
	
}
