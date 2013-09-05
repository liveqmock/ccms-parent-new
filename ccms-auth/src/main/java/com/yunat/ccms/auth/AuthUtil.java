package com.yunat.ccms.auth;

import java.util.Collection;

import org.springframework.core.Ordered;

import com.yunat.ccms.core.support.auth.SupportOp;

public class AuthUtil {

	public static Collection<SupportOp> permitAllSupportOps() {
		return null;
	}

	public static int lowerOrder(final int refedOrder) {
		return lowerOrder(refedOrder, 1);
	}

	public static int lowerOrder(final int refedOrder, final int lowerLevel) {
		if (Ordered.LOWEST_PRECEDENCE > refedOrder) {
			return refedOrder + lowerLevel;
		}
		if (Ordered.LOWEST_PRECEDENCE < refedOrder) {
			return refedOrder - lowerLevel;
		}
		//Ordered.LOWEST_PRECEDENCE == refedOrder
		return Ordered.LOWEST_PRECEDENCE;
	}

	public static int higherOrder(final int refedOrder) {
		return higherOrder(refedOrder, 1);
	}

	public static int higherOrder(final int refedOrder, final int higherLevel) {
		if (Ordered.HIGHEST_PRECEDENCE < refedOrder) {
			return refedOrder - higherLevel;
		}
		if (Ordered.HIGHEST_PRECEDENCE > refedOrder) {
			return refedOrder + higherLevel;
		}
		//Ordered.HIGHEST_PRECEDENCE == refedOrder
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
