package qetaa.jsf.dashboard.model.user;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserHolder implements Serializable{

	private static final long serialVersionUID = 1L;
	private User user;
	private List<Role> roles;
	private List<Activity> activities;
	private String token;
	
	
	@JsonIgnore
	public boolean hasAccess(int activityId) {
		return activities.contains(new Activity(activityId));
	}
	 
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public User getUser() {
		return user;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public List<Activity> getActivities() {
		return activities;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	
	
}
