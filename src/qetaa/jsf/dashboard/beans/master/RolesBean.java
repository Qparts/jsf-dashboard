package qetaa.jsf.dashboard.beans.master;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.user.Activity;
import qetaa.jsf.dashboard.model.user.Role;



@Named(value = "roleBean")
@ViewScoped
public class RolesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Role> allRoles;
	private List<Role> activeRoles;
	private Role role;
	private Role selectedRole;

	@Inject
	Requester reqs;

	@PostConstruct
	private void init() {
		role = new Role();
		selectedRole = new Role();
		initAllRoles();
		initNewRole();
		initActiveRoles();
	}

	private void initNewRole() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_ACTIVITIES);
		if (r.getStatus() == 200) {
			List<Activity> acts = r.readEntity(new GenericType<List<Activity>>() {
			});
			this.role.setActivityList(acts);
		}
	}

	private void initAllRoles() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_ROLES);
		if (r.getStatus() == 200) {
			this.allRoles = r.readEntity(new GenericType<List<Role>>() {
			});
		}
	}
	

	private void initActiveRoles() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ACTIVE_ROLES);
		if (r.getStatus() == 200) {
			this.activeRoles = r.readEntity(new GenericType<List<Role>>() {
			});
		}
	}

	public void createRole() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_ROLE, role);
		if (r.getStatus() == 200) {
			init();
			Helper.addInfoMessage("Role created");
		} else {
			Helper.addErrorMessage("an eroor occured");
		}
	}

	public void saveRoleChanges() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_ROLE, selectedRole);
		if (r.getStatus() == 200) {
			init();
			Helper.addInfoMessage("Role Updated");
		} else {
			Helper.addErrorMessage("an eroor occured");
		}
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}
	
	public List<Role> getActiveRoles() {
		return activeRoles;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(Role selectedRole) {
		this.selectedRole = selectedRole;
	}
	
	

}
