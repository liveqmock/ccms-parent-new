package com.yunat.ccms.channel.external;

import java.util.List;

import org.apache.commons.lang.ClassUtils;

import com.google.common.collect.Lists;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;

public abstract class AbstractDefaultRetryPolicy implements RetryPolicy {

	protected final List<Class<? extends Exception>> retryOn = Lists.newArrayList();
	{
		retryOn.add(Exception.class);
	}

	protected int retryCounter = 0;
	protected int maxRetryCount = 5;

	@Override
	public boolean needRetry(Exception ex) {
		boolean result = false;
		for (Class<? extends Exception> type : retryOn) {
			if (ClassUtils.getAllSuperclasses(ex.getClass()).contains(type)) {
				result = true;
			}
		}

		result = result && retryCounter < maxRetryCount;
		if (result) {
			retryCounter++;
		}
		return result;
	}

	@Override
	public Long getDelay(Long lastDelay) {
		return lastDelay * 3;
	}

	@Override
	public void doWhenFinallyFail(Exception ex) {
		 throw new CcmsBusinessException("remote interface invoke retry some times fail, don't again retry, will be stop", ex);
	}

}