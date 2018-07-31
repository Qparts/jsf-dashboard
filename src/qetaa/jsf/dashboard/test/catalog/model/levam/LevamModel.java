package qetaa.jsf.dashboard.test.catalog.model.levam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LevamModel {

	@JsonProperty("Марка")
	private String unknown;// ":"audi",
	@JsonProperty("Line")
	private String line;
	@JsonProperty("Vehicle line")
	private String vehicleLine;
	@JsonProperty("Region")
	private String region;
	@JsonProperty("Production Date")
	private String prodoctionDate;
	@JsonProperty("Vehicle Type")
	private String vehicleType;
	@JsonProperty("Body Type")
	private String bodyType;
	@JsonProperty("Engine Capacity")
	private String engineCapacity;
	@JsonProperty("Engine Type")
	private String engineType;
	@JsonProperty("Fuel Type")
	private String fuelType;
	@JsonProperty("Transaxle")
	private String transaxle;
	@JsonProperty("Drive Type")
	private String driveType;
	@JsonProperty("Weather Type")
	private String weatherType;
	@JsonProperty("Exterior Colour")
	private String exteriorColor;
	@JsonProperty("Up")
	private String up;
	@JsonProperty("Down")
	private String down;
	@JsonProperty("Interior Colour")
	private String interiorColor;
	@JsonProperty("Model")
	private String model;
	@JsonProperty("Country")
	private String country;
	@JsonProperty("Region add")
	private String regionAdd;
	@JsonProperty("Plant")
	private String plant;
	@JsonProperty("Vehicle Factory")
	private String vehicleFactory;
	@JsonProperty("Engine Factory")
	private String engineFactory;
	@JsonProperty("Engine Number")
	private String engineNumber;
	@JsonProperty("Engine Code")
	private String engineCode;
	@JsonProperty("Transmisson Factory")
	private String transmissionFactory;
	@JsonProperty("Transmission Number")
	private String transmissionNumber;
	@JsonProperty("Transmission Code")
	private String transmissionCode;
	@JsonProperty("Standart options")
	private String standardOptions;
	@JsonProperty("Optional options")
	private String optionalOptions;
	@JsonProperty("Transmission")
	private String transmission;// ":"EKX",
	@JsonProperty("Release date")
	private String releaseDate;// ":"1999",
	@JsonProperty("The market")
	private String theMarket;// ":"MEX, RA, RDW, USA, ZA",
	@JsonProperty("link")
	private String link;// ":"QWRpQSs0OEQyN3ZPbTU5ai8yWU1DZUFuY3RVQXUwNS8wOFJycUxVUzcySTZERDBIeFhHVUJrdFlVNk9IZUZvNlgvWktHQThlSzVKSHU1SVkyQjdJdkVuOHhhai80VWUwMVFuOEc0dlBEZ2tFbnZiZmt2bG51ZjBzd0hGbldaUXI="
	@JsonProperty("Brand")
	private String brand;
	@JsonProperty("Chassis code")
	private String chassisCode;
	@JsonProperty("Date of manufacture")
	private String dateOfManufacture;
	@JsonProperty("ENGINE 1")
	private String egine1;
	@JsonProperty("BODY")
	private String body;
	@JsonProperty("GRADE")
	private String grade;
	@JsonProperty("GEAR SHIFT TYPE")
	private String gearShiftType;
	@JsonProperty("Wheel")
	private String wheel;
	@JsonProperty("NO.OF DOORS")
	private String noOfDoors;
	@JsonProperty("FUEL INDUCTION")
	private String fuelInduction;
	@JsonProperty("DESTINATION")
	private String destination;
	@JsonProperty("BUILDING CONDITION")
	private String buildingCondition;

	private LevamUnifiedModel unifiedModel;

	public void initUnified(String catCode) {
		this.unifiedModel = new LevamUnifiedModel();
		this.unifiedModel.setLink(link);
		this.unifiedModel.setMakeName(findMakeName(catCode));
		
	}
	

	private String findMakeName(String catCode) {
		switch (catCode) {
		case "19":
		case "10":
		case "21":
		case "14":
		case "32":
		case "29":
			return this.brand;
		case "27":
		case "3":
		case "28":
		case "4":
			return this.unknown;
		}
		return "";
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getChassisCode() {
		return chassisCode;
	}

	public void setChassisCode(String chassisCode) {
		this.chassisCode = chassisCode;
	}

	public String getDateOfManufacture() {
		return dateOfManufacture;
	}

	public void setDateOfManufacture(String dateOfManufacture) {
		this.dateOfManufacture = dateOfManufacture;
	}

	public String getEgine1() {
		return egine1;
	}

	public void setEgine1(String egine1) {
		this.egine1 = egine1;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGearShiftType() {
		return gearShiftType;
	}

	public void setGearShiftType(String gearShiftType) {
		this.gearShiftType = gearShiftType;
	}

	public String getWheel() {
		return wheel;
	}

	public void setWheel(String wheel) {
		this.wheel = wheel;
	}

	public String getNoOfDoors() {
		return noOfDoors;
	}

	public void setNoOfDoors(String noOfDoors) {
		this.noOfDoors = noOfDoors;
	}

	public String getFuelInduction() {
		return fuelInduction;
	}

	public void setFuelInduction(String fuelInduction) {
		this.fuelInduction = fuelInduction;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getBuildingCondition() {
		return buildingCondition;
	}

	public void setBuildingCondition(String buildingCondition) {
		this.buildingCondition = buildingCondition;
	}

	public String getUnknown() {
		return unknown;
	}

	public void setUnknown(String unknown) {
		this.unknown = unknown;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getTheMarket() {
		return theMarket;
	}

	public void setTheMarket(String theMarket) {
		this.theMarket = theMarket;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getVehicleLine() {
		return vehicleLine;
	}

	public void setVehicleLine(String vehicleLine) {
		this.vehicleLine = vehicleLine;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProdoctionDate() {
		return prodoctionDate;
	}

	public void setProdoctionDate(String prodoctionDate) {
		this.prodoctionDate = prodoctionDate;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getBodyType() {
		return bodyType;
	}

	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}

	public String getEngineCapacity() {
		return engineCapacity;
	}

	public void setEngineCapacity(String engineCapacity) {
		this.engineCapacity = engineCapacity;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getTransaxle() {
		return transaxle;
	}

	public void setTransaxle(String transaxle) {
		this.transaxle = transaxle;
	}

	public String getDriveType() {
		return driveType;
	}

	public void setDriveType(String driveType) {
		this.driveType = driveType;
	}

	public String getWeatherType() {
		return weatherType;
	}

	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}

	public String getExteriorColor() {
		return exteriorColor;
	}

	public void setExteriorColor(String exteriorColor) {
		this.exteriorColor = exteriorColor;
	}

	public String getUp() {
		return up;
	}

	public void setUp(String up) {
		this.up = up;
	}

	public String getDown() {
		return down;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public String getInteriorColor() {
		return interiorColor;
	}

	public void setInteriorColor(String interiorColor) {
		this.interiorColor = interiorColor;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegionAdd() {
		return regionAdd;
	}

	public void setRegionAdd(String regionAdd) {
		this.regionAdd = regionAdd;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getVehicleFactory() {
		return vehicleFactory;
	}

	public void setVehicleFactory(String vehicleFactory) {
		this.vehicleFactory = vehicleFactory;
	}

	public String getEngineFactory() {
		return engineFactory;
	}

	public void setEngineFactory(String engineFactory) {
		this.engineFactory = engineFactory;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getEngineCode() {
		return engineCode;
	}

	public void setEngineCode(String engineCode) {
		this.engineCode = engineCode;
	}

	public String getTransmissionFactory() {
		return transmissionFactory;
	}

	public void setTransmissionFactory(String transmissionFactory) {
		this.transmissionFactory = transmissionFactory;
	}

	public String getTransmissionNumber() {
		return transmissionNumber;
	}

	public void setTransmissionNumber(String transmissionNumber) {
		this.transmissionNumber = transmissionNumber;
	}

	public String getTransmissionCode() {
		return transmissionCode;
	}

	public void setTransmissionCode(String transmissionCode) {
		this.transmissionCode = transmissionCode;
	}

	public String getStandardOptions() {
		return standardOptions;
	}

	public void setStandardOptions(String standardOptions) {
		this.standardOptions = standardOptions;
	}

	public String getOptionalOptions() {
		return optionalOptions;
	}

	public void setOptionalOptions(String optionalOptions) {
		this.optionalOptions = optionalOptions;
	}

	public LevamUnifiedModel getUnifiedModel() {
		return unifiedModel;
	}

	public void setUnifiedModel(LevamUnifiedModel unifiedModel) {
		this.unifiedModel = unifiedModel;
	}

}
