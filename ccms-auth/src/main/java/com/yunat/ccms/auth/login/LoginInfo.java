package com.yunat.ccms.auth.login;

import java.util.Date;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.cons.ProductEdition;

/**
 * 登录信息
 * 
 * @author wenjian.liang
 */
public interface LoginInfo {

	/**
	 * 登录的用户
	 * 
	 * @return
	 */
	User getUser();

	/**
	 * 登录时间
	 * 
	 * @return
	 */
	Date getLoginTime();

	/**
	 * 用户购买的产品版本
	 * 
	 * @return
	 */
	ProductEdition getProductEdition();
}
