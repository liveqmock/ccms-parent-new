package com.yunat.ccms.tradecenter.domain;

import java.util.Date;

import javax.persistence.Transient;

/**
 *
 * 退款订单Domain
 *
 * @author shaohui.li
 * @version $Id: RefundOrderDomain.java, v 0.1 2013-7-15 下午03:15:45 shaohui.li Exp $
 */
public class RefundOrderDomain extends OrderDomain{

    /**  */
    private static final long serialVersionUID = 8104339966231158425L;

    /**  退款时间 **/
    @Transient
    private Date refundTime;

    /** 退款金额 **/
    @Transient
    private Double refundFee;

    /** 退款成功时间 **/
    @Transient
    private Date successTime;

    /** 子订单id **/
    @Transient
    private String oid;

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Double getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Double refundFee) {
        this.refundFee = refundFee;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }
}
