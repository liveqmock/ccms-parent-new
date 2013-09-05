package com.yunat.ccms.util;

import java.util.Map;

import com.yunat.ccms.auth.exceptions.IllegalLoginParamException;
import com.yunat.ccms.auth.login.taobao.TaobaoLoginUtils;
import com.yunat.ccms.auth.login.taobao.TopParams;

public class _EncodingTest {

	public static void main(final String[] args) {
//		final Map<String, String> map = TaobaoLoginUtils
//				.convertBase64StringtoMap("dzJfZXhwaXJlc19pbj0wJnZpc2l0b3JfaWQ9NzU3ODUzNjk3JmlmcmFtZT0xJnNob3BfaWQ9dGFvYmFvXzY4Nzg0NDQ2JncxX2V4cGlyZXNfaW49MzA4MTM5NDkmdHM9MTM3MDQxNDA1MDIxMCZyZV9leHBpcmVzX2luPTMwODEzOTQ5JnZpc2l0b3Jfbmljaz1tc2Vra2/ml5foiLDlupcmcjJfZXhwaXJlc19pbj0yMjY0MjImZXhwaXJlc19pbj0zMDgxMzk0OSZyZWZyZXNoX3Rva2VuPTYxMDAzMTUyYzMwM2M3ZDZhODQ3ZmIxMTBkM2NkMGE4ODYyMzRmNTc0YzYwYmQ5NzU3ODUzNjk3JnIxX2V4cGlyZXNfaW49MzA4MTM5NDk=");
//		System.out.println("@@@@@@TaobaoLoginSource.main():" + map);
//		final String string = map.get("visitor_nick");
//		System.out.println("@@@@@@TaobaoLoginSource.main():"
//				+ new String(string.getBytes(Charset.forName("gbk")), Charset.forName("utf8")));
//		System.out.println("@@@@@@TaobaoLoginSource.main():"
//				+ new String("大狗子19890202".getBytes(Charset.forName("utf8")), Charset.forName("gbk")));

		parseParams();
		System.out.println("@@@@@@_EncodingTest.main():end");
	}

	protected static TopParams parseParams() {
		final String top_appkey = "12283535";
		final String top_parameters = "dzJfZXhwaXJlc19pbj04NjQwMCZ2aXNpdG9yX2lkPTEyMzM0JmlmcmFtZT0xJncxX2V4cGlyZXNfaW49ODY0MDAwMCZ0cz0xMzcwNDIyNzQ4MTk0JnNob3BfaWQ9dGFvYmFvXzY4Nzg0NDQ2JnJlX2V4cGlyZXNfaW49ODY0MDAwMCZ2aXNpdG9yX25pY2s9bXNla2tvxuy9orXqJnIyX2V4cGlyZXNfaW49ODY0MDAmZXhwaXJlc19pbj0xMzc5MDYyNzQ4MTk0JnJlZnJlc2hfdG9rZW49NjEwMWExMWQ2NDQ0Y2VhNTk1OTEyOWVkNTU1NTM2OGVhZTcxM2JkODM1NmMzZmI3NTc4NTM2OTcmcjFfZXhwaXJlc19pbj04NjQwMDAw";//getParam(request, "top_parameters");
		final String top_session = "6101a11d6444cea5959129ed5555368eae713bd8356c3fb757853697";// getParam(request, "top_session");
		final String top_sign = "EfLxK+EGlpwv48Ic2HXdSA==";// getParam(request, "top_sign");

		final String sign = "213ED065B1294CD818B36F2179C0180D";//request.getParameter("sign");
		final String timestamp = "1370414049157";//request.getParameter("timestamp");
		final String[] itemCode = { "ts-11631-16xx" };// request.getParameterValues("itemCode");

		final TopParams params = new TopParams();
		params.setTop_appkey(top_appkey);
		params.setTop_parameters(top_parameters);
		params.setTop_sign(top_sign);
		params.setTop_session(top_session);
		params.setTimestamp(timestamp);
		params.setSign(sign);
		params.setItemCode(itemCode);

		final Map<String, String> map = TaobaoLoginUtils.convertBase64StringtoMap(top_parameters);
		if (map == null) {
			System.out.println("@@@@@@_EncodingTest.parseParams():");
		}

		final String visitor_id = getParam(map, "visitor_id");
		final String visitor_nick = getParam(map, "visitor_nick");
		//注:shop_id这一段是跟ccms3不同的!具体原因如下:
		//订购中心跳转去ccms3时,会有一个额外的参数shop_id,其值是不带前缀的纯数字的shop_id.
		//@施俊 说不要用这种方式,应该从base64那个字符串中自己解析出shop_id.于是订购中心跳转去ccms4时没有带有shop_id参数
		//而从base64字符串中解析出来的shop_id参数是带有taobao_这个前缀的.
		final String shopIdWithSuffix = getParam(map, "shop_id");
		final String shop_id = shopIdWithSuffix.substring("taobao_".length());

		final String sub_taobao_user_id = map.get("sub_taobao_user_id");
		final String sub_taobao_user_nick = map.get("sub_taobao_user_nick");
		params.setVisitor_id(visitor_id);
		params.setVisitor_nick(visitor_nick);
		params.setShop_id(shop_id);
		params.setSub_taobao_user_id(sub_taobao_user_id);
		params.setSub_taobao_user_nick(sub_taobao_user_nick);
		params.setSubuser(sub_taobao_user_id != null);

		if (!TaobaoLoginUtils.validate(params)) {
			System.out.println("@@@@@@_EncodingTest.parseParams():");
		}
		return params;
	}

	protected static String getParam(final Map<String, String> map, final String paramName)
			throws IllegalLoginParamException {
		final String value = map.get(paramName);
		return value;
	}

}
