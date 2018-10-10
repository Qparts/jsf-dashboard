package qetaa.jsf.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.servlet.http.Part;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name="prd_manufacturer")
@Entity 
public class Manufacturer implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "prd_manufacturer_id_seq_gen", sequenceName = "prd_manufacturer_id_seq", initialValue=1000, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prd_manufacturer_id_seq_gen")
	@Column(name="id")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="name_ar")
	private String nameAr;
	@Column(name="status")
	private char status;
	@Column(name="created")
	private Date created;
	@Column(name="createdBy")
	private int createdby;
	@Column(name="category_id")
	private int categoryId;
	@Column(name="genuine")
	private boolean genuine;
	@JsonIgnore
	private Part main;
	@JsonIgnore
	private Part thumbnail;
	
	
	
	public Part getMain() {
		return main;
	}
	public void setMain(Part main) {
		this.main = main;
	}
	public Part getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(Part thumbnail) {
		this.thumbnail = thumbnail;
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
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public boolean isGenuine() {
		return genuine;
	}
	public void setGenuine(boolean genuine) {
		this.genuine = genuine;
	}
	
	
	

}
