package com.yunat.ccms.tradecenter.service;

import com.yunat.ccms.channel.support.domain.MobileBlackList;

/**
 * 短信黑名单
 * 
 * @author teng.zeng
 *         date 2013-6-3 下午06:58:12
 */
public interface MobileBlackListService {

	MobileBlackList getByMobile(String mobile);

}
