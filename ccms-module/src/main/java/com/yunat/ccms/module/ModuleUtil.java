package com.yunat.ccms.module;

public class ModuleUtil {

	public static String keyPath(final Module module) {
		final StringBuilder sb = new StringBuilder(module.getKeyName());
		for (Module p = module.getContainerModule(); //
		p != null && p.getId() != ModuleCons.CONTAINER_OF_MODULES_IN_EACH_PAGE_MODULE_ID; //
		p = p.getContainerModule()) {
			sb.insert(0, "/").insert(0, p.getKeyName());
		}
		return sb.toString();
	}
}
