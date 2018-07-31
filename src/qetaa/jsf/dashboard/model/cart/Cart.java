package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.customer.CustomerAddress;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

public class Cart implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private long customerId;
	private char status;
	private String vin;
	private Date created;
	private Date submitted;
	private Integer createdBy;
	private Integer submitteBy;
	private Integer cityId;
	private Integer appCode;
	private Integer vehicleYear;
	private Integer makeId;
	private Integer deliveryFees;
	private List<CartItem> cartItems;
	private double vatPercentage;
	private boolean noVin;
	private boolean vinImage;
	private Integer promotionCode;
	private ModelYear modelYear;
	private Customer customer;
	private List<Quotation> quotations;
	
	
	
	@JsonIgnore
	private City city;
	@JsonIgnore
	private CustomerAddress address;
	@JsonIgnore
	private List<PartCollectionItem> collections;
	private List<ApprovedQuotationItem> approvedItems;
	@JsonIgnore
	private List<PartsOrderItemApproved> partsItemsApproved;
	@JsonIgnore
	private PartsOrder partsOrder;
	private List<CartReview> reviews;
	@JsonIgnore
	private PromotionCode promoCodeObject;

	@JsonIgnore
	public double getPartsItemsApprovedTotalSales() {
		if (null != partsItemsApproved) {
			double total = 0;
			for (PartsOrderItemApproved approved : partsItemsApproved) {
				total = total + approved.getSalesPrice() * approved.getApprovedQuantity();
			}
			return total;
		}
		return 0;
	}
	
	@JsonIgnore

	public boolean isPartsAvailableforPurchase() {
		boolean found = false;
		for(PartsOrderItemApproved approved : getPartsItemsApproved()) {
			if(approved.getStockQuantity() < approved.getApprovedQuantity()) {
				found = true;
				break;
			}
		}
		return found;
	}

	@JsonIgnore
	public double getPartsItemsApprovedTotalCost() {
		if (null != partsItemsApproved) {
			double total = 0;
			for (PartsOrderItemApproved approved : partsItemsApproved) {
				total = total + approved.getCostPrice() * approved.getApprovedQuantity();
			}
			return total;
		}
		return 0;
	}

	public List<PartsOrderItemApproved> getPartsItemsApproved() {
		return partsItemsApproved;
	}

	public void setPartsItemsApproved(List<PartsOrderItemApproved> partsItemsApproved) {
		this.partsItemsApproved = partsItemsApproved;
	}
	
	
	@JsonIgnore
	public List<QuotationItem> getWaitingQuotationItems(){
		List<QuotationItem> qis = new ArrayList<>();
		for(Quotation q : quotations) {
			for(QuotationItem qitem : q.getQuotationItems()) {
				if(qitem.getStatus() == 'W' || qitem.getStatus() == 'N') {
					qis.add(qitem);
				}
			}
		}
		return qis;
	}

	@JsonIgnore
	public double totalSales() {
		double total = 0;
		if (approvedItems != null) {
			for (ApprovedQuotationItem item : approvedItems) {
				total += item.getTotalSales();
			}
		}
		return total;
	}


	@JsonIgnore
	public double totalSalesInlcudingVatAndDelivery() {
		double total = 0;
		if (approvedItems != null) {
			for (ApprovedQuotationItem item : approvedItems) {
				total += item.getTotalSales();
			}
		}
		total = total + this.deliveryFees;
		total = total + (total * this.vatPercentage);
		return total;
	}
	
	public List<CartReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<CartReview> reviews) {
		this.reviews = reviews;
	}

	public List<ApprovedQuotationItem> getApprovedItems() {
		return approvedItems;
	}

	public void setApprovedItems(List<ApprovedQuotationItem> approvedItems) {
		this.approvedItems = approvedItems;
	}

	public List<PartCollectionItem> getCollections() {
		return collections;
	}

	public void setCollections(List<PartCollectionItem> collections) {
		this.collections = collections;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Date submitted) {
		this.submitted = submitted;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getSubmitteBy() {
		return submitteBy;
	}

	public void setSubmitteBy(Integer submitteBy) {
		this.submitteBy = submitteBy;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getAppCode() {
		return appCode;
	}

	public void setAppCode(Integer appCode) {
		this.appCode = appCode;
	}

	public Integer getVehicleYear() {
		return vehicleYear;
	}

	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public ModelYear getModelYear() {
		return modelYear;
	}

	public void setModelYear(ModelYear modelYear) {
		this.modelYear = modelYear;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Integer getMakeId() {
		return makeId;
	}

	public void setMakeId(Integer makeId) {
		this.makeId = makeId;
	}

	public Integer getDeliveryFees() {
		return deliveryFees;
	}

	public void setDeliveryFees(Integer deliveryFees) {
		this.deliveryFees = deliveryFees;
	}

	public double getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	public CustomerAddress getAddress() {
		return address;
	}

	public void setAddress(CustomerAddress address) {
		this.address = address;
	}

	public PartsOrder getPartsOrder() {
		return partsOrder;
	}

	public void setPartsOrder(PartsOrder partsOrder) {
		this.partsOrder = partsOrder;
	}

	public boolean isNoVin() {
		return noVin;
	}

	public void setNoVin(boolean noVin) {
		this.noVin = noVin;
	}

	public boolean isVinImage() {
		return vinImage;
	}

	public void setVinImage(boolean vinImage) {
		this.vinImage = vinImage;
	}

	public Integer getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(Integer promotionCode) {
		this.promotionCode = promotionCode;
	}

	public PromotionCode getPromoCodeObject() {
		return promoCodeObject;
	}

	public void setPromoCodeObject(PromotionCode promoCodeObject) {
		this.promoCodeObject = promoCodeObject;
	}

	public List<Quotation> getQuotations() {
		return quotations;
	}

	public void setQuotations(List<Quotation> quotations) {
		this.quotations = quotations;
	}
	
	
	

}
