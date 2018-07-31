package qetaa.jsf.dashboard.model.product;

import java.io.Serializable;
import java.util.Date;

public class ProductStock implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private Date created;
	private Product product;
	private int quantity;
	private long purchaseId;
	private Long cartId;
	private Long salesReturnId;

	public Long getSalesReturnId() {
		return salesReturnId;
	}

	public void setSalesReturnId(Long salesReturnId) {
		this.salesReturnId = salesReturnId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

}
