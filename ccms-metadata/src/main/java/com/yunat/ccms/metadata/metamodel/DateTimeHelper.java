package com.yunat.ccms.metadata.metamodel;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.utils.HStringUtils;

/**
 * 业务辅助类：专门用来处理绝对时间，相对时间等情形下的时间类型转换处理
 * 
 * @author kevin.jiang 2013-4-8
 */
public class DateTimeHelper {

	private static Logger logger = LoggerFactory.getLogger(DateTimeHelper.class);

	/**
	 * 转换相对日期+时间， 能处理的相对日期包括： <br>
	 * 前{m}月,第{n}号 <br>
	 * 前{m}天 <br>
	 * 前{m}分钟<br>
	 * 
	 * @param sRelativeDate
	 * @param hour
	 * @return
	 */
	public static String convertRelDateTime(String sRelativeDate, String hour) {

		Date date = DateUtils.getRelativeDate(new Date(), sRelativeDate);

		if (hour != null && !"".equals(hour)) {
			try {
				date = HStringUtils.parseDateTime(date, hour);
			} catch (Throwable e) {
				logger.error("", e);
			}
		}

		return HStringUtils.formatDatetime(date);
	}

	/**
	 * 转换相对日期， 包括前几天和后几天： <br>
	 * 前几天 type=2 <br>
	 * 后几天type=3 <br>
	 * 
	 * @param iRelative
	 * @param type
	 * @return 日期的字符串形式
	 */
	public static String convertRelDate(String sRelativeDate) {

		Date date = DateUtils.getRelativeDate(new Date(), sRelativeDate);

		return HStringUtils.formatDate(date);
	}

	/**
	 * 转换相对类型的月日, 包括前几天和后几天, 用于表达生日：<br>
	 * 前几天 type=2 <br>
	 * 后几天type=3 <br>
	 * 
	 * @param iRelative
	 * @param type
	 * @return 日期的字符串形式
	 */
	public static String convertRelMonthDay(int iRelative, int type) {

		Integer month, day;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		if (type == 2) { // 今天的前几天
			cal.add(Calendar.DAY_OF_YEAR, -iRelative);
		} else { // 今天的后几天 3
			cal.add(Calendar.DAY_OF_YEAR, iRelative);
		}
		month = cal.get(Calendar.MONTH) + 1;
		day = cal.get(Calendar.DAY_OF_MONTH);

		return month + "-" + day;
	}
}
