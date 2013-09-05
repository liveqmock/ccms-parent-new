package com.yunat.ccms.auth;

import java.util.Collection;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMethod;

import com.yunat.ccms.auth.permission.Permission;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.cons.ProductEdition;

public interface AuthContext {

	/**
	 * 获得用户对象
	 * 
	 * @return
	 */
	User getUser();

	/**
	 * 获得用户购买的产品的版本id
	 * 
	 * @return
	 */
	ProductEdition getProductEdition();

	/**
	 * 获得此用户的权限
	 * 
	 * @return
	 */
	Collection<Permission> getUserPermissions();

	//以下暂时没用
	/**
	 * 获得用户本次请求的某参数
	 * 
	 * @param name
	 * @return
	 */
	Object getRequestParam(String name);

	/**
	 * 获得用户本次请求的某请求头
	 * 
	 * @param name
	 * @return
	 */
	String getRequestHeader(String name);

	/**
	 * 获得用户本次请求的请求方法(GET/POST/PUT/DELETE)
	 * 
	 * @return
	 */
	RequestMethod getRequestMethod();

	/**
	 * 获得用户本次请求的发生时间
	 * 
	 * @return
	 */
	Date getRequestTime();

	/**
	 * 获得用户本次请求的来源ip
	 * 
	 * @return
	 */
	String getRequestIp();

	/**
	 * 获得用户本次请求的来源页面
	 * 
	 * @return
	 */
	String getRequestReferer();
}
