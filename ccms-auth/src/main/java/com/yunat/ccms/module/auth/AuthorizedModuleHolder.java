package com.yunat.ccms.module.auth;

import java.util.Collection;

import com.yunat.ccms.module.Module;

/**
 * <pre>
 * 本次请求中验证过的有权限的模块的持有者.
 * 注:依靠{@link ModuleInterceptor}注入,所以若有Interceptor需要使用本类,其优先级必须在{@link ModuleInterceptor}之后.
 * </pre>
 * 
 * @author MaGiCalL
 */
public class AuthorizedModuleHolder {

	protected static final ThreadLocal<Collection<Module>> AUTHORIZED_MODULES_LOCAL = new ThreadLocal<Collection<Module>>();

	/**
	 * 获取本次请求中验证过有权限的模块
	 * 
	 * @return
	 */
	public static Collection<Module> getAuthorizedModules() {
		return AUTHORIZED_MODULES_LOCAL.get();
	}
}
