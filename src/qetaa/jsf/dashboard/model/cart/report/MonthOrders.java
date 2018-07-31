package qetaa.jsf.dashboard.model.cart.report;

public class MonthOrders {
	private int month;
	private String monthName;
	private int carts;
	private int partOrders;
	
	public int getMonth() {
		return month;
	}
	
	
	public void setMonth(int month) {
		this.month = month;
	}
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public int getCarts() {
		return carts;
	}
	public void setCarts(int count) {
		this.carts = count;
	}

	public int getPartOrders() {
		return partOrders;
	}

	public void setPartOrders(int partOrders) {
		this.partOrders = partOrders;
	}
	
}
