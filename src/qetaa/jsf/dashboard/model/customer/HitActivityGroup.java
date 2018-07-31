package qetaa.jsf.dashboard.model.customer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HitActivityGroup implements Serializable{

	private static final long serialVersionUID = 1L;

	private HitActivity firstActivity;
	private List<HitActivity> tailingActicities;
	
	
	@JsonIgnore
	public int getTailingActivitiesSize() {
		return this.getTailingActivitiesSize();
	}
	@JsonIgnore
	public String getDuration() {
		Date date1 = firstActivity.getCreated();
		Date date2 = tailingActicities.get(tailingActicities.size()-1).getCreated();
		long millis = date2.getTime()-date1.getTime();
		long seconds = millis / 1000;
		long minutes = seconds / 60;
		
		String time = minutes % 60 + ":" + seconds % 60; 
		return time;

	} 
	
	public HitActivity getFirstActivity() {
		return firstActivity;
	}
	public void setFirstActivity(HitActivity firstActivity) {
		this.firstActivity = firstActivity;
	}
	public List<HitActivity> getTailingActicities() {
		return tailingActicities;
	}
	public void setTailingActicities(List<HitActivity> tailingActicities) {
		this.tailingActicities = tailingActicities;
	}
	
	
}
