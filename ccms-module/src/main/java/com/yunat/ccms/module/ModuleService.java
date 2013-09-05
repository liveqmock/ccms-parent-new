package com.yunat.ccms.module;

import org.springframework.security.access.annotation.Secured;

import com.google.common.cache.RemovalCause;

public interface ModuleService {

	interface CacheReloadListener {
		void onCacheReload(final RemovalCause cause);
	}

	/**
	 * 根据id获得一个ModuleType对象
	 * 
	 * @param id
	 * @return
	 */
	ModuleType getModuleTypeById(Long id);

	/**
	 * 根据id获得一个Module对象
	 * 
	 * @param id
	 * @return
	 */
	Module getModuleById(Long id);

	Module getModuleByFullName(String fullName);

	void regCacheRemovalListener(CacheReloadListener listener);

	@Secured({ "addAcl" })
	//FIXME:测试:在jpa的方法上加@secured可能会导致transaction还没有提交就进入了afterInvocation,因此咱们在service层加.
	Module save(ModuleFromDB module);
}
