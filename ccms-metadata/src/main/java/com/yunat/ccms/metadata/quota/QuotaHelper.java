package com.yunat.ccms.metadata.quota;

import java.lang.reflect.Method;

public class QuotaHelper {

	/**
	 * 把指标从名称转换到枚举
	 * 
	 * 指标在数据库存放的字符串是类全名.枚举类型
	 * 
	 * 如：com.yunat.ccms.metadata.quota.QuotaOrder.BUY_FEE
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Quota convertToEnum(String name) throws Exception {

		int index = name.lastIndexOf(".");
		String quotaClass = name.substring(0, index);
		String quotaType = name.substring(index + 1);
		Class clazz = Class.forName(quotaClass);
		Method method = clazz.getMethod("valueOf", String.class);
		return (Quota) method.invoke(null, quotaType);
	}

	public static String ensureNoDivisionByZero(String dividend, String divisor) {
		return " case when (" + divisor + " = 0 or " + divisor + " is null) then 0 else " + dividend + " / " + divisor
				+ " end ";
	}
}
