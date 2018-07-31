package qetaa.jsf.dashboard.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import qetaa.jsf.dashboard.model.cart.Cart;
import qetaa.jsf.dashboard.model.payment.Wallet;
import qetaa.jsf.dashboard.model.purchase.Purchase;
import qetaa.jsf.dashboard.model.sales.Sales;

public class Helper {
	private static String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxuz1234567890";
	
	public static List<Integer> getYearsRange(int from, int to){
		List<Integer> ints = new ArrayList<Integer>();
		for(int i = from; i <= to; i++) {
			ints.add(i);
		}
		return ints;
	}
	
	public String getDateFormat(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	
	public static double deductAddedPercentage(double orig, double percentage) {
		double x = orig / (1.0 + percentage);
		return x;
	}
	
	public static long[] getCustomerIds(List<Cart> carts) {
		long[] ids = new long[carts.size()];
		for(int i = 0; i < carts.size(); i++) {
			ids[i] = carts.get(i).getCustomerId();
		}
		return ids;
	}
	
	public static long[] getCustomerIdsFromWallet(List<Wallet> wallets) {
		long[] ids = new long[wallets.size()];
		for(int i = 0; i < wallets.size(); i++) {
			ids[i] = wallets.get(i).getCustomerId();
		}
		return ids;
	}
	
	public static long[] getCustomerIdsFromSales(List<Sales> sales) {
		long[] ids = new long[sales.size()];
		for(int i = 0; i < sales.size(); i++) {
			ids[i] = sales.get(i).getCustomerId();
		}
		return ids;
	}
	
	public static long[] getCustomerIdsFromPurchase(List<Purchase> purchase) {
		long[] ids = new long[purchase.size()];
		for(int i = 0; i < purchase.size(); i++) {
			ids[i] = purchase.get(i).getCustomerId();
		}
		return ids;
	}

	public static void redirect(String path) {
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().redirect(path);
			return;
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
    }
	
	public static Integer paymentIntegerFormat(double am){
		return Double.valueOf(am * 100).intValue();
	}
	
	public static String getParam(String key){
		FacesContext context = FacesContext.getCurrentInstance();
        String code = context.getExternalContext().getRequestParameterMap().get(key);
        return code;
	}
	
	public static void addInfoMessage(String text) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, text, null));
	}

	public static void addErrorMessage(String text) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, text, null));
	}
	
	public static void addErrorMessage(String text, String clientId) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, text, null));
	}
	
	public static String getRandomSaltString(){
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
		return salt.toString();
	}
	
	
	public static String cypherSha256(String text) {
		try{
		String shaval = "";
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");

		byte[] defaultBytes = text.getBytes();

		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		StringBuilder hexString = new StringBuilder();

		for (int i = 0; i < messageDigest.length; i++) {
			String hex = Integer.toHexString(0xFF & messageDigest[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		shaval = hexString.toString();

		return shaval;
		}
		catch(NoSuchAlgorithmException na){
			return text;
		}
	}
}
