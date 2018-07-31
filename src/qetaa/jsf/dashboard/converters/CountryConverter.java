package qetaa.jsf.dashboard.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import qetaa.jsf.dashboard.beans.master.CountriesBean;
import qetaa.jsf.dashboard.model.location.Country;

@Named
@ApplicationScoped
public class CountryConverter implements Converter {

	@Inject
	private CountriesBean countryBean;  

	@Override
	public Object getAsObject(FacesContext context, UIComponent uic, String value) {
		try {
			if (value != null && value.trim().length() > 0) {
				Integer id = Integer.parseInt(value);
				try {
					Country r = new Country();
					for (Country country: countryBean.getCountries()) {
						if (country.getId() == id) {
							r = country;
							break;
						}
					}
					return r;
				} catch (NumberFormatException e) {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uic, Object object) {
		if (object != null && object != "") {
			if (object instanceof Country) {
				return String.valueOf(((Country) object).getId());
			}
			return object.toString();
		} else {
			return "";
		}
	}

}
