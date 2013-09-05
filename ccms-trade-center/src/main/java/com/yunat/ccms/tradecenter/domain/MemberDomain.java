package com.yunat.ccms.tradecenter.domain;

import com.yunat.ccms.tradecenter.support.cons.MemberType;

import java.util.Date;

import javax.persistence.*;


/**
 *
 * 会员等级表对象
 * @author shaohui.li
 * @version $Id: MemberDomain.java, v 0.1 2013-6-3 下午03:57:17 shaohui.li Exp $
 */
@Entity
@Table(name = "plt_taobao_crm_member")
public class MemberDomain extends BaseDomain{

    /**  */
    private static final long serialVersionUID = -151266765331521438L;

    @EmbeddedId
    MemberDomainPK id;

    /** 会员状态 **/
    @Column(name = "status")
    private String status;

    /** 会员等级 **/
    @Column(name = "grade")
    private String grade;

    /** 交易成功笔数 **/
    @Column(name = "trade_count")
    private Long tradeCount;

    /** 交易金额 **/
    @Column(name = "trade_amount")
    private Double tradeAmount;

    /** 最后交易时间 **/
    @Column(name = "last_trade_time")
    private Date lastTradeTime;

    @Transient
    public String getGradeDes() {

        String gradeDes = "";
        if ("0".equals(grade)) {
            if (tradeCount != null && tradeCount > 0) {
                gradeDes = "未分级";
            } else {
                gradeDes = "新用户";
            }
        } else {
            gradeDes = MemberType.getMessage(grade);
        }

        return gradeDes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public Double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Date getLastTradeTime() {
        return lastTradeTime;
    }

    public void setLastTradeTime(Date lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    public MemberDomainPK getId() {
        return id;
    }

    public void setId(MemberDomainPK id) {
        this.id = id;
    }
}
