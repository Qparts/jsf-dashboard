package qetaa.jsf.dashboard.test.catalog.model.levam;

import java.io.Serializable;
import java.util.List;

public class LevamCatalogs implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<LevamCatalog> catalogs;
	private String error;
	
	
	
	public List<LevamCatalog> getCatalogs() {
		return catalogs;
	}
	public void setCatalogs(List<LevamCatalog> catalogs) {
		this.catalogs = catalogs;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
}
