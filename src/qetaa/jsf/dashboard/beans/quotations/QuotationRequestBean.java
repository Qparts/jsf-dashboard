package qetaa.jsf.dashboard.beans.quotations;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.Cart;

@Named
@ViewScoped
public class QuotationRequestBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Cart cart;
	private int[] quantityArray;
	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("cart-id");
			initCart(s);
			initquantityArray();

		} catch (Exception ex) {
		}
	}

	private void initCart(String param) throws Exception {
		if (param == null) {
			throw new Exception();
		}
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getCart(id));
		if (r.getStatus() == 200) {
			cart = r.readEntity(Cart.class);
			if (!(cart.getStatus() == 'W' || cart.getStatus() == 'N' || cart.getStatus() == 'Q'
					|| cart.getStatus() == 'A' || cart.getStatus() == 'R')) {
				throw new Exception();
			}
		} else
			throw new Exception();
	}

	private void initquantityArray() {
		quantityArray = new int[20];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
		}
	}

}
