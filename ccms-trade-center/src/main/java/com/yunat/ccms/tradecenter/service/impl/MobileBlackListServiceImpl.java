package com.yunat.ccms.tradecenter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.channel.support.domain.MobileBlackList;
import com.yunat.ccms.channel.support.repository.MobileBlackListRepository;
import com.yunat.ccms.tradecenter.service.MobileBlackListService;

/**
 * 短信黑名单
 *
 * @author teng.zeng
 *         date 2013-6-3 下午07:18:08
 */
@Service
public class MobileBlackListServiceImpl implements MobileBlackListService {

	@Autowired
	MobileBlackListRepository mobileBlackListRepository;

	@Override
	public MobileBlackList getByMobile(final String mobile) {
		return mobileBlackListRepository.findOne(mobile);
	}

}
