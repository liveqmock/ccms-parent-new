package com.yunat.ccms.channel.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.channel.support.domain.EmailBlackList;
import com.yunat.ccms.channel.support.repository.EmailBlackListRepository;

public class EmailBlackListRepositoryTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	EmailBlackListRepository emailBlackListRepository;

	@Test
	public void testSave() {

		EmailBlackList email = new EmailBlackList();
		email.setContact("kevin@yunat.net");
		email.setSource("手工输入");
		email.setCreated(new Date());
		emailBlackListRepository.save(email);
	}

	@Test
	public void testLoad() {

		EmailBlackList email = emailBlackListRepository.findOne("kevin@yunat.net");
		Assert.assertNotNull(email);
	}

	@Test
	public void testPage() {

		Page<EmailBlackList> email = emailBlackListRepository.findAll(new PageRequest(0, 15));
		Assert.assertTrue(email.getContent().size() > 0);
	}

	@Test
	public void testRemove() {

		EmailBlackList email = new EmailBlackList();
		email.setContact("kevin@yunat.net");
		email.setSource("手工输入");
		email.setCreated(new Date());
		emailBlackListRepository.delete(email);
	}
}
