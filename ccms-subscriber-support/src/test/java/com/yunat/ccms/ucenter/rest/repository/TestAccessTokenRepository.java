package com.yunat.ccms.ucenter.rest.repository;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

public class TestAccessTokenRepository {

	private static final String ucenter_url_for_test = "http://apptest.fenxibao.com/ucenter-restful-impl";

	private static AccessTokenRepository accessTokenRepository = null;

	@BeforeClass
	public static void beforeClass() {
		AccessTokenRepositoryImpl atr = new AccessTokenRepositoryImpl();
		atr.setUcenterServerPath(ucenter_url_for_test);
		accessTokenRepository = atr;
	}

	@Test
	public void test() {
		String shopId = "65927470";
		AccessToken token = accessTokenRepository.getAccessToken(PlatEnum.taobao, shopId);
		Assert.assertNotNull(token);
		System.out.print(token);
	}

	@Test
	public void test2() {
		String shopId = "100571094";
		AccessToken token = accessTokenRepository.getAccessToken(PlatEnum.taobao, shopId);
		Assert.assertNotNull(token);
		System.out.print(token);
	}

}
