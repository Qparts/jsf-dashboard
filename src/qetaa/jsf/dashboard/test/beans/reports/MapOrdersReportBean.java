package qetaa.jsf.dashboard.test.beans.reports;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.model.location.OrdersSummary;

@Named
@ViewScoped
public class MapOrdersReportBean implements Serializable{

	private OrdersSummary orderSummary;

	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		orderSummary = new OrdersSummary();
		Response r = reqs.getSecuredRequest(AppConstants.GET_ORDERS_MAP_REPORT);
		if(r.getStatus() == 200) {
			orderSummary = r.readEntity(OrdersSummary.class);
		}
	}
	
	public OrdersSummary getOrderSummary() {
		return orderSummary;
	}

	public void setOrderSummary(OrdersSummary orderSummary) {
		this.orderSummary = orderSummary;
	}
	
	
	
}
