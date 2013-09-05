/**
 *
 */
package com.yunat.ccms.tradecenter.domain;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-9 下午02:41:14
 */
public class CareSummaryDomain extends BaseDomain{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 催付类型: 1：自动催付 2：预关闭催付 3：聚划算催付
	 * */
	private Integer urpayType;

	/**
	 * 店铺ID
	 * */
	private String dpId;

	/**
	 * 催付日期
	 * */
	private String urpayDate;

	/**
	 * 发送短信数
	 * */
	private Integer sendNum;

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

	public String getUrpayDate() {
		return urpayDate;
	}

	public void setUrpayDate(String urpayDate) {
		this.urpayDate = urpayDate;
	}

	public Integer getSendNum() {
		return sendNum;
	}

	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}



}
