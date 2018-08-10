package qetaa.jsf.dashboard.beans.master;

import java.io.Serializable;
import java.util.ArrayList;
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
import qetaa.jsf.dashboard.model.vendor.PromotionProvider;

@Named
@ViewScoped
public class PromotionBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PromotionProvider provider;
	private List<PromotionProvider> providers;
	
	@Inject
	private Requester reqs;
	
	@PostConstruct
	private void init() {
		provider = new PromotionProvider();
		providers = new ArrayList<>();
		initProviders();
	}
	
	
	private void initProviders() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_PROMOTION_PROVIDERS);
		if(r.getStatus() == 200) {
			this.providers = r.readEntity(new GenericType<List<PromotionProvider>>() {});
		}
		else {
			
		}
	}
	
	public void createProvider() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_PROMOTION_PROVIDER, provider);
		if(r.getStatus() == 201) {
			init();
			Helper.addInfoMessage("provider created");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public PromotionProvider getProvider() {
		return provider;
	}

	public void setProvider(PromotionProvider provider) {
		this.provider = provider;
	}

	public List<PromotionProvider> getProviders() {
		return providers;
	}

	public void setProviders(List<PromotionProvider> providers) {
		this.providers = providers;
	}
	
	
	
	
}
