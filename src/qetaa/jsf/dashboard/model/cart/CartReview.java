package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.user.User;

public class CartReview implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private long cartId;
	private int reviewerId;
	private Date created;
	private char status;
	private String reviewText;
	private char actionValue;
	private Double cartPrice;
	private Double alternativePrice;
	private String boughtFrom;
	private Integer boughtCity;
	private Date reminderDate;
	private User reviewer;
	private Integer stage;//1 = no vin, 2 = quotation, 3 = follow up, 4 = wire transfer, 5 = open archived or closed

	@JsonIgnore
	public char getStatusFromActionValue() {
		switch (actionValue) {
		case 'A':
		case 'C':
		case 'D':
		case 'F':
		case 'O':
		case 'X':
			return 'C';
		case 'G':
		case 'B':
		case 'H':
			return 'A';
		case 'E':
			return 'P';
		default:
			return 'C';
		}
	}

	@JsonIgnore
	public String getActionValueString() {
		switch (actionValue) {
		case 'A':
			return "Not interested";
		case 'B':
			return "General";
		case 'C':
			return "Exepnsive";
		case 'D':
			return "Late Quotation";
		case 'E':
			return "Postponed";
		case 'F':
			return "Purchased from another";
		case 'G':
			return "Edit items";
		case 'O':
			return "Other and Closed";
		case 'H':
			return "No Answer";
		default:
			return "";
		}
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public int getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(int reviewerId) {
		this.reviewerId = reviewerId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public char getActionValue() {
		return actionValue;
	}

	public void setActionValue(char actionValue) {
		this.actionValue = actionValue;
	}

	public Double getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(Double cartPrice) {
		this.cartPrice = cartPrice;
	}

	public Double getAlternativePrice() {
		return alternativePrice;
	}

	public void setAlternativePrice(Double alternativePrice) {
		this.alternativePrice = alternativePrice;
	}

	public String getBoughtFrom() {
		return boughtFrom;
	}

	public void setBoughtFrom(String boughtFrom) {
		this.boughtFrom = boughtFrom;
	}

	public Integer getBoughtCity() {
		return boughtCity;
	}

	public void setBoughtCity(Integer boughtCity) {
		this.boughtCity = boughtCity;
	}

	public Date getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + actionValue;
		result = prime * result + ((alternativePrice == null) ? 0 : alternativePrice.hashCode());
		result = prime * result + ((boughtCity == null) ? 0 : boughtCity.hashCode());
		result = prime * result + ((boughtFrom == null) ? 0 : boughtFrom.hashCode());
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((cartPrice == null) ? 0 : cartPrice.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((reminderDate == null) ? 0 : reminderDate.hashCode());
		result = prime * result + ((reviewText == null) ? 0 : reviewText.hashCode());
		result = prime * result + reviewerId;
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
		CartReview other = (CartReview) obj;
		if (actionValue != other.actionValue)
			return false;
		if (alternativePrice == null) {
			if (other.alternativePrice != null)
				return false;
		} else if (!alternativePrice.equals(other.alternativePrice))
			return false;
		if (boughtCity == null) {
			if (other.boughtCity != null)
				return false;
		} else if (!boughtCity.equals(other.boughtCity))
			return false;
		if (boughtFrom == null) {
			if (other.boughtFrom != null)
				return false;
		} else if (!boughtFrom.equals(other.boughtFrom))
			return false;
		if (cartId != other.cartId)
			return false;
		if (cartPrice == null) {
			if (other.cartPrice != null)
				return false;
		} else if (!cartPrice.equals(other.cartPrice))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id != other.id)
			return false;
		if (reminderDate == null) {
			if (other.reminderDate != null)
				return false;
		} else if (!reminderDate.equals(other.reminderDate))
			return false;
		if (reviewText == null) {
			if (other.reviewText != null)
				return false;
		} else if (!reviewText.equals(other.reviewText))
			return false;
		if (reviewerId != other.reviewerId)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

}
