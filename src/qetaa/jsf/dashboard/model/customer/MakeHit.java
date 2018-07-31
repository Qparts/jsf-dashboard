package qetaa.jsf.dashboard.model.customer;

import java.io.Serializable;

import qetaa.jsf.dashboard.model.vehicle.Make;

public class MakeHit implements Serializable{

	private static final long serialVersionUID = 1L;
	private Make make;
	private Long count;
	
	public Make getMake() {
		return make;
	}
	public void setMake(Make make) {
		this.make = make;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
