package com.yunat.ccms.core.support.utils;

/**
 * 
 * @author xiaojing.qu
 * 
 */
public class UrlBuilder {

	private StringBuffer sb = new StringBuffer();

	private boolean appendingParam = false;
	private int paramCount = 0;

	public UrlBuilder(String httpRootPath) {
		sb.append(httpRootPath);
		if (httpRootPath.endsWith("?") || httpRootPath.contains("?")) {
			appendingParam = true;
		}
	}

	/**
	 * 添加路径
	 * 
	 * @param relativePath
	 * @return
	 */
	public UrlBuilder append(String relativePath) {
		if (!appendingParam) {
			boolean endWith = sb.length() > 0 && sb.charAt(sb.length() - 1) == '/';
			boolean startWith = relativePath.startsWith("/");
			if (endWith && startWith) {
				sb.append(relativePath.substring(1));
			} else if (!endWith && !startWith) {
				sb.append("/");
				sb.append(relativePath);
			} else {
				sb.append(relativePath);
			}

		}
		if (relativePath.endsWith("?") || relativePath.contains("?")) {
			appendingParam = true;
		}
		return this;
	}

	/**
	 * 添加参数
	 * PS：如果value为英文，还要考虑url转码（暂时不做了）
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public UrlBuilder addParam(String key, String value) {
		if (!appendingParam) {
			appendingParam = true;
			sb.append("?");
		}

		if (paramCount != 0) {
			sb.append("&");
		}
		if (key != null) {
			paramCount++;
			sb.append(key);
			sb.append("=");
			if (value != null) {
				sb.append(value);
			}
		}

		return this;
	}

	public String build() {
		return sb.toString();
	}
}
