package qetaa.jsf.dashboard.model.sales;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.cart.Cart;

public class Sales implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private int makeId;
	private long customerId;
	private Date salesDate;
	private Date dueDate;
	private char transactionType;
	private char paymentStatus;
	private int createdBy;
	private double deliveryFees;
	private double vatPercentage;
	private double shipmentFees;
	private double promotionDiscount;
	private Integer promotionId;
	private String courrierName;
	private String shipmentReference;
	private List<SalesProduct> salesProducts;
	private List<SalesPayment> salesPayments;
	private List<SalesReturn> salesReturns;

	@JsonIgnore
	private Cart cart;

	
	@JsonIgnore
	public List<SalesPayment> getReturnPayments(){
		List<SalesPayment> pays = new ArrayList<>();
		if(this.salesReturns != null) {
			for(SalesReturn sr : this.salesReturns) {
				if(sr.getSalesPayments() != null) {
					pays.addAll(sr.getSalesPayments());
				}
			}
		}
		return pays;
	}
	
	@JsonIgnore
	public double getOutstanding() {
		return this.getTotalPartsWvAmount() - this.getPaid() - this.getPromotionDiscount() + this.getReturned();
	}
	
	@JsonIgnore
	public double getPaid() {
		double total = 0;
		if(this.salesPayments != null) {
			for(SalesPayment sp : this.salesPayments) {
				total += sp.getAmount();
			}
		}
		return total;
	}
	
	@JsonIgnore
	public double getReturned() {
		double total = 0;
		if(this.salesReturns != null) {
			for(SalesReturn sr : this.salesReturns) {
				if(sr.getSalesPayments() != null) {
					for(SalesPayment sp : sr.getSalesPayments()) {
						total += sp.getAmount();
					}
				}
			}
		}
		return total;
	}
	@JsonIgnore
	public double getTotalPartsWvAmount() {
		double total = 0;
		if (this.salesProducts != null) {
			for (SalesProduct sp : salesProducts) {
				total = total + sp.getUnitSalesWv() * sp.getQuantity();
			}
		}
		return total;
	}

	@JsonIgnore
	public List<SalesReturnProduct> getSalesReturnProducts() {
		List<SalesReturnProduct> srps = new ArrayList<>();
		for (SalesReturn sr : salesReturns) {
			srps.addAll(sr.getSalesReturnProducts());
		}
		return srps;
	}

	@JsonIgnore
	public double getTotalSalesReturnParts() {
		double total = 0;
		for (SalesReturn sr : salesReturns) {
			total = total + sr.getTotalPartsSales();
		}
		return total;
	}

	@JsonIgnore
	public double getTotalSalesReturnPromotionDeduction() {
		double total = 0;
		for (SalesReturn sr : salesReturns) {
			total = total + sr.getPromotionDiscount();
		}
		return total;
	}

	@JsonIgnore
	public double getTotalSalesReturnPartsWv() {
		double total = 0;
		for (SalesReturn sr : salesReturns) {
			total = total + sr.getTotalPartsSalesWv();
		}
		return total;
	}

	@JsonIgnore
	public double getTotalSalesReturnFees() {
		double total = 0;
		for (SalesReturn sr : salesReturns) {
			total = total + sr.getTotalDeductionFees();
		}
		return total;
	}

	@JsonIgnore
	public double getTotalPartsAmount() {
		double total = 0;
		if (this.salesProducts != null) {
			for (SalesProduct sp : salesProducts) {
				total = total + sp.getUnitSales() * sp.getQuantity();
			}
		}
		return total;
	}

	@JsonIgnore
	public double getTotalPartsCost() {
		double total = 0;
		if (this.salesProducts != null) {
			for (SalesProduct sp : salesProducts) {
				total = total + sp.getUnitCost() * sp.getQuantity();
			}
		}
		return total;
	}

	@JsonIgnore
	public double getTotalCreditFees() {
		double total = 0;
		
		if (this.salesPayments != null) {
			for (SalesPayment sp : salesPayments) {
				if (sp.getCreditFees() != null)
					total = total + sp.getCreditFees();
			}
		}
		return total;
	}

	@JsonIgnore
	public double getTotalPartsCostWv() {
		double total = 0;
		if (this.salesProducts != null) {
			for (SalesProduct sp : salesProducts) {
				total = total + sp.getUnitCostWv() * sp.getQuantity();
			}
		}
		return total;
	}

	@JsonIgnore
	public double getTotalDeliveryFees() {
		return deliveryFees + deliveryFees * this.vatPercentage;
	}

	@JsonIgnore
	public double getTotalSalesWv() {
		return this.getTotalPartsWvAmount() + this.getTotalDeliveryFees();
	}
	
	@JsonIgnore
	public double getNetTotalSales() {
		return this.getTotalPartsWvAmount() + this.getTotalDeliveryFees() - this.promotionDiscount;
	}

	@JsonIgnore
	public double getTotalSales() {
		return this.getTotalPartsAmount() + this.deliveryFees;
	}

	@JsonIgnore
	public double getTotalCostWv() {
		return this.getTotalPartsCostWv() + this.getShipmentFees() + this.getPromotionDiscount()
				+ this.getTotalCreditFees();
	}
	
	@JsonIgnore
	public double getProfit() {
		return getTotalSalesWv() - getTotalCostWv();
	}

	public List<SalesProduct> getSalesProducts() {
		return salesProducts;
	}

	public void setSalesProducts(List<SalesProduct> salesProducts) {
		this.salesProducts = salesProducts;
	}

	public List<SalesPayment> getSalesPayments() {
		return salesPayments;
	}

	public void setSalesPayments(List<SalesPayment> salesPayments) {
		this.salesPayments = salesPayments;
	}

	public double getDeliveryFees() {
		return deliveryFees;
	}

	public void setDeliveryFees(double deliveryFees) {
		this.deliveryFees = deliveryFees;
	}

	public double getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	public String getShipmentReference() {
		return shipmentReference;
	}

	public void setShipmentReference(String shipmentReference) {
		this.shipmentReference = shipmentReference;
	}

	public double getShipmentFees() {
		return shipmentFees;
	}

	public void setShipmentFees(double shipmentFees) {
		this.shipmentFees = shipmentFees;
	}

	public double getPromotionDiscount() {
		return promotionDiscount;
	}

	public void setPromotionDiscount(double promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public String getCourrierName() {
		return courrierName;
	}

	public void setCourrierName(String courrierName) {
		this.courrierName = courrierName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@JsonIgnore
	public String getPaymentMethodEn() {
		if (this.salesPayments.isEmpty()) {
			return "";
		}
		switch (this.getSalesPayments().get(0).getMethod()) {
		case 'R':
			return "Wire Transfer";
		case 'C':
			return "Credit Card";
		case 'S':
			return "Sadad";
		case 'M':
			return "Mada";
		case 'D':
			return "Cash On Delivery";
		default:
			return "Wire Transfer";
		}
	}
	
	@JsonIgnore
	public String getPaymentMethodAr() {
		if (this.salesPayments.isEmpty()) {
			return "";
		}
		switch (this.getSalesPayments().get(0).getMethod()) {
		case 'R':
			return "تحويل بنكي";
		case 'C':
			return "بطاقة إئتمانية";
		case 'S':
			return "سداد";
		case 'M':
			return "مدى";
		case 'D':
			return "دفع عند الإستلام";
		default:
			return "تحويل بنكي";
		}
	}

	@JsonIgnore
	public String getTransactionTypeEn() {
		return (transactionType == 'C' ? "Cash" : "Credit");
	}

	@JsonIgnore
	public String getTransactionTypeAr() {
		return (transactionType == 'C' ? "نقدية" : "آجل");
	}

	public char getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(char transactionType) {
		this.transactionType = transactionType;
	}

	public char getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(char paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public List<SalesReturn> getSalesReturns() {
		return salesReturns;
	}

	public void setSalesReturns(List<SalesReturn> salesReturns) {
		this.salesReturns = salesReturns;
	}

	public int getMakeId() {
		return makeId;
	}

	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}

}
