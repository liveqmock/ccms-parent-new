package com.yunat.ccms.tradecenter.service;

import com.yunat.ccms.tradecenter.domain.CustomerDomain;

public interface CustomerService {

	/**
	 * 根据用户no查询用户详细信息
	 * @param customerno
	 * @return
	 */
	CustomerDomain findByCustomerno(String customerno);

}
