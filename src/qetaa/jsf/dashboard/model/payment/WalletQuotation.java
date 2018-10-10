package qetaa.jsf.dashboard.model.payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WalletQuotation implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long walletId;
	private Date created;
	private int createdBy;
	private List<WalletQuotationItem> walletQuotationItems;
	
	
	@JsonIgnore
	public double getTotalQuotationItemsAmount(List<WalletItem> wis) {
		double total = 0;
		if (this.walletQuotationItems != null) {
			for (WalletQuotationItem wq : walletQuotationItems) {
				WalletItem wi = getWalletItemFromId(wis, wq.getWalletItemId());
				total = total + wi.getUnitSales() * wi.getQuantity();
			}
		}
		return total;
	}
	
	public List<WalletItem> getAvailableWalletItems(List<WalletItem> oitems) {
		List<WalletItem> wis = new ArrayList<>();
		for(WalletQuotationItem wqi : walletQuotationItems) {
			for(WalletItem wi : oitems) {
				if(wi.getId() == wqi.getWalletItemId()) {
					wis.add(wi);
					break;
				}
			}
		}
		return wis;
	}
	
	private WalletItem getWalletItemFromId(List<WalletItem> wis, long wiId) {
		for(WalletItem wi : wis) {
			if(wi.getId() == wiId) {
				return wi;
			}
		}
		return null;
	}
	
	public List<WalletQuotationItem> getWalletQuotationItems() {
		return walletQuotationItems;
	}
	public void setWalletQuotationItems(List<WalletQuotationItem> walletQuotationItems) {
		this.walletQuotationItems = walletQuotationItems;
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
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	
	
}
