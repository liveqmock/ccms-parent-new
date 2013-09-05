package com.yunat.ccms.schedule.core.trigger;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.DateTime;

import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.utils.HStringUtils;
import com.yunat.ccms.node.biz.evaluate.NodeEvaluate;
import com.yunat.ccms.node.biz.time.NodeTime;
import com.yunat.ccms.node.biz.wait.NodeWait;
import com.yunat.ccms.schedule.core.TaskTrigger;

public class TriggerFactory {

	/**
	 * 通过时间节点配置为活动创建trigger
	 * 
	 * @param jobName
	 * @param nodeTime
	 * @return
	 */
	public static TaskTrigger createCampTrigger(String jobName, NodeTime nodeTime) {
		if (nodeTime == null) {
			return new InstantTrigger();
		}
		if (nodeTime.getIscycle().intValue() == 0) {
			// 非周期性
			Date date;
			if (nodeTime.getIsrealtime().intValue() == 1) {
				// 马上执行
				return new InstantTrigger();
			} else {
				// 指定时间
				date = HStringUtils.parseDateTime(nodeTime.getRealtimebeginDate(), nodeTime.getRealtimebeginTime());
			}
			return new SimpleTrigger(jobName, date);
		} else {
			// 周期性
			String time = nodeTime.getCyclebeginTime();
			int[] hms = HStringUtils.getHMS(time);
			int hour = hms[0];
			int minute = hms[1];
			int second = hms[2];
			StringBuffer cron = new StringBuffer(second + " " + minute + " " + hour + " ");
			if (nodeTime.getCycleType().equals(NodeTime.CYCLE_TYPE_DAY)) {
				cron.append("* * ?");
			} else if (nodeTime.getCycleType().equals(NodeTime.CYCLE_TYPE_MONTH)) {
				cron.append(nodeTime.getCycleValue()).append(" * ?");
			} else if (nodeTime.getCycleType().equals(NodeTime.CYCLE_TYPE_WEEK)) {
				cron.append("? * ").append(nodeTime.getCycleValue());
			} else if (nodeTime.getCycleType().equals(NodeTime.CYCLE_TYPE_HOUR)) {
				cron = new StringBuffer(second + " " + minute + " 0/").append(nodeTime.getCycleValue().trim()).append(
						" * * ?");
			}

			Date startDate = HStringUtils.parseDateTime(nodeTime.getCyclebeginDate(), time);
			Date endDate = HStringUtils.parseDateTime(DateUtils.addDay(nodeTime.getCycleendDate(), 1), "00:00:00");

			try {
				return new CronTrigger(jobName, cron.toString(), startDate, endDate);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * 为等待节点创建trigger
	 * 
	 * @param jobName
	 * @param nodeWait
	 * @return
	 */
	public static TaskTrigger createWaitNodeTrigger(String jobName, NodeWait nodeWait) {
		if (nodeWait == null) {
			return instantTrigger();
		}
		Date date = null;
		if (nodeWait.getIsDate().intValue() == 0) {
			date = HStringUtils.parseDateTime(nodeWait.getWaitdate(), nodeWait.getWaittime());
		} else if (nodeWait.getIsDate().intValue() == 1) {
			date = DateUtils.addDay(HStringUtils.parseDateTime(new Date(), nodeWait.getWaittime()), nodeWait
					.getWaitday().intValue());
		} else if (nodeWait.getIsDate().intValue() == 2) {
			date = new Date();
			int hour = nodeWait.getWaithour();
			int minute = nodeWait.getWaitminute();
			date = DateUtils.addHour(date, hour);
			date = DateUtils.addMinute(date, minute);
		}
		return new SimpleTrigger(jobName, date);
	}

	/**
	 * 为效果评估节点创建trigger
	 * 
	 * @param jobName
	 * @param config
	 * @return
	 */
	public static TaskTrigger createEvaluateNodeTrigger(String jobName, NodeEvaluate nodeEvaluate) {
		if (nodeEvaluate == null) {
			return instantTrigger();
		}
		// 前面节点的渠道发送时间默认当前时间
		int evaluateCycle = nodeEvaluate.getEvaluateCycle();
		DateTime dateTime = new DateTime();
		Date startDate = dateTime.plusMinutes(10).toDate();// 防止第一次立刻执行
		Date endDate = dateTime.plusDays(evaluateCycle).toDate();
		dateTime = dateTime.plusHours(12);
		StringBuffer cron = new StringBuffer();
		int second = dateTime.getSecondOfMinute();
		cron.append(second + " ");
		int minute = dateTime.getMinuteOfHour();
		cron.append(minute + " ");
		int hour = dateTime.getHourOfDay();
		cron.append(hour + "/12 ");
		cron.append(" * ");// day of month
		cron.append(" * ");// month of year
		cron.append(" ? ");// day of week
		dateTime = dateTime.plusDays(evaluateCycle);

		try {
			return new CronTrigger(jobName, cron.toString(), startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 创建立刻执行的triggers
	 * 
	 * @return
	 */
	public static TaskTrigger instantTrigger() {
		return new InstantTrigger();
	}

}