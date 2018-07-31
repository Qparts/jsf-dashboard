package qetaa.jsf.dashboard.test.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.master.VehiclesBean;
import qetaa.jsf.dashboard.model.vehicle.Make;
import qetaa.jsf.dashboard.test.catalog.model.levam.LevamCatalog;
import qetaa.jsf.dashboard.test.catalog.model.levam.LevamCatalogs;
import qetaa.jsf.dashboard.test.catalog.model.levam.LevamGroup;
import qetaa.jsf.dashboard.test.catalog.model.levam.LevamGroupResult;
import qetaa.jsf.dashboard.test.catalog.model.levam.LevamModel;
import qetaa.jsf.dashboard.test.catalog.model.levam.LevamVinResult;

@Named
@SessionScoped
public class LevamCatalogBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String BASE = "https://api.levam.ru/oem/v1/";
	private String vin = "KMHCT41B0GU987600";
	private String catalogId = "hyundai";
	private int makeId = 4;
	private LevamCatalogs levamCats;
	private LevamVinResult vinResult;
	private LevamModel selectedModel;
	private LevamGroupResult rootGroup;
	
	
	
	@Inject
	private LevamRequester levamReqs;
	
	@Inject
	private VehiclesBean vehiclesbeans;
	
	@PostConstruct
	private void init() {
		levamCats = new LevamCatalogs();
		this.vinResult = new LevamVinResult();
		//initCats();
	}
	
	public void chooseModel(LevamModel lvm) {
		this.selectedModel = lvm;
		this.initFirstGroups();
	}
	
	public void searchVin() {
		vinResult = new LevamVinResult();
		selectedModel = null;
		Response r = levamReqs.getRequest(BASE + "VinFind?vin=" + vin + "&");
		if(r.getStatus() == 200) {
			vinResult = r.readEntity(LevamVinResult.class);
			if(vinResult.getError().length() > 0) {
				//there is an error
			}
			else {
				if(vinResult.getModels().size() == 1) {
					this.selectedModel = vinResult.getModels().get(0);
					this.initFirstGroups();
				}
				else if (vinResult.getModels().size() > 1) {
					boolean differentLinks = false;
					String check = vinResult.getModels().get(0).getLink();
					for(LevamModel lm : this.getVinResult().getModels()) {
						if(!check.equals(lm.getLink())) {
							differentLinks = true;
							break;
						}
					}
					if(!differentLinks) {
						this.selectedModel = vinResult.getModels().get(0);
						initFirstGroups();
					}
				}
			}
		}
	}
	
	public void initFirstGroups() {
		Response r = levamReqs.getRequest(BASE + "PartGroupsGet?ssd=" + this.vinResult.getClient().getSsd() + "&link=" + this.selectedModel.getLink() + "&");
		if(r.getStatus() == 200) {
			rootGroup = r.readEntity(LevamGroupResult.class);
		}
	}
	
	public void callPartsOrSubgroup(LevamGroupResult groupResult, LevamGroup levamGroup) {
		if(groupResult.getNext() == 1) {
			Response r = levamReqs.getRequest(BASE + "PartGroupsGet?ssd=" + this.vinResult.getClient().getSsd() + "&link=" + this.selectedModel.getLink() + "&group=" +levamGroup.getGroupName()+"&");
			if(r.getStatus() == 200) {
				LevamGroupResult gr = r.readEntity(LevamGroupResult.class);
				levamGroup.setGroupResult(groupResult);
			}
		}else {
			
		}
	}
	
	private void initCats() {
		Response r = levamReqs.getRequest(BASE +"CatalogsListGet?");
		if(r.getStatus() == 200) {
			this.levamCats = r.readEntity(LevamCatalogs.class);
			List<LevamCatalog> newLcs = new ArrayList<>();;
			for(LevamCatalog lc : this.levamCats.getCatalogs()) {
				boolean found = false;
				for(Make make : this.vehiclesbeans.getMakes()) {
					if(make.getName().toLowerCase().trim().equals(lc.getName().toLowerCase().trim())) {
						found = true;
					}
				}
				if(found) {
					newLcs.add(lc);
				}
			}
			this.levamCats.setCatalogs(newLcs);
		}
	}
	
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}
	public int getMakeId() {
		return makeId;
	}
	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}
	public LevamCatalogs getLevamCats() {
		return levamCats;
	}
	public void setLevamCats(LevamCatalogs levamCats) {
		this.levamCats = levamCats;
	}

	public LevamVinResult getVinResult() {
		return vinResult;
	}

	public void setVinResult(LevamVinResult vinResult) {
		this.vinResult = vinResult;
	}

	public LevamModel getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(LevamModel selectedModel) {
		this.selectedModel = selectedModel;
	}

	public LevamGroupResult getRootGroup() {
		return rootGroup;
	}

	public void setRootGroup(LevamGroupResult rootGroup) {
		this.rootGroup = rootGroup;
	}


	
	
	
	
}
