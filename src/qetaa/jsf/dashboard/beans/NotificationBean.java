package qetaa.jsf.dashboard.beans;
 
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.PojoRequester;

@Named
@SessionScoped
public class NotificationBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String header;
	private int userId;
	private int noVin;
	private int quotations;
	private int allQuotations;
	private int parts;
	private int followups;
	private int wires;
	private int unassigned;
	private int collection;
	private int receival;

	@Inject
	private Requester reqs;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		this.userId = loginBean.getUserHolder().getUser().getId();
		this.header = reqs.getSecurityHeader();
		try {
			initNotifications();
		} catch (Exception ex) {

		}
	}

	public void initNotifications() throws InterruptedException {
		Thread[] threads = new Thread[9];
		Thread t1 = initFollowups();
		Thread t2 = initUnassigned();
		Thread t3 = initParts();
		Thread t4 = initCollections();
		Thread t5 = initReceival();
		Thread t6 = initWires();
		Thread t7 = initQuotations();
		Thread t8 = initAllQuotations();
		Thread t9 = initNoVins();
		threads[0] = t1;
		threads[1] = t2;
		threads[2] = t3;
		threads[3] = t4;
		threads[4] = t5;
		threads[5] = t6;
		threads[6] = t7;
		threads[7] = t8;
		threads[8] = t9;
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
	}

	public void updateFollowups() {
		try {
			Response r = PojoRequester.getSecuredRequest(AppConstants.GET_FOLLOWUPS_NOTOFICATION, header);
			if (r.getStatus() == 200) {
				followups = r.readEntity(Integer.class);
			}
		} catch (Exception ex) {

		}
	}

	private Thread initFollowups() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_FOLLOWUPS_NOTOFICATION, header);
					if (r.getStatus() == 200) {
						followups = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	private Thread initNoVins() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_NO_VINS_NOTIFICATION, header);
					if (r.getStatus() == 200) {
						noVin = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}

	private Thread initParts() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_PARTS_NOTIFICATION, header);
					if (r.getStatus() == 200) {
						parts = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}

	private Thread initQuotations() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getQuotationNotification(userId), header);
					if (r.getStatus() == 200) {
						quotations = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initAllQuotations() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_ALL_QUOTATIONS_NOTIFICATION, header);
					if (r.getStatus() == 200) {
						allQuotations = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initUnassigned() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_UNASSIGNED_NOTIFICATIONS, header);
					if (r.getStatus() == 200) {
						unassigned = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initWires() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_WIRE_NOTIFICATION, header);
					if (r.getStatus() == 200) {
						wires = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}

	private Thread initCollections() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_COLLECTION_NOTIFICATION, header);
					if (r.getStatus() == 200) {
						collection = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	Thread initReceival() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_RECEIVAL_NOTIFICATION, header);
					if (r.getStatus() == 200) {
						receival = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
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

	public int getUnassigned() {
		return unassigned;
	}

	public int getCollection() {
		return collection;
	}

	public int getReceival() {
		return this.receival;
	}

	public int getAllQuotations() {
		return allQuotations;
	}

	public int getNoVin() {
		return noVin;
	}

}
