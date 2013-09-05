package com.yunat.ccms.node.biz.coupon;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.node.spi.support.NodeCloneHandler;

@Component
public class NodeCouponHandler implements NodeCloneHandler {

	@Autowired
	private NodeCouponService nodeCouponService;

	@Override
	public void clone(Long nodeId, Long newNodeId) {
		NodeCoupon nodeCoupon = nodeCouponService.getNodeCoupon(nodeId);
		if (nodeCoupon != null) {
			NodeCoupon newNodeCoupon = new NodeCoupon();
			BeanUtils.copyProperties(nodeCoupon, newNodeCoupon);
			newNodeCoupon.setNodeId(newNodeId);
			nodeCouponService.saveNodeCoupon(newNodeCoupon);
		}
	}

	@Override
	public void delete(Long nodeId) {
		nodeCouponService.delete(nodeId);
	}

	@Override
	public boolean updatable() {
		return false;
	}

	@Override
	public void refresh(Long nodeId, Long newNodeId) {
	}

}
