package com.yunat.ccms.core.support.utils.net;

import java.nio.charset.Charset;

import org.junit.Test;

public class HttpClientUrlConnectorTest {

	@Test
	public void testDownBytes() {
		final UrlConnectorConfig config = new UrlConnectorConfig("http://www.baidu.com");
		final ToStringUrlConnectorContentHandler handler = new ToStringUrlConnectorContentHandler(
				Charset.forName("utf8"));
		new HttpClientUrlConnector().downBytes(config, handler);
		System.out.println(handler.getData());
	}

}
