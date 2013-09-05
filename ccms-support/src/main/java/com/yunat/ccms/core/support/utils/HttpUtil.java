package com.yunat.ccms.core.support.utils;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {

	public static final String PROTOCAL_SEPERATOR = "://";
	public static final String HTTP = "http";
	public static final String HTTP_PROTOCAL = HTTP + PROTOCAL_SEPERATOR;

	public static String getReferer(final HttpServletRequest request) {
		return request.getHeader("Referer");
	}

	public static String getRefererDomain(final HttpServletRequest request) {
		final String referer = getReferer(request);
		if (referer == null || referer.isEmpty()) {
			return null;
		}
		return getDomain(referer);
	}

	/**
	 * 
	 * @param url
	 *            一个url,可以不带有协议
	 * @return
	 * @throws IllegalArgumentException
	 *             url参数并非一个合法的url
	 */
	public static String getDomain(final String url) throws IllegalArgumentException {
		String urlToUse = url;
		try {
			final int index = urlToUse.indexOf(PROTOCAL_SEPERATOR);
			if (index < 0) {
				urlToUse = HTTP_PROTOCAL + urlToUse;// 假设为http协议,为避免URL对象因找不到协议抛出异常.
			}
			final URL urlObject = new URL(urlToUse);
			return urlObject.getHost();
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException(url + " is not a url", e);
		}
	}

	public static boolean belongsTo(final String url, final String domain) {
		final String domain2 = getDomain(url);
		return domain2.equals(domain) || domain2.endsWith("." + domain);
	}

	public static void main(final String[] args) throws Exception {
		final URL url = new URL("www.taobao.com/");
		System.out.println("@@@@@@HttpUtil.main():" + url.getHost());
	}
}
