package qetaa.jsf.dashboard.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import qetaa.jsf.dashboard.beans.master.RegionsBean;
import qetaa.jsf.dashboard.model.location.Region;

@Named
@ApplicationScoped
public class RegionConverter implements Converter {

	@Inject
	private RegionsBean regionBean;  

	@Override
	public Object getAsObject(FacesContext context, UIComponent uic, String value) {
		try {
			if (value != null && value.trim().length() > 0) {
				Integer id = Integer.parseInt(value);
				try {
					Region r = new Region();
					for (Region region: regionBean.getRegions()) {
						if (region.getId() == id) {
							r = region;
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
			if (object instanceof Region) {
				return String.valueOf(((Region) object).getId());
			}
			return object.toString();
		} else {
			return "";
		}
	}

}
