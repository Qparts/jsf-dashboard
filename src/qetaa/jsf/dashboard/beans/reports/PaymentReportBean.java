package qetaa.jsf.dashboard.beans.reports;

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
import qetaa.jsf.dashboard.model.cart.report.CompletedCart;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

@Named
@ViewScoped
public class PaymentReportBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int year;
	private int month;
	private String paymentType;
	private List<CompletedCart> payments;
	private List<PromotionCode> discountPromos;

	@Inject
	private Requester reqs;

	@PostConstruct()
	private void init() {
		payments = new ArrayList<>();
		initCurrentDate();
		discountPromos = new ArrayList<>();
		initAllDiscountPromoCodes();
	}
	
	private void initAllDiscountPromoCodes() {
		discountPromos = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_DISCOUNT_PROMOTION_CODES);
		if(r.getStatus() == 200) {
			this.discountPromos = r.readEntity(new GenericType<List<PromotionCode>>() {});
		}
	}
	
	private void initCurrentDate() {
		Calendar c = Calendar.getInstance();
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
	}
	
	public void generateReport() {
		Response r = reqs.getSecuredRequest(AppConstants.getPaymentReport(year, month, paymentType));
		if(r.getStatus() == 200) {
			this.payments = r.readEntity(new GenericType<List<CompletedCart>>() {
			});
			for(CompletedCart cc : this.payments) {
				if(cc.getCart().getPromotionCode() != null) {
					for(PromotionCode pp : this.discountPromos) {
						if(cc.getCart().getPromotionCode().equals(pp.getId())) {
							cc.getCart().setPromoCodeObject(pp);
							break;
						}
					}
				}
			}
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public Double salesTotal() {
		Double total = 0D;
		for(CompletedCart pp : payments) {
			total = total + (pp.getPartsSales() + pp.getDeliveryFees());
		}
		return total;
	}
	
	public Double costTotal() {
		Double cost = 0D;
		for(CompletedCart pp : payments) {
			cost = cost + (pp.getPartsCost() + pp.getShipmentCost() + pp.getPartsReturnTotal() + pp.getCreditFees() + pp.getPromotionDiscount());
		}
		return cost;
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
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public List<CompletedCart> getPayments() {
		return payments;
	}

	public void setPayments(List<CompletedCart> payments) {
		this.payments = payments;
	}


}
