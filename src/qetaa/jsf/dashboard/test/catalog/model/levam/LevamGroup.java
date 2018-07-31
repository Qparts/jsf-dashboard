package qetaa.jsf.dashboard.test.catalog.model.levam;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LevamGroup implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty("group_name")
	private String groupName;
	@JsonProperty("full_name")
	private String fullName;
	@JsonProperty("image")
	private List<String> image;
	
	private LevamGroupResult groupResult;
	
	
	
	public LevamGroupResult getGroupResult() {
		return groupResult;
	}
	public void setGroupResult(LevamGroupResult groupResult) {
		this.groupResult = groupResult;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	
}
