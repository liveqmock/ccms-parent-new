package com.yunat.ccms.core.support.cons;

public interface AppCons {// TODO:这个类风格严重不一样
	/**
	 * 字符串 分隔符
	 */
	public static final String SEPARATOR = ",";

	/**
	 * 对象转json时强制过滤的字段
	 */
	public static final String[] JSON_EXCLUDE_FIELDS = new String[] { "password" };

	String VERSION_VAR = "version";
	String CHANNEL_ANNOUNCEMENT_URL = "announcementURL";
}
