package qetaa.jsf.dashboard.beans.master;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.vendor.PromotionProvider;

@Named(value = "promocodeBean")
@ViewScoped
public class PromoCodeBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private PromotionProvider provider;
	private int bulkNumner;
	private Date expireDate;
	private boolean discountPromo;
	private boolean reusable;
	private Double discountPercentage;

	@Inject
	private Requester reqs;
	

	@PostConstruct
	private void init() {
		try {
			expireDate = new Date();
			String s = Helper.getParam("provider");
			initProvider(s);
			this.reusable = false;
			this.discountPromo = false;
			this.discountPercentage = 0.05;
		} catch (Exception ex) {
			Helper.redirect("no-vin-carts");
		}
	}
	
	private void initProvider(String param) throws Exception {
		provider = new PromotionProvider();
		Integer id = Integer.parseInt(param);
		Response r = reqs.getSecuredRequest(AppConstants.getPromotionProvider(id));
		if (r.getStatus() == 200) {
			provider = r.readEntity(PromotionProvider.class);
		} else {
			throw new Exception();
		}
	}
	
	public void createBulkCodes() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("providerId", this.provider.getId());
		map.put("bulkNumber", this.bulkNumner);
		map.put("expire", this.expireDate.getTime());
		map.put("reusable", this.reusable);
		map.put("discountPromo", this.discountPromo);
		map.put("discountPercentage", this.discountPercentage);
		Response r = reqs.postSecuredRequest(AppConstants.POST_BULK_PROMOTION_CODES, map);
		if(r.getStatus() == 201) {
			Helper.redirect("promo-codes?provider=" + this.provider.getId());
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

	public int getBulkNumner() {
		return bulkNumner;
	}

	public void setBulkNumner(int bulkNumner) {
		this.bulkNumner = bulkNumner;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public boolean isDiscountPromo() {
		return discountPromo;
	}

	public void setDiscountPromo(boolean discountPromo) {
		this.discountPromo = discountPromo;
	}

	public boolean isReusable() {
		return reusable;
	}

	public void setReusable(boolean reusable) {
		this.reusable = reusable;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	
	
	
	

}
