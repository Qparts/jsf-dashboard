package qetaa.jsf.dashboard.beans.customerservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.primefaces.context.RequestContext;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartItem;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.customer.EmailAccess;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.location.Country;
import qetaa.jsf.dashboard.model.vehicle.Make;
import qetaa.jsf.dashboard.model.vehicle.Model;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;

@Named
@SessionScoped
public class CustomerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private EmailAccess emailAccess;
	private List<Customer> customers;
	private List<Long> inactiveCustomers;
	private Customer selectedCustomer;
	private String smsCode;
	private String smsCodeUser;
	private String search;
	private int step;
	private Make selectedMake;
	private Model selectedModel;
	private ModelYear selectedModelYear;
	private List<Make> activeMakes;
	private int[] quantityArray;
	private Cart cart;
	private List<City> cities;
	private long lastOrderId;
	private List<Country> countries;

	@Inject 
	private LoginBean loginBean;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void initBean() {
		init();
		initActiveMakes();
		initCities();
		initQuantityArray();
		initCountries();
	}

	private void init() {
		cart = new Cart();
		cart.setCartItems(new ArrayList<>());
		addItem();
		selectedMake = new Make();
		selectedModel = new Model();
		selectedModelYear = new ModelYear();
		emailAccess = new EmailAccess();
		customers = new ArrayList<>();
		smsCode = null;
		smsCodeUser = null;
		search = "";
		selectedCustomer = new Customer();
		step = 1;
	}
	
	private void initCountries() {
		this.emailAccess.setCountryId(1);
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_COUNTRIES);
		if (r.getStatus() == 200) {
			countries = r.readEntity(new GenericType<List<Country>>() {
			});
		} else {
			countries = new ArrayList<>();
		}
	}
	
	public void editCustomer() {
		Response r = reqs.putSecuredRequest(AppConstants.PUT_EDIT_CUSTOMER, this.selectedCustomer);
		if(r.getStatus() == 201) {
			Helper.addInfoMessage("Customer Updated");
			for(Customer c : customers) {
				if(c.getId() == this.selectedCustomer.getId()) {
					c = this.selectedCustomer;
				}
			}
		}
		else {
			Helper.addErrorMessage("An error occured");
		}
	}

	public void startCart(Customer customer) {
		this.selectedCustomer = customer;
		this.step = 1;
		this.selectedMake = new Make();
		this.selectedModel = new Model();
		this.selectedModelYear = new ModelYear();
		this.cart = new Cart();
		cart.setCartItems(new ArrayList<>());
		addItem();
	}

	private void initCities() {
		this.cities = new ArrayList<>();
		Response r = reqs.getSecuredRequest(AppConstants.getCountryCities(1));
		if (r.getStatus() == 200) {
			cities = r.readEntity(new GenericType<List<City>>() {
			});
		}
		// get saudi arabia cities only
	}

	public void chooseMake(Make make) {
		this.selectedMake = make;
		step = 2;
	}

	public void previous() {
		this.step--;
	}

	public void chooseModel(Model model) {
		this.selectedModel = model;
		step = 3;
	}

	public void chooseModelYear(ModelYear modelYear) {
		this.selectedModelYear = modelYear;
		step = 4;
	}

	public void toStep5() {
		if (this.cart.getVin().length() == 17) {
			cart.getVin().toUpperCase();
			this.step = 5;
		} else {
			Helper.addErrorMessage("Invalid VIN");
		}
	}
	
	public void addItem() { 
		CartItem item = new CartItem();
		cart.getCartItems().add(item);
	}

	public void removeItem(CartItem item) {
		this.cart.getCartItems().remove(item);
	}

	public void toStep6() {
		this.step = 6;
	}
	
	
	

	public void submit() {
		cart.setCustomerId(this.selectedCustomer.getId());
		cart.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
		cart.setMakeId(this.selectedMake.getId());
		cart.setVatPercentage(0.05);
		cart.setVehicleYear(this.selectedModelYear.getId());
		for(CartItem ci : cart.getCartItems()) {
			ci.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
			ci.setName(ci.getName().trim());
		}
		Response r = reqs.postSecuredRequest(AppConstants.POST_CREATE_CART, cart);
		if (r.getStatus() == 200) {
			this.lastOrderId = r.readEntity(Long.class);
			init();
			Helper.addInfoMessage("Cart created : " +this.lastOrderId);
			RequestContext.getCurrentInstance().execute("PF('dlgwv').hide(");
		}
		else if(r.getStatus() == 429) {
			init();
		}
		else {
			Helper.addErrorMessage("An error occured");
			//log exception 
		}
	}
	

	private void initActiveMakes() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_ALL_ACTIVE_MAKES);
		if (r.getStatus() == 200) {
			this.activeMakes = r.readEntity(new GenericType<List<Make>>() {
			});
		}
	}

	public void searchCustomers() {
		Response r = reqs.getSecuredRequest(AppConstants.searchCustomerAny(search));
		if (r.getStatus() == 200) {
			this.customers = r.readEntity(new GenericType<List<Customer>>() {
			});
			initInactive();
		} else {
			customers = new ArrayList<>();

		}
		search = "";
	}
	
	private void initInactive() {
		List<Long> list = new ArrayList<>();
		for(Customer customer : customers) {
			list.add(customer.getId());
		}
		Response r2 = reqs.postSecuredRequest(AppConstants.POST_FILTER_OUT_ACTIVE_CUSTOMERS, list);
		if(r2.getStatus() == 200) {
			this.inactiveCustomers = r2.readEntity(new GenericType<List<Long>>() {});
		}
	}
	
	public boolean isInactive(long id) {
		return this.inactiveCustomers.contains(id);
	}

	public void createNewCustomer() {
		emailAccess.setCreatedBy(loginBean.getUserHolder().getUser().getId());

	}

	public void requestSMS() {
		if (this.emailAccess.getPassword().equals(this.emailAccess.getConfirmPassword())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", emailAccess.getMobile());
			map.put("email", emailAccess.getEmail());
			
			String link = "";
			if (this.emailAccess.getCountryId() == 1) {
				map.put("countryCode", "966");
				emailAccess.setCountryCode("966");
				link = AppConstants.POST_REGISTER_SMS;
			} else {
				for (Country c : countries) {
					if (c.getId() == emailAccess.getCountryId()) {
						map.put("countryCode", c.getCountryCode());
						emailAccess.setCountryCode(c.getCountryCode());
						break;
					}
				}
				link = AppConstants.POST_REGISTER_EMAIL;
			}
			
			Response r = reqs.postSecuredRequest(link, map);
			if (r.getStatus() == 200) {
				this.smsCode = r.readEntity(String.class);
				Helper.addInfoMessage("SMS Sent to customer");
			} else if (r.getStatus() == 409) {
				Helper.addErrorMessage("customer already registered");
			} else {
				Helper.addErrorMessage("An error occured");
			}
		} else {
			Helper.addErrorMessage("password did not Matche");
		}
	}

	public void activateAndRegisterSMS() {
		if (this.smsCode.equals(this.smsCodeUser)) {
			emailAccess.setCreatedBy(this.loginBean.getUserHolder().getUser().getId());
			emailAccess.setAppSecret(AppConstants.APP_SECRET);
			Response r = reqs.postSecuredRequest(AppConstants.POST_MOBILE_REGISTER, emailAccess);
			if (r.getStatus() == 200) {
				init();
				Helper.addInfoMessage("New customer created");
			} else {
				Helper.addErrorMessage("An error occured");
			}

		} else {
			Helper.addErrorMessage("Invalid SMS");
		}
	}

	private void initQuantityArray() {
		quantityArray = new int[20];
		for (int i = 0; i < quantityArray.length; i++) {
			quantityArray[i] = i + 1;
		}
	}

	public EmailAccess getEmailAccess() {
		return emailAccess;
	}

	public void setEmailAccess(EmailAccess emailAccess) {
		this.emailAccess = emailAccess;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customer) {
		this.customers = customer;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getSmsCodeUser() {
		return smsCodeUser;
	}

	public void setSmsCodeUser(String smsCodeUser) {
		this.smsCodeUser = smsCodeUser;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String mobile) {
		this.search = mobile;
	}

	public Customer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(Customer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
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

	public List<Make> getActiveMakes() {
		return activeMakes;
	}

	public void setActiveMakes(List<Make> activeMakes) {
		this.activeMakes = activeMakes;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public int[] getQuantityArray() {
		return quantityArray;
	}

	public void setQuantityArray(int[] quantityArray) {
		this.quantityArray = quantityArray;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public long getLastOrderId() {
		return lastOrderId;
	}

	public void setLastOrderId(long lastOrderId) {
		this.lastOrderId = lastOrderId;
	}

	public List<Long> getInactiveCustomers() {
		return inactiveCustomers;
	}

	public void setInactiveCustomers(List<Long> inactiveCustomers) {
		this.inactiveCustomers = inactiveCustomers;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

}
