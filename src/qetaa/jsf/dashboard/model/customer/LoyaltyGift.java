package qetaa.jsf.dashboard.model.customer;

import java.io.Serializable;

import javax.servlet.http.Part;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoyaltyGift implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String giftName;
	private String giftNameAr;
	private int points;
	private char status;
	@JsonIgnore
	private Part file;
	

	public Part getFile() {
		return file;
	}
	public void setFile(Part file) {
		this.file = file;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGiftName() {
		return giftName;
	}
	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}
	public String getGiftNameAr() {
		return giftNameAr;
	}
	public void setGiftNameAr(String giftNameAr) {
		this.giftNameAr = giftNameAr;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	
	
}
