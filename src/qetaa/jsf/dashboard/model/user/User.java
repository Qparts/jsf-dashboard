package qetaa.jsf.dashboard.model.user;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String firstName;
	private String lastName;
	private char status;
	private String username;
	private List<Role> rolesList;

	public int getId() {
		return id;
	}
	
	@JsonIgnore
	public boolean hasAccess(int id) {
		for(Role role : rolesList) {
			for(Activity a : role.getActivityList()) {
				if(a.getId() == id) {
					if(a.isAccess()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public char getStatus() {
		return status;
	}

	public String getUsername() {
		return username;
	}

	public List<Role> getRolesList() {
		return rolesList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRolesList(List<Role> rolesList) {
		this.rolesList = rolesList;
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
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	

}
