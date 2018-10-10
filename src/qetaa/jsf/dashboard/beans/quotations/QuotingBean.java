package qetaa.jsf.dashboard.beans.quotations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.VendorBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.WebsocketLinks;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.cart.Quotation;
import qetaa.jsf.dashboard.model.cart.QuotationItem;
import qetaa.jsf.dashboard.model.cart.QuotationItemResponse;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.product.ProductPrice;

@Named
@ViewScoped
public class QuotingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cart> carts;
	private QuotationItem selectedQuotationItem;
	private QuotationItemResponse selectedQuotationItemResponse;
	private String partNumber;
	private Product foundProduct;
	private ProductPrice productPrice;
	private ProductPrice incompleteProductPrice;
	private boolean newPrice;
	private QuotationItem newQuotationItem;
	private Long positiveScore;
	private Long negativeScore;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@Inject
	private VendorBean vendorBean;

	@PostConstruct
	private void init() {
		newQuotationItem = new QuotationItem();
		newPrice = false;
		foundProduct = new Product();
		productPrice = new ProductPrice();
		incompleteProductPrice = new ProductPrice();
		selectedQuotationItem = new QuotationItem();
		selectedQuotationItemResponse = new QuotationItemResponse();
		initCarts();
		initCustomers();
		initQuotationItemProducts();
		initCurrentScore();
	}

	public void unassign(long cartId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", loginBean.getLoggedUserId());
		map.put("cartId", cartId);
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UNASSIGN_CART, map);
		if (r.getStatus() == 201) {
			//Helper.redirect("quoting");
		} else {
			Helper.addErrorMessage("An error occured");
		}

	}

	public void requestAssignment() {
		if (this.carts.size() > 4) {
			Helper.addErrorMessage("Maximum allowed carts is 5");
		} else {
			Response r = reqs.postSecuredRequest(AppConstants.POST_ASSIGN_CART, loginBean.getLoggedUserId());
			if (r.getStatus() == 404) {
				Helper.addErrorMessage("No carts available right now! try again later");
			} else if (r.getStatus() == 201) {
				Helper.redirect("quoting");
			}
		}
	}

	private void initCurrentScore() {
		Response r = reqs.getSecuredRequest(AppConstants.getCurrentFinderScore(loginBean.getLoggedUserId()));
		if (r.getStatus() == 200) {
			Map<String, Object> map = r.readEntity(Map.class);
			this.positiveScore = ((Number) map.get("positive")).longValue();
			this.negativeScore = ((Number) map.get("negative")).longValue();
		} else {
			this.positiveScore = 0L;
			this.negativeScore = 0L;
		}

	}

	private void initCarts() {
		Response r = reqs.getSecuredRequest(AppConstants.getAssignedCarts(loginBean.getLoggedUserId()));
		if (r.getStatus() == 200) {
			carts = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {
			carts = new ArrayList<>();
		}
	}
	
	private void initCustomers() {
		for(Cart cart: carts) {
			Response r = reqs.getSecuredRequest(AppConstants.getCustomer(cart.getCustomerId()));
			if(r.getStatus() == 200) {
				cart.setCustomer(r.readEntity(Customer.class));
			}else {
				
			}
		}
	}
	
	private void initCustomer(Cart cart) {
			Response r = reqs.getSecuredRequest(AppConstants.getCustomer(cart.getCustomerId()));
			if(r.getStatus() == 200) {
				cart.setCustomer(r.readEntity(Customer.class));
			}else {
				
			}
	}

	public void selectProductWithoutPrice() {
		QuotationItemResponse qir = new QuotationItemResponse();
		QuotationItem qi = this.selectedQuotationItem;
		qir.setCartId(qi.getCartId());
		qir.setCreatedBy(this.loginBean.getLoggedUserId());
		qir.setDefaultPercentage(null);
		qir.setDesc(qi.getItemDesc());
		qir.setProduct(foundProduct);
		qir.setProductId(foundProduct.getId());
		qir.setProductPrice(null);
		qir.setProductPriceId(null);
		qir.setQuantity(qi.getQuantity());
		qir.setQuotationId(qi.getQuotationId());
		qir.setQuotationItemId(qi.getId());
		qir.setStatus('I');
		this.selectedQuotationItem.getQuotationItemResponses().clear();
		this.selectedQuotationItem.getQuotationItemResponses().add(qir);
		Helper.addInfoMessage("Part number saved without price");

	}

	public void removeResponse(QuotationItemResponse qir, QuotationItem qi) {
		qi.getQuotationItemResponses().clear();
	}

	public void selectPriceComplete(ProductPrice pp) {
		QuotationItemResponse qir = this.selectedQuotationItemResponse;
		QuotationItem qi = getQuotationItemFromId(qir.getQuotationItemId());
		qir.setCreatedBy(loginBean.getLoggedUserId());
		qir.setDesc(qi.getItemDesc());
		qir.setProductPrice(pp.getCloned());
		qir.setProductPriceId(pp.getId());
		qir.setStatus('C');
		qir.setDefaultPercentage(
				vendorBean.getPercentage(pp.getVendorId(), this.getCartFromQuotationItem(qi).getMakeId()));
		Helper.addInfoMessage("Price selected");
	}

	public void selectPrice(ProductPrice pp) {
		QuotationItemResponse qir = new QuotationItemResponse();
		QuotationItem qi = this.getSelectedQuotationItem();
		qir.setCartId(this.selectedQuotationItem.getCartId());
		qir.setCreatedBy(this.loginBean.getLoggedUserId());
		qir.setDesc(qi.getItemDesc());
		qir.setProductPriceId(pp.getId());
		qir.setProductPrice(pp.getCloned());
		qir.setQuantity(qi.getQuantity());
		qir.setQuotationId(qi.getQuotationId());
		qir.setQuotationItemId(qi.getId());
		qir.setProduct(foundProduct.getClonedProduct());
		qir.setProductId(foundProduct.getId());
		qir.setStatus('C');
		qir.setDefaultPercentage(
				vendorBean.getPercentage(pp.getVendorId(), this.getCartFromQuotationItem(qi).getMakeId()));

		// only allow one product
		this.selectedQuotationItem.getQuotationItemResponses().clear();
		this.selectedQuotationItem.getQuotationItemResponses().add(qir);
		Helper.addInfoMessage("Price selected");
		/*
		 * boolean found = false; for(QuotationItemResponse qr :
		 * this.selectedQuotationItem.getQuotationItemResponses()) {
		 * if(qr.getProductId().equals(qir.getProductId())) {
		 * this.selectedQuotationItem.getQuotationItemResponses().remove(qr);
		 * this.selectedQuotationItem.getQuotationItemResponses().add(qir);
		 * Helper.addInfoMessage("Price Replaced"); found = true; break; } } if(!found)
		 * { this.selectedQuotationItem.getQuotationItemResponses().add(qir);
		 * Helper.addInfoMessage("Price Selected"); }
		 */

	}

	private QuotationItem getQuotationItemFromId(long qiId) {
		for (Cart cart : this.getCarts()) {
			for (QuotationItem qi : cart.getAllQuotationItems()) {
				if (qi.getId() == qiId) {
					return qi;
				}
			}
		}
		return null;
	}

	private void initQuotationItemProducts() {
		for (Cart cart : carts) {
			for (QuotationItem item : cart.getAllQuotationItems()) {
				for (QuotationItemResponse res : item.getQuotationItemResponses()) {
					if (res.getProductId() != null) {
						Response r = reqs.getSecuredRequest(AppConstants.getProductWithPriceList(res.getProductId()));
						if (r.getStatus() == 200) {
							Product p = r.readEntity(Product.class);
							res.setProduct(p);
						}
					}
					if (res.getProductPriceId() != null) {
						Response r = reqs.getSecuredRequest(AppConstants.getProductPrice(res.getProductPriceId()));
						if (r.getStatus() == 200) {
							ProductPrice pp = r.readEntity(ProductPrice.class);
							res.setProductPrice(pp);
						}
					}
				}
			}
		}
	}

	private void initQuotationItemProducts(Cart cart) {
		for (QuotationItem item : cart.getAllQuotationItems()) {
			for (QuotationItemResponse res : item.getQuotationItemResponses()) {
				if (res.getProductId() != null) {
					Response r = reqs.getSecuredRequest(AppConstants.getProductWithPriceList(res.getProductId()));
					if (r.getStatus() == 200) {
						Product p = r.readEntity(Product.class);
						res.setProduct(p);
					}
				}
				if (res.getProductPriceId() != null) {
					Response r = reqs.getSecuredRequest(AppConstants.getProductPrice(res.getProductPriceId()));
					if (r.getStatus() == 200) {
						ProductPrice pp = r.readEntity(ProductPrice.class);
						res.setProductPrice(pp);
					}
				}
			}
		}
	}

	public Cart getCartFromSelectedQuotationItemResponse() {
		for (Cart cart : this.getCarts()) {
			for (QuotationItem qi : cart.getAllQuotationItems()) {
				for (QuotationItemResponse qir : qi.getQuotationItemResponses()) {
					if (qir.getId() == this.selectedQuotationItemResponse.getId()) {
						return cart;
					}
				}
			}
		}
		return null;
	}

	public Cart getCartFromSelectedQuotation() {
		if (this.selectedQuotationItem != null) {
			for (Cart cart : carts) {
				if (cart.getId() == this.selectedQuotationItem.getCartId()) {
					return cart;
				}
			}
		}
		return null;
	}

	public Cart getCartFromQuotationItem(QuotationItem qi) {
		for (Cart cart : carts) {
			if (cart.getId() == qi.getCartId()) {
				return cart;
			}
		}

		return null;
	}

	public void updateNewPrice(ProductPrice productPriceP, Product product, boolean incomplete) {
		double price = productPriceP.getCostPrice();
		if (productPriceP.isVatIncluded()) {
			productPriceP.setCostPriceWv(price);
			productPriceP.setCostPrice(price / 1.05);
		} else {
			productPriceP.setCostPriceWv(price * 1.05);
		}
		productPriceP.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
		if (incomplete) {
			productPriceP.setMakeId(this.getCartFromSelectedQuotationItemResponse().getMakeId());
		} else {
			productPriceP.setMakeId(this.getCartFromSelectedQuotation().getMakeId());
		}

		productPriceP.setProductId(product.getId());
		Response r = reqs.putSecuredRequest(AppConstants.PUT_PRODUCT_PRICE, productPriceP);
		if (r.getStatus() == 200) {
			ProductPrice serverPP = r.readEntity(ProductPrice.class);
			for (ProductPrice pp : product.getPriceList()) {
				if (pp.getVendorId() == serverPP.getVendorId()) {
					product.getPriceList().remove(pp);
					break;
				}
			}
			product.getPriceList().add(serverPP);
			this.newPrice = false;
			productPriceP = new ProductPrice();

			Helper.addInfoMessage("New price added");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void saveResponse(QuotationItem qi) {
		if (qi.isNotAvailable()) {
			QuotationItemResponse qir = new QuotationItemResponse();
			qir.setCartId(qi.getCartId());
			qir.setCreatedBy(loginBean.getLoggedUserId());
			qir.setDefaultPercentage(null);
			qir.setDesc(null);
			qir.setProduct(null);
			qir.setProductId(null);
			qir.setProductPrice(null);
			qir.setQuantity(qi.getQuantity());
			qir.setQuotationId(qi.getQuotationId());
			qir.setQuotationItemId(qi.getId());
			qir.setStatus('N');
			qi.getQuotationItemResponses().add(qir);
			qi.setStatus('N');
		}
		boolean ok = true;
		for (QuotationItemResponse qir : qi.getQuotationItemResponses()) {
			if (qir.getId() > 0) {
				if (qir.getStatus() == 'I') {
					ok = false;
					break;
				}
			}
		}
		if (ok) {
			Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_ITEM_RESPONSE, qi);
			if (r.getStatus() == 201) {
				Helper.redirect("quoting?dummy=c"+Helper.getRandomSaltString() + "#c" + qi.getCartId());
			} else {
				Helper.addErrorMessage("An error occured " + r.getStatus());
			}
		} else {
			Helper.addErrorMessage("Response is not complete");
		}

	}

	public void findProduct() {
		Cart cart = this.getCartFromSelectedQuotation();
		String desc = this.selectedQuotationItem.getItemDesc();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("number", this.partNumber);
		map.put("name", desc);
		map.put("makeId", cart.getMakeId());
		Response r = reqs.postSecuredRequest(AppConstants.FIND_PRODUCT_CREATE_IF_NOT_AVAILABLE, map);
		if (r.getStatus() == 200) {
			Product p = r.readEntity(Product.class);
			if (!productAdded(p, cart)) {
				this.foundProduct = p;
			} else {
				Helper.addErrorMessage("This product is already added! Please change quantity instead");
			}
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	private boolean productAdded(Product p, Cart cart) {
		for (QuotationItem qitem : cart.getAllQuotationItems()) {
			if (qitem.getQuotationItemResponses() != null) {
				for (QuotationItemResponse qir : qitem.getQuotationItemResponses()) {
					if (qir.getProduct() != null) {
						if (qir.getProduct().getId() == p.getId()) {
							if (qitem.getStatus() != 'N') {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public void initQoutationItem(Cart cart) {
		QuotationItem quotationItem = new QuotationItem();
		quotationItem.setCartId(cart.getId());
		quotationItem.setStatus('W');
		quotationItem.setQuantity(1);
		quotationItem.setEdit(true);
		quotationItem.setQuotationId(cart.getQuotations().get(0).getId());
		cart.getQuotations().get(0).getQuotationItems().add(quotationItem);
	}

	public void createNewQuotationItem() {
		this.newQuotationItem.setCreatedBy(loginBean.getLoggedUserId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_QUOTATION_ITEM, newQuotationItem);
		if (r.getStatus() == 200) {
			Quotation q = r.readEntity(Quotation.class);
			for (Cart cart : carts) {
				if (cart.getId() == q.getCartId()) {
					Helper.redirect("quoting?dummy=c"+Helper.getRandomSaltString() + "#c" + cart.getId());
					break;
				}
			}
		}
	}

	public void submitNewReview(Cart cart) {
		CartReview cr = cart.getNewReview();
		cr.setCartId(cart.getId());
		cr.setStage(2);
		cr.setActionValue('B');// general
		cr.setReviewerId(loginBean.getLoggedUserId());
		cr.setStatus(cr.getStatusFromActionValue());
		Response r = reqs.postSecuredRequest(AppConstants.POST_FOLLOW_UP_REVIEW, cr);
		if (r.getStatus() == 200) {
			Helper.redirect("quoting?dummy=c"+Helper.getRandomSaltString() + "#c" + cart.getId());
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void chooseNewQuotationItem(Cart cart) {
		this.newQuotationItem = new QuotationItem();
		this.newQuotationItem.setCartId(cart.getId());
	}

	public void chooseQuotationItemResponse(QuotationItemResponse qir) {
		this.selectedQuotationItemResponse = qir;
	}

	public void saveEdit(QuotationItem qi) {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_QUOTATION_ITEM, qi);
		if (r.getStatus() == 201) {
			Helper.addInfoMessage("Item updated");
			qi.setEdit(false);
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void chooseQuotationItem(QuotationItem qi) {
		this.selectedQuotationItem = qi;
		this.foundProduct = new Product();
		this.partNumber = "";
		this.newPrice = false;
		this.productPrice = new ProductPrice();
	}

	public void changeOccured() {
		try {
			Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			String data = map.get("param");
			if (data != null) {
				String[] messages = data.split(",");
				String function = messages[0];
				String value = messages[1];
				switch (function) {
				case "unassigned cart":
					loadUnassigned(Long.parseLong(value));
					break;
				case "newly assigned":
					loadNewlyAssignedCart(Long.parseLong(value));
					break;
				case "update cart":
					loadUpdatedCart(Long.parseLong(value));
					break;
				default:
					System.out.println("default");

				}
			}
		} catch (Exception ex) {

		}

	}

	private void loadUnassigned(long cartId) {
		for (Cart cart : this.carts) {
			if (cartId == cart.getId()) {
				Helper.addErrorMessage("Cart " + cart.getId() + " unassigned");
				carts.remove(cart);
				break;
			}
		}
	}

	private void loadNewlyAssignedCart(long cartId) {
		Response r = reqs.getSecuredRequest(AppConstants.getAssignedCart(loginBean.getLoggedUserId(), cartId));
		if (r.getStatus() == 200) {
			Cart reloaded = r.readEntity(Cart.class);
			initQuotationItemProducts(reloaded);
			initCustomer(reloaded);
			boolean found = false;
			for (Cart cart : carts) {
				if (cart.getId() == cartId) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				this.carts.add(reloaded);
				Helper.addInfoMessage("Cart Assigned " + reloaded.getId());
			} else {
				// if found do this
				for (int i = 0; i < carts.size(); i++) {
					if (carts.get(i).getId() == reloaded.getId()) {
						carts.set(i, reloaded);
						Helper.addInfoMessage("Cart Assigned " + carts.get(i).getId());
						break;
					}
				}
			}

		}
	}

	private void loadUpdatedCart(long cartId) {
		Response r = reqs.getSecuredRequest(AppConstants.getAssignedCart(loginBean.getLoggedUserId(), cartId));
		if (r.getStatus() == 200) {
			Cart reloaded = r.readEntity(Cart.class);
			initQuotationItemProducts(reloaded);
			for (int i = 0; i < carts.size(); i++) {
				if (carts.get(i).getId() == reloaded.getId()) {
					carts.set(i, reloaded);
					Helper.addWarMessage("Cart updated " + carts.get(i).getId());
					break;
				}
			}
		}
	}

	public QuotationItem getSelectedQuotationItem() {
		return selectedQuotationItem;
	}

	public void setSelectedQuotationItem(QuotationItem selectedQuotationItem) {
		this.selectedQuotationItem = selectedQuotationItem;
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public Product getFoundProduct() {
		return foundProduct;
	}

	public void setFoundProduct(Product foundProduct) {
		this.foundProduct = foundProduct;
	}

	public ProductPrice getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(ProductPrice productPrice) {
		this.productPrice = productPrice;
	}

	public boolean isNewPrice() {
		return newPrice;
	}

	public void setNewPrice(boolean newPrice) {
		this.newPrice = newPrice;
	}

	public QuotationItemResponse getSelectedQuotationItemResponse() {
		return selectedQuotationItemResponse;
	}

	public void setSelectedQuotationItemResponse(QuotationItemResponse selectedQuotationItemResponse) {
		this.selectedQuotationItemResponse = selectedQuotationItemResponse;
	}

	public ProductPrice getIncompleteProductPrice() {
		return incompleteProductPrice;
	}

	public void setIncompleteProductPrice(ProductPrice incompleteProductPrice) {
		this.incompleteProductPrice = incompleteProductPrice;
	}

	public QuotationItem getNewQuotationItem() {
		return newQuotationItem;
	}

	public void setNewQuotationItem(QuotationItem newQuotationItem) {
		this.newQuotationItem = newQuotationItem;
	}

	public Long getPositiveScore() {
		return positiveScore;
	}

	public void setPositiveScore(Long positiveScore) {
		this.positiveScore = positiveScore;
	}

	public Long getNegativeScore() {
		return negativeScore;
	}

	public void setNegativeScore(Long negativeScore) {
		this.negativeScore = negativeScore;
	}

	public String getQuotingWSLink() {
		return WebsocketLinks.getQuotingLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
	}

}
