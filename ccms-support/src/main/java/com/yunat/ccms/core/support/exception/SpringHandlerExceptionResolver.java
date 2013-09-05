package com.yunat.ccms.core.support.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.yunat.ccms.core.support.vo.ControlerResult;

public class SpringHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	private final Logger log = LoggerFactory.getLogger(SpringHandlerExceptionResolver.class);

	private String defaultErrorView;

	protected ObjectMapper objectMapper = new ObjectMapper();

	public void setDefaultErrorView(final String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	@Override
	protected ModelAndView doResolveHandlerMethodException(final HttpServletRequest request,
			final HttpServletResponse response, final HandlerMethod handlerMethod, final Exception exception) {
		// 打印异常堆栈到log
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final PrintStream s = new PrintStream(byteArrayOutputStream);
		exception.printStackTrace(s);
		log.error(new String(byteArrayOutputStream.toByteArray()));

		if (handlerMethod == null) {
			return null;
		}

		final Method method = handlerMethod.getMethod();

		if (method == null) {
			return null;
		}

		final ModelAndView returnValue = new ModelAndView();

		final ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		if (responseBodyAnn != null) {
			return handlerAjaxException(exception, response);
		} else {
			return handlerViewException(exception, returnValue);
		}
	}

	/**
	 * 处理ajax异常
	 * 
	 * @param response
	 * @return
	 */
	private ModelAndView handlerAjaxException(final Exception exception, final HttpServletResponse response) {
		response.setContentType("application/json;charset=UTF-8");
		try {
			response.setStatus(500);
			final ControlerResult result = ControlerResult.newError(exception.getMessage());
			final PrintWriter writer = response.getWriter();
			getObjectMapper().writeValue(writer, result);
			writer.flush();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return new ModelAndView();
	}

	/**
	 * 处理跳转页面异常
	 * 
	 * @param exception
	 * @param returnValue
	 * @return
	 */
	private ModelAndView handlerViewException(final Exception exception, final ModelAndView returnValue) {
		final String exceptionMessage = exceptionType(exception);
		log.info("错误信息:", exception);
		returnValue.addObject("message", exceptionMessage);
		returnValue.setViewName(defaultErrorView);
		return returnValue;
	}

	/**
	 * 异常类型分类
	 * 
	 * @param exception
	 * @return
	 */
	private String exceptionType(final Exception exception) {
		String exceptionMessage = "";
		if (exception instanceof NullPointerException) {
			exceptionMessage = "空指向异常";
		}
		if (exception instanceof ClassCastException) {
			exceptionMessage = "类转换异常";
		}
		if (exception instanceof IndexOutOfBoundsException) {
			exceptionMessage = "数组越界异常";
		}
		return exceptionMessage;
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

}
