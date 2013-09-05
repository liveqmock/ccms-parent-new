package com.yunat.ccms.module.auth;

import java.util.Collection;

import com.yunat.ccms.auth.AuthContext;
import com.yunat.ccms.auth.permission.Permission;
import com.yunat.ccms.module.Module;

public class PermissionIdValidator implements ModuleEntryFieldValidator {
	private boolean findPermission(final Collection<Permission> userPermissions, final Long permissionId) {
		for (final Permission permission : userPermissions) {
			if (permissionId.equals(permission.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ValidateResult accpept(final AuthContext authContext, final Module module, final ModuleEntry moduleEntry) {
		final Long permissionId = moduleEntry.getPermissionId();
		//未登录用户的permission=null或空
		//moduleEntry.getPermissionId()为null可匹配用户permission=null或空的情况；
		//为0不能匹配权限id=null或空的情况但可匹配其他任何值，对匿名的情况很有用
		if (permissionId == null) {
			return ValidateResult.ALL_CONTAINING;
		}
		final Collection<Permission> userPermissions = authContext.getUserPermissions();
		if (userPermissions == null || userPermissions.isEmpty()) {
			//没有permission的用户
			return ValidateResult.NOT_CONTAINING;
		}
		if (permissionId == 0) {
			return ValidateResult.RANGE_CONTAINING;
		}

		if (findPermission(userPermissions, permissionId)) {
			return ValidateResult.EXACT_CONTAINING;
		} else {
			//用户的Permission不包含ModuleEntry指定的permissionId
			return ValidateResult.NOT_CONTAINING;
		}

	}
}
