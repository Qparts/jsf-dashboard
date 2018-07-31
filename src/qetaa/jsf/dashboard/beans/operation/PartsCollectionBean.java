package qetaa.jsf.dashboard.beans.operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.PartCollectionItem;

@Named
@ViewScoped
public class PartsCollectionBean implements Serializable {

	private static final long serialVersionUID = 1L; 
	private List<PartCollectionItem> collections;
	private PartCollectionItem selectedCollection;
	
	@Inject
	private Requester reqs;
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		this.selectedCollection = new PartCollectionItem();
		initCollections();
	}
	
	public void receiveItem() {
		this.selectedCollection.setReceivedBy(this.loginBean.getUserHolder().getUser().getId());
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_RECEIVE_ITEM, selectedCollection);
		if(r.getStatus() == 201) {
			init();
			Helper.addInfoMessage("Item received at warehouse");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	
	public void collectItem() {
		this.selectedCollection.setCollectedBy(this.loginBean.getUserHolder().getUser().getId());
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_COLLECT_ITEM, selectedCollection);
		if(r.getStatus() == 201) {
			init();
			Helper.addInfoMessage("Item collected from vendor");
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	private void initCollections() {
		collections = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.GET_PARTS_RECEIVAL_ITEMS);
		if (r.getStatus() == 200) {
			this.collections = r.readEntity(new GenericType<List<PartCollectionItem>>() {
			});
		} else {
			
		}
	}

	public List<PartCollectionItem> getCollections() {
		return collections;
	}

	public void setCollections(List<PartCollectionItem> collections) {
		this.collections = collections;
	}

	public PartCollectionItem getSelectedCollection() {
		return selectedCollection;
	}

	public void setSelectedCollection(PartCollectionItem selectedCollection) {
		this.selectedCollection = selectedCollection;
	}
	
}
