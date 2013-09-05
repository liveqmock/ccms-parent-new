package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="plt_taobao_transitstepinfo_tc")
public class TransitstepinfoDomain extends BaseDomain {

	/**
	 *
	 */
	private static final long serialVersionUID = 2055557550718968739L;

	/**
	 * 订单ID
	 */
	private String tid;

	/**
	 * 运单号.具体一个物流公司的运单号码.
	 */
	private String outSid;

	/**
	 * 订单的物流状态 *
	 * 等候发送给物流公司 *已提交给物流公司,等待物流公司接单 *已经确认消息接收，等待物流公司接单 *物流公司已接单 *物流公司不接单 *物流公司揽收失败 *物流公司揽收成功 *签收失败 *对方已签收 *对方拒绝签收
	 */
	private String status;

	/**
	 * 解析的订单物流状态
	 */
	private Integer logisticsStatus;

	/**
	 * 物流公司名称
	 */
	private String companyName;

	/**
	 * 流转信息文件路径
	 */
	private String transitStepInfo;

	/**
	 * 流转状态：1:到同城，2、派件；3：已签收
	 */
	private Integer shippingStatus;

	/**
	 * 签收时间
	 */
	private Date signedTime;

	/**
	 * 到达同城时间
	 */
	private Date arrivedTime;

	/**
	 * 派件时间
	 */
	private Date deliveryTime;

    private Date recentlyTime;

    private String abnormalStatus;

	/**
	 *
	 */
	private Date created;

	/**
	 *
	 */
	private Date updated;


	@Id
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

	@Column(name="out_sid")
	public String getOutSid() {
		return outSid;
	}
	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="logistics_status")
	public Integer getLogisticsStatus() {
		return logisticsStatus;
	}
	public void setLogisticsStatus(Integer logisticsStatus) {
		this.logisticsStatus = logisticsStatus;
	}

	@Column(name="company_name")
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name="transit_step_info")
	public String getTransitStepInfo() {
		return transitStepInfo;
	}
	public void setTransitStepInfo(String transitStepInfo) {
		this.transitStepInfo = transitStepInfo;
	}

	@Column(name="shipping_status")
	public Integer getShippingStatus() {
		return shippingStatus;
	}
	public void setShippingStatus(Integer shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	@Column(name="signed_time")
	public Date getSignedTime() {
		return signedTime;
	}
	public void setSignedTime(Date signedTime) {
		this.signedTime = signedTime;
	}

	@Column(name="arrived_time")
	public Date getArrivedTime() {
		return arrivedTime;
	}
	public void setArrivedTime(Date arrivedTime) {
		this.arrivedTime = arrivedTime;
	}

	@Column(name="delivery_time")
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

    @Column(name="recently_time")
    public Date getRecentlyTime() {
        return recentlyTime;
    }

    public void setRecentlyTime(Date recentlyTime) {
        this.recentlyTime = recentlyTime;
    }

    @Column(name="abnormal_status")
    public String getAbnormalStatus() {
        return abnormalStatus;
    }

    public void setAbnormalStatus(String abnormalStatus) {
        this.abnormalStatus = abnormalStatus;
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
}
