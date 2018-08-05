package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;

@Named
@SessionScoped
public class LocationsCartBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String jsonObject;
	private Date from;
	private Date to;
	private int makeId;
	private boolean archived;
	private boolean ordered;

	@Inject
	private Requester reqs;


	@PostConstruct
	public void init() {

	}

	public void search() {
		Response r = reqs.getSecuredRequest(AppConstants.getLocationCartsCount(from.getTime(), to.getTime(), makeId, archived, ordered));
		if (r.getStatus() == 200) {
			this.jsonObject = r.readEntity(String.class);
		}
	}


	public String getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public int getMakeId() {
		return makeId;
	}

	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public Requester getReqs() {
		return reqs;
	}

	public void setReqs(Requester reqs) {
		this.reqs = reqs;
	}

	

}
