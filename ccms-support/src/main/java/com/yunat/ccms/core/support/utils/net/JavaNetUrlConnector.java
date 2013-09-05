package com.yunat.ccms.core.support.utils.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.springframework.util.StringUtils;

/**
 * 使用java.net.HttpURLConnection进行连接,可能一些设置不全.
 * 
 * @author MaGiCalL
 * 
 */
public class JavaNetUrlConnector implements UrlConnector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yunat.ccms.core.support.utils.net.UrlConnector#downBytes(com.yunat
	 * .ccms.core.support.utils.net.UrlConnectorConfig,
	 * com.yunat.ccms.core.support.utils.net.UrlConnectorContentHandler)
	 */
	@Override
	public void downBytes(UrlConnectorConfig config, UrlConnectorContentHandler handler) {
		final String url = config.getUrl();
		InputStream in = null;
		try {
			final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			buildConnection(config, url, conn);

			conn.connect();

			final int code = conn.getResponseCode();
			if (Util.isSuccess(code)) {
				in = conn.getInputStream();
				final int contentLen = conn.getContentLength();

				int readSteamEachSize = config.getReadSteamEachSize();
				final String contentRange = conn.getHeaderField("Content-Range");
				if (contentRange != null) {
					int index = contentRange.indexOf('/');
					assert index > 0;
					final int fetchedTotalBytes = Integer.parseInt(contentRange.substring(index + 1));
					if (readSteamEachSize != fetchedTotalBytes) {
						readSteamEachSize = fetchedTotalBytes;
					}
				}

				final ByteArrayOutputStream byteArrayOutputStream = contentLen < 0 ? new ByteArrayOutputStream()
						: new ByteArrayOutputStream(contentLen);
				final byte[] tmp = new byte[readSteamEachSize];
				in.read(tmp);
				for (int r = in.read(tmp); r != -1; r = in.read(tmp)) {
					byteArrayOutputStream.write(tmp, 0, r);
				}
				conn.disconnect();

				final byte[] bs = byteArrayOutputStream.toByteArray();

				handler.handleData(bs);
			} else {
			}
		} catch (final MalformedURLException e) {
			throw new RuntimeException("fail to connect to url:" + url, e);
		} catch (final ProtocolException e) {
			throw new RuntimeException("fail to connect to url:" + url, e);
		} catch (final IOException e) {
			throw new RuntimeException("fail to connect to url:" + url, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void buildConnection(UrlConnectorConfig config, final String url, final HttpURLConnection conn)
			throws ProtocolException {
		final String host = Util.getHost(url);
		// 伪造的referer
		final String referer = host + "/";

		conn.setRequestMethod(config.getRequestMethod());

		// 请求头
		conn.setRequestProperty(RequestHeader.Host.getHeadName(), host);
		conn.setRequestProperty(RequestHeader.User_Agent.getHeadName(), config.getRequestSender().getUserAgent());

		final String acceptLanguage = config.getAcceptLanguage();
		if (!StringUtils.hasText(acceptLanguage)) {
			conn.setRequestProperty(RequestHeader.Accept_Language.getHeadName(), acceptLanguage);
		}

		final String acceptEncoding = config.getAcceptEncoding();
		if (!StringUtils.hasText(acceptEncoding)) {
			conn.setRequestProperty(RequestHeader.Accept_Encoding.getHeadName(), acceptEncoding);
		}

		final String acceptCharset = config.getAcceptCharset();
		if (!StringUtils.hasText(acceptCharset)) {
			conn.setRequestProperty(RequestHeader.Accept_Charset.getHeadName(), acceptCharset);
		}

		final String keepAlive = config.getKeepAlive();
		if (!StringUtils.hasText(keepAlive)) {
			conn.setRequestProperty(RequestHeader.Keep_Alive.getHeadName(), keepAlive);
		}

		final String headConnection = config.getHeadConnection();
		if (!StringUtils.hasText(headConnection)) {
			conn.setRequestProperty(RequestHeader.Connection.getHeadName(), headConnection);
		}
		conn.setRequestProperty(RequestHeader.Referer.getHeadName(), referer);

		conn.setConnectTimeout(config.getConnectTimeout());
		conn.setReadTimeout(config.getReadTimeout());
	}
}
