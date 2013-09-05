package com.yunat.ccms.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Maps;

public class RequestParamsPreHandleInterceptor extends HandlerInterceptorAdapter implements Ordered {

	private static final ThreadLocal<Map<Key, Object>> TYPED_VALUE_LOCAL = new ThreadLocal<Map<Key, Object>>();
	private static final ThreadLocal<Map<String, String[]>> RAW_VALUE_LOCAL = new ThreadLocal<Map<String, String[]>>();

	@Component
	public static class RequestParamHolderImpl implements RequestParamHolder {

		@Override
		public String[] getRawValue(final String paramName) {
			return RAW_VALUE_LOCAL.get().get(paramName);
		}

		@Override
		public String getFirstRawValue(final String paramName) {
			final String[] rawValues = getRawValue(paramName);
			if (rawValues == null || rawValues.length == 0) {
				return null;
			}
			return rawValues[0];
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getValue(final String paramName, final Class<T> expectedType) {
			if (expectedType == String[].class) {
				return (T) getRawValue(paramName);
			}
			if (expectedType == String.class) {
				return (T) getFirstRawValue(paramName);
			}
			final Key key = new Key(paramName, expectedType);
			final Object value = TYPED_VALUE_LOCAL.get().get(key);
			if (value == null) {
				//...
			}
			return (T) value;
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {
		@SuppressWarnings("unchecked")
		final Map<String, String[]> rawMap = request.getParameterMap();
		RAW_VALUE_LOCAL.set(rawMap);
		final Map<Key, Object> typeMap = Maps.newHashMap();
		TYPED_VALUE_LOCAL.set(typeMap);
		return super.preHandle(request, response, handler);
	}

	private static class Key {
		String paramName;
		Class<?> expectedType;

		public Key(final String paramName, final Class<?> expectedType) {
			super();
			this.paramName = paramName;
			this.expectedType = expectedType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (expectedType == null ? 0 : expectedType.hashCode());
			result = prime * result + (paramName == null ? 0 : paramName.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Key other = (Key) obj;
			if (expectedType == null) {
				if (other.expectedType != null) {
					return false;
				}
			} else if (!expectedType.equals(other.expectedType)) {
				return false;
			}
			if (paramName == null) {
				if (other.paramName != null) {
					return false;
				}
			} else if (!paramName.equals(other.paramName)) {
				return false;
			}
			return true;
		}
	}
}
