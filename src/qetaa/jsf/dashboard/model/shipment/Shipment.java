package qetaa.jsf.dashboard.model.shipment;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Shipment implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long customerId;
	private Date created;
	private int createdBy;
	private int courierId;
	private String trackReference;
	private long addressId;
	private boolean trackable;
	private char status;
	private double shipmentFees;
	private Character bound;
	private List<ShipmentItem> shipmentItems;
	
	
	@JsonIgnore
	public Set<Long> getCartIds(){
		Set<Long> set = new HashSet<Long>();
		if(this.shipmentItems != null) {
			for(ShipmentItem sh : this.shipmentItems) {
				set.add(sh.getWalletItem().getCartId());
			}
		}
		return set;
	}
	
	public List<ShipmentItem> getShipmentItems() {
		return shipmentItems;
	}
	public void setShipmentItems(List<ShipmentItem> shipmentItems) {
		this.shipmentItems = shipmentItems;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public int getCourierId() {
		return courierId;
	}
	public void setCourierId(int courrierId) {
		this.courierId = courrierId;
	}
	public String getTrackReference() {
		return trackReference;
	}
	public void setTrackReference(String trackReference) {
		this.trackReference = trackReference;
	}
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	public boolean isTrackable() {
		return trackable;
	}
	public void setTrackable(boolean trackable) {
		this.trackable = trackable;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public double getShipmentFees() {
		return shipmentFees;
	}
	public void setShipmentFees(double shipmentFees) {
		this.shipmentFees = shipmentFees;
	}

	public Character getBound() {
		return bound;
	}

	public void setBound(Character bound) {
		this.bound = bound;
	}
	
	
	
}
