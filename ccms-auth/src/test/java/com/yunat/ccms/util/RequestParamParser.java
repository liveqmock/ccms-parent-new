package com.yunat.ccms.util;

public interface RequestParamParser {

	Object parse(String paramName, String[] rawValue);
}
