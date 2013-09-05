package com.yunat.ccms.module.auth;

import java.util.Collection;

import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.module.Module;

public class RoleIdValidator implements ModuleEntryFieldValidator {
	private boolean findRole(final Collection<Role> userRoles, final Long roleId) {
		for (final Role role : userRoles) {
			if (roleId.equals(role.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ValidateResult accpept(final AuthContext authContext, final Module module, final ModuleEntry moduleEntry) {
		final Long roleId = moduleEntry.getRoleId();
		if (roleId == null) {
			return ValidateResult.ALL_CONTAINING;
		}
		final Collection<Role> userPermissions = authContext.getUser().getRoles();
		if (userPermissions == null || userPermissions.isEmpty()) {
			return ValidateResult.NOT_CONTAINING;
		}

		if (findRole(userPermissions, roleId)) {
			return ValidateResult.EXACT_CONTAINING;
		} else {
			//用户的Role不包含ModuleEntry指定的roleId
			return ValidateResult.NOT_CONTAINING;
		}

	}
}
