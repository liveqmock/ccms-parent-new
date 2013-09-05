package com.yunat.ccms.node.biz.coupon.taobao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.login.taobao.TaobaoShop;
import com.yunat.ccms.auth.login.taobao.TaobaoShopService;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.channel.support.domain.Channel;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.core.support.vo.PageRequestVo;
import com.yunat.ccms.core.support.vo.PagedResultVo;

/**
 * 淘宝优惠券管理接口
 * </br> http://wiki.yunat.com/pages/viewpage.action?pageId=15906079
 * 
 * @author xiaojing.qu
 * 
 */
@Controller
@RequestMapping(value = "/coupon/taobao/*")
public class TaobaoCouponControler {

	private static Logger logger = LoggerFactory.getLogger(TaobaoCouponControler.class);

	@Autowired
	private TaobaoCouponService taobaoCouponService;

	@Autowired
	private TaobaoShopService taobaoShopService;

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PagedResultVo<TaobaoCoupon> listCoupons(@ModelAttribute PageRequestVo pageRequest) {
		return taobaoCouponService.listByPage(pageRequest);
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ControlerResult create(@RequestBody final TaobaoCouponVo couponVo) {
		Date currentDate = new Date();
		logger.info("Try create Coupon:{}", couponVo);
		TaobaoCoupon tbCoupon;
		TaobaoCouponCreateResultVo createResult;
		try {
			Assert.notNull(couponVo, "参数为空");
			Assert.notNull(couponVo.getShopId(), "店铺不能为空");
			Assert.notNull(couponVo.getCouponName(), "优惠券名不能为空");
			Assert.notNull(couponVo.getStartTime(), "生效时间不能为空");
			Assert.notNull(couponVo.getEndTime(), "到期时间不能为空");
			Date startTime = DateUtils.getDateTime(couponVo.getStartTime());
			Date endTime = DateUtils.getDateTime(couponVo.getEndTime());
			Assert.notNull(startTime, "生效时间格式错误");
			Assert.notNull(endTime, "到期时间格式错误");
			Assert.isTrue(startTime.before(endTime), "生效时间不能晚于到期时间");
			Assert.isTrue(endTime.after(currentDate), "到期时间不能早于当前时间");
			TaobaoShop shop = taobaoShopService.get(couponVo.getShopId());
			Assert.notNull(shop, "店铺不存在");
			TaobaoCouponDenomination denomination = taobaoCouponService
					.getDenomination(couponVo.getDenominationValue());
			Assert.notNull(denomination, "优惠券面额不存在");
			if (couponVo.getThreshold() == null) {
				couponVo.setThreshold(0L);
			}
			User creator = LoginInfoHolder.getCurrentUser();
			Assert.notNull(creator, "当前未登录或者登陆超时");

			tbCoupon = new TaobaoCoupon();
			tbCoupon.setCouponName(couponVo.getCouponName());
			tbCoupon.setShop(shop);
			tbCoupon.setThreshold(couponVo.getThreshold());
			tbCoupon.setDenomination(denomination);
			tbCoupon.setCreator(creator);
			tbCoupon.setCreateTime(currentDate);
			tbCoupon.setStartTime(startTime);
			tbCoupon.setEndTime(endTime);
			tbCoupon.setEnable(true);
			tbCoupon.setRemark(couponVo.getRemark());
			createResult = taobaoCouponService.createCoupon(tbCoupon);
			if (createResult.isSuccess()) {
				return ControlerResult.newSuccess(tbCoupon);
			} else {
				return ControlerResult.newError("", createResult.getErrorMsg());
			}
		} catch (IllegalArgumentException e) {
			return ControlerResult.newError("", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ControlerResult.newError("", "创建优惠券失败");
		}

	}

	@ResponseBody
	@RequestMapping(value = "/denomination", method = RequestMethod.GET)
	public ControlerResult listDenominations() {
		List<TaobaoCouponDenomination> denominations = taobaoCouponService.listDenominations();
		Map<String, Object> map = Maps.newHashMap();
		map.put("denominations", denominations);
		return ControlerResult.newSuccess(map);
	}

	@ResponseBody
	@RequestMapping(value = "/shop/{shopId}/authorization", method = RequestMethod.GET)
	public ControlerResult getAuthorization(@PathVariable final String shopId) {
		boolean authorized = taobaoCouponService.getCouponAuthorization(shopId);
		logger.info("店铺：{},authorized：{}", shopId, authorized);
		Map<String, Object> map = Maps.newHashMap();
		map.put("authorized", authorized);
		return ControlerResult.newSuccess(map);
	}

	@ResponseBody
	@RequestMapping(value = "/shop/{shopId}", method = RequestMethod.GET)
	public ControlerResult listCoupons(@PathVariable final String shopId) {
		List<TaobaoCoupon> coupons = taobaoCouponService.getAvaliableCoupons(shopId);
		Map<String, Object> map = Maps.newHashMap();
		map.put("coupons", coupons);
		return ControlerResult.newSuccess(map);
	}

	@ResponseBody
	@RequestMapping(value = "/channel", method = RequestMethod.GET)
	public ControlerResult listChannels() {
		List<Channel> channels = taobaoCouponService.getAvaliableChannels();
		Map<String, Object> map = Maps.newHashMap();
		map.put("channels", channels);
		return ControlerResult.newSuccess(map);
	}

	@ResponseBody
	@RequestMapping(value = "/grandUrl", method = RequestMethod.GET)
	public ControlerResult getGrandUrl() {
		String grandUrl = taobaoCouponService.getGrandUrl();
		Map<String, Object> map = Maps.newHashMap();
		map.put("grandUrl", grandUrl);
		return ControlerResult.newSuccess(map);
	}

	@ResponseBody
	@RequestMapping(value = "/{couponId}", method = RequestMethod.GET)
	public ControlerResult getCoupon(@PathVariable final Long couponId) {
		TaobaoCoupon tbCoupon = taobaoCouponService.getCoupon(couponId);
		if (tbCoupon == null) {
			ControlerResult.newError("", couponId + "优惠券不存在");
		}
		return ControlerResult.newSuccess(tbCoupon);
	}

	@ResponseBody
	@RequestMapping(value = "/{couponId}", method = RequestMethod.PUT)
	public ControlerResult update(@PathVariable final Long couponId, @RequestBody final TaobaoCouponVo couponVo) {
		TaobaoCoupon tbCoupon = taobaoCouponService.getCoupon(couponId);
		if (tbCoupon == null) {
			ControlerResult.newError("", couponId + "优惠券不存在");
		}
		boolean modified = false;
		if (couponVo.isEnable() != null) {
			tbCoupon.setEnable(couponVo.isEnable());
			modified = true;
		}
		if (!StringUtils.isEmpty(couponVo.getCouponName())) {
			tbCoupon.setCouponName(couponVo.getCouponName());
			tbCoupon.setRemark(couponVo.getRemark());
			modified = true;
		}
		if (!modified) {
			return ControlerResult.newError("", "未填写相应修改参数");
		}
		taobaoCouponService.updateCoupon(tbCoupon);
		return ControlerResult.newSuccess("更新成功");
	}

}
