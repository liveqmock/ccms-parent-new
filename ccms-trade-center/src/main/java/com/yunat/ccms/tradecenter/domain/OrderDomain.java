package com.yunat.ccms.tradecenter.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

// TODO: Auto-generated Javadoc
/**
 * 主订单表.
 *
 * @author 李卫林
 */

@Entity
@Table(name = "plt_taobao_order_tc")
public class OrderDomain extends BaseDomain{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7688327266383466579L;

	/** 订单id. */
	private String tid;

	/** 店铺Id. */
	private String dpId;

	/** 客户ID. */
	private String customerno;

	/** 交易创建时间. */
	private Date created;

	/** 交易结束时间. */
	private Date endtime;

	/** 交易状态 TRADE_NO_CREATE_PAY(没创建支付宝交易),WAIT_BUYER_PAY,WAIT_SELLER_SEND_GOODS,WAIT_BUYER_CONFIRM_GOODS, TRADE_BUYER_SIGNED(买家已签收,货到付款专用),TRADE_FINISHED,TRADE_CLOSED(付款以后用户退款成功，交易自动关闭),TRADE_CLOSED_BY_TAOBAO. */
	private String status;

	/** 交易来源 WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算). */
	private String tradeFrom;

	/** 付款时间. */
	private Date payTime;

	/** 卖家发货时间. */
	private Date consignTime;

	/** 订单修改时间. */
	private Date modified;

	/**
	 * 实付金额
	 * 精确到2位小数;单位:元。如:200.07，表示:200元7分
	 */
	private Double payment;

	/** 邮费. */
	private Double postFee;

	/** 创建交易时的物流方式 交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。可选值：ems, express, post, free, virtual. */
	private String shippingType;

	/** 收货人的姓名. */
	private String receiverName;

	/** 收货人的所在省份. */
	private String receiverState;

	/** 收货人的所在城市. */
	private String receiverCity;

	/** 收货人的所在地区. */
	private String receiverDistrict;

	/** 收货人的详细地址. */
	private String receiverAddress;

	/** 收货人的邮编. */
	private String receiverZip;

	/** 收货人的手机号码. */
	private String receiverMobile;

	/** 收货人的电话号码. */
	private String receiverPhone;

	/** 商品数量总计. */
	private Integer num;

	/** 买家留言. */
	private String buyerMessage;

	/** 卖家备注. */
	private String sellerMemo;

	/** 卖家备注旗帜. */
	private Integer sellerFlag;

	/** 订单状态. */
	private Integer orderStatus;

	/** 订单记录创建时间 *. */
	private Date orderCreated;

	/** 订单记录最后修改时间 *. */
	private Date orderUpdated;


	/** 订单延长的超期时间. */
	private Date timeoutActionTime;


	/** The buyer interaction statistic domain. */
	private BuyerInteractionStatisticDomain buyerInteractionStatisticDomain;

	/** The order item domains. */
	private List<OrderItemDomain> orderItemDomains;

	/** The urpay status domains. */
	private List<UrpayStatusDomain> urpayStatusDomains;

	/** The customer orders ship domain. */
	private CustomerOrdersShipDomain customerOrdersShipDomain;

	/** The member domain. */
	private MemberDomain memberDomain;

	/** The send log domains. */
	private List<SendLogDomain> sendLogDomains;

	/** 自动催付状态 **/
	private Integer autoUrpayStatus;

	@Transient
	public Integer getAutoUrpayStatus() {
        return autoUrpayStatus;
    }

    public void setAutoUrpayStatus(Integer autoUrpayStatus) {
        this.autoUrpayStatus = autoUrpayStatus;
    }

    /**
	 * Gets the timeout action time.
	 *
	 * @return the timeout action time
	 */
	@Column(name = "timeout_action_time")
	public Date getTimeoutActionTime() {
		return timeoutActionTime;
	}

	/**
	 * Sets the timeout action time.
	 *
	 * @param timeoutActionTime the new timeout action time
	 */
	public void setTimeoutActionTime(Date timeoutActionTime) {
		this.timeoutActionTime = timeoutActionTime;
	}

	/**
	 * Gets the buyer interaction statistic domain.
	 *
	 * @return the buyer interaction statistic domain
	 */
	@Transient
	public BuyerInteractionStatisticDomain getBuyerInteractionStatisticDomain() {
		return buyerInteractionStatisticDomain;
	}

	/**
	 * Sets the buyer interaction statistic domain.
	 *
	 * @param buyerInteractionStatisticDomain the new buyer interaction statistic domain
	 */
	public void setBuyerInteractionStatisticDomain(BuyerInteractionStatisticDomain buyerInteractionStatisticDomain) {
		this.buyerInteractionStatisticDomain = buyerInteractionStatisticDomain;
	}

	/**
	 * Gets the order item domains.
	 *
	 * @return the order item domains
	 */
	@Transient
	public List<OrderItemDomain> getOrderItemDomains() {
		return orderItemDomains;
	}

	/**
	 * Sets the order item domains.
	 *
	 * @param orderItemDomains the new order item domains
	 */
	public void setOrderItemDomains(List<OrderItemDomain> orderItemDomains) {
		this.orderItemDomains = orderItemDomains;
	}

	/**
	 * Gets the urpay status domains.
	 *
	 * @return the urpay status domains
	 */
	@Transient
	public List<UrpayStatusDomain> getUrpayStatusDomains() {
		return urpayStatusDomains;
	}

	/**
	 * Sets the urpay status domains.
	 *
	 * @param urpayStatusDomains the new urpay status domains
	 */
	public void setUrpayStatusDomains(List<UrpayStatusDomain> urpayStatusDomains) {
		this.urpayStatusDomains = urpayStatusDomains;
	}

	/**
	 * Gets the customer orders ship domain.
	 *
	 * @return the customer orders ship domain
	 */
	@Transient
	public CustomerOrdersShipDomain getCustomerOrdersShipDomain() {
		return customerOrdersShipDomain;
	}

	/**
	 * Sets the customer orders ship domain.
	 *
	 * @param customerOrdersShipDomain the new customer orders ship domain
	 */
	public void setCustomerOrdersShipDomain(CustomerOrdersShipDomain customerOrdersShipDomain) {
		this.customerOrdersShipDomain = customerOrdersShipDomain;
	}

	/**
	 * Gets the member domain.
	 *
	 * @return the member domain
	 */
	@Transient
	public MemberDomain getMemberDomain() {
		return memberDomain;
	}

	/**
	 * Sets the member domain.
	 *
	 * @param memberDomain the new member domain
	 */
	public void setMemberDomain(MemberDomain memberDomain) {
		this.memberDomain = memberDomain;
	}

	/**
	 * Gets the send log domains.
	 *
	 * @return the send log domains
	 */
	@Transient
	public List<SendLogDomain> getSendLogDomains() {
		return sendLogDomains;
	}

	/**
	 * Sets the send log domains.
	 *
	 * @param sendLogDomains the new send log domains
	 */
	public void setSendLogDomains(List<SendLogDomain> sendLogDomains) {
		this.sendLogDomains = sendLogDomains;
	}

	/**
	 * Gets the tid.
	 *
	 * @return the tid
	 */
	@Id
	public String getTid() {
		return tid;
	}

	/**
	 * Sets the tid.
	 *
	 * @param tid the new tid
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * Gets the dp id.
	 *
	 * @return the dp id
	 */
	@Column(name = "dp_id")
	public String getDpId() {
		return dpId;
	}

	/**
	 * Sets the dp id.
	 *
	 * @param dpId the new dp id
	 */
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	/**
	 * Gets the customerno.
	 *
	 * @return the customerno
	 */
	public String getCustomerno() {
		return customerno;
	}

	/**
	 * Sets the customerno.
	 *
	 * @param customerno the new customerno
	 */
	public void setCustomerno(String customerno) {
		this.customerno = customerno;
	}

	/**
	 * Gets the created.
	 *
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets the created.
	 *
	 * @param created the new created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}


	/**
	 * Gets the endtime.
	 *
	 * @return the endtime
	 */
	public Date getEndtime() {
		return endtime;
	}

	/**
	 * Sets the endtime.
	 *
	 * @param endtime the new endtime
	 */
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the trade from.
	 *
	 * @return the trade from
	 */
	@Column(name = "trade_from")
	public String getTradeFrom() {
		return tradeFrom;
	}

	/**
	 * Sets the trade from.
	 *
	 * @param tradeFrom the new trade from
	 */
	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	/**
	 * Gets the pay time.
	 *
	 * @return the pay time
	 */
	@Column(name = "pay_time")
	public Date getPayTime() {
		return payTime;
	}

	/**
	 * Sets the pay time.
	 *
	 * @param payTime the new pay time
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	/**
	 * Gets the consign time.
	 *
	 * @return the consign time
	 */
	@Column(name = "consign_time")
	public Date getConsignTime() {
		return consignTime;
	}

	/**
	 * Sets the consign time.
	 *
	 * @param consignTime the new consign time
	 */
	public void setConsignTime(Date consignTime) {
		this.consignTime = consignTime;
	}

	/**
	 * Gets the modified.
	 *
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * Sets the modified.
	 *
	 * @param modified the new modified
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * Gets the payment.
	 *
	 * @return the payment
	 */
	public Double getPayment() {
		return payment;
	}

	/**
	 * Sets the payment.
	 *
	 * @param payment the new payment
	 */
	public void setPayment(Double payment) {
		this.payment = payment;
	}


	/**
	 * Gets the post fee.
	 *
	 * @return the post fee
	 */
	@Column(name = "post_fee")
	public Double getPostFee() {
		return postFee;
	}

	/**
	 * Sets the post fee.
	 *
	 * @param postFee the new post fee
	 */
	public void setPostFee(Double postFee) {
		this.postFee = postFee;
	}

	/**
	 * Gets the shipping type.
	 *
	 * @return the shipping type
	 */
	@Column(name = "shipping_type")
	public String getShippingType() {
		return shippingType;
	}

	/**
	 * Sets the shipping type.
	 *
	 * @param shippingType the new shipping type
	 */
	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	/**
	 * Gets the receiver name.
	 *
	 * @return the receiver name
	 */
	@Column(name = "receiver_name")
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * Sets the receiver name.
	 *
	 * @param receiverName the new receiver name
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * Gets the receiver state.
	 *
	 * @return the receiver state
	 */
	@Column(name = "receiver_state")
	public String getReceiverState() {
		return receiverState;
	}

	/**
	 * Sets the receiver state.
	 *
	 * @param receiverState the new receiver state
	 */
	public void setReceiverState(String receiverState) {
		this.receiverState = receiverState;
	}

	/**
	 * Gets the receiver city.
	 *
	 * @return the receiver city
	 */
	@Column(name = "receiver_city")
	public String getReceiverCity() {
		return receiverCity;
	}

	/**
	 * Sets the receiver city.
	 *
	 * @param receiverCity the new receiver city
	 */
	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	/**
	 * Gets the receiver district.
	 *
	 * @return the receiver district
	 */
	@Column(name = "receiver_district")
	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	/**
	 * Sets the receiver district.
	 *
	 * @param receiverDistrict the new receiver district
	 */
	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}

	/**
	 * Gets the receiver address.
	 *
	 * @return the receiver address
	 */
	@Column(name = "receiver_address")
	public String getReceiverAddress() {
		return receiverAddress;
	}

	/**
	 * Sets the receiver address.
	 *
	 * @param receiverAddress the new receiver address
	 */
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	/**
	 * Gets the receiver zip.
	 *
	 * @return the receiver zip
	 */
	@Column(name = "receiver_zip")
	public String getReceiverZip() {
		return receiverZip;
	}

	/**
	 * Sets the receiver zip.
	 *
	 * @param receiverZip the new receiver zip
	 */
	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}

	/**
	 * Gets the receiver mobile.
	 *
	 * @return the receiver mobile
	 */
	@Column(name = "receiver_mobile")
	public String getReceiverMobile() {
		return receiverMobile;
	}

	/**
	 * Sets the receiver mobile.
	 *
	 * @param receiverMobile the new receiver mobile
	 */
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	/**
	 * Gets the receiver phone.
	 *
	 * @return the receiver phone
	 */
	@Column(name = "receiver_phone")
	public String getReceiverPhone() {
		return receiverPhone;
	}

	/**
	 * Sets the receiver phone.
	 *
	 * @param receiverPhone the new receiver phone
	 */
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	/**
	 * Gets the num.
	 *
	 * @return the num
	 */
	public Integer getNum() {
		return num;
	}

	/**
	 * Sets the num.
	 *
	 * @param num the new num
	 */
	public void setNum(Integer num) {
		this.num = num;
	}

	/**
	 * Gets the buyer message.
	 *
	 * @return the buyer message
	 */
	@Column(name = "buyer_message")
	public String getBuyerMessage() {
		return buyerMessage;
	}

	/**
	 * Sets the buyer message.
	 *
	 * @param buyerMessage the new buyer message
	 */
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	/**
	 * Gets the seller memo.
	 *
	 * @return the seller memo
	 */
	@Column(name = "seller_memo")
	public String getSellerMemo() {
		return sellerMemo;
	}

	/**
	 * Sets the seller memo.
	 *
	 * @param sellerMemo the new seller memo
	 */
	public void setSellerMemo(String sellerMemo) {
		this.sellerMemo = sellerMemo;
	}

	/**
	 * Gets the seller flag.
	 *
	 * @return the seller flag
	 */
	@Column(name = "seller_flag")
	public Integer getSellerFlag() {
		return sellerFlag;
	}

	/**
	 * Sets the seller flag.
	 *
	 * @param sellerFlag the new seller flag
	 */
	public void setSellerFlag(Integer sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	/**
	 * Gets the order status.
	 *
	 * @return the order status
	 */
	@Column(name = "order_status")
	public Integer getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Sets the order status.
	 *
	 * @param orderStatus the new order status
	 */
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * Gets the order created.
	 *
	 * @return the order created
	 */
	@Column(name = "order_created")
    public Date getOrderCreated() {
        return orderCreated;
    }

    /**
     * Sets the order created.
     *
     * @param orderCreated the new order created
     */
    public void setOrderCreated(Date orderCreated) {
        this.orderCreated = orderCreated;
    }

    /**
     * Gets the order updated.
     *
     * @return the order updated
     */
    @Column(name = "order_updated")
    public Date getOrderUpdated() {
        return orderUpdated;
    }

    /**
     * Sets the order updated.
     *
     * @param orderUpdated the new order updated
     */
    public void setOrderUpdated(Date orderUpdated) {
        this.orderUpdated = orderUpdated;
    }

}
