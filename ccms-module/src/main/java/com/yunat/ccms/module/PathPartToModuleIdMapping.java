package com.yunat.ccms.module;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

/**
 * {@link RequestToModuleIdMapping}的一个实现,取路径中位于/module/之后的部分.可能是字符串也可能是数字
 * 
 * @author MaGiCalL
 */
@Component
public class PathPartToModuleIdMapping implements RequestToModuleIdMapping {

	private static final String PREFIX = "/" + ModuleCons.APPLICATION_MODULE_NAME + "/";
	private static final int PREFIX_LEN = PREFIX.length();

	@Override
	public Object analyzeModuleId(final HttpServletRequest request) {
		final String uri = request.getRequestURI();
		final int prefixIndex = uri.lastIndexOf(PREFIX);
		if (prefixIndex < 0) {
			throw new IllegalArgumentException("request uri is not requiring module");
		}
		final String moduleKeyStr = uri.substring(prefixIndex + PREFIX_LEN);

		try {
			return Long.parseLong(moduleKeyStr);
		} catch (final NumberFormatException e) {
			return moduleKeyStr;
		}
		//TODO:似乎url后面会有“!xxx”的东西来定义调用方法，所以这里应该还会需要修改
	}

	@Override
	public boolean isForModule(final HttpServletRequest request) {
		final String uri = request.getRequestURI();
		return uri.contains(PREFIX);
	}

}
