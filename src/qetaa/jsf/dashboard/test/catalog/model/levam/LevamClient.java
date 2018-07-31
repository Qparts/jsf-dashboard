package qetaa.jsf.dashboard.test.catalog.model.levam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LevamClient implements Serializable{
	@JsonProperty("mark")
	private String mark; // Audi
	@JsonProperty("catalog_code")
	private String catalogCode; // IntegerString
	@JsonProperty("family")
	private String family; // "Audi A6 qu. 2,8",
	@JsonProperty("model")
	private String model; // Audi A6 qu. 2,8",
	@JsonProperty("ssd")
	private String ssd;// "0e9b124de36c6d32187bb9838b3bfaf8:e851b8798763085ed35f24024beed098",
	@JsonProperty("param")
	private String param;// ":"",
	@JsonProperty("modification")
	private String modification;// ":"QWRpQSs0OEQyN3ZPbTU5ai8yWU1DZUFuY3RVQXUwNS8wOFJycUxVUzcySTZERDBIeFhHVUJrdFlVNk9IZUZvNlgvWktHQThlSzVKSHU1SVkyQjdJdkVuOHhhai80VWUwMVFuOEc0dlBEZ2syS202Nm5Ha2J4M2hka3IvWjdCejB3MUdKNFFUZVVOQnRMdHhVUzFHbm9LdUVVL1l2QXUrUy9iWlpNL0VJZnI2cm9KNXA2MlBnYVBydVdCTXF0NUZkeHhndzdhMmMvMFp1M2IvTWJOREc5T0FEL0tUZEJTRG9Rd21mUmZBYUs1Ym0vbUFiVVY0anhoWW5sZExWbm5lUQ=="
	@JsonProperty("group")
	private String group;// ":""
	@JsonProperty("subgroup")
	private String subgroup;// ":"",
	@JsonProperty("vin")
	private String vin;// ":"WAUBA24B3XN104537",
	@JsonProperty("frame")
	private String frame;// ":"",
	@JsonProperty("universal_classifier")
	private String universalClassifier;// ":"yes"
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getCatalogCode() {
		return catalogCode;
	}
	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSsd() {
		return ssd;
	}
	public void setSsd(String ssd) {
		this.ssd = ssd;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getModification() {
		return modification;
	}
	public void setModification(String modification) {
		this.modification = modification;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	public String getUniversalClassifier() {
		return universalClassifier;
	}
	public void setUniversalClassifier(String universalClassifier) {
		this.universalClassifier = universalClassifier;
	}
	
	
}

