package qetaa.jsf.dashboard.model.product;

public class PriceHolder {

	private int vendorId;
	private int makeId;
	private String number;
	private String desc;
	private double price;
	private double priceWv;
	
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public int getMakeId() {
		return makeId;
	}
	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPriceWv() {
		return priceWv;
	}
	public void setPriceWv(double priceWv) {
		this.priceWv = priceWv;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
