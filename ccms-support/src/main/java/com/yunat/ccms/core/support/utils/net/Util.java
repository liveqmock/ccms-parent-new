package com.yunat.ccms.core.support.utils.net;

public class Util {
	public static final char PROTOCAL_SEPERATOR_CHAR = ':';
	public static final String PROTOCAL_SEPERATOR = String.valueOf(PROTOCAL_SEPERATOR_CHAR);
	public static final char PATH_SEPERATOR_CHAR = '/';
	public static final String PATH_SERPERATOR = String.valueOf(PATH_SEPERATOR_CHAR);
	private static final int HTTP_LENGTH = "://".length();

	public static boolean isSuccess(final int statusCode) {
		return statusCode / 100 == 2;
	}

	public static String getHost(final String url) {
		final int protocalIndex = url.indexOf("://");
		final int end = url.indexOf(PATH_SEPERATOR_CHAR, protocalIndex + HTTP_LENGTH);
		return end < 0 ? url.substring(protocalIndex + HTTP_LENGTH + 1) : url.substring(
				protocalIndex + HTTP_LENGTH + 1, end);
	}

	public static String getProtocal(final String url) {
		if (url == null || url.length() == 0) {
			return null;
		}
		final int index = url.indexOf(PROTOCAL_SEPERATOR_CHAR);
		return index < 0 ? "http" : url.substring(0, index);
	}

	public static void main(final String[] args) {
		System.out.println("@@@@@@Util.main():" + getHost("http://www.baidu.com"));
		System.out.println("@@@@@@Util.main():" + getHost("http://www.baidu.com/"));
		System.out.println("@@@@@@Util.main():" + getHost("http://www.baidu.com/jjyy"));
		System.out.println("@@@@@@Util.main():" + getHost("http://www.baidu.com/jjyy/jjww"));
	}
}
