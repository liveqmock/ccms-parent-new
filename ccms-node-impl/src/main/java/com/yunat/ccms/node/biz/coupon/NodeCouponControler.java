package com.yunat.ccms.node.biz.coupon;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.node.biz.coupon.taobao.TaobaoCoupon;
import com.yunat.ccms.node.biz.coupon.taobao.TaobaoCouponService;

/**
 * 优惠券节点
 * </br> http://wiki.yunat.com/pages/viewpage.action?pageId=15906026
 * 
 * @author xiaojing.qu
 * 
 */
@Controller
@RequestMapping(value = "/node/coupon/*")
public class NodeCouponControler {

	@Autowired
	private NodeCouponService nodeCouponService;

	@Autowired
	private TaobaoShopService taobaoShopService;

	@Autowired
	private TaobaoCouponService taobaoCouponService;

	@ResponseBody
	@RequestMapping(value = "/{nodeId}", method = RequestMethod.GET)
	public ControlerResult getNodeCoupon(@PathVariable Long nodeId) {
		NodeCoupon nodeCoupon = nodeCouponService.getNodeCoupon(nodeId);
		return ControlerResult.newSuccess(nodeCoupon);
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ControlerResult saveNodeCoupon(@RequestBody NodeCouponVo nodeCouponVo) {
		TaobaoShop shop = null;
		TaobaoCoupon coupon = null;
		try {
			Assert.notNull(nodeCouponVo.getNodeId(), "节点ID不能为空");
			Assert.notNull(nodeCouponVo.getChannelId(), "渠道ID不能为空");
			Assert.notNull(nodeCouponVo.getShopId(), "店铺ID不能为空");
			Assert.notNull(nodeCouponVo.getCouponId(), "优惠券ID不能为空");
			shop = taobaoShopService.get(nodeCouponVo.getShopId());
			Assert.notNull(shop, "店铺不存在");
			coupon = taobaoCouponService.getCoupon(nodeCouponVo.getCouponId());
			Assert.notNull(coupon, "优惠券不存在");
		} catch (IllegalArgumentException e) {
			return ControlerResult.newError("", e.getMessage());
		}

		String previewCustomers = nodeCouponVo.getPreviewCustomers();
		if (StringUtils.isNotBlank(nodeCouponVo.getPreviewCustomers())) {
			previewCustomers = StringUtils.replaceChars(previewCustomers, " ", "");
			previewCustomers = StringUtils.replaceChars(previewCustomers, "，", ",");
			previewCustomers = StringUtils.trimToNull(previewCustomers);
		}
		NodeCoupon nodeCoupon = new NodeCoupon();
		nodeCoupon.setNodeId(nodeCouponVo.getNodeId());
		nodeCoupon.setShop(shop);
		nodeCoupon.setCoupon(coupon);
		nodeCoupon.setChannelId(nodeCouponVo.getChannelId());
		nodeCoupon.setOutputControl(nodeCouponVo.getOutputControl());
		nodeCoupon.setPreviewCustomers(previewCustomers);
		nodeCoupon.setRemark(nodeCouponVo.getRemark());
		nodeCoupon = nodeCouponService.saveNodeCoupon(nodeCoupon);
		return ControlerResult.newSuccess(nodeCoupon);
	}
}
