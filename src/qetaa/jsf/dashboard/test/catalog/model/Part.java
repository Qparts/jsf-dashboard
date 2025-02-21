package qetaa.jsf.dashboard.test.catalog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.product.Product;

public class Part {

	private String id;
	private String number;
	private String name;
	private String notice;
	private String description;
	private String positionNumber;
	private String url;
	@JsonIgnore
	private Product product;

	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPositionNumber() {
		return positionNumber;
	}
	public void setPositionNumber(String positionNumber) {
		this.positionNumber = positionNumber;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
