package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.master.VehiclesBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.PojoRequester;
import qetaa.jsf.dashboard.model.customer.MakeHit;
import qetaa.jsf.dashboard.model.vehicle.Make;

@Named
@ViewScoped
public class OverallActivityBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Date date;
	private int cartsCreated;//
	private int quotationSubmitted;//
	private int partsOrdered;//
	private int wireTransferCreated;
	private int cartsArchived;//
	private int followupReviewsCreated;
	private int loginsCount;
	private int registeredCount;
	private int hitCount;
	private int newVisitCount;
	private int visitCount;
	private String header;
	private List<MakeHit> makeHits;
	
	@Inject
	private VehiclesBean vehiclesBean;
	@Inject
	private Requester reqs;
	
	
	@PostConstruct
	private void init() {
		this.header = reqs.getSecurityHeader();
		date = new Date();
	}
	
	public boolean isEven(double num) {
		return ((num % 2) == 0);
	}
	
	public void generateReport() throws InterruptedException {
		Thread[] threads = new Thread[12];
		threads[0] = initCartsCreated();
		threads[1] = initCartsArchived();
		threads[2] = initPartsOrders();
		threads[3] = initQuotationsSubmitted();
		threads[4] = initWireTransferCreated();
		threads[5] = initFollowUpReviewsCreated();
		threads[6] = initLoginsCount();
		threads[7] = initRegisteredCount();
		threads[8] = initHitCount();
		threads[9] = initMakeHits();
		threads[10] = initNewVisitsCount();
		threads[11] = initVisitsCount();
		
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
			threads[i].join();
		}
		
		
	}
	
	
	private Thread initCartsCreated() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartsCreated(date.getTime()), header);
					if (r.getStatus() == 200) {
						cartsCreated = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	
	private Thread initMakeHits() {
		List<Make> makes = vehiclesBean.getMakes();
		makeHits = new ArrayList<>();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for(Make m : makes) {
						Response r = PojoRequester.getSecuredRequest(AppConstants.getMakeHits(date.getTime(), m.getName()), header);
						if (r.getStatus() == 200) {
							Long count = r.readEntity(Long.class);
							MakeHit mh = new MakeHit();
							mh.setMake(m);
							mh.setCount(count);
							makeHits.add(mh);
						}
					}
					//sort
					Collections.sort(makeHits, new Comparator<MakeHit>() {
						@Override
						public int compare(MakeHit o1, MakeHit o2) {
							return o2.getCount().compareTo(o1.getCount());
						}
					});

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	private Thread initLoginsCount() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getLoginsCount(date.getTime()), header);
					if (r.getStatus() == 200) {
						loginsCount = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initRegisteredCount() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getRegisteredCount(date.getTime()), header);
					if (r.getStatus() == 200) {
						registeredCount = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	private Thread initHitCount() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getHitCount(date.getTime()), header);
					if (r.getStatus() == 200) {
						hitCount = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	private Thread initCartsArchived() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartsArchived(date.getTime()), header);
					if (r.getStatus() == 200) {
						cartsArchived = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	private Thread initPartsOrders() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getPartsOrders(date.getTime()), header);
					if (r.getStatus() == 200) {
						partsOrdered = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	
	private Thread initQuotationsSubmitted() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getSubmittedQuotations(date.getTime()), header);
					if (r.getStatus() == 200) {
						quotationSubmitted = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	private Thread initNewVisitsCount() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getNewVisitCount(date.getTime()), header);
					if (r.getStatus() == 200) {
						newVisitCount = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	private Thread initVisitsCount() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getVisitCount(date.getTime()), header);
					if (r.getStatus() == 200) {
						visitCount = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	
	private Thread initWireTransferCreated() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getWireTransfersCreated(date.getTime()), header);
					if (r.getStatus() == 200) {
						wireTransferCreated = r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	private Thread initFollowUpReviewsCreated() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getFollowUpReviewCreated(date.getTime()), header);
					if (r.getStatus() == 200) {
						followupReviewsCreated= r.readEntity(Integer.class);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;

	}
	
	public int getCartsCreated() {
		return cartsCreated;
	}


	public void setCartsCreated(int cartsCreated) {
		this.cartsCreated = cartsCreated;
	}


	public int getQuotationSubmitted() {
		return quotationSubmitted;
	}


	public void setQuotationSubmitted(int quotationSubmitted) {
		this.quotationSubmitted = quotationSubmitted;
	}


	public int getPartsOrdered() {
		return partsOrdered;
	}


	public void setPartsOrdered(int partsOrdered) {
		this.partsOrdered = partsOrdered;
	}


	public int getWireTransferCreated() {
		return wireTransferCreated;
	}


	public void setWireTransferCreated(int wireTransferCreated) {
		this.wireTransferCreated = wireTransferCreated;
	}


	public int getCartsArchived() {
		return cartsArchived;
	}


	public void setCartsArchived(int cartsArchived) {
		this.cartsArchived = cartsArchived;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}

	public int getFollowupReviewsCreated() {
		return followupReviewsCreated;
	}

	public void setFollowupReviewsCreated(int followupReviewsCreated) {
		this.followupReviewsCreated = followupReviewsCreated;
	}

	public int getLoginsCount() {
		return loginsCount;
	}

	public void setLoginsCount(int loginsCount) {
		this.loginsCount = loginsCount;
	}

	public int getRegisteredCount() {
		return registeredCount;
	}

	public void setRegisteredCount(int registeredCount) {
		this.registeredCount = registeredCount;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}

	public List<MakeHit> getMakeHits() {
		return makeHits;
	}

	public void setMakeHits(List<MakeHit> makeHits) {
		this.makeHits = makeHits;
	}

	public int getNewVisitCount() {
		return newVisitCount;
	}

	public void setNewVisitCount(int newVisitCount) {
		this.newVisitCount = newVisitCount;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}
	
	
	
	

}
