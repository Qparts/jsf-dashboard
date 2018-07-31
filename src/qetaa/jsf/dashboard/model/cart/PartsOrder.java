package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.customer.CustomerAddress;


public class PartsOrder implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private Date created;
	private int appCode;
	private long addressId;
	private char status;//W = waiting, C = all items collected, R = ready for shipment, S = items shipped
	private long paymentId;
	private double salesAmount;
	private double costAmount;
	private String shipmentReference;
	private Double shipmentCost;
	private String courrierName;
	private Date shipped;
	private Integer shippedBy;
	private CustomerAddress address; 
	private List<PartsOrderItem> partsItems; 
	
	
	@JsonIgnore
	public double getTotalPartsPrice(){
		double total  = 0;
		for(PartsOrderItem poi : this.partsItems){
			total = total + (poi.getOrderedQuantity() * poi.getSalesPrice());
		}
		return total;
	}
	
	@JsonIgnore
	public void adjustFinalQuantity() {
		for(PartsOrderItem poi : this.getPartsItems()) {
			poi.setOrderedQuantity(poi.getNewQuantity());
		}
	}
	
	@JsonIgnore
	public double getNewTotalPartsPrice(){
		double total  = 0;
		for(PartsOrderItem poi : this.partsItems){
			total = total + (poi.getNewQuantity() * poi.getSalesPrice());
		}
		return total;
	}
	
	public String getShipmentReference() {
		return shipmentReference;
	}

	public Date getShipped() {
		return shipped;
	}

	public void setShipped(Date shipped) {
		this.shipped = shipped;
	}

	public Integer getShippedBy() {
		return shippedBy;
	}

	public void setShippedBy(Integer shippedBy) {
		this.shippedBy = shippedBy;
	}

	public void setShipmentCost(Double shipmentCost) {
		this.shipmentCost = shipmentCost;
	}

	public void setShipmentReference(String shipmentReference) {
		this.shipmentReference = shipmentReference;
	}


	public String getCourrierName() {
		return courrierName;
	}




	public void setCourrierName(String courrierName) {
		this.courrierName = courrierName;
	}




	public Double getShipmentCost() {
		return shipmentCost;
	}

	public List<PartsOrderItem> getPartsItems() {
		return partsItems;
	}
	public void setPartsItems(List<PartsOrderItem> partsItems) {
		this.partsItems = partsItems;
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
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getAppCode() {
		return appCode;
	}
	public void setAppCode(int appCode) {
		this.appCode = appCode;
	}
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}
	public double getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(double salesAmount) {
		this.salesAmount = salesAmount;
	}
	public double getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(double costAmount) {
		this.costAmount = costAmount;
	}
	
	
	

	public CustomerAddress getAddress() {
		return address;
	}

	public void setAddress(CustomerAddress address) {
		this.address = address;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (addressId ^ (addressId >>> 32));
		result = prime * result + appCode;
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		long temp;
		temp = Double.doubleToLongBits(costAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((partsItems == null) ? 0 : partsItems.hashCode());
		result = prime * result + (int) (paymentId ^ (paymentId >>> 32));
		temp = Double.doubleToLongBits(salesAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + status;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartsOrder other = (PartsOrder) obj;
		if (addressId != other.addressId)
			return false;
		if (appCode != other.appCode)
			return false;
		if (cartId != other.cartId)
			return false;
		if (Double.doubleToLongBits(costAmount) != Double.doubleToLongBits(other.costAmount))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id != other.id)
			return false;
		if (partsItems == null) {
			if (other.partsItems != null)
				return false;
		} else if (!partsItems.equals(other.partsItems))
			return false;
		if (paymentId != other.paymentId)
			return false;
		if (Double.doubleToLongBits(salesAmount) != Double.doubleToLongBits(other.salesAmount))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	
}
