package com.yunat.ccms.core.support.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunat.ccms.core.support.cons.AppCons;
import com.yunat.ccms.core.support.exception.CcmsRuntimeException;

public final class HStringUtils extends StringUtils {

	private static SimpleDateFormat formatDate = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat formatYearMonth = new SimpleDateFormat(
			"yyyy-MM");
	private static SimpleDateFormat formatDatetime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat formatDatetimeCompact = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static SimpleDateFormat formatDateforDataBase = new SimpleDateFormat(
			"yyMMddHHmmss");

	private static Logger logger = LoggerFactory.getLogger(HStringUtils.class);

	/**
	 * Returns whether or not the String is empty. A String is considered to be
	 * empty if it is null or if it has a length of 0.
	 *
	 * @param string
	 *            The String that should be examined.
	 * @return Whether or not the String is empty.
	 */
	public static boolean isEmpty(String string) {
		return ((string == null) || (string.trim().length() == 0));
	}

	/**
	 * 将一个字符串的首字母改为大写或者小写
	 *
	 * @param srcString
	 *            源字符串
	 * @param lowercase
	 *            大小写标识，ture小写，false大些
	 * @return 改写后的新字符串
	 */
	public static String toLowerCaseInitial(String srcString, boolean lowercase) {
		StringBuilder sb = new StringBuilder();
		if (lowercase) {
			sb.append(Character.toLowerCase(srcString.charAt(0)));
		} else {
			sb.append(Character.toUpperCase(srcString.charAt(0)));
		}
		sb.append(srcString.substring(1));
		return sb.toString();
	}

	/**
	 * 将一个字符串按照句点（.）分隔，返回最后一段
	 *
	 * @param clazzName
	 *            源字符串
	 * @return 句点（.）分隔后的最后一段字符串
	 */
	public static String getLastName(String clazzName) {
		String[] ls = clazzName.split("\\.");
		return ls[ls.length - 1];
	}

	/**
	 * 把String安全转化为Long 还需验证string是否可以转换为long,待加
	 *
	 * @param s
	 * @return
	 */
	public static final Long safeToLong(String s) {
		if (s == null || "".equals(s)) {
			return null;
		}
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static final String safeToString(Object o) {
		if (o == null) {
			return null;
		} else {
			return o.toString();
		}
	}

	public static final Integer safeToInteger(String str) {
		if (StringUtils.isNotBlank(str)) {
			return Integer.parseInt(str);
		}
		return null;
	}

	public static final boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static final String listToStringBySepartor(List<String> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		StringBuffer cus = new StringBuffer();
		for (String s : list) {
			if ("".equals(cus.toString())) {
				cus.append(s);
			} else {
				cus.append(AppCons.SEPARATOR).append(s);
			}
		}
		return cus.toString();
	}

	public static final String listToStringBySepartor(List<String> list,
			String split) {
		if (list == null || list.size() == 0) {
			return null;
		}
		StringBuffer cus = new StringBuffer();
		for (String s : list) {
			if ("".equals(cus.toString())) {
				cus.append(s);
			} else {
				cus.append(split).append(s);
			}
		}
		return cus.toString();
	}

	public static final List<String> SpiltString(String s, String separtor) {
		if (s == null || "".equals(s)) {
			return new ArrayList<String>();
		}
		String[] sp = s.split(separtor);
		if (sp.length > 0) {
			return java.util.Arrays.asList(sp);
		}
		return new ArrayList<String>();
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return ((SimpleDateFormat) formatDate.clone()).format(date);
	}

	public static String formatYearMonth(Date date) {
		if (date == null) {
			return null;
		}
		return ((SimpleDateFormat) formatYearMonth.clone()).format(date);
	}

	public static String formatDateForDataBase(Date date) {
		if (date == null) {
			return null;
		}

		return ((SimpleDateFormat) formatDateforDataBase.clone()).format(date);
	}

	/**
	 * 返回"yyyy-MM-dd HH:mm:ss"格式时间字符串
	 *
	 * @param date
	 * @return
	 */
	public static String formatDatetime(Date date) {
		if (date == null) {
			return null;
		}

		return ((SimpleDateFormat) formatDatetime.clone()).format(date);
	}

	/**
	 * 返回"yyyyMMddHHmmss"紧凑格式时间字符串
	 *
	 * @param date
	 * @return
	 */
	public static String formatDatetimeCompact(Date date) {
		if (date == null) {
			return null;
		}

		return ((SimpleDateFormat) formatDatetimeCompact.clone()).format(date);
	}

	private static String getFullTime(String time) {
		if (time.split(":").length - 1 == 1) {
			return time + ":00";
		} else {
			return time;
		}

	}

	public static Date parseDate(String str) {
		Date res = null;
		try {

			res = ((SimpleDateFormat) formatDate.clone()).parse(str);
		} catch (ParseException e) {
			throw new CcmsRuntimeException("日期解析错误:" + str, e);
		}
		return res;
	}

	public static Date parseDateTime(String str) {
		Date res = null;
		try {

			res = ((SimpleDateFormat) formatDatetime.clone()).parse(str);
		} catch (ParseException e) {
			throw new CcmsRuntimeException("日期时间解析错误:" + str, e);
		}
		return res;
	}

	public static Date parseDateTime(String str, SimpleDateFormat formatDate) {
		Date res = null;
		try {

			res = ((SimpleDateFormat) formatDate.clone()).parse(str);
		} catch (ParseException e) {
			throw new CcmsRuntimeException("日期解析错误:" + str, e);
		}
		return res;
	}

	public static Date parseDateTime(Date date, String time) {
		if (StringUtils.isEmpty(time)) {
			return date;
		}
		String fullTime = getFullTime(time);
		return parseDateTime(formatDate(date) + " " + fullTime);
	}

	public static int[] getHMS(String time) {
		String fullTime = getFullTime(time);
		int[] res = new int[3];
		String[] tmp = fullTime.split(":");
		for (int i = 0; i < 3; i++) {
			res[i] = Integer.parseInt(tmp[i]);
		}

		return res;
	}

	/**
	 * 指定范围内的随机数 获得的随机数范围为0-target
	 *
	 * @param target
	 *            >1
	 * @return
	 */
	public static int random(int target) {
		Random random = new Random();
		return random.nextInt(target);
	}

	public static void removeLastComma(StringBuffer sb) {
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
	}

	public static String removeLastComma(String s) {
		if (s.charAt(s.length() - 1) == ',') {
			return s.substring(0, s.length() - 1);
		} else {
			return s;
		}
	}

	/**
	 * 判断一个字符串是否是整数
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 根据传入的异常返回异常信息的字符串
	 *
	 * @param t
	 * @return
	 * @throws IOException
	 */
	public static String getExceptionMessage(Throwable t) {
		if (t == null) {
			return "";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			t.printStackTrace(new PrintStream(baos));
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				logger.warn("", e);
			}
		}
		return baos.toString();
	}
}