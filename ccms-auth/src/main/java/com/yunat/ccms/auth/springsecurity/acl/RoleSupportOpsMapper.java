package com.yunat.ccms.auth.springsecurity.acl;

import com.yunat.ccms.auth.role.Role;
import com.yunat.ccms.core.support.auth.SupportOp;

public interface RoleSupportOpsMapper {

	SupportOp[] getSupportOps(Role role, Object model);
}
