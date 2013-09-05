package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * 短信待发送队列
 * @author shaohui.li
 * @version $Id: SmsTobesendDomain.java, v 0.1 2013-5-31 下午07:43:39 shaohui.li Exp $
 */

@Entity
@Table(name = "tb_tc_sms_queue")
public class SmsQueueDomain extends BaseDomain{

    /**  */
    private static final long serialVersionUID = 5693429517659639443L;

    /**主键 **/
    private Long pkid;

    /**数据创建时间 **/
    private Date created;

    /**数据更新时间 **/
    private Date updated;

    /** 订单id **/
    private String tid;

    /** 子订单id **/
    @Transient
    private String oid;

	/** 店铺id **/
    private String dpId;

    /** 买家昵称 **/
    private String buyer_nick;

    /** 下单时间 **/
    private Date trade_created;

    /** 短信内容 **/
    private String sms_content;

    /** 手机号码 **/
    private String mobile;

    /** 发送者 **/
    private String send_user;

    /** 催付类型 **/
    private Integer type;

    /** 短信通道 **/
    private Long gatewayId;

    /** 下单时间 **/
    private Date send_time;



    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pkid", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    @Transient
    public String getOid() {
		return oid;
	}

    @Transient
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

    public String getBuyer_nick() {
        return buyer_nick;
    }

    public void setBuyer_nick(String buyer_nick) {
        this.buyer_nick = buyer_nick;
    }

    public Date getTrade_created() {
        return trade_created;
    }

    public void setTrade_created(Date trade_created) {
        this.trade_created = trade_created;
    }

    public String getSms_content() {
        return sms_content;
    }

    public void setSms_content(String sms_content) {
        this.sms_content = sms_content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSend_user() {
        return send_user;
    }

    public void setSend_user(String send_user) {
        this.send_user = send_user;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "gateway_id")
    public Long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Long gatewayId) {
        this.gatewayId = gatewayId;
    }

    @Transient
    private Integer smsNum;

    @Transient
	public Integer getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Integer smsNum) {
		this.smsNum = smsNum;
	}

	public Date getSend_time() {
		return send_time;
	}

	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}



}
