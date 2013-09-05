package com.yunat.ccms.tradecenter.support.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;


/**
 * 日期工具类
 * @author liweilin
 *
 */
public class DateUtil {

	/**
	 * 获取两个时间间相差的秒数
	 * end - start
	 * @param end
	 * @param start
	 * @return
	 */
	public static long getSeconds(Date end, Date start) {
		long times = end.getTime() - start.getTime();

		return times/1000;
	}

	/**
	 * 获取下一个时间的日期
	 * 如果当期时间小于9点那么下一个9点就是今天的9天，如果当前时间大于9点，那么下一个9点就是明天的9点
	 * @param time  格式 '9:00:00'
	 * @return
	 */
	public static Date getNextTime(String time) {

		long timeInt = getSecondsFromStrTime(time);

		//如果当前时间大于备选时间，则返回下一天
		if (getSecondsFromTime(new Date()) - timeInt > 0) {
			return new Date(getTomorrow().getTime() + timeInt * 1000);
		}
		//否则返回今日
		else {
			return new Date(getToday().getTime() + timeInt * 1000);
		}
	}

	private static long getSecondsFromStrTime(String time) {
		int minu = 0;

		String[] tis = time.split(":");

		int hour = Integer.parseInt(tis[0]);
		int minute = Integer.parseInt(tis[1]);

		int secend = 0;
		if (tis.length > 2) {
			secend = Integer.parseInt(tis[2]);
		}


		minu = hour * 3600 + minute * 60 + secend;

		return minu;
	}

	private static long getSecondsFromTime(Date dateTime) {
		int minu = 0;

		String time = new SimpleDateFormat("HH:mm:ss").format(dateTime);

		String[] tis = time.split(":");

		int hour = Integer.parseInt(tis[0]);
		int minute = Integer.parseInt(tis[1]);

		int secend = 0;
		if (tis.length > 2) {
			secend = Integer.parseInt(tis[2]);
		}


		minu = hour * 3600 + minute * 60 + secend;

		return minu;
	}

	//获得今日凌晨
	private static Date getToday() {
		return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	}

	//获得明日凌晨
	private static Date getTomorrow() {
		long getTomorrowInterval = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH).getTime() + 24 * 3600 * 1000;
		return new Date(getTomorrowInterval);
	}
}
