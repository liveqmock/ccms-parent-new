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
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-20 下午01:45:37
 */
@Entity
@Table(name = "tb_tc_mq_info_log")
public class MQInfoLogDomain extends BaseDomain{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Long pkid;

	/**
	 * 消息内容
	 */
	private String msg;

	/**
	 * 消息（1：发送或2：接收）
	 */
	private Integer type;

	/**
	 * 消息状态（0：初始状态，1：消费成功）
	 */
	private Integer status;

	/**
	 * 数据创建时间
	 */
	private Date created;

	/**
	 * 数据更新时间
	 */
	private Date updated;

	/**
	 * 备注
	 */
	private String remark;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkid", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
