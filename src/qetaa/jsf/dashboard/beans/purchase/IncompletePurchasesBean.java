package qetaa.jsf.dashboard.beans.purchase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.model.purchase.Purchase;

@Named
@RequestScoped
public class IncompletePurchasesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Purchase> purchases;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		purchases = new ArrayList<>();
		initIncompletePurchases();
	}
	
	
	private void initIncompletePurchases() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_INCOMPLETE_PURCHASES);
		if(r.getStatus() == 200) {
			this.purchases = r.readEntity(new GenericType<List<Purchase>> () {});
		}
	}


	public List<Purchase> getPurchases() {
		return purchases;
	}


	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}
	
	
	
}

