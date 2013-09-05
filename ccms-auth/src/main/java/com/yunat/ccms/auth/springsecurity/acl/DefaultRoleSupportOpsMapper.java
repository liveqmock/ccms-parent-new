package com.yunat.ccms.auth.springsecurity.acl;

import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.core.support.auth.SupportOp;

public class DefaultRoleSupportOpsMapper implements RoleSupportOpsMapper {

	private static final long ADMIN_ROLE_ID = 100000L;

	@Override
	public SupportOp[] getSupportOps(final Role role, final Object model) {
		if (role.getId() == ADMIN_ROLE_ID) {
			return SupportOp.values();
		}
		//其他角色只有看的权限
		return new SupportOp[] { SupportOp.VIEW };
	}

}
