package com.yunat.ccms.core.support.ucenter.cons;

public interface UCenterCons {

	/**
	 * 在订购中心的app id
	 */
	int APP_ID_IN_UCENTER = 0;

	String URL_PREFIX = "https://sub.fenxibao.com/ucenter-restful-impl";//

	String TICKET = "ticket=pC7ErNVAQWgHopgyt4ph8KlnT";

	String URL_SUBSCRIBER_SERVICE_PART = "/rest/services/subscriber_service/action/find/invoke?app_id="
			+ APP_ID_IN_UCENTER + "&" + TICKET;

	String URL_SUBSCRIBER_SERVICE = URL_PREFIX + URL_SUBSCRIBER_SERVICE_PART;

	String URL_SET_SERVICE_PART = "/rest/services/set_service/action/find/invoke?app_id=" + APP_ID_IN_UCENTER + "&"
			+ TICKET;
	String URL_SET_SERVICE = URL_PREFIX + URL_SET_SERVICE_PART;

	String URL_SET_SUBSCRIPTION_SERVICE_PART = "/rest/services/set_subscription_service/action/find/invoke?" + TICKET;
	String URL_SET_SUBSCRIPTION_SERVICE = URL_PREFIX + URL_SET_SUBSCRIPTION_SERVICE_PART;

	String STATUS_OK = "OK";

}
