package com.yunat.ccms.tradecenter.service.queryobject;


public class LogisticsQuery extends BaseQuery{

	/**
	 *
	 */
	private static final long serialVersionUID = -1121586674246511840L;

    private String dpId;

	/**
	 * 最小在途时长
	 */
	private Integer minInTransitDuration;

	/**
	 * 是否关怀过
	 */
	private Boolean isCare;

	/**
	 * 是否隐藏
	 */
	private Boolean isHide;

	/**
	 * 物流公司名称
	 */
	private String companyName;

	/**
	 * 运单号
	 */
	private String outSid;

	/**
	 * 客户昵称
	 */
	private String customerno;

	/**
	 * 发货开始时间
	 */
	private String consignStartTime;

	/**
	 * 发货结束时间
	 */
	private String consignEndTime;

	/**
	 * 订单id
	 */
	private String tid;

	/**
	 * 物流关键字
	 */
	private String keyWord;

	/**
	 * 收件人手机号
	 */
	private String receiverMobile;

	/**
	 * 收货人省份
	 */
	private String receiverState;


    /**
     * 收货人省份列表
     */
    private String[] receiverStates;

	/**
	 * 物流状态
	 */
	private String shippingStatus;

    /**
     * 异常状态
     */
    private String abnormalStatus;

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public Integer getMinInTransitDuration() {
		return minInTransitDuration;
	}

	public void setMinInTransitDuration(Integer minInTransitDuration) {
		this.minInTransitDuration = minInTransitDuration;
	}

	public Boolean getIsCare() {
		return isCare;
	}

	public void setIsCare(Boolean isCare) {
		this.isCare = isCare;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getOutSid() {
		return outSid;
	}

	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}

	public String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	public String getConsignStartTime() {
		return consignStartTime;
	}

	public void setConsignStartTime(String consignStartTime) {
		this.consignStartTime = consignStartTime;
	}

	public String getConsignEndTime() {
		return consignEndTime;
	}

	public void setConsignEndTime(String consignEndTime) {
		this.consignEndTime = consignEndTime;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
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

    public String[] getReceiverStates() {
        return receiverStates;
    }

    public void setReceiverStates(String[] receiverStates) {
        this.receiverStates = receiverStates;
    }

    public String getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(String shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getAbnormalStatus() {
        return abnormalStatus;
    }

    public void setAbnormalStatus(String abnormalStatus) {
        this.abnormalStatus = abnormalStatus;
    }

	public Boolean getIsHide() {
		return isHide;
	}

	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
	}
}
