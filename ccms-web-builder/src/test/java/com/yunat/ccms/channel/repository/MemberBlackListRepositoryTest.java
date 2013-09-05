package com.yunat.ccms.channel.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.channel.support.domain.MemberBlackList;
import com.yunat.ccms.channel.support.repository.MemberBlackListRepository;

public class MemberBlackListRepositoryTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	MemberBlackListRepository memberBlackListRepository;

	@Test
	public void testSave() {

		MemberBlackList member = new MemberBlackList();
		member.setContact("无印良品");
		member.setSource("手工输入");
		member.setCreated(new Date());
		memberBlackListRepository.save(member);
	}

	@Test
	public void testLoad() {

		MemberBlackList member = memberBlackListRepository.findOne("无印良品");
		Assert.assertNotNull(member);
	}

	@Test
	public void testRemove() {

		MemberBlackList member = new MemberBlackList();
		member.setContact("无印良品");
		member.setSource("手工输入");
		member.setCreated(new Date());
		memberBlackListRepository.delete(member);
	}
}
