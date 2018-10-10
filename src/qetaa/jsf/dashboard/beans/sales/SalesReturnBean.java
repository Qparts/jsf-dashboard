package qetaa.jsf.dashboard.beans.sales;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.beans.LoginBean;
import qetaa.jsf.dashboard.beans.Requester;
import qetaa.jsf.dashboard.beans.shipments.CourierBean;
import qetaa.jsf.dashboard.helpers.AppConstants;
import qetaa.jsf.dashboard.helpers.Helper;
import qetaa.jsf.dashboard.helpers.ThreadRunner;
import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesPayment;
import qetaa.jsf.dashboard.model.sales.SalesProduct;
import qetaa.jsf.dashboard.model.sales.SalesReturn;
import qetaa.jsf.dashboard.model.sales.SalesReturnProduct;
import qetaa.jsf.dashboard.model.sales.SalesReturnWallet;
import qetaa.jsf.dashboard.model.shipment.Courier;
import qetaa.jsf.dashboard.model.shipment.Shipment;
import qetaa.jsf.dashboard.model.shipment.ShipmentItem;

@Named
@ViewScoped
public class SalesReturnBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Sales sales;
	private SalesProduct selectedSalesProduct;
	private SalesReturn salesReturn;
	private SalesReturnProduct salesReturnProduct;
	private Shipment shipment;
	private boolean returnDelivery;
	private int courierId;

	@Inject
	private Requester reqs;

	@Inject
	private CourierBean courierBean;

	@Inject
	private LoginBean loginBean;

	@PostConstruct
	private void init() {
		try {
			shipment = new Shipment();
			returnDelivery = false;
			String s = Helper.getParam("id");
			initSales(s);
			initCart(sales.getCartId());
			initCartVariables();
			initProducts();
			salesReturn = new SalesReturn();
			salesReturn.setReturnedDeliveryFees(0D);
			salesReturn.setVatPercentage(sales.getCart().getVatPercentage());
			salesReturn.setSalesReturnProducts(new ArrayList<>());
			selectedSalesProduct = new SalesProduct();
			salesReturnProduct = new SalesReturnProduct();
		} catch (Exception ex) {
			ex.printStackTrace();
			Helper.redirect("sales-return-search");
		}
	}

	private void initShipment() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customerId", this.shipment.getCustomerId());
		map.put("addressId", this.shipment.getAddressId());
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_SHIPMENT, map);
		if (r.getStatus() == 200) {
			Long shId = r.readEntity(Long.class);
			shipment.setId(shId);
			shipment.setAddressId(this.getSales().getCart().getAddress().getAddressId());
			shipment.setCreated(new Date());
			shipment.setCreatedBy(loginBean.getUserHolder().getUser().getId());
			shipment.setCustomerId(this.sales.getCustomerId());
			shipment.setStatus('S');
			shipment.setBound('I');// Inbound
			initCourierTrackable();
			initShipmentItems();
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_SHIPMENT, shipment);
			if(r2.getStatus() == 201) {
				Helper.addInfoMessage("Shipped");
			}
			else {
				Helper.addErrorMessage("An error occured");
			}
		}
	}

	private void initCourierTrackable() {
		for (Courier c : this.courierBean.getCouriers()) {
			if (c.getId() == this.shipment.getCourierId()) {
				shipment.setTrackable(c.isTrackable());
			}
		}
	}

	private void initShipmentItems() {
		shipment.setShipmentItems(new ArrayList<>());
		for (SalesReturnProduct wi : this.salesReturn.getSalesReturnProducts()) {
			ShipmentItem si = new ShipmentItem();
			si.setQuantity(wi.getQuantity());
			si.setShipmentId(shipment.getId());
			si.setShippedBy(loginBean.getUserHolder().getUser().getId());
			si.setWalletItemId(wi.getPurchaseProduct().getWalletItemId());
			shipment.getShipmentItems().add(si);
		}
	}

	public void createSalesReturn() {
		Response r = reqs.postSecuredRequest(AppConstants.POST_NEW_SALES_RETURN, "");
		if (r.getStatus() == 200) {
			Long id = (r.readEntity(Long.class));
			salesReturn.setId(id);
			salesReturn.setCartId(sales.getCartId());
			salesReturn.setMethod('R');
			salesReturn.setPromotionDiscount(
					salesReturn.getTotalPromotionDiscountDeductionNewQuantity(this.sales.getCart()));
			salesReturn.setReturnBy(loginBean.getUserHolder().getUser().getId());
			salesReturn.setPromotionId(sales.getPromotionId());
			salesReturn.setSales(sales);

			for (SalesReturnProduct srp : salesReturn.getSalesReturnProducts()) {
				srp.setQuantity(srp.getNewQuantity());
				srp.setSalesReturn(null);
			}
			initSalesPayment();
			
			SalesReturnWallet srw = new SalesReturnWallet();
			srw.setBankId(this.getSalesReturn().getBankId());
			srw.setCustomerId(this.getSales().getCart().getCustomerId());
			srw.setCustomerName(this.getSales().getCart().getCustomer().getFullName());
			srw.setDiscountPercentage(this.getDiscountPercentage());
			srw.setSalesReturn(this.salesReturn);
			Response r2 = reqs.putSecuredRequest(AppConstants.PUT_NEW_SALES_RETURN, srw);
			if (r2.getStatus() == 201) {
				initShipment();
				Helper.redirect("sales-return?id=" + this.sales.getId());

			} else {
				Helper.addErrorMessage("could not update sales");
			}
		}
	}

	private double getDiscountPercentage() {
		if (this.getSales().getCart().getPromoCodeObject() != null) {
			if (this.getSales().getCart().getPromoCodeObject().isDiscountPromo()) {
				return this.getSales().getCart().getPromoCodeObject().getDiscountPercentage();
			}
		}
		return 0;
	}

	private void initSalesPayment() {
		SalesPayment sp = new SalesPayment();
		sp.setAmount(salesReturn.getNetSalesReturn() * -1);// negative, amount goes back to customers
		sp.setBankId(salesReturn.getBankId());
		sp.setMethod('R');
		sp.setPaymentDate(new Date());
		sp.setPaymentRef(salesReturn.getShipmentReference());
		sp.setSales(null);
		salesReturn.setSalesPayments(new ArrayList<>());
		salesReturn.getSalesPayments().add(sp);

	}

	public void chooseSalesProduct(SalesProduct sp) {
		this.salesReturnProduct = new SalesReturnProduct();
		this.salesReturnProduct.setProductId(sp.getProductId());
		this.salesReturnProduct.setPurchaseProduct(sp.getPurchaseProduct());
		this.salesReturnProduct.setQuantity(sp.getQuantity());
		this.salesReturnProduct.setNewQuantity(sp.getQuantity());
		this.salesReturnProduct.setReturnDeductionFees(0);
		this.salesReturnProduct.setUnitCost(sp.getUnitCost());
		this.salesReturnProduct.setUnitCostWv(sp.getUnitCostWv());
		this.salesReturnProduct.setUnitSales(sp.getUnitSales());
		this.salesReturnProduct.setUnitSalesWv(sp.getUnitSalesWv());
		this.salesReturnProduct.setProduct(sp.getProduct());
		this.selectedSalesProduct = sp;
	}

	public boolean isItemsAvailableForReturn() {
		boolean found = false;
		for (SalesProduct sp : this.sales.getSalesProducts()) {
			if (getTotalQuantityReturnedInPreviousOrders(sp) < sp.getQuantity()) {
				found = true;
				break;
			}
		}
		return found;
	}

	public int getTotalQuantityReturnedInPreviousOrders(SalesProduct salesProduct) {
		int total = 0;
		for (SalesReturn sr : this.sales.getSalesReturns()) {
			for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
				if (srp.getProductId() == salesProduct.getProductId()
						&& srp.getPurchaseProduct().getId() == salesProduct.getPurchaseProduct().getId())
					total = total + srp.getQuantity();
			}
		}
		return total;
	}

	public int getTotalQuantityReturnedInPreviousOrders(SalesReturnProduct salesReturnProduct) {
		int total = 0;
		for (SalesReturn sr : this.sales.getSalesReturns()) {
			for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
				if (srp.getProductId() == salesReturnProduct.getProductId()
						&& srp.getPurchaseProduct().getId() == salesReturnProduct.getPurchaseProduct().getId())
					total = total + srp.getQuantity();
			}
		}
		return total;
	}

	public void addToReturnProducts() {
		boolean found = false;
		for (SalesReturnProduct srp : this.salesReturn.getSalesReturnProducts()) {
			if (srp.getProduct().getId() == this.salesReturnProduct.getProduct().getId()
					&& srp.getPurchaseProduct().getId() == salesReturnProduct.getPurchaseProduct().getId()) {
				found = true;
				break;
			}
		}
		// check previous returns
		if (!found) {
			for (SalesReturn sr : this.sales.getSalesReturns()) {
				for (SalesReturnProduct srp : sr.getSalesReturnProducts()) {
					if (this.salesReturnProduct.getPurchaseProduct().getId() == srp.getPurchaseProduct().getId()) {
						if (salesReturnProduct.getNewQuantity()
								+ getTotalQuantityReturnedInPreviousOrders(srp) > salesReturnProduct.getQuantity())
							found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}

		if (!found) {
			SalesReturnProduct srp = new SalesReturnProduct();
			srp.setProduct(salesReturnProduct.getProduct());
			srp.setProductId(salesReturnProduct.getProductId());
			srp.setPurchaseProduct(salesReturnProduct.getPurchaseProduct());
			srp.setQuantity(salesReturnProduct.getQuantity());
			srp.setReturnDeductionFees(salesReturnProduct.getReturnDeductionFees());
			srp.setUnitCost(salesReturnProduct.getUnitCost());
			srp.setNewQuantity(salesReturnProduct.getNewQuantity());
			srp.setUnitCostWv(salesReturnProduct.getUnitCostWv());
			srp.setUnitSales(salesReturnProduct.getUnitSales());
			srp.setUnitSalesWv(salesReturnProduct.getUnitSalesWv());
			this.salesReturn.getSalesReturnProducts().add(srp);
		} else {
			Helper.addErrorMessage("Product already added or returned");
		}
	}

	private void initSales(String s) throws Exception {
		Long id = Long.parseLong(s);
		Response r = reqs.getSecuredRequest(AppConstants.getSalesFromId(id));
		if (r.getStatus() == 200) {
			sales = r.readEntity(Sales.class);
		} else {
			throw new Exception();
		}
	}

	private void initCart(long cartId) {
		Response r = reqs.getSecuredRequest(AppConstants.getCart(cartId));
		if (r.getStatus() == 200) {
			sales.setCart(r.readEntity(Cart.class));
		}
	}

	private void initProducts() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[sales.getSalesProducts().size()];
		int index = 0;
		for (SalesProduct sp : sales.getSalesProducts()) {
			sp.setProduct(new Product());
			threads[index] = ThreadRunner.initProduct(sp, header);
			index++;
		}
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void initCartVariables() {
		String header = reqs.getSecurityHeader();
		Thread[] threads = new Thread[4];
		threads[0] = ThreadRunner.initReviews(sales.getCart(), header);
		threads[1] = ThreadRunner.initCity(sales.getCart(), header);
		threads[2] = ThreadRunner.initPromoCode(sales.getCart(), header);
		threads[3] = ThreadRunner.initAddress(sales.getCart(), header);
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void chooseReturnDelivery() {
		if (this.returnDelivery) {
			this.salesReturn.setReturnedDeliveryFees(this.getSales().getDeliveryFees());
		} else {
			this.salesReturn.setReturnedDeliveryFees(0D);
		}
	}

	public Sales getSales() {
		return sales;
	}

	public void setSales(Sales sales) {
		this.sales = sales;
	}

	public SalesProduct getSelectedSalesProduct() {
		return selectedSalesProduct;
	}

	public void setSelectedSalesProduct(SalesProduct selectedSalesProduct) {
		this.selectedSalesProduct = selectedSalesProduct;
	}

	public SalesReturn getSalesReturn() {
		return salesReturn;
	}

	public void setSalesReturn(SalesReturn salesReturn) {
		this.salesReturn = salesReturn;
	}

	public SalesReturnProduct getSalesReturnProduct() {
		return salesReturnProduct;
	}

	public void setSalesReturnProduct(SalesReturnProduct salesReturnProduct) {
		this.salesReturnProduct = salesReturnProduct;
	}

	public boolean isReturnDelivery() {
		return returnDelivery;
	}

	public void setReturnDelivery(boolean returnDelivery) {
		this.returnDelivery = returnDelivery;
	}

	public int getCourierId() {
		return courierId;
	}

	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

}
