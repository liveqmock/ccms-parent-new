/**
 *
 */
package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xiahui.zhang
 *
 * @version 创建时间：2013-5-30 上午11:20:36
 */
@Entity
@Table(name = "tb_tc_urpay_summary")
public class UrpaySummaryDomain extends BaseDomain{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 * */
	private Long pkid;

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
	 * 催付订单数
	 * */
	private Integer orderNum;

	/**
	 * 催付响应订单数据
	 * */
	private Integer responseNum;

	/**
	 * 催付响应金额
	 * */
	private Double responseAmount;

	/**
	 * 发送短信数
	 * */
	private Integer sendNum;

	/**
	 * 创建时间
	 * */
	private Date created;

	/**
	 * 更新时间
	 * */
	private Date updated;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkid", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	@Column(name = "urpay_type")
	public Integer getUrpayType() {
		return urpayType;
	}

	public void setUrpayType(Integer urpayType) {
		this.urpayType = urpayType;
	}

	@Column(name = "dp_id")
	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	@Column(name = "urpay_date")
	public String getUrpayDate() {
		return urpayDate;
	}

	public void setUrpayDate(String urpayDate) {
		this.urpayDate = urpayDate;
	}

	@Column(name = "order_num")
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "response_num")
	public Integer getResponseNum() {
		return responseNum;
	}

	public void setResponseNum(Integer responseNum) {
		this.responseNum = responseNum;
	}

	@Column(name = "response_Amount")
	public Double getResponseAmount() {
		return responseAmount;
	}

	public void setResponseAmount(Double responseAmount) {
		this.responseAmount = responseAmount;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Column(name = "send_num")
	public Integer getSendNum() {
		return sendNum;
	}

	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}






}
