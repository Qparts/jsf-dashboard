package qetaa.jsf.dashboard.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.engine.JRException;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.user.User;
import qetaa.jsf.dashboard.model.user.UserHolder;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private UserHolder userHolder; 
	
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() throws JRException{
		userHolder = new UserHolder();
		userHolder.setUser(new User());
	}
	
	public void checkAccessRedirectHomeL(List<String> ids) {
		if(!hasAccessL(ids)) {
			Helper.redirect("home");
		}
	}
	
	public void checkAccessRedirectHome(int id) {
		if(!hasAccess(id)) {
			Helper.redirect("home");
		}
	}
	
	public boolean hasAccess(int id) {
		return userHolder.hasAccess(id);
	}
	
	public boolean hasAccessL(List<String> ids) {
		boolean found = false;
		for(String s : ids) {
			if(userHolder.hasAccess(Integer.parseInt(s))) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	public boolean hasAccess(String id) {
		return userHolder.hasAccess(Integer.parseInt(id));
	}
	
	
	public int getLoggedUserId() {
		return this.getUserHolder().getUser().getId();
	}
	
	public void login(){
		HashMap<String,Object> map = new HashMap<>();
		map.put("username", username);
		map.put("appSecret", AppConstants.APP_SECRET);
		map.put("language", 'E');
		map.put("code", password);
		Response r = reqs.postSecuredRequest(AppConstants.USER_LOGIN, map);
		if(r.getStatus() == 200){
			this.userHolder = r.readEntity(UserHolder.class);
			doLogin();
		}
	}
	
	public boolean isLogged() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user.access") != null;
	}
	
	private void doLogin() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("user.access", userHolder);
		Helper.redirect("dash/home");
	}

	public void doLogout() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user.access");
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		Helper.redirect("/dashboard/login");
	}
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public UserHolder getUserHolder() {
		return userHolder;
	}

	public void setUserHolder(UserHolder userHolder) {
		this.userHolder = userHolder;
	}
	
	
}
