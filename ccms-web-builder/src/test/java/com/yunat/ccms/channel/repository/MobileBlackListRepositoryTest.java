package com.yunat.ccms.channel.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.channel.support.domain.MobileBlackList;
import com.yunat.ccms.channel.support.repository.MobileBlackListRepository;

public class MobileBlackListRepositoryTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	MobileBlackListRepository mobileBlackListRepository;

	@Test
	public void testSave() {

		MobileBlackList mobile = new MobileBlackList();
		mobile.setContact("13817466666");
		mobile.setSource("手工输入");
		mobile.setCreated(new Date());
		mobileBlackListRepository.save(mobile);
	}

	@Test
	public void testLoad() {

		MobileBlackList mobile = mobileBlackListRepository.findOne("13817466666");
		Assert.assertNotNull(mobile);
	}

	@Test
	public void testRemove() {

		MobileBlackList mobile = new MobileBlackList();
		mobile.setContact("13817466666");
		mobile.setSource("手工输入");
		mobile.setCreated(new Date());
		mobileBlackListRepository.delete(mobile);
	}
}
