/**
 *
 */
package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xiahui.zhang
 *
 * @version 创建时间：2013-5-30 下午01:38:21
 */
@Entity
@Table(name = "tb_tc_urpay_status")
public class UrpayStatusDomain extends BaseDomain{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 * */
	private String tid;

	/**
	 * 数据更新时间
	 * */
	private Date updated;

	/**
	 * 数据创建时间
	 * */
	private Date created;

	/**
	 * 自动催付状态，  0：默认，1：已催付，2：次日催付
	 * */
	private Integer autoUrpayStatus;

	/**
	 * 自动催付扫描线程,记录扫描线程ID，时间戳
	 * */
	private String autoUrpayThread;

	/**
	 * 预关闭催付状态,0：默认，1：已催付，2：次日催付
	 * */
	private Integer closeUrpayStatus;

	/**
	 * 预关闭催付扫描线程,记录扫描线程ID，时间戳。
	 * */
	private String closeUrpayThread;

	/**
	 * 聚划算催付状态,0：默认，1：已催付，2：次日催付
	 * */
	private Integer cheapUrpayStatus;

	/**
	 * 聚划算催付扫描线程，记录扫描线程ID，时间戳。
	 * */
	private String cheapUrpayThread;

	/**
	 * 手动催付状态;默认0，催付1
	 * */
	private Integer manualUrpayStatus;

	@Id
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "auto_urpay_status")
	public Integer getAutoUrpayStatus() {
		return autoUrpayStatus;
	}

	public void setAutoUrpayStatus(Integer autoUrpayStatus) {
		this.autoUrpayStatus = autoUrpayStatus;
	}


	@Column(name = "auto_urpay_thread")
	public String getAutoUrpayThread() {
		return autoUrpayThread;
	}

	public void setAutoUrpayThread(String autoUrpayThread) {
		this.autoUrpayThread = autoUrpayThread;
	}

	@Column(name = "close_urpay_status")
	public Integer getCloseUrpayStatus() {
		return closeUrpayStatus;
	}

	public void setCloseUrpayStatus(Integer closeUrpayStatus) {
		this.closeUrpayStatus = closeUrpayStatus;
	}

	@Column(name = "close_urpay_thread")
	public String getCloseUrpayThread() {
		return closeUrpayThread;
	}

	public void setCloseUrpayThread(String closeUrpayThread) {
		this.closeUrpayThread = closeUrpayThread;
	}

	@Column(name = "cheap_urpay_status")
	public Integer getCheapUrpayStatus() {
		return cheapUrpayStatus;
	}

	public void setCheapUrpayStatus(Integer cheapUrpayStatus) {
		this.cheapUrpayStatus = cheapUrpayStatus;
	}

	@Column(name = "cheap_urpay_thread")
	public String getCheapUrpayThread() {
		return cheapUrpayThread;
	}

	public void setCheapUrpayThread(String cheapUrpayThread) {
		this.cheapUrpayThread = cheapUrpayThread;
	}

	@Column(name = "manual_urpay_status")
	public Integer getManualUrpayStatus() {
		return manualUrpayStatus;
	}

	public void setManualUrpayStatus(Integer manualUrpayStatus) {
		this.manualUrpayStatus = manualUrpayStatus;
	}



}
