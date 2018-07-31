package qetaa.jsf.dashboard.model.payment;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.WireTransfer;


public class PartsPayment implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private long customerId;
	private long cartId;
	private char status;//P = paid, F = failed
	private int index;//payment index
	private Date created; 
	private String provider;
	private String transactionId;
	private String currency;
	private String paymentType;//creditcard, sadad, wire transfer
	private int amount;//in halalas
	private String company;//visa, mastercard
	private String holderName;//holder name
	private String maskedNumber;
	private Double creditFees;
	//for wire transfers
	private Bank bank;	
	private Integer bankConfirmedBy;
	private char walletType;//P = Payment, R = Refund
	private char walletStatus;//F = finished, U = under process
	
	
	public PartsPayment(WireTransfer wtf) {
		this.customerId = wtf.getCustomerId();
		this.cartId = wtf.getCartId();
		this.status = 'P';
		this.index = 1;
		this.created = new Date();
		this.provider = "Wire Transfer";
		this.currency = "SAR";
		this.amount = Helper.paymentIntegerFormat(wtf.getAmount());
		this.paymentType = "wiretransfer";
		this.walletStatus = 'U';//under process wallet
		this.walletType = 'P';//payment type wallet
	}
	
	public PartsPayment() {
		
	}
	
	
	
	public char getWalletType() {
		return walletType;
	}

	public void setWalletType(char walletType) {
		this.walletType = walletType;
	}

	public char getWalletStatus() {
		return walletStatus;
	}

	public void setWalletStatus(char walletStatus) {
		this.walletStatus = walletStatus;
	}

	public Double getCreditFees() {
		return creditFees;
	}

	public void setCreditFees(Double creditFees) {
		this.creditFees = creditFees;
	}

	public long getId() {
		return id;
	}
	public void setId(long paymentId) {
		this.id = paymentId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
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
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
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
		default:
			return 'R';
		}
	}
	
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	public String getMaskedNumber() {
		return maskedNumber;
	}
	public void setMaskedNumber(String maskedNumber) {
		this.maskedNumber = maskedNumber;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + ((bankConfirmedBy == null) ? 0 : bankConfirmedBy.hashCode());
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + ((holderName == null) ? 0 : holderName.hashCode());
		result = prime * result + index;
		result = prime * result + ((maskedNumber == null) ? 0 : maskedNumber.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
		result = prime * result + status;
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
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
		PartsPayment other = (PartsPayment) obj;
		if (amount != other.amount)
			return false;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (bankConfirmedBy == null) {
			if (other.bankConfirmedBy != null)
				return false;
		} else if (!bankConfirmedBy.equals(other.bankConfirmedBy))
			return false;
		if (cartId != other.cartId)
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (customerId != other.customerId)
			return false;
		if (holderName == null) {
			if (other.holderName != null)
				return false;
		} else if (!holderName.equals(other.holderName))
			return false;
		if (index != other.index)
			return false;
		if (maskedNumber == null) {
			if (other.maskedNumber != null)
				return false;
		} else if (!maskedNumber.equals(other.maskedNumber))
			return false;
		if (id != other.id)
			return false;
		if (paymentType == null) {
			if (other.paymentType != null)
				return false;
		} else if (!paymentType.equals(other.paymentType))
			return false;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		if (status != other.status)
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		return true;
	}

}
