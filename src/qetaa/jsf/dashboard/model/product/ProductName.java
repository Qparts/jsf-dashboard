package qetaa.jsf.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductName implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String nameAr;
	private Date created;
	private List<ProductNameAlternative> altProductNames;

	
	public List<ProductNameAlternative> getAltProductNames() {
		return altProductNames;
	}
	public void setAltProductNames(List<ProductNameAlternative> altProductNames) {
		this.altProductNames = altProductNames;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	
	
	
	
}
