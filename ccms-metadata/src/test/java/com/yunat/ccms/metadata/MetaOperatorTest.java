package com.yunat.ccms.metadata;

import java.lang.reflect.Method;

import org.junit.Test;

import com.yunat.ccms.metadata.metamodel.MetaOperator;
import com.yunat.ccms.metadata.quota.QuotaOrder;

public class MetaOperatorTest {

	@Test
	public void testPrintName() {

		System.out.println(MetaOperator.EQ.name());
		System.out.println(MetaOperator.EQ.getOpName());
		System.out.println(MetaOperator.EQ.getChineseName());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testParseQuota() {

		try {

			String xxx = "com.yunat.ccms.metadata.quota.QuotaOrder.BUY_FEE";

			int index = xxx.lastIndexOf(".");
			String prefix = xxx.substring(0, index);
			String surfix = xxx.substring(index + 1);
			Class clazz = Class.forName(prefix);
			Method method = clazz.getMethod("valueOf", String.class);
			Object obj = method.invoke(null, surfix);
			QuotaOrder order = (QuotaOrder) obj;
			System.out.println(order.getChineseName());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReplace() {

		String str = "0000?00000011111?11112222?22222333333?33333444444?44444";
		for (int i = 0; i < 5; i++) {

			str = str.replaceAll("[?]" + i, "-T-");
		}
		System.out.println(str);
	}

	@Test
	public void testFind() {

		String str = "?0.customerno";
		String[] strs = str.split("[.]");
		System.out.println(strs[0]);
		System.out.println(strs[1]);
	}
}
