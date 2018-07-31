package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;

public class CartAssignment implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private Integer assignedTo;
	private Integer assignedBy;
	private char stage;
	private long cartId;
	private Date assignedDate;
	private Date completedDate;
	private char status;//A = active, D = deactive, C = completed
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Integer getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(Integer assignedTo) {
		this.assignedTo = assignedTo;
	}
	public Integer getAssignedBy() {
		return assignedBy;
	}
	public void setAssignedBy(Integer assignedBy) {
		this.assignedBy = assignedBy;
	}
	public char getStage() {
		return stage;
	}
	public void setStage(char stage) {
		this.stage = stage;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public Date getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignedBy == null) ? 0 : assignedBy.hashCode());
		result = prime * result + ((assignedDate == null) ? 0 : assignedDate.hashCode());
		result = prime * result + ((assignedTo == null) ? 0 : assignedTo.hashCode());
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((completedDate == null) ? 0 : completedDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + stage;
		result = prime * result + status;
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
		CartAssignment other = (CartAssignment) obj;
		if (assignedBy == null) {
			if (other.assignedBy != null)
				return false;
		} else if (!assignedBy.equals(other.assignedBy))
			return false;
		if (assignedDate == null) {
			if (other.assignedDate != null)
				return false;
		} else if (!assignedDate.equals(other.assignedDate))
			return false;
		if (assignedTo == null) {
			if (other.assignedTo != null)
				return false;
		} else if (!assignedTo.equals(other.assignedTo))
			return false;
		if (cartId != other.cartId)
			return false;
		if (completedDate == null) {
			if (other.completedDate != null)
				return false;
		} else if (!completedDate.equals(other.completedDate))
			return false;
		if (id != other.id)
			return false;
		if (stage != other.stage)
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	

	
}
