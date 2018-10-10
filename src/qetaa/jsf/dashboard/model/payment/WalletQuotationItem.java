package qetaa.jsf.dashboard.model.payment;

import java.io.Serializable;
import java.util.Date;

public class WalletQuotationItem implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long walletId;
	private long walletQuotationId;
	private long walletItemId;
	private Date created;
	
	
	
	public long getWalletQuotationId() {
		return walletQuotationId;
	}
	public void setWalletQuotationId(long walletQuotationId) {
		this.walletQuotationId = walletQuotationId;
	}
	public long getWalletItemId() {
		return walletItemId;
	}
	public void setWalletItemId(long walletItemId) {
		this.walletItemId = walletItemId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getWalletId() {
		return walletId;
	}
	public void setWalletId(long walletId) {
		this.walletId = walletId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	
}
