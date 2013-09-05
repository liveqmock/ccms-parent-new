package com.yunat.ccms.rule.center.drl.convert;

import java.util.Date;
import java.util.List;

public enum JFieldType {
	STRING_TYPE(String.class), //
	LONG_TYPE(Long.class), //
	DOUBLE_TYPE(Double.class), //
	DATE_TYPE(Date.class), //
	LIST_TYPE(List.class);

	private final Class<?> clazz;
	private final String typeName;

	public static JFieldType valueOf(final Class<?> clazz) {
		for (final JFieldType t : values()) {
			if (t.clazz == clazz) {
				return t;
			}
		}
		return null;
	}

	public static JFieldType valueOfIgnoreCase(final String _typeName) {
		for (final JFieldType t : values()) {
			if (t.getTypeName().equalsIgnoreCase(_typeName)) {
				return t;
			}
		}
		return null;
	}

	private JFieldType(final Class<?> clazz) {
		typeName = clazz.getSimpleName();
		this.clazz = clazz;
	}

	public String getTypeName() {
		return typeName;
	}

}