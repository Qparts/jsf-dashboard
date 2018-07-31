package qetaa.jsf.dashboard.beans.master;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
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
import qetaa.jsf.dashboard.model.customer.LoyaltyGift;

@Named
@ViewScoped
public class LoyaltyGiftsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private LoyaltyGift gift;
	private List<LoyaltyGift> gifts;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		gifts = new ArrayList<>();
		gift = new LoyaltyGift();
		initGifts();
	}
	
	public void createLoyaltyGift() {
		gift.setGiftName(gift.getGiftName().trim());
		gift.setGiftNameAr(gift.getGiftNameAr().trim());
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_LOYALTY_GIFT, gift);
		if(r.getStatus() == 200) {
			Integer i = r.readEntity(Integer.class);
			saveFile(i);
			Helper.addInfoMessage("Gift created");
			init();
		}
		else if(r.getStatus() == 409) {
			Helper.addErrorMessage("Gift already added");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}
	private void saveFile(Integer id) {
		 try (InputStream input = this.gift.getFile().getInputStream()) {
			 Files.copy(input, new File(AppConstants.getGiftsDirectory(), id + ".png").toPath());
		 }catch (IOException e) {
		  e.printStackTrace();
		 }
	}

	private void initGifts() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_LOYALTY_GIFTS);
		if (r.getStatus() == 200) {
			this.gifts = r.readEntity(new GenericType<List<LoyaltyGift>>() {
			});
		}
	}

	public LoyaltyGift getGift() {
		return gift;
	}

	public void setGift(LoyaltyGift gift) {
		this.gift = gift;
	}

	public List<LoyaltyGift> getGifts() {
		return gifts;
	}

	public void setGifts(List<LoyaltyGift> gifts) {
		this.gifts = gifts;
	}
	
	
}
