package com.yunat.ccms.node.biz.coupon;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.channel.support.ChannelEntity;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.core.support.annotation.Descriptor;
import com.yunat.ccms.node.biz.coupon.taobao.TaobaoCoupon;
import com.yunat.ccms.workflow.domain.Node;

/**
 * 优惠券节点(淘宝)
 * 
 * @author xiaojing.qu
 * 
 */

@Entity
@Table(name = "twf_node_coupon")
@Descriptor(type = NodeCoupon.TYPE, validatorClass = com.yunat.ccms.node.biz.coupon.NodeCouponValidator.class,
		handlerClass = com.yunat.ccms.node.biz.coupon.NodeCouponHandler.class,
		processorClass = com.yunat.ccms.node.biz.coupon.NodeCouponProcessor.class)
public class NodeCoupon implements Serializable, ChannelEntity {

	/***  */
	private static final long serialVersionUID = 1L;

	public static final String TYPE = "tcommunicateEC";

	@Id
	@Column(name = "node_id", nullable = false, updatable = false)
	private Long nodeId; // 节点ID

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "node_id", referencedColumnName = "node_id", nullable = false)
	private Node node;

	/*** 需要发送优惠券的店铺 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "shop_id", referencedColumnName = "shop_id", nullable = false)
	private TaobaoShop shop;

	/*** 需要发送的优惠券 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "coupon_id", referencedColumnName = "coupon_id", nullable = false)
	private TaobaoCoupon coupon;

	/*** 优惠券渠道 */
	@Column(name = "channel_id", nullable = false)
	private Long channelId;

	/*** 测试客户 */
	@Column(name = "preview_customers", nullable = true, length = 512)
	private String previewCustomers;

	/*** 输出控制，是否输出发送失败的用户 */
	@Column(name = "output_control", nullable = false)
	private Integer outputControl;

	/*** 备注 */
	@Column(name = "remark", nullable = true, length = 256)
	private String remark;

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public TaobaoShop getShop() {
		return shop;
	}

	public void setShop(TaobaoShop shop) {
		this.shop = shop;
	}

	public TaobaoCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(TaobaoCoupon coupon) {
		this.coupon = coupon;
	}

	public String getPreviewCustomers() {
		return previewCustomers;
	}

	public void setPreviewCustomers(String previewCustomers) {
		this.previewCustomers = previewCustomers;
	}

	public Integer getOutputControl() {
		return outputControl;
	}

	public void setOutputControl(Integer outputControl) {
		this.outputControl = outputControl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Override
	public Long getChannelType() {
		return ChannelType.EC.getCode();
	}
}
