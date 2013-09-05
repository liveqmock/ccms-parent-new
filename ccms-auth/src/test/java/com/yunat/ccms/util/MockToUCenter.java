package com.yunat.ccms.util;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunat.ccms.core.support.ucenter.Subscriber;
import com.yunat.ccms.core.support.ucenter.SubscriberResult;
import com.yunat.ccms.core.support.ucenter.cons.UCenterCons;

public class MockToUCenter {
	private static final ObjectMapper mapper = new ObjectMapper()//
			.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)//
			.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	//生产环境拟定为: https://sub.fenxibao.com/ucenter-restful-impl,
	//预发布环境拟定为: http://apptest.fenxibao.com/ucenter-restful-impl
	private static final String URL_PREFIX = "http://apptest.fenxibao.com/ucenter-restful-impl";
	private static final String URL_SUBSCRIBER_SERVICE = URL_PREFIX
			+ "/rest/services/subscriber_service/action/find/invoke";
	private static final String URL_SET_SERVICE = URL_PREFIX + "/rest/services/set_service/action/find/invoke";

	static void a(final String subscriberName) {
		final String urlSubscriberService = URL_SUBSCRIBER_SERVICE + "?app_id=0&subscriber_name=" + subscriberName;
		final String urlSetService = URL_SET_SERVICE + "";
//		{"status" : /* status code */,"errCode" : /* error code */,"message" : /* message */,"data" : /* List of subscriber structure */}
		final String result = "{\"status\" : \"OK\","//
				+ "\"errCode\" : \"OK\","//
				+ "\"message\" : \"success\","//
				+ "\"data\" :{\"subscriberId\":1,"//
				+ /*	*/"\"appId\":\"X\","//
				+ /*	*/"\"subscriberName\":\"qiushi\","//
				+ /*	*/"\"etlEnabled\":false,"//
				+ /*	*/"\"createTime\":1358575125000,"//
				+ /*	*/"\"updateTime\":1358575159000,"//
				+ /*	*/"\"shops\": {\"shopId\":\"taobao_61559109\","//
				+ /*		*/"\"platCode\":\"taobao\","//
				+ /*		*/"\"platShopId\":\"61559109\","//
				+ /*		*/"\"platNick\":\"rohnjarry\","//
				+ /*		*/"\"openDate\":1289664000000,"//
				+ /*		*/"\"createTime\":1326957887000,"//
				+ /*		*/"\"updateTime\":1358580443000,"//
				+ /*		*/"\"shopProperties\":{\"TAOBAO_UID\":\"85284125\","//
				+ /*			*/"\"SHOP_TYP\":\"C\","//
				+ /*			*/"\"MMBR_CNT\":\"1000\","//
				+ /*			*/"\"SHOP_LVL\":\"6\""//
				+ /*		*/"}"//
				+ /*	*/"},"//
				+ /*	*/"\"account\":{\"accountName\":\"qiushi\","
				+ /*			*/"\"accountPass\":null,"
				+ /*			*/"\"accountPassPlain\":null,"//
//				+ "\"aaa\":123,"//
				+ /*			*/"\"status\":\"NORMAL\","//
				+ /*			*/"\"createTime\":1358580215000,"//
				+ /*			*/"\"updateTime\":1358580215000,"//
				+ /*			*/"\"createdBy\":\"taobao_65927470\""//
				+ /*		*/"}"//
				+ /*	*/"}"//
				+ ",\"bbb\":{}}";
		try {
			final SubscriberResult r = mapper.readValue(result, SubscriberResult.class);
			System.out.println("@@@@@@Test.a():" + r);
			final List<Subscriber> subscribers = r.getData();
			final String appId = String.valueOf(UCenterCons.APP_ID_IN_UCENTER);
			for (final Subscriber s : subscribers) {
				if (appId.equals(s.getAppId())) {

				}
			}
		} catch (final JsonParseException e) {
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(final String... args) {
		a("qiushi");
	}
}
