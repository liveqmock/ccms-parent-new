package com.yunat.ccms.core.support.io;

import java.io.File;
import java.io.InputStream;

public class ClassPathResourceUtil {

	/**
	 * 获取类根路径下的资源流. 实例：如类目录下的某个属性文件 InputStream inputStream =
	 * ClassPathResourceUtil.getInputStreamBySourceName("pro-config.properties")
	 * 
	 * @param sourceName
	 * @return
	 */
	public static InputStream getInputStreamBySourceName(String sourceName) {
		InputStream configFileInputStream = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		configFileInputStream = classLoader.getResourceAsStream(sourceName);
		return configFileInputStream;
	}

	/**
	 * 
	 * 获取与类下面的同路径的资源
	 * 
	 * @param fileName
	 * @param aClass
	 * @return
	 */
	public static InputStream getInputStreamBySoureNameInSamePackage(String fileName, Class<?> aClass) {
		InputStream configFileInputStream = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String packagePath = ClassUtil.getPackagePath(aClass);
		configFileInputStream = classLoader.getResourceAsStream(packagePath + File.separator + fileName);
		return configFileInputStream;
	}

}
