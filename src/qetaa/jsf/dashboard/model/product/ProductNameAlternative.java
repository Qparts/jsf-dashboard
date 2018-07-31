package qetaa.jsf.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;

public class ProductNameAlternative implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private Date created;
	private ProductName productName;
	
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public ProductName getProductName() {
		return productName;
	}
	public void setProductName(ProductName productName) {
		this.productName = productName;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		ProductNameAlternative other = (ProductNameAlternative) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
