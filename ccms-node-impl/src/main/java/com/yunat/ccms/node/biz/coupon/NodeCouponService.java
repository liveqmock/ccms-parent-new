package com.yunat.ccms.node.biz.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NodeCouponService {

	@Autowired
	private NodeCouponRepository nodeCouponRepository;

	public NodeCoupon getNodeCoupon(Long nodeId) {
		return nodeCouponRepository.findOne(nodeId);
	}

	public NodeCoupon saveNodeCoupon(NodeCoupon nodeCoupon) {
		return nodeCouponRepository.saveAndFlush(nodeCoupon);
	}

	public void delete(Long nodeId) {
		nodeCouponRepository.delete(nodeId);
	}

}
