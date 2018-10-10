package qetaa.jsf.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.Part;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Category implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String nameAr;
	private char status;
	private Date created;
	private int createdby;
	@JsonIgnore
	private Part main;
	@JsonIgnore
	private Part thumb;
	
	public Part getMain() {
		return main;
	}
	public void setMain(Part main) {
		this.main = main;
	}
	public Part getThumb() {
		return thumb;
	}
	public void setThumb(Part thumb) {
		this.thumb = thumb;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getCreatedby() {
		return createdby;
	}
	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}
	

}
