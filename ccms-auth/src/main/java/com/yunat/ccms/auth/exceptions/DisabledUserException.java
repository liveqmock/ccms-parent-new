package com.yunat.ccms.auth.exceptions;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.core.support.exception.CcmsRuntimeException;

public class DisabledUserException extends CcmsRuntimeException {
	private static final long serialVersionUID = 7122565317826011900L;

	public final User user;

	public DisabledUserException(final User user, final Throwable t) {
		super("用户" + user.getLoginName() + "在系统中已经被停用", t);
		this.user = user;
	}

	public DisabledUserException(final User user) {
		this(user, null);
	}

}
