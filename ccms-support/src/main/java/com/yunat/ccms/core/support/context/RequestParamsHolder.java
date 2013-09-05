package com.yunat.ccms.core.support.context;

import java.util.Collections;
import java.util.Map;

public class RequestParamsHolder {

	protected static final ThreadLocal<Map<String, String[]>> REQUEST_PARAMS_LOCAL = new ThreadLocal<Map<String, String[]>>();

	public static Map<String, String[]> getParams() {
		final Map<String, String[]> m = REQUEST_PARAMS_LOCAL.get();
		return m == null ? null : Collections.unmodifiableMap(m);
	}

	public static String[] getParamRawValues(final String paramName) {
		final Map<String, String[]> params = getParams();
		return params == null ? null : params.get(paramName);
	}

	public static String getParamRawValue(final String paramName) {
		final String[] rawValues = getParamRawValues(paramName);
		return rawValues == null || rawValues.length == 0 ? null : rawValues[0];
	}
}
