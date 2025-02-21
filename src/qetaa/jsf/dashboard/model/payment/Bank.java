package qetaa.jsf.dashboard.model.payment;

import java.io.Serializable;

public class Bank implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int bankId;
	private String name;
	private String nameAr;
	private String iban;
	private String account;
	private String owner;
	private char customerStatus;
	private char internalStatus;
	
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public char getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(char customerStatus) {
		this.customerStatus = customerStatus;
	}
	public char getInternalStatus() {
		return internalStatus;
	}
	public void setInternalStatus(char internalStatus) {
		this.internalStatus = internalStatus;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result + bankId;
		result = prime * result + customerStatus;
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		result = prime * result + internalStatus;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameAr == null) ? 0 : nameAr.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		Bank other = (Bank) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (bankId != other.bankId)
			return false;
		if (customerStatus != other.customerStatus)
			return false;
		if (iban == null) {
			if (other.iban != null)
				return false;
		} else if (!iban.equals(other.iban))
			return false;
		if (internalStatus != other.internalStatus)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nameAr == null) {
			if (other.nameAr != null)
				return false;
		} else if (!nameAr.equals(other.nameAr))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}
	
	

}
