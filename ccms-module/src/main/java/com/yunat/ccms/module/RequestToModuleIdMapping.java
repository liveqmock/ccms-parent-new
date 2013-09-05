package com.yunat.ccms.module;

import javax.servlet.http.HttpServletRequest;

/**
 * 一个映射关系,能将request对象映射到Module的id.
 * 
 * @author MaGiCalL
 */
public interface RequestToModuleIdMapping {

	/**
	 * 确定本请求是否是来请求Module的
	 * 
	 * @param request
	 * @return
	 */
	boolean isForModule(HttpServletRequest request);

	/**
	 * 从request里解析出当前请求所请求的根模块的id(String型的)
	 * 
	 * @param request
	 * @return
	 */
	Object analyzeModuleId(HttpServletRequest request);
}
