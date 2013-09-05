package com.yunat.ccms.module.auth;

import java.util.Collection;

public interface ModuleEntryService {

	/**
	 * 获取某模块的准入规则(ACEs).
	 * 
	 * @param moduleId
	 * @return
	 */
	Collection<ModuleEntry> getEntriesOfModule(Long moduleId);
}
