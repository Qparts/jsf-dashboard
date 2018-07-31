package qetaa.jsf.dashboard.beans.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.event.DragDropEvent;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.model.cart.AdvisorCarts;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartAssignment;
import qetaa.jsf.dashboard.model.user.User;
 
@Named
@ViewScoped
public class AssignBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cart> unassigned;
	private List<AdvisorCarts> advisorCarts;
	private Cart selectedCart;

	@Inject
	private LoginBean loginBean;
	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		initUnassigned();
		initAdvisorCarts();
	}
	
	
	

	private void initUnassigned() {
		unassigned = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_UNASSIGNED_CARTS);
		if (r.getStatus() == 200) {
			this.unassigned = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {

		}
	}

	private void initAdvisorCarts() {
		advisorCarts = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_ADVISOR_QUOTATION_CARTS);
		if (r.getStatus() == 200) {
			this.advisorCarts = r.readEntity(new GenericType<List<AdvisorCarts>>() {
			});
		} else {

		}
	}

	public void unassignCart(Cart cart) {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UNASSIGN_CART, cart);
		if (r.getStatus() == 201) {
			init();
		} else {

		}
	}
	
	public void onCartDrop(DragDropEvent ddEvent){
		Cart cart = (Cart) ddEvent.getData();
		String s = ddEvent.getDropId();
		String[] parts = s.split(":");
		int index = Integer.valueOf(parts[2]);
		User assignedTo = this.advisorCarts.get(index).getUser();
		assignCart(assignedTo, cart);
		
	}
	
	private void assignCart(User assignedTo, Cart cart){
		CartAssignment ca = new CartAssignment();
		ca.setAssignedBy(loginBean.getUserHolder().getUser().getId());
		ca.setAssignedTo(assignedTo.getId());
		ca.setCartId(cart.getId());
		ca.setStage(cart.getStatus());
		Response r = reqs.postSecuredRequest(AppConstants.POST_ASSIGN_CART, ca);
		if(r.getStatus() == 201){
			init();
		}
		else{
			
		}	
	}

	public List<Cart> getUnassigned() {
		return unassigned;
	}

	public void setUnassigned(List<Cart> unassigned) {
		this.unassigned = unassigned;
	}

	public List<AdvisorCarts> getAdvisorCarts() {
		return advisorCarts;
	}

	public void setAdvisorCarts(List<AdvisorCarts> advisorCarts) {
		this.advisorCarts = advisorCarts;
	}

	public Cart getSelectedCart() {
		return selectedCart;
	}

	public void setSelectedCart(Cart selectedCart) {
		this.selectedCart = selectedCart;
	}

}
