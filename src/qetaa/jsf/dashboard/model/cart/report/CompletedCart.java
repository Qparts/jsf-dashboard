package qetaa.jsf.dashboard.model.cart.report;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.PartsOrder;
import qetaa.jsf.dashboard.model.payment.PartsPayment;

public class CompletedCart implements Serializable {

	private static final long serialVersionUID = 1L;

	private Cart cart;
	private PartsOrder partsOrder;
	private PartsPayment payment;
	private double partsReturnTotal;

	@JsonIgnore
	public Double getPartsSales() {
		return partsOrder.getSalesAmount() - cart.getDeliveryFees()
				- (cart.getDeliveryFees() * cart.getVatPercentage());
	}

	@JsonIgnore
	public Double getDeliveryFees() {
		return cart.getDeliveryFees() + (cart.getDeliveryFees() * cart.getVatPercentage());
	}

	@JsonIgnore
	public Double getPartsCost() {
		return partsOrder.getCostAmount();
	}

	@JsonIgnore
	public Double getShipmentCost() {
		return partsOrder.getShipmentCost() == null ? 0 : partsOrder.getShipmentCost();
	}

	@JsonIgnore
	public Double getCreditFees() {
		return payment.getCreditFees() == null ? 0 : (payment.getCreditFees() / 100);
	}

	@JsonIgnore
	public Double getPromotionDiscount() {
		if (cart.getPromoCodeObject() != null && cart.getPromoCodeObject().isDiscountPromo()) {
			return cart.getPromoCodeObject().getDiscountPercentage() * (cart.getDeliveryFees() + this.getPartsOrder().getTotalPartsPrice());
		}
		else return 0D;
	}

	@JsonIgnore
	public Double getProfit() {
		return getPartsSales() + getDeliveryFees() - getPartsCost() - getShipmentCost() - getPartsReturnTotal()
				- getCreditFees() - getPromotionDiscount();
	}

	public double getPartsReturnTotal() {
		return partsReturnTotal;
	}

	public void setPartsReturnTotal(double partsReturnTotal) {
		this.partsReturnTotal = partsReturnTotal;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public PartsOrder getPartsOrder() {
		return partsOrder;
	}

	public void setPartsOrder(PartsOrder partsOrder) {
		this.partsOrder = partsOrder;
	}

	public PartsPayment getPayment() {
		return payment;
	}

	public void setPayment(PartsPayment payment) {
		this.payment = payment;
	}

}
