package com.yunat.ccms.tradecenter.controller.vo;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;

/**
 * 催付订单成功记录
 *
 * @author ming.peng
 * @date 2013-6-9
 * @since 4.1.0
 */
public class UrPayOrdersLogVo extends Pagination<Map<String, Object>> {

	private double totalAmount;// 回收总金额
	private String endDateTime; // 催付成功订单三日内的结束时间
	private String startDateTime; // 催付成功订单三日内的起始时间

	public UrPayOrdersLogVo() {
		Calendar date = Calendar.getInstance();
		endDateTime = DateFormatUtils.format(date, "yyyy年MM月dd日");
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 3);
		startDateTime = DateFormatUtils.format(date, "yyyy年MM月dd日");
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setContent(List<Map<String, Object>> content) {
		if (CollectionUtils.isNotEmpty(content)){
			Map<Integer, String> types = UserInteractionType.getTypeMsgMap();
			for (Map<String, Object> item : content) {
				item.put("type", types.get(item.get("type")));
			}
		}
		super.setContent(content);
	}

}
