package qetaa.jsf.dashboard.beans.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.vehicle.Make;
import qetaa.jsf.dashboard.model.vehicle.Model;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;


@Named(value = "vehicleBean")
@ViewScoped
public class VehiclesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Make> makes;
	private TreeNode root;
	private TreeNode selectedNode;
	private Make make; 
	private Model model;
	private ModelYear modelYear;
	private Make selectedMake;
	private Model selectedModel;
	private ModelYear selectedModelYear;
	
	private Make seriesMake;
	private int seriesMakeId;
	private Model seriesModel;
	private int seriesModelId;
	private int seriesYearFrom;
	private int seriesYearTo;
	private char seriesStatus;
	
	
	

	@Inject
	private Requester reqs;
	
	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		root = new DefaultTreeNode("Root", null);
		makes = new ArrayList<>();
		make = new Make();
		model = new Model();
		modelYear = new ModelYear();
		initMakes();
		initVehicleTree();
		selectedMake = new Make();
		selectedModel = new Model();
		selectedModelYear = new ModelYear();
		seriesMake = new Make();
		seriesModel = new Model();
	}

	private void initMakes() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_MAKES);
		if (r.getStatus() == 200) {
			makes = r.readEntity(new GenericType<List<Make>>() {
			});
		}
	}
	
	public void seriesMakeListener(){
		 for(Make make : makes) {
			 if(make.getId() == this.seriesMakeId) {
				 this.seriesMake = make;
				 break;
			 }
		 }
	}
	
	public void seriesModelListener(){
		 for(Model type: seriesMake.getModels()) {
			 if(type.getId() == this.seriesModelId) {
				 this.seriesModel = type;
				 break;
			 }
		 }
	}

	private void initVehicleTree() {
		for (Make make : makes) {
			TreeNode makeNode = new DefaultTreeNode("make", make, root);
			for (Model model : make.getModels()) {
				TreeNode modelNode = new DefaultTreeNode("model", model , makeNode);
				for (ModelYear myear : model.getModelYears()) {
					new DefaultTreeNode("modelYear", myear, modelNode);
				}
			}
		}
	}
	
	public void createSeries() {
		seriesModelListener();
		Calendar c = Calendar.getInstance();
		int maxYear = c.get(Calendar.YEAR) + 1;
		if(seriesYearFrom < 1999) {
			Helper.addErrorMessage("Year should be 1999 or greater");			
		}
		else if(seriesYearTo > maxYear){
			Helper.addErrorMessage("Year should be less than "+ maxYear);
		}
		
		if(this.seriesYearFrom > this.seriesYearTo) {
			Helper.addErrorMessage("Year to cannot be greater than year from");
		}
		else {
		List<Integer> years = Helper.getYearsRange(this.seriesYearFrom, this.seriesYearTo);
			int created = 0;
			for(Integer y : years) {
				boolean found = false;
				for(ModelYear vm : this.seriesModel.getModelYears()) {
					if(vm.getYear() == y) {
						found = true;
						break;
					}
				}
				if(!found) {
					ModelYear vm = new ModelYear();
					vm.setYear(y);
					vm.setModel(this.seriesModel);
					vm.setMake(this.seriesMake);
					vm.setStatus(this.seriesStatus);
					Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_MODEL_YEAR, vm);
					if(r.getStatus() == 200) {
						created++;
						continue;
					}
					else {
						Helper.addErrorMessage("An error occured");
						break;
					}
				}
					
			}
			init();
			Helper.addInfoMessage(created + " model years Added");
		}
	}

	public void createMake() {
		make.getName().trim();
		make.getNameAr().trim();
		make.setCreated(new Date());
		make.setCreatedBy(loginBean.getUserHolder().getUser().getId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_MAKE, make);
		if (r.getStatus() == 200) {
			init();
			Helper.addInfoMessage("Make created!");
		} else if (r.getStatus() == 409) {
			Helper.addErrorMessage("Make already added!");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public void createModel(){
		this.model.getName().trim();
		this.model.getNameAr().trim();
		this.model.setMake(this.selectedMake);
		this.model.setCreated(new Date());
		this.model.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_MODEL, model);
		if(r.getStatus() == 200){
			Helper.addInfoMessage("Model created");
			init();
		}
		else if (r.getStatus() == 409){
			Helper.addErrorMessage("Model already added!");
		}
		else{
			Helper.addErrorMessage("An error occured!");
		}
	}
	
	public void createModelYear(){
		modelYear.setModel(this.selectedModel);
		modelYear.setMake(this.selectedModel.getMake());
		modelYear.setCreated(new Date());
		modelYear.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_MODEL_YEAR, modelYear);
		if(r.getStatus() == 200){
			init();
			Helper.addInfoMessage("Model Year created!");
		}
		else if (r.getStatus() == 409){
			Helper.addErrorMessage("Model Year already added!");
		}
		else{
			Helper.addErrorMessage("An error occured!");
		}
	}

	public void initSelectedNode() {
		if (null != selectedNode && this.selectedNode.getData() != null) {
			if (this.selectedNode.getData() instanceof Make) {
				this.selectedMake = (Make) this.selectedNode.getData();
			} else if (this.selectedNode.getData() instanceof Model) {
				this.selectedModel = (Model) this.selectedNode.getData();
			} else if (this.selectedNode.getData() instanceof ModelYear) {
				this.selectedModelYear = (ModelYear) this.selectedNode.getData();
			}
		}
	}

	public void updateMake() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_MAKE, this.selectedMake);
		if (r.getStatus() == 200) {
			init();
			Helper.addErrorMessage("Make updated!");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void updateModel() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_MODEL, this.selectedModel);
		if (r.getStatus() == 200) {
			init();
			Helper.addInfoMessage("Model updated!");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void updateModelYear() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_UPDATE_MODEL_YEAR, this.selectedModelYear);
		if (r.getStatus() == 200) {
			init();
			Helper.addErrorMessage("Model Year updated!");
		} else {
			Helper.addErrorMessage("An error occured");
		}
	}
	
	public Make getMakeFromId(Integer id) {
		for(Make m : makes){
			if(m.getId() == id) {
				return m;
			}
		}
		return null;
	}
	
	public String getStyle(char status){
		if(status == 'I'){
			return "color:red";
		}
		else return "";
	}

	public List<Make> getMakes() {
		return makes;
	}

	public void setMakes(List<Make> makes) {
		this.makes = makes;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public Make getMake() {
		return make;
	}

	public void setMake(Make make) {
		this.make = make;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ModelYear getModelYear() {
		return modelYear;
	}

	public void setModelYear(ModelYear modelYear) {
		this.modelYear = modelYear;
	}

	public Make getSelectedMake() {
		return selectedMake;
	}

	public void setSelectedMake(Make selectedMake) {
		this.selectedMake = selectedMake;
	}

	public Model getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(Model selectedModel) {
		this.selectedModel = selectedModel;
	}

	public ModelYear getSelectedModelYear() {
		return selectedModelYear;
	}

	public void setSelectedModelYear(ModelYear selectedModelYear) {
		this.selectedModelYear = selectedModelYear;
	}

	public Make getSeriesMake() {
		return seriesMake;
	}

	public void setSeriesMake(Make seriesMake) {
		this.seriesMake = seriesMake;
	}

	public int getSeriesMakeId() {
		return seriesMakeId;
	}

	public void setSeriesMakeId(int seriesMakeId) {
		this.seriesMakeId = seriesMakeId;
	}

	public Model getSeriesModel() {
		return seriesModel;
	}

	public void setSeriesModel(Model seriesModel) {
		this.seriesModel = seriesModel;
	}

	public int getSeriesModelId() {
		return seriesModelId;
	}

	public void setSeriesModelId(int seriesModelId) {
		this.seriesModelId = seriesModelId;
	}

	public int getSeriesYearFrom() {
		return seriesYearFrom;
	}

	public void setSeriesYearFrom(int seriesYearFrom) {
		this.seriesYearFrom = seriesYearFrom;
	}

	public int getSeriesYearTo() {
		return seriesYearTo;
	}

	public void setSeriesYearTo(int seriesYearTo) {
		this.seriesYearTo = seriesYearTo;
	}

	public char getSeriesStatus() {
		return seriesStatus;
	}

	public void setSeriesStatus(char seriesStatus) {
		this.seriesStatus = seriesStatus;
	}

}
