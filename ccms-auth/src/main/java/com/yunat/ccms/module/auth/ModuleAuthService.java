package com.yunat.ccms.module.auth;

import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.core.support.auth.SupportOp;
import com.yunat.ccms.module.Module;

/**
 * 当前访问用户对模块的权限验证。
 * 
 * @author MaGiCalL
 */
public interface ModuleAuthService {

	/**
	 * 根据当前访问用户的权限上下文，检查他对一个模块及其所包含的模块的权限。
	 * 如果对某模块无权，不会包含在返回集合中。
	 * 操作权限可能包括多种，参见{@link Module#Module.getSupportOps()}。
	 * 
	 * @param authContext
	 * @param rootModuleId 根模块id
	 * @return 有权限的模块的集合。如果对根模块无权，则为一个空的Collection
	 */
	Module authorizeModule(AuthContext authContext, Object rootModuleId);

	/**
	 * 根据当前访问用户的权限上下文,检查他对某个特定模块的权限.
	 * 若无精确匹配的权限规则,会继承container模块的权限.
	 * 
	 * @param authContext
	 * @param module
	 * @return
	 */
	SupportOp[] getSupportOps(AuthContext authContext, Module module);
}
