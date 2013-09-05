package com.yunat.ccms.tradecenter.service;

import java.util.List;



/**
 *
 * 变量替换服务
 *
 * @author shaohui.li
 * @version $Id: VariableReplaceService.java, v 0.1 2013-6-7 下午03:51:13 shaohui.li Exp $
 */
public interface VariableReplaceService {

	/**
	 * @param template
	 * @param list
	 * @return
	 */
	String replaceSmsContent(String template, List<Object> list);
}
