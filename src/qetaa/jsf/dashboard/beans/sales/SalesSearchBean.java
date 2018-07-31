package qetaa.jsf.dashboard.beans.sales;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

@Named
@ViewScoped
public class SalesSearchBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Sales> foundSales;
	private Sales selectedSales;
	private Date from;
	private Date to;
	private Long cartId;
	private String customerVar;
	private String productVar;
	private char paymentStatus;
	private String promotionString;
	private String courrier;
	private Integer makeId;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		foundSales = new ArrayList<>();
	}

	public List<Long> searchCustomers() {
		if (this.customerVar == null || this.customerVar.equals("")) {
			return new ArrayList<>();
		} else {
			List<Long> list = new ArrayList<>();
			Response r = reqs.getSecuredRequest(AppConstants.searchCustomerAny(customerVar));
			if (r.getStatus() == 200) {
				List<Customer> customers = r.readEntity(new GenericType<List<Customer>>() {
				});
				for (Customer c : customers) {
					list.add(c.getId());
				}

			}
			return list;
		}
	}

	public List<Long> searchProducts() {
		List<Long> list = new ArrayList<>();
		if (this.productVar == null || this.productVar.equals("")) {

		} else {
			Response r = reqs.getSecuredRequest(AppConstants.getProductIdsSearch(this.productVar));
			if (r.getStatus() == 200) {
				list = r.readEntity(new GenericType<List<Long>>() {
				});
			}
			if (list.isEmpty()) {
				list.add(0L);
			}

		}
		return list;
	}


	public Integer searchPromoCodes() {
		PromotionCode pcode = new PromotionCode();
		if (this.promotionString == null || this.promotionString.equals("")) {
			return null;
		} else {
			Response r = reqs.getSecuredRequest(AppConstants.getPromotionCodeFromCode(this.promotionString));
			if (r.getStatus() == 200) {
				pcode = r.readEntity(PromotionCode.class);
				return pcode.getId();
			} else {
				return 0;
			}
		}
	}

	public void search() {
		List<Long> customers = searchCustomers();
		List<Long> products = searchProducts();
		Integer promoCode = searchPromoCodes();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productIds", products);
		map.put("cartId", this.cartId);
		map.put("makeId", this.makeId);
		map.put("customerIds", customers);
		map.put("promoCode", promoCode);
		map.put("courrier", courrier);
		if (from == null) {
			map.put("from", 0);
		} else {
			map.put("from", from.getTime());
		}

		if (to == null) {
			map.put("to", 0);
		} else {
			map.put("to", to.getTime());
		}
		
		Response r = reqs.postSecuredRequest(AppConstants.POST_SALES_SEARCH, map);
		if (r.getStatus() == 200) {
			this.foundSales = r.readEntity(new GenericType<List<Sales>>() {
			});
			initCartVariables();
		} else {
			System.out.println(r.getStatus());
			Helper.addErrorMessage("An error occured");
		}
	}
	
	private List<Customer> initAllCustomers() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_ALL_CUSTOMERS_FROM_IDS, Helper.getCustomerIdsFromSales(foundSales));
		if(r.getStatus() == 200) {
			List<Customer> l = r.readEntity(new GenericType<List<Customer>>() {});
			return l;
		}
		else {
			return new ArrayList<>();
		}
	}
	
	private void initCartVariables() {
		List<Customer> allCustomers = initAllCustomers();
		Thread[] mainThreads = new Thread[foundSales.size()];
		int index = 0;
		for (Sales sales : foundSales) {
			sales.setCart(new Cart());
			sales.getCart().setCustomerId(sales.getCustomerId());
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Thread[] threads = new Thread[1];
					threads[0] = initCustomer(allCustomers, sales.getCart());
					for (int i = 0; i < threads.length; i++)
						try {
							threads[i].start();
							threads[i].join();
						} catch (InterruptedException e) {
							e.printStackTrace();	
						}
				}
			});
			mainThreads[index] = t;
			index++;
		}
		for (int i = 0; i < mainThreads.length; i++) {
			try {
				mainThreads[i].start();
				mainThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	private Thread initCustomer(List<Customer> customers, Cart cart) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (Customer c : customers) {
						if (c.getId() == cart.getCustomerId()) {
							cart.setCustomer(c);
							break;
						}
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}

	public List<Sales> getFoundSales() {
		return foundSales;
	}

	public void setFoundSales(List<Sales> foundSales) {
		this.foundSales = foundSales;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public String getCustomerVar() {
		return customerVar;
	}

	public void setCustomerVar(String customerVar) {
		this.customerVar = customerVar;
	}

	public String getProductVar() {
		return productVar;
	}

	public void setProductVar(String productVar) {
		this.productVar = productVar;
	}

	public char getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(char paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPromotionString() {
		return promotionString;
	}

	public void setPromotionString(String promotionString) {
		this.promotionString = promotionString;
	}

	public String getCourrier() {
		return courrier;
	}

	public void setCourrier(String courrier) {
		this.courrier = courrier;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Integer getMakeId() {
		return makeId;
	}

	public void setMakeId(Integer makeId) {
		this.makeId = makeId;
	}

	public Sales getSelectedSales() {
		return selectedSales;
	}

	public void setSelectedSales(Sales selectedSales) {
		this.selectedSales = selectedSales;
	}
	
	

}
