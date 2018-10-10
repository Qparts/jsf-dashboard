package qetaa.jsf.dashboard.helpers;

public class WebsocketLinks {

	//private static final String BASE = "ws://localhost:8081/";
	private static final String BASE = "wss://www.qetaa.com/";
	private static final String CART_SERVICE_WS = BASE + "service-qetaa-cart/ws/";
	
	
	
	
	
	public static final String getQuotingLink(Integer username, String token) {
		return CART_SERVICE_WS + "quoting/user/"+username+"/token/" + token;
	}
	
	public static final String getQuotationsLink(Integer username, String token) {
		return CART_SERVICE_WS + "quotations/user/"+username+"/token/" + token;
	}
	
	public static final String getNotificationsLink(Integer username, String token) {
		return CART_SERVICE_WS + "notifications/user/"+username+"/token/" + token;
	}
}
