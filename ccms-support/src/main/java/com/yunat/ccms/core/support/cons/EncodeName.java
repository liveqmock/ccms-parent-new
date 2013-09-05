package com.yunat.ccms.core.support.cons;

import java.nio.charset.Charset;

public enum EncodeName {
	UTF8, //
	GBK, //
	GB2312, //
	ISO8859_1, //
	;
	public final String name;

	private EncodeName() {
		name = name();
	}

	public String getName() {
		return name;
	}

	public Charset charset() {
		return Charset.forName(name);
	}

	public static void main(final String[] args) {
		for (final EncodeName n : EncodeName.values()) {
			System.out.println("@@@@@@EncodeName.main():" + n.charset());
		}
	}
}
