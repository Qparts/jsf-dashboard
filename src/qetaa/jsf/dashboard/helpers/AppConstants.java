package qetaa.jsf.dashboard.helpers;

public class AppConstants {

	public final static String GIFTS_DIRECTORY_WINDOWS = "C:\\loyalty-gifts-pics\\";
	public final static String GIFTS_DIRECTORY_LINUX = "/home/ubuntu/loyalty-gifts-pics/";
	public final static String VIN_DIRECTORY_WINDOWS = "C:\\VIN-DIR\\";
	public final static String VIN_DIRECTORY_LINUX = "/home/ubuntu/VIN-DIR/";
	private final static String OS = System.getProperty("os.name").toLowerCase();

	public final static String SERVICE_HOST = "http://localhost:8081";
	public final static String APP_HOST_LOCAL = "http://localhost:8081";
	public final static String APP_SECRET = "093X3b*y&iWu4U&F181X#3ZE0)%9374";

	private final static String USER_SERVICE = SERVICE_HOST + "/service-qetaa-user/rest/";
	private final static String PAYMENT_SERVICE_BANKS = SERVICE_HOST + "/service-qetaa-payment/rest/banks/";
	private final static String LOCATION_SERVICE = SERVICE_HOST + "/service-qetaa-location/rest/";
	private final static String VEHICLE_SERVICE = SERVICE_HOST + "/service-qetaa-vehicle/rest/";
	private final static String CART_SERVICE = SERVICE_HOST + "/service-qetaa-cart/rest/";
	private final static String CART_REPORT_SERVICE = SERVICE_HOST + "/service-qetaa-cart/rest/report/";
	private final static String CUSTOMER_SERVICE = SERVICE_HOST + "/service-qetaa-customer/rest/";
	private final static String VENDOR_SERVICE = SERVICE_HOST + "/service-qetaa-vendor/rest/";
	private final static String PAYMENT_SERVICE = SERVICE_HOST + "/service-qetaa-payment/rest/";
	private final static String PROMOTION_SERVICE = SERVICE_HOST + "/service-qetaa-vendor/rest/promotion/";
	private final static String PRODUCT_SERVICE = SERVICE_HOST + "/service-qetaa-product/rest/";
	private final static String INVOICE_SERVICE = SERVICE_HOST + "/service-qetaa-invoice/rest/";
	private final static String PUBLIC_LOCATION_SERVICE = LOCATION_SERVICE + "api/v1/";
	
	public final static String USER_LOGIN = USER_SERVICE + "login";

	public final static String GET_ALL_ACTIVITIES = USER_SERVICE + "all-activities";
	public final static String GET_ALL_ROLES = USER_SERVICE + "all-roles";
	public final static String GET_ACTIVE_ROLES = USER_SERVICE + "active-roles";
	public final static String POST_CREATE_ROLE = USER_SERVICE + "role";
	public final static String PUT_UPDATE_ROLE = USER_SERVICE + "role";
	
	public final static String GET_ORDERS_MAP_REPORT = PUBLIC_LOCATION_SERVICE + "carts-count";

	public final static String POST_CREATE_USER = USER_SERVICE + "user";
	public final static String PUT_UPDATE_USER = USER_SERVICE + "user";
	public final static String GET_ALL_USERS = USER_SERVICE + "all-users";

	public final static String GET_ALL_BANKS = PAYMENT_SERVICE_BANKS + "all-banks/user";
	public final static String GET_ACTIVE_BANKS = PAYMENT_SERVICE_BANKS + "active-banks/user";
	public final static String POST_CREATE_BANK = PAYMENT_SERVICE_BANKS + "bank";
	
	public final static String GET_ALL_COURIERS = VENDOR_SERVICE + "couriers";
	public final static String GET_ACTIVE_COURIERS = VENDOR_SERVICE + "active-couriers/user";
	public final static String POST_CREATE_COURIER = VENDOR_SERVICE + "courier";

	public final static String GET_ALL_COUNTRIES = LOCATION_SERVICE + "all-countries";
	public final static String POST_CREATE_COUNTRY = LOCATION_SERVICE + "country";
	public final static String GET_ALL_CITIES = LOCATION_SERVICE + "all-cities";
	public final static String GET_ALL_REGIONS = LOCATION_SERVICE + "all-regions";
	public final static String GET_VENDORS_REGIONS = VENDOR_SERVICE + "regions-vendors";
	public final static String POST_VENDOR_REGION = VENDOR_SERVICE + "vendor-region";
	public final static String POST_CREATE_CITY = LOCATION_SERVICE + "city";
	public final static String POST_CREATE_REGION = LOCATION_SERVICE + "region";
	public final static String GET_ACTIVE_CITIES_INTERNAL = LOCATION_SERVICE + "active-cities-internal";

	public final static String GET_ALL_MAKES = VEHICLE_SERVICE + "all-makes";
	public final static String GET_ALL_ACTIVE_MAKES = VEHICLE_SERVICE + "all-active-makes";
	public final static String GET_ALL_MODEL_YEARS = VEHICLE_SERVICE + "all-model-years";
	public final static String POST_CREATE_MODEL_YEAR = VEHICLE_SERVICE + "model-year";
	public final static String POST_CREATE_MAKE = VEHICLE_SERVICE + "make";
	public final static String POST_CREATE_MODEL = VEHICLE_SERVICE + "model";

	public final static String PUT_UPDATE_MAKE = VEHICLE_SERVICE + "make";
	public final static String PUT_UPDATE_MODEL = VEHICLE_SERVICE + "model";
	public final static String PUT_UPDATE_MODEL_YEAR = VEHICLE_SERVICE + "model-year";

	public final static String GET_UNASSIGNED_NOTIFICATIONS = CART_SERVICE + "unasssigned-notification";

	public final static String GET_ALL_QUOTATIONS_NOTIFICATION = CART_SERVICE + "quotation-notification";

	public final static String POST_QUOTATION = CART_SERVICE + "quotation";
	public final static String POST_QUOTATION_FOR_FINDERS = CART_SERVICE + "quotation/finders";
	public final static String PUT_UPDATE_ADDITIONAL_QUOTATION = CART_SERVICE + "additional-quotation";
	public final static String POST_ADDITIONAL_QUOTATION = CART_SERVICE + "additional-quotation";
	public final static String POST_MANUAL_QUOTATION_VENDOR = CART_SERVICE + "manual-quotation-vendor";
	public final static String APPROVE_QUOTATION = CART_SERVICE + "approve-quotation";
	public final static String GET_QUOTATIONS_WAITING = CART_SERVICE + "waiting-quotation-carts";
	public final static String PUT_ARCHIVE_CART = CART_SERVICE + "archive-cart";
	public final static String PUT_EDIT_CART = CART_SERVICE + "cart";
	public final static String POST_QUOTATION_ITEM_RESPONSE = CART_SERVICE + "quotation-item-response";
	
	public final static String POST_BULK_PROMOTION_CODES = PROMOTION_SERVICE + "generate-code/bulk";
	public final static String GET_ALL_DISCOUNT_PROMOTION_CODES = PROMOTION_SERVICE +"promotion-codes/discount-only";

	public final static String GET_ALL_VENDORS = VENDOR_SERVICE + "all-vendors";
	public final static String PUT_UPDATE_VENDOR = VENDOR_SERVICE + "vendor";
	public final static String POST_VENDOR_MAKE = VENDOR_SERVICE + "vendor-make";
	public final static String POST_CREATE_VENDOR = VENDOR_SERVICE + "vendor";
	public final static String POST_CREATE_VENDOR_USER = VENDOR_SERVICE + "vendor-user";
	public final static String POST_PROMOTION_PROVIDER = PROMOTION_SERVICE + "provider";
	public final static String GET_ALL_PROMOTION_PROVIDERS = PROMOTION_SERVICE + "all-providers";

	public final static String GET_PARTS_NOTIFICATION = CART_SERVICE + "parts-notification";
	public final static String GET_PARTS_COLLECTION_ITEMS = CART_SERVICE + "collection-items";

	public final static String PUT_UPDATE_COLLECT_ITEM = CART_SERVICE + "collect-item";
	public final static String GET_COLLECTION_NOTIFICATION = CART_SERVICE + "collection-items-notification";
	public final static String GET_WIRE_NOTIFICATION = CART_SERVICE + "wire-notification";
	public final static String GET_FOLLOWUPS_NOTOFICATION = CART_SERVICE + "followups-notification";
	public final static String GET_PARTS_RECEIVAL_ITEMS = CART_SERVICE + "receival-items";
	public final static String GET_RECEIVAL_NOTIFICATION = CART_SERVICE + "receival-items-notification";
	public final static String PUT_UPDATE_RECEIVE_ITEM = CART_SERVICE + "receive-item";
	public final static String GET_WAITING_PART_CARTS = CART_SERVICE + "waiting-part-carts";
	public final static String GET_NO_VINS_NOTIFICATION = CART_SERVICE + "no-vin-notification";
	public final static String PUT_UPDATE_SHIP_ITEMS = CART_SERVICE + "ship-items";

//	public final static String GET_ALL_CUSTOMERS = CUSTOMER_SERVICE + "customers";
	public final static String POST_ALL_CUSTOMERS_FROM_IDS = CUSTOMER_SERVICE + "customers-from-ids";
	public final static String GET_FOLLOW_UP_CARTS = CART_SERVICE + "active-followups";
	public final static String GET_NO_ANSWER_FOLLOW_UP_CARTS = CART_SERVICE + "no-answer-followups";
	public final static String POST_FOLLOW_UP_REVIEW = CART_SERVICE + "submit-review";
	public final static String GET_ACTIVE_WIRE_TRANSFERS = CART_SERVICE + "active-wire-transfers";
	public final static String POST_BANK_PARTS_PAYMENT = PAYMENT_SERVICE + "save-successful-payment/user";
	
	public final static String POST_NEW_WALLET = PAYMENT_SERVICE + "new-wallet";
	public final static String PUT_FUND_WALLET = PAYMENT_SERVICE + "fund-wallet/wire-transfer";
	public final static String POST_NEW_WALLET_REFUND = PAYMENT_SERVICE + "new-wallet/refund";
	public final static String PUT_REFUND_WALLET = PAYMENT_SERVICE + "refund-wallet/wire-transfer";
	public final static String POST_WALLET_ITEM_VENDOR = PAYMENT_SERVICE + "wallet-item-vendors";
	
	
	public final static String GET_PROCESS_WALLETS = PAYMENT_SERVICE + "wallets/process";
	public final static String PUT_CONFIRM_WIRE_TRANSFER = CART_SERVICE + "confirm-wire-transfer";
	public final static String PUT_UNDO_WIRE_TRANSFER = CART_SERVICE + "undo-wire-transfer";

	public final static String GET_UNASSIGNED_CARTS = CART_SERVICE + "unassigned-carts";
	public final static String GET_ADVISOR_QUOTATION_CARTS = CART_SERVICE + "advisor-quotation-carts";
	public final static String PUT_UNASSIGN_CART = CART_SERVICE + "unassign-cart";
	public final static String POST_ASSIGN_CART = CART_SERVICE + "assign-cart";

	public final static String GET_TOTAL_REGISTERED_CUSTOMERS = CUSTOMER_SERVICE + "all-customers-number";
	public final static String GET_CURRENT_MONTH_REGISTERED_CUSTOMERS = CUSTOMER_SERVICE
			+ "current-month-customers-number";
	public final static String GET_LAST_MONTH_REGISTERED_CUSTOMERS = CUSTOMER_SERVICE + "last-month-customers-number";
	public final static String GET_LOGIN_TO_CUSTOMER_RATIO = CUSTOMER_SERVICE + "login-to-customer-ratio";
	public final static String GET_LOYALTY_GIFTS = CUSTOMER_SERVICE + "loyalty-gifts";
	public final static String POST_CREATE_LOYALTY_GIFT = CUSTOMER_SERVICE + "loyalty-gift";

	public final static String GET_GLOBAL_HITS = CUSTOMER_SERVICE + "hit-count";
	public final static String GET_TODAY_HITS = CUSTOMER_SERVICE + "hit-count/today";
	public final static String GET_TODAY_REGISTRATION = CUSTOMER_SERVICE + "registered-count/today";
	public final static String GET_GLOBAL_LOGINS = CUSTOMER_SERVICE + "logins-count";
	public final static String GET_TODAY_LOGINS = CUSTOMER_SERVICE + "logins-count/today";
	public final static String GET_GLOBAL_CARTS = CART_SERVICE + "carts-count";
	public final static String GET_TODAY_CARTS = CART_SERVICE + "carts-count/today";
	public final static String GET_GLOBAL_PART_ORDERS = CART_SERVICE + "part-orders-count";
	public final static String GET_TODAY_PART_ORDERS = CART_SERVICE + "part-orders-count";
	public final static String POST_FILTER_OUT_ACTIVE_CUSTOMERS = CART_SERVICE + "filter-out-active-customers";
	public final static String GET_ACTIVE_SESSIONS = CUSTOMER_SERVICE + "hit-count/active";

	public final static String POST_REGISTER_SMS = CUSTOMER_SERVICE + "register-sms";
	public final static String POST_REGISTER_EMAIL = CUSTOMER_SERVICE + "register-email";
	public final static String POST_MOBILE_REGISTER = CUSTOMER_SERVICE + "mobile-register";

	public final static String POST_CREATE_CART = CART_SERVICE + "cart";
	public final static String GET_MAKE_ORER_MONTHS = CART_REPORT_SERVICE + "order-makes/last-year";

	public final static String POST_WIRE_TRASNFER = CART_SERVICE + "wire-transfer";
	public final static String POST_CREDIT_SALES = CART_SERVICE +"parts-order/credit-sales";
	public final static String POST_CASH_ON_DELIVERY = CART_SERVICE + "parts-order/cash-on-delivery";
	public final static String GET_POSTPONED_CARTS = CART_SERVICE + "postponed-sales";
	public final static String GET_NO_VIN_CARTS = CART_SERVICE +"no-vin-carts";

	public final static String PUT_UPDATE_APPROVED_ITEMS = CART_SERVICE + "parts-approved-items";
	public final static String PUT_UPDATE_APPROVED_ITEM = CART_SERVICE + "parts-approved-item";
	public final static String PUT_MANUAL_QUOTATION_VENDOR_ITEM = CART_SERVICE + "quotation-vendor-item/user";
	public final static String POST_QUOTATION_ITEM = CART_SERVICE + "quotation-item";
	public final static String POST_RETURN_ITEM = CART_SERVICE + "return-item";
	
	public final static String GET_INCOMPLETE_PURCHASES = INVOICE_SERVICE + "incomplete-purchases";
	public final static String PUT_COMPLETE_PURCHASE = INVOICE_SERVICE + "complete-purchase-costs";
	public final static String GET_PURCHASE_PAYABLES = INVOICE_SERVICE + "payables";
	public final static String GET_SALES_RECEIVABLES = INVOICE_SERVICE + "receivables";
	public final static String POST_PURCHASE_PAYMENTS = INVOICE_SERVICE + "purchase-payments";
	public final static String POST_PURCHASE_PAYMENT = INVOICE_SERVICE + "purchase-payment";
	public final static String POST_SALES_PAYMENT = INVOICE_SERVICE + "sales-payment";
	
	public final static String PUT_EDIT_CUSTOMER = CUSTOMER_SERVICE + "edit-customer";
	public final static String PUT_UPDATE_ADDRESS = CUSTOMER_SERVICE + "address";
	
	public final static String POST_PRODUCT_NAME = PRODUCT_SERVICE + "product-name";
	public final static String POST_PRODUCT = PRODUCT_SERVICE + "product";
	
	public final static String POST_NEW_PURCHASE = INVOICE_SERVICE + "new-purchase";
	public final static String PUT_NEW_PURCHASE = INVOICE_SERVICE + "purchase";
	
	public final static String POST_NEW_SALES = INVOICE_SERVICE + "new-sales";
	public final static String PUT_NEW_SALES = INVOICE_SERVICE + "sales";
	
	public final static String POST_NEW_SHIPMENT = PAYMENT_SERVICE + "shipment/new-shipment";
	public final static String PUT_NEW_SHIPMENT = PAYMENT_SERVICE + "shipment/shipment";
	
	public final static String POST_NEW_SALES_RETURN = INVOICE_SERVICE + "new-sales-return";
	public final static String PUT_NEW_SALES_RETURN = INVOICE_SERVICE + "sales-return";
	
	public final static String POST_NEW_PURCHASE_RETURN = INVOICE_SERVICE + "new-purchase-return";
	public final static String PUT_NEW_PURCHASE_RETURN = INVOICE_SERVICE + "purchase-return";
	
	public final static String GET_SALES_RETURN_PURCHASE_NOT_RETURNED = INVOICE_SERVICE + "sales-returns/purchase-not-returned";
	
	public final static String POST_SALES_SEARCH = INVOICE_SERVICE + "search-sales";
	public final static String POST_PURCHASE_SEARCH = INVOICE_SERVICE + "search-purchases";
	
	public final static String GET_VENDOR_JOIN_REQUESTS = VENDOR_SERVICE + "active-vendor-join-requests";
	
	public final static String POST_ADDRESSES_FROM_CART_IDS = CART_SERVICE + "addresses/cart-ids";
	
	public final static String POST_SALES_REPORT_3 = INVOICE_SERVICE + "sales-report";
	public final static String POST_SALES_RETURN_REPORT_3 = INVOICE_SERVICE + "sales-return-report";

	public final static String deleteVendorJoinRequest(int vendorJoinId) {
		return VENDOR_SERVICE + "vendor-join/" + vendorJoinId;
	}
	
	public final static String getCustomerPartsApprovedITems(long cartId) {
		return CART_SERVICE + "customer-parts-approved-items/cart/" + cartId;
	}

	public final static String getFullPartsApprovedItems(long cartId, int vendorId) {
		return CART_SERVICE + "full-parts-approved-items/cart/" + cartId + "/vendor/" + vendorId;
	}

	public final static String getFullPartsApprovedItems(long cartId) {
		return CART_SERVICE + "full-parts-approved-items/cart/" + cartId;
	}

	public final static String getDetailedActivityHits(long date) {
		return CUSTOMER_SERVICE + "hit-activities/date/" + date;
	}
	
	public final static String getCustomerActivityHits(long customerId) {
		return CUSTOMER_SERVICE + "hit-activities/customer/" + customerId;
	}

	public final static String searchCustomer(String mobile) {
		return CUSTOMER_SERVICE + "search-customers/mobile/" + mobile;
	}

	public final static String searchCustomerAny(String any) {
		return CUSTOMER_SERVICE + "search-customers/" + any;
	}

	public final static String getCartFollowUp(long cartId) {
		return CART_SERVICE + "active-followup/" + cartId;
	}
	
	public final static String getNoVinCart(long cartId) {
		return CART_SERVICE + "no-vin-cart/" + cartId;
	}
	
	public final static String getWireTransfer(long cartId) {
		return CART_SERVICE + "wire-transfer/cart/" + cartId;
	}
	
	public final static String getPreviousCarts(long customerId, long cartId) {
		return CART_SERVICE + "cart/customer/"+customerId+"/except/"+cartId;
	}

	public final static String getQuotationNotification(int assignedTo) {
		return CART_SERVICE + "quotation-notification/assigned-to/" + assignedTo;
	}

	public final static String getUsersWhoHasAccessTo(int activityId) {
		return USER_SERVICE + "user/has-access-to/" + activityId;
	}

	public final static String getWaitingQuotationCarts(int assignedTo) {
		return CART_SERVICE + "waiting-quotation-carts/assigned-to/" + assignedTo;
	}

	public final static String getModelYear(int modelYearId) {
		return VEHICLE_SERVICE + "model-year/" + modelYearId;
	}

	public final static String getFinalizedItems(long cartId) {
		return CART_SERVICE + "quotation-finalized-items/cart/" + cartId;
	}

	public final static String getFinalizedItemsFull(long cartId) {
		return CART_SERVICE + "quotation-finalized-items-full/cart/" + cartId;
	}

	public final static String getCustomer(long customerId) {
		return CUSTOMER_SERVICE + "customer/" + customerId;
	}

	public final static String getUser(long userId) {
		return USER_SERVICE + "user/" + userId;
	}

	public final static String getCity(long cityId) {
		return LOCATION_SERVICE + "city/" + cityId;
	}

	public final static String getCountryCities(int countryId) {
		return LOCATION_SERVICE + "active-cities-internal/country/" + countryId;
	}

	public final static String getCart(long cartId, int requesterId) {
		return CART_SERVICE + "cart/" + cartId + "/requester-id/" + requesterId;
	}

	public final static String getCart(long cartId) {
		return CART_SERVICE + "cart/" + cartId;
	}

	public final static String getCartInternal(long cartId) {
		return CART_SERVICE + "cart-internal/" + cartId;
	}

	public final static String getShippedCart(long cartId) {
		return CART_SERVICE + "shipped-cart/" + cartId;
	}
	
	public final static String getPartsOrderCart(long cartId) {
		return CART_SERVICE + "parts-order-cart/" + cartId;
	}
	
	

	public final static String getCartQuotations(long cartId) {
		return CART_SERVICE + "quotations/cart/" + cartId;
	}

	public final static String getUnselectedMakes(int vendorId) {
		return VENDOR_SERVICE + "unselected-makes/vendor/" + vendorId;
	}

	public final static String getMakeVendors(int makeId) {
		return VENDOR_SERVICE + "all-vendors/make/" + makeId;
	}

	public final static String deleteVendorMake(int vendor, int make) {
		return VENDOR_SERVICE + "vendor-make/vendor/" + vendor + "/make/" + make;
	}

	public final static String getCartAddress(long cartId) {
		return CART_SERVICE + "address/cart/" + cartId;
	}

	public final static String getCartCollectionItems(long cartId) {
		return CART_SERVICE + "collection-items/cart/" + cartId;
	}
	
	public final static String putMergeCarts(long mainId, long slaveId, int userId) {
		return CART_SERVICE + "merge-cart/main/"+mainId+"/slave/"+slaveId+"/user/" + userId;
	}

	public final static String getCustomerApprovedItems(long cartId) {
		return CART_SERVICE + "customer-approved-items/cart/" + cartId;
	}
	
	public final static String deleteQuotation(long quotationId) {
		return CART_SERVICE + "quotation/" + quotationId;
	}
	
	public final static String deleteQuotationItem(long quotationItemId) {
		return CART_SERVICE + "quotation-item/" + quotationItemId;
	}

	public final static String getCartReviews(long cartId) {
		return CART_SERVICE + "reviewes/cart/" + cartId;
	}

	public final static String getPartsOrder(long cartId) {
		return CART_SERVICE + "parts-order/cart/" + cartId;
	}

	public final static String getPartsItemReturns(long cartId) {
		return CART_SERVICE + "return-items/cart/" + cartId;
	}
	
	public final static String getPartPayment(long cartId) {
		return PAYMENT_SERVICE + "parts-payment/cart/" + cartId;
	}

	public final static String getPaymentReport(int year, int month, String paymentType) {
		return PAYMENT_SERVICE + "payment-report/year/" + year + "/month/" + month + "/payment-type/" + paymentType;
	}
	
	public final static String getWalletsReport(int year, int month, String paymentType) {
		return PAYMENT_SERVICE + "wallets/year/" + year + "/month/" + month + "/payment-type/" + paymentType;
	}
	

	public final static String getShipmentsReport(int year, int month, int courierId, long cartId) {
		return PAYMENT_SERVICE + "shipment/shipments/year/" + year + "/month/" + month + "/courier/" + courierId + "/cart/" + cartId;
	}
	
	public final static String getSalesReport(int year, int month, char method) {
		return INVOICE_SERVICE + "sales-report/year/" + year + "/month/" + month + "/method/" + method ;
	}
	
	public final static String getSalesReturnReport(int year, int month) {
		return INVOICE_SERVICE + "sales-return-report/year/" + year + "/month/" + month;
	}

	public final static String getLostSalesReport(int year, int month) {
		return CART_SERVICE + "lost-sales/year/" + year + "/month/" + month;
	}

	public final static String getNweCustomersReport(int year, int month) {
		return CUSTOMER_SERVICE + "customer-registration/year/" + year + "/month/" + month;
	}

	public final static String searchCart(long cartId, long customerId) {
		return CART_SERVICE + "search-cart/cart/" + cartId + "/customer-id/" + customerId;
	}

	public final static String getGiftsDirectory() {
		if (OS.indexOf("win") >= 0) {
			return GIFTS_DIRECTORY_WINDOWS;
		} else {
			return GIFTS_DIRECTORY_LINUX;
		}
	} 
	
	public final static String getVINDirectory() {
		if (OS.indexOf("win") >= 0) {
			return VIN_DIRECTORY_WINDOWS;
		} else {
			return VIN_DIRECTORY_LINUX;
		}
	}
	
	public final static String getVINDirectoryWithDate(int year, int month, int day) {
		if (OS.indexOf("win") >= 0) {
			return VIN_DIRECTORY_WINDOWS + year +  "\\" + month + "\\" + day + "\\";
		} else {
			return VIN_DIRECTORY_LINUX + year + "/" + month + "/" + day + "/";
		}
	}
	
	public final static String getCartsCreated(long date) {
		return CART_REPORT_SERVICE + "carts-count/date/" + date;
	}
	
	public final static String getCartsArchived(long date) {
		return CART_REPORT_SERVICE + "carts-count/archived/date/" + date;
	}
	
	public final static String getPartsOrders(long date) {
		return CART_REPORT_SERVICE + "parts-orders-count/date/" + date;
	}
	
	public final static String getSubmittedQuotations(long date) {
		return CART_REPORT_SERVICE + "submitted-quotation-count/date/" + date;
	}
	
	public final static String getWireTransfersCreated(long date) {
		return CART_REPORT_SERVICE + "wire-transfer-count/date/" + date;
	}

	public final static String getFollowUpReviewCreated(long date) {
		return CART_REPORT_SERVICE + "follow-up-review-count/date/" + date;
	}
	
	public final static String getLoginsCount(long date) {
		return CUSTOMER_SERVICE + "login-count/date/" + date;
	}
	
	public final static String getHitCount(long date) {
		return CUSTOMER_SERVICE + "hit-count/date/" + date;
	}
	
	public final static String getRegisteredCount(long date) {
		return CUSTOMER_SERVICE + "registered-count/date/" + date;
	}
	
	public final static String getMakeHits(long date, String make) {
		return CUSTOMER_SERVICE + "make-hits/date/"+date+"/make/"+ make;
	}
	
	public final static String getNewVisitCount(long date) {
		return CUSTOMER_SERVICE + "new-visit-count/date/"+date;
	}
	
	public final static String getVisitCount(long date) {
		return CUSTOMER_SERVICE + "visit-count/date/"+date;
	}
	
	public final static String getPromotionProvider(int id) {
		return PROMOTION_SERVICE + "provider/" + id;
	}
	
	public final static String searchProductsName(String name) {
		return PRODUCT_SERVICE + "search-product-names/" + name;
	}
	
	public final static String searchProducts(String name, String number, int makeId) {
		return PRODUCT_SERVICE + "search-products/name/" + name + "/number/" + number + "/make/" + makeId;
	}
	
	public final static String findProduct(String number, int makeId) {
		return PRODUCT_SERVICE + "product/number/"+number+"/make/" + makeId;
	}
	
	public final static String getWaitingPartsOrder(long cartId) {
		return CART_SERVICE + "waiting-part-cart/cart/" + cartId;
	}
	
	public final static String getAwaitingWallet(long walletId) {
		return PAYMENT_SERVICE + "wallet/" + walletId + "/awaiting";
	}
	
	public final static String getPurchasedWallet(long walletId) {
		return PAYMENT_SERVICE + "wallet/" + walletId + "/purchased";
	}
	
	public final static String putUpdateProductAdvanced() {
		return PRODUCT_SERVICE + "product/set-name-id/advance";
	}
	
	public final static String getProduct(long productId) {
		return PRODUCT_SERVICE + "product/" + productId;
	}
	
	public final static String getProductForCart(long productId, long cartId) {
		return PRODUCT_SERVICE + "product/" + productId+"/cart/" + cartId;
	}
	
	public final static String getPromoCode(long promoCodeId) {
		return PROMOTION_SERVICE  + "promotion-code/" + promoCodeId;
	}
	
	public final static String getPromotionCodeFromCode(String code) {
		return PROMOTION_SERVICE + "promotion-code/code/" + code;
	}
	
	public final static String getCartPurchases(long cartId) {
		return INVOICE_SERVICE + "purchases/cart/" + cartId;
	}
	
	public final static String getSalesFromCartId(long cartId) {
		return INVOICE_SERVICE + "sales/cart/" + cartId;
	}
	
	public final static String getSalesFromId(long salesId) {
		return INVOICE_SERVICE + "sales/" + salesId;
	}
	
	public final static String getPurchaseFromId(long pId) {
		return INVOICE_SERVICE + "purchase/" + pId;
	}
	
	public final static String getSalesReturnFromId(long salesReturnId) {
		return INVOICE_SERVICE + "sales-return/" + salesReturnId;
	}
	
	public final static String getProductIdsSearch(String search) {
		return PRODUCT_SERVICE + "product-ids/search/" + search;
	}
	
	public final static String getWaitingQuotation(int userId) {
		return CART_SERVICE + "waiting-quotations/user/" + userId;
	}
	
	public final static String getSoldWalletItems(long cid) {
		return PAYMENT_SERVICE + "wallet-items/sold/customer/" + cid;
	}
	
	public final static String getIncompletePurchase(long pid) {
		return INVOICE_SERVICE + "incomplete-purchase/" + pid;
	}
	
	public final static String getVendorPayables(int id) {
		return INVOICE_SERVICE + "payables/vendor/" + id;
	}
	
	public final static String getLocationCartsCount(long from, long to, int makeId, boolean archived, boolean ordered) {
		return PUBLIC_LOCATION_SERVICE + "carts-count"
		+ "/from/" + from
		+ "/to/" + to
		+ "/make/" + makeId
		+ "/archived/" + archived
		+ "/ordered/" + ordered;
	}
	
	public final static String getCountryRegions(int countryId) {
		return LOCATION_SERVICE + "regions/country/" + countryId;
	}
	
	public final static String getRegionVendors(int regionId) {
		return VENDOR_SERVICE + "vendors/region/" + regionId;
	}
	
	
}
