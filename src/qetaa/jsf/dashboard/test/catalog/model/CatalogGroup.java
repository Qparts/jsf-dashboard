package qetaa.jsf.dashboard.test.catalog.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CatalogGroup {
	
	private String id;
	private String parentId;
	private boolean hasParts;
	private String name;
	private String img;
	private String description;
	@JsonIgnore
	private List<CatalogGroup> subGroups;
	@JsonIgnore
	private PartList partList;
	
	
	public CatalogGroup() {
		subGroups = new ArrayList<>();
	}

	
	
	
	
	public List<CatalogGroup> getSubGroups() {
		return subGroups;
	}





	public void setSubGroups(List<CatalogGroup> subGroups) {
		this.subGroups = subGroups;
	}





	public PartList getPartList() {
		return partList;
	}


	public void setPartList(PartList partList) {
		this.partList = partList;
	}


	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean isHasParts() {
		return hasParts;
	}
	public void setHasParts(boolean hasParts) {
		this.hasParts = hasParts;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	

}
