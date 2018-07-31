package qetaa.jsf.dashboard.test.catalog.model.levam;

import java.util.ArrayList;
import java.util.List;

public class LevamVinResult {

	private String error;
	private LevamClient client;
	private List<LevamModel> models;
	
	public LevamVinResult() {
		error = "";
		client = new LevamClient();
		models = new ArrayList<>();
	}
	
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public LevamClient getClient() {
		return client;
	}
	public void setClient(LevamClient client) {
		this.client = client;
	}
	public List<LevamModel> getModels() {
		return models;
	}
	public void setModels(List<LevamModel> models) {
		this.models = models;
	}
	
	
}
