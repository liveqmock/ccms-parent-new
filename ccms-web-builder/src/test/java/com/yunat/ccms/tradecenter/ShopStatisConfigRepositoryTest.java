package com.yunat.ccms.tradecenter;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.tradecenter.domain.ShopStatisConfigDomain;
import com.yunat.ccms.tradecenter.repository.ShopStatisConfigRepository;

public class ShopStatisConfigRepositoryTest extends AbstractJunit4SpringContextBaseTests{

	@Autowired
	private ShopStatisConfigRepository shopStatisConfigRepository;

	@Test
	public void testGetByDpId() {
		ShopStatisConfigDomain shopStatisConfig = shopStatisConfigRepository.getByDpId("123456");

		System.out.println(shopStatisConfig);
	}
}
