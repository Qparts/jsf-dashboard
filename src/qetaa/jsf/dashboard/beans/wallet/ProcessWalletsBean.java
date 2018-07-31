package qetaa.jsf.dashboard.beans.wallet;

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
import qetaa.jsf.dashboard.beans.master.CitiesBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.payment.Wallet;

@Named
@ViewScoped
public class ProcessWalletsBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Wallet> wallets;
	
	@Inject
	private Requester reqs;
	
	@Inject
	private CitiesBean citiesBean;
	
	@PostConstruct
	private void init() {
		wallets = new ArrayList<>();
		initWallets();
		try {
			initCartVariables();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void initCartVariables() throws InterruptedException {
		Thread[] mainThreads = new Thread[wallets.size()];
		int index = 0;
		String header = reqs.getSecurityHeader();
		for (Wallet wallet : wallets) {
			wallet.setCart(new Cart());
			wallet.getCart().setId(wallet.getCartId());
			wallet.getCart().setCustomerId(wallet.getCustomerId());
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[1];
					threads[0] = ThreadRunner.initAddress(wallet.getCart(), header);
					for (int i = 0; i < threads.length; i++)
						try {
							threads[i].start();
							threads[i].join();
						} catch (InterruptedException e) {
							e.printStackTrace();	
						}
				}
			});
			mainThreads[index] = t;
			index++;
		}
		for (int i = 0; i < mainThreads.length; i++) {
			mainThreads[i].start();
			mainThreads[i].join();
		}
	}		
	
	private void initWallets() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_PROCESS_WALLETS);
		if(r.getStatus() == 200) {
			wallets = r.readEntity(new GenericType<List<Wallet>>() {});
		}
	}

	public List<Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}
	
	
	
	
	
}
