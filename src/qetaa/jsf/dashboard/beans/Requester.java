package qetaa.jsf.dashboard.beans;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.helpers.AppConstants;

@Named
@RequestScoped
public class Requester implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private LoginBean loginBean;
	
	public String getSecurityHeader() {
		return "Bearer " + loginBean.getUserHolder().getToken() + " && " + loginBean.getUserHolder().getUser().getId() + " && "
	 			+ AppConstants.APP_SECRET + " && U";//from user
	}

	public Response getSecuredRequest(String link) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader());
		Response r = b.get();
		return r;
	}
	
	public Response deleteSecuredRequest(String link) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader());
		Response r = b.delete();
		return r;
	}

	public <T> Response postSecuredRequest(String link, T t) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader());
		Response r = b.post(Entity.entity(t, "application/json"));// not secured
		return r;
	}
	
	public <T> Response putSecuredRequest(String link, T t) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, getSecurityHeader());
		Response r = b.put(Entity.entity(t, "application/json"));// not secured
		return r;
	}
	
	
	
}

