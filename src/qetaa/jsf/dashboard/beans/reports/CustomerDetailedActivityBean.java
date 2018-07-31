package qetaa.jsf.dashboard.beans.reports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.model.customer.HitActivityGroup;

@Named(value="detailedActivityBean")
@SessionScoped
public class CustomerDetailedActivityBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<HitActivityGroup> hitGroups;
	private List<HitActivityGroup> filtered;
	private Date date;
	private HitActivityGroup selectedHitGroup;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		date = new Date();
		hitGroups = new ArrayList<>();
	}

	public void generateReport() {
		hitGroups = new ArrayList<>();
		filtered = null;
		Response r = reqs.getSecuredRequest(AppConstants.getDetailedActivityHits(date.getTime()));
		if(r.getStatus() == 200) {
			hitGroups = r.readEntity(new GenericType<List<HitActivityGroup>>() {});
		}else {
			
		}
	}

	public List<HitActivityGroup> getHitGroups() {
		return hitGroups;
	}

	public void setHitGroups(List<HitActivityGroup> hitGroups) {
		this.hitGroups = hitGroups;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public HitActivityGroup getSelectedHitGroup() {
		return selectedHitGroup;
	}
	
	public void chooseSelectedHitGroup(HitActivityGroup selectedHitGroup) {
		this.selectedHitGroup = selectedHitGroup;
	}

	public void setSelectedHitGroup(HitActivityGroup selectedHitGroup) {
		this.selectedHitGroup = selectedHitGroup;
	}

	public List<HitActivityGroup> getFiltered() {
		return filtered;
	}

	public void setFiltered(List<HitActivityGroup> filtered) {
		this.filtered = filtered;
	}	

}
