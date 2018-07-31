package qetaa.jsf.dashboard.test.catalog;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

@Named
@RequestScoped
public class LevamRequester implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String API_KEY = "4061d1c156ce18b60931fed26a72b0ca";
	
	
	public Response getRequest(String link) {
		link = link + "api_key=" + API_KEY;
		System.out.println("requesting " + link);
		Builder b = ClientBuilder.newClient().target(link).request();
		Response r = b.get();
		return r;
	}

}
