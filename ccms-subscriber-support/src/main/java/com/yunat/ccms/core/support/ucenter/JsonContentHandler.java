package com.yunat.ccms.core.support.ucenter;

import java.io.IOException;
import java.nio.charset.Charset;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunat.ccms.core.support.utils.net.ToStringUrlConnectorContentHandler;
import com.yunat.ccms.core.support.utils.net.UrlConnectorContentHandler;

public class JsonContentHandler<T> implements UrlConnectorContentHandler {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()//
			.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)//
			.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private static final ToStringUrlConnectorContentHandler toString = new ToStringUrlConnectorContentHandler(
			Charset.forName("utf8"));

	private final Class<? extends T> clazz;

	private T data;

	public JsonContentHandler(final Class<? extends T> clazz) {
		this.clazz = clazz;
	}

	public T getData() {
		return data;
	}

	@Override
	public void handleData(final byte[] bs) {
		toString.handleData(bs);
		final String s = toString.getData();
		try {
			data = OBJECT_MAPPER.readValue(s, clazz);
		} catch (final JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}