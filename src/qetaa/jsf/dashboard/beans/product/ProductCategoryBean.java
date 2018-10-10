package qetaa.jsf.dashboard.beans.product;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.model.product.Category;

@Named
@ViewScoped
public class ProductCategoryBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Category category;
	private List<Category> categories;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		categories = new ArrayList<>();
		category = new Category();
		initCategories();
	}

	public void createCategory() {
		try {
			category.setName(category.getName().trim());
			category.setNameAr(category.getNameAr().trim());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("category", category);
			String main64 = Helper.inputStreamToBase64(category.getMain().getInputStream());
			System.out.println(main64.length());
			map.put("main", main64);
			map.put("thumbnail", Helper.inputStreamToBase64(category.getThumb().getInputStream()));
			Response r = reqs.postSecuredRequest(AppConstants.POST_PRODUCT_CATEGORY, map);
			if (r.getStatus() == 201) {
				Helper.addInfoMessage("Category created");
				init();
			}
		} catch (IOException e) {
			Helper.addErrorMessage("An error occured");
		}
	}

	private void initCategories() {
		Response r = reqs.getSecuredRequest(AppConstants.GET_PRODUCT_CATEGORIES);
		if (r.getStatus() == 200) {
			this.categories = r.readEntity(new GenericType<List<Category>>() {
			});
		}
	}
	
	public Category getCategoryFromId(int id) {
		for(Category cat : categories) {
			if(cat.getId() == id) {
				return cat;
			}
		}
		return null;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

}
