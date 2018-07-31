package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.model.customer.ActiveSessions;

@Named
@ViewScoped
public class CustomerActivityBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private long globalHits;
	private long todayHits;
	private long globalLogins;
	private long todayLogins;
	private long globalCarts;
	private long todayCarts;
	private long globalPartsOrdes;
	private long todayPartsOrders;
	private long globalFeedbacks;
	private long todayRegistered;
	private String header;
	private ActiveSessions activeSessions;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		this.header = reqs.getSecurityHeader();
		try {
			initActivity();
		} catch (Exception ex) {

		}
	}

	public void initActivity() throws InterruptedException {
		Thread[] threads = new Thread[11];
		Thread t1 = initGlobalHits();
		Thread t2 = initGlobalLogins();
		Thread t3 = initGlobalCarts();
		Thread t4 = initGlobalPartsOrdes();
		Thread t5 = initglobalFeedbacks();
		Thread t6 = initActiveSessions();
		Thread t7 = initTodayHits();
		Thread t8 = initTodayLogins();
		Thread t9 = initTodayCarts();
		Thread t10 = initTodayPartsOrdes();
		Thread t11 = initTodayRegistered();
		threads[0] = t1;
		threads[1] = t2;
		threads[2] = t3;
		threads[3] = t4;
		threads[4] = t5;
		threads[5] = t6;
		threads[6] = t7;
		threads[7] = t8;
		threads[8] = t9;
		threads[9] = t10;
		threads[10] = t11;
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
			threads[i].join();
		}
	}

	private Thread initActiveSessions() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_ACTIVE_SESSIONS, header);
					if (r.getStatus() == 200) {
						activeSessions = r.readEntity(ActiveSessions.class);
					}

				} catch (Exception ex) {
				}
			}
		});
		return thread;
	}

	private Thread initglobalFeedbacks() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

			}
		});
		return thread;
	}

	private Thread initGlobalPartsOrdes() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_GLOBAL_PART_ORDERS, header);
					if (r.getStatus() == 200) {
						globalPartsOrdes = r.readEntity(Long.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initTodayPartsOrdes() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_TODAY_PART_ORDERS, header);
					if (r.getStatus() == 200) {
						todayPartsOrders = r.readEntity(Long.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initGlobalCarts() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_GLOBAL_CARTS, header);
					if (r.getStatus() == 200) {
						globalCarts = r.readEntity(Long.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initTodayCarts() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_TODAY_CARTS, header);
					if (r.getStatus() == 200) {
						todayCarts = r.readEntity(Long.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	private Thread initGlobalLogins() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_GLOBAL_LOGINS, header);
					if (r.getStatus() == 200) {
						globalLogins = r.readEntity(Long.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initTodayLogins() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_TODAY_LOGINS, header);
					if (r.getStatus() == 200) {
						todayLogins = r.readEntity(Long.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}


	private Thread initGlobalHits() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_GLOBAL_HITS, header);
					if (r.getStatus() == 200) {
						globalHits = r.readEntity(Long.class);
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initTodayHits() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_TODAY_HITS, header);
					if (r.getStatus() == 200) {
						todayHits = r.readEntity(Long.class);
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initTodayRegistered() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.GET_TODAY_REGISTRATION, header);
					if (r.getStatus() == 200) {
						todayRegistered = r.readEntity(Long.class);
					}
				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	public long getGlobalHits() {
		return globalHits;
	}

	public long getGlobalLogins() {
		return globalLogins;
	}

	public long getGlobalCarts() {
		return globalCarts;
	}

	public long getGlobalPartsOrdes() {
		return globalPartsOrdes;
	}

	public long getGlobalFeedbacks() {
		return globalFeedbacks;
	}

	public ActiveSessions getActiveSessions() {
		return activeSessions;
	}

	public void setActiveSessions(ActiveSessions activeSessions) {
		this.activeSessions = activeSessions;
	}

	public long getTodayHits() {
		return todayHits;
	}

	public long getTodayLogins() {
		return todayLogins;
	}

	public long getTodayCarts() {
		return todayCarts;
	}

	public long getTodayPartsOrders() {
		return todayPartsOrders;
	}

	public long getTodayRegistered() {
		return todayRegistered;
	}

	

}
