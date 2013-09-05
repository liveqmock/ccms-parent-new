package com.yunat.ccms.tradecenter.controller.vo;

import java.util.List;

import com.yunat.ccms.tradecenter.controller.BaseVO;

public class OrderVO extends BaseVO {
	private String tid;
	private String serviceStaff;
	private String grade;
	private String created;

	/**
	 * 预关闭时间
	 */
	private String endTime;
	private String receiverName;
	private String receiverPhone;
	private String receiverState;
	private String receiverCity;
	private String receiverDistrict;
	private String receiverAddress;
	private String receiverZip;
	private double payment;
	private double postFee;
	private String status;
	private Integer sellerFlag;
	private String buyerMessage;
	private Boolean isHide;
    private Integer followupStatus;
    private Long followupId;
	private String tradeFrom;

    /** 买家留言对象  **/
    private TradeMemoVO memoVo;

	private Integer urpayStatus;
	private BuyerStatisVO urpayStatus0;
	private List<SendLogVO> urpayStatus1;
	private List<OrderItemVO> goodsItems;

	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getServiceStaff() {
		return serviceStaff;
	}
	public void setServiceStaff(String serviceStaff) {
		this.serviceStaff = serviceStaff;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
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
	public double getPostFee() {
		return postFee;
	}
	public void setPostFee(double postFee) {
		this.postFee = postFee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

    public String getTradeFrom() {
		return tradeFrom;
	}
	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

    public TradeMemoVO getMemoVo() {
        return memoVo;
    }

    public void setMemoVo(TradeMemoVO memoVo) {
        this.memoVo = memoVo;
    }

    public Integer getUrpayStatus() {
		return urpayStatus;
	}
	public void setUrpayStatus(Integer urpayStatus) {
		this.urpayStatus = urpayStatus;
	}
	public BuyerStatisVO getUrpayStatus0() {
		return urpayStatus0;
	}
	public void setUrpayStatus0(BuyerStatisVO urpayStatus0) {
		this.urpayStatus0 = urpayStatus0;
	}
	public List<SendLogVO> getUrpayStatus1() {
		return urpayStatus1;
	}
	public void setUrpayStatus1(List<SendLogVO> urpayStatus1) {
		this.urpayStatus1 = urpayStatus1;
	}
	public List<OrderItemVO> getGoodsItems() {
		return goodsItems;
	}
	public void setGoodsItems(List<OrderItemVO> goodsItems) {
		this.goodsItems = goodsItems;
	}
}
