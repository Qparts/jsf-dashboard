package qetaa.jsf.dashboard.beans.quotations;

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
import qetaa.jsf.dashboard.beans.master.VendorBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.QuotationItem;
import qetaa.jsf.dashboard.model.product.Product;

@Named
@ViewScoped
public class QuotingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cart> carts;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@Inject
	private VendorBean vendorBean;

	@PostConstruct
	private void init() {
		initCarts();
	}

	private void initCarts() {
		Response r = reqs
				.getSecuredRequest(AppConstants.getWaitingQuotation(loginBean.getUserHolder().getUser().getId()));
		if (r.getStatus() == 200) {
			carts = r.readEntity(new GenericType<List<Cart>>() {
			});
		} else {
			carts = new ArrayList<>();
		}
	}

	public void findProduct(QuotationItem qitem, int makeId) {
		Response r = reqs.getSecuredRequest(AppConstants.findProduct(qitem.getItemDescAr(), makeId));
		if (r.getStatus() == 200) {
			qitem.setProduct(r.readEntity(Product.class));
		} else {
			Helper.addErrorMessage("Price was not found for " + qitem.getItemDescAr());
		}
	}

	public void findProducts(Cart cart) {
		for (QuotationItem qi : cart.getWaitingQuotationItems()) {
			if (qi.getStatus() == 'N') {
				qi.setStatus('W');
			}
			Response r = reqs.getSecuredRequest(AppConstants.findProduct(qi.getItemDescAr(), cart.getMakeId()));
			if (r.getStatus() == 200) {
				qi.setProduct(r.readEntity(Product.class));
			} else {
				// Helper.addErrorMessage("Price was not found for " + qi.getItemDescAr());
			}
		}
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

	private void createNewQuotationItems(Cart cart) {
		for (QuotationItem qi : cart.getQuotations().get(0).getQuotationItems()) {
			if (qi.getId() == 0) {
				qi.setCreated(new Date());
				qi.setCreatedBy(loginBean.getUserHolder().getUser().getId());
				Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_ITEM, qi);
				if (r.getStatus() == 201) {
					qi.setId(r.readEntity(Long.class));
				}
			}
		}
	}

	public void submitQuote(Cart cart) {
		createNewQuotationItems(cart);
		List<Map<String, Object>> list = new ArrayList<>();
		for (QuotationItem qi : cart.getWaitingQuotationItems()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cartId", qi.getCartId());
			map.put("cost", qi.getProduct().getSelectedPrice().getCostPrice());
			map.put("costWv", qi.getProduct().getSelectedPrice().getCostPriceWv());
			map.put("createdBy", loginBean.getUserHolder().getUser().getId());
			map.put("desc", qi.getItemDesc());
			map.put("quantity", qi.getQuantity());
			map.put("quotationId", qi.getQuotationId());
			map.put("quotationItemId", qi.getId());
			if(qi.getProduct() != null) {
				map.put("productId", qi.getProduct().getId());//could be null
				if(qi.getProduct().getSelectedPrice() != null) {
					map.put("vendorId", qi.getProduct().getSelectedPrice().getVendorId());//could be null
					map.put("cost", qi.getProduct().getSelectedPrice().getCostPrice());//could be null
					map.put("percentage", vendorBean.getPercentage(qi.getProduct().getSelectedPrice().getVendorId(), cart.getMakeId()));//could be null
				}
				else {
					map.put("vendorId", null);//could be null
					map.put("cost", null);//could be null
					map.put("percentage", null);//could be null
				}
			}
			else {
				map.put("productId", null);//could be null
				map.put("vendorId", null);//could be null
				map.put("cost", null);//could be null
				map.put("percentage", null);//could be null
			}
			list.add(map);
		}
		
		Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_ITEM_RESPONSE, list);
		if(r.getStatus() == 201) {
			this.carts.remove(cart);
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

}
