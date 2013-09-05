package com.yunat.ccms.node.biz.coupon.taobao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.external.scs.handler.TaobaoCouponHandler;
import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.domain.Channel;
import com.yunat.ccms.channel.support.service.GatewayService;
import com.yunat.ccms.channel.support.vo.GatewayInfoResponse;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.external.ChannelParamBuilder;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.core.support.vo.PageRequestVo;
import com.yunat.ccms.core.support.vo.PagedResultVo;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;

@Service
public class TaobaoCouponService {

	private static final Logger logger = LoggerFactory.getLogger(TaobaoCouponService.class);

	@Autowired
	private TaobaoCouponRepository taobaoCouponRepository;

	@Autowired
	private TaobaoCouponDenominationRepository taobaoCouponDenominationRepository;

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	@Autowired
	private TaobaoCouponHandler taobaoCouponHandler;

	@Autowired
	private AccessTokenService accessTokenService;

	@Autowired
	private GatewayService gatewayService;

	public List<TaobaoCouponDenomination> listDenominations() {
		return taobaoCouponDenominationRepository.findAll();
	}

	public TaobaoCouponDenomination getDenomination(Long denominationValue) {
		return taobaoCouponDenominationRepository.findOne(denominationValue);
	}

	public PagedResultVo<TaobaoCoupon> listByPage(final PageRequestVo pageRequest) {
		Specification<TaobaoCoupon> spec = pageRequest.getSpecification();
		Pageable pageable = pageRequest.getPageable();
		Page<TaobaoCoupon> pagedCoupons = taobaoCouponRepository.findAll(spec, pageable);
		return new PagedResultVo<TaobaoCoupon>(pagedCoupons);
	}

	public TaobaoCoupon getCoupon(Long couponId) {
		return taobaoCouponRepository.findOne(couponId);
	}

	public TaobaoCouponCreateResultVo createCoupon(TaobaoCoupon tbCoupon) {
		TaobaoCouponCreateResultVo result = new TaobaoCouponCreateResultVo();
		String tenantId = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		String tenantPassword = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
		String shopId = tbCoupon.getShop().getShopId();
		String shop_key = ChannelParamBuilder.shopKey(PlatEnum.taobao, shopId);
		String startTime = DateUtils.getStringDate(tbCoupon.getStartTime());
		String endTime = DateUtils.getStringDate(tbCoupon.getEndTime());
		Long threshold = tbCoupon.getThreshold() == null ? 0 : tbCoupon.getThreshold();
		Long denominationValue = tbCoupon.getDenomination().getDenominationValue();
		BaseResponse<String> response = taobaoCouponHandler.createCoupon(tenantId, tenantPassword, shop_key, threshold,
				denominationValue, startTime, endTime, PlatEnum.taobao);
		if (response.isSuccess()) {
			String couponId = response.getRtnData();
			logger.info("创券：{couponId}成功！相关信息输出:{}", couponId, tbCoupon);
			tbCoupon.setCouponId(Long.valueOf(couponId));
			tbCoupon = taobaoCouponRepository.saveAndFlush(tbCoupon);
			logger.info("保存优惠券成功！相关信息输出:" + tbCoupon);
			result.setSuccess(true);
			result.setResult(tbCoupon);
			return result;
		} else {
			String errorMessage = response.getErrMsg();
			logger.error("创券失败！原因:" + errorMessage + "," + tbCoupon);
			result.setErrorMsg(errorMessage);
			result.setSuccess(false);
			return result;
		}
	}

	public TaobaoCoupon updateCoupon(TaobaoCoupon coupon) {
		return taobaoCouponRepository.saveAndFlush(coupon);
	}

	public boolean getCouponAuthorization(String shopId) {
		return accessTokenService.w2Available(PlatEnum.taobao, shopId);
	}

	public List<TaobaoCoupon> getAvaliableCoupons(String shopId) {
		return taobaoCouponRepository.getAvaliableCoupons(shopId);
	}

	public List<Channel> getAvaliableChannels() {
		String tenantId = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_ID);
		String tenantPassword = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_TENANT_PASSWORD);
		List<GatewayInfoResponse> result = gatewayService.retrievalAllGateways(tenantId, tenantPassword,
				ChannelType.EC, PlatEnum.taobao);
		List<Channel> channels = new ArrayList<Channel>();
		for (GatewayInfoResponse gateway : result) {
			Channel c = new Channel();
			c.setChannelId(gateway.getGatewayId());
			c.setChannelName(gateway.getGatewayName());
			c.setChannelType(gateway.getGatewayType().longValue());
			channels.add(c);
		}
		return channels;
	}

	public String getGrandUrl() {
		return appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.TOP_CCMS_GRANT_URL);
	}

}
