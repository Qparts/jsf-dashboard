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
import qetaa.jsf.dashboard.model.sales.Sales;
import qetaa.jsf.dashboard.model.sales.SalesProduct;

public class SalesInvoiceGenerator {
	private static final String LOGO = "/resources/images/logo-qetaa.jpg";
	private static final String JASPER_FILE = "/WEB-INF/jasper/salesInvoice.jasper";
	private static final String VAT_NUMBER = "310229170200003";
	
	private Sales sales;
	private InputStream jasperIS;
	private String imagePath;
	private byte[] invoiceBytes;
	private Map<String, Object> map;
	private String idString;
	private JRDataSource dataSource;
	
	
	public SalesInvoiceGenerator(Sales sales) {
		try {
			this.sales = sales;
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
	
	
	private void addParameters() {
		Helper h = new Helper();
		idString = h.getDateFormat(sales.getSalesDate(), "yyyyMMdd") +sales.getId();
		map = new HashMap<>();
		map.put("IMAGE_PATH", imagePath);
		map.put("ID", idString);
		map.put("SALES_DATE", h.getDateFormat(sales.getSalesDate(), "dd-MMM-yyyy HH:mm"));
		map.put("INVOICE_TYPE", sales.getTransactionTypeEn() + " - " + sales.getTransactionTypeAr());
		map.put("ORDER_ID", sales.getCartId());
		map.put("VAT_NUMBER", VAT_NUMBER);
		map.put("PAYMENT_METHOD", sales.getPaymentMethodEn() + " - " + sales.getPaymentMethodAr());
		map.put("CUSTOMER_NAME", sales.getCart().getCustomer().getFullName());
		map.put("Email", sales.getCart().getCustomer().getEmail());		
		map.put("MOBILE", sales.getCart().getCustomer().getMobile());
		map.put("ADDRESS1", sales.getCart().getAddress().getLine1());
		map.put("ADDRESS2", sales.getCart().getAddress().getLine2());
		map.put("ADDRESS_REGION", sales.getCart().getAddress().getCity().getRegion().getName() + " - " + sales.getCart().getAddress().getCity().getRegion().getNameAr());
		map.put("ADDRESS_CITY", sales.getCart().getAddress().getCity().getName() + " - " + sales.getCart().getAddress().getCity().getNameAr());
		map.put("ADDRESS_COUNTRY", sales.getCart().getAddress().getCity().getCountry().getName() + " - " + sales.getCart().getAddress().getCity().getCountry().getNameAr());
		map.put("SUB_TOTAL", getRounded(sales.getTotalPartsAmount()));
		map.put("DELIVERY_FEES", getRounded(sales.getDeliveryFees()));
		map.put("VAT", getRounded(sales.getTotalSales() * sales.getVatPercentage()));
		map.put("DISCOUNTS", getRounded(sales.getPromotionDiscount()));
		map.put("TOTAL", getRounded(sales.getTotalPartsWvAmount() + sales.getTotalDeliveryFees() - sales.getPromotionDiscount()));
	}
	
	private void loadSaleItems() {
		dataSource = new JRBeanCollectionDataSource(getSalesItems());
	}

	public void jasperGenerator() {		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperIS, map, dataSource);
			invoiceBytes = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static HttpSession getHttpSession(boolean createFlag) {
		FacesContext fc = FacesContext.getCurrentInstance();
		return (HttpSession) fc.getExternalContext().getSession(createFlag);
	}

	
	
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.invoiceBytes);
	}

	private String getRounded(double num) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		return formatter.format(num).replaceAll("\\$", "SR ");
	}

	private List<SalesItem> getSalesItems() {
		List<SalesItem> salesItems = new ArrayList<>();
		for (SalesProduct sp : sales.getSalesProducts()) {
			SalesItem si = new SalesItem();
			si.setItemName(sp.getProduct().getProductNameCheck());
			si.setItemNumber(sp.getProduct().getProductNumber());
			si.setPrice(sp.getUnitSales());
			si.setPriceWithVat(sp.getUnitSalesWv());
			si.setQuantity(sp.getQuantity());
			salesItems.add(si);
		}
		return salesItems;
	}
	

	public class SalesItem {
		
		public SalesItem() {
			
		}
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
