package qetaa.jsf.dashboard.model.location;

import java.io.Serializable;
import java.util.List;

public class Region implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String nameAr;
	private double latitude;
	private double longitude;
	private int mapZoom;
	private Country country;
	private List<City> cities;
	private String jsonCode;
	/*
	 ['sa-4293', 0],
    ['sa-tb', 1],
    ['sa-jz', 2],
    ['sa-nj', 3],
    ['sa-ri', 4],
    ['sa-md', 5],
    ['sa-ha', 6],
    ['sa-qs', 7],
    ['sa-hs', 8],
    ['sa-jf', 9],
    ['sa-sh', 10],
    ['sa-ba', 11],
    ['sa-as', 12],
    ['sa-mk', 13]
	
	
	*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getMapZoom() {
		return mapZoom;
	}
	public void setMapZoom(int mapZoom) {
		this.mapZoom = mapZoom;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public String getJsonCode() {
		return jsonCode;
	}
	public void setJsonCode(String jsonCode) {
		this.jsonCode = jsonCode;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cities == null) ? 0 : cities.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + id;
		result = prime * result + ((jsonCode == null) ? 0 : jsonCode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + mapZoom;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameAr == null) ? 0 : nameAr.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Region other = (Region) obj;
		if (cities == null) {
			if (other.cities != null)
				return false;
		} else if (!cities.equals(other.cities))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (id != other.id)
			return false;
		if (jsonCode == null) {
			if (other.jsonCode != null)
				return false;
		} else if (!jsonCode.equals(other.jsonCode))
			return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		if (mapZoom != other.mapZoom)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nameAr == null) {
			if (other.nameAr != null)
				return false;
		} else if (!nameAr.equals(other.nameAr))
			return false;
		return true;
	}
	
	
	
}


