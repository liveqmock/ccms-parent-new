package com.yunat.ccms.core.support.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {

	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public static Date parse(final String date, final String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (final ParseException e) {
			return null;
		}
	}

	public static Date dateStart(final Date date) {
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 比较两个时间的差
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static String compareDate(final Date startDate, final Date endDate) {
		if (startDate == null || endDate == null) {
			return "";
		}

		StringBuffer date = new StringBuffer();
		long time = Math.abs(endDate.getTime() - startDate.getTime());
		final int hour = (int) (time / (1000 * 3600));
		if (hour > 0 || date.length() > 0) {
			date = date.append(hour).append(" 时  ");
		}

		time = time - hour * 1000 * 3600;
		final int minute = (int) (time / (1000 * 60));
		if (minute > 0 || date.length() > 0) {
			date = date.append(minute).append(" 分 ");
		}

		time = time - minute * 1000 * 60;
		final int second = (int) (time / 1000);
		if (second > 0 || date.length() > 0) {
			date = date.append(second).append(" 秒 ");
		} else {
			date = date.append(0).append(" 秒 ");
		}
		// logger.debug("ExecuteDate:"+date.toString());
		return date.toString();
	}

	public static Date addMinute(final Date date, final int count) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, count);
		return cal.getTime();
	}

	public static Date addHour(final Date date, final int count) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, count);
		return cal.getTime();
	}

	/**
	 * 取间隔若干天后的日期.
	 * 
	 * @param date
	 *            the date
	 * @param count
	 *            the day count
	 * @return the Date
	 */
	public static Date addDay(final Date date, final int count) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, count);
		return cal.getTime();

	}

	/**
	 * 取间隔若干月后的日期.
	 * 
	 * @param date
	 *            the date
	 * @param count
	 *            the month count
	 * @return the Date
	 */
	public static Date addMonth(final Date date, final int count) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, count);
		return cal.getTime();

	}

	/**
	 * 从字符串解析相对时间，得到绝对时间
	 * 
	 * @param date
	 *            参照时间
	 * @param relativeDate
	 *            相对时间字符串
	 * @return 绝对时间
	 */
	public static Date getRelativeDate(final Date date, String relativeDate) {
		if (null == relativeDate || "".equals(relativeDate)) {
			return null;
		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		relativeDate = relativeDate.trim();

		if (relativeDate.indexOf("月") != -1) {
			if (relativeDate.indexOf("号") != -1) {
				// 前1月,第5日
				final String[] tmp = relativeDate.replace("前", "").replace("月", "").replace("第", "").replace("号", "")
						.split(",");
				final int month = Integer.parseInt(tmp[0].trim());
				int day = Integer.parseInt(tmp[1].trim());
				cal.add(Calendar.MONTH, -1 * month);
				// 如果day大于目标月份的最大天数，则day=最大天数
				final int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				if (day > maxDay) {
					day = maxDay;
				}
				cal.set(Calendar.DAY_OF_MONTH, day);

			} else {
				// 前若干月
				final int month = Integer.parseInt(relativeDate.replace("前", "").replace("月", "").trim());
				cal.add(Calendar.MONTH, -1 * month);
			}
		} else if (relativeDate.indexOf("天") != -1) {
			// 前若干天
			final int day = Integer.parseInt(relativeDate.replace("前", "").replace("天", "").trim());
			cal.add(Calendar.DAY_OF_MONTH, -1 * day);
		} else {
			// 前若干分钟
			cal.setTime(date);
			final int minute = Integer.parseInt(relativeDate.replace("前", "").replace("分钟", "").trim());
			cal.add(Calendar.MINUTE, -1 * minute);
		}

		return cal.getTime();
	}

	/**
	 * 取两个日期中较早的一个日期
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Date getEarlierDate(final Date d1, final Date d2) {
		if (d1.after(d2)) {
			return d2;
		} else {
			return d1;
		}
	}

	/**
	 * 取两个日期中较晚的一个日期
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static Date getLaterDate(final Date d1, final Date d2) {
		if (d1.before(d2)) {
			return d2;
		} else {
			return d1;
		}
	}

	/**
	 * 取相对或绝对时间, 用于获取含相对时间判断的各个子查询中的时间
	 * 
	 * @param aDate
	 * @param relDate
	 * @param isRelativeTime
	 * @return
	 */
	public static Date getQueryDate(final Date aDate, final String relDate, final boolean isRelativeTime) {
		Date date = new Date();
		if (!isRelativeTime) {
			date = aDate;
		} else {
			date = DateUtils.getRelativeDate(new Date(), relDate);
		}
		return date;
	}

	/**
	 * 取相对或绝对时间, 用于获取含相对时间判断的各个子查询中的时间 暂时只在QueryNodeProcessorRetail中使用
	 * 
	 * @param aDate
	 * @param relDate
	 * @param hour
	 * @param isRelativeTime
	 * @return
	 */
	public static Date getQueryDate(final Date aDate, final String relDate, final String hour,
			final boolean isRelativeTime) {
		Date date = new Date();
		if (!isRelativeTime) {
			date = aDate;
		} else {// 相对时间
			date = DateUtils.getRelativeDate(new Date(), relDate);
		}
		// 判断是否需要添加时分
		if (hour != null && !"".equals(hour)) {
			try {
				date = HStringUtils.parseDateTime(date, hour);
			} catch (final Throwable e) {
				logger.error("", e);
			}
		}

		return date;
	}

	/**
	 * 获取指定日期的最晚时间点，即当天的23:59:59:999
	 * 
	 * @param date
	 * @return
	 */
	public static Date dateEnd(final Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static void main(final String[] args) {
		System.out.println("@@@@@@DateUtils.main():" + dateEnd(new Date()));
	}

	/**
	 * 获取日期 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateTime(final String source) {
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(source);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将字符串转换为Date yyyy-MM-dd
	 * 
	 * @param source
	 * @return
	 */
	public static Date getDate(final String source) {
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(source);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将字符串转换为Date，格式 HH:mm
	 * 
	 * @param source
	 * @return
	 */
	public static Date getTime(final String source) {
		final DateFormat format = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = format.parse(source);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 转换Date为String yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getString(final Date date) {
		if (date == null) {
			return null;
		}
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	/**
	 * 转换Date为String yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringYMD(final Date date) {
		if (date == null) {
			return null;
		}
		final DateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}

	/**
	 * 转换Date为String yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringDate(final Date date) {
		if (date == null) {
			return null;
		}
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 转换Date为String yyyyMMddHHmmss
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringDateName(final Date date) {
		if (date == null) {
			return null;
		}
		final DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date);
	}

	/**
	 * 转换Date为String yyyyMMddHHmmssSSS
	 * 
	 * @param date
	 * @return
	 */
	public static String getFullStringDate(final Date date) {
		if (date == null) {
			return null;
		}
		final DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(date);
	}
}
