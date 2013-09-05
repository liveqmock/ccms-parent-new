package com.yunat.ccms.core.support.utils.net;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.yunat.ccms.core.support.utils.SizeUnits;

public class UrlConnectorConfig {
	private static final Comparator<CharsetWeight> CHARSET_WEIGHT_COMPARATOR = new Comparator<CharsetWeight>() {
		@Override
		public int compare(final CharsetWeight o1, final CharsetWeight o2) {
			final float f1 = o1.getWeight();
			final float f2 = o2.getWeight();
			return f1 == f2 ? 0 : f1 > f2 ? -1 : 1;
		}
	};

	private final String url;
	private String requestMethod = "GET";

	private int connectTimeout = (int) TimeUnit.MINUTES.toMillis(1);
	private int readTimeout = (int) TimeUnit.MINUTES.toMillis(1);
	private int readSteamEachSize = SizeUnits.KB.toBytes(4);

	private String cookies;
	private final RequestSender requestSender = CommonWebBrowsers.IE7_;
	private String acceptMetaType = "*/*";
	private String acceptLanguage = "zh-cn,zh;q=0.5";
	private String acceptEncoding = "gzip,deflate";

	private final SortedSet<CharsetWeight> charsets = new TreeSet<CharsetWeight>(CHARSET_WEIGHT_COMPARATOR);
	// private String acceptCharset = "GB2312,utf-8;q=0.7,*;q=0.7";
	private String keepAlive = "100";
	private String headConnection = "keep-alive";

	private Map<String, Object> params = new HashMap<String, Object>();

	public UrlConnectorConfig(String url) {
		super();
		this.url = url;
	}

	public UrlConnectorConfig setParam(String key, Object value) {
		// 暂时不考虑很复杂的情况
		params.put(key, value);
		return this;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public String getUrl() {
		return url;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public UrlConnectorConfig setConnectTimeout(final int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public UrlConnectorConfig setConnectTimeout(TimeUnit timeUnit, int count) {
		this.connectTimeout = (int) timeUnit.toMillis(count);
		return this;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public UrlConnectorConfig setReadTimeout(final int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	public UrlConnectorConfig setReadTimeout(TimeUnit timeUnit, int count) {
		this.readTimeout = (int) timeUnit.toMillis(count);
		return this;
	}

	public int getReadSteamEachSize() {
		return readSteamEachSize;
	}

	public UrlConnectorConfig setReadSteamEachSize(final int readSteamEachSize) {
		this.readSteamEachSize = readSteamEachSize;
		return this;
	}

	public String getCookies() {
		return cookies;
	}

	public UrlConnectorConfig setCookies(final String cookies) {
		this.cookies = cookies;
		return this;
	}

	public String getAcceptMetaType() {
		return acceptMetaType;
	}

	public UrlConnectorConfig setAcceptMetaType(final String acceptMetaType) {
		this.acceptMetaType = acceptMetaType;
		return this;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public UrlConnectorConfig setAcceptLanguage(final String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
		return this;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public UrlConnectorConfig setAcceptEncoding(final String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
		return this;
	}

	public String getAcceptCharset() {
		if (charsets.isEmpty()) {
			// return "GBK,utf-8;q=0.7,*;q=0.7";
			return "*";
		}
		final StringBuilder sb = new StringBuilder();
		for (final CharsetWeight t : charsets) {
			sb.append(t.getCharset());
			if (t.getWeight() != 1) {
				sb.append(";q=").append(t.getWeight());
			}
			sb.append(",");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
		// return acceptCharset;
	}

	public String getKeepAlive() {
		return keepAlive;
	}

	public UrlConnectorConfig setKeepAlive(final String keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public String getHeadConnection() {
		return headConnection;
	}

	public UrlConnectorConfig setHeadConnection(final String headConnection) {
		this.headConnection = headConnection;
		return this;
	}

	public RequestSender getRequestSender() {
		return requestSender;
	}

	public Charset getFirstCharset() {
		return charsets.isEmpty() ? null : charsets.first().getCharset();
	}

	public UrlConnectorConfig addCharset(final Charset charset, final float weight) {
		charsets.add(new CharsetWeight(charset, weight));
		return this;
	}

	public UrlConnectorConfig addCharset(final Charset charset) {
		charsets.add(new CharsetWeight(charset, 1f));
		return this;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public UrlConnectorConfig setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}
}
