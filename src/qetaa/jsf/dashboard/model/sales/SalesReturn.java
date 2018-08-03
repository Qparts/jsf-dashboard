package qetaa.jsf.dashboard.model.sales;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.cart.Cart;

public class SalesReturn implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private Sales sales;
	private Date returnDate;
	private char method;
	private int returnBy;
	private long cartId;
	private double shipmentFees;
	private String courrierName;
	private String shipmentReference;
	private double vatPercentage;
	private Double promotionDiscount;
	private Integer promotionId;
	private Integer bankId;
	private Double returnedDeliveryFees; 
	private List<SalesReturnProduct> salesReturnProducts;
	private List<SalesPayment> salesPayments;
	
	@JsonIgnore
	public double getTotalPartsSalesNewQuantity() {
		double total = 0;
		if(salesReturnProducts == null) {
			return 0;
		}
		for(SalesReturnProduct srp : salesReturnProducts) {
			total = total + srp.getUnitSales() * srp.getNewQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getTotalPartsSales() {
		double total = 0;
		if(salesReturnProducts == null) {
			return 0;
		}
		for(SalesReturnProduct srp : salesReturnProducts) {
			total = total + srp.getUnitSales() * srp.getQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getTotalVat() {
		double total = 0;
		if(salesReturnProducts == null) {
			return 0;
		}
		for(SalesReturnProduct srp : salesReturnProducts) {
			total = total + srp.getUnitSales() * srp.getQuantity();
		}
		if(this.shipmentFees < 0) {
			total = total + (this.shipmentFees * -1);
		}
		return total * vatPercentage;
	}
	
	@JsonIgnore
	public double getReturnedDeliveryFeesWithVat() {
		return this.returnedDeliveryFees * (1 + this.getVatPercentage());
	}
	
	
	@JsonIgnore
	public double getTotalPromotionDiscountDeductionNewQuantity(Cart cart) {
		if(cart.getPromotionCode() == null) {
			return 0;
		}
		if(!cart.getPromoCodeObject().isDiscountPromo()) {
			return 0;
		}
		return (getTotalPartsSalesNewQuantity() + this.getReturnedDeliveryFees())* cart.getPromoCodeObject().getDiscountPercentage();
	}
	
	@JsonIgnore
	public double getTotalPromotionDiscountDeduction(Cart cart) {
		if(cart.getPromotionCode() == null) {
			return 0;
		}
		if(!cart.getPromoCodeObject().isDiscountPromo()) {
			return 0;
		}
		return getTotalPartsSalesNewQuantity() * cart.getPromoCodeObject().getDiscountPercentage();
	}
	
	@JsonIgnore
	public double getTotalPartsSalesWvNewQuantity() {
		double total = 0;
		if(salesReturnProducts == null) {
			return 0;
		}
		for(SalesReturnProduct srp : salesReturnProducts) {
			total = total + srp.getUnitSalesWv() * srp.getNewQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getNetSalesReturn() {
		double total = 0;
		if(salesReturnProducts == null) {
			return 0;
		}
		total = total + (getTotalPartsSalesWv() - this.getTotalDeductionFees() - this.getPromotionDiscount() + this.getReturnedDeliveryFeesWvSafe());		
		return total;
	}
	
	@JsonIgnore
	public double getTotalPartsSalesWv() {
		double total = 0;
		if(salesReturnProducts == null) {
			return 0;
		}
		for(SalesReturnProduct srp : salesReturnProducts) {
			total = total + srp.getUnitSalesWv() * srp.getQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getTotalPartsCostWv() {
		double total = 0;
		if(salesReturnProducts == null) {
			return 0;
		}
		for(SalesReturnProduct srp : salesReturnProducts) {
			total = total + srp.getUnitCostWv() * srp.getQuantity();
		}
		return total;
	}
	
	@JsonIgnore
	public double getReturnProfit() {
		return getTotalPartsCostWv() - this.getNetSalesReturn() - shipmentFees;
	}
	
	@JsonIgnore
	public double getTotalDeductionFees() {
		double total = 0;
		if(salesReturnProducts == null) {
			return 0;
		}
		for(SalesReturnProduct srp : salesReturnProducts) {
			total = total + srp.getReturnDeductionFees();
		}
		return total;
	}
	
	public List<SalesReturnProduct> getSalesReturnProducts() {
		return salesReturnProducts;
	}
	public void setSalesReturnProducts(List<SalesReturnProduct> salesReturnProducts) {
		this.salesReturnProducts = salesReturnProducts;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Sales getSales() {
		return sales;
	}
	public void setSales(Sales sales) {
		this.sales = sales;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	@JsonIgnore
	public String getPaymentMethodEn() {
		switch (getMethod()) {
		case 'R':
			return "Wire Transfer";
		case 'C':
			return "Credit Card";
		case 'S':
			return "Sadad";
		case 'M':
			return "Mada";
		case 'D':
			return "Cash";
		default:
			return "Wire Transfer";
		}
	}
	
	@JsonIgnore
	public String getPaymentMethodAr() {
		switch (getMethod()) {
		case 'R':
			return "تحويل بنكي";
		case 'C':
			return "بطاقة إئتمانية";
		case 'S':
			return "سداد";
		case 'M':
			return "مدى";
		case 'D':
			return "نقدية";
		default:
			return "تحويل بنكي";
		}
	}
	
	@JsonIgnore
	public double getReturnedDeliveryFeesSafe() {
		return (this.returnedDeliveryFees == null ? 0 : this.returnedDeliveryFees);
	}
	
	@JsonIgnore
	public double getReturnedDeliveryFeesWvSafe() {
		return (this.getReturnedDeliveryFees() == null ? 0 : this.getReturnedDeliveryFeesWithVat());
	}
	
	public char getMethod() {
		return method;
	}
	public void setMethod(char method) {
		this.method = method;
	}
	public int getReturnBy() {
		return returnBy;
	}
	public void setReturnBy(int returnBy) {
		this.returnBy = returnBy;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public double getShipmentFees() {
		return shipmentFees;
	}
	public void setShipmentFees(double shipmentFees) {
		this.shipmentFees = shipmentFees;
	}
	public String getCourrierName() {
		return courrierName;
	}
	public void setCourrierName(String courrierName) {
		this.courrierName = courrierName;
	}
	public String getShipmentReference() {
		return shipmentReference;
	}
	public void setShipmentReference(String shipmentReference) {
		this.shipmentReference = shipmentReference;
	}
	public double getVatPercentage() {
		return vatPercentage;
	}
	public void setVatPercentage(double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}
	public Double getPromotionDiscount() {
		return promotionDiscount;
	}
	public void setPromotionDiscount(Double promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}
	public Integer getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public List<SalesPayment> getSalesPayments() {
		return salesPayments;
	}

	public void setSalesPayments(List<SalesPayment> salesPayments) {
		this.salesPayments = salesPayments;
	}

	public Double getReturnedDeliveryFees() {
		return returnedDeliveryFees;
	}

	public void setReturnedDeliveryFees(Double returnedDeliveryFees) {
		this.returnedDeliveryFees = returnedDeliveryFees;
	}
	
	
	
	

}
