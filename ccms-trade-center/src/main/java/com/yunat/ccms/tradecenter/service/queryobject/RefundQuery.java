package com.yunat.ccms.tradecenter.service.queryobject;


public class RefundQuery extends BaseQuery{

	/**
	 *
	 */
	private static final long serialVersionUID = 3898171044412763323L;

    private String dpId;
	private String reason;
	private Boolean hasGoodReturn;
	private String orderStatus;
	private String buyerNick;
	private String tid;
	private String refundId;
	private String createdStartTime;
	private String createdEndTime;
	private Boolean needCustomerService;
	private String title;
	private String numIid;
    private String status;

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

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
	public String getRefundId() {
		return refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

    public String getCreatedStartTime() {
        return createdStartTime;
    }

    public void setCreatedStartTime(String createdStartTime) {
        this.createdStartTime = createdStartTime;
    }

    public String getCreatedEndTime() {
        return createdEndTime;
    }

    public void setCreatedEndTime(String createdEndTime) {
        this.createdEndTime = createdEndTime;
    }

	public Boolean getNeedCustomerService() {
		return needCustomerService;
	}

	public void setNeedCustomerService(Boolean needCustomerService) {
		this.needCustomerService = needCustomerService;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
