package qetaa.jsf.dashboard.test.catalog.model.levam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LevamCatalog implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty("name")
	private String name;//Toyota
	@JsonProperty("code")
	private String code;//ID of make : String integer
	@JsonProperty("catalog_code")
	private String catalogCode;//ID of catalog : String integer
	@JsonProperty("logo")
	private String logo;//URL
	@JsonProperty("logo_small")
	private String logoSmall;//URL
	@JsonProperty("supportframesearch")
    private String supportFrameSearch;//yes, no
	@JsonProperty("frameexample")
    private String frameExample;//empty
    @JsonProperty("supportvinsearch")
    private String supportVinSearch;//yes, no
    @JsonProperty("vinexample")
    private String vinExample;//1FA6P8CF0F5389504
    @JsonProperty("universal_classifier")
    private String universalClassifier;//yes, no
    @JsonProperty("type_moto")
    private String typeMoto;//yes, no
    @JsonProperty("type_auto")
    private String typeAuto;//yes, no
    @JsonProperty("type_transport")
    private String typeTransport;//yes, no
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCatalogCode() {
		return catalogCode;
	}
	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getLogoSmall() {
		return logoSmall;
	}
	public void setLogoSmall(String logoSmall) {
		this.logoSmall = logoSmall;
	}
	public String getSupportFrameSearch() {
		return supportFrameSearch;
	}
	public void setSupportFrameSearch(String supportFrameSearch) {
		this.supportFrameSearch = supportFrameSearch;
	}
	public String getFrameExample() {
		return frameExample;
	}
	public void setFrameExample(String frameExample) {
		this.frameExample = frameExample;
	}
	public String getSupportVinSearch() {
		return supportVinSearch;
	}
	public void setSupportVinSearch(String supportVinSearch) {
		this.supportVinSearch = supportVinSearch;
	}
	public String getVinExample() {
		return vinExample;
	}
	public void setVinExample(String vinExample) {
		this.vinExample = vinExample;
	}
	public String getUniversalClassifier() {
		return universalClassifier;
	}
	public void setUniversalClassifier(String universalClassifier) {
		this.universalClassifier = universalClassifier;
	}
	public String getTypeMoto() {
		return typeMoto;
	}
	public void setTypeMoto(String typeMoto) {
		this.typeMoto = typeMoto;
	}
	public String getTypeAuto() {
		return typeAuto;
	}
	public void setTypeAuto(String typeAuto) {
		this.typeAuto = typeAuto;
	}
	public String getTypeTransport() {
		return typeTransport;
	}
	public void setTypeTransport(String typeTransport) {
		this.typeTransport = typeTransport;
	}
    
    
}
