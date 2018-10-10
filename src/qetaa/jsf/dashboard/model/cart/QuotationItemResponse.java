package qetaa.jsf.dashboard.model.cart;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.product.ProductPrice;

public class QuotationItemResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private long cartId;
	private long quotationId;
	private long quotationItemId;
	private Long productId;
	private Long productPriceId;
	private String desc;
	private int quantity;
	private char status;//status of the item C = completed, N = not available, I = incomplete
	private Date created;//date of response
	private int createdBy;//or finder_id
	private Double defaultPercentage;
	@JsonIgnore
	private Product product;
	@JsonIgnore
	private ProductPrice productPrice;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public ProductPrice getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(ProductPrice productPrice) {
		this.productPrice = productPrice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public long getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(long quotationid) {
		this.quotationId = quotationid;
	}
	public long getQuotationItemId() {
		return quotationItemId;
	}
	public void setQuotationItemId(long quotationItemId) {
		this.quotationItemId = quotationItemId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Long getProductPriceId() {
		return productPriceId;
	}
	public void setProductPriceId(Long productPriceId) {
		this.productPriceId = productPriceId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Double getDefaultPercentage() {
		return defaultPercentage;
	}
	public void setDefaultPercentage(Double defaultPercentage) {
		this.defaultPercentage = defaultPercentage;
	}
	
	
	
	
	
}
