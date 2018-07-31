package qetaa.jsf.dashboard.model.vendor;

import java.io.Serializable;
import java.util.Date;

public class VendorUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private Vendor vendor;
	private String username;
	private String firstName;
	private String lastNAme;
	private String password;
	private Date created;
	private int createdBy;
	private String contactNumber;
	private String email;
	private char status;
	
	public int getId() {
		return id;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastNAme() {
		return lastNAme;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public Date getCreated() {
		return created;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public char getStatus() {
		return status;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastNAme(String lastNAme) {
		this.lastNAme = lastNAme;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	
	
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		VendorUser other = (VendorUser) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
