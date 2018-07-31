package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.product.ProductStock;


public class PartsOrderItemApproved implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private long partsOrderId;
	private long partsItemId;
	private long quotationItemApprovedId;
	private int approvedQuantity;
	private Date collected;
	private Date received;
	private Date prepared;
	private Date shipped;
	private Integer collectedBy;
	private Integer receivedBy;
	private Integer shippedBy;
	private Integer preparedBy;
	private String shipmentId;
	private char status;//W = waiting, V=ready at the vendor, C = collected, R = received, S = shipped 
	private double salesPrice;
	private double costPrice;
	private String itemNumber;
	private int vendorId;
	private String itemDesc;
	private Integer actualVendorId;
	private String invReference;
	private Long productId;
	@JsonIgnore
	private Product product;
	
	@JsonIgnore
	public int getQuantityArray() []{
		int[] quantityArray = new int[approvedQuantity];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
		}
		return quantityArray;
	}
	
	@JsonIgnore
	public int getStockQuantity() {
		if(product == null) {
			return 0;
		}
		if(product.getStockList().isEmpty()) {
			return 0;
		}
		int q = 0;
		for(ProductStock stock : product.getStockList()) {
			q = q + stock.getQuantity();
		}
		return q;
	}

	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}





	public Long getProductId() {
		return productId;
	}



	public void setProductId(Long productId) {
		this.productId = productId;
	}



	public Integer getActualVendorId() {
		return actualVendorId;
	}
	public void setActualVendorId(Integer actualVendorId) {
		this.actualVendorId = actualVendorId;
	}
	public String getInvReference() {
		return invReference;
	}
	public void setInvReference(String invReference) {
		this.invReference = invReference;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	public Date getPrepared() {
		return prepared;
	}
	public void setPrepared(Date prepared) {
		this.prepared = prepared;
	}
	public Integer getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(Integer preparedBy) {
		this.preparedBy = preparedBy;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
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
	public long getPartsOrderId() {
		return partsOrderId;
	}
	public void setPartsOrderId(long partsOrderId) {
		this.partsOrderId = partsOrderId;
	}
	public long getPartsItemId() {
		return partsItemId;
	}
	public void setPartsItemId(long partsItemId) {
		this.partsItemId = partsItemId;
	}
	public long getQuotationItemApprovedId() {
		return quotationItemApprovedId;
	}
	public void setQuotationItemApprovedId(long quotationItemApprovedId) {
		this.quotationItemApprovedId = quotationItemApprovedId;
	}
	public int getApprovedQuantity() {
		return approvedQuantity;
	}
	public void setApprovedQuantity(int approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}
	public Date getCollected() {
		return collected;
	}
	public void setCollected(Date collected) {
		this.collected = collected;
	}
	public Date getReceived() {
		return received;
	}
	public void setReceived(Date received) {
		this.received = received;
	}
	public Date getShipped() {
		return shipped;
	}
	public void setShipped(Date shipped) {
		this.shipped = shipped;
	}
	public Integer getCollectedBy() {
		return collectedBy;
	}
	public void setCollectedBy(Integer collectedBy) {
		this.collectedBy = collectedBy;
	}
	public Integer getReceivedBy() {
		return receivedBy;
	}
	public void setReceivedBy(Integer receivedBy) {
		this.receivedBy = receivedBy;
	}
	public Integer getShippedBy() {
		return shippedBy;
	}
	public void setShippedBy(Integer shippedBy) {
		this.shippedBy = shippedBy;
	}
	public String getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}
	
	

	
}
