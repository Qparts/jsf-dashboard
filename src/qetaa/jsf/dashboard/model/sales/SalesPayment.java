package qetaa.jsf.dashboard.model.sales;

import java.io.Serializable;
import java.util.Date;

public class SalesPayment implements Serializable{
	private static final long serialVersionUID = 1L;
	private long id;
	private Sales sales;
	private SalesReturn salesReturn;
	private double amount;
	private String paymentRef;
	private char method;
	private Date paymentDate;
	private String provider;
	private Double creditFees;
	private Integer bankId;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Sales getSales() {
		return sales;
	}
	public void setSales(Sales sales) {
		this.sales = sales;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPaymentRef() {
		return paymentRef;
	}
	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}
	public char getMethod() {
		return method;
	}
	public void setMethod(char method) {
		this.method = method;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public Double getCreditFees() {
		return creditFees;
	}
	public void setCreditFees(Double creditFees) {
		this.creditFees = creditFees;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public SalesReturn getSalesReturn() {
		return salesReturn;
	}
	public void setSalesReturn(SalesReturn salesReturn) {
		this.salesReturn = salesReturn;
	}
	
}
