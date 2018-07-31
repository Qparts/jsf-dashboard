package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.List;

import qetaa.jsf.dashboard.model.user.User;


public class AdvisorCarts implements Serializable{

	private static final long serialVersionUID = 1L;
	private User user;
	private List<Cart> carts;
	public User getUser() {
		return user;
	}
	public List<Cart> getCarts() {
		return carts;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}
	
	
}
