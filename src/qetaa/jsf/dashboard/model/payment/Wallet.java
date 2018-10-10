package qetaa.jsf.dashboard.model.payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.cart.Cart;

public class Wallet implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private char walletType;//Payment , Refund
	private long customerId;
	private String customerName;
	private long cartId;
	@JsonIgnore
	private Cart cart;
	private char status;//A = Awaiting sales, S = Sales Made 	
	private Date created;
	private String gateway;//Moyassar
	private String transactionId;//
	private String currency;
	private String paymentType;// creditcard, sadad, wire transfer
	private String ccCompany;// visa, mastercard
	private Bank bank;
	private Integer bankConfirmedBy;//user
	private Double creditFees;//credit fees
	private Double discountPercentage;
	private List<WalletItem> walletItems;
	private List<WalletQuotation> walletQuotations;
	
	
	@JsonIgnore
	public double getNetAmount() {
		double amount = 0;
		if(walletItems != null) {
			for(WalletItem wi : walletItems) {
				if(wi.getItemType() == 'F') {
					amount = amount - wi.getUnitSalesNetWv(); 
				}
				else {
					amount = amount + wi.getUnitSalesNetWv() * (wi.getItemType() == 'P' ? wi.getQuantity() : 1);
				}
			}
		}
		return amount;
	}
	
	@JsonIgnore
	public boolean isCreditSales() {
		switch(paymentType) {
			case ("wiretransfer"):
			case ("creditcard"):
			case ("sadad"):
			case ("mada"):
				return false;
			case ("cashondelivery"):
			case ("creditsales"):
				return true;
		}
		return false;
	}
	
	@JsonIgnore
	public char getPaymentTypeChar() {
		switch(paymentType) {
		case ("wiretransfer"):
			return 'R';
		case ("creditcard"):
			return 'C';
		case("sadad"):
			return 'S';
		case("mada"):
			return 'M';
		case("cashondelivery"):
			return 'D';
		case("creditsales"):
			return 'T'; 
		default:
			return 'R';
		}
	}
	
	@JsonIgnore
	public List<WalletItem> getProductWalletItem(){
		List<WalletItem> productItems = new ArrayList<>();
		if(this.walletItems != null) {
			for(WalletItem walletItem : walletItems) {
				if(walletItem.getItemType() == 'P') {
					productItems.add(walletItem);
				}
			}
		}
		
		return productItems;
	}

	public List<WalletItem> getWalletItems() {
		return walletItems;
	}

	public void setWalletItems(List<WalletItem> walletItems) {
		this.walletItems = walletItems;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public char getWalletType() {
		return walletType;
	}

	public void setWalletType(char walletType) {
		this.walletType = walletType;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getCcCompany() {
		return ccCompany;
	}

	public void setCcCompany(String ccCompany) {
		this.ccCompany = ccCompany;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Integer getBankConfirmedBy() {
		return bankConfirmedBy;
	}

	public void setBankConfirmedBy(Integer bankConfirmedBy) {
		this.bankConfirmedBy = bankConfirmedBy;
	}

	public Double getCreditFees() {
		return creditFees;
	}

	public void setCreditFees(Double creditFees) {
		this.creditFees = creditFees;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public List<WalletQuotation> getWalletQuotations() {
		return walletQuotations;
	}

	public void setWalletQuotations(List<WalletQuotation> walletQuotations) {
		this.walletQuotations = walletQuotations;
	}
	
	
	
	
	
}
