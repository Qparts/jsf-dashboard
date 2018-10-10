package qetaa.jsf.dashboard.beans.master;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.user.Role;
import qetaa.jsf.dashboard.model.user.User;


@Named
@ViewScoped
public class UsersBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<User> users;
	private User user; 
	private User selectedUser;
	private String password;
	private List<User> activeAdvisors;
	private int selectedRoleId;
	private int selectedRoleId2;

	@Inject
	private Requester reqs;

	@Inject
	private RolesBean rolebean;

	@PostConstruct
	private void init() {
		user = new User();
		selectedUser = new User();
		user.setRolesList(new ArrayList<>());
		initUsers();
		initActiveAdvisors();
	}
	
	public User getUserFromId(int id) {
		for(User user : users) {
			if(user.getId() == id) {
				return user;
			}
		}
		return new User();
	}
	
	public List<User> getUsersWhoHaveAccessTo(String id) {
		List<User> accessUsers = new ArrayList<>();
		for(User user : users) {
			if(user.hasAccess(Integer.parseInt(id))) {
				accessUsers.add(user);
			}
		}
		System.out.println(accessUsers.size());
		return accessUsers;
	}
	
	public void removeRole(Role role) {
		if(this.selectedUser.getRolesList().size() > 1) {
			this.selectedUser.getRolesList().remove(role);
		}
		else {
			Helper.addErrorMessage("The user should at least have one role!");
		}
	}
	
	
	public void saveUserChanges() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_USER, selectedUser);
		if(r.getStatus() == 200) {
			Helper.addInfoMessage("Changes saved");
			init();
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void createUser() {
		if (!user.getRolesList().isEmpty()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user", user);
			map.put("password", password);
			Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_USER, map);
			if (r.getStatus() == 200) {
				Helper.addInfoMessage("User created!");
				init();
			}
			else {
				Helper.addErrorMessage("An error occured");
			}
		} else {
			Helper.addErrorMessage("No roles added for user!");
		}

	}

	public void addRole() {
		for (Role role : rolebean.getActiveRoles()) {
			if (role.getId() == this.selectedRoleId) {
				if (!user.getRolesList().contains(role)) {
					this.user.getRolesList().add(role);
				} else {
					Helper.addErrorMessage("Role already added");
				}
				break;
			}
		}
	}
	
	public void addRoleEdit() {
		for(Role role : rolebean.getActiveRoles()) {
			if(role.getId() == this.selectedRoleId2) {
				if(!selectedUser.getRolesList().contains(role)) {
					this.selectedUser.getRolesList().add(role);
				}else {
					Helper.addErrorMessage("Role already added");
				}
			}
		}
	}

	private void initUsers() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_USERS);
		if (r.getStatus() == 200) {
			this.users = r.readEntity(new GenericType<List<User>>() {
			});
		}
	}

	private void initActiveAdvisors() {
		activeAdvisors = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.getUsersWhoHasAccessTo(8));
		if (r.getStatus() == 200) {
			this.activeAdvisors = r.readEntity(new GenericType<List<User>>() {
			});
		}
	}

	public List<User> getActiveAdvisors() {
		return activeAdvisors;
	}

	public void setActiveAdvisors(List<User> activeAdvisors) {
		this.activeAdvisors = activeAdvisors;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSelectedRoleId() {
		return selectedRoleId;
	}

	public void setSelectedRoleId(int selectedRoleId) {
		this.selectedRoleId = selectedRoleId;
	}

	public int getSelectedRoleId2() {
		return selectedRoleId2;
	}

	public void setSelectedRoleId2(int selectedRoleId2) {
		this.selectedRoleId2 = selectedRoleId2;
	}
	
	

}
