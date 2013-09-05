package com.yunat.ccms.core.support.io;

import java.io.File;

public class ClassUtil {

	/**
	 * 获取包名
	 * 
	 * @param aClass
	 * @return
	 */
	public static String getPackageName(Class<?> aClass) {
		return aClass.getPackage().getName();
	}

	/**
	 * 把包名转换为路径格式
	 * 
	 * @param aClass
	 * @return
	 */
	public static String getPackagePath(Class<?> aClass) {
		return getPackageName(aClass).replace('.', File.separatorChar);
	}

}
