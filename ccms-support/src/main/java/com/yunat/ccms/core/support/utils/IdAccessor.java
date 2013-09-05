/**
 * 
 */
package com.yunat.ccms.core.support.utils;

import java.io.Serializable;

/**
 * @author Administrator
 * @version Mar 19, 2011 9:06:11 AM
 */
public enum IdAccessor implements KeyAccessor<Comparable<?>, HasIdGetter<?>>, Serializable {
	INSTANCE;

	@Override
	public Comparable<?> extract(final HasIdGetter<?> value) {
		return value.getId();
	}
}
