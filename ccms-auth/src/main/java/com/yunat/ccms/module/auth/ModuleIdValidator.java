package com.yunat.ccms.module.auth;

import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.module.Module;

public class ModuleIdValidator extends AbsModuleEntryFieldValidator {

	@Override
	protected Object moduleEntryValue(final ModuleEntry moduleEntry) {
		return moduleEntry.getModuleId();
	}

	@Override
	protected Object authContextValue(final AuthContext authContext, final Module module) {
		return module.getId();
	}

}
