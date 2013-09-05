package com.yunat.ccms.tradecenter.service;

import com.yunat.ccms.tradecenter.domain.LoginSubuserRelaDomain;

/**
 * 登录名与旺旺子账号关系
 * @author xin.chen
 *
 */
public interface LoginSubuserRelaService {

	/**
	 * 获取当前登录账号与旺旺子账号的绑定信息,可能为null
	 */
	LoginSubuserRelaDomain findloginSubuserRela(String dpId);

	/**
	 * 保存用户和旺旺子账号的关系
	 */
	boolean save(LoginSubuserRelaDomain loginSubuserRelaDomain);

}
