/**
 *
 */
package com.yunat.ccms.tradecenter.controller.vo;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-5-30 下午05:00:15
 */
public class UrpaySummaryListRequest {

	/**
	 * 催付类型: 1：自动催付 2：预关闭催付 3：聚划算催付
	 * */
	private Integer urpayType;

	/**
	 * 店铺ID
	 * */
	private String dpId;

	public Integer getUrpayType() {
		return urpayType;
	}

	public void setUrpayType(Integer urpayType) {
		this.urpayType = urpayType;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

}
