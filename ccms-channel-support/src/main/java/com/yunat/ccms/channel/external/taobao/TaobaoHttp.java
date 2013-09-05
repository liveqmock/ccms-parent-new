package com.yunat.ccms.channel.external.taobao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunat.ccms.channel.external.RetryPolicy;
import com.yunat.ccms.channel.external.scs.RemoteHttp;

public class TaobaoHttp {
	private static Logger logger = LoggerFactory.getLogger(RemoteHttp.class);

	private Long delayBeforeNextTry;
	public TaobaoHttp(Long initialRetryInterval) {
		delayBeforeNextTry = initialRetryInterval;
	}
	
	public <T> T call(TaobaoCallable<T> callable, RetryPolicy retryPolicy) {
		Exception finallyException;
		while(true) {
			try {
				T result = callable.call();
				if (null == result) {
					logger.error("response result is null");
					throw new RuntimeException("response info is not success");
				}
				
//				TaobaoResponse info = null;
//				try {
//					info = (TaobaoResponse)result;
//				} catch (ClassCastException cex) {
//					logger.error("taobao API throws ClassCaseException");
//					cex.printStackTrace();
//				}
//				
//				if (!info.isSuccess()) {
//					logger.error("response info is not success");
//					throw new RuntimeException("response info is not success");
//				}
				return result;
			} catch (Exception e) {
				retryPolicy.doWhenFail(e);
				if (retryPolicy.needRetry(e)) {
					try {
						long delay = retryPolicy.getDelay(delayBeforeNextTry);
						logger.warn("taobao API invoke fail, {} ms setup retry.", delayBeforeNextTry, e);
						Thread.sleep(delayBeforeNextTry);
						delayBeforeNextTry = delay;
					} catch (Exception e2) {
						logger.error("taobao API throws Exception: {}", e2.getMessage());
						e2.printStackTrace();
					}
				} else {
					finallyException = e;
					break;
				}
			}
		}
		
		logger.warn("taobao API invoke retry some times fail, don't again retry", finallyException);
		retryPolicy.doWhenFinallyFail(finallyException);
		return null;

	}
}
