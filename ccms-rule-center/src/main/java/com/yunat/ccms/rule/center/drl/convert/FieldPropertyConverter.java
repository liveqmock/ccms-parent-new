package com.yunat.ccms.rule.center.drl.convert;

public class FieldPropertyConverter {

	/**
	 * java对象属性转换为数据库字段，如userName-->user_name
	 * 
	 * @param property
	 * @return
	 */
	public static String propertyNameToColumnName(final String property) {
		if (null == property) {
			return "";
		}
		final char[] chars = property.toCharArray();
		final StringBuilder field = new StringBuilder();
		for (final char c : chars) {
			if (Character.isUpperCase(c)) {
				field.append("_").append(Character.toLowerCase(c));
			} else {
				field.append(c);
			}
		}
		return field.toString();
	}

	/**
	 * 将数据库字段转换为java属性，如user_name-->userName
	 * 
	 * @param field 字段名
	 * @return
	 */
	public static String columnNameToPropertyName(final String field) {
		if (null == field) {
			return "";
		}
		final char[] chars = field.toCharArray();
		final StringBuilder property = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			final char c = chars[i];
			if (c == '_') {
				i++;
				if (i < chars.length) {//这里可能会出现比较蛋疼的情况:连续两个字符都是下划线.
					property.append(Character.toUpperCase(chars[i]));
				}
			} else {
				property.append(c);
			}
		}
		return property.toString();
	}
}
