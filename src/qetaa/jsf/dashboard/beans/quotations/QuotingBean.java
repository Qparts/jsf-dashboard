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
				 Helper.addErrorMessage("Price was not found for " + qi.getItemDescAr());
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
		System.out.println("received");
		createNewQuotationItems(cart);
		System.out.println("ok");
		List<Map<String, Object>> list = new ArrayList<>();
		System.out.println("map");
		for (QuotationItem qi : cart.getWaitingQuotationItems()) {
			System.out.println("for");
			Map<String, Object> map = new HashMap<String, Object>();
			System.out.println(1);
			map.put("cartId", qi.getCartId());
			System.out.println(2);
			map.put("cost", qi.getProduct().getSelectedPrice().getCostPrice());
			System.out.println(3);
			map.put("costWv", qi.getProduct().getSelectedPrice().getCostPriceWv());
			System.out.println(4);
			map.put("createdBy", loginBean.getUserHolder().getUser().getId());
			System.out.println(5);
			map.put("desc", qi.getItemDesc());
			System.out.println(6);
			map.put("quantity", qi.getQuantity());
			System.out.println(7);
			map.put("quotationId", qi.getQuotationId());
			System.out.println(8);
			map.put("quotationItemId", qi.getId());
			System.out.println(9);
			if(qi.getProduct() != null) {
				System.out.println(10);
				map.put("productId", qi.getProduct().getId());//could be null
				System.out.println(12);
				if(qi.getProduct().getSelectedPrice() != null) {
					System.out.println(13);
					map.put("vendorId", qi.getProduct().getSelectedPrice().getVendorId());//could be null
					System.out.println(14);
					map.put("cost", qi.getProduct().getSelectedPrice().getCostPrice());//could be null
					System.out.println(15);
					map.put("percentage", vendorBean.getPercentage(qi.getProduct().getSelectedPrice().getVendorId(), cart.getMakeId()));//could be null
					System.out.println(16);
				}
				else {
					System.out.println(17);
					map.put("vendorId", null);//could be null
					System.out.println(18);
					map.put("cost", null);//could be null
					System.out.println(19);
					map.put("percentage", null);//could be null
					System.out.println(20);
				}
			}
			else {
				System.out.println(21);
				map.put("productId", null);//could be null
				System.out.println(22);
				map.put("vendorId", null);//could be null
				System.out.println(23);
				map.put("cost", null);//could be null
				System.out.println(24);
				map.put("percentage", null);//could be null
				System.out.println(25);
			}
			System.out.println(26);
			list.add(map);
		}
		System.out.println(27);
		System.out.println("submitting");
		Response r = reqs.postSecuredRequest(AppConstants.POST_QUOTATION_ITEM_RESPONSE, list);
		System.out.println(r.getStatus());
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
