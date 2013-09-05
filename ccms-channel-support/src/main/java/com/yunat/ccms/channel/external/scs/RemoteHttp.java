package com.yunat.ccms.channel.external.scs;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunat.base.enums.app.AppEnum;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.channel.external.RetryPolicy;
import com.yunat.channel.client.ChannelClient;
import com.yunat.channel.enums.error.InvokeErrorEnum;
import com.yunat.channel.request.service.TaskGetRequest;

public final class RemoteHttp {
	private static Logger logger = LoggerFactory.getLogger(RemoteHttp.class);
	
	private Long delayBeforeNextTry;
	public RemoteHttp(Long initialRetryInterval) {
		delayBeforeNextTry = initialRetryInterval;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T call(RemoteCallable<T> callable, RetryPolicy retryPolicy, final PlatEnum plat) {
		Exception finallyException;
		while(true) {
			try {
				T result = callable.call();
				BaseResponse info = null;
				try {
					info = (BaseResponse)result;
				} catch (ClassCastException cex) {
					logger.error("channel interface throws ClassCaseException");
					cex.printStackTrace();
				}
				
				if (!info.isSuccess()) {
					if (InvokeErrorEnum.READ_TIME_OUT.getErrCode().equals(info.getErrCode())) {
						String taskId = callable.getTaskId();
						if (null != taskId) {
							TaskGetRequest taskGetRequest = new TaskGetRequest();
							taskGetRequest.setTaskId(taskId);
							
							BaseResponse<Boolean> baseResponse = ChannelClient.getInstance(AppEnum.CCMS, 
									plat).excute(taskGetRequest);
							if (baseResponse.isSuccess()) {
								info.setSuccess(true);
								return (T)info;
							} else {
								throw new TimeoutException(info.getErrMsg());
							}
						} else {
							throw new TimeoutException(info.getErrMsg());
						}
					} else if (InvokeErrorEnum.REMOTE_HOST_ERROR.getErrCode().equals(info.getErrCode())) {
						throw new RuntimeException(info.getErrMsg());
					}
				} 
				return result;
			} catch (Exception e) {
				retryPolicy.doWhenFail(e);
				if (retryPolicy.needRetry(e)) {
					try {
						long delay = retryPolicy.getDelay(delayBeforeNextTry);
						logger.warn("remote interface invoke fail, {} ms setup retry.", delayBeforeNextTry, e);
						Thread.sleep(delayBeforeNextTry);
						delayBeforeNextTry = delay;
					} catch (Exception e2) {
						logger.error("channel interface throws Exception: {}", e2.getMessage());
						e2.printStackTrace();
					}
				} else {
					finallyException = e;
					break;
				}
			}
		}
		
		logger.warn("remote interface invoke retry some times fail, don't again retry", finallyException);
		retryPolicy.doWhenFinallyFail(finallyException);
		return null;
	}
	
}