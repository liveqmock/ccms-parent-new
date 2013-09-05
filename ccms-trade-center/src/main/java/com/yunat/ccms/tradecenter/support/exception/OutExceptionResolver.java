package com.yunat.ccms.tradecenter.support.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常打印类
 * 由于默认情况下spring mvc不会输出异常信息，在这里拦截异常并日志输出
 * @author 李卫林
 *
 */
public class OutExceptionResolver implements HandlerExceptionResolver {
    protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.error("exception", ex);

		return null;
	}

}
