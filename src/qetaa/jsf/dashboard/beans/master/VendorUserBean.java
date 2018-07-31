package qetaa.jsf.dashboard.beans.master;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.vendor.VendorHolder;
import qetaa.jsf.dashboard.model.vendor.VendorUser;



@Named
@ViewScoped
public class VendorUserBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private VendorHolder vendorHolder;
	private VendorUser vendorUser;
	
	
	@Inject
	private Requester reqs;
	@Inject
	private VendorBean vendorBean;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		vendorUser = new VendorUser();
		vendorHolder = new VendorHolder();
		generatePassword();
		String s = Helper.getParam("vendor-id");
		if (s != null) {
			try {
				Integer vId = Integer.parseInt(s);
				boolean found = false;
				for (VendorHolder vh : vendorBean.getVendorHolders()) {
					if (vh.getVendor().getId() == vId) {
						vendorHolder = vh;
						found = true;
						break;
					}
				}
				if(!found){
					throw new Exception();
				}
				
			} catch (Exception ex) {
				Helper.redirect("vendors");
			}
		}
	}
	
	public void createVendorUser(){
		this.vendorUser.getFirstName().trim();
		this.vendorUser.getLastNAme().trim();
		this.vendorUser.getEmail().trim();
		this.vendorUser.getUsername().trim();
		this.vendorUser.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
		this.vendorUser.setVendor(this.vendorHolder.getVendor());
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_VENDOR_USER, vendorUser);
		if(r.getStatus() == 200){
			Helper.redirect("vendors");
		}
		else if(r.getStatus() == 409){
			Helper.addErrorMessage("Username exists!");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
		
		
	}
	
	private void generatePassword(){
		this.vendorUser.setPassword(Helper.getRandomSaltString());
	}

	public VendorHolder getVendorHolder() {
		return vendorHolder;
	}

	public void setVendorHolder(VendorHolder vendorHolder) {
		this.vendorHolder = vendorHolder;
	}

	public VendorUser getVendorUser() {
		return vendorUser;
	}

	public void setVendorUser(VendorUser vendorUser) {
		this.vendorUser = vendorUser;
	}

}
