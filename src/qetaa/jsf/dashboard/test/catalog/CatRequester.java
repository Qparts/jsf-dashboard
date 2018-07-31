package qetaa.jsf.dashboard.test.catalog;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Named
@RequestScoped
public class CatRequester implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String PC_API_KEY = "OEM-API-9BC9464D-D8ED-4D69-8943-32CF9FA0D3F7";
	
	
	public Response getSecuredRequest(String link) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, PC_API_KEY);
		Response r = b.get();
		return r;
	}

}
