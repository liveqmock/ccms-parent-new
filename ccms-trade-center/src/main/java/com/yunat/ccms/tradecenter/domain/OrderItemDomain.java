package com.yunat.ccms.tradecenter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * 子订单表
 * @author 李卫林
 */

@Entity
@Table(name = "plt_taobao_order_item_tc")
public class OrderItemDomain extends BaseDomain{

	/**
	 *
	 */
	private static final long serialVersionUID = 5423055654501892045L;

	/**
	 * 子订单编号
	 */
	private String oid;

	/**
	 * 主订单编号
	 */
	private String tid;


	/**
	 * 店铺ID
	 */
	private String dpId;

	/**
	 * 应付金额
	 */
	private Double totalFee;

	/**
	 * 订单优惠金额
	 */
	private Double discountFee;

	/**
	 * 手工调整金额
	 */
	private Double adjustFee;

	/**
	 * 子订单实付金额
	 */
	private Double payment;

	/**
	 * 商品单价
	 */
	private Double price;

	/**
	 * 订单状态
	 */
	private String status;

	/**
	 * 购买数量
	 */
	private Integer num;

	/**
	 * 商品数字ID
	 */
	private String numIid;

	/**
	 * 退款状态
	 * 可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)
	 */
	private String refundStatus;

	/**
	 * 商品标题
	 */
	private String title;


	/**
	 *  商品图片URl
	 */
	private String picPath;

	/**
	 * sku信息
	 */
	private String skuPropertiesName;

	@Id
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	@Column(name="dp_id")
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	@Column(name="total_fee")
	public Double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	@Column(name="discount_fee")
	public Double getDiscountFee() {
		return discountFee;
	}
	public void setDiscountFee(Double discountFee) {
		this.discountFee = discountFee;
	}

	@Column(name="adjust_fee")
	public Double getAdjustFee() {
		return adjustFee;
	}
	public void setAdjustFee(Double adjustFee) {
		this.adjustFee = adjustFee;
	}

	public Double getPayment() {
		return payment;
	}
	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}

	@Column(name="num_iid")
	public String getNumIid() {
		return numIid;
	}
	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}

	@Column(name="refund_status")
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="pic_path")
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	@Column(name="sku_properties_name")
	public String getSkuPropertiesName() {
		return skuPropertiesName;
	}
	public void setSkuPropertiesName(String skuPropertiesName) {
		this.skuPropertiesName = skuPropertiesName;
	}


}
