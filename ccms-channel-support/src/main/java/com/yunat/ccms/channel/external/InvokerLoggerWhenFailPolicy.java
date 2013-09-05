package com.yunat.ccms.channel.external;

import org.apache.commons.lang.ClassUtils;

import com.taobao.api.ApiException;
import com.taobao.api.ApiRuleException;
import com.yunat.ccms.channel.support.service.RemoteLoggerService;
import com.yunat.ccms.core.support.utils.HStringUtils;

public class InvokerLoggerWhenFailPolicy extends AbstractDefaultRetryPolicy {

	private final String exceptionLogKey;
	private final RemoteLoggerService httpLogService;

	public InvokerLoggerWhenFailPolicy(String exceptionLogKey, RemoteLoggerService httpLogService) {
		this.exceptionLogKey = exceptionLogKey;
		this.httpLogService = httpLogService;
	}

	public InvokerLoggerWhenFailPolicy(String exceptionLogKey, RemoteLoggerService httpLogService, int maxRetryCount) {
		this.exceptionLogKey = exceptionLogKey;
		this.httpLogService = httpLogService;
		this.maxRetryCount = maxRetryCount;
	}

	@Override
	public void doWhenFail(Exception ex) {
		httpLogService.save(exceptionLogKey, HStringUtils.getExceptionMessage(ex));
	}

	@Override
	public boolean needRetry(Exception ex) {
		boolean result = false;
		for (Class<? extends Exception> type : retryOn) {
			if (ClassUtils.getAllSuperclasses(ex.getClass()).contains(type)) {
				// 接口返回的参数错误不需要重试，排除
				if (!ex.getClass().equals(ApiRuleException.class) && !ex.getClass().equals(NumberFormatException.class)
						&& !ex.getClass().equals(ApiException.class)) {
					ex.printStackTrace();
					result = true;
				}
			}
		}

		result = result && retryCounter < maxRetryCount;
		if (result) {
			retryCounter++;
		}
		return result;
	}

}