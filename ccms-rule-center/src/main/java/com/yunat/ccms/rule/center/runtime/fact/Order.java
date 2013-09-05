package com.yunat.ccms.rule.center.runtime.fact;

import java.util.List;

import com.yunat.ccms.rule.center.engine.Fact;

public class Order extends Fact<Long> {

	private static final long serialVersionUID = 6374187931906755025L;

	/**
	 * 注意，默认值为非法值，如果该字段未被填充，说明该字段未取到
	 */
	/**
	 * 订单号
	 */
	private String tid;
	/**
	 * 交易来源
	 */
	private String tradeFrom;
	/**
	 * 实付金额
	 */
	private Double payment;
	/**
	 * 商品总数
	 */
	private Long productCount;
	/**
	 * 商品件数
	 */
	private Long productAmount;
	/**
	 * 收货人所在省份
	 */
	private String receiverLocation;
	/**
	 * 订单优惠金额
	 */
	private Double discountFee;
	/**
	 * 邮费
	 */
	private Double postFee;
	/**
	 * 包含商品
	 */
	private List<String> hasProducts; // numIid集

	private Customer customer = new Customer();

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Long getProductCount() {
		return productCount;
	}

	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}

	public Long getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(Long productAmount) {
		this.productAmount = productAmount;
	}

	public String getReceiverLocation() {
		return receiverLocation;
	}

	public void setReceiverLocation(String receiverLocation) {
		this.receiverLocation = receiverLocation;
	}

	public Double getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(Double discountFee) {
		this.discountFee = discountFee;
	}

	public Double getPostFee() {
		return postFee;
	}

	public void setPostFee(Double postFee) {
		this.postFee = postFee;
	}

	public List<String> getHasProducts() {
		return hasProducts;
	}

	public void setHasProducts(List<String> hasProducts) {
		this.hasProducts = hasProducts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tid == null) ? 0 : tid.hashCode());
		return result;
	}

}