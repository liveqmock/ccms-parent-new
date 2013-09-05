package com.yunat.ccms.metadata.metamodel;

public enum EnumTimeType {

	// 绝对时间
	ABSTIME(0L),
	// 相对时间
	RELTIME(1L),
	// 时间变量
	VARTIME(2L);

	private Long value;

	EnumTimeType(Long value) {
		this.value = value;
	}

	/**
	 * 获取枚举值存入数据库时的字段取值
	 */
	public Long getValue() {
		return value;
	}

	public static EnumTimeType toTimeType(Long value) {
		EnumTimeType timeType = null;
		switch (value.intValue()) {
		case 0:
			timeType = EnumTimeType.ABSTIME;
			break;
		case 1:
			timeType = EnumTimeType.RELTIME;
			break;
		case 2:
			timeType = EnumTimeType.VARTIME;
			break;
		default:
			throw new IllegalArgumentException("时间查询类型错误");
		}
		return timeType;
	}
}
