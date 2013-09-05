package com.yunat.ccms.ucenter.rest.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.core.support.external.ChannelParamBuilder;
import com.yunat.ccms.ucenter.rest.repository.AccessTokenRepository;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

	private static final Logger logger = LoggerFactory.getLogger(AccessTokenServiceImpl.class);

	@Autowired
	private AccessTokenRepository accessTokenRepository;

	protected Map<String, AccessToken> accessTokenMap = new ConcurrentHashMap<String, AccessToken>();;

	@Override
	public boolean w2Available(final PlatEnum plat, final String shopId) {
		final AccessToken token = accessTokenRepository.getAccessToken(plat, shopId);
		if (token == null) {
			return false;
		}
		final Long w2 = token.getW2Expire();
		final Long now = System.currentTimeMillis();
		logger.info("Now:{},W2Expire:{}", now, w2);
		if (w2 != null && w2 > now) {
			return true;
		}
		return false;
	}

	@Override
	public AccessToken getAccessToken(PlatEnum plat, final String shopId) {
		String shopKey = ChannelParamBuilder.shopKey(plat, shopId);
		AccessToken accessToken = accessTokenMap.get(shopKey);
		if (accessToken == null || System.currentTimeMillis() >= accessToken.getR1Expire()) {
			accessToken = accessTokenRepository.getAccessToken(plat, shopId);
			if (accessToken != null) {
				accessTokenMap.put(shopKey, accessToken);
			}
		}
		return accessToken;
	}
}
