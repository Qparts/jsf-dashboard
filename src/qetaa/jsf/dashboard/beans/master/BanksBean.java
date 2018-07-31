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
import qetaa.jsf.dashboard.model.payment.Bank;
 
@Named
@ViewScoped
public class BanksBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Bank bank; 
	private List<Bank> banks;
	private List<Bank> activeBanks;
	@Inject
	private Requester reqs;
	
	
	@PostConstruct
	private void init() {
		bank = new Bank();
		initBanks();
		initActiveBanks();
	}
	
	private void initBanks() {
		banks = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_BANKS);
		if(r.getStatus() == 200) {
			this.banks= r.readEntity(new GenericType<List<Bank>>() {});
		}
	}
	
	public Bank getBankFromId(int bankId) {
		for(Bank b : banks) {
			if(b.getBankId() == bankId)
				return b;
		}
		return null;
	}
	
	private void initActiveBanks() {
		this.activeBanks = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_ACTIVE_BANKS);
		if(r.getStatus() == 200) {
			this.activeBanks= r.readEntity(new GenericType<List<Bank>>() {});
		}
	}
	
	
	public void createBank() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_BANK, bank);
		if(r.getStatus() == 200) {
			init();
			Helper.addInfoMessage("Bank created");
		}
		else {
			Helper.addErrorMessage("Something went wrong");
		}
	}


	public Bank getBank() {
		return bank;
	}

	public List<Bank> getBanks() {
		return banks;
	}
	
	public List<Bank> getActiveBanks() {
		return this.activeBanks;
	}

}
