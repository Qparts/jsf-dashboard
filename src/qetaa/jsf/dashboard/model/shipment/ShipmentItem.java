package qetaa.jsf.dashboard.model.shipment;

import java.io.Serializable;
import java.util.Date;

import qetaa.jsf.dashboard.model.payment.WalletItem;

public class ShipmentItem implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long shipmentId;
	private int quantity;
	private long walletItemId;
	private WalletItem walletItem;
	private char status;
	private Integer collectedBy;
	private Date collected;
	private int shippedBy;
	private Date shipped;

	
	
	public WalletItem getWalletItem() {
		return walletItem;
	}

	public void setWalletItem(WalletItem walletItem) {
		this.walletItem = walletItem;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getWalletItemId() {
		return walletItemId;
	}

	public void setWalletItemId(long walletItemId) {
		this.walletItemId = walletItemId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getCollectedBy() {
		return collectedBy;
	}

	public void setCollectedBy(Integer collectedBy) {
		this.collectedBy = collectedBy;
	}

	public Date getCollected() {
		return collected;
	}

	public void setCollected(Date collected) {
		this.collected = collected;
	}

	public int getShippedBy() {
		return shippedBy;
	}

	public void setShippedBy(int shippedBy) {
		this.shippedBy = shippedBy;
	}

	public Date getShipped() {
		return shipped;
	}

	public void setShipped(Date shipped) {
		this.shipped = shipped;
	}	
	
}
