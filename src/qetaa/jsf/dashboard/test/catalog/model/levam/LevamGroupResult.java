package qetaa.jsf.dashboard.test.catalog.model.levam;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LevamGroupResult implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty("groups")
	private List<LevamGroup> groups;
	@JsonProperty("next")
	private Integer next;
	@JsonProperty("link")
	private String link;
	@JsonProperty("model_image")
	private String modelImage;
	@JsonProperty("model_info")
	private LevamModel levamModel;
	@JsonProperty("error")
	private String error;
	@JsonProperty("client")
	private LevamClient levamClient;
	
	
	public List<LevamGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<LevamGroup> groups) {
		this.groups = groups;
	}
	public Integer getNext() {
		return next;
	}
	public void setNext(Integer next) {
		this.next = next;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getModelImage() {
		return modelImage;
	}
	public void setModelImage(String modelImage) {
		this.modelImage = modelImage;
	}
	public LevamModel getLevamModel() {
		return levamModel;
	}
	public void setLevamModel(LevamModel levamModel) {
		this.levamModel = levamModel;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public LevamClient getLevamClient() {
		return levamClient;
	}
	public void setLevamClient(LevamClient levamClient) {
		this.levamClient = levamClient;
	}
	
}
