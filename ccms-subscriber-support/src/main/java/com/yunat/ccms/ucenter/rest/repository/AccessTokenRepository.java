package com.yunat.ccms.ucenter.rest.repository;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

/**
 * 
 * @author xiaojing.qu
 * 
 */
public interface AccessTokenRepository {

	/**
	 * 获取CCMS的AccessToken
	 * 
	 * @param plat
	 *            平台代码
	 * @param shopId
	 *            店铺代码
	 * @return
	 */
	AccessToken getAccessToken(PlatEnum plat, String shopId);
}
