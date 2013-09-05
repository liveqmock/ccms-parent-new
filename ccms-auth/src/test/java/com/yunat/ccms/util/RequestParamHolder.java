package com.yunat.ccms.util;

public interface RequestParamHolder {

	String[] getRawValue(String paramName);

	String getFirstRawValue(String paramName);

	<T> T getValue(String paramName, Class<T> expectedType);
}
