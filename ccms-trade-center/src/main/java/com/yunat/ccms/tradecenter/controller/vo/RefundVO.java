package com.yunat.ccms.tradecenter.controller.vo;

import java.util.List;

import com.yunat.ccms.tradecenter.controller.BaseVO;

public class RefundVO extends BaseVO {
    private String reason;
    private String refundDesc;
    private Boolean hasGoodReturn;
    private String orderStatus;
    private String status;
    private String buyerNick;
    private String tid;
    private String oid;
    private String refundId;
    private Integer csStatus;
    private String title;
    private Double refundFee;
    private String numIid;
    private String payment;
    private String shippingStatus;
    private String companyName;
    private String grade;
    private String timeout;
    private String created;
    private String receiverMobile;
    private Boolean isCare;
    private Boolean refundFollowup;
    private String abnormalStatus;
    private String abnormalReason;
    private String picPath;
    private String skuPropertiesName;
    private List<SendLogVO> careLogs;
    private Integer followupStatus;
    private Long followupId;

    /** 买家留言对象  **/
    private TradeMemoVO memoVo;

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public Boolean getHasGoodReturn() {
        return hasGoodReturn;
    }
    public void setHasGoodReturn(Boolean hasGoodReturn) {
        this.hasGoodReturn = hasGoodReturn;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getBuyerNick() {
        return buyerNick;
    }
    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }
    public String getTid() {
        return tid;
    }
    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getRefundId() {
        return refundId;
    }
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public Integer getCsStatus() {
        return csStatus;
    }
    public void setCsStatus(Integer csStatus) {
        this.csStatus = csStatus;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getNumIid() {
        return numIid;
    }
    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Double refundFee) {
        this.refundFee = refundFee;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public Boolean getIsCare() {
		return isCare;
	}
	public void setIsCare(Boolean isCare) {
		this.isCare = isCare;
	}

    public Boolean getRefundFollowup() {
        return refundFollowup;
    }

    public void setRefundFollowup(Boolean refundFollowup) {
        this.refundFollowup = refundFollowup;
    }

    public String getAbnormalStatus() {
		return abnormalStatus;
	}
	public void setAbnormalStatus(String abnormalStatus) {
		this.abnormalStatus = abnormalStatus;
	}
	public String getAbnormalReason() {
		return abnormalReason;
	}
	public void setAbnormalReason(String abnormalReason) {
		this.abnormalReason = abnormalReason;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getSkuPropertiesName() {
		return skuPropertiesName;
	}
	public void setSkuPropertiesName(String skuPropertiesName) {
		this.skuPropertiesName = skuPropertiesName;
	}

    public List<SendLogVO> getCareLogs() {
        return careLogs;
    }

    public void setCareLogs(List<SendLogVO> careLogs) {
        this.careLogs = careLogs;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public Integer getFollowupStatus() {
        return followupStatus;
    }

    public void setFollowupStatus(Integer followupStatus) {
        this.followupStatus = followupStatus;
    }

    public Long getFollowupId() {
        return followupId;
    }

    public void setFollowupId(Long followupId) {
        this.followupId = followupId;
    }

    public TradeMemoVO getMemoVo() {
        return memoVo;
    }

    public void setMemoVo(TradeMemoVO memoVo) {
        this.memoVo = memoVo;
    }
}
