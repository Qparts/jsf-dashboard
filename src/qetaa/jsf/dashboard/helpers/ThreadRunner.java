package qetaa.jsf.dashboard.helpers;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.cart.CartReview;
import qetaa.jsf.dashboard.model.customer.Customer;
import qetaa.jsf.dashboard.model.customer.CustomerAddress;
import qetaa.jsf.dashboard.model.location.City;
import qetaa.jsf.dashboard.model.payment.Wallet;
import qetaa.jsf.dashboard.model.payment.WalletItem;
import qetaa.jsf.dashboard.model.product.Product;
import qetaa.jsf.dashboard.model.purchase.PurchaseProduct;
import qetaa.jsf.dashboard.model.purchase.PurchaseReturnProduct;
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesProduct;
import qetaa.jsf.dashboard.model.sales.SalesReturnProduct;
import qetaa.jsf.dashboard.model.vehicle.ModelYear;
import qetaa.jsf.dashboard.model.vendor.PromotionCode;

public class ThreadRunner {

	public static Thread initCustomer(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCustomer(cart.getCustomerId()),
							header);
					if (r.getStatus() == 200) {
						cart.setCustomer(r.readEntity(Customer.class));
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	public static Thread initSalesCart(Sales sales, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCart(sales.getCartId()),
							header);
					if (r.getStatus() == 200) {
						sales.setCart(r.readEntity(Cart.class));
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	public static Thread initWalletCart(Wallet wallet, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCart(wallet.getCartId()),
							header);
					if (r.getStatus() == 200) {
						wallet.setCart(r.readEntity(Cart.class));
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	public static Thread initPromoCode(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (cart.getPromotionCode() != null) {
						Response r = PojoRequester.getSecuredRequest(AppConstants.getPromoCode(cart.getPromotionCode()),
								header);
						if (r.getStatus() == 200) {
							cart.setPromoCodeObject(r.readEntity(PromotionCode.class));
						}
					}else {
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	public static Thread initAddress(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartAddress(cart.getId()), header);
					if (r.getStatus() == 200) {
						cart.setAddress(r.readEntity(CustomerAddress.class));
						try {
							Response r2 = PojoRequester
									.getSecuredRequest(AppConstants.getCity(cart.getAddress().getCityId()), header);
							if (r2.getStatus() == 200) {
								cart.getAddress().setCity(r2.readEntity(City.class));
							}

						} catch (Exception ex) {

						}
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	public static Thread initProduct(SalesProduct sp , String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester
							.getSecuredRequest(AppConstants.getProduct(sp.getProductId()), header);
					if(r.getStatus() == 200) {
						sp.setProduct(r.readEntity(Product.class));
					}
				}
				catch(Exception ex) {
				}
				
			}
		});
		return thread;
	}
	
	
	public static Thread initProduct(WalletItem wi , Long cartId, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester
							.getSecuredRequest(AppConstants.getProductForCart(wi.getProductId(), cartId), header);
					if(r.getStatus() == 200) {
						wi.setProduct(r.readEntity(Product.class));
					}
				}
				catch(Exception ex) {
				}
				
			}
		});
		return thread;
	}
	
	public static Thread initProduct(PurchaseProduct sp , String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester
							.getSecuredRequest(AppConstants.getProduct(sp.getProductId()), header);
					if(r.getStatus() == 200) {
						sp.setProduct(r.readEntity(Product.class));
					}
				}
				catch(Exception ex) {
				}
				
			}
		});
		return thread;
	}
	
	public static Thread initProduct(SalesReturnProduct sp , String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester
							.getSecuredRequest(AppConstants.getProduct(sp.getProductId()), header);
					if(r.getStatus() == 200) {
						sp.setProduct(r.readEntity(Product.class));
					}
				}
				catch(Exception ex) {
				}
				
			}
		});
		return thread;
	}
	
	public static Thread initProduct(PurchaseReturnProduct sp , String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester
							.getSecuredRequest(AppConstants.getProduct(sp.getProductId()), header);
					if(r.getStatus() == 200) {
						sp.setProduct(r.readEntity(Product.class));
					}
				}
				catch(Exception ex) {
				}
				
			}
		});
		return thread;
	}
	
	public static Thread initReviews(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCartReviews(cart.getId()), header);
					if (r.getStatus() == 200) {
						List<CartReview> reviews = r.readEntity(new GenericType<List<CartReview>>() {
						});
						cart.setReviews(reviews);
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	
	public static Thread initCity(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getCity(cart.getCityId()), header);
					if (r.getStatus() == 200) {
						cart.setCity(r.readEntity(City.class));
					}

				} catch (Exception ex) {

				}
			}
		});
		return thread;
	}
	
	public static Thread initModelYear(Cart cart, String header) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Response r = PojoRequester.getSecuredRequest(AppConstants.getModelYear(cart.getVehicleYear()),
							header);
					if (r.getStatus() == 200) {
						cart.setModelYear(r.readEntity(ModelYear.class));
					}
				} catch (Exception ex) {
				}
			}
		});
		return thread;
	}
	
}
