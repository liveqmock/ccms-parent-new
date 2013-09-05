package com.yunat.ccms.auth.user;

public enum UserType {

	BUILD_IN("build-in"), //是build-in(数据库表字段的默认值)不是built-in
	TAOBAO("taobao"), //
	KFXT("kfxt"), //
	;
	public final String name;

	private UserType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
