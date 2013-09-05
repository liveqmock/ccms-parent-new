package com.yunat.ccms.util;

public class LongParser implements RequestParamParser {

	@Override
	public Object parse(final String paramName, final String[] rawValue) {
		return Long.valueOf(rawValue[0]);
	}
}
