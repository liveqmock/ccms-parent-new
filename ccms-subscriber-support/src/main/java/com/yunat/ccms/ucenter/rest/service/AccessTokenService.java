package com.yunat.ccms.ucenter.rest.service;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

public interface AccessTokenService {

	/**
	 * 获取店铺短授权是否可用
	 * 
	 * @param plat
	 * @param shopId
	 * @return
	 */
	boolean w2Available(PlatEnum plat, String shopId);

	/**
	 * 获取access token(包含session key)
	 * @param plat TODO
	 * @param shopId
	 * 
	 * @return
	 */
	AccessToken getAccessToken(PlatEnum plat, String shopId);

}
