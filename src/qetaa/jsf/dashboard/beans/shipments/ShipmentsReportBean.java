package qetaa.jsf.dashboard.beans.shipments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.shipment.Shipment;

@Named
@ViewScoped
public class ShipmentsReportBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int year;
	private int month;
	private int courierId;
	private long cartId;
	private List<Shipment> shipments;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct()
	private void init() {
		shipments = new ArrayList<>();
		initCurrentDate();
	}
	
	private void initCurrentDate() {
		Calendar c = Calendar.getInstance();
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
	}
	
	public void generateReport() {
		Response r = reqs.getSecuredRequest(AppConstants.getShipmentsReport(year, month, courierId, cartId));
		if(r.getStatus() == 200) {
			this.shipments = r.readEntity(new GenericType<List<Shipment>>() {});
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getCourierId() {
		return courierId;
	}

	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}

	public List<Shipment> getShipments() {
		return shipments;
	}

	public void setShipments(List<Shipment> shipments) {
		this.shipments = shipments;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	
	
	
	
	
}
