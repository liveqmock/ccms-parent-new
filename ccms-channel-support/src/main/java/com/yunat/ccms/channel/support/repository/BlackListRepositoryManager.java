package com.yunat.ccms.channel.support.repository;

import java.util.EnumMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.yunat.ccms.channel.support.cons.EnumBlackList;
import com.yunat.ccms.channel.support.domain.BlackList;

/**
 * 黑名单持久层管理类
 * 
 * @author kevin.jiang 2013-4-24
 */
@Component
public class BlackListRepositoryManager {

	private EnumMap<EnumBlackList, JpaRepository<BlackList, Long>> repositoryMap = new EnumMap<EnumBlackList, JpaRepository<BlackList, Long>>(
			EnumBlackList.class);

	@Autowired
	@Qualifier("emailBlackListRepository")
	JpaRepository<BlackList, Long> emailBlackListRepository;

	@Autowired
	@Qualifier("mobileBlackListRepository")
	JpaRepository<BlackList, Long> mobileBlackListRepository;

	@Autowired
	@Qualifier("memberBlackListRepository")
	JpaRepository<BlackList, Long> memberBlackListRepository;

	@PostConstruct
	public void init() {

		repositoryMap.clear();
		repositoryMap.put(EnumBlackList.EMAIL, emailBlackListRepository);
		repositoryMap.put(EnumBlackList.MOBILE, mobileBlackListRepository);
		repositoryMap.put(EnumBlackList.MEMBER, memberBlackListRepository);
	}

	public JpaRepository<BlackList, Long> getBlackListRepositoryByType(EnumBlackList type) {

		if (repositoryMap == null || repositoryMap.size() <= 0) {

			repositoryMap = new EnumMap<EnumBlackList, JpaRepository<BlackList, Long>>(EnumBlackList.class);
			init();
		}

		return repositoryMap.get(type);
	}
}
