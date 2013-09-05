package com.yunat.ccms.core.support.utils.net;

import java.nio.charset.Charset;

import com.yunat.ccms.core.support.cons.EncodeName;

public class ToStringUrlConnectorContentHandler implements UrlConnectorContentHandler {

	private final Charset charset;
	private String data;

	public ToStringUrlConnectorContentHandler(final Charset charset) {
		super();
		this.charset = charset;
	}

	public ToStringUrlConnectorContentHandler() {
		this(EncodeName.UTF8.charset());
	}

	@Override
	public void handleData(final byte[] bs) {
		data = new String(bs, charset);
	}

	public String getData() {
		return data;
	}
}
