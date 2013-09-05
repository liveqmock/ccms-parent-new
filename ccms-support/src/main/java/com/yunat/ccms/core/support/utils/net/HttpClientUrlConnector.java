package com.yunat.ccms.core.support.utils.net;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.util.StringUtils;

public class HttpClientUrlConnector implements UrlConnector {

	@Override
	public void downBytes(final UrlConnectorConfig config, final UrlConnectorContentHandler handler) {
		final String url = config.getUrl();
		final String host = Util.getHost(url);
		// 伪造的referer
		final String referer = host + "/";

		final HttpClient httpClient = new HttpClient();
		final String requestMethod = config.getRequestMethod();
		final HttpMethod httpMethod = requestMethod.equalsIgnoreCase("GET") ? new GetMethod(url) : new PostMethod(url);// TODO:暂时就这两种吧

		// 请求头
		httpMethod.setRequestHeader(RequestHeader.Host.getHeadName(), host);
		httpMethod.setRequestHeader(RequestHeader.User_Agent.getHeadName(), config.getRequestSender().getUserAgent());

		final String acceptLanguage = config.getAcceptLanguage();
		if (!StringUtils.hasText(acceptLanguage)) {
			httpMethod.setRequestHeader(RequestHeader.Accept_Language.getHeadName(), acceptLanguage);
		}

		final String acceptEncoding = config.getAcceptEncoding();
		if (!StringUtils.hasText(acceptEncoding)) {
			httpMethod.setRequestHeader(RequestHeader.Accept_Encoding.getHeadName(), acceptEncoding);
		}

		final String acceptCharset = config.getAcceptCharset();
		if (!StringUtils.hasText(acceptCharset)) {
			httpMethod.setRequestHeader(RequestHeader.Accept_Charset.getHeadName(), acceptCharset);
		}

		final String keepAlive = config.getKeepAlive();
		if (!StringUtils.hasText(keepAlive)) {
			httpMethod.setRequestHeader(RequestHeader.Keep_Alive.getHeadName(), keepAlive);
		}

		final String headConnection = config.getHeadConnection();
		if (!StringUtils.hasText(headConnection)) {
			httpMethod.setRequestHeader(RequestHeader.Connection.getHeadName(), headConnection);
		}
		httpMethod.setRequestHeader(RequestHeader.Referer.getHeadName(), referer);

		final HttpMethodParams httpMethodParams = httpMethod.getParams();
		httpMethodParams.setSoTimeout(config.getConnectTimeout());

		final Map<String, Object> params = config.getParams();
		for (final Entry<String, Object> e : params.entrySet()) {
			httpMethodParams.setParameter(e.getKey(), e.getValue());
		}

		try {
			final int statusCode = httpClient.executeMethod(httpMethod);
			if (statusCode != HttpStatus.SC_OK) {
				throw new RuntimeException("连接失败（状态" + statusCode + "）。");
			}
			final byte[] bs = httpMethod.getResponseBody();
			handler.handleData(bs);
		} catch (final HttpException e) {
			throw new RuntimeException("fail to connect to url:" + url, e);
		} catch (final IOException e) {
			throw new RuntimeException("fail to connect to url:" + url, e);
		} finally {
			httpMethod.releaseConnection();
		}
	}

}
