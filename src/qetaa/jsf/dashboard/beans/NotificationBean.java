package qetaa.jsf.dashboard.beans;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.WebsocketLinks;

@Named
@SessionScoped
public class NotificationBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId;
	private int noVin;
	private int quotations;
	private int allQuotations;
	private int parts;
	private int followups;
	private int wires;
	private int wallets;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		this.userId = loginBean.getUserHolder().getUser().getId();
	}

	public void changeOccured() {
		try {
			Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			String data = map.get("param");
			if (data != null) {
				String[] messages = data.split(",");
				String function = messages[0];
				String value = messages[1];
				switch (function) {
				case "load initials":
					loadInitials();
					break;
				case "followups":
					followups = Integer.parseInt(value);
					break;
				case "wire transfers":
					wires = Integer.parseInt(value);
					break;
				case "no vins":
					this.noVin = Integer.parseInt(value);
					break;
				case "quotations":
					this.allQuotations = Integer.parseInt(value);
					break;
				case "my quotations":
					this.quotations = Integer.parseInt(value);
					break;
				default:
					System.out.println("default");

				}
			}
		} catch (Exception ex) {

		}

	}

	public void loadInitials() {
		initFollowups();
		initWires();
		initQuotations();
		initAllQuotations();
		initNoVins();
		initWallets();
	}
	
	private void initWallets() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_PROCESS_WALLET_NOTIFICATION);
		if (r.getStatus() == 200) {
			wallets = r.readEntity(Integer.class);
		}

	}


	private void initFollowups() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_FOLLOWUPS_NOTOFICATION);
		if (r.getStatus() == 200) {
			followups = r.readEntity(Integer.class);
		}

	}

	private void initNoVins() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_NO_VINS_NOTIFICATION);
		if (r.getStatus() == 200) {
			noVin = r.readEntity(Integer.class);
		}
	}

	private void initQuotations() {
		Response r = reqs.getSecuredRequest(AppConstants.getQuotationNotification(userId));
		if (r.getStatus() == 200) {
			quotations = r.readEntity(Integer.class);
		}
	}

	private void initAllQuotations() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_QUOTATIONS_NOTIFICATION);
		if (r.getStatus() == 200) {
			allQuotations = r.readEntity(Integer.class);
		}
	}

	private void initWires() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_WIRE_NOTIFICATION);
		if (r.getStatus() == 200) {
			wires = r.readEntity(Integer.class);
		}

	}

	public int getQuotations() {
		return quotations;
	}

	public int getParts() {
		return parts;
	}

	public int getFollowups() {
		return followups;
	}

	public int getWires() {
		return wires;
	}

	public int getAllQuotations() {
		return allQuotations;
	}

	public int getNoVin() {
		return noVin;
	}

	
	public int getWallets() {
		return wallets;
	}
	public String getNotificationsWSLink() {
		return WebsocketLinks.getNotificationsLink(loginBean.getLoggedUserId(), loginBean.getUserHolder().getToken());
	}

}
