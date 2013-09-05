package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * 主订单表
 *
 * @author 李卫林
 */

@Entity
@Table(name = "tb_tc_send_log")
public class SendLogDomain extends BaseDomain {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	private Long pkid;

	/**
	 * 交易id
	 */
	private String tid;

	/**
	 * 交易明细id
	 */

	private String oid;

	/**
	 * 店铺Id
	 */
	private String dpId;

	/**
	 * 卖家昵称
	 */
	private String buyerNick;

	/**
	 * 交易创建时间
	 */
	private Date tradeCreated;

	/**
	 * 短信内容
	 */
	private String smsContent;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 发送者昵称
	 */
	private String sendUser;

	/**
	 * 发送类型
	 */
	private Integer type;

	private String sendType;

	private Date updated;
	private Date created;

	private String sendTime;

	/** 短信通道 **/
	private Long gatewayId;

	/** 发送状态 **/
	private Integer sendStatus;

	private Integer smsNum;

	private String task_id;

	@Column(name = "sms_num")
	public Integer getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Integer smsNum) {
		this.smsNum = smsNum;
	}

	@Column(name = "gateway_id")
	public Long getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(Long gatewayId) {
		this.gatewayId = gatewayId;
	}

	@Column(name = "send_status")
	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}

	@Id
	@GeneratedValue
	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	@Column(name = "tid")
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	@Column(name = "oid")
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	@Column(name = "dp_id")
	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	@Column(name = "buyer_nick")
	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	@Column(name = "trade_created")
	public Date getTradeCreated() {
		return tradeCreated;
	}

	public void setTradeCreated(Date tradeCreated) {
		this.tradeCreated = tradeCreated;
	}

	@Column(name = "sms_content")
	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "send_user")
	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	@Transient
    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

	@Transient
    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
