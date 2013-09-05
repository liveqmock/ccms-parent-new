package com.yunat.ccms.channel.support;

import org.junit.Test;

import com.yunat.ccms.core.support.utils.MD5Utils;
import com.yunat.channel.util.DataSecurityUtil;

public class TestBase64Decode {
	@Test
	public void qiushi() {
		System.out.println("@@@@@@TestBase64Decode.qiushi():"
				+ decode("UvmdlZ+Y7/HhdPQa7SPjaPOPfCSxXWEc94tC7R83E+JpO0imhK/lIA=="));//5325ddba94c446beac479f67bc06a180
	}

	@Test
	public void xiaowei() {
		System.out.println("@@@@@@TestBase64Decode.xiaowei():"
				+ decode("dNfNkh7wQ82sAQ9/KTzYL3jZ6CaBy6faGh1CGgPmSCAhPO9WBYbJ+A=="));//d3102559117f4e7194e7d9b2c83ffb8c
	}

	private static String decode(final String secureStr) {
		return DataSecurityUtil.base64Decode(secureStr, "93CoPRKZWnzwPGPlvePdMe0e");
	}

	public static void main(final String[] args) {
		System.out.println("@@@@@@TestBase64Decode.main():"
				+ DataSecurityUtil.baseEncode("51d531kxsyofum3g6iq22sbx8mebvaquhnr5", "93CoPRKZWnzwPGPlvePdMe0e"));
		//BM6mywn0lEVw+XyzlmXgm/4UUtZ//uS+aKB8RbA5o+O6PJMJmzGyiw==
	}

	public static void b(final String[] args) {
		final String str = "kefu" + "51d531kxsyofum3g6iq22sbx8mebvaquhnr5" + 1369126720413L + "xiaowei";
		final String validate = MD5Utils.EncodeByMd5(str);
		System.out.println("@@@@@@KfxtLoginSource.main():" + validate);
	}
}
