package com.yunat.ccms.tradecenter.controller.vo;

import java.util.Date;
import java.util.List;

import com.yunat.ccms.tradecenter.controller.BaseVO;

public class LogisticsVO extends BaseVO {
	private String tid;
    private String outSid;
	private String endTime;
	private String consignTime;
	private String serverTime;

    private String customerno;
    private String companyName;
	private String receiverName;
	private String receiverMobile;
	private String receiverState;
	private String receiverCity;
	private String receiverDistrict;
	private String receiverAddress;
	private String receiverZip;
    private String shippingType;

    /**
     * 未同城、同城、派件、签收
     */
    private String shippingStatus;

    /**
     * 异常状态
     * 疑难件、超区件...
     */
    private String abnormalStatus;

    /**
     * 异常状态原因
     * 物流中含疑难件关键字、3天无更新
     */
    private String abnormalReason;

    /**
     * 黑名单信息
     */
    private String blacklistMessage;

	private double payment;
	private Integer sellerFlag;
	private String buyerMessage;
	private Boolean isHide;
    private Boolean logisticsFollowup;
    private Integer followupStatus;
    private Long followupId;
    private Boolean isCare;
    private String recentlyTime;
    private Date recentlyTime1;
    private Date consignTime1;

    /** 买家留言对象  **/
    private TradeMemoVO memoVo;

    private List<SendLogVO> careLogs;

	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getConsignTime() {
		return consignTime;
	}
	public void setConsignTime(String consignTime) {
		this.consignTime = consignTime;
	}
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverMobile() {
		return receiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getReceiverState() {
		return receiverState;
	}
	public void setReceiverState(String receiverState) {
		this.receiverState = receiverState;
	}
	public String getReceiverCity() {
		return receiverCity;
	}
	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}
	public String getReceiverDistrict() {
		return receiverDistrict;
	}
	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverZip() {
		return receiverZip;
	}
	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}
	public double getPayment() {
		return payment;
	}
	public void setPayment(double payment) {
		this.payment = payment;
	}
	public Integer getSellerFlag() {
		return sellerFlag;
	}
	public void setSellerFlag(Integer sellerFlag) {
		this.sellerFlag = sellerFlag;
	}
	public String getBuyerMessage() {
		return buyerMessage;
	}
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public Boolean getIsHide() {
		return isHide;
	}
	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
	}

    public Boolean getLogisticsFollowup() {
        return logisticsFollowup;
    }

    public void setLogisticsFollowup(Boolean logisticsFollowup) {
        this.logisticsFollowup = logisticsFollowup;
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

    public String getOutSid() {
        return outSid;
    }

    public void setOutSid(String outSid) {
        this.outSid = outSid;
    }

    public Date getConsignTime1() {
        return consignTime1;
    }

    public void setConsignTime1(Date consignTime1) {
        this.consignTime1 = consignTime1;
    }

    public TradeMemoVO getMemoVo() {
        return memoVo;
    }

    public void setMemoVo(TradeMemoVO memoVo) {
        this.memoVo = memoVo;
    }

    public String getCustomerno() {
        return customerno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public Boolean getIsCare() {
        return isCare;
    }

    public void setIsCare(Boolean isCare) {
        this.isCare = isCare;
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
	public String getBlacklistMessage() {
		return blacklistMessage;
	}
	public void setBlacklistMessage(String blacklistMessage) {
		this.blacklistMessage = blacklistMessage;
	}
	public Date getRecentlyTime1() {
        return recentlyTime1;
    }

    public void setRecentlyTime1(Date recentlyTime1) {
        this.recentlyTime1 = recentlyTime1;
    }
	public String getRecentlyTime() {
		return recentlyTime;
	}
	public void setRecentlyTime(String recentlyTime) {
		this.recentlyTime = recentlyTime;
	}
	public List<SendLogVO> getCareLogs() {
		return careLogs;
	}
	public void setCareLogs(List<SendLogVO> careLogs) {
		this.careLogs = careLogs;
	}
}
