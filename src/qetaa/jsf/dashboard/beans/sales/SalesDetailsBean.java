package qetaa.jsf.dashboard.beans.sales;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.SalesInvoiceGenerator;
import qetaa.jsf.dashboard.helpers.SalesReturnInvoiceGenerator;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesProduct;
import qetaa.jsf.dashboard.model.sales.SalesReturn;
import qetaa.jsf.dashboard.model.sales.SalesReturnProduct;

@Named
@ViewScoped
public class SalesDetailsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Sales sales;

	@Inject
	private Requester reqs;

	@PostConstruct
	private void init() {
		try {
			String s = Helper.getParam("id");
			sales = new Sales();
			initSales(s);
			initCart();
			initCartVariables();
		} catch (Exception ex) {
			ex.printStackTrace();
			//Helper.redirect("sales-search");
		}
	}
	
	
	
	public StreamedContent getPdfInvoice() {
		SalesInvoiceGenerator ig = new SalesInvoiceGenerator(sales);
		return new DefaultStreamedContent(ig.getInputStream(), "application/pdf", ig.getIdString() + ".pdf");
	}
	
	public StreamedContent getPdfReturnInvoice(long returnId) {
		SalesReturn salesReturn = null;
		for(SalesReturn sr : this.sales.getSalesReturns()) {
			if(sr.getId() == returnId)
				salesReturn = sr;
		}
		SalesReturnInvoiceGenerator ig = new SalesReturnInvoiceGenerator(sales, salesReturn);
		return new DefaultStreamedContent(ig.getInputStream(), "application/pdf", ig.getIdString() + ".pdf");
	}

	private void initSales(String param) throws Exception {
		Long id = Long.parseLong(param);
		Response r = reqs.getSecuredRequest(AppConstants.getSalesFromId(id));
		if (r.getStatus() == 200) {
			sales = r.readEntity(Sales.class);
		} else {
			throw new Exception();
		}
	}

	private void initCart() throws Exception {
		Response r = reqs.getSecuredRequest(AppConstants.getCart(sales.getCartId()));
		if (r.getStatus() == 200) {
			sales.setCart(r.readEntity(Cart.class));
		} else {
			throw new Exception();
		}
	}

	private void initCartVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[6];
		threads[0] = ThreadRunner.initModelYear(sales.getCart(), header);
		threads[1] = ThreadRunner.initCustomer(sales.getCart(), header);
		threads[2] = ThreadRunner.initAddress(sales.getCart(), header);
		threads[3] = ThreadRunner.initReviews(sales.getCart(), header);
		threads[4] = ThreadRunner.initPromoCode(sales.getCart(), header);
		threads[5] = initProducts(header);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
			}
		}
	}

	private Thread initProducts(String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread[] threads = new Thread[sales.getSalesProducts().size() + sales.getSalesReturnProducts().size()];
					int index = 0;
					for (SalesProduct sp : sales.getSalesProducts()) {
						threads[index] = ThreadRunner.initProduct(sp, header);
						threads[index].start();
						threads[index].join();
						index++;
					}
					
					for(SalesReturnProduct srp : sales.getSalesReturnProducts()) {
						threads[index] = ThreadRunner.initProduct(srp, header);
						threads[index].start();
						threads[index].join();
						index++;
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	public double getTotalSalesProducts() {
		double t = 0;
		for (SalesProduct sp : sales.getSalesProducts()) {
			t = t + sp.getUnitSales() * sp.getQuantity();
		}
		return t;
	}

	@JsonIgnore
	public double getTotalSalesProductsWv() {
		double t = 0;
		for (SalesProduct sp : sales.getSalesProducts()) {
			t = t + sp.getUnitSalesWv() * sp.getQuantity();
		}
		return t;
	}
	
	@JsonIgnore
	public double getTotalVat() {
		return sales.getCart().getVatPercentage() * (getTotalSalesProducts() + sales.getCart().getDeliveryFees());
	}
	
	@JsonIgnore
	public double getPromotionDiscount() {
		if (sales.getCart().getPromoCodeObject() == null) {
			return 0;
		}
		if (!sales.getCart().getPromoCodeObject().isDiscountPromo()) {
			return 0;
		}
		return sales.getCart().getPromoCodeObject().getDiscountPercentage() * (getTotalSalesProducts() + sales.getCart().getDeliveryFees());
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

}
