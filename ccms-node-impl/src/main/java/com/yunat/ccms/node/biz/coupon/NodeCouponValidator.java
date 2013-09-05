package com.yunat.ccms.node.biz.coupon;

import org.springframework.stereotype.Component;

import com.yunat.ccms.node.biz.coupon.taobao.TaobaoCoupon;
import com.yunat.ccms.node.spi.support.NodeValidationContext;
import com.yunat.ccms.node.spi.support.NodeValidator;
import com.yunat.ccms.node.spi.support.ValidateMessage;

@Component
public class NodeCouponValidator implements NodeValidator<NodeCoupon> {

	@Override
	public ValidateMessage validate(NodeCoupon nodeCoupon, NodeValidationContext validateContext) {
		Long nodeId = validateContext.getNodeId();
		// 没有节点数据
		if (nodeCoupon == null) {
			return ValidateMessage.forNodeError("节点未配置", nodeId);
		}
		TaobaoCoupon taobaoCoupon = nodeCoupon.getCoupon();
		if (taobaoCoupon == null) {
			return ValidateMessage.forNodeError("未设置优惠券", nodeId);
		}
		if (!taobaoCoupon.isEnable()) {
			return ValidateMessage.forNodeError("设置的优惠券已被禁用", nodeId);
		}
		if (!taobaoCoupon.isAvailable()) {
			return ValidateMessage.forNodeError("设置的优惠券已失效", nodeId);
		}
		return null;
	}
}
