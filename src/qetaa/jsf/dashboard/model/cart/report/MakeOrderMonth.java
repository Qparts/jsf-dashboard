package qetaa.jsf.dashboard.model.cart.report;

import java.util.List;

import qetaa.jsf.dashboard.model.vehicle.Make;

public class MakeOrderMonth {

	private int makeId;
	private Make make;
	private List<MonthOrders> monthOrders;
	
	
	
	public Make getMake() {
		return make;
	}
	public void setMake(Make make) {
		this.make = make;
	}
	public int getMakeId() {
		return makeId;
	}
	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}
	public List<MonthOrders> getMonthOrders() {
		return monthOrders;
	}
	public void setMonthOrders(List<MonthOrders> monthOrders) {
		this.monthOrders = monthOrders;
	}
	
}
