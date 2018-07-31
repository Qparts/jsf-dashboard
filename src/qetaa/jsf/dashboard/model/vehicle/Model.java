package qetaa.jsf.dashboard.model.vehicle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
public class Model implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private Make make;
	private String name;
	private String nameAr;
	private char status;
	private Date created;
	private int createdBy;
	private List<ModelYear> modelYears;
	
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	
	public List<ModelYear> getModelYears() {
		return modelYears;
	}
	public void setModelYears(List<ModelYear> modelYears) {
		this.modelYears = modelYears;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	

	
	public int getId() {
		return id;
	}
	public Make getMake() {
		return make;
	}
	public String getName() {
		return name;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setMake(Make make) {
		this.make = make;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
