package qetaa.jsf.dashboard.model.vendor;

import java.io.Serializable;
import java.util.Date;

public class VendorRegion implements Serializable{
	private static final long serialVersionUID = 1L;
	private Vendor vendor;
	private int regionId;
	private Date created;
	private int createdBy;

	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
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
	

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + createdBy;
		result = prime * result + regionId;
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
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
		VendorRegion other = (VendorRegion) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdBy != other.createdBy)
			return false;
		if (regionId != other.regionId)
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (!vendor.equals(other.vendor))
			return false;
		return true;
	}




	public static class VendorRegionPK implements Serializable{
		private static final long serialVersionUID = 1L;
		protected int vendor;
		protected int regionId;
		public VendorRegionPK() {}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + vendor;
			result = prime * result + regionId;
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
			VendorRegionPK other = (VendorRegionPK) obj;
			if (vendor != other.vendor)
				return false;
			if (regionId != other.regionId)
				return false;
			return true;
		}
		
	}
	
}
