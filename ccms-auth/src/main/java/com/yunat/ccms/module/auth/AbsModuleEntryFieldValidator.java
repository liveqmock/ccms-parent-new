package com.yunat.ccms.module.auth;

import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.module.Module;

public abstract class AbsModuleEntryFieldValidator implements ModuleEntryFieldValidator {

	@Override
	public ValidateResult accpept(final AuthContext authContext, final Module module, final ModuleEntry moduleEntry) {
		final Object moduleEntryValue = moduleEntryValue(moduleEntry);
		if (moduleEntryValue == null) {
			return ValidateResult.ALL_CONTAINING;
		}
		final Object authContextValue = authContextValue(authContext, module);
		return validateValue(moduleEntryValue, authContextValue);
	}

	/**
	 * @param moduleEntryValue
	 * @param authContextValue
	 * @return
	 */
	protected ValidateResult validateValue(final Object moduleEntryValue, final Object authContextValue) {
		if (moduleEntryValue.equals(authContextValue)) {
			return ValidateResult.EXACT_CONTAINING;
		} else {
			return ValidateResult.NOT_CONTAINING;
		}
	}

	protected abstract Object moduleEntryValue(final ModuleEntry moduleEntry);

	protected abstract Object authContextValue(final AuthContext authContext, final Module module);
}
