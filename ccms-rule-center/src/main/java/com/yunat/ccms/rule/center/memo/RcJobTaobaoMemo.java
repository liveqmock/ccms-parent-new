package com.yunat.ccms.rule.center.memo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "rc_job_taobao_memo")
public class RcJobTaobaoMemo implements Serializable {

	private static final long serialVersionUID = -4785224153069226667L;

	/**
	 * 订单号
	 */
	@Id
	@Column(name = "tid")
	private String tid;

	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private String shopId;

	/**
	 * 上一次备注内容
	 */
	@Column(name = "last_memo")
	private String lastMemo;

	/**
	 * 规则引擎输出的备注内容
	 */
	@Column(name = "memo")
	private String memo;

	/**
	 * 备注任务状态
	 */
	@Column(name = "status")
	private String status;

	/**
	 * 错误描述
	 */
	@Column(name = "error_msg")
	private String errorMsg;

	/**
	 * 提交备注任务时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submit_time")
	private Date submitTime;

	/**
	 * 开始处理备注任务时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * 结束处理备注任务时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	private Date endTime;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getLastMemo() {
		return lastMemo;
	}

	public void setLastMemo(String lastMemo) {
		this.lastMemo = lastMemo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
