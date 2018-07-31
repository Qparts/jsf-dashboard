package qetaa.jsf.dashboard.beans.purchase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.sales.SalesReturn;

@Named
@ViewScoped
public class PurchaseReturnSearchBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<SalesReturn> salesReturns;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		salesReturns = new ArrayList<>();
		initReturnPurchases();
		initVariables();
	}
	
	private void initVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[salesReturns.size()];
		int index = 0;
		try {
			for(SalesReturn sr : salesReturns) {
				threads[index] = ThreadRunner.initSalesCart(sr.getSales(), header);
				threads[index].start();
				threads[index].join();
				index++;
			}
		}catch(Exception ex) {
			
		}
	}

	private void initReturnPurchases() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_SALES_RETURN_PURCHASE_NOT_RETURNED);
		if(r.getStatus() == 200) {
			this.salesReturns = r.readEntity(new GenericType<List<SalesReturn>>() {});
		}
	}

	public List<SalesReturn> getSalesReturns() {
		return salesReturns;
	}

	public void setSalesReturns(List<SalesReturn> salesReturns) {
		this.salesReturns = salesReturns;
	}
	
	
	
	

}
