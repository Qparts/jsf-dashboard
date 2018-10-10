package qetaa.jsf.dashboard.helpers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import qetaa.jsf.dashboard.model.payment.Wallet;
import qetaa.jsf.dashboard.model.payment.WalletItem;
import qetaa.jsf.dashboard.model.payment.WalletQuotation;

public class QuotationGenerator {
	private static final String LOGO = "/resources/images/logo-qetaa.jpg";
	private static final String JASPER_FILE = "/WEB-INF/jasper/quotation.jasper";
	private static final String VAT_NUMBER = "310229170200003";
	
	private Wallet wallet;
	private WalletQuotation walletQuotation;
	private InputStream jasperIS;
	private String imagePath;
	private byte[] invoiceBytes;
	private Map<String, Object> map;
	private String idString;
	private JRDataSource dataSource;
	
	public QuotationGenerator(Wallet wallet, WalletQuotation wq) {
		try {
			this.wallet = wallet;
			this.walletQuotation = wq;
			loadFiles();
			addParameters();
			loadSaleItems();
			jasperGenerator();

		} catch (NullPointerException ex) {
		} catch (Exception e) {
		}
	}
	
	private void loadFiles() {
		try {
			FacesContext ctx = FacesContext.getCurrentInstance();
			jasperIS = ctx.getExternalContext().getResourceAsStream(JASPER_FILE);
			imagePath = getHttpSession(true).getServletContext().getRealPath(LOGO);
		} catch (Exception ex) {

		}
	}
	
	public static HttpSession getHttpSession(boolean createFlag) {
		FacesContext fc = FacesContext.getCurrentInstance();
		return (HttpSession) fc.getExternalContext().getSession(createFlag);
	}
	
	private void addParameters() {
		Helper h = new Helper();
		idString = h.getDateFormat(walletQuotation.getCreated(), "yyyyMMdd") +walletQuotation.getId();
		map = new HashMap<>();
		map.put("IMAGE_PATH", imagePath);
		map.put("ID", idString);
		map.put("SALES_DATE", h.getDateFormat(walletQuotation.getCreated(), "dd-MMM-yyyy HH:mm"));
		map.put("INVOICE_TYPE", " Quotation - تسعيرة");
		map.put("ORDER_ID", wallet.getCartId());
		map.put("VAT_NUMBER", VAT_NUMBER);
		//map.put("PAYMENT_METHOD", sales.getPaymentMethodEn() + " - " + sales.getPaymentMethodAr());
		map.put("CUSTOMER_NAME", wallet.getCart().getCustomer().getFullName());
		map.put("Email", wallet.getCart().getCustomer().getEmail());
		map.put("MOBILE", wallet.getCart().getCustomer().getMobile());
		map.put("ADDRESS1", wallet.getCart().getAddress().getLine1());
		map.put("ADDRESS2", wallet.getCart().getAddress().getLine2());
		map.put("ADDRESS_REGION", wallet.getCart().getAddress().getCity().getRegion().getName() + " - " + wallet.getCart().getAddress().getCity().getRegion().getNameAr());
		map.put("ADDRESS_CITY", wallet.getCart().getAddress().getCity().getName() + " - " + wallet.getCart().getAddress().getCity().getNameAr());
		map.put("ADDRESS_COUNTRY", wallet.getCart().getAddress().getCity().getCountry().getName() + " - " + wallet.getCart().getAddress().getCity().getCountry().getNameAr());
		map.put("SUB_TOTAL", getRounded(getTotalQuotationItems()));
		map.put("DELIVERY_FEES", getRounded(getDeliveryFees()));
		map.put("VAT", getRounded((getTotalQuotationItems() + getDeliveryFees()) * wallet.getCart().getVatPercentage()));
		map.put("DISCOUNTS", getRounded(getTotalDiscount()));
		map.put("TOTAL", getRounded(getTotalQuotationItemsWv() + getDeliveryFeesWv() - getTotalDiscount()));
	}
	
	private double getTotalQuotationItems() {
		return walletQuotation.getTotalQuotationItemsAmount(wallet.getWalletItems());
	}
	
	private double getTotalQuotationItemsWv() {
		double sub = walletQuotation.getTotalQuotationItemsAmount(wallet.getWalletItems());
		return sub + sub*wallet.getCart().getVatPercentage();
	}
	
	private double getTotalDiscount() {
		return getTotalQuotationItems() * wallet.getDiscountPercentage();
	}
	
	private double getDeliveryFeesWv() {
		return getDeliveryFees() + getDeliveryFees()* wallet.getCart().getVatPercentage();
	}
	
	private double getDeliveryFees() {
		for(WalletItem wi : wallet.getWalletItems()) {
			if(wi.getItemType() == 'D') {
				if(wi.getStatus() != 'R') {
					return wi.getUnitSales();
				}
			}
		}
		return 0;
	}
	
	private void loadSaleItems() {
		dataSource = new JRBeanCollectionDataSource(getSalesItems());
	}
	
	private List<SalesItem> getSalesItems() {
		List<SalesItem> salesItems = new ArrayList<>();
		for (WalletItem wi : walletQuotation.getAvailableWalletItems(wallet.getWalletItems())) {
			SalesItem si = new SalesItem();
			si.setItemName(wi.getProduct().getProductNameCheck());
			si.setItemNumber(wi.getProduct().getProductNumber());
			si.setPrice(wi.getUnitSales());
			si.setPriceWithVat(wi.getUnitSalesWv());
			si.setQuantity(wi.getQuantity());
			salesItems.add(si);
		}
		return salesItems;
	}
	
	public void jasperGenerator() {		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperIS, map, dataSource);
			invoiceBytes = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.invoiceBytes);
	}
	
	private String getRounded(double num) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		return formatter.format(num).replaceAll("\\$", "SR ");
	}
	
	public class SalesItem {
		
		private String itemNumber;
		private String itemName;
		private double price;
		private double priceWithVat;
		private int quantity;

		public String getItemNumber() {
			return itemNumber;
		}

		public void setItemNumber(String itemNumber) {
			this.itemNumber = itemNumber;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public double getPriceWithVat() {
			return priceWithVat;
		}

		public void setPriceWithVat(double priceWithVat) {
			this.priceWithVat = priceWithVat;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
	}
	
	public String getIdString() {
		return idString;
	}
}
