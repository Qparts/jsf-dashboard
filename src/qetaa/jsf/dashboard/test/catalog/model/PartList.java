package qetaa.jsf.dashboard.test.catalog.model;

import java.util.List;

public class PartList {
	
	private String img;
	private String imgDescription;
	private List<PartGroup> partGroups;
	private List<PartPosition> positions;
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getImgDescription() {
		return imgDescription;
	}
	public void setImgDescription(String imgDescription) {
		this.imgDescription = imgDescription;
	}
	public List<PartGroup> getPartGroups() {
		return partGroups;
	}
	public void setPartGroups(List<PartGroup> partGroups) {
		this.partGroups = partGroups;
	}
	public List<PartPosition> getPositions() {
		return positions;
	}
	public void setPositions(List<PartPosition> positions) {
		this.positions = positions;
	}
}
